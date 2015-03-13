package gui;

import java.awt.*;
import javax.swing.*;

/**
 * SurmountHeader extends JLabel implements ListCellRenderer
 * 
 * @author jdhenry08 and lundbj86
 */
@SuppressWarnings("serial")
public class SurmountHeader extends JLabel implements ListCellRenderer {
	/**
	 * Constructor - sets a nice border and aligns the labels to the center
	 */
	public SurmountHeader() {
	    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
	    setHorizontalAlignment(CENTER);
	}

	/**
	 * Sets the text of each header object based on what is given in the method
	 */
	public Component getListCellRendererComponent
		(JList l, Object v, int i, boolean sel, boolean foc) {
		    setText((v == null) ? "" : v.toString());
		    return this;
	}
}