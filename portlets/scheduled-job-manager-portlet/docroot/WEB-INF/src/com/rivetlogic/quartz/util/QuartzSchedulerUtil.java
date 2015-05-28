/**
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

package com.rivetlogic.quartz.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.scheduler.SchedulerEngineHelperUtil;
import com.liferay.portal.kernel.scheduler.SchedulerException;
import com.liferay.portal.kernel.scheduler.StorageType;
import com.liferay.portal.kernel.scheduler.TriggerState;
import com.liferay.portal.kernel.scheduler.messaging.SchedulerResponse;
import com.liferay.portal.kernel.util.FastDateFormatFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;
import com.rivetlogic.quartz.bean.SchedulerJobBean;
import com.rivetlogic.quartz.bean.impl.SchedulerJobBeanImpl;
import com.rivetlogic.quartz.sort.EndTimeComparator;
import com.rivetlogic.quartz.sort.GroupNameComparator;
import com.rivetlogic.quartz.sort.JobNameComparator;
import com.rivetlogic.quartz.sort.NextFireTimeComparator;
import com.rivetlogic.quartz.sort.PreviousFireTimeComparator;
import com.rivetlogic.quartz.sort.StartTimeComparator;
import com.rivetlogic.quartz.sort.StateComparator;
import com.rivetlogic.quartz.sort.StorageTypeComparator;

import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.portlet.PortletRequest;

/**
 * @author steven.barba
 * 
 */
public class QuartzSchedulerUtil {
    
    public static SchedulerJobBean getSchedulerJob(SchedulerResponse schedulerResponse) {
        TriggerState triggerState = SchedulerEngineHelperUtil.getJobState(schedulerResponse);
        Date startTime = SchedulerEngineHelperUtil.getStartTime(schedulerResponse);
        Date endTime = SchedulerEngineHelperUtil.getEndTime(schedulerResponse);
        Date previousFireTime = SchedulerEngineHelperUtil.getPreviousFireTime(schedulerResponse);
        Date nextFireTime = SchedulerEngineHelperUtil.getNextFireTime(schedulerResponse);
        StorageType storageType = schedulerResponse.getStorageType();
        
        SchedulerJobBean schedulerJobBean = new SchedulerJobBeanImpl();
        schedulerJobBean.setJobName(schedulerResponse.getJobName());
        schedulerJobBean.setGroupName(schedulerResponse.getGroupName());
        schedulerJobBean.setTriggerState(triggerState == null ? SchedulerJobBean.NULL_VALUE_DISPLAY : triggerState
                .toString());
        schedulerJobBean.setStartTime(startTime);
        schedulerJobBean.setEndTime(endTime);
        schedulerJobBean.setPreviousFireTime(previousFireTime);
        schedulerJobBean.setNextFireTime(nextFireTime);
        schedulerJobBean.setStorageType(storageType == null ? SchedulerJobBean.NULL_VALUE_DISPLAY : storageType
                .toString().trim());
        return schedulerJobBean;
    }
    
    public static List<SchedulerJobBean> getSchedulerJobsList(List<SchedulerResponse> schedulerResponses) {
        List<SchedulerJobBean> schedulerJobBeans = new ArrayList<SchedulerJobBean>();
        for (SchedulerResponse scheduler : schedulerResponses) {
            schedulerJobBeans.add(getSchedulerJob(scheduler));
        }
        return schedulerJobBeans;
    }
    
    public static void scheduleJobServiceAction(String action) throws SchedulerException {
        
        if (action.equals(ACTION_SHUTDOWN)) {
            _log.info(LOG_SHUTDOWN_ACTION_MSG);
            SchedulerEngineHelperUtil.shutdown();
        }
    }
    
    public static void scheduleJobAction(PortletRequest request, String action) throws SchedulerException,
        ParseException {
        String jobName;
        String groupName;
        String storageTypeText;
        StorageType storageType;
        
        // Checking all the rows to see which are selected
        for (int i = 0; i <= QuartzSchedulerUtil.NUMBER_OF_ROWS; i++) {
            
            boolean rowSelected = ParamUtil.getBoolean(request, PARAMETER_JOB_SELECTED + i, false);
            if (rowSelected) {
                
                jobName = ParamUtil.getString(request, PARAMETER_JOB_NAME + i);
                groupName = ParamUtil.getString(request, PARAMETER_JOB_GROUP + i);
                storageTypeText = ParamUtil.getString(request, PARAMETER_STORAGE_TYPE + i);
                storageType = StorageType.valueOf(storageTypeText);
                
                // Log Info messages
                _log.info(String.format(LOG_JOB_FORMAT, action, LOG_ACTION_MSG, jobName, groupName, storageType));
                
                if (action.equals(ACTION_PAUSE)) {
                    SchedulerEngineHelperUtil.pause(jobName, groupName, storageType);
                } else if (action.equals(ACTION_RESUME)) {
                    SchedulerEngineHelperUtil.resume(jobName, groupName, storageType);
                }
            }
        }      
    }
    
