package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * Displays all of the controllers in the database
 * Can add to a collection
 * @author Anthony Schmitt
 *
 */
public class ControllerListScreen extends ListScreen implements CompanySelectable, ConsoleSelectable
{

	private JButton add;
	private CollectionControllerAdder cca;
	private CompanySelectOptions p;
	private ConsoleSelectOptions c;
	private int[] companyList, consoleList;
	private static String[] columns = {"Name","Console"};
	
	/**
	 * Constructor.
	 * @param query Query Runner
	 */
	public ControllerListScreen(QueryRunner query)
	{
		super(query,Queries.All_CONTROLELRS,columns);
		this.setTitle("View Controllers");
		p = new CompanySelectOptions(query, this, true, Queries.CONSOLE_MAKERS);
		p.setTitle("Filter by Console Maker");
		p.setVisible(false);
		c = new ConsoleSelectOptions(query, this, true);
		c.setTitle("Filter by Console");
		c.setVisible(false);
	}
	
	/**
	 * Initializes the object.
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
		add = new JButton("Add to Collection");
		add.addActionListener(this);
		this.setLayout(new BorderLayout());
		this.add(add, BorderLayout.SOUTH);
		//setUpMenu();
		this.add(mainView, BorderLayout.CENTER);
		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		cca = new CollectionControllerAdder(query,0,"");
		cca.setVisible(false);
	}
	
	/**
	 * Sets up the menus.
	 */
	protected void setUpMenu()
	{
		menu = new JMenuBar();
		JMenu menu1 = new JMenu("Filter");
		JMenuItem itm1 = new JMenuItem("By Console");
		itm1.addActionListener(this);
		JMenuItem itm6 = new JMenuItem("By Console Maker");
		itm6.addActionListener(this);
		JMenuItem itm7 = new JMenuItem("Clear Filters");
		itm7.addActionListener(this);
		menu1.add(itm1);
		menu1.add(itm6);
		menu1.add(itm7);
		menu.add(menu1);
		this.setJMenuBar(menu);
	}
	
	/**
	 * For having the CompnaySelectOptions pass it the selected list.
	 */
	public void haveGrabSelectedCompanyList()
	{
		companyList = p.getSelected();
		buildQuery();
		updateList();
	}

	/**
	 * Handles menu selections.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Add to Collection"))
		{
			if(theTable.getSelectedColumn() != -1)
			{
				String controllerName = model.getValueAt(theTable.getSelectedRow(), 0).toString();
				int controllerID = query.storeProcedure("getControllerID", controllerName);
				cca.setController(controllerID, controllerName);
				cca.setVisible(true);
			}
			else
			{
				JOptionPane.showMessageDialog(null,"You must select a console to add.","Nothing Selected",JOptionPane.WARNING_MESSAGE);
			}
		}
		if(e.getActionCommand().equals("By Console Maker"))
		{
			p.setVisible(true);
		}
		if(e.getActionCommand().equals("Clear Filters"))
		{
			c.clearSelected();
			p.clearSelected();
			haveGrabSelectedCompanyList();
			haveGrabSelectedConsoleList();
		}
		if(e.getActionCommand().equals("By Console"))
		{
			c.setVisible(true);
		}
	}
	
	/**
	 * Builds the query based on selected filters.
	 */
	protected void buildQuery()
	{		
		currentQuery = "Select Controller.name, Console.name from Console, Controller where Console.id = Controller.console_id "; 
		String end = " order by Controller.name;";
		String addOn = "";

		if(companyList != null && companyList.length > 0)
		{
			addOn += "AND (";
			for(int a = 0; a < companyList.length; a++)
			{
				addOn += "Console.company_id = " + companyList[a] + " OR ";
			}
			addOn = addOn.substring(0, addOn.length()-4);
			addOn += ") ";
		}
		if(consoleList != null && consoleList.length > 0)
		{
			addOn += "AND (";
			for(int a = 0; a < consoleList.length; a++)
			{
				addOn += "Console.id = " + consoleList[a] + " OR ";
			}
			addOn = addOn.substring(0, addOn.length()-4);
			addOn += ") ";
		}
		currentQuery += addOn + end;
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
}
