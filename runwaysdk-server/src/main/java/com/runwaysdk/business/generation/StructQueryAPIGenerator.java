/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
 */
package com.runwaysdk.business.generation;

import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.StructIterator;
import com.runwaysdk.business.StructQuery;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdLocalStructDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.query.AttributeLocal;
import com.runwaysdk.query.AttributeLocalIF;
import com.runwaysdk.query.AttributeStruct;
import com.runwaysdk.query.ColumnInfo;
import com.runwaysdk.query.ComponentQuery;
import com.runwaysdk.query.GeneratedStructQuery;
import com.runwaysdk.query.Join;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.SelectableStruct;
import com.runwaysdk.query.ValueQuery;

public class StructQueryAPIGenerator extends EntityQueryAPIGenerator
{

  public StructQueryAPIGenerator(MdStructDAOIF mdStructIF)
  {
    super(mdStructIF);
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
    this.createAttributeMultiReferenceFactory(this.srcBuffer);
    this.createIteratorMethods();

    this.refInnerInterfaceDef();
    this.structInnerClassDef();
    this.close();
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("BASE_QUERY", this.getSignature());
  }

  /**
   * Returns the name of the interface for the given type which extends
   * AttributeStruct.
   * 
   * @param type
   *          protected
   * @return name of the interface for the given type which extends
   *         AttributeStruct.
   */
  protected static String getAttrStructIntefaceName(String type)
  {
    return type + "QueryStructIF";
  }

