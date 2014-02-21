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

package com.rivetlogic.quartz.sort;

import java.text.ParseException;
import java.util.Date;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.rivetlogic.quartz.bean.SchedulerJobBean;
import com.rivetlogic.quartz.util.QuartzSchedulerUtil;

/**
 * @author steven.barba
 * 
 */
public class NextFireTimeComparator extends OrderByComparator {
    
    private static final long serialVersionUID = -4403619283278634299L;
    private static final Log log = LogFactoryUtil.getLog(NextFireTimeComparator.class);
    
    private boolean asc;
    
    public NextFireTimeComparator() {
        this(false);
    }
    
    public NextFireTimeComparator(boolean asc) {
        this.asc = asc;
    }
    
    @Override
    public int compare(Object arg0, Object arg1) {
        
        boolean value = false;
        SchedulerJobBean jobBean0 = (SchedulerJobBean) arg0;
        SchedulerJobBean jobBean1 = (SchedulerJobBean) arg1;
        
        Date nextFireTime0 = null;
        Date nextFireTime1 = null;
        String nextFire0 = jobBean0.getNextFireTime();
        String nextFire1 = jobBean1.getNextFireTime();
        if (!nextFire0.trim().equals(SchedulerJobBean.NULL_VALUE_DISPLAY) && !nextFire1.trim().equals(SchedulerJobBean.NULL_VALUE_DISPLAY)) {
            try {
                nextFireTime0 = QuartzSchedulerUtil.FORMAT_DATE_TIME.parse(nextFire0);
                nextFireTime1 = QuartzSchedulerUtil.FORMAT_DATE_TIME.parse(nextFire1);
            } catch (ParseException e) {
                log.warn(e);
            }
            value = nextFireTime0.after(nextFireTime1);
        } else {
            if (!nextFire0.trim().equals(SchedulerJobBean.NULL_VALUE_DISPLAY)) {
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
