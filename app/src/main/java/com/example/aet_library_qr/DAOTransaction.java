package com.example.aet_library_qr;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DAOTransaction {
    private DatabaseReference databaseReference;

    public DAOTransaction(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Transaction.class.getSimpleName());
    }

    public Task<Void> add(@NonNull Transaction tran){
        return databaseReference.child(tran.getBookID()+tran.getStdID()).setValue(tran);
    }

    public void getStudentTransactionsByUID(String uid, ValueEventListener listener) {
        databaseReference.orderByChild("stdID").equalTo(uid).addListenerForSingleValueEvent(listener);
    }
}
