package br.edu.ifba.inf008;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.edu.ifba.inf008.models.Book;

public class Library implements Serializable{
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) throws Exception{
        if(books.stream().anyMatch(b -> b.getIsbn().equals(book.getIsbn()))){
            throw new Exception("ISBN already exists.");
        }
        if(UserManager.currentUser == null){
            throw new Exception("Login necessary.");
        }
        books.add(book);
    }

    public List<Book> searchForBooks(String title){
        return books.stream()
            .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
            .collect(Collectors.toList());
    }
}
