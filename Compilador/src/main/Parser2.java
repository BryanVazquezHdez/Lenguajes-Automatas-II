package main;

import java.util.ArrayList;

public class Parser2 {

	ArrayList<Token> compo;
	Token cp;
	String salida = "";
	public static String salida2 ="";
	private int idx = 0;
	private int contador = 0,conIF = 0,conDV = 0;
	private boolean imprime,avanza = false,ideC = false;
	private ArrayList<Identificador> ide;
	private short lin;
	private final static int noValue = 30;

	public Parser2(ArrayList<Token >c){
		compo = c;
		cp = compo.get(idx);
		ide = new ArrayList<>();

	}
	
	public String Sintactico(){
		salida2 = "";
		CD();
		return salida;
	}

	private boolean Acomodar(int tipo ,String s){
		if(cp.getTipo() == tipo && cp.getToken().equals(s)){
			Avanza();
			return true;
		}else{
			error(tipo,s);
			return false;
		}
	}

	
	
	private void Avanza(){
		salida2 += "Token obtenido:"+cp.getToken()+"\n"+"Token Esperado: "+cp.getToken()+"\n-------------------------------------------\n";
		if(idx < compo.size() - 1) idx++;
		try {
			if(cp.getTipo() == Token.ID) ideC = true;
			else ideC = false;
			cp = compo.get(idx);
		} catch (IndexOutOfBoundsException e) {
			idx--;
			Token caux = compo.get(idx);
			cp = new Token(19, "", caux.getColumna(), caux.getFila());
			//error(tipo,s);
		}
	}
	private void error(int t,String to){
		switch (t) {
		case Token.PR:
			switch (to) {
			case "class":
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \"class\"\t"+cp.getToken()+"\n";
				break;
			case "if":
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \"if\"\t"+cp.getToken()+"\n";
				break;
			case "while":
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \"while\"\t"+cp.getToken()+"\n";
				break;
			}
			break;
		case Token.SE:
			switch (to) {
			case "{":
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \""+to+"\"\t"+cp.getToken()+"\n";	
				break;
			case "}":
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \""+to+"\"\t"+cp.getToken()+"\n";	
				break;
			case "(":
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \""+to+"\"\t"+cp.getToken()+"\n";	
				break;
			case ")":
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \""+to+"\"\t"+cp.getToken()+"\n";	
				break;
			case ";":
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \""+to+"\"\t"+cp.getToken()+"\n";	
				break;
			default:
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un Simbolo especial\t"+cp.getToken()+"\n";
				break;
			}
			break;
		case Token.OP:
			if(to.equals("arit"))
				salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un operador aritmetico\t"+cp.getToken()+"\n";
			else
			break;
		case Token.TIPO:
			salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \"int\" o \"boolean\"\t"+cp.getToken()+"\n";
			break;
		case Token.MOD:
			salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un \"public\" o \"private\"\t"+cp.getToken()+"\n";
			break;
		case Token.DIG:
			salida +="Error Sintactico, Fila: "+cp.getFila()+" se espeba un digito\t"+cp.getToken()+"\n";
			break;
		case Token.VAL:
			salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba \"true\" o \"false\"\t"+cp.getToken()+"\n";
			break;
		case Token.ID:
			salida +="Error Sintactico, Fila: "+cp.getFila()+" se esperaba un identificador\t"+cp.getToken()+"\n";
			break;
		case Token.ID_DIG:
			salida +="\tSintactical Error, Line: "+cp.getFila()+" token \""+cp.getToken()+"\" isn't identifier or Digit\n";
			break;
		case noValue:
			salida +="\tSintactical Error, Line: "+cp.getFila()+" Digit, Boolean or String expected\n";
			break;
		}
		salida2 += "Token obtenido:"+cp.getToken()+"\n"+"Token Esperado: "+to+"\n-------------------------------------------\n";
		
	}
	private void CD(){
		Token c = cp,cs = compo.get(idx + 1);
		if(!c.getToken().equals("class")){
			M();
		}
		c = cp;
		Acomodar(Token.PR,"class");
			
		c = cp;
		ID();
		c = cp;
		Acomodar(Token.SE,"{");
		
		//-----------------FD
		c = cp;
		//if(c.getTipo() == Componente.MOD || c.getTipo() == Componente.TIPO )

		FD();
		//-----------------S
		S();
		Acomodar(Token.SE,"}");
	}
	private void FD(){
		Token c = cp;
		if(c.getTipo() == Token.MOD || c.getTipo() == Token.TIPO){
			VDN();
			c = cp;
			Acomodar(Token.SE,";");
		}
	}
	private void VDN(){
		//conDV++;
		Token c= null ,caux = null;
		c = cp;
		String ty = null ,nom = null, val = null,alcance;
		if(c.getTipo() != Token.TIPO)
			M();
		ty = T();
		caux = ID();

		if(caux != null)
			nom = caux.getToken();
		c= cp;
		if(c.getToken().equals("=")){
			Avanza();
			val = VDR();
		}
		if( ty != null && nom != null && val == null){
			switch (ty) {
			case "int": 	val = "0"; break; 
			case "boolean": val = "false"; break;
			case "String": 	val = "\"\""; break;
			case "double": 	val = "0.0"; break;
			case "float": 	val = "0.0f"; break;
			}
			ide.add(new Identificador(nom, ty, val, caux.getFila()));
		}else if( val != null){
			ide.add(new Identificador(nom, ty, val, caux.getFila()));
		}
	}
	private String VDR(){
		Token c,cauxa;
		c = cp;
		if(c.getTipo() == Token.DIG)
			return ((Token) IL()).getToken();
		else if(c.getTipo() == Token.VAL)
			return BL();
		else
			error(noValue,"");
			return null;
	}
	private void E(){
		TE();
	}
	private void TE(){
		Token c = null,caux = null;
		c = cp;
		if(c.getTipo() == Token.DIG)
			IL();
		else
			ID();
		c = cp;
		if(c.getToken().matches("(>|<|>=|<=|==|!=)"))
			Avanza();
		else
			error(Token.OP,"log");
		c = cp;
		if(c.getTipo() == Token.DIG)
			IL();
		else
			ID();
	}
	private void S(){
		Token c = null,caux = null;
		c = cp;
		if(c.getToken().equals("if")){
			Avanza();
			IS();
		}else if(c.getToken().equals("while")){
			Avanza();
			WS();
		}else if(c.getTipo() == Token.MOD || c.getTipo() == Token.TIPO){
			VDN();
			Acomodar(Token.SE, ";");
			S();
		}else if(c.getTipo() == Token.ID) {
			AE2();
			S();
		}
	}
	private void WS(){
		Token c=null,caux = null,cauxa = null;
		c = cp;
		Acomodar(Token.SE,"(");
		E();
		Acomodar(Token.SE,")");
		Acomodar(Token.SE,"{");
		S();
		Acomodar(Token.SE,"}");
	}
	private void IS(){
		Token c;
		c = cp;
		Acomodar(Token.SE,"(");
		E();
		Acomodar(Token.SE,")");
		Acomodar(Token.SE,"{");
		AE();
		Acomodar(Token.SE,"}");
		
		S();
	}
	private String T(){
		return TS();
	}
	private String TS(){
		Token c = null, caux = null;
		c = cp;
		//if(c.getToken().matches("(int|boolean)"))
		/*if(c.getToken().equals("int"))
			Avanza();
		else if(c.getToken().equals("boolean"))
			Avanza();
		else
			error(Token.TIPO, "");*/
		switch (c.getToken()) {
		case "int": 	Avanza(); return "int"; 
		case "boolean": Avanza(); return "boolean";
		case "String": 	Avanza(); return "String";
		case "double": 	Avanza(); return "double";
		case "float": 	Avanza(); return "float";
		default:
			error(Token.TIPO, "");
			return null;
		}
	}
	private void M(){
		Token c = null,caux = null;
		c = cp;
		if(c.getToken().equals("public")) 
			Avanza();
		else if(c.getToken().equals("private")) 
			Avanza();
		else
			error(Token.MOD, "");
		}

