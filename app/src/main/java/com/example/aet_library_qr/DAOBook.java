package com.example.aet_library_qr;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

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

    public void getBook(String key, ValueEventListener listener)
    {
        this.reference.child(key).addListenerForSingleValueEvent(listener);
    }

    public Task<Void> updateBorrow(String key, HashMap<String, Object> hashMap){
        return this.reference.child(key).updateChildren(hashMap);
    }

    public Task<Void> updateBook(String key, Book book)
    {
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("author", book.getAuthor());
        hashMap.put("title", book.getTitle());
        hashMap.put("yearPublished", book.getYearPublished());

        return this.reference.child(key).updateChildren(hashMap);
    }

    public Task<Void> deleteBook(String key) {
        return this.reference.child(key).removeValue();
    }

}