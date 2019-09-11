/**
 * Copyright (C) 2005-2017 Rivet Logic Corporation.
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

AUI().ready('aui-node', function(A) {
	
    var thList = A.all('.aui .table thead th');   
    
    //Add the CSS rules ".hidden-phone" and ".hidden-tablet" and some cols of the search-container for give responsive behavior to the table
    var hideTableCols = (function hideColsForDevices(){
    	if(thList.size() > 0) {
	    	thList.item(2).addClass('hidden-phone hidden-tablet');// Group Name col.
	        thList.item(5).addClass('hidden-phone hidden-tablet');// End Time col.
	        thList.item(6).addClass('hidden-phone hidden-tablet');// Previous FireTime col.
	        thList.item(8).addClass('hidden-phone hidden-tablet');// Storage Type col.
    	}
    })();  
    
    var portletNamespace = "";
	var shutdownAction = "shutdown";
	var runAction = "run";
	var resumeAction = "resume";
	var pauseAction = "pause";
	var dialog = undefined;
	var validation = undefined;
	var form = A.one('form');
	
    A.namespace('scheduledjobutil');
    
    A.scheduledjobutil.setPortletNamespace = function(pns){		 
		portletNamespace = pns;
	};
	
	A.scheduledjobutil.setShutdownActionName = function(action){		 
		shutdownAction = action;
	};
	
	A.scheduledjobutil.setRunActionName = function(action){		 
		runAction = action;
	};
	
	A.scheduledjobutil.setResumeActionName = function(action){		 
		resumeAction = action;
	};
	
	A.scheduledjobutil.setPauseActionName = function(action){		 
		pauseAction = action;
	};
    
	A.scheduledjobutil.showValidation = function(event) {
    	if (validation == undefined) {
	        validation = Liferay.Util.Window.getWindow(
	                {
	    	            dialog: {
	                        bodyContent: Liferay.Language.get('com.rivet.scheduled_job.popup.validation.msg.body'),
	                        width: 400,
	                        height: 250,
	                        toolbars: {
	                            footer: [
	                                {
	                                    label: Liferay.Language.get('accept'),
	                                    on: {
	                                        click: function() {
	                                                validation.hide();
	                                        }
	                                    }
	                                }
	                            ]
	                        }
	    	            },
	    	            title: Liferay.Language.get('com.rivet.scheduled_job.popup.validation.msg.title')
	                }
	        );
    	}
    	validation.show();
    };
	
    A.scheduledjobutil.showDialog = function(event) {
    	//Popup defination
    	if (dialog == undefined) {
	        dialog = Liferay.Util.Window.getWindow(
	                {
	    	            dialog: {
	                        bodyContent: Liferay.Language.get('com.rivet.scheduled_job.popup.warning.msg.body'),
	                        width: 400,
	                        height: 250,
	                        toolbars: {
	                            footer: [
	                                {
	                                    label: Liferay.Language.get('accept'),
	                                    on: {
	                                        click: function() {
	                                                submitForm(form);
	                                        }
	                                    }
	                                },
	                                {
	                                    label: Liferay.Language.get('cancel'),
	                                    on: {
	                                        click: function() {
	                                                dialog.hide();
	                                        }
	                                    }
	                                }
	                            ]
	                        }
	    	            },
	    	            title: Liferay.Language.get('warning')
	                }
	        );
    	}
    	dialog.show();
    };
    
    A.scheduledjobutil.actionButtonHandler = function(event) {

    	event.preventDefault();

    	var action = event.target.get('id');
    	
    	A.one('#'+portletNamespace+'jobAction').val(action);
    	
    	if (action === shutdownAction){
    		A.scheduledjobutil.showDialog();
    	} else if(action == runAction || action == resumeAction || action == pauseAction) {
    		inputs = A.all('input[type=checkbox]:checked');
    		if(inputs.size() < 1) {
    			A.scheduledjobutil.showValidation();
    			return;
    		}
    		form.submit();
    	} else {
    		form.submit();
    	}
    };

});

