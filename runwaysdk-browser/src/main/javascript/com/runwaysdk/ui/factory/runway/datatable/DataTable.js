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
/**
 * RunwaySDK Javascript UI library.
 * 
 * @author Terraframe
 */
(function(){

var RW = Mojo.Meta.alias(Mojo.RW_PACKAGE + "*");
var UI = Mojo.Meta.alias(Mojo.UI_PACKAGE + "*");

var Column = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Column', {
  
  Extends : RW.Widget,
  
  Instance : {
    initialize : function(config) {
      this.$initialize("col");
      
      if (Mojo.Util.isValid(config)) {
        this.sortable = config.sortable || true;
        this.resizable = config.resizable || true;
        this.draggable = config.draggable || true;
        this.columnNumber = config.columnNumber;
        this.parentTable = config.parentTable;
      }
    },
    
    setStyle : function(k,v) {
      var rowNumber = 1;
      var row = this.parentTable.getRow(1);
      
      while(Mojo.Util.isValid(row)) {
        UI.DOMFacade.setStyle(row._getRawCell(this.columnNumber), k, v);
        row = this.parentTable.getRow(++rowNumber);
      }
    },
    
    getColumnNumber : function() {
      return this.columnNumber;
    },
    
    getParentTable : function() {
      return this.parentTable;
    },
  }
});

var Row = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'Row', {
  
  Extends : RW.Widget,
    
  Instance : {
    initialize : function(config) {
      this.$initialize("tr");
      
      if (config) {
        this.resizable = config.resizable || true;
        this.draggable = config.draggable || true;
        this.isHeader = config.isHeader || false;
        this.rowNumber = config.rowNumber;
        this.parentTable = config.parentTable;
        
        if (config.data)
          this.addData(config.data);
      }
      
      if (this.isHeader) {
        this.addClassName("headerrow");
      }
      else {
        if (this.rowNumber % 2 == 0) 
          this.addClassName("evenrow");
        else 
          this.addClassName("oddrow");
      }
      
      this._rowData = [];
    },

    addData : function(data)
    {
      var td;
      if (this.isHeader) {
        td = this.getFactory().newElement("th");
      }
      else {
        td = this.getFactory().newElement("td");
      }
      
      var container = this.getFactory().newElement("div");
      var appender = container;
      
      if (this.isHeader) {
        var content = this.getFactory().newElement("div");
        container.appendChild(content);
        
        UI.DOMFacade.addClassName(content, "columntitle");
        
        var resizer = this.getFactory().newElement("div");
        UI.DOMFacade.addClassName(resizer, "columnresizer");
        container.appendChild(resizer);
        
        appender = content;
      }
      
      td.appendChild(container);
      this._rowData.push(td);
      
      if (Mojo.Util.isObject(data)) {
        appender.appendChild(data);
      }
      else {
        UI.DOMFacade.appendInnerHTML(appender, data);
      }
      
      this.appendChild(td);
    },
    
    getRowNumber : function() {
      return this.rowNumber;
    },
    
    getParentTable : function() {
      return this.parentTable;
    },
    
    getCell : function(num) {
      return this.getFactory().wrapElement(this._rowData[num-1]);
    },
    
    // FIXME : is the speed enhancement worth it?
    // This is a psuedo-private method intended to only be used by the column class
    // in order to optimize the column's setStyle method. This should not be called by end users.
    _getRawCell : function(num) {
      return this._rowData[num-1];
    }
  }
});

