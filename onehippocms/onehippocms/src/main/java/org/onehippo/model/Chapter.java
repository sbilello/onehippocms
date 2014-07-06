package org.onehippo.model;

import java.util.List;

import org.onehippo.forge.utilities.hst.simpleocm.JcrNodeType;
import org.onehippo.forge.utilities.hst.simpleocm.JcrPath;

@JcrNodeType(value="library:chapter")
public class Chapter {
	
	@JcrPath(value="library:chapterName")
	private String name;
	
	@JcrPath(value="library:paragraph")
	private List<Paragraph> paragraphs;
	
	public String getName() {
		return name;
	}
	public List<Paragraph> getParagraphs() {
        return paragraphs;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setParagraphs(List<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

}
