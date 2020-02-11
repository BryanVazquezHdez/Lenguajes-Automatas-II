package Formato;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Documento2 extends DefaultStyledDocument{
	
	private static final long serialVersionUID = 1L;
	private StyleContext cont;
	private AttributeSet attr,attrBlack;
	private final static Color r_PR = new Color(127, 0, 145);
			//r_CAD = new Color(42, 0, 255);
	private ArrayList<coloreado> a;
	String currentString,oldString;
	public Documento2() {
		cont = StyleContext.getDefaultStyleContext();
        attr = cont.addAttribute(cont.getEmptySet(),StyleConstants.Foreground,Color.red);
        attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        //attrBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, r_CAD);
	}
	
	@Override
	public void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException {
		super.insertString(arg0, arg1, arg2);
		algo();
	}
	
	@Override
	public void remove(int arg0, int arg1) throws BadLocationException {
		// TODO Auto-generated method stub
		super.remove(arg0, arg1);
		algo();
	}
	
	private synchronized void algo() throws BadLocationException{
		MutableAttributeSet asnew = null;
		a = new ArrayList<>();
		colorea();
		asnew = new SimpleAttributeSet(attrBlack.copyAttributes());
    	StyleConstants.setBold(asnew, false);
        setCharacterAttributes(0,getText(0, getLength()).length(),asnew,true);
		
		for (int i = 0; i < a.size(); i++) {
			asnew = new SimpleAttributeSet(attr.copyAttributes());
        	StyleConstants.setBold(asnew, true);
            setCharacterAttributes(a.get(i).pos, a.get(i).palabra.length(),asnew,true);
		}
	}
	
	private void colorea() throws BadLocationException{
		String t = getText(0, getLength()), P = "";
		//JOptionPane.showMessageDialog(null, t);
		currentString = t;
		t += " ";
		int delimitador =  0;
		
		for (int i = 0; i < t.length(); i++) {
			char car = t.charAt(i);
			if(car == '"'){
				i++;
				car = t.charAt(i);
				P += car;
				while(car != '"'){
					P += car;
					i++;
					car = t.charAt(i);
				}
				
				i++;
				car = t.charAt(i);
				P += car;
				delimitador = i;
				if(P.length() > 0){
					//if(palabraRes(P)){
						a.add(new coloreado(delimitador-P.length(), P));
					//}
					P = "";
				}
			}
		}
		oldString = currentString;
	}
	private boolean palabraRes(String palabra){
		if( palabra.matches("(if|while|public|private|true|false|class|boolean|int)"))
			return true;
		return false;
	}
	class coloreado{
		int pos;
		String palabra;
		coloreado(int po, String pa){
			pos = po;
			palabra = pa;
		}
	}
}
