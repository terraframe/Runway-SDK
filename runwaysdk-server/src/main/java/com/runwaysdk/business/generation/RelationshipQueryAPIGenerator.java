/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.business.generation;

import java.util.Map;

import com.runwaysdk.business.RelationshipIterator;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.GeneratedRelationshipQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;

public class RelationshipQueryAPIGenerator extends EntityQueryAPIGenerator
{
  public RelationshipQueryAPIGenerator(MdRelationshipDAOIF mdRelationshipIF)
  {
    super(mdRelationshipIF);
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdClassIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // do not generate if the file has already been generated and is semantically equivilant.
    if (LocalProperties.isKeepBaseSource() &&
        AbstractGenerator.hashEquals(this.getSerialVersionUID(), TypeGenerator.getQueryAPIsourceFilePath(this.getMdClassIF())))
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

    this.buildHasChildMethod();
    this.buildDoesNotHaveChildMethod();
    this.buildHasParentMethod();
    this.buildDoesNotHaveParentMethod();

    this.createAttributeRefFactory(this.srcBuffer);
    this.createAttributeStructFactory(this.srcBuffer);
    this.createAttributeLocalFactory(this.srcBuffer);
    this.createAttributeEnumerationFactory(this.srcBuffer);
    this.createAttributeMultiReferenceFactory(this.srcBuffer);
    this.addAccessors();

    this.createIteratorMethods();

    this.close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("BASE_QUERY", this.getSignature());
  }

  /**
   *
   * @param parentMdEntityIF
   */
  protected void addExtends(MdClassDAOIF parentMdEntityIF)
  {
    if (parentMdEntityIF == null)
    {
      write(srcBuffer, GeneratedRelationshipQuery.class.getName());
    }
    else
    {
      write(srcBuffer, EntityQueryAPIGenerator.getQueryClass((MdEntityDAOIF)parentMdEntityIF));
    }
  }

  /**
   *
   *
   */
  protected void addConstructors()
  {
    //Constructor for the class
    String queryClass = RelationshipQuery.class.getName();
    String queryVariable = CommonGenerationUtil.lowerFirstCharacter(RelationshipQuery.class.getSimpleName());

    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "("+QueryFactory.class.getName()+" componentQueryFactory)");
    writeLine(this.srcBuffer, "  {");

