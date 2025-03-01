package br.edu.ifba.inf008;

import br.edu.ifba.inf008.models.Book;
import br.edu.ifba.inf008.models.Loan;
import br.edu.ifba.inf008.models.User;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryApp extends Application {
    private Library library = new Library();
    private UserManager userManager = new UserManager();
    private LoanStorage loanStorage = new LoanStorage();
    private static final String DATA_FILE = "library-data.txt";
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        loadAppData();

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

        primaryStage.setOnCloseRequest(event -> {
            saveAppData();
        });

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
        
        Tab tab2 = new Tab("Borrowed Books", createLoanPane());
        tab.setClosable(false);
        tabPane.getTabs().add(tab2);

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

    private VBox createUserLoginPane() {
        VBox addLoginPane = new VBox(10);
        addLoginPane.setPadding(new Insets(10));
    
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
    
        addLoginPane.getChildren().addAll(
            new Label("Login By Id:"), idField,
            loginButton,
            userInfoBox,
            addMessage
        );
    
        return addLoginPane;
    }

    private VBox createBookSearchPane() {
        VBox searchPane = new VBox(10);
        searchPane.setPadding(new Insets(10));

        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        ListView<Book> resultList = new ListView<>();
        Button borrowButton = new Button("Borrow Selected Books");
        Label borrowMessage = new Label();

        resultList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getTitle() + " (" + book.getIsbn() + ")");
                }
            }
        });

        searchButton.setOnAction(e -> {
            String title = searchField.getText();
            List<Book> results = library.searchForBooks(title).stream()
                    .collect(Collectors.toList());
            resultList.getItems().setAll(results);
        });

        borrowButton.setOnAction(e -> {
            List<Book> selectedBooks = new ArrayList<>(resultList.getSelectionModel().getSelectedItems());
            if (selectedBooks.isEmpty()) {
                borrowMessage.setText("Error: No books selected.");
                return;
            }

            try {
                Loan loan = new Loan();
                for (Book book : selectedBooks) {
                    loan.addBookToLoan(book);
                }
                userManager.registerLoan(UserManager.currentUser, loan);
                loanStorage.storeLoan(loan);
                borrowMessage.setText("Books borrowed successfully.");
            } catch (Exception ex) {
                borrowMessage.setText("Error: " + ex.getMessage());
            }
        });

        searchPane.getChildren().addAll(
                new Label("Search By Title:"), searchField,
                searchButton, resultList,
                borrowButton, borrowMessage
        );
        return searchPane;
    }


    private VBox createLoanPane() {
        VBox loanPane = new VBox(10);
        loanPane.setPadding(new Insets(10));

        ListView<Book> loanListView = new ListView<>();
        Button returnButton = new Button("Return Selected Books");
        Label loanMessage = new Label();

        loanListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (empty || book == null) {
                    setText(null);
                } else {
                    setText(book.getTitle() + " (" + book.getIsbn() + ")");
                }
            }
        });

        loanPane.setOnMouseEntered(e -> {
            if (UserManager.currentUser == null) {
                loanListView.getItems().clear();
                returnButton.setVisible(false);
            } else {
                List<Book> borrowedBooks = UserManager.currentUser.getCurrentLoanBooks();
                loanListView.getItems().setAll(borrowedBooks);
                returnButton.setVisible(!borrowedBooks.isEmpty());
            }
        });
        

        returnButton.setOnAction(e -> {
            List<Book> selectedBooks = new ArrayList<>(loanListView.getSelectionModel().getSelectedItems());

            if (selectedBooks.isEmpty()) {
                loanMessage.setText("Error: No books selected.");
                return;
            }

            boolean success = true;
            for (Book book : selectedBooks) {
                try {
                    userManager.returnBook(UserManager.currentUser, book);
                } catch (Exception ex) {
                    loanMessage.setText("Error: " + ex.getMessage());
                    success = false;
                    break;
                }
            }

            if (success) {
                loanMessage.setText("Books returned successfully.");
                loanListView.getItems().removeAll(selectedBooks);
            }
        });

        loanPane.getChildren().addAll(new Label("Borrowed Books:"), loanListView, returnButton, loanMessage);
        return loanPane;
    }

    private void saveAppData() {
        int userIdCounter = User.getIdCounter();
        DataHelper data = new DataHelper(library, userManager, /*loanStorage,*/ userIdCounter);
        SerializationManager.saveData(data, DATA_FILE);
    }
    

    private void loadAppData() {
        DataHelper data = SerializationManager.loadData(DATA_FILE, DataHelper.class);
        if (data != null) {
            this.library = data.getLibrary();
            this.userManager = data.getUserManager();
            //this.loanStorage = data.getLoanStorage();
            User.setIdCounter(data.getUserIdCounter());
        } else {
            this.library = new Library();
            this.userManager = new UserManager();
        }
    }
    

    public static void main(String[] args) {
        launch();
    }
}
