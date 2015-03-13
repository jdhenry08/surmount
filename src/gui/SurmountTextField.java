package gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import resources.*;

/**
 * SurmountTextField Class extends JTextField and implements the Focus and MouseInput Listeners
 * 
 * @author jdHenery08 and lundbj86
 */
@SuppressWarnings("serial")
public class SurmountTextField extends JTextField 
								implements FocusListener, MouseInputListener {
	
	/**
	 * Constructor
	 * @author jdHenery08 and lundbj86
	 */
	public SurmountTextField() {
		super();
		addFocusListener(this);
		addMouseListener(this);
	}

	/**
	 * Implemented method for listener
	 */
	public void focusGained(FocusEvent e) {
		selectAll();
	}

	/**
	 * Implemented method for listener
	 */
	public void focusLost(FocusEvent e) {}

	/**
	 * Implemented method for listener
	 */
	public void mouseClicked(MouseEvent e) {
		maybeShowPopup(e);
	}

	/**
	 * Implemented method for listener
	 */
	public void mouseEntered(MouseEvent e) {
		maybeShowPopup(e);
	}

	/**
	 * Implemented method for listener
	 */
	public void mouseExited(MouseEvent e) {
		maybeShowPopup(e);
	}

	/**
	 * Implemented method for listener
	 */
	public void mousePressed(MouseEvent e) {
		maybeShowPopup(e);
	}

	/**
	 * Implemented method for listener
	 */
	public void mouseReleased(MouseEvent e) {
		maybeShowPopup(e);
	}

	/**
	 * Implemented method for listener
	 */
	public void mouseDragged(MouseEvent e) {
		maybeShowPopup(e);
	}

	/**
	 * Implemented method for listener
	 */
	public void mouseMoved(MouseEvent e) {
		maybeShowPopup(e);
	}

	/**
	 * Implemented method for listener
	 */
	private void maybeShowPopup(MouseEvent e) {
        if(e.isPopupTrigger()) {
        	try {
        		String label = JOptionPane.showInputDialog(null,
        				"Please enter a name for the cell:", "", 1);
        		SurmountTable p =(SurmountTable)getParent();
        		int row = p.getEditingRow();
        		int col = p.getEditingColumn();
        		String cell = SurmountTable.getCellName(row, col);
        		Tools.addVariable(label + ":" + cell);
			} catch(VariableAssignmentException vae) {} // Big error here!
        }
    }
}