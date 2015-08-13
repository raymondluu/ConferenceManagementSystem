package model;

/**
 * 
 * @author Raymond Luu
 */
public class Recommendation {
	
	/**
	 * The Reviewer who is making recommendation.
	 */
	private User recommender;
	
	/**
	 * The rating of this recommendation.
	 */
	private int recommendation_rating;
	
	/**
	 * The rationale for the recommendation.
	 */
	private String rationale;
	
	/**
	 * Recommendation constructor.
	 */
	public Recommendation(User the_recommender, int the_rating, String the_rationale)
	{
		recommender = the_recommender;
		recommendation_rating = the_rating;
		rationale = the_rationale;
	}
	
	/**
	 * Get the recommendation rating.
	 * 
	 * @return recommendation rating.
	 */
	public int getRating()
	{
		return recommendation_rating;
	}
	
	public String getRationale() 
	{
		return rationale;
	}
	
	public User getRecommender(){
		return recommender;
	}
	
	public String toString()
	{
		return "Rating: " + recommendation_rating + " " + rationale;
	}
	
	
}
