package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

public class Archivo {
//	Variables de Control
			public String nombreArch = null;
			private File guardar = null;
			boolean guardado = false;
			public String txt = "";
	
	public void hazloNull(){
		guardar = null;
		nombreArch = null;
	}
	
	public String OpenFile(JFrame jf){	 //Abrir Archivo
		String documento="", aux = "";
		JFileChooser file = new JFileChooser();
		File abre = null;
		if(file.showOpenDialog(jf) == JFileChooser.APPROVE_OPTION){
			try  {
			   abre=file.getSelectedFile();
			   //JOptionPane.showMessageDialog(null, abre);
			   if(abre!=null) {  
				  guardar=abre;
				  FileReader archivos=new FileReader(guardar);
			      BufferedReader lee=new BufferedReader(archivos);
			      while((aux=lee.readLine())!=null)
			      {
			         documento+= aux+ "\n";
			      }
			      nombreArch = remueveExtension(guardar.getName());
			      lee.close();
			    }
			   }catch(IOException ex){
				   JOptionPane.showMessageDialog(null, abre.getPath()+"\n"+abre.getName());
			     JOptionPane.showMessageDialog(null,ex+"" +
			           "\nNo se ha encontrado el archivo",
			                 "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
			    }
		}
		txt = documento;
		return documento;   //El texto se almacena en el JTextArea
	}
	
	public void SaveFile( JTextPane ta,JFrame jf ) throws IOException{ //Metodo que Guarda Archivo
		String lineas = "";
		JFileChooser buscardor = new JFileChooser(System.getProperty("user.dir"));
		do{
			try {
				if(!guardar.exists() ){
					nombreArch = guardar.getName();
					String cosa = guardar+".txt";
					FileWriter fw = new FileWriter( cosa);
					BufferedWriter escritura = new BufferedWriter( fw );
					lineas = ta.getText();
					char[] caracBytes = new char[ lineas.length() ];
					
					for (int i = 0; i < lineas.length(); i++) {
						caracBytes[ i ]  = lineas.charAt( i );
					}
					for (int i = 0; i < lineas.length(); i++) {
						if( ! ( lineas.charAt(i) == 10 ) ){
							escritura.write( lineas.charAt( i ) );
						}else{
							escritura.newLine();
						}
					}
					escritura.close();
					guardado = true;
					
					break;
				}else{
					nombreArch = remueveExtension(guardar.getName());
					FileWriter fw = new FileWriter( guardar);
					BufferedWriter escritura = new BufferedWriter( fw );
					lineas = ta.getText();
					char[] caracBytes = new char[ lineas.length() ];
					
					for (int i = 0; i < lineas.length(); i++) {
						caracBytes[ i ]  = lineas.charAt( i );
					}
					for (int i = 0; i < lineas.length(); i++) {
						if( ! ( lineas.charAt(i) == 10 ) ){
							escritura.write( lineas.charAt( i ) );
						}else{
							escritura.newLine();
						}
					}
					escritura.close();
					guardado = true;
					break;
					
				}
			} catch (Exception e) {
				
				if( buscardor.showSaveDialog(jf) == JFileChooser.APPROVE_OPTION){
					guardar = buscardor.getSelectedFile();
				}
			}
		}while( guardar != null);
		txt = lineas;
	}
	public String remueveExtension(String s){
		String vuel = "";
		
		for (int i = 0; i < s.length(); i++) {
			if( i < s.length()-4)
				vuel += s.charAt(i);
		}
		
		return vuel;
	}
}
