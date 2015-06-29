/**
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
 */
package com.runwaysdk.business.generation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.ComponentInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.RelationshipInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.generation.CommonGenerationUtil;

/**
 * @author jsmethie
 *
 */
public class GenerationUtil
{
  /**
   * Returns a comma seperated list of the parameter type name pairings
   * of the format: ,Type1 name1, Type2 name2, etc.
   *
   * @param map A LinkedHashMap (Ordered) with the Type as the key and name is the value
   * @param isStart A flag denoting if there should be a comma in front of the first element
   * @return
   */
  public static String buildBusinessParameters(List<MdParameterDAOIF> list, boolean isStart)
  {
    String parameters = new String();

    for(MdParameterDAOIF mdParameter : list)
    {
      String type = mdParameter.getParameterType().getType();
      String name = mdParameter.getParameterName();

      parameters = parameters.concat(", " + type + " " + name);
    }

    if(isStart)
    {
      parameters = parameters.replaceFirst(", ", "");
    }

    return parameters;
  }

  /**
   * Returns a comma seperated list of parameters for methods using JSON/Javascript.
   *
   * @param list
   * @return
   */
  public static String buildJSONParameters(List<MdParameterDAOIF> list)
  {
    // build the parameters list
    String parameters = "clientRequest ";
    for(MdParameterDAOIF mdParameterIF : list)
    {
      parameters += mdParameterIF.getParameterName() + " ";
    }
    parameters = parameters.trim();
    parameters = parameters.replaceAll(" ", ", ");

    return parameters;
  }

  /**
   * Returns a comma seperated list of the parameter type name pairings
   * of the format: Type1DTO name1, Type2DTO name2, etc.
   *
   * @param list A LinkedHashMap (Ordered) with the Type as the key and name is the value
   * @param isStart Flag determining if a comma should be place before the first parameter or not.
   * @return
   */
  public static String buildDTOParameters(List<MdParameterDAOIF> list, boolean isStart)
  {
    StringBuffer parameters = new StringBuffer();

    for(MdParameterDAOIF mdParameter : list)
    {
      String type = mdParameter.getParameterType().getDTOType();
      parameters.append(", " + type + " " + mdParameter.getParameterName());
    }

    if(isStart)
    {
      return parameters.toString().replaceFirst(", ", "");
    }

    return parameters.toString();
  }

  /**
   * Returns a comma seperated list of the parameter type name pairings
   * of the format: EntityDTO name1, EntityDTO name2, etc.
   *
   * @param list A LinkedHashMap (Ordered) with the Type as the key and name is the value
   * @param isStart boolean
   * @return
   */
  public static String buildGenericDTOParameters(List<MdParameterDAOIF> list, boolean isStart)
  {
    String parameters = new String();

    for(MdParameterDAOIF mdParameter : list)
    {
      String type = mdParameter.getParameterType().getGenericDTOType();
      String name = mdParameter.getParameterName();

      parameters = parameters.concat(", " + type + " " + name);
    }

    if(isStart)
    {
      parameters = parameters.replaceFirst(", ", "");
    }

    return parameters;
  }

  public static String buildDocumentParameters(List<MdParameterDAOIF> list, boolean isStart)
  {
    String parameters = new String();

    for(MdParameterDAOIF mdParameter : list)
    {
      String name = mdParameter.getParameterName();
      parameters = parameters.concat(", " + Document.class.getName() + " " + name);
    }

    if(isStart)
    {
      parameters = parameters.replaceFirst(", ", "");
    }

    return parameters;
  }

  public static String buildJSONParameters(List<MdParameterDAOIF> list, boolean isStart)
  {
    String parameters = new String();

    for(MdParameterDAOIF mdParameter : list)
    {
      String name = mdParameter.getParameterName();
      parameters = parameters.concat(", " + String.class.getName() + " " + name);
    }

    if(isStart)
    {
      parameters = parameters.replaceFirst(", ", "");
    }

    return parameters;
  }

  /**
   * Returns a comma seperated list of the method java Class names,
   * aka class.getName().
   * of the format: className1, className2, etc.
   *
   * @param list A LinkedHashMap (Ordered) with the Type as the key and name is the value
   * @return
   */
  public static String buildMethodArray(List<MdParameterDAOIF> list)
  {
    String methodTypes = new String();

    for(MdParameterDAOIF mdParameter : list)
    {
      String methodType = mdParameter.getParameterType().getJavaClass();
      methodTypes = methodTypes.concat(", \"" + methodType + "\"");
    }

    methodTypes = methodTypes.replaceFirst(", ", "");

    return methodTypes;
  }

  public static String[] getDeclaredTypes(String methodId)
  {
    MdMethodDAOIF methodDAO = MdMethodDAO.get(methodId);

    List<String> declaredTypes = new LinkedList<String>();

    for(MdParameterDAOIF param : methodDAO.getMdParameterDAOs())
    {
      declaredTypes.add(param.getParameterType().getJavaClass());
    }

    return declaredTypes.toArray(new String[declaredTypes.size()]);
  }

