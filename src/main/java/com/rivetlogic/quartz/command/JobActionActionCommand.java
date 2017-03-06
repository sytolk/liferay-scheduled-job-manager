/**
 * Copyright 2000-present Liferay, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rivetlogic.quartz.command;

import com.rivetlogic.quartz.constants.ScheduledJobManagerPortletKeys;
import com.rivetlogic.quartz.util.QuartzSchedulerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + ScheduledJobManagerPortletKeys.LiferayScheduledJobManagerpanelapp,
        "mvc.command.name=jobAction"
    },
    service = MVCActionCommand.class
)
public class JobActionActionCommand implements MVCActionCommand {
	@Override
	public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {

		_handleActionCommand(actionRequest);

		return true;
	}

	private void _handleActionCommand(ActionRequest actionRequest) {
		// Getting the name of the button pressed
        String jobAction = ParamUtil.getString(actionRequest, QuartzSchedulerUtil.PARAMETER_JOB_ACTION,
                StringPool.BLANK);
        
        if (!jobAction.isEmpty()) {
            if (jobAction.equals(QuartzSchedulerUtil.ACTION_REFRESH)) {
                // None action here for now. just going to render phase
            } else if (jobAction.equals(QuartzSchedulerUtil.ACTION_SHUTDOWN)) {
                try {
                    QuartzSchedulerUtil.scheduleJobServiceAction(jobAction);
                } catch (SchedulerException e) {
                    _log.error(e);
                    SessionErrors.add(actionRequest, SESSION_MESSAGE_ERROR);
                }
            } else {
                try {
                    QuartzSchedulerUtil.scheduleJobAction(actionRequest, jobAction);
                } catch (Exception e) {
                    _log.error(e);
                    SessionErrors.add(actionRequest, SESSION_MESSAGE_ERROR);
                }
            }
        }
	}

	private static final Log _log = LogFactoryUtil.getLog(JobActionActionCommand.class);
	private static final String SESSION_MESSAGE_ERROR = "your-request-failed-to-complete";
}