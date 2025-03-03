package br.edu.ifba.inf008.plugins.impl;

import br.edu.ifba.inf008.models.Loan;
import br.edu.ifba.inf008.models.LoanStorage;
import br.edu.ifba.inf008.plugins.ReportPlugin;

import java.time.Duration;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class OverdueBooksReport implements ReportPlugin {

    private static final double FINE_PER_DAY = 0.50;

    @Override
    public String generateReport(LoanStorage loanStorage) {
        LocalDate today = LocalDate.now();
    
        String report = loanStorage.getLoans().stream()
            .filter(Loan::isOverdue)
            .flatMap(loan -> loan.getSelectedBooks().stream().map(book -> {
                long daysLate = Duration.between(loan.getLoanDate(), today).toDays();
                double fine = daysLate * FINE_PER_DAY;
                return String.format("Book: %s | Days Overdue: %d | Fine: R$ %.2f",
                    book.getTitle(), daysLate, fine);
            }))
            .collect(Collectors.joining("\n"));
    
        return report.isEmpty() ? "No books are overdue." : report;
    }
    

    @Override
    public String getPluginName() {
        return "Overdue Books and Fines Report";
    }
}
