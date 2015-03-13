package gui;

import javax.swing.*;
import javax.swing.table.*;

/**
 * SurmountTable Class extends JTable
 * 
 * @author jdhenry08 and lundbj86
 */
@SuppressWarnings("serial")
public class SurmountTable extends JTable {
	public static final int NUM_ROWS = (int)Math.pow(2, 16);
	public static final int NUM_COLS = (int)Math.pow(2, 8);

	public SurmountTableModel model;
	private SurmountTextField field;
	private SurmountCellEditor editor;
	public boolean isFormulaView;

	/**
	 * Constructor
	 * 
	 * @param rows number of rows
	 * @param cols number of columns
	 * @param isFormulaView shows formula or value
	 */
	public SurmountTable(int rows, int cols, boolean isFormulaView) {
		model = new SurmountTableModel(rows, cols, isFormulaView);
		field = new SurmountTextField();
		editor = new SurmountCellEditor(field);
		this.isFormulaView = isFormulaView;

		setModel(model);
		setDefaultEditor(String.class, editor);
		updateUI();
	}

	/**
	 * Constructor
	 * 
	 * @param table another table to make similar too
	 * @param isFormulaView shows formula or value
	 */
	public SurmountTable(SurmountTable table, boolean isFormulaView) {
		SurmountTableModel temp = table.model;
		model = new SurmountTableModel(temp.getDataValues(), temp.getErrors(), isFormulaView);
		field = new SurmountTextField();
		editor = new SurmountCellEditor(field);
		this.isFormulaView = isFormulaView;

		setModel(model);
		setDefaultEditor(String.class, editor);
		updateUI();
	}

	public SurmountTable(SurmountTableModel model, boolean isFormulaView) {
		this.model = new SurmountTableModel(model.getDataValues(), model.getErrors(), isFormulaView);
		field = new SurmountTextField();
		editor = new SurmountCellEditor(field);
		this.isFormulaView = isFormulaView;

		setModel(model);
		setDefaultEditor(String.class, editor);
		updateUI();
	}

	/**
	 * Updates the ui
	 */
	public void updateUI() {
		super.updateUI();
		TableColumnModel model = getColumnModel();
		for(int i = 0; i < getColumnCount(); i++) {
			model.getColumn(i).setCellRenderer(new SurmountCellRenderer());
		}
	}

	/**
	 * returns the name of the cell as a string
	 * 
	 * @param row row number
	 * @param col column number
	 * @return String name of cell
	 */
	public static String getCellName(int row, int col) {
		return SurmountTableModel.getColName(col) + String.valueOf(row + 1);
	}
}