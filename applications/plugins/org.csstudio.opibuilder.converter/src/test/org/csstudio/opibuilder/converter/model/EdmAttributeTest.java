package org.csstudio.opibuilder.converter.model;

import junit.framework.TestCase;

public class EdmAttributeTest extends TestCase {

	private EdmAttribute testAttribute;
	private static final String testVal = "TEST";
	private static final String val2 = "VALUE_TWO";
	private static final String val3 = "VALUE_THREE";
	private static final String val4 = "VALUE_FOUR";

	/**
	 * Creates basic instance of EdmAttribute class with sample element.
	 */
	public void setupAttribute() {
		testAttribute = new EdmAttribute(testVal);
	}

	public void testAppendValue() {

		testAttribute = new EdmAttribute();

		assertEquals("check_count", 0, testAttribute.getValueCount());

		testAttribute.appendValue(testVal);

		assertEquals("check_count", 1, testAttribute.getValueCount());
		assertEquals("check_value", testVal,
				testAttribute.getValue(testAttribute.getValueCount() - 1 ));
	}

	public void testAppendAndSetMoreValues() {

		setupAttribute();

		testAttribute.appendValue(val2);
		assertEquals("check_value", val2,
				testAttribute.getValue(testAttribute.getValueCount() - 1 ));

		testAttribute.appendValue(val3);
		assertEquals("check_value", val3,
				testAttribute.getValue(testAttribute.getValueCount() - 1 ));

		testAttribute.appendValue(val4);
		assertEquals("check_value", val4,
				testAttribute.getValue(testAttribute.getValueCount() - 1 ));

		assertEquals("check_count", 4, testAttribute.getValueCount());

		assertEquals("check_value", val3,
				testAttribute.getValue(testAttribute.getValueCount() - 2 ));
		assertEquals("check_value", val2,
				testAttribute.getValue(testAttribute.getValueCount() - 3 ));

	}

	public void testToStringMethod() {
		setupAttribute();

		testAttribute.appendValue(val2);
		testAttribute.appendValue(val3);
		testAttribute.appendValue(val4);
		
		String concatenatedString = testVal + " " + val2 + " " + val3 + " " + val4;

		assertEquals(concatenatedString, testAttribute.toString());
	}

	public void testCopyConstructor() {

		setupAttribute();
		EdmAttribute copy = new EdmAttribute(testAttribute);

		int valCount = testAttribute.getValueCount();
		assertEquals(valCount, copy.getValueCount());

		for (int i = 0; i < valCount; i++)
			assertEquals(testAttribute.getValue(i), copy.getValue(i));
	}
}