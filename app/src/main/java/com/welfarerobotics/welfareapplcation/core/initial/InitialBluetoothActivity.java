package com.welfarerobotics.welfareapplcation.core.initial;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.util.Sound;
import com.welfarerobotics.welfareapplcation.util.ToastType;
import com.welfarerobotics.welfareapplcation.util.data_util.Preference;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// TODO 블루투스 액티비티 리팩토링 필요
public class InitialBluetoothActivity extends BaseActivity {
    private Switch sw;

    //블루투스 지원 유무 확인
    //BluetoothAdapter
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    //블루투스 권한요청 액티비티 코드
    final static int BLUETOOTH_REQUEST_CODE = 100;
    //위치 권한요청 액티비티 코드
    final static int ACCESS_COARSE_LOCATION_CODE = 101;
    final static int ACCESS_FINE_LOCATION_CODE = 102;
    //블루투스 연결
    private BluetoothSocket mConnectSocket;
    private boolean mIsConnected = false;

    private ListView listDeviceView;
    private ListView pairing_listDeviceView;
    private TextView txtState;
    private TextView pairing_txtState;
    //Adapter
    SimpleAdapter adapterPaired;
    SimpleAdapter adapterDevice;
    //list - Device 목록 저장
    private List<Map<String, String>> dataDevice_list;
    private List<Map<String, String>> pairing_dataDevice_list;
    private List<BluetoothDevice> bluetoothDevices;
    int selectDevice;
    //imageView
    ImageView next_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_bluetooth);
        //UI
        listDeviceView = (ListView) findViewById(R.id.searched_list);
        sw = (Switch) findViewById(R.id.bl_switch);
        txtState = (TextView) findViewById(R.id.bl_state);
        pairing_listDeviceView = (ListView) findViewById(R.id.paired_list);
        next_imageView = (ImageView) findViewById(R.id.next_imageView);
        //Adapter1
        dataDevice_list = new ArrayList<>();
        adapterDevice = new SimpleAdapter(this, dataDevice_list, android.R.layout.simple_list_item_2, new String[]{"name", "address"}, new int[]{android.R.id.text1, android.R.id.text2});
        listDeviceView.setAdapter(adapterDevice);
        //Adapter2
        pairing_dataDevice_list = new ArrayList<>();
        adapterPaired = new SimpleAdapter(this, pairing_dataDevice_list, android.R.layout.simple_list_item_2, new String[]{"name", "address"}, new int[]{android.R.id.text1, android.R.id.text2});
        pairing_listDeviceView.setAdapter(adapterPaired);
        //검색된 블루투스 디바이스 데이터
        bluetoothDevices = new ArrayList<>();
        //선택한 디바이스 없음
        selectDevice = -1;
        //블루투스 지원 유무 확인
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        next_imageView.setOnClickListener(view -> {
            Intent intent = new Intent(this, InitialWifiActivity.class);
            Sound.get().effectSound(getApplicationContext(), R.raw.click);
            startActivity(intent);
            finish();
        });
        //블루투스를 지원하지 않으면 null을 리턴한다
        if (mBluetoothAdapter == null) {
            showToast("블루투스를 지원하지 않는 단말기 입니다.", ToastType.error);
            startActivity(new Intent(this, InitialWifiActivity.class));
            return;
        }
        //블루투스 브로드캐스트 리시버 등록
        //리시버1
        IntentFilter stateFilter = new IntentFilter();
        stateFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); //BluetoothAdapter.ACTION_STATE_CHANGED : 블루투스 상태변화 액션
        registerReceiver(mBluetoothStateReceiver, stateFilter);
        //리시버2
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //BluetoothAdapter.ACTION_DISCOVERY_STARTED : 블루투스 검색 시작
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND); //BluetoothDevice.ACTION_FOUND : 블루투스 디바이스 찾음
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //BluetoothAdapter.ACTION_DISCOVERY_FINISHED : 블루투스 검색 종료
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBluetoothSearchReceiver, searchFilter);
        //리시버3
        IntentFilter scanmodeFilter = new IntentFilter();
        scanmodeFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBluetoothScanmodeReceiver, scanmodeFilter);

        // 해당 permission체크후 권한이없다면 souldShow 퍼미션 창띄우기
        if (ContextCompat.checkSelfPermission(InitialBluetoothActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(InitialBluetoothActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(InitialBluetoothActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_CODE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        // 해당 permission체크후 권한이없다면 souldShow 퍼미션 창띄우기
        if (ContextCompat.checkSelfPermission(InitialBluetoothActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(InitialBluetoothActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(InitialBluetoothActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        //searched list에 onItemClickListener를 추가
        listDeviceView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = bluetoothDevices.get(position);
                try {
                    //선택한 디바이스 페어링 요청
                    Method method = device.getClass().getMethod("createBond", (Class[]) null);
                    method.invoke(device, (Object[]) null);
                    selectDevice = position;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //1. 블루투스가 꺼져있으면 활성화
//        if(!mBluetoothAdapter.isEnabled()){
//            mBluetoothAdapter.enable(); //강제 활성화
//        }
        //2. 블루투스가 꺼져있으면 사용자에게 활성화 요청하기
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        }
        //스위치의 체크 이벤트를 위한 리스너 등록
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Sound.get().effectSound(getApplicationContext(), R.raw.click);
            // TODO Auto-generated method stub
            if (isChecked == true) {
//                    makeText(BlActivity.this, "스위치-ON", LENGTH_SHORT).show();
                sw.setText("     사용");
//                    mBluetoothAdapter.startDiscovery(); //: 블루투스 검색 시작
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
                    txtState.setText("블루투스 활성화");
                }
                visibleMyBluetooth();
                mBluetoothAdapter.startDiscovery();
            } else {
//                    makeText(BlActivity.this, "스위치-OFF", LENGTH_SHORT).show();
                sw.setText("     사용안함");
//                    mBluetoothAdapter.isDiscovering(); //블루투스 검색중인지 여부 확인
                int count = adapterDevice.getCount();
                for (int i = 0; i < count; i++) {
                    dataDevice_list.remove(i);
                    listDeviceView.setAdapter(adapterDevice);
                }
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery(); // 블루투스 검색 취소
                }
//                    mBluetoothAdapter.disable(); // 블루투스 사용종료
            }
        });
    }

    public void visibleMyBluetooth() { //내 블루투스를 다른 핸드폰이 보도록 하기
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
    }

    //블루투스 상태변화 BroadcastReceiver
    private BroadcastReceiver mBluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //BluetoothAdapter.EXTRA_STATE : 블루투스의 현재상태 변화
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            //블루투스 활성화
            if (state == BluetoothAdapter.STATE_ON) {
                txtState.setText("블루투스 활성화");
            }
            //블루투스 활성화 중
            else if (state == BluetoothAdapter.STATE_TURNING_ON) {
                txtState.setText("블루투스 활성화 중...");
            }
            //블루투스 비활성화
            else if (state == BluetoothAdapter.STATE_OFF) {
                txtState.setText("블루투스 비활성화");
            }
            //블루투스 비활성화 중
            else if (state == BluetoothAdapter.STATE_TURNING_OFF) {
                txtState.setText("블루투스 비활성화 중...");
            }
        }
    };
    //블루투스 검색결과 BroadcastReceiver
    BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                //블루투스 디바이스 검색시작
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    dataDevice_list.clear();
                    bluetoothDevices.clear();
                    txtState.setText("블루투스 활성화 중... ");
                    break;
                //블루투스 디바이스 찾음
                case BluetoothDevice.ACTION_FOUND:
                    //검색한 블루투스 디바이스의 객체를 구한다
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    txtState.setText("블루투스 장치검색중... ");
                    //데이터 저장
                    Map map = new HashMap();
                    map.put("name", device.getName()); //device.getName() : 블루투스 디바이스의 이름
                    map.put("address", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소

                    dataDevice_list.add(map);
                    //리스트 목록갱신
                    adapterDevice.notifyDataSetChanged();
                    //블루투스 디바이스 저장
                    bluetoothDevices.add(device);
                    break;
                //블루투스 디바이스 검색 종료
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(InitialBluetoothActivity.this, "블루투스 검색 종료", Toast.LENGTH_SHORT).show();
                    mBluetoothAdapter.cancelDiscovery(); // 블루투스 검색 취소
                    txtState.setText("");
//                    btnSearch.setEnabled(true);
                    break;
                //블루투스 디바이스 페어링 상태 변화
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
//                    mBluetoothAdapter.cancelDiscovery(); // 블루투스 검색 취소
                    BluetoothDevice paired = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (paired.getBondState() == BluetoothDevice.BOND_BONDED) {
                        mBluetoothAdapter.cancelDiscovery(); // 블루투스 검색 취소
                        SharedPreferences pref = getSharedPreferences("Bluetooth", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Bluetooth", paired.getAddress());
                        editor.commit();

                        SharedPreferences pref1 = getSharedPreferences("Bluetooth", MODE_PRIVATE);
                        System.out.println("페어링주소"+pref1.getString("Bluetooth", "B8:27:EB:16:AE:38"));
                        Log.w("TAG: ", "로그 페어링되면 쉐어드프리퍼런스에 주소저장");
                        //데이터 저장
                        Map map2 = new HashMap();
                        map2.put("name", paired.getName()); //device.getName() : 블루투스 디바이스의 이름
                        map2.put("address", paired.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                        pairing_dataDevice_list.add(map2);
                        //리스트 목록갱신
                        adapterPaired.notifyDataSetChanged();
                        //검색된 목록
                        if (selectDevice != -1) {
                            bluetoothDevices.remove(selectDevice);
                            dataDevice_list.remove(selectDevice);
                            adapterDevice.notifyDataSetChanged();
                            selectDevice = -1;
                        }
                    }
                    break;
            }
        }
    };

    //블루투스 검색응답 모드 BroadcastReceiver
    BroadcastReceiver mBluetoothScanmodeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, -1);
            switch (state) {
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                case BluetoothAdapter.SCAN_MODE_NONE:
//                    chkFindme.setChecked(false);
//                    chkFindme.setEnabled(true);
                    Toast.makeText(InitialBluetoothActivity.this, "검색응답 모드 종료", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                    Toast.makeText(InitialBluetoothActivity.this, "다른 블루투스 기기에서 내 휴대폰을 찾을 수 있습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BLUETOOTH_REQUEST_CODE:
                //블루투스 활성화 승인
                if (resultCode == Activity.RESULT_OK) {
                }
                //블루투스 활성화 거절
                else {
                    showToast("블루투스를 활성화 해야합니다.", ToastType.warning);
                    finish();
                    return;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBluetoothSearchReceiver);
    }
}
