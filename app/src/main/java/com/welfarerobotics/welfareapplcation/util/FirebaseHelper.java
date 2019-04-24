package com.welfarerobotics.welfareapplcation.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import java8.util.function.Consumer;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 12:12 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class FirebaseHelper {
    private static FirebaseHelper instance;

    private FirebaseHelper(){

    }

    public static synchronized FirebaseHelper get() {
        if (instance == null)
            instance = new FirebaseHelper();
        return instance;
    }

    public void connect(DatabaseReference reference, Consumer<DataSnapshot> consumer) {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                consumer.accept(dataSnapshot);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
