/**
 * 
 */
package test;

import static org.junit.Assert.*;

import java.util.HashSet;

import model.Conference;
import model.Paper;
import model.PaperSubmission;
import model.Role;
import model.User;

import org.junit.Before;
import org.junit.Test;

import database.DataAccess;

/**
 * @author Mars Gokturk
 * @version Spring, 2013
 *
 */
public class DataAccessTest {
	
	//DataAccess my_database;
	User my_user;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		my_user = new User();
	}

	/**
	 * This method executes a blackbox testing for the loadUser method.(Not complete)
	 * Tested with: 
	 * User name = Albert Einstein
	 * User id=1
	 * password = e=mc2
	 * papers=P1,P2
	 */
	@Test
	public void testLoadUser() {
		
		//test whether the method returns a user.
		User test =my_user.login("Albert Einstein", "e=mc2");
		assertNotNull("testLoadUser fail: User object is null",test);
		
		//test whether we grabbed the correct user+loaded correct info.
		assertEquals("Albert Einstein", test.getName());
		assertEquals("albert@relativity.com", test.getEMail());
		
		//test whether the papers are loaded correctly.
		HashSet<Paper> papiers = (HashSet<Paper>) test.getPapers();
		for(Paper p: papiers){
			 assertEquals(p.getAuthor(), test.getName());
		}
		
		
		
	}
	
	/**
	 * This method tests whether a correct Conference object is loaded from the database.
	 * Tested with:
	 * ProgramChair:U1:Albert Einstein
	 * Subprogram Chair: U2,U3,U4
	 * Submissions:S1,S2,S3,S4
	 * 
	 * @param the_conference_id
	 * @return
	 */
	@Test
	public void testloadConference(){
		
		Conference test = DataAccess.loadConference("C1");
		
		//Test whether the Program chair is loaded correctly.
		HashSet<User> programchair = (HashSet<User>) test.getRoles().get(Role.PROGRAM_CHAIR);
		for(User u: programchair){
			assertEquals(u.getName(), "Albert Einstein");
		}
		
		//test subprogram chairs
		HashSet<User> subprogramchair =  (HashSet<User>) test.getRoles().get(Role.SUBPROGRAM_CHAIR);
		
		//test the size of the subprogramchair set
				assertSame(3, subprogramchair.size());
		for(User u : subprogramchair){
			String id = u.getID();
			assertFalse(id.equals("U1"));
			assertFalse(id.equals("U5"));
			assertFalse(id.equals("U6"));
			assertFalse(id.equals("U7"));
			
		}
		
		//test authors:
		//test subprogram chairs
		HashSet<User> authors =  (HashSet<User>) test.getRoles().get(Role.AUTHOR);
				for(User u : authors){
					String id = u.getID();
					assertFalse(id.equals("U1"));
					assertFalse(id.equals("U2"));
					assertFalse(id.equals("U4"));				
					
				}
		
		//test whether submissions are loaded correctly
		HashSet<PaperSubmission> submissions = (HashSet<PaperSubmission>) test.getSubmissions();
		
		//test the size of the submissions set
		assertSame(4, submissions.size());
		
		for(PaperSubmission ps: submissions){
			String id = ps.getAuthor().getID();
			assertFalse(id.equals("U1"));
			assertFalse(id.equals("U2"));
			assertFalse(id.equals("U4"));
		}
		
		
	}
	
	public void testloadReview(){
		
	}
	
	public void testloadRecommendation(){
		
	}

}
