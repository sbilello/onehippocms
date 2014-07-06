package org.onehippo.constant;

public enum ConstantProperty {
	JCR_CHAPTER_NAME("library:chapterName"), 
	JCR_PARAGRAPH_TEXT("library:text"),
	JCR_BOOK_TITLE("library:bookTitle");
	
	private final String value;

	private ConstantProperty(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
