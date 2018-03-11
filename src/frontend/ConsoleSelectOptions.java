package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * Allows for the selection of consoles
 * @author Anthony Schmitt
 *
 */
public class ConsoleSelectOptions extends JFrame
{
	private JCheckBox[] boxList;
	private String[] consoles;
	private QueryRunner query;
	private int cols = 3;
	private JButton clear, apply;
	private int[] selectedList;
	private ConsoleSelectable cs;
	private boolean multiSelect;
	private ButtonGroup group;
	
	/**
	 * Constructor.
	 * @param q Query Runner
	 * @param g Referring Console Selectable
	 * @param multi Allow multiple selection
	 */
	public ConsoleSelectOptions(QueryRunner q, ConsoleSelectable g, boolean multi)
	{
		super("Filter by Console");
		this.query = q;
		this.cs = g;
		this.multiSelect = multi;
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
		consoles = query.querySingleListReturnColumn(Queries.CONSOLES);
		boxList = new JCheckBox[consoles.length];
		int rows = boxList.length/cols;
		if(boxList.length%cols != 0)
		{
			rows += 1;
		}
		optionsPanel.setLayout(new GridLayout(rows,cols));
		for(int a = 0; a < boxList.length; a++)
		{
			boxList[a] = new JCheckBox(consoles[a]);
			optionsPanel.add(boxList[a]);
		}
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
						tmp[ctr] = query.storeProcedure("getConsoleID", boxList[a].getText());
						ctr++;
					}
				}
				selectedList = new int[ctr];
				for(int a = 0; a < selectedList.length; a++)
				{
					selectedList[a] = tmp[a];
				}
				invisible();
				cs.haveGrabSelectedConsoleList();
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
				cs.haveGrabSelectedConsoleList();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		buttonPanel.add(apply);
		buttonPanel.add(clear);
		this.setLayout(new BorderLayout());
		this.add(optionsPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-200;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-200;
		this.setLocation(width, height);
		if(!multiSelect)
		{
			group = new ButtonGroup();
			for(int a = 0; a < boxList.length; a++)
			{
				group.add(boxList[a]);
			}
		}
	}
	
	/**
	 * Returns the list of selected items.
	 * @return List of selected items.
	 */
	public int[] getSelected()
	{
		return selectedList;
	}

	/**
	 * Sets the selected item.
	 * @param selected Selected Item.
	 */
	public void setSelected(String selected)
	{
		clearSelected();
		int tmp = 0;
		for(int a = 0; a < boxList.length; a++)
		{
			if(boxList[a].getText().equals(selected))
			{
				tmp = query.storeProcedure("getConsoleID", boxList[a].getText());
				boxList[a].setSelected(true);
			}
		}
		selectedList = new int[1];
		selectedList[0] = tmp;
	}
	
	/**
	 * Sets the selected items.
	 * @param selected List of selected items.
	 */
	public void setSelected(String[] selected)
	{
		clearSelected();
		int ctr = 0;
		int tmp[] = new int[boxList.length];
		for(int a = 0; a < boxList.length; a++)
		{
			for(int b = 0; b < selected.length; b++)
			{
				if(boxList[a].getText().equals(selected[b]))
				{
					tmp[a] = query.storeProcedure("getConsoleID", boxList[a].getText());
					boxList[a].setSelected(true);
					ctr++;
				}
			}
		}
		selectedList = new int[ctr];
		for(int a = 0; a < ctr; a++)
		{
			selectedList[a] = tmp[a];
		}
	}
	
	/**
	 * Clears the selections.
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
	 * Enables or disables the clear button.
	 * @param enable True for enable, False for disable
	 */
	public void setClearButtonEnable(boolean enable)
	{
		if(enable)
		{
			clear.setEnabled(true);
		}
		else
		{
			clear.setEnabled(false);
		}
	}
	
	/**
	 * Makes invisible.
	 */
	private final void invisible()
	{
		this.setVisible(false);
	}
}
