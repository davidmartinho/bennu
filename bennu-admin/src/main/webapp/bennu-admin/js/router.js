define([
    'jquery',
    'underscore',
    'mustache',
    'backbone',
    'marionette',
    'menu-manager',
    'app',
    'appLayout',
    'layouts/MenuLayout',
], function($, _, Mustache, Backbone, Marionette, MenuManager, App, AppLayout, MenuLayout) {

    var Router = Backbone.Marionette.AppRouter.extend({

        initialize: function() {
            App.Layout = new AppLayout();
            App.Layout.render();
            App.page.show(App.Layout);
            console.log("initialize controller ...");
			if (!App.themes) {
				App.themes = new MenuManager.Collections.Theme();
    			App.themes.fetch();
			}
        },
        
        appRoutes: {
    		"" : "showHosts",
    		"hosts": "showHosts",
    		"hosts/edit/:hostname" : "editHost",
    		"hosts/create": "createHost",
    		"menu/:id" : "showMenu",
    	},
    	
    	controller: {
    		initialize : function() {
    		},

    		showHosts : function() {
    			var hostCollection = new MenuManager.Collections.Host();
    			hostCollection.fetch({
    				success : function() {
    					App.Layout.contentRegion.show(new MenuManager.Views.HostList({
    						collection : hostCollection
    					}));
    				}
    			});
    		},
    		
    		
    		
    		showMenu: function (topMenuId) {
    			var menuModel = new MenuManager.Models.Menu({id : topMenuId});
    			
    			App.Layout.menuLayout = new MenuLayout();
    			
				App.Layout.contentRegion.show(App.Layout.menuLayout);
				
    			menuModel.fetch({
    				success: function() {
    					menuModel.initialize();
    					/*var menu = menuModel.get("menu");
    					var menuCollection = new MenuManager.Collections.Menu(menu);
    					menuCollection.parent = menuModel;
    					menuTree = new MenuManager.Views.Menu({collection : menuCollection });
    					menuModel.unset("menu");
    					App.Layout.menuLayout.tree.show(menuTree);*/
    					var menuTree = new MenuManager.Views.SingleMenu({model : menuModel });	
    					App.Layout.menuLayout.tree.show(menuTree);
    					
    					if (!App.appsView) {
    						App.apps = new MenuManager.Collections.App();
    						App.apps.fetch();
//    						App.appsView = new MenuManager.Views.App({collection : App.apps});
    					}
//    					App.Layout.menuLayout.functionalities.show(App.appsView);
    				},
    			});
    			
    		},
    		
    		createHost : function() {
    			App.Layout.contentRegion.show(new MenuManager.Views.HostCreate({model : new MenuManager.Models.Host()}));
    		},
    		
    		editHost : function(id) {
    			var hostModel = new MenuManager.Models.Host({id : id});
    			hostModel.fetch({
    				success: function() {
    					App.Layout.contentRegion.show(new MenuManager.Views.HostCreate({model : hostModel}));
    				}
    			});
    		}
    	}
    });
    
    return Router;
    
});