package main;

public class Identificador {
	
	private String nombre,tipo,valor;
	private int fila;
	public Identificador(String n, String t,String v,int f){
		nombre = n; tipo = t; valor = v; fila = f;
	}
	public String getNombre() {
		return nombre;
	}
	public String getTipo() {
		return tipo;
	}
	public String getValor() {
		return valor;
	}
	public int getFila(){
		return fila;
	}
	
	
}
