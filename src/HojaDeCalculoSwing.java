import java.util.*;
import java.util.ResourceBundle.Control;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;


public class HojaDeCalculoSwing {
    public static void main(String[] args) {
        creadorDeVentana();

    }

    /**
     * Metodo que crea la ventana y la hoja de calculo
     */
    public static void creadorDeVentana() {

        int valores[];

        /********************
         * JFRAME Y PANELES *
         ********************/
        Hoja hoja;
        JFrame ventana = new JFrame("Hoja de calculo");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout()); // Panel principal
        JPanel panelBarraYTexto = new JPanel(new BorderLayout()); // Panel para poner la barra y la celda de texto
        JPanel panelTextos = new JPanel(new BorderLayout()); // Panel para guardar los textos que ira en el otro panel
        JTextPane panelTexto = new JTextPane(); // Panel donde ira el texto de las celdas
        JTextPane panelTextoFilaCol = new JTextPane(); // Panel donde ira la fila y la columna seleccionada
        JButton botonCalcular = new JButton("Calcular");
        valores = nuevaHoja(); // Tamaño que tendrá la hoja
        Hoja primeraHoja = new Hoja(valores[0], valores[1]); // Hoja de calculo
        hoja=primeraHoja;
        JScrollPane panelHoja = new JScrollPane(hoja.getTable()); // Panel scrollable al que añado la hoja

        /*******************
         * BARRAS Y MENUS *
         *******************/

        JMenuBar barra = new JMenuBar(); // La Barra
        JMenu archivo = new JMenu("Archivo"); // Menu Archivo
        JMenu editar = new JMenu("Editar"); // Menu Editar

        // Botones del menu Archivo
        JMenuItem nueva = new JMenuItem("Nueva hoja");
        JMenuItem guardar = new JMenuItem("Guardar");
        JMenuItem cargar = new JMenuItem("Cargar");
        archivo.add(nueva);
        archivo.add(guardar);
        archivo.add(cargar);

        // Botones del menu Editar
        JMenuItem deshacer = new JMenuItem("Deshacer");
        JMenuItem rehacer = new JMenuItem("Rehacer");
        editar.add(deshacer);
        editar.add(rehacer);

        /************************
         * PROPIEDADES PANELES *
         ************************/

        panelTextoFilaCol.setOpaque(true);
        panelTextoFilaCol.setBackground(new Color(238, 238, 238));
        panelTextoFilaCol.setEditable(false);
        panelTextoFilaCol.setText("Fila: N/A Columna: N/A");

        /************************
         * LISTENERS Y ACCIONES *
         ************************/

        hoja.getTable().addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                panelTextoFilaCol.setText("Fila: " + (hoja.getTable().rowAtPoint(e.getPoint()) + 1) + " Columna: "
                        + (hoja.getTable().columnAtPoint(e.getPoint()) + 1));
            }
        });

        //Nueva Hoja
        Action nuevo = new AbstractAction("Nueva hoja") {

            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                int nuevosValores[] = nuevaHoja();
                Hoja nuevaHoja = new Hoja(valores[0], valores[1]);
                hoja = nuevaHoja;
                ventana.remove(panelHoja);
                ventana.add(new JScrollPane(nuevaHoja.getTable()));
                */
            }

        };
        nuevo.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)); //Para el shortcut CTRL+N
        nueva.setAction(nuevo);

        //Guardar
        Action guardado = new AbstractAction("Guardar") {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("Guardar");
            }

        };
        guardado.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); //Para el shortcut CTRL+S
        guardar.setAction(guardado);

        //Cargar
        Action cargado = new AbstractAction("Cargar") {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("Cargar");
            }

        };
        cargado.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)); //Para el shortcut CTRL+O
        cargar.setAction(cargado);
        
        //Deshacer
        Action deshaz = new AbstractAction("Deshacer") {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("Deshacer");
            }

        };
        deshaz.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK)); //Para el shortcut CTRL+Z
        deshacer.setAction(deshaz);

        //Rehacer
        Action rehaz = new AbstractAction("Rehacer") {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("Rehacer");
            }

        };
        rehaz.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK)); //Para el shortcut CTRL+Y
        rehacer.setAction(rehaz);

        //Calcular
        Action calcula = new AbstractAction("Calcular") {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.out.println("Calcular");
            }

        };
        botonCalcular.setAction(calcula);

        /******************
         * AÑADIR PANELES *
         ******************/

        barra.add(archivo);
        barra.add(editar);

        panelTextos.add(panelTextoFilaCol, BorderLayout.WEST);
        panelTextos.add(panelTexto, BorderLayout.CENTER);
        panelTextos.add(botonCalcular, BorderLayout.EAST);

        panelBarraYTexto.add(barra, BorderLayout.NORTH);
        panelBarraYTexto.add(panelTextos, BorderLayout.SOUTH);

        panel.add(panelBarraYTexto, BorderLayout.NORTH);
        panel.add(panelHoja, BorderLayout.CENTER);
        panel.add(hoja.getNFilaTable(), BorderLayout.WEST);


        ventana.add(panel);
        ventana.pack(); // Mejor tamaño posible
        ventana.setVisible(true);

    }

    /**
     * Metodo que pide los datos para una nueva hoja
     * @return tamaño de filas y columnas para la nueva hoja
     */
    public static int[] nuevaHoja() {

        int filasYCol[] = new int[2];

        JTextField pedirFilas = new JTextField(10);
        JTextField pedirColumnas = new JTextField(10);
        boolean pidiendo = true;

        JPanel panelAviso = new JPanel();
        panelAviso.add(new JLabel("Introduzca el numero de Filas: "));
        panelAviso.add(pedirFilas);
        panelAviso.add(new JLabel("Introduzca el numero de Columnas: "));
        panelAviso.add(pedirColumnas);

        while (pidiendo) {
            int accion = JOptionPane.showConfirmDialog(null, panelAviso, "Introduce las filas y las columnas",
                    JOptionPane.OK_CANCEL_OPTION);

            if (accion == JOptionPane.OK_OPTION) {

                try {
                    filasYCol[0] = Integer.parseInt(pedirFilas.getText().trim().replaceAll(" ", ""));
                    filasYCol[1] = Integer.parseInt(pedirColumnas.getText().trim().replaceAll(" ", ""));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error en filas o columnas.");
                    continue;
                }
                pidiendo = false;
            }
        }

        return filasYCol;

    }

}

