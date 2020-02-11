package Grafico;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import Formato.Documento2;
import Formato.Formato;
import main.Analizador;
import main.Archivo;
import main.Token;
import main.Identificador;
import main.Parser2;

import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

public class Ventana extends JFrame{
	
	private static final long serialVersionUID = 1L;
	//Componentes Nativos
	private JTable table;
	private JTextPane txtTrabajo,txtConsola,txtLog;
	JLabel lbColumna,lbLinea;
	private JMenuItem mnitNuevo,mnitGuardar,mnitAbrir,mnitSalir,mnitEjecutar,mnitAcerca;
	private JMenuItem mnitCopiar,mnitPegar,mnitCortar;
	private JMenuItem mnitCerrar;
	private JMenuItem mnitpCopiar,mnitpPegar,mnitpCortar,mnitTabTok,mnitTabSimb;
	private JButton btnNuevo,btnGuardar,btnEjecutar;
	private JTabbedPane workSpace;
	private JScrollPane jstb;
	//Componentes Personalizados
	private FrameAcerca FAD;
	private TablaSimbolosEx TSE;
	private TablaSimbolosEx2 TS;
	private Archivo ach;
	private Analizador bot = new Analizador();
	private ModeloTabla mt;
	private ModeloTabla2 mt2;
	//Oyentes
	private Oyente O = new Oyente();
	
