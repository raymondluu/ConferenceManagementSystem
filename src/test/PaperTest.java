package test;

import static org.junit.Assert.*;

import model.Paper;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Raymond Luu
 *
 */
public class PaperTest {
	
	/**
	 * Paper class to test.
	 */
	private Paper my_paper;
	
	/**
	 * Set up JUnit test.
	 */
	@Before
	public void setUp()
	{
		my_paper = new Paper("Author", "Title");
	}
	
	/**
	 * Test getAuthor() method.
	 */
	@Test
	public void testGetAuthor()
	{
		assertEquals("Author", my_paper.getAuthor());
	}
	
	/**
	 * Test getAbstract() method.
	 */
	@Test
	public void testGetAbstract()
	{
		assertEquals("", my_paper.getAbstract());
	}
	
	/**
	 * Test toString().
	 */
	@Test
	public void testToString()
	{
		assertEquals("Title by Author", my_paper.toString());
	}

}
