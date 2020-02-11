package main;

public class Token {
	final static int PR=0;
	final static int SE = 1;
	final static int OP = 2;
	final static int TIPO  = 3;
	final static int MOD = 4;
	final static int DIG = 5;
	final static int VAL = 6;
	final static int ID = 7;
	
	private String desc,token;
	private int tipo,columna,fila;
	private String [] significado = {"Palabra reservada","Simbolo especial","Operador","Tipo","Modificador","Digito","Valor","Identificador"};


	public Token(int tp,String t,int col, int fi){
		tipo = tp;
		token = t;
		columna = col;
		fila = fi;
		if(tipo < 9)
			desc = significado[tipo];
		else
			desc ="";
	}

	public String getDesc() {
		return desc;
	}

	public String getToken() {
		return token;
	}
	
	public int getColumna() {
		return columna;
	}

	public int getFila() {
		return fila;
	}
	public int getTipo(){
		return tipo;
	}
}
