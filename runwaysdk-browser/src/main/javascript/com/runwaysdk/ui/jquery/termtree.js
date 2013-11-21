(function(){
  
/**
 * @class com.runwaysdk.ui.jquery.Treee.parentRelationshipCache A parent relationship cache that maps a childId to known parent records. This class is used internally only.
 */
Mojo.Meta.newClass('com.runwaysdk.ui.jquery.Treee.parentRelationshipCache', {
  
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
var tree = Mojo.Meta.newClass('com.runwaysdk.ui.jquery.Tree', {
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
      this.parentRelationshipCache = new com.runwaysdk.ui.jquery.Treee.parentRelationshipCache();
      
      this.selectCallbacks = [];
      this.deselectCallbacks = [];
      
      this.curSelected = null;
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
      
      $thisTree.tree(
        'appendNode',
        {
          label: this.rootTermId,
          id: this.rootTermId
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
        	
        	var applyCallback = {
        	    onSuccess : function(appliedRelDTO) {
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
     *    @onFailure com.runwaysdk.Exception Fails synchronously if term is the root node.
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
      
      com.runwaysdk.Facade.deleteChild(deleteCallback, parentRecord.relId);
    },
    
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
      var that = this;
      
      var duplicateLogic = function(callbacksName) {
        if (term == null) {
          // Request node from server
          var callback = {
            onSuccess : function(obj) {
              term = obj;
              that.termCache[term.getId()] = term;
              
              // invoke callbacks
              for (i = 0; i < that[callbacksName].length; ++i) {
                that[callbacksName][i](term);
              }
            },
            
            onFailure : function(obj) {
              throw new com.runwaysdk.Exception(obj);
            }
          };
          Mojo.Util.copy(new Mojo.ClientRequest(callback), callback);
          
          com.runwaysdk.Facade.get(callback, node.id);
        }
        else {
          // Invoke callbacks
          for (i = 0; i < that[callbacksName].length; ++i) {
            that[callbacksName][i](term);
          }
        }
      }
      
      if (e.node) {
        // node was selected
        var node = e.node;
        var term = this.termCache[node.id];
        
        if (this.curSelected != null) {
          this.__onNodeSelect({previous_node: this.curSelected});
        }
        this.curSelected = node;
        
        duplicateLogic("selectCallbacks");
      }
      else {
        // event.node is null
        // a node was deselected
        // e.previous_node contains the deselected node
        var node = e.previous_node;
        var term = this.termCache[node.id];
        
        this.curSelected = null;
        
        duplicateLogic("deselectCallbacks");
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
          onSuccess : function(termAndRels) {
             for (var i = 0; i < termAndRels.length; ++i) {
               var $tree = $(that.nodeId);
               var termId = termAndRels[i].getTerm().getId();
               var fetchedNode = $tree.tree("getNodeById", termId);
               
               var parentRecord = {parentId: e.node.id, relId: termAndRels[i].getRelationshipId(), relType: termAndRels[i].getRelationshipType()};
               that.parentRelationshipCache.put(termId, parentRecord);
               
               that.termCache[termId] = termAndRels[i].getTerm();
               
               if (fetchedNode == null) {
                 $tree.tree(
                   'appendNode',
                   {
                     label: termId,
                     id: termId
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
                   $tree.tree('getNodeById', termId)
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
        
        com.runwaysdk.Facade.getTermAllChildren(callback, e.node.id, 0, 0);
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