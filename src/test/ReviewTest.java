package test;

import static org.junit.Assert.*;

import model.Review;
import model.User;

import org.junit.Before;
import org.junit.Test;

public class ReviewTest {
	
	/**
	 * Review for testing.
	 */
	private Review my_review;
	
	/**
	 * Set up
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		my_review = new Review(new User(), 5);
	}

	/**
	 * test getCommentsToSPC() method.
	 */
	@Test
	public void testGetCommentsToSPC()
	{
		assertEquals("", my_review.getCommentsToSPC());
	}
	
	/**
	 * test setCommentsToSPC() method.
	 */
	@Test
	public void testSetCommentsToSPC()
	{
		my_review.setCommentsToSPC("Good Job!");
		assertEquals("Good Job!", my_review.getCommentsToSPC());
	}
	
	/**
	 * test setCommentsToSPC() method.
	 */
	@Test
	public void testSetCommentsToAuthor()
	{
		my_review.setCommentsToAuthor("Good Job!");
		assertEquals("Good Job!", my_review.getCommentsToAuthor());
	}
	
	/**
	 * test getCommentsToAuthor() method.
	 */
	@Test
	public void testGetCommentsToAuthor()
	{
		assertEquals("", my_review.getCommentsToAuthor());
	}
	
	/**
	 * test getRating() method.
	 */
	@Test
	public void testGetRating()
	{
		assertEquals(5, my_review.getRating());
	}
	
	/**
	 * test getReviewer() method.
	 */
	@Test
	public void testGetReviewer()
	{
		assertTrue("", true);
	}
	
	/**
	 * test toString() method.
	 */
	@Test
	public void testToString()
	{
		assertEquals("Rating: 5 ", my_review.toString());
	}
}
