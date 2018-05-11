package johnario;

import javax.swing.JFrame;

/**
 * Třída obsahující hlavní okno programu a metodu main.
 * @author vita
 */
public class Johnario extends JFrame {
    
    /**
     * Konstruktor hlavního okna programu.
     */
    public Johnario() {
        this.setTitle("Johnario v0.1");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Obrazovka panel = new Obrazovka();
        this.add(panel);
        this.setResizable(false);
        this.pack();
    }
    
     /**
      * Vstupní bod programu. 
      * Vytvoří hlavní okno s programem.
      * @param args - v tomto programu nepoužívám
      */
    public static void main(String[] args) {
        Johnario hlavniOkno = new Johnario();
        hlavniOkno.setVisible(true);
    }
}
