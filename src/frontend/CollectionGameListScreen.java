package frontend;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import backend.*;

/**
 * Shows the list of all games by collection
 * can switch between collections
 * @author Anthony Schmitt
 *
 */
public class CollectionGameListScreen extends ListScreen implements ConsoleSelectable, CompanySelectable, DateSelectable, GenreSelectable//, CollectionGameViewable
{
	
	private ConsoleSelectOptions c;
	private int[] consoleList;
	private String selectedCollection;
	private DateSelectOptions d;
	private String[] dateList, collectionList;
	private int[] publisherList, developerList, genreList;
	private CompanySelectOptions p, dev;
	private CollectionGameView gv;
	private GenreSelectOptions gso;
	private JComboBox<String> collectionBox;
	private static String[] columns = {"Title","Console","Release Date","Format","Publisher"};
	
	/**
	 * Constructor
	 * @param query QueryRunner
	 */
	public CollectionGameListScreen(QueryRunner query)
	{
		super(query,"",columns);
		this.setTitle("View Games in Collections");
		c = new ConsoleSelectOptions(query, this, true);
		d = new DateSelectOptions(this);
		p = new CompanySelectOptions(query, this, true, Queries.PUBLISHERS);
		p.setTitle("Filter by Publisher");
		dev = new CompanySelectOptions(query, this, true, Queries.DEVELOPERS);
		dev.setTitle("Filter by Developer");
		gso = new GenreSelectOptions(query, this);
		gv = new CollectionGameView(query, this);
		c.setVisible(false);
		d.setVisible(false);
		p.setVisible(false);
		gv.setVisible(false);
		dev.setVisible(false);
		gso.setVisible(false);
	}
	
