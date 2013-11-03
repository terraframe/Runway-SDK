(function(){

var callbackHandler = Mojo.Meta.newClass('com.runwaysdk.jquery.tree.CallbackHandler', {
    
    Instance: {
      initialize: function(yuiTest, obj){
        Mojo.Util.copy(new Mojo.ClientRequest(obj), this);
        this.yuiTest = yuiTest;
      },
      
      onSuccess: function() {
        
      },
      onFailure: function() {
        
      }
    }
});

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
      
      this.cache = {};
    },
    
    /**
     * Internal, is binded to node open and loads new nodes from the server, if necessary.
     */
    __onNodeOpen : function(e) {
      var node = e.node;
      var that = this;
      
      if (this.cache[e.node.id] == null || this.cache[e.node.id] == undefined) {
        var callback = {
          onSuccess : function(obj) {
             for (var i = 0; i < obj.length; ++i) {
               var $tree = $(that.nodeId);
               var fetchedNode = $tree.tree("getNodeById", obj[i].getId());
               
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
             
             that.cache[node.id] = true;
          },
          
          onFailure : function(obj) {
            // TODO
          }
        };
        Mojo.Util.copy(new Mojo.ClientRequest(callback), callback);
        
        Mojo.$.com.runwaysdk.Facade.getChildren(callback, e.node.id, e.node.relationshipType);
      }
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
    },
    
    /**
     * Adds a child to the tree under parent with the given relationship.
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
      
      var parentNode = $thisTree.tree('getNodeById', parentId);
      if (parentNode == null || parentNode == undefined) {
        throw new com.runwaysdk.Exception("The provided parent [" + parentId + "] does not exist in this tree.");
      }
      
      var hisCallback = callback;
      var myCallback = {
        onSuccess : function(obj) {
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
        	    onSuccess : function() {
        	      hisCallback.onSuccess(obj);
        	    },
        	    
        	    onFailure : function() {
        	      hisCallback.onFailure(obj);
        	    }
        	}
        	Mojo.Util.copy(new Mojo.ClientRequest(applyCallback), applyCallback);
        	
        	obj.apply(applyCallback);
        },
        
        onFailure : function(obj) {
          hisCallback.onFailure(obj);
        }
      };
      Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
      
      Mojo.$.com.runwaysdk.Facade.addChild(myCallback, parentId, childId, relationshipType);
    },
    
    /**
     * @param com.runwaysdk.business.TermDTO or String (Id) term A term in the tree 
     */
    expandNode : function(term, callback) {
      
    },
    
    /**
     * @param com.runwaysdk.business.TermDTO or String (Id) term The term to remove from the tree.
     */
    removeTerm : function(term, callback) {
      var termId = (term instanceof Object) ? term.getId() : term;
      
      var $thisTree = $(this.nodeId);
      
      var hisCallback = callback;
      var myCallback = {
        onSuccess : function(obj) {
          $thisTree.tree(
            'removeNode',
        	  $thisTree.tree('getNodeById', termId)
          );
        	
          hisCallback.onSuccess(obj);
        },
        
        onFailure : function(obj) {
          hisCallback.onFailure(obj);
        }
      };
      Mojo.Util.copy(new Mojo.ClientRequest(myCallback), myCallback);
      
      Mojo.$.com.runwaysdk.Facade.aChild(myCallback, parentId, childId, relationshipType);
    }
  }
});

})();