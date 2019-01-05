package ba.unsa.rpr.tutorijal10;

import ba.unsa.rpr.tutorijal10.Main;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @org.junit.jupiter.api.Test
    void ispisiGradove() {
        String result = Main.ispisiGradove();
        String expected = "London (Velika Britanija) - 8825000\n" +
                "Pariz (Francuska) - 2206488\n" +
                "Beč (Austrija) - 1899055\n" +
                "Manchester (Velika Britanija) - 545500\n" +
                "Graz (Austrija) - 280200\n";
        assertEquals(expected, result);
    }
}