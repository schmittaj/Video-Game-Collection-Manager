package frontend;

import java.awt.*;
import java.awt.event.*;
import backend.*;
import javax.swing.*;

/**
 * Shows more information about a game in a collection
 * allows for editing of info as well as removal from collection
 * @author Anthony Schmitt
 *
 */
public class CollectionGameView extends JFrame implements ActionListener
{
	private QueryRunner q;
	private int game_id, orgRate, rating;
	private String title, console, release_date, format, game_notes, publisher, genres, developers, orgNotes, copyNotes, collection;
	private JPanel infoPanel, buttonPanel;
	private JTextField titleField, formatField, notesArea, consoleField, releaseDateField, publisherField, genresField, developersField, copyNotesField;
	private JButton apply, cancel, edit, remove;
	private String[] genreArray, devArray;
	private boolean box, seal, booklet, orgBox, orgSeal, orgBooklet;
	private JCheckBox haveBox, isSealed, haveBooklet;
	private JComboBox<Integer> ratingSelecter;
	private ListScreen cgv;
	
	/**
	 * Constructor.
	 * @param q QueryRunner
	 * @param id Game ID
	 * @param coll Collection Name
	 * @param cg Referring ListScreen
	 */
	public CollectionGameView(QueryRunner q, int id, String coll, ListScreen cg)
	{
		super("Collection Game View");
		this.q = q;
		this.game_id = id;
		this.cgv = cg;
		init();
	}
	
	/**
	 * Constructor.
	 * @param q QueryRunner
	 * @param cg Referring ListScreen
	 */
	public CollectionGameView(QueryRunner q, ListScreen cg)
	{
		super("Collection Game View");
		this.q = q;
		this.game_id = 1;
		this.cgv = cg;
		init();
	}
	
	/**
	 * Sets the game and collection.
	 * @param id Game ID.
	 * @param col Collection Name
	 */
	public void setGame(int id, String col)
	{
		this.game_id = id;
		this.collection = col;
		updateInfo();
	}

	/**
	 * Updates the info displayed.
	 */
	private void updateInfo()
	{
		String query = "select title, Console.name, Game.release_date, format, Game.notes, Company.name from Game, Console, Company where Game.console_id = Console.id AND Game.publisher_id = Company.id AND Game.id = " + game_id + ";";
		String[] tmp = q.querySingleListReturnRow(query);
		this.title = tmp[0];
		this.console = tmp[1];
		this.release_date = tmp[2];
		this.format = tmp[3];
		this.game_notes = tmp[4];
		if(game_notes.equals("null"))
		{
			game_notes = "";
		}
		this.publisher = tmp[5];
		this.genres = "";
		this.genreArray = q.querySingleListReturnColumn("select Genre.name from Has_Genre, Genre where Genre.id = Has_Genre.genre_id AND Has_Genre.game_id = " + game_id + ";");
		for(int a = 0; a < genreArray.length; a++)
		{
			genres += genreArray[a];
			if(a != genreArray.length-1)
			{
				genres += ", ";
			}
		}
		this.developers = "";
		this.devArray = q.querySingleListReturnColumn("select Company.name from Developed_Game, Company where Company.id = Developed_Game.company_id AND Developed_Game.game_id = "+game_id+";");
		for(int a = 0; a < devArray.length; a++)
		{
			developers += devArray[a];
			if(a != devArray.length-1)
			{
				developers += ", ";
			}
		}
		query = "select is_sealed, have_box, have_booklet, rating, notes from Game_In_Collection where game_id = "+game_id+" AND collection_name = "+collection+";";
		tmp = q.querySingleListReturnRow(query);
		this.seal = Boolean.parseBoolean(tmp[0]);
		this.orgSeal = seal;
		this.box  = Boolean.parseBoolean(tmp[1]);
		this.orgBox = box;
		this.booklet = Boolean.parseBoolean(tmp[2]);
		this.orgBooklet = booklet;
		this.rating = Integer.parseInt(tmp[3]);
		this.orgRate = rating;
		this.copyNotes = tmp[4];
		if(copyNotes.equals("null"))
		{
			copyNotes = "";
		}
		this.orgNotes = copyNotes;
		titleField.setText(title);
		formatField.setText(format);
		notesArea.setText(game_notes);
		consoleField.setText(console);
		releaseDateField.setText(release_date);
		publisherField.setText(publisher);
		genresField.setText(genres);
		developersField.setText(developers);
		isSealed.setSelected(seal);
		haveBox.setSelected(box);
		haveBooklet.setSelected(booklet);
		ratingSelecter.setSelectedIndex(rating);
		copyNotesField.setText(copyNotes);
	}

