package ba.unsa.rpr.tutorijal10;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class adderController implements Initializable {

    GeografijaDAO g = GeografijaDAO.getInstance();
    private BazaModel bm;
    public Label nazivLabela = new Label();
    public Label magicnaLabelaJedan = new Label();
    public Label magicnaLabelaDva = new Label();
    public TextField nazivTf = new TextField();
    public TextField magicniTfJedan = new TextField();
    public TextField magicniTfDva = new TextField() ;
    private String magicnaLabelaJedanText = "";
    public void setNazivTf(String nazivTfText) {
        nazivTf.setText( nazivTfText );
    }

    public void setMagicniTfJedan( int magicniTfJedanText,String test ) {
        if( test.equals("drzava") ){
            magicniTfJedan.setVisible(false);
            magicnaLabelaJedan.setText("");
            return;
        }

        magicniTfJedan.setText( String.valueOf( magicniTfJedanText ) );
    }

    public void setMagicniTfDva() {
        magicnaLabelaDva.setText("");
        magicniTfDva.setVisible(false);
    }



    public adderController(BazaModel bm, String magicnaLabelaJedanText ){
        this.bm = bm;
        this.magicnaLabelaJedanText = magicnaLabelaJedanText;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

        if( magicnaLabelaJedanText.equals("grad") ){
            magicnaLabelaJedan.setText("Broj stanovnika");
            magicnaLabelaDva.setText("Drzava");
            magicniTfDva.setVisible(true);
        }
        else if( magicnaLabelaJedanText.equals("drzava") ){
            magicnaLabelaJedan.setText("Glavni grad");
            magicnaLabelaDva.setText("");
            magicniTfDva.setVisible(false);
        }

        nazivTf.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.length() == 0) {
                nazivTf.getStyleClass().removeAll("poljeIspravno", "poljeNijeIspravno", "poljeNeutralno");
                nazivTf.getStyleClass().add("poljeNeutralno");
            } else if ( ispravnostNaziva(newValue) ) {
                nazivTf.getStyleClass().removeAll("poljeNeutralno", "poljeNijeIspravno", "poljeIspravno");
                nazivTf.getStyleClass().add("poljeIspravno");
            } else if ( !ispravnostNaziva(newValue) ) {
                nazivTf.getStyleClass().removeAll("poljeNeutralno", "poljeIspravno", "poljeNijeIspravno");
                nazivTf.getStyleClass().add("poljeNijeIspravno");
            }
        });

        magicniTfJedan.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.length() == 0) {
                magicniTfJedan.getStyleClass().removeAll("poljeIspravno", "poljeNijeIspravno", "poljeNeutralno");
                magicniTfJedan.getStyleClass().add("poljeNeutralno");
            } else if ( ispravnostMagicneLabeleJedan(newValue) ) {
                magicniTfJedan.getStyleClass().removeAll("poljeNeutralno", "poljeNijeIspravno", "poljeIspravno");
                magicniTfJedan.getStyleClass().add("poljeIspravno");
            } else if ( !ispravnostMagicneLabeleJedan(newValue) ) {
                magicniTfJedan.getStyleClass().removeAll("poljeNeutralno", "poljeIspravno", "poljeNijeIspravno");
                magicniTfJedan.getStyleClass().add("poljeNijeIspravno");
            }
        });

        magicniTfDva.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue.length() == 0) {
                magicniTfDva.getStyleClass().removeAll("poljeIspravno", "poljeNijeIspravno", "poljeNeutralno");
                magicniTfDva.getStyleClass().add("poljeNeutralno");
            } else if ( ispravnostNaziva(newValue) ) {
                magicniTfDva.getStyleClass().removeAll("poljeNeutralno", "poljeNijeIspravno", "poljeIspravno");
                magicniTfDva.getStyleClass().add("poljeIspravno");
            } else if ( !ispravnostNaziva(newValue) ) {
                magicniTfDva.getStyleClass().removeAll("poljeNeutralno", "poljeIspravno", "poljeNijeIspravno");
                magicniTfDva.getStyleClass().add("poljeNijeIspravno");
            }
        });

    }

    private boolean ispravnostMagicneLabeleJedan(String newValue) {
        if( magicnaLabelaJedan.getText().equals("Broj stanovnika") ){
            Integer brojStanovnika;
            try{
                brojStanovnika = Integer.parseInt( newValue );
                if( brojStanovnika < 0 ) return false;
            }catch ( Exception error ){
                return false;
            }
            return true;
        }
        else if( magicnaLabelaJedan.getText().equals("Glavni grad") )
            return ispravnostNaziva( newValue );

        return false;
    }

    private boolean ispravnostNaziva(String newValue) {
        for( int i = 0; i < newValue.length(); i++ )
            if( !( Character.isAlphabetic( newValue.charAt(i) ) ) && newValue.charAt(i) != ' ' ) return false;
        return true;
    }

    public Drzava dajValidnuDrzavu(){
        Drzava d = new Drzava();
        if( magicnaLabelaJedan.getText().equals("Glavni grad") ){

            if( nazivTf.getStyleClass().contains("poljeNijeIspravno") || nazivTf.getStyleClass().contains("poljeNeutralno") ) return null;
            if( magicniTfJedan.getStyleClass().contains("poljeNijeIspravno") || magicniTfJedan.getStyleClass().contains("poljeNeutralno") ) return null;
            d.setId( g.dajSljedeciIndeks("drzava") );
            d.setNaziv( nazivTf.getText() );
            Grad g = new Grad();
            g.setNaziv( magicniTfJedan.getText() );
            d.setGlavniGrad( g );
            return d;
        }
        return null;
    }

    public Grad dajValidanGrad(){
        Grad grad = new Grad();
        if( magicnaLabelaJedan.getText().equals("Broj stanovnika") ){
            if( nazivTf.getStyleClass().contains("poljeNijeIspravno") || nazivTf.getStyleClass().contains("poljeNeutralno") ) return null;
            if( magicniTfJedan.getStyleClass().contains("poljeNijeIspravno") || magicniTfJedan.getStyleClass().contains("poljeNeutralno") ) return null;
            if( magicniTfDva.getStyleClass().contains("poljeNijeIspravno") || magicniTfDva.getStyleClass().contains("poljeNeutralno") ) return null;
            grad.setId( g.dajSljedeciIndeks("grad") );
            grad.setNaziv( nazivTf.getText() );
            grad.setBrojStanovnika( Integer.parseInt( magicniTfJedan.getText() ) );
            Drzava d = new Drzava();
            d.setNaziv( magicniTfDva.getText() );
            grad.setDrzava( d );
            return grad;
        }
        return null;
    }

    public boolean provjeriPolje( String s ){
        if( s.equals("grad") ){
            if( nazivTf.getStyleClass().contains("poljeNijeIspravno") || nazivTf.getStyleClass().contains("poljeNeutralno") ) return false;
            if( magicniTfJedan.getStyleClass().contains("poljeNijeIspravno") || magicniTfJedan.getStyleClass().contains("poljeNeutralno") ) return false;
            return true;
        }
        else if( s.equals("drzava") ) {
            if (nazivTf.getStyleClass().contains("poljeNijeIspravno") || nazivTf.getStyleClass().contains("poljeNeutralno")) return false;
            return true;

        }
        return false;
    }

    public String getNazivTf() {
        return nazivTf.getText();
    }


    public String getMagicniTfJedan() {
        return magicniTfJedan.getText();
    }
}