	private Token IL(){
		Token intV = null;
		if( Acomodar(Token.DIG, cp.getToken()) ){
			intV = compo.get( idx - 1 );
			lin = (short)compo.get( idx - 1 ).getFila();
		}
		return intV;
	}
	private String BL(){
		String booleanV = cp.getToken();
		lin = (short)cp.getFila();
		Avanza();
		return booleanV;
	}
	private Token ID(){
		Token c = null,cosa = null;
		c = cp;
		if ( Acomodar(Token.ID,c.getToken()) ){
			cosa = compo.get(idx - 1 );
			//lin = (short) compo.get(idx - 1 ).getFila();
		}
		return cosa;
	}
	private void AE(){
		Token c;
		c = cp;
		ID();
		
		Acomodar(Token.SE,"=");
		
		IL();
		c = cp;
		if(c.getToken().matches("[\\+|-|/|\\*]"))
			Avanza();
		else
			error(Token.OP, "arit");
		IL();
		
		Acomodar(Token.SE,";");
	}
	private int contador(String t){
		int c = 0;
		/*Nodo<Componente> aux = componentes.inicio();
		while(aux != null){
			if(aux.valor.getToken().equals(t))
				c++;
			aux = aux.sig;
			
		}*/
		return c;
	}
	public ArrayList<Identificador> r(){
		return ide;
	}
	
