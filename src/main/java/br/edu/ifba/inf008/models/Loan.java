package br.edu.ifba.inf008.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import br.edu.ifba.inf008.UserManager;

public class Loan implements Serializable{
    private User borrower;
    private Set<Book> selectedBooks;
    private LocalDate loanDate;

    public Loan(){
        this.borrower = UserManager.currentUser;
        this.loanDate = LocalDate.now();
        this.selectedBooks = new HashSet<>();
        System.out.println(borrower.getName() + loanDate);
    }
    
    public void addBookToLoan(Book book){
        selectedBooks.add(book);
    }

    public User getBorrower(){
        return borrower;
    }
}
