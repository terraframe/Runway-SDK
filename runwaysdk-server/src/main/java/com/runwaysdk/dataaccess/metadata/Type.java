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
package com.runwaysdk.dataaccess.metadata;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.ComponentQueryDTO;
import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.business.ontology.Term;
import com.runwaysdk.business.ontology.TermAndRel;
import com.runwaysdk.constants.FormObjectInfo;
import com.runwaysdk.constants.MdActionInfo;
import com.runwaysdk.constants.ValueQueryDTOInfo;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.query.ValueQuery;

/**
 * Encapsulates type data for MdMethods and MdParameters.
 * 
 * @author Justin Smethie
 * 
 */
public class Type
{
  /**
   * A list of all the acceptable primitives
   */
  private static final Set<String> primitives = getPrimitiveList();

  private final String             type;

  public Type(String type)
  {
    this.type = type;
  }

  public Type(String base, int dimension)
  {
    StringBuffer buffer = new StringBuffer(base);

    for (int i = 0; i < dimension; i++)
    {
      buffer.append("[]");
    }

    this.type = buffer.toString();
  }

  /**
   * Returns the type name
   * 
   * @param type
   */
  public String getType()
  {
    return type;
  }

  /**
   * Returns the DTO represenation of the type name
   * 
   * @param type
   */
  public String getDTOType()
  {
    String baseType = type.replaceAll("\\[\\]", "");

    if (isPrimitive() || isVoid() || isStream() || isFormObject() || isMultipartFileParameter())
    {
      return type;
    }

    if (isValueQuery())
    {
      // TODO fix this hack
      return ValueQueryDTOInfo.CLASS;
    }

    return type.replace(baseType, baseType + ComponentDTOGenerator.DTO_SUFFIX);
  }

  /**
   * Returns the DTO Generic representation of the type name
   * 
   * @return
   */
  public String getGenericDTOType()
  {
    String baseType = type.replaceAll("\\[\\]", "");

    if (isPrimitive() || isVoid() || isStream())
    {
      return type;
    }

    if (this.isValueQuery())
    {
      return this.getDTOType();
    }

    if (isQuery())
    {
      return ComponentQueryDTO.class.getName();
    }

    // if the type is a MdEnumeration return EnumDTO otherwise
    // return EntityDTO
    if (MdTypeDAO.getMdTypeDAO(baseType) instanceof MdEnumerationDAOIF)
    {
      return type.replace(baseType, EnumDTO.class.getName());
    }

    return type.replace(baseType, ComponentDTO.class.getName());
  }

  /**
   * If the type is an array it returns the base component of the type name.
   * Otherwise getBaseType() is equivalent to getType().
   * 
   * @return
   */
  public String getRootType()
  {
    // ValueQuery has no root type (this is to avoid removing "Query")
    if (type.equals(ValueQuery.class.getName()))
    {
      return type;
    }

    // Remove query suffix and array brackets
    int index = type.indexOf(EntityQueryAPIGenerator.QUERY_API_SUFFIX);

    if (index > 0 && ( index == type.length() - EntityQueryAPIGenerator.QUERY_API_SUFFIX.length() ))
    {
      return type.substring(0, index).replaceAll("\\[\\]", "");
    }

    return type.replaceAll("\\[\\]", "");
  }

  private String getDTOBaseType()
  {
    String dto = getDTOType();
    return dto.replaceAll("\\[\\]", "");
  }

  private String getGenericDTOBaseType()
  {
    String dto = getGenericDTOType();
    return dto.replaceAll("\\[\\]", "");
  }

  /**
   * Returns the equivalent of ClassLoader(type).getName().
   * 
   * @return
   */
  public String getJavaClass()
  {
    String javaClass = type;
    boolean first = true;

    while (javaClass.contains("[]"))
    {
      javaClass = javaClass.replaceFirst("\\[\\]", "");

      if (first)
      {
        javaClass = "[L" + javaClass + ";";
        first = false;
      }
      else
      {
        javaClass = "[" + javaClass;
      }
    }

    return javaClass;
  }

  /**
   * Returns the equivalent of ClassLoader(dtoType).getName().
   * 
   * @return
   */
  public String getDTOJavaClass()
  {
    String javaClass = getDTOType();
    boolean first = true;

    while (javaClass.contains("[]"))
    {
      javaClass = javaClass.replaceFirst("\\[\\]", "");

      if (first)
      {
        javaClass = "[L" + javaClass + ";";
        first = false;
      }
      else
      {
        javaClass = "[" + javaClass;
      }
    }

    return javaClass;
  }

  public String getGenericDTOJavaClass()
  {
    String generic = getGenericDTOBaseType();
    String dto = getDTOBaseType();
    String javaClass = getDTOJavaClass();

    return javaClass.replace(dto, generic);
  }

  public boolean isTermAndRel()
  {
    String baseType = getRootType();

    return baseType.equals(TermAndRel.CLASS);
  }
  
  public boolean isTerm()
  {
    String baseType = getRootType();
    
    return baseType.equals(Term.CLASS);
  }
  
  public boolean isValueQuery()
  {
    String baseType = getRootType();

    return baseType.equals(ValueQuery.class.getName());
  }

  /**
   * Returns if the base type is a Primitive
   * 
   * @return
   */
  public boolean isPrimitive()
  {
    String baseType = getRootType();

    if (primitives.contains(baseType))
    {
      return true;
    }

    return false;
  }

  /**
   * Returns if the type is void
   * 
   * @return
   */
  public boolean isVoid()
  {
    return type.equals("void");
  }

  public boolean isDefinedType()
  {
    return ! ( this.isPrimitive() || this.isValueQuery() || this.isStream() || this.isVoid() || this.isFormObject() || this.isMultipartFileParameter() );
  }

  public boolean isQuery()
  {
    int index = type.indexOf(EntityQueryAPIGenerator.QUERY_API_SUFFIX);

    return ( index > 0 && ( index == type.length() - EntityQueryAPIGenerator.QUERY_API_SUFFIX.length() ) );
  }

  /**
   * Returns if the type is an array
   * 
   * @return
   */
  public boolean isArray()
  {
    return type.contains("[]");
  }

  /**
   * Generate the list of primitive object names defined in java and their
   * corresponding java class names.
   * 
   * @return
   */
  private static Set<String> getPrimitiveList()
  {
    Set<String> tree = new TreeSet<String>();

    tree.add(Integer.class.getName());

    tree.add(Boolean.class.getName());

    tree.add(Long.class.getName());

    tree.add(Short.class.getName());

    tree.add(Byte.class.getName());

    tree.add(Float.class.getName());

    tree.add(Double.class.getName());

    tree.add(Character.class.getName());

    tree.add(String.class.getName());

    tree.add(Date.class.getName());

    return tree;
  }

  public int getDimensions()
  {
    String base = this.getType();
    int count = 0;

    while (base.contains("[]"))
    {
      base = base.replaceFirst("\\[\\]", "");
      count++;
    }

    return count;
  }

  public boolean isStream()
  {
    return ( type.equals(InputStream.class.getName()) || type.equals(OutputStream.class.getName()) );
  }

  public boolean isFormObject()
  {
    return type.equals(FormObjectInfo.CLASS);
  }

  public boolean isMultipartFileParameter()
  {
    return type.equals(MdActionInfo.MULTIPART_FILE_PARAMETER);
  }
}
