package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * SurmountCellRenderer extends DefaultTableCellRenderer
 * 
 * @author jdhenry08 and lundbj86
 */
@SuppressWarnings("serial")
public class SurmountCellRenderer extends DefaultTableCellRenderer {
	/**
	 * Calls the super() version of this method, but changes the cell's colors
	 * to red and pink if they contain an error.
	 * 
	 * @return c the component requested
	 */
	public Component getTableCellRendererComponent
			(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
		Component c = 
			super.getTableCellRendererComponent(t, v, sel, foc, row, col);
		SurmountTable st = (SurmountTable)t;

		if(st.model.isError(row, col)) {
			c.setForeground(new Color(255, 0, 0));
			c.setBackground(new Color(255, 208, 208));
		} else if(st.isFormulaView && row == t.getSelectedRow()) {
			c.setBackground(new Color(232, 232, 255));
		} else if(row % 2 == 0){
			c.setForeground(new Color(0, 0, 0));
			c.setBackground(new Color(255, 255, 255));
		} else {
			c.setForeground(new Color(0, 0, 0));
			c.setBackground(new Color(255, 255, 208));
		}

		return c;
	}
}