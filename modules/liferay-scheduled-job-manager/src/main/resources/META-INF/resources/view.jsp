<%@ include file="/init.jsp" %>

<%
	PortletURL portletURL = renderResponse.createRenderURL();
    List<SchedulerJobBean> schedulerJobBeans = (List<SchedulerJobBean>) request.getAttribute("schedulerJobsList");
    
    String displayStyle = ParamUtil.getString(request, "displayStyle");
    
	/* Sorting */
	String orderByCol = ParamUtil.getString(request, "orderByCol");
	String orderByType = ParamUtil.getString(request, "orderByType");
	
	if (orderByCol == null || orderByCol.equals(StringPool.BLANK)) {
	    orderByCol = QuartzSchedulerUtil.DEFAULT_ORDER_BY_COL;
	}
	
	if (orderByType == null || orderByType.equals(StringPool.BLANK)) {
	    orderByType = QuartzSchedulerUtil.DEFAULT_ORDER_BY_TYPE;
	}

	OrderByComparator orderByComparator = QuartzSchedulerUtil.getSchedulerJobComparator(orderByCol, orderByType);
	Collections.sort(schedulerJobBeans, orderByComparator);

	Format dateFormatDateTime = FastDateFormatFactoryUtil.getDateTime(locale, timeZone);
%>

<c:set var="jobactionparam" value ="<%= QuartzSchedulerUtil.PARAMETER_JOB_ACTION %>"/>
<c:set var="jobselectedparam" value ="<%=QuartzSchedulerUtil.PARAMETER_JOB_SELECTED %>"/>
<c:set var="jobnameparam" value ="<%=QuartzSchedulerUtil.PARAMETER_JOB_NAME %>"/>
<c:set var="jobgroupparam" value ="<%=QuartzSchedulerUtil.PARAMETER_JOB_GROUP %>"/>
<c:set var="jobstoragetypeparam" value ="<%=QuartzSchedulerUtil.PARAMETER_STORAGE_TYPE %>"/>
<c:set var="runSupported" value="<%=QuartzSchedulerUtil.RUN_SUPPORTED %>"/>

