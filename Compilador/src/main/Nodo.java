package main;

public class Nodo<E> {
	E valor;
	Nodo<E> ant,sig;
	
	public Nodo(E v){
		valor = v;
	}
}
