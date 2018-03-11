package frontend;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import backend.*;

/**
 * Generic class to be a parent for all of the list screens.
 * @author Anthony Schmitt
 *
 */
public class ListScreen extends JInternalFrame implements ActionListener, ListSelectionListener
{
	
	protected int height, width;
	protected JTable theTable;
	protected MyTableModel model;
	protected QueryRunner query;
	protected JScrollPane mainView;
	protected JMenuBar menu;
	protected String currentQuery;
	protected String[] columnNames;
	
	/**
	 * Constructor.
	 * @param query Query Runner
	 * @param defaultQuery Starting query that will fill the list
	 * @param colName Names for the table columns
	 * @param h Window height
	 * @param w Window width
	 */
	public ListScreen(QueryRunner query, String defaultQuery, String[] colName, int h, int w)
	{
		super("",true,true,true,true);
		this.query = query;
		this.height = h;
		this.width = w;
		this.currentQuery = defaultQuery;
		this.columnNames = colName;
		init();
	}
	
	/**
	 * Constructor.
	 * @param query Query Runner
	 * @param defaultQuery Starting query that will fill the list
	 * @param colName Names for the table columns
	 */
	public ListScreen(QueryRunner query, String defaultQuery, String[] colName)
	{
		super("",true,true,true,true);
		this.query = query;
		this.height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		this.width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		this.currentQuery = defaultQuery;
		this.columnNames = colName;
		init();
	}
	
	/**
	 * Initializes the object.
	 */
	protected void init()
	{
		if(height != 0 & width != 0)
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
		}
		else
		{
			try 
			{
				this.setMaximum(true);
			} 
			catch (PropertyVetoException e) 
			{
				e.printStackTrace();
			}
		}
		setUpTable();
		setUpMenu();
		this.add(mainView);
		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets up the menu
	 */
	protected void setUpMenu()
	{
		menu = new JMenuBar();
		this.setJMenuBar(menu);
	}

	/**
	 * Sets up the table
	 */
	protected void setUpTable() 
	{
		String[][] data = query.query(currentQuery);
		model = new MyTableModel(columnNames,data);
		theTable = new JTable();
		theTable.setModel(model);
		mainView = new JScrollPane(theTable);
		theTable.setFillsViewportHeight(true);
		theTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		theTable.getSelectionModel().addListSelectionListener(this);
	}

	/**
	 * For handling menu items
	 */
	public void actionPerformed(ActionEvent e)
	{
		//Handle menu items here
	}
	
	/**
	 * Updates the list
	 */
	public void updateList()
	{
		String[][] data = query.query(currentQuery);
		model.setData(columnNames, data);
		model.fireTableDataChanged();		
	}
	
	/**
	 * Spot for building the query
	 */
	protected void buildQuery()
	{
		//build the query here
	}
	
	/**
	 * Spot for dealing with a change in selected table item
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if(model.getValueAt(theTable.getSelectedRow(), 0) != null)
		{
			//change for whateve values wanted
			String value = model.getValueAt(theTable.getSelectedRow(), 0).toString();
		}
	}
	
	/**
	 * Makes the screen visible.
	 */
	public void makeVisible()
	{
		updateList();
		this.setVisible(true);
	}
}
