package br.edu.ifba.inf008;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SerializationManager{
    public static <T extends Serializable> void saveData(T data, String filename){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))){
            oos.writeObject(data);
            showSuccess("Data saved successfully");
        }
        catch (IOException e){
            showError("Failed to save data: " + e.getMessage());
        }
    }

    public static <T extends Serializable> T loadData(String filename, Class<T> c){
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return c.cast(ois.readObject());
        }
        catch(IOException | ClassNotFoundException e){
            showError("Failed to load data: " + e.getMessage());
            return null;
        }
    }

    private static void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setContentText(message);
        alert.showAndWait();
    }

}