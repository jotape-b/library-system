package br.edu.ifba.inf008;

import br.edu.ifba.inf008.models.Book;
import br.edu.ifba.inf008.models.User;
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
    private UserManager userManager = new UserManager();
    private static final String DATA_FILE = "library.ser";
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadLibrary();

        Scene registerScene = createRegisterScene();
        Scene mainAppScene = createMainAppScene();

        Button switchToMainApp = new Button("Go To Library Screen");
        switchToMainApp.setOnAction(e -> primaryStage.setScene(mainAppScene));

        Button switchToRegister = new Button("Go To Administration Screen");
        switchToRegister.setOnAction(e -> primaryStage.setScene(registerScene));

        ((VBox) registerScene.getRoot()).getChildren().add(switchToMainApp);
        ((VBox) mainAppScene.getRoot()).getChildren().add(switchToRegister);

        primaryStage.setTitle("Library");
        primaryStage.setScene(registerScene);
        primaryStage.show();
    }

    private Scene createRegisterScene() {
        TabPane tabPane = new TabPane();
        Tab tab = new Tab("Add Book", createBookRegisterPane());
        tab.setClosable(false);
        tabPane.getTabs().add(tab);

        Tab tab2 = new Tab("Add User", createUserRegisterPane());
        tab2.setClosable(false);
        tabPane.getTabs().add(tab2);

        Tab tab3 = new Tab("Login as User", createUserLoginPane());
        tab3.setClosable(false);
        tabPane.getTabs().add(tab3);

        return new Scene(new VBox(tabPane), 600, 400);
    }

    private Scene createMainAppScene() {
        TabPane tabPane = new TabPane();
        Tab tab = new Tab("Search For Books", createBookSearchPane());
        tab.setClosable(false);
        tabPane.getTabs().add(tab);

        return new Scene(new VBox(tabPane), 600, 400);
    }

    private VBox createBookRegisterPane() {
        VBox addBookRegisterPane = new VBox(7);
        addBookRegisterPane.setPadding(new Insets(10));

        TextField isbnField = new TextField();
        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField yearField = new TextField();
        TextField genreField = new TextField();
        Button addButton = new Button("Add Book");
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
                addMessage.setText("Book successfully added.");
            } catch (Exception ex) {
                addMessage.setText("Error: " + ex.getMessage());
            }
        });

        addBookRegisterPane.getChildren().addAll(
                new Label("ISBN:"), isbnField,
                new Label("Title:"), titleField,
                new Label("Author:"), authorField,
                new Label("Year of Release:"), yearField,
                new Label("Genre:"), genreField,
                addButton, addMessage
        );
        return addBookRegisterPane;
    }

    private VBox createUserRegisterPane(){
        VBox addUserRegisterPane = new VBox(10);
        addUserRegisterPane.setPadding(new Insets(10));

        TextField nameField = new TextField();
        Button addButton = new Button("Add User");
        Label addMessage = new Label();

        addButton.setOnAction(e -> {
            try{
                String name = nameField.getText();

                User user = new User(name);
                userManager.addUser(user);
                //Salvar aqui
                addMessage.setText("User successfully added. " + "ID: " + user.getId());
            } catch (Exception ex){
                addMessage.setText("Error: " + ex.getMessage());
            }
        });

        addUserRegisterPane.getChildren().addAll(
            new Label("Name:"), nameField,
            addButton, addMessage
        );
        return addUserRegisterPane;
    }

    private VBox createBookSearchPane() {
        VBox bookSearchPane = new VBox(10);
        bookSearchPane.setPadding(new Insets(10));

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

        bookSearchPane.getChildren().addAll(
                new Label("Search By Title:"), searchField,
                searchButton, resultList
        );
        return bookSearchPane;
    }

    private VBox createUserLoginPane() {
        VBox loginPane = new VBox(10);
        loginPane.setPadding(new Insets(10));
    
        TextField idField = new TextField();
        Button loginButton = new Button("Login");
        Label userLabel = new Label("Current User: ");
        Label nameLabel = new Label("NULL");
        Label addMessage = new Label();
    
        HBox userInfoBox = new HBox(5);
        userInfoBox.getChildren().addAll(userLabel, nameLabel);
    
        loginButton.setOnAction(e -> {
            try {
                Integer id = Integer.parseInt(idField.getText());
                userManager.login(id);
                nameLabel.setText(UserManager.currentUser.getName());
                addMessage.setText("Login successful. Welcome");
            } catch (Exception ex) {
                addMessage.setText("Error: " + ex.getMessage());
            }
        });
    
        loginPane.getChildren().addAll(
            new Label("Login By Id:"), idField,
            loginButton,
            userInfoBox,
            addMessage
        );
    
        return loginPane;
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
