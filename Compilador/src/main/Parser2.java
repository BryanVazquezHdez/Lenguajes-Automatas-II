package main;

import java.util.ArrayList;

import main.Identificador;
import main.Token;

public class Parser2 {

	ArrayList<Token> tokens;
	Token tk;
	String output = "";
	public static String output2 ="";
	private int idx = 0;
	private ArrayList<Identificador> ide;
	private final static int noValue = 30,exceso = 31;
	private short lin;
	private boolean flag = false;
	
	
	public Parser2(ArrayList<Token >c){
		tokens = c;
		tk = tokens.get(idx);
		ide = new ArrayList<>();
	}
	
	public String sintacticAnalize(){
		output2 = "";
		CD();
		return output;
	}

	private boolean consume(int tipo ,String s,String sig){
		if(tk.getType() == tipo && tk.getToken().equals(s)){
			advance();
			return true;
		}else{
			error(tipo,s);
			Token tok = null;
			try {
				tok = tokens.get( idx + 1);
			} catch (IndexOutOfBoundsException e) {
				tok = new Token(-1, "", -1, -1);
			}
			if(tok.getToken().equals(sig))
				advance();
			return false;
		}
	}
	private void advance(){
		output2 += "Token Adquirido: "+tk.getToken()+"\n"+"Token Esperado: "+tk.getToken()+"\n-------------------------------------------\n";
		if(idx < tokens.size() - 1) idx++;
		try {
			tk = tokens.get(idx);
		} catch (IndexOutOfBoundsException e) {
			idx--;
			Token caux = tokens.get(idx);
			tk = new Token(19, "", caux.getColumn(), caux.getLine());
		}
	}
	private void error(int t,String to){
		switch (t) {
		case Token.PR:
			switch (to) {
			case "class":
				output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba: \"class\"";
				break;
			case "if":
				output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba: \"if\"";
				break;
			case "while":
				output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba: \"while\"";
				break;
			case "void":
				output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba: \"void\"";
				break;
			case "static":
				output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba: \"static\"";
				break;
			}
			break;
		case Token.SE:
			switch (to) {
			case "{":
				output +="Error Sintactico, linea: "+tk.getLine()+"\""+to+"\" esperado\n";	
				break;
			case "}":
				output +="Error Sintactico, linea: "+tk.getLine()+" \""+to+"\" esperado\n";	
				break;
			case "(":
				output +="Error Sintactico, linea: "+tk.getLine()+" \""+to+"\" esperado\n";	
				break;
			case ")":
				output +="Error Sintactico, linea: "+tk.getLine()+" \""+to+"\" esperado\n";	
				break;
			case ";":
				output +="Error Sintactico, linea: "+tk.getLine()+" \""+to+"\" esperado\n";	
				break;
			default:
				output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba un Simbolo Especial\n";
				break;
			}
			break;
		case Token.LOP:
			//if(to.equals("arit"))
				output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba un operador lógico\n";
			break;
		case Token.AOP:
			output +="Error Sintactico, linea: "+tk.getLine()+" token \""+tk.getToken()+"\" no es un operador artimético \n";
			break;
		case Token.Type:
			output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba un \"int\" o \"boolean\"";
			break;
		case Token.MOD:
			output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba un \"public\" or \"private\"";
			break;
		case Token.DIG:
			output +="Error Sintactico, linea: "+tk.getLine()+"se esperaba un Digito";
			break;
		case Token.VAL:
			output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba un \"true\" o \"false\"";
			break;
		case Token.ID:
				if((to.length() == 0 || to.length() != 0) && !tk.getToken().equals(to))
					output +="Error Sintactico, linea: "+tk.getLine()+" identificador \""+to+"\" se esperaba\n";
				else
					output +="Error Sintactico, linea: "+tk.getLine()+" se esperaba un identificador";
			break;
		case Token.ID_DIG:
			output +="Error Sintactico, linea: "+tk.getLine()+" token \""+tk.getToken()+"\" no es un identificador o dígito";
			break;
		case noValue:
			output +="Error Sintactico, linea: "+tk.getLine()+" se esperba un Digiti, Boolean o String";
			break;
		case exceso:
			output +="Error Sintactico, linea: "+tk.getLine()+" \""+tk.getToken()+"\" la gramática no es compatible\n";
			break;
		default:
			output +="Error Sintactico, linea: "+tk.getLine()+" "+to+"\n";
			break;
		}
		output2 += "Token adquirido: "+tk.getToken()+"\n"+" Token esperado: "+to+"\n-------------------------------------------\n";
		
	}
	