	public Ventana() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Ventana.class.getResource("/Imagenes/icono-96.png")));
		setTitle("vaja");
		setSize(1200, 720);
		init();						//Metodo que inicializa todos los componentes
		try
		{
			setDefaultLookAndFeelDecorated(true);
			setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI( this );			
		}
		catch ( Exception excepcion ) {	excepcion.printStackTrace();	}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void init(){
		JSplitPane panel3 = new JSplitPane();
		TSE = new TablaSimbolosEx(Ventana.this, bot.retArr());		//Instancia de la tabla de simbolos extendida
		TS = new TablaSimbolosEx2(this, bot.retArrS());
		FAD = new FrameAcerca(this);					//Instancia de la ventana Acerca de
		workSpace = new JTabbedPane();					//Instancia del contenedor del area de trabajo
		JSplitPane panel = new JSplitPane();
		panel.setDividerSize(2);
		panel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(panel, BorderLayout.CENTER);
		
		JSplitPane panel2 = new JSplitPane();
		panel2.setResizeWeight(0.9);
		panel.setTopComponent(panel2);
		
		//panel2.setResizeWeight(0.99);
		//panel.setResizeWeight(0.1);
		panel3.setResizeWeight(0.7);
		
		
		//Seccion del area de trabajo
		WorkSpace WS = new WorkSpace(this);
		txtTrabajo = WS.getTxtTrabajo();
		workSpace.addTab("Nuevo", WS);
		panel2.setLeftComponent(workSpace);
		//Seccion de la consola
		Documento2 doc = new Documento2();
		txtConsola = new JTextPane(doc);
		txtConsola.setFont(new Font("Consolas", Font.PLAIN, 13));
		txtConsola.setEditable(false);
		JScrollPane jscc = new JScrollPane(txtConsola);
		JTabbedPane tpConsola = new JTabbedPane(JTabbedPane.TOP);
		tpConsola.addTab("Consola",new ImageIcon(Ventana.class.getResource("/Imagenes/consola-16.png")),jscc);
		panel3.setLeftComponent(tpConsola);
		panel.setBottomComponent(panel3);
		
		txtLog = new JTextPane();
		txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
		panel3.setRightComponent(new JScrollPane(txtLog));
		
		//Seccion de la tabla de simbolos pequeña
		creaTabla(null);
		jstb = new JScrollPane(table);
		jstb.setPreferredSize(new Dimension(200, 402));
		panel2.setRightComponent(jstb);
		//Seccion del contador de lineas y columnas
		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		lbLinea = new JLabel("Linea:  1");
		Border border = lbLinea.getBorder();
		Border margin = new EmptyBorder(0,0,0,50);
		lbLinea.setBorder(new CompoundBorder(border, margin));
		panel_1.add(lbLinea);
		
		lbColumna = new JLabel("Columna:  1");
		border = lbColumna.getBorder();
		margin = new EmptyBorder(0,0,0,200);
		lbColumna.setBorder(new CompoundBorder(border, margin));
		panel_1.add(lbColumna);	
		//Seccion de la barra de herramientas
		JToolBar toolBar = new JToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		btnNuevo = new JButton("");
		btnNuevo.setToolTipText("Nueva Ventana");
		btnNuevo.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/nuevo-16.png")));
		btnNuevo.addActionListener(O);
		toolBar.add(btnNuevo);
		toolBar.addSeparator(new Dimension(5,0));
		
		btnGuardar = new JButton("");
		btnGuardar.setToolTipText("Guardar");
		btnGuardar.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/guardar-16.png")));
		btnGuardar.addActionListener(O);
		toolBar.add(btnGuardar);
		toolBar.addSeparator(new Dimension(5,0));
		
		JToolBar toolBar_1 = new JToolBar();
		toolBar.add(toolBar_1);
		
		btnEjecutar = new JButton("");
		btnEjecutar.setToolTipText("Ejecutar");
		btnEjecutar.setSize(20, 20);
		ImageIcon im = new ImageIcon(Ventana.class.getResource("/Imagenes/hoja.jpeg"));
		Image r = im.getImage().getScaledInstance(btnEjecutar.getWidth(), btnEjecutar.getHeight(), Image.SCALE_SMOOTH);
		Icon i = new ImageIcon(r);
		btnEjecutar.setIcon(i);
		btnEjecutar.addActionListener(O);
		toolBar_1.add(btnEjecutar);
		//Seccion del menu de opciones
		creaMenu();				//metodo que crea y añade todos los elementos del menu
	}
	private void creaMenu(){
		JMenuBar menuBar = new JMenuBar();
		JPopupMenu mnppEdicion = new JPopupMenu();
		JPopupMenu mnppCerrar = new JPopupMenu();
		setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo");
		menuBar.add(mnArchivo);
		
		mnitNuevo = new JMenuItem("Nuevo");
		mnitNuevo.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/nuevo-16.png")));
		mnitNuevo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnitNuevo.addActionListener(O);
		mnArchivo.add(mnitNuevo);
		mnArchivo.addSeparator();
		
		mnitGuardar = new JMenuItem("Guardar");
		mnitGuardar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		mnitGuardar.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/guardar-16.png")));
		mnitGuardar.addActionListener(O);
		mnArchivo.add(mnitGuardar);
		
		mnitAbrir = new JMenuItem("Abrir");
		mnitAbrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnitAbrir.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/abrir-16.png")));
		mnitAbrir.addActionListener(O);
		mnArchivo.add(mnitAbrir);
		mnArchivo.addSeparator();
		
		mnitSalir = new JMenuItem("Salir");
		mnitSalir.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/salir-16.png")));
		mnitSalir.addActionListener(O);
		mnArchivo.add(mnitSalir);
		
		JMenu mnEditar = new JMenu("Editar");
		menuBar.add(mnEditar);
		
		mnitCopiar = new JMenuItem("Copiar");
		mnitCopiar.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/copiar-16.png")));
		mnitCopiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnitCopiar.addActionListener(O);
		mnEditar.add(mnitCopiar);
		
		mnitPegar = new JMenuItem("Pegar");
		mnitPegar.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/pegar-16.png")));
		mnitPegar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnitPegar.addActionListener(O);
		mnEditar.add(mnitPegar);
		
		mnitCortar = new JMenuItem("Cortar");
		mnitCortar.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/cortar-16.png")));
		mnitCortar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnitCortar.addActionListener(O);
		mnEditar.add(mnitCortar);
		
		JMenu mnCorrer = new JMenu("Correr");
		menuBar.add(mnCorrer);
		
		mnitEjecutar = new JMenuItem("Ejecutar");
		mnitEjecutar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		mnitEjecutar.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/ejecutar-16.png")));
		mnitEjecutar.addActionListener(O);
		mnCorrer.add(mnitEjecutar);
		
		JMenu mnVer = new JMenu("Ver");
		menuBar.add(mnVer);
		
		mnitTabTok = new JMenuItem("Tabla de Tokens Extendida");
		mnitTabTok.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/table-16.png")));
		mnitTabTok.addActionListener(O);
		mnVer.add(mnitTabTok);
		
		mnitTabSimb = new JMenuItem("Tabla de Simbolos");
		mnitTabSimb.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/table-16.png")));
		mnitTabSimb.addActionListener(O);
		mnVer.add(mnitTabSimb);
		
		JMenu mnAyuda = new JMenu("Ayuda");
		mnAyuda.setIcon(null);
		menuBar.add(mnAyuda);
		
		mnitAcerca = new JMenuItem("Acerca Vaja...");
		mnitAcerca.setIcon(new ImageIcon(Ventana.class.getResource("/Imagenes/info-16.png")));
		mnitAcerca.addActionListener(O);
		mnAyuda.add(mnitAcerca);
		
		mnitpCopiar = new JMenuItem("Copiar");
		mnitpCopiar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		mnitpCopiar.addActionListener(O);
		mnppEdicion.add(mnitpCopiar);
		
		mnitpPegar = new JMenuItem("Pegar");
		mnitpPegar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		mnitpPegar.addActionListener(O);
		mnppEdicion.add(mnitpPegar);
		
		mnitpCortar = new JMenuItem("Cortar");
		mnitpCortar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		mnitpCortar.addActionListener(O);
		mnppEdicion.add(mnitpCortar);
		txtTrabajo.setComponentPopupMenu(mnppEdicion);
		
		mnitCerrar = new JMenuItem("Cerrar");
		mnitCerrar.addActionListener(O);
		mnppCerrar.add(mnitCerrar);
		workSpace.setComponentPopupMenu(mnppCerrar);
		
	}
	
	private void creaTabla(ArrayList<Token> a){
		mt= new ModeloTabla(a,true);								//Creaacion del modelo de tabla
		table = new JTable();										//Instancia de la tabla
		table.setModel(mt);
		table.getTableHeader().setResizingAllowed(false);			//Quita la opcion de redimencionar la cabezera de la tabla
		table.getTableHeader().setReorderingAllowed(false);			//quira la opcion de reordenar la cabezera de la tabla
		table.setDefaultRenderer(Object.class, new Formato());		//Indica el formato de las celdas
		table.setRowHeight(22);										//Tamaño del renglon
	}
	private void creaTabla2(ArrayList<Identificador> a){
		mt2 = new ModeloTabla2(a);								//Creaacion del modelo de tabla
		table = new JTable();										//Instancia de la tabla
		table.setModel(mt);
		table.getTableHeader().setResizingAllowed(false);			//Quita la opcion de redimencionar la cabezera de la tabla
		table.getTableHeader().setReorderingAllowed(false);			//quira la opcion de reordenar la cabezera de la tabla
		table.setDefaultRenderer(Object.class, new Formato());		//Indica el formato de las celdas
		table.setRowHeight(22);										//Tamaño del renglon
	}
	
	public static void main(String[] args) {
		new Ventana();
	}

	class Oyente implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource();
			if( obj == mnitNuevo || obj == btnNuevo ){
				int lim = 15;
				if( workSpace.getTabCount() < lim){
					workSpace.addTab("Nuevo", new WorkSpace(Ventana.this));		//Crea una nueva ventaña con un nuevo area de trabajo
					workSpace.setSelectedIndex(workSpace.getTabCount()-1);		//Posiciona sobre la nueva pestaña
				}else
					JOptionPane.showMessageDialog(Ventana.this, "No es posible tener mas de "+lim+" pestañas abiertas");
				
			}else if( obj == mnitGuardar || obj == btnGuardar ){
				WorkSpace ks = (WorkSpace) workSpace.getComponentAt(workSpace.getSelectedIndex());	//Determina el area de trabajo segun la pestaña actual
				txtTrabajo = ks.getTxtTrabajo();		//
				ach = ks.getArchivo();
				try {
					ach.SaveFile(txtTrabajo,Ventana.this);	//Metodo que Guarda Archivo
				} catch (IOException e1) {
						e1.printStackTrace();
				}
			}else if( obj == mnitAbrir ){
				int i = workSpace.getSelectedIndex();
				WorkSpace ks = (WorkSpace) workSpace.getComponentAt(i);
				ach = ks.getArchivo();
				String texto = ach.OpenFile(Ventana.this), //Metodo que abre el Arhivo
						n = ach.nombreArch,
						dato = ach.txt;
				//JOptionPane.showMessageDialog(null, dato);
				boolean p1 = !texto.equals("");		//Verifica si se abrio algun archivo
				if( p1 ){
															//obtiene el indice de la pestaña actual
					workSpace.setTitleAt(i, n);					//renombra la ventana segun el indice
															//Determina el area de trabajo segun la pestaña actual
					txtTrabajo = ks.getTxtTrabajo();
					txtTrabajo.setText(texto);	//Escribe el texto del Arhivo
				}
			}else if( obj == mnitEjecutar || obj == btnEjecutar ){
				int d = workSpace.getSelectedIndex();						//indice de la pestaña actual
				WorkSpace ks = (WorkSpace) workSpace.getComponentAt(d);		//Determina el area de trabajo segun la pestaña actual
				txtTrabajo = ks.getTxtTrabajo();							//...
				if(!txtTrabajo.getText().equals("")){
					String cs = bot.compilacion(txtTrabajo.getText());				//Obtiene una cadema con los errores lexicos en caso de haber alguno
					txtLog.setText(Parser2.salida2);
					creaTabla(bot.retArr());									//llena la tabla de simbolos pequeña
					jstb.setViewportView(table);
					txtConsola.setText(cs);										//muestra los errores en caso de haber alguno
					TSE.actCat(bot.retArr());									//llena la tabla de simbolos extendida
					TS.actCat(bot.retArrS());
				}else
					JOptionPane.showMessageDialog(null, "No hay texto para analizar");
			}else if( obj == mnitTabTok){
				TSE.isVisible(true);				//hace visible la tabla de simbolos extendida
				TSE.actCat(bot.retArr());			//llena la tabla de simbolos extendida
			}else if( obj == mnitTabSimb){
				TS.isVisible(true);
				TS.actCat(bot.retArrS());
			}else if( obj == mnitAcerca){
				FAD.setVisible(true);		//hace visible la ventana Acerca de
			}else if( obj == mnitCopiar || obj == mnitpCopiar ){
				WorkSpace ks = (WorkSpace) workSpace.getComponentAt(workSpace.getSelectedIndex());		//Determina el area de trabajo segun la pestaña actual
				txtTrabajo = ks.getTxtTrabajo();							//...
				txtTrabajo.copy();
			}else if( obj == mnitPegar || obj == mnitpPegar ){
				WorkSpace ks = (WorkSpace) workSpace.getComponentAt(workSpace.getSelectedIndex());		//Determina el area de trabajo segun la pestaña actual
				txtTrabajo = ks.getTxtTrabajo();							//...
				txtTrabajo.paste();
			}else if( obj == mnitCortar || obj == mnitpCortar){
				WorkSpace ks = (WorkSpace) workSpace.getComponentAt(workSpace.getSelectedIndex());		//Determina el area de trabajo segun la pestaña actual
				txtTrabajo = ks.getTxtTrabajo();							//...
				txtTrabajo.cut();
			}else if( obj == mnitCerrar){
				int d = workSpace.getSelectedIndex();						//indice de la pestaña actual
				WorkSpace ks = (WorkSpace) workSpace.getComponentAt(d);		//Determina el area de trabajo segun la pestaña actual
				txtTrabajo = ks.getTxtTrabajo();							//...
				ach = ks.getArchivo();
				String dato = ach.txt,dato2 = txtTrabajo.getText();
				//JOptionPane.showMessageDialog(null, dato+"\n"+dato2);
				if( !dato.equals(dato2)){
					//JOptionPane.showMessageDialog(null, "cerrado manual");
					Object[] opciones = { "Si","No","Cancelar"};
					
					int n = JOptionPane.showOptionDialog(Ventana.this, "¿Desea guardar los cambios?"
								, "Ward", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE
								, null, opciones, opciones[0]);
						if( n == JOptionPane.YES_OPTION){
							try {
								ach.SaveFile(txtTrabajo, Ventana.this);	//Metodo que Guarda el Archivo
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							//System.exit(0);
						}else if( n == JOptionPane.NO_OPTION)
							workSpace.remove(d);
				}else{
					//JOptionPane.showMessageDialog(null, "cerrado automatico");
					workSpace.remove(d);
				}
				/*try {
					ach.SaveFile(txtTrabajo,Ventana.this);					//Metodo que Guarda Archivo
				} catch (IOException e1) {
						e1.printStackTrace();
				}*/
				//					//quita la pestaña actual
			}else{
				System.exit(0);		//Sale de la aplicacion
			}
		}
	}
}
