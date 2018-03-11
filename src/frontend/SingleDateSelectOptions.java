package frontend;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;

import backend.SingleDateSelectable;

/**
 * For selecting a single date for filtering
 * @author Anthony Schmitt
 *
 */
public class SingleDateSelectOptions extends JFrame
{

	private JButton apply;
	private String selected;
	private SingleDateSelectable game;
	private JSpinner monthSpin;
	private JSpinner daySpin;
	private JSpinner yearSpin;
	
	/**
	 * Constructor
	 * @param g Referring object.
	 */
	public SingleDateSelectOptions(SingleDateSelectable g)
	{
		super("Set Date");
		this.game = g;
		init();
		this.pack();
	}
	
	/**
	 * Initializes the object.
	 */
	private void init()
	{
		this.selected = null;
		JPanel spinPanel = new JPanel();
		String [] months = {"January","Febuary","March","April","May","June","July","August","September","October","November","December"};
		SpinnerListModel monthsModel = new SpinnerListModel(months);
		monthSpin = new JSpinner(monthsModel);
		SpinnerNumberModel daysModel = new SpinnerNumberModel(1,1,31,1);
		daySpin = new JSpinner(daysModel);
		SpinnerNumberModel yearModel = new SpinnerNumberModel(1990,1950,2020,1);
		yearSpin = new JSpinner(yearModel);
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(yearSpin,"#");
		yearSpin.setEditor(editor);
		spinPanel.setLayout(new GridLayout(1,3));
		spinPanel.add(monthSpin);
		spinPanel.add(daySpin);
		spinPanel.add(yearSpin);

		apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				selected = "";
				int year = Integer.parseInt(yearSpin.getValue().toString());
				int day = Integer.parseInt(daySpin.getValue().toString());
				String tmp = monthSpin.getValue().toString();
				int month = monthToNum(tmp);
				DecimalFormat decFormat = new DecimalFormat("00");
				String date = "" + year + "-" + decFormat.format(month) + "-" + decFormat.format(day);
				selected = date;
				invisible();
				game.haveGrabSelectedDate();
			}
			
		});
		this.setLayout(new BorderLayout());
		this.add(spinPanel, BorderLayout.CENTER);
		this.add(apply, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-200;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-200;
		this.setLocation(width, height);
	}
	
	/**
	 * Gets selected date
	 * @return Date selected
	 */
	public String getSelected()
	{
		return selected;
	}
	
	/**
	 * Sets the selected date
	 * @param yr Year to set to
	 * @param mo Month to set to
	 * @param dy Day to set to
	 */
	public void setSelected(int yr, String mo, int dy)
	{
		yearSpin.setValue(yr);
		monthSpin.setValue(mo);
		daySpin.setValue(dy);
	}

	/**
	 * Clears the selection
	 */
	public void clearSelected()
	{
		yearSpin.setValue(1990);
		monthSpin.setValue("January");
		daySpin.setValue(1);
	}
	
	/**
	 * Makes this window invisible.
	 */
	private final void invisible()
	{
		this.setVisible(false);
	}
	
	/**
	 * Helper function to switch month String to month's int
	 * @param tmp Month name
	 * @return Month as int
	 */
	private int monthToNum(String tmp)
	{
		int month = 0;
		if(tmp.equals("January"))
		{
			month = 1;
		}
		else if(tmp.equals("Feburary"))
		{
			month = 2;
		}
		else if(tmp.equals("March"))
		{
			month = 3;
		}
		else if(tmp.equals("April"))
		{
			month = 4;
		}
		else if(tmp.equals("May"))
		{
			month = 5;
		}
		else if(tmp.equals("June"))
		{
			month = 6;
		}
		else if(tmp.equals("July"))
		{
			month = 7;
		}
		else if(tmp.equals("August"))
		{
			month = 8;
		}
		else if(tmp.equals("September"))
		{
			month = 9;
		}
		else if(tmp.equals("October"))
		{
			month = 10;
		}
		else if(tmp.equals("November"))
		{
			month = 11;
		}
		else if(tmp.equals("December"))
		{
			month = 12;
		}
		return month;
	}
	
	/**
	 * Helper function to turn month number into the String
	 * @param tmp Month number
	 * @return Month name
	 */
	public String numToMonth(int tmp)
	{
		String month = "";
		if(tmp == 1)
		{
			month = "January";
		}
		else if(tmp == 2)
		{
			month = "Feburary";
		}
		else if(tmp == 3)
		{
			month = "March";
		}
		else if(tmp == 4)
		{
			month = "April";
		}
		else if(tmp == 5)
		{
			month = "May";
		}
		else if(tmp == 6)
		{
			month = "June";
		}
		else if(tmp == 7)
		{
			month = "July";
		}
		else if(tmp == 8)
		{
			month = "August";
		}
		else if(tmp == 9)
		{
			month = "September";
		}
		else if(tmp == 10)
		{
			month = "October";
		}
		else if(tmp == 11)
		{
			month = "November";
		}
		else if(tmp == 12)
		{
			month = "December";
		}
		return month;
	}
}
