package org.onehippo.model;

import java.util.List;

import org.onehippo.forge.utilities.hst.simpleocm.JcrNodeType;
import org.onehippo.forge.utilities.hst.simpleocm.JcrPath;


@JcrNodeType(value="library:book")
public class Book {
	@JcrPath(value="library:bookTitle")
	private String title;
	
	@JcrPath(value="library:chapter")
	private List<Chapter> chapters;
	
	public List<Chapter> getChapters() {
        return  chapters;
    }
	
	public String getTitle() {
	    return title;		
	}
	
	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

}