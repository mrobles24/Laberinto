/* 
Pràctica curs 2020-2021 Programació II

Es desitja programar un joc per trobar la sortida d'un laberint. Per això cal representar 
el laberint a partir d'unes dades, situar una fitxa a un lloc aleatori i després mitjançant
el teclat de l'ordinador poder-la moure fins a la sortida.
En el programa no s'han de generar els laberints de forma automàtica sinó que s'ha de
construir a partir de les dades que es proporcionaran per al desenvolupament de la pràctica.
Ens donen 4 fitxers de text que contenen la informació per a la construcció de 4 laberints 
diferents (maze1.txt, maze2.txt, ...).

A més el programa oferirà un menú amb les opcions
 -Obrir un nou laberint, llegint un altre fitxer de text.
 -Reiniciar la posició de la fitxa.
 -Sortir del programa.
 */

package laberint;

/**
  AUTOR: Miguel Vicente Robles Mclean
  ENLLAÇ AL VIDEO EXPLICATIU:
  Youtube : https://www.youtube.com/watch?v=so4lj9Hkuws 
  OneDrive : https://bit.ly/35sduN6
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/* CLASSE Main:
    Classe principal del programa, on es creen i inicialitzen tots els components de la 
    finestra gráfica. A més, també es gestionen tots els seus events, ja siguin control
    de opcions o lectura del teclat. A la classe, també es gestiona la creació i 
    actualització de els diferents laberints disponibles.
 */
public class Main extends JFrame implements KeyListener {

    // CONSTANTS
    private final Container panelContinguts;
    private final Random ran = new Random();

    // VARIABLES
    private int randomFila, randomColumna;
    private Laberint laberint;
    private String nomFitxer = "maze1.txt";

    /* Métode Constructor:
        Afegeix un títol a la finestra principal, un escoltador de teclat i la aturada
        automàtica quan es tanca la finestra. A més, asigna al contenidor de continguts
        el que hi ha a la finestra i finalment inicialitza els components. 
     */
    public Main() {
        super("Laberint - Programació II");
        super.addKeyListener(this);
        this.setResizable(false);
        this.setDefaultCloseOperation(Main.EXIT_ON_CLOSE);
        // Asignam un contenidor per als components
        panelContinguts = getContentPane();
        // Iniciam components amb el nom del fitxer del laberint que volem jugar
        initComponents(nomFitxer);
    }

    /* Métode initComponents:
        Inicialitza tots els components de la finestra gráfica, un d'ells el laberint.
        També conté tots els gestors d'events relacionats amb la interacció amb components.
     */
    private void initComponents(String fitxer) {
        try {
            // Agafam el look and feel del sistema operatiu en ús
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

            // Gestor d'events que gestiona la interacció amb components de la barra de menú
            ActionListener gestorEvents = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evento) {
                    // Amb el switch, distingim els events
                    switch (evento.getActionCommand()) {
                        // Cas triat obrir: triam el nou mapa a obrir
                        case "OBRIR":
                            triarMapa();
                            break;

                        // Cas reiniciar: movem la bolla a un altre lloc aleatòri
                        case "REINICIAR":
                            Object[] opcionsReinici = {"OK", "CANCELAR"};
                            // Mostram un JOption per confirmar el reinici
                            int reinici = JOptionPane.showOptionDialog(laberint,
                                    "Pitja OK per reiniciar el joc",
                                    "REINICIAR",
                                    JOptionPane.DEFAULT_OPTION,
                                    JOptionPane.INFORMATION_MESSAGE,
                                    null, opcionsReinici, opcionsReinici[0]);
                            // Si confirmen el reinici, llevam la fitxa  de la casella                         
                            // on està i la posam a un altre casella aleatòria
                            if (reinici == 0) {
                                laberint.buida(laberint.filaFITXA, laberint.columnaFITXA);
                                posaFitxa();
                            }
                            // Finalment repintam el laberint per mostrar els canvis
                            repintarLaberint();
                            break;

                        // Cas sortir: acabam la execució i tancam la finestra
                        case "SORTIR":
                            System.exit(0);
                            break;
                    }
                }
            };

            // Inicialitzam el laberint, segons el fitxer triat
            iniciaLaberint(fitxer);

            // Incialitzam la barra de menus
            JMenuBar jmbarBarraMenu = new JMenuBar();

