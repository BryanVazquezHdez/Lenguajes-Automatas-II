package main;

public class PRUEBA {

	public static String CD(){
		return "("+M()+")*class"+I()+"{("+FD()+")*("+S()+")*}";
	}
	private static String FD(){
		return VDN()+";";
	}
	private static String VDN(){
		return "("+M()+")*("+T()+"("+VDR()+"|"+I();
	}
	private static String VDR(){
		return I()+"=("+IL()+"|"+BL()+")";
	}
	private static String E(){
		return TE();
	}
	private static String TE(){
		return "("+IL()+"|"+I()+"(>|<|>=|<=|==|!=)"+IL()+"|"+I();
	}
	private static String S(){
		return "("+VDN()+"|"+IS()+"|"+WS()+")";
	}
	private static String WS(){
		return "while("+E()+"){"+AE()+"("+S()+")*}";
	}
	private static String IS(){
		return "if("+E()+"){"+AE()+"("+S()+")*}";
	}
	private static String T(){
		return TS();
	}
	private static String TS(){
		return "(boolean|int)";
	}
	private static String M(){
		return "(public|private)";
	}
	private static String IL(){
		return "[1-9][0-9]?";
	}
	private static String BL(){
		return "(true|false)";
	}
	private static String I(){
		return "[a-z|_]";
	}
	private static String AE(){
		return I()+"="+IL()+"(+|-|/|*)"+IL()+";";
	}
	
	
	
	 public static void main(String[] args) {
		System.out.println("ALGO %s \n");
		 
	}

}
