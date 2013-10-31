/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.business.generation;

import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.BusinessIterator;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTermDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.AttributeEnumeration;
import com.runwaysdk.query.AttributeReference;
import com.runwaysdk.query.AttributeTerm;
import com.runwaysdk.query.BasicCondition;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.ComponentQuery;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.GeneratedBusinessQuery;
import com.runwaysdk.query.Join;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.SelectableEnumeration;
import com.runwaysdk.query.SelectableReference;
import com.runwaysdk.query.ValueQuery;

public class BusinessQueryAPIGenerator extends EntityQueryAPIGenerator
{
  public static final String QUERY_IF_SUFFIX               = EntityQueryAPIGenerator.QUERY_API_SUFFIX + "IF";

  public static final String QUERY_ENUMERATION_SUFFIX      = "QueryEnumeration";

  public static final String QUERY_IF_ENUMERATION_SUFFIX   = "QueryEnumerationIF";

  public static final String QUERY_REFERENCE_SUFFIX        = "QueryReference";

  public static final String QUERY_IF_REFERENCE_SUFFIX     = "QueryReferenceIF";

  public static final String NOT_IN_RELATIONSHIP_PREFIX    = "NOT_IN_";

  public static final String SUBSELECT_RELATIONSHIP_PREFIX = "SUBSELECT_";

  protected BufferedWriter   refSrcBuffer;

  protected Set<String>      refImportSet;

  public BusinessQueryAPIGenerator(MdBusinessDAOIF mdBusinessIF)
  {
    super(mdBusinessIF);

    this.refImportSet = new HashSet<String>();
    this.refImportSet.add(java.util.Set.class.getName());
    this.refImportSet.add(ComponentQuery.class.getName());
    this.refImportSet.add(MdBusinessDAOIF.class.getName());
    this.refImportSet.add(Join.class.getName());
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business
    // classes for metadata.
    if (this.getMdClassIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // do not generate if the file has already been generated and is
    // semantically equivilant.
    if (LocalProperties.isKeepBaseSource() && AbstractGenerator.hashEquals(this.getSerialVersionUID(), TypeGenerator.getQueryAPIsourceFilePath(this.getMdClassIF())))
    {
      return;
    }

    super.go(forceRegeneration);

    this.addPackage();
    this.addSignatureAnnotation();
    this.addClassName();

    this.addSerialVersionUID();
    this.addConstructors();
    this.addGetClassTypeMethod();
    this.addAccessors();
    this.createAttributeRefFactory(this.srcBuffer);
    this.createAttributeStructFactory(this.srcBuffer);
    this.createAttributeLocalFactory(this.srcBuffer);
    this.createAttributeEnumerationFactory(this.srcBuffer);

    this.createIteratorMethods();

    this.addParentRelationshipMethods();
    this.addChildRelationshipMethods();
    this.addEnumerationItemRelationships();
    this.addStateQueryMethods();

    this.refInnerInterfaceDef();
    this.refInnerClassDef();
    this.enumInnerInterface();
    this.enumInnerClass();
    this.enumInnerSubInterfaces();
    this.enumInnerSubClasses();

    this.close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("BASE_QUERY", this.getSignature());
  }

  /**
   * Returns the reference to the MdBusinessIF object that defines the entity
   * type for which this object generates a query API object for.
   * 
   * @return reference to the MdBusinessIF object that defines the entity type
   *         for which this object generates a query API object for.
   */
  protected MdBusinessDAOIF getMdClassIF()
  {
    return (MdBusinessDAOIF) super.getMdClassIF();
  }

  /**
   * Returns the name of the interface for the given type which extends
   * AttributeReferenceIF.
   * 
   * @param mdBusinessIF
   * @return name of the interface for the given type which extends
   *         AttributeReferenceIF.
   */
  protected static String getRefInterfaceName(MdBusinessDAOIF mdBusinessIF)
  {
    return mdBusinessIF.getTypeName() + QUERY_IF_REFERENCE_SUFFIX;
  }

  /**
   * Returns the qualified name of the interface for the given type which
   * extends AttributeReferenceIF.
   * 
   * @param mdBusinessIF
   * @return qualified name of the interface for the given type which extends
   *         AttributeReferenceIF.
   */
  protected static String getRefInterface(MdBusinessDAOIF mdBusinessIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdBusinessIF) + "." + mdBusinessIF.getTypeName() + QUERY_IF_REFERENCE_SUFFIX;
  }