	/**
	 * Initializes the object
	 */
	protected void init()
	{
		setUpTable();
		setUpMenu();
		setUpCollectionSelect();
		this.setLayout(new BorderLayout());
		this.add(mainView, BorderLayout.CENTER);
		this.add(collectionBox, BorderLayout.NORTH);
		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets up the collections to selecting
	 */
	private void setUpCollectionSelect()
	{
		collectionList = query.querySingleListReturnColumn(Queries.COLLECTIONS);
		selectedCollection = "'"+collectionList[0]+"'"; 
		collectionBox = new JComboBox<String>();
		for(int a = 0; a < collectionList.length; a++)
		{
			collectionBox.addItem(collectionList[a]);
		}
		collectionBox.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				selectedCollection = "'"+collectionList[collectionBox.getSelectedIndex()]+"'";
				buildQuery();
				updateList();
			}
		});
	}
	
	/**
	 * Sets up the menu
	 */
	protected void setUpMenu()
	{
		menu = new JMenuBar();
		JMenu menu1 = new JMenu("Filter");
		JMenuItem itm1 = new JMenuItem("By Console");
		itm1.addActionListener(this);
		JMenuItem itm2 = new JMenuItem("By Date");
		itm2.addActionListener(this);
		JMenuItem itm4 = new JMenuItem("By Genre");
		itm4.addActionListener(this);
		JMenuItem itm5 = new JMenuItem("By Developer");
		itm5.addActionListener(this);
		JMenuItem itm6 = new JMenuItem("By Publisher");
		itm6.addActionListener(this);
		JMenuItem itm7 = new JMenuItem("Clear All Filters");
		itm7.addActionListener(this);
		menu1.add(itm1);
		menu1.add(itm2);
		menu1.add(itm4);
		menu1.add(itm5);
		menu1.add(itm6);
		menu1.add(itm7);
		menu.add(menu1);
		this.setJMenuBar(menu);
	}

	/**
	 * Handles the menu selections
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("By Console"))
		{
			c.setVisible(true);
		}
		if(e.getActionCommand().equals("By Date"))
		{
			d.setVisible(true);
		}
		if(e.getActionCommand().equals("By Genre"))
		{
			gso.setVisible(true);
		}
		if(e.getActionCommand().equals("By Developer"))
		{
			dev.setVisible(true);
		}
		if(e.getActionCommand().equals("By Publisher"))
		{
			p.setVisible(true);
		}
		if(e.getActionCommand().equals("Clear All Filters"))
		{
			c.clearSelected();
			d.clearSelected();
			p.clearSelected();
			dev.clearSelected();
			gso.clearSelected();
			haveGrabSelectedConsoleList();
			haveGrabSelectedDateList();
			haveGrabSelectedCompanyList();
			haveGrabSelectedGenreList();
		}
	}
	
	/**
	 * Used to have this screen pull the list of consoles to filter from the ConsoleSelectOptions object.
	 * Then builds new query and updates list.
	 */
	public void haveGrabSelectedConsoleList()
	{
		consoleList = c.getSelected();
		buildQuery();
		updateList();
	}
	
	/**
	 * Used to have this screen pull the list of dates to filter from the DateSelectOptions object.
	 * Then builds new query and updates list.
	 */
	public void haveGrabSelectedDateList()
	{
		dateList = d.getSelected();
		buildQuery();
		updateList();
	}
	
	/**
	 * Used to have this screen pull the list of companies to filter from the CompanySelectOptions objects (for publishers and developers).
	 * Then builds new query and updates list.
	 */
	public void haveGrabSelectedCompanyList()
	{
		publisherList = p.getSelected();
		buildQuery();
		updateList();
		developerList = dev.getSelected();
		buildQuery();
		updateList();
	}
	
	/**
	 * Used to have this screen pull the list of genres to filter from the GenreeSelectOptions object.
	 * Then builds new query and updates list.
	 */
	public void haveGrabSelectedGenreList()
	{
		genreList = gso.getSelected();
		buildQuery();
		updateList();
		
	}
	
	/**
	 * Builds the SQL query based on the specified filters.
	 */
	protected void buildQuery()
	{		
		currentQuery = "Select distinct title, Console.name, Game.release_date, format, Company.name from Game, Console, Company, Developed_Game, Has_Genre, Game_In_Collection where Game.console_id = Console.id AND Company.id = Game.publisher_id AND Developed_Game.game_id = Game.id AND Has_Genre.game_id = Game.id AND Game.id = Game_In_Collection.game_id AND Game_In_Collection.collection_name = " + selectedCollection + " "; 
		String end = " order by Console.name, Game.title;";
		String addOn = "";
		if(consoleList != null && consoleList.length > 0)
		{
			addOn += "AND (";
			for(int a = 0; a < consoleList.length; a++)
			{
				addOn += "Game.console_id = " + consoleList[a] + " OR ";
			}
			addOn = addOn.substring(0, addOn.length()-4);
			addOn += ") ";
		}
		if(dateList != null && (dateList[0] != null || dateList[1] != null))
		{
			addOn += "AND (";
			if(dateList[0] != null && dateList[1] != null)
			{
				String tmp = "'"+dateList[0]+"'";
				addOn += "Game.release_date > " + tmp + " AND ";
				String tmp1 = "'"+dateList[1]+"'";
				addOn += "Game.release_date < " + tmp1 + ") ";
			}
			else if(dateList[0] != null)
			{
				String tmp = "'"+dateList[0]+"'";
				addOn += "Game.release_date < " + tmp + ") ";
			}
			else if(dateList[1] != null)
			{
				String tmp = "'"+dateList[1]+"'";
				addOn += "Game.release_date > " + tmp + ") ";
			}
		}
		if(publisherList != null && publisherList.length > 0)
		{
			addOn += "AND (";
			for(int a = 0; a < publisherList.length; a++)
			{
				addOn += "Game.publisher_id = " + publisherList[a] + " OR ";
			}
			addOn = addOn.substring(0, addOn.length()-4);
			addOn += ") ";
		}
		if(developerList != null && developerList.length > 0)
		{
			addOn += "AND (";
			for(int a = 0; a < developerList.length; a++)
			{
				addOn += "Developed_Game.company_id = " + developerList[a] + " OR ";
			}
			addOn = addOn.substring(0, addOn.length()-4);
			addOn += ") ";
		}
		if(genreList != null && genreList.length > 0)
		{
			addOn += "AND (";
			for(int a = 0; a < genreList.length; a++)
			{
				addOn += "Has_Genre.genre_id = " + genreList[a] + " OR ";
			}
			addOn = addOn.substring(0, addOn.length()-4);
			addOn += ") ";
		}
		currentQuery += addOn + end;
	}
	
	/**
	 * Grabs the game ID for a selected item in the list and brings up the GameView screen with that game's info.
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if(model.getValueAt(theTable.getSelectedRow(), 0) != null)
		{
			String gameName = model.getValueAt(theTable.getSelectedRow(), 0).toString();
			String consoleName = model.getValueAt(theTable.getSelectedRow(), 1).toString();
			int consoleID = query.storeProcedure("getConsoleID", consoleName);
			int id = query.storeProcedure("getGameID", gameName, consoleID);
			gv.setGame(id,selectedCollection);
			gv.setVisible(true);
		}
	}
	
	/**
	 * Makes this screen visible. Makes sure that any changes made in other screens are reflected here.
	 */
	public void makeVisible()
	{
		buildQuery();
		updateList();
		this.add(collectionBox, BorderLayout.NORTH);
		this.setVisible(true);
	}
}