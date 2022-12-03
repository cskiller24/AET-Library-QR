package com.example.aet_library_qr;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOTransaction {
    private DatabaseReference databaseReference;

    public DAOTransaction(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Transaction.class.getSimpleName());
    }

    public Task<Void> add(Transaction tran){
        return databaseReference.child(tran.getBookID()+tran.getStdID()).setValue(tran);
    }
}