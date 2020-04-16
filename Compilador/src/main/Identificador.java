package main;

import java.util.ArrayList;

public class Identificador {
	
	private String nombre,tipo,valor,alcance;
	private int fila,faux;
	private ArrayList<Token> exp;
	public Identificador(String n, String t,String v,int f,int fa)
	{
		nombre = n; tipo = t; valor = v; fila = f; faux=fa;
	}
	
	public Identificador(String n, String t,String v,int f){
		nombre = n; tipo = t; valor = v; fila = f;
	}
	public Identificador(String n, String t,String v,int f,String al){
		nombre = n; tipo = t; valor = v; fila = f; alcance = al;
	}
	
	
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor)
	{
		this.valor=valor;
		
	}
	public int getFila(){
		return fila;
	}
	public void setFila(int fila) {
		this.fila = fila;
	}
	
	
	public int getFaux() {
		return faux;
	}
	public void setFaux(int faux) {
		this.faux = faux;
	}
	public ArrayList<Token> getExp(){
		return exp;
	}
	public void setExp(ArrayList<Token> e){
		exp = e;
	}
	public String getAlcance() {
		return alcance;
	}
	public void setAlcance(String alcance) {
		this.alcance = alcance;
	}
	public String retExpression(){
		String res = "";
		if( exp != null && exp.size() != 0){
			res = nombre+" =";
			for(Token token : exp) 
				res += " "+token.getToken();
		}
		return res;
	}
	
}
