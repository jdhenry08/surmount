package gui;

import javax.swing.table.*;

import resources.*;


@SuppressWarnings("serial")
public class SurmountTableModel extends AbstractTableModel {
	private Object[][] dataValues;
	private boolean[][] errors;
	private boolean canEdit;

	public SurmountTableModel(int rows, int cols, boolean canEdit) {
		dataValues = new String[rows][cols];
		errors = new boolean[rows][cols];
		this.canEdit = canEdit;
	}

	public SurmountTableModel(Object[][] dataValues, boolean[][] errors, boolean canEdit) {
		this.dataValues = dataValues;
		this.errors = errors;
		this.canEdit = canEdit;
	}

	public Object[][] getDataValues() {
		return dataValues;
	}

	public boolean[][] getErrors() {
		return errors;
	}

	public int getColumnCount() {
		return dataValues[0].length;
	}

	public int getRowCount() {
		return dataValues.length;
	}

	public Object getValueAt(int row, int col) {
		if(dataValues[row][col] == null) {
			return null;
		} else if(dataValues[row][col].equals("")) {
			return "";
		} else {
			String exp = "";
			try {
				exp = dataValues[row][col].toString();
				Evaluator eval = Tools.getEvaluator(exp);
				Object answer = eval.evaluate(exp);
				errors[row][col] = false;
				return canEdit ? exp : answer;
			} catch(VariableAssignmentException vae) {
				errors[row][col] = true;
				return canEdit ? exp : "#BADASS!";
			} catch(NoSuchVariableExistsException nsvee) {
				errors[row][col] = true;
				return canEdit ? exp : "#NOVAR!";
			} catch(InvalidExpressionException iee) {
				errors[row][col] = true;
				return canEdit ? exp : "#INVEXP!";
			} catch(DivideByZeroException dbze) {
				errors[row][col] = true;
				return canEdit ? exp : "#DIV/0!";
			} catch(CircularReferenceException cre) {
				errors[row][col] = true;
				return canEdit ? exp : "#CIRREF!";
			}
		}
	}

	public String getColumnName(int col) {
		if(col < 26) {
			return String.valueOf((char)(65 + col));
		} else {
			return getColumnName(col / 26 - 1) + getColumnName(col % 26);
		}
	}

	public static String getColName(int col) {
		if(col < 26) {
			return String.valueOf((char)(65 + col));
		} else {
			return getColName(col / 26 - 1) + getColName(col % 26);
		}
	}

	@SuppressWarnings("unchecked")
	public Class getColumnClass(int col) {
		return String.class;
	}

	public boolean isCellEditable(int row, int col) {
		return canEdit;
	}

	public void setValueAt(Object obj, int row, int col) {
		if(obj.toString().equals("")) {
			errors[row][col] = false;
		}
		
		dataValues[row][col] = obj;
			
		try {
			Tools.addVariable(SurmountTable.getCellName(row, col) + ":" + obj.toString());
		} catch(VariableAssignmentException vae) { }

		fireTableDataChanged();
	}

	public boolean isError(int row, int col) {
		return errors[row][col];
	}
}