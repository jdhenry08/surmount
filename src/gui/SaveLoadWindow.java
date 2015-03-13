package gui;

import java.awt.event.*;
import java.io.IOException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;
import javax.xml.stream.XMLStreamException;

import tools.*;

@SuppressWarnings("serial")
public class SaveLoadWindow extends JFrame implements ActionListener, ListSelectionListener {
	private static final String[] TYPES = {"Save", "Load"};

	private int type;

	private JSplitPane mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

	private JPanel subPanel = new JPanel();
	private JButton delButton = new JButton("Delete");
	private JButton actionButton = new JButton();
	private JButton cancelButton = new JButton("Cancel");

	private JList saveList;
	private JTextField saveNameTextField = new JTextField("");

	private Surmount parent;

	public SaveLoadWindow(int type, Surmount parent) {
        super(TYPES[type] + " file...");
        saveList = new JList(DatabaseIO.getSaves(parent.getUserName()));
        this.type = type;
        this.parent = parent;

        setListeners();
        initialize();
    }

    private void setListeners() {
    	delButton.addActionListener(this);
    	actionButton.addActionListener(this);
    	cancelButton.addActionListener(this);

    	saveList.addListSelectionListener(this);
    }

    private void initialize() {
        actionButton.setText(TYPES[type]);

    	saveNameTextField.setColumns(10);
    	subPanel.add(saveNameTextField);
    	subPanel.add(delButton);
		subPanel.add(actionButton);
		subPanel.add(cancelButton);

    	mainPanel.setBottomComponent(subPanel);
    	mainPanel.setTopComponent(saveList);
    	mainPanel.setDividerLocation(150);
    	mainPanel.setDividerSize(0);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);
        setSize(250, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    private void save(String fileName) throws IOException, XMLStreamException {
    	String text = "Please enter a comma-separated list of users";
    	String userList = JOptionPane.showInputDialog(this, text);
    	String[] users = userList.split(",");
    	Vector<String> dbUsers = DatabaseIO.getUserList();
    	DatabaseIO.delete(fileName);

    	for(int i = 0; i < users.length; i++) {
    		if(dbUsers.contains(users[i])) {
        		FileIO.save(fileName, users[i], parent.getModel());
    		}
    	}

		FileIO.save(fileName, parent.getUserName(), parent.getModel());
    }

    public void actionPerformed(ActionEvent e) {
    	String fileName = saveNameTextField.getText();

    	if(e.getSource().equals(actionButton)) {
    		try {
    			switch(type) {
                case 0: save(fileName);
                		break;
                case 1: parent.reset(FileIO.load(fileName, parent.getUserName(), parent.getPassword()));
                		break;
                }
    		} catch(Exception x) {
    			JOptionPane.showMessageDialog(null, x.getClass().getName() +
    				" error while trying to " + TYPES[type].toLowerCase() +
    				" your file.\n" + x.getMessage(), "Error!", 0);
    			x.printStackTrace();
    		}

    		setVisible(false);
        } else if(e.getSource().equals(delButton)) {
        	DatabaseIO.delete(fileName);
        	repaint();
        	saveList.clearSelection();
        	saveNameTextField.setText("New File");
        } else if(e.getSource().equals(cancelButton)) {
            setVisible(false);
        }
    }

	public void valueChanged(ListSelectionEvent e) {
		if(saveList.getSelectedValue() != null) {
			String data = saveList.getSelectedValue().toString();
			saveNameTextField.setText(data);
		}
	}
}