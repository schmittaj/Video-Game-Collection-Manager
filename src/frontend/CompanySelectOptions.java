package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * This gives the companies in selectable form
 * can pass in different company lists as well as allow for single or multiple selection
 * @author Anthony Schmitt
 *
 */
public class CompanySelectOptions extends JFrame
{
	private JCheckBox[] boxList;
	private String[] companies;
	private QueryRunner query;
	private int cols = 5;
	private JButton clear, apply;
	private int[] selectedList;
	private CompanySelectable cs;
	private JScrollPane scroller;
	private boolean multipleSelect;
	private String queryToRun;
	
	/**
	 * Constructor.
	 * @param q	Query Runner
	 * @param g Referring CompanySelectable object
	 * @param multi Allow multiple selection?
	 * @param qtr Query to pull list
	 */
	public CompanySelectOptions(QueryRunner q, CompanySelectable g, boolean multi, String qtr)
	{
		super("Filter by Company");
		this.query = q;
		this.cs = g;
		this.multipleSelect = multi;
		this.queryToRun = qtr;
		init();
		this.pack();
	}
	
	/**
	 * Initializes the object.
	 */
	private void init()
	{
		this.selectedList = null;
		JPanel optionsPanel = new JPanel();
		companies = query.querySingleListReturnColumn(queryToRun);
		boxList = new JCheckBox[companies.length];
		int rows = boxList.length/cols;
		if(boxList.length%cols != 0)
		{
			rows += 1;
		}
		optionsPanel.setLayout(new GridLayout(rows,cols));
		for(int a = 0; a < boxList.length; a++)
		{
			boxList[a] = new JCheckBox(companies[a]);
			optionsPanel.add(boxList[a]);
		}
		scroller = new JScrollPane(optionsPanel);
		apply = new JButton("Apply");
		apply.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int ctr = 0;
				int[] tmp = new int[boxList.length];
				for(int a = 0; a < boxList.length; a++)
				{
					if(boxList[a].isSelected())
					{
						tmp[ctr] = query.storeProcedure("getCompanyID", boxList[a].getText());
						ctr++;
					}
				}
				selectedList = new int[ctr];
				for(int a = 0; a < selectedList.length; a++)
				{
					selectedList[a] = tmp[a];
				}
				invisible();
				cs.haveGrabSelectedCompanyList();
			}
			
		});
		clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				for(int a = 0; a < boxList.length; a++)
				{
					boxList[a].setSelected(false);
				}
				selectedList = null;
				cs.haveGrabSelectedCompanyList();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		buttonPanel.add(apply);
		buttonPanel.add(clear);
		this.setLayout(new BorderLayout());
		this.add(scroller, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-200;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-200;
		this.setLocation(width, height);
		if(!multipleSelect)
		{
			ButtonGroup group = new ButtonGroup();
			for(int a = 0; a < boxList.length; a++)
			{
				group.add(boxList[a]);
			}
		}
	}
	
	/**
	 * Returns the selected items as a list of id numbers.
	 * @return IDs of selected companies.
	 */
	public int[] getSelected()
	{
		return selectedList;
	}

	/**
	 * Sets the selected item.
	 * @param sel Selected company.
	 */
	public void setSelected(String sel)
	{
		clearSelected();
		int tmp = 0;
		for(int a = 0; a < boxList.length; a++)
		{
			if(boxList[a].getText().equals(sel))
			{
				tmp = query.storeProcedure("getCompanyID", boxList[a].getText());
				boxList[a].setSelected(true);
			}
		}
		selectedList = new int[1];
		selectedList[0] = tmp;
	}
	
	/**
	 * Sets the selected items.
	 * @param sel List of selected companies
	 */
	public void setSelected(String[] sel)
	{
		clearSelected();
		int ctr = 0;
		int[] tmp = new int[boxList.length];
		for(int a = 0; a < boxList.length; a++)
		{
			for(int b = 0; b < sel.length; b++)
			{
				if(boxList[a].getText().equals(sel[b]))
				{
					tmp[ctr] = query.storeProcedure("getCompanyID", boxList[a].getText());
					boxList[a].setSelected(true);
					ctr++;
				}
			}
		}
		selectedList = new int[ctr];
		for(int a = 0; a < selectedList.length; a++)
		{
			selectedList[a] = tmp[a];
		}
	}
	
	/**
	 * Clears out the selected items.
	 */
	public void clearSelected()
	{
		for(int a = 0; a < boxList.length; a++)
		{
			boxList[a].setSelected(false);
		}
		selectedList = null;
	}
	
	/**
	 * Enable or disable the clear button.
	 * @param enable True to enable, False to disable.
	 */
	public void setClearEnable(boolean enable)
	{
		clear.setEnabled(enable);
	}
	
	/**
	 * Make it invisible.
	 */
	private final void invisible()
	{
		this.setVisible(false);
	}
}
