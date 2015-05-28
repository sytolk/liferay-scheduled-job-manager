<%--
  /*
  * Copyright (C) 2005-2014 Rivet Logic Corporation.
  *
  * This program is free software; you can redistribute it and/or
  * modify it under the terms of the GNU General Public License
  * as published by the Free Software Foundation; version 3
  * of the License.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program; if not, write to the Free Software
  * Foundation, Inc., 51 Franklin Street, Fifth Floor,
  * Boston, MA 02110-1301, USA.
  */
--%>

<%@include file="init.jsp" %>

<liferay-ui:success key="rivet_scheduled_success" message="rivet.scheduled.success"/>
<liferay-ui:error key="rivet_scheduled_error" message="rivet.scheduled.error" />

<%
    List<SchedulerJobBean> schedulerJobBeans = (List<SchedulerJobBean>) request.getAttribute("schedulerJobsList");
	
	/* Sorting */
	String orderByCol = ParamUtil.getString(request, "orderByCol");
	String orderByType = ParamUtil.getString(request, "orderByType");
	
	if (orderByCol == null || orderByCol.equals(StringPool.BLANK)) {
	    orderByCol = QuartzSchedulerUtil.DEFAULT_ORDER_BY_COL;
	}
	
	if (orderByType == null || orderByType.equals(StringPool.BLANK)) {
	    orderByType = QuartzSchedulerUtil.DEFAULT_ORDER_BY_TYPE;
	}

	if (orderByCol != null || orderByType != null) {
		OrderByComparator orderByComparator = QuartzSchedulerUtil.getSchedulerJobComparator(orderByCol, orderByType);
		Collections.sort(schedulerJobBeans, orderByComparator);
	}

	Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);
%>

<c:set var="jobactionparam" value ="<%= QuartzSchedulerUtil.PARAMETER_JOB_ACTION %>"/>
<c:set var="jobselectedparam" value ="<%=QuartzSchedulerUtil.PARAMETER_JOB_SELECTED %>"/>
<c:set var="jobnameparam" value ="<%=QuartzSchedulerUtil.PARAMETER_JOB_NAME %>"/>
<c:set var="jobgroupparam" value ="<%=QuartzSchedulerUtil.PARAMETER_JOB_GROUP %>"/>
<c:set var="jobstoragetypeparam" value ="<%=QuartzSchedulerUtil.PARAMETER_STORAGE_TYPE %>"/>

<div id="schedulerManager">
	<div id="schedulerJobsContainer">
	
	    <portlet:actionURL var="jobActionURL" name="jobAction"></portlet:actionURL>
        <aui:form name ="jobsActionsForm" action="${jobActionURL}" method="post">
        
			<aui:input type="hidden" name="${jobactionparam}" id ="${jobactionparam}" value=""></aui:input>
			
			<fieldset>
				<legend><liferay-ui:message key="scheduled.job.manager.title" /></legend>
				<div class="btn-toolbar well clearfix">
					<div class="btn-group scheduler-job-actions pull-left">
						<button id="<%= QuartzSchedulerUtil.ACTION_RESUME %>" class="btn btn-large  btn-primary icon-play" type="button" value="Resume">&nbsp;Resume</button>
						<button id="<%= QuartzSchedulerUtil.ACTION_PAUSE %>" class="btn btn-large  btn-warning icon-pause" type="button" value="Pause">&nbsp;Pause</button>
					</div>
					<div class="btn-group refresh pull-right">
						<button id="<%= QuartzSchedulerUtil.ACTION_SHUTDOWN %>" class="btn btn-large  btn-danger icon-off" type="button" value="Shutdown">&nbsp;Shutdown</button>
						<button id="<%= QuartzSchedulerUtil.ACTION_REFRESH %>" class="btn btn-large  btn-success icon-refresh" type="button" value="Refresh">&nbsp;Refresh</button>
					</div>
					<div class="btn-group refresh pull-right clearfix"></div>
				</div>
			</fieldset>

			<liferay-ui:search-container id="schedulerJobListContainer" delta="<%= QuartzSchedulerUtil.NUMBER_OF_ROWS %>"
				emptyResultsMessage="scheduled.job.manager.empty" orderByCol="<%= orderByCol %>" orderByType="<%= orderByType %>">
				
				<liferay-ui:search-container-results results="<%= QuartzSchedulerUtil.subList(schedulerJobBeans, searchContainer.getStart(), searchContainer.getEnd()) %>"
					total="${count}" />
					
				<liferay-ui:search-container-row
					className="com.rivetlogic.quartz.bean.impl.SchedulerJobBeanImpl"
					modelVar="schedulerJobBean">
			
					<liferay-ui:search-container-column-text name="">
							<aui:input label="" name ="${jobselectedparam}${index}" type="checkbox"/>
					</liferay-ui:search-container-column-text>
					
					<liferay-ui:search-container-column-text name="short-name" orderable="true" orderableProperty="shortName" >
			                ${schedulerJobBean.shortName}
			                <aui:input name="${jobnameparam}${index}" type="hidden" value="${schedulerJobBean.jobName}"/>
			        </liferay-ui:search-container-column-text>
			        <liferay-ui:search-container-column-text name="group-name"  orderable="true" orderableProperty="shortGroup" cssClass="hidden-phone hidden-tablet" >
			                ${schedulerJobBean.shortGroup}
			                <aui:input name="${jobgroupparam}${index}" type="hidden" value="${schedulerJobBean.groupName}"/>
			        </liferay-ui:search-container-column-text>
			        <liferay-ui:search-container-column-text name="state"  orderable="true" orderableProperty="state">
			                ${schedulerJobBean.triggerState}
			        </liferay-ui:search-container-column-text>
			        <liferay-ui:search-container-column-text name="start-time"  orderable="true" orderableProperty="startTime">
			                <%= schedulerJobBean.getStartTime() == null ? '-' : dateFormatDateTime.format(schedulerJobBean.getStartTime()) %>
			        </liferay-ui:search-container-column-text>
			        <liferay-ui:search-container-column-text name="end-time"  orderable="true" orderableProperty="endTime" cssClass="hidden-phone hidden-tablet" >
			                <%= schedulerJobBean.getEndTime() == null ? '-' : dateFormatDateTime.format(schedulerJobBean.getEndTime()) %>
			        </liferay-ui:search-container-column-text>
			        <liferay-ui:search-container-column-text name="previous-fire-time"  orderable="true" orderableProperty="previousFireTime" cssClass="hidden-phone hidden-tablet">
			                <%= schedulerJobBean.getPreviousFireTime() == null ? '-' : dateFormatDateTime.format(schedulerJobBean.getPreviousFireTime()) %>
			        </liferay-ui:search-container-column-text>
			        <liferay-ui:search-container-column-text name="next-fire-time"  orderable="true" orderableProperty="nextFireTime">
			                <%= schedulerJobBean.getNextFireTime() == null ? '-' : dateFormatDateTime.format(schedulerJobBean.getNextFireTime()) %>
			        </liferay-ui:search-container-column-text>
			        <liferay-ui:search-container-column-text name="storage-type"  orderable="true" orderableProperty="storageType" cssClass="hidden-phone hidden-tablet">
			                ${schedulerJobBean.storageType}
			                <aui:input name="${jobstoragetypeparam}${index}" type="hidden" value="${schedulerJobBean.storageType}"/>
			        </liferay-ui:search-container-column-text>
			        
				</liferay-ui:search-container-row>
				
				<liferay-ui:search-iterator/>
			
			</liferay-ui:search-container>
		
		</aui:form>
		
	</div>
</div>

<aui:script use ="scheduledjobutil">
A.scheduledjobutil.setPortletNamespace('${pns}');
A.scheduledjobutil.setShutdownActionName('<%= QuartzSchedulerUtil.ACTION_SHUTDOWN %>');
A.all('#schedulerJobsContainer :button').on('click',A.scheduledjobutil.actionButtonHandler);
</aui:script>
