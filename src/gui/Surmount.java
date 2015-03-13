package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;

import tools.DatabaseIO;
import tools.FileIO;

/**
 * Surmount.java - Creates a new GUI instance of the Surmount program.
 * 
 * @authors jdhenry08, lundbj86
 */
@SuppressWarnings("serial")
public class Surmount extends JFrame implements ActionListener, PropertyChangeListener {
	private JMenuBar menu;
	private JMenuItem menuNew, load, save, exit, about;

	private JTabbedPane tabs;
	private JSplitPane combo;
	private JScrollPane[] jsps;

	private SurmountTableModel model;
	private SurmountTable[] tables;

	private String userName, password;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Error enabling system look and feel.");
		}

		new Surmount(SurmountTable.NUM_ROWS, SurmountTable.NUM_COLS);
	}

	/**
	 * Constructor
	 *
	 * @param rows number of rows
	 * @param cols number of columns
	 */
	public Surmount(int rows, int cols) {
		super("Surmount");

		JOptionPane.showMessageDialog(this, "Attempting to connect to the clo" +
				"ud.  Please wait...", "Connecting...", 1);
		if(!DatabaseIO.testConnection()) {
			JOptionPane.showMessageDialog(this, "There was an error connectin" +
					"g to the cloud.  The program will now exit.", "Error!", 0);
			System.exit(-1);
		}

		model = new SurmountTableModel(SurmountTable.NUM_ROWS,
									   SurmountTable.NUM_COLS, true);

		userPassPrompt();

		setupMenu();
		setupTabs();

		initialize();
	}

	private void userPassPrompt() {
		String text = "Please enter your user name:";
		userName = null;
		while(userName == null) {
			userName = JOptionPane.showInputDialog(this, text);
		}

		if(!DatabaseIO.userExists(userName)) {
			JPasswordField pwd = new JPasswordField(10);
			password = null;
			while(password == null || password.length() < 8) {
				text = "Please choose a 8+ character password";
				JOptionPane.showConfirmDialog(this, pwd, text, 2);  
				password = new String(pwd.getPassword());
			}

			DatabaseIO.setupUser(userName, password);
		} else {
			JPasswordField pwd = new JPasswordField(10);
			int tries = 0;
			password = null;
			while(password == null || !DatabaseIO.validateUser(userName, password)) {
				text = "Please enter your password";
				JOptionPane.showConfirmDialog(this, pwd, text, 2);  
				password = new String(pwd.getPassword());

				if(password != null && !DatabaseIO.validateUser(userName, password)) {
					tries++;
					text = tries + "/3: Incorrect password.";
					if(tries == 3) {
						text += "  The program will now exit.";
						JOptionPane.showMessageDialog(this, text, "Error!", 0);
						System.exit(-1);
					} else {
						JOptionPane.showMessageDialog(this, text, "Error!", 0);
					}
				}
			}
		}

		FileIO.makeRSAKeys(userName, password);
	}

	/**
	 * Sets up the top menu of the frame.
	 */
	private void setupMenu() {
		menu = new JMenuBar();

		JMenu file = new JMenu("File");
		menuNew = new JMenuItem("New");
		load = new JMenuItem("Load");
		save = new JMenuItem("Save");
		exit = new JMenuItem("Exit");
		menuNew.addActionListener(this);
		load.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);
		file.add(menuNew);
		file.add(load);
		file.add(save);
		file.add(exit);

		JMenu help = new JMenu("Help");
		about = new JMenuItem("About");
		about.addActionListener(this);
		help.add(about);

		menu.add(file);
		menu.add(help);

		setJMenuBar(menu);
	}

	/**
	 * Sets up the three tabs for the various views.
	 */
	private void setupTabs() {
		tables = new SurmountTable[4];
		jsps = new JScrollPane[4];
		tabs = new JTabbedPane();

		ListModel rowList = new AbstractListModel() {
			public int getSize() { return SurmountTable.NUM_ROWS; }
			public Object getElementAt(int index) {
				return String.valueOf(index + 1);
			}
		};

		tables[0] = new SurmountTable(model, true);
		tables[1] = new SurmountTable(tables[0], false);
		tables[2] = new SurmountTable(tables[0], true);
		tables[3] = new SurmountTable(tables[0], false);

		for(int i = 0; i < tables.length; i++) {
			tables[i].addPropertyChangeListener(this);
			tables[i].setCellSelectionEnabled(true);
			tables[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tables[i].setRowHeight(20);
			tables[i].getTableHeader().setReorderingAllowed(false);

			jsps[i] = new JScrollPane(tables[i]);
			jsps[i].setHorizontalScrollBarPolicy(30);
			jsps[i].setVerticalScrollBarPolicy(20);

			JList rowHeader = new JList(rowList);
			rowHeader.setFixedCellWidth(50);
			rowHeader.setFixedCellHeight(tables[i].getRowHeight());
			rowHeader.setCellRenderer(new SurmountHeader());
			jsps[i].setRowHeaderView(rowHeader);
		}

		combo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jsps[2], jsps[3]);
		combo.setOneTouchExpandable(true);
		combo.setDividerLocation(
			Toolkit.getDefaultToolkit().getScreenSize().width / 2);

		tabs.add("Formula",jsps[0]);
		tabs.add("Value", jsps[1]);
		tabs.add("Combo", combo);

		getContentPane().add(tabs, BorderLayout.CENTER);
	}

	/**
	 * Initializes the JFrame window
	 */
	private void initialize() {
		setIconImage(new ImageIcon("img/s.gif").getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		setVisible(true);
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public SurmountTableModel getModel() {
		return model;
	}

	public void reset(SurmountTableModel model) {
		int tab = tabs.getSelectedIndex();
		this.model = model;
		getContentPane().removeAll();

		setupTabs();
		initialize();
		tabs.setSelectedIndex(tab);
	}

	private void saveQuestion() {
		String title = "Save File";
		String text = "Would you like to save your work?";
		int choice = JOptionPane.showConfirmDialog(null, text, title, 0);
		if(choice == JOptionPane.YES_OPTION) {
			new SaveLoadWindow(0, this).setVisible(true);
		}
	}

	private void about() {
		Icon icon = new ImageIcon("img/s.gif");
		String title = "About Surmount";
		String info = 
		"Created by Justin Henry and John Lundberg, with formula\n" +
		"evaluation based on the form-eval package created by\n" +
		"Bryan Gago, William Gates, and Justin Henry." +
		"\n\n\u00A9 2010";

		JOptionPane.showMessageDialog(null, info, title, 1, icon);
	}

	public void propertyChange(PropertyChangeEvent e) {
		SurmountTable t = (SurmountTable)e.getSource();
		SurmountTableModel model = t.model;
		int row = t.getEditingRow();
		int col = t.getEditingColumn();

		if(row != -1) {
			String exp = (String)model.getValueAt(row, col);
			if(exp == null) return;

			for(int i = 0; i < tables.length; i++) {
				tables[i].model.setValueAt(exp, row, col);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		if(src == about) {
			about();
		} else if(src == exit) {
			saveQuestion();

			System.exit(0);
		} else if(src == load) {
			new SaveLoadWindow(1, this).setVisible(true);
		} else if(src == menuNew) {
			saveQuestion();
			reset(new SurmountTableModel(SurmountTable.NUM_ROWS,
									   SurmountTable.NUM_COLS,
									   true));
		} else if(src == save) {
			new SaveLoadWindow(0, this).setVisible(true);
		}
	}
}