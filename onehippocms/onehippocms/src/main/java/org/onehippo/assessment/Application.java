package org.onehippo.assessment;

import java.util.Stack;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.UnsupportedRepositoryOperationException;
import javax.jcr.Value;
import javax.jcr.observation.Event;
import javax.jcr.observation.ObservationManager;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.lang.StringUtils;
import org.hippoecm.hst.content.beans.ContentNodeBindingException;
import org.hippoecm.repository.HippoRepository;
import org.hippoecm.repository.HippoRepositoryFactory;
import org.onehippo.forge.utilities.hst.simpleocm.build.NodeBuilder;
import org.onehippo.forge.utilities.hst.simpleocm.build.NodeBuilderImpl;
import org.onehippo.forge.utilities.hst.simpleocm.load.BeanLoader;
import org.onehippo.forge.utilities.hst.simpleocm.load.BeanLoaderImpl;
import org.onehippo.listener.MyListener;
import org.onehippo.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sergio
 */
public final class Application {

    private static Logger log = LoggerFactory.getLogger(Application.class);
    
    public static void main(String[] args) throws RepositoryException, ContentNodeBindingException {
        
        Session session = retrieveSessionFromHippoRepository();
        System.out.println(">>>>>>>>>>>> EXERCISE TWO");
        exerciseTwo(session,"/content");
        System.out.println("<<<<<<<<<<<< EXERCISE TWO");
        System.out.println(">>>>>>>>>>>> EXERCISE THREE");
        exerciseThree(session,"about-us");
        System.out.println("<<<<<<<<<<<< EXERCISE THREE");
        // Other exercises see testCases and testListener
        session.logout();
    }
    
    /*
     * Connect to the repository by rmi connection
     * 
     * @return session
     * @throws LoginException, RepositoryException
     *
     */
    public static Session retrieveSessionFromHippoRepository() throws LoginException, RepositoryException{
 		 String repoUrl = "rmi://localhost:1099/hipporepository";
         String username = "admin";
         char[] password = "admin".toCharArray();
         HippoRepository repository = HippoRepositoryFactory.getHippoRepository(repoUrl);
         return  repository.login(username, password);
 	}
    
    /*
     * Traverse Recursively from path
     * @param session
     * @param path
     * @throws RepositoryException
     */
    public static void exerciseTwo(Session session, String path) throws RepositoryException{
    	log.info("Looking for Node with path: "+path);
    	try{
    		Node node = session.getNode(path);
    		traverse(node);
    	}catch(PathNotFoundException e){
    		log.info("Node with path "+path+" has been not found");
        }
    }
    
    /*
     * TraverseIterative from path
     * @param session
     * @param path
     * @throws RepositoryException
     */
    public static void exerciseTwoSecondVersion(Session session, String path) throws RepositoryException{
    	log.info("Looking for Node with path: "+path);
    	try{
    		Node node = session.getNode(path);
    		traverseIterative(node);
    	}catch(PathNotFoundException e){
    		log.info("Node with path "+path+" has been not found");
        }
    }
    /*
     *  Traverse the tree starting from Node n recursively and printing 
     *  properties and values
     *  @param node starting node
     *  @throws RepositoryException
     */
    private static void traverse(Node node) throws RepositoryException {
    		
    		System.out.print(" Path "+node.getPath());
    		
    		if (node.hasProperties()){
	    		PropertyIterator i = node.getProperties();
	    		while (i.hasNext()){
    				Property p = i.nextProperty();
	    			printProperty(p);
	    			if (p.getDefinition().isMultiple()){
		    			for (Value v : p.getValues()){
		    				printValue(v);
		    			}
	    			}
	    			else{
	    				Value v = p.getValue();
	    				printValue(v);
	    			}
	    		}
    		}
    		System.out.println("");
    		
    		for (NodeIterator ni = node.getNodes(); ni.hasNext();) {
    			Node k = ni.nextNode();
    			traverse(k);
    		}
    }
    /*
     *  Traverse the tree starting from Node n recursively and printing 
     *  properties and values
     *  @param node starting node
     *  @throws RepositoryException
     */
    private static void traverseIterative(Node node) throws RepositoryException {
    		Stack<Node> stack = new Stack<Node>();
    		stack.push(node);
    		while(!stack.isEmpty()){
    			Node k = stack.pop();
    			System.out.print(" Path "+k.getPath());
        		
        		if (node.hasProperties()){
    	    		PropertyIterator i = k.getProperties();
    	    		while (i.hasNext()){
        				Property p = i.nextProperty();
    	    			printProperty(p);
    	    			if (p.getDefinition().isMultiple()){
    		    			for (Value v : p.getValues()){
    		    				printValue(v);
    		    			}
    	    			}
    	    			else{
    	    				Value v = p.getValue();
    	    				printValue(v);
    	    			}
    	    		}
        		}
        		System.out.println("");
        		for (NodeIterator ni = k.getNodes(); ni.hasNext();) {
        			Node i = ni.nextNode();
        			stack.push(i);
        		}
    		} 		
    }
    
