package ba.unsa.rpr.tutorijal10;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BazaModel {

    private static GeografijaDAO g = GeografijaDAO.getInstance();
    private ObservableList<Grad> gradovi = FXCollections.observableArrayList();
    private ObservableList<Drzava> drzave = FXCollections.observableArrayList();
    private ObjectProperty<Grad> trenutniGrad = new SimpleObjectProperty<>();
    private ObjectProperty<Drzava> trenutnaDrzava = new SimpleObjectProperty<>();


    public void napuni(){
//        Grad g1 = new Grad( 1,"Pariz",2206488,null );
//        Grad g2 = new Grad( 2,"London",8825000,null );
//        Grad g3 = new Grad( 3,"Beč",1899055,null );
//        Grad g4 = new Grad( 4,"Manchester",545500,null );
//        Grad g5 = new Grad( 5,"Graz",280200,null );
//
//        Drzava d1 = new Drzava( 1,"Francuska", g1 );
//        Drzava d2 = new Drzava( 2,"Velika Britanija", g2 );
//        Drzava d3 = new Drzava( 3,"Austrija", g3 );
//
//        g1.setDrzava( d1 );
//        g2.setDrzava( d2 );
//        g3.setDrzava( d3 );
//        g4.setDrzava( d2 );
//        g5.setDrzava( d3 );
//
//        gradovi.add( g1 );
//        gradovi.add( g2 );
//        gradovi.add( g3 );
//        gradovi.add( g4 );
//        gradovi.add( g5 );
//
//
//        drzave.add( d1 );
//        drzave.add( d2 );
//        drzave.add( d3 );
        gradovi.setAll( g.gradovi() );
        drzave.setAll( g.drzave() );
    }

    public void addGrad( Grad g ){
        gradovi.add( g );
    }

    public void addDrzava( Drzava d ){
        drzave.add( d );
    }

    public void deleteGrad( Grad g ){

        for( int i = 0; i < gradovi.size(); i++ ){
            if( gradovi.get(i).equals( trenutniGrad.get())){
                gradovi.remove(i);
                setTrenutniGrad(null);
                break;
            }
        }
    }

    public void deleteDrzava( Drzava d ){

        for( int i = 0; i < drzave.size(); i++ ){
            if( drzave.get(i).equals( trenutnaDrzava.get())){
                drzave.remove(i);
                setTrenutniGrad(null);
                break;
            }
        }
    }

    public ObservableList<Grad> getGradovi() {
        return gradovi;
    }

    public void setGradovi(ObservableList<Grad> gradovi) {
        this.gradovi = gradovi;
    }

    public ObservableList<Drzava> getDrzave() {
        return drzave;
    }

    public void setDrzave(ObservableList<Drzava> drzave) {
        this.drzave = drzave;
    }

    public Grad getTrenutniGrad() {
        return trenutniGrad.get();
    }

    public ObjectProperty<Grad> trenutniGradProperty() {
        return trenutniGrad;
    }

    public void setTrenutniGrad(Grad trenutniGrad) {
        this.trenutniGrad.set(trenutniGrad);
    }

    public Drzava getTrenutnaDrzava() {
        return trenutnaDrzava.get();
    }

    public ObjectProperty<Drzava> trenutnaDrzavaProperty() {
        return trenutnaDrzava;
    }

    public void setTrenutnaDrzava(Drzava trenutnaDrzava) {
        this.trenutnaDrzava.set(trenutnaDrzava);
    }

}
