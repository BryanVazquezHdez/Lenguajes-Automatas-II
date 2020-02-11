package Grafico;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import Formato.Documento;
import main.Archivo;

import java.awt.Color;
import java.awt.Font;
import javax.swing.border.MatteBorder;

public class WorkSpace extends JScrollPane{
	
	private static final long serialVersionUID = 1L;
	private JTextPane txtTrabajo,taLineas;
	private Archivo arc;
	
	public WorkSpace(Ventana jf) {
		arc = new Archivo();
		Documento docc = new Documento();
		
		txtTrabajo = new JTextPane(docc);
		txtTrabajo.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				int pos = txtTrabajo.getCaretPosition();
	            Element map = txtTrabajo.getDocument().getDefaultRootElement();
	            int row = map.getElementIndex(pos);
	            Element lineElem = map.getElement(row);
	            int col = pos - lineElem.getStartOffset();
	            col++;
	            row++;
	            jf.lbColumna.setText("Columna:  "+col);
	            jf.lbLinea.setText("Fila:  "+row);
			}
		});
		txtTrabajo.setFont(new Font("Consolas", Font.PLAIN, 16));
		txtTrabajo.getDocument().addDocumentListener(new DocumentListener(){
			public String getText(){
				int caretPosition = txtTrabajo.getDocument().getLength();
				Element root = txtTrabajo.getDocument().getDefaultRootElement();
				String text = "1" + System.getProperty("line.separator");
				for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++){
					text += i + System.getProperty("line.separator");
				}
				return text;
			}
			@Override
			public void changedUpdate(DocumentEvent de) {
				taLineas.setText(getText());
			}
 
			@Override
			public void insertUpdate(DocumentEvent de) {
				taLineas.setText(getText());
			}
 
			@Override
			public void removeUpdate(DocumentEvent de) {
				taLineas.setText(getText());
			}
 
		});
		setViewportView(txtTrabajo);
		
		taLineas = new JTextPane();
		taLineas.setBorder(new MatteBorder(0, 0, 0, 1, (Color) new Color(0, 0, 0)));
		taLineas.setEditable(false);
		taLineas.setFont(new Font("Consolas", Font.PLAIN, 16));
		setRowHeaderView(taLineas);
	}

	public JTextPane getTxtTrabajo() {
		return txtTrabajo;
	}

	public JTextPane getTaLineas() {
		return taLineas;
	}
	public Archivo getArchivo(){
		return arc;
	}
}
