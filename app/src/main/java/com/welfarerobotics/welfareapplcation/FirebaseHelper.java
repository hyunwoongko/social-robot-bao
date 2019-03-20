package com.welfarerobotics.welfareapplcation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.function.Consumer;

public class FirebaseHelper {

    private static FirebaseHelper helper = null;

    public synchronized static FirebaseHelper get(){
        if (helper == null){
            helper = new FirebaseHelper();
        }
        return helper;
    }

    void getDatabase(Consumer<String> callback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.accept((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
