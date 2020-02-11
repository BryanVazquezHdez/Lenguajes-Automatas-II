package main;

import java.util.ArrayList;

public class Analizador {
	
	private static final String[] COMLEX = {"(class|if|while)","[{|}|\\;|=|(|)]","(>|<|>=|<=|==|!=|\\+|-|/|\\*)",
			"(boolean|int)","(public|private)","[1-9]?[0-9]","(true|false)","[a-z|_]+"},
			significado = {"Palabra reservada","Simbolo especial","Operador","Tipo","Modificador","Digito","Valor","Identificador"};
	private Lista<Token> componentes;
	private ArrayList<Token> arr = new ArrayList<>();
	private String salida = "";
	private boolean algo = true;
	private Lexer lex = new Lexer();
	private Parser2 p;
	private Semantic sem;
	
	public boolean lexico(String cad){
		componentes = new Lista<>();
		salida = "";
		arr.clear();				//Limpia el Arragloe de los tokens
		String token = "";
		boolean noError = false;		//Variable que controla los noErrores
		int columna = 0, linea = 1;	//Varibbles que cuentan la linea y la columna
		for (int i = 0; i < cad.length(); i++) {	//REcorremos la cadena
			 char car = cad.charAt(i);				//Tomamos el caracter segun la posicion
			 if(Character.isLetterOrDigit(car)){	// El caracter es un numero o digito?
				 token += car;				//Concatenamos el caracter en el token
				 int j = i + 1;				//Avanzamos al siguiente caracter
				 columna++;					//Incrementamos la columna
				 try {
					 while(Character.isLetterOrDigit(cad.charAt(j))){		// mientras el siguente caracter sea digito o letra
						 token += cad.charAt(j);		//concatenamos el caracter
						 j++;							//incrementamos el contador
						 columna++;						//incrementamos el no. de columna
						 if(j == cad.length()) break;	//en caso de que el indice sea igual que el tamaño de la cadena; salimos del ciclo interno
					 }
				} catch (StringIndexOutOfBoundsException e) { }
				 i = j-1;					//regresamos el contador
			 }else if( isOperator(car)){	//pregunta si el caracter es un operador
				 token += car;		//concatenamos
				 int j = i + 1;		//
				 columna++;
				 try {
					 while(isOperator(cad.charAt(j))){
						 token += cad.charAt(j);
						 j++;
						 columna++;
						 if(j == cad.length()) break;
					 }
				} catch (StringIndexOutOfBoundsException e) { }
				 i = j-1;
			 }else if(String.valueOf(car).matches("\\S")){	//el caracter de cualquier caracter que no sea un espacion en blanco,un tabulador, un retorno de carro o un salto de linea
				 token += car;		//concatenamos el caracter
				 columna++;
			 }else if(car == '\n' ){	// es un salto de linea
				 linea++;		//incrementamos la linea
				 noError = true;	//media para que el caracter no sea tomado como uno no valido
				 columna = 0;
			 }else if( car == '\t' || car == '\r' || car == ' '){
				 noError = true;    //media para que el caracter no sea tomado como uno no valido
			 }
			 //cliclo que verifica que el token cuadre con los tokens dados por la gramatica
			 for (int j = 0; j < COMLEX.length; j++) {
				if(token.matches(COMLEX[j])){
					arr.add(new Token(j, token,columna,linea));
					componentes.insertar(new Token(j, token, columna, linea));
					noError = true;  //media para que el token no sea tomado como uno no valido
					break;
				}
			}
			 
			if(noError == false){ //en caso de que el token sea uno no valido
				if(car != '\n' || car != '\r' || car != '\t' || car != ' '){// se descarta la opcion de que sea un salto de liena o un retorno de carro o un tabulador o un espacion en blanco 
					//muestra la salida de los errores por consola
					salida += "\tError en el token \""+token+"\" en la posicion Columna: "+columna+"\t Linea: "+linea+"\n";
				}
			}
			 token = "";			//vaciamos el token
			 noError = false;		//lo devolvemos como al inicio
		}
		if(!salida.equals("")) return false;
		return true;
	}
	/*Token Sintactico(Nodo<Token> nodo){
		Token cant,cl;
		boolean hayIf = false;
		
		if(nodo != null){
			cl = nodo.valor;
			cant = Sintactico(nodo.sig);
			switch (cl.getTipo()) {
			case Token.MOD:
				int ante = cant.getTipo();
				if( ante != Token.TIPO && !cant.getToken().equals("class"))
					//System.out.println("se esperaba un modificador");
					salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", se esperaba un tipo de dato o la palabra class\n";	
				else if (ante == Token.ID)
					salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", se esperaba la palabra class\n";
				break;
			case Token.ID:
				String s = cant.getToken();
				//if( hayIf)
					if(!s.matches("[{|=|\\;]"))
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", identificador no valido\n";
					else if(nodo.ant.valor.getToken().equals("class"))
						//
					
				break;
			case Token.SE:
				String simbolo = cl.getToken();
				boolean error = false;
				switch (simbolo) {
				case "(":
					error = contador(simbolo) != contador(")");
					if(error)
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", falta un \")\"\n";
					break;
				case "{":
					error = contador(simbolo) != contador("}");
					if(error)
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", falta un \"}\"\n";
					break;
				case ")":
					error = contador(simbolo) != contador("(");
					if(error)
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", falta un \"(\"\n";				
					break;
				case "}":
					error = contador(simbolo) != contador("{");
					if(error)
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", falta un \"{\"\n";				
					break;
				}
				break;
			case Token.TIPO:
				if(nodo.ant != null && nodo.ant.valor.getTipo() == Token.MOD)
					if( cant.getTipo() != Token.ID)
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", se esperaba un identificador\n";
				break;
			case Token.PR:
				String p = cl.getToken();
				switch (p) {
				case "if":
					if(!cant.getToken().equals("("))
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", se esperaba un \"(\"";
					hayIf = true;
					break;
				case "while":
					if(!cant.getToken().equals("("))
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", se esperaba un \"(\"";
					break;
				}
				break;
			case Token.OP:
				if( nodo.ant.valor.getTipo() != Token.DIG && nodo.ant.valor.getTipo() != Token.ID)
					salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", se esperaba una constante\n";
				if( cant.getTipo() != Token.DIG && nodo.ant.valor.getTipo() != Token.ID)
					salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", se esperaba una constante\n";
				break;
			case Token.DIG: case Token.VAL:
				if(nodo.ant.valor.getToken().equals("="))
					if(cant.getTipo() != Token.OP && cant.getTipo() != Token.DIG && !cant.getToken().equals(";"))
						salida += "\tError Sintactico en linea: "+cl.getFila()+" columna "+cl.getColumna()+", asignacion no valida\n";
				break;
			
			}
			return cl;
		}
		return new Token(9, "", 0, 0);
	}*/
	private static boolean isOperator(char o){
		switch(o){
			case '+' : return true;
			case '-' : return true;
			case '/' : return true;
			case '*' : return true;
			case '<' : return true;
			case '>' : return true;
			case '=' : return true;		
		}
		return false;
	}
	public ArrayList<Token> retArr(){
		return arr;
	}
	public ArrayList<Identificador> retArrS(){
		try {
			return p.r();
		} catch (NullPointerException e) {
			return new ArrayList<Identificador>(0);
		}
		
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
	public String compilacion(String entrada){
		salida = "";
		if( lex.lexico(entrada)){
			salida += "\tNo hay errores Lexicos\n";
			//Sintactico(componentes.inicio());
			//p = new Parser(componentes);
			arr = lex.retComp();
			p = new Parser2(arr);
			salida += p.Sintactico();
		}
		if( salida.equals("\tNo hay errores Lexicos\n") ){
			salida += "\tNo hay errores Sintacticos\n";
			sem = new Semantic(p.r());
			salida += sem.Semantico();
		}
		if( salida.endsWith("Sintacticos\n")){
			salida += "\tNo hay errores Semanticos\n";
		}
		return salida;
	}
	
}
