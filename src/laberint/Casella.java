package laberint;

/**
  AUTOR: Miguel Vicente Robles Mclean
  ENLLAÇ AL VIDEO EXPLICATIU:
  Youtube : https://www.youtube.com/watch?v=so4lj9Hkuws 
  OneDrive : https://bit.ly/35sduN6
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/* CLASSE Casella:
    Classe que s'encarrega de crear i pintar una casella instanciada a la classe laberint.
    També ens diu si la casella está ocupada per una fitxa i ens dona la informació
    de les parets que té la casella
 */
public class Casella {

    // CONSTANTS
    private final Rectangle2D.Float rec;
    private final char[] lados = new char[4];
    private final Color colorParet = new Color(153, 99, 0);

    // VARIABLES
    private Color colorFondo = new Color(255, 201, 12);
    private Boolean ocupada;
    private Boolean fin = false;
    private Fitxa fitxa;

    /* Métode Constructor:
        Assigna els arguments passats per parámetre a les variables que té una casella.
        A més a més, copia l'array de parets a un altre array intern i posa la fitxa de 
        la casella a null.
     */
    public Casella(Rectangle2D.Float r, Boolean ocu, char[] parets) {
        this.rec = r;
        this.ocupada = ocu;
        System.arraycopy(parets, 0, lados, 0, parets.length);
        this.fitxa = null;
    }

    /* Métode paintComponent:
        S'encarrega de pintar una casella i les seves parets, segons les parets que tengui.
        També pinta la casella final.
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Emplenam una casella amb el color de fons
        g2d.setColor(colorFondo);
        g2d.fill(this.rec);

        // Assignam el color de dibuixat al color d'una paret
        g2d.setPaint(colorParet);

        // Si la casella té una paret al nord, la dibuixam
        if (getNord()) {
            // Cream un rectangle que farà de paret nord i el pintam
            Rectangle2D.Float r = new Rectangle2D.Float((int) this.rec.x, (int) this.rec.y, 35, 3);
            g2d.fill(r);
            g2d.draw(r);
        }

        // Si la casella té una paret a l'est, la dibuixam
        if (getEst()) {
            // Cream un rectangle que farà de paret est i el pintam
            Rectangle2D.Float r = new Rectangle2D.Float((int) (this.rec.x + 32), (int) this.rec.y, 3, 35);
            g2d.fill(r);
            g2d.draw(r);
        }

        // Si la casella té una paret al sud, la dibuixam
        if (getSud()) {
            // Cream un rectangle que farà de paret sud i el pintam
            Rectangle2D.Float r = new Rectangle2D.Float((int) this.rec.x, (int) (this.rec.y + 32), 35, 3);
            g2d.fill(r);
            g2d.draw(r);
        }

        // Si la casella té una paret a l'oest, la dibuixam
        if (getOest()) {
            // Cream un rectangle que farà de paret oest i el pintam
            Rectangle2D.Float r = new Rectangle2D.Float((int) this.rec.x, (int) this.rec.y, 3, 35);
            g2d.fill(r);
            g2d.draw(r);
        }

        // Si la casella está ocupada per una fitxa, pintam aquesta fitxa
        if (this.ocupada) {
            this.fitxa.paintComponent(g, this.rec.x, this.rec.y);
        }

        // Si la casella es la de sortida, dibuixam la imatge que indica la sortida
        // mitjançant un BufferedImage
        if (fin) {
            try {
                BufferedImage imagenFichero = ImageIO.read(new File("exit.png"));
                g2d.drawImage(imagenFichero, (int) (this.rec.x + 2), (int) (this.rec.y + 2), 33, 33, null);
            } catch (IOException ex) {
                System.out.println("ERROR d'I/O: No s'ha pogut dibuixar l'icona"
                        + " de sortida!!");
            }
        }
    }

    /* Métode getRec:
        Retorna el rectangle de la casella.
     */
    public Rectangle2D.Float getRec() {
        return rec;
    }

    /* Métode isOcupada:
        Retorna true si la casella está ocupada per una fitxa.
     */
    public boolean isOcupada() {
        return ocupada;
    }

    /* Métode getNord:
        Retorna true si la casella té una paret al nord.
     */
    public boolean getNord() {
        return this.lados[0] == '1';
    }

    /* Métode getEst:
        Retorna true si la casella té una paret a l'est.
     */
    public boolean getEst() {
        return this.lados[1] == '1';
    }

    /* Métode getSud:
        Retorna true si la casella té una paret al sud.
     */
    public boolean getSud() {
        return this.lados[2] == '1';
    }

    /* Métode getOest:
        Retorna true si la casella té una paret a l'oest.
     */
    public boolean getOest() {
        return this.lados[3] == '1';
    }

    /* Métode setFitxa:
        Marca com a ocupada las casella i li asigna una fitxa passada per paràmetre
     */
    public void setFitxa(Fitxa f) {
        this.ocupada = true;
        this.fitxa = f;
    }

    /* Métode setFi:
        Marca una casella com a casella de sortida. També posa el color de fons de color
        blanc, perquè es vegi millor la imatge de sortida.
     */
    public void setFi() {
        fin = true;
        colorFondo = Color.WHITE;
    }

    /* Métode setOcupada:
        Assigna un booleà passat per paràmetre a la variable ocupada, que indica si 
        la casella té una fitxa o no.
     */
    public void setOcupada(boolean b) {
        ocupada = b;
    }

}
