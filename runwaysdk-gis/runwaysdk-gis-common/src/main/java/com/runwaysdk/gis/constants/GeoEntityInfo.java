package com.runwaysdk.gis.constants;

import com.runwaysdk.constants.BusinessInfo;

public interface GeoEntityInfo extends BusinessInfo
{
  public static final String   CLASS              = GISConstants.GEO_PACKAGE+".GeoEntity";
  
  public static final String   ID_VALUE           = "8b521d00-d772-3efc-9525-d88f520000d8";
  
  public static final String   TABLE              = "geo_entity";
  
  
  public static final String   GEOID              = "geoId";
  
  public static final String   GEOPOINT           = "geoPoint";
  
  public static final String   GEOMULTIPOINT      = "geoMultiPoint";
  
  public static final String   GEOLINE            = "geoLine";
  
  public static final String   GEOMULTILINE       = "geoMultiLine";
  
  public static final String   GEOPOLYGON         = "geoPolygon";
  
  public static final String   GEOMULTIPOLYGON    = "geoMultiPolygon";
  
  public static final String[] DEFAULT_ATTRIBUTES = new String[] {GEOID, GEOPOINT, GEOMULTIPOINT, GEOLINE, GEOMULTILINE, GEOPOLYGON, GEOMULTIPOLYGON};
  
}
