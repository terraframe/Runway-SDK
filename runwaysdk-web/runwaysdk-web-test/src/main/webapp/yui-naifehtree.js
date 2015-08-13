/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
var YUI_ONLINE_CONF = {};
YUI_OFFLINE_CONF = {
    base: "yui3/build/",
    combine:0,
    filter: 'raw',
    groups: {
        gallery: {
            base:'yui3-gallery/build/',
            patterns:  { 'gallery-': {} }
        },
        yui2: {
            base: '2in3/dist/2.9.0/build/',
            patterns:  { 
                'yui2-': {
                    configFn: function(me) {
                        if(/-skin|reset|fonts|grids|base/.test(me.name)) {
                            me.type = 'css';
                            me.path = me.path.replace(/\.js/, '.css');
                            me.path = me.path.replace(/\/yui2-skin/, '/assets/skins/sam/yui2-skin');
                        }
                    }
                } 
            }
        }
    }
 };
//ONLINE = (navigator.online) ? true : false;
ONLINE = false;
CURRENT_CONF = (ONLINE) ? YUI_ONLINE_CONF : YUI_OFFLINE_CONF;
  

var rwsortable = Mojo.Meta.newClass('com.runwaysdk.ui.yui3.Sortable', {
  Extends : MouseEvent,
  Implements : DragEventIF,
  Instance : {
    initialize : function()
    {
      this.$initialize();
    },
    getDataTransfer : function()
    {
      return this.getEvent().dataTransfer; // TODO normalize into wrapper class
    }
  }
});


  //////////////////////////////////////////////////////////
  // create a sortable list
  // from one of the examples.
  
  YUI(YUI_OFFLINE_CONF).use('dd-constrain', 'sortable', function(Y) {
    var list1 = new Y.Sortable({
        container: '#tree',
        nodes: 'li',
        opacity: '.5',
        moveType: "insert"
    });
    list1.delegate.dd.plug(Y.Plugin.DDConstrained, {
        constrain2node: '#tree'
    });

  // Using nested lists with Sortable has a bug... ticket filed!
   
    ///////////////////////////////////////////////////////////////
    // Handle the drag drop events
    // Key thing is 
    
    // nodes we're moving around
    var addedNode, newNode;



     // make a new node that can be inserted as a new tree sometimes
    Y.DD.DDM.on("drag:start", function(ev){
    
       //newNode = document.createElement("ol");
       newNode = Y.Node.create( "<ol></ol>" );
       newNode.setAttribute("id", Y.guid() );
     
       //newNode.appendChild( document.createElement( "ol" ) );
    
    });


    // insert the nodes where needed
    Y.DD.DDM.on("drag:over", function(ev){ 
    
        var t = ev.drop.get("node"),
            tOl = t.one( "ol" );
  
        // if we've over an li, add the new ol child block
        switch( t.get("nodeName").toLowerCase() ) {
        
           case "li":
                
                // try and append it to existing ol on the target
                if( tOl ) {
                  try {
                    tOl.append( ev.drag.get("node") );
                  } catch(e){}
                } 
                
                // else add a new ol to the target
                else {
                  
                  // remove it from where it was
                  if( addedNode !== undefined ) {
                    try{
                      addedNode.remove();
                    } catch(e){}
                  }
                  
                  // try adding it
                  try{
                    t.append( newNode );
                    newNode.append( ev.drag.get( "node" ) );
                 
                    addedNode = newNode;//.getAttribute("id") );
          
                    
                  } catch(e){ }
                }
                break;
         
        
        // if we're over an ol, just add this as a new li child
          case "ol":
              try{
                t.append( ev.drag.get("node" ) );
              }
              catch(e){}
              break;

          default:
              ev.halt();
              break;
              
        }
    
    });



    // reset things at the end of the drag
    Y.DD.DDM.after("drag:end", function( ev ){

       addedNode = undefined; 
       newNode = undefined;
       // DD somewhere sets some element styles, which mess up alignment somewhere
       // in IE
       ev.target.get("node").removeAttribute( "style" );
       
       // re-read the DOM of the tree to put + and - classes in the right places
       TreeView( "#tree" );
       
         
    } );
    
    
    
    
        
   ///////////////////////////////////////////////////
   // The 'TreeView'

   // add a delegated listener to expand collapsed sub trees
    Y.one( "#tree" ).delegate( "click" , function( ev ){

       if( ev.currentTarget.get('parentNode').one("ol") ){
         ev.currentTarget.get('parentNode').toggleClass("collapsed");
       }
    
    } , "li.haschildren span" );

    
    
    // add "haschildren" classes where needed
    // just iterates the DOM looking for li's with ol li children (ie a sub tree)
    var TreeView = function( el ){
    
      Y.each( Y.one( el ).all( "li" ) , function(node){ 
         
         if( node.one( "ol li" ) ) {
           node.addClass( "haschildren" );
         } else {
           node.removeClass( "haschildren" );
         }
      });
    
    }

   // call treeview first time
   TreeView( "#tree" ); 
   
   
   
      
   });



/*
  var Dom = YAHOO.util.Dom;
  


  YAHOO.util.Event.delegate( "tree" , "click" , function(ev , el){
     console.log(arguments);  
     
     
     if( Dom.hasClass( el.parentNode , "collapsed" ) ){
     
       Dom.removeClass( el.parentNode , "collapsed" );
     
     } else {
       Dom.addClass( el.parentNode, "collapsed" );
     }
  
  }, "li.haschildren span" );
  */
  