            // Inicialitzam un menu
            JMenu jmnuFitxer = new JMenu("Fitxer");

            // Inicialitzam l'ítem obrir del menú i li assignam un gestor d'events
            JMenuItem jmItemObrir = new JMenuItem("OBRIR");
            jmItemObrir.addActionListener(gestorEvents);
            // Afegim l'ítem al menu
            jmnuFitxer.add(jmItemObrir);

            // Inicialitzam l'ítem reiniciar del menú i li assignam un gestor d'events
            JMenuItem jmItemReiniciar = new JMenuItem("REINICIAR");
            jmItemReiniciar.addActionListener(gestorEvents);
            // Afegim l'ítem al menu
            jmnuFitxer.add(jmItemReiniciar);

            // Inicialitzam l'ítem sortir del menú i li assignam un gestor d'events
            JMenuItem jmItemSortir = new JMenuItem("SORTIR");
            jmItemSortir.addActionListener(gestorEvents);
            // Afegim l'ítem al menu
            jmnuFitxer.add(jmItemSortir);

            // Afegim el menu a la barra de menus
            jmbarBarraMenu.add(jmnuFitxer);

            // Afegim la barra de menus al nord del panell de continguts
            panelContinguts.add(jmbarBarraMenu, BorderLayout.NORTH);

        } catch (ClassNotFoundException | IllegalAccessException
                | InstantiationException | UnsupportedLookAndFeelException ex) {
            System.out.println("ERROR a l'inicialització de Components!!");
        }
    }

    /* Métode iniciaLaberint:  
        Inicialitzam un laberint i l'afegim al layout del contenidor, a més, colocam
        la fitxa a una casella aleatòria del laberint.
     */
    private void iniciaLaberint(String s) {
        //  Inicialitzam un nou laberint segons un nom de fitxer passat per paràmetre. 
        laberint = new Laberint(s);
        // Assignam el tamany de la finestra actual segons el tamany del laberint.
        this.setSize(laberint.getPreferredSize());
        // Finalment afegim el laberint al centre del panell de continguts.
        panelContinguts.add(laberint, BorderLayout.CENTER);
        // Posam la fitxa a una casella aleatòria del laberint
        posaFitxa();
    }

    /* Métode repintarLaberint:
        Refrescam la finestra gràfica del laberint i si la fitxa está a la casella de sortida
        anunciam la victòria al jugador.
     */
    private void repintarLaberint() {
        laberint.repaint();
        // Si la casella de sortida está ocupada per la fitxa, anunciam victòria
        if (laberint.isOcupat(laberint.filaFI, laberint.columnaFI)) {
            // Anunciam la victòria mitjançant un JOption
            JOptionPane.showMessageDialog(laberint, "ENHORABONA, HAS GUANYAT!!", "VICTÒRIA", JOptionPane.INFORMATION_MESSAGE);

            Object[] opcionsFi = {"Tornar a jugar", "Sortir"};
            // Mostram la opció de tornar a jugar a un mapa nou o sortir del programa
            int obrirFitxer = JOptionPane.showOptionDialog(laberint,
                    "Que vol fer a continuació?",
                    "VICTÒRIA",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, opcionsFi, opcionsFi[0]);

            switch (obrirFitxer) {
                // Si s'ha triat tornar a jugar, triam el nou mapa a jugar
                case 0:
                    triarMapa();
                    break;

                // Si s'ha triat sortir, acabam l'execució del programa
                case 1:
                    System.exit(0);
                    break;
            }
        }
    }

    /* Métode posaFitxa:
        Col·loca la fitxa a una casella aleatòria del laberint.
     */
    private void posaFitxa() {
        try {
            do {
                // Calculam un nombre aleatòri per la fila i per la columna
                randomFila = ran.nextInt(laberint.FILES);
                randomColumna = ran.nextInt(laberint.COLUMNES);
            } while ((randomFila == laberint.filaFI) && (randomColumna == laberint.columnaFI));
            // Si la casella triada no es la casella de sortida, colocam la fitxa
            laberint.posa(randomFila, randomColumna);
        } catch (Exception e) {
            System.out.println("ERROR: No s'ha pogut posar la fitxa");
        }
    }

    /* Métode triarMapa:
        Permet a l'usuari triar el nou laberint a obrir i una vegada triat, tanca el laberint
        actual i obri el nou.
     */
    private void triarMapa() {
        Object[] opcionsJoc = {"MAPA 1", "MAPA 2", "MAPA 3", "MAPA 4"};
        // Demanam a l'usuari quin laberint volen obrir
        int obrirFitxer = JOptionPane.showOptionDialog(laberint,
                "Tria el mapa al que vols jugar",
                "OBRIR",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, opcionsJoc, opcionsJoc[0]);

        switch (obrirFitxer) {
            // Si es tria l'opció 0, asignam el nom del fitxer al laberint "maze1.txt"
            case 0:
                nomFitxer = "maze1.txt";
                break;
            // Si es tria l'opció 1, asignam el nom del fitxer al laberint "maze2.txt"
            case 1:
                nomFitxer = "maze2.txt";
                break;
            // Si es tria l'opció 2, asignam el nom del fitxer al laberint "maze3.txt"      
            case 2:
                nomFitxer = "maze3.txt";
                break;
            // Si es tria l'opció 3, asignam el nom del fitxer al laberint "maze4.txt"    
            case 3:
                nomFitxer = "maze4.txt";
                break;
        }
        // Eliminam tots els components de la finestra i la tancam
        dispose();
        // Cream una nova finestra
        Main mapa1 = new Main();
        // Inicialitzam els components de la nova finestra amb el nou nom de fitxer
        mapa1.initComponents(nomFitxer);
        // Marcam visible la nova finestra
        mapa1.setVisible(true);
    }

    /* Gestor d'events keyPressed:
        Gestiona el moviment de la fitxa mitjançant les tecles:
        - W: Fitxa amunt
        - S: Fitxa avall
        - A: Fitxa esquerra
        - D: Fitxa dreta
     */
    @Override
    public void keyPressed(KeyEvent e) {

        // Si s'ha pitjat la tecla W, i no hi ha paret al nord, movem la fitxa amunt
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (!(laberint.paretNord(laberint.filaFITXA, laberint.columnaFITXA))) {
                // Eliminam la fitxa de la casella actual
                laberint.buida(laberint.filaFITXA, laberint.columnaFITXA);
                // Movem la fitxa una fila per amunt
                laberint.posa(laberint.filaFITXA - 1, laberint.columnaFITXA);
            }
        }

        // Si s'ha pitjat la tecla A, i no hi ha paret al oest, movem la fitxa a l'esquerra
        if (e.getKeyCode() == KeyEvent.VK_A) {
            if (!(laberint.paretOest(laberint.filaFITXA, laberint.columnaFITXA))) {
                // Eliminam la fitxa de la casella actual
                laberint.buida(laberint.filaFITXA, laberint.columnaFITXA);
                // Movem la fitxa una columna a l'esquerra
                laberint.posa(laberint.filaFITXA, laberint.columnaFITXA - 1);
            }
        }

        // Si s'ha pitjat la tecla D, i no hi ha paret al est, movem la fitxa a la dreta
        if (e.getKeyCode() == KeyEvent.VK_D) {
            if (!(laberint.paretEst(laberint.filaFITXA, laberint.columnaFITXA))) {
                // Eliminam la fitxa de la casella actual
                laberint.buida(laberint.filaFITXA, laberint.columnaFITXA);
                // Movem la fitxa una columna a la dreta
                laberint.posa(laberint.filaFITXA, laberint.columnaFITXA + 1);
            }
        }

        // Si s'ha pitjat la tecla S, i no hi ha paret al sud, movem la fitxa avall
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (!(laberint.paretSud(laberint.filaFITXA, laberint.columnaFITXA))) {
                // Eliminam la fitxa de la casella actual
                laberint.buida(laberint.filaFITXA, laberint.columnaFITXA);
                // Movem la fitxa una fila avall
                laberint.posa(laberint.filaFITXA + 1, laberint.columnaFITXA);
            }
        }

        // Repintam el laberint per reflexar els canvis fets
        repintarLaberint();
    }

    /* Métodes keyTyped i keyReleased:
        Son part de l'escoltador i s'han de implementar, tot i que per la nostra pràctica,
        no les usam
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /* Métode main:
        Crea una nova finestra gráfica, cosa que automàticament crea un laberint amb el 
        fitxer predeterminat "maze1.txt", després el posa visible.
     */
    public static void main(String[] args) throws FileNotFoundException {
        Main inici = new Main();
        inici.setVisible(true);
    }

}