  /**
   * Returns the qualified name of the file of the interface for the given type
   * which extends AttributeReferenceIF.
   * 
   * @param mdBusinessIF
   * @return qualified name of the interface for the given type which extends
   *         AttributeReferenceIF.
   */
  public static String getRefInterfaceCompiled(MdBusinessDAOIF mdBusinessIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdBusinessIF) + "$" + mdBusinessIF.getTypeName() + QUERY_IF_REFERENCE_SUFFIX;
  }

  /**
   * Returns the name of the class for the given type which extends
   * AttributeReference.
   * 
   * @param mdBusinessIF
   * @return name of the class for the given type which extends
   *         AttributeReference.
   */
  protected static String getRefClassName(MdBusinessDAOIF mdBusinessIF)
  {
    return mdBusinessIF.getTypeName() + QUERY_REFERENCE_SUFFIX;
  }

  /**
   * Returns the qualified name of the class for the given type which extends
   * AttributeReference.
   * 
   * @param mdBusinessIF
   * @return qualified name of the inner for the given type which extends
   *         AttributeReference.
   */
  protected static String getRefClass(MdBusinessDAOIF mdBusinessIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdBusinessIF) + "." + mdBusinessIF.getTypeName() + QUERY_REFERENCE_SUFFIX;
  }

  /**
   * Returns the name of the interface for the given type which extends
   * AttributeEnumerationIF.
   * 
   * @param mdBusinessIF
   * @return name of the interface for the given type which extends
   *         AttributeEnumerationIF.
   */
  protected static String getEnumInterfaceName(MdBusinessDAOIF mdBusinessIF)
  {
    return mdBusinessIF.getTypeName() + QUERY_IF_ENUMERATION_SUFFIX;
  }

  /**
   * Returns the qualified name of the interface for the given type which
   * extends AttributeEnumerationIF.
   * 
   * @param mdBusinessIF
   * @return qualified name of the interface for the given type which extends
   *         AttributeEnumerationIF.
   */
  protected static String getEnumInterface(MdBusinessDAOIF mdBusinessIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdBusinessIF) + "." + mdBusinessIF.getTypeName() + QUERY_IF_ENUMERATION_SUFFIX;
  }

  /**
   * Returns the qualified name of the interface for the given type which
   * extends AttributeEnumerationIF.
   * 
   * @param mdBusinessIF
   * @return qualified name of the interface for the given type which extends
   *         AttributeEnumerationIF.
   */
  public static String getEnumInterfaceCompiled(MdBusinessDAOIF mdBusinessIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdBusinessIF) + "$" + mdBusinessIF.getTypeName() + QUERY_IF_ENUMERATION_SUFFIX;
  }

  /**
   * Returns the name of the class for the given type which extends
   * AttributeEnumeration.
   * 
   * @param mdBusinessIF
   * @return name of the class for the given type which extends
   *         AttributeEnumeration.
   */
  protected static String getEnumClassName(MdBusinessDAOIF mdBusinessIF)
  {
    return mdBusinessIF.getTypeName() + QUERY_ENUMERATION_SUFFIX;
  }

  /**
   * Returns the qualified name of the class for the given type which extends
   * AttributeEnumeration.
   * 
   * @param mdBusinessIF
   * @return qualified name of the inner for the given type which extends
   *         AttributeEnumeration.
   */
  protected static String getEnumClass(MdBusinessDAOIF mdBusinessIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdBusinessIF) + "." + mdBusinessIF.getTypeName() + QUERY_ENUMERATION_SUFFIX;
  }

  /**
   * Returns the name of the sub interface for the given type that is type safe.
   * 
   * @param mdEnumerationIF
   * @return name of the sub interface for the given type that is type safe.
   */
  protected static String getEnumSubInterfaceName(MdEnumerationDAOIF mdEnumerationIF)
  {
    return mdEnumerationIF.getTypeName() + QUERY_IF_SUFFIX;
  }

  /**
   * Returns the qualified name of the sub interface for the given type that is
   * type safe.
   * 
   * @param mdEnumerationIF
   * @return qualified name of the sub interface for the given type that is type
   *         safe.
   */
  protected static String getEnumSubInterface(MdEnumerationDAOIF mdEnumerationIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdEnumerationIF.getMasterListMdBusinessDAO()) + "." + mdEnumerationIF.getTypeName() + QUERY_IF_SUFFIX;
  }

  /**
   * Returns the qualified name of the sub interface for the given type that is
   * type safe.
   * 
   * @param mdEnumerationIF
   * @return qualified name of the sub interface for the given type that is type
   *         safe.
   */
  public static String getEnumSubInterfaceCompiled(MdEnumerationDAOIF mdEnumerationIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdEnumerationIF.getMasterListMdBusinessDAO()) + "$" + mdEnumerationIF.getTypeName() + QUERY_IF_SUFFIX;
  }

  /**
   * Returns the qualified name of the sub class for the given type that is type
   * safe.
   * 
   * @param mdEnumerationIF
   * @return qualified name of the sub class for the given type that is type
   *         safe.
   */
  protected static String getEnumSubClass(MdEnumerationDAOIF mdEnumerationIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdEnumerationIF.getMasterListMdBusinessDAO()) + "." + mdEnumerationIF.getTypeName() + EntityQueryAPIGenerator.QUERY_API_SUFFIX;
  }

  /**
   * Returns the name of the sub class for the given type that is type safe.
   * 
   * @param mdEnumerationIF
   * @return name of the sub class for the given type that is type safe.
   */
  protected static String getEnumSubClassName(MdEnumerationDAOIF mdEnumerationIF)
  {
    return mdEnumerationIF.getTypeName() + EntityQueryAPIGenerator.QUERY_API_SUFFIX;
  }

  /**
   * 
   * @param parent
   */
  protected void addExtends(MdClassDAOIF parentMdEntityIF)
  {
    if (parentMdEntityIF == null)
    {
      writeLine(srcBuffer, GeneratedBusinessQuery.class.getName());
    }
    else
    {
      writeLine(srcBuffer, EntityQueryAPIGenerator.getQueryClass(parentMdEntityIF));
    }
  }

  /**
   *
   *
   */
  protected void addConstructors()
  {
    // Constructor for the class
    String queryClass = BusinessQuery.class.getName();
    String queryVariable = CommonGenerationUtil.lowerFirstCharacter(BusinessQuery.class.getSimpleName());

    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "(" + QueryFactory.class.getName() + " componentQueryFactory)");
    writeLine(this.srcBuffer, "  {");

    if (this.getMdClassIF().getSuperClass() == null)
    {
      writeLine(this.srcBuffer, "     super();");
    }
    else
    {
      writeLine(this.srcBuffer, "    super(componentQueryFactory);");
    }
    writeLine(this.srcBuffer, "    if (this.getComponentQuery() == null)");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      " + queryClass + " " + queryVariable + " = componentQueryFactory.businessQuery(this.getClassType());");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "       this.setBusinessQuery(" + queryVariable + ");");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "(" + ValueQuery.class.getName() + " valueQuery)");
    writeLine(this.srcBuffer, "  {");

    if (this.getMdClassIF().getSuperClass() == null)
    {
      writeLine(this.srcBuffer, "     super();");
    }
    else
    {
      writeLine(this.srcBuffer, "    super(valueQuery);");
    }
    writeLine(this.srcBuffer, "    if (this.getComponentQuery() == null)");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      " + queryClass + " " + queryVariable + " = new " + queryClass + "(valueQuery, this.getClassType());");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "       this.setBusinessQuery(" + queryVariable + ");");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

  }

  /**
   * Generates methods on the child that fetch the parent.
   * 
   */
  private void addChildRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getParentMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }
      this.addChildMethod(mdRelationshipIF);
      this.addNotChildMethod(mdRelationshipIF);
    }
  }

  /**
   * Generates a method on the child that fetches the parent.
   * 
   * @param mdRelationshipIF
   */
  private void addChildMethod(MdRelationshipDAOIF mdRelationshipIF)
  {
    String methodName = CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getChildMethod());
    String relTypeClass = mdRelationshipIF.definesType() + ".CLASS";

    // Not an explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    // Explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn_SUBSELECT(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    MdBusinessDAOIF childMdBusinessIF = mdRelationshipIF.getChildMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
    {
      String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
      String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

      // Not an explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn_SUBSELECT(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }

    if (!GenerationUtil.isReservedAndHardcoded(childMdBusinessIF))
    {
      String childQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(childMdBusinessIF));
      String childQueryClass = EntityQueryAPIGenerator.getQueryClass(childMdBusinessIF);

      // Not an explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasChild(" + childQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + childQueryClass + " " + childQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasChild(" + childQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn_SUBSELECT(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        // Not an explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasChild(" + childQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");

        // Explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasChild(" + childQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn_SUBSELECT(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   * Generates a method on the child to query for objects that do not
   * participate in the relationship.
   * 
   * @param mdRelationshipIF
   */
  private void addNotChildMethod(MdRelationshipDAOIF mdRelationshipIF)
  {
    String methodName = NOT_IN_RELATIONSHIP_PREFIX + CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getChildMethod());
    String relTypeClass = mdRelationshipIF.definesType() + ".CLASS";

    // Not an explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotParentIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    // Explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotParentIn_SUBSELECT(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    MdBusinessDAOIF childMdBusinessIF = mdRelationshipIF.getChildMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
    {
      String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
      String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

      // Not an explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotParentIn(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotParentIn_SUBSELECT(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }

    if (!GenerationUtil.isReservedAndHardcoded(childMdBusinessIF))
    {
      String childQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(childMdBusinessIF));
      String childQueryClass = EntityQueryAPIGenerator.getQueryClass(childMdBusinessIF);

      // Not an explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasChild(" + childQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotParentIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + childQueryClass + " " + childQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasChild(" + childQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotParentIn_SUBSELECT(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        // Not an explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasChild(" + childQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotParentIn(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");

        // Explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasChild(" + childQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotParentIn_SUBSELECT(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  private void addEnumerationItemRelationships()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    if (mdBusinessIF.isEnumerationMasterMdBusiness())
    {
      List<MdEnumerationDAOIF> mdEnumerationList = mdBusinessIF.getMdEnumerationDAOs();
      for (MdEnumerationDAOIF mdEnumerationIF : mdEnumerationList)
      {
        this.addEnumerationItemRelationship(mdEnumerationIF);
        this.addEnumerationItemNotRelationship(mdEnumerationIF);
      }
    }
  }

  private void addEnumerationItemRelationship(MdEnumerationDAOIF mdEnumerationIF)
  {
    String methodName = mdEnumerationIF.getQueryMethodName();

    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "/**");
    writeLine(this.srcBuffer, " * ");
    writeLine(this.srcBuffer, " **/");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType() + ".CLASS);");
    writeLine(this.srcBuffer, "");
    write(this.srcBuffer, "    " + BusinessQuery.class.getName() + " businessQuery = ");
    writeLine(this.srcBuffer, "queryFactory.businessQuery(" + MdEnumerationInfo.CLASS + ".CLASS);");
    writeLine(this.srcBuffer, "    " + MdEnumerationDAOIF.class.getName() + " mdEnumerationIF = " + MdEnumerationDAO.class.getName() + ".getMdEnumerationDAO(" + mdEnumerationIF.definesType() + ".CLASS); ");
    writeLine(this.srcBuffer, "    businessQuery.WHERE(businessQuery.id().EQ(mdEnumerationIF.getId()));");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

  private void addEnumerationItemNotRelationship(MdEnumerationDAOIF mdEnumerationIF)
  {
    String methodName = mdEnumerationIF.getNegatedQueryMethodName();

    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "/**");
    writeLine(this.srcBuffer, " * ");
    writeLine(this.srcBuffer, " **/");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType() + ".CLASS);");
    writeLine(this.srcBuffer, "");
    write(this.srcBuffer, "    " + BusinessQuery.class.getName() + " businessQuery = ");
    writeLine(this.srcBuffer, "queryFactory.businessQuery(" + MdEnumerationInfo.CLASS + ".CLASS);");
    writeLine(this.srcBuffer, "    " + MdEnumerationDAOIF.class.getName() + " mdEnumerationIF = " + MdEnumerationDAO.class.getName() + ".getMdEnumerationDAO(" + mdEnumerationIF.definesType() + ".CLASS); ");
    writeLine(this.srcBuffer, "    businessQuery.WHERE(businessQuery.id().EQ(mdEnumerationIF.getId()));");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    relationshipQuery.WHERE(relationshipQuery.hasParent(businessQuery));");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

  /**
   * Generates methods on the parent that fetch the child.
   * 
   */
  private void addParentRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getChildMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }
      this.addParentMethod(mdRelationshipIF);
      this.addNotParentMethod(mdRelationshipIF);
    }
  }

  /**
   * Generates a method on the parent that fetches the child.
   * 
   * @param mdRelationshipIF
   */
  private void addParentMethod(MdRelationshipDAOIF mdRelationshipIF)
  {
    String methodName = CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getParentMethod());
    String relTypeClass = mdRelationshipIF.definesType() + ".CLASS";

    // Not an explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    // Explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn_SUBSELECT(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    MdBusinessDAOIF parentMdBusinessIF = mdRelationshipIF.getParentMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
    {
      String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
      String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

      // Not an explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn_SUBSELECT(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }

    if (!GenerationUtil.isReservedAndHardcoded(parentMdBusinessIF))
    {
      String parentQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(parentMdBusinessIF));
      String parentQueryClass = EntityQueryAPIGenerator.getQueryClass(parentMdBusinessIF);

      // Not an explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasParent(" + parentQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasParent(" + parentQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn_SUBSELECT(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        // Not an explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasParent(" + parentQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");

        // Explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasParent(" + parentQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.getBusinessQuery().isChildIn_SUBSELECT(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   * Generates a method on the parent that fetches the child.
   * 
   * @param mdRelationshipIF
   */
  private void addNotParentMethod(MdRelationshipDAOIF mdRelationshipIF)
  {
    String methodName = NOT_IN_RELATIONSHIP_PREFIX + CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getParentMethod());
    String relTypeClass = mdRelationshipIF.definesType() + ".CLASS";

    // Not an explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    // Explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn_SUBSELECT(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    MdBusinessDAOIF parentMdBusinessIF = mdRelationshipIF.getParentMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
    {
      String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
      String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

      // Not an explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn_SUBSELECT(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }

    if (!GenerationUtil.isReservedAndHardcoded(parentMdBusinessIF))
    {
      String parentQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(parentMdBusinessIF));
      String parentQueryClass = EntityQueryAPIGenerator.getQueryClass(parentMdBusinessIF);

      // Not an explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasParent(" + parentQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasParent(" + parentQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn_SUBSELECT(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        // Not an explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasParent(" + parentQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");

        // Explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasParent(" + parentQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.getBusinessQuery().isNotChildIn_SUBSELECT(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   * Add methods that query by object state.
   * 
   */
  private void addStateQueryMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();
    if (!mdBusinessIF.hasStateMachine())
    {
      return;
    }

    MdStateMachineDAOIF stateMachine = mdBusinessIF.definesMdStateMachine();

    for (StateMasterDAOIF stateMaster : stateMachine.definesStateMasters())
    {
      // Get the status mdRelationship defined for the entry state
      MdRelationshipDAOIF mdRelationship = stateMachine.getMdStatus(stateMaster);

      String stateName = stateMaster.getName();
      String methodName = "state_" + stateName;
      writeLine(this.srcBuffer, "  /**");
      writeLine(this.srcBuffer, "   * Restricts query to [" + mdBusinessIF.definesType() + "] objects in the [" + stateName + "] state.");
      writeLine(this.srcBuffer, "   */");
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      // Using the string value instead of the constant, as we do not generate
      // classes for state transitions
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(\"" + mdRelationship.definesType() + "\");");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.getBusinessQuery().isParentIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }
  }

  /**
   * Add methods that query by object state.
   * 
   */
  private void addRefInterfaceStateQueryMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();
    if (!mdBusinessIF.hasStateMachine())
    {
      return;
    }

    MdStateMachineDAOIF stateMachine = mdBusinessIF.definesMdStateMachine();

    for (StateMasterDAOIF stateMaster : stateMachine.definesStateMasters())
    {
      String stateName = stateMaster.getName();
      String methodName = "state_" + stateName;
      writeLine(this.srcBuffer, "  /**");
      writeLine(this.srcBuffer, "   * Restricts query to [" + mdBusinessIF.definesType() + "] objects in the [" + stateName + "] state.");
      writeLine(this.srcBuffer, "   */");
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "();");
    }
  }

  /**
   * Add methods that query by object state.
   * 
   */
  private void addRefStateQueryMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();
    if (!mdBusinessIF.hasStateMachine())
    {
      return;
    }

    MdStateMachineDAOIF stateMachine = mdBusinessIF.definesMdStateMachine();

    for (StateMasterDAOIF stateMaster : stateMachine.definesStateMasters())
    {
      // Get the status mdRelationship defined for the entry state
      MdRelationshipDAOIF mdRelationship = stateMachine.getMdStatus(stateMaster);

      String stateName = stateMaster.getName();
      String methodName = "state_" + stateName;
      writeLine(this.srcBuffer, "  /**");
      writeLine(this.srcBuffer, "   * Restricts query to [" + mdBusinessIF.definesType() + "] objects in the [" + stateName + "] state.");
      writeLine(this.srcBuffer, "   */");
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      // Using the string value instead of the constant, as we do not generate
      // classes for state transitions
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(\"" + mdRelationship.definesType() + "\");");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isParentIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }
  }

  /**
   * 
   * Builds an inner interface that is implemented by the inner class that
   * extends AttributeEnumeration.
   * 
   */
  protected void enumInnerInterface()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();
    if (mdBusinessIF.isEnumerationMasterMdBusiness())
    {
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "/**");
      writeLine(this.srcBuffer, " * Interface that masks all type unsafe query methods and defines all type safe methods.");
      writeLine(this.srcBuffer, " * This type is used when a join is performed on this class as an enumeration.");
      writeLine(this.srcBuffer, " **/");
      write(this.srcBuffer, "  public interface " + getEnumInterfaceName(mdBusinessIF) + " extends ");

      if (!mdBusinessIF.isSystemPackage())
      {
        write(this.srcBuffer, Reloadable.class.getName() + ", ");
      }

      MdBusinessDAOIF parentMdBusinessIF = mdBusinessIF.getSuperClass();
      if (parentMdBusinessIF != null)
      {
        writeLine(this.srcBuffer, getEnumInterface(parentMdBusinessIF));
      }
      else
      {
        writeLine(this.srcBuffer, SelectableEnumeration.class.getName());
      }
      writeLine(this.srcBuffer, "  {");

      writeLine(this.srcBuffer, "");
      this.addInnerInterfaceAccessors();
      writeLine(this.srcBuffer, "");

      writeLine(this.srcBuffer, "  }");
    }
  }

  /**
   * 
   * Builds an inner class that extends AttributeEnumeration.
   * 
   */
  protected void enumInnerClass()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    if (mdBusinessIF.isEnumerationMasterMdBusiness())
    {
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "/**");
      writeLine(this.srcBuffer, " * Implements type safe query methods.");
      writeLine(this.srcBuffer, " * This type is used when a join is performed on this class as an enumeration.");
      writeLine(this.srcBuffer, " **/");
      write(this.srcBuffer, "  public static class " + getEnumClassName(mdBusinessIF) + " extends ");

      MdBusinessDAOIF parentMdBusinessIF = mdBusinessIF.getSuperClass();
      if (parentMdBusinessIF != null)
      {
        writeLine(this.srcBuffer, getEnumClass(parentMdBusinessIF));
      }
      else
      {
        writeLine(this.srcBuffer, AttributeEnumeration.class.getName());
      }

      write(this.srcBuffer, " implements " + getEnumInterfaceName(this.getMdClassIF()));

      if (!mdBusinessIF.isSystemPackage())
      {
        write(this.srcBuffer, ", " + Reloadable.class.getName());
      }
      writeLine(this.srcBuffer, "");

      writeLine(this.srcBuffer, "  {");

      this.addSerialVersionUIDForInnerClasses("ENUM_QUERY", this.getSignature());

      this.addEnumClassConstructor();
      this.addInnerClassAccessors();

      this.createAttributeRefFactory(this.srcBuffer);
      this.createAttributeStructFactory(this.srcBuffer);
      this.createAttributeLocalFactory(this.srcBuffer);
      this.createAttributeEnumerationFactory(this.srcBuffer);

      writeLine(this.srcBuffer, "  }");
    }
  }

  /**
    *
    *
    */
  protected void addEnumClassConstructor()
  {
    write(this.srcBuffer, "  public " + getEnumClassName(this.getMdClassIF()) + "(");
    write(this.srcBuffer, MdAttributeEnumerationDAOIF.class.getName() + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ");
    writeLine(this.srcBuffer, "String mdEnumerationTableName," + MdBusinessDAOIF.class.getName() + " masterMdBusinessIF, String masterTableAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String alias, String displayLabel)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

  /**
   * 
   * Builds an inner interfaces for each MdEnumeration that uses this class as a
   * master list. Each one of these inner interfaces that extends the inner
   * interface that extends AttributeEnumerationIF.
   * 
   */
  protected void enumInnerSubInterfaces()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    for (MdEnumerationDAOIF mdEnumerationIF : mdBusinessIF.getMdEnumerationDAOs())
    {
      this.enumInnerSubInterface(mdEnumerationIF);
    }
  }

  /**
   * 
   * Builds an inner interfaces for an MdEnumeration that uses this class as a
   * master list. The inner interfaceextends the inner interface that extends
   * AttributeEnumerationIF.
   * 
   */
  protected void enumInnerSubInterface(MdEnumerationDAOIF mdEnumerationIF)
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "/**");
    writeLine(this.srcBuffer, " * Specifies type safe query methods for the enumeration " + mdEnumerationIF.definesType() + ".");
    writeLine(this.srcBuffer, " * This type is used when a join is performed on this class as an enumeration.");
    writeLine(this.srcBuffer, " **/");
    write(this.srcBuffer, "  public interface " + getEnumSubInterfaceName(mdEnumerationIF) + " extends ");

    if (!mdBusinessIF.isSystemPackage())
    {
      write(this.srcBuffer, Reloadable.class.getName() + ", ");
    }

    write(this.srcBuffer, getEnumInterfaceName(mdBusinessIF));

    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "");

    this.enumInnerMethodDef("containsAny", mdEnumerationIF);
    writeLine(this.srcBuffer, ";");
    this.enumInnerMethodDef("notContainsAny", mdEnumerationIF);
    writeLine(this.srcBuffer, ";");
    this.enumInnerMethodDef("containsAll", mdEnumerationIF);
    writeLine(this.srcBuffer, ";");
    this.enumInnerMethodDef("notContainsAll", mdEnumerationIF);
    writeLine(this.srcBuffer, ";");
    this.enumInnerMethodDef("containsExactly", mdEnumerationIF);
    writeLine(this.srcBuffer, ";");

    writeLine(this.srcBuffer, "  }");
  }

  /**
   * 
   * @param methodName
   * @param mdEnumerationIF
   */
  private void enumInnerMethodDef(String methodName, MdEnumerationDAOIF mdEnumerationIF)
  {
    String parameterName = CommonGenerationUtil.lowerFirstCharacter(mdEnumerationIF.getTypeName());
    write(this.srcBuffer, "    public " + Condition.class.getName() + " " + methodName + "(" + mdEnumerationIF.definesType() + " ... " + parameterName + ")");
  }

  /**
   * 
   * Builds an inner class for each MdEnumeration that uses this class as a
   * master list. Each one of these inner classes that extends the inner class
   * that extends AttributeEnumeration.
   * 
   */
  protected void enumInnerSubClasses()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    for (MdEnumerationDAOIF mdEnumerationIF : mdBusinessIF.getMdEnumerationDAOs())
    {
      this.enumInnerSubClass(mdEnumerationIF);
    }
  }

  /**
   * 
   * Builds an inner class for an MdEnumeration that uses this class as a master
   * list. The inner classes extends the inner class that extends
   * AttributeEnumeration.
   * 
   */
  protected void enumInnerSubClass(MdEnumerationDAOIF mdEnumerationIF)
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "/**");
    writeLine(this.srcBuffer, " * Implements type safe query methods for the enumeration " + mdEnumerationIF.definesType() + ".");
    writeLine(this.srcBuffer, " * This type is used when a join is performed on this class as an enumeration.");
    writeLine(this.srcBuffer, " **/");
    write(this.srcBuffer, "  public static class " + getEnumSubClassName(mdEnumerationIF) + " extends " + getEnumClassName(mdBusinessIF));
    write(this.srcBuffer, " implements  " + getEnumSubInterfaceName(mdEnumerationIF));

    if (!mdBusinessIF.isSystemPackage())
    {
      write(this.srcBuffer, ", " + Reloadable.class.getName());
    }
    writeLine(this.srcBuffer, "");

    writeLine(this.srcBuffer, "  {");
    write(this.srcBuffer, "  public " + getEnumSubClassName(mdEnumerationIF) + "(");
    write(this.srcBuffer, MdAttributeEnumerationDAOIF.class.getName() + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ");
    writeLine(this.srcBuffer, "String mdEnumerationTableName," + MdBusinessDAOIF.class.getName() + " masterMdBusinessIF, String masterTableAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String alias, String displayLabel)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdEnumerationTableName, masterMdBusinessIF, masterTableAlias, rootQuery, tableJoinSet, alias, displayLabel);");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  }");

    this.enumInnerMethodImp("containsAny", mdEnumerationIF);
    this.enumInnerMethodImp("notContainsAny", mdEnumerationIF);
    this.enumInnerMethodImp("containsAll", mdEnumerationIF);
    this.enumInnerMethodImp("notContainsAll", mdEnumerationIF);
    this.enumInnerMethodImp("containsExactly", mdEnumerationIF);

    write(this.srcBuffer, "  }");
  }

  /**
    *
    */
  private void enumInnerMethodImp(String methodName, MdEnumerationDAOIF mdEnumerationIF)
  {
    String parameterName = CommonGenerationUtil.lowerFirstCharacter(mdEnumerationIF.getTypeName());
    writeLine(this.srcBuffer, "");
    this.enumInnerMethodDef(methodName, mdEnumerationIF);
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "      String[] enumIdArray = new String[" + parameterName + ".length]; ");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "      for (int i=0; i<" + parameterName + ".length; i++)");
    writeLine(this.srcBuffer, "      {");
    writeLine(this.srcBuffer, "        enumIdArray[i] = " + parameterName + "[i].getId();");
    writeLine(this.srcBuffer, "      }");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "      return this." + methodName + "(enumIdArray);");
    writeLine(this.srcBuffer, "  }");
  }

  /**
   * Adds the inner interface that is implemented by the inner class that
   * extends AttributeReference.
   * 
   */
  protected void refInnerInterfaceDef()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "/**");
    writeLine(this.srcBuffer, " * Interface that masks all type unsafe query methods and defines all type safe methods.");
    writeLine(this.srcBuffer, " * This type is used when a join is performed on this class as a reference.");
    writeLine(this.srcBuffer, " **/");
    write(this.srcBuffer, "  public interface " + getRefInterfaceName(mdBusinessIF) + " extends ");

    if (!mdBusinessIF.isSystemPackage())
    {
      write(this.srcBuffer, Reloadable.class.getName() + ", ");
    }

    MdBusinessDAOIF parentMdBusinessIF = mdBusinessIF.getSuperClass();
    if (parentMdBusinessIF != null)
    {
      writeLine(this.srcBuffer, getRefInterface(parentMdBusinessIF));
    }
    else
    {
      writeLine(this.srcBuffer, SelectableReference.class.getName());
    }
    writeLine(this.srcBuffer, "  {");

    writeLine(this.srcBuffer, "");
    this.addInnerInterfaceAccessors();
    writeLine(this.srcBuffer, "");

    String paramName = CommonGenerationUtil.lowerFirstCharacter(mdBusinessIF.getTypeName());
    writeLine(this.srcBuffer, "    public " + BasicCondition.class.getName() + " EQ(" + mdBusinessIF.definesType() + " " + paramName + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    public " + BasicCondition.class.getName() + " NE(" + mdBusinessIF.definesType() + " " + paramName + ");");
    writeLine(this.srcBuffer, "");

    this.addRefIFChildRelationshipMethods();
    this.addRefIFNotInChildRelationshipMethods();
    this.addRefIFParentRelationshipMethods();
    this.addRefIFNotInParentRelationshipMethods();
    this.addRefInterfaceStateQueryMethods();

    writeLine(this.srcBuffer, "  }");
  }

  /**
   *
   *
   */
  protected void refInnerClassDef()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "/**");
    writeLine(this.srcBuffer, " * Implements type safe query methods.");
    writeLine(this.srcBuffer, " * This type is used when a join is performed on this class as a reference.");
    writeLine(this.srcBuffer, " **/");
    write(this.srcBuffer, "  public static class " + getRefClassName(mdBusinessIF) + " extends ");

    MdBusinessDAOIF parentMdBusinessIF = mdBusinessIF.getSuperClass();
    if (parentMdBusinessIF != null)
    {
      writeLine(this.srcBuffer, getRefClass(parentMdBusinessIF));
    }
    else
    {
      if (mdBusinessIF instanceof MdTermDAOIF)
      {
        writeLine(this.srcBuffer, AttributeTerm.class.getName());
      }
      else
      {
        writeLine(this.srcBuffer, AttributeReference.class.getName());
      }
    }

    writeLine(this.srcBuffer, " implements " + getRefInterfaceName(this.getMdClassIF()));

    if (!mdBusinessIF.isSystemPackage())
    {
      write(this.srcBuffer, ", " + Reloadable.class.getName());
    }
    writeLine(this.srcBuffer, "");

    writeLine(this.srcBuffer, "  {");

    this.addSerialVersionUIDForInnerClasses("REF_QUERY", this.getSignature());

    this.addRefClassConstructor();

    writeLine(this.srcBuffer, "");
    String paramName = CommonGenerationUtil.lowerFirstCharacter(mdBusinessIF.getTypeName());
    writeLine(this.srcBuffer, "    public " + BasicCondition.class.getName() + " EQ(" + mdBusinessIF.definesType() + " " + paramName + ")");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      if(" + paramName + " == null) return this.EQ((" + String.class.getName() + ")null);");
    writeLine(this.srcBuffer, "      return this.EQ(" + paramName + ".getId());");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    public " + BasicCondition.class.getName() + " NE(" + mdBusinessIF.definesType() + " " + paramName + ")");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      if(" + paramName + " == null) return this.NE((" + String.class.getName() + ")null);");
    writeLine(this.srcBuffer, "      return this.NE(" + paramName + ".getId());");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "");

    this.addInnerClassAccessors();

    this.addRefChildRelationshipMethods();
    this.addRefNotInChildRelationshipMethods();
    this.addRefParentRelationshipMethods();
    this.addRefNotInParentRelationshipMethods();

    this.addRefStateQueryMethods();

    this.createAttributeRefFactory(this.srcBuffer);
    this.createAttributeStructFactory(this.srcBuffer);
    this.createAttributeLocalFactory(this.srcBuffer);
    this.createAttributeEnumerationFactory(this.srcBuffer);

    writeLine(this.srcBuffer, "  }");
  }

  /**
   * Generates methods on the child that fetch the parent.
   * 
   */
  private void addRefIFChildRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getParentMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }

      this.addRefIFChildMethod(mdRelationshipIF, "", "");
      this.addRefIFChildMethod(mdRelationshipIF, "", SUBSELECT_RELATIONSHIP_PREFIX);
    }
  }

  /**
   * Generates methods on the child that fetch the parent.
   * 
   */
  private void addRefIFNotInChildRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getParentMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }

      this.addRefIFChildMethod(mdRelationshipIF, NOT_IN_RELATIONSHIP_PREFIX, "");
      this.addRefIFChildMethod(mdRelationshipIF, NOT_IN_RELATIONSHIP_PREFIX, SUBSELECT_RELATIONSHIP_PREFIX);
    }
  }

  /**
   * Generates a method on the child that fetches the parent.
   * 
   * @param mdRelationshipIF
   * @param methodPrefix
   */
  private void addRefIFChildMethod(MdRelationshipDAOIF mdRelationshipIF, String methodPrefix, String subSelectMethodPrefix)
  {
    String methodName = subSelectMethodPrefix + methodPrefix + CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getChildMethod());

    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "();");
    writeLine(this.srcBuffer, "");

    MdBusinessDAOIF childMdBusinessIF = mdRelationshipIF.getChildMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(childMdBusinessIF))
    {
      String childQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(childMdBusinessIF));
      String childQueryClass = EntityQueryAPIGenerator.getQueryClass(childMdBusinessIF);

      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ");");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   * Generates methods on the parent that fetch the child.
   * 
   */
  private void addRefIFParentRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getChildMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }

      this.addRefIFParentMethod(mdRelationshipIF, "", "");
      this.addRefIFParentMethod(mdRelationshipIF, "", SUBSELECT_RELATIONSHIP_PREFIX);
    }
  }

  /**
   * Generates methods on the parent that fetch the child.
   * 
   */
  private void addRefIFNotInParentRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getChildMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }

      this.addRefIFParentMethod(mdRelationshipIF, NOT_IN_RELATIONSHIP_PREFIX, "");
      this.addRefIFParentMethod(mdRelationshipIF, NOT_IN_RELATIONSHIP_PREFIX, SUBSELECT_RELATIONSHIP_PREFIX);
    }
  }

  /**
   * Generates a method on the parent that fetches the child.
   * 
   * @param mdRelationshipIF
   */
  private void addRefIFParentMethod(MdRelationshipDAOIF mdRelationshipIF, String methodPrefix, String subSelectPrefix)
  {
    String methodName = subSelectPrefix + methodPrefix + CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getParentMethod());

    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "();");

    MdBusinessDAOIF parentMdBusinessIF = mdRelationshipIF.getParentMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(parentMdBusinessIF))
    {
      String parentQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(parentMdBusinessIF));
      String parentQueryClass = EntityQueryAPIGenerator.getQueryClass(parentMdBusinessIF);

      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ");");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   * Generates methods on the child that fetch the parent.
   * 
   */
  private void addRefChildRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getParentMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }
      this.addRefChildMethod(mdRelationshipIF);
    }
  }

  /**
   * Generates a method on the child that fetches the parent.
   * 
   * @param mdRelationshipIF
   */
  private void addRefChildMethod(MdRelationshipDAOIF mdRelationshipIF)
  {
    String methodName = CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getChildMethod());
    String relTypeClass = mdRelationshipIF.definesType() + ".CLASS";

    // Not a explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.isParentIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    // Explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.isParentIn_SUBSELECT(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    MdBusinessDAOIF childMdBusinessIF = mdRelationshipIF.getChildMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
    {
      String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
      String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

      // Not a explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.isParentIn(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.isParentIn_SUBSELECT(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }

    if (!GenerationUtil.isReservedAndHardcoded(childMdBusinessIF))
    {
      String childQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(childMdBusinessIF));
      String childQueryClass = EntityQueryAPIGenerator.getQueryClass(childMdBusinessIF);

      // Not a explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasChild(" + childQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isParentIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + childQueryClass + " " + childQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasChild(" + childQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isParentIn_SUBSELECT(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        // Not a explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasChild(" + childQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.isParentIn(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");

        // Explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasChild(" + childQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.isParentIn_SUBSELECT(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   * Generates methods on the child that fetch the parent.
   * 
   */
  private void addRefNotInChildRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getParentMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }
      this.addRefNotInChildMethod(mdRelationshipIF);
    }
  }

  /**
   * Generates a method on the child that fetches the parent.
   * 
   * @param mdRelationshipIF
   */
  private void addRefNotInChildMethod(MdRelationshipDAOIF mdRelationshipIF)
  {
    String methodName = NOT_IN_RELATIONSHIP_PREFIX + CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getChildMethod());
    String relTypeClass = mdRelationshipIF.definesType() + ".CLASS";

    // Not a explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.isNotParentIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    // Explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.isNotParentIn_SUBSELECT(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
    {
      String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
      String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

      // Not a explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.isNotParentIn(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.isNotParentIn_SUBSELECT(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }

    MdBusinessDAOIF childMdBusinessIF = mdRelationshipIF.getChildMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(childMdBusinessIF))
    {
      String childQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(childMdBusinessIF));
      String childQueryClass = EntityQueryAPIGenerator.getQueryClass(childMdBusinessIF);

      // Not a explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasChild(" + childQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isNotParentIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + childQueryClass + " " + childQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasChild(" + childQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isNotParentIn_SUBSELECT(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        // Not a explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasChild(" + childQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.isNotParentIn(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");

        // Explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + childQueryClass + " " + childQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasChild(" + childQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.isNotParentIn_SUBSELECT(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   * Generates methods on the parent that fetch the child.
   * 
   */
  private void addRefParentRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getChildMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }
      this.addRefParentMethod(mdRelationshipIF);
    }
  }

  /**
   * Generates a method on the parent that fetches the child.
   * 
   * @param mdRelationshipIF
   */
  private void addRefParentMethod(MdRelationshipDAOIF mdRelationshipIF)
  {
    String methodName = CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getParentMethod());
    String relTypeClass = mdRelationshipIF.definesType() + ".CLASS";

    // Not a explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.isChildIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    // Explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.isChildIn_SUBSELECT(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
    {
      String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
      String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

      // Not a explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.isChildIn(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.isChildIn_SUBSELECT(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }

    MdBusinessDAOIF parentMdBusinessIF = mdRelationshipIF.getParentMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(parentMdBusinessIF))
    {
      String parentQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(parentMdBusinessIF));
      String parentQueryClass = EntityQueryAPIGenerator.getQueryClass(parentMdBusinessIF);

      // Not a explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasParent(" + parentQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isChildIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasParent(" + parentQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isChildIn_SUBSELECT(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        // Not a explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasParent(" + parentQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.isChildIn(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");

        // Explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasParent(" + parentQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.isChildIn_SUBSELECT(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   * Generates methods on the parent that fetch the child.
   * 
   */
  private void addRefNotInParentRelationshipMethods()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();

    List<MdRelationshipDAOIF> mdRelationshipIFList = mdBusinessIF.getChildMdRelationshipsOrdered();

    for (MdRelationshipDAOIF mdRelationshipIF : mdRelationshipIFList)
    {
      if (BusinessBaseGenerator.isStatus(mdBusinessIF, mdRelationshipIF) || BusinessBaseGenerator.isStateMachine(mdRelationshipIF))
      {
        continue;
      }
      this.addRefNotInParentMethod(mdRelationshipIF);
    }
  }

  /**
   * Generates a method on the parent that fetches the child.
   * 
   * @param mdRelationshipIF
   */
  private void addRefNotInParentMethod(MdRelationshipDAOIF mdRelationshipIF)
  {
    String methodName = NOT_IN_RELATIONSHIP_PREFIX + CommonGenerationUtil.lowerFirstCharacter(mdRelationshipIF.getParentMethod());
    String relTypeClass = mdRelationshipIF.definesType() + ".CLASS";

    // Not a explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.isNotChildIn(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    // Explicit subselect
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
    write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
    writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    return this.isNotChildIn_SUBSELECT(relationshipQuery);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

    if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
    {
      String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
      String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

      // Not a explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.isNotChildIn(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    return this.isNotChildIn_SUBSELECT(" + relationshipQueryClassParam + ");");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");
    }

    MdBusinessDAOIF parentMdBusinessIF = mdRelationshipIF.getParentMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(parentMdBusinessIF))
    {
      String parentQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(parentMdBusinessIF));
      String parentQueryClass = EntityQueryAPIGenerator.getQueryClass(parentMdBusinessIF);

      // Not a explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasParent(" + parentQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isNotChildIn(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      // Explicit subselect
      writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ")");
      writeLine(this.srcBuffer, "  {");
      writeLine(this.srcBuffer, "    " + QueryFactory.class.getName() + " queryFactory = this.getQueryFactory();");
      write(this.srcBuffer, "    " + RelationshipQuery.class.getName() + " relationshipQuery = ");
      writeLine(this.srcBuffer, "queryFactory.relationshipQuery(" + relTypeClass + ");");
      writeLine(this.srcBuffer, "    relationshipQuery.AND(relationshipQuery.hasParent(" + parentQueryClassParam + "));");
      writeLine(this.srcBuffer, "");
      writeLine(this.srcBuffer, "    return this.isNotChildIn_SUBSELECT(relationshipQuery);");
      writeLine(this.srcBuffer, "  }");
      writeLine(this.srcBuffer, "");

      if (!GenerationUtil.isReservedAndHardcoded(mdRelationshipIF))
      {
        String relationshipQueryClassParam = CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(mdRelationshipIF));
        String relationshipQueryClass = EntityQueryAPIGenerator.getQueryClass(mdRelationshipIF);

        // Not a explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasParent(" + parentQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.isNotChildIn(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");

        // Explicit subselect
        writeLine(this.srcBuffer, "  public " + Condition.class.getName() + " " + SUBSELECT_RELATIONSHIP_PREFIX + methodName + "(" + parentQueryClass + " " + parentQueryClassParam + ", " + relationshipQueryClass + " " + relationshipQueryClassParam + ")");
        writeLine(this.srcBuffer, "  {");
        writeLine(this.srcBuffer, "    " + relationshipQueryClassParam + ".AND(" + relationshipQueryClassParam + ".hasParent(" + parentQueryClassParam + "));");
        writeLine(this.srcBuffer, "    return this.isNotChildIn_SUBSELECT(" + relationshipQueryClassParam + ");");
        writeLine(this.srcBuffer, "  }");
        writeLine(this.srcBuffer, "");
      }
    }
  }

  /**
   *
   *
   */
  protected void addRefClassConstructor()
  {
    MdBusinessDAOIF mdBusinessIF = this.getMdClassIF();
    write(this.srcBuffer, "  public " + getRefClassName(mdBusinessIF) + "(");
    write(this.srcBuffer, MdAttributeRefDAOIF.class.getName() + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ");
    writeLine(this.srcBuffer, MdBusinessDAOIF.class.getName() + " referenceMdBusinessIF, String referenceTableAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String alias, String displayLabel)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, referenceMdBusinessIF, referenceTableAlias, rootQuery, tableJoinSet, alias, displayLabel);");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

  /**
   * Creates emthods that will return type safe iterators of the query result.
   * 
   */
  protected void createIteratorMethods()
  {
    writeLine(this.srcBuffer, "  /**  ");
    writeLine(this.srcBuffer, "   * Returns an iterator of Business objects that match the query criteria specified");
    writeLine(this.srcBuffer, "   * on this query object. ");
    writeLine(this.srcBuffer, "   * @return iterator of Business objects that match the query criteria specified");
    writeLine(this.srcBuffer, "   * on this query object.");
    writeLine(this.srcBuffer, "   */");
    writeLine(this.srcBuffer, "  public " + OIterator.class.getName() + "<? extends " + this.getMdClassIF().getTypeName() + ">" + " " + EntityQueryAPIGenerator.ITERATOR_METHOD + "()");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    this.checkNotUsedInValueQuery();");
    writeLine(this.srcBuffer, "    String sqlStmt;");
    writeLine(this.srcBuffer, "    if (_limit != null && _skip != null)");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      sqlStmt = this.getComponentQuery().getSQL(_limit, _skip);");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "    else");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      sqlStmt = this.getComponentQuery().getSQL();");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "    " + Map.class.getName() + "<String, " + ColumnInfo.class.getName() + "> columnInfoMap = this.getComponentQuery().getColumnInfoMap();");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    " + java.sql.ResultSet.class.getName() + " results = " + Database.class.getName() + ".query(sqlStmt);");
    writeLine(this.srcBuffer, "    return new " + BusinessIterator.class.getName() + "<" + this.getMdClassIF().getTypeName() + ">(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

}
