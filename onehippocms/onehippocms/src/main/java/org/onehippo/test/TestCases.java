package org.onehippo.test;

import java.util.LinkedList;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeTypeManager;

import org.hippoecm.hst.content.beans.ContentNodeBindingException;
import org.junit.Assert;
import org.junit.Test;
import org.onehippo.assessment.Application;
import org.onehippo.constant.ConstantNodeType;
import org.onehippo.forge.utilities.hst.simpleocm.build.NodeBuilder;
import org.onehippo.forge.utilities.hst.simpleocm.build.NodeBuilderImpl;
import org.onehippo.forge.utilities.hst.simpleocm.load.BeanLoader;
import org.onehippo.forge.utilities.hst.simpleocm.load.BeanLoaderImpl;
import org.onehippo.model.Book;
import org.onehippo.model.Chapter;
import org.onehippo.model.Paragraph;



public class TestCases {
	
	
	@Test
	public void checkingRegisteredType() throws RepositoryException {
    	Session session = Application.retrieveSessionFromHippoRepository();
		NodeTypeManager nm = session.getWorkspace().getNodeTypeManager();
    	boolean check = true;
    	for (ConstantNodeType t : ConstantNodeType.values() ){
    		if (nm.hasNodeType(t.getValue())){
            	System.out.println("Type "+t+" is registered");
            }
    		else{
    			check=false;
    			System.out.println("Type "+t+" is not registered");
    		}
    	}
    	Assert.assertTrue(check);
	}
	
    @Test
    public void bookWithTitle() throws RepositoryException, ContentNodeBindingException {
    	NodeBuilder nodeBuilder = new NodeBuilderImpl();
        Book book = new Book();
        book.setTitle("My Book Title");
        Session session = Application.retrieveSessionFromHippoRepository();
        Node mybookNode = nodeBuilder.build(session.getNode("/content/books"), "MyFirstBook", book);
        Assert.assertEquals("My Book Title",mybookNode.getProperty("library:bookTitle").getString());
    }
    
    @Test
    public void bookWithChapter() throws RepositoryException, ContentNodeBindingException {
    	Session session = Application.retrieveSessionFromHippoRepository();
    	// Create Book with Chapter
        Book book = new Book();
        book.setTitle("My Book Title");
        Chapter chap = new Chapter();
        chap.setName("Chapter 1");
        List<Chapter> chapters =  new LinkedList<Chapter>();
        
        chapters.add(chap);
        book.setChapters(chapters);
        
        // Build my book node
        NodeBuilder nodeBuilder = new NodeBuilderImpl();
        Node mybookNode = nodeBuilder.build(session.getNode("/content/books"), "MyFirstBook", book);
        session.save();
        
        // Load my mybookNode into a Book
        BeanLoader beanLoader = new BeanLoaderImpl();
        Book mybook = new Book();
        beanLoader.loadBean(mybookNode, mybook);

        // Checking
        Assert.assertEquals(mybook.getTitle(), book.getTitle());
        Assert.assertEquals(mybook.getChapters().get(0).getName(), book.getChapters().get(0).getName());
    }
    
    
    @Test
    public void bookWithChapterAndParagraph() throws RepositoryException, ContentNodeBindingException {
    	Session session = Application.retrieveSessionFromHippoRepository();
    	// Create Book with Chapter and Paragraph
        Book book = new Book();
        book.setTitle("My Book Title");
        Chapter chap = new Chapter();
        chap.setName("Chapter 1");
        List<Chapter> chapters = new LinkedList<Chapter>();
        List<Paragraph> paragraphs = new LinkedList<Paragraph>();
        Paragraph par = new Paragraph();
        par.setText("Paragraph 1");
        paragraphs.add(par);
        chap.setParagraphs(paragraphs);
        chapters.add(chap);
        book.setChapters(chapters);
        
        // Build myBookNode
        NodeBuilder nodeBuilder = new NodeBuilderImpl();
        Node mybookNode = nodeBuilder.build(session.getNode("/content/books"), "MyFirstBook", book);
        session.save();
        
        // Load mybookNode into a Book
        BeanLoader beanLoader = new BeanLoaderImpl();
        Book mybook = new Book();
        beanLoader.loadBean(mybookNode, mybook);
        // Checking
        Assert.assertEquals(mybook.getTitle(), book.getTitle());
        Assert.assertEquals(mybook.getChapters().get(0).getName(), book.getChapters().get(0).getName());
        Assert.assertEquals(mybook.getChapters().get(0).getParagraphs().get(0).getText(), book.getChapters().get(0).getParagraphs().get(0).getText());
    }
    
    /** 
     * Test method to Store and to Get a Book
     * 
     * @throws RepositoryException 
     * @throws LoginException 
     * @throws ContentNodeBindingException
     * 
     */
     @Test
     public void exerciseFour() throws ContentNodeBindingException, LoginException, RepositoryException{
     	
     	// Create Book with Title Chapter and Paragraph
         Book book = new Book();
         book.setTitle("My Book Title");
         Chapter chap = new Chapter();
         chap.setName("Chapter 1");
         List<Chapter> chapters = new LinkedList<Chapter>();
         List<Paragraph> paragraphs = new LinkedList<Paragraph>();
         Paragraph par1 = new Paragraph();
         par1.setText("Paragraph 1");
         paragraphs.add(par1);
         Paragraph par2 = new Paragraph();
         par2.setText("Paragraph 2");
         paragraphs.add(par2);
         chap.setParagraphs(paragraphs);
         chapters.add(chap);
         book.setChapters(chapters);
         
         // Store my book into repository
         String name = "SecondBook";
         boolean stored = Application.storeBook(book,name);
         Assert.assertTrue(stored);
         String path = "/content/books/"+name;
         // Retrieve my book
         Book myretrievedBook = Application.retrieveBook(path);
    	 // Checking
         Assert.assertEquals(myretrievedBook.getTitle(), book.getTitle());
         Assert.assertEquals(myretrievedBook.getChapters().get(0).getName(), book.getChapters().get(0).getName());
         Assert.assertEquals(myretrievedBook.getChapters().get(0).getParagraphs().get(0).getText(), book.getChapters().get(0).getParagraphs().get(0).getText());
     }

     @Test(expected = PathNotFoundException.class)
     public void pathNotFound() throws ContentNodeBindingException, LoginException, RepositoryException{
    	 Application.retrieveBook("/badpath");
     }
     
}
