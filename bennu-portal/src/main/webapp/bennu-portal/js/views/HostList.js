define([
    'jquery',
    'backbone',
    'marionette',
    'app',
    'text!templates/HostList.html',
    'views/SingleHost'
], function($, Backbone, Marionette, App, tpl, SingleHostView) {

    return Backbone.Marionette.CollectionView.extend({

        template: tpl,
        itemView: SingleHostView,
        itemViewContainer: "tbody",
        modelEvents : {
        	"change" : "render",
        },

	events: {
		"click .edit-host" : "editHost",
		"click .create-host": "createHost",
		"click .delete-host": "deleteHost",
		"click .show-menu" : "showMenu",
	},
	
	showMenu : function(e) {
		Backbone.history.navigate("#menu/" + e.target.id, true);
	},
	
	editHost : function(e) {
		e.preventDefault();
		Backbone.history.navigate("#hosts/edit/" + e.target.id, true);
	},
		
	createHost: function(e) {
		e.preventDefault();
		Backbone.history.navigate("#hosts/create" , true);
	},
	
	deleteHost : function(e) {
		var hostModel = this.collection.get(e.target.id);
		hostModel.destroy({
            	success:function () {
            		alert('Host apagado!');
            	}
        });
		return false;
	},
	
    });
});