package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import backend.*;

/**
 * Lets user add controller to a collection
 * @author Anthony Schmitt
 *
 */
public class CollectionControllerAdder extends JFrame
{
	private QueryRunner query;
	private int controller_id;
	private String controller_name;
	private JButton add, cancel;
	private JComboBox<String> collectionSelecter;
	private JSpinner amountSelecter;
	private JPanel buttonPanel;
	private String[] collections;
	
	/**
	 * Constructor
	 * @param q The connection to the database
	 * @param cid The id of the controller
	 * @param cname The name of the controller
	 */
	public CollectionControllerAdder(QueryRunner q, int cid, String cname)
	{
		this.query = q;
		this.controller_id = cid;
		this.controller_name = cname;
		init();
	}

	/**
	 * Sets up the window
	 */
	private void init()
	{
		this.setTitle("Add " + controller_name + " to a Collection");
		collections = query.querySingleListReturnColumn(Queries.COLLECTIONS);
		collectionSelecter = new JComboBox<String>(collections);
		JPanel inerPanel = new JPanel();
		SpinnerNumberModel quantitySpinModel = new SpinnerNumberModel(1, 1, 100, 1);
		amountSelecter = new JSpinner(quantitySpinModel);
		JLabel amountLabel = new JLabel("Number of Controllers");
		inerPanel.setLayout(new BorderLayout());
		inerPanel.add(amountLabel, BorderLayout.NORTH);
		inerPanel.add(amountSelecter, BorderLayout.CENTER);
		this.setLayout(new BorderLayout());
		this.add(inerPanel, BorderLayout.CENTER);
		this.add(collectionSelecter, BorderLayout.NORTH);
		setUpButtons();
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setSize(500,500);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	/**
	 * Sets up the buttons
	 */
	private void setUpButtons()
	{
		add = new JButton("Add to Collection");
		add.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				String col = collectionSelecter.getSelectedItem().toString();
				boolean in = query.storeProcedure("inCollectionController", controller_id, col); 
				
				if(!in)
				{
					query.addControllerToCollection(controller_id, col, Integer.parseInt(amountSelecter.getValue().toString()));
					JOptionPane.showMessageDialog(null,controller_name + " had been added to collection " + col,"Game Added to Collection",JOptionPane.INFORMATION_MESSAGE);
					clearFields();
					invisible();
				}
				else
				{
					JOptionPane.showMessageDialog(null,controller_name + " already exists in collection " + col,"Already in Collection",JOptionPane.WARNING_MESSAGE);
				}
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
		buttonPanel.add(add);
		buttonPanel.add(cancel);
	}
	
	/**
	 * Clears the input fields and updates the title
	 */
	private void update()
	{
		clearFields();
		this.setTitle("Add " + controller_name + " to a Collection");
	}
	
	/**
	 * Makes the window invisible.
	 */
	private void invisible()
	{
		this.setVisible(false);
	}
	
	/**
	 * Resets the data to its defaults
	 */
	private void clearFields()
	{
		amountSelecter.setValue(1);
		collectionSelecter.setSelectedIndex(0);
	}
	
	/**
	 * Changes the controller.
	 * @param cid The controller ID
	 * @param cname The controller Name
	 */
	public void setController(int cid, String cname)
	{
		this.controller_id = cid;
		this.controller_name = cname;
		update();
	}
	
}
