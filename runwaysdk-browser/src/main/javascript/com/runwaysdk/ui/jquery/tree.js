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
	 *   @param String obj.nodeId
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
    },
    
    /**
     * Sets the root term for the tree. The root term must be set before the tree can be used.
     * 
     * @param com.runwaysdk.business.TermDTO or String (Id) rootTerm The root term of the tree.
     */
    setRootTerm : function(rootTerm) {
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
		            id: childId
		        },
		        parentNode
		      );
        	
        	hisCallback.onSuccess(obj);
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
    },
  }
});

})();