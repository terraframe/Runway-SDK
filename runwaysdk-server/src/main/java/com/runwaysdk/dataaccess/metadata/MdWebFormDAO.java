/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.metadata;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.runwaysdk.constants.WebFormFieldInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebFieldDAOIF;
import com.runwaysdk.dataaccess.MdWebFormDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.system.metadata.MdWebField;
import com.runwaysdk.system.metadata.WebGroupField;

public class MdWebFormDAO extends MdFormDAO implements MdWebFormDAOIF
{

  /**
   * 
   */
  private static final long serialVersionUID = 2756828613984090383L;

  public MdWebFormDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);
  }

  public MdWebFormDAO()
  {
    super();
  }

  /**
   * Returns the MdFormDAOIF with the given id.
   * 
   * @param id
   * @return
   */
  public static MdWebFormDAOIF get(String id)
  {
    return (MdWebFormDAOIF) BusinessDAO.get(id);
  }

  public static MdWebFormDAO newInstance()
  {
    return (MdWebFormDAO) BusinessDAO.newInstance(MdWebFormDAOIF.CLASS);
  }

  public MdWebFormDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdWebFormDAO(attributeMap, MdWebFormDAOIF.CLASS);
  }

  @Override
  public List<MdWebFieldDAOIF> getAllMdFieldsForDelete()
  {
    // FIXME push logic into MdFormDAO
    List<MdWebFieldDAOIF> fields = new LinkedList<MdWebFieldDAOIF>();

    for (RelationshipDAOIF rel : getChildren(WebFormFieldInfo.CLASS))
    {
      BusinessDAOIF child = rel.getChild();

      if (child instanceof MdWebFieldDAOIF)
      {
        fields.add((MdWebFieldDAOIF) child);
      }
    }

    // Sort the MdParamters into ascending order by the parameter order
    Collections.sort(fields, new Comparator<MdWebFieldDAOIF>()
    {
      public int compare(MdWebFieldDAOIF f1, MdWebFieldDAOIF f2)
      {
        Integer o1 = Integer.parseInt(f1.getFieldOrder());
        Integer o2 = Integer.parseInt(f2.getFieldOrder());

        return o1.compareTo(o2);
      }
    });

    return fields;
  }

  @Override
  public List<MdWebFieldDAOIF> getAllMdFields()
  {
    // FIXME push logic into MdFormDAO
    List<MdWebFieldDAOIF> fields = new LinkedList<MdWebFieldDAOIF>();

    for (RelationshipDAOIF rel : getChildren(WebFormFieldInfo.CLASS))
    {
      BusinessDAOIF child = rel.getChild();

      if (child instanceof MdWebFieldDAOIF)
      {
        fields.add((MdWebFieldDAOIF) child);
      }
    }

    return fields;
  }

  @SuppressWarnings("unchecked")
  public List<? extends MdFieldDAOIF> getOrderedMdFields()
  {
    QueryFactory f = new QueryFactory();
    BusinessDAOQuery q = f.businessDAOQuery(MdWebField.CLASS);
    BusinessDAOQuery q1 = f.businessDAOQuery(MdWebField.CLASS);
    RelationshipDAOQuery relQ = f.relationshipDAOQuery(WebGroupField.CLASS);

    // exclude fields that are directly beneath a group
    relQ.WHERE(relQ.childId().EQ(q1.id()));
    q.AND(q.isNotChildIn_SUBSELECT(relQ));

    q.WHERE(q.aReference(MdWebField.DEFININGMDFORM).EQ(this));
    q.ORDER_BY_ASC(q.aInteger(MdWebField.FIELDORDER));

    OIterator<? extends BusinessDAOIF> iterator = q.getIterator();

    try
    {
      List<MdFieldDAOIF> allFields = new LinkedList<MdFieldDAOIF>();

      List<? extends BusinessDAOIF> filter = iterator.getAll().stream().filter(business -> ( business instanceof MdFieldDAOIF )).collect(Collectors.toList());

      List<? extends MdWebFieldDAO> fields = (List<? extends MdWebFieldDAO>) filter;

      recurseFields(allFields, fields);

      return allFields;
    }
    finally
    {
      iterator.close();
    }
  }

  private void recurseFields(List<MdFieldDAOIF> allFields, List<? extends MdFieldDAOIF> fields)
  {
    for (MdFieldDAOIF field : fields)
    {
      allFields.add(field);

      if (field instanceof MdWebGroupDAO)
      {
        MdWebGroupDAO group = (MdWebGroupDAO) field;
        recurseFields(allFields, group.getGroupFields());
      }
    }
  }

  @Override
  public MdWebFieldDAOIF getMdField(String fieldName)
  {
    // FIXME optimize by performing a query for a single object or by reading
    // the cache
    return this.getMdFieldsByName().get(fieldName);
  }

  @Override
  public Map<String, ? extends MdWebFieldDAOIF> getMdFieldsByName()
  {
    Map<String, MdWebFieldDAOIF> fields = new HashMap<String, MdWebFieldDAOIF>();
    for (MdWebFieldDAOIF field : this.getAllMdFields())
    {
      fields.put(field.getFieldName(), field);
    }

    return fields;
  }

}
