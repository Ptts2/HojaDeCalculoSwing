import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
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
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;

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
        ventana.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout()); // Panel principal
        JPanel panelBarraYTexto = new JPanel(new BorderLayout()); // Panel para poner la barra y la celda de texto
        JPanel panelTextos = new JPanel(new BorderLayout()); // Panel para guardar los textos que ira en el otro panel
        JTextPane panelTexto = new JTextPane(); // Panel separacion
        JTextPane panelTextoFilaCol = new JTextPane(); // Panel donde ira la fila y la columna seleccionada
        JButton botonCalcular = new JButton("Calcular");
        JScrollPane panelHoja; // Panel scrollable al que añado la hoja
        JScrollPane filas; //Panel donde iran los numeros de fila
        valores = new int [2];
        hoja = new Hoja(); // Hoja de calculo

        /********************
         *  BARRAS Y MENUS  *
         ********************/

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
        

        /********************
         *  INICIO DE HOJA  *
         ********************/

        valores = nuevaHoja();

        if(valores[0] > 0 && valores[1] > 0){
            hoja.nuevaHoja(valores[0], valores[1]);
        }
        panelHoja = new JScrollPane(hoja.getTable());
        filas = new JScrollPane(hoja.getNFilaTable());

        /************************
         * LISTENERS Y ACCIONES *
         ************************/

        ventana.addWindowListener(new WindowAdapter(){
            
            @Override
            public void windowClosing(WindowEvent e){

                if(!hoja.equals(null) && hoja.isEditado()){
                    boolean salir = noGuardado();

                    if(salir == false){
                        System.exit(0);
                    }else{
                        hoja.guardar();
                        System.exit(0);
                    }

                }else{

                    System.exit(0);
                }
            }

        });
        hoja.getTable().addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                panelTextoFilaCol.setText("Fila: " + (hoja.getTable().rowAtPoint(e.getPoint()) + 1) + " Columna: "
                        + (hoja.getTable().columnAtPoint(e.getPoint()) + 1));
            }
        });

        // Nueva Hoja
        Action nuevo = new AbstractAction("Nueva hoja") {

            @Override
            public void actionPerformed(ActionEvent e) {

                if(hoja.isEditado()){
                    boolean cambiar = noGuardado();

                    if(cambiar == true){
                        hoja.guardar();
                    }
                }

                int[] nuevosValores = nuevaHoja();

                if(nuevosValores[0] > 0 && nuevosValores[1] >0){
                    panel.remove(panelHoja);
                    panel.remove(hoja.getNFilaTable());
                    panelHoja.remove(hoja.getTable());
                    panel.remove(filas);

                    hoja.nuevaHoja(nuevosValores[0], nuevosValores[1]);

                    panelHoja.setViewportView(hoja.getTable());
                    filas.setViewportView(hoja.getNFilaTable());
                    panel.add(filas, BorderLayout.WEST);
                    panel.add(panelHoja, BorderLayout.CENTER);
                    SwingUtilities.updateComponentTreeUI(ventana);
                    hoja.getTable().addMouseListener(new java.awt.event.MouseAdapter() {

                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            panelTextoFilaCol.setText("Fila: " + (hoja.getTable().rowAtPoint(e.getPoint()) + 1) + " Columna: "
                                    + (hoja.getTable().columnAtPoint(e.getPoint()) + 1));
                        }
                    });
                }
            }

        };
        nuevo.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)); // Para el shortcut CTRL+N                                                                
        nueva.setAction(nuevo);

        // Guardar
        Action guardado = new AbstractAction("Guardar") {

            @Override
            public void actionPerformed(ActionEvent e) {

                hoja.guardar();

                hoja.setEditado(false);
            }

        };
        guardado.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); // Para el shortcut CTRL+S
        guardar.setAction(guardado);

        // Cargar
        Action cargado = new AbstractAction("Cargar") {

            @Override
            public void actionPerformed(ActionEvent e) {

                if(hoja.isEditado()){
                    boolean cambiar = noGuardado();

                    if(cambiar == true){
                        hoja.guardar();
                    }
                }

                File archivoALeer = null;
                JFileChooser elegir = new JFileChooser();
                FileNameExtensionFilter filtro = new FileNameExtensionFilter("Hojas de calculo con extension TXT", "txt");
                elegir.setFileFilter(filtro);
                boolean pedir = true;
                
                while(pedir){
                    int opcion = elegir.showOpenDialog(null);
                    
                    if(opcion == JFileChooser.CANCEL_OPTION){
                        pedir = false;
    
                    //Compruebo si existe o si termina con txt
                    }else if( !(elegir.getSelectedFile().toString().endsWith(".txt")) || !(elegir.getSelectedFile().exists()) ){
                        JOptionPane.showMessageDialog(null, "El archivo indicado debe ser de extension .txt y existir");
                       
                    }else{ //Si existe el archivo
    
                        archivoALeer = elegir.getSelectedFile();
                        pedir = false;
                    }
                }

                ArrayList<String[]> lista = new ArrayList<String[]>();

                try{
                Scanner lector = new Scanner(archivoALeer);

                    while(lector.hasNextLine()){
                        lista.add(lector.nextLine().split("SEPARACION"));
                    }
                    lector.close();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null, "Error al abrir el archivo");
                };

                panel.remove(panelHoja);

                panel.remove(hoja.getNFilaTable());
                panelHoja.remove(hoja.getTable());
                panel.remove(filas);
                

                hoja.nuevaHoja(lista.size(), lista.get(0).length);

                panelHoja.setViewportView(hoja.getTable());
                filas.setViewportView(hoja.getNFilaTable());
                panel.add(filas, BorderLayout.WEST);
                panel.add(panelHoja, BorderLayout.CENTER);
                SwingUtilities.updateComponentTreeUI(ventana);
                hoja.getTable().addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                        panelTextoFilaCol.setText("Fila: " + (hoja.getTable().rowAtPoint(e.getPoint()) + 1) + " Columna: "
                                + (hoja.getTable().columnAtPoint(e.getPoint()) + 1));
                    }
                });
                
                
                for(int i = 0; i<hoja.getCol(); i++){
                    hoja.setFila(lista.get(i), i);
                }

                SwingUtilities.updateComponentTreeUI(ventana);
            }

        };
        cargado.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)); // Para el shortcut CTRL+O
        cargar.setAction(cargado);

        // Deshacer
        Action deshaz = new AbstractAction("Deshacer") {

            @Override
            public void actionPerformed(ActionEvent e) {

                hoja.deshacer();
            }

        };
        deshaz.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK)); // Para el shortcut CTRL+Z
        deshacer.setAction(deshaz);

        // Rehacer
        Action rehaz = new AbstractAction("Rehacer") {

            @Override
            public void actionPerformed(ActionEvent e) {

                hoja.rehacer();
            }

        };
        rehaz.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK)); // Para el shortcut CTRL+Y
        rehacer.setAction(rehaz);

        // Calcular
        Action calcula = new AbstractAction("Calcular") {

            @Override
            public void actionPerformed(ActionEvent e) {

                hoja.calcular();
            }

        };
        botonCalcular.setAction(calcula);

        /************************
         * PROPIEDADES PANELES *
         ************************/

        panelTextoFilaCol.setOpaque(true);
        panelTextoFilaCol.setBackground(new Color(238, 238, 238));
        panelTextoFilaCol.setEditable(false);
        panelTextoFilaCol.setText("Fila: N/A Columna: N/A");
        filas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        filas.getVerticalScrollBar().setModel(panelHoja.getVerticalScrollBar().getModel());
        filas.setPreferredSize(new Dimension(100,16));
        panelTexto.setOpaque(true);
        panelTexto.setBackground(new Color(238, 238, 238));
        panelTexto.setEditable(false);

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
        panel.add(filas, BorderLayout.WEST);

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

                    if(filasYCol[0] <= 0 || filasYCol[1] <= 0){
                        JOptionPane.showMessageDialog(null, "Error en filas o columnas.");
                        continue;
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error en filas o columnas.");
                    continue;
                }
                pidiendo = false;
            }

            if(accion == JOptionPane.CANCEL_OPTION){
                pidiendo = false;
                System.exit(0);
            }
        }
    
        return filasYCol;

    }

    public static boolean noGuardado(){
        
        int opcion = JOptionPane.showConfirmDialog(null, "Los cambios no han sido guardados, ¿Desea guardarlos?");

        if(opcion == JOptionPane.YES_OPTION){
            return true; //Iria el acceso al metodo de guardar
        }else if(opcion == JOptionPane.NO_OPTION){
            return false; 
            
        }else if(opcion == JOptionPane.CANCEL_OPTION){
            return true;
           
        }
        return false;

    }

}

