package br.edu.ifba.inf008;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.models.Book;

public class Library implements Serializable{
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) throws Exception{
        if(books.stream().anyMatch(b -> b.getIsbn().equals(book.getIsbn()))){
            throw new Exception("Erro: O ISBN inserido já existe.");
        }
        books.add(book);
    }

    public List<Book> searchForBooks(String title){
        return books.stream()
            .filter(b -> b.getTitle().contains(title))
            .toList();
    }
}
