package org.csstudio.opibuilder.converter;

import junit.framework.TestCase;

import org.csstudio.opibuilder.converter.model.EdmException;

public class EdmConverterTest extends TestCase {

	private static final String edl1 = "src/test/resources/ArcTest.edl";
	private static final String edl2 = "src/test/resources/LLRF_AUTO.edl";
	private static final String edl3 = "src/test/resources/navwogif.edl";
	private static final String edl4 = "src/test/resources/rccsWaterSkid.edl";

	private static final String colorDefFile = "src/test/resources/colors.list";

	private void setEnvironment() {
		System.setProperty("edm2xml.colorsFile", colorDefFile);
		// Enable fail-fast mode for stricter tests.
		System.setProperty("edm2xml.robustParsing", "false");
	}
	
	public void testExampleEDL1() throws EdmException {
		setEnvironment();

		String[] args = {edl1};
		EdmConverter.main(args);
	}
	
	public void testExampleEDL2() throws EdmException {
		setEnvironment();

		String[] args = {edl2};
		EdmConverter.main(args);
	}
	
	public void testExampleEDL3() throws EdmException {
		setEnvironment();

		String[] args = {edl3};
		EdmConverter.main(args);
	}
	
	public void testExampleEDL4() throws EdmException {
		setEnvironment();

		String[] args = {edl4};
		EdmConverter.main(args);
	}
}