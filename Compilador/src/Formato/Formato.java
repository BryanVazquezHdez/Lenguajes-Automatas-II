package Formato;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class Formato extends DefaultTableCellRenderer{
	
	private static final long serialVersionUID = 1L;
	private Font fuente = new Font("Arial", Font.PLAIN, 12);

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		Component cellComponent = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
		// General
		if(row%2==0 )
			setBackground(Color.white);
		else
			setBackground(new Color(244, 244, 244));
		
		cellComponent.setFont(fuente);
		
		setHorizontalAlignment(SwingConstants.CENTER);

		return cellComponent;
	}

}
