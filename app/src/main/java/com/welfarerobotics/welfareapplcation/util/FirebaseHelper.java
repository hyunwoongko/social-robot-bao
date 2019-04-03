package com.welfarerobotics.welfareapplcation.util;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {

    private static FirebaseHelper helper = null;

    public synchronized static FirebaseHelper get(){
        if (helper == null){
            helper = new FirebaseHelper();
        }
        return helper;
    }

    void getDatabase(FirebaseDatabase dbPath, FirebaseCallback callback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.run(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public interface FirebaseCallback{
        void run(DataSnapshot snapshot);
    }
}
