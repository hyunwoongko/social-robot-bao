package com.welfarerobotics.welfareapplcation.util.bluetooth;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.welfarerobotics.welfareapplcation.bot.face.Coordinate;
import com.welfarerobotics.welfareapplcation.bot.face.Eye;
import org.json.JSONException;
import org.json.JSONObject;


public class BluetoothHandler extends Handler {
    private ArrayAdapter<String> mConversationArrayAdapter;
    private BluetoothData data;
    private String mConnectedDeviceName = null;
    static String currentStatus = "not connected";
    private BluetoothChatService mChatService = null;
    private StringBuffer mOutStringBuffer;
    private Activity context;

    public BluetoothHandler(BluetoothData data, ArrayAdapter<String> mConversationArrayAdapter, BluetoothChatService mChatService, Activity context) {
        this.data = data;
        this.mConversationArrayAdapter = mConversationArrayAdapter;
        this.mChatService = mChatService;
        mOutStringBuffer = new StringBuffer("");
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                    case BluetoothChatService.STATE_CONNECTED:
                        mConversationArrayAdapter.clear();
                        break;
                    case BluetoothChatService.STATE_CONNECTING:
                        break;
                    case BluetoothChatService.STATE_LISTEN:
                    case BluetoothChatService.STATE_NONE:
                        break;
                }
                break;
            case Constants.MESSAGE_WRITE:
                data.isRead = false;
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                //Log.d(TAG, "writeMessage = " + writeMessage);
                mConversationArrayAdapter.add("Command:  " + writeMessage);
                //Toast.makeText(context,writeMessage,Toast.LENGTH_SHORT);

                break;
            case Constants.MESSAGE_READ:
                data.isRead = true;
//                    byte[] readBuf = (byte[]) msg.obj;
//                     construct a string from the valid bytes in the buffer
//                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    String readMessage = new String(readBuf);
                String readMessage = (String) msg.obj;
                Log.d("블루투스 통신", "readMessage = " + readMessage);
                if (readMessage.contains("Emotion")) {
                    String replace = readMessage.replace("Emotion:", "");
                    Log.d("감정왔음:", "readMessage = " + replace);
                    Eye.getEye(context).seeEmotion(replace);
                } else if (readMessage.contains("Cordinate")) {
                    String replace = readMessage.replace("Cordinate:", "");
                    String cordinates[] = replace.split(",");
                    Coordinate coordinate = new Coordinate();

                    cordinates[0] = cordinates[0].replace("height:", "");
                    cordinates[1] = cordinates[1].replace("width:", "");
                    cordinates[2] = cordinates[2].replace("left:", "");
                    cordinates[3] = cordinates[3].replace("top:", "");
                    cordinates[3] = cordinates[3].replace("l", "");
                    //Log.d("각도왔음:", "readMessage = " + cordinates[0]+cordinates[1]+cordinates[2]+cordinates[3]);
                    try {
                        coordinate.setHeight(Integer.parseInt(cordinates[0]));
                        coordinate.setWidth(Integer.parseInt(cordinates[1]));
                        coordinate.setLeft(Integer.parseInt(cordinates[2]));
                        coordinate.setTop(Integer.parseInt(cordinates[3]));
                        Eye.getEye(context).see(coordinate.getLeft(), 0.25f);

                    } catch (Exception e) {


                    }

                    Log.d("각도왔음:", "readMessage = " + readMessage);

                }
                //TODO: if message is json -> callback from RPi
                if (isJson(readMessage)) {
                    handleCallback(readMessage);
                } else {
                    if (data.isCountdown) {
                        //this.removeCallbacks(watchDogTimeOut);
                        data.isCountdown = false;
                    }

                    //remove the space at the very end of the readMessage -> eliminate space between items
                    readMessage = readMessage.substring(0, readMessage.length() - 1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    mConversationArrayAdapter.add(readMessage);
                    Log.d("블루투스", "받은 메세지 = " + readMessage);
                    //라즈베리파이에서 받는 메세지 부분 readMessage
                    // Eye.see(null,null);

                }

                break;
            case Constants.MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                if (null != this) {
//                        Toast.makeText(this, "Connected to "
//                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.MESSAGE_TOAST:
//                    if (null != activity) {
//                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
//                                Toast.LENGTH_SHORT).show();
//                    }
                break;
        }


    }

    public void setmChatService(BluetoothChatService mChatService) {

        this.mChatService = mChatService;
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            // Toast.makeText(this,"BT not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer); 수신받은내용을 출력해주는부분
            //Toast.makeText(this, mOutStringBuffer, Toast.LENGTH_SHORT).show();
            // emtion_status = mOutStringBuffer.toString();
        }
    }


    public void sendMessage(String SSID, String PWD) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            // Toast.makeText(this, "BT not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (SSID.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            JSONObject mJson = new JSONObject();
            try {
                mJson.put("SSID", SSID);
                mJson.put("PWD", PWD);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            byte[] send = mJson.toString().getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer); 수신받은내용을 출력해주는부분
            //  Toast.makeText(this, mOutStringBuffer, Toast.LENGTH_SHORT).show();
        }
    }


    public void handleCallback(String str) {
        String result;
        String ip;
        if (data.isCountdown) {
            //this.removeCallbacks(watchDogTimeOut);
            data.isCountdown = false;
        }

//        //enable user interaction
//        mProgressDialog.dismiss();
        try {
            JSONObject mJSON = new JSONObject(str);
            result = mJSON.getString("result") == null ? "" : mJSON.getString("result");
            ip = mJSON.getString("IP") == null ? "" : mJSON.getString("IP");
            //Toast.makeText(getActivity(), "result: "+result+", IP: "+ip, Toast.LENGTH_LONG).show();

            if (!result.equals("SUCCESS")) {
                //   Toast.makeText(this, "FAIL", Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity(),getString(R.string.config_success) + ip,Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // error handling
            //  Toast.makeText(this, "SOMETHING WENT WRONG", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isJson(String str) {
        try {
            new JSONObject(str);
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

}
