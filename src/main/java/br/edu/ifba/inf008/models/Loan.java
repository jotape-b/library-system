package br.edu.ifba.inf008.models;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.edu.ifba.inf008.UserManager;

public class Loan implements Serializable{
    private User borrower;
    private List<Book> selectedBooks;
    private LocalDateTime loanDate;

    public Loan(){
        this.borrower = UserManager.currentUser;
        this.loanDate = LocalDateTime.now();
        this.selectedBooks = new ArrayList<>();
    }
    
    public void addBookToLoan(Book book) throws Exception{
        if(selectedBooks.size() == 5){
            throw new Exception("You can only borrow up to five books at a time.");
        }
        if(UserManager.currentUser == null){
            throw new Exception("Login required.");
        }
        book.setIsAvailable(false);
        selectedBooks.add(book);
    }

    public boolean isOverdue(){
        LocalDateTime today = LocalDateTime.now();
        long days = Duration.between(this.loanDate, today).toDays();
        return days > 14;
    }
    

    public List<Book> getSelectedBooks(){
        return selectedBooks;
    }

    public User getBorrower(){
        return borrower;
    }

    public LocalDateTime getLoanDate(){
        return loanDate;
    }

    /*
    @Override
    public int hashCode() {
        return Objects.hash(loanDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Loan other = (Loan) obj;
        return Objects.equals(loanDate, other.loanDate);
    }*/
}
