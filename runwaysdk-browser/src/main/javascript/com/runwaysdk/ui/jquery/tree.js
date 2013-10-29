(function(){

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
     * @param com.runwaysdk.business.TermDTO child The child term that will be added to the tree.
     * @param com.runwaysdk.business.TermDTO parent The parent term that the child will be appended under. If null, it will be appended to the root.
     */
    addChild : function(child, parent) {
      var $thisTree = $(this.nodeId);
      
      if (parent == null) {
    	var $parent = null;
      }
      else {
        var $parent = $(parent.getId());
      }
      
      $thisTree.tree(
        'appendNode',
        {
            label: child.getLabel(),
            id: child.getId()
        },
        $parent
      );
    }
  }
});

})();