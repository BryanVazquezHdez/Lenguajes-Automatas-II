package Grafico;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import main.Identificador;

public class ModeloTabla2 extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private String[] titulo ={"Identificador","Tipo","Valor","Fila"};
	private ArrayList<Identificador> ide;

	public ModeloTabla2(ArrayList<Identificador> id){
		ide = id; 
	}
	
	@Override
	public int getColumnCount() {
		return titulo.length;
	}

	@Override
	public int getRowCount() {
			return ide.size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		Object val = null;
		Identificador id = null;
		if (ide != null)
			id = ide.get(r);
		
		switch (c) {
		case 0: val = id.getNombre(); break;
		case 1: val = id.getTipo();  break;
		case 2: val = id.getValor(); break;
		case 3: val = id.getFila(); break;
		}
		return val;
	}
	public String getColumnName(int colu) {
		return titulo[colu];
	}
}