  /**
   * Returns a comma seperated list of the parameter names
   * of the format: name1, name2, etc.
   *
   * @param list A LinkedHashMap (Ordered) with the Type as the key and name is the value
   * @return
   */
  public static String buildParameterArray(List<MdParameterDAOIF> list)
  {
    String parameter = new String();

    for(MdParameterDAOIF mdParameter : list)
    {
      String name = mdParameter.getParameterName();
      parameter = parameter.concat(", " + name);
    }

    parameter = parameter.replaceFirst(", ", "");

    return parameter;
  }

  public static String buildCallParameter(List<MdParameterDAOIF> list, String prefix, boolean isStart)
  {
    String callString = new String();

    for(MdParameterDAOIF mdParameter : list)
    {
      String name = mdParameter.getParameterName();
      callString = callString.concat(", " + prefix + name);
    }

    if(isStart)
    {
      callString = callString.replaceFirst(", ", "");
    }

    return callString;
  }

  /**
   * Write a string to file.
   *
   * @param writer The file writer
   * @param s The String to write
   */
  public static void write(BufferedWriter writer, String s)
  {
    try
    {
      writer.write(s);
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Writes a String to a file followed by a newline.
   *
   * @param writer The file writer
   * @param s The String to write
   */
  public static void writeLine(BufferedWriter writer, String s)
  {
    try
    {
      GenerationUtil.write(writer, s);
      writer.newLine();
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  /**
   * Returns a string of the package of the type as it would appear on the file system.
   *   It basically replaces all dots with slashes.
   * @param mdTypeIF
   * @return string of the package of the type as it would appear on the file system.
   */
  public static String getPackageForFileSystem(MdTypeDAOIF mdTypeIF)
  {
    return CommonGenerationUtil.replacePackageDotsWithSlashes(mdTypeIF.getPackage());
  }

  public static boolean isStatus(MdBusinessDAOIF mdBusinessIF, MdRelationshipDAOIF relationship)
  {
    if (!mdBusinessIF.hasStateMachine()) return false;

    String master_status = mdBusinessIF.definesMdStateMachine().getMdStatus().definesType();

    for (MdElementDAOIF dad : relationship.getSuperClasses())
      if (dad.definesType().equalsIgnoreCase(master_status))
        return true;

    return false;
  }

  public static boolean isStateMachine(MdRelationshipDAOIF relationship)
  {
    if (relationship.definesType().startsWith(MdStateMachineDAO.STATE_PACKAGE + '.'))
      return true;

    return false;
  }

  /**
   * Some types have hard coded class representations or are not generated at all. These
   * types need to be skipped during generation and compilation - this method provides a
   * central test to find if any type is reserved.
   *
   * @param mdTypeIF
   *          The possibly reserved type
   * @return <code>true</code> is the type is reserved - <code>false</code> otherwise
   */
  public static boolean isReservedAndHardcoded(MdTypeDAOIF mdTypeIF)
  {
    boolean isReservedType = false;

    if (isReservedType(mdTypeIF))
    {
      return true;
    }

    if (isHardcodedType(mdTypeIF))
    {
      return true;
    }

    return isReservedType;
  }

  /**
   * Some types have hard coded class representations or are not generated at all. These
   * types need to be skipped during generation and compilation - this method provides a
   * central test to find if any type is reserved.
   *
   * @param mdTypeIF
   *          The possibly reserved type
   * @return <code>true</code> is the type is reserved - <code>false</code> otherwise
   */
  public static boolean isReservedType(MdTypeDAOIF mdTypeIF)
  {
    boolean isReservedType = false;

    if (mdTypeIF instanceof MdElementDAOIF)
    {
      MdElementDAOIF mdEntityIF = ((MdElementDAOIF)mdTypeIF);
      MdElementDAOIF superEntity = mdEntityIF.getSuperClass();
      if (superEntity!=null)
      {
       if (superEntity.definesType().equalsIgnoreCase(EntityTypes.STATE_MASTER.getType()) ||
           superEntity.definesType().equalsIgnoreCase(RelationshipTypes.TRANSITION_RELATIONSHIP.getType()) ||
            superEntity.definesType().toLowerCase().startsWith(MdStateMachineDAO.STATE_PACKAGE + '.'))
       {
         isReservedType = true;
        }
      }
    }
    if (mdTypeIF instanceof MdRelationshipDAOIF)
    {
      if (mdTypeIF.definesType().startsWith(MdStateMachineDAO.STATE_PACKAGE+"."))
      {
        isReservedType = true;
      }
    }
    return isReservedType;
  }

  /**
   * Some types have hard coded class representations that are in the common directory at the DTO layer.
   * Returns true if the given type is one of the hardcoded types that reside in the common directory at
   * the DTO layer, false otherwise.
   *
   * @param mdTypeIF
   *          The possibly reserved type
   * @return <code>true</code> is the type is reserved - <code>false</code> otherwise
   */
  public static boolean isHardcodedType(MdTypeDAOIF mdTypeIF)
  {
    boolean isReservedType = false;

    if (mdTypeIF.definesType().equalsIgnoreCase(ComponentInfo.CLASS) ||
        mdTypeIF.definesType().equalsIgnoreCase(EntityInfo.CLASS) ||
        mdTypeIF.definesType().equalsIgnoreCase(ElementInfo.CLASS) ||
        mdTypeIF.definesType().equalsIgnoreCase(BusinessInfo.CLASS) ||
        mdTypeIF.definesType().equalsIgnoreCase(RelationshipInfo.CLASS))
    {
      isReservedType = true;
    }

    return isReservedType;
  }

  /**
   * Checks to see if the attribute is a special case that requires special handling for
   * generation.
   *
   * @param m
   *          Attribute to check as a special case
   * @return <code>true</code> if the attribute is a special case
   */
  public static boolean isSpecialCaseSetter(MdAttributeDAOIF m)
  {
    MdClassDAOIF mdClassDAOIF = m.definedByClass();

    if (m.isSystem() && !isHardcodedType(mdClassDAOIF))
    {
      return true;
    }

    return isSpecialCaseBasic(m);
  }

  /**
   * A basic check for special MdAttributeIFs
   *
   * @param m
   * @return
   */
  private static boolean isSpecialCaseBasic(MdAttributeDAOIF m)
  {
    MdAttributeConcreteDAOIF mdAttributeConcrete = m.getMdAttributeConcrete();

    if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
    {
      MdAttributeReferenceDAOIF ref = (MdAttributeReferenceDAOIF)mdAttributeConcrete;
      MdBusinessDAOIF refMdBusiness = ref.getReferenceMdBusinessDAO();
      if(refMdBusiness != null)
      {
        for (MdElementDAOIF superEntity : refMdBusiness.getSuperClasses())
        {
          String definesType = superEntity.definesType();
          if (definesType.equalsIgnoreCase(EnumerationMasterInfo.CLASS) ||
              definesType.equalsIgnoreCase(EntityTypes.STATE_MASTER.getType()) ||
              definesType.equalsIgnoreCase(RelationshipTypes.TRANSITION_RELATIONSHIP.getType()) ||
              definesType.toLowerCase().startsWith(MdStateMachineDAO.STATE_PACKAGE + '.'))
            return true;
        }
      }
    }

    return false;
  }

  /**
   * Checks to see if the attribute is a special case that requires special handling for
   * generation. This method applies to DTOs only.
   *
   * @param m
   *          Attribute to check as a special case
   * @return <code>true</code> if the attribute is a special case
   */
  public static boolean isDTOSpecialCase(MdAttributeDAOIF m)
  {
    // don't create accessors for id or type because
    // we don't want to override the native getId()/getType()
    // in EntityDTO
    if(m.definesAttribute().equals(EntityInfo.ID) ||
        m.definesAttribute().equals(EntityInfo.TYPE))
    {
      return true;
    }

    return isSpecialCaseBasic(m);
  }

  /**
   * Checks to see if the attribute is a special case that requires special handling for
   * generation. This method applies to DTOs only.
   *
   * @param m
   *          Attribute to check as a special case
   * @return <code>true</code> if the attribute is a special case
   */
  public static boolean isDTOSpecialCaseSetter(MdAttributeDAOIF m)
  {
    MdClassDAOIF mdClassDAOIF = m.definedByClass();

    if (!m.getSetterVisibility().equals(VisibilityModifier.PUBLIC) ||
        (m.isSystem() && !isHardcodedType(mdClassDAOIF)))
    {
      return true;
    }

    return isSpecialCaseBasic(m);
  }

  public static MdParameterDAO getMdParameterId()
  {
    MdParameterDAO id = MdParameterDAO.newInstance();
    id.setValue(MdParameterInfo.TYPE, String.class.getName());
    id.setValue(MdParameterInfo.NAME, "id");
    id.setValue(MdParameterInfo.ORDER, "-99999999");

    return id;
  }

  /**
   * Returns the method signature modifier according to the isStatic and isFinal flags.
   *
   * @param isStatic Flag denoting that a method signature is static
   * @param isFinal Flag denoting that a method signature is final
   * @return
   */
  public static String getModifier(boolean isStatic, boolean isFinal)
  {
    String modifier = "";

    if(isStatic)
    {
      modifier = modifier.concat("static ");
    }

    if(isFinal)
    {
      modifier = modifier.concat("final ");
    }

    return modifier;
  }

  /**
   * Returns true if the given type should be generated, false otherwise.
   * The criteria is that we should not regenerate system classes if we are not tin the runway environment.
   *
   * @param mdTypeDAOIF
   * @return true if the given type should be generated, false otherwise.
   */
  public static boolean shouldGenerate(MdTypeDAOIF mdTypeDAOIF)
  {
    if (mdTypeDAOIF.isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return false;
    }
    else
    {
      return true;
    }
  }

}
