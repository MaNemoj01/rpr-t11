package ba.unsa.rpr.tutorijal10;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class Main extends Application {
    private static GeografijaDAO g = GeografijaDAO.getInstance();
    @Override
    public void start(Stage primaryStage) throws Exception{
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        BazaModel bm = new BazaModel();
        bm.napuni();
        FXMLLoader loader = new FXMLLoader( getClass().getResource("bazacontroller.fxml"), bundle );
        BazaController bc = new BazaController(bm);
        loader.setController( bc );
        Parent root = loader.load();
        primaryStage.setTitle("Baza");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Napomena!");
        a.setHeaderText("Ako zelite da izmijenite grad ili drzavu,\nsamo kliknite na nju dva puta!!!");
        a.showAndWait();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }



    public static void main(String[] args) {
        launch(args);
    }

    public static String ispisiGradove() {
        ArrayList<Grad> cities  = g.gradovi();

        String s = "";
        if( cities != null ) {

            for (int i = 0; i < cities.size(); i++) {
                s += cities.get(i).getNaziv();
                s += " (";
                s += cities.get(i).getDrzava().getNaziv();
                s += ") - ";
                s += cities.get(i).getBrojStanovnika();
                s += "\n";
            }
        }
        return s;
    }


    private static void glavniGrad( String nazivDrzave ){

    }
}