package org.onehippo.test;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.UnsupportedRepositoryOperationException;

import org.hippoecm.hst.content.beans.ContentNodeBindingException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.onehippo.assessment.Application;

public class TestListener {
	@Rule
	public final ExpectedSystemExit exit = ExpectedSystemExit.none();
	
	@Test
	public void systemExitWithSelectedStatusCode0() throws UnsupportedRepositoryOperationException, LoginException, RepositoryException, ContentNodeBindingException {
	    exit.expectSystemExitWithStatus(0);
	    Application.moreThanFiveChanges();
	}
}
