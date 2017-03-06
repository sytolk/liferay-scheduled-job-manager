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

package com.rivetlogic.quartz.bean.impl;

import com.rivetlogic.quartz.bean.SchedulerJobBean;

import java.util.Date;

/**
 * @author steven.barba
 * @author Tobias Liefke
 */
public class SchedulerJobBeanImpl implements SchedulerJobBean {

    private static final long serialVersionUID = -8744907046781772258L;
    
    protected String jobName;
    protected String groupName;
    protected String triggerState;
    protected Date startTime;
    protected Date endTime;
    protected Date previousFireTime;
    protected Date nextFireTime;
    protected String storageType;
    
    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }
    
    /**
     * @param jobName
     *            the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
    
    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }
    
    /**
     * @param groupName
     *            the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    /**
     * @return the triggerState
     */
    public String getTriggerState() {
        return triggerState;
    }
    
    /**
     * @param triggerState
     *            the triggerState to set
     */
    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }
    
    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }
    
    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    
    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }
    
    /**
     * @param endTime
     *            the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    /**
     * @return the previousFireTime
     */
    public Date getPreviousFireTime() {
        return previousFireTime;
    }
    
    /**
     * @param previousFireTime
     *            the previousFireTime to set
     */
    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }
    
    /**
     * @return the nextFireTime
     */
    public Date getNextFireTime() {
        return nextFireTime;
    }
    
    /**
     * @param nextFireTime
     *            the nextFireTime to set
     */
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }
    
    /**
     * @return the storageType
     */
    public String getStorageType() {
        return storageType;
    }
    
    /**
     * @param storageType
     *            the storageType to set
     */
    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
    
    public String getShortName() {
        if (jobName != null && !jobName.isEmpty() && jobName.indexOf('.') > 0) {
            return jobName.substring(jobName.lastIndexOf('.')).substring(1);
        } else {
            return null;
        }
    }
    
    public String getShortGroup() {
        if (groupName != null) {
            return groupName.substring(groupName.lastIndexOf(".") + 1);
        } else {
            return null;
        }
    }
}
