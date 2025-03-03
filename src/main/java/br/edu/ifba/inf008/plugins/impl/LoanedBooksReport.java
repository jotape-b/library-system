package br.edu.ifba.inf008.plugins.impl;

import br.edu.ifba.inf008.models.Loan;
import br.edu.ifba.inf008.models.LoanStorage;
import br.edu.ifba.inf008.plugins.ReportPlugin;
import java.util.stream.Collectors;

public class LoanedBooksReport implements ReportPlugin {

    @Override
    public String generateReport(LoanStorage loanStorage) {
        String report = loanStorage.getLoans().stream()
            .filter(loan -> !loan.isOverdue())
            .map(Loan::toString)
            .collect(Collectors.joining("\n"));

        return report.isEmpty() ? "No books have been borrowed." : report;
    }

    @Override
    public String getPluginName() {
        return "Loaned Books Report";
    }
}
