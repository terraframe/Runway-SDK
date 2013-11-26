/*
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */

(function(){
  
/**
 * @class com.runwaysdk.ui.ontology.ParentRelationshipCache A parent relationship cache that maps a childId to known parent records. This class is used internally only.
 */
var ParentRelationshipCache = Mojo.Meta.newClass('com.runwaysdk.ui.ontology.ParentRelationshipCache', {
  
  IsAbstract : false,
  
  Instance : {

    initialize : function()
    {
      // Map<childId, record[]>
      this.cache = {};
    },
    
    /**
     * @param childId
     * @param record {parentId, relId, relType}
     */
    put : function(childId, record) {
      var cacheRecordArray = this.cache[childId] ? this.cache[childId] : [];
      
      // If the record is already in the cache, update it and return.
      for (var i = 0; i < cacheRecordArray.length; ++i) {
        if (cacheRecordArray[i].parentId === record.parentId && cacheRecordArray[i].relType === record.relType) {
          this.cache[childId][i].relId = record.relId;
          return;
        }
      }
      
      // else add the new record to the cache
      if (this.cache[childId] == null || this.cache[childId] == undefined) {
        this.cache[childId] = [];
      }
      
      this.cache[childId].push(record);
    },
    
    /**
     * @returns record[] {parentId, relId, relType}
     */
    get : function(childId) {
      return this.cache[childId] ? this.cache[childId] : [];
    }
  }
});

/**
 * @class com.runwaysdk.ui.jquery.Tree A wrapper around JQuery widget jqTree to allow for integration with Term objects.
 * 
 * @constructs
 * @param obj
 *   @param String obj.nodeId The id of the div defined in html, specifying the location for the tree. The id is prefixed with #.
 *   @param Object obj.data Optional, a properly formatted data object as documented by jqTree.
 *   @param Boolean obj.dragDrop Optional, set to true to enable drag drop, false to disable. Default is false.
 */
var tree = Mojo.Meta.newClass('com.runwaysdk.ui.ontology.TermTree', {
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      
      var data = {};
      if (obj.data != null) {
        data = obj.data;
      }
      
      var dragDrop = false;
      if (obj.dragDrop != null) {
        dragDrop = obj.dragDrop;
      }
      
      // Create the jqTree
      $(function() {
        $(obj.nodeId).tree({
            data: data,
            dragAndDrop: dragDrop,
            useContextMenu: false,
            onCreateLi: function(node, $li) {
              com.runwaysdk.ui.DOMFacade.getChildren($li[0])[0].__runwayid = node.id;
            }
        });
      });
      
      this.nodeId = obj.nodeId;
      var $tree = $(this.nodeId);
      
      $tree.bind(
          'tree.open',
          Mojo.Util.bind(this, this.__onNodeOpen)
      );
//      $tree.bind(
//          'tree.select',
//          Mojo.Util.bind(this, this.__onNodeSelect)
//      );
      $tree.bind(
          'tree.move',
          Mojo.Util.bind(this, this.__onNodeMove)
      );
      $tree.bind(
          'tree.contextmenu',
          Mojo.Util.bind(this, this.__onNodeRightClick)
      );
      
      this.hasFetchedChildren = {};
      this.termCache = {};
      this.parentRelationshipCache = new ParentRelationshipCache();
      
      this.selectCallbacks = [];
      this.deselectCallbacks = [];
      
//      this.curSelected = null;
      
      
      
      
      
      // Create the context menu
      $.contextMenu({
          selector: ".jqtree-element",
          items: {
              "copy": {name: "Create Under", icon: "copy", callback: Mojo.Util.bind(this, this.__onContextCreateClick)},
              "edit": {name: "Edit", icon: "edit", callback: Mojo.Util.bind(this, this.__onContextEditClick)},
//                "cut": {name: "Cut", icon: "cut"},
//                "paste": {name: "Paste", icon: "paste"},
              "delete": {name: "Delete", icon: "delete", callback: Mojo.Util.bind(this, this.__onContextDeleteClick)},
//                "sep1": "---------",
//                "quit": {name: "Quit", icon: "quit"}
          }
      });
      
//      factory = com.runwaysdk.ui.Manager.getFactory("Runway");
      
//      container = factory.newElement("div");
//      container.setId("container");
//      container.render(el);
//      
//      aa = factory.newElement("div");
//      aa.addClassName("hasmenu");
//      aa.setInnerHTML("AA");
//      aa.render(container);
//      
//      bb = factory.newElement("div");
//      bb.addClassName("hasmenu");
//      bb.setInnerHTML("BB");
//      bb.render(container);
//      
//      cc = factory.newElement("div");
//      cc.addClassName("hasmenu");
//      cc.setInnerHTML("CC");
//      cc.render(container);
//      
//      $(this.nodeId).contextmenu({
//          delegate: "*",
//          menu: [
//              {title: "Copy", cmd: "copy", uiIcon: "ui-icon-copy"},
//              {title: "----"},
//              {title: "More", children: [
//                  {title: "Sub 1", cmd: "sub1"},
//                  {title: "Sub 2", cmd: "sub1"}
//                  ]}
//              ],
//          select: function(event, ui) {
//              alert("select " + ui.cmd + " on " + ui.target.text());
//          }
//      });
    },
    
    /**
     * Sets the root term for the tree. The root term must be set before the tree can be used.
     * 
     * @param com.runwaysdk.business.TermDTO or String (Id) rootTerm The root term of the tree.
     */
    setRootTerm : function(rootTerm) {
      this.__assertRequire("rootTerm", rootTerm);
      
      this.rootTermId = (rootTerm instanceof Object) ? rootTerm.getId() : rootTerm;
      
      var $thisTree = $(this.nodeId);
      
      this.__createTreeNode(this.rootTermId);
    },
    
    /**
     * Registers a function to on term select.
     * 
     * @param Function callback A function with argument 'term', the selected term. 
     */
    registerOnTermSelect : function(callback) {
      this.__assertRequire("callback", callback);
      
      this.selectCallbacks.push(callback);
    },
    
    /**
     * Registers a function to on term deselect.
     * 
     * @param Function callback A function with argument 'term', the deselected term. 
     */
    registerOnTermDeselect : function(callback) {
      this.__assertRequire("callback", callback);
      
      this.deselectCallbacks.push(callback);
    },
    
    /**
     * Returns the jqTree node that represents the given term.
     * 
     * @param com.runwaysdk.business.TermDTO or String (Id) term The term.
     */
    getJQNodeFromTerm : function(term) {
      this.__assertPrereqs();
      this.__assertRequire("term", term);
      
      var termId = (term instanceof Object) ? term.getId() : term;
      
      return $(this.nodeId).tree('getNodeById', termId);
    },
    
    /**
     * Adds a child term to the tree under parent with the given relationship.
     * 
     * @param com.runwaysdk.business.TermDTO or String (id) child The child term that will be added to the tree.
     * @param com.runwaysdk.business.TermDTO or String (id) parent The parent term that the child will be appended under.
     * @param String relationshipType The relationship type that the child will be appended with. The relationship type must extend com.runwaysdk.business.TermRelationship.
     * @param Object callback A callback object with onSuccess and onFailure methods.
     */
    addChild : function(child, parent, relationshipType, callback) {
      this.__assertPrereqs();
      this.__assertRequire("child", child);
      this.__assertRequire("parent", parent);
      this.__assertRequire("relationshipType", relationshipType);
      this.__assertRequire("callback", callback);
      
      var childId = (child instanceof Object) ? child.getId() : child;
      var parentId = (parent instanceof Object) ? parent.getId() : parent;
      
      var $thisTree = $(this.nodeId);
      var that = this;
      
      var parentNode = $thisTree.tree('getNodeById', parentId);
      if (parentNode == null || parentNode == undefined) {
        var ex = com.runwaysdk.Exception("The provided parent [" + parentId + "] does not exist in this tree.");
        this.__handleException(ex);
        return;
      }
      
      var hisCallback = callback;
      var myCallback = {
        onSuccess : function(relDTO) {
        	var applyCallback = {
        	    onSuccess : function(appliedRelDTO) {
        	      that.__createTreeNode(childId, parentNode);
        	      
        	      var parentRecord = {parentId: parentId, relId: appliedRelDTO.getId(), relType: appliedRelDTO.getType()};
        	      that.parentRelationshipCache.put(childId, parentRecord);
        	      
        	      hisCallback.onSuccess(appliedRelDTO);
        	    },
        	    
        	    onFailure : function(obj) {
        	      hisCallback.onFailure(obj);
        	    }
        	}
        	Mojo.Util.copy(new Mojo.ClientRequest(applyCallback), applyCallback);
        	
        	relDTO.apply(applyCallback);
        },
        
        onFailure : function(obj) {
          hisCallback.onFailure(obj);
        }
      };
      Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
      
      com.runwaysdk.Facade.addChild(myCallback, parentId, childId, relationshipType);
    },
    
    /**
     * Removes the term and all its children from the tree and notifies the server to remove the relationship in the database.
     * 
     * @returns
     *    @onFailure com.runwaysdk.Exception Fails synchronously if the term is not mapped to a parent record in the parentRelationshipCache.  
     *    @onFailure com.runwaysdk.Exception Fails synchronously if the term is the root node.
     * 
     * @param com.runwaysdk.business.TermDTO or String (Id) term The term to remove from the tree.
     * @param String relationshipType The relationship type that the term has with its parent.
     * @param Object callback A callback object with onSuccess and onFailure methods.
     */
    removeTerm : function(term, callback) {
      this.__assertPrereqs();
      this.__assertRequire("term", term);
      this.__assertRequire("callback", callback);
      
      var termId = (term instanceof Object) ? term.getId() : term;
      
      if (termId === this.rootTermId) {
        var ex = new com.runwaysdk.Exception("You cannot delete the root node.");
        callback.onFailure(ex);
        return;
      }
      
      // TODO : this will not work with multiple relationships
      var parentRecord = this.parentRelationshipCache.get(termId)[0];
      if (parentRecord == null || parentRecord == undefined) {
        var ex = new com.runwaysdk.Exception("The term [" + term + "] is not mapped to a parent record in the parentRelationshipCache.");
        callback.onFailure(ex);
        return;
      }
      
      var that = this;
      
      var $thisTree = $(this.nodeId);
      
      var hisCallback = callback;
      var deleteCallback = {
        onSuccess : function(obj) {
          $thisTree.tree(
            'removeNode',
            $thisTree.tree('getNodeById', termId)
          );
          
//          if (that.curSelected.id === termId) {
//            that.curSelected = null;
//          }
          
          hisCallback.onSuccess(obj);
        },
        
        onFailure : function(obj) {
          hisCallback.onFailure(obj);
          return;
        }
      }
      Mojo.Util.copy(new Mojo.ClientRequest(deleteCallback), deleteCallback);
      
      com.runwaysdk.Facade.deleteChild(deleteCallback, parentRecord.relId);
    },
    
    /**
     * Internal, is binded to context menu option Create. 
     */
    __onContextCreateClick : function(key, opt) {
      var dialog = this.getFactory().newDialog("Create Term", {modal: true});
      
      
      // Form
      var form = this.getFactory().newForm("Test Form");
      
      var fNameTextInput = this.getFactory().newFormControl('text', 'firstName');
      form.addEntry("First Name", fNameTextInput);
      
      var lNameTextInput = this.getFactory().newFormControl('text', 'lastName');
      form.addEntry("Last name", lNameTextInput);
      
      dialog.appendChild(form);
      
      var that = this;
      
      
      var cancelCallback = function() {
        dialog.close();
      };
      dialog.addButton("Cancel", cancelCallback);
      
      var submitCallback = function() {
        var values = form.accept(that.getFactory().newFormControl('FormVisitor'));
        dialog.close();
        
        var applyCallback = {
          onSuccess : function(term) {
            var addChildCallback = {
              onSuccess : function(relat) {
                
              },
              
              onFailure : function(err) {
                that.__handleException(err);
                return;
              }
            };
            
            that.addChild(term, opt.$trigger[0].__runwayid, "com.runwaysdk.jstest.business.ontology.Sequential", addChildCallback);
          },
          
          onFailure : function(err) {
            that.__handleException(err);
            return;
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(applyCallback), applyCallback);
        
        var al = new com.runwaysdk.jstest.business.ontology.Alphabet();
        al.apply(applyCallback);
      };
      dialog.addButton("Submit", submitCallback);
      
      dialog.render();
    },
    
    /**
     * Internal, is binded to context menu option Edit. 
     */
    __onContextEditClick : function(key, opt) {
      var termId = opt.$trigger[0].__runwayid;
      
      var dialog = this.getFactory().newDialog("Edit Term", {modal: true});
      
      // Form
      var form = this.getFactory().newForm("Test Form");
      
      // TODO : add fields to the term
      var fNameTextInput = this.getFactory().newFormControl('text', 'firstName');
      form.addEntry("First Name", fNameTextInput);
      
      var lNameTextInput = this.getFactory().newFormControl('text', 'lastName');
      form.addEntry("Last name", lNameTextInput);
      
      dialog.appendChild(form);
      
      var that = this;
      
      
      var cancelCallback = function() {
        dialog.close();
      };
      dialog.addButton("Cancel", cancelCallback);
      
      var submitCallback = function() {
        var values = form.accept(that.getFactory().newFormControl('FormVisitor'));
        dialog.close();
        
        var getTermCallback = {
          onSuccess : function(term) {
            var lockCallback = {
              onSuccess : function(relat) {
                // TODO : read the values and change the term
                
                var applyCallback = {
                  onSuccess : function(term) {
                    
                  },
                  onFailure : function(err) {
                    that.__handleException(err);
                    return;
                  }
                }
              },
              
              onFailure : function(err) {
                that.__handleException(err);
                return;
              }
            };
            Mojo.Util.copy(new Mojo.ClientRequest(lockCallback), lockCallback);
            
            term.lock(lockCallback);
          },
          
          onFailure : function(err) {
            that.__handleException(err);
            return;
          }
        };
        
        that.__getTermFromId(termId, getTermCallback);
      };
      dialog.addButton("Submit", submitCallback);
      
      dialog.render();
    },
    
    /**
     * Internal, is binded to context menu option Delete. 
     */
    __onContextDeleteClick : function(key, opt) {
      var termId = opt.$trigger[0].__runwayid;
      var that = this;
      
      if (termId === this.rootTermId) {
        var ex = new com.runwaysdk.Exception("You cannot delete the root node.");
        this.__handleException(ex);
        return;
      }
      // TODO : this will not work with multiple relationships
      var parentRecord = this.parentRelationshipCache.get(termId)[0];
      if (parentRecord == null || parentRecord == undefined) {
        var ex = new com.runwaysdk.Exception("The term [" + term + "] is not mapped to a parent record in the parentRelationshipCache.");
        this.__handleException(ex);
        return;
      }
      
      var deleteCallback = {
          onSuccess : function() {
            
          },
          onFailure : function(err) {
            that.__handleException(err);
            return;
          }
      }
      var deleteHandler = function() {
        that.removeTerm(termId, deleteCallback);
        dialog.close();
      };
      var cancelHandler = function() {
        dialog.close();
      };
      
      var dialog = this.getFactory().newDialog("Delete Term", {modal: true, width: 550, height: 250});
      dialog.appendContent("Are you sure you want to delete the term:<br/>");
      dialog.appendContent(termId + "<br/><br/>");
      dialog.appendContent("all of its children, and the associated relationship:<br/>");
      dialog.appendContent(parentRecord.relId);
      dialog.addButton("Delete Term", deleteHandler);
      dialog.addButton("Cancel", cancelHandler);
      dialog.render();
    },
    
    /**
     * Internal, is binded to tree.contextmenu, called when the user right clicks on a node.
     */
    __onNodeRightClick : function(e) {
//      e.preventDefault();
      
//      var node = e.node;
//      var that = this;
//      
//      var callback = {
//        onSuccess : function(term) {
//          var jq = $(that.nodeId);
//          jq.contextMenu(true);
//          jq.contextMenu();
//          jq.contextMenu(false);
//          
//          var pos = com.runwaysdk.ui.DOMFacade.getMousePos();
//          
//          var cmenu = $(".context-menu-root")[0];
//          com.runwaysdk.ui.DOMFacade.setPos(cmenu, pos.x + "px", pos.y + "px");
//        },
//        onFailure : function(err) {
//          that.__handleException(err);
//          return;
//        }
//      };
//      this.__getTermFromId(node.id, callback);
    },
    
    /**
     * Internal, is binded to jqtree's node move event. You can prevent the move by calling event.preventDefault()
     */
    __onNodeMove : function(event) {
      var movedNode = event.move_info.moved_node;
      var targetNode = event.move_info.target_node;
      var previousParent = event.move_info.previous_parent;
      
      $("#" + moved_node.id).contextmenu("open", $(""));
    },
    
    /**
     * Internal, Is binded to jqtree's node select (and deselect) event and invokes our term select listeners.
     */
    __onNodeSelect : function(e) {
//      var that = this;
//      
//      var callback = {
//        onFailure : function(err) {
//          that.__handleException(err);
//          return;
//        }
//      }
//      
//      if (e.node) {
//        // node was selected
//        if (this.curSelected != null) {
//          this.__onNodeSelect({previous_node: this.curSelected});
//        }
//        this.curSelected = e.node;
//        
//        callback.onSuccess = function(term) {
//          // invoke our term listeners
//          for (i = 0; i < that.selectCallbacks.length; ++i) {
//            that.selectCallbacks[i](term);
//          }
//        }
//        this.__getTermFromId(e.node.id, callback);
//      }
//      else {
//        // event.node is null
//        // a node was deselected
//        // e.previous_node contains the deselected node
//        var node = e.previous_node;
//        var term = this.termCache[node.id];
//        
//        this.curSelected = null;
//        
//        callback.onSuccess = function(term) {
//          // invoke our term listeners
//          for (i = 0; i < that.deselectCallbacks.length; ++i) {
//            that.deselectCallbacks[i](term);
//          }
//        }
//        this.__getTermFromId(node.id, callback);
//      }
    },
    
    /**
     * Internal, is binded to jqtree's node open event and loads new nodes from the server with a getChildren request, if necessary.
     */
    __onNodeOpen : function(e) {
      var node = e.node;
      var that = this;
      
      if (this.hasFetchedChildren[e.node.id] == null || this.hasFetchedChildren[e.node.id] == undefined) {
        var callback = {
          onSuccess : function(termAndRels) {
             for (var i = 0; i < termAndRels.length; ++i) {
               var $tree = $(that.nodeId);
               var termId = termAndRels[i].getTerm().getId();
               var fetchedNode = $tree.tree("getNodeById", termId);
               
               var parentRecord = {parentId: e.node.id, relId: termAndRels[i].getRelationshipId(), relType: termAndRels[i].getRelationshipType()};
               that.parentRelationshipCache.put(termId, parentRecord);
               
               that.termCache[termId] = termAndRels[i].getTerm();
               
               if (fetchedNode == null) {
                 that.__createTreeNode(termId, node);
               }
             }
             
             that.hasFetchedChildren[node.id] = true;
          },
          
          onFailure : function(err) {
            that.__handleException(err);
            return;
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(callback), callback);
        
        com.runwaysdk.Facade.getTermAllChildren(callback, e.node.id, 0, 0);
      }
    },
    
    /**
     * Internal method, do not call.
     */
    __handleException : function(ex) {
      var dialog = this.getFactory().newDialog("Error", {modal: true});
      dialog.appendContent(ex.getLocalizedMessage() || ex.getMessage() || ex.getDeveloperMessage());
      dialog.addButton("Ok", function(){dialog.close();});
      dialog.render();
      
//      throw ex;
    },
    
    /**
     * Internal method, do not call.
     */
    __assertPrereqs : function() {
      if (this.rootTermId == null || this.rootTermId == undefined) {
        var ex = new com.runwaysdk.Exception("You must call setRootTerm first before you can use this method.");
        this.__handleException(ex);
      }
    },
    
    /**
     * Internal method, do not call.
     */
    __assertRequire : function(name, value) {
      if (value == null || value == undefined) {
        var ex = new com.runwaysdk.Exception("Parameter [" + name + "] is required.");
        this.__handleException(ex);
      }
    },
    
    /**
     * Internal, attempts to find the node in the cache, if the node does not exist in the cache it will request it from the server.
     * 
     * @returns
     *   @onSuccess com.runwaysdk.business.TermDTO term The requested term.
     * 
     * @param String termId The id of the requested term.
     * @param Object callback A callback object with onSuccess and onFailure methods.
     */
    __getTermFromId : function(termId, hisCallback) {
      var term = this.termCache[termId];
      
      var that = this;
      
      if (term == null) {
        // Request node from server
        var myCallback = {
          onSuccess : function(obj) {
            term = obj;
            that.termCache[term.getId()] = term;
            
            hisCallback.onSuccess(term);
          },
          
          onFailure : function(obj) {
            hisCallback.onFailure(obj);
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
        
        com.runwaysdk.Facade.get(myCallback, termId);
      }
      else {
        hisCallback.onSuccess(term);
      }
    },
    
    /**
     * Internal, creates a new jqTree node and appends it to the tree.
     */
    __createTreeNode : function(childId, parentNode) {
      var $thisTree = $(this.nodeId);
      
      if (parentNode == null || parentNode == undefined) {
        $thisTree.tree(
          'appendNode',
          {
              label: childId,
              id: childId
          }
        );
      }
      else {
        $thisTree.tree(
          'appendNode',
          {
              label: childId,
              id: childId
          },
          parentNode
        );
        $thisTree.tree(
          'appendNode',
          {
              label: "",
              phantom: true
          },
          $thisTree.tree('getNodeById', childId)
        );
      }
      
      $thisTree.tree('getNodeById', childId).element.__runwayid = childId;
    },
    
    getFactory : function() {
      return com.runwaysdk.ui.Manager.getFactory();
    }
    
    /**
     * Returns the relationships that the term has with its parent. The relationships may be cached and the method may return
     * synchronously. The cache may or may not contain all relationships the term has with its parent.
     * 
     * @param com.runwaysdk.business.TermDTO or String (Id) term The term to remove from the tree.
     * @param Object callback A callback object with onSuccess and onFailure methods.
     * @returns com.runwaysdk.business.TermRelationship[] The relationships.
     */
//    getRelationshipsWithParent : function(term, callback) {
//      this.__assertPrereqs();
//      this.__assertRequire("term", term);
//      this.__assertRequire("callback", callback);
//      
//      var termId = (term instanceof Object) ? term.getId() : term;
//      
//      var that = this;
//      
//      var hisCallback = callback;
//      var myCallback = {
//        onSuccess : function(relationships) {
//          hisCallback(relationships);
//        },
//        
//        onFailure : function(err) {
//          hisCallback.onFailure(err);
//          return;
//        }
//      };
//      Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
//      
//      if (this.parentRelationshipCache[termId] != null && this.parentRelationshipCache != undefined) {
//        myCallback.onSuccess([this.parentRelationshipCache[termId]]);
//      }
//      else {
//        com.runwaysdk.Facade.getParentRelationships(myCallback, termId, relationshipType);
//      }
//    },
  }
});

})();