package ba.unsa.rpr.tutorijal10;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {


    private static GeografijaDAO instance = null;
    private static Connection connection;
    private PreparedStatement mojUpit;
    private ArrayList<Grad> gradovi = new ArrayList<>();
    private ArrayList<Drzava> drzave = new ArrayList<>();


    private GeografijaDAO(){

        try {
            String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/baza.db";
            connection = DriverManager.getConnection( url );
            mojUpit = connection.prepareStatement("PRAGMA foreign_keys = ON");
            mojUpit.executeUpdate();

            try {
                mojUpit = connection.prepareStatement("SELECT 'x' FROM grad");
                mojUpit.executeQuery();
                mojUpit = connection.prepareStatement("SELECT 'x' FROM drzava");
                mojUpit.executeQuery();

            }
            catch (SQLException e){
                kreirajTabele();
                napuniTabelu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initialize() {
        instance = new GeografijaDAO();

    }

    public static GeografijaDAO getInstance() {
        if (instance == null) initialize();
        return instance;
    }

    public static void removeInstance() {
        if( connection != null ){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> grads = new ArrayList<>();
        ArrayList<Drzava> drzavas = new ArrayList<>();
        try {
            mojUpit = connection.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
            ResultSet resultSet = mojUpit.executeQuery();
            while( resultSet.next() )
                grads.add( new Grad( resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), new Drzava( resultSet.getInt(4), "", null ) ) );
            mojUpit = connection.prepareStatement( "SELECT * FROM drzava" );
            resultSet = mojUpit.executeQuery();
            while( resultSet.next() ) {
                Drzava tempDrzava = new Drzava( resultSet.getInt(1), resultSet.getString(2), null );
                for( int i = 0; i < grads.size(); i++ )
                    if( resultSet.getInt(3) == grads.get(i).getId() ){
                        tempDrzava.setGlavniGrad( grads.get(i) );
                        break;
                    }
                drzavas.add( tempDrzava );
            }
            for( int i = 0; i < grads.size(); i++ )
                for( int j = 0; j < drzavas.size(); j++ )
                    if( grads.get(i).getDrzava().getId() == drzavas.get(j).getId() )
                        grads.get(i).setDrzava( drzavas.get(j) );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return grads;
    }

    public ArrayList<Drzava> drzave() {
        ArrayList<Grad> grads = new ArrayList<>();
        ArrayList<Drzava> drzavas = new ArrayList<>();
        try {
            mojUpit = connection.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
            ResultSet resultSet = mojUpit.executeQuery();
            while( resultSet.next() )
                grads.add( new Grad( resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3), new Drzava( resultSet.getInt(4), "", null ) ) );
            mojUpit = connection.prepareStatement( "SELECT * FROM drzava" );
            resultSet = mojUpit.executeQuery();
            while( resultSet.next() ) {
                Drzava tempDrzava = new Drzava( resultSet.getInt(1), resultSet.getString(2), null );
                for( int i = 0; i < grads.size(); i++ )
                    if( resultSet.getInt(3) == grads.get(i).getId() ){
                        tempDrzava.setGlavniGrad( grads.get(i) );
                        break;
                    }
                drzavas.add( tempDrzava );
            }
            for( int i = 0; i < grads.size(); i++ )
                for( int j = 0; j < drzavas.size(); j++ )
                    if( grads.get(i).getDrzava().getId() == drzavas.get(j).getId() )
                        grads.get(i).setDrzava( drzavas.get(j) );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drzavas;
    }

    public void obrisiDrzavu( String drzava ){
        try {
            mojUpit = connection.prepareStatement("DELETE FROM drzava WHERE naziv = ?");
            mojUpit.setString( 1, drzava );
            mojUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void obrisiGrad( String grad ){
        try {
            mojUpit = connection.prepareStatement("DELETE FROM grad WHERE naziv = ?");
            mojUpit.setString( 1, grad );
            mojUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dodajGrad( Grad grad ){
        try {
            //Provjeravamo da li grad vec postoji.
            int gradId = dajSljedeciIndeks("grad");
            mojUpit = connection.prepareStatement("SELECT * FROM grad WHERE naziv = ?");
            mojUpit.setString( 1, grad.getNaziv() );
            ResultSet resultSet = mojUpit.executeQuery();
            int rows = 0;
            while( resultSet.next() )
                rows++;
            if( rows != 0 ) return;

            //Provjeravamo ima li drzave poslanog grada
            mojUpit = connection.prepareStatement("SELECT id FROM drzava WHERE naziv = ?");
            mojUpit.setString( 1, grad.getDrzava().getNaziv() );
            resultSet = mojUpit.executeQuery();
            int drzavaGlavnogGrada = -1;
            rows = 0;
            while( resultSet.next() ){
                drzavaGlavnogGrada = resultSet.getInt(1);
                rows++;
            }
            if( rows == 0 ){
                drzavaGlavnogGrada = dajSljedeciIndeks("drzava");
                mojUpit = connection.prepareStatement("INSERT INTO drzava VALUES ( ? , ?, NULL)");
                mojUpit.setInt( 1, drzavaGlavnogGrada );
                mojUpit.setString( 2, grad.getDrzava().getNaziv() );
                mojUpit.executeUpdate();
            }

            mojUpit = connection.prepareStatement("INSERT INTO grad VALUES (?, ?, ?, ?)");
            mojUpit.setInt( 1, gradId );
            mojUpit.setString( 2, grad.getNaziv() );
            mojUpit.setInt( 3, grad.getBrojStanovnika() );
            mojUpit.setInt( 4, drzavaGlavnogGrada );
            mojUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int dajSljedeciIndeks( String s ){
        try {
            mojUpit = connection.prepareStatement("SELECT id FROM " + s + " ORDER BY id DESC LIMIT 1");
            ResultSet resultSet = mojUpit.executeQuery();
            int sljedeciIndeks = -1;
            while( resultSet.next() )
                sljedeciIndeks = resultSet.getInt(1);
            return ++sljedeciIndeks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -999;
    }

    public void dodajDrzavu( Drzava drzava ){
        try {
            //Provjeravamo da li drzava vec postoji.
            int drzavaId = dajSljedeciIndeks("drzava");
            mojUpit = connection.prepareStatement("SELECT * FROM drzava WHERE naziv = ?");
            mojUpit.setString( 1, drzava.getNaziv() );
            ResultSet resultSet = mojUpit.executeQuery();
            int rows = 0;
            while( resultSet.next() )
                rows++;
            if( rows != 0 ) return;

            //Provjeravamo ima li glavnog grada poslane drzave
            mojUpit = connection.prepareStatement("SELECT id FROM grad WHERE naziv = ?");
            mojUpit.setString( 1, drzava.getGlavniGrad().getNaziv() );
            resultSet = mojUpit.executeQuery();
            int glavniGradId = -1;
            rows = 0;
            while( resultSet.next() ){
                glavniGradId = resultSet.getInt(1);
                rows++;
            }
            if( rows == 0 ){
                glavniGradId = dajSljedeciIndeks("grad");
                mojUpit = connection.prepareStatement("INSERT INTO grad VALUES ( ? , ?, ?, NULL)");
                mojUpit.setInt( 1, glavniGradId );
                mojUpit.setString( 2, drzava.getGlavniGrad().getNaziv() );
                mojUpit.setInt( 3, drzava.getGlavniGrad().getBrojStanovnika() );
                mojUpit.executeUpdate();
            }

            mojUpit = connection.prepareStatement("INSERT INTO drzava VALUES (?, ?, ?)");
            mojUpit.setInt( 1, drzavaId );
            mojUpit.setString( 2, drzava.getNaziv() );
            mojUpit.setInt( 3, glavniGradId );
            mojUpit.executeUpdate();

            mojUpit = connection.prepareStatement("UPDATE grad SET drzava = ? WHERE id = ?");
            mojUpit.setInt( 1, drzavaId );
            mojUpit.setInt( 2, glavniGradId );
            mojUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(  String drzava ){
        try {
            mojUpit = connection.prepareStatement("SELECT * FROM drzava WHERE naziv=?" );
            mojUpit.setString( 1, drzava );
            ResultSet resultSet = mojUpit.executeQuery();
            int rows = 0;
            Drzava povratnaDrzava = new Drzava( -1, "", null );
            int glavniGradId = -1;
            while( resultSet.next() ){
                povratnaDrzava.setId( resultSet.getInt(1 ) );
                povratnaDrzava.setNaziv( resultSet.getString(2 ) );
                glavniGradId = resultSet.getInt(3);
                rows++;
            }
            if( rows == 0 ) return null;
            mojUpit = connection.prepareStatement("SELECT * FROM grad WHERE id=?");
            mojUpit.setInt( 1, glavniGradId );
            resultSet = mojUpit.executeQuery();
            while( resultSet.next() )
                povratnaDrzava.setGlavniGrad( new Grad( glavniGradId, resultSet.getString(2), resultSet.getInt(3), povratnaDrzava ) );

            return povratnaDrzava;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void izmijeniGrad( Grad grad ){
        try {
            mojUpit = connection.prepareStatement("SELECT * FROM grad WHERE id=?");
            mojUpit.setInt( 1, grad.getId() );
            ResultSet resultSet = mojUpit.executeQuery();
            int rows = 0;
            while( resultSet.next() )
                rows++;
            if( rows == 0 ) return;
            mojUpit = connection.prepareStatement("UPDATE grad SET naziv=?,broj_stanovnika=? WHERE id=?");
            mojUpit.setString( 1, grad.getNaziv() );
            mojUpit.setInt( 2, grad.getBrojStanovnika() );
            mojUpit.setInt( 3, grad.getId() );
            mojUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void izmijeniDrzavu( Drzava d ){
        try {
            mojUpit = connection.prepareStatement("SELECT * FROM drzava WHERE id=?");
            mojUpit.setInt( 1, d.getId() );
            ResultSet resultSet = mojUpit.executeQuery();
            int rows = 0;
            while( resultSet.next() )
                rows++;
            if( rows == 0 ) return;
            mojUpit = connection.prepareStatement("UPDATE drzava SET naziv=? WHERE id=?");
            mojUpit.setString( 1, d.getNaziv() );
            mojUpit.setInt( 2, d.getId() );
            mojUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Grad glavniGrad( String drzava ){
        try {
            mojUpit = connection.prepareStatement("SELECT * FROM drzava WHERE naziv=?");
            mojUpit.setString( 1, drzava );
            ResultSet resultSet = mojUpit.executeQuery();
            int rows = 0;
            Drzava tempDrzava = new Drzava( -1, "", null );
            int glavniGradId = -1;
            while( resultSet.next() ){
                tempDrzava.setId( resultSet.getInt(1) );
                tempDrzava.setNaziv( resultSet.getString(2) );
                glavniGradId = resultSet.getInt(3);
                rows++;
            }
            if( rows == 0 ) return null;
            mojUpit = connection.prepareStatement("SELECT * FROM grad WHERE id=?");
            mojUpit.setInt( 1, glavniGradId );
            resultSet = mojUpit.executeQuery();
            Grad g = new Grad(-1,"",-1, tempDrzava );
            while( resultSet.next() ){
                g.setId( resultSet.getInt(1) );
                g.setNaziv( resultSet.getString(2) );
                g.setBrojStanovnika( resultSet.getInt(3) );

            }
            return g;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void napuniPodacima(){
        Grad g1 = new Grad( 1,"Pariz",2206488,null );
        Grad g2 = new Grad( 2,"London",8825000,null );
        Grad g3 = new Grad( 3,"Beč",1899055,null );
        Grad g4 = new Grad( 4,"Manchester",545500,null );
        Grad g5 = new Grad( 5,"Graz",280200,null );

        Drzava d1 = new Drzava( 1,"Francuska", g1 );
        Drzava d2 = new Drzava( 2,"Velika Britanija", g2 );
        Drzava d3 = new Drzava( 3,"Austrija", g3 );

        g1.setDrzava( d1 );
        g2.setDrzava( d2 );
        g3.setDrzava( d3 );
        g4.setDrzava( d2 );
        g5.setDrzava( d3 );

        gradovi.add( g1 );
        gradovi.add( g2 );
        gradovi.add( g3 );
        gradovi.add( g4 );
        gradovi.add( g5 );


        drzave.add( d1 );
        drzave.add( d2 );
        drzave.add( d3 );
    }

    public void kreirajTabele() {
        Statement statement= null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            statement.execute(new String(Files.readAllBytes(Paths.get("src/kreiranjeTabela.sql")), StandardCharsets.UTF_8));
            statement.execute(new String(Files.readAllBytes(Paths.get("src/kreirajTabelaDrzava.sql")), StandardCharsets.UTF_8));
            statement.execute(new String(Files.readAllBytes(Paths.get("src/alter.sql")), StandardCharsets.UTF_8));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void napuniTabelu(){
        try {
            napuniPodacima();
            mojUpit = connection.prepareStatement("INSERT INTO grad VALUES (?,?,?,NULL)");
            for (Grad aGradovi : gradovi) {
                mojUpit.setInt(1, aGradovi.getId());
                mojUpit.setString(2, aGradovi.getNaziv());
                mojUpit.setInt(3, aGradovi.getBrojStanovnika());
                mojUpit.executeUpdate();
            }

            mojUpit = connection.prepareStatement("INSERT INTO drzava VALUES (?,?,NULL)");
            for (int i = 0; i < drzave.size(); i++) {
                mojUpit.setInt(1, drzave.get(i).getId());
                mojUpit.setString(2, drzave.get(i).getNaziv());
                mojUpit.executeUpdate();
            }

            mojUpit = connection.prepareStatement("UPDATE grad SET drzava=? WHERE id=?");
            mojUpit.setInt(1, 1);
            mojUpit.setInt(2, 1);
            mojUpit.executeUpdate();
            mojUpit.setInt(1, 2);
            mojUpit.setInt(2, 2);
            mojUpit.executeUpdate();
            mojUpit.setInt(1, 3);
            mojUpit.setInt(2, 3);
            mojUpit.executeUpdate();
            mojUpit.setInt(1, 2);
            mojUpit.setInt(2, 4);
            mojUpit.executeUpdate();
            mojUpit.setInt(1, 3);
            mojUpit.setInt(2, 5);
            mojUpit.executeUpdate();


            mojUpit = connection.prepareStatement("UPDATE drzava SET glavni_grad=? WHERE id=?");
            mojUpit.setInt(1, 1);
            mojUpit.setInt(2, 1);
            mojUpit.executeUpdate();
            mojUpit.setInt(1, 2);
            mojUpit.setInt(2, 2);
            mojUpit.executeUpdate();
            mojUpit.setInt(1, 3);
            mojUpit.setInt(2, 3);
            mojUpit.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }


    }

    public Connection getConnection() {
        return connection;
    }
}
