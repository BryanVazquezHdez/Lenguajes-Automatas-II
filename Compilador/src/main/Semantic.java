package main;

import java.text.ParseException;
import java.util.ArrayList;

import main.Identificador;
import main.Token;

public class Semantic {

	private ArrayList<Identificador> ide;
	private String output = "";

	public Semantic(ArrayList<Identificador> ar){
		ide = ar;
	}
	public String semanticAnalize(){
		semanticProcess();
		return output;
	}
	public void semanticProcess(){
		String exp = "";
		for (Identificador ident : ide) {
			
			switch (ident.getTipo()) {
			case "":
					output += "\t1st - Error Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" no esta declarada\n";
					/*if( ident.getExp() != null)
						ident.setValor(""+resultado2(ident));*/
					if( ident.getExp() != null)
						checkExpression(ident);
				break;
			case "int":
				exp = "(true|false|([0-9]+\\.[0-9]+f?)|(\".*\"))";
				if( checkRepeated(ident) ){
					output += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declarada en la linea "
							+findIndetifier(ident.getNombre()).getFila()+"\n";
				}else{
					if( ident.getValor().matches(exp)){
						output += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es un valor entero\n";
					}else{
						if( ident.getExp() != null)
							checkExpression(ident);
					}
				}
				break;
			case "double":
				exp = "(true|false|(\".*\"))";
				
				if( checkRepeated(ident) )
					output += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declaradaen la linea "+
				findIndetifier(ident.getNombre()).getFila()+"\n";
				else
					if(ident.getValor().matches(exp) || ident.getValor().contains("f") || !ident.getValor().contains("."))
						output += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es un valor double\n";
					else{
						if( ident.getExp() != null)
							checkExpression(ident);
					}
				break;
			case "float":
				
				if( checkRepeated(ident) )
					output += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declarada en la linea "+
				findIndetifier(ident.getNombre()).getFila()+"\n";
				else
					if( !ident.getValor().contains(".") && !ident.getValor().contains("f"))
						output += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es un valor flotante\n";
					else{
						if( ident.getExp() != null)
							checkExpression(ident);
					}
				break;
			case "boolean":
				
				if( checkRepeated(ident) )
					output += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declaradaen la linea "+
				findIndetifier(ident.getNombre()).getFila()+"\n";
				else 
					if(!ident.getValor().matches("(false|true)")){ //tipo correcto de dato
						output += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es un valor booleano\n";
					}
				
				break;
			case "String":
				if(!ident.getValor().matches("\".*\"")){ //tipo correcto de dato
					output += "\tError Semantico, Fila: "+ident.getFila()+" \""+ident.getValor()+"\" no es una Cadena\n";
				}
				if( checkRepeated(ident) )
					output += "\tError Semantico, Fila: "+ident.getFila()+" la variable \""+ident.getNombre()+"\" ya esta declarada en la linea "+
				findIndetifier(ident.getNombre()).getFila()+"\n";
				break;
			}
		}
	}

	private boolean checkRepeated(Identificador id){
		int rep = 0;
		boolean cosa = false, cosa2 = false;
		//System.out.print("Indetificador["+id.getNombre()+"] ->");
		for (int i = ide.indexOf(id) - 1; i >= 0; i--) {
			//System.out.print(ide.get(i).getNombre()+" ");
			Identificador ident = ide.get(i);
			if( ident.getNombre().equals(id.getNombre())){
				rep++;
				if( ident.getTipo().equals(""))
					cosa2 = true;
				if(rep > 0)
					cosa = true;
			}
		}
		//System.out.println();
		if( cosa == cosa2)
			cosa = false;
		return cosa;
	}
	
	private boolean revisaDeclarada(String nom){
		//int rep = 0;
		for (Identificador ident : ide) {
			if( ident.getNombre().equals(nom) && !ident.getValor().equals(""))
				//rep++;
				return true;
		}
		//if( rep > 1) return true;
		return false;
	}
	
	public boolean checkExpression(Identificador ide){
		boolean valido = true;
		ArrayList<Token> expre = ide.getExp();
		String valo;
		for (Token tok: expre) {
			if( tok.getType() == Token.ID ){
				if( revisaDeclarada(tok.getToken())){
					Identificador id = findIndetifier(tok.getToken());
					if( !id.getTipo().equals(ide.getTipo())){
						output += "\tError Semantico, Fila: "+tok.getLine()+" la variable \""+tok.getToken()+"\" y \""+ide.getNombre()+"\" no son de los mismos tipos\n";
						valido = false;
					}
				}
			}else if( tok.getType() == Token.DIG ){
				valo = tok.getToken();
				if( valo.contains(".")){
					if( valo.contains("f") && !ide.getTipo().equals("float")){
						output += "\tError Semantico, Fila: "+tok.getLine()+" el dato \""+tok.getToken()+"\" no es tipo \" "+ide.getTipo()+"\"\n";
						valido = false;
					}/*else if( valo.contains("f") && !ide.getTipo().equals("double")){
						salida += "\tError Semantico, Fila: "+tok.getFila()+" el dato \""+tok.getToken()+"\" no es tipo \""+ide.getTipo()+"\"\n";
						valido = false;
					}*/
					/*if( valo.contains("f")){
						if( !ide.getTipo().equals("float")){
							
						}
					}*/
				}else{
					if( !ide.getTipo().equals("int")){
						output += "\tError Semantico, Fila: "+tok.getLine()+" el dato \""+tok.getToken()+"\" no es "+ide.getTipo()+"\n";
						valido = false;
					}
				}
			}
		}
			
		return valido;
	}
	private Identificador findIndetifier(String nom){
		for (Identificador identificador : ide) {
			if (identificador.getNombre().equals(nom))
				return identificador;
		}
		return new Identificador("false", "dif", nom,-50,-10);
	}
}
