package com.rivetlogic.quartz.portlet;

import com.rivetlogic.quartz.constants.ScheduledJobManagerPortletKeys;

import java.io.IOException;
import java.util.ArrayList;

import com.rivetlogic.quartz.bean.SchedulerJobBean;
import com.rivetlogic.quartz.util.QuartzSchedulerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.scheduler.SchedulerException;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.add-default-resource=true",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.layout-cacheable=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.use-default-template=true",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.footer-portlet-javascript=/js/main.js",
		"javax.portlet.display-name=Scheduled Job Manager",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.init-param.help-template=/help.jsp",
		"javax.portlet.name=" + ScheduledJobManagerPortletKeys.LiferayScheduledJobManagerpanelapp,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=administrator",
		"javax.portlet.supports.mime-type=text/html"
	},
	service = Portlet.class
)
public class ScheduledJobManagerPortlet extends MVCPortlet {
	
	/**
     * Rendering View Liferay Mode with the scheduler job list in the Liferay
     * system
     */
    public void render(RenderRequest request, RenderResponse response) throws IOException, PortletException {
        try {
            QuartzSchedulerUtil.getSchedulerJobs(request);
        } catch (SchedulerException e) {
            request.setAttribute(QuartzSchedulerUtil.ATTRIBUTE_JOBS_LIST, new ArrayList<SchedulerJobBean>());
            request.setAttribute(QuartzSchedulerUtil.ATTRIBUTE_COUNT, NOT_RESULTS);
            _log.error(e);
        }
        super.render(request, response);
    }
    
    private static final int NOT_RESULTS = 0;
    private static final Log _log = LogFactoryUtil.getLog(ScheduledJobManagerPortlet.class);
}