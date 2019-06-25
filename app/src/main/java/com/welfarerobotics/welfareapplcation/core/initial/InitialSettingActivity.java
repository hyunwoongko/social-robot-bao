package com.welfarerobotics.welfareapplcation.core.initial;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kinda.alert.KAlertDialog;
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
import com.welfarerobotics.welfareapplcation.util.ToastType;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;
import com.welfarerobotics.welfareapplcation.util.data_util.Preference;
import es.dmoral.toasty.Toasty;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class InitialSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_init_setting);

        Button nextButton = findViewById(R.id.initial_setting_next_button);
        NetworkUtil.wifiSafe(this); // 네트워크 체크

        nextButton.setOnClickListener(v ->
                FirebaseHelper.get().connect(FirebaseDatabase.getInstance().getReference("server"), snapshot -> {
            Server server = snapshot.getValue(Server.class);
            ServerCache.setInstance(server);

            String id = DeviceId.getInstance(this).getUUID();
            User model = new User();
            model.setId(id);

            ArrayList<Conversation> conversations = new ArrayList<>();
            Conversation firstConversation = new Conversation();
            firstConversation.setInput("안녕");
            firstConversation.setOutput("안녕하세요. 반가워요!");
            firstConversation.setInput("이름");
            firstConversation.setOutput("제 이름은 바오에요.");
            firstConversation.setInput("이름이 뭐야");
            firstConversation.setOutput("제 이름은 바오에요!");
            conversations.add(firstConversation);
            model.setDict(conversations);

            EditText nameEditText = findViewById(R.id.user_name);
            EditText addressEditText = findViewById(R.id.user_address);


            if (nameEditText.getText() != null &&
                    !nameEditText.getText().toString().trim().equals("") &&
                    !nameEditText.getText().toString().replaceAll(" ", "").equals("") &&
                    addressEditText.getText() != null &&
                    !addressEditText.getText().toString().trim().equals("") &&
                    !addressEditText.getText().toString().replaceAll(" ", "").equals("")) {

                model.setName(nameEditText.getText().toString());
                model.setLocation(addressEditText.getText().toString());
                UserCache.setInstance(model);
                // 싱글톤 업로드

                FirebaseDatabase.getInstance()
                        .getReference("user")
                        .child(id)
                        .setValue(model);

                Preference.get(this).setBoolean("isFirst", false);
                startActivity(new Intent(InitialSettingActivity.this, MainActivity.class));
                finish();
            }
        }));
    }
}