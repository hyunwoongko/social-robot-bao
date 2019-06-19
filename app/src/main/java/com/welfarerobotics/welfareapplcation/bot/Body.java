package com.welfarerobotics.welfareapplcation.bot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.util.bluetooth.Bluetooth;

public class Body extends BaseActivity {
    Button btn[] = new Button[9];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_baofallow);
        btn[0]=findViewById(R.id.lef_b);
        btn[1]=findViewById(R.id.lef_h);
        btn[2]=findViewById(R.id.lef_m);
        btn[3]=findViewById(R.id.nec_l);
        btn[4]=findViewById(R.id.nec_m);
        btn[5]=findViewById(R.id.nec_r);
        btn[6]=findViewById(R.id.rit_b);
        btn[7]=findViewById(R.id.rit_h);
        btn[8]=findViewById(R.id.rit_m);
        for(int i =0; i<btn.length;i++){
            btn[i].setClickable(true);

        }
        btn[0].setOnClickListener(view -> {
            Bluetooth.getInstance().sendMessage("Movelef,1");

        });
        btn[1].setOnClickListener(view -> {
            Bluetooth.getInstance().sendMessage("Movelef,0");

        });
        btn[2].setOnClickListener(view -> {
            Bluetooth.getInstance().sendMessage("Movelef,2");

        });
        btn[3].setOnClickListener(view -> {
            Bluetooth.getInstance().sendMessage("Movenec,0");

        });
        btn[4].setOnClickListener(view -> {
            Bluetooth.getInstance().sendMessage("Movenec,1");

        });
        btn[5].setOnClickListener(view -> {

            Bluetooth.getInstance().sendMessage("Movenec,2");
        });
        btn[6].setOnClickListener(view -> {
            Bluetooth.getInstance().sendMessage("Moverit,1");

        });
        btn[7].setOnClickListener(view -> {

            Bluetooth.getInstance().sendMessage("Moverit,0");
        });
        btn[8].setOnClickListener(view -> {
            Bluetooth.getInstance().sendMessage("Moverit,2");

        });

    }
}
