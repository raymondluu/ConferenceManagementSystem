package model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Represents a single conference.
 * 
 * @author Alex Yuly
 */
public class Conference
{	
	/* INSTANCE FIELDS */
	
	/**
	 * A map of each Role to all associated Users.
	 */
	private Map<Role, Set<User>> my_roles = new HashMap<Role, Set<User>>(); //changed to test the code
	
	/**
	 * A set of all submissions in the conference.
	 */
	private Set<PaperSubmission> my_submissions = new HashSet<PaperSubmission>();
	
	/* Deadlines*/
	private Date my_deadline_submission;
	private Date my_deadline_review;
	private Date my_deadline_decision;
	private String my_name;
	
	
	/* METHODS */
	
	public Conference(final Date the_deadline_submission,
					  final Date the_deadline_review,
					  final Date the_deadline_decision)
	{
		// Initialize each Role with an empty Set of Users.
		for (Role r : Role.values())
		{
			my_roles.put(r, new HashSet<User>());
		}
		
		setDeadlineSubmission(the_deadline_submission);
		my_deadline_review = the_deadline_review;
		my_deadline_decision = the_deadline_decision;
		
	}
	
	
	/* PAPERSUBMISSION METHODS */
	
	/**
	 * @return an iterator over the conference's submissions, so as not to violate encapsulation.
	 */
	public Iterator<PaperSubmission> paperSubmissions()
	{
		return my_submissions.iterator();
	}
	
	/**
	 * Adds the_submission to the submissions contained by this conference.
	 * @param the_submission The submission to be added.
	 */
	public void addSubmission(final PaperSubmission the_submission)
	{
		my_submissions.add(the_submission);
	}
	
	/**
	 * Removes the_submission from the submissions contained by the conference.
	 * @param the_submission The submission to be removed.
	 * @return true if the conference contained the submission and it was removed.
	 */
	public boolean removeSubmission(final PaperSubmission the_submission)
	{
		return my_submissions.remove(the_submission);
	}
	
	
	/* USER METHODS */
	
	/**
	 * Assigns a User to a Role.
	 * @param the_user The User to be assigned.
	 * @param the_role The Role to be assigned.
	 * @return true if the assignment was valid and succeeded. False otherwise.
	 */
	public boolean assignUser(final User the_user, final Role the_role)
	{
		// True if assigning the User to the Role is allowed.
		boolean success = true;
		
		switch(the_role)
		{
		case HEAD_OFFICE:
			break;
		case PROGRAM_CHAIR:
			// BR03: Only 1 PC is allowed.
			if (my_roles.get(Role.PROGRAM_CHAIR).size() > 0)
			{
				success = false;
			}
			break;
		case SUBPROGRAM_CHAIR:
			// BR06: A User may only be an SPC if already a Reviewer.
			if (!checkUser(the_user, Role.REVIEWER))
			{
				success = false;
			}
			break;
		case AUTHOR:
			break;
		case REVIEWER:
			break;
		case GUEST:
			
		}
		
		// If assigning is allowed, we add the User to the Role.
		if (success)
		{
			my_roles.get(the_role).add(the_user);
		}
		return success;
	}
	
	/**
	 * Checks to see if a User has been assigned the specified Role.
	 * @param the_user The User to be assigned.
	 * @param the_role The Role to be assigned.
	 * @return true if the User is assigned to the Role. False otherwise.
	 */
	public boolean checkUser(final User the_user, final Role the_role)
	{
		boolean has_role = false;
		
		// Fetch list of Users in specified Role.
		final Set<User> user_list = my_roles.get(the_role);
		
		// Iterate through Users and return true if specified User is found.
		for (User u : user_list)
		{
			if (u == the_user)
			{
				has_role = true;
				break;
			}
		}
		
		return has_role;
	}
	
	/**
	 * Removes a User from a Role.
	 * @param the_user The User to be assigned.
	 * @param the_role The Role to be assigned.
	 * @return true if the User previously had the Role and was removed. False otherwise.
	 */
	public boolean removeUser(final User the_user, final Role the_role)
	{
		boolean success = false;
		
		// Fetch list of Users in specified Role.
		final Set<User> user_list = my_roles.get(the_role);
		
		// Iterate through Users and remove specified User, if present.
		for (User u : user_list)
		{
			if (u == the_user)
			{
				user_list.remove(u);
				success = true;
				break;
			}
		}
		
		// Replace the old User list with the updated one.
		my_roles.put(the_role, user_list);
		
		return success;
	}
	

	/* DEADLINE GET/SET METHODS */
	
	public Date deadlineSubmission()
	{
		return my_deadline_submission;
	}

	public void setDeadlineSubmission(final Date the_deadline_submission)
	{
		my_deadline_submission = the_deadline_submission;
	}
	
	public Date deadlineReview()
	{
		return my_deadline_review;
	}

	public void setDeadlineReview(final Date the_deadline_review)
	{
		my_deadline_review = the_deadline_review;
	}
	
	public Date deadlineDecision()
	{
		return my_deadline_decision;
	}

	public void setDeadlineDecision(final Date the_deadline_decision)
	{
		my_deadline_decision = the_deadline_decision;
	}
	
	public Set<PaperSubmission> getSubmissions(){
		return my_submissions;
	}
	
	public Map<Role, Set<User>> getRoles(){
		return my_roles;
	}
	
	public String getName() {
		return my_name;
	}
	
	public void setName(final String the_name) {
		my_name = the_name;
	}
	
	public String toString() {
		return my_name;
	}
	
}