	private void AE2(){
		Token c,res;
		int type = -1;
		c = cp;
		String nom = null, val1 = null, op = null , val2 = null;
		ArrayList<Token> exp = new ArrayList<>();
		res = ID();
		if( res != null)
			nom = res.getToken();
		short ind = 0;
		boolean caca = false,caca2 = false;
		ArrayList<Integer> pos = new ArrayList<>();
		
		if( nom != null){
			if( !buscar(nom)){
				ide.add(new Identificador(nom, "", "", res.getFila()/*,-1*/));
				ind = (short) (ide.size() - 1);
				caca = true;
			}
			
		}
		res =  null;
		Acomodar(Token.SE,"=");
		c = cp;
		if(c.getTipo() == Token.DIG){
			/*if( c.getToken().contains(".") && c.getToken().contains("f")){
				res = FTL();
				val1 = res.getToken();
			}else if( c.getToken().contains(".")){
				res = DBL();
				val1 = res.getToken();
			}else{*/
				res = IL();
				val1 = res.getToken();
			//}
		}else if( c.getTipo() == Token.ID){
			res = ID();
			
			if( res != null)
				val1 = res.getToken();
			
			if( val1 != null )
				if( !buscar(val1))
					if( !nom.equals(val1) )
						ide.add(new Identificador(val1, "", "", res.getFila()/*,-1*/));
					else
						caca2 = true;
				else
					caca2 = true;
		}else
			error(Token.ID_DIG, "DIG/ID");
		
		/*pos.add((int)lin);
		exp.add(res);*/
		if(res != null) {
		exp.add(res);
		}
		c = cp;
		type = c.getTipo();
		while( !c.getToken().equals(";") ){
			//simple = false;
			if( c.getToken().matches("[\\+|[-]|/|\\*]")){
				op = cp.getToken();
				pos.add(cp.getFila());
				exp.add(c);
				Avanza();
			}else{
				error(Token.AOP, "arit");
				Avanza();
				break;
			}
			c = cp;
			type = c.getTipo();
			switch (type) {
			case Token.DIG:
				/*if( c.getToken().contains(".") && c.getToken().contains("f")){
					res = FTL();
					val2 = res.getToken();	
				}else if( c.getToken().contains(".")){
					res = DBL();
					val2 = res.getToken();
				}else{*/
					res = IL();
					val2 = res.getToken();
				//}
				break;
			case Token.ID:
				res = ID();
				if( res != null)
					val2 = res.getToken();
				if( val2 != null)
					if( !buscar(val2))
						if( !nom.equals(val2) )
							ide.add(new Identificador(val2, "", "", res.getFila()/*,-1*/));
						else
							caca2 = true;
					else
						caca2 = true;
				break;
				default:
					error(Token.ID_DIG,"DIG/ID");
					Avanza();
					break;
			}
			exp.add(res);
			c = cp;
			type = c.getTipo();
		}
		c = cp;
		//update(nom, val1, true, exp);
		/*if( simple ){
			if( res.getTipo() == Token.DIG)*/
				
		//}
		/*pos.add((int)lin);
		exp.add(res);*/
		
		//DESBORRAR if( val1 != null && op != null && val2 != null){
			if( caca ){
				ide.get(ind).setExp(exp);
			}else if( nom != null)
				update(nom, val1+op+val2,caca2,exp);
		//DESBORRAR }
			
			
		Acomodar(Token.SE,";");
	}
	
	private boolean buscar(String tok){
		for (Identificador token : ide) {
 			if( token.getNombre().equals(tok))
				return true;
		}
		return false;
	}
	private void update(String tok,String val,boolean algo,ArrayList<Token> e){
		for (Identificador token : ide) {
			if( token.getNombre().equals(tok)){
					if( !algo ){
						token.setExp(e);
						token.setValor(returnS(e));
					}else{
						//token.setFaux(lin);
						token.setExp(e);
						token.setValor(returnS(e));
					}
				return;
			}
		}
	}
	private String returnS(ArrayList<Token> tokenss) {
		String aux="";
		 for (int i = 0; i < tokenss.size(); i++) {
			aux = aux + tokenss.get(i).getToken();
		 }
		 return aux;
	}
}
