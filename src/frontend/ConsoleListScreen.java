package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * This is a list of all consoles in the database
 * User can add them to collection
 * @author Anthony Schmitt
 *
 */
public class ConsoleListScreen extends ListScreen
{

	private boolean byName, byRelease, byEnd, bySold, byPrice, byCreator;
	private JButton add;
	private CollectionConsoleAdder cca;
	private static String[] columns = {"Name","Release Date","Production End","Units Sold","Original Price","Creator"};
	
	/**
	 * Constructor.
	 * @param query Query Runner
	 */
	public ConsoleListScreen(QueryRunner query)
	{
		super(query,Queries.ALL_CONSOLES,columns);
		this.setTitle("View Consoles");
	}
	
	/**
	 * Initializes the object
	 */
	protected void init()
	{
		int temp = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		if(temp < height)
		{
			height = temp;
		}
		temp = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		if(temp < width)
		{
			width = temp;
		}
		setUpTable();
		setUpMenu();
		add = new JButton("Add to Collection");
		add.addActionListener(this);
		this.setLayout(new BorderLayout());
		this.add(add, BorderLayout.SOUTH);
		this.add(mainView, BorderLayout.CENTER);
		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		cca = new CollectionConsoleAdder(query, 1, "");
		cca.setVisible(false);
	}
	
	/**
	 * Sets up the menus
	 */
	protected void setUpMenu()
	{
		menu = new JMenuBar();
		JMenu menu1 = new JMenu("Sort");
		JMenuItem itm1 = new JMenuItem("By Console Name");
		itm1.addActionListener(this);
		JMenuItem itm2 = new JMenuItem("By Release Date");
		itm2.addActionListener(this);
		JMenuItem itm4 = new JMenuItem("By Production End Date");
		itm4.addActionListener(this);
		JMenuItem itm5 = new JMenuItem("By Units Sold");
		itm5.addActionListener(this);
		JMenuItem itm6 = new JMenuItem("By Original Price");
		itm6.addActionListener(this);
		JMenuItem itm7 = new JMenuItem("By Creator");
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
		if(e.getActionCommand().equals("By Console Name"))
		{
			byName = true;
			byRelease = false;
			byEnd = false;
			bySold = false;
			byPrice = false;
			byCreator = false;
			buildQuery();
			updateList();
		}
		if(e.getActionCommand().equals("By Release Date"))
		{
			byName = false;
			byRelease = true;
			byEnd = false;
			bySold = false;
			byPrice = false;
			byCreator = false;
			buildQuery();
			updateList();
		}
		if(e.getActionCommand().equals("By Production End Date"))
		{
			byName = false;
			byRelease = false;
			byEnd = true;
			bySold = false;
			byPrice = false;
			byCreator = false;
			buildQuery();
			updateList();
		}
		if(e.getActionCommand().equals("By Units Sold"))
		{
			byName = false;
			byRelease = false;
			byEnd = false;
			bySold = true;
			byPrice = false;
			byCreator = false;
			buildQuery();
			updateList();
		}
		if(e.getActionCommand().equals("By Original Price"))
		{
			byName = false;
			byRelease = false;
			byEnd = false;
			bySold = false;
			byPrice = true;
			byCreator = false;
			buildQuery();
			updateList();
		}
		if(e.getActionCommand().equals("By Creator"))
		{
			byName = false;
			byRelease = false;
			byEnd = false;
			bySold = false;
			byPrice = false;
			byCreator = true;
			buildQuery();
			updateList();
		}
		if(e.getActionCommand().equals("Add to Collection"))
		{
			if(theTable.getSelectedColumn() != -1)
			{
				String consoleName = model.getValueAt(theTable.getSelectedRow(), 0).toString();
				int consoleID = query.storeProcedure("getConsoleID", consoleName);
				cca.setConsole(consoleID, consoleName);
				cca.setVisible(true);
			}
			else
			{
				JOptionPane.showMessageDialog(null,"You must select a console to add.","Nothing Selected",JOptionPane.WARNING_MESSAGE);
			}
		}
	}
	
	/**
	 * Builds query based on selected filters.
	 */
	protected void buildQuery()
	{
		currentQuery = "Select Console.name, release_date, production_end_date, units_sold, original_price, Company.name from Console, Company where Console.company_id = Company.id order by "; 
		if(byName)
		{
			currentQuery += " Console.name;";
		}
		if(byRelease)
		{
			currentQuery += " release_date;";
		}
		if(byEnd)
		{
			currentQuery += " production_end_date;";
		}
		if(bySold)
		{
			currentQuery += " units_sold;";
		}
		if(byPrice)
		{
			currentQuery += " original_price;";
		}
		if(byCreator)
		{
			currentQuery += " Company.name;";
		}
	}

}
