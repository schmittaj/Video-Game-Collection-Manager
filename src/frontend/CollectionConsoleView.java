package frontend;

import java.awt.*;
import java.awt.event.*;
import backend.*;
import javax.swing.*;

/**
 * Shows user information about a specific console in the collection
 * can edit information for the consoles in collection
 * can remove consoles from collections as well
 * @author Anthony Schmitt
 *
 */
public class CollectionConsoleView extends JFrame implements ActionListener
{
	private QueryRunner q;
	private int console_id;
	private String console, orgNotes, copyNotes, collection;
	private JPanel infoPanel, buttonPanel;
	private JTextField consoleNameField, copyNotesField;
	private JButton apply, cancel, edit, remove;
	private boolean box, seal, booklet, cord, orgCord, orgBox, orgSeal, orgBooklet;
	private JCheckBox haveBox, isSealed, haveBooklet, haveCord;
	private CollectionConsoleListScreen ccv;
	
	/**
	 * Constructor
	 * @param q The connection to the database
	 * @param id The ID of the console that's being viewed
	 * @param coll The name of the collection that is being viewed
	 * @param cg an interface to the calling object so that we can have it update to reflect any changes that are made here
	 */
	public CollectionConsoleView(QueryRunner q, int id, String coll, CollectionConsoleListScreen cg)
	{
		super("Collection Console View");
		this.q = q;
		this.console_id = id;
		this.collection = coll;
		this.ccv = cg;
		init();
	}
	
	/**
	 * Alternate Constructor
	 * @param q The connection to the database
	 * @param cg an interface to the calling object so that we can have it update to reflect any changes that are made here
	 */
	public CollectionConsoleView(QueryRunner q, CollectionConsoleListScreen cg)
	{
		super("Collection Console View");
		this.q = q;
		this.console_id = 1;
		this.ccv = cg;
		init();
	}
	
	/**
	 * Sets the console and updates the information accordingly
	 * @param id Console ID
	 * @param col Collection Name
	 */
	public void setConsole(int id, String col)
	{
		this.console_id = id;
		this.collection = col;
		updateInfo();
	}

	/**
	 * Grabs the information for the console and it's record in the collection from the database and fills that into its fields.
	 */
	private void updateInfo()
	{
		String query = "select Console.name, have_cords, have_booklet, have_box, is_sealed, notes from Console_In_Collection, Console where Console_In_Collection.console_id = Console.id AND Console.id = " + console_id + " AND Console_In_Collection.collection_name = " + collection +";";
		String[] tmp = q.querySingleListReturnRow(query);
		this.console = tmp[0];
		this.cord = Boolean.parseBoolean(tmp[1]);
		this.booklet = Boolean.parseBoolean(tmp[2]);
		this.box = Boolean.parseBoolean(tmp[3]);
		this.seal = Boolean.parseBoolean(tmp[4]);
		this.copyNotes = tmp[5];
		if(copyNotes.equals("null"))
		{
			copyNotes = "";
		}
		this.orgSeal = seal;
		this.orgBox = box;
		this.orgBooklet = booklet;
		this.orgCord = cord;
		this.orgNotes = copyNotes;
		consoleNameField.setText(console);
		haveCord.setSelected(cord);
		isSealed.setSelected(seal);
		haveBox.setSelected(box);
		haveBooklet.setSelected(booklet);
		copyNotesField.setText(copyNotes);
	}

