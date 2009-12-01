package org.csstudio.opibuilder.converter.model;

import junit.framework.TestCase;

public class EdmBooleanTest extends TestCase {

	public void testEdmBoolean() throws EdmException {
		EdmAttribute a = new EdmAttribute();
		EdmBoolean b = new EdmBoolean(a);
		
		assertEquals(true, b.is());
		assertEquals(true, b.isInitialized());
		
		//false
		b = new EdmBoolean(null);
		assertEquals(false, b.is());
		assertEquals(true, b.isInitialized());
	}
	
	public void testWrongInput() throws EdmException {
		EdmAttribute a = new EdmAttribute("aSDF");
		
		try {
			new EdmBoolean(a);
		}
		catch (EdmException e) {
			assertEquals(EdmException.BOOLEAN_FORMAT_ERROR, e.getType());
		}
	}
}