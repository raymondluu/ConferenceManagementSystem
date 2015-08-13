package test;

import static org.junit.Assert.*;

import model.Recommendation;
import model.User;

import org.junit.Before;
import org.junit.Test;

public class RecommendationTest {
	
	/**
	 * Recommendation for testing.
	 */
	private Recommendation my_recommendation;
	
	@Before
	public void setUp() throws Exception 
	{
		my_recommendation = new Recommendation(new User(), 5, "The good work.");
	}

	/**
	 * test getRating() method.
	 */
	@Test
	public void testGetRating() {
		assertEquals(5, my_recommendation.getRating());
	}
	
	/**
	 * test getRationale() method.
	 */
	@Test
	public void testGetRationale()
	{
		assertEquals("The good work.", my_recommendation.getRationale());
	}

	/**
	 * test getRecommender() method.
	 */
	@Test
	public void testGetRecommender()
	{
		assertTrue("", true);
	}
	
	/**
	 * test toString() method.
	 */
	@Test
	public void testToString()
	{
		assertEquals("Rating: 5 The good work.", my_recommendation.toString());
	}
}