	/**
	 * Initializes the object.
	 */
	private void init()
	{
		String query = "select title, Console.name, Game.release_date, format, Game.notes, Company.name from Game, Console, Company where Game.console_id = Console.id AND Game.publisher_id = Company.id AND Game.id = " + game_id + ";";
		String[] tmp = q.querySingleListReturnRow(query);
		this.title = tmp[0];
		this.console = tmp[1];
		this.release_date = tmp[2];
		this.format = tmp[3];
		this.game_notes = tmp[4];
		if(game_notes.equals("null"))
		{
			game_notes = "";
		}
		this.orgNotes = game_notes;
		this.publisher = tmp[5];
		this.genres = "";
		this.genreArray = q.querySingleListReturnColumn("select Genre.name from Has_Genre, Genre where Genre.id = Has_Genre.genre_id AND Has_Genre.game_id = " + game_id + ";");
		for(int a = 0; a < genreArray.length; a++)
		{
			genres += genreArray[a];
			if(a != genreArray.length-1)
			{
				genres += ", ";
			}
		}
		this.developers = "";
		this.devArray = q.querySingleListReturnColumn("select Company.name from Developed_Game, Company where Company.id = Developed_Game.company_id AND Developed_Game.game_id = "+game_id+";");
		for(int a = 0; a < devArray.length; a++)
		{
			developers += devArray[a];
			if(a != devArray.length-1)
			{
				developers += ", ";
			}
		}
		tmp = new String[] {"false","false","false","0",""};
		this.seal = Boolean.parseBoolean(tmp[0]);
		this.orgSeal = seal;
		this.box  = Boolean.parseBoolean(tmp[1]);
		this.orgBox = box;
		this.booklet = Boolean.parseBoolean(tmp[2]);
		this.orgBooklet = booklet;
		this.rating = Integer.parseInt(tmp[3]);
		this.orgRate = rating;
		this.copyNotes = tmp[4];
		if(copyNotes.equals("null"))
		{
			copyNotes = "";
		}
		this.orgNotes = copyNotes;
		setUpButtons();
		setUpView();
		this.setLayout(new BorderLayout());
		this.add(infoPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-400;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-400;
		this.setLocation(width, height);
		this.setSize(600, 600);
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
				titleField.setEditable(false);
				formatField.setEditable(false);
				notesArea.setEditable(false);
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
	 * Sets up the things to display the info.
	 */
	private void setUpView()
	{
		haveBox = new JCheckBox("Have Game Box");
		isSealed = new JCheckBox("Is Box Sealed");
		haveBooklet = new JCheckBox("Have Game Booklet");
		JPanel cbPanel = new JPanel();
		cbPanel.setLayout(new GridLayout(1,3));
		cbPanel.add(haveBooklet);
		cbPanel.add(haveBox);
		cbPanel.add(isSealed);
		haveBooklet.setSelected(booklet);
		haveBox.setSelected(box);
		isSealed.setSelected(seal);
		haveBooklet.setEnabled(false);
		haveBox.setEnabled(false);
		isSealed.setEnabled(false);
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
		JLabel rateLabel = new JLabel("Game Rating:");
		ratingSelecter = new JComboBox<Integer>();
		ratingSelecter.addItem(0);
		ratingSelecter.addItem(1);
		ratingSelecter.addItem(2);
		ratingSelecter.addItem(3);
		ratingSelecter.addItem(4);
		ratingSelecter.addItem(5);
		ratingSelecter.setEditable(false);
		ratingSelecter.setEnabled(false);
		JPanel ratePanel = new JPanel();
		ratePanel.setLayout(new GridLayout(2,1));
		ratePanel.add(rateLabel);
		ratePanel.add(ratingSelecter);
		JLabel copyNotesLabel = new JLabel("Copy Notes:");
		copyNotesField = new JTextField(copyNotes);
		copyNotesField.setEditable(false);
		JPanel copyNotesPanel = new JPanel();
		copyNotesPanel.setLayout(new GridLayout(2,1));
		copyNotesPanel.add(copyNotesLabel);
		copyNotesPanel.add(copyNotesField);
		JPanel anotherPanel = new JPanel();
		anotherPanel.setLayout(new GridLayout(2,1));
		anotherPanel.add(ratePanel);
		anotherPanel.add(copyNotesPanel);
		JLabel titleLabel = new JLabel("Title:");
		titleField = new JTextField(title);
		titleField.setEditable(false);
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridLayout(2,1));
		titlePanel.add(titleLabel);
		titlePanel.add(titleField);
		JLabel consoleLabel = new JLabel("Console:");
		consoleField = new JTextField(console);
		consoleField.setEditable(false);
		JPanel consolePanel = new JPanel();
		consolePanel.setLayout(new GridLayout(2,1));
		consolePanel.add(consoleLabel);
		consolePanel.add(consoleField);
		JLabel releaseDateLabel = new JLabel("Release Date:");
		releaseDateField = new JTextField(release_date);
		releaseDateField.setEditable(false);
		JPanel releaseDatePanel = new JPanel();
		releaseDatePanel.setLayout(new GridLayout(2,1));
		releaseDatePanel.add(releaseDateLabel);
		releaseDatePanel.add(releaseDateField);
		JLabel formatLabel = new JLabel("Format:");
		formatField = new JTextField(format);
		formatField.setEditable(false);
		JPanel formatPanel = new JPanel();
		formatPanel.setLayout(new GridLayout(2,1));
		formatPanel.add(formatLabel);
		formatPanel.add(formatField);
		JLabel notesLabel = new JLabel("Game Notes:");
		notesArea = new JTextField(game_notes);
		notesArea.setEditable(false);
		JPanel notesPanel = new JPanel();
		notesPanel.setLayout(new GridLayout(2,1));
		notesPanel.add(notesLabel);
		notesPanel.add(notesArea);
		JLabel publisherLabel = new JLabel("Publisher:");
		publisherField = new JTextField(publisher);
		publisherField.setEditable(false);
		JPanel publisherPanel = new JPanel();
		publisherPanel.setLayout(new GridLayout(2,1));
		publisherPanel.add(publisherLabel);
		publisherPanel.add(publisherField);
		JLabel genresLabel = new JLabel("Genre(s):");
		genresField = new JTextField(genres);
		genresField.setEditable(false);
		JPanel genresPanel = new JPanel();
		genresPanel.setLayout(new GridLayout(2,1));
		genresPanel.add(genresLabel);
		genresPanel.add(genresField);
		JLabel developersLabel = new JLabel("Developer(s):");
		developersField = new JTextField(developers);
		developersField.setEditable(false);
		JPanel developersPanel = new JPanel();
		developersPanel.setLayout(new GridLayout(2,1));
		developersPanel.add(developersLabel);
		developersPanel.add(developersField);
		JPanel insidePanel = new JPanel();
		insidePanel.setLayout(new GridLayout(2,3));
		insidePanel.add(consolePanel);
		insidePanel.add(releaseDatePanel);
		insidePanel.add(formatPanel);
		insidePanel.add(publisherPanel);
		insidePanel.add(developersPanel);
		insidePanel.add(genresPanel);
		JPanel moreInside = new JPanel();
		moreInside.setLayout(new BorderLayout());
		moreInside.add(insidePanel, BorderLayout.CENTER);
		moreInside.add(cbPanel, BorderLayout.SOUTH);
		JPanel nearlyTherePanel = new JPanel();
		nearlyTherePanel.setLayout(new BorderLayout());
		nearlyTherePanel.add(titlePanel, BorderLayout.NORTH);
		nearlyTherePanel.add(moreInside, BorderLayout.CENTER);
		nearlyTherePanel.add(notesPanel, BorderLayout.SOUTH);
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(nearlyTherePanel, BorderLayout.CENTER);
		infoPanel.add(anotherPanel, BorderLayout.SOUTH);
		
	}

	/**
	 * Makes this window invisible.
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
			ratingSelecter.setEnabled(true);
			haveBox.setEnabled(true);
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
			ratingSelecter.setEnabled(false);
			haveBox.setEnabled(false);
			isSealed.setEnabled(false);
			haveBooklet.setEnabled(false);
			rating = ratingSelecter.getSelectedIndex();
			copyNotes = copyNotesField.getText();
			if(copyNotes == null || copyNotes.equals(""))
			{
				copyNotes = "null";
			}
			box = haveBox.isSelected();
			seal = isSealed.isSelected();
			booklet = haveBooklet.isSelected();
			databaseUpdate();
			updateInfo();
			cgv.updateList();
		}
		if(e.getActionCommand().equals("Cancel Changes"))
		{
			edit.setVisible(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			remove.setEnabled(false);
			copyNotesField.setEditable(false);
			ratingSelecter.setEnabled(false);
			haveBox.setEnabled(false);
			isSealed.setEnabled(false);
			haveBooklet.setEnabled(false);
			updateInfo();
		}
		if(e.getActionCommand().equals("Remove from Collection"))
		{
			q.storeProcedureJustIn("removeGameFromCollection", game_id, collection.substring(1, collection.length()-1));
			JOptionPane.showMessageDialog(null,title + " had been removed from collection " + collection+".","Game Removed",JOptionPane.INFORMATION_MESSAGE);
			edit.setVisible(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			remove.setEnabled(false);
			copyNotesField.setEditable(false);
			ratingSelecter.setEnabled(false);
			haveBox.setEnabled(false);
			isSealed.setEnabled(false);
			haveBooklet.setEnabled(false);
			cgv.updateList();
			invisible();
		}
	}
	
	/**
	 * Updates the database with any changes made.
	 */
	private void databaseUpdate()
	{
		String tmp = collection.substring(1, collection.length()-1);
		if(rating != orgRate)
		{
			q.storeProcedureJustIn("updateGameRating", game_id, tmp, rating);
		}
		if(!orgNotes.equals(copyNotes))
		{
			if(copyNotes.equals("") || copyNotes == null)
			{
				copyNotes = "null";
			}
			q.storeProcedureJustIn("updateGameNotesCollection", game_id, tmp, copyNotes);
		}
		if(box != orgBox)
		{
			q.storeProcedureJustIn("updateGameBox", game_id, tmp, box);
		}
		if(booklet != orgBooklet)
		{
			q.storeProcedureJustIn("updateGameBooklet", game_id, tmp, booklet);
		}
		if(seal != orgSeal)
		{
			q.storeProcedureJustIn("updateGameSealed", game_id, tmp, seal);
		}
	}
	
}