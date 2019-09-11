package com.rivetlogic.quartz.application.list;

import com.rivetlogic.quartz.constants.ScheduledJobManagerPortletKeys;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = {
		"panel.app.order:Integer=100",
		"panel.category.key=" + PanelCategoryKeys.CONTROL_PANEL_CONFIGURATION,
	},
	service = PanelApp.class
)
public class ScheduledJobManagerPanelApp extends BasePanelApp {

	@Override
	public String getPortletId() {
		return ScheduledJobManagerPortletKeys.LiferayScheduledJobManagerpanelapp;
	}

	@Override
	@Reference(
		target = "(javax.portlet.name=" + ScheduledJobManagerPortletKeys.LiferayScheduledJobManagerpanelapp + ")",
		unbind = "-"
	)
	public void setPortlet(Portlet portlet) {
		super.setPortlet(portlet);
	}

}