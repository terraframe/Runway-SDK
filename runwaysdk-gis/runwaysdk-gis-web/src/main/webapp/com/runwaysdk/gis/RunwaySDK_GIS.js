/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway GIS(tm).
 *
 * Runway GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
(function(){

Mojo.GIS_PACKAGE = Mojo.ROOT_PACKAGE+'gis.transport.attributes.';
Mojo.GIS_MD_PACKAGE = Mojo.ROOT_PACKAGE+'gis.transport.metadata.';
Mojo.GIS_FIELD_PACKAGE = Mojo.ROOT_PACKAGE+'gis.form.web.field.';
Mojo.GIS_FIELD_METADATA_PACKAGE = Mojo.ROOT_PACKAGE+'gis.form.web.metadata.';

// geometry
var attrGeo = Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeGeometryDTO', {

  Extends : Mojo.ATTRIBUTE_DTO_PACKAGE+'AttributeDTO',

  IsAbstract : true,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});


var geoMd = Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeGeometryMdDTO', {

  Extends : Mojo.MD_DTO_PACKAGE+'AttributeMdDTO',

  IsAbstract : true,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// lineString
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeLineStringDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeLineStringMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// point
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributePointDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributePointMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// polygon
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributePolygonDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributePolygonMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});


// multiLine
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeMultiLineStringDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeMultiLineStringMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// multiPoint
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeMultiPointDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeMultiPointMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

// multiPolygon
Mojo.Meta.newClass(Mojo.GIS_PACKAGE+'AttributeMultiPolygonDTO', {

  Extends: attrGeo,
  
  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_MD_PACKAGE+'AttributeMultiPolygonMdDTO', {

  Extends: geoMd,

  Instance : {
  
    initialize : function(obj)
    {
      this.$initialize(obj);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_FIELD_PACKAGE+'WebPoint', {
  Extends : Mojo.FORM_PACKAGE.FIELD+'WebAttribute',
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
      
      this._x = obj.x;
      this._y = obj.y;
    },
    setXY : function(x, y){
      var wkt = 'POINT('+x+' '+y+')';
      this.setValue(wkt);
    },
    getX : function(){ return this._x; },
    getY : function(){ return this._y; },
    accept : function(visitor){
      visitor.visitWebFormComponent(this);
    }
  }
});

Mojo.Meta.newClass(Mojo.GIS_FIELD_METADATA_PACKAGE+'WebPointMd', {
  Extends : Mojo.FORM_PACKAGE.METADATA+'WebAttributeMd',
  Instance : {
    initialize : function(obj){
      this.$initialize(obj);
    }
  }
});

})();
