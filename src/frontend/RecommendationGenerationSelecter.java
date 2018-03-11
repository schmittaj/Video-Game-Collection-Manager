package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * This generates a list of recommended games based on selected collection
 * @author Anthony Schmitt
 *
 */
public class RecommendationGenerationSelecter extends JFrame
{
	private QueryRunner query;
	private String collection;
	private JButton generate, cancel;
	private JComboBox<String> collectionSelecter;
	private JPanel buttonPanel;
	private String[] collections, suggestions;
	private RecommendationGrabber rg;
	
	/**
	 * Constructor
	 * @param q	Query Runner
	 * @param grab RecommendationGrabber referrer
	 */
	public RecommendationGenerationSelecter(QueryRunner q, RecommendationGrabber grab)
	{
		super("Generate Recommendations");
		this.query = q;
		this.rg = grab;
		init();
	}

	/**
	 * Initializes the object.
	 */
	private void init()
	{
		collections = query.querySingleListReturnColumn(Queries.COLLECTIONS);
		collectionSelecter = new JComboBox<String>(collections);
		this.setLayout(new BorderLayout());
		this.add(collectionSelecter, BorderLayout.CENTER);
		setUpButtons();
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(500,500);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets up the buttons. Most importantly builds the game recommendation list based on the selected collection.
	 * Grabs all games from collection where the rating is a 4 or higher, then it look at the genres and developers
	 * and builds a list of games where the genre and developer are both the same as a high rated game.
	 */
	private void setUpButtons()
	{
		generate = new JButton("Generate Recommendations");
		generate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				collection = collectionSelecter.getSelectedItem().toString();
				collection = "'"+collection+"'";
				suggestions = query.querySingleListReturnColumn("Select helper.showID From "+
				"(Select Game.title AS showTitle, Game.id AS showID, Has_Genre.genre_id AS geneID, Developed_Game.company_id AS compID "+
				"From Game, Has_Genre, Developed_Game where Game.id = Has_Genre.game_id AND Game.id = Developed_Game.game_id) AS helper, "+
				"(Select Genre.id AS foundGenreID, Developed_Game.company_id AS developerID "+
				"from Game_In_Collection, Game, Has_Genre, Genre, Company, Developed_Game "+ 
				"Where Game.id = Game_In_Collection.game_id AND Game.id = Has_Genre.game_id "+
				"AND Has_Genre.genre_id = Genre.id AND Developed_Game.game_id = Game.id "+ 
				"AND Developed_Game.company_id = Company.id AND rating >= 4 AND collection_name = "+collection+") AS suggest "+
				"where suggest.foundGenreID = helper.geneID AND suggest.developerID = helper.compID AND helper.showID not in "+
				"(select game_id AS haveIDs from Game_In_Collection where collection_name = "+collection+") order by helper.showID;");
				invisible();
				rg.haveGrabRecommendations();
			}	
		});
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				clearFields();
				invisible();
			}
		});
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		buttonPanel.add(generate);
		buttonPanel.add(cancel);
	}
	
	/**
	 * Passes out the recommendation list.
	 * @return
	 */
	public int[] getRecommendationList()
	{
		int[] output = new int[suggestions.length];
		for(int a = 0; a < suggestions.length; a++)
		{
			output[a] = Integer.parseInt(suggestions[a]);
		}
		return output;
	}
	
	/**
	 * Makes this invisible.
	 */
	private void invisible()
	{
		this.setVisible(false);
	}
	
	/**
	 * Clears the fields.
	 */
	private void clearFields()
	{
		collectionSelecter.setSelectedIndex(0);
	}
	
}
