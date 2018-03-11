package frontend;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import backend.*;

/**
 * Shows all games in the database
 * @author Anthony Schmitt
 *
 */
public class GameListScreen extends ListScreen implements ConsoleSelectable, CompanySelectable, DateSelectable, GenreSelectable
{
	
	private ConsoleSelectOptions c;
	private int[] consoleList;
	private String searchName;
	private DateSelectOptions d;
	private String[] dateList;
	private int[] publisherList, developerList, genreList;
	private CompanySelectOptions p, dev;
	protected GameView gv;
	private GenreSelectOptions gso;
	private static String[] columns = {"Title","Console","Release Date","Format","Publisher"};

	/**
	 * Constructor.
	 * @param qu Query Runner
	 */
	public GameListScreen(QueryRunner qu)
	{
		super(qu,Queries.ALL_GAMES,columns);
		this.setTitle("Game List");
		c = new ConsoleSelectOptions(query, this, true);
		d = new DateSelectOptions(this);
		p = new CompanySelectOptions(query, this, true, Queries.PUBLISHERS);
		p.setTitle("Filter by Publisher");
		dev = new CompanySelectOptions(query, this, true, Queries.DEVELOPERS);
		dev.setTitle("Filter by Developer");
		gso = new GenreSelectOptions(query, this);
		gv = new GameView(query);
		c.setVisible(false);
		d.setVisible(false);
		p.setVisible(false);
		gv.setVisible(false);
		dev.setVisible(false);
		gso.setVisible(false);
	}
	
	/**
	 * Sets up the menus
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
		JMenuItem itm8 = new JMenuItem("By Name");
		itm8.addActionListener(this);
		menu1.add(itm1);
		menu1.add(itm2);
		menu1.add(itm4);
		menu1.add(itm5);
		menu1.add(itm6);
		menu1.add(itm8);
		menu1.add(itm7);
		menu.add(menu1);
		this.setJMenuBar(menu);
	}
	
	/**
	 * Handles the menu selections.
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
			searchName = "";
			haveGrabSelectedConsoleList();
			haveGrabSelectedDateList();
			haveGrabSelectedCompanyList();
			haveGrabSelectedGenreList();
		}
		if(e.getActionCommand().equals("By Name"))
		{
			searchName = JOptionPane.showInputDialog(null, "Name to search:");
			if(searchName != null && !searchName.equals(""))
			{
				buildQuery();
				updateList();
			}
		}
	}
	
	/**
	 * For having the ConsoleSelectOptions pass its list.
	 */
	public void haveGrabSelectedConsoleList()
	{
		consoleList = c.getSelected();
		buildQuery();
		updateList();
	}
	
	/**
	 * For having the DateSelectOptions pass its list.
	 */
	public void haveGrabSelectedDateList()
	{
		dateList = d.getSelected();
		buildQuery();
		updateList();
	}
	
	/**
	 * For having the CompanySelectOptions pass its list.
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
	 * For having the GenreSelectOptions pass its list.
	 */
	public void haveGrabSelectedGenreList()
	{
		genreList = gso.getSelected();
		buildQuery();
		updateList();
		
	}
	
	/**
	 * Builds the query based on the selected filters.
	 */
	protected void buildQuery()
	{
		currentQuery = "Select distinct title, Console.name, Game.release_date, format, Company.name from Game, Console, Company, Developed_Game, Has_Genre where Game.console_id = Console.id AND Company.id = Game.publisher_id AND Developed_Game.game_id = Game.id AND Has_Genre.game_id = Game.id "; 
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
		if(searchName != null && !searchName.equals(""))
		{
			addOn = addOn + " AND Game.title like '%" + searchName + "%' ";
		}
		currentQuery += addOn + end;
	}
	
	/**
	 * Sets game for the GameView and makes it visible for newly selected items in the list.
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if(model.getValueAt(theTable.getSelectedRow(), 0) != null)
		{
			String gameName = model.getValueAt(theTable.getSelectedRow(), 0).toString();
			String consoleName = model.getValueAt(theTable.getSelectedRow(), 1).toString();
			int consoleID = query.storeProcedure("getConsoleID", consoleName);
			int id = query.storeProcedure("getGameID", gameName, consoleID);
			gv.setGame(id);
			gv.setVisible(true);
		}
	}
	
}