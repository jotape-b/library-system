package br.edu.ifba.inf008.models;
import java.io.Serializable;

public class User implements Serializable{
    private static int idCounter = 0;
    private int id;
    private String name;
    private Loan borrowedBooks;

    public User(String name){
        this.id = idCounter++;
        this.name = name;
    }

    public static int getIdCounter(){
        return idCounter;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public static void setIdCounter(int counter) {
        idCounter = counter;
    }

}
