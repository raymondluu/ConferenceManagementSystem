package database;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import view.GUI;

import model.Conference;
import model.Paper;
import model.PaperSubmission;
import model.Recommendation;
import model.Review;
import model.Role;
import model.Status;
import model.User;

/**
 * A utility class that provides read/write access to the database.
 * 
 * @author Mars Gokturk, Alex Yuly
 */
public final class DataAccess
{
	
	private static User my_user = null;
	private static int my_conference_num = 0;
	

	/**
	 * Private constructor to inhibit instantiation.
	 */
	private DataAccess()
	{
		// No operations performed.
	}

	/**
	 * This method returns the mapping between the user types and their allowed actions.
	 * @return Map<Role, HashSet<String>> Roles->actions
	 */
	public static Map<Role, HashSet<String>> loadBusinessRules()
	{
		Map<Role, HashSet<String>>result = new HashMap<Role, HashSet<String>>();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/UserActions.txt");
			Document doc = builder.parse(file);
			
			NodeList nodes = doc.getElementsByTagName("user");
			int size = nodes.getLength();
			
			for (int i = 0; i < size; i++)
			{
				HashSet<String> actionsSet = new HashSet<String>();
				
				Element type = (Element) nodes.item(i);				
				final String t = type.getAttribute("type");
				final Role role = mapRole(t);				
				result.put(role, actionsSet); 			
							
				NodeList actlist = type.getElementsByTagName("action");
				int actsize = actlist.getLength();
				for(int j = 0; j < actsize ; j++){
				Element field = (Element) actlist.item(j);					
				String a = field.getTextContent();
				result.get(role).add(a);
				}
			}
					

		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}		
		
