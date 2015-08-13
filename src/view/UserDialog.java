package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.User;

import database.DataAccess;

/**
 * Window allowing a user to log in or register a new account.
 * 
 * @author Alex Yuly
 */
@SuppressWarnings("serial")
public class UserDialog extends JFrame
{
	/**
	 * The input field for the user's Full Name.
	 */
	private final JTextField my_input_name = new JTextField();
	
	/**
	 * The input field for the user's Password.
	 */
	private final JTextField my_input_pass = new JPasswordField();
	
	
	/**
	 * Initializes and displays the window.
	 * @param the_parent The main GUI window.
	 */
	public UserDialog(final JFrame the_parent)
	{
		// Initialize main frame and panel.
		super(AppConstants.APP_NAME);
		final JPanel main_panel = new JPanel(new GridLayout(6, 1));
		main_panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// Prepare labels and input fields.
		main_panel.add(new JLabel("<html>Welcome to " + AppConstants.APP_NAME + "! Please enter your full name and password and click Log In, or click Register if this is your first visit.</html>"));
		main_panel.add(new JLabel("Full name:"));
		main_panel.add(my_input_name);
		main_panel.add(new JLabel("Password:"));
		main_panel.add(my_input_pass);
		
		// Prepare and add Log In, Register, and Quit buttons.
		final JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		button_panel.add(new JButton(new LogInAction(the_parent)));
		button_panel.add(new JButton(new RegisterAction(the_parent)));
		button_panel.add(new JButton(new QuitAction()));
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
	 * The user attempts to log in to an existing account.
	 * 
	 * @author Alex Yuly
	 */
	private class LogInAction extends AbstractAction
	{
		/**
		 * The main GUI window.
		 */
		private final JFrame my_parent;
		
		/**
		 * @param the_parent The main GUI window.
		 */
		public LogInAction(final JFrame the_parent)
		{
			super("Log In");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
			my_parent = the_parent;
		}
		
		@Override
		public void actionPerformed(final ActionEvent the_action)
		{
			final User user = DataAccess.loadUser(my_input_name.getText(), my_input_pass.getText());
			
			// NO, log in was NOT successful.
			if (user == null)
			{
				JOptionPane.showMessageDialog(UserDialog.this,
											  "<html>Your name or password is incorrect. Please try again.</html>",
											  AppConstants.APP_NAME,
											  JOptionPane.WARNING_MESSAGE);
			}
			// YES, log in WAS successful.
			else
			{
				GUI.setUser(user);
				MainPanel.updateUserLabel();
				UserDialog.this.dispose();
				new ConferenceDialog(my_parent, false);
			}
		}
	}
	
	/**
	 * The user attempts to register a new account.
	 * 
	 * @author Alex Yuly
	 */
	private class RegisterAction extends AbstractAction
	{
		/**
		 * The main GUI window.
		 */
		private final JFrame my_parent;
		
		/**
		 * @param the_parent The main GUI window.
		 */
		public RegisterAction(final JFrame the_parent)
		{
			super("Register");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R);
			my_parent = the_parent;
		}
		
		@Override
		public void actionPerformed(final ActionEvent the_action)
		{
			if (DataAccess.hasUser(my_input_name.getText()))
			{
				JOptionPane.showMessageDialog(UserDialog.this,
						  "<html>A user already exists with the specified name. Please try again.</html>",
						  AppConstants.APP_NAME,
						  JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				String email = JOptionPane.showInputDialog(UserDialog.this,
										"<html>Please enter your email address.</html>",
										AppConstants.APP_NAME,
										JOptionPane.QUESTION_MESSAGE);
				
				if (email != null)
				{
					DataAccess.createNewUser(my_input_name.getText(), my_input_pass.getText(), email);
					final User user = DataAccess.loadUser(my_input_name.getText(), my_input_pass.getText());
					
					GUI.setUser(user);
					MainPanel.updateUserLabel();
					UserDialog.this.dispose();
					new ConferenceDialog(my_parent, false);
				}
			}
		}
	}

}