	private void CD(){
		Token c = tk,c2;
		String clss = null, id = null;
		if(!c.getToken().equals("class")){
			M();
		}
		c = tk;
		if( consume(Token.PR,"class","") )
			clss = "class";
			
		c = tk;
		c2 = ID();
		id = c2.getToken();
		if( id != null )
			ide.add(new Identificador(id, "-", "-", c2.getLine(),"Global"));
		c = tk;
		consume(Token.SE,"{","");
		
		//-----------------FD
		c = tk;
		//if(c.getTipo() == Componente.MOD || c.getTipo() == Componente.TIPO )
		FD();
		//-----------------S
		//S();
		MD();
		consume(Token.SE,"}","\uffff");
		if( tk.getType() != Token.EOF){
			while(tk.getType() != Token.EOF){
				//men = cp.getToken()+" ";	
				error(exceso, tk.getToken());
				advance();
			}
		}else
			consume(Token.EOF, "\uffff", null);
	}
	private void MD(){
		Token c = tk;
		String ty = null;
		if( !c.getToken().equals("}")){
			consume(Token.MOD, "public","static");
			consume(Token.PR, "static","void");
			if( consume(Token.PR, "void","main") )
				ty = "void";
			if( consume(Token.ID,"main","(") && ty != null){
				ide.add(new Identificador("main", "-", "", tokens.get(idx-1).getLine(),"Global"));
			}
			consume(Token.SE, "(",")");
			consume(Token.SE,")","{");
			consume(Token.SE, "{","}");
			S();
			consume(Token.SE, "}","}");
		}
		
	}
	private void FD(){
		Token c = tk, caux;
		try {
			caux = tokens.get(idx + 1);
		} catch (IndexOutOfBoundsException e) {
			caux = new Token(-1, "", -1, -1);
		}
		flag = false;
		if((c.getType() == Token.MOD || c.getType() == Token.Type) && caux.getType() != Token.PR){
			VDN();
			c = tk;
			consume(Token.SE,";","public");
		}
		flag = true;
	}
	private void VDN(){
		//conDV++;
		Token c= null,c2;
		c = tk;
		String ty = null ,nom = null, val = null,alcance;
		if(c.getType() != Token.Type)
			M();
		ty = T();
		c2 = ID();
		if( c2 != null)
			nom = c2.getToken();
		c = tk;
		if(c.getToken().equals("=")){
			advance();
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
			if( !flag )
				alcance = "Global";
			else
				alcance = "Local";
			ide.add(new Identificador(nom, ty, val, c2.getLine(),alcance));
		}else if( val != null){
			if( !flag )
				alcance = "Global";
			else
				alcance = "Local";
			ide.add(new Identificador(nom, ty, val, c2.getLine(),alcance));
		}
		//contador ++;
	}
	private String VDR(){
		Token c;
		c = tk;
		if(c.getType() == Token.DIG){
			if( c.getToken().contains(".") && c.getToken().contains("f"))
				return ((Token)FTL()).getToken();
			else if( c.getToken().contains("."))
				return ((Token)DBL()).getToken();
			else
				return ((Token)IL()).getToken();
		}else if(c.getType() == Token.VAL)
			return BL();
		else if( c.getType() == Token.STG)
			return ((Token)STGL()).getToken();
		else {
			error(noValue,"");
			return null;
		}
	}
	private void E(){
		TE();
	}
	private void TE(){
		Token c = null;
		c = tk;
		String algote = null,algote2 = null;
		if(c.getType() == Token.DIG){
			if( c.getToken().contains(".") && c.getToken().contains("f"))
				FTL();
			else if( c.getToken().contains("."))
				DBL();
			else
				IL();
		}else if(c.getType() == Token.DIG){
			algote = ((Token)ID()).getToken();
			if( algote != null && !buscar(algote))
				ide.add(new Identificador(algote, "", "", lin/*,-1*/));
		}else{
			error(Token.ID_DIG, "Digito/Identificador");
			consume(Token.ID_DIG, c.getToken(), "<");
		}
			
		
		c = tk;
		if(c.getToken().matches("(>|<|>=|<=|==|!=)")){
			advance();
		}else
			error(Token.LOP,"log");
		c = tk;
		if(c.getType() == Token.DIG){
			if( c.getToken().contains(".") && c.getToken().contains("f"))
				FTL();
			else if( c.getToken().contains("."))
				DBL();
			else
				IL();
		}else if( c.getType() == Token.ID){
			algote2 = ((Token)ID()).getToken();
			if( algote2 != null && !buscar(algote2))
				ide.add(new Identificador(algote2, "", null, lin/*,-1*/));
		}else
			error(Token.ID_DIG, "Digito/Identificador");
			
	}
	private void S(){
		Token c = null;
		c = tk;
		if(c.getToken().equals("if")){
			advance();
			IS();
		}else if(c.getToken().equals("while")){
			advance();
			WS();
		}else if(c.getType() == Token.MOD || c.getType() == Token.Type){
			VDN();
			consume(Token.SE, ";", "");
			S();
		}else if( c.getType() == Token.ID){
			AE2();
			/*
			 * DO NOT UNCOMMENT this section, it still in process...
			 * 
				Avanza();
				c = cp;
				Acomodar(Token.SE, "=", "");
				c=cp;
				Expr();
				c=cp;
				Acomodar(Token.SE, ";", "");
				c=cp;
			*/
			S();
		}
	}
	private void WS(){
		/*Token c=null,caux = null,cauxa = null;
		c = cp;*/
		consume(Token.SE,"(","");
		E();
		consume(Token.SE,")","{");
		consume(Token.SE,"{","");
		S();
		consume(Token.SE,"}","");
	}
	private void IS(){
		/*Token c;
		c = cp;*/
		consume(Token.SE,"(","");
		E();
		consume(Token.SE,")","{");
		consume(Token.SE,"{","");
		AE();
		consume(Token.SE,"}","");
		
		S();
	}
	private String T(){
		return TS();
	}
	private String TS(){
		Token c = null;
		c = tk;
		
		
		switch (c.getToken()) {
		case "int": 	advance(); return "int"; 
		case "boolean": advance(); return "boolean";
		case "String": 	advance(); return "String";
		case "double": 	advance(); return "double";
		case "float": 	advance(); return "float";
		default:
			error(Token.Type, "");
			return null;
		}
		
	}
	private void M(){
		Token c = null;
		c = tk;
		if(c.getToken().equals("public")){ 
			advance();
			//return "public";
		}else if(c.getToken().equals("private")){ 
			advance();
			//return "private";
		}else{
			error(Token.MOD, "");
			//return null;
		}
	}
	private Token IL(){
		Token intV = null;
		if( consume(Token.DIG, tk.getToken(),"") ){
			intV = tokens.get( idx - 1 );
			lin = (short)tokens.get( idx - 1 ).getLine();
		}
		return intV;
	}
	private String BL(){
		String booleanV = tk.getToken();
		lin = (short)tk.getLine();
		advance();
		return booleanV;
	}
	private Token STGL(){
		Token stringV = tk;
		lin = (short)tk.getLine();
		advance();
		return stringV;
	}
	private Token DBL(){
		Token doubleV = tk;
		//lin = (short)cp.getFila();
		advance();
		return doubleV; 
	}
	private Token FTL(){
		//String floatV = cp.getToken();
		Token floatV = tk;
		//lin = (short)cp.getFila();
		advance();
		return floatV;
	}
	private Token ID(){
		Token c = null,cosa = null;
		c = tk;
		if ( consume(Token.ID,c.getToken(),"") ){
			cosa = tokens.get(idx - 1 );
			//lin = (short) compo.get(idx - 1 ).getFila();
		}
		return cosa;
	}
	private void AE(){
		Token c,res;
		c = tk;
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
				ide.add(new Identificador(nom, "", "", res.getLine()/*,-1*/));
				ind = (short) (ide.size() - 1);
				caca = true;
			}
			
		}
		
		consume(Token.SE,"=","");
		c = tk;
		if(c.getType() == Token.DIG){
			if( c.getToken().contains(".") && c.getToken().contains("f")){
				res = FTL();
				val1 = res.getToken();
			}else if( c.getToken().contains(".")){
				res = DBL();
				val1 = res.getToken();
			}else{
				res = IL();
				val1 = res.getToken();
			}
		}else if( c.getType() == Token.ID){
			res = ID();
			
			if( res != null)
				val1 = res.getToken();
			
			if( val1 != null )
				if( !buscar(val1))
					if( !nom.equals(val1) )
						ide.add(new Identificador(val1, "", "", res.getLine()/*,-1*/));
					else
						caca2 = true;
				else
					caca2 = true;
		}else
			error(Token.ID_DIG, "");
		
		pos.add((int)lin);
		exp.add(res);
		
		c = tk;
		if(c.getToken().matches("[\\+|[-]|/|\\*]")){
			op = tk.getToken();
			pos.add(tk.getLine());
			exp.add(c);
			advance();
		}else
			error(Token.AOP, "arit");
		
		c = tk;
		if(c.getType() == Token.DIG){
			if( c.getToken().contains(".") && c.getToken().contains("f")){
				res = FTL();
				val2 = res.getToken();	
			}else if( c.getToken().contains(".")){
				res = DBL();
				val2 = res.getToken();
			}else{
				res = IL();
				val2 = res.getToken();
			}
		}else if( c.getType() == Token.ID){
			res = ID();
			if( res != null)
				val2 = res.getToken();
			if( val2 != null)
				if( !buscar(val2))
					if( !nom.equals(val2) )
						ide.add(new Identificador(val2, "", "", res.getLine()/*,-1*/));
					else
						caca2 = true;
				else
					caca2 = true;
		}else
			error(Token.ID_DIG, "");
		
		pos.add((int)lin);
		exp.add(res);
		if( val1 != null && op != null && val2 != null)
			if( caca ){
				ide.get(ind).setExp(exp);
			}else if( nom != null)
				update(nom, val1+op+val2,caca2,exp);
		
		consume(Token.SE,";","");
	}

	private void AE2(){
		Token c,res;
		int type = -1;
		c = tk;
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
				ide.add(new Identificador(nom, "", "", res.getLine()/*,-1*/));
				ind = (short) (ide.size() - 1);
				caca = true;
			}
			
		}
		
		consume(Token.SE,"=","");
		c = tk;
		if(c.getType() == Token.DIG){
			if( c.getToken().contains(".") && c.getToken().contains("f")){
				res = FTL();
				val1 = res.getToken();
			}else if( c.getToken().contains(".")){
				res = DBL();
				val1 = res.getToken();
			}else{
				res = IL();
				val1 = res.getToken();
			}
		}else if( c.getType() == Token.ID){
			res = ID();
			
			if( res != null)
				val1 = res.getToken();
			
			if( val1 != null )
				if( !buscar(val1))
					if( !nom.equals(val1) )
						ide.add(new Identificador(val1, "", "", res.getLine()/*,-1*/));
					else
						caca2 = true;
				else
					caca2 = true;
		}else
			error(Token.ID_DIG, "DIG/ID");
		
		/*pos.add((int)lin);
		exp.add(res);*/
		exp.add(res);
		c = tk;
		type = c.getType();
		while( !c.getToken().equals(";") ){
			//simple = false;
			if( c.getToken().matches("[\\+|[-]|/|\\*]")){
				op = tk.getToken();
				pos.add(tk.getLine());
				exp.add(c);
				advance();
			}else{
				error(Token.AOP, "arit");
				advance();
				break;
			}
			c = tk;
			type = c.getType();
			switch (type) {
			case Token.DIG:
				if( c.getToken().contains(".") && c.getToken().contains("f")){
					res = FTL();
					val2 = res.getToken();	
				}else if( c.getToken().contains(".")){
					res = DBL();
					val2 = res.getToken();
				}else{
					res = IL();
					val2 = res.getToken();
				}
				break;
			case Token.ID:
				res = ID();
				if( res != null)
					val2 = res.getToken();
				if( val2 != null)
					if( !buscar(val2))
						if( !nom.equals(val2) )
							ide.add(new Identificador(val2, "", "", res.getLine()/*,-1*/));
						else
							caca2 = true;
					else
						caca2 = true;
				break;
				default:
					error(Token.ID_DIG,"DIG/ID");
					advance();
					break;
			}
			exp.add(res);
			c = tk;
			type = c.getType();
		}
		c = tk;
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
			
		
		consume(Token.SE,";","");
	}
	private void Expr(){
		Token c = tk;
		Term();
		c = tk;
		while(!c.getToken().equals(";") && !c.getToken().equals(")")){
			if( c.getToken().matches("[\\+|[-]|/|\\*]")){
				advance();
			}else{
				error(Token.AOP, "arit");
			}
			c=tk;
			Term();
			c=tk;
		}
	}
	
	private void Term(){
		Token c = tk;
		if( c.getType() == Token.ID){
			advance();
		}else if(c.getType() == Token.DIG){
			advance();
		}else if( c.getToken().equals("(")){
			advance();
			c=tk;
			Expr();
			c=tk;
			consume(Token.SE, ")", "");
		}else
			error(-1, "TERM INvalido");
	}

	public ArrayList<Identificador> r(){
		return ide;
	}
	
	private void update(String tok,String val,boolean algo,ArrayList<Token> e){
		for (Identificador token : ide) {
			if( token.getNombre().equals(tok)){
					if( !algo ){
						token.setExp(e);
					}else{
						//token.setFaux(lin);
						token.setExp(e);
					}
				
				return;
			}
		}
	}
	
	
	private boolean buscar(String tok){
		for (Identificador token : ide) {
 			if( token.getNombre().equals(tok))
				return true;
		}
		return false;
	}
}
