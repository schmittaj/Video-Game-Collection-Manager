package frontend;

import javax.swing.event.*;
import backend.*;

/**
 * This is just a list of games, but of recommended games
 * @author Anthony Schmitt
 *
 */
public class RecommendationGameListScreen extends GameListScreen
{
	
	private int[] gameList;
	
	/**
	 * Constructor
	 * @param query Query Runner
	 */
	public RecommendationGameListScreen(QueryRunner query)
	{
		super(query);
		this.setTitle("View Recommended Games");
		this.query = query;
	}
	
	/**
	 * Builds the query to show the recommendations.
	 */
	protected void buildQuery()
	{
		currentQuery = "Select distinct title, Console.name, Game.release_date, format, Company.name from Game, Console, Company, Developed_Game, Has_Genre where Game.console_id = Console.id AND Company.id = Game.publisher_id AND Developed_Game.game_id = Game.id AND Has_Genre.game_id = Game.id "; 
		String end = ") order by Console.name, Game.title;";
		String addOn = " AND (";
		for(int a = 0; a < gameList.length; a++)
		{
			addOn = addOn + "Game.id = " + gameList[a] + " OR ";
		}
		addOn = addOn.substring(0, addOn.length()-3);	
		currentQuery += addOn + end;
	}
	
	/**
	 * When a new table item is selected, it is passed to the game to a GameView and makes it visible.
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
	
	/**
	 * Sets the list of recommended games.
	 * @param game List of recommended games
	 */
	public void setGameList(int[] game)
	{
		this.gameList = game;
	}
	
	/**
	 * Makes this visible
	 */
	public void makeVisible()
	{
		buildQuery();
		updateList();
		this.setVisible(true);
	}
	
}