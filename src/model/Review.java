package model;

/**
 * 
 * @author Raymond Luu
 */
public class Review {
	
	/**
	 * The paper reviewer.
	 */
	private User reviewer;
	
	/**
	 * Comments to Sub Program Chair.
	 */
	private String comments_to_SPC;
	
	/**
	 * Comments to the Author.
	 */
	private String comments_to_author;
	
	/**
	 * Rating of the paper.
	 */
	private int rating;
	
	/**
	 * @author Mars
	 * Review Class Constructor.
	 */
	public Review(User the_reviewer, int the_rating)
	{
		reviewer = the_reviewer;
		rating = the_rating;
		comments_to_SPC = "";
		comments_to_author = "";
	}
	
	/**
	 * Get comments to Sub Program Chair.
	 * 
	 * @return the comments to Sub Program Chair.
	 */
	public String getCommentsToSPC()
	{
		return comments_to_SPC;
	}
	
	public void setCommentsToSPC(String the_comments)
	{
		comments_to_SPC = the_comments;
	}
	
	public void setCommentsToAuthor(String the_comments)
	{
		comments_to_author = the_comments;
	}
	
	/**
	 * Get comments to Author.
	 * 
	 * @return the comments to Author.
	 */
	public String getCommentsToAuthor()
	{
		return comments_to_author;
	}
	
	/**
	 * Get papers rating.
	 * 
	 * @return papers rating.
	 */
	public int getRating()
	{
		return rating;
	}
	
	public User getReviewer() 
	{
		return reviewer;
	}
	
	public String toString() 
	{
		return "Rating: " + rating + " " + comments_to_SPC;
	}
}
