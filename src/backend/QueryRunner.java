package backend;

import java.io.*;
import java.sql.*;

/**
 * The connection to MySql.
 * Can pass through queries, or you can call stored procedures.
 * @author Anthony Schmitt
 *
 */
public class QueryRunner 
{	
    private String databasePrefix, netID, hostName, password, fileName, databaseURL;
	private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
        
    /**
     * Constructor, auto connects to the database.
     * @param infoFile A text file where the first line is the databasePrefix, the 2nd line is the netID, the 3rd line is the hostName, and the 4th line is the password
     */
    public QueryRunner(String infoFile)
    {
    	this.fileName = infoFile;
    	grabInfo();
    	Connection();
    }
    
    /**
     * Pulls the database connection information out of the given file.
     */
    private void grabInfo()
    {
    	FileReader fr;
		try 
		{
			fr = new FileReader(new File(fileName));
			BufferedReader read = new BufferedReader(fr);
	    	this.databasePrefix = read.readLine();
	    	this.netID = read.readLine();
	    	this.hostName = read.readLine();
	    	this.password = read.readLine();
	    	read.close();
	    	this.databaseURL ="jdbc:mysql://"+hostName+"/"+databasePrefix+"?noAccessToProcedureBodies=true";
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
    
    /**
     * Connects to the database.
     */
	public void Connection()
	{  
		try 
		{
	    	Class.forName("com.mysql.jdbc.Driver");
	    	connection = DriverManager.getConnection(databaseURL, netID, password);
		}
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.out.println("Connection Failed!");
		}
	}

	/**
	 * Runs a query and passes back the results in a 2-D String array.
	 * @param sql The query to run
	 * @return The resulting table in a 2-D String array.
	 */
	public String[][] query(String sql)
	{
		String[][] output = null;
		if(!sql.equals(""))
		{
			try 
			{
	    		statement = connection.createStatement();
	    		resultSet = statement.executeQuery(sql);
	
	    		ResultSetMetaData metaData = resultSet.getMetaData();
	    		resultSet.last();
	    		int rows = resultSet.getRow();
	    		resultSet.beforeFirst();
	    		int columns = metaData.getColumnCount();
	    		output = new String[rows][columns];
	    		for(int a = 0; a < rows; a++) 
	    		{
	    			resultSet.next();
	    			for (int b = 0; b < columns; b++) 
	    			{
						Object check = resultSet.getObject(b+1);
						if(check != null)
						{
							output[a][b] = check.toString();
						}
						else
						{
							output[a][b] = "";
						}    				
	    			}
	    		}
	    	}
	    	catch (SQLException e) 
	    	{
	    		e.printStackTrace();
	    	}
		}
    	return output;
    }
	
	/**
	 * Runs a query and returns a String array of the results for the first column.
	 * @param sql Query to run.
	 * @return A String array of the first column.
	 */
	public String[] querySingleListReturnColumn(String sql)
	{
		String[] output = null;
		
		String[][] tmp = query(sql);
		output = new String[tmp.length];
		for(int a = 0; a < tmp.length; a++)
		{
			output[a] = tmp[a][0];
		}
		return output;
	}
	
	/**
	 * Runs a query and returns a String array of the first row of the results.
	 * @param sql Query to run.
	 * @return A String array of the first row.
	 */
	public String[] querySingleListReturnRow(String sql)
	{
		String[] output = null;
		
		String[][] tmp = query(sql);
		output = new String[tmp[0].length];
		for(int a = 0; a < tmp[0].length; a++)
		{
			output[a] = tmp[0][a];
		}
		return output;
	}
	
	/**
	 * Runs a stored procedure that takes in a single String input and returns an int.
	 * @param spName The name of the stored procedure
	 * @param inputs The string input
	 * @return The int result from the stored procedure
	 */
	public int storeProcedure(String spName, String inputs) 
	{
		int output = 0;
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?)}");
			myCallStmt.setString(1, inputs);
	        myCallStmt.registerOutParameter(2,Types.BIGINT);
	        myCallStmt.execute();
	        output = myCallStmt.getInt(2);
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * Runs a stored procedure that takes in a String input and an int input and returns an int.
	 * @param spName he name of the stored procedure
	 * @param input1 The string input
	 * @param input2 The int input
	 * @return The int result from the stored procedure
	 */
	public int storeProcedure(String spName, String input1, int input2) 
	{
		int output = 0;
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?,?)}");
			myCallStmt.setString(1, input1);
			myCallStmt.setInt(2, input2);
	        myCallStmt.registerOutParameter(3,Types.BIGINT);
	        myCallStmt.execute();
	        output = myCallStmt.getInt(3);
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * Runs a stored procedure that takes in an int input and a String input and returns a boolean.
	 * @param spName The name of the stored procedure
	 * @param input1 The int input
	 * @param input2 The String input
	 * @return The boolean result from the stored procedure
	 */
	public boolean storeProcedure(String spName, int input1, String input2) 
	{
		boolean output = false;
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?,?)}");
			myCallStmt.setInt(1, input1);
			myCallStmt.setString(2,input2);
	        myCallStmt.registerOutParameter(3,Types.BOOLEAN);
	        myCallStmt.execute();
	        output = myCallStmt.getBoolean(3);
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * Runs a stored procedure that takes in a single int input and returns a String.
	 * @param spName The name of the stored procedure
	 * @param inputs The int input
	 * @return The String result from the stored procedure
	 */
	public String storeProcedureString(String spName, int inputs) 
	{
		String output = "";
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?)}");
			myCallStmt.setInt(1, inputs);
	        myCallStmt.registerOutParameter(2,Types.VARCHAR);
	        myCallStmt.execute();
	        output = myCallStmt.getString(2);
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * Runs a stored procedure that takes in two int inputs
	 * @param spName The name of the stored procedure
	 * @param input1 First int input
	 * @param input2 Second int input
	 */
	public void storeProcedureJustIn(String spName, int input1, int input2) 
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?)}");
			myCallStmt.setInt(1, input1);
			myCallStmt.setInt(2, input2);
	        myCallStmt.execute();
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a stored procedure that takes in a single int input
	 * @param spName The name of the stored procedure
	 * @param input The int input
	 */
	public void storeProcedureJustIn(String spName, int input) 
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?)}");
			myCallStmt.setInt(1, input);
	        myCallStmt.execute();
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a stored procedure that takes in an int and a String
	 * @param spName The name of the stored procedure
	 * @param input1 The int input
	 * @param input2 The String input
	 */
	public void storeProcedureJustIn(String spName, int input1, String input2) 
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?)}");
			myCallStmt.setInt(1, input1);
			if(input2.equals("null"))
			{
				myCallStmt.setNull(2, Types.NULL);
			}
			else
			{
				myCallStmt.setString(2, input2);
			}
	        myCallStmt.execute();
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a stored procedure that takes in three ints
	 * @param spName The name of the stored procedure
	 * @param input1 The first int input
	 * @param input2 The second int input
	 * @param input3 The third int input
	 */
	public void storeProcedureJustIn(String spName, int input1, String input2, int input3) 
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?,?)}");
			myCallStmt.setInt(1, input1);
			myCallStmt.setString(2, input2);
			myCallStmt.setInt(3, input3);
	        myCallStmt.execute();
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a stored procedure that takes in one int and two Strings
	 * @param spName The name of the stored procedure
	 * @param input1 The int input
	 * @param input2 The first String input
	 * @param input3 The second String input
	 */
	public void storeProcedureJustIn(String spName, int input1, String input2, String input3) 
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?,?)}");
			myCallStmt.setInt(1, input1);
			myCallStmt.setString(2, input2);
			if(input3.equals("null"))
			{
				myCallStmt.setNull(3, Types.NULL);
			}
			else
			{
				myCallStmt.setString(3, input3);
			}
	        myCallStmt.execute();
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs a stored procedure that takes in an int, a String, and a boolean
	 * @param spName The stored procedure
	 * @param input1 The int input
	 * @param input2 The String input
	 * @param input3 The boolean input
	 */
	public void storeProcedureJustIn(String spName, int input1, String input2, boolean input3) 
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"(?,?,?)}");
			myCallStmt.setInt(1, input1);
			myCallStmt.setString(2, input2);
			myCallStmt.setBoolean(3, input3);
	        myCallStmt.execute();
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a game to the database from given input. This assumes that the program already has a unique ID number for the game.
	 * @param gameID A unique ID for the game
	 * @param title The title of the Game
	 * @param date The release date for the Game
	 * @param format The media format of the game (i.e. Cartridge, CD, etc.)
	 * @param notes Any notes on the game.
	 * @param consoleID The ID number of the console the Game is on
	 * @param publisherID The ID number of the publisher of the game
	 */
	public void addGame(int gameID, String title, String date, String format, String notes, int consoleID, int publisherID) 
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call addGameDB(?,?,?,?,?,?,?)}");
			myCallStmt.setInt(1, gameID);
			myCallStmt.setString(2, title);
			Date d = new Date(Integer.parseInt(date.substring(0,4)),Integer.parseInt(date.substring(5,7)),Integer.parseInt(date.substring(8,10)));
			myCallStmt.setDate(3, d);
			myCallStmt.setString(4, format);
			if(notes.equals("null"))
			{
				myCallStmt.setNull(5, Types.NULL);
			}
			else
			{
				myCallStmt.setString(5, notes);
			}
			myCallStmt.setInt(6, consoleID);
			myCallStmt.setInt(7, publisherID);
	        myCallStmt.execute();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Takes a game in the database and adds it to the specified collection.
	 * @param gameID The ID for the Game to add
	 * @param collectionName The name of the Collection that is being added to
	 * @param sealed If the copy of the Game is sealed
	 * @param box If the copy of the Game has a box 
	 * @param booklet If the copy of the Game has its booklet
	 * @param rate The user's rating of the game (0-5)
	 * @param notes Any notes about the copy of the Game
	 */
	public void addGameToCollection(int gameID, String collectionName, boolean sealed, boolean box, boolean booklet, int rate, String notes)
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call addGameToCollection(?,?,?,?,?,?,?)}");
			myCallStmt.setInt(1, gameID);
			myCallStmt.setString(2, collectionName);
			myCallStmt.setBoolean(3,sealed);
			myCallStmt.setBoolean(4, box);
			myCallStmt.setBoolean(5, booklet);
			myCallStmt.setInt(6, rate);
			if(notes.equals("null"))
			{
				myCallStmt.setNull(7, Types.NULL);
			}
			else
			{
				myCallStmt.setString(7, notes);
			}
			myCallStmt.execute();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Calls a stored procedure that takes in a GameID and another ID
	 * @param spName The stored procedure to run
	 * @param gameID The Game ID
	 * @param otherID The ID for the other item
	 */
	public void addWithTwo(String spName, int gameID, int otherID) 
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call "+spName+"("+gameID+","+otherID+")}");
	        myCallStmt.execute();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds a console to a collection.
	 * @param console_id The ID of the console to add
	 * @param col The name of the collection to add the console to
	 * @param cords If the copy of the console has its cords
	 * @param booklet If the copy of the console has its booklet
	 * @param box If the copy of the console has its box
	 * @param seal If the copy of the console is sealed
	 * @param notes Any notes about the copy of the console
	 */
	public void addConsoleToCollection(int console_id, String col, boolean cords, boolean booklet,
			boolean box, boolean seal, String notes)
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call addConsoleToCollection(?,?,?,?,?,?,?)}");
			myCallStmt.setInt(1, console_id);
			myCallStmt.setString(2, col);
			myCallStmt.setBoolean(3,cords);
			myCallStmt.setBoolean(4,booklet);
			myCallStmt.setBoolean(5, box);
			myCallStmt.setBoolean(6, seal);
			if(notes.equals("null"))
			{
				myCallStmt.setNull(7, Types.NULL);
			}
			else
			{
				myCallStmt.setString(7, notes);
			}
			myCallStmt.execute();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds a controller to a collection.
	 * @param controller_id The ID for the controller
	 * @param col The collection to add the controller to
	 * @param amt The amount of that type of controllers you have
	 */
	public void addControllerToCollection(int controller_id, String col, int amt)
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call addControllerToCollection(?,?,?)}");
			myCallStmt.setInt(1, controller_id);
			myCallStmt.setString(2, col);
			myCallStmt.setInt(3,amt);
			myCallStmt.execute();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}

	/**
	 * Adds a company to the database. Assumes the program already has a unique ID for the company.
	 * @param company_id A unique company id.
	 * @param name The name of the company
	 * @param open The year the company was founded
	 * @param closed The year the company closed, or null if the company has not closed
	 * @param notes Any notes about the company
	 */
	public void addCompany(int company_id, String name, String open, String closed, String notes)
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call addCompanyToDB(?,?,?,?,?)}");
			myCallStmt.setInt(1, company_id);
			myCallStmt.setString(2, name);
			myCallStmt.setInt(3,Integer.parseInt(open));
			if(closed.equals("null"))
			{
				myCallStmt.setNull(4, Types.NULL);
			}
			else
			{
				myCallStmt.setInt(4, Integer.parseInt(closed));
			}
			if(notes.equals("null"))
			{
				myCallStmt.setNull(5, Types.NULL);
			}
			else
			{
				myCallStmt.setString(5, notes);
			}
			myCallStmt.execute();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns if a given collection exists or not in the database. Helper to verify that any new collections in the database will be using a unique name.
	 * @param collectionName Name of the collection
	 * @return True if the collection name is in the database or False if it is not
	 */
	public boolean collectionExits(String collectionName)
	{
		boolean output = false;
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call isCollection(?,?)}");
			myCallStmt.setString(1,collectionName);
	        myCallStmt.registerOutParameter(2,Types.BOOLEAN);
	        myCallStmt.execute();
	        output = myCallStmt.getBoolean(2);
	
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return output;
	}
	
	/**
	 * Adds a collection to the database.
	 * @param collectionName Name of the collection to add to the database
	 */
	public void addCollection(String collectionName)
	{
		try 
		{
			statement = connection.createStatement();
			CallableStatement myCallStmt = connection.prepareCall("{call addCollection(?)}");
			myCallStmt.setString(1, collectionName);
			myCallStmt.execute();
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
}