  /**
   * Returns the qualified name of the interface for the given type which
   * extends AttributeStruct.
   * 
   * @param type
   * @return qualified name of the interface for the given type which extends
   *         AttributeStruct.
   */
  protected static String getAttrStructInterface(MdStructDAOIF mdStructIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdStructIF) + "." + mdStructIF.getTypeName() + "QueryStructIF";
  }

  /**
   * Returns the qualified name of the file of the interface for the given type
   * which extends AttributeStruct.
   * 
   * @param type
   * @return qualified name of the interface for the given type which extends
   *         AttributeStruct.
   */
  public static String getAttrStructInterfaceCompiled(MdStructDAOIF mdStructIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdStructIF) + "$" + mdStructIF.getTypeName() + "QueryStructIF";
  }

  /**
   * Returns the name of the class for the given type which extends
   * AttributeStruct.
   * 
   * @param type
   * @return name of the class for the given type which extends AttributeStruct.
   */
  protected static String getAttrStructClassName(String type)
  {
    return type + "QueryStruct";
  }

  /**
   * Returns the qualified name of the class for the given type which extends
   * AttributeStruct.
   * 
   * @param type
   * @return qualified name of the inner for the given type which extends
   *         AttributeStruct.
   */
  protected static String getAttrStructClass(MdStructDAOIF mdStructIF)
  {
    return EntityQueryAPIGenerator.getQueryClass(mdStructIF) + "." + mdStructIF.getTypeName() + "QueryStruct";
  }

  /**
   * 
   * @param parent
   */
  protected void addExtends(MdClassDAOIF parentMdEntityIF)
  {
    // Structs have no parents, so parentMdEntityIF will be null
    write(this.srcBuffer, GeneratedStructQuery.class.getName());
  }

  /**
   *
   *
   */
  protected void addConstructors()
  {
    // Constructor for the class
    String queryClass = StructQuery.class.getName();
    String queryVariable = CommonGenerationUtil.lowerFirstCharacter(StructQuery.class.getSimpleName());

    writeLine(this.srcBuffer, "  public " + this.queryTypeName + "(" + QueryFactory.class.getName() + " componentQueryFactory)");
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
    writeLine(this.srcBuffer, "      " + queryClass + " " + queryVariable + " = componentQueryFactory.structQuery(this.getClassType());");
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "       this.setStructQuery(" + queryVariable + ");");
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
    writeLine(this.srcBuffer, "       this.setStructQuery(" + queryVariable + ");");
    writeLine(this.srcBuffer, "    }");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");

  }

  /**
   *
   *
   */
  protected void refInnerInterfaceDef()
  {
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "/**");
    writeLine(this.srcBuffer, " * Interface that masks all type unsafe query methods and defines all type safe methods.");
    writeLine(this.srcBuffer, " * This type is used when a join is performed on this class as a struct attribute.");
    writeLine(this.srcBuffer, " **/");
    ;

    write(this.srcBuffer, "  public interface " + getAttrStructIntefaceName(this.getMdClassIF().getTypeName()));

    if (this.getMdClassIF() instanceof MdLocalStructDAOIF)
    {
      write(this.srcBuffer, " extends " + AttributeLocalIF.class.getName());
    }
    else
    {
      write(this.srcBuffer, " extends " + SelectableStruct.class.getName());
    }

    writeLine(this.srcBuffer, "  {");

    writeLine(this.srcBuffer, "");
    this.addInnerInterfaceAccessors();
    writeLine(this.srcBuffer, "");

    writeLine(this.srcBuffer, "  }");
  }

  /**
   *
   *
   */
  protected void structInnerClassDef()
  {
    writeLine(this.srcBuffer, "");
    writeLine(this.srcBuffer, "/**");
    writeLine(this.srcBuffer, " * Implements type safe query methods.");
    writeLine(this.srcBuffer, " * This type is used when a join is performed on this class as a struct attribute.");
    writeLine(this.srcBuffer, " **/");
    write(this.srcBuffer, "  public static class " + getAttrStructClassName(this.getMdClassIF().getTypeName()) + " extends ");

    if (this.getMdClassIF() instanceof MdLocalStructDAOIF)
    {
      write(this.srcBuffer, AttributeLocal.class.getName());
    }
    else
    {
      write(this.srcBuffer, AttributeStruct.class.getName());
    }

    write(this.srcBuffer, " implements " + getAttrStructIntefaceName(this.getMdClassIF().getTypeName()));

    writeLine(this.srcBuffer, "  {");

    this.addSerialVersionUIDForInnerClasses("STRUCT_QUERY", this.getSignature());

    this.addAttrStructConstructor();
    this.addInnerClassAccessors();

    // Technically Struct attributes cannot reference other objects, but I am
    // including this method call anyway, as this
    // decision may change later and I won't have to go back and add this line
    // back in. As of now, it will produce nothing.
    this.createAttributeRefFactory(this.srcBuffer);
    this.createAttributeStructFactory(this.srcBuffer);
    this.createAttributeLocalFactory(this.srcBuffer);
    this.createAttributeEnumerationFactory(this.srcBuffer);
    this.createAttributeMultiReferenceFactory(this.srcBuffer);

    writeLine(srcBuffer, "  }");
  }

  /**
   *
   *
   */
  protected void addAttrStructConstructor()
  {
    write(this.srcBuffer, "  public " + getAttrStructClassName(this.getMdClassIF().getTypeName()) + "(");

    String mdStructClassName = MdStructDAOIF.class.getName();
    String mdAttrClassName = MdAttributeStructDAOIF.class.getName();

    if (this.getMdClassIF() instanceof MdLocalStructDAOIF)
    {
      mdStructClassName = MdLocalStructDAOIF.class.getName();
      mdAttrClassName = MdAttributeLocalDAOIF.class.getName();
    }

    write(this.srcBuffer, mdAttrClassName + " mdAttributeIF, String attributeNamespace, String definingTableName, String definingTableAlias, ");
    writeLine(this.srcBuffer, mdStructClassName + " mdStructIF, String structTableAlias, " + ComponentQuery.class.getName() + " rootQuery, " + Set.class.getName() + "<" + Join.class.getName() + "> tableJoinSet, String alias, String displayLabel)");
    writeLine(this.srcBuffer, "  {");
    writeLine(this.srcBuffer, "    super(mdAttributeIF, attributeNamespace, definingTableName, definingTableAlias, mdStructIF, structTableAlias, rootQuery, tableJoinSet, alias, displayLabel);");
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
    writeLine(this.srcBuffer, "  public " + OIterator.class.getName() + "<? extends " + this.getMdClassIF().getTypeName() + ">" + " " + EntityQueryAPIGenerator.ITERATOR_METHOD + "" + "()");
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
    writeLine(this.srcBuffer, "    return new " + StructIterator.class.getName() + "<" + this.getMdClassIF().getTypeName() + ">(this.getComponentQuery().getMdEntityIF(), columnInfoMap, results);");
    writeLine(this.srcBuffer, "  }");
    writeLine(this.srcBuffer, "");
  }

}
