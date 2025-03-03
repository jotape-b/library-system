package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.models.LoanStorage;

public interface ReportPlugin {
    String generateReport(LoanStorage loanStorage);
    String getPluginName();
}
