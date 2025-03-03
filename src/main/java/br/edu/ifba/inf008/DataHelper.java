package br.edu.ifba.inf008;

import java.io.Serializable;

import br.edu.ifba.inf008.models.LoanStorage;

public class DataHelper implements Serializable {
    private Library library;
    private UserManager userManager;
    private LoanStorage loanStorage;
    private int userIdCounter;

    public DataHelper(Library library, UserManager userManager, LoanStorage loanStorage, int userIdCounter) {
        this.library = library;
        this.userManager = userManager;
        this.loanStorage = loanStorage;
        this.userIdCounter = userIdCounter;
    }

    public Library getLibrary() {
        return library;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public LoanStorage getLoanStorage(){
        return loanStorage;
    }

    public int getUserIdCounter() {
        return userIdCounter;
    }
}
