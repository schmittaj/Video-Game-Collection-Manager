package frontend;

import java.awt.*;
import java.awt.event.*;
import backend.*;
import javax.swing.*;

/**
 * Shows more specific information about a controller in the collection
 * Allows editing and remove from collection
 * @author Anthony Schmitt
 *
 */
public class CollectionControllerView extends JFrame implements ActionListener
{
	private QueryRunner q;
	private int controller_id, quantity, orgQuantity;
	private String console, collection, controller;
	private JTextField controllerField, consoleField;
	private JPanel infoPanel, buttonPanel;
	private JSpinner quantitySpin;
	private JButton apply, cancel, edit, remove;
	private CollectionControllerListScreen ccv;
	
	/**
	 * Constructor.
	 * @param q QueryRunner
	 * @param cg The calling ListScreen
	 */
	public CollectionControllerView(QueryRunner q, CollectionControllerListScreen cg)
	{
		super("Collection Controller View");
		this.q = q;
		this.controller_id = 1;
		this.ccv = cg;
		init();
	}
	
	/**
	 * Sets the console that is viewed.
	 * @param id The console's id number.
	 * @param col The name of the console.
	 */
	public void setConsole(int id, String col)
	{
		this.controller_id = id;
		this.collection = col;
		updateInfo();
	}

	/**
	 * Updates the information in the view.
	 */
	private void updateInfo()
	{
		String query = "Select Controller.name, quantity, Console.name from Console, Controller_In_Collection, Controller where Console.id = Controller.console_id AND Controller_In_Collection.controller_id = Controller.id AND Controller_In_Collection.collection_name = " + collection + " AND Controller_In_Collection.controller_id = "+controller_id+";";
		String[] tmp = q.querySingleListReturnRow(query);
		this.controller = tmp[0];
		this.quantity = Integer.parseInt(tmp[1]);
		this.console = tmp[2];
		this.orgQuantity = quantity;
		controllerField.setText(controller);
		consoleField.setText(console);
		quantitySpin.setValue(quantity);
	}

	/**
	 * Does the initial set up for the object.
	 */
	private void init()
	{
		setUpButtons();
		setUpView();
		this.setLayout(new BorderLayout());
		this.add(infoPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2)-400;
		int width = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2)-400;
		this.setLocation(width, height);
		this.setSize(300, 400);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e)
			{
				edit.setVisible(true);
				remove.setEnabled(false);
				apply.setVisible(false);
				cancel.setVisible(false);
				edit.setEnabled(true);
				invisible();
			}
		});
	}

	/**
	 * Does the set up for the buttons.
	 */
	private void setUpButtons()
	{
		remove = new JButton("Remove from Collection");
		remove.addActionListener(this);
		remove.setEnabled(false);
		edit = new JButton("Edit Info");
		edit.addActionListener(this);
		apply = new JButton("Apply Changes");
		apply.setVisible(false);
		apply.addActionListener(this);
		cancel = new JButton("Cancel Changes");
		cancel.setVisible(false);
		cancel.addActionListener(this);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4,1));
		buttonPanel.add(edit);
		buttonPanel.add(remove);
		buttonPanel.add(apply);
		buttonPanel.add(cancel);
	}


	/**
	 * Sets up the rest of the components
	 */
	private void setUpView()
	{
		JLabel controllerLabel = new JLabel("Controller Name:");
		controllerField = new JTextField();
		JPanel controllerPanel = new JPanel();
		controllerPanel.setLayout(new GridLayout(1,2));
		controllerPanel.add(controllerLabel);
		controllerPanel.add(controllerField);
		JLabel consoleLabel = new JLabel("Console:");
		consoleField = new JTextField();
		JPanel consolePanel = new JPanel();
		consolePanel.setLayout(new GridLayout(1,2));
		consolePanel.add(consoleLabel);
		consolePanel.add(consoleField);
		SpinnerNumberModel quantitySpinModel = new SpinnerNumberModel(1, 1, 100, 1);
		quantitySpin = new JSpinner(quantitySpinModel);
		JLabel quantityLabel = new JLabel("Quantity");
		JPanel quantityPanel = new JPanel();
		quantityPanel.setLayout(new GridLayout(1,2));
		quantityPanel.add(quantityLabel);
		quantityPanel.add(quantitySpin);
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(3,1));
		infoPanel.add(controllerPanel);
		infoPanel.add(consolePanel);
		infoPanel.add(quantityPanel);
		controllerField.setEditable(false);
		consoleField.setEditable(false);
		quantitySpin.setEnabled(false);
	}

	/**
	 * Makes view invisible.
	 */
	private final void invisible()
	{
		this.setVisible(false);
	}

	/**
	 * Handles the buttons.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Edit Info"))
		{
			edit.setEnabled(false);
			apply.setVisible(true);
			cancel.setVisible(true);
			remove.setEnabled(true);
			quantitySpin.setEnabled(true);
		}
		if(e.getActionCommand().equals("Apply Changes"))
		{
			edit.setVisible(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			remove.setEnabled(false);
			quantitySpin.setEnabled(false);
			databaseUpdate();
			updateInfo();
			ccv.updateList();
		}
		if(e.getActionCommand().equals("Cancel Changes"))
		{
			edit.setVisible(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			remove.setEnabled(false);
			quantitySpin.setEnabled(false);
			updateInfo();
		}
		if(e.getActionCommand().equals("Remove from Collection"))
		{
			q.storeProcedureJustIn("removeControllerFromCollection", controller_id, collection.substring(1, collection.length()-1));
			JOptionPane.showMessageDialog(null,controller + " had been removed from collection " + collection+".","Game Removed",JOptionPane.INFORMATION_MESSAGE);
			edit.setVisible(true);
			apply.setVisible(false);
			cancel.setVisible(false);
			edit.setEnabled(true);
			remove.setEnabled(false);
			quantitySpin.setEnabled(false);
			ccv.updateList();
			invisible();
		}
	}
	
	/**
	 * Updates the database.
	 */
	private void databaseUpdate()
	{
		String tmp = collection.substring(1, collection.length()-1);
		quantity = Integer.parseInt(quantitySpin.getValue().toString());
		if(orgQuantity != quantity)
		{
			q.storeProcedureJustIn("updateControllerQuantityCollection", controller_id, tmp, quantity);
		}
		
	}



	
}