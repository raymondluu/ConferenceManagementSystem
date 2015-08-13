package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import model.Conference;
import model.User;

/**
 * GUI utility class.
 * There is only one GUI, so all methods are static.
 * 
 * @author Raymond Luu, Alex Yuly
 */
public final class GUI 
{
	/**
	 * The main window.
	 */
	private static JFrame frame;
	
	/**
	 * The main display panel.
	 */
	private static MainPanel main_panel = new MainPanel();
	
	/**
	 * The currently logged-in user.
	 */
	private static User user = new User();
	
	
	/**
	 * Sets up and displays the main window.
	 */
	public static void setup() 
	{
		// Initialize main window and its components.
		frame = new JFrame(AppConstants.APP_NAME);
		frame.setJMenuBar(new MenuBar(frame));
		main_panel = new MainPanel();
		
		// Prepare and add panels.
		main_panel.setPreferredSize(new Dimension(1060, 640));
		frame.add(main_panel, BorderLayout.CENTER);

		// Prepare main window for display, and then show Log In/Register dialog.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1120, 700));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(false);
		new UserDialog(frame);
	}
	
	/**
	 * Closes the main window.
	 */
	public static void close()
	{
		frame.dispose();
	}
	
	/**
	 * Makes visible or invisible the main window, according the opposite of its current state.
	 */
	public static void switchFocus()
	{
		if (frame.isVisible())
		{
			frame.setVisible(false);
		}
		else
		{
			frame.setVisible(true);
		}
	}
	
	/**
	 * Sets the current user.
	 * @param the_user The user to be set.
	 */
	public static void setUser(final User the_user)
	{
		user = the_user;
	}
	
	/**
	 * @return The current user.
	 */
	public static User user()
	{
		return user;
	}
	
	public static void updateMainPanel(final Conference the_conf)
	{
		main_panel.setMainWindow(the_conf);
	}

	public static void refreshPapers()
	{
		main_panel.displayMyPapers();
	}
}
