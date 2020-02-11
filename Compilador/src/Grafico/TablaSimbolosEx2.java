package Grafico;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Formato.Formato;
import main.Token;
import main.Identificador;

public class TablaSimbolosEx2 extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private JTable tabla;
	private JScrollPane jsp;
	private ArrayList<Identificador> id;
	private ModeloTabla2 mo;
	private boolean visible = false;
	private Oyente O = new Oyente(); 
	
	public TablaSimbolosEx2( JFrame fr, ArrayList<Identificador> dis){
		setTitle("Tabla de Simbolos");
		id = dis;
		setSize(650, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(fr);
		init();
	}
	
	private void init(){
		tablilla();
		jsp = new JScrollPane( tabla );
		getContentPane().add(jsp);
		
		try{
			setDefaultLookAndFeelDecorated(true);
			setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI( this );
		}catch ( Exception excepcion ) {	excepcion.printStackTrace();	}
		addWindowListener(O);
	}
	public void isVisible(boolean r){
		visible = true;
	}
	private void tablilla(){
		mo = new ModeloTabla2(id);
		tabla = new JTable( mo );
		tabla.getTableHeader().setResizingAllowed(false);
		tabla.getTableHeader().setReorderingAllowed(false);
		//tabla.getColumnModel().getColumn(2).setPreferredWidth(10);
		tabla.setFillsViewportHeight(true);
		tabla.setRowHeight(22);
		tabla.setDefaultRenderer(Object.class, new Formato());
	}
	public void actCat( ArrayList<Identificador> cd ){
		id = cd;
		tablilla();
		jsp.setViewportView(tabla);
		this.setVisible(visible);
	}
	private class Oyente extends WindowAdapter{

		@Override
		public void windowClosed(WindowEvent e) { visible = false; }
		
	}
}
	