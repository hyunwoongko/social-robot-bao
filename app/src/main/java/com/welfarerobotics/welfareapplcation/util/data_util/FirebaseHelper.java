package com.welfarerobotics.welfareapplcation.util.data_util;

import com.google.firebase.database.*;
import java8.util.function.Consumer;

/**
 * @Author : Hyunwoong
 * @When : 4/24/2019 12:12 AM
 * @Homepage : https://github.com/gusdnd852
 */
public class FirebaseHelper {
    private static FirebaseHelper instance;

    private FirebaseHelper() {

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

            @Override public void onCancelled(DatabaseError databaseError) {
            }
        });



    }


    public void download(DatabaseReference reference, Consumer<DataSnapshot> consumer) {
        reference.addChildEventListener(new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                consumer.accept(dataSnapshot);
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                consumer.accept(dataSnapshot);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                consumer.accept(dataSnapshot);
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
