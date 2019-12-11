import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class HojaDeCalculoSwing{
    public static void main(String[] args){
        creadorDeVentana();

    }


    public static void creadorDeVentana(){
        JFrame ventana = new JFrame("Hoja de calculo");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        JMenuBar barra = new JMenuBar(); //La barra
        JMenu archivo = new JMenu("Archivo"); //el menu
        JMenu editar = new JMenu("Editar"); //menu editar
        //Botones de archivo
        JMenuItem nueva = new JMenuItem("Nueva hoja");
        JMenuItem guardar = new JMenuItem("Guardar");
        JMenuItem cargar = new JMenuItem("Cargar");
        JTable hoja = new JTable(50,50);

        //Numero de las filas (crear metodo)
        JTable filas = new JTable(50,1);
        Color gris = new Color(204,204,204);
        filas.setOpaque(true);
        filas.setFillsViewportHeight(true);
        filas.setBackground(gris);
        filas.setRowSelectionAllowed(false);
        filas.setFocusable(false);

        
        hoja.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
        archivo.add(nueva);
        archivo.add(guardar);
        archivo.add(cargar);
        //Botones de editar
        JMenuItem deshacer = new JMenuItem("Deshacer");
        JMenuItem rehacer = new JMenuItem("Rehacer");
        editar.add(deshacer);
        editar.add(rehacer);

        barra.add(archivo);
        barra.add(editar);
        JScrollPane panelHoja = new JScrollPane(hoja);
        panel.add(barra,BorderLayout.NORTH);
        panel.add(panelHoja, BorderLayout.CENTER);
        panel.add(filas,BorderLayout.WEST);
        ventana.add(panel);
        ventana.pack();
        
        ventana.setVisible(true);



    }
}
