package test;

import static org.junit.Assert.*;

import java.util.Date;

import model.Conference;
import model.Role;
import model.User;

import org.junit.Test;

/**
 * @author alexyuly
 * @version 22 May 2013
 */
public class ConferenceTest
{

	@Test
	/**
	 * Tests the Conference.assignUser method.
	 */
	public void testAssignUser()
	{
		final Conference c = new Conference(new Date(), new Date(), new Date());
		final User user1 = new User("Name", "Email", "ID1");
		
		// We should not assign a User as an SPC if s/he is not already a Reviewer.
		assertEquals(false, c.assignUser(user1, Role.SUBPROGRAM_CHAIR));
		
		// We should be able to assign a User as a Reviewer.
		assertEquals(true, c.assignUser(user1, Role.REVIEWER));
		
		// We should NOW be able to assign a User as an SPC.
		assertEquals(true, c.assignUser(user1, Role.SUBPROGRAM_CHAIR));
		
		// We should also be able to assign a User as a PC.
		assertEquals(true, c.assignUser(user1, Role.PROGRAM_CHAIR));
		
		final User user2 = new User("Name", "Email", "ID2");
		
		// We should NOT be able to assign a 2nd User as a PC.
		assertEquals(false, c.assignUser(user2, Role.PROGRAM_CHAIR));
	}

}
