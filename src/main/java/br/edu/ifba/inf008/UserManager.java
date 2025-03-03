package br.edu.ifba.inf008;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import br.edu.ifba.inf008.models.Book;
import br.edu.ifba.inf008.models.Loan;
import br.edu.ifba.inf008.models.User;

public class UserManager implements Serializable {
    private Map<Integer, User> users = new HashMap<>();
    public static User currentUser = null;

    public void addUser(User user) throws Exception {
        if (users.containsKey(user.getId())) {
            throw new Exception("This ID has already been registered.");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new Exception("User name cannot be empty.");
        }
        users.put(user.getId(), user);
    }

    public void registerLoan(User user, Loan loan) throws Exception {
        if (UserManager.currentUser == null) {
            throw new Exception("Login required.");
        }
        user.addLoan(loan);
    }

    public void returnLoan(User user, Loan loan) throws Exception {
        if (!user.getLoans().contains(loan)) {
            throw new Exception("This loan is not registered for this user.");
        }
        user.returnLoan(loan);
    }

    public void returnBook(User user, Book book) throws Exception {
        Loan loanToModify = null;
        
        for (Loan loan : user.getLoans()) {
            if (loan.getSelectedBooks().contains(book)) {
                loanToModify = loan;
                break;
            }
        }

        if (loanToModify == null) {
            throw new Exception("This book is not in any of your loans.");
        }

        loanToModify.getSelectedBooks().remove(book);
        book.setIsAvailable(true);
        
        if (loanToModify.getSelectedBooks().isEmpty()) {
            user.returnLoan(loanToModify);
        }
    }

    public void login(Integer id) throws Exception {
        currentUser = users.get(id);
        if (currentUser == null) {
            throw new Exception("ID not found.");
        }
    }
}
