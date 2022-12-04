package com.example.aet_library_qr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookLogListAdapter extends RecyclerView.Adapter<BookLogListAdapter.BookLogViewHolder> {

    Context context;
    ArrayList<Transaction> transactions;

    public BookLogListAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public BookLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_book_logs_student_card, parent, false);
        return new BookLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookLogViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.logBookTitle.setText(transaction.getBookTitle());
        holder.logBookAuthor.setText(transaction.getBookAuthor());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class BookLogViewHolder extends RecyclerView.ViewHolder {
        TextView logBookTitle, logBookAuthor;
        public BookLogViewHolder(@NonNull View itemView) {
            super(itemView);
            logBookTitle = itemView.findViewById(R.id.logBookTitle);
            logBookAuthor = itemView.findViewById(R.id.logBookAuthor);
        }
    }
}
