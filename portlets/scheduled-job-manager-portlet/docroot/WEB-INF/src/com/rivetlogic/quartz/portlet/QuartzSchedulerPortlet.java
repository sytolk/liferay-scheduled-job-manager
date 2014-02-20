/*
 * Copyright (C) 2005-2014 Rivet Logic Corporation.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; version 3 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.rivetlogic.quartz.portlet;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.rivetlogic.quartz.bean.SchedulerJobBean;
import com.rivetlogic.quartz.util.QuartzSchedulerUtil;

import java.io.IOException;
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * Portlet implementation class QuartzSchedulerPortlet
 */
public class QuartzSchedulerPortlet extends MVCPortlet {
    
    /**
     * Rendering View Liferay Mode with the scheduler job list in the Liferay
     * system
     */
    public void doView(RenderRequest request, RenderResponse response) throws IOException, PortletException {
        try {
            QuartzSchedulerUtil.getSchedulerJobs(request);
        } catch (SchedulerException e) {
            request.setAttribute(QuartzSchedulerUtil.ATTRIBUTE_JOBS_LIST, new ArrayList<SchedulerJobBean>());
            request.setAttribute(QuartzSchedulerUtil.ATTRIBUTE_COUNT, 0);
            _log.error(e);
        }
        super.doView(request, response);
    }
    
    /**
     * 
     * @param actionRequest
     * @param actionResponse
     * @throws IOException
     * @throws PortletException
     */
    public void jobAction(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException,
        PortletException {
        
        // Getting the name of the button pressed
        String jobAction = ParamUtil.getString(actionRequest, QuartzSchedulerUtil.PARAMETER_JOB_ACTION,
                StringPool.BLANK);
        
        if (!jobAction.isEmpty()) {
            if (jobAction.equals(QuartzSchedulerUtil.ACTION_REFRESH)) {
                // None action here for now. just going to render phase
            } else if (jobAction.equals(QuartzSchedulerUtil.ACTION_SHUTDOWN)) {
                try {
                    QuartzSchedulerUtil.scheduleJobServiceAction(jobAction);
                    SessionMessages.add(actionRequest, SESSION_MESSAGE_SUCCESS);
                } catch (SchedulerException e) {
                    _log.error(e);
                    SessionErrors.add(actionRequest, SESSION_MESSAGE_ERROR);
                }
            } else {
                try {
                    QuartzSchedulerUtil.scheduleJobAction(actionRequest, jobAction);
                    SessionMessages.add(actionRequest, SESSION_MESSAGE_SUCCESS);
                } catch (Exception e) {
                    _log.error(e);
                    SessionErrors.add(actionRequest, SESSION_MESSAGE_ERROR);
                }
            }
        }
    }
    
    private static final Log _log = LogFactoryUtil.getLog(QuartzSchedulerPortlet.class);
    private static final String SESSION_MESSAGE_SUCCESS = "rivet_scheduled_success";
    private static final String SESSION_MESSAGE_ERROR = "rivet_scheduled_error";
}
