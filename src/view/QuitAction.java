package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * The user quits the application.
 * 
 * @author Alex Yuly
 */
@SuppressWarnings("serial")
public class QuitAction extends AbstractAction
{
	public QuitAction()
	{
		super("Quit");
		putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.exit(0);
	}
}