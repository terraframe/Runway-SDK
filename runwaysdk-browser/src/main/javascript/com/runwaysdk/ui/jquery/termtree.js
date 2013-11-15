(function(){
var tree = Mojo.Meta.newClass('com.runwaysdk.ui.jquery.Tree', {
  Instance : {
	/**
	 * @class A wrapper around JQuery widget jqTree to allow for integration with Term objects.
	 * 
	 * @constructs
	 * @param obj
	 *   @param String obj.nodeId The id of the div defined in html, specifying the location for the tree. The id is prefixed with #.
	 *   @param Object obj.data Optional, a properly formatted data object as documented by jqTree.
	 *   @param Boolean obj.dragDrop Optional, set to true to enable drag drop, false to disable. Default is false.
	 */
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
      
      $(function() {
        $(obj.nodeId).tree({
            data: data,
            dragAndDrop: dragDrop
        });
      });
      
      this.nodeId = obj.nodeId;
      var $tree = $(this.nodeId);
      
      $tree.bind(
          'tree.open',
          Mojo.Util.bind(this, this.__onNodeOpen)
      );
      $tree.bind(
          'tree.select',
          Mojo.Util.bind(this, this.__onNodeSelect)
      );
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
      this.parentRelationshipCache = {};
      
      this.selectCallbacks = [];
      this.deselectCallbacks = [];
      
      this.curSelected = null;
    },
    
    /**
     * Sets the root term for the tree. The root term must be set before the tree can be used.
     * 
     * @param com.runwaysdk.business.TermDTO or String (Id) rootTerm The root term of the tree.
     * @param String relationshipType This parameter is required because the relationship type is currently stored on all nodes.
     *                                  This parameter can be removed when we have a facade method that returns all children regardless
     *                                  of their relationship type.
     */
    setRootTerm : function(rootTerm, relationshipType) {
      this.__assertRequire("rootTerm", rootTerm);
      this.__assertRequire("relationshipType", relationshipType);
      
      this.rootTermId = (rootTerm instanceof Object) ? rootTerm.getId() : rootTerm;
      
      var $thisTree = $(this.nodeId);
      
      $thisTree.tree(
        'appendNode',
        {
          label: this.rootTermId,
          id: this.rootTermId,
          relationshipType: relationshipType
        }
      );
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
        throw new com.runwaysdk.Exception("The provided parent [" + parentId + "] does not exist in this tree.");
      }
      
      var hisCallback = callback;
      var myCallback = {
        onSuccess : function(relDTO) {
        	$thisTree.tree(
		        'appendNode',
		        {
		            label: childId,
		            id: childId,
		            relationshipType: relationshipType
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
        	
        	var applyCallback = {
        	    onSuccess : function(appliedRelDTO) {
        	      that.parentRelationshipCache[childId] = appliedRelDTO;
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
      
      Mojo.$.com.runwaysdk.Facade.addChild(myCallback, parentId, childId, relationshipType);
    },
    
    /**
     * @param com.runwaysdk.business.TermDTO or String (Id) term The term to remove from the tree.
     * @param String relationshipType
     * @param Object callback
     */
    removeTerm : function(term, relationshipType, callback) {
      this.__assertPrereqs();
      this.__assertRequire("term", term);
      this.__assertRequire("relationshipType", relationshipType);
      this.__assertRequire("callback", callback);
      
      var termId = (term instanceof Object) ? term.getId() : term;
      
      if (termId === this.rootTermId) {
        var ex = new com.runwaysdk.Exception("You cannot delete the root node.");
        callback.onFailure(ex);
        return;
      }
      
      var that = this;
      
      var $thisTree = $(this.nodeId);
      
      var hisCallback = callback;
      var myCallback = {
        onSuccess : function(relationships) {
          
          var deleteCallback = {
            onSuccess : function(obj) {
              $thisTree.tree(
                'removeNode',
                $thisTree.tree('getNodeById', termId)
              );
              
              if (that.curSelected.id === termId) {
                that.curSelected = null;
              }
              
              hisCallback.onSuccess(obj);
            },
            
            onFailure : function(obj) {
              hisCallback.onFailure(obj);
              return;
            }
          }
          Mojo.Util.copy(new Mojo.ClientRequest(deleteCallback), deleteCallback);
          
          Mojo.$.com.runwaysdk.Facade.deleteChild(deleteCallback, relationships[0].getId());
        },
        
        onFailure : function(err) {
          hisCallback.onFailure(err);
          return;
        }
      };
      Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
      
      if (this.parentRelationshipCache[termId] != null && this.parentRelationshipCache != undefined) {
        myCallback.onSuccess([this.parentRelationshipCache[termId]]);
      }
      else {
        Mojo.$.com.runwaysdk.Facade.getParentRelationships(myCallback, termId, relationshipType);
      }
    },
    
    /**
     * Internal, is binded to tree.contextmenu, called when the user right clicks on a node.
     */
    __onNodeRightClick : function(e) {
      var node = event.node;
      
      
    },
    
    /**
     * Internal, is binded to node move. You can prevent the move by calling event.preventDefault()
     */
    __onNodeMove : function(event) {
      var movedNode = event.move_info.moved_node;
      var targetNode = event.move_info.target_node;
      var previousParent = event.move_info.previous_parent;
      
      $("#" + moved_node.id).contextmenu("open", $(""));
    },
    
    /**
     * Internal, Is binded to node select and calls listeners.
     */
    __onNodeSelect : function(e) {
      if (e.node) {
        // node was selected
        var node = e.node;
        var term = this.termCache[node.id];
        var that = this;
        
        if (this.curSelected != null) {
          this.__onNodeSelect({previous_node: this.curSelected});
        }
        this.curSelected = node;
        
        if (term == null) {
          // Request node from server
          var callback = {
            onSuccess : function(obj) {
              term = obj;
              that.termCache[term.getId()] = term;
              
              for (i = 0; i < that.selectCallbacks.length; ++i) {
                that.selectCallbacks[i](term);
              }
            },
            
            onFailure : function(obj) {
              throw new com.runwaysdk.Exception(obj);
            }
          };
          Mojo.Util.copy(new Mojo.ClientRequest(callback), callback);
          
          Mojo.$.com.runwaysdk.Facade.get(callback, node.id);
        }
        else {
          for (i = 0; i < that.selectCallbacks.length; ++i) {
            that.selectCallbacks[i](term);
          }
        }
      }
      else {
        // event.node is null
        // a node was deselected
        // e.previous_node contains the deselected node
        var node = e.previous_node;
        var term = this.termCache[node.id];
        var that = this;
        
        this.curSelected = null;
        
        if (term == null) {
          // Request node from server
          var callback = {
            onSuccess : function(obj) {
              term = obj;
              that.termCache[term.getId()] = term;
              
              for (i = 0; i < that.deselectCallbacks.length; ++i) {
                that.deselectCallbacks[i](term);
              }
            },
            
            onFailure : function(obj) {
              throw new com.runwaysdk.Exception(obj);
            }
          };
          Mojo.Util.copy(new Mojo.ClientRequest(callback), callback);
          
          Mojo.$.com.runwaysdk.Facade.get(callback, node.id);
        }
        else {
          for (i = 0; i < that.deselectCallbacks.length; ++i) {
            that.deselectCallbacks[i](term);
          }
        }
      }
    },
    
    /**
     * Internal, is binded to node open and loads new nodes from the server with a getChildren request, if necessary.
     */
    __onNodeOpen : function(e) {
      var node = e.node;
      var that = this;
      
      if (this.hasFetchedChildren[e.node.id] == null || this.hasFetchedChildren[e.node.id] == undefined) {
        var callback = {
          onSuccess : function(obj) {
             for (var i = 0; i < obj.length; ++i) {
               var $tree = $(that.nodeId);
               var fetchedNode = $tree.tree("getNodeById", obj[i].getId());
               
               that.termCache[obj[i].getId()] = obj[i];
               
               if (fetchedNode == null) {
                 $tree.tree(
                   'appendNode',
                   {
                     label: obj[i].getId(),
                     id: obj[i].getId(),
                     relationshipType: node.relationshipType
                   },
                   node
                 );
                 // This node is a phantom node, it exists only to allow for expansion of nodes with unfetched children.
                 $tree.tree(
                   'appendNode',
                   {
                     label: "",
                     phantom: true
                   },
                   $tree.tree('getNodeById', obj[i].getId())
                 );
               }
             }
             
             that.hasFetchedChildren[node.id] = true;
          },
          
          onFailure : function(obj) {
            throw new com.runwaysdk.Exception(obj);
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(callback), callback);
        
        Mojo.$.com.runwaysdk.Facade.getChildren(callback, e.node.id, e.node.relationshipType);
      }
    },
    
    /**
     * Internal method, do not call.
     */
    __assertPrereqs : function() {
      if (this.rootTermId == null || this.rootTermId == undefined) {
        throw new com.runwaysdk.Exception("You must call setRootTerm first before you can use this method.");
      }
    },
    
    /**
     * Internal method, do not call.
     */
    __assertRequire : function(name, value) {
      if (value == null || value == undefined) {
        throw new com.runwaysdk.Exception("Parameter [" + name + "] is required.");
      }
    }
  }
});

})();