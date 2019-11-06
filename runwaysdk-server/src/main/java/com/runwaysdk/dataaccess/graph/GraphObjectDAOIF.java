package com.runwaysdk.dataaccess.graph;

import java.util.Date;
import java.util.List;

import com.runwaysdk.dataaccess.ComponentDAO;
import com.runwaysdk.dataaccess.ComponentDAOIF;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;

public interface GraphObjectDAOIF extends ComponentDAOIF
{
  /**
   * Column name of the attribute that specifies the OID.
   */
  public static final String ID_ATTRIBUTE = "oid";

  public Object getRID();

  /**
   * Returns the embedded {@link ComponentDAO} for the attribute of the given
   * name.
   * 
   * @param attriubteName
   * @return
   */
  public ComponentDAO getEmbeddedComponentDAO(String attributeName);

  public Object getObjectValue(String name, Date date);

  public List<ValueOverTime> getValuesOverTime(String name);

  public void setValue(String name, Object value, Date startDate, Date endDate);
}
