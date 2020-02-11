package Grafico;

import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class FrameAcerca extends JDialog
{
    private static final long serialVersionUID = 1L;
	JLabel datos,labelLogo,labelTec,labelTextTec,labelClip;
    PanelAcerca panelAcerca;
    ImageIcon icono,ridemncionado,iconoTec;
    JTextArea areaDeTexto,areTextoTec;
    public FrameAcerca(JFrame panelContenedor)
    {   
        labelClip = new JLabel();
        areTextoTec = new JTextArea();
        labelTec = new JLabel();
        areaDeTexto = new JTextArea(50,40);
        setTitle("Sobre vaja");
        panelAcerca = new PanelAcerca();
        setSize(575,502);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(panelContenedor);
        setModal(true);
        getContentPane().add(panelAcerca);
    }
    class PanelAcerca extends JPanel
    {
       private static final long serialVersionUID = 1L;

		public PanelAcerca()
        {
         setLayout(null);
         Font fuenteAreaTexto = new Font("Franklin Gothic Book",Font.PLAIN,16 );
         areTextoTec.setBounds(150, 5, 350, 70);
         areTextoTec.setText("    Instituto Tecnológico de Culiacán\n Ingeniería en Sistemas Computacionales");
         areTextoTec.setFont(fuenteAreaTexto);
         areTextoTec.setOpaque(false);
         
         labelTec.setBounds(5,0,75,75);
         iconoTec = new ImageIcon(FrameAcerca.class.getResource("/Imagenes/tec_logo.png"));
         ImageIcon rideTec = new ImageIcon(iconoTec.getImage().getScaledInstance(labelTec.getWidth(), labelTec.getHeight(), Image.SCALE_DEFAULT));
         labelTec.setIcon(rideTec);
         
         datos = new JLabel(); 
         datos.setBounds(5, 0, 100,50);
         
         labelLogo = new JLabel();
 		 labelLogo.setBorder(null);
 		 labelLogo.setBounds(5,100,70,70);
 		 icono = new ImageIcon(FrameAcerca.class.getResource("/Imagenes/icono-96.png"));
 		 ImageIcon ridemencionado = new ImageIcon(icono.getImage().getScaledInstance(labelLogo.getWidth(), labelLogo.getHeight(), Image.SCALE_DEFAULT));
 		 labelLogo.setIcon(ridemencionado);
         
         areaDeTexto.setBounds(100, 100, 420, 300);
         areaDeTexto.setOpaque(false);
         areaDeTexto.setText("2019 Version 1.0 Software compilador de texto\nCompilador desarrollado por estudiantes "
         		+ "pertenecientes\nal Insituto Tecnologico de Culiacan,\nse prohibe su venta y distribucion."
         		+ "\n\nAutores :\n* Loera Hérnandez Jesús Alberto\n* Ramos Félix Francisco Fernando"
         		+ "\n* Soria Ramírez Elías Misael\n* Vázquez Hérnandez Bryan");
         areaDeTexto.setFont(fuenteAreaTexto);
         areaDeTexto.setEditable(false);
         areTextoTec.setEditable(false);
         
         add(areTextoTec);
         add(labelTec);
         add(areaDeTexto);
         add(datos);
         add(labelLogo);
       }
    }
}