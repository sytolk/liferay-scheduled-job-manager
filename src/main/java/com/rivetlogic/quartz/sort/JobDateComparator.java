/**
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

import com.liferay.portal.kernel.util.OrderByComparator;
import com.rivetlogic.quartz.bean.SchedulerJobBean;

/**
 * Base class for all comparators that compare date and times.
 * 
 * @author Tobias Liefke
 */
public abstract class JobDateComparator extends OrderByComparator {

	private static final long serialVersionUID = 1L;

	private final boolean asc;

	public JobDateComparator() {
		this(false);
	}

	public JobDateComparator(boolean asc) {
		this.asc = asc;
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		SchedulerJobBean jobBean0 = (SchedulerJobBean) arg0;
		SchedulerJobBean jobBean1 = (SchedulerJobBean) arg1;

		Date date0 = getDateToCompare(jobBean0);
		if (date0 == null) {
			date0 = new Date(0);
		}

		Date date1 = getDateToCompare(jobBean1);
		if (date1 == null) {
			date1 = new Date(0);
		}

		if (asc) {
			return date0.compareTo(date1);
		} else {
			return date1.compareTo(date0);
		}

	}

	/**
	 * Finds the correct value from the bean for this comparator.
	 * 
	 * @param jobBean
	 *            the bean of the current row
	 * @return the value of the compared column
	 */
	protected abstract Date getDateToCompare(SchedulerJobBean jobBean);

}
