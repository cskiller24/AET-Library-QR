package com.example.aet_library_qr;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DAOBook {
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public DAOBook()
    {
        this.database = FirebaseDatabase.getInstance();
        this.reference = this.database.getReference(Book.class.getSimpleName());
    }

    public Task<Void> addBook(Book book)
    {
        return this.reference.push().setValue(book);
    }

    public void getAllBooks(ValueEventListener listener)
    {
        this.reference.addListenerForSingleValueEvent(listener);
    }

}