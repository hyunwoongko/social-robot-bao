package com.welfarerobotics.welfareapplcation.core.initial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.BaseActivity;
import com.welfarerobotics.welfareapplcation.core.main.MainActivity;
import com.welfarerobotics.welfareapplcation.entity.Conversation;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.NetworkUtil;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.data_util.Preference;
import es.dmoral.toasty.Toasty;

import java.util.ArrayList;

public class InitialSettingActivity extends BaseActivity {
    private final String[] Main_Category = {"지역선택", "서울", "인천", "대전", "대구", "광주", "부산", "울산", "세종",
            "경기도", "강원도", "충청북도", "충청남도", "경상북도", "경상남도", "전라북도", "전라남도", "제주도"};
    private final String[] Seoul = {"시군구 선택", "강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", "금천구", "노원구", "도봉구",
            "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", "성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
    private final String[] Incheon = {"시군구 선택", "강화군", "계양구", "남구", "남동구", "동구", "부평구", "서구", "연수구", "웅진구", "중구"};
    private final String[] Daejeon = {"시군구 선택", "대덕구", "동구", "서구", "유성구", "중구"};
    private final String[] Daegu = {"시군구 선택", "남구", "달성구", "달성군", "동구", "북구", "서구", "수성구", "중구"};
    private final String[] Gwangju = {"시군구 선택", "광산구", "남구", "동구", "북구", "서구"};
    private final String[] Busan = {"시군구 선택", "강서구", "금정구", "기장군", "남구", "동구", "동래구", "부산진구", "북구", "사상구", "사하구", "서구", "수영구",
            "영제구", "영도구", "중구", "해운대구"};
    private final String[] Ulsan = {"시군구 선택", "중구", "남구", "동구", "북구", "울주군"};
    private final String[] Sejong = {"시군구 선택", "세종 특별 자치시"};
    private final String[] Gyeonggido = {"시군구 선택", "가평군", "고양시", "과천시", "광명시", "광주시", "구리시", "군포시", "김포시", "남양주시", "동두천시", "부천시", "성남시",
            "수원시", "시흥시", "안산시", "안성시", "안양시", "양주시", "양평군", "여주시", "연천군", "오산시", "용인시", "의왕시", "의정부시", "인천시", "파주시", "평택시", "포천시", "하남시", "화천시"};
    private final String[] Gangwondo = {"시군구 선택", "강릉시", "고성군", "동해시", "삼척시", "속초시", "양양군", "월영군", "원주시", "인제군", "정선군", "철원군", "춘천시", "태백시", "평창군",
            "홍천군", "화천군", "횡성군"};
    private final String[] Chungcheongbukdo = {"시군구 선택", "괴산군", "단양군", "보은군", "영동군", "옥천군", "음천군", "제천시", "청원군", "청주시", "충주시", "증평군"};
    private final String[] Chungcheongnamdo = {"시군구 선택", "공주시", "금산군", "논산시", "당진시", "보령시", "부여군", "서산시", "서천군", "아산시", "예산군", "천안시", "청양군", "태안군", "홍성군", "계룡시"};
    private final String[] Gyeongsangbukdo = {"시군구 선택", "경산시", "경주시", "고령군", "구미시", "군위군", "김천시", "문경시", "봉화군", "상주시", "성주시", "안동시", "영덕군",
            "영양군", "영주시", "예천군", "울릉군", "울진군", "의성군", "창도군", "청성군", "칠곡군", "포항시"};
    private final String[] Gyeongsangnamdo = {"시군구 선택", "거제시", "거창군", "고성군", "김해시", "남해군", "마산시", "밀양시", "사천시", "산청군", "양산시", "의령군", "진주시", "진해시",
            "창녕군", "창원시", "통영시", "하동군", "함안군", "함양군", "함천군"};
    private final String[] Jeollabukdo = {"시군구 선택", "고창군", "군산시", "김제시", "남원시", "무주군", "부안군", "순창군", "완주군", "익산시", "임실군", "장수군", "전주시", "정읍시", "진안군"};
    private final String[] Jeollanamdo = {"시군구 선택", "강진군", "고흥군", "곡성군", "광양시", "구례군", "나주시", "담양군", "목포시", "무안군", "보성군", "순천시", "신안군", "여수시", "영광군", "영암군"
            , "완도군", "장성군", "장흥군", "진도군"};
    private final String[] Island = {"시군구 선택", "남제주군", "북제주군", "서귀포시", "제주시"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_init_setting);

        Button nextButton = findViewById(R.id.initial_setting_next_button);
        NetworkUtil.wifiSafe(this); // 네트워크 체크
        final String[] location = {null};

        Spinner states = findViewById(R.id.spinner_states);
        Spinner cities = findViewById(R.id.spinner_cities);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,
                Main_Category);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setAdapter(arrayAdapter);
        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] city = {"지역을 선택해주세요."};
                if (i == 1) city = Seoul;
                else if (i == 2) city = Incheon;
                else if (i == 3) city = Daejeon;
                else if (i == 4) city = Daegu;
                else if (i == 5) city = Gwangju;
                else if (i == 6) city = Busan;
                else if (i == 7) city = Ulsan;
                else if (i == 8) city = Sejong;
                else if (i == 9) city = Gyeonggido;
                else if (i == 10) city = Gangwondo;
                else if (i == 11) city = Chungcheongbukdo;
                else if (i == 12) city = Chungcheongnamdo;
                else if (i == 13) city = Gyeongsangbukdo;
                else if (i == 14) city = Gyeongsangnamdo;
                else if (i == 15) city = Jeollabukdo;
                else if (i == 16) city = Jeollanamdo;
                else if (i == 17) city = Island;

                try {
                    ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(
                            getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            city);

                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    cities.setAdapter(cityAdapter);
                    String[] finalCity = city;
                    cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i != 0) location[0] = finalCity[i];
                        }

                        @Override public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        nextButton.setOnClickListener(v ->
                FirebaseHelper.get().connect(FirebaseDatabase.getInstance().getReference("server"), snapshot -> {
                    Server server = snapshot.getValue(Server.class);
                    ServerCache.setInstance(server);

                    String id = DeviceId.getInstance(this).getUUID();
                    User model = new User();
                    model.setId(id);

                    ArrayList<Conversation> conversations = new ArrayList<>();

                    model.setDict(conversations);
                    EditText nameEditText = findViewById(R.id.user_name);

                    try {
                        if (nameEditText.getText() != null &&
                                !nameEditText.getText().toString().trim().equals("") &&
                                !nameEditText.getText().toString().replaceAll(" ", "").equals("") &&
                                location[0] != null &&
                                !location[0].trim().equals("") &&
                                !location[0].replaceAll(" ", "").equals("")) {

                            model.setName(nameEditText.getText().toString());
                            model.setLocation(location[0]);
                            UserCache.setInstance(model);
                            // 싱글톤 업로드

                            FirebaseDatabase.getInstance()
                                    .getReference("user")
                                    .child(id)
                                    .setValue(model);

                            Preference.get(this).setBoolean("isFirst", false);
                            startActivity(new Intent(InitialSettingActivity.this, MainActivity.class));
                            finish();
                        }else{
                            Toasty.error(this, " 모든 정보를 입력해주세요");
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));
    }
}