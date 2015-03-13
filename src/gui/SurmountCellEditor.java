package gui;

import java.awt.*;
import javax.swing.*;

/**
 * SurmountCellEditor Class extends DefaultCellEditor
 * 
 * @author jdhenery08 and lundbj86
 */
@SuppressWarnings("serial")
public class SurmountCellEditor extends DefaultCellEditor {
	/**
	 * Consturctor
	 * 
	 * @param field parent field
	 */
	public SurmountCellEditor(SurmountTextField field) {
		super(field);
	  	setClickCountToStart(1);
	}

	/**
	 * gets the cell being edited component
	 */
	public Component getTableCellEditorComponent
		(JTable t, Object v, boolean sel, int row, int col) {
		return super.getTableCellEditorComponent(t, v, sel, row, col);
	}
}