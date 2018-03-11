package frontend;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.*;
import backend.*;

/**
 * Lets user add a game to the database
 * @author Anthony Schmitt
 *
 */
public class AddGameScreen extends JInternalFrame 
{
	private int height, width;
	private QueryRunner query;
	
	/**
	 * The Constructor
	 * @param query The connection to the database
	 */
	public AddGameScreen(QueryRunner query)
	{
		super("Add Games to Database",true,true,true,true);
		this.query = query;
		init();
	}
	
	/**
	 * Sets up the window
	 */
	protected void init()
	{
		height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()-100;
		width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-100;
		String[] consoles = query.querySingleListReturnColumn(Queries.CONSOLES);
		String[] companies = query.querySingleListReturnColumn(Queries.COMPANIES);
		String[] genres = query.querySingleListReturnColumn(Queries.GENRES);
		setUpInputs(consoles,companies,genres);
		this.setSize(new Dimension(width,height));
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	/**
	 * Sets up the input fields.
	 * @param consoles List of consoles in the database
	 * @param companies List of companies in the database
	 * @param genres List of genres in the database
	 */
	private void setUpInputs(String[] consoles, String[] companies, String[] genres) 
	{
		JLabel titleLabel = new JLabel("Title:");
		final JTextField titleField = new JTextField();
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(titleLabel,BorderLayout.NORTH);
		titlePanel.add(titleField,BorderLayout.CENTER);
		JLabel dateLabel = new JLabel("Release Date:");
		String [] months = {"January","Febuary","March","April","May","June","July","August","September","October","November","December"};
		SpinnerListModel monthsModel = new SpinnerListModel(months);
		final JSpinner monthSpin = new JSpinner(monthsModel);
		SpinnerNumberModel daysModel = new SpinnerNumberModel(1,1,31,1);
		final JSpinner daySpin = new JSpinner(daysModel);
		SpinnerNumberModel yearModel = new SpinnerNumberModel(1990,1950,2020,1);
		final JSpinner yearSpin = new JSpinner(yearModel);
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearSpin,"#");
		yearSpin.setEditor(editor);
		JLabel noteLabel = new JLabel("Notes:");
		JPanel spinnersPanel = new JPanel();
		spinnersPanel.setLayout(new GridLayout(1, 3));
		spinnersPanel.add(monthSpin);
		spinnersPanel.add(daySpin);
		spinnersPanel.add(yearSpin);
		JPanel datePanel = new JPanel();
		datePanel.setLayout(new BorderLayout());
		datePanel.add(dateLabel,BorderLayout.NORTH);
		datePanel.add(spinnersPanel, BorderLayout.CENTER);
		final JTextField noteField = new JTextField();
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new BorderLayout());
		notePanel.add(noteLabel,BorderLayout.NORTH);
		notePanel.add(noteField,BorderLayout.CENTER);
		final JComboBox<String> consoleSelect = new JComboBox<String>(consoles);
		final JList<String> pubList = new JList<String>(companies);
		pubList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pubList.setLayoutOrientation(JList.VERTICAL);
		pubList.setVisibleRowCount(-1);
		JScrollPane pubListScroll = new JScrollPane(pubList);
		pubListScroll.setPreferredSize(new Dimension(400,40));
		final JList<String> devList = new JList<String>(companies);
		devList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		devList.setLayoutOrientation(JList.VERTICAL);
		devList.setVisibleRowCount(-1);
		JScrollPane devListScroll = new JScrollPane(devList);
		devListScroll.setPreferredSize(new Dimension(400,40));
		final JList<String> genreList = new JList<String>(genres);
		genreList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		genreList.setLayoutOrientation(JList.VERTICAL);
		genreList.setVisibleRowCount(-1);
		JScrollPane genreListScroll = new JScrollPane(genreList);
		genreListScroll.setPreferredSize(new Dimension(400,40));
		JLabel formatLabel = new JLabel("Format");
		final JTextField formatField = new JTextField();
		JPanel formatPanel = new JPanel();
		formatPanel.setLayout(new BorderLayout());
		formatPanel.add(formatLabel,BorderLayout.NORTH);
		formatPanel.add(formatField,BorderLayout.CENTER);
		JPanel inputsPanel = new JPanel();
		inputsPanel.setLayout(new GridLayout(2,4));
		inputsPanel.add(titlePanel);
		inputsPanel.add(datePanel);
		inputsPanel.add(notePanel);
		inputsPanel.add(formatPanel);
		inputsPanel.add(consoleSelect);
		inputsPanel.add(pubListScroll);
		inputsPanel.add(devListScroll);
		inputsPanel.add(genreListScroll);
		JButton add = new JButton("Add to database");
		add.addActionListener(new ActionListener() 
		{	
			/**
			 * Listener for the add button. Checks the inputs to ensure that the data is correct for the database.
			 * Give error messages in a pop-up box if any of the data is not valid for the database.
			 */
			public void actionPerformed(ActionEvent e) 
			{
				boolean goodToGo = true;
				String errors = "";
				String title = titleField.getText();
				if(title == null || title.equals(""))
				{
					goodToGo = false;
					errors += "You must enter a title.\n";
				}
				else if(title.length() > 255)
				{
					title = title.substring(0, 254);
				}
				String format = formatField.getText();
				if(format == null || format.equals(""))
				{
					goodToGo = false;
					errors += "You must enter a format.\n";
				}
				else if(format.length() > 255)
				{
					format = format.substring(0, 254);
				}
				String notes = noteField.getText();
				int year = Integer.parseInt(yearSpin.getValue().toString());
				int day = Integer.parseInt(daySpin.getValue().toString());
				String tmp = monthSpin.getValue().toString();
				int month = monthToNum(tmp);
				DecimalFormat decFormat = new DecimalFormat("00");
				String date = "" + year + "-" + decFormat.format(month) + "-" + decFormat.format(day);
				int consoleID = 0;
				tmp = consoleSelect.getSelectedItem().toString();				
				consoleID = query.storeProcedure("getConsoleID", tmp);
				int publisherID = 0;
				if(pubList.getSelectedIndex() == -1)
				{
					goodToGo = false;
					errors += "You must select a publisher.\n";
				}
				else
				{	
					tmp = pubList.getSelectedValue().toString();
					publisherID = query.storeProcedure("getCompanyID", tmp);
				}
				int[] devIDs = new int[devList.getSelectedIndices().length];
				if(devList.getSelectedIndex() == -1)
				{
					goodToGo = false;
					errors += "You must select at least 1 developer.\n";
				}
				for(int a = 0; a < devIDs.length; a++)
				{
					tmp = devList.getSelectedValuesList().get(a).toString();					
					devIDs[a] = query.storeProcedure("getCompanyID", tmp);
				}
				int[] genreIDs = new int[genreList.getSelectedIndices().length];
				if(genreList.getSelectedIndex() == -1)
				{
					goodToGo = false;
					errors += "You must select at least 1 genre.\n";
				}
				for(int a = 0; a < genreIDs.length; a++)
				{
					tmp = genreList.getSelectedValuesList().get(a).toString();
					genreIDs[a] = query.storeProcedure("getGenreID", tmp);
				}
				int gameID = Integer.parseInt(query.querySingleListReturnColumn(Queries.MAX_GAME_ID)[0]) + 1;
				if(notes == null || notes.equals(""))
				{
					notes = "null";
				}
				if(goodToGo)
				{
					query.addGame(gameID, title, date, format, notes, consoleID, publisherID);
					for(int a = 0; a < devIDs.length; a++)
					{
						query.addWithTwo("addDevelopedGameDB", gameID, devIDs[a]);
					}
					for(int a = 0; a < genreIDs.length; a++)
					{

						query.addWithTwo("addHasGenreDB", gameID, genreIDs[a]);
					}
					JOptionPane.showMessageDialog(null,title + " had been added to the Database.","Game Added",JOptionPane.INFORMATION_MESSAGE);
					titleField.setText("");
					formatField.setText("");
					noteField.setText("");
					yearSpin.setValue(1990);
					monthSpin.setValue("January");
					daySpin.setValue(1);
					consoleSelect.setSelectedIndex(0);
					pubList.clearSelection();
					devList.clearSelection();
					genreList.clearSelection();
				}
				else
				{
					JOptionPane.showMessageDialog(null,errors,"Missing Input",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.setLayout(new BorderLayout());
		this.add(inputsPanel, BorderLayout.CENTER);
		this.add(add,BorderLayout.SOUTH);
	}
	
	/**
	 * Helper function to quickly grab the month value from a String.
	 * @param tmp Month as String
	 * @return Month as int
	 */
	private int monthToNum(String tmp)
	{
		int month = 0;
		if(tmp == "January")
		{
			month = 1;
		}
		else if(tmp == "Feburary")
		{
			month = 2;
		}
		else if(tmp == "March")
		{
			month = 3;
		}
		else if(tmp == "April")
		{
			month = 4;
		}
		else if(tmp == "May")
		{
			month = 5;
		}
		else if(tmp == "June")
		{
			month = 6;
		}
		else if(tmp == "July")
		{
			month = 7;
		}
		else if(tmp == "August")
		{
			month = 8;
		}
		else if(tmp == "September")
		{
			month = 9;
		}
		else if(tmp == "October")
		{
			month = 10;
		}
		else if(tmp == "November")
		{
			month = 11;
		}
		else if(tmp == "December")
		{
			month = 12;
		}
		return month;
	}
	
	/**
	 * Kills window
	 */
	public void kill()
	{
		this.dispose();
	}

}
