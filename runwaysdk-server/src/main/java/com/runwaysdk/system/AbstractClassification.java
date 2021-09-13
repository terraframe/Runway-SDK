package com.runwaysdk.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.business.graph.GraphQuery;
import com.runwaysdk.business.graph.VertexObject;
import com.runwaysdk.constants.MdAttributeLocalEmbeddedInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.dataaccess.MdAttributeClassificationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassificationDAOIF;
import com.runwaysdk.dataaccess.MdEdgeDAOIF;
import com.runwaysdk.dataaccess.MdGraphClassDAOIF;
import com.runwaysdk.dataaccess.MdVertexDAOIF;
import com.runwaysdk.dataaccess.metadata.graph.MdGraphClassDAO;
import com.runwaysdk.session.Session;

public abstract class AbstractClassification extends AbstractClassificationBase
{
  private static final long  serialVersionUID = -1972569469;

  public static final String CODE             = "code";

  public AbstractClassification()
  {
    super();
  }

  public abstract String getCode();

  public abstract void setCode(String code);

  public static VertexObject findMatchingClassification(String value, MdAttributeClassificationDAOIF mdAttribute)
  {
    MdClassificationDAOIF mdClassification = mdAttribute.getMdClassificationDAOIF();
    MdVertexDAOIF mdVertex = mdClassification.getReferenceMdVertexDAO();
    MdEdgeDAOIF mdEdge = mdClassification.getReferenceMdEdgeDAO();
    MdAttributeDAOIF labelAttribute = mdVertex.definesAttributeRecursive(AbstractClassification.DISPLAYLABEL);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM (");
    statement.append(" TRAVERSE out('" + mdEdge.getDBClassName() + "') FROM :parent");
    statement.append(")");
    statement.append(" WHERE " + localize(labelAttribute.getColumnName()) + " = :value");
    statement.append(" OR code = :value");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("parent", mdAttribute.getRoot().getRID());
    query.setParameter("value", value);

    return query.getSingleResult();
  }

  public static VertexObject get(String code, MdClassificationDAOIF mdClassification)
  {
    MdVertexDAOIF mdVertex = mdClassification.getReferenceMdVertexDAO();
    MdAttributeDAOIF codeAttribute = mdVertex.definesAttribute(AbstractClassification.CODE);

    StringBuilder statement = new StringBuilder();
    statement.append("SELECT FROM " + mdVertex.getDBClassName());
    statement.append(" WHERE " + codeAttribute.getColumnName() + " = :code");

    GraphQuery<VertexObject> query = new GraphQuery<VertexObject>(statement.toString());
    query.setParameter("code", code);

    return query.getSingleResult();
  }

  private static String localize(String prefix)
  {
    final MdGraphClassDAOIF mdLocalStruct = MdGraphClassDAO.getMdGraphClassDAO(MdAttributeLocalEmbeddedInfo.EMBEDDED_LOCAL_VALUE);

    List<String> list = new ArrayList<String>();

    Locale locale = Session.getCurrentLocale();
    addLocale(mdLocalStruct, locale, list);

    list.add(MdAttributeLocalInfo.DEFAULT_LOCALE);

    StringBuilder builder = new StringBuilder();
    builder.append("COALESCE(");

    for (int i = 0; i < list.size(); i++)
    {
      if (i != 0)
      {
        builder.append(", ");
      }

      builder.append(prefix + "." + list.get(i));
    }

    builder.append(")");

    return builder.toString();
  }

  private static void addLocale(final MdGraphClassDAOIF mdLocalStruct, Locale locale, List<String> list)
  {
    String localeString = locale.toString();

    for (int i = localeString.length(); i > 0; i = localeString.lastIndexOf('_', i - 1))
    {
      String subLocale = localeString.substring(0, i);

      for (MdAttributeConcreteDAOIF a : mdLocalStruct.definesAttributes())
      {
        if (a.definesAttribute().equalsIgnoreCase(subLocale))
        {
          list.add(subLocale.toLowerCase());
        }
      }
    }
  }

}
