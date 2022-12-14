package com.example.aet_library_qr;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DAOStudent {
    private DatabaseReference databaseReference;

    public DAOStudent() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Student.class.getSimpleName());
    }


    public Task<Void> add(Student std, String uid) {
        return databaseReference.child(uid).setValue(std);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public void getAllStudents(ValueEventListener listener)
    {
        this.databaseReference.addListenerForSingleValueEvent(listener);
    }

}
