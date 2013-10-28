(function(){

var tree = Mojo.Meta.newClass('com.runwaysdk.ui.jquery.Tree', {
  Instance : {
	/**
	 * @class A wrapper around JQuery widget jqTree to allow for integration with Term objects.
	 * 
	 * @constructs
	 * @param obj
	 *   @param String obj.nodeId
	 */
    initialize : function(obj){
      this.$initialize(obj);
      
      $(function() {
        $(nodeId).tree({
            data: data,
            dragAndDrop: true
        });
      });
      
      this.nodeId = nodeId;
    },
    
    /**
     * @param com.runwaysdk.business.TermDTO child The child term that will be added to the tree.
     * @param com.runwaysdk.business.TermDTO parent The parent term that the child will be appended under. If null, it will be appended to the root.
     */
    addChild : function(child, parent) {
      var $thisTree = $(this.nodeId);
      
      if (parent == null) {
        var $parent = $(parent.getId());
      }
      else {
        var $parent = null;
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