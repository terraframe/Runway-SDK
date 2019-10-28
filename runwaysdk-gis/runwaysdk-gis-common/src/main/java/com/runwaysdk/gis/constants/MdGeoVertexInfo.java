package com.runwaysdk.gis.constants;

import com.runwaysdk.constants.graph.MdVertexInfo;

public interface MdGeoVertexInfo extends MdVertexInfo
{
  /**
   * Class {@link MdGeoVertexInfo}.
   */
  public static final String CLASS              = GISConstants.GRAPH_GIS_METADATA_PACKAGE+".MdGeoVertex";
  
  /**
   * OID.
   */
  public static final String ID_VALUE           = "61ea830a-2ea7-3943-9f99-a6561c00003a";  
  
  /**
   * SRID.
   */
  public static final String DEFAULT_SRID       = "4326";
  
  /**
   * SRID.
   */
  public static final String DEFAULT_DIMENSION  = "2";
}
