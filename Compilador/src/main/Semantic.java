package main;

import java.text.ParseException;
import java.util.ArrayList;

public class Semantic {

	private ArrayList<Identificador> ide;
	private String salida = "";

	public Semantic(ArrayList<Identificador> ar){
		ide = ar;
	}
	public String Semantico(){
		algo();
		revisaRedeclaracionVariable();
		return salida;
	}
	
	
	
	
	public void algo()
	{
		String exp = "";
		for (Identificador ident : ide) {
			
			switch (ident.getTipo()) {
			case "":
					salida += "\t1st - Error Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" no esta declarada\n";
					
			break;
			
			
			case "int":
				exp = "(true|false|([0-9]+\\.[0-9]+f?)|(\".*\"))";
				if( ChecarRepetido(ident) ){
					salida += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declarada en la linea "
							+buscaIdentificador(ident.getNombre()).getFila()+"\n";
				}else{
					if( ident.getValor().matches(exp)){
						salida += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es un valor entero\n";
					}else{
						if( ident.getExp() != null)
							checarExpresion(ident);
					}
				}
				break;
			case "double":
				exp = "(true|false|(\".*\"))";
				
				if( ChecarRepetido(ident) )
					salida += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declaradaen la linea "+
				buscaIdentificador(ident.getNombre()).getFila()+"\n";
				else
					if(ident.getValor().matches(exp) || ident.getValor().contains("f") || !ident.getValor().contains("."))
						salida += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es un valor double\n";
					else{
						if( ident.getExp() != null)
							checarExpresion(ident);
					}
				break;
			case "float":
				
				if( ChecarRepetido(ident) )
					salida += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declarada en la linea "+
				buscaIdentificador(ident.getNombre()).getFila()+"\n";
				else
					if( !ident.getValor().contains(".") && !ident.getValor().contains("f"))
						salida += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es un valor flotante\n";
					else{
						if( ident.getExp() != null)
							checarExpresion(ident);
					}
				break;
			case "boolean":
				
				if( ChecarRepetido(ident) )
					salida += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declaradaen la linea "+
				buscaIdentificador(ident.getNombre()).getFila()+"\n";
				else 
					if(!ident.getValor().matches("(false|true)")){ //tipo correcto de dato
						salida += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es un valor booleano\n";
					}
				
				break;
			case "String":
				if(!ident.getValor().matches("\".*\"")){ //tipo correcto de dato
					salida += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es una Cadena\n";
				}
				if( ChecarRepetido(ident) )
					salida += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declarada en la linea "+
							buscaIdentificador(ident.getNombre()).getFila()+"\n";
				break;
			}
		}
	}
	
	
	
	
	
private boolean revisaDeclarada(String nom){
		
		for (Identificador ident : ide) {
			if( ident.getNombre().equals(nom) && !ident.getValor().equals(""))
				//rep++;
				return true;
		}
		
		return false;
	}
	
public boolean checarExpresion(Identificador ide){
		boolean valido = true;
		ArrayList<Token> expre = ide.getExp();
		String valo;
		for (Token tok: expre) {
			if( tok.getTipo() == Token.ID ){
				if( revisaDeclarada(tok.getToken())){
					Identificador id = buscaIdentificador(tok.getToken());
					if( !id.getTipo().equals(ide.getTipo())){
						salida += "\tError Semantico, Fila: "+tok.getFila()+" la variable \""+tok.getToken()+"\" y \""+ide.getNombre()+"\" no son de los mismos tipos\n";
						valido = false;
					}
				}
			}else if( tok.getTipo() == Token.DIG ){
				valo = tok.getToken();
				if( valo.contains(".")){
					if( valo.contains("f") && !ide.getTipo().equals("float")){
						salida += "\tError Semantico, Fila: "+tok.getFila()+" el dato \""+tok.getToken()+"\" no es tipo \" "+ide.getTipo()+"\"\n";
						valido = false;
					}
					
				}else{
					if( !ide.getTipo().equals("int")){
						salida += "\tError Semantico, Fila: "+tok.getFila()+" el dato \""+tok.getToken()+"\" no es "+ide.getTipo()+"\n";
						valido = false;
					}
				}
			}
		}
			
		return valido;
	}
	
private boolean ChecarRepetido(Identificador id){
		int rep = 0;
		boolean cosa = false, cosa2 = false;
		
		for (int i = ide.indexOf(id) - 1; i >= 0; i--) {
			
			Identificador ident = ide.get(i);
			if( ident.getNombre().equals(id.getNombre())){
				rep++;
				if( ident.getTipo().equals(""))
					cosa2 = true;
				if(rep > 0)
					cosa = true;
			}
		}
		
		if( cosa == cosa2)
			cosa = false;
		return cosa;
	}


private Identificador buscaIdentificador(String nom){
		for (Identificador identificador : ide) {
			if (identificador.getNombre().equals(nom))
				return identificador;
		}
		return new Identificador("false", "dif", nom,-50,-10);
	}

private void revisaRedeclaracionVariable() {
	
}
}