class Hoja {

    private JTable hoja;
    private JTable hojaFilas;
    private String[][] hojaString;
    private int nFilas;
    private int nCol;
    private Cambio anterior;
    private Cambio posterior;
    private boolean editado;
    private File archivoHoja;

    /**
     * Constructor de la hoja de calculo
     */
    public Hoja() {
        hojaFilas = null;

    }

    /**
     * Crea una nueva hoja con las filas y las columnas
     * @param nFilas nuevas filas
     * @param nCol nuevas columnas
     */
    public void nuevaHoja(int nFilas, int nCol) {

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
                editado = true;
                Object value = "";
                if (hoja.isEditing()) {
                    fila = hoja.getSelectedRow();
                    columna = hoja.getSelectedColumn();
                    value = hoja.getValueAt(fila, columna);

                }

                if (fila != -1 && columna != -1 /*&& !String.valueOf(value).isEmpty()*/) {      

                    if(anterior == null){
                        anterior = new Cambio(hojaString, null);
                    }else{
                        anterior = new Cambio(hojaString, anterior);
                    }
                    hojaString[fila][columna] = String.valueOf(value);

                }
                
            }

        });

        hoja.setModel(modelo);

        this.hojaString = new String[this.nFilas][this.nCol];
        this.anterior = null;
        this.posterior = null;
        this.hojaFilas = null;
        this.editado = false;
        this.archivoHoja = null;

        this.anterior = null;
        this.posterior = null;

    }

    /*********************
     * GETTERS Y SETTERS *
     *********************/

    /**
     * Metodo que devuelve la hoja de calculo
     * 
     * @return hoja de calculo
     */
    public JTable getTable() {
        return this.hoja;
    }

    /**
     * Metodo que devuelve el numero de columnas
     * @return el numero de columnas
     */
    public int getCol(){
        return this.nCol;
    }

    /**
     * Metodo que devuelve el numero de filas
     * @return el numero de filas
     */
    public int getFil(){
        return this.nFilas;
    }

    /**
     * Devuelve la hoja como una tabla de strings
     * @return hoja como una tabla de strings
     */
    public String[][] getHojaString(){
        return this.hojaString;
    }

    /**
     * Metodo que dice si la hoja ha sido editada
     * @return si ha sido editada true, si no false
     */
    public boolean isEditado(){

        return this.editado;
    }
    /**
     * Metodo que calcula la tabla de filas
     * 
     * @return tabla de numero de filas
     */
    public JTable getNFilaTable() {

        if (this.hojaFilas != null) {
            return this.hojaFilas;
        } else {
            // Tabla que solo tiene 1 columna donde iran los numeros de las filas
            JTable filas = new JTable(this.nFilas + 2, 1);
            
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

            //Le quito la cabecera y le cambio la anchura
            filas.setTableHeader(null);

            // Le pongo el texto de los numeros a las casillas
            for (int i = 1; i <= this.nFilas; i++) {
                filas.setValueAt(String.valueOf(i), i, 0);
            }

            this.hojaFilas = filas;
            return this.hojaFilas;
        }
    }

    /**
     * Metodo para decir si la hoja ha sido editada
     * @param editado si ha sido editada o no
     */
    public void setEditado(boolean editado){
        this.editado = editado;
    }

    public void setFila(String[] fila, int nFila){

        for(int i = 0; i<nCol; i++){
            hojaString[nFila][i] = fila[i];
            hoja.setValueAt(fila[i], nFila, i);
        }

    }

    /********************
     * RESOLVER FORMULA *
     ********************/


     /**
     * Hace los calculos correspondientes a la hoja de calculo cuando la matriz de
     * referencias esta completa
     */
    public void calcular() {


        if(anterior == null){
            anterior = new Cambio(hojaString, null);
        }else{
            anterior = new Cambio(hojaString, anterior);
        }

        ArrayList <Formula> formulas = new ArrayList<Formula>();
        for (int i = 0; i < this.nFilas; i++) {
            for (int j = 0; j < this.nCol; j++) {

                // Si es formula la añado a una lista para hacerlo cuando todos los valores de-
                // las demas casillas esten puestos
                if(this.hojaString[i][j] != null){

                    if (this.hojaString[i][j].charAt(0) == '=') {
                        formulas.add(new Formula(this.hojaString[i][j], i, j));
                    } 
                }
            }
        }
        //Ahora resuelvo las formulas, solo las resuelvo cuando todas las casillas que afectan a la formula tienen un valor asignado
        
        boolean resolviendo = true;
        int cont = 0;

        if(formulas.isEmpty())
            resolviendo = false;
        
        while(resolviendo){
            String valor = resolverFormula(formulas.get(cont));
             
            if(valor.equals("##ERRORFORM")){
                cont++;
                if( cont >=formulas.size() ){
                    for(int i = 0; i<formulas.size();i++){
                        hojaString[formulas.get(i).getFil()][formulas.get(i).getCol()] = "##ERRORFORMULA";
                    }
                    resolviendo = false;
                }
            }else{
                this.hojaString[formulas.get(cont).getFil()][formulas.get(cont).getCol()] = valor;
                formulas.remove(cont);
                cont = 0;
                if(formulas.isEmpty())
                    resolviendo = false;
            }
        }

        for(int i = 0; i<nFilas;i++){
            for(int j = 0; j<nCol;j++){
                if(hojaString[i][j] != null)
                    this.hoja.setValueAt(hojaString[i][j], i, j);
            }
        }

    }

    /**
     * Metodo que resuelve una formula
     * 
     * @param form Formula
     * @return valor numérico de la formula
     */
    public String resolverFormula(Formula form) {
        String formula = form.getFormula();
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

                if (String.valueOf(this.hoja.getValueAt(casillas.get(i)[0], casillas.get(i)[1])).isEmpty()) {
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
     * 
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

     /*********************
     *   GUARDAR Y ABRIR  *
     *********************/

    /**
     * Metodo que permite guardar la hoja en un archivo
     */
    public void guardar (){

        if(archivoHoja == null || !archivoHoja.exists()){
            JFileChooser elegir = new JFileChooser();
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Hojas de calculo con extension TXT", "txt");
            elegir.setFileFilter(filtro);
            boolean pedir = true;

            while(pedir){

                int opcion = elegir.showOpenDialog(null);
                

                if(opcion == JFileChooser.CANCEL_OPTION){
                    pedir = false;

                    //Si no existe el archivo (compruebo que acabe en txt o no, ya que luego se le pondra esa extension)
                }else if(! (elegir.getSelectedFile().toString().endsWith(".txt") ? elegir.getSelectedFile().exists() : new File(elegir.getSelectedFile().toString()+".txt").exists())){

                    int opc2 = JOptionPane.showConfirmDialog(elegir, "No existe el archivo, ¿Desea crearlo?");

                    if(opc2 == JOptionPane.OK_OPTION){
                        pedir = false;
                        archivoHoja = elegir.getSelectedFile();

                        if(!archivoHoja.toString().endsWith(".txt")){ //Le pongo la extension txt
                            archivoHoja = new File(archivoHoja.toString() +".txt");
                        }
                        try{
                            archivoHoja.createNewFile();
                        }catch(Exception e){}
                    }
                }else{ //Si ya existe el archivo

                    archivoHoja = elegir.getSelectedFile().toString().endsWith(".txt") ? elegir.getSelectedFile() : new File(elegir.getSelectedFile().toString()+".txt");
                    pedir = false;
                }
            }
        }

        try{

            //Creo un escritor para escribir en la hoja
            archivoHoja.setWritable(true);
            PrintWriter escribir = new PrintWriter(archivoHoja, "UTF-8");
            
            for(int i = 0; i<this.nFilas; i++){
                for(int j = 0; j<this.nCol; j++){

                    if(j != this.nCol -1){

                        if(this.hojaString[i][j] == null){
                            escribir.print(" SEPARACION"); //Supongo que no va a haber texto, por eso uso este separador
                        }else{
                            escribir.print(this.hojaString[i][j]+"SEPARACION");
                        }
                    }else{
                        if(this.hojaString[i][j] == null){
                            escribir.print(" ");
                        }else{
                            escribir.print(this.hojaString[i][j]);
                        }
                    }
                }
                if(i != this.nFilas -1)
                    escribir.println();
            }
            
            escribir.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo");
            return;
        }

        JOptionPane.showMessageDialog(null, "Guardado en "+archivoHoja.toString()+ " correctamente.");
    }


    /************************
     *  DESHACER Y REHACER  *
     ************************/

    public void deshacer(){

        if(this.anterior!=null){

            String[][] anterior = this.anterior.getTabla();

            if(this.posterior == null){
                this.posterior = new Cambio(this.hojaString, null);
            }else{
                this.posterior = new Cambio(this.hojaString, this.posterior);
            }
            

            if(this.anterior.getSiguiente() == null){
                this.anterior = null;
            }else{
                this.anterior = this.anterior.getSiguiente();
            }

            for(int i = 0; i<nFilas; i++){
                for(int j = 0; j<nCol; j++){

                    hoja.setValueAt(anterior[i][j], i, j);
                    this.hojaString[i][j] = anterior[i][j];
                }
            }
        }
    }

    public void rehacer(){

        if(this.posterior != null){
            String[][] posterior = this.posterior.getTabla();

            this.anterior = new Cambio(this.hojaString, this.anterior);

            if(this.posterior.getSiguiente() == null){
                this.posterior = null;
            }else{
                this.posterior = this.posterior.getSiguiente();
            }

            for(int i = 0; i<nFilas; i++){
                for(int j = 0; j<nCol; j++){

                    hoja.setValueAt(posterior[i][j], i, j);
                    this.hojaString[i][j] = posterior[i][j];
                }
            }
        }
    }
}

class Cambio{


    private String[][] tabla;
    private Cambio siguiente;

    public Cambio(String[][] tabla, Cambio cambio){

        this.tabla = new String[tabla.length][];

        for(int i = 0; i< tabla.length; i++){
            this.tabla[i] = tabla[i].clone();
        }

        this.siguiente = cambio;
    }

    public void setCambio(String[][] tabla, Cambio cambio){

        for(int i = 0; i< tabla.length; i++){
            this.tabla[i] = tabla[i].clone();
        }

        this.tabla = tabla;
        this.siguiente = cambio;
    }

    public String[][] getTabla(){

        return this.tabla;
    }

    public Cambio getSiguiente(){
        return this.siguiente;
    }

}

class Formula {

    /* Atributos de la hoja de cálculo */
    private String formula;
    private int filFormula;
    private int colFormula;

    /**
     * Constructor de la clase formula
     * 
     * @param formula String que contiene las casillas de la formula y los
     *                operadores
     * @param fil     Fila a la que pertenece la formula
     * @param col     Columna a la que pertenece la formula
     */
    public Formula(String formula, int fil, int col) {
        this.formula = formula;
        this.filFormula = fil;
        this.colFormula = col;
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

}
