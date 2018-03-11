package frontend;


import java.awt.*;
import java.awt.event.*;
import backend.*;
import javax.swing.*;

/**
 * Shows detailed information about a game
 * allows it to be edited or added to collection
 * @author Anthony Schmitt
 *
 */
public class GameView extends JFrame implements ActionListener, ConsoleSelectable, SingleDateSelectable, CompanySelectable, GenreSelectable
{
	private QueryRunner q;
	private int game_id;
	private String title, console, release_date, format, notes, publisher, genres, developers, orgTitle, orgConsole, orgRlease_date, orgFormat, orgNotes, orgPublisher;
	private JPanel infoPanel, buttonPanel;
	private JTextField titleField, formatField, notesArea, consoleField, releaseDateField, publisherField, genresField, developersField;
	private JButton add, apply, cancel, edit, consoleButton, releaseDateButton, publisherButton, developerButton, genreButton;
	private String[] genreArray, devArray, orgGenreArray, orgDevArray;
	private boolean consoleChanged, release_dateChanged, publisherChanged, genresChanged, devChanged;
	private ConsoleSelectOptions cs;
	private SingleDateSelectOptions sds;
	private CompanySelectOptions p, d;
	private GenreSelectOptions g;
	private CollectionGameAdder cga;
	
	/**
	 * Constructor.
	 * @param q Query Runner
	 * @param id Game's ID number
	 */
	public GameView(QueryRunner q, int id)
	{
		super("Game View");
		this.q = q;
		this.game_id = id;
		init();
	}
	
	/**
	 * Constructor.
	 * @param q Query Runner
	 */
	public GameView(QueryRunner q)
	{
		super("Game View");
		this.q = q;
		this.game_id = 1;
		init();
	}
	
	/**
	 * Sets the game.
	 * @param id Game's ID number
	 */
	public void setGame(int id)
	{
		this.game_id = id;
		updateInfo();
	}

	/**
	 * Updates the displayed information.
	 */
	private void updateInfo()
	{
		String query = "select title, Console.name, Game.release_date, format, Game.notes, Company.name from Game, Console, Company where Game.console_id = Console.id AND Game.publisher_id = Company.id AND Game.id = " + game_id + ";";
		String[] tmp = q.querySingleListReturnRow(query);
		this.title = tmp[0];
		this.orgTitle = title;
		this.console = tmp[1];
		this.orgConsole = console;
		this.release_date = tmp[2];
		this.orgRlease_date = release_date;
		this.format = tmp[3];
		this.orgFormat = format;
		this.notes = tmp[4];
		if(notes.equals("null"))
		{
			notes = "";
		}
		this.orgNotes = notes;
		this.publisher = tmp[5];
		this.orgPublisher = publisher;
		this.genresChanged = false;
		this.devChanged = false;
		this.publisherChanged = false;
		this.consoleChanged = false;
		this.release_dateChanged = false;
		this.genres = "";
		this.genreArray = q.querySingleListReturnColumn("select Genre.name from Has_Genre, Genre where Genre.id = Has_Genre.genre_id AND Has_Genre.game_id = " + game_id + ";");
		this.orgGenreArray = new String[genreArray.length];
		for(int a = 0; a < genreArray.length; a++)
		{
			orgGenreArray[a] = genreArray[a];
			genres += genreArray[a];
			if(a != genreArray.length-1)
			{
				genres += ", ";
			}
		}
		this.developers = "";
		this.devArray = q.querySingleListReturnColumn("select Company.name from Developed_Game, Company where Company.id = Developed_Game.company_id AND Developed_Game.game_id = "+game_id+";");
		this.orgDevArray = new String[devArray.length];
		for(int a = 0; a < devArray.length; a++)
		{
			orgDevArray[a] = devArray[a];
			developers += devArray[a];
			if(a != devArray.length-1)
			{
				developers += ", ";
			}
		}
		titleField.setText(title);
		formatField.setText(format);
		notesArea.setText(notes);
		consoleField.setText(console);
		releaseDateField.setText(release_date);
		publisherField.setText(publisher);
		genresField.setText(genres);
		developersField.setText(developers);
		cs.setSelected(console);
		int yr = Integer.parseInt(release_date.substring(0, 4));
		int dy = Integer.parseInt(release_date.substring(8, 10));
		String mo = sds.numToMonth(Integer.parseInt(release_date.substring(5, 7)));
		sds.setSelected(yr, mo, dy);
		p.setSelected(publisher);
		d.setSelected(devArray);
		g.setSelected(genreArray);
		cga.setGame(game_id, title);
	}

