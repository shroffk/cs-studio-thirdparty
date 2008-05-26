package org.csstudio.nams.configurator.editor;

import org.csstudio.nams.configurator.treeviewer.model.IConfigurationModel;
import org.csstudio.nams.configurator.treeviewer.model.treecomponents.IConfigurationBean;
import org.csstudio.nams.configurator.treeviewer.model.treecomponents.IConfigurationNode;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class ConfigurationEditorInput implements IEditorInput {

	private final IConfigurationBean bean;
	private final IConfigurationModel model;

	public ConfigurationEditorInput(IConfigurationBean newElement,
			IConfigurationModel model) {
		this.bean = newElement;
		this.model = model;
	}

	public IConfigurationBean getBean() {
		return bean;
	}

	public IConfigurationModel getModel() {
		return this.model;
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return "ConfigurationEditorInput";
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return "Configuration Editor";
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * Da nur ein ConfigurationEditor geöffnet werden woll, muss equals immer
	 * true liefern
	 */
	public boolean equals(Object obj) {
		return (obj instanceof ConfigurationEditorInput);
	}

}
