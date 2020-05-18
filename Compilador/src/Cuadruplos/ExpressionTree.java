package Cuadruplos;

import java.util.ArrayList;

import Cuadruplos.Cuadruple;
import main.Identificador;
import main.Token;

public class ExpressionTree {
	/*
		1- Get the first item and initialise the tree with it.
		2- Currently the root node is also the current node. The current node is the node we currently lie on.
		3- Get the next item. This will be called the new item.

		4- Climb up the tree as long as the precedence of the current node item is greater than or equal to 
		that of the new item.When this is over, the current node item has a precedence strictly 
		less than that of the new item.
		
		5- Create a new node for the new item. Then set the left child of the new node to be the old 
		right child of the current node. Finally set the new right child of the current node to be 
		the new node (also set the parent of the new node).

		6- Set the current node to be the new node.
		
		7- Repeat steps 3, 4, 5 and 6 till there is no item left.
	*/
	private Node<Token> root,current;
	private boolean flag = false;
	private int counter = 0;
	public String result = "", temp = "";
	private Identificador token;
	private ArrayList<Identificador> symbols;
	private ArrayList<Cuadruple> cuadruples = new ArrayList<>();

	public ExpressionTree(){}

	public ExpressionTree(Identificador t,ArrayList<Identificador> symb){
		token = t; symbols = symb;
	}
	public ArrayList<Cuadruple> retList(){
		return cuadruples;
	}
	public void add(Token t){
		current = insert(current,t);
		flag = false;
	}
	
	public Node<Token> insert(Node<Token> current,Token t){
		Node<Token> newNode, aux;
		if( current == null){
			newNode = new Node<Token>(t);
			if( root != null){
				newNode.left = root;
				root.parent = newNode;
			}
			root = newNode;
			
			return newNode;
		}
		newNode = new Node<Token>(t);
		int currentPriority = priority(current), 
				newPriority = priority(newNode);
		if( currentPriority >= newPriority){
			aux = insert(current.parent,t);
			if( flag ){
				current.parent = aux;
				aux.left = current;
			}
			
			current = aux;
			flag=false; 
		}else{
			newNode.parent = current;
			current.right = newNode;
			current = newNode;
			flag = true;
		}
		
		return current;
	}

	private int priority(Node<Token> n){
		int val = -1;
		Token t = (Token) n.data;
		switch (t.getType()) {
		case Token.DIG: 
			case Token.ID: val = 3; break;
		case Token.AOP:
			if( t.getToken().matches("(\\*|/)") ){
				//System.out.println("Entro IF con -> "+t.getToken());
				val = 2;
			}else{
				//System.out.println("Entro ELSE con -> "+t.getToken());
				val = 1;
			}
			/*switch (t.getToken()) {
			case "*": case "/":
				val = 2;
				break;
			case "+": case "-":
				val = 1;
				break;
			}*/
			break;
		}
		return val;
	}
	private boolean assignment(){
		if( root.left == null && root.right == null )
			return true;
		return false;
	}
	public String generateCuadruple(Node<Token> node) {
	     if (node != null) {
	    	 String value1,value2,vv1=null,vv2=null;
	    	 value1 = generateCuadruple(node.left);
	         value2 = generateCuadruple(node.right);
	         if( node.data.getType() == Token.AOP){
	        	 int distance = 8;
	        	 counter++;
	        	 if( !value1.matches("(T\\d+|\\d+)")){
	        		 vv1 = retValue(value1);
	        	 }
	        	 if( !value2.matches("(T\\d+|\\d+)")){
	        		 vv2 = retValue(value2);
	        	 }
	        	 
	        	 if( node.parent == null){
	        		 result += String.format("%5s %-"+(distance+2)+"s %-"+(distance+2)
	        				 +"s %-"+(distance+4)+"s %-"+(distance+0)+"s %n","",node.data.getToken(),value1,value2,token.getNombre());
	        		 cuadruples.add(
	        				 new Cuadruple(token.getNombre(), node.data.getToken(), value1, value2, vv1, vv2)	
	        				 );
	        	 }else{
	        		 //result += String.format("%"+espacios+"s %s %4s %4s %4s %n", "T"+contador,":=",v1,node.dato.getToken(),v2);
	        	 	result += String.format("%5s %-"+(distance+2)+"s %-"+(distance+2)+"s %-"
	        		 +(distance+4)+"s %-"+(1+0)+"s %n","",node.data.getToken(),value1,value2,"T"+counter);
	        	 	cuadruples.add(
	        				 new Cuadruple("T"+counter, node.data.getToken(), value1, value2, vv1, vv2)	
	        				 );
	        	 }
	        	 return "T"+(counter);
	         }else{
	        	 if( assignment() )
	        		 result += String.format("%8s %s %4s %n %n", "T1",":=",node.data.getToken());
	        	 return node.data.getToken();
	         }
	         
	         
	     }
	     return "";
	 }
	
