/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wx
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MagazineHandler {

    private Map<String, MagazineService> magazineServiceMap;
    private AlertHandler alert = new AlertHandler();

    public MagazineHandler() {
        magazineServiceMap = new HashMap<>();
    }

    public void addMagazineService(String magazineName) {
        MagazineService magazineService = new MagazineService();
        magazineServiceMap.put(magazineName, magazineService);
    }

    public MagazineService getMagazineService(String magazineName) {
        return magazineServiceMap.get(magazineName);
    }

    public boolean compareMagazineName(String magazineName) {
        return magazineServiceMap.containsKey(magazineName);
    }

    public ArrayList<String> getAllMagazineNames() {
        return new ArrayList<>(magazineServiceMap.keySet());
    }

    public void saveMagazineToFile(String magazineName) {
        try {
            File file = new File(magazineName + ".ser");

            // Check if file already exists
            if (file.exists()) {
                // If the file exists, delete it and write new object over
                if (!file.delete()) {
                    alert.showAlert("Failed to delete the existing file.");
                }
            }

            try (FileOutputStream outputFile = new FileOutputStream(file);
                    ObjectOutputStream objectOut = new ObjectOutputStream(outputFile)) {

                // Write the MagazineService object to the file
                objectOut.writeObject(magazineServiceMap.get(magazineName));
                alert.showAlert(magazineName + " has been saved successfully");

            }
        } catch (IOException ex) {
            alert.showAlert("I/O Error");
        }
    }

    public void loadMagazineFromFile(String magazineName) {
        try (FileInputStream inputFile = new FileInputStream(magazineName + ".ser");
                ObjectInputStream objectIn = new ObjectInputStream(inputFile)) {

            magazineServiceMap.put(magazineName, (MagazineService) objectIn.readObject());
            alert.showAlert(magazineName + " has been loaded successfully");

        } catch (IOException | ClassNotFoundException ex) {
            // Handle any IOException or ClassNotFoundException that might occur during deserialization
            alert.showAlert("File not found");
        }
    }
}
