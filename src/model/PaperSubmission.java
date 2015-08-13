package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents an entire submission, including a single paper, plus recommendations and reviews.
 * 
 * @author Caleb DelVillar-Fox, Alex Yuly
 */

public class PaperSubmission
{
	private Map<User, Recommendation> my_recommendations;
	private Map<User, Review> my_reviews;
	private User my_spc;
	private User my_author;
	private Status my_status;
	private Paper my_paper;
	
	
	public PaperSubmission()
	{
		my_status = Status.UNDECIDED;
		my_recommendations = new HashMap<User, Recommendation>();
		my_reviews = new HashMap<User, Review>();
		my_spc = new User();		
	}
	
	public void addReviewer(User the_reviewer)
	{
		my_reviews.put(the_reviewer, new Review(the_reviewer, 0));
	}
	
	public void addReview(User the_reviewer, Review the_review)
	{
		my_reviews.put(the_reviewer, the_review);
	}
	
	public void setSubprogramchair(User the_chair)
	{
		my_spc = the_chair;
	}
	
	public void setRecommendation(Recommendation the_rec)
	{
		my_recommendations.put(my_spc, the_rec);
	}
	
	public void setStatus(Status the_status)
	{
		my_status = the_status;
	}
	
	public Status getStatus()
	{
		return my_status;
	}
	
	public User getSPC()
	{
		return my_spc;
	}
	
	public User getAuthor()
	{
		return my_author;
	}
	
	public Set<User> getReviewers() {
		return my_reviews.keySet();
	}
	
	public Map<User, Review> getReviews() {
		return my_reviews;
	}
	
	public Double getAverageRating() {
		Double average = 0.0;
		int numRevs = 0;
		if(!my_reviews.isEmpty()) {	
			for(User reviewer: my_reviews.keySet()) {
				Review r = my_reviews.get(reviewer);
				average = average + r.getRating();
				numRevs++;
			}
		}
		average = average / numRevs;
		return average;
	}
	
	public void setAuthor(final User the_author) {
		my_author = the_author;
	}
	
	public void setPaper(final Paper the_paper) {
		my_paper = the_paper;
	}
	
	public String toString() 
	{
		return my_paper + ", SPC: " + my_spc.getName() + ", Status: " + my_status;
	}
}