	 public String solve(){
	 	String resultValue = "";
		 switch(token.getTipo()){
		 	case "int":
				resultValue = String.valueOf((int)solveGen(root));
				//generaCuadruplo(root);
				//result += String.format("%8s %s %4s %n",token.getNombre(),":=",vuelto);
				break;
		 	case "double":
				resultValue = String.valueOf(solveGen(root));
				//result += token.getNombre()+" := "+vuelto+"\n";
				break;
		 	case "float":
				resultValue = String.valueOf(solveGen(root))+"f";
				//result += token.getNombre()+" := "+vuelto+"\n";
				break;
		 }
		 	generateCuadruple(root);
			result += String.format("%8s %s %4s %n",token.getNombre(),":=",resultValue);
		 return resultValue;
	 }
	 /*public int resuelveInt(Nodo<Token> node) {
	     if (node != null) {
	    	 int v1,v2;
	    	 v1 = resuelveInt(node.izq);
	         v2 = resuelveInt(node.der);
	         if( node.dato.getTipo() == Token.OPA){
	         	char c = node.dato.getToken().charAt(0);
	         	int res = 0;
	        	 switch(c){
	        	 	case '+': res = v1 + v2; break;
	        	 	case '-': res = v1 - v2; break;
	        	 	case '*': res = v1 * v2; break;
	        	 	case '/': res = v1 / v2; break;
	        	 }
	        	 return res;
	         }else if( node.dato.getTipo() == Token.ID)
	        	 return Integer.parseInt(retValor(node.dato.getToken()));
	         else
	        	 return Integer.parseInt(node.dato.getToken());
	         
	     }
	     return 0;
	 }*/
	 public double solveGen(Node<Token> node) {
	     if (node != null) {
	    	 double value1,value2;
	    	 value1 = solveGen(node.left);
	         value2 = solveGen(node.right);
	         if( node.data.getType() == Token.AOP){
	         	char c = node.data.getToken().charAt(0);
	         	double res = 0;
	        	 switch(c){
	        	 	case '+': res = value1 + value2; break;
	        	 	case '-': res = value1 - value2; break;
	        	 	case '*': res = value1 * value2; break;
	        	 	case '/': res = value1 / value2; break;
	        	 }
	        	 //contador++;
	        	 //result += "T"+contador+" := "+v1+"  "+c+"  "+v2+"\n";
	        	 return res;
	         }else if( node.data.getType() == Token.ID)
	        	 return Double.parseDouble(retValue(node.data.getToken()));
	         else
	        	 return Double.parseDouble(node.data.getToken());
	         
	     }
	     return 0;
	 }
	 /*public float resuelveFloat(Nodo<Token> node) {
	     if (node != null) {
	    	 float v1,v2;
	    	 v1 = resuelveFloat(node.izq);
	         v2 = resuelveFloat(node.der);
	         if( node.dato.getTipo() == Token.OPA){
	         	char c = node.dato.getToken().charAt(0);
	         	float res = 0;
	        	 switch(c){
	        	 	case '+': res = v1 + v2; break;
	        	 	case '-': res = v1 - v2; break;
	        	 	case '*': res = v1 * v2; break;
	        	 	case '/': res = v1 / v2; break;
	        	 }
	        	 contador++;
	        	 result += "T"+contador+" := "+v1+"  "+c+"  "+v2+"\n";
	        	 return res;
	         }else if( node.dato.getTipo() == Token.ID)
	        	 return Float.parseFloat(retValor(node.dato.getToken()));
	         else
	        	 return Float.parseFloat(node.dato.getToken());
	         
	     }
	     return 0;
	 }*/
	 private String retValue(String id){
		 for (Identificador identifier : symbols) {
			if( identifier.getNombre().equals(id) )
				return identifier.getValor();
		}
		 return null;
	 }
	class Node<Token>{
		Token data;
		Node<Token> parent,right,left;
		
		public Node(Token val){ data = val; }
	}
}
