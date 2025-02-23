module br.edu.ifba.inf008 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens br.edu.ifba.inf008 to javafx.fxml;
    exports br.edu.ifba.inf008;
}
