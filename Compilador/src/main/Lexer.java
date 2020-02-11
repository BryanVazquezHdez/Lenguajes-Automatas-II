package main;

import java.util.ArrayList;

public class Lexer {
	
	private String salida;
	private static final String[] COMLEX = {"(class|if|while)","[{|}|\\;|=|(|)]","(>|<|>=|<=|==|!=|\\+|-|/|\\*)",
			"(boolean|int)","(public|private)","[1-9]?[0-9]","(true|false)","[a-z|_]+"};
	private ArrayList<Token> arr = new ArrayList<>();
	private boolean Bandera = true;

	public boolean lexico(String cad){
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
					//componentes.insertar(new Token(j, token, columna, linea));
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
		if(!salida.equals("")) Bandera = false;
		return Bandera;
	}
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
	public String retMsg(){
		return salida;
	}
	public ArrayList<Token> retComp(){
		return arr;
	}
}
