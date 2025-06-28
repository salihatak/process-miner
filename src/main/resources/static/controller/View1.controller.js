sap.ui.define([
	"sap/m/MessageBox",
	"sap/ui/core/mvc/Controller"
], function(MessageBox, Controller) {
	"use strict";

	return Controller.extend("PMInterface.controller.View1", {
		
		onInit: function() {
			
			var model =  new sap.ui.model.json.JSONModel();
			model.setData({ processData: "", project: { pname : "", pdescription : "" }});
			this.getView().setModel(model);
			
			this.getProjects();
		},
		
		newAnalysis: function(){
			var oData = this.getView().getModel().getData();
			oData.processData = "";
			oData.project = { pname : "", pdescription : "" };
			oData.dataInfo = {};
			this.getView().getModel().refresh();
			// Reset the variant list
			var variantList = this.getView().byId('variantList');
			variantList.removeSelections(true);
			variantList.getItems().forEach(function(item) {
				item.setSelected(false);
			});
			// Clear Cytoscape graph if it exists
			if (window.cy) {
				window.cy.elements().remove();
			}
			// Navigate to the variant page
			this.getView().byId("navCon").to(this.getView().byId("variantPage"), "slide");	
		},

		onBack: function() {
			var that = this;
			this.getProjects(function(){
				that.getView().byId("navCon").back();
			});
		},		
		
		onUploadDataDialog: function () {
			if (!this._oDialog) {
				this._oDialog = sap.ui.xmlfragment("PMInterface.controller.forward", this);
				this._oDialog.setContentWidth("600px");
//				this._oDialog.setContentHeight("300px");
				this._oDialog.setModel(this.getView().getModel());
			}
			
			//jQuery.sap.syncStyleClass("sapUiSizeCompact", this.getView(), this._oDialog);
			this._oDialog.open();
		},
		
		onUploadSaveDialog: function () {
			if (!this._sDialog) {
				this._sDialog = sap.ui.xmlfragment("PMInterface.controller.save", this);
				this._sDialog.setContentWidth("400px");
//				this._oDialog.setContentHeight("300px");
				this._sDialog.setModel(this.getView().getModel());
			}
			
			//jQuery.sap.syncStyleClass("sapUiSizeCompact", this.getView(), this._oDialog);
			this._sDialog.open();
		},
		
		onUploadClose: function () {
			if (this._oDialog) {
				this._oDialog.destroy();
				delete this._oDialog;
			}
		},	
		
		onProjectClose: function () {
			if (this._sDialog) {
				this._sDialog.destroy();
				delete this._sDialog;
			}
		},	
		
		getProjects: function(callBack){
			var blockLayoutCell = this.byId('launchpad');
			blockLayoutCell.removeAllContent();
			var that = this;
			var oData = this.getView().getModel().getData();
			var pressTile = function(oEvent) {
				var pid = oEvent.getSource().getTooltip();
				that.getView().getModel().getData().dataInfo = {};
				$.ajax( {
					type : "GET",
					dataType : "json",
					url : "project/"+pid+"?t=" + new Date().getTime(),
					success : function(rtrn){
						oData.processData = rtrn.pdata;
						that.onUploadData();
						that.onRunWithData(null, true);
						
						that.getView().byId("navCon").to(that.getView().byId("variantPage"), "slide");
					}, error : function(jqXHR, textStatus, errorThrown){
						MessageBox.show("Hata oluştu", { icon: MessageBox.Icon.ERROR, title: "Bilgi", actions: MessageBox.Action.OK});
						console.log("Error: " + textStatus);
						callBack();
					}
				});				
			};
			
			var utime = new Date().getTime();
			$.ajax( {
				type : "GET",
				dataType : "json",
				url : "project?&t=" + new Date().getTime(),
				success : function(rtrn){
					var genericTile;
					for(var i = 0 ; i < rtrn.length ; i++){
												
						genericTile = new sap.m.GenericTile("g_" + utime + "_" + i, {
							header : rtrn[i].name,
							tileContent : [new sap.m.TileContent("image-tile-cont-" + utime + "-" + i, {
								footer : rtrn[i].createdBy,
								content : new sap.m.ImageContent({
										src : "sap-icon://instance"
										})
							})],
							press : pressTile,
							tooltip : rtrn[i].id
						});
						
						genericTile.addStyleClass("sapUiTinyMarginBegin sapUiTinyMarginTop tileLayout");
						blockLayoutCell.addContent(genericTile);
					}
					
					if(callBack)
						callBack();
				}, error : function(jqXHR, textStatus, errorThrown){
					MessageBox.show("Hata oluştu", { icon: MessageBox.Icon.ERROR, title: "Bilgi", actions: MessageBox.Action.OK});
					console.log("Error: " + textStatus);
					callBack();
				}
			});
		},
		
		onProjectSave: function () {
			var that = this;
			var oData = this.getView().getModel().getData();
			// Map UI model to Java Project object structure
			var projectPayload = {
				id: oData.project.id || "", // or generate a UUID if needed
				name: oData.project.pname || "",
				description: oData.project.pdescription || "",
				createdBy: oData.project.createdBy || "user",
				updatedBy: oData.project.updatedBy || "user",
				creationDate: oData.project.creationDate || null,
				process: oData.project.process || "",
				pdata: oData.processData || ""
			};
			$.ajax({
				type: "POST",
				dataType: "json",
				contentType: "application/json",
				data: JSON.stringify(projectPayload),
				url: "project?t=" + new Date().getTime(),
				success: function(ret) {
					that.onProjectClose();
					MessageBox.show(ret.message, { icon: MessageBox.Icon.SUCCESS, title: "Bilgi", actions: MessageBox.Action.OK });
				},
				error: function(jqXHR, textStatus, errorThrown) {
					MessageBox.show("Hata oluştu", { icon: MessageBox.Icon.ERROR, title: "Bilgi", actions: MessageBox.Action.OK });
					console.log("Error: " + textStatus);
				}
			});
		},
		
		onUploadData: function () {
			var that = this;
			var oData = this.getView().getModel().getData();
			$.ajax( {
				type : "POST",
				data : oData.processData,
				url : "process?t=" + new Date().getTime(),
				success : function(ret){
					console.log("Return: " + ret);
					that.onUploadClose();
				}, error : function(jqXHR, textStatus, errorThrown){
					console.log("Error: " + textStatus);
					that.onUploadClose();
				}
			});
		},	
		
		onRunWithData: function (oEvent, first) {
			var that = this;
			var oModel = this.getView().getModel();
			var oData = this.getView().getModel().getData();
			
			var variantList = this.getView().byId('variantList');
			var variantListItems = variantList.getSelectedContexts();
			
			var arrayOfVariants = [];
			if(!first){
				for(var i = 0 ; i < variantListItems.length ; i++)
					arrayOfVariants.push(oModel.getProperty(variantListItems[i].sPath));	
			}
			
			$.ajax( {
				type : "POST",
				dataType: "json",
				data : "selectedVariants=" + JSON.stringify(arrayOfVariants),
				url : "process/retrieveProcessData?t=" + new Date().getTime(),
				success : function(ret){
					
					ret.variants.sort(function(a, b){
						if (a.count > b.count)
							return -1;
						if (a.count < b.count)
							return 1;
						return 0;
					});

					for(var i = 0 ; i < ret.variants.length ; i++){
						ret.variants[i].No = '#' + (i + 1);
						ret.variants[i].diff = ret.totalCaseCount - ret.variants[i].count;
					}
					
					that.getView().getModel().getData().dataInfo = ret;
					
					that.drawFlowChart(ret.nodes, ret.edges);
					that.getView().getModel().refresh();
					
					var variantListAll = variantList.getItems();
					for(var i = 0 ; i < variantListAll.length ; i++){
						var itemData = oModel.getProperty(variantListAll[i].getBindingContextPath());
						variantListAll[i].setSelected(false);
						for(var j = 0 ; j < ret.selectedVariants.length ; j++){
							if(itemData.variantId ==  ret.selectedVariants[j].variantId)
								variantListAll[i].setSelected(true);
						}
					}
					
				}, error : function(jqXHR, textStatus, errorThrown){
					console.log("Error: " + textStatus);
					that.onUploadClose();
				}
			});
		},	
		
		drawFlowChart : function(nodes, edges){
			var cy = window.cy = cytoscape({
				  container: document.getElementById('cy'),

				  boxSelectionEnabled: false,
				  autounselectify: true,

				  layout: {
					name: 'dagre'
				  },

				  style: [
					{
					  selector: 'node',
					  style: {
						'content': 'data(id)',
//				        'text-opacity': 0.5,
						'text-valign': 'center',
						'text-halign': 'right',
						'background-color': '#1190B6',
						'shape' : 'hexagon'
					  }
					},

					{
					  selector: 'edge',
					  style: {
						'content': 'data(weight)',
						'curve-style': 'bezier',
						'width': 'mapData(weight, 70, 100, 2, 6)',
						'target-arrow-shape': 'triangle',
						'line-color': '#97D1E4',
						'target-arrow-color': '#97D1E4'
					  }
					}
				  ],

				  elements: {
					  nodes: nodes,
					  edges: edges
					},
				});			
		},
		
		onSelectAll: function() {
			var variantList = this.getView().byId('variantList');
			var items = variantList.getItems();
			items.forEach(function(item) {
				item.setSelected(true);
			});
		},
		
		onSelectNone: function() {
			var variantList = this.getView().byId('variantList');
			var items = variantList.getItems();
			items.forEach(function(item) {
				item.setSelected(false);
			});
		},		
	});

});