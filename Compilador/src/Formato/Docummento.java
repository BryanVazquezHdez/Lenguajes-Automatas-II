package Formato;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Docummento extends DefaultStyledDocument{
	
	private static final long serialVersionUID = 1L;
	
	private StyleContext cont;
	private AttributeSet attr,attrBlack,attrBlue;
	private final static Color r_PR = new Color(127, 0, 145),
			r_CAD = new Color(42, 0, 255);
	public Docummento() {
		cont = StyleContext.getDefaultStyleContext();
        attr = cont.addAttribute(cont.getEmptySet(),StyleConstants.Foreground,r_PR);
        attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        attrBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, r_CAD);
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offs, str, a);

        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offs);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offs + str.length());
        int wordL = before;
        int wordR = before;
        MutableAttributeSet asnew = null;

        while (wordR <= after) {
        	String car = String.valueOf(text.charAt(wordR));
        	boolean co = car.matches("\\W");
        	if (wordR == after || co) {
                if (text.substring(wordL, wordR).matches("(\\W)*(\\s)*(if|while|for|public|private|true|false)$")){
                	asnew = new SimpleAttributeSet(attr.copyAttributes());
                	StyleConstants.setBold(asnew, true);
                	setCharacterAttributes(wordL, wordR - wordL, asnew, false);
                }else if(text.substring(wordL, wordR).matches("(\\W)*[\"].*[\"]")){
                	asnew = new SimpleAttributeSet(attrBlue.copyAttributes());
                	StyleConstants.setBold(asnew, false);
                	setCharacterAttributes(wordL, wordR - wordL, asnew, false);
                }else{
                	asnew = new SimpleAttributeSet(attrBlack.copyAttributes());
                	StyleConstants.setBold(asnew, false);
                    setCharacterAttributes(wordL, wordR - wordL,asnew,false);
                }
                wordL = wordR;
            }
            wordR++;
        }
	}
	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);

        String text = getText(0, getLength());
        int before = findLastNonWordChar(text, offs);
        if (before < 0) before = 0;
        int after = findFirstNonWordChar(text, offs);
        MutableAttributeSet asnew = null;

        if (text.substring(before, after).matches("(\\W)*(if|while|for)")) {
        	asnew = new SimpleAttributeSet(attr.copyAttributes());
        	StyleConstants.setBold(asnew, true);
            setCharacterAttributes(before, after - before, asnew, false);
        } else {

        	asnew = new SimpleAttributeSet(attrBlack.copyAttributes());
        	StyleConstants.setBold(asnew, false);
            setCharacterAttributes(before, after - before, asnew, false);
        }
	}
	
	 private int findLastNonWordChar(String text, int index) {
	        while (--index >= 0) {
	            if (String.valueOf(text.charAt(index)).matches("\\W")) {
	                break;
	            }
	        }
	        return index;
	    }

	    private int findFirstNonWordChar(String text, int index) {
	        while (index < text.length()) {
	            if (String.valueOf(text.charAt(index)).matches("\\W")) {
	                break;
	            }
	            index++;
	        }
	        return index;
	    }

}
