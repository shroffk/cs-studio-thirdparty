package org.csstudio.opibuilder.converter.parser;

import org.csstudio.opibuilder.converter.model.EdmException;

import junit.framework.TestCase;

public class EdmParserTest extends TestCase {

	public void testFileDoesNotExist() {
		String fileName = "test.edl";
		
		try {
			@SuppressWarnings("unused")
			EdmParser edmParser = new EdmParser(fileName);
		}
		catch (Exception e){
			assertTrue(e instanceof EdmException);
			EdmException edmException = (EdmException)e;
			assertEquals(edmException.getType(), EdmException.FILE_NOT_FOUND);
			assertEquals("FILE_NOT_FOUND exception: File " + fileName + " does not exist.",
					edmException.getMessage());
		}
	}
}
