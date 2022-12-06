package com.example.aet_library_qr;

import com.example.aet_library_qr.utils.DateHelpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Transaction {
    private String bookID, bookTitle, bookAuthor, bookYear, stdID, stdNum, stdName;
    private String borrowedAt;
    private String expiresAt;

    public Transaction() {

    }

    public Transaction(String bookID, String bookTitle, String bookAuthor, String bookYear, String stdID, String stdNum, String stdName) {
        this.bookID = bookID;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookYear = bookYear;
        this.stdID = stdID;
        this.stdNum = stdNum;
        this.stdName = stdName;
        DateHelpers helpers = DateHelpers.getInstance();
        this.borrowedAt = helpers.getDateToday();
        this.expiresAt = helpers.getDateDaysFromNow(7);
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookYear() {
        return bookYear;
    }

    public void setBookYear(String bookYear) {
        this.bookYear = bookYear;
    }

    public String getStdID() {
        return stdID;
    }

    public void setStdID(String stdID) {
        this.stdID = stdID;
    }

    public String getStdNum() {
        return stdNum;
    }

    public void setStdNum(String stdNum) {
        this.stdNum = stdNum;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public String getBorrowedAt() {
        return borrowedAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }
}
