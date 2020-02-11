package main;

import java.util.ArrayList;

public class Parser {

	Lista<Token> componentes;
	Nodo<Token> comp;
	Token cp;
	String salida = "";
	private int contador = 0,conIF = 0;
	private boolean imprime,avanza = false;
	private ArrayList<Identificador> ide;
	public Parser(Lista<Token> c){
		componentes = c;
		comp = componentes.inicio();
		ide = new ArrayList<>();
	}
	
	public String Sintactico(){
		CD();
		return salida;
	}
	private void CD(){
		Token c = new Token(10, "", -1, -1),caux = new Token(10, "", -1, -1);
		
		try {
			c = comp.valor;
			try { 
				caux = comp.sig.valor;
			} catch (NullPointerException e) { System.out.println(e);}
		} catch (NullPointerException e) { //return; 
		}
		//if( c.getTipo() == Componente.MOD)
		M();
		
		try {
			c = comp.valor;
			try {
				caux = comp.sig.valor;
			} catch (NullPointerException e) {
				
			}
			
		} catch (NullPointerException e) { 
			salida += "\t2Error Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un \"class\"\n";
			System.out.println(salida);
			return; 
		}
		if(!c.getToken().equals("class")){
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un \"class\"\n";
			try {
				comp = comp.sig;
				c = comp.valor;
			} catch (NullPointerException e) {
				salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un identificador\n";
				return;
			}
		}else{
			try {
				comp = comp.sig;
				c = comp.valor;
			} catch (NullPointerException e) {
				salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un identificador\n";
				return;
			}
		}
		
		ID();
		try {
			c = comp.valor;
			caux = comp.ant.valor;
		} catch (NullPointerException e) {
			return;
		}
		if(!c.getToken().equals("{")){
			salida += "\tError Sintactico en linea: "+caux.getFila()+" columna "+caux.getColumna()+", se esperaba un \"{\"\n";
			System.out.println(salida);
		}else{
			/*else if( contador(((Componente)comp.valor).getToken()) != contador("}"))
		}
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \"}\"\n";*/
			try {
				comp = comp.sig;
				 c = comp.valor;
			} catch (NullPointerException e) {
				salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", esperaba un \"}\"";
				return;
			}
		}
		//System.out.println(c.getToken());
		
		
		/*if(!c.getToken().equals("}")){
			FD();
			S();
		}else if( c.getToken().equals("}") && comp.sig != null){
			
				try {
					comp = comp.sig;
					 c = comp.valor;
				} catch (NullPointerException e) {
					salida += "se esperaba un }";
					return;
				}
				if(!c.getToken().equals("}"))
					salida += "se esperaba un }";
				else if( contador(c.getToken()) != contador("{"))
					salida += "falta un \"{\"\n";
			
		}*/
		
		FD();
		S();
		try {
			c = comp.valor;
		} catch (NullPointerException e) {
			//return;
			//c = new Componente(10, "", -1, -1);
		}
		try{
			if(comp.sig != null ){
				//comp = comp.sig;
				//String errores = ""+comp.valor.getToken()+" ";
				String errores = "";
				int l = comp.valor.getFila();
				while(comp != null /*&& !comp.valor.getToken().equals("}")*/ && comp.sig != null){
					errores += comp.valor.getToken()+" ";
					comp = comp.sig;
				}
				salida += "\tError Sintactico en linea: "+l+", \""+errores+"\" no concuerda con la gramatica\n";
			}
		}catch(NullPointerException e){
			
		}
		try {
			c = comp.valor;
		} catch (NullPointerException e) {
			//return;
			//c = new Componente(10, "", -1, -1);
		}
		//System.out.println(c.getToken());
		if(!c.getToken().equals("}"))
			salida += c.getToken()+"\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un }f\n";
		//System.out.println(comp.ant.ant.valor.getToken()+"\nAnterior: "+comp.ant.valor.getToken()+"\nActual: "
			//+comp.valor.getToken()+"\nSiguiente: "+comp.sig.valor.getToken());
		if( imprime && contador("{") != contador("}"))
			salida += "Error Sintactico, faltan una o varias \"}\"\n";
		try{
			if(comp.sig != null ){
				//comp = comp.sig;
				//String errores = ""+comp.valor.getToken()+" ";
				String errores = "";
				int l = comp.valor.getFila();
				while(comp != null && !comp.valor.getToken().equals("}")){
					errores += comp.valor.getToken()+" ";
					comp = comp.sig;
				}
				salida += "\tError Sintactico en linea: "+l+", \""+errores+"\"no concuerda con la gramatica\n";
			}
		}catch(NullPointerException e){
			
		}
		
		
		//salida += "\tTermina CLASS DECLARATION\n";
		
	}
	private void FD(){
		Token c = comp.valor,caux = comp.ant.valor;
		//System.out.println(c.getToken());
		if(!c.getToken().equals("}") && c.getTipo() != Token.PR){
			VDN();
			c = comp.valor;
			if( !caux.getToken().equals(";") && c.getToken().equals("}")){
				salida += "\tError Sintactico en linea: "+caux.getFila()+" columna "+caux.getColumna()+", se esperaba un ;\n";
			}else{
				try {
					comp = comp.sig;
				} catch (NullPointerException e) {
					return;
				}
			}
			
		}
		//System.out.println(c.getToken());
		//salida += "\tTermina FIELD DECLARATION\n";
	}
	private void VDN(){
		Token c= null ,caux = null;
		M();
		//comp = comp.sig;
		T();
		try {
			c=comp.valor;
			caux=comp.sig.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if( c.getTipo() == Token.ID && caux.getToken().equals("=")){
			VDR();
		}else{
			ID();
		}
		
		//salida += "\tTermina VARIABLE DECLARATION\n";
		contador ++;
	}
	private void VDR(){
		Token c,cauxa;
		ID();
		try {
			c = comp.valor;
		} catch (NullPointerException e) {
			return;
		}
		
		//System.out.println(c.getToken());
		if( !((Token)comp.valor).getToken().equals("=") ){
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un =";
		}
		try {
			comp = comp.sig;
		} catch (NullPointerException e) {
			return;
		}
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
		} catch (NullPointerException e) {
			return;
		}
		//caux=comp.ant.valor;
		//System.out.println(c.getToken());
		if( c.getTipo() == Token.DIG ){
			IL();
		}else if(c.getTipo() == Token.VAL){
			BL();
		}else{
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un Digito o un Booleano\n";
			avanza = true;
		}
		//salida += "\tTermina VARIABLE DECLARATOR\n";
	}
	private void E(){
		TE();
	}
	private void TE(){
		Token c = null,caux = null;
		try {
			c = comp.valor;
			caux = comp.sig.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if(c.getTipo() == Token.DIG ){
			IL();
			try {
				c = comp.valor;
				caux = comp.sig.valor;
			} catch (NullPointerException e) {
				return;
			}
			
		}else if( c.getTipo() == Token.ID){
			ID();
			try {
				c = comp.valor;
				caux = comp.sig.valor;
			} catch (NullPointerException e) {
				return;
			}
		}
		//System.out.println(c.getToken());
		if( !c.getToken().matches("[>|<|>=|<=|==|!=|\\+|-|/|\\*]")){
			salida += "se esperaba un operador\n";
		}
		
		try {
			comp = comp.sig;
		} catch (NullPointerException e) {
			return;
		}
		try {
			c = comp.valor;
			caux = comp.sig.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if(c.getTipo() == Token.DIG){
			IL();
			//comp = comp.sig;
		}else if( c.getTipo() == Token.ID){
			ID();
		}
	}
	private void S(){
		Token c = null,caux = null;
		try {
			c = comp.valor;
			caux = comp.sig.valor;
		} catch (NullPointerException e) {return; }
		//Componente c = comp.valor,caux = comp.sig.valor;
		//System.out.println(c.getToken());
		if( c.getTipo() == Token.MOD && (caux.getToken().matches("(int|boolean)"))
				||
				(c.getToken().matches("(int|boolean)"))){
			VDN();
			try {
				if( !avanza )
					c = comp.valor;
				else{
					comp = comp.sig;
					c = comp.valor;
					avanza = false;
				}
			} catch (NullPointerException e) {
				return;
			}
			if( !c.getToken().equals(";")){
				salida += c.getToken()+"\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un ;\n";
			}else{
				try {
					comp = comp.sig;
					c = comp.valor;
				} catch (NullPointerException e) {
					return;
				}
			}
			
			
		}else if(c.getToken().equals("if")){
			IS();
		}else if(c.getToken().equals("while")){
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
			WS();
		}
		//salida += "\tTermina STATEMENT\n";
	}
	private void WS(){
		Token c=null,caux = null,cauxa = null;
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
			caux = comp.sig.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if(!c.getToken().equals("(")){
			salida += "\tError Sintactico en linea: "+caux.getFila()+" columna "+caux.getColumna()+", se esperaba un (\n";
		}/*else if( contador(c.getToken()) != contador(")"))
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \")\"\n";*/
		else{
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		E();
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if(!c.getToken().equals(")")){
			salida += "\tError Sintactico en linea: "+cauxa.getFila()+" columna "+cauxa.getColumna()+", se esperaba un )\n";
		}/*else if( contador(c.getToken()) != contador("("))
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \"(\"\n";*/
		else{
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if(!c.getToken().equals("{")){
			salida += "\tError Sintactico en linea: "+cauxa.getFila()+" columna "+cauxa.getColumna()+", se esperaba un {\n";
		}/*else if( contador(c.getToken()) != contador("}"))
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \"}\"\n";*/
		else{
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		
		S();
		
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());

		int n1 = contador("}"),n2 = contador("{");
		if(!c.getToken().equals("}")){
			salida += "\tError Sintactico en linea: "+cauxa.getFila()+" columna "+cauxa.getColumna()+", se esperaba un }\n";
		}/*else if( contador(c.getToken()) != contador("{"))
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \"{\"\n";*/
		else if(c.getToken().equals("}") && n1 != n2 && comp.sig != null){
			salida += "\tWSError Sintactico en linea: "+cauxa.getFila()+" columna "+cauxa.getColumna()+", se esperaba un }\n";
		}else{
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		
		//salida += "\tTermina WHILE STATEMENT\n";
	}
	private void IS(){
		try {
			comp = comp.sig;
		} catch (NullPointerException e) {
			return;
		}
		Token c=null,caux = null,cauxa = null;
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
			caux = comp.sig.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if(!c.getToken().equals("(")){
			salida += "\tError Sintactico en linea: "+caux.getFila()+" columna "+caux.getColumna()+", se esperaba un (\n";
		}/*else if( contador(c.getToken()) != contador(")"))
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \")\"\n";*/
		else{
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		
		E();
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if(!c.getToken().equals(")")){
			salida += "\tError Sintactico en linea: "+cauxa.getFila()+" columna "+cauxa.getColumna()+", se esperaba un )\n";
		}/*else if( contador(c.getToken()) != contador("("))
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \"(\"\n";*/
		else{
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if(!c.getToken().equals("{")){
			salida += "\tError Sintactico en linea: "+cauxa.getFila()+" columna "+cauxa.getColumna()+", se esperaba un {\n";
		}/*else if( contador(c.getToken()) != contador("}"))
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \"}\"\n";*/
		else{
			try {
				comp = comp.sig;
				c = comp.valor;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		
		AE();
		
		S();
		
		try {
			c = comp.valor;
			cauxa = comp.ant.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		int n1 = contador("}"),n2 = contador("{");
		if(!c.getToken().equals("}")){
			//imprime = true;
			if(cauxa.getTipo() == Token.SE || cauxa.getTipo() == Token.OP)
				salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un }\n";
			else
				salida += "\tError Sintactico en linea: "+cauxa.getFila()+" columna "+cauxa.getColumna()+", se esperaba un {\n";
			/*try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}*/
		}/*else if( contador(c.getToken()) != contador("{"))
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", falta un \"{\"\n";*/
		else if(n1 != n2 && cauxa.getToken().equals("}")){
			//salida += "\tISError Sintactico en linea: "+cauxa.getFila()+" columna "+c.getColumna()+", se esperaba un }\n";
			imprime = true;
			//comp = comp.ant;
		}else{
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		
		/*if( contador("}") != contador("{")){
			//salida += "\tISError Sintactico en linea: "+cauxa.getFila()+" columna "+c.getColumna()+", se esperaba un }\n";
			salida += "\tFalta un }\n";
		}*/
		//salida += "\tTermina IF STATEMENT\n";
	}
	private void T(){
		TS();
		//comp = comp.sig;
	}
	private void TS(){
		Token c = null, caux = null;
		try {
			c = comp.valor;
			caux = comp.sig.valor;
		} catch (NullPointerException e) {
			return;
		}
		//System.out.println(c.getToken());
		if( c.getTipo() != Token.TIPO && caux.getTipo() == Token.ID ){
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un tipo de dato\n";
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}else if(c.getTipo() != Token.TIPO && caux.getTipo() != Token.ID){
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+",  Se esperaba un tipo de dato\n";
		}else if( c.getTipo() == Token.TIPO && caux.getTipo() == Token.ID){
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		//salida += "\tTermina TYPE SPECIFIER\n";
	}
	private void M(){
		Token c = null,caux = null;
		try {
			c = comp.valor;
			try {
				caux = comp.sig.valor;
			} catch (NullPointerException e) { return;}
		} catch (NullPointerException e) { return;}
		//System.out.println(c.getToken());
		if(c.getToken().equals("class") || c.getToken().matches("(int|boolean)")){
			//comp = comp.sig;
			return;
		}else if(c.getTipo() != Token.MOD && !(caux.getToken().matches("(class|boolean|int)"))){
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un \"modificador\"\n";
			//comp = comp.sig;
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}else if(c.getTipo() != Token.MOD && (caux.getToken().matches("(class|boolean|int)"))){
			salida = "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un modificador\n";
			//comp = comp.sig;
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}else if (c.getTipo() == Token.MOD)
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		
		//salida += "\tTermina MODIFIER\n";
	}
	private void IL(){
		Token c = comp.valor,caux = comp.sig.valor,cauxa = comp.ant.valor;
		//System.out.println(c.getToken());
		if(caux.getToken().equals("=")){
			if(caux.getTipo() != Token.OP && caux.getTipo() != Token.DIG)
				salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", asignacion no valida\n";
			
		}else if(c.getToken().equals(";") || c.getTipo() != Token.DIG)
			salida += ";\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un DIGITO\n";
		else if((cauxa.getToken().equals("=") && c.getTipo() != Token.DIG) ||
				(cauxa.getToken().matches("[\\+|-|/|*]") && c.getTipo() != Token.DIG)){
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un DIGITO\n";
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}else
		//System.out.println(c.getToken());
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		//salida += "\tTermina INTEGER LITERAL\n";
	}
	private void BL(){
		Token c = comp.valor,caux = comp.sig.valor;
		//System.out.println(c.getToken());
		if(caux.getToken().equals("="))
			if(caux.getTipo() != Token.OP && caux.getTipo() != Token.DIG)
				salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", asignacion no valida\n";
		try {
			comp = comp.sig;
		} catch (NullPointerException e) {
			return;
		}
		//salida += "\tTermina BOOLEAN LITERAL\n";
	}
	private void ID(){
		Token c,caux,cauxa;
		try {
			
			c = comp.valor;
			caux = comp.sig.valor;
			cauxa = comp.ant.valor;
			//comp = comp.sig;
			//cp = comp.valor;
			String s = caux.getToken();
			//System.out.println(c.getToken());
			//if( !s.matches("[{|=]"))
				//salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", identificador no valido\n";
			if( c.getTipo() != Token.ID){
				salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un IDENTIFICADOR\n";
				return;
			}
			if(cauxa.getTipo() == Token.TIPO && caux.getToken().equals(";"))
				ide.add(new Identificador(c.getToken(),cauxa.getToken(), ""));
			else if( cauxa.getToken().equals("class"))
				ide.add(new Identificador(c.getToken(),"class",""));
			else if(caux.getToken().equals("=") && cauxa.getTipo() == Token.TIPO &&
					(comp.sig.sig.valor.getTipo() == Token.DIG || comp.sig.sig.valor.getTipo() == Token.VAL ))
				ide.add(new Identificador(c.getToken(), cauxa.getToken(),comp.sig.sig.valor.getToken() ));
			else if(caux.getToken().equals("=") && (comp.sig.sig.valor.getTipo() == Token.DIG || comp.sig.sig.valor.getTipo() == Token.TIPO)){
				Nodo<Token> xi = comp;
				comp = comp.sig.sig;
				String salida = "";
				while(!comp.valor.getToken().equals(";")){
					salida +=  comp.valor.getToken();
					comp = comp.sig;
				}
				//salida = comp.valor.getToken();
				ide.add(new Identificador(c.getToken(),"",salida,c.getFila()));
				comp = xi;
			}
				
			comp = comp.sig;
		} catch (NullPointerException e) {
			//salida += "identificador no valido\n";
		}
		//salida += "\tTermina IDENTIFIER\n";
	}
	private void AE(){
		ID();
		Token c = comp.valor;
		if( !c.getToken().equals("=") )
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un \"=\"\n";
		else
			try {
				comp = comp.sig;
				c = comp.valor;
			} catch (NullPointerException e) {
				return;
			}
		IL();
		c = comp.valor;
		if( !c.getToken().matches("[\\+|-|/|\\*]") )
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un operador aritmetico\n";
		else
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		IL();
		c = comp.valor;
		//if(comp.ant.valor)
		if( !c.getToken().equals(";") )
			salida += "\tError Sintactico en linea: "+c.getFila()+" columna "+c.getColumna()+", se esperaba un ;\n";
		else{
			try {
				comp = comp.sig;
			} catch (NullPointerException e) {
				return;
			}
		}
		
		//salida += "\tTermina ARITMETICA EXPRESSION\n";
	}
	private int contador(String t){
		int c = 0;
		Nodo<Token> aux = componentes.inicio();
		while(aux != null){
			if(aux.valor.getToken().equals(t))
				c++;
			aux = aux.sig;
			
		}
		return c;
	}
	public ArrayList<Identificador> r(){
		return ide;
	}
}
