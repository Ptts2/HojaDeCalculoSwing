import java.util.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class HojaDeCalculoSwing {
    public static void main(String[] args) {
        creadorDeVentana();

    }

    public static void creadorDeVentana() {
        JFrame ventana = new JFrame("Hoja de calculo");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelBarraYTexto = new JPanel(new BorderLayout()); // Panel para poner la barra y la celda de texto
        JPanel panelTextos = new JPanel(new BorderLayout()); // Panel para guardar los textos que ira en el otro panel

        /* Barra y menus */
        JMenuBar barra = new JMenuBar(); // La barra
        JMenu archivo = new JMenu("Archivo"); // el menu
        JMenu editar = new JMenu("Editar"); // menu editar
        // Botones de archivo
        JMenuItem nueva = new JMenuItem("Nueva hoja");
        JMenuItem guardar = new JMenuItem("Guardar");
        JMenuItem cargar = new JMenuItem("Cargar");
        archivo.add(nueva);
        archivo.add(guardar);
        archivo.add(cargar);
        // Botones de editar
        JMenuItem deshacer = new JMenuItem("Deshacer");
        JMenuItem rehacer = new JMenuItem("Rehacer");
        editar.add(deshacer);
        editar.add(rehacer);
        // Añadir los menus a la barra
        barra.add(archivo);
        barra.add(editar);

        /* Tabla que hará de hoja de calculo */
        JTable hoja = new JTable(); // (cambiar 50,50 por variable, posible metodo)
        hoja.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTable filas = numerosFila(50); // Numeros de las filas
        DefaultTableModel modelo = new DefaultTableModel(50, 50);
        modelo.addTableModelListener(new TableModelListener() {

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

        // Añado la hoja al ScollPane y los elementos al panel principal con
        // BorderLayout


        JScrollPane panelHoja = new JScrollPane(hoja); //hoja
        JTextPane panelTexto = new JTextPane();

        // Panel donde se representaran las filas y las columnas seleccionadas
        JTextPane panelTextoFilaCol = new JTextPane();
        panelTextoFilaCol.setOpaque(true);
        panelTextoFilaCol.setBackground(new Color(238, 238, 238));
        panelTextoFilaCol.setEditable(false);
        panelTextoFilaCol.setText("Fila: N/A Columna: N/A");

        hoja.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                panelTextoFilaCol.setText("Fila: " + (hoja.getSelectedRow()+1) + " Columna: " + (hoja.getSelectedColumn() +1 ));
            }
            
        });
        
        panelTextos.add(panelTextoFilaCol, BorderLayout.WEST);
        panelTextos.add(panelTexto, BorderLayout.CENTER);

        panelBarraYTexto.add(barra, BorderLayout.NORTH);
        panelBarraYTexto.add(panelTextos, BorderLayout.SOUTH);

        panel.add(panelBarraYTexto, BorderLayout.NORTH);
        panel.add(panelHoja, BorderLayout.CENTER);
        panel.add(filas, BorderLayout.WEST);

        // Añadir al frame
        ventana.add(panel);
        ventana.pack(); // Mejor tamaño posible
        ventana.setVisible(true);

    }

    public static JTable numerosFila(int nFilas) {

        // Tabla que solo tiene 1 columna donde iran los numeros de las filas
        JTable filas = new JTable(nFilas + 1, 1);

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
        for (int i = 1; i <= nFilas; i++) {
            filas.setValueAt(String.valueOf(i), i, 0);
        }

        return filas;
    }


    public static String resolverFormula(Formula form){
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

                if(fila == -1 || columna == -1){
                    return "##1ERRORFORM";
                }

                if( String.valueOf(hoja.getValueAt( casillas.get(i)[0], casillas.get(i)[1])).isEmpty() ){
                    valor = valor + 0; //Puede cambiar, dependiendo especificaciones
                }else{
                valor = valor + Integer.parseInt(String.valueOf(hoja.getValueAt(fila, columna)));
                }
                
            }

        } catch (Exception e) {
            return "##ERRORFORM";
        }
        return String.valueOf(valor);
    }

    public static int[] descifrarCasilla(String casilla) {
        final String abecedario = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int[] casillas = new int[2];
        int iFil = 0, iCol = 0, i = 0;
        boolean buscando = true;
        String columnas;

        do {

            if (!(abecedario.contains(String.valueOf(casilla.charAt(i))))) {

                if (i == 0) {
                    casillas[0]= -1;
                    casillas[1]= -1;
                    return casillas;
                }
                iFil = i;
                buscando = false;
            }
            i++;
        } while (buscando && i < casilla.length());

        try {
            casillas[0] = Integer.parseInt(casilla.substring(iFil)) -1;
        } catch (Exception e) {
            casillas[0]= -1;
            casillas[1]= -1;
            return casillas;
        }

        // Calcular las columnas
        columnas = casilla.substring(0, iFil);

        try {
            for (i = 0; i < columnas.length(); i++) {
                int indice = (abecedario.indexOf(columnas.charAt(i)) + 1);

                if (indice < 0) {
                    casillas[0]= -1;
                    casillas[1]= -1;
                    return casillas;
                }
                // para calcular el valor en base 26 ValorDec * sistemadenum ^ indice

                iCol = iCol + (indice * ((int) (Math.pow(26.0, (double) ((columnas.length() - 1) - i)))));
                iCol--;
            }
        } catch (Exception e) {
            casillas[0]= -1;
            casillas[1]= -1;
            return casillas;
        }

        casillas[1] = iCol;
        return casillas;
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

    public JTable getHoja(){
        return this.hoja;
    }

}
