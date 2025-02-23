package br.edu.ifba.inf008;

import java.io.Serializable;

public class DataHelper implements Serializable {
    private Library library;
    private UserManager userManager;
    private int userIdCounter;

    public DataHelper(Library library, UserManager userManager, int userIdCounter) {
        this.library = library;
        this.userManager = userManager;
        this.userIdCounter = userIdCounter;
    }

    public Library getLibrary() {
        return library;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public int getUserIdCounter() {
        return userIdCounter;
    }
}
