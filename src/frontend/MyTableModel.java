package frontend;

import javax.swing.table.*;

/**
 * This was necessary for updating information in the tables
 * @author Anthony Schmitt
 *
 */
class MyTableModel extends AbstractTableModel 
{
	
    private String[] columnNames;
    private Object[][] data;

    /**
     * Constructor
     * @param colTitle Column Titles
     * @param datas Data for the table
     */
    public MyTableModel(String[] colTitle, Object[][] datas)
    {
    	this.columnNames = colTitle;
    	this.data = datas;
    }
    
    /**
     * Returns the number of columns
     */
    public int getColumnCount() 
    {
    	int output = 0;
    	if(columnNames != null)
    	{
    		output = columnNames.length;
    	}
    	return output;
    }

    /**
     * Returns the number of rows
     */
    public int getRowCount() 
    {
    	int output = 0;
    	if(data != null)
    	{
    		output = data.length;
    	}
    	return output;
    }

    /**
     * Gives column name based on row
     */
    public String getColumnName(int col) 
    {
    	String output = "";
    	if(columnNames != null && col <= columnNames.length)
    	{
    		output = columnNames[col];
    	}
    	return output;
    }

    /**
     * Returns the object in specified place in table.
     */
    public Object getValueAt(int row, int col) 
    {
    	Object output = null;
    	if(row >= 0)
    	{
    		output = data[row][col];	
    	}
    	return output;
    }

    /**
     * Return the class of the items in a column.
     */
    public Class getColumnClass(int c) 
    {
        return getValueAt(0, c).getClass();
    }
   
    /**
     * Cells are all set as not editable.
     */
    public boolean isCellEditable(int row, int col) 
    {
            return false;
    }
    
    /**
     * Sets the data and column titles
     * @param colTitle Column titles
     * @param datas Data for table
     */
    public void setData(String[] colTitle, Object[][] datas)
    {
    	this.columnNames = colTitle;
    	this.data = datas;
    }
}
