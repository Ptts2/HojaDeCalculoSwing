import java.util.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.io.*;

public class HojaDeCalculoSwing{
    public static void main(String[] args){
        creadorDeVentana();

    }


    public static void creadorDeVentana(){
        JFrame ventana = new JFrame("Hoja de calculo");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());

        /*Barra y menus*/
        JMenuBar barra = new JMenuBar(); //La barra
        JMenu archivo = new JMenu("Archivo"); //el menu
        JMenu editar = new JMenu("Editar"); //menu editar
        //Botones de archivo
        JMenuItem nueva = new JMenuItem("Nueva hoja");
        JMenuItem guardar = new JMenuItem("Guardar");
        JMenuItem cargar = new JMenuItem("Cargar");
        archivo.add(nueva);
        archivo.add(guardar);
        archivo.add(cargar);
        //Botones de editar
        JMenuItem deshacer = new JMenuItem("Deshacer");
        JMenuItem rehacer = new JMenuItem("Rehacer");
        editar.add(deshacer);
        editar.add(rehacer);
        //Añadir los menus a la barra
        barra.add(archivo);
        barra.add(editar);

        /*Tabla que hará de hoja de calculo */
        JTable hoja = new JTable(); //(cambiar 50,50 por variable, posible metodo)
        hoja.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        JTable filas = numerosFila(50); //Numeros de las filas
        DefaultTableModel modelo = new DefaultTableModel(50,50);
        modelo.addTableModelListener(new TableModelListener(){
            
            public void tableChanged(TableModelEvent e){
                int fila=-1, columna=-1;
                Object value = "";
                if(hoja.isEditing()){
                    fila = hoja.getSelectedRow();
                    columna = hoja.getSelectedColumn();
                    value = hoja.getValueAt(fila,columna);
                }

                if(fila !=-1 && columna != -1 && !String.valueOf(value).isEmpty() ){

                    if(String.valueOf(value).charAt(0) == '=')
                    hoja.setValueAt(""/*Resolver formula*/, fila, columna);

                }
                
            }
 
        });
        
        hoja.setModel(modelo);
        

        //Añado la hoja al ScollPane y los elementos al panel principal con BorderLayout
        JScrollPane panelHoja = new JScrollPane(hoja);
        panel.add(barra,BorderLayout.NORTH);
        panel.add(panelHoja, BorderLayout.CENTER);
        panel.add(filas,BorderLayout.WEST);


        //Añadir al frame
        ventana.add(panel);
        ventana.pack(); //Mejor tamaño posible
        ventana.setVisible(true);

        
    }


    public static JTable numerosFila(int nFilas){

        //Tabla que solo tiene 1 columna donde iran los numeros de las filas
        JTable filas = new JTable(nFilas+1,1);

        //Le pongo color gris
        Color gris = new Color(238,238,238);
        filas.setOpaque(true);
        filas.setFillsViewportHeight(true);
        filas.setBackground(gris);
        //Hago que no se pueda seleccionar ni editar
        filas.setRowSelectionAllowed(false);
        filas.setFocusable(false);
        //La deshabilito para que no se pueda editar
        filas.setEnabled(false);
        //Altura de las filas para que encaje con la tabla
        filas.setRowHeight(16);
        filas.setRowHeight(0,21);

        //Le pongo el texto de los numeros a las casillas
        for(int i = 1; i<=nFilas;i++){
            filas.setValueAt(String.valueOf(i),i,0);
        }

        return filas;
    }


}
