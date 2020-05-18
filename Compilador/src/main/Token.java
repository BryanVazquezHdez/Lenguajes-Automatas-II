package main;

public class Token {
	public final static int PR = 0;
	public final static int SE = 1;
	public final static int LOP = 2;
	public final static int AOP = 3;
	public final static int Type = 4;
	public final static int MOD = 5;
	public final static int DIG = 6;
	public final static int VAL = 7;
	public final static int STG = 8;
	public final static int ID = 9;
	public final static int EOF = 10;
	public final static int ID_DIG = 11;
	
	private String desc,token;
	private int type,column,line;
	private String [] meaning = {"Palabra reservada","Simbolo especial","Operador lógico","Operador Aritmetico"
			,"Tipo","Modificador","Digito","Valor","String","Identificador","Fin del archivo"};


	public Token(int tp,String t,int col, int fi){
		type = tp;
		token = t;
		column = col;
		line = fi;
		if(type != -1)
			desc = meaning[type];
		else
			desc ="";
	}

	public String getDesc() {
		return desc;
	}

	public String getToken() {
		return token;
	}
	
	public int getColumn() {
		return column;
	}

	public int getLine() {
		return line;
	}
	public int getType(){
		return type;
	}
}
