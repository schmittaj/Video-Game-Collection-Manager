package frontend;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;
import backend.*;

/**
 * Lets you select dates for filtering
 * @author Anthony Schmitt
 *
 */
public class DateSelectOptions extends JFrame
{
	private JButton clear, apply;
	private String[] selectedList;
	private DateSelectable game;
	private JSpinner monthSpin;
	private JSpinner daySpin;
	private JSpinner yearSpin;
	private JSpinner monthEndSpin;
	private JSpinner dayEndSpin;
	private JSpinner yearEndSpin;
	
	/**
	 * Constructor.
	 * @param g DateSelectable using the object
	 */
	public DateSelectOptions(DateSelectable g)
	{
		super("Filter by Date");
		this.game = g;
		init();
		this.pack();
	}
	
	/**
	 * Initializes the object.
	 */
	private void init()
	{
		this.selectedList = null;
		JPanel spin1Panel = new JPanel();
		JPanel spin2Panel = new JPanel();
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
		spin1Panel.setLayout(new GridLayout(1,3));
		spin1Panel.add(monthSpin);
		spin1Panel.add(daySpin);
		spin1Panel.add(yearSpin);
		SpinnerListModel monthsEndModel = new SpinnerListModel(months);
		monthEndSpin = new JSpinner(monthsEndModel);
		SpinnerNumberModel daysEndModel = new SpinnerNumberModel(1,1,31,1);
		dayEndSpin = new JSpinner(daysEndModel);
		SpinnerNumberModel yearEndModel = new SpinnerNumberModel(1990,1950,2020,1);
		yearEndSpin = new JSpinner(yearEndModel);
		JSpinner.NumberEditor editorEnd = new JSpinner.NumberEditor(yearEndSpin,"#");
		yearEndSpin.setEditor(editorEnd);
		spin2Panel.setLayout(new GridLayout(1,3));
		spin2Panel.add(monthEndSpin);
		spin2Panel.add(dayEndSpin);
		spin2Panel.add(yearEndSpin);
		spinPanel.add(spin1Panel);
		spinPanel.add(spin2Panel);
		monthEndSpin.setEnabled(false);
		dayEndSpin.setEnabled(false);
		yearEndSpin.setEnabled(false);
		JRadioButton before = new JRadioButton("Before");
		before.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				if(before.isSelected())
				{
					monthEndSpin.setEnabled(false);
					dayEndSpin.setEnabled(false);
					yearEndSpin.setEnabled(false);
				}
			}
		});
		JRadioButton after = new JRadioButton("After");
		after.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				if(after.isSelected())
				{
					monthEndSpin.setEnabled(false);
					dayEndSpin.setEnabled(false);
					yearEndSpin.setEnabled(false);
				}
			}
		});
		JRadioButton between = new JRadioButton("Between");
		between.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				if(between.isSelected())
				{
					monthEndSpin.setEnabled(true);
					dayEndSpin.setEnabled(true);
					yearEndSpin.setEnabled(true);
				}
			}
		});
		ButtonGroup timeGroup = new ButtonGroup();
		timeGroup.add(before);
		timeGroup.add(after);
		timeGroup.add(between);
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(1,3));
		radioPanel.add(before);
		radioPanel.add(after);
		radioPanel.add(between);
		apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				selectedList = new String[2];
				if(before.isSelected())
				{
					int year = Integer.parseInt(yearSpin.getValue().toString());
					int day = Integer.parseInt(daySpin.getValue().toString());
					String tmp = monthSpin.getValue().toString();
					int month = monthToNum(tmp);
					DecimalFormat decFormat = new DecimalFormat("00");
					String date = "" + year + "-" + decFormat.format(month) + "-" + decFormat.format(day);
					selectedList[0] = date;
				}
				else if(after.isSelected())
				{
					int year = Integer.parseInt(yearSpin.getValue().toString());
					int day = Integer.parseInt(daySpin.getValue().toString());
					String tmp = monthSpin.getValue().toString();
					int month = monthToNum(tmp);
					DecimalFormat decFormat = new DecimalFormat("00");
					String date = "" + year + "-" + decFormat.format(month) + "-" + decFormat.format(day);
					selectedList[1] = date;
				}
				else if(between.isSelected())
				{
					int year = Integer.parseInt(yearSpin.getValue().toString());
					int day = Integer.parseInt(daySpin.getValue().toString());
					String tmp = monthSpin.getValue().toString();
					int month = monthToNum(tmp);
					DecimalFormat decFormat = new DecimalFormat("00");
					String date = "" + year + "-" + decFormat.format(month) + "-" + decFormat.format(day);
					selectedList[0] = date;
					
					year = Integer.parseInt(yearEndSpin.getValue().toString());
					day = Integer.parseInt(dayEndSpin.getValue().toString());
					tmp = monthEndSpin.getValue().toString();
					month = monthToNum(tmp);
					date = "" + year + "-" + decFormat.format(month) + "-" + decFormat.format(day);
					selectedList[1] = date;
				}
				invisible();
				game.haveGrabSelectedDateList();
			}
			
		});
		clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				
				selectedList = null;
				invisible();
				game.haveGrabSelectedDateList();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		buttonPanel.add(apply);
		buttonPanel.add(clear);
		this.setLayout(new BorderLayout());
		this.add(radioPanel, BorderLayout.NORTH);
		this.add(spinPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-200;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-200;
		this.setLocation(width, height);
	}
	
	/**
	 * Get the list of selected dates.
	 * @return List of selected dates.
	 */
	public String[] getSelected()
	{
		return selectedList;
	}

	/**
	 * Makes invisible.
	 */
	private final void invisible()
	{
		this.setVisible(false);
	}
	
	/**
	 * Clears the selections.
	 */
	public void clearSelected()
	{
		selectedList = null;
		yearSpin.setValue(1990);
		monthSpin.setValue("January");
		daySpin.setValue(1);
		yearEndSpin.setValue(1990);
		monthEndSpin.setValue("January");
		dayEndSpin.setValue(1);
	}
	
	/**
	 * Helper to quick convert month to its number value.
	 * @param tmp Month Sting
	 * @return Month int
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
}
