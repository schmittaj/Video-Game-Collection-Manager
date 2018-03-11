package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import backend.*;

/**
 * This displays all consoles and information about them in the collection
 * can switch between collections
 * can see more detailed information by selecting
 * @author Anthony Schmitt
 *
 */
public class CollectionConsoleListScreen extends ListScreen implements CompanySelectable 
{
	private String selectedCollection;
	private String[] collectionList;
	private int[] companyList;
	private CompanySelectOptions p;
	private JComboBox<String> collectionBox;
	private CollectionConsoleView cv;
	private static String[] columns = {"Name","Release Date","Production End Date","Units Sold","Original Price","Creator"};
	
	/**
	 * Constructor
	 * @param query Connection to database
	 */
	public CollectionConsoleListScreen(QueryRunner query)
	{
		super(query,"",columns);
		this.setTitle("View Consoles in Collections");
		p = new CompanySelectOptions(query, this, true, Queries.CONSOLE_MAKERS);
		p.setTitle("Filter by Creator");
		p.setVisible(false);
		cv = new CollectionConsoleView(query, this);
		cv.setVisible(false);
	}
	
	/**
	 * Sets up the window
	 */
	protected void init()
	{
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
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
	 * Sets up the list of collections in a combo box.
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
	 * Sets up the menu
	 */
	protected void setUpMenu()
	{
		menu = new JMenuBar();
		JMenu menu1 = new JMenu("Filter");
		JMenuItem itm6 = new JMenuItem("By Creator");
		itm6.addActionListener(this);
		JMenuItem itm7 = new JMenuItem("Clear Filters");
		itm7.addActionListener(this);
		menu1.add(itm6);
		menu1.add(itm7);
		menu.add(menu1);
		this.setJMenuBar(menu);
	}

	/**
	 * Listens for menu selections and handles them
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("By Creator"))
		{
			p.setVisible(true);
		}
		if(e.getActionCommand().equals("Clear Filters"))
		{
			p.clearSelected();
			haveGrabSelectedCompanyList();
		}
	}
	
	/**
	 * For having it grab the selected list of companies
	 */
	public void haveGrabSelectedCompanyList()
	{
		companyList = p.getSelected();
		buildQuery();
		updateList();

	}
	
	/**
	 * Rebuilds the query based on the selected filters
	 */
	protected void buildQuery()
	{		
		currentQuery = "select Console.name, have_cords, have_booklet, have_box, is_sealed, notes from Console, Console_In_Collection where Console.id = Console_In_Collection.console_id AND Console_In_Collection.collection_name = " + selectedCollection; 
		String end = " order by Console.name;";
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
		currentQuery += addOn + end;
	}
	
	/**
	 * Handles a different console in the collection being selected
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if(model.getValueAt(theTable.getSelectedRow(), 0) != null) //otherwise freezes when table is updated and what it was selected is all of a sudden null
		{
			String consoleName = model.getValueAt(theTable.getSelectedRow(), 0).toString();
			int consoleID = query.storeProcedure("getConsoleID", consoleName);
			cv.setConsole(consoleID,selectedCollection);
			cv.setVisible(true);
		}
	}
	
	/**
	 * Makes the window visible and updates all of the information.
	 */
	public void makeVisible()
	{
		buildQuery();
		updateList();
		this.add(collectionBox, BorderLayout.NORTH);
		this.setVisible(true);
	}
}