var DataTable = Mojo.Meta.newClass(Mojo.RW_PACKAGE+'DataTable', {
  
  Extends : RW.Widget,
  
  Instance : {
    
    initialize : function(dataSource, title, config)
    {    
      this.$initialize("div");
      
      title = title || "";
      config = config || {};
      config.border = config.border || "1";
      
      this._table = this.getFactory().newElement("table", {border: config.border}, config.styles);
      var caption = this.getFactory().newElement("caption");
      caption.setInnerHTML(title);
      this._table.appendChild(caption);
      
      this._tBody = this.getFactory().newElement("tbody");
      this._table.appendChild(this._tBody);
      
      this._headerRow = new Row({isHeader:true});
      this._tBody.appendChild(this._headerRow);
      
      this._rows = [];
      this._columns = [];
      
      //this.acceptArray( dataSource.getData() );

      this.appendChild(this._table);
    },
    
    acceptArray : function(array)
    {
      this.addHeader(array[0]);
      
      var v;
      var numColumns;
      for (var k = 1; k < array.length; ++k)
      {
        this.addRow( array[k] );
        numColumns = UI.Util.max(numColumns, array[k].length);
      }
      
      for (var k = 0; k < numColumns; ++k) {
        this.addColumn(k+1);
      }
      
      this._data = array;
    },
    
    addHeader: function(inner) // can take a header primitive or a collection of header primitives
    {
      if (Mojo.Util.isObject(inner) || Mojo.Util.isArray(inner)) {
        for (key in inner) {
          this.addHeader(inner[key]);
        }
      }
      else {
        this._headerRow.addData(inner);
      }
    },
    
    /** This function only adds the col element, it doesn't actually modify the content of the
     * table at all.
     * @param {Object} columnNumber : The number representing which column it is in the table.
     */
    addColumn : function(columnNum) {
      var column = new Column({columnNumber: columnNum, parentTable: this});
      this._table.appendChild(column);
      this._columns.push(column);
    },
    
    addRow : function(rowData, repeatNumber, rowNum) // if repeatNumber is 2 this will add the same row twice (if its 1 or 0 or null it does nothing)
    {
      if ( !(Mojo.Util.isObject(rowData) || Mojo.Util.isArray(rowData)) ) { // rowData is a primative (or null)
        var row = new Row({rowNumber: this._rows.length+1, parentTable: this});
        this._rows.push(row);
        this._tBody.appendChild(row);
        
        return row;
      }
      else if ( !(Mojo.Util.isObject(rowData[0]) || Mojo.Util.isArray(rowData[0])) ) { // rowData is an array of primitives (most common use case)
        var row = new Row({rowNumber: this._rows.length+1, parentTable: this});
        this._rows.push(row);
        this._tBody.appendChild(row);
        
        for (var k in rowData) {
          row.addData(rowData[k]);
        }
        
        if (Mojo.Util.isNumber(repeatNumber)) {
          repeatNumber--;
          
          for (var i = 0; i < repeatNumber; i++) {
            this.addRow(rowData);
          }
        }
        
        return row;
      }
      else { // rowData is an array of arrays (return an array of rows)
        var retArr = [];
        
        for (var k in rowData) {
          retArr.push( this.addRow(rowData[k]) );
        }
        
        return retArr;
      }
    },
    
    deleteRow : function(rowNumber, deleteNumber) // This function works exactly like array.splice
    {
      var row;
      deleteNumber = deleteNumber || 1;
      var rowIndex = rowNumber - 1;
      var exitCondition = rowIndex + deleteNumber;
      
      for (; rowIndex < exitCondition; ++rowIndex) {
        row = this._rows[rowIndex];
        if (row == undefined) {
          deleteNumber = rowIndex - (rowNumber-1);
          break;
        }
        
        this._tBody.removeChild(row);
      }
      
      this._rows.splice(rowNumber-1, deleteNumber);
    },
    
    getRow : function(rowNumber)
    {
      if (rowNumber == 0)
        return this.getHeaderRow();
      
      if (Mojo.Util.isValid(rowNumber))
        return this._rows[rowNumber-1];
      else
        return this._rows;
    },
    
    getHeaderRow : function() {
      return this._headerRow;
    },
    
    getRows : function() {
      return this.getRow();
    },
    
    getColumn : function(columnNumber) {
      if (Mojo.Util.isValid(columnNumber))
        return this._columns[columnNumber-1];
      else
        return this._columns;
    },
    
    getColumns : function() {
      return this.getColumn();
    },
    
    getNumberOfRows : function() {
      return this._rows.length;
    }
    
  }
  
});

})();