		return result;

	}


	/**
	 * Gets the existing conferences in the database.
	 * @return A map of conference names and their ids Map<ConferenceName, ConferenceId>
	 */
	public static Map<String, String> findConferences()
	{
		Map<String,String> result = new HashMap<String,String>();
		
		for (int i = 0 ; i < 2 ; i++)
		{
			String con_name = findTags("ConferenceMap", "conference", "name", i);			
			String con_id = findTags("ConferenceMap", "conference", "id", i);			
			result.put(con_name, con_id);
		}
		
		return result;
	}
	
	
	public boolean createNewConference(Date the_submission, Date the_review, Date the_decision, User the_PC)
	{
		File conference = new File("C"+my_conference_num+".txt");
		return false;
	}

	/**
	 * This method loads all relevant information about the current conference,
	 * and creates the objects. (Users,submissions,papers)
	 * 
	 * @param the_conference_id The ID of the conference to be returned.
	 * @return The selected conference.
	 */
	public static Conference loadConference(String the_conference_id)
	{
		Date submission = new Date(System.currentTimeMillis());
		Date review = new Date(System.currentTimeMillis());
		Date decision = new Date(System.currentTimeMillis());		
		Conference result = new Conference(submission,review,decision);
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/"+the_conference_id+".txt");
			Document doc = builder.parse(file);
			
			
			NodeList userlist = doc.getElementsByTagName("role");
			int userlistSize = userlist.getLength();
			for (int i = 0; i < userlistSize; i++)
			{
				Element user = (Element)userlist.item(i);
				Role role = mapRole(user.getAttribute("type")); 				
				NodeList uslist = user.getElementsByTagName("id");
				int uslistSize = uslist.getLength();
				for (int j = 0; j < uslistSize ; j++)
				{
					User u = createUser(uslist.item(j).getTextContent()); 
					result.getRoles().get(role).add(u);				 
				}
			}
				
			//add submissions:
			NodeList submissionlist = doc.getElementsByTagName("submissions");
			Element submissionid = (Element) submissionlist.item(0);
			NodeList submissionidlist = submissionid.getElementsByTagName("id");
			
			int submissionsize = submissionidlist.getLength();				
			for (int k = 0; k < submissionsize; k++ )
			{
				Element sub = (Element) submissionidlist.item(k);
				//System.out.println(sub.getTextContent());
				PaperSubmission ps = loadSubmission(sub.getTextContent());
				result.addSubmission(ps);
			}		
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}	

		return result;
	}
	
	/**
	 * This method looks up the user in the database, and returns
	 * a User object that encapsulates the user data. (User name, email,
	 * conferences, and papers.)
	 * 
	 * @param the_name
	 *            The name of the user.
	 * @param the_password
	 *            The password of the user.
	 * @return User if the guest is in the database, otherwise it returns null.
	 */
	public static User loadUser(String the_name, String the_password)
	{
		User result = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/Users.txt");
			Document doc = builder.parse(file);
			
			NodeList userlist = doc.getElementsByTagName("user");
			int userlistSize = userlist.getLength();
			for (int i = 0; i < userlistSize; i++)
			{
				Element user = (Element)userlist.item(i);
				String userId = user.getAttribute("id");				
				NodeList passlist = user.getElementsByTagName("password");
				String pass = passlist.item(0).getTextContent();				
				if (pass.equals(the_password))
				{ 
					result = createUser(userId);
					result.setActions(loadBusinessRules()); 
				}					
			}				
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}	
			
		return result;
	}
	
	/**
	 * Creates a PaperSubmission object from a given Submission file.
	 * @param the_submissionID
	 * @return
	 */
	private static PaperSubmission loadSubmission(String the_submissionID)
	{
		PaperSubmission result = new PaperSubmission();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/"+the_submissionID+".txt");
			Document doc = builder.parse(file);
			
			NodeList author = doc.getElementsByTagName("author");
			User u = createUser(author.item(0).getTextContent());
			result.setAuthor(u);
			
			NodeList paperlist = doc.getElementsByTagName("paper");
			String paperid = paperlist.item(0).getTextContent();			
			Paper p = loadPaper(paperid);
			result.setPaper(p);
			NodeList statuslist = doc.getElementsByTagName("status");
			String status = statuslist.item(0).getTextContent();
			result.setStatus(mapStatus(status));
			
			NodeList recommendationlist = doc.getElementsByTagName("recommendation");
			Recommendation r = loadRecommendation(recommendationlist.item(0).getTextContent());
			result.setRecommendation(r);
			
			NodeList reviewlist = doc.getElementsByTagName("review");
			int reviewlistsize = reviewlist.getLength();
			for (int i = 0; i < reviewlistsize; i++)
			{
				Review rew = loadReview(reviewlist.item(i).getTextContent());				
				result.addReview(rew.getReviewer(), rew);
			}
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}	

		return result;
	}
	
	/**
	 * Creates a recommendation object given its id.
	 * @param the_recommendationID
	 * @return
	 */
	private static Recommendation loadRecommendation(String the_recommendationID)
	{
		Recommendation result = null;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/"+the_recommendationID+".txt");
			Document doc = builder.parse(file);
			
			NodeList useridlist = doc.getElementsByTagName("userid");
			String usId = useridlist.item(0).getTextContent();
			User recommender = createUser(usId);
			
			//create user here
			NodeList rating = doc.getElementsByTagName("rating");			
			String rat = rating.item(0).getTextContent();
			
			NodeList rationalelist = doc.getElementsByTagName("rationale");
			String rationale = rationalelist.item(0).getTextContent();
			
			result = new Recommendation(recommender, Integer.parseInt(rat),rationale);
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}	
		
		return result;
	}
	
	/**
	 * Creates a review object from the text file.
	 * @param the_reviewID
	 * @return
	 */
	private static Review loadReview(String the_reviewID)
	{
		Review result = null;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/"+the_reviewID+".txt");
			Document doc = builder.parse(file);
			
			NodeList useridlist = doc.getElementsByTagName("userid");
			String usId = useridlist.item(0).getTextContent();
			User reviewer = createUser(usId);
			
			//create user here
			NodeList rating = doc.getElementsByTagName("rating");
			String rat = rating.item(0).getTextContent();
			
			result = new Review(reviewer, Integer.parseInt(rat));
			
			NodeList comtospclist = doc.getElementsByTagName("commentstospc");
			String comtospc = comtospclist.item(0).getTextContent();
			result.setCommentsToSPC(comtospc);
			
			NodeList cmtoauthorlist = doc.getElementsByTagName("commentstoauthor");
			String comtoauthor = cmtoauthorlist.item(0).getTextContent();
			result.setCommentsToAuthor(comtoauthor);
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}	
		
		return result;
	}
	
	/**
	 * Given a paper id, and a user, this method creates the paper object.
	 * @param the_paperID
	 * @param the_user
	 * @return
	 */
	private static Paper getPaper(String the_paperID, User the_user)
	{
		Paper result = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/"+the_paperID+".txt");
			Document doc = builder.parse(file);
			
			NodeList title = doc.getElementsByTagName("title");
			String t = (String)title.item(0).getTextContent();
			
			result = new Paper(the_user.getName(),t);
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}
		
		return result;	
	}
	
	/**
	 * Given a paper id,this method creates the paper object with an author info in the database.
	 * @param the_paperID
	 * @param the_user
	 * @return
	 */
	private static Paper loadPaper(String the_paperID)
	{
		Paper result = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/"+the_paperID+".txt");
			Document doc = builder.parse(file);
			
			NodeList title = doc.getElementsByTagName("title");
			String t = (String)title.item(0).getTextContent();
			NodeList author = doc.getElementsByTagName("author");
			String userid = author.item(0).getTextContent();
			User u = createUser(userid);
			
			result = new Paper(u.getName(), t);
		}
		
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}
		
		return result;
	}
	
	

	public static boolean saveConference(Conference the_conference)
	{
		return true;
	}
	
	
	/**
	 * @param the_title The title of the paper to check for.
	 * @return the ID of the next paper that would be created. 0 if a paper with the_title was found.
	 */
	public static int nextPaperID(final String the_title)
	{
		int next_id = 1;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc;
			
			// Loop until IOException is thrown (meaning there are no more papers).
			while (true)
			{
				doc = builder.parse(new File("Data/P" + next_id + ".txt"));
				final Node title = doc.getElementsByTagName("title").item(0);
				if (title.getTextContent().equals(the_title))
				{
					next_id = 0;
					break;
				}
				
				// Increment paper ID (very important)
				next_id += 1;
			}
		}
		catch (final ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (final SAXException e)
		{
			e.getMessage();
		}
		catch (final IOException e)
		{
			e.getMessage();
		}
		
		return next_id;
	}
	
	/**
	 * Add a new paper as a file in the database.
	 */
	public static void createNewPaper(final int the_id,
									  final String the_title,
									  final String the_keywords,
									  final String the_categories,
									  final String the_abstract,
									  final String the_text)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);

		try
		{
			// Create new empty document
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			
			// Create new paper element
			final Element paper = doc.createElement("paper");
			
			// Add user ID to paper
			final Element author = doc.createElement("author");
			author.setTextContent(GUI.user().getID());
			paper.appendChild(author);
			
			// Add title to paper
			final Element title = doc.createElement("title");
			title.setTextContent(the_title);
			paper.appendChild(title);
			
			// Add keywords to paper
			final String[] keywords = the_keywords.split(",");
			for (String k : keywords)
			{
				final Element keyword = doc.createElement("keyword");
				keyword.setTextContent(k);
				paper.appendChild(keyword);
			}
			
			// Add abstract to paper
			final Element aabstract = doc.createElement("abstract");
			title.setTextContent(the_abstract);
			paper.appendChild(aabstract);
			
			// Add categories to paper
			final String[] categories = the_categories.split(",");
			for (String c : categories)
			{
				final Element category = doc.createElement("category");
				category.setTextContent(c);
				paper.appendChild(category);
			}
			
			// Add text to paper
			final Element text = doc.createElement("text");
			title.setTextContent(the_text);
			paper.appendChild(text);
			
			// Add paper element to file
			doc.appendChild(paper);
			
			// Save changes to database
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File("Data/P" + the_id + ".txt"));
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
			
			/* Add paper to Users.txt file */
			File file = new File("Data/Users.txt");
			doc = builder.parse(file);
			
			final NodeList users = doc.getElementsByTagName("user");
			Element current_user = null;
			for (int i = 0; i < users.getLength(); i += 1)
			{
				final Element user = (Element) users.item(i);
				final String id = user.getAttribute("id");
				if (id.equals(GUI.user().getID()))
				{
					current_user = user;
				}
			}
			final Node new_paper = doc.createElement("paper");
			new_paper.setTextContent("P" + the_id);
			current_user.appendChild(new_paper);
			
			output = new StreamResult(new File("Data/Users.txt"));
			input = new DOMSource(doc);
			transformer.transform(input, output);
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerFactoryConfigurationError e)
		{
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param the_name The name of the user to check for.
	 * @return true if there already exists a user with name the_name.
	 */
	public static boolean hasUser(final String the_name)
	{
		boolean has_user = false;
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(new File("Data/Users.txt"));
			final NodeList users = doc.getElementsByTagName("name");
			
			for (int i = 0; i < users.getLength(); i += 1)
			{
				if (users.item(i).getTextContent().equals(the_name))
				{
					has_user = true;
				}
			}
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}
		
		return has_user;
	}

	/**
	 * Add a new given user to the user file in the database.
	 * @param the_user
	 */
	public static void createNewUser(final String the_name, final String the_pass, final String the_email)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		
		String user_id = "";
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(new File("Data/Users.txt"));
			int num_users = doc.getElementsByTagName("user").getLength();
			
			// Create new user element
			final Element user = doc.createElement("user");
			user_id = "U" + (num_users + 1);
			user.setAttribute("id", user_id);
			
			// Add name to user
			final Element name = doc.createElement("name");
			name.setTextContent(the_name);
			user.appendChild(name);
			
			// Add password to user
			final Element pass = doc.createElement("password");
			pass.setTextContent(the_pass);
			user.appendChild(pass);
			
			// Add email to user
			final Element email = doc.createElement("email");
			email.setTextContent(the_email);
			user.appendChild(email);
			
			// Add user to database
			Node users = doc.getElementsByTagName("users").item(0);
			users.appendChild(user);
			
			// Save changes to database
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File("Data/Users.txt"));
			Source input = new DOMSource(doc);
			transformer.transform(input, output);
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}
		catch (TransformerConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (TransformerFactoryConfigurationError e)
		{
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean addNewConference(Conference the_conference)
	{
		return true;
	}

	/**
	 * Given a user ID, this method creates a user, finds all their papers and returns the user.
	 * @param the_userID
	 * @return
	 */
	public static User createUser(String the_userID)
	{
		User result = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/Users.txt");
			Document doc = builder.parse(file);
			
			NodeList userlist = doc.getElementsByTagName("user");
			int userlistSize = userlist.getLength();
			
			for (int i = 0 ; i < userlistSize ; i++)
			{
				Element user = (Element)userlist.item(i);
				String attr = user.getAttribute("id");
				if (attr.equals(the_userID))
				{
					NodeList namelist = user.getElementsByTagName("name");
					
					String name = namelist.item(0).getTextContent();
					//System.out.println(name);
					NodeList emaillist = user.getElementsByTagName("email");
					
					String email = emaillist.item(0).getTextContent();
					//System.out.println(email);
					
					result = new User(name,email,attr); //create user here
					my_user = result;				//update the my_user here
					
					NodeList paperlist = user.getElementsByTagName("paper");
					int paperlistSize = paperlist.getLength();
					for (int j = 0; j < paperlistSize; j++)
					{
						String paperId = (String)paperlist.item(j).getTextContent();
						//System.out.println(paperId);
						Paper p = getPaper(paperId, my_user);
						my_user.addPaper(p);
					}					
				}
			}
		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}
		
		return result;
	}		
	
	/**
	 * Helper method to map roles to parsed roles.
	 * @param the_role
	 * @return
	 */
	private static Role mapRole(String the_role)
	{
		Role result ;
		switch(the_role){
		case "programchair":
			result = Role.PROGRAM_CHAIR;
			break;
		case "subprogramchair":
			result = Role.SUBPROGRAM_CHAIR;
			break;
		case "author":
			result =Role.AUTHOR;
			break;
		case "reviewer":
			result = Role.REVIEWER;
			break;
		case "headoffice":
			result = Role.HEAD_OFFICE;
			break;
		default:
			result = Role.GUEST;
		}
		
		return result;		
	}
	
	
	private static Status mapStatus(String the_status)
	{
		Status result;
		
		switch(the_status)
		{
		case "undecided":
			result = Status.UNDECIDED;
			break;
		case "yes":
			result = Status.YES;
			break;
		case "no":
			result=Status.NO;
			break;
		default:
			result =Status.UNDECIDED;
		}
		
		return result;
	}
	
	
	/**
	 * Helper method to extract the tag values from an xml file.
	 * @param the_fileName
	 * @return
	 */
	private static String findTags(String the_fileName, String the_parentName, String the_tag, int the_index)
	{
		String result = "";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringElementContentWhitespace(true);
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			File file = new File("Data/" + the_fileName + ".txt");
			Document doc = builder.parse(file);
			
			NodeList nodes = doc.getElementsByTagName(the_parentName); //conferencemap			
			Element element = (Element)nodes.item(the_index);	//get the first element of the parent node			
			NodeList name = element.getElementsByTagName(the_tag);
			Element field = (Element) name.item(0);
			result = field.getTextContent();			

		}
		catch (ParserConfigurationException e)
		{
			e.getMessage();
		}
		catch (SAXException e)
		{
			e.getMessage();
		}
		catch (IOException e)
		{
			e.getMessage();
		}

		return result;
	}

}