	/**
	 * Initializes the object.
	 */
	private void init()
	{
		String query = "select title, Console.name, Game.release_date, format, Game.notes, Company.name from Game, Console, Company where Game.console_id = Console.id AND Game.publisher_id = Company.id AND Game.id = " + game_id + ";";
		String[] tmp = q.querySingleListReturnRow(query);
		this.title = tmp[0];
		this.orgTitle = title;
		this.console = tmp[1];
		this.orgConsole = console;
		this.release_date = tmp[2];
		this.orgRlease_date = release_date;
		this.format = tmp[3];
		this.orgFormat = format;
		this.notes = tmp[4];
		if(notes.equals("null"))
		{
			notes = "";
		}
		this.orgNotes = notes;
		this.publisher = tmp[5];
		this.orgPublisher = publisher;
		this.genres = "";
		this.genreArray = q.querySingleListReturnColumn("select Genre.name from Has_Genre, Genre where Genre.id = Has_Genre.genre_id AND Has_Genre.game_id = " + game_id + ";");
		this.orgGenreArray = new String[genreArray.length];
		this.genresChanged = false;
		this.devChanged = false;
		this.publisherChanged = false;
		this.consoleChanged = false;
		this.release_dateChanged = false;
		for(int a = 0; a < genreArray.length; a++)
		{
			orgGenreArray[a] = genreArray[a];
			genres += genreArray[a];
			if(a != genreArray.length-1)
			{
				genres += ", ";
			}
		}
		this.developers = "";
		this.devArray = q.querySingleListReturnColumn("select Company.name from Developed_Game, Company where Company.id = Developed_Game.company_id AND Developed_Game.game_id = "+game_id+";");
		this.orgDevArray = new String[devArray.length];
		for(int a = 0; a < devArray.length; a++)
		{
			orgDevArray[a] = devArray[a];
			developers += devArray[a];
			if(a != devArray.length-1)
			{
				developers += ", ";
			}
		}
		setUpButtons();
		setUpView();
		this.setLayout(new BorderLayout());
		this.add(infoPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-400;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-400;
		this.setLocation(width, height);
		this.setSize(600, 400);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e)
			{
				edit.setVisible(true);
				apply.setVisible(false);
				cancel.setVisible(false);
				edit.setEnabled(true);
				titleField.setEditable(false);
				formatField.setEditable(false);
				notesArea.setEditable(false);
				consoleButton.setVisible(false);
				releaseDateButton.setVisible(false);
				publisherButton.setVisible(false);
				developerButton.setVisible(false);
				genreButton.setVisible(false);
				invisible();
			}
		});
		cs = new ConsoleSelectOptions(q, this, false);
		cs.setVisible(false);
		cs.setClearButtonEnable(false);
		cs.setTitle("Change Console");
		sds = new SingleDateSelectOptions(this);
		sds.setVisible(false);
		p = new CompanySelectOptions(q, this, false, Queries.PUBLISHERS);
		p.setVisible(false);
		p.setClearEnable(false);
		p.setTitle("Change Publisher");
		d = new CompanySelectOptions(q, this, true, Queries.DEVELOPERS);
		d.setVisible(false);
		d.setTitle("Change Developers");
		d.setClearEnable(false);
		g = new GenreSelectOptions(q,this);
		g.setVisible(false);
		g.setTitle("Change Genres");
		g.setClearEnable(false);
		cga = new CollectionGameAdder(q,game_id,title);
		cga.setVisible(false);
		
		cs.setSelected(console);
		int yr = Integer.parseInt(release_date.substring(0, 4));
		int dy = Integer.parseInt(release_date.substring(8, 10));
		String mo = sds.numToMonth(Integer.parseInt(release_date.substring(5, 7)));
		sds.setSelected(yr, mo, dy);
		p.setSelected(publisher);
		d.setSelected(devArray);
		g.setSelected(genreArray);
	}

	/**
	 * Sets up the buttons.
	 */
	private void setUpButtons()
	{
		add = new JButton("Add to Collection");
		add.addActionListener(this);
		edit = new JButton("Edit Info");
		edit.addActionListener(this);
		apply = new JButton("Apply Changes");
		apply.setVisible(false);
		apply.addActionListener(this);
		cancel = new JButton("Cancel Changes");
		cancel.setVisible(false);
		cancel.addActionListener(this);
		consoleButton = new JButton("Edit Console");
		consoleButton.setVisible(false);
		consoleButton.addActionListener(this);
		releaseDateButton = new JButton("Edit Release Date");
		releaseDateButton.setVisible(false);
		releaseDateButton.addActionListener(this);
		publisherButton = new JButton("Edit Publisher");
		publisherButton.setVisible(false);
		publisherButton.addActionListener(this);
		developerButton = new JButton("Edit Developers");
		developerButton.setVisible(false);
		developerButton.addActionListener(this);
		genreButton = new JButton("Edit Genres");
		genreButton.setVisible(false);
		genreButton.addActionListener(this);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4,1));
		buttonPanel.add(add);
		buttonPanel.add(apply);
		buttonPanel.add(edit);
		buttonPanel.add(cancel);
	}

	/**
	 * Sets up the information display areas.
	 */
	private void setUpView()
	{
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
		consolePanel.setLayout(new GridLayout(3,1));
		consolePanel.add(consoleLabel);
		consolePanel.add(consoleField);
		consolePanel.add(consoleButton);
		JLabel releaseDateLabel = new JLabel("Release Date:");
		releaseDateField = new JTextField(release_date);
		releaseDateField.setEditable(false);
		JPanel releaseDatePanel = new JPanel();
		releaseDatePanel.setLayout(new GridLayout(3,1));
		releaseDatePanel.add(releaseDateLabel);
		releaseDatePanel.add(releaseDateField);
		releaseDatePanel.add(releaseDateButton);
		JLabel formatLabel = new JLabel("Format:");
		formatField = new JTextField(format);
		formatField.setEditable(false);
		JPanel formatPanel = new JPanel();
		formatPanel.setLayout(new GridLayout(2,1));
		formatPanel.add(formatLabel);
		formatPanel.add(formatField);
		JLabel notesLabel = new JLabel("Notes:");
		notesArea = new JTextField(notes);
		notesArea.setEditable(false);
		JPanel notesPanel = new JPanel();
		notesPanel.setLayout(new GridLayout(2,1));
		notesPanel.add(notesLabel);
		notesPanel.add(notesArea);
		JLabel publisherLabel = new JLabel("Publisher:");
		publisherField = new JTextField(publisher);
		publisherField.setEditable(false);
		JPanel publisherPanel = new JPanel();
		publisherPanel.setLayout(new GridLayout(3,1));
		publisherPanel.add(publisherLabel);
		publisherPanel.add(publisherField);
		publisherPanel.add(publisherButton);
		JLabel genresLabel = new JLabel("Genre(s):");
		genresField = new JTextField(genres);
		genresField.setEditable(false);
		JPanel genresPanel = new JPanel();
		genresPanel.setLayout(new GridLayout(3,1));
		genresPanel.add(genresLabel);
		genresPanel.add(genresField);
		genresPanel.add(genreButton);
		JLabel developersLabel = new JLabel("Developer(s):");
		developersField = new JTextField(developers);
		developersField.setEditable(false);
		JPanel developersPanel = new JPanel();
		developersPanel.setLayout(new GridLayout(3,1));
		developersPanel.add(developersLabel);
		developersPanel.add(developersField);
		developersPanel.add(developerButton);
		JPanel insidePanel = new JPanel();
		insidePanel.setLayout(new GridLayout(2,3));
		insidePanel.add(consolePanel);
		insidePanel.add(releaseDatePanel);
		insidePanel.add(formatPanel);
		insidePanel.add(publisherPanel);
		insidePanel.add(developersPanel);
		insidePanel.add(genresPanel);
		infoPanel = new JPanel();
		infoPanel.setLayout(new BorderLayout());
		infoPanel.add(titlePanel, BorderLayout.NORTH);
		infoPanel.add(insidePanel, BorderLayout.CENTER);
		infoPanel.add(notesPanel, BorderLayout.SOUTH);
	}

	/**
	 * Makes the window invisible.
	 */
	private final void invisible()
	{
		this.setVisible(false);
	}

	/**
	 * Handles button presses.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Edit Info"))
		{
			edit.setEnabled(false);
			add.setEnabled(false);
			titleField.setEditable(true);
			formatField.setEditable(true);
			notesArea.setEditable(true);
			apply.setVisible(true);
			cancel.setVisible(true);
			consoleButton.setVisible(true);
			releaseDateButton.setVisible(true);
			publisherButton.setVisible(true);
			developerButton.setVisible(true);
			genreButton.setVisible(true);
		}
		if(e.getActionCommand().equals("Apply Changes"))
		{
			edit.setVisible(true);
			add.setEnabled(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			titleField.setEditable(false);
			formatField.setEditable(false);
			notesArea.setEditable(false);
			consoleButton.setVisible(false);
			releaseDateButton.setVisible(false);
			publisherButton.setVisible(false);
			developerButton.setVisible(false);
			genreButton.setVisible(false);
			title = titleField.getText();
			if(title.length() > 255)
			{
				title = title.substring(0, 254);
			}
			format = formatField.getText();
			if(format.length() > 255)
			{
				format = format.substring(0, 254);
			}
			notes = notesArea.getText();
			databaseUpdate();
			updateInfo();
		}
		if(e.getActionCommand().equals("Cancel Changes"))
		{
			edit.setVisible(true);
			add.setEnabled(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			titleField.setEditable(false);
			formatField.setEditable(false);
			notesArea.setEditable(false);
			consoleButton.setVisible(false);
			releaseDateButton.setVisible(false);
			publisherButton.setVisible(false);
			developerButton.setVisible(false);
			genreButton.setVisible(false);
			updateInfo();
		}
		if(e.getActionCommand().equals("Edit Console"))
		{
			cs.setVisible(true);
		}
		if(e.getActionCommand().equals("Edit Release Date"))
		{
			sds.setVisible(true);
		}
		if(e.getActionCommand().equals("Edit Publisher"))
		{
			p.setVisible(true);
		}
		if(e.getActionCommand().equals("Edit Developers"))
		{
			d.setVisible(true);
		}
		if(e.getActionCommand().equals("Edit Genres"))
		{
			g.setVisible(true);
		}
		if(e.getActionCommand().equals("Add to Collection"))
		{
			cga.setVisible(true);
		}
	}
	
	/**
	 * Updates the displayed information.
	 */
	private void databaseUpdate()
	{
		if(!title.equals(orgTitle))
		{
			q.storeProcedureJustIn("updateGameTitle", game_id, title);
		}
		if(release_dateChanged)
		{
			q.storeProcedureJustIn("updateGameRelease_Date", game_id, release_date);
		}
		if(!format.equals(orgFormat))
		{
			q.storeProcedureJustIn("updateGameFormat", game_id, format);
		}
		if(!notes.equals(orgNotes))
		{
			if(notes.equals("") || notes == null)
			{
				notes = "null";
			}
			q.storeProcedureJustIn("updateGameNotes", game_id, notes);
		}
		if(consoleChanged)
		{
			int tmp = q.storeProcedure("getConsoleID", console);
			q.storeProcedureJustIn("updateGameConsole", game_id, tmp);
		}
		if(publisherChanged)
		{
			int tmp = q.storeProcedure("getCompanyID", publisher);
			q.storeProcedureJustIn("updateGamePublisher", game_id, tmp);
		}
		if(devChanged)
		{
			q.storeProcedureJustIn("deleteGameDevelopers", game_id);
			for(int a = 0; a < devArray.length; a++)
			{
				int tmp = q.storeProcedure("getCompanyID", devArray[a]);
				q.storeProcedureJustIn("addGameDevelopers", game_id, tmp);
			}
		}
		if(genresChanged)
		{
			q.storeProcedureJustIn("deleteGameGenres", game_id);
			for(int a = 0; a < genreArray.length; a++)
			{
				int tmp = q.storeProcedure("getGenreID", genreArray[a]);
				q.storeProcedureJustIn("addGameGenres", game_id, tmp);
			}
		}
	}

	/**
	 * For having the ConsoleSelectOptions pass selected item through.
	 */
	public void haveGrabSelectedConsoleList()
	{
		console = q.storeProcedureString("getConsoleName", cs.getSelected()[0]);
		consoleField.setText(console);
		consoleChanged = (!console.equals(orgConsole));
	}
	
	/**
	 * For having the SingleDateSelectOptions pass selected date through.
	 */
	public void haveGrabSelectedDate()
	{
		release_date = sds.getSelected();
		releaseDateField.setText(release_date);
		release_dateChanged = (!release_date.equals(orgRlease_date));
	}

	/**
	 * For having the CompanySelectOptions pass selected items through.
	 */
	public void haveGrabSelectedCompanyList()
	{
		publisher = q.storeProcedureString("getCompanyName", p.getSelected()[0]);
		publisherField.setText(publisher);
		publisherChanged = (!publisher.equals(orgPublisher));
		
		int[] tmp = d.getSelected();
		devArray = new String[tmp.length];
		if(devArray.length < 1)
		{
			JOptionPane.showMessageDialog(null,"You must have at least 1 developer selected.","Missing Developer",JOptionPane.ERROR_MESSAGE);
		}
		for(int a = 0; a < tmp.length; a++)
		{
			devArray[a] = q.storeProcedureString("getCompanyName", d.getSelected()[a]);
		}
		developers = "";
		for(int a = 0; a < devArray.length; a++)
		{
			if(devArray.length == orgDevArray.length && !devArray[a].equals(orgDevArray[a]))
			{
				devChanged = true;
			}
			else if(devArray.length != orgDevArray.length)
			{
				devChanged = true;
			}
			developers += devArray[a];
			if(a != devArray.length-1)
			{
				developers += ", ";
			}
		}
		developersField.setText(developers);
	}

	/**
	 * For having the GenreSelectOptions pass selected item through.
	 */
	public void haveGrabSelectedGenreList()
	{
		int[] tmp = g.getSelected();
		genreArray = new String[tmp.length];
		for(int a = 0; a < tmp.length; a++)
		{
			genreArray[a] = q.storeProcedureString("getGenreName", g.getSelected()[a]);
		}
		genres = "";
		if(genreArray.length != orgGenreArray.length)
		{
			genresChanged = true;
		}
		if(genreArray.length > 0)
		{
			for(int a = 0; a < genreArray.length; a++)
			{
				if(!genreArray[a].equals(orgGenreArray[a]))
				{
					genresChanged = true;
				}
				genres += genreArray[a];
				if(a != genreArray.length-1)
				{
					genres += ", ";
				}
			}
		}
		genresField.setText(genres);
	}
	
}