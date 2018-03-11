package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * Lets user add a company to the database
 * @author Anthony Schmitt
 *
 */
public class AddCompanyScreen extends JInternalFrame
{
	
	private int height, width;
	private QueryRunner query;
	
	/**
	 * Constructor.
	 * @param query The database connection
	 */
	public AddCompanyScreen(QueryRunner query)
	{
		super("Add Companiess to Database",true,true,true,true);
		this.query = query;
		init();
	}
	
	/**
	 * Sets up the window.
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
		JLabel nameLabel = new JLabel("Name:");
		final JTextField nameField = new JTextField();
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BorderLayout());
		namePanel.add(nameLabel,BorderLayout.NORTH);
		namePanel.add(nameField,BorderLayout.CENTER);
		JLabel openLabel = new JLabel("Year Founded:");
		JTextField openField = new JTextField();
		JPanel openPanel = new JPanel();
		openPanel.setLayout(new GridLayout(1,2));
		openPanel.add(openLabel);
		openPanel.add(openField);
		JLabel closedLabel = new JLabel("Year Closed:");
		JTextField closedField = new JTextField();
		JPanel closedPanel = new JPanel();
		closedPanel.setLayout(new GridLayout(1,2));
		closedPanel.add(closedLabel);
		closedPanel.add(closedField);
		JLabel noteLabel = new JLabel("Notes:");
		final JTextField noteField = new JTextField();
		JPanel notePanel = new JPanel();
		notePanel.setLayout(new BorderLayout());
		notePanel.add(noteLabel,BorderLayout.NORTH);
		notePanel.add(noteField,BorderLayout.CENTER);
		
		JPanel inputsPanel = new JPanel();
		inputsPanel.setLayout(new GridLayout(1,4));
		inputsPanel.add(namePanel);
		inputsPanel.add(openPanel);
		inputsPanel.add(closedPanel);
		inputsPanel.add(notePanel);
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
				String name = nameField.getText();
				if(name == null || name.equals(""))
				{
					goodToGo = false;
					errors += "You must enter a name.\n";
				}
				else if(name.length() > 255)
				{
					name = name.substring(0, 254);
				}
				String open = openField.getText();
				if(open == null || open.equals(""))
				{
					goodToGo = false;
					errors += "You must enter a Year Founded.\n";
				}
				else
				{
					int tmp = Integer.parseInt(open);
					if(tmp < 1700 || tmp > 2100)
					{
						goodToGo = false;
						errors += "You must enter a valid Year Founded.\n";
					}
				}
				String closed = closedField.getText();
				if(closed == null || closed.equals(""))
				{
					closed = "null";
				}
				else
				{
					int tmp = Integer.parseInt(closed);
					if(tmp < 1700 || tmp > 2100)
					{
						goodToGo = false;
						errors += "You must enter a valid Year Closed.\n";
					}
					if(tmp < Integer.parseInt(open))
					{
						goodToGo = false;
						errors += "The company cannot have closed before it was founded.\n";
					}
				}
				String notes = noteField.getText();
				if(notes == null || notes.equals(""))
				{
					notes = "null";
				}
				if(goodToGo)
				{
					int company_id = Integer.parseInt(query.querySingleListReturnColumn(Queries.MAX_COMPANY_ID)[0]) + 1;
					query.addCompany(company_id, name, open, closed, notes);
					JOptionPane.showMessageDialog(null,name + " had been added to the Database.","Game Added",JOptionPane.INFORMATION_MESSAGE);
					nameField.setText("");
					openField.setText("");
					closedField.setText("");
					noteField.setText("");
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
	 * Kills the window.
	 */
	public void kill()
	{
		this.dispose();
	}

}
