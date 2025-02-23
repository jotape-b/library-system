package br.edu.ifba.inf008;

import br.edu.ifba.inf008.models.Book;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryApp extends Application {
    private Library library = new Library();

    @Override
    public void start(Stage primaryStage) {
        loadLibrary();
        // Painel de Adição de Livros
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
                addMessage.setText("Livro adicionado com sucesso!");
                saveLibrary();
            } catch (Exception ex) {
                addMessage.setText("Erro: " + ex.getMessage());
            }
        });
        
        VBox addBookPane = new VBox(10, new Label("ISBN:"), isbnField,
                                    new Label("Título:"), titleField,
                                    new Label("Autor:"), authorField,
                                    new Label("Ano de Lançamento:"), yearField,
                                    new Label("Gênero:"), genreField,
                                    addButton, addMessage);
        addBookPane.setPadding(new Insets(10));

        // Painel de Pesquisa de Livros
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

        VBox searchPane = new VBox(10, new Label("Buscar por Título:"), searchField, searchButton, resultList);
        searchPane.setPadding(new Insets(10));
        
        // Layout principal
        TabPane tabPane = new TabPane();
        tabPane.getTabs().add(new Tab("Adicionar Livro", addBookPane));
        tabPane.getTabs().add(new Tab("Buscar Livros", searchPane));

        primaryStage.setTitle("Biblioteca");
        primaryStage.setScene(new Scene(tabPane, 400, 400));
        primaryStage.show();
    }

    private void saveLibrary() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("NEWFILE.txt"))) {
            oos.writeObject(library);
        } catch (IOException e) {
            System.err.println("Erro ao salvar a biblioteca: " + e.getMessage());
        }
    }

    private void loadLibrary() {
        File file = new File("NEWFILE.txt");
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
