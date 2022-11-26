package com.example.aet_library_qr;

public class Book {
    private String author;
    private String title;
    private String yearPublished;
    private boolean is_available;
    private boolean is_deleted;

    public Book(){}

    public Book(String title, String author, String yearPublished)
    {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.is_available = true;
        this.is_deleted = false;
    }

    public Book(String title, String author, String yearPublished, boolean is_available, boolean is_deleted)
    {
        this.title = title;
        this.author = author;
        this.yearPublished = yearPublished;
        this.is_available = is_available;
        this.is_deleted = is_deleted;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(String yearPublished) {
        this.yearPublished = yearPublished;
    }

    public boolean isIs_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }
}