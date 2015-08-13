package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import model.Conference;
import model.User;

import database.DataAccess;

/**
 * Window allowing a user to select a conference to view/modify.
 * 
 * @author Alex Yuly
 */
@SuppressWarnings("serial")
public class ConferenceDialog extends JFrame
{
	/**
	 * The input field for the selected conference (as a drop-down list).
	 */
	private JComboBox my_input_conf;
	
	/**
	 * Initializes and displays the window.
	 * @param the_parent The main GUI window.
	 */
	public ConferenceDialog(final JFrame the_parent, final boolean return_to_parent)
	{
		// Initialize main frame and panel.
		super(AppConstants.APP_NAME);
		final JPanel main_panel = new JPanel(new GridLayout(3, 1));
		main_panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// Prepare label and input field (drop-down menu).
		main_panel.add(new JLabel("Please select the conference you'd like to view."));
		Map<String, String> conferences = DataAccess.findConferences();
		Set<String> names = conferences.keySet();
		my_input_conf = new JComboBox(names.toArray());
		main_panel.add(my_input_conf);
		
		// Prepare and add OK and Cancel buttons.
		final JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		button_panel.add(new JButton(new OKAction()));
		button_panel.add(new JButton(new CancelAction(the_parent, return_to_parent)));
		main_panel.add(button_panel);
		
		// Prepare window for display and show it.
		add(main_panel);
		setPreferredSize(new Dimension(450, 300));
		setResizable(false);
		pack();
		setLocationRelativeTo(the_parent);
		setVisible(true);
	}
	
	/**
	 * The user clicks OK after choosing an existing conference from the list.
	 * 
	 * @author Alex Yuly
	 */
	private class OKAction extends AbstractAction
	{
		public OKAction()
		{
			super("OK");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		}
		
		@Override
		public void actionPerformed(final ActionEvent the_action)
		{
			final int selected_conf = my_input_conf.getSelectedIndex();
			
			// NO conference was selected.
			if (selected_conf == -1)
			{
				JOptionPane.showMessageDialog(ConferenceDialog.this,
											  "<html>You must select a conference from the list. Please try again.</html>",
											  AppConstants.APP_NAME,
											  JOptionPane.WARNING_MESSAGE);
			}
			// YES, a conference WAS selected.
			else
			{
				final String conf_id = "C" + (selected_conf + 1);
				final String conf_name = (String) my_input_conf.getSelectedItem();
				Conference current_conf = DataAccess.loadConference(conf_id);
				current_conf.setName(conf_name);
				GUI.user().setConference(conf_id, conf_name);
				GUI.updateMainPanel(current_conf);
				
				GUI.switchFocus();
				ConferenceDialog.this.dispose();
			}
		}
	}
	
	/**
	 * The user clicks Cancel, which returns him/her to the log in/register dialog.
	 * 
	 * @author Alex Yuly
	 */
	private class CancelAction extends AbstractAction
	{
		/**
		 * The main GUI window.
		 */
		private final JFrame my_parent;
		
		/**
		 * True if the dialog returns to its parent dialog when canceled.
		 */
		private final boolean my_return_to_parent;
		
		/**
		 * @param the_parent The main GUI window.
		 * @param return_to_parent True if the dialog returns to its parent dialog when canceled.
		 */
		public CancelAction(final JFrame the_parent, final boolean return_to_parent)
		{
			super("Cancel");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
			my_parent = the_parent;
			my_return_to_parent = return_to_parent;
		}
		
		@Override
		public void actionPerformed(final ActionEvent the_action)
		{
			// If we were previously at the main window, return there.
			if (my_return_to_parent)
			{
				GUI.switchFocus();
			}
			// Otherwise, return to log in/register dialog.
			else
			{
				new UserDialog(my_parent);
			}
			
			ConferenceDialog.this.dispose();
		}
	}

}