	/**
	 * Sets up the window
	 */
	private void init()
	{
		setUpButtons();
		setUpView();
		this.setLayout(new BorderLayout());
		this.add(infoPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-400;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-400;
		this.setLocation(width, height);
		this.setSize(300, 400);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e)
			{
				edit.setVisible(true);
				remove.setEnabled(false);
				apply.setVisible(false);
				cancel.setVisible(false);
				edit.setEnabled(true);
				copyNotesField.setEditable(false);
				invisible();
			}
		});
	}

	/**
	 * Sets up the buttons.
	 */
	private void setUpButtons()
	{
		remove = new JButton("Remove from Collection");
		remove.addActionListener(this);
		remove.setEnabled(false);
		edit = new JButton("Edit Info");
		edit.addActionListener(this);
		apply = new JButton("Apply Changes");
		apply.setVisible(false);
		apply.addActionListener(this);
		cancel = new JButton("Cancel Changes");
		cancel.setVisible(false);
		cancel.addActionListener(this);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4,1));
		buttonPanel.add(edit);
		buttonPanel.add(remove);
		buttonPanel.add(apply);
		buttonPanel.add(cancel);
	}


	/**
	 * Sets up all of the inputs and information displays
	 */
	private void setUpView()
	{
		haveBox = new JCheckBox("Have Console Box");
		isSealed = new JCheckBox("Is Box Sealed");
		haveBooklet = new JCheckBox("Have Console Booklet");
		haveCord = new JCheckBox("Have Console Cords");
		JPanel cbPanel = new JPanel();
		cbPanel.setLayout(new GridLayout(4,1));
		cbPanel.add(haveBooklet);
		cbPanel.add(haveBox);
		cbPanel.add(isSealed);
		cbPanel.add(haveCord);
		haveBooklet.setSelected(booklet);
		haveBox.setSelected(box);
		isSealed.setSelected(seal);
		haveCord.setSelected(cord);
		haveBooklet.setEnabled(false);
		haveBox.setEnabled(false);
		isSealed.setEnabled(false);
		haveCord.setEnabled(false);
		haveBox.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				if(haveBox.isEnabled())
				{
					isSealed.setEnabled(haveBox.isSelected());
				}
				if(!haveBox.isSelected())
				{
					isSealed.setSelected(false);
				}
			}
		});
		JLabel copyNotesLabel = new JLabel("Copy Notes:");
		copyNotesField = new JTextField(copyNotes);
		copyNotesField.setEditable(false);
		JPanel copyNotesPanel = new JPanel();
		copyNotesPanel.setLayout(new GridLayout(2,1));
		copyNotesPanel.add(copyNotesLabel);
		copyNotesPanel.add(copyNotesField);
		JLabel consoleNameLabel = new JLabel("Console Name:");
		consoleNameField = new JTextField(console);
		consoleNameField.setEditable(false);
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridLayout(2,1));
		titlePanel.add(consoleNameLabel);
		titlePanel.add(consoleNameField);
		JPanel nearlyTherePanel = new JPanel();
		nearlyTherePanel.setLayout(new BorderLayout());
		nearlyTherePanel.add(titlePanel, BorderLayout.NORTH);
		nearlyTherePanel.add(cbPanel, BorderLayout.CENTER);
		nearlyTherePanel.add(copyNotesPanel, BorderLayout.SOUTH);
		infoPanel = new JPanel();
		infoPanel.add(nearlyTherePanel);
		
	}


	/**
	 * Makes the window invisible.
	 */
	private final void invisible()
	{
		this.setVisible(false);
	}

	/**
	 * Handles the button presses.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Edit Info"))
		{
			edit.setEnabled(false);
			apply.setVisible(true);
			cancel.setVisible(true);
			remove.setEnabled(true);
			copyNotesField.setEditable(true);
			haveBox.setEnabled(true);
			haveCord.setEnabled(true);
			if(haveBox.isSelected())
			{
				isSealed.setEnabled(true);
			}
			haveBooklet.setEnabled(true);
		}
		if(e.getActionCommand().equals("Apply Changes"))
		{
			edit.setVisible(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			remove.setEnabled(false);
			copyNotesField.setEditable(false);
			haveBox.setEnabled(false);
			isSealed.setEnabled(false);
			haveBooklet.setEnabled(false);
			haveCord.setEnabled(false);
			copyNotes = copyNotesField.getText();
			if(copyNotes == null || copyNotes.equals(""))
			{
				copyNotes = "null";
			}
			box = haveBox.isSelected();
			seal = isSealed.isSelected();
			booklet = haveBooklet.isSelected();
			cord = haveCord.isSelected();
			databaseUpdate();
			updateInfo();
			ccv.updateList();
		}
		if(e.getActionCommand().equals("Cancel Changes"))
		{
			edit.setVisible(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			remove.setEnabled(false);
			copyNotesField.setEditable(false);
			haveCord.setEnabled(false);
			haveBox.setEnabled(false);
			isSealed.setEnabled(false);
			haveBooklet.setEnabled(false);
			updateInfo();
		}
		if(e.getActionCommand().equals("Remove from Collection"))
		{
			q.storeProcedureJustIn("removeConsoleFromCollection", console_id, collection.substring(1, collection.length()-1));
			JOptionPane.showMessageDialog(null,console + " had been removed from collection " + collection+".","Game Removed",JOptionPane.INFORMATION_MESSAGE);
			edit.setVisible(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			remove.setEnabled(false);
			copyNotesField.setEditable(false);
			haveCord.setEnabled(false);
			haveBox.setEnabled(false);
			isSealed.setEnabled(false);
			haveBooklet.setEnabled(false);
			ccv.updateList();
			invisible();
		}
	}
	
	/**
	 * Updates the database with any changes made
	 */
	private void databaseUpdate()
	{
		String tmp = collection.substring(1, collection.length()-1);
		if(!orgNotes.equals(copyNotes))
		{
			if(copyNotes.equals("") || copyNotes == null)
			{
				copyNotes = "null";
			}
			q.storeProcedureJustIn("updateConsoleNotesCollection", console_id, tmp, copyNotes);
		}
		if(box != orgBox)
		{
			q.storeProcedureJustIn("updateConsoleBox", console_id, tmp, box);
		}
		if(booklet != orgBooklet)
		{
			q.storeProcedureJustIn("updateConsoleBooklet", console_id, tmp, booklet);
		}
		if(seal != orgSeal)
		{
			q.storeProcedureJustIn("updateConsoleSealed", console_id, tmp, seal);
		}
		if(cord != orgCord)
		{
			q.storeProcedureJustIn("updateConsoleCord", console_id, tmp, cord);
		}
	}



	
}