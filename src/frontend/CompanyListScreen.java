package frontend;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import backend.*;

/**
 * Shows all the companies in the database
 * @author Anthony Schmitt
 *
 */
public class CompanyListScreen extends ListScreen
{
	private boolean byName, byFormed, byClosed;
	private static String[] columns = {"Name","Year Formed","Year Closed","Info"};
	
	/**
	 * Constructor
	 * @param query QueryRunner
	 */
	public CompanyListScreen(QueryRunner query)
	{
		super(query,Queries.ALL_COMPANIES,columns);
		this.setTitle("View Companies");
	}
	
	/**
	 * Sets up the menus.
	 */
	protected void setUpMenu()
	{
		menu = new JMenuBar();
		JMenu menu1 = new JMenu("Sort");
		JMenuItem itm1 = new JMenuItem("By Company Name");
		itm1.addActionListener(this);
		JMenuItem itm2 = new JMenuItem("By Formed Date");
		itm2.addActionListener(this);
		JMenuItem itm4 = new JMenuItem("By Closed Date");
		itm4.addActionListener(this);
		menu1.add(itm1);
		menu1.add(itm2);
		menu1.add(itm4);
		menu.add(menu1);
		this.setJMenuBar(menu);
	}

	/**
	 * Handles menu selections.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("By Company Name"))
		{
			byName = true;
			byFormed = false;
			byClosed = false;
			buildQuery();
			updateList();
		}
		if(e.getActionCommand().equals("By Formed Date"))
		{
			byName = false;
			byFormed = true;
			byClosed = false;
			buildQuery();
			updateList();
		}
		if(e.getActionCommand().equals("By Closed Date"))
		{
			byName = false;
			byFormed = false;
			byClosed = true;
			buildQuery();
			updateList();
		}
	}

	/**
	 * Builds the query based on the selected filters.
	 */
	protected void buildQuery()
	{
		currentQuery = "Select name, year_formed, year_closed, notes from Company order by"; 
		if(byName)
		{
			currentQuery = Queries.ALL_COMPANIES;
		}
		if(byFormed)
		{
			currentQuery += " year_formed;";
		}
		if(byClosed)
		{
			currentQuery += " year_closed;";
		}
	}

}
