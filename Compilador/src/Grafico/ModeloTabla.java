package Grafico;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

import main.Token;

public class ModeloTabla extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private String[] titulo ={"Tipo","Token","Columna","Renglon"};
	private ArrayList<Token> ComLex;
	private boolean chico;

	public ModeloTabla(ArrayList<Token> cl,boolean c){
		ComLex = cl;		chico = c;
	}
	
	@Override
	public int getColumnCount() {
		if( chico )
			return 2;
		else
			return 4;
	}

	@Override
	public int getRowCount() {
		if (ComLex == null)
			return 0;
		else
			return ComLex.size();
	}

	@Override
	public Object getValueAt(int r, int c) {
		Object val = null;
		Token cl = null;
		if (ComLex != null)
			cl = ComLex.get(r);
		
		switch (c) {
		case 0: 
			if( cl != null)
				val = cl.getDesc();
			else
				val = "";
			break;
		case 1: 
			if( cl != null)
				val = cl.getToken();
			else
				val = "";
			break;
		case 2: val = cl.getColumna();
			break;
		case 3: val = cl.getFila();
			break;
		}
		return val;
	}
	public String getColumnName(int colu) {
		return titulo[colu];
	}
}
