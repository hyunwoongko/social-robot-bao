package com.welfarerobotics.welfareapplcation.util.data_loader;

import android.content.Intent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.welfarerobotics.welfareapplcation.core.initial.SplashActivity;
import com.welfarerobotics.welfareapplcation.core.main.MainActivity;
import com.welfarerobotics.welfareapplcation.entity.Server;
import com.welfarerobotics.welfareapplcation.entity.User;
import com.welfarerobotics.welfareapplcation.entity.cache.ServerCache;
import com.welfarerobotics.welfareapplcation.entity.cache.UserCache;
import com.welfarerobotics.welfareapplcation.util.DeviceId;
import com.welfarerobotics.welfareapplcation.util.NetworkUtil;
import com.welfarerobotics.welfareapplcation.util.data_util.FirebaseHelper;

/**
 * @author : Hyunwoong
 * @when : 2019-07-01 오후 8:09
 * @homepage : https://github.com/gusdnd852
 */
public class ServerDataLoader implements DataLoader {

    static private ServerDataLoader instance =null;

    static ServerDataLoader getInstance() {
        if (instance == null) {
            instance = new ServerDataLoader();
        }
        return instance;
    }

    @Override public void load() {
        FirebaseHelper.get().connect(FirebaseDatabase.getInstance()
                .getReference()
                .child("server"), this::save);
    }

    @Override public void save(DataSnapshot snapshot) {
        Server server = snapshot.getValue(Server.class);
        ServerCache.setInstance(server);
    }
}
