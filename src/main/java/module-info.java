module br.edu.ifba.inf008 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Declara que este módulo usa a interface ReportPlugin
    uses br.edu.ifba.inf008.plugins.ReportPlugin;

    // Permite que o JavaFX acesse as classes do pacote br.edu.ifba.inf008
    opens br.edu.ifba.inf008 to javafx.fxml;

    // Exporta os pacotes necessários
    exports br.edu.ifba.inf008;
    exports br.edu.ifba.inf008.plugins;
}