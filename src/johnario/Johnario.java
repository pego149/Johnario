package johnario;

import javax.swing.JFrame;

/**
 * Hlavna trieda
 * @author pego1
 */
public class Johnario extends JFrame {
    
    /**
     * Konstruktor hlavneho okna programu.
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
      * Vstupny bod programu. 
      * Vytvorí okno s programom.
      * @param args - nepouzite
      */
    public static void main(String[] args) {
        Johnario hlavniOkno = new Johnario();
        hlavniOkno.setVisible(true);
    }
}
