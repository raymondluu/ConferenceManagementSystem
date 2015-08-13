package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import model.User;

/**
 * Conference Menu Bar.
 * 
 * @author Raymond Luu, Alex Yuly
 *
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar
{
	/**
	 * Constructs the app's menu bar.
	 */
	public MenuBar(final JFrame the_parent)
	{
		super();
		
		final JMenu user_menu = new JMenu("User");
		user_menu.setMnemonic(KeyEvent.VK_U);
		user_menu.add(new JMenuItem(new LogoutAction()));
		user_menu.addSeparator();
		user_menu.add(new JMenuItem(new QuitAction()));
		
		final JMenu conf_menu = new JMenu("Conference");
		conf_menu.setMnemonic(KeyEvent.VK_C);
		conf_menu.add(new JMenuItem(new AddPaperAction(the_parent)));
		conf_menu.addSeparator();
		conf_menu.add(new JMenuItem(new ChooseConfAction(the_parent)));
		
		add(user_menu);
		add(conf_menu);
	}

	
	/**
	 * The user wants to log out.
	 * 
	 * @author Alex Yuly
	 */
	private class LogoutAction extends AbstractAction
	{
		public LogoutAction()
		{
			super("Log Out/Switch User...");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
		}
		
		@Override
		public void actionPerformed(final ActionEvent the_action)
		{
			final int reply = JOptionPane.showConfirmDialog(null,
															"Are you sure you want to log out of your current session?",
															AppConstants.APP_NAME,
															JOptionPane.YES_NO_OPTION);
	        if (reply == JOptionPane.YES_OPTION)
	        {
	        	GUI.setUser(new User());
	        	GUI.close();
	        	GUI.setup();
	        }
		}
	}
	
	/**
	 * The user wants to add a new paper to his/her collection.
	 * 
	 * @author Alex Yuly
	 */
	private class AddPaperAction extends AbstractAction
	{
		/**
		 * The main GUI window.
		 */
		private final JFrame my_parent;
		
		/**
		 * @param the_parent The main GUI window.
		 */
		public AddPaperAction(final JFrame the_parent)
		{
			super("Add a New Paper...");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
			my_parent = the_parent;
		}
		
		@Override
		public void actionPerformed(final ActionEvent the_action)
		{
			GUI.switchFocus();
			new PaperDialog(my_parent);
		}
	}
	
	/**
	 * The user wants to choose another conference.
	 * 
	 * @author Alex Yuly
	 */
	private class ChooseConfAction extends AbstractAction
	{
		/**
		 * The main GUI window.
		 */
		private final JFrame my_parent;
		
		/**
		 * @param the_parent The main GUI window.
		 */
		public ChooseConfAction(final JFrame the_parent)
		{
			super("Choose Another Conference...");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
			my_parent = the_parent;
		}
		
		@Override
		public void actionPerformed(final ActionEvent the_action)
		{
			GUI.switchFocus();
			new ConferenceDialog(my_parent, true);
		}
	}
	
}
