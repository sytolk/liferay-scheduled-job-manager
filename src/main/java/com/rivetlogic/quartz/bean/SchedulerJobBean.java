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

package com.rivetlogic.quartz.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author steven.barba
 * 
 */
public interface SchedulerJobBean extends Serializable {
    
    public static final String NULL_VALUE_DISPLAY = "-";

    public String getJobName();
    
    public void setJobName(String jobName);
    
    public String getGroupName();
    
    public void setGroupName(String groupName);
    
    public String getTriggerState();
    
    public void setTriggerState(String triggerState);
    
    public Date getStartTime();
    
    public void setStartTime(Date startTime);
    
    public Date getEndTime();
    
    public void setEndTime(Date endTime);
    
    public Date getPreviousFireTime();
    
    public void setPreviousFireTime(Date previousFireTime);
    
    public Date getNextFireTime();
    
    public void setNextFireTime(Date nextFireTime);
    
    public String getStorageType();
    
    public void setStorageType(String storageType);
    
    public String getShortName();
    
    public String getShortGroup();
}
