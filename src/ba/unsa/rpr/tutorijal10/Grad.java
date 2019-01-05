package ba.unsa.rpr.tutorijal10;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

public class Grad {

    private int id;
    private String naziv;
    private int brojStanovnika;
    private Drzava drzava;

//    private SimpleIntegerProperty idGrad = new SimpleIntegerProperty();
//    private SimpleStringProperty nazivGrad = new SimpleStringProperty();
//    private SimpleIntegerProperty brojStanovnikaGrad = new SimpleIntegerProperty();
//    private SimpleObjectProperty<Drzava> drzavaGradaId = new SimpleObjectProperty<>();

    public Grad(){}

    public Grad( int id, String naziv, int brojStanovnika,Drzava drzava ){
        this.id = id;
        this.naziv = naziv;
        this.brojStanovnika = brojStanovnika;
        this.drzava = drzava;
//        idGrad.set( id );
//        nazivGrad.set( naziv );
//        brojStanovnikaGrad.set( brojStanovnika );
//        drzavaGradaId.set( drzava );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
//        idGrad.set( id );
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
//        nazivGrad.set( naziv );
    }

    public int getBrojStanovnika() {
        return brojStanovnika;
    }

    public void setBrojStanovnika(int brojStanovnika) {
        this.brojStanovnika = brojStanovnika;
//        brojStanovnikaGrad.set( brojStanovnika );
    }

    public Drzava getDrzava() {
        return drzava;
    }

    public void setDrzava(Drzava drzava) {
        this.drzava = drzava;
//        drzavaGradaId.set( drzava );
    }

//    public SimpleIntegerProperty idGradProperty(){
//        return idGrad;
//    }
//    public SimpleStringProperty nazivGradaProperty(){
//        return nazivGrad;
//    }
//    public SimpleIntegerProperty brojStanovnikaGradProperty(){
//        return brojStanovnikaGrad;
//    }
//    public SimpleObjectProperty<Drzava> drzavaGradaIdProperty(){
//        return drzavaGradaId;
//    }


}
