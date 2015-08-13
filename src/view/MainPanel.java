package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import database.DataAccess;

import model.Conference;
import model.Paper;
import model.PaperSubmission;
import model.Review;
import model.Status;
import model.User;

/**
 * Main Window Panel.
 * 
 * @author Raymond Luu, Caleb DelVillar-Fox, Alex Yuly
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel
{
	/**
	 * Main info Panel.
	 */
	private JPanel my_main_info_panel;
	
	private JPanel my_manuscripts_PC_display;
	private JPanel my_papers_display;
	private JPanel my_action_buttons;
	private JPanel my_review_panel;
	
	/**
	 * Conference label.
	 */
	private JLabel my_conference_label;
	
	/**
	 * Current submissions label.
	 */
	private JLabel my_current_subs_label;
	
	/**
	 * The label displaying the currently logged-in user.
	 */
	private final static JLabel user_label = new JLabel();
	
	/**
	 * Constructor for Main Window Panel.
	 */
	public MainPanel()
	{
		super();
		
		setLayout(new BorderLayout());
		
		// Set up Panels
		my_main_info_panel = new JPanel(new BorderLayout());
		my_manuscripts_PC_display = new JPanel();
		my_papers_display = new JPanel();
		my_review_panel = new JPanel();
		
		// Set up Labels
		my_conference_label = new JLabel();
		my_current_subs_label = new JLabel();
		
		// Add Labels
		my_main_info_panel.add(user_label, BorderLayout.NORTH);
		my_main_info_panel.add(my_conference_label, BorderLayout.SOUTH);
		my_main_info_panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		my_main_info_panel.setBackground(Color.WHITE);
		
		// Add Panels to Main Window Panel
		add(my_main_info_panel, BorderLayout.NORTH);
		add(my_manuscripts_PC_display, BorderLayout.CENTER);
		add(my_papers_display, BorderLayout.CENTER);
		add(my_review_panel, BorderLayout.CENTER);
	}
	
	public void displayManuscriptsPC()
	{
		clearMainWindow();
		my_manuscripts_PC_display = new JPanel();
		my_manuscripts_PC_display.setVisible(true);
		my_manuscripts_PC_display.setLayout(new BoxLayout(my_manuscripts_PC_display, BoxLayout.PAGE_AXIS));
		Conference conf = GUI.user().getCurrentConference();
		Iterator<PaperSubmission> papers = conf.paperSubmissions();
		while (papers.hasNext())
		{
			PaperSubmission currentsub = papers.next();
			JPanel submissionPanel = getSubmissionPanel(currentsub);
			my_manuscripts_PC_display.add(submissionPanel);
		}
		add(my_manuscripts_PC_display, BorderLayout.CENTER);
	}
	
	private JPanel getSubmissionPanel(final PaperSubmission the_sub)
	{
		JPanel result = new JPanel();
		final String info = the_sub.toString();
		final JLabel paperlabel = new JLabel(info);
		final JButton accept = new JButton("Accept");
		final JButton reject = new JButton("Reject");
		JButton read = new JButton("Read");
		accept.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(final ActionEvent the_event)
				{
					the_sub.setStatus(Status.YES);
					accept.setEnabled(false);
					reject.setEnabled(true);
					paperlabel.setText(the_sub.toString());
				}
			}
		);
		reject.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(final ActionEvent the_event)
				{
					the_sub.setStatus(Status.NO);
					reject.setEnabled(false);
					accept.setEnabled(true);
					paperlabel.setText(the_sub.toString());
				}
			}
		);
		result.add(paperlabel);
		result.add(accept);
		result.add(reject);
		result.add(read);
		return result;
	}
	
	//Creates the buttons for any possible action this user can perform
	private void loadActionButtons()
	{
		my_action_buttons = new JPanel();
		my_action_buttons.setVisible(true);
		add(my_action_buttons, BorderLayout.SOUTH);
		
		GUI.user().getID();
		Set<String> actions = GUI.user().getAllActions();
		my_action_buttons.add(getViewMyPapersButton());
		if (actions.contains("viewSubmissions")) {
			JButton viewSubs = new JButton("View Submissions");
			viewSubs.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(final ActionEvent the_event)
					{
						displayManuscriptsPC();
					}
				}
			);
			my_action_buttons.add(viewSubs);
		}
		if (actions.contains("submitReview"))
		{
			JButton viewtoReview = getToReviewButton();
			my_action_buttons.add(viewtoReview);
		}
		add(my_action_buttons, BorderLayout.SOUTH);
	}
	
	private JButton getViewMyPapersButton()
	{
		JButton viewPapers = new JButton("View my papers");
		viewPapers.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(final ActionEvent the_event)
				{
					displayMyPapers();
				}
			}
		);
		return viewPapers;
	}
	
	public void displayMyPapers()
	{
		clearMainWindow();
		Set<Paper> papers = GUI.user().getPapers();
		my_papers_display = new JPanel(new GridLayout(papers.size() + 1, 1));
		my_papers_display.setVisible(true);
		Map<String, String> confs = DataAccess.findConferences();
		final JComboBox conferenceSelect = new JComboBox();
		final JPanel conf_sel_panel = new JPanel(new FlowLayout());
		conf_sel_panel.add(new JLabel("Papers will be submitted to the following conference:"));
		conf_sel_panel.add(conferenceSelect);
		my_papers_display.add(conf_sel_panel);
		for (String name : confs.keySet())
		{
			conferenceSelect.addItem(name);
		}
		
		for (final Paper currentpap : papers)
		{
			JPanel paperpanel = new JPanel();
			paperpanel.add(new JLabel(currentpap.toString()));
			JButton submit = new JButton("Submit");
			JButton viewRevs = new JButton("View Reviews");
			
			submit.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(final ActionEvent the_event)
					{
						//action for submitting the paper to a specific conference
						String selectedname = (String) conferenceSelect.getSelectedItem();	
						String confID = DataAccess.findConferences().get(selectedname);
						GUI.user().submitPaper(currentpap, confID);
						for (PaperSubmission pap: GUI.user().getCurrentConference().getSubmissions())
						{
							System.out.println(pap.toString());
						}
					}
				}
			);
			
			paperpanel.add(submit);
			paperpanel.add(viewRevs);
			my_papers_display.add(paperpanel);
		}
		add(my_papers_display);
	}
	
	private JButton getToReviewButton()
	{
		JButton toReview = new JButton("Papers to Review");
		toReview.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(final ActionEvent the_event)
				{
					displayReviewPanel();
				}
			}
		);
		return toReview;
	}
	
	private void displayReviewPanel()
	{
		clearMainWindow();
		my_review_panel = new JPanel();
		my_review_panel.setVisible(true);
		Set<PaperSubmission> subs = GUI.user().getCurrentConference().getSubmissions();
		Set<PaperSubmission> mySubs = new HashSet<PaperSubmission>();
		for (PaperSubmission csub : subs)
		{
			csub.addReviewer(GUI.user());
		
			Set<User> reviewers = csub.getReviewers();
			for (User reviewer: reviewers)
			{
				if (reviewer.getID() == GUI.user().getID())
				{
					mySubs.add(csub);
				}
			}
		}
		for (final PaperSubmission submis : mySubs)
		{
			JPanel substuff = new JPanel();
			substuff.add(new JLabel(submis.toString() + " Avg Rating: " + submis.getAverageRating()));
			JButton submitReview = new JButton("Submit Review");
			//String[] ratings = {"0", "1", "2", "3", "4"};
			final JComboBox rating = new JComboBox();
			rating.addItem("0");
			rating.addItem("1");
			rating.addItem("2");
			rating.addItem("3");
			rating.addItem("4");
			substuff.add(rating);
			submitReview.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(final ActionEvent the_event)
					{
						int reviewscore = rating.getSelectedIndex();
						System.out.println(reviewscore);
						Review currentReview = new Review(GUI.user(), reviewscore);
						submis.addReview(GUI.user(), currentReview);
						displayReviewPanel();
					}
				}
			);
			substuff.add(submitReview);
			my_review_panel.add(substuff);
		}
		add(my_review_panel, BorderLayout.CENTER);
	}
	
	public void clearMainWindow()
	{
		for (Component comp : getComponents())
		{
			if (!comp.equals(my_action_buttons) && !comp.equals(my_main_info_panel) &&
					!comp.equals(my_conference_label) && !comp.equals(user_label))
			{
				comp.setVisible(false);
			}
		}
	}
	
	public void setMainWindow(final Conference the_conf)
	{
		my_conference_label.setText("You are now in " + the_conf);
		my_current_subs_label.setText("These are the current submissions to the conference:");
		
		displayManuscriptsPC();
		loadActionButtons();
	}
	
	public static void updateUserLabel()
	{
		user_label.setText("Welcome, " + GUI.user().getName());
	}
}
