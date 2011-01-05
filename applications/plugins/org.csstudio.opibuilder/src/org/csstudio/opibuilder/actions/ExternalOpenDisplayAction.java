package org.csstudio.opibuilder.actions;

import java.util.LinkedHashMap;

import org.csstudio.opibuilder.runmode.RunModeService;
import org.csstudio.opibuilder.runmode.RunModeService.TargetWindow;
import org.csstudio.opibuilder.util.MacrosInput;
import org.csstudio.opibuilder.util.ResourceUtil;
import org.csstudio.platform.ui.display.IOpenDisplayAction;
import org.csstudio.platform.util.StringUtil;

/**Run OPI from external program, such as alarm GUI, data browser...
 * @author Xihui Chen
 *
 */
public class ExternalOpenDisplayAction implements IOpenDisplayAction {

	/**Open OPI file.
	 * @param path the path of the OPI file, it can be a workspace path, file system path or URL
	 * @param data the input macros in format of {@code "macro1 = hello", "macro2 = hello2"}
	 * @throws Exception
	 */
	public void openDisplay(String path, String data) throws Exception {
		if(path != null && path.trim().length() > 0){
			MacrosInput macrosInput = null;
			//parse macros
			if(data != null && data.trim().length() > 0){
				macrosInput = new MacrosInput(new LinkedHashMap<String, String>(), false);
				 final String pairs[] = StringUtil.splitIgnoreInQuotes(data, ',', true); //$NON-NLS-1$
			     for (String pair : pairs){
			        final String name_value[] = StringUtil.splitIgnoreInQuotes(pair, '=', true); //$NON-NLS-1$
			        if (name_value.length != 2)
			                throw new Exception("Input '" + pair + "' does not match 'name=value'");
			            macrosInput.getMacrosMap().put(name_value[0], name_value[1]);
			     }		     
			}
			RunModeService.getInstance().runOPI(
					ResourceUtil.getPathFromString(path), TargetWindow.SAME_WINDOW, 
					null, macrosInput, null);
		}
	}

}
