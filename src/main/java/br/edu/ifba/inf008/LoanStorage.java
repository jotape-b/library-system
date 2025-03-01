package br.edu.ifba.inf008;

import br.edu.ifba.inf008.models.Loan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifba.inf008.models.Loan;

public class LoanStorage {
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
}
