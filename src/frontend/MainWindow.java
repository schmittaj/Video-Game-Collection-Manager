package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * This is the main window for the program, holds all the internal windows.
 * @author Anthony Schmitt
 *
 */
public class MainWindow extends JFrame implements ActionListener, RecommendationGrabber
{
	private JMenuBar mBar;
	private int height, width;
	private GameListScreen gls;
	private AddGameScreen ags;
	private AddCompanyScreen acs;
	private JDesktopPane desktop;
	protected QueryRunner query;
	private CollectionGameListScreen cgls;
	private CompanyListScreen cls;
	private ConsoleListScreen conls;
	private ControllerListScreen contls;
	private CollectionConsoleListScreen ccls;
	private CollectionControllerListScreen cconls;
	private RecommendationGenerationSelecter rgs;
	private RecommendationGameListScreen rgls;
	private String infoFile = "serverinfo.txt";
	
	/**
	 * Constructor.
	 */
	public MainWindow()
	{
		init();
	}
	
	/**
	 * Initializes the object.
	 */
	protected void init()
	{
		query = new QueryRunner(infoFile);
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		setUpMenus();
		desktop = new JDesktopPane();
		this.setContentPane(desktop);
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		setUpWindows();
		this.setJMenuBar(mBar);
		this.setSize(new Dimension(width,height));
		this.setTitle("Game Collection Management System");
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	/**
	 * Sets up all of the windows. 
	 */
	private void setUpWindows()
	{
		gls = new GameListScreen(query);
		desktop.add(gls);
		gls.setVisible(true);
		ags = new AddGameScreen(query);
		desktop.add(ags);
		ags.setVisible(false);
		acs = new AddCompanyScreen(query);
		desktop.add(acs);
		acs.setVisible(false);
		cgls = new CollectionGameListScreen(query);
		desktop.add(cgls);
		cgls.setVisible(false);
		cls = new CompanyListScreen(query);
		cls.setVisible(false);
		desktop.add(cls);
		conls = new ConsoleListScreen(query);
		conls.setVisible(false);
		desktop.add(conls);
		contls = new ControllerListScreen(query);
		contls.setVisible(false);
		desktop.add(contls);
		ccls = new CollectionConsoleListScreen(query);
		ccls.setVisible(false);
		desktop.add(ccls);
		cconls = new CollectionControllerListScreen(query);
		cconls.setVisible(false);
		desktop.add(cconls);
		rgs = new RecommendationGenerationSelecter(query,this);
		rgs.setVisible(false);
		rgls = new RecommendationGameListScreen(query);
		rgls.setVisible(false);
		desktop.add(rgls);
	}

	/**
	 * Sets up the menus.
	 */
	private void setUpMenus()
	{
		JMenu menu1 = new JMenu("Views");
		JMenuItem itm1 = new JMenuItem("Games");
		itm1.addActionListener(this);
		JMenuItem itm2 = new JMenuItem("Game Collections");
		itm2.addActionListener(this);
		JMenuItem itm3 = new JMenuItem("Companies");
		itm3.addActionListener(this);
		JMenuItem itm4 = new JMenuItem("Consoles");
		itm4.addActionListener(this);
		JMenuItem itm5 = new JMenuItem("Controllers");
		itm5.addActionListener(this);
		JMenu menu2 = new JMenu("Add to Database");
		JMenuItem itm6 = new JMenuItem("Add Companies");
		itm6.addActionListener(this);
		JMenuItem itm7 = new JMenuItem("Add Games");
		itm7.addActionListener(this);
		JMenu menu3 = new JMenu("Collections");
		JMenuItem itm8 = new JMenuItem("Console Collections");
		itm8.addActionListener(this);
		JMenuItem itm9 = new JMenuItem("Controller Collections");
		itm9.addActionListener(this);
		JMenuItem itm10 = new JMenuItem("Generate Suggested Games");
		itm10.addActionListener(this);
		JMenuItem itm11 = new JMenuItem("Create New Collection");
		itm11.addActionListener(this);
		menu1.add(itm1);
		menu1.add(itm3);
		menu1.add(itm4);
		menu1.add(itm5);
		menu2.add(itm6);
		menu2.add(itm7);
		menu3.add(itm2);
		menu3.add(itm8);
		menu3.add(itm9);
		menu3.add(itm11);
		mBar = new JMenuBar();
		mBar.add(menu1);
		mBar.add(menu3);
		mBar.add(menu2);
		mBar.add(itm10);
	}

	/**
	 * Handles the menu selections.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Add Games"))
		{
			ags.setVisible(true);
			gls.setVisible(false);
			cgls.setVisible(false);
			cls.setVisible(false);
			conls.setVisible(false);
			contls.setVisible(false);
			acs.setVisible(false);
			ccls.setVisible(false);
			cconls.setVisible(false);
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Add Companies"))
		{
			acs.setVisible(true);
			ags.setVisible(false);
			gls.setVisible(false);
			cgls.setVisible(false);
			cls.setVisible(false);
			conls.setVisible(false);
			contls.setVisible(false);
			ccls.setVisible(false);
			cconls.setVisible(false);
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Games"))
		{
			gls.makeVisible();
			ags.setVisible(false);
			cgls.setVisible(false);
			cls.setVisible(false);
			conls.setVisible(false);
			contls.setVisible(false);
			acs.setVisible(false);
			ccls.setVisible(false);
			cconls.setVisible(false);
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Game Collections"))
		{
			cgls.makeVisible();
			ags.setVisible(false);
			gls.setVisible(false);
			cls.setVisible(false);
			conls.setVisible(false);
			contls.setVisible(false);
			acs.setVisible(false);
			ccls.setVisible(false);
			cconls.setVisible(false);
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Companies"))
		{
			cls.makeVisible();
			ags.setVisible(false);
			gls.setVisible(false);
			cgls.setVisible(false);
			conls.setVisible(false);
			contls.setVisible(false);
			acs.setVisible(false);
			ccls.setVisible(false);
			cconls.setVisible(false);
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Consoles"))
		{
			conls.makeVisible();
			ags.setVisible(false);
			gls.setVisible(false);
			cgls.setVisible(false);
			cls.setVisible(false);
			contls.setVisible(false);
			acs.setVisible(false);
			ccls.setVisible(false);
			cconls.setVisible(false);
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Controllers"))
		{
			contls.makeVisible();
			conls.setVisible(false);
			ags.setVisible(false);
			gls.setVisible(false);
			cgls.setVisible(false);
			cls.setVisible(false);
			acs.setVisible(false);
			ccls.setVisible(false);
			cconls.setVisible(false);
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Console Collections"))
		{
			contls.setVisible(false);
			conls.setVisible(false);
			ags.setVisible(false);
			gls.setVisible(false);
			cgls.setVisible(false);
			cls.setVisible(false);
			acs.setVisible(false);
			ccls.makeVisible();
			cconls.setVisible(false);
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Controller Collections"))
		{
			contls.setVisible(false);
			conls.setVisible(false);
			ags.setVisible(false);
			gls.setVisible(false);
			cgls.setVisible(false);
			cls.setVisible(false);
			acs.setVisible(false);
			ccls.setVisible(false);
			cconls.makeVisible();
			rgls.setVisible(false);
		}
		if(e.getActionCommand().equals("Generate Suggested Games"))
		{
			rgs.setVisible(true);
		}
		if(e.getActionCommand().equals("Create New Collection"))
		{
			String collectionName = JOptionPane.showInputDialog(null, "Name of new collection:");
			if(collectionName != null)
			{
				if(collectionName.length() > 255)
				{
					collectionName = collectionName.substring(0, 254);
				}
				if(!query.collectionExits(collectionName)) 
				{
					query.addCollection(collectionName);
					cgls = null;
					ccls = null;
					cconls = null;
					newCollectionHolders();
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Collection already exists!","Already Exists",JOptionPane.WARNING_MESSAGE);
				}
			}
		}
	}
	
	/**
	 * Updating for when there is a new collection.
	 */
	private void newCollectionHolders()
	{	
		cgls = new CollectionGameListScreen(query);
		cgls.setVisible(false);
		desktop.add(cgls);
		ccls = new CollectionConsoleListScreen(query);
		ccls.setVisible(false);
		desktop.add(ccls);
		cconls = new CollectionControllerListScreen(query);
		cconls.setVisible(false);
		desktop.add(cconls);
	}

	/**
	 * Helper for when Recommendation List is selected.
	 */
	public void haveGrabRecommendations()
	{
		contls.setVisible(false);
		conls.setVisible(false);
		ags.setVisible(false);
		gls.setVisible(false);
		cgls.setVisible(false);
		cls.setVisible(false);
		acs.setVisible(false);
		ccls.setVisible(false);
		cconls.setVisible(false);
		rgls.setGameList(rgs.getRecommendationList());
		rgls.makeVisible();
	}

}
