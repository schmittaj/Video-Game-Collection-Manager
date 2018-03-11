package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * Allows for selecting of genres for filtering
 * @author Anthony Schmitt
 *
 */
public class GenreSelectOptions extends JFrame
{
	private JCheckBox[] boxList;
	private String[] genres;
	private QueryRunner query;
	private int cols = 4;
	private JButton clear, apply;
	private int[] selectedList;
	private GenreSelectable game;
	private JScrollPane scroller;
	
	/**
	 * Constructor
	 * @param q Query Runner
	 * @param g Referring GenreSelectable
	 */
	public GenreSelectOptions(QueryRunner q, GenreSelectable g)
	{
		super("Filter by Genre");
		this.query = q;
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
		JPanel optionsPanel = new JPanel();
		genres = query.querySingleListReturnColumn(Queries.GENRES);
		boxList = new JCheckBox[genres.length];
		int rows = boxList.length/cols;
		if(boxList.length%cols != 0)
		{
			rows += 1;
		}
		optionsPanel.setLayout(new GridLayout(rows,cols));
		for(int a = 0; a < boxList.length; a++)
		{
			boxList[a] = new JCheckBox(genres[a]);
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
						tmp[ctr] = query.storeProcedure("getGenreID", boxList[a].getText());
						ctr++;
					}
				}
				selectedList = new int[ctr];
				for(int a = 0; a < selectedList.length; a++)
				{
					selectedList[a] = tmp[a];
				}
				invisible();
				game.haveGrabSelectedGenreList();
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
				game.haveGrabSelectedGenreList();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,1));
		buttonPanel.add(apply);
		buttonPanel.add(clear);
		this.setLayout(new BorderLayout());
		//this.add(optionsPanel, BorderLayout.CENTER);
		this.add(scroller, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-200;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-200;
		this.setLocation(width, height);
	}
	
	/**
	 * Returns the selected items.
	 * @return List of selected items.
	 */
	public int[] getSelected()
	{
		return selectedList;
	}

	/**
	 * Set an item as selected
	 * @param sel Item to be selected.
	 */
	public void setSelected(String sel)
	{
		clearSelected();
		int tmp = 0;
		for(int a = 0; a < boxList.length; a++)
		{
			if(boxList[a].getText().equals(sel))
			{
				tmp = query.storeProcedure("getGenreID", boxList[a].getText());
				boxList[a].setSelected(true);
			}
		}
		selectedList = new int[1];
		selectedList[0] = tmp;
	}
	
	/**
	 * Sets selected items.
	 * @param sel List of items to select.
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
					tmp[ctr] = query.storeProcedure("getGenreID", boxList[a].getText());
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
	 * Clears the selected items.
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
	 * @param enable True for enable, False for disable.
	 */
	public void setClearEnable(boolean enable)
	{
		clear.setEnabled(enable);
	}
	
	/**
	 * Makes this window invisible.
	 */
	private final void invisible()
	{
		this.setVisible(false);
	}
}