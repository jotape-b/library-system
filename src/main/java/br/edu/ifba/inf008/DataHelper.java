package br.edu.ifba.inf008;

import java.io.Serializable;

public class DataHelper implements Serializable {
    private Library library;
    private UserManager userManager;

    public DataHelper(Library library, UserManager userManager) {
        this.library = library;
        this.userManager = userManager;
    }

    public Library getLibrary() {
        return library;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
