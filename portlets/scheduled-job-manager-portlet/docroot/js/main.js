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

AUI().ready('aui-node', function(A) {
	
    var thList = A.all('.aui .table thead th');   
    
    //Add the CSS rules ".hidden-phone" and ".hidden-tablet" and some cols of the search-container for give responsive behavior to the table
    var hideTableCols = (function hideColsForDevices(){
    	thList.item(2).addClass('hidden-phone hidden-tablet');// Group Name col.
        thList.item(5).addClass('hidden-phone hidden-tablet');// End Time col.
        thList.item(6).addClass('hidden-phone hidden-tablet');// Previous FireTime col.
        thList.item(8).addClass('hidden-phone hidden-tablet');// Storage Type col.
    })();  

});


AUI.add('scheduledjobutil', function(A){
    
	var portletNamespace = "";
	var shutdownAction = "shutdown";
	var dialog = undefined;
	var form = A.one('form');
	
    A.namespace('scheduledjobutil');
    
    A.scheduledjobutil.setPortletNamespace = function(pns){		 
		portletNamespace = pns;
	};
	A.scheduledjobutil.setShutdownActionName = function(action){		 
		shutdownAction = action;
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
	                                    label: Liferay.Language.get('com.rivet.scheduled_job.popup.button.accept'),
	                                    on: {
	                                        click: function() {
	                                                submitForm(form);
	                                        }
	                                    }
	                                },
	                                {
	                                    label: Liferay.Language.get('com.rivet.scheduled_job.popup.button.cancel'),
	                                    on: {
	                                        click: function() {
	                                                dialog.hide();
	                                        }
	                                    }
	                                }
	                            ]
	                        }
	    	            },
	    	            title: Liferay.Language.get('com.rivet.scheduled_job.popup.warning.title')
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
    	}
    	else{

    		form.submit();
    	}
    };
},
'',
{
    requires: ['liferay-util-window']
}
);

