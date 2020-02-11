package main;

public class Lista<E> {
	Nodo<E> ini,fin;
	int tamaño = 0;
	
	boolean raiz(){
		return fin == null;
	}
	int tamaño(){
		return tamaño;
	}
	boolean insertar(E comp){
		Nodo<E> n = new Nodo<E>(comp);
		if( raiz()){
			ini = n;
			fin = n;
			return true;
		}
		//Nodo<E> aux = ini;
		fin.sig = n;
		n.ant = fin;
		fin = n;
		return true;
	}
	Nodo<E> buscar(int dato){
		Nodo<E> aux = fin;
		while(aux != null){
			if(aux.valor.equals(dato))
				return aux;
			aux = aux.ant;
		}
		return null;
	}
	Nodo<E> fin(){
		return fin;
	}
	Nodo<E> inicio(){
		return ini;
	}
}
