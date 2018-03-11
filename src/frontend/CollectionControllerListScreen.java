package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import backend.*;

/**
 * Shows a list of all controllers in a collection
 * Can select to edit information
 * @author Anthony Schmitt
 *
 */
public class CollectionControllerListScreen extends ListScreen implements CompanySelectable, ConsoleSelectable
{
	private String selectedCollection;
	private String[] collectionList;
	private int[] companyList, consoleList;
	private CompanySelectOptions p;
	private ConsoleSelectOptions c;
	private JComboBox<String> collectionBox;
	private CollectionControllerView cv;
	private static String[] columns = {"Controller Name","Quantity","Console"};
	
	/**
	 * Constructor
	 * @param query Connection to the database
	 */
	public CollectionControllerListScreen(QueryRunner query)
	{
		super(query,"",columns);
		this.setTitle("View Controllers in Collections");
		cv = new CollectionControllerView(query, this);
		cv.setVisible(false);
		p = new CompanySelectOptions(query, this, true, Queries.CONSOLE_MAKERS);
		p.setTitle("Filter by Console Maker");
		p.setVisible(false);
		c = new ConsoleSelectOptions(query, this, true);
		c.setTitle("Filter by Console");
		c.setVisible(false);
	}
	
	/**
	 * Sets up the window
	 */
	protected void init()
	{
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		setUpTable();
		setUpCollectionSelect();
		setUpMenu();
		this.setLayout(new BorderLayout());
		this.add(mainView, BorderLayout.CENTER);
		this.add(collectionBox, BorderLayout.NORTH);
		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
		
	/**
	 * Sets up the combo box for the list of collections.
	 */
	private void setUpCollectionSelect()
	{
		collectionList = query.querySingleListReturnColumn(Queries.COLLECTIONS);
		selectedCollection = "'"+collectionList[0]+"'"; 
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>();
		collectionBox = new JComboBox<String>(comboModel);
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
	 * Handles the menu selections
	 */
	public void actionPerformed(ActionEvent e)
	{
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
	 * Used so the company selector can let the object know that its finished and to grab the selection and update the list accordingly
	 */
	public void haveGrabSelectedCompanyList()
	{
		companyList = p.getSelected();
		buildQuery();
		updateList();

	}
	
	/**
	 * Updates the list
	 */
	public void updateList()
	{
		String[][] data = query.query(currentQuery);
		String[] columnNames = {"Controller Name","Quantity","Console"};
		model.setData(columnNames, data);
		model.fireTableDataChanged();		
	}
	
	/**
	 * Builds the query based on the selected filters.
	 */
	protected void buildQuery()
	{		
		currentQuery = "Select Controller.name, quantity, Console.name from Console, Controller_In_Collection, Controller where Console.id = Controller.console_id AND Controller_In_Collection.controller_id = Controller.id AND Controller_In_Collection.collection_name = " + selectedCollection; 
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
	 * Handles selection of different items being selected
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if(model.getValueAt(theTable.getSelectedRow(), 0) != null) //otherwise freezes when table is updated and what it was selected is all of a sudden null
		{
			String controllerName = model.getValueAt(theTable.getSelectedRow(), 0).toString();
			int consoleID = query.storeProcedure("getControllerID", controllerName);
			cv.setConsole(consoleID,selectedCollection);
			cv.setVisible(true);
		}
	}
	
	/**
	 * Used so the console selector can let the object know that its finished and to grab the selection and update the list accordingly
	 */
	public void haveGrabSelectedConsoleList()
	{
		consoleList = c.getSelected();
		buildQuery();
		updateList();
	}
	
	/**
	 * Updates the information in the list and makes the window visible.
	 */
	public void makeVisible()
	{
		buildQuery();
		updateList();
		this.add(collectionBox, BorderLayout.NORTH);
		this.setVisible(true);
	}
}