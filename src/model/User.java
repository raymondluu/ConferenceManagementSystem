package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import database.DataAccess;

/**
 * Represents a single user.
 * 
 * @author Mars Gokturk
 */
public class User {
	
	private Role my_currentRole;
	private Map<Role, HashSet<String>> my_actions;
	private Conference my_conference;
	private String my_name;
	private String my_email;	
	private Set<Paper> my_papers;
	private String my_ID ;
	private final  Set<Role>my_roles;
	
	
	
	public User(){
		
		this("Unassigned",null,null);
	}
	
	public User(String the_name, String the_email, String the_ID){
		
		my_currentRole = Role.HEAD_OFFICE; //might have to change
		my_conference = null;
		my_name = the_name;
		my_papers = new HashSet<Paper>();
		my_email = the_email;
		my_ID = the_ID;
		my_roles = new HashSet<Role>();
		
		//load actions here
		my_actions = DataAccess.loadBusinessRules();
		
		
	}	
	
	/**
	 * It checks whether the user is in the database, if it is 
	 * @param the_name
	 * @param the_password
	 * @return The logged in user.
	 */
	public User login(String the_name, String the_password){
		User result = DataAccess.loadUser(the_name, the_password);
		if(result != null){
			my_actions = result.my_actions;
			my_name = result.getName();
			my_ID = result.getID();
			my_email = result.getEMail();
			my_papers = result.getPapers();
			
		}
		
		return this;
	}
	
	/**
	 * Creates a new conference with given deadlines and the ProgramChair
	 * @param the_deadline_submission
	 * @param the_deadline_review
	 * @param the_deadline_decision
	 * @return
	 */
	/*Conference createConference(final Date the_deadline_submission,
			final Date the_deadline_review, final Date the_deadline_decision, User the_ProgramChair) {
		return new Conference(the_deadline_submission, the_deadline_review,
				the_deadline_decision, the_ProgramChair);
	}*/
	
	/**
	 * It sets the current conference this User acts in, and it loads all roles
	 * available to this user in this conference.
	 * 
	 * @param the_conference
	 */
	public void setConference(String the_conferenceID, String the_name) {
		if (the_conferenceID == null) {
			throw new IllegalArgumentException();
		}
		my_conference = DataAccess.loadConference(the_conferenceID);
		my_conference.setName(the_name);
		
		for (Role r : my_conference.getRoles().keySet()) {
			final Set<User> temp = my_conference.getRoles().get(r);
			for (User u: temp)
			{
				if (u.equals(this))
				{
					this.my_roles.add(r);
				}
			}
		}
		

	}
	
	/**
	 * Submits a given paper to the current conference.
	 * @param the_paper
	 * @param the_conferenceID The ID of the conference this paper will be submitted to.
	 * @return true if it is successful, false otherwise
	 */
	public boolean submitPaper(Paper the_paper, String the_conferenceID){
		boolean res = false;
		Map<String, String> ids = DataAccess.findConferences();
		if(the_paper != null && the_conferenceID !=null){
			Conference conference;
			if(ids.get(my_conference.getName()).equals(the_conferenceID)) {
				conference = my_conference;
				conference.setName(my_conference.getName());
			} else {
				conference = DataAccess.loadConference(the_conferenceID);
			}
			PaperSubmission result = new PaperSubmission();
			result.setAuthor(this);
			result.setStatus(Status.UNDECIDED);
			result.setPaper(the_paper);
			conference.getRoles().get(Role.AUTHOR).add(this);
			conference.addSubmission(result);
			//write it to the database: Add submission to a conference
			res = true;
		}
		
		return res;		
	}
	
	
	/**
	 * 
	 * @return All available roles in the current conference.
	 */
	public Set<Role> getRoles(){
		return (Set<Role>) Collections.unmodifiableCollection(my_roles);
	}
	
	public void setRole(Role the_role){
		my_currentRole = the_role;
	}
	
	public Role getRole(){
		return my_currentRole;
	}
	
	public Conference getCurrentConference(){
		return my_conference;
	}
	
	public void setActions(Map<Role, HashSet<String>> map){
		if(map == null || map.isEmpty()){
			throw new IllegalArgumentException();
		}
		my_actions = map;
	}
	
	public Set<String> getActions(){
		return my_actions.get(my_currentRole);
	}
	
	public Set<String> getAllActions() {
		Set<String> result = new HashSet<String>();
		for(Role r: my_roles) {
			Set<String> actions = my_actions.get(r);
			result.addAll(actions);
		}
		return result;
	}
	
	//Wrote to test the database
	public void addPaper(Paper the_paper){
		my_papers.add(the_paper);
	}
	
	public String getName(){
		return my_name;
	}
	
	public String getEMail(){
		return my_email;
	}
	
	public String getID(){
		return my_ID;
	}
	
	public Set<Paper> getPapers(){
		return my_papers;
	}
	
	
	@Override
	public boolean equals(Object the_other) {
		boolean result = false;
		if (this == the_other) {
			result = true;
		} else if ((the_other != null && (the_other.getClass() == this
				.getClass()))) {
			final User u = (User) the_other;
			result = u.getID().equals(this.getID());
		}

		return result;
	}

	@Override
	public int hashCode() {

		return this.toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("User ID = " + my_ID + "\n");
		result.append("User Name = " + my_name + "\n");
		result.append("User Email = " + my_email + "\n");

		for (Paper p : my_papers) {
			result.append("Paper: " + p + "\n");
		}

		return result.toString();
	}
	
}