    if (this.getMdClassIF().getSuperClass() == null)
    {
      writeLine(this.srcBuffer, "     super();");
    }
    else
    {
      writeLine(this.srcBuffer, "     super(componentQueryFactory);");
    }
    writeLine(this.srcBuffer, "    if (this.getComponentQuery() == null)");
    writeLine(this.srcBuffer, "    {");
    writeLine(this.srcBuffer, "      "+queryClass+" "+queryVariable+" = componentQueryFactory.relationshipQuery(this.getClassType());");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "       this.setRelationshipQuery("+queryVariable+");");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");


    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "("+ValueQuery.class.getName()+" valueQuery)");
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
    writeLine(this.srcBuffer, "      "+queryClass+" "+queryVariable+" = new "+queryClass+"(valueQuery, this.getClassType());");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "       this.setRelationshipQuery("+queryVariable+");");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

  }

  /**
   *
   *
   */
  protected void buildHasChildMethod()
  {
    MdBusinessDAOIF childMdBusinessIF = this.getMdClassIF().getChildMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(childMdBusinessIF))
    {
      String childQueryClassVar =
        CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(childMdBusinessIF));
      String childQueryClass = EntityQueryAPIGenerator.getQueryClass(childMdBusinessIF);

      writeLine(this.srcBuffer, "  /**");
      writeLine(this.srcBuffer, "   * Restricts the query to include objects that are children in this relationship.");
      writeLine(this.srcBuffer, "   * @param "+childQueryClassVar);
      writeLine(this.srcBuffer, "   * @return Condition restricting objects that are children in this relationship.");
      writeLine(this.srcBuffer, "   */");
      writeLine(this.srcBuffer, "   public "+Condition.class.getName()+" hasChild("+childQueryClass+" "+childQueryClassVar+")");
      writeLine(this.srcBuffer, "   {");
      writeLine(this.srcBuffer, "     return this.getRelationshipQuery().hasChild("+childQueryClassVar+");");
      writeLine(this.srcBuffer, "   }");
    }
  }

  /**
   *
   *
   */
  protected void buildDoesNotHaveChildMethod()
  {
    MdBusinessDAOIF childMdBusinessIF = this.getMdClassIF().getChildMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(childMdBusinessIF))
    {
      String childQueryClassVar =
        CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(childMdBusinessIF));
      String childQueryClass = EntityQueryAPIGenerator.getQueryClass(childMdBusinessIF);

      writeLine(this.srcBuffer, "  /**");
      writeLine(this.srcBuffer, "   * Restricts the query to include objects that are children in this relationship.");
      writeLine(this.srcBuffer, "   * @param "+childQueryClassVar);
      writeLine(this.srcBuffer, "   * @return Condition restricting objects that are children in this relationship.");
      writeLine(this.srcBuffer, "   */");
      writeLine(this.srcBuffer, "   public "+Condition.class.getName()+" doesNotHaveChild("+childQueryClass+" "+childQueryClassVar+")");
      writeLine(this.srcBuffer, "   {");
      writeLine(this.srcBuffer, "     return this.getRelationshipQuery().doesNotHaveChild("+childQueryClassVar+");");
      writeLine(this.srcBuffer, "   }");
    }
  }

  /**
   *
   *
   */
  protected void buildHasParentMethod()
  {
    MdBusinessDAOIF parentMdBusinessIF = this.getMdClassIF().getParentMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(parentMdBusinessIF))
    {
      String parentQueryClassVar =
        CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(parentMdBusinessIF));
      String parentQueryClass = EntityQueryAPIGenerator.getQueryClass(parentMdBusinessIF);

      writeLine(this.srcBuffer, "  /**");
      writeLine(this.srcBuffer, "   * Restricts the query to include objects that are parents in this relationship.");
      writeLine(this.srcBuffer, "   * @param "+parentQueryClassVar);
      writeLine(this.srcBuffer, "   * @return Condition restricting objects that are parents in this relationship.");
      writeLine(this.srcBuffer, "   */");
      writeLine(this.srcBuffer, "   public "+Condition.class.getName()+" hasParent("+parentQueryClass+" "+parentQueryClassVar+")");
      writeLine(this.srcBuffer, "   {");
      writeLine(this.srcBuffer, "     return this.getRelationshipQuery().hasParent("+parentQueryClassVar+");");
      writeLine(this.srcBuffer, "   }");
    }
  }

  /**
   *
   *
   */
  protected void buildDoesNotHaveParentMethod()
  {
    MdBusinessDAOIF parentMdBusinessIF = this.getMdClassIF().getParentMdBusiness();

    if (!GenerationUtil.isReservedAndHardcoded(parentMdBusinessIF))
    {
      String parentQueryClassVar =
        CommonGenerationUtil.lowerFirstCharacter(EntityQueryAPIGenerator.getQueryClassName(parentMdBusinessIF));
      String parentQueryClass = EntityQueryAPIGenerator.getQueryClass(parentMdBusinessIF);

      writeLine(this.srcBuffer, "  /**");
      writeLine(this.srcBuffer, "   * Restricts the query to include objects that are parents in this relationship.");
      writeLine(this.srcBuffer, "   * @param "+parentQueryClassVar);
      writeLine(this.srcBuffer, "   * @return Condition restricting objects that are parents in this relationship.");
      writeLine(this.srcBuffer, "   */");
      writeLine(this.srcBuffer, "   public "+Condition.class.getName()+" doesNotHaveParent("+parentQueryClass+" "+parentQueryClassVar+")");
      writeLine(this.srcBuffer, "   {");
      writeLine(this.srcBuffer, "     return this.getRelationshipQuery().doesNotHaveParent("+parentQueryClassVar+");");
      writeLine(this.srcBuffer, "   }");
    }
  }

  /**
   * Returns the reference to the MdRelationshipIF object that defines the entity type
   * for which this object generates a query API object for.
   * @return reference to the MdRelationshipIF object that defines the entity type
   * for which this object generates a query API object for.
   */
  protected MdRelationshipDAOIF getMdClassIF()
  {
    return (MdRelationshipDAOIF)super.getMdClassIF();
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
    writeLine(this.srcBuffer, "  public "+OIterator.class.getName()+"<? extends "+this.getMdClassIF().getTypeName()+">"+" "+EntityQueryAPIGenerator.ITERATOR_METHOD+""+"()");
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
    writeLine(this.srcBuffer, "    "+Map.class.getName()+"<String, "+ColumnInfo.class.getName()+"> columnInfoMap = this.getComponentQuery().getColumnInfoMap();");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "    "+java.sql.ResultSet.class.getName()+" results = "+Database.class.getName()+".query(sqlStmt);");
    writeLine(this.srcBuffer, "    return new "+RelationshipIterator.class.getName()+"<"+this.getMdClassIF().getTypeName()+">(this.getComponentQuery().getMdEntityIF(), this.getRelationshipQuery(), columnInfoMap, results);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

}
