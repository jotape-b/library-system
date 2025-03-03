package br.edu.ifba.inf008.models;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoanStorage implements Serializable{
    private List<Loan> loans = new ArrayList<>();

    public void storeLoan(Loan loan){
        loans.add(loan);
    }

    public void deleteLoan(Loan loan) throws Exception{
        if (!loans.contains(loan)) {
            throw new Exception("Loan does not exist.");
        }
        loans.remove(loan);;
    }

    public List<Loan> getLoans(){
        return loans;
    }
}
