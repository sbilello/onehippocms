package org.onehippo.model;

import org.onehippo.forge.utilities.hst.simpleocm.JcrNodeType;
import org.onehippo.forge.utilities.hst.simpleocm.JcrPath;

@JcrNodeType(value="library:paragraph")
public class Paragraph {
	@JcrPath(value="library:text")
	private String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
