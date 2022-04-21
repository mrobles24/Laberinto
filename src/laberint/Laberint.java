package laberint;

/**
  AUTOR: Miguel Vicente Robles Mclean
  ENLLAÇ AL VIDEO EXPLICATIU:
  Youtube : https://www.youtube.com/watch?v=so4lj9Hkuws 
  OneDrive : https://bit.ly/35sduN6
 */

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JPanel;

/* CLASSE Laberint:
    Classe que s'encarrega de crear un laberint a partir d'un fitxer donat. El procediment
    seguit llegeix un fitxer per poder crear un tauler de joc segons les dades donades
    al fitxer. Per a cada casella llegida, es crea un objecte de la classe Casella i si es
    la casella final també ho indica.
 */
public class Laberint extends JPanel {

    //CONSTANTS
    public final int COSTAT = 35;   // Dimensió d'un costat en pixels

    //VARIABLES 
    public int FILES;   // Nombre de files totals
    public int COLUMNES;    // Nombre de columnes totals
    public int filaFI;  // Fila on es troba la casella de sortida
    public int columnaFI;   // Columna os es troba la casella de sortida
    public int filaFITXA;   // Fila on es troba la fitxa actualment
    public int columnaFITXA;    // Columna on es troba la fitxa actualment
    private char[] parets = new char[4];
    private Casella t[][];
    private FileInputStream entrada;
    private BufferedReader in;

    /* Métode Constructor:
        Crea un nou BufferedReader des d'un FileInputStream el cual té com a argument el 
        nom del fitxer a llegir passat per paràmetre. Una vegada fet això, es crea el tauler
        de joc a partir del fitxer a llegir.
     */
    public Laberint(String fitxer) {
        try {
            entrada = new FileInputStream(fitxer);
            in = new BufferedReader(new InputStreamReader(entrada));
            crearTauler();  //Cream el tauler de joc
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR D'I/O: Fitxer laberint no Trobat!!");
            System.exit(0);
        }
    }

    /* Métode crearTauler:
        Llegeix la dimensió del laberint i a partir d'aqui per a cada fila i cada columna,
        obté les dades de les parets de les caselles. Finalment crea una casella amb les
        dades obtingudes.
     */
    private void crearTauler() {
        try {
            // Obtenim el nombre total de files i columnes (2 primeres files del fitxer)
            FILES = Integer.parseInt(in.readLine());
            COLUMNES = Integer.parseInt(in.readLine());

            // Cream una matriu de objectes casella de dimensio Files x Columnes
            t = new Casella[FILES][COLUMNES];
            int y = 0;

            // Per cada fila
            for (int i = 0; i < FILES; i++) {
                int x = 0;
                // Per cada columna dins una fila
                for (int j = 0; j < COLUMNES; j++) {
                    // Cream un nou array, que tendrá l'informació de les 4 parets
                    parets = new char[4];
                    // Per cada paret d'una casella Llegim un carácter i si aquest es 
                    // diferent a un salt de línea el guardam dins l'array de parets
                    for (int k = 0; k < 4; k++) {
                        char c = (char) in.read();
                        if (c == '\n') {
                            k--;
                        } else {
                            parets[k] = c;
                        }
                    }

                    // Cream un objecte gráfic rectangle amb les coordenades actuals i
                    // l'altura i l'amplada d'un costat
                    Rectangle2D.Float r = new Rectangle2D.Float(x, y, COSTAT, COSTAT);

                    // Cream un objecte casella a la posició de la matriu de caselles,
                    // amb el rectangle i l'array de parets com a arguments, així com 
                    // un boolea que indica que la casella no está ocupada de moment
                    t[i][j] = new Casella(r, false, parets);
                    x += COSTAT;
                }
                y += COSTAT;
            }

            // Finalment llegim la fila i columna on es troba la casella de sortida
            in.read();
            filaFI = Integer.parseInt(in.readLine());
            columnaFI = Integer.parseInt(in.readLine()) - 1;

            // Assignam a la casella final la seva condició
            t[filaFI][columnaFI].setFi();

        } catch (IOException | NumberFormatException ex) {
            System.out.println("ERROR: No s'ha pogut crear el laberint!!");
        }
    }

    /* Métode paintComponent:
        Recorr totes les caselles del tauler del laberint i pinta totes les seves 
        components.
     */
    @Override
    public void paintComponent(Graphics g) {
        for (int i = 0; i < FILES; i++) {
            for (int j = 0; j < COLUMNES; j++) {
                // Pintam les components d'una casella
                t[i][j].paintComponent(g);
            }
        }
    }

    /* Métode getPreferredSize:
        Retorna amb un objecte Dimension les dimensions del laberint. Cal dir que
        s'afegeixen 49 pixels a l'alçada per poder fer lloc per la barra de menus.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COLUMNES * COSTAT, FILES * COSTAT + 49);
    }

    /* Métode Posa:
        Col·loca la fitxa a una casella donada la seva posició dins la matriu.
     */
    public void posa(int i, int j) {
        filaFITXA = i;
        columnaFITXA = j;
        t[i][j].setFitxa(new Fitxa());
    }

    /* Métode isOcupat:
        Retorna TRUE si la casella, donada la seva posició dins la matriu, está ocupada per 
        una fitxa.
     */
    public boolean isOcupat(int i, int j) {
        return t[i][j].isOcupada();
    }

    /* Métode buida:
        Elimina una fitxa d'una casella, donada la seva posició dins la matriu.
     */
    public void buida(int i, int j) {
        t[i][j].setOcupada(false);
    }

    /* Métode paretNord:
        Retorna true si la casella, donada la seva posició dins la matriu, té una 
        paret al nord.
     */
    public boolean paretNord(int i, int j) {
        return t[i][j].getNord();
    }

    /* Métode paretEst:
        Retorna true si la casella, donada la seva posició dins la matriu, té una
        paret a l'est.
     */
    public boolean paretEst(int i, int j) {
        return t[i][j].getEst();
    }

    /* Métode paretSud:
        Retorna true si la casella, donada la seva posició dins la matriu, té una 
        paret al sud.
     */
    public boolean paretSud(int i, int j) {
        return t[i][j].getSud();
    }

    /* Métode paretOest:
        Retorna true si la casella, donada la seva posició dins la matriu, té una
        paret a l'oest.
     */
    public boolean paretOest(int i, int j) {
        return t[i][j].getOest();
    }
}
