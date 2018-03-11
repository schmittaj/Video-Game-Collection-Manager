package backend;

/**
 * For holding useful queries.
 * @author Anthony Schmitt
 *
 */
public final class Queries 
{
	public static final String ALL_GAMES = "Select title, Console.name, Game.release_date, format, Company.name from Game, Console, Company where Game.console_id = Console.id AND Company.id = Game.publisher_id order by Console.id, Game.title;";
	public static final String GENRES = "select name from Genre order by name;";
	public static final String COMPANIES = "select name from Company order by name;";
	public static final String CONSOLES = "select name from Console order by name;";
	public static final String COLLECTIONS = "select name from Collection order by name;";
	public static final String HIGHEST_GAME_ID = "select max(id) from Game;";
	public static final String GAMES_IN_COLLECTION = "Select Game.title, is_sealed, have_box, have_booklet, rating, Game_In_Collection.notes, Console.name from Game_In_Collection, Game, Console where Game.id = Game_In_Collection.game_id AND Console.id = Game.console_id AND collection_name = 'Tony';";
	public static final String CONSOLES_IN_COLLECTION = "Select Console.name, have_cords, have_booklet, have_box, is_sealed, Console_In_Collection.notes from Console, Console_In_Collection where Console_In_Collection.console_id = Console.id;";
	public static final String CONTROLLERS_IN_COLLECTION = "Select Controller.name, quantity, Console.name from Controller, Controller_In_Collection, Console where Controller.id = Controller_In_Collection.controller_id AND Controller.console_id = Console.id;";
	public static final String MAX_GAME_ID = "select max(id) from Game;";
	public static final String MAX_COMPANY_ID = "select max(id) from Company;";
	public static final String ALL_COMPANIES = "Select name, year_formed, year_closed, notes from Company order by name;";
	public static final String ALL_CONSOLES = "Select Console.name, release_date, production_end_date, units_sold, original_price, Company.name from Console, Company where Console.company_id = Company.id order by Console.name;";
	public static final String All_CONTROLELRS = "Select Controller.name, Console.name from Controller, Console where Console.id = Controller.console_id order by Console.name, Controller.name;";
	public static final String DEVELOPERS = "select distinct name from Company, Developed_Game where company_id = id order by name;";
	public static final String PUBLISHERS = "select distinct Company.name from Company, Game where publisher_id = Company.id order by Company.name;";
	public static final String CONSOLE_MAKERS = "select distinct Company.name from Company, Console where company_id = Company.id order by Company.name;";
}
