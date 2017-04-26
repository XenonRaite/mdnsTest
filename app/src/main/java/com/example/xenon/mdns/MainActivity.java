package com.example.xenon.mdns;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.xenon.mdns.LogController.log;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    Button btnRegister;

    Button btnSearch2;
    Button btnRegister2;

    TextView logTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = (Button) findViewById(R.id.buttonRegister);
        btnSearch = (Button) findViewById(R.id.buttonSearch);

        btnRegister2 = (Button) findViewById(R.id.button);
        btnSearch2 = (Button) findViewById(R.id.button2);

        logTxt = (TextView) findViewById(R.id.editTextLog);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NSDController.INSTANCE.registerService(7778);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NSDController.INSTANCE.discoverServices();
            }
        });

        btnRegister2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDNSController.INSTANCE.regisretService();
            }
        });

        btnSearch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDNSController.INSTANCE.discover();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        logTxt.setText(LogController.INSTANCE.getLog());

        LogController.INSTANCE.setLogUpdate(new LogController.LogUpdate() {
            @Override
            public void update(final String log) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logTxt.setText(log);
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogController.INSTANCE.setLogUpdate(null);
    }
}
