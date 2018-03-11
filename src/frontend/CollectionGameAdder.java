package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * Allows user to add a game to a collection
 * @author Anthony Schmitt
 *
 */
public class CollectionGameAdder extends JFrame
{
	private QueryRunner query;
	private int game_id;
	private String game_title;
	private JButton add, cancel;
	private JTextField notesField;
	private JCheckBox haveBox, isSealed, haveBooklet;
	private JComboBox<String> collectionSelecter;
	private JComboBox<Integer> raitingSelecter;
	private JPanel buttonPanel;
	private String[] collections;
	
	/**
	 * Constructor.
	 * @param q QueryRunner.
	 * @param gid the ID number for the game.
	 * @param gtitle The title for the game.
	 */
	public CollectionGameAdder(QueryRunner q, int gid, String gtitle)
	{
		this.query = q;
		this.game_id = gid;
		this.game_title = gtitle;
		init();
	}

	/**
	 * Does the initial set up for the object.
	 */
	private void init()
	{
		this.setTitle("Add " + game_title + " to a Collection");
		JLabel notesLabel = new JLabel("Notes on copy of game:");
		notesField = new JTextField();
		JPanel notesPanel = new JPanel();
		notesPanel.setLayout(new GridLayout(2,1));
		notesPanel.add(notesLabel);
		notesPanel.add(notesField);
		haveBox = new JCheckBox("Have Game Box");
		isSealed = new JCheckBox("Is Box Sealed");
		haveBooklet = new JCheckBox("Have Game Booklet");
		JPanel cbPanel = new JPanel();
		cbPanel.setLayout(new GridLayout(1,3));
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
		JLabel rateLabel = new JLabel("Rate Game:");
		raitingSelecter = new JComboBox<Integer>();
		raitingSelecter.addItem(0);
		raitingSelecter.addItem(1);
		raitingSelecter.addItem(2);
		raitingSelecter.addItem(3);
		raitingSelecter.addItem(4);
		raitingSelecter.addItem(5);
		JPanel ratePanel = new JPanel();
		ratePanel.setLayout(new GridLayout(2,1));
		ratePanel.add(rateLabel);
		ratePanel.add(raitingSelecter);
		JPanel inerPanel = new JPanel();
		inerPanel.setLayout(new GridLayout(3,1));
		inerPanel.add(notesPanel);
		inerPanel.add(cbPanel);
		inerPanel.add(ratePanel);
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
				boolean in = query.storeProcedure("inCollection", game_id, col);
				
				if(!in)
				{
					String notes = notesField.getText();
					if(notes.equals(""))
					{
						notes = "null";
					}
					query.addGameToCollection(game_id, col, isSealed.isSelected(), haveBox.isSelected(), haveBooklet.isSelected(), raitingSelecter.getSelectedIndex(), notes);
					JOptionPane.showMessageDialog(null,game_title + " had been added to collection " + col,"Game Added to Collection",JOptionPane.INFORMATION_MESSAGE);
					clearFields();
					invisible();
				}
				else
				{
					JOptionPane.showMessageDialog(null,game_title + " already exists in collection " + col,"Already in Collection",JOptionPane.WARNING_MESSAGE);
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
	 * Updates the title
	 */
	private void update()
	{
		clearFields();
		this.setTitle("Add " + game_title + " to a Collection");
	}
	
	/**
	 * Makes the object invisible.
	 */
	private void invisible()
	{
		this.setVisible(false);
	}
	
	/**
	 * Clears the fields.
	 */
	private void clearFields()
	{
		notesField.setText("");
		haveBox.setSelected(false);
		haveBooklet.setSelected(false);
		isSealed.setSelected(false);
		raitingSelecter.setSelectedIndex(0);
		collectionSelecter.setSelectedIndex(0);
	}
	
	/**
	 * Changes the game.
	 * @param gid Game ID number.
	 * @param gtitle Game title.
	 */
	public void setGame(int gid, String gtitle)
	{
		this.game_id = gid;
		this.game_title = gtitle;
		update();
	}
	
}
