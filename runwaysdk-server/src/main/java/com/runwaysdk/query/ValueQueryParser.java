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
package com.runwaysdk.query;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.configuration.ConfigurationManager;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.MdTableClassIF;
import com.runwaysdk.dataaccess.MdTableDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.io.XMLException;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class ValueQueryParser
{
  /**
   * 
   */
  public interface ParseInterceptor
  {
    public void interceptCondition(InterceptorChain chain, ValueQuery valueQuery, String entityAlias, Condition condition);

    public void interceptSelectable(InterceptorChain chain, ValueQuery valueQuery, String entityAlias, Selectable selectable, String attributeName, AttributeDoesNotExistException t);
  }

  /**
   * Class to manage an ordered chain of ParseInterceptor objects. The
   * interceptors are added in a stack-like fashion with the last interceptor to
   * execute first.
   */
  public final class InterceptorChain
  {
    /**
     * A single entry in the chain.
     */
    private final class Entry
    {
      private Entry            nextInChain;

      private ParseInterceptor interceptor;

      private Entry(ParseInterceptor interceptor, Entry nextInChain)
      {
        this.interceptor = interceptor;
        this.nextInChain = nextInChain;
      }

      private Entry getNextInChain()
      {
        return nextInChain;
      }

      private ParseInterceptor getInterceptor()
      {
        return this.interceptor;
      }
    }

    // Most recent Entry of the chain, which will be the first to execute. This
    // is used
    // for iteration instead of the stack which does not iterate correctly
    // because it extends
    // Vector. What a shame.
    private Entry                   mostRecent;

    private Stack<ParseInterceptor> interceptorStack;

    private InterceptorChain()
    {
      this.mostRecent = null;
      this.interceptorStack = new Stack<ParseInterceptor>();
    }

    private void addInterceptor(ParseInterceptor interceptor)
    {
      this.interceptorStack.push(interceptor);
      Entry last = this.mostRecent;
      this.mostRecent = new Entry(interceptor, last);
    }

    @SuppressWarnings("unchecked")
    private Stack<ParseInterceptor> getInterceptors()
    {
      return (Stack<ParseInterceptor>) this.interceptorStack.clone();
    }

    private void interceptConditionBatch(ValueQuery valueQuery, String entityAlias, Condition condition)
    {
      Entry temp = this.mostRecent;

      this.interceptCondition(valueQuery, entityAlias, condition);

      this.mostRecent = temp;
    }

    public void interceptCondition(ValueQuery valueQuery, String entityAlias, Condition condition)
    {
      if (this.mostRecent == null)
      {
        return;
      }

      Entry current = this.mostRecent;
      this.mostRecent = current.getNextInChain();
      current.getInterceptor().interceptCondition(this, valueQuery, entityAlias, condition);
    }

    private void interceptSelectableBatch(ValueQuery valueQuery, String entityAlias, Selectable selectable, String attributeName, AttributeDoesNotExistException t)
    {
      Entry temp = this.mostRecent;

      this.interceptSelectable(valueQuery, entityAlias, selectable, attributeName, t);

      this.mostRecent = temp;
    }

    /**
     * @param valueQuery
     * @param entityAlias
     * @param selectable
     */
    public void interceptSelectable(ValueQuery valueQuery, String entityAlias, Selectable selectable, String attributeName, AttributeDoesNotExistException t)
    {
      if (this.mostRecent == null)
      {
        return;
      }

      Entry current = this.mostRecent;
      this.mostRecent = current.getNextInChain();
      current.getInterceptor().interceptSelectable(this, valueQuery, entityAlias, selectable, attributeName, t);
    }

  }

  /**
   * Class representing a custom attribute select entry, created by a user
   * independent of the input XML.
   */
  private class CustomAttributeSelect
  {
    private String entityAlias;

    private String attribute;

    private String userAlias;

    private String columnAlias;

    private Object data;

    private CustomAttributeSelect(String entityAlias, String attribute, String userAlias, String columnAlias, Object data)
    {
      this.entityAlias = entityAlias;
      this.attribute = attribute;
      this.userAlias = userAlias;
      this.columnAlias = columnAlias;
      this.data = data;
    }
  }

  /**
   * Class representing a forced change to a column's alias.
   */
  private class CustomColumnAlias
  {
    // private String entityAlias;
    // private String userAlias;
    private String columnAlias;

    private CustomColumnAlias(String entityAlias, String userAlias, String columnAlias)
    {
      // this.entityAlias = entityAlias;
      // this.userAlias = userAlias;
      this.columnAlias = columnAlias;
    }
  }

  public static final String                    ENTITIES_TAG               = "entities";

  public static final String                    ENTITY_TAG                 = "entity";

  public static final String                    ENTITY_TYPE_TAG            = "type";

  public static final String                    ENTITY_ALIAS_TAG           = "alias";

  public static final String                    ENTITY_CRITERIA_TAG        = "criteria";

  public static final String                    BASICCONDITION_TAG         = "basicCondition";

  public static final String                    OPERATOR_TAG               = "operator";

  public static final String                    VALUE_TAG                  = "value";

  public static final String                    COMPOSITECONDITION_TAG     = "compositeCondition";

  public static final String                    AND_TAG                    = "and";

  public static final String                    OR_TAG                     = "or";

  public static final String                    SELECTABLE_TAG             = "selectable";

  public static final String                    SQLBOOLEAN_TAG             = "sqlboolean";

  public static final String                    SQLCHARACTER_TAG           = "sqlcharacter";

  public static final String                    SQLUUID_TAG                = "sqluuid";

  public static final String                    SQLTEXT_TAG                = "sqltext";

  public static final String                    SQLCLOB_TAG                = "sqltext";

  public static final String                    SQLDATE_TAG                = "sqldate";

  public static final String                    SQLDATETIME_TAG            = "sqldatetime";

  public static final String                    SQLTIME_TAG                = "sqltime";

  public static final String                    SQLDECIMAL_TAG             = "sqldecimal";

  public static final String                    SQLDOUBLE_TAG              = "sqldouble";

  public static final String                    SQLFLOAT_TAG               = "sqlfloat";

  public static final String                    SQLINTEGER_TAG             = "sqlinteger";

  public static final String                    SQLLONG_TAG                = "sqllong";

  public static final String                    ISAGGREGETE_TAG            = "isaggregate";

  public static final String                    ATTRIBUTE_TAG              = "attribute";

  public static final String                    ATTRIBUTENAME_TAG          = "name";

  public static final String                    ATTRIBUTE_ENTITY_ALIAS_TAG = "entityAlias";

  public static final String                    SELECTABLE_USER_ALIAS_TAG  = "userAlias";

  public static final String                    SELECTABLE_USER_LABEL_TAG  = "userDisplayLabel";

  public static final String                    AVG_TAG                    = "avg";

  public static final String                    COUNT_TAG                  = "count";

  public static final String                    MAX_TAG                    = "max";

  public static final String                    MIN_TAG                    = "min";

  public static final String                    STDDEV_TAG                 = "stddev";

  public static final String                    SUM_TAG                    = "sum";

  public static final String                    VARIANCE_TAG               = "variance";

  public static final String                    SELECT_TAG                 = "select";

  public static final String                    GROUPBY_TAG                = "groupby";

  public static final String                    HAVING_TAG                 = "having";

  public static final String                    ORDERBY_TAG                = "orderby";

  public static final String                    ORDER_TAG                  = "order";

  public static final String                    DIRECTION_TAG              = "direction";

  public static final String                    ASC_TAG                    = "asc";

  public static final String                    DESC_TAG                   = "desc";

  private Document                              document;

  private ValueQuery                            valueQuery;

  // Key is the entity alias defined in the XML
  private Map<String, TableClassQuery>          tableQueryMap;

  // Key is the selectable name
  private Map<String, SelectableSQL>            selectableSQLMap;

  private Map<String, GeneratedTableClassQuery> generatedTableClassQueryMap;

  private QueryFactory                          queryFactory;

  private List<CustomAttributeSelect>           customAttributeSelects;

  private Map<String, CustomColumnAlias>        customColumnAliases;

  private Map<String, ValueQuery>               valueQueryMap;

  private InterceptorChain                      chain;

  /**
   * This is a hack for DDMS. We're doing this now because its easier than
   * adding support to the ValueQuery API for WITH entries (which we need for
   * adding geo columns, see DDMS ticket #3306)
   */
  private List<String>                          ignoreSelectables;

  private Map<String, String>                   ignoreDisplayLabels;

  public ValueQueryParser(Document document, ValueQuery valueQuery)
  {
    this.document = document;
    this.valueQuery = valueQuery;
    this.tableQueryMap = new HashMap<String, TableClassQuery>();
    this.selectableSQLMap = new HashMap<String, SelectableSQL>();
    this.generatedTableClassQueryMap = new HashMap<String, GeneratedTableClassQuery>();
    this.queryFactory = valueQuery.getQueryFactory();
    this.customAttributeSelects = new LinkedList<CustomAttributeSelect>();
    this.customColumnAliases = new HashMap<String, CustomColumnAlias>();
    this.valueQueryMap = new HashMap<String, ValueQuery>();
    this.ignoreSelectables = new ArrayList<String>();
    this.ignoreDisplayLabels = new HashMap<String, String>();

    // Create the chain and set the default interceptor
    this.chain = new InterceptorChain();
    this.chain.addInterceptor(new ParseInterceptor()
    {
      @Override
      public void interceptCondition(InterceptorChain chain, ValueQuery valueQuery, String entityAlias, Condition condition)
      {
        valueQuery.AND(condition);

        chain.interceptCondition(valueQuery, entityAlias, condition);
      }

      /**
       * This is the default interceptor for when an Attribute could not be
       * found. Interceptors before this must attempt to rectify the
       * AttributeDoesNotExistException, otherwise it will be thrown here. This
       * method has Interceptor like behavior as it delegates in a chain, but
       * it's really for the error case in which a Selectable cannot be
       * resolved.
       */
      @Override
      public void interceptSelectable(InterceptorChain chain, ValueQuery valueQuery, String entityAlias, Selectable selectable, String attributeName, AttributeDoesNotExistException t)
      {
        throw t;
      }
    });
  }

  public ValueQueryParser(String stringDocument, ValueQuery valueQuery)
  {
    this(buildDocument(stringDocument), valueQuery);
  }

  public ValueQueryParser(File xmlFile, ValueQuery valueQuery)
  {
    this(buildDocument(xmlFile), valueQuery);
  }

  private static Document buildDocument(Object xml)
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try
    {
      factory.setValidating(true);
      // factory.setNamespaceAware(true);
      factory.setAttribute(XMLConstants.JAXP_SCHEMA_LANGUAGE, XMLConstants.W3C_XML_SCHEMA);

      URL url = ConfigurationManager.getResource(ConfigGroup.XSD, "query.xsd");
      factory.setAttribute(XMLConstants.JAXP_SCHEMA_SOURCE, url.openStream());

      DocumentBuilder builder = factory.newDocumentBuilder();
      builder.setErrorHandler(new XMLErrorHandler());

      if (xml instanceof File)
      {
        return builder.parse((File) xml);
      }
      else
      {
        InputStream is = new ByteArrayInputStream( ( (String) xml ).getBytes(Charset.forName("UTF-8")));
        return builder.parse(is);
      }
    }
    catch (ParserConfigurationException e)
    {
      throw new XMLException(e);
    }
    catch (SAXException e)
    {
      throw new XMLException(e);
    }
    catch (IOException e)
    {
      throw new XMLException(e);
    }
  }

  private static class XMLErrorHandler implements ErrorHandler
  {
    public void fatalError(SAXParseException exception) throws SAXException
    {
      String errMsg = "Error occured parsing the report query XML: " + exception.getMessage();
      throw new QueryException(errMsg, exception);
    }

    // treat validation errors as warnings
    public void error(SAXParseException exception) throws SAXParseException
    {
      String errMsg = "Error occured parsing the report query XML: " + exception.getMessage();
      throw new QueryException(errMsg, exception);
    }

    // dump warnings too
    public void warning(SAXParseException exception) throws SAXParseException
    {
      System.out.println("** Warning" + ", line " + exception.getLineNumber() + ", uri " + exception.getSystemId());
      System.out.println("   " + exception.getMessage());
    }
  }

  /**
   * Adds another ParseInterceptor to the chain for execution.
   * 
   * @param interceptor
   */
  public void addParseIntercepter(ParseInterceptor interceptor)
  {
    this.chain.addInterceptor(interceptor);
  }

  /**
   * Returns a stack of ParseInterceptors in the LIFO order in which they will
   * be executed. Modifying the stack does not affect the original interceptors.
   * This method should not be called during parsing.
   */
  public Stack<ParseInterceptor> getInterceptors()
  {
    return chain.getInterceptors();
  }

  /**
   * Adds a custom attribute selectable independent of the initial XML. This
   * must be called before parse().
   * 
   * @param entityAlias
   * @param attribute
   * @param userAlias
   */
  public void addAttributeSelectable(String entityAlias, String attribute, String userAlias, String columnAlias)
  {
    this.addAttributeSelectable(entityAlias, attribute, userAlias, columnAlias, null);
  }

  public void addAttributeSelectable(String entityAlias, String attribute, String userAlias, String columnAlias, Object data)
  {
    this.customAttributeSelects.add(new CustomAttributeSelect(entityAlias, attribute, userAlias, columnAlias, data));
  }

  public void setValueQuery(String alias, ValueQuery valueQuery)
  {
    // JN change
    this.valueQueryMap.put(alias, valueQuery);
  }

  /**
   * Hack for DDMS
   */
  public void addIgnoreSelectable(String alias)
  {
    this.ignoreSelectables.add(alias);
  }

  /**
   * Hack for DDMS, needed because of the ignoreSelectables hack, see ticket
   * #3313
   */
  public String getIgnoredDisplayLabel(String attrName)
  {
    return this.ignoreDisplayLabels.get(attrName);
  }

  public Map<String, GeneratedTableClassQuery> parse()
  {
    this.tableQueryMap.clear();
    this.selectableSQLMap.clear();
    this.generatedTableClassQueryMap.clear();

    this.buildTableClassMap();

    this.buildSelectClause();

    for (TableClassQuery tableClassQuery : tableQueryMap.values())
    {
      this.valueQuery.FROM(tableClassQuery);
    }

    this.buildEntityCriteria();

    this.buildGroupByClause();

    this.buildHavingClause();

    this.buildOrderByClause();

    this.convertEntityMapToTypeSafe();

    this.customAttributeSelects.clear();

    return generatedTableClassQueryMap;
  }

  /**
   * Returns the {@link SelectableSQL} object with the given alias used in the
   * value query, or null if no object with such an alias exists.
   * 
   * @param userAlias
   * @return {@link SelectableSQL} object with the given alias used in the value
   *         query, or null if no object with such an alias exists.
   */
  public SelectableSQL getSelectableSQL(String attributeName)
  {
    return this.selectableSQLMap.get(attributeName);
  }

  /**
   * Populates the map with type-safe versions of the entity queries.
   */
  private void convertEntityMapToTypeSafe()
  {
    for (String alias : this.tableQueryMap.keySet())
    {
      TableClassQuery tableClassQuery = this.getTableClassQuery(alias);

      if (tableClassQuery instanceof EntityQuery)
      {
        String queryType = EntityQueryAPIGenerator.getQueryClass(tableClassQuery.getType());

        Class<?> queryClass = LoaderDecorator.load(queryType);

        try
        {
          GeneratedEntityQuery generatedEntityQuery = (GeneratedEntityQuery) queryClass.getConstructor(QueryFactory.class).newInstance(this.queryFactory);
          generatedEntityQuery.setComponentQuery(tableClassQuery);
          this.generatedTableClassQueryMap.put(alias, generatedEntityQuery);
        }
        catch (Throwable e)
        {
          throw new ProgrammingErrorException(e);
        }
      }
      else if (tableClassQuery instanceof TableQuery)
      {
        MdTableDAOIF mdTableDAOIF = (MdTableDAOIF) tableClassQuery.getMdTableClassIF();
        this.generatedTableClassQueryMap.put(alias, new GenericTableQuery(mdTableDAOIF, this.queryFactory));
      }

    }
  }

  // Build the entities
  private void buildTableClassMap()
  {
    Element queryElement = this.document.getDocumentElement();

    Element entitiesElement = (Element) queryElement.getElementsByTagName(ENTITIES_TAG).item(0);

    NodeList entityNodeList = entitiesElement.getElementsByTagName(ENTITY_TAG);

    for (int i = 0; i < entityNodeList.getLength(); i++)
    {
      Element entityElement = (Element) entityNodeList.item(i);

      addTableClass(entityElement);
    }
  }

  private void buildEntityCriteria()
  {
    Element queryElement = this.document.getDocumentElement();

    Element entitiesElement = (Element) queryElement.getElementsByTagName(ENTITIES_TAG).item(0);

    NodeList entityNodeList = entitiesElement.getElementsByTagName(ENTITY_TAG);

    for (int i = 0; i < entityNodeList.getLength(); i++)
    {
      Element entityElement = (Element) entityNodeList.item(i);

      addTableClassCriteria(entityElement);
    }
  }

  // build the select clause
  private void buildSelectClause()
  {
    Element queryElement = this.document.getDocumentElement();

    Element selectElement = (Element) queryElement.getElementsByTagName(SELECT_TAG).item(0);

    NodeList childNodeList = selectElement.getChildNodes();

    Map<String, Selectable> selectableMap = new LinkedHashMap<String, Selectable>();

    for (int i = 0; i < childNodeList.getLength(); i++)
    {
      Node node = childNodeList.item(i);

      if (node instanceof Element)
      {
        Element selectableElement = (Element) node;

        if (selectableElement.getTagName().equals(SELECTABLE_TAG))
        {
          Selectable selectable = getSelectable(selectableElement);

          if (selectable != null) // This only happens if the selectable is part
                                  // of the ignore list (DDMS hack)
          {
            String key = selectable.getUserDefinedAlias();
            if (this.customColumnAliases.containsKey(key))
            {
              CustomColumnAlias entry = this.customColumnAliases.get(key);
              selectable.setColumnAlias(entry.columnAlias);
            }

            String mapKey = selectable.getAttributeNameSpace() + "-" + selectable._getAttributeName();
            selectableMap.put(mapKey, selectable);
          }
        }
      }
    }

    // add the custom attribute selectables
    for (CustomAttributeSelect custom : this.customAttributeSelects)
    {
      // don't overwrite an existing selectable, just modify it
      Selectable selectable;
      if (valueQueryMap.containsKey(custom.entityAlias))
      {
        // JN change
        selectable = this.valueQueryMap.get(custom.entityAlias).get(custom.userAlias);
      }
      else
      {
        selectable = this.getTableClassQuery(custom.entityAlias).get(custom.attribute, custom.userAlias);
      }

      String key = custom.userAlias;
      boolean exists = false;
      if (selectableMap.containsKey(key))
      {
        selectable = selectableMap.get(key);
        exists = true;
      }

      if (custom.columnAlias.trim().length() > 0)
      {
        selectable.setColumnAlias(custom.columnAlias);
      }

      if (custom.data != null)
      {
        selectable.setData(custom.data);
      }

      if (!exists)
      {
        selectableMap.put(key, selectable);
      }
    }

    Selectable[] selectableArray = selectableMap.values().toArray(new Selectable[selectableMap.size()]);

    this.valueQuery.SELECT(selectableArray);
  }

  // build the group by clause
  private void buildGroupByClause()
  {
    Element queryElement = this.document.getDocumentElement();

    Element groupByElement = (Element) queryElement.getElementsByTagName(GROUPBY_TAG).item(0);

    NodeList selectableNodeList = groupByElement.getElementsByTagName(SELECTABLE_TAG);

    ArrayList<SelectableSingle> selectableSingleList = new ArrayList<SelectableSingle>();

    for (int i = 0; i < selectableNodeList.getLength(); i++)
    {
      Element selectableElement = (Element) selectableNodeList.item(i);

      Selectable selectable = getSelectable(selectableElement);

      if (selectable instanceof SelectableSingle)
      {
        selectableSingleList.add((SelectableSingle) selectable);
      }
      else
      {
        String errMsg = "Aggregate functions are not allowed in a GROUP BY clause.";

        throw new NoAggregateInGroupByException(errMsg);
      }
    }

    SelectableSingle[] selectableArray = selectableSingleList.toArray(new SelectableSingle[selectableSingleList.size()]);

    this.valueQuery.GROUP_BY(selectableArray);
  }

  // build the having clause
  private void buildHavingClause()
  {
    Element queryElement = this.document.getDocumentElement();

    Element groupByElement = (Element) queryElement.getElementsByTagName(HAVING_TAG).item(0);

    NodeList basicConditionNodeList = groupByElement.getElementsByTagName(BASICCONDITION_TAG);

    if (basicConditionNodeList.getLength() >= 1)
    {
      Element basicConditionElement = (Element) basicConditionNodeList.item(0);

      Condition condition = getBasicCondition(basicConditionElement);

      if (condition instanceof BasicCondition)
      {
        this.valueQuery.HAVING((BasicCondition) condition);
      }
      else
      {
        String errMsg = "HAVING clause can only have basic conditions.";
        throw new QueryException(errMsg);
      }
    }
  }

  // build the order by clause
  private void buildOrderByClause()
  {
    Element queryElement = this.document.getDocumentElement();

    Element orderByElement = (Element) queryElement.getElementsByTagName(ORDERBY_TAG).item(0);

    NodeList orderNodeList = orderByElement.getElementsByTagName(ORDER_TAG);

    for (int i = 0; i < orderNodeList.getLength(); i++)
    {
      Element orderElement = (Element) orderNodeList.item(i);

      Element selectableElement = (Element) orderElement.getElementsByTagName(SELECTABLE_TAG).item(0);
      Selectable selectable = getSelectable(selectableElement);

      if (selectable instanceof SelectablePrimitive)
      {
        SelectablePrimitive selectablePrimitive = (SelectablePrimitive) selectable;
        Element directionElement = (Element) orderElement.getElementsByTagName(DIRECTION_TAG).item(0);

        if (directionElement.getElementsByTagName(DESC_TAG).getLength() > 0)
        {
          this.valueQuery.ORDER_BY_DESC(selectablePrimitive);
        }
        else
        {
          this.valueQuery.ORDER_BY_ASC(selectablePrimitive);
        }
      }
      else
      {
        String errMsg = "Attributes in the ORDER BY clause must be primitive.";
        throw new InvalidOrderByPrimitiveException(errMsg);
      }
    }

  }

  /**
   * Creates an {@link TableClassQuery} object from the given element.
   * 
   * @param entityElement
   */
  private void addTableClass(Element entityElement)
  {
    Element typeElement = (Element) entityElement.getElementsByTagName(ENTITY_TYPE_TAG).item(0);
    String type = typeElement.getTextContent().trim();

    Element aliasElement = (Element) entityElement.getElementsByTagName(ENTITY_ALIAS_TAG).item(0);
    String alias = aliasElement.getTextContent().trim();

    MdTableClassIF mdTalbeClassIF = MdClassDAO.getMdTableClassIF(type);
    TableClassQuery tableClassQuery = this.queryFactory.tableClassQuery(mdTalbeClassIF);

    this.tableQueryMap.put(alias, tableClassQuery);
  }

  /**
   * 
   * @param entityElement
   */
  private void addTableClassCriteria(Element entityElement)
  {
    String alias = entityElement.getElementsByTagName(ENTITY_ALIAS_TAG).item(0).getTextContent();
    Element criteriaElement = (Element) entityElement.getElementsByTagName(ENTITY_CRITERIA_TAG).item(0);

    NodeList criteriaChildNodes = criteriaElement.getChildNodes();

    for (int i = 0; i < criteriaChildNodes.getLength(); i++)
    {
      Node node = criteriaChildNodes.item(i);

      if (node instanceof Element)
      {
        Element element = (Element) node;
        Condition cond = null;
        if (element.getTagName().equals(BASICCONDITION_TAG))
        {
          cond = this.getBasicCondition(element);
        }
        else if (element.getTagName().equals(COMPOSITECONDITION_TAG))
        {
          cond = this.getCompositeCondition(element);
        }

        chain.interceptConditionBatch(valueQuery, alias, cond);
      }
    }
  }

  /**
   * Returns a condition object derived from the basicCondition element.
   * 
   * @param basicCondition
   * @return condition object derived from the basicCondition element.
   */
  private Condition getCompositeCondition(Element compositeCondition)
  {
    NodeList criteriaChildNodes = compositeCondition.getChildNodes();

    Element andOrTag = null;

    for (int i = 0; i < criteriaChildNodes.getLength(); i++)
    {
      Node node = criteriaChildNodes.item(i);

      if (node instanceof Element)
      {
        Element element = (Element) node;

        if (element.getTagName().equals(OR_TAG))
        {
          andOrTag = element;
        }
        else if (element.getTagName().equals(AND_TAG))
        {
          andOrTag = element;
        }
      }
    }

    NodeList andOrChildNodes = andOrTag.getChildNodes();

    ArrayList<Condition> conditionList = new ArrayList<Condition>();

    for (int i = 0; i < andOrChildNodes.getLength(); i++)
    {
      Node node = andOrChildNodes.item(i);

      if (node instanceof Element)
      {
        Element element = (Element) node;

        if (element.getTagName().equals(BASICCONDITION_TAG))
        {
          conditionList.add(this.getBasicCondition(element));
        }
        else if (element.getTagName().equals(COMPOSITECONDITION_TAG))
        {
          conditionList.add(this.getCompositeCondition(element));
        }
      }
    }

    Condition[] conditionArray = conditionList.toArray(new Condition[conditionList.size()]);

    if (andOrTag.getTagName().equals(OR_TAG))
    {
      return OR.get(conditionArray);
    }
    else
    {
      return AND.get(conditionArray);
    }
  }

  /**
   * Returns a condition object derived from the basicCondition element.
   * 
   * @param basicCondition
   * @return condition object derived from the basicCondition element.
   */
  private Condition getBasicCondition(Element basicCondition)
  {
    Element selectableElement = (Element) basicCondition.getElementsByTagName(SELECTABLE_TAG).item(0);

    Selectable selectable = this.getSelectable(selectableElement);

    Element operatorElement = (Element) basicCondition.getElementsByTagName(OPERATOR_TAG).item(0);

    Element valueElement = (Element) basicCondition.getElementsByTagName(VALUE_TAG).item(0);

    return selectable.getCondition(operatorElement.getTextContent(), valueElement.getTextContent());
  }

  /**
   * Returns a <code>Selectable</code> object from the selectable element.
   * 
   * @param selectableElement
   * @return a <code>Selectable</code> object from the selectable element.
   */
  private Selectable getSelectable(Element selectableElement)
  {
    NodeList nodeList = selectableElement.getChildNodes();

    for (int i = 0; i < nodeList.getLength(); i++)
    {
      Node node = nodeList.item(i);

      if (node instanceof Element)
      {
        Element element = (Element) node;

        String tagName = element.getTagName();

        if (tagName.equals(ATTRIBUTE_TAG))
        {
          Element entityAliasElement = (Element) element.getElementsByTagName(ATTRIBUTE_ENTITY_ALIAS_TAG).item(0);
          String entityAlias = entityAliasElement.getTextContent().trim();

          String userAlias = null;
          Element userAliasElement = (Element) element.getElementsByTagName(SELECTABLE_USER_ALIAS_TAG).item(0);
          if (userAliasElement != null)
          {
            if (!userAliasElement.getTextContent().trim().equals(""))
            {
              userAlias = userAliasElement.getTextContent().trim();
            }
          }

          String userLabel = null;
          Element userLabelElement = (Element) element.getElementsByTagName(SELECTABLE_USER_LABEL_TAG).item(0);
          if (userLabelElement != null)
          {
            if (!userLabelElement.getTextContent().trim().equals(""))
            {
              userLabel = userLabelElement.getTextContent().trim();
            }
          }

          Element attributeNameElement = (Element) element.getElementsByTagName(ATTRIBUTENAME_TAG).item(0);
          String attributeNameTagValue = attributeNameElement.getTextContent().trim();

          String[] attributeNames = attributeNameTagValue.split("\\.");

          Selectable attribute = null;

          if (attributeNames.length == 0)
          {
            if (valueQueryMap.containsKey(entityAlias))
            {
              // JN Change
              attribute = this.valueQueryMap.get(entityAlias).get(userAlias, userLabel);
            }
            else
            {
              attribute = this.getTableClassQuery(entityAlias).get("", userAlias, userLabel);
            }
          }
          else
          {
            for (String attributeName : attributeNames)
            {
              try
              {
                if (attribute != null && attribute instanceof AttributeLocal && attributeName.equals("currentValue"))
                {
                  attribute = ( (AttributeLocal) attribute ).getSessionLocale(userAlias, userLabel);
                }
                else if (attribute == null)
                {
                  if (ignoreSelectables.contains(entityAlias)) // Hack for DDMS
                  {
                    this.ignoreDisplayLabels.put(userAlias, userLabel);
                    return null;
                  }
                  else if (valueQueryMap.containsKey(entityAlias)) // Probably
                                                                   // another
                                                                   // hack for
                                                                   // DDMS
                  {
                    attribute = this.valueQueryMap.get(entityAlias).get(userAlias, userLabel);
                  }
                  else
                  {
                    attribute = this.getTableClassQuery(entityAlias).getS(attributeName, userAlias, userLabel);
                  }
                }
                else
                {
                  if (attribute instanceof HasAttributeFactory)
                  {
                    attribute = ( (HasAttributeFactory) attribute ).get(attributeName, userAlias, userLabel);
                  }
                }
              }
              catch (AttributeDoesNotExistException ex)
              {
                // This may not be the best place for the Interceptor but it
                // allows
                // calling code to make a last attempt to parse/manipulate the
                // attribute
                // as the above code has failed.
                chain.interceptSelectableBatch(valueQuery, entityAlias, attribute, attributeName, ex);
              }
            }
          }
          return attribute;
        }
        else if (tagName.equals(SQLBOOLEAN_TAG) || tagName.equals(SQLCHARACTER_TAG) || tagName.equals(SQLTEXT_TAG) || tagName.equals(SQLCLOB_TAG) || tagName.equals(SQLDATE_TAG) || tagName.equals(SQLDATETIME_TAG) || tagName.equals(SQLTIME_TAG) || tagName.equals(SQLDECIMAL_TAG) || tagName.equals(SQLDOUBLE_TAG) || tagName.equals(SQLFLOAT_TAG) || tagName.equals(SQLINTEGER_TAG) || tagName.equals(SQLLONG_TAG))
        {
          Element attributeNameElement = (Element) element.getElementsByTagName(ATTRIBUTENAME_TAG).item(0);
          String attributeName = attributeNameElement.getTextContent().trim();

          String userAlias = null;
          Element userAliasElement = (Element) element.getElementsByTagName(SELECTABLE_USER_ALIAS_TAG).item(0);
          if (userAliasElement != null)
          {
            if (!userAliasElement.getTextContent().trim().equals(""))
            {
              userAlias = userAliasElement.getTextContent().trim();
            }
          }

          String resultSetAttrName;
          if (userAlias != null)
          {
            resultSetAttrName = userAlias;
          }
          else
          {
            resultSetAttrName = attributeName;
          }

          String userLabel = null;
          Element userLabelElement = (Element) element.getElementsByTagName(SELECTABLE_USER_LABEL_TAG).item(0);
          if (userLabelElement != null)
          {
            if (!userLabelElement.getTextContent().trim().equals(""))
            {
              userLabel = userLabelElement.getTextContent().trim();
            }
          }

          boolean isAggregate = false;

          if (element.getElementsByTagName(ISAGGREGETE_TAG).getLength() > 0)
          {
            Element isAggregateElement = (Element) element.getElementsByTagName(ISAGGREGETE_TAG).item(0);
            isAggregate = Boolean.parseBoolean(isAggregateElement.getTextContent().trim());
          }
          // we use the attribute name as the defualt value for sqlselectables
          // to make querying on precalculatated views easier.
          if (tagName.equals(SQLBOOLEAN_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLBoolean(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLCHARACTER_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLCharacter(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLUUID_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLUUID(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLTEXT_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLText(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLCLOB_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLClob(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLDATE_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLDate(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLDATETIME_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLDateTime(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLTIME_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLTime(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLDECIMAL_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLDecimal(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLDOUBLE_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLDouble(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLFLOAT_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLFloat(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLINTEGER_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLInteger(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
          else if (tagName.equals(SQLLONG_TAG))
          {
            SelectableSQL selectableSQL = new SelectableSQLLong(isAggregate, this.valueQuery, attributeName, attributeName, userAlias, userLabel);
            this.selectableSQLMap.put(resultSetAttrName, selectableSQL);
            return selectableSQL;
          }
        }
        else
        {
          Element functionSelectableElement = (Element) element.getElementsByTagName(SELECTABLE_TAG).item(0);

          String userAlias = null;
          String userLabel = null;

          NodeList functionChildNodes = element.getChildNodes();

          for (int k = 0; k < functionChildNodes.getLength(); k++)
          {
            Node childNode = functionChildNodes.item(k);

            if (childNode instanceof Element && ( (Element) childNode ).getTagName().equals(SELECTABLE_USER_ALIAS_TAG))
            {
              if (! ( (Element) childNode ).getTextContent().trim().equals(""))
              {
                userAlias = ( (Element) childNode ).getTextContent();
              }
            }

            if (childNode instanceof Element && ( (Element) childNode ).getTagName().equals(SELECTABLE_USER_LABEL_TAG))
            {
              if (! ( (Element) childNode ).getTextContent().trim().equals(""))
              {
                userLabel = ( (Element) childNode ).getTextContent();
              }
            }
          }

          if (tagName.equals(AVG_TAG))
          {
            return F.AVG(this.getSelectable(functionSelectableElement), userAlias, userLabel);
          }
          else if (tagName.equals(COUNT_TAG))
          {
            return F.COUNT(this.getSelectable(functionSelectableElement), userAlias, userLabel);
          }
          else if (tagName.equals(MAX_TAG))
          {
            return F.MAX(this.getSelectable(functionSelectableElement), userAlias, userLabel);
          }
          else if (tagName.equals(MIN_TAG))
          {
            return F.MIN(this.getSelectable(functionSelectableElement), userAlias, userLabel);
          }
          else if (tagName.equals(STDDEV_TAG))
          {
            return F.STDDEV(this.getSelectable(functionSelectableElement), userAlias, userLabel);
          }
          else if (tagName.equals(SUM_TAG))
          {
            return F.SUM(this.getSelectable(functionSelectableElement), userAlias, userLabel);
          }
          else if (tagName.equals(VARIANCE_TAG))
          {
            return F.VARIANCE(this.getSelectable(functionSelectableElement), userAlias, userLabel);
          }
        }
      }
    }

    String errMsg = "Invalid [" + selectableElement.getTagName() + "] attribute tag.";
    throw new QueryException(errMsg);
  }

  private TableClassQuery getTableClassQuery(String alias)
  {
    if (!this.tableQueryMap.containsKey(alias))
    {
      String errMesg = "Alias [" + alias + "] is not defined.";
      throw new QueryException(errMesg);
    }
    else
    {
      return this.tableQueryMap.get(alias);
    }
  }

}
