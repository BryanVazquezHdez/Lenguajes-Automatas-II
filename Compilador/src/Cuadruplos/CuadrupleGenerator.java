package Cuadruplos;

import java.awt.FontMetrics;
import java.util.ArrayList;

import javax.swing.JTextPane;

import Cuadruplos.Cuadruple;
import main.Identificador;
import main.Token;

public class CuadrupleGenerator{

	private ArrayList<Identificador> tSymb;
	private ArrayList<String> cuads;
	private ArrayList<ArrayList<Cuadruple>> objs;
	private ExpressionTree tree;
	private String output = "";
	private JTextPane txtpane;

	public CuadrupleGenerator(){ }
	public CuadrupleGenerator(ArrayList<Identificador> t,JTextPane view){
		tSymb = t;
		txtpane = view;
		cuads = new ArrayList<>();
		objs = new ArrayList<>();
		//arbol = new ArbolExpresion();
	}

	public ArrayList<Identificador> retTable(){
		return tSymb;
	}
	public String retOutput(){
		return output;
	}
	public String delimiliter(){
		FontMetrics fm = txtpane.getFontMetrics(txtpane.getFont());
		int charwidth = fm.charWidth('w');
		int total_width = txtpane.getWidth();
		int num = total_width/charwidth;
		String doubleline = "";
		for (int i = 0; i < num-3; i++) {
			doubleline += "=";
		}
		return doubleline += "\n";
	}
	public ArrayList<Cuadruple> retC(){
		return tree.retList();
	}
	public ArrayList<String> retStr(){
		return cuads;
	}
	public ArrayList<ArrayList<Cuadruple>> retObjs(){
		return objs;
	}
	public void genarateCuadruples(){
		output = "";
		int i = 1;
		String men="",aux = "";
		int space = 11;
		String delimiter = delimiliter();
		for( Identificador ide: tSymb){
			if( !ide.getTipo().equals("-") && ide.getExp() != null){
				output += delimiter;
				output += String.format("%25s %n","Cadruple #"+i);
				tree = new ExpressionTree(ide,tSymb);
				ArrayList<Token> expression = ide.getExp();
				for(Token tok: expression){
					tree.add(tok);
					men += " "+tok.getToken();
				}
				output += aux += String.format("%10s %s %s %n", ide.getNombre(),"=",men);
				output += aux += String.format("%"+space+"s "+"%"+space+"s "+"%"+space+"s "+
						"%"+space+"s %n", "Operador","Operand1","Operand2","Resultado");
				String val = tree.solve();
				ide.setValor(val);
				output += aux += tree.result;
				i++;
				men = "";
				cuads.add(aux);
				objs.add(tree.retList());
				aux = "";
			}
		}
		output += delimiter;
	}
}