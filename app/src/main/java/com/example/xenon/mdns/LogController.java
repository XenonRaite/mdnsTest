package com.example.xenon.mdns;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum  LogController {
    INSTANCE;

    public interface LogUpdate{
        void update(String log);
    }

    private StringBuilder stringBuilder;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    private LogUpdate logUpdate;

    public void init(){
        stringBuilder = new StringBuilder();
    }

    public void append(String msg){
        stringBuilder.append(simpleDateFormat.format(new Date())).append(msg).append("\n");
        if(logUpdate!=null){
            logUpdate.update(stringBuilder.toString());
        }
    }

    public static void log(String msg){
        LogController.INSTANCE.append(msg);
    }

    public void setLogUpdate(LogUpdate logUpdate) {
        this.logUpdate = logUpdate;
    }

    public String getLog(){
        return stringBuilder.toString();
    }
}
