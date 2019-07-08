package com.welfarerobotics.welfareapplcation.util.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.bot.Body.Signal;

import static android.content.Context.MODE_PRIVATE;


public class Bluetooth{


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
    public static Bluetooth getInstance(Activity activity){
        if(bluetooth==null){
            bluetooth = new Bluetooth(activity);

        }
        return  bluetooth;
    }

    public static Bluetooth getInstance(){
        return bluetooth;
    }


    public Bluetooth(Activity activity) {
        data = new BluetoothData(false, false);
        mConversationArrayAdapter = new ArrayAdapter<String>(activity, R.layout.message) {
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
        String address;
        SharedPreferences pref = activity.getSharedPreferences("Bluetooth", MODE_PRIVATE);
        address = pref.getString("Bluetooth", "");
        Log.d("Bluetooth","Adress:"+address);
        // Get the BluetoothDevice object
        handler = new BluetoothHandler(data, mConversationArrayAdapter,mChatService, activity);
        try{
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            mChatService = new BluetoothChatService(activity, handler);
            mChatService.connect(device, true);
        }catch (Exception e){
            Log.d("블루투스","주소 에러 adress:"+address);
        }

        // Attempt to connect to the device'


//        //Get the wifipswd
//        String wifipswd;
//        SharedPreferences pref1 = activity.getSharedPreferences("Wifi", MODE_PRIVATE);
//        wifipswd = pref1.getString("Wifi", "12345678");
//
//        // Get the BluetoothDevice object
//        handler = new BluetoothHandler(data, mConversationArrayAdapter,mChatService, activity);
//        BluetoothDevice device1 = mBluetoothAdapter.getRemoteDevice(wifipswd);
//        // Attempt to connect to the device'
//        mChatService = new BluetoothChatService(activity, handler);
//        mChatService.connect(device1, true);

    }

  synchronized public void sendMessage(String msg){
        handler.sendMessage(msg);

    }

  synchronized public void sendSignal(Signal signal, float angle){

        handler.sendMessage(signal.toString()+","+angle);

    }
//돌리는 것
  synchronized public void sendWifi(String SSID, String PWD){

        handler.sendMessage(SSID, PWD);
   }/*와이파이 번호 보내세요.*/
    synchronized public void sendPort(String port){

        handler.sendMessage("Port,"+port);
    }/*스트리밍 포트를 보내주세요*/

}








