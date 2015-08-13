package model;

import java.util.Set;

/**
 * Represents a single paper.
 * 
 * @author Raymond Luu
 */
public class Paper {
	
	/**
	 * Paper author.
	 */
	private String author;
	
	/**
	 * Paper title.
	 */
	private String title;
	
	/**
	 * 
	 */
	private Set<String> keywords;
	
	/**
	 * Paper abstract.
	 */
	private String the_abstract;
	
	/**
	 * 
	 */
	private Set<String> conference_catagories;
	
	/**
	 * Paper class constructor.
	 */
	public Paper(String the_author,String the_title) //constructor changed
	{
		author = the_author;
		title = the_title;
		the_abstract = "";
	}
	
//	/**
//	 * 
//	 */
//	public edit()
//	{
//		
//	}
	
	/**
	 * Get the author of paper.
	 * 
	 * @return the author.
	 */
	public String getAuthor()
	{
		return author;
	}
	
	public String getAbstract() {
		return the_abstract;
	}
	
	public String toString() {
		return title + " by " + author;
	}
}
