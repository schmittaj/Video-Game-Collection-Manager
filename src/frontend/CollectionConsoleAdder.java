package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * Lets user add a console to a collection
 * @author Anthony Schmitt
 *
 */
public class CollectionConsoleAdder extends JFrame
{
	private QueryRunner query;
	private int console_id;
	private String console_name;
	private JButton add, cancel;
	private JTextField notesField;
	private JCheckBox haveBox, isSealed, haveBooklet, haveCords;
	private JComboBox<String> collectionSelecter;
	private JPanel buttonPanel;
	private String[] collections;
	
	/**
	 * Constructor
	 * @param q Connection to the database
	 * @param cid Id of the console to add
	 * @param cname Name of the Console to add
	 */
	public CollectionConsoleAdder(QueryRunner q, int cid, String cname)
	{
		this.query = q;
		this.console_id = cid;
		this.console_name = cname;
		init();
	}

	/**
	 * Sets up the window
	 */
	private void init()
	{
		this.setTitle("Add " + console_name + " to a Collection");
		JLabel notesLabel = new JLabel("Notes on copy of game:");
		notesField = new JTextField();
		JPanel notesPanel = new JPanel();
		notesPanel.setLayout(new GridLayout(2,1));
		notesPanel.add(notesLabel);
		notesPanel.add(notesField);
		haveBox = new JCheckBox("Have Box");
		isSealed = new JCheckBox("Is Box Sealed");
		haveBooklet = new JCheckBox("Have Booklet");
		haveCords = new JCheckBox("Have Cords");
		JPanel cbPanel = new JPanel();
		cbPanel.setLayout(new GridLayout(1,4));
		cbPanel.add(haveCords);
		cbPanel.add(haveBooklet);
		cbPanel.add(haveBox);
		cbPanel.add(isSealed);
		isSealed.setEnabled(false);
		haveBox.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				isSealed.setEnabled(haveBox.isSelected());
				if(!haveBox.isSelected())
				{
					isSealed.setSelected(false);
				}
			}
		});
		collections = query.querySingleListReturnColumn(Queries.COLLECTIONS);
		collectionSelecter = new JComboBox<String>(collections);
		JPanel inerPanel = new JPanel();
		inerPanel.setLayout(new GridLayout(3,1));
		inerPanel.add(notesPanel);
		inerPanel.add(cbPanel);
		this.setLayout(new BorderLayout());
		this.add(inerPanel, BorderLayout.CENTER);
		this.add(collectionSelecter, BorderLayout.NORTH);
		setUpButtons();
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(500,500);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets up the buttons.
	 */
	private void setUpButtons()
	{
		add = new JButton("Add to Collection");
		add.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				String col = collectionSelecter.getSelectedItem().toString();
				boolean in = query.storeProcedure("inCollectionConsole", console_id, col); 
				
				if(!in)
				{
					String notes = notesField.getText();
					if(notes.equals(""))
					{
						notes = "null";
					}
					query.addConsoleToCollection(console_id, col, haveCords.isSelected(), haveBooklet.isSelected(), haveBox.isSelected(), isSealed.isSelected(), notes);
					JOptionPane.showMessageDialog(null,console_name + " had been added to collection " + col,"Game Added to Collection",JOptionPane.INFORMATION_MESSAGE);
					clearFields();
					invisible();
				}
				else
				{
					JOptionPane.showMessageDialog(null,console_name + " already exists in collection " + col,"Already in Collection",JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				clearFields();
				invisible();
			}
		});
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		buttonPanel.add(add);
		buttonPanel.add(cancel);
	}
	
	/**
	 * Resets the inputs to defaults and changes the window name to reflect whatever console is being added.
	 */
	private void update()
	{
		clearFields();
		this.setTitle("Add " + console_name + " to a Collection");
	}
	
	/**
	 * Hides the window.
	 */
	private void invisible()
	{
		this.setVisible(false);
	}
	
	/**
	 * Resets all of the input fields to their default values.
	 */
	private void clearFields()
	{
		notesField.setText("");
		haveCords.setSelected(false);
		haveBox.setSelected(false);
		haveBooklet.setSelected(false);
		isSealed.setSelected(false);
		collectionSelecter.setSelectedIndex(0);
	}
	
	/**
	 * Changes the console that is being added.
	 * @param cid The ID of the console to be added
	 * @param cname The name of the console to be added
	 */
	public void setConsole(int cid, String cname)
	{
		this.console_id = cid;
		this.console_name = cname;
		update();
	}
	
}
