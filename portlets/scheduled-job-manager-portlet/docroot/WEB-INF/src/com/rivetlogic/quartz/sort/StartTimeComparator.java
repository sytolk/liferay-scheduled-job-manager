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

package com.rivetlogic.quartz.sort;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.rivetlogic.quartz.bean.SchedulerJobBean;
import com.rivetlogic.quartz.util.QuartzSchedulerUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * @author steven.barba
 * 
 */
public class StartTimeComparator extends OrderByComparator {
    
    private static final long serialVersionUID = -4403619283278634299L;
    private static final Log log = LogFactoryUtil.getLog(StartTimeComparator.class);
    
    private boolean asc;
    
    public StartTimeComparator() {
        this(false);
    }
    
    public StartTimeComparator(boolean asc) {
        this.asc = asc;
    }
    
    @Override
    public int compare(Object arg0, Object arg1) {
        
        boolean value = false;
        SchedulerJobBean jobBean0 = (SchedulerJobBean) arg0;
        SchedulerJobBean jobBean1 = (SchedulerJobBean) arg1;
        
        Date startTime0 = null;
        Date startTime1 = null;
        String start0 = jobBean0.getStartTime();
        String start1 = jobBean1.getStartTime();
        if (!start0.trim().equals(SchedulerJobBean.NULL_VALUE_DISPLAY) && !start1.trim().equals(SchedulerJobBean.NULL_VALUE_DISPLAY)) {
            try {
                startTime0 = QuartzSchedulerUtil.FORMAT_DATE_TIME.parse(start0);
                startTime1 = QuartzSchedulerUtil.FORMAT_DATE_TIME.parse(start1);
            } catch (ParseException e) {
                log.warn(e);
            }
            value = startTime0.after(startTime1);
        } else {
            if (!start0.trim().equals(SchedulerJobBean.NULL_VALUE_DISPLAY)) {
                value = true;
            }
        }
        
        if (asc) {
            return value ? 1 : -1;
        } else {
            return value ? -1 : 1;
        }
        
    }
    
}
