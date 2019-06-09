package com.welfarerobotics.welfareapplcation.util.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.welfarerobotics.welfareapplcation.R;


public class Bluetooth {


    // Intent request codes

    private ArrayAdapter<String> mConversationArrayAdapter;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    private BluetoothData data = null;
    public static Bluetooth bluetooth;
    private BluetoothHandler handler;


    /*
    싱글톤의 변형임. 처음 메인에다가 박아두고 다른 액티비티에서 사용할 때는 Context로 안받고 사용하면 됨.
    */
    public static Bluetooth getInstance(Context context){
        if(bluetooth==null){
            bluetooth = new Bluetooth(context);

        }
        return  bluetooth;
    }

    public static Bluetooth getInstance(){


        return bluetooth;


    }


    public Bluetooth(Context context) {

        data = new BluetoothData(false, false);

        mConversationArrayAdapter = new ArrayAdapter<String>(context, R.layout.message) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tView = (TextView) view.findViewById(R.id.listItem);
                if (data.isRead) {
                    tView.setTextColor(Color.BLUE);
                } else {
                    tView.setTextColor(Color.RED);
                }
                return view;
            }
        };


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        // Get the device MAC address
        String address = "B8:27:EB:16:AE:38";/*승민이형 바꿔주세요*/
        // Get the BluetoothDevice object
        handler = new BluetoothHandler(data, mConversationArrayAdapter,mChatService);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device'
        mChatService = new BluetoothChatService(context, handler);
        mChatService.connect(device, true);



    }

    public void sendMessage(String msg){
        handler.sendMessage(msg);

    }
   public void sendMessage(String SSID, String PWD){

        handler.sendMessage(SSID, PWD);
   }

}








