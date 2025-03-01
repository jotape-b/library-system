package br.edu.ifba.inf008.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User implements Serializable {
    private static int idCounter = 0;
    private int id;
    private String name;
    private Set<Loan> loans;

    public User(String name) {
        this.id = idCounter++;
        this.name = name;
        this.loans = new HashSet<>();
    }

    public void addLoan(Loan loan) throws Exception {
        int newTotal = getTotalBorrowedBooks() + loan.getSelectedBooks().size();
        if (newTotal > 5) {
            throw new Exception("You can only borrow up to five books at a time.");
        }
        loans.add(loan);
    }

    public void returnLoan(Loan loan) {
        loans.remove(loan);
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Book> getCurrentLoanBooks() {
        List<Book> borrowedBooks = new ArrayList<>();
        for (Loan loan : loans) {
            borrowedBooks.addAll(loan.getSelectedBooks());
        }
        return borrowedBooks;
    }
    

    public int getTotalBorrowedBooks() {
        return loans.stream().mapToInt(loan -> loan.getSelectedBooks().size()).sum();
    }

    public Set<Loan> getLoans(){
        return loans;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }


}