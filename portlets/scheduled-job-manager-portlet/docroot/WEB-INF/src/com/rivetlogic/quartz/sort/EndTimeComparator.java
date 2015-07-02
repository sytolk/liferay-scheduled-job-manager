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

import java.util.Date;

import com.rivetlogic.quartz.bean.SchedulerJobBean;

/**
 * @author steven.barba
 * @author Tobias Liefke
 */
public class EndTimeComparator extends JobDateComparator {

	private static final long serialVersionUID = 1L;

	public EndTimeComparator() {
		super();
	}

	public EndTimeComparator(boolean asc) {
		super(asc);
	}

	@Override
	protected Date getDateToCompare(SchedulerJobBean jobBean) {
		return jobBean.getEndTime();
	}

}
