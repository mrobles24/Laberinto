package laberint;

/**
  AUTOR: Miguel Vicente Robles Mclean
  ENLLAÇ AL VIDEO EXPLICATIU:
  Youtube : https://www.youtube.com/watch?v=so4lj9Hkuws 
  OneDrive : https://bit.ly/35sduN6
 */

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/* CLASSE Fitxa:
    Classe que s'encarrega de crear una fitxa a partir d'un fitxer fixe i també el 
    pinta.
 */
public class Fitxa {

    // CONSTANT que ens diu quin fitxer de imatge usarem per pintar la fitxa
    public static final String FITXA = "cercle.png";

    // VARIABLE que ens servirà per dibuixar la imatge
    private BufferedImage img;

    /* Métode Constructor:
        Assigna un fitxer a un BufferedImage mitjançant la operació
        ImageIO.read del fitxer fixe.
     */
    public Fitxa() {
        try {
            img = ImageIO.read(new File(FITXA));
        } catch (IOException e) {
            System.out.println("ERROR d'I/O: No s'ha pogut dibuixar la fitxa!!");
        }
    }

    /* Métode paintComponent:
        Dibuixa la imatge donades les coordenades de la casella on s'ha de pintar.
        Cal dir que el métode drawImage usat usa els paràmetres:
        - X incial
        - Y inicial
        - Amplada
        - Alçada
     */
    public void paintComponent(Graphics g, float x, float y) {
        g.drawImage(img, (int) x + 7, (int) y + 7, 21, 21, null);
    }
}