<div id=schedulerJobsContainer class="custom-scheduled">

	<div class="navbar navbar-default">
		<div class="collapse navbar-collapse">
  			<ul class="lfr-nav nav navbar-nav">
  				<li class="active">
  					<a class="" href="javascript:void(0);" role="menuitem" title="Jobs"> <span class="nav-item-label"> Jobs </span> </a>
				</li>
			</ul>
		</div>
	</div>
	
	    <portlet:actionURL var="jobActionURL" name="jobAction"></portlet:actionURL>
        <aui:form name ="jobsActionsForm" action="${jobActionURL}" method="post">
        
			<aui:input type="hidden" name="${jobactionparam}" id ="${jobactionparam}" value=""></aui:input>
			
			<fieldset>
				<div class="btn-toolbar form-actions">
					<div class="btn-group pull-left">
						<button id="<%= QuartzSchedulerUtil.ACTION_RUN %>" class="btn btn-large btn-success" type="button" value="Run"><span class="icon-play"></span> <liferay-ui:message key="execute" /></button>
                        &nbsp;
						<button id="<%= QuartzSchedulerUtil.ACTION_RESUME %>" class="btn btn-large btn-primary" type="button" value="Resume"><span class="icon-eject"></span> <liferay-ui:message key="resume" /></button>
                        &nbsp;
						<button id="<%= QuartzSchedulerUtil.ACTION_PAUSE %>" class="btn btn-large btn-warning" type="button" value="Pause"><span class="icon-pause"></span> <liferay-ui:message key="pause" /></button>
					</div>
					<div class="btn-group pull-right">
						<button id="<%= QuartzSchedulerUtil.ACTION_SHUTDOWN %>" class="btn btn-large btn-danger" type="button" value="Shutdown"><span class="icon-off"></span> <liferay-ui:message key="shutdown" /></button>
                        &nbsp;
						<button id="<%= QuartzSchedulerUtil.ACTION_REFRESH %>" class="btn btn-large btn-success" type="button" value="Refresh"><span class="icon-refresh"></span> <liferay-ui:message key="refresh" /></button>
					</div>
				</div>
			</fieldset>
			
			<liferay-ui:search-container delta="5" deltaConfigurable="<%= true %>" id="schedulerJobListContainer" emptyResultsMessage="scheduled.job.manager.empty" total="<%= schedulerJobBeans.size() %>" orderByCol="<%= orderByCol %>" orderByType="<%= orderByType %>">
				
				<liferay-ui:search-container-results results="<%= QuartzSchedulerUtil.subList(schedulerJobBeans, searchContainer.getStart(), searchContainer.getEnd()) %>" />
						
					<liferay-ui:search-container-row className="com.rivetlogic.quartz.bean.impl.SchedulerJobBeanImpl" modelVar="schedulerJobBean">
				
						<liferay-ui:search-container-column-text name="">
								<aui:input label="" name ="${jobselectedparam}${index}" type="checkbox"/>
						</liferay-ui:search-container-column-text>
						
						<liferay-ui:search-container-column-text name="short-name" orderable="true" orderableProperty="shortName" >
				                ${schedulerJobBean.shortName}
				                <aui:input name="${jobnameparam}${index}" type="hidden" value="${schedulerJobBean.jobName}"/>
				        </liferay-ui:search-container-column-text>
				        <liferay-ui:search-container-column-text name="group-name" orderable="true" orderableProperty="shortGroup" cssClass="hidden-phone hidden-tablet" >
				                ${schedulerJobBean.shortGroup}
				                <aui:input name="${jobgroupparam}${index}" type="hidden" value="${schedulerJobBean.groupName}"/>
				        </liferay-ui:search-container-column-text>
				        <liferay-ui:search-container-column-text name="status" orderable="true" orderableProperty="state">
				                ${schedulerJobBean.triggerState}
				        </liferay-ui:search-container-column-text>
				        <liferay-ui:search-container-column-text name="start-time" orderable="true" orderableProperty="startTime">
				                <%= schedulerJobBean.getStartTime() == null ? '-' : dateFormatDateTime.format(schedulerJobBean.getStartTime()) %>
				        </liferay-ui:search-container-column-text>
				        <liferay-ui:search-container-column-text name="termination-time" orderable="true" orderableProperty="endTime" cssClass="hidden-phone hidden-tablet" >
				                <%= schedulerJobBean.getEndTime() == null ? '-' : dateFormatDateTime.format(schedulerJobBean.getEndTime()) %>
				        </liferay-ui:search-container-column-text>
				        <liferay-ui:search-container-column-text name="previous-fire-time" orderable="true" orderableProperty="previousFireTime" cssClass="hidden-phone hidden-tablet">
				                <%= schedulerJobBean.getPreviousFireTime() == null ? '-' : dateFormatDateTime.format(schedulerJobBean.getPreviousFireTime()) %>
				        </liferay-ui:search-container-column-text>
				        <liferay-ui:search-container-column-text name="next-fire-time" orderable="true" orderableProperty="nextFireTime">
				                <%= schedulerJobBean.getNextFireTime() == null ? '-' : dateFormatDateTime.format(schedulerJobBean.getNextFireTime()) %>
				        </liferay-ui:search-container-column-text>
				        <liferay-ui:search-container-column-text name="storage-type" orderable="true" orderableProperty="storageType" cssClass="hidden-phone hidden-tablet">
				                ${schedulerJobBean.storageType}
				                <aui:input name="${jobstoragetypeparam}${index}" type="hidden" value="${schedulerJobBean.storageType}"/>
				        </liferay-ui:search-container-column-text>
				        
					</liferay-ui:search-container-row>
					
				<liferay-ui:search-iterator displayStyle="<%= displayStyle %>" markupView="lexicon" paginate="<%= true %>" />
				
			</liferay-ui:search-container>
		</aui:form>
	</div>
</div>

<aui:script use ="scheduledjobutil">

AUI().ready('aui-node', function(A) {
	A.scheduledjobutil.setPortletNamespace('${pns}');
	A.scheduledjobutil.setShutdownActionName('<%= QuartzSchedulerUtil.ACTION_SHUTDOWN %>');
	A.scheduledjobutil.setRunActionName('<%= QuartzSchedulerUtil.ACTION_RUN %>');
	A.scheduledjobutil.setResumeActionName('<%= QuartzSchedulerUtil.ACTION_RESUME %>');
	A.scheduledjobutil.setPauseActionName('<%= QuartzSchedulerUtil.ACTION_PAUSE %>');
	A.all('#schedulerJobsContainer :button').on('click',A.scheduledjobutil.actionButtonHandler);
});

</aui:script>