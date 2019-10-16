package com.runwaysdk.gis.dataaccess.graph.attributes;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.Attribute;
import com.runwaysdk.dataaccess.graph.attributes.GraphAttributeFactoryIF;
import com.runwaysdk.gis.dataaccess.MdAttributeLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiLineStringDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributeMultiPolygonDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePointDAOIF;
import com.runwaysdk.gis.dataaccess.MdAttributePolygonDAOIF;

public class GISGraphAttributeFactory implements GraphAttributeFactoryIF
{

  @Override
  public Attribute createGraphAttribute(MdAttributeConcreteDAOIF mdAttributeDAOIF, String definingType)
  {
    if (mdAttributeDAOIF instanceof MdAttributePointDAOIF)
    {
      return new AttributePoint(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributePolygonDAOIF)
    {
      return new AttributePolygon(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeLineStringDAOIF)
    {
      return new AttributeLineString(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeMultiPointDAOIF)
    {
      return new AttributeMultiPoint(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeMultiPolygonDAOIF)
    {
      return new AttributeMultiPolygon(mdAttributeDAOIF, definingType);
    }
    else if (mdAttributeDAOIF instanceof MdAttributeMultiLineStringDAOIF)
    {
      return new AttributeMultiLineString(mdAttributeDAOIF, definingType);
    }

    return null;
  }

}
