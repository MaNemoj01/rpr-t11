package ba.unsa.rpr.tutorijal10;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class BazaController implements Initializable{
    private static GeografijaDAO g = GeografijaDAO.getInstance();
    private BazaModel bm;
    public TableView<Grad> tabelaGradova;
    private String  choiceBoxHelper = "";
    public TableColumn<Grad, Integer> idGrad = new TableColumn<>();
    public TableColumn<Grad, String> nazivGrad = new TableColumn<>();
    public TableColumn<Grad, Integer> brojStanovnikaGrad = new TableColumn<>();
    public TableView<Drzava> tabelaDrzava;
    public TableColumn<Drzava, Integer> idDrzava = new TableColumn<>();
    public TableColumn<Drzava, String> nazivDrzava = new TableColumn<>();
    @FXML
    Label rezultatPretrageLabela = new Label();
    @FXML
    TextField tfPretraga = new TextField();
    @FXML
    public ChoiceBox<String> cbJezici = new ChoiceBox<>();

    public BazaController( BazaModel bm ){
        choiceBoxHelper = "Bosnian";
        this.bm = bm;
        tabelaGradova = new TableView<>();
        tabelaDrzava = new TableView<>();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        cbJezici.setValue( choiceBoxHelper );
        //cbJezici.setValue("Bosnian");
        tabelaDrzava.setEditable( true );
        tabelaGradova.setEditable( true );
        idGrad.setCellValueFactory( new PropertyValueFactory<>("id"));
        nazivGrad.setCellValueFactory( new PropertyValueFactory<>("naziv"));
        brojStanovnikaGrad.setCellValueFactory( new PropertyValueFactory<>("brojStanovnika"));
        //drzavaGradaId.setCellValueFactory( new PropertyValueFactory<>("drzava"));
        idDrzava.setCellValueFactory( new PropertyValueFactory<>("id"));
        nazivDrzava.setCellValueFactory( new PropertyValueFactory<>("naziv"));
        //glavniGradDrzaveId.setCellValueFactory( new PropertyValueFactory<>("glavniGrad"));
        tabelaGradova.setItems( bm.getGradovi() );
        tabelaDrzava.setItems( bm.getDrzave() );

        tabelaGradova.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                bm.setTrenutniGrad(tabelaGradova.getSelectionModel().getSelectedItem());
            }
        });

        tabelaDrzava.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                bm.setTrenutnaDrzava(tabelaDrzava.getSelectionModel().getSelectedItem());
            }
        });

        tabelaGradova.setRowFactory(tr ->
        { TableRow<Grad> red = new TableRow<>();
            red.setOnMouseClicked(
                    event -> {
                        if( event.getClickCount() == 2 && (!red.isEmpty()) )
                            try{

                                runChange(null,"grad");
                            }
                            catch (Exception ignored){

                            }
                    }
            ); return red; } );

        tabelaDrzava.setRowFactory(tr ->
        { TableRow<Drzava> red = new TableRow<>();
            red.setOnMouseClicked(
                    event -> {
                        if( event.getClickCount() == 2 && (!red.isEmpty()) )
                            try{

                                runChange(null,"drzava");
                            }
                            catch (Exception ignored){

                            }
                    }
            ); return red; } );

        cbJezici.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
                    switch (newValue){
                        case "French":
                            Locale.setDefault( new Locale("fr","FR") );
                            choiceBoxHelper = "French";
                            try {
                                reload();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "Bosnian":
                            Locale.setDefault( new Locale("bs","BA") );
                            choiceBoxHelper = "Bosnian";
                            try {
                                reload();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "German":
                            Locale.setDefault( new Locale("de","DE") );
                            choiceBoxHelper = "German";
                            try {
                                reload();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "English":
                            Locale.setDefault(new Locale("en_US","ENG"));
                            choiceBoxHelper = "English";
                            try {
                                reload();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        default: break;
                    }
                });

    }

    public void clickDodajDrzavu(ActionEvent actionEvent) throws IOException {
        BazaModel bm = new BazaModel();
        bm.napuni();
        bm.setTrenutnaDrzava( this.bm.getTrenutnaDrzava() );
        bm.setTrenutniGrad( this.bm.getTrenutniGrad() );
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addercontroller.fxml"));
        adderController ac = new adderController(bm,"drzava");
        loader.setController(ac);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Dodajem drzavu");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
            //Kada zatvorimo prozor, vrsi se provjera validnosti unesene knjige.
            //Metoda dajValidnuKnjigu vraca null ukoliko je format neispravan.
            if (ac.dajValidnuDrzavu() != null) {
                g.dodajDrzavu( ac.dajValidnuDrzavu() );
                bm.addDrzava(ac.dajValidnuDrzavu());
            }
        });
    }

    public void clickDodajGrad(ActionEvent actionEvent) throws IOException {
        BazaModel bm = new BazaModel();
        bm.napuni();
        bm.setTrenutnaDrzava( this.bm.getTrenutnaDrzava() );
        bm.setTrenutniGrad( this.bm.getTrenutniGrad() );
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addercontroller.fxml"));
        adderController ac = new adderController(bm,"grad");
        loader.setController(ac);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Dodajem grad");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
            //Kada zatvorimo prozor, vrsi se provjera validnosti unesene knjige.
            //Metoda dajValidnuKnjigu vraca null ukoliko je format neispravan.
            if (ac.dajValidanGrad() != null) {
                g.dodajGrad( ac.dajValidanGrad() );
                bm.addGrad(ac.dajValidanGrad());
            }
        });
    }

    public void clickPretrazi( ActionEvent actionEvent ){
        String s = tfPretraga.getText();
        if( s.equals("") ){
            rezultatPretrageLabela.setText("Ukucaj nesto...");
            return;
        }
        String postavkaLabele = "";
        for( Grad g : bm.getGradovi() ){
            if( g.getNaziv().equals( s ) ){
                postavkaLabele += "ID: ";
                postavkaLabele += g.getId();
                postavkaLabele += "\nNaziv: ";
                postavkaLabele += g.getNaziv();
                postavkaLabele += "\nBroj stanovnika: ";
                postavkaLabele += g.getBrojStanovnika();
                break;
            }
        }
        if( !(postavkaLabele.equals("")) ){
            rezultatPretrageLabela.setText( postavkaLabele );
            tfPretraga.setText("");
            return;
        }
        for( Drzava d: bm.getDrzave() ){
            if( d.getNaziv().equals( s ) ){
                postavkaLabele += "ID: ";
                postavkaLabele += d.getId();
                postavkaLabele += "\nNaziv: ";
                postavkaLabele += d.getNaziv();
                break;
            }
        }
        if( !(postavkaLabele.equals("")) ){
            rezultatPretrageLabela.setText( postavkaLabele );
            tfPretraga.setText("");
            return;
        }
        tfPretraga.setText("");
        rezultatPretrageLabela.setText("Nema rezultata!");
        System.out.println( cbJezici );
    }

    public void clickObrisiDrzavu( ActionEvent actionEvent ){
        if( bm.getTrenutnaDrzava() == null ) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Brisem drzavu...");
        a.setHeaderText("Da li ste sigurni da zelite obrisati drzavu " + bm.getTrenutnaDrzava().getNaziv() + "?");
        Optional<ButtonType> result = a.showAndWait();
        if( result.get() == ButtonType.OK ) {
            g.obrisiDrzavu(bm.getTrenutnaDrzava().getNaziv());
            bm.setTrenutnaDrzava(null);
        }
        else bm.setTrenutnaDrzava(null);
    }

    public void clickObrisiGrad( ActionEvent actionEvent ){
        if( bm.getTrenutniGrad() == null ) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Brisem grad...");
        a.setHeaderText("Da li ste sigurni da zelite obrisati grad " + bm.getTrenutniGrad().getNaziv() + "?");
        Optional<ButtonType> result = a.showAndWait();
        if( result.get() == ButtonType.OK ) {
            g.obrisiGrad(bm.getTrenutniGrad().getNaziv());
            bm.setTrenutniGrad(null);
        }
        else bm.setTrenutniGrad(null);
    }

    public void clickRefreshMe( ActionEvent actionEvent ){
        ArrayList<Grad> tempGradovi = g.gradovi();
        ArrayList<Drzava> tempDrzave = g.drzave();
        bm.getGradovi().setAll( tempGradovi );
        bm.getDrzave().setAll( tempDrzave );
        tabelaGradova.setItems( bm.getGradovi() );
        tabelaDrzava.setItems( bm.getDrzave() );

    }

    public void runChange( ActionEvent actionEvent, String helpMeOut ) throws IOException {
        BazaModel bm = new BazaModel();
        bm.napuni();
        bm.setTrenutnaDrzava( this.bm.getTrenutnaDrzava() );
        bm.setTrenutniGrad( this.bm.getTrenutniGrad() );
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addercontroller.fxml"));
        adderController ac = new adderController(bm,helpMeOut );
        loader.setController(ac);
        Parent root = loader.load();
        Stage primaryStage = new Stage();
        if( helpMeOut.equals("grad") ) {
            primaryStage.setTitle("Mijenjam grad");
            ac.setNazivTf( bm.getTrenutniGrad().getNaziv() );
            ac.setMagicniTfJedan( bm.getTrenutniGrad().getBrojStanovnika(),"grad" );
            ac.setMagicniTfDva();

        }
        if( helpMeOut.equals("drzava") ) {
            primaryStage.setTitle("Mijenjam drzavu");
            ac.setNazivTf( bm.getTrenutnaDrzava().getNaziv() );
            ac.setMagicniTfJedan( 0, "drzava" );
            ac.setMagicniTfDva();

        }
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent event1) -> {
            ac.provjeriPolje(helpMeOut);
            if( ac.provjeriPolje( helpMeOut ) ){
                if( helpMeOut.equals("grad") ){
                    System.out.println( ac.getNazivTf() );
                    bm.getTrenutniGrad().setNaziv( ac.getNazivTf() );
                    bm.getTrenutniGrad().setBrojStanovnika( Integer.parseInt( ac.getMagicniTfJedan() ) );
                    g.izmijeniGrad( bm.getTrenutniGrad() );
                    bm.setTrenutniGrad(null);
                }
                else if( helpMeOut.equals("drzava") ){
                    bm.getTrenutnaDrzava().setNaziv( ac.getNazivTf() );
                    g.izmijeniDrzavu( bm.getTrenutnaDrzava() );
                    bm.setTrenutnaDrzava(null);
                }
            }
        });
    }

    public void stampajGradove(ActionEvent actionEvent) {
        try {
            new PrintReport().showReport( g.getConnection());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }

    public void stampajGradoveDrzave(ActionEvent actionEvent) {
        try {
            if( bm.getTrenutnaDrzava() == null ) return;
            new PrintReport().showReportDrzava( g.getConnection() , bm.getTrenutnaDrzava());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }

    public void reload() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("Translation");
        Scene scene = tabelaDrzava.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bazacontroller.fxml"), bundle);
        loader.setController(this);
        try {
            scene.setRoot(loader.load());

        } catch (IOException ignored) {

        }
    }

    public void clickOnSaveAs( ActionEvent actionEvent ){
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter xslmExtenizija = new FileChooser.ExtensionFilter("XSLX", "*.xslx");
        fc.getExtensionFilters().add( xslmExtenizija );
        FileChooser.ExtensionFilter docxExtenzija = new FileChooser.ExtensionFilter("DOCX", "*.docx");
        fc.getExtensionFilters().add( docxExtenzija );
        FileChooser.ExtensionFilter pdfExtenzija = new FileChooser.ExtensionFilter("PDF", "*.pdf");
        fc.getExtensionFilters().add( pdfExtenzija );
        fc.setTitle("Saving a file");
        File selectedFile = fc.showSaveDialog(null);

        //Ako ne odaberemo nista, nista se i ne desi.
        if (selectedFile != null)
            doSave(selectedFile);
    }


    private void doSave(File datoteka) {
        try {
            new PrintReport().save(datoteka.getAbsolutePath(), g.getConnection());
        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
    }

}