    /*
     * Print property Name and Type 
     */
    private static void printProperty(Property p) throws RepositoryException{
    	System.out.print(" PropertyName "+p.getName()+" "+" PropertyType "+PropertyType.nameFromValue(p.getType()));
    }
    
    /*
     * Print Value 
     */
    private static void printValue(Value v) throws RepositoryException{
    	System.out.print(" Value "+v);
    }
    
    /*
     * 
     * Print the path of the nodes that contains findText
     * @param  session
     * @param  findText - string to find
     * @throws RepositoryException
     * 
     */

     @SuppressWarnings("deprecation")
     public static void exerciseThree(Session session, String findText) throws RepositoryException{

     	// Check text argument
     	if (StringUtils.isEmpty(findText)){
     		throw
     			new IllegalArgumentException("FindText argument is empty ");
     	}
     	else{
 	    	// Obtain the query manager for the session ...
 	    	QueryManager queryManager = session.getWorkspace().getQueryManager();
 	
 	    	// Define statement
 	    	String expression = "//*[jcr:contains(.,'"+findText+"')]";
 	    	
 	    	log.info("Query statement: "+expression);
 	    	
 	    	// Create a query object ...
 			Query query = queryManager.createQuery(expression,Query.XPATH);
 	
 	    	// Execute the query and get the results ...
 	    	QueryResult result = query.execute();
 	
 	    	// Iterate over the nodes in the results ...
 	    	final NodeIterator nodeIter = result.getNodes();
 	    	
 	    	if (nodeIter.hasNext()){
	 	    	while ( nodeIter.hasNext() ) {
	 	    		final Node n = nodeIter.nextNode();
	 	    	    System.out.println("NodePath "+n.getPath());    		
	 	    	}
 	    	}
 	    	else{
 	    		log.info("No query results");
 	    	}
     	}
     }
     
     /*
      * Store a book under /content/books
      *  
      * 
      * @param  book that you want to store into repository
      * @param  nodeName 
      * @return True Book is stored or False Book is not stored
      */
     public static boolean storeBook (Book book, String nodeName) throws LoginException, RepositoryException, ContentNodeBindingException{
     	// I should add further checks for the nodeName
     	if (StringUtils.isNotEmpty(nodeName)){
     		NodeBuilder nodeBuilder = new NodeBuilderImpl();
     		Session session = retrieveSessionFromHippoRepository();
     		Node mybookNode = nodeBuilder.build(session.getNode("/content/books"), nodeName, book);
     		if (mybookNode!=null){
     			session.save();
     			return true;
     		}
     	}
     	else{
         	throw 
     			new IllegalArgumentException("Node Name is empty");
     	}
         return false;
     }
     
     /*
      * Get Book by absolutePath
      * @param absolutePath
      * @throws LoginException,RepositoryException,ContentNodeBindingException,NullPointerException,IllegalArgumentException
      */
     public static Book retrieveBook (String absolutePath) throws LoginException, RepositoryException, ContentNodeBindingException{
     	// I should add further checks for the absolutePath
     	if (StringUtils.isNotEmpty(absolutePath)){
     		Session session = retrieveSessionFromHippoRepository();
     		Node mybookNode = session.getNode(absolutePath);
            BeanLoader beanLoader = new BeanLoaderImpl();
            Book mybook = new Book();
            beanLoader.loadBean(mybookNode, mybook);
            return mybook;
     	}
     	else{
         	throw 
     			new IllegalArgumentException("AbsolutePath is empty");
     	}
     }

    
    /*
     * @param Session session
     * @param absPath is the absolutePath 
     * @throws IllegalArgumentException
     */
    public static void exerciseFive(Session session, String absPath) throws UnsupportedRepositoryOperationException, RepositoryException {
		
    	// Check absPath argument
	   	if (StringUtils.isEmpty(absPath)){
	   		throw
	   			new IllegalArgumentException("AbsPath argument is empty ");
	   	}
	   	else{
		    MyListener ls = new MyListener();
	    	ObservationManager o = session.getWorkspace().getObservationManager();
	    	o.addEventListener(ls,Event.NODE_ADDED, absPath, true, null, null, false);
	    	o.addEventListener(ls,Event.NODE_MOVED, absPath, true, null, null, false);
	    	o.addEventListener(ls,Event.NODE_REMOVED, absPath, true, null, null, false);
	    	o.addEventListener(ls,Event.PROPERTY_ADDED, absPath, true, null, null, false);
	    	o.addEventListener(ls,Event.PROPERTY_CHANGED, absPath, true, null, null, false);
	    	o.addEventListener(ls,Event.PROPERTY_REMOVED, absPath, true, null, null, false);
	   	}
	 }
    
    public static void moreThanFiveChanges() throws UnsupportedRepositoryOperationException, LoginException, RepositoryException, ContentNodeBindingException{
         exerciseFive(retrieveSessionFromHippoRepository(), "/content");
         Book b = new Book();
         b.setTitle("RedundantBook");
         for (int i=0;i<10;i++){
       		 storeBook(b,"redundantTest");
       	 }
    }
}