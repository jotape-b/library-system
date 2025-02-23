package br.edu.ifba.inf008.models;
import java.io.Serializable;

public class Book implements Serializable{
    private String isbn;
    private String title;
    private String author;
    private int yearOfRelease;
    private String genre;
    private boolean available;

    public Book(String isbn, String title, String author, int yearOfRelease, String genre){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.yearOfRelease = yearOfRelease;
        this.genre = genre;
        this.available = true;
    }
    
    public String getIsbn(){
        return isbn;
    }

    public String getTitle(){
        return title;
    }
}
