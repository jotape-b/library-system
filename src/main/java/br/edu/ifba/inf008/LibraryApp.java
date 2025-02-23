package br.edu.ifba.inf008;

import br.edu.ifba.inf008.models.Book;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryApp extends Application {
    private Library library = new Library();
    private static final String DATA_FILE = "library.ser";
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadLibrary();

        Scene registerScene = createRegisterScene();
        Scene mainAppScene = createMainAppScene();

        Button switchToMainApp = new Button("Ir para Buscar Livros");
        switchToMainApp.setOnAction(e -> primaryStage.setScene(mainAppScene));

        Button switchToRegister = new Button("Ir para Adicionar Livro");
        switchToRegister.setOnAction(e -> primaryStage.setScene(registerScene));

        ((VBox) registerScene.getRoot()).getChildren().add(switchToMainApp);
        ((VBox) mainAppScene.getRoot()).getChildren().add(switchToRegister);

        primaryStage.setTitle("Biblioteca");
        primaryStage.setScene(registerScene);
        primaryStage.show();
    }

    private Scene createRegisterScene() {
        TabPane tabPane = new TabPane();
        Tab tab = new Tab("Adicionar Livro", createRegisterPane());
        tab.setClosable(false);
        tabPane.getTabs().add(tab);
        return new Scene(new VBox(tabPane), 600, 400);
    }

    private Scene createMainAppScene() {
        TabPane tabPane = new TabPane();
        Tab tab = new Tab("Buscar Livros", createMainAppPane());
        tab.setClosable(false);
        tabPane.getTabs().add(tab);
        return new Scene(new VBox(tabPane), 600, 400);
    }

    private VBox createRegisterPane() {
        VBox addBookPane = new VBox(10);
        addBookPane.setPadding(new Insets(10));

        TextField isbnField = new TextField();
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField yearField = new TextField();
        TextField genreField = new TextField();
        Button addButton = new Button("Adicionar Livro");
        Label addMessage = new Label();

        addButton.setOnAction(e -> {
            try {
                String isbn = isbnField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText());
                String genre = genreField.getText();

                Book book = new Book(isbn, title, author, year, genre);
                library.addBook(book);
                saveLibrary();
                addMessage.setText("Livro adicionado com sucesso!");
            } catch (Exception ex) {
                addMessage.setText("Erro: " + ex.getMessage());
            }
        });

        addBookPane.getChildren().addAll(
                new Label("ISBN:"), isbnField,
                new Label("Título:"), titleField,
                new Label("Autor:"), authorField,
                new Label("Ano de Lançamento:"), yearField,
                new Label("Gênero:"), genreField,
                addButton, addMessage
        );
        return addBookPane;
    }

    private VBox createMainAppPane() {
        VBox searchPane = new VBox(10);
        searchPane.setPadding(new Insets(10));

        TextField searchField = new TextField();
        Button searchButton = new Button("Buscar");
        ListView<String> resultList = new ListView<>();

        searchButton.setOnAction(e -> {
            String title = searchField.getText();
            List<Book> results = library.searchForBooks(title).stream()
                    .collect(Collectors.toList());
            resultList.getItems().clear();
            for (Book book : results) {
                resultList.getItems().add(book.getTitle() + " (" + book.getIsbn() + ")");
            }
        });

        searchPane.getChildren().addAll(
                new Label("Buscar por Título:"), searchField,
                searchButton, resultList
        );
        return searchPane;
    }

    private void saveLibrary() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(library);
        } catch (IOException e) {
            System.err.println("Erro ao salvar a biblioteca: " + e.getMessage());
        }
    }

    private void loadLibrary() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                library = (Library) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar a biblioteca: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
