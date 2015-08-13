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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.User;

import database.DataAccess;

/**
 * Window allowing a user to add a new paper.
 * 
 * @author Alex Yuly
 */
@SuppressWarnings("serial")
public class PaperDialog extends JFrame
{
	/**
	 * The input field for the paper's title.
	 */
	private final JTextField my_input_title = new JTextField();
	
	/**
	 * The input field for the paper's keywords.
	 */
	private final JTextField my_input_keywords = new JTextField();
	
	/**
	 * The input field for the paper's categories.
	 */
	private final JTextField my_input_categories = new JTextField();
	
	/**
	 * The input field for the paper's abstract.
	 */
	private final JTextField my_input_abstract = new JTextField();
	
	/**
	 * The input field for the paper's main text content.
	 */
	private final JTextArea my_input_text = new JTextArea(20, 20);
	
	
	/**
	 * Initializes and displays the window.
	 * @param the_parent The main GUI window.
	 */
	public PaperDialog(final JFrame the_parent)
	{
		// Initialize main frame and panel.
		super(AppConstants.APP_NAME + " - add a new paper");
		final JPanel main_panel = new JPanel(new GridLayout(11, 1));
		main_panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		
		// Prepare labels and input fields.
		main_panel.add(new JLabel("Title:"));
		main_panel.add(my_input_title);
		main_panel.add(new JLabel("Keywords (comma-separated):"));
		main_panel.add(my_input_keywords);
		main_panel.add(new JLabel("Categories (comma-separated):"));
		main_panel.add(my_input_categories);
		main_panel.add(new JLabel("Abstract:"));
		main_panel.add(my_input_abstract);
		main_panel.add(new JLabel("Text:"));
		main_panel.add(my_input_text);
		
		// Prepare and add Add and Cancel buttons.
		final JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		button_panel.add(new JButton(new AddAction()));
		button_panel.add(new JButton(new CancelAction()));
		main_panel.add(button_panel);
		
		// Prepare window for display and show it.
		add(main_panel);
		setPreferredSize(new Dimension(450, 500));
		setResizable(false);
		pack();
		setLocationRelativeTo(the_parent);
		setVisible(true);
	}
	
	/**
	 * The user attempts to add a new paper to his/her collection.
	 * 
	 * @author Alex Yuly
	 */
	private class AddAction extends AbstractAction
	{
		public AddAction()
		{
			super("Add");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
		}
		
		@Override
		public void actionPerformed(final ActionEvent the_action)
		{
			final int next_paper_id = DataAccess.nextPaperID(my_input_title.getText());
			if (next_paper_id == 0)
			{
				JOptionPane.showMessageDialog(PaperDialog.this,
						  "<html>A paper already exists with the specified name. Please try again.</html>",
						  AppConstants.APP_NAME,
						  JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				System.out.println(next_paper_id);
				DataAccess.createNewPaper(next_paper_id,
							  			  my_input_title.getText(),
							  			  my_input_keywords.getText(),
							  			  my_input_categories.getText(),
							  			  my_input_abstract.getText(),
							  			  my_input_text.getText());
				
				PaperDialog.this.dispose();
				GUI.switchFocus();
			}
		}
	}
	
	/**
	 * The user clicks Cancel, which returns him/her to the main GUI window.
	 * 
	 * @author Alex Yuly
	 */
	private class CancelAction extends AbstractAction
	{
		public CancelAction()
		{
			super("Cancel");
			putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
		}
		
		@Override
		public void actionPerformed(final ActionEvent e)
		{
			GUI.switchFocus();
			PaperDialog.this.dispose();
		}
	}
}