class Hoja {

    private JTable hoja;
    private String[][] hojaString;
    private int nFilas;
    private int nCol;
    private Hoja anterior;
    private Hoja posterior;


    /**
     * Constructor de la hoja de calculo
     * @param nFilas Numero de filas
     * @param nCol Numero de columnas
     */
    public Hoja(int nFilas, int nCol) {
        this.nFilas = nFilas;
        this.nCol = nCol;

        DefaultTableModel modelo = new DefaultTableModel(this.nFilas, this.nCol);
        this.hoja = new JTable();
        hoja.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        hoja.setRowSelectionAllowed(false);
        hoja.setCellSelectionEnabled(true);
        modelo.addTableModelListener(new TableModelListener() { // Cambiar por boton

            public void tableChanged(TableModelEvent e) {
                int fila = -1, columna = -1;
                Object value = "";
                if (hoja.isEditing()) {
                    fila = hoja.getSelectedRow();
                    columna = hoja.getSelectedColumn();
                    value = hoja.getValueAt(fila, columna);
                }

                if (fila != -1 && columna != -1 && !String.valueOf(value).isEmpty()) {

                    if (String.valueOf(value).charAt(0) == '=') {
                        Formula formula = new Formula(String.valueOf(value), fila, columna, hoja);
                        hoja.setValueAt(resolverFormula(formula), fila, columna);
                    }
                }

            }

        });

        hoja.setModel(modelo);

        this.hojaString = new String[this.nFilas][this.nCol];
        this.anterior = null;
        this.posterior = null;

    }

    /*********************
     * GETTERS Y SETTERS *
     *********************/

    /**
     * Metodo que devuelve la hoja de calculo
     * @return hoja de calculo
     */
    public JTable getTable() {
        return this.hoja;
    }

    /**
     * Metodo que calcula la tabla de filas
     * @return tabla de numero de filas
     */
    public JTable getNFilaTable() {

        // Tabla que solo tiene 1 columna donde iran los numeros de las filas
        JTable filas = new JTable(this.nFilas + 1, 1);

        // Le pongo color gris
        Color gris = new Color(238, 238, 238);
        filas.setOpaque(true);
        filas.setFillsViewportHeight(true);
        filas.setBackground(gris);

        // Hago que no se pueda seleccionar ni editar
        filas.setRowSelectionAllowed(false);
        filas.setFocusable(false);

        // La deshabilito para que no se pueda editar
        filas.setEnabled(false);

        // Altura de las filas para que encaje con la tabla
        filas.setRowHeight(16);
        filas.setRowHeight(0, 21);

        // Le pongo el texto de los numeros a las casillas
        for (int i = 1; i <= this.nFilas; i++) {
            filas.setValueAt(String.valueOf(i), i, 0);
        }

        return filas;
    }

    /**
     * Metodo que cambia la tabla anterior
     * @param anterior nueva tabla anterior
     */
    public void setAnterior(Hoja anterior){
        this.anterior = anterior;
    }

    /**
     * Metodo que cambia la tabla posterior
     * @param posterior nueva tabla posterior
     */
    public void setPosterior(Hoja posterior){
        this.posterior = posterior;
    }


    /********************
     * RESOLVER FORMULA *
     ********************/

    /**
     * Metodo que resuelve una formula
     * @param form Formula
     * @return valor numérico de la formula
     */
    public static String resolverFormula(Formula form) {
        String formula = form.getFormula();
        JTable hoja = form.getHoja();
        int valor = 0;
        ArrayList<int[]> casillas = new ArrayList<int[]>();
        // Suma
        String[] factores = formula.substring(1).split("\\+");

        for (int i = 0; i < factores.length; i++) {
            casillas.add(descifrarCasilla(factores[i]));
        }

        try {
            for (int i = 0; i < factores.length; i++) {
                int fila = casillas.get(i)[0];
                int columna = casillas.get(i)[1];

                if (fila == -1 || columna == -1) {
                    return "##1ERRORFORM";
                }

                if (String.valueOf(hoja.getValueAt(casillas.get(i)[0], casillas.get(i)[1])).isEmpty()) {
                    valor = valor + 0; // Puede cambiar, dependiendo especificaciones
                } else {
                    valor = valor + Integer.parseInt(String.valueOf(hoja.getValueAt(fila, columna)));
                }

            }

        } catch (Exception e) {
            return "##ERRORFORM";
        }
        return String.valueOf(valor);
    }

    /**
     * Metodo que descifra una casilla
     * @param casilla Casilla en formato letra(columna) numero(fila) Ejemplo: A1
     * @return casilla en formato [fila][columna] Ejemplo: [0][0]
     */
    public static int[] descifrarCasilla(String casilla) {
        final String abecedario = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int[] casillas = new int[2];
        int iFil = 0, iCol = 0, i = 0;
        boolean buscando = true;
        String columnas;

        do {

            if (!(abecedario.contains(String.valueOf(casilla.charAt(i))))) {

                if (i == 0) {
                    casillas[0] = -1;
                    casillas[1] = -1;
                    return casillas;
                }
                iFil = i;
                buscando = false;
            }
            i++;
        } while (buscando && i < casilla.length());

        try {
            casillas[0] = Integer.parseInt(casilla.substring(iFil)) - 1;
        } catch (Exception e) {
            casillas[0] = -1;
            casillas[1] = -1;
            return casillas;
        }

        // Calcular las columnas
        columnas = casilla.substring(0, iFil);

        try {
            for (i = 0; i < columnas.length(); i++) {
                int indice = (abecedario.indexOf(columnas.charAt(i)) + 1);

                if (indice < 0) {
                    casillas[0] = -1;
                    casillas[1] = -1;
                    return casillas;
                }
                // para calcular el valor en base 26 ValorDec * sistemadenum ^ indice

                iCol = iCol + (indice * ((int) (Math.pow(26.0, (double) ((columnas.length() - 1) - i)))));
                iCol--;
            }
        } catch (Exception e) {
            casillas[0] = -1;
            casillas[1] = -1;
            return casillas;
        }

        casillas[1] = iCol;
        return casillas;
    }


    public void nuevaHoja(int nFilas, int nCol){


       /* this = new Hoja(nFilas, nCol);*/

    }

}

class Formula {

    /* Atributos de la hoja de cálculo */
    private String formula;
    private int filFormula;
    private int colFormula;
    private JTable hoja;

    /**
     * Constructor de la clase formula
     * 
     * @param formula String que contiene las casillas de la formula y los
     *                operadores
     * @param fil     Fila a la que pertenece la formula
     * @param col     Columna a la que pertenece la formula
     */
    public Formula(String formula, int fil, int col, JTable hoja) {
        this.formula = formula;
        this.filFormula = fil;
        this.colFormula = col;
        this.hoja = hoja;
    }

    /**
     * Devuelve la formula como string
     * 
     * @return formula como string
     */
    public String getFormula() {
        return this.formula;
    }

    /**
     * Devuelve la fila a la que pertenece la formula
     * 
     * @return fila de la formula
     */
    public int getFil() {
        return this.filFormula;
    }

    /**
     * Devuelve la columna a la que pertenece la formula
     * 
     * @return columna de la formula
     */
    public int getCol() {
        return this.colFormula;
    }

    public JTable getHoja() {
        return this.hoja;
    }

}
