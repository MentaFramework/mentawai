package org.mentawai.coc;

import org.mentawai.core.ActionConfig;
import org.mentawai.core.ApplicationManager;

public class CoCApplicationManager extends ApplicationManager {

	private ConsequenceProvider consequenceProvider = new ForwardConsequenceProvider();

	public ActionConfig getActionConfig(String name) {
		ActionConfig actionConfig = super.getActionConfig(name);
		if (actionConfig == null) {
			actionConfig = new CoCActionConfig(name);
			addActionConfig(actionConfig);
		}
		return actionConfig;
	}

	public ActionConfig getActionConfig(String name, String innerAction) {
		ActionConfig actionConfig = super.getActionConfig(name, innerAction);
		if (actionConfig == null) {
			actionConfig = new CoCActionConfig(name);
			addActionConfig(actionConfig);
		}
		return actionConfig;
	}

	public ConsequenceProvider getConsequenceProvider() {
		return consequenceProvider;
	}

	public void setConsequenceProvider(
			ConsequenceProvider consequenceProvider) {
		this.consequenceProvider = consequenceProvider;
	}
}