    public static void getSchedulerJobs(PortletRequest request) throws SchedulerException {
        // Scheduler List
        List<SchedulerResponse> schedulerJobs = SchedulerEngineHelperUtil.getScheduledJobs();
        List<SchedulerJobBean> schedulerJobBeans = getSchedulerJobsList(schedulerJobs);
        request.setAttribute(ATTRIBUTE_JOBS_LIST, schedulerJobBeans);
        request.setAttribute(ATTRIBUTE_COUNT, schedulerJobBeans.size());
    }
    
    public static List<SchedulerJobBean> subList(List<SchedulerJobBean> list, Integer start, Integer end) {
        if (list != null) {
            return ListUtil.subList(list, start, end);
        } else {
            return new ArrayList<SchedulerJobBean>();
        }
    }
    
    public static OrderByComparator getSchedulerJobComparator(String orderByCol, String orderByType) {
        boolean orderByAsc = false;
        if (orderByType.equals(DEFAULT_ORDER_BY_TYPE)) {
            orderByAsc = true;
        }
        
        OrderByComparator orderByComparator = null;
        
        if (orderByCol.equalsIgnoreCase(COLUMN_SHORT_NAME)) {
            orderByComparator = new JobNameComparator(orderByAsc);
        } else if (orderByCol.equalsIgnoreCase(COLUMN_SHORT_GROUP)) {
            orderByComparator = new GroupNameComparator(orderByAsc);
        } else if (orderByCol.equalsIgnoreCase(COLUMN_STATE)) {
            orderByComparator = new StateComparator(orderByAsc);
        } else if (orderByCol.equalsIgnoreCase(COLUMN_START_TIME)) {
            orderByComparator = new StartTimeComparator(orderByAsc);
        } else if (orderByCol.equalsIgnoreCase(COLUMN_END_TIME)) {
            orderByComparator = new EndTimeComparator(orderByAsc);
        } else if (orderByCol.equalsIgnoreCase(COLUMN_PREVIOUS_FIRE_TIME)) {
            orderByComparator = new PreviousFireTimeComparator(orderByAsc);
        } else if (orderByCol.equalsIgnoreCase(COLUMN_NEXT_FIRE_TIME)) {
            orderByComparator = new NextFireTimeComparator(orderByAsc);
        } else if (orderByCol.equalsIgnoreCase(COLUMN_STORAGE_TYPE)) {
            orderByComparator = new StorageTypeComparator(orderByAsc);
        }
        
        return orderByComparator;
    }
    
    public static Format getDateTimeFormat(User user) {
    	return FastDateFormatFactoryUtil.getDateTime(user == null ? Locale.getDefault() : user.getLocale(), 
        		user == null ? TimeZone.getDefault() : user.getTimeZone());    	
    }
    
    public static final String ATTRIBUTE_JOBS_LIST = "schedulerJobsList";
    public static final String ATTRIBUTE_COUNT = "count";
    
    private static final Log _log = LogFactoryUtil.getLog(QuartzSchedulerUtil.class);
    private static final String LOG_ACTION_MSG = "action will be processed on job ";
    private static final String LOG_SHUTDOWN_ACTION_MSG = "Processing request for shutdown";
    private static final String LOG_JOB_FORMAT = "%s %s %s [%s, %s]";
    
    private static final String COLUMN_SHORT_NAME = "shortName";
    private static final String COLUMN_SHORT_GROUP = "shortGroup";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_START_TIME = "startTime";
    private static final String COLUMN_END_TIME = "endTime";
    private static final String COLUMN_PREVIOUS_FIRE_TIME = "previousFireTime";
    private static final String COLUMN_NEXT_FIRE_TIME = "nextFireTime";
    private static final String COLUMN_STORAGE_TYPE = "storageType";
    
    public static final String ACTION_PAUSE = "pause";
    public static final String ACTION_RESUME = "resume";
    public static final String ACTION_SHUTDOWN = "shutdown";
    public static final String ACTION_REFRESH = "refresh";
    public static final String PARAMETER_JOB_ACTION = "jobAction";
    public static final String PARAMETER_JOB_SELECTED = "jobSelected";
    public static final String PARAMETER_JOB_NAME = "jobName";
    public static final String PARAMETER_JOB_GROUP = "jobGroup";
    public static final String PARAMETER_STORAGE_TYPE = "jobStorageType";
    public static final String DEFAULT_ORDER_BY_TYPE = "asc";
    public static final String DEFAULT_ORDER_BY_COL = COLUMN_SHORT_NAME;
    public static final int NUMBER_OF_ROWS = 10;
    
}
