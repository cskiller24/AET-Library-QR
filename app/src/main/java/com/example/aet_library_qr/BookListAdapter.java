package com.example.aet_library_qr;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    Context context;
    ArrayList<Book> books;
    ArrayList<String> bookKeys;

    public BookListAdapter(Context context, ArrayList<Book> books, ArrayList<String> bookKeys) {
        this.context = context;
        this.books = books;
        this.bookKeys = bookKeys;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_card, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.cardBookAuthor.setText(book.getAuthor());
        holder.cardBookTitle.setText(book.getTitle());
        Log.i("SSS2", "" + bookKeys.get(position));
        holder.setKey(bookKeys.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView cardBookTitle;
        TextView cardBookAuthor;
        CardView bookCard;
        String key;
        private final Context context;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            cardBookAuthor = itemView.findViewById(R.id.cardBookAuthor);
            cardBookTitle = itemView.findViewById(R.id.cardBookTitle);

            bookCard = itemView.findViewById(R.id.bookCard);
            bookCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, BookInfoAdmin.class);
                    intent.putExtra("key", getKey());
                    context.startActivity(intent);
                }
            });
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }
}