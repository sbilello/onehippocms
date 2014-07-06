package org.onehippo.constant;

public enum ConstantNodeType {
	JCR_BOOK  ("library:book"),
	JCR_BOOKS ("library:books"),
	JCR_CHAPTER ("library:chapter"),
	JCR_PARAGRAPH ("library:paragraph");

  private final String value;
 
  private ConstantNodeType (String value)
  {
    this.value = value;
  }
 
  public String getValue() {
    return value;
  }
}
