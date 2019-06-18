package com.welfarerobotics.welfareapplcation.core.settings;

import android.Manifest;
import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.kinda.alert.KAlertDialog;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.util.Sound;
import es.dmoral.toasty.Toasty;

import java.util.*;

import static android.content.Context.MODE_PRIVATE;

public class WifiFragment extends Fragment {
    public class device {
        CharSequence name;


        public String getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(String capabilities) {
            this.capabilities = capabilities;
        }

        String capabilities;

        public void setName(CharSequence name) {
            this.name = name;
        }

        public CharSequence getName() {
            return name;
        }
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 125;
    List<ScanResult> wifiList;
    private WifiManager wifi;
    List<device> values = new ArrayList<device>();
    int netCount = 0;
    RecyclerView recyclerView;
    WifiScanAdapter wifiScanAdapter;
    private static String TAG = "WifiFragment";
    private String password = null;
    //Option Menu for wifi connection

    public WifiFragment() {
        // Required empty public constructor
    }

    public static WifiFragment newInstance() {
        WifiFragment fragment = new WifiFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Make instance of Wifi
        Button btnScan = (Button) getActivity().findViewById(R.id.wifiScan);
        wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //Check wifi enabled or not
        if (!wifi.isWifiEnabled()) {
            Toasty.info(getActivity(), "와이파이를 켜는중입니다.").show();
            wifi.setWifiEnabled(true);
        }
        //register Broadcast receiver
        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                wifiList = wifi.getScanResults();
                netCount = wifiList.size();
                Log.d("Wifi", "Total Wifi Network" + netCount);
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiScanAdapter = new WifiScanAdapter(values, getContext());
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.wifiRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(wifiScanAdapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkandAskPermission();
        } else {
            wifi.startScan();
            values.clear();
            try {
                netCount = netCount - 1;
                while (netCount >= 0) {
                    device d = new device();
                    d.setName(wifiList.get(netCount).SSID.toString());
                    d.setCapabilities(wifiList.get(netCount).capabilities);
                    Log.d("WiFi", d.getName().toString());
                    values.add(d);
                    wifiScanAdapter.notifyDataSetChanged();
                    netCount = netCount - 1;
                }
                Collections.reverse(values);
                wifiScanAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                Log.d("Wifi", e.getMessage());
            }
        }
        btnScan.setOnClickListener(view -> {
            Sound.get().effectSound(getActivity(), R.raw.click);
            scan();
        });
        wifiScanAdapter.setOnClickListener(v -> {
            final device d = (device) v.findViewById(R.id.ssid_name).getTag();
            Log.d(TAG, "Selected Network is " + d.getName());

            LayoutInflater li = LayoutInflater.from(getContext());
            View promptsView = li.inflate(R.layout.menuwifi, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    getContext());
            alertDialogBuilder.setView(promptsView);
            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextPassword);
            TextView ssidText = (TextView) promptsView.findViewById(R.id.textViewSSID);
            ssidText.setText("선택된 와이파이 : " + d.getName());
            TextView security = (TextView) promptsView.findViewById(R.id.textViewSecurity);
            security.setText("와이파이에 설정된 보안방식 :\n" + d.getCapabilities());
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("확인",
                            (dialog, id) -> {
                                // get user input and set it to result
                                // edit text
                                Log.d(TAG, "Password is:" + userInput.getText());
                                password = userInput.getText().toString();
                                //  result.setText(userInput.getText());
                                savewifi(getActivity());
                                connectWiFi(String.valueOf(d.getName()), password, d.capabilities);

                            })
                    .setNegativeButton("취소",
                            (dialog, id) -> dialog.cancel());

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        });
    }
    private void savewifi(Activity activity){
        SharedPreferences pref = activity.getSharedPreferences("Wifi", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Wifi",password);
        editor.commit();

//        SharedPreferences pref1 = activity.getSharedPreferences("Wifi", MODE_PRIVATE);
//        System.out.println("페어링주소"+pref1.getString("Bluetooth", "12345678"));
//        Log.w("TAG: ", "와이파이연결되면 쉐어드프리퍼런스에 비밀번호저장");
    }
    private void scan() {
        wifi.startScan();
        KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("검색중...");
        pDialog.setCancelable(false);
        pDialog.show();
        values.clear();
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            System.out.println("++++++++++");
            try {
                netCount = netCount - 1;
                while (netCount >= 0) {
                    device d = new device();
                    d.setName(wifiList.get(netCount).SSID.toString());
                    d.setCapabilities(wifiList.get(netCount).capabilities);
                    values.add(d);
                    wifiScanAdapter.notifyDataSetChanged();
                    netCount = netCount - 1;
                }
                Collections.reverse(values);
                wifiScanAdapter.notifyDataSetChanged();
                pDialog.dismissWithAnimation();
            } catch (Exception e) {
                Log.d("Wifi", e.getMessage());
                pDialog.dismissWithAnimation();
            }
        },5000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_wifi, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    wifi.startScan();
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "권한이 허용되지 않았습니다", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    private void checkandAskPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Network");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 0; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        (dialog, which) -> requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS));
                return;
            }

            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        // initVideo();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    public void connectWiFi(String SSID, String password, String Security) {
        try {

            Log.d(TAG, "Item clicked, SSID " + SSID + " Security : " + Security);

            String networkSSID = SSID;
            String networkPass = password;

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;

            if (Security.toUpperCase().contains("WEP")) {
                Log.v("rht", "Configuring WEP");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                if (networkPass.matches("^[0-9a-fA-F]+$")) {
                    conf.wepKeys[0] = networkPass;
                } else {
                    conf.wepKeys[0] = "\"".concat(networkPass).concat("\"");
                }

                conf.wepTxKeyIndex = 0;

            } else if (Security.toUpperCase().contains("WPA")) {
                Log.v(TAG, "Configuring WPA");

                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                conf.preSharedKey = "\"" + networkPass + "\"";

            } else {
                Log.v(TAG, "Configuring OPEN network");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.clear();
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);

            Log.v(TAG, "Add result " + networkId);

            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    Log.v(TAG, "WifiConfiguration SSID " + i.SSID);

                    boolean isDisconnected = wifiManager.disconnect();
                    Log.v(TAG, "isDisconnected : " + isDisconnected);

                    boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);
                    Log.v(TAG, "isEnabled : " + isEnabled);

                    boolean isReconnected = wifiManager.reconnect();
                    Log.v(TAG, "isReconnected : " + isReconnected);

                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}