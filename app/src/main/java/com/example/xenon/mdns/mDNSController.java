package com.example.xenon.mdns;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.impl.JmmDNSImpl;
import javax.jmdns.impl.tasks.Responder;

import static com.example.xenon.mdns.LogController.log;
import static com.example.xenon.mdns.Utils.getIPAddress;

/**
 * Created by xenon on 13.03.2017.
 */
public enum mDNSController {
    INSTANCE;

    private Context context;
    private JmDNS jmdns;
    private SampleListener sampleListener;


    android.net.wifi.WifiManager.MulticastLock lock;

    public void init(Context context){
        this.context = context;

        android.net.wifi.WifiManager wifi =
                (android.net.wifi.WifiManager)
                        context.getSystemService(android.content.Context.WIFI_SERVICE);
        lock = wifi.createMulticastLock("HeeereDnssdLock");
        lock.setReferenceCounted(true);
        lock.acquire();
        try {
            // Create a JmDNS instance

            jmdns = JmDNS.create(InetAddress.getByName(Utils.getIPAddress(false)),"XenonRaite");
        } catch (IOException e) {
            e.printStackTrace();
            log("IOException " + e.getLocalizedMessage());
        }
    }

    InetAddress getIP() {
        try {
            Socket socket = new Socket("google.com", 80);
            return socket.getLocalAddress();
        } catch (Exception e) {

        }
        return null;
    }

    public void regisretService(){
        try {
            // Register a service
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "mDNS", 7779, "text");
            jmdns.registerService(serviceInfo);
            log("registerService " + serviceInfo);
        } catch (IOException e) {
            log("IOException " + e.getLocalizedMessage());
        }
    }

    public void unregister(){
        // Unregister all services
        jmdns.unregisterAllServices();
        if (lock != null) lock.release();
        log("unregisterAllServices ");
    }

    public void discover(){

        // Add a service listener

        jmdns.addServiceListener("_http._tcp.local.", sampleListener = new SampleListener());
        log("addServiceListener");

    }

    private class SampleListener implements ServiceListener {

        @Override
        public void serviceAdded(ServiceEvent event) {
            log("serviceAdded " + event.toString());
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            log("serviceRemoved " + event.getInfo());

        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            log("serviceResolved " + event.getInfo());
            jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
        }
    }

    /**
     * Retrieves the net.hostname system property
     * @param defValue the value to be returned if the hostname could
     * not be resolved
     */
    public static String getHostName(String defValue) {
        try {
            Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return getString.invoke(null, "net.hostname").toString();
        } catch (Exception ex) {
            return defValue;
        }
    }
}
