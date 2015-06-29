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
package com.runwaysdk.transport.conversion.business;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeBooleanDAOIF;
import com.runwaysdk.dataaccess.MdAttributeCharacterDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.MdViewDAOIF;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.session.PermissionFacade;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionFacade;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDTOFacade;
import com.runwaysdk.transport.attributes.AttributeDTOFactory;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.conversion.ConversionException;
import com.runwaysdk.transport.metadata.AttributeBooleanMdDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;
import com.runwaysdk.transport.metadata.AttributeReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.transport.metadata.ServerAttributeFacade;
import com.runwaysdk.transport.metadata.TypeMd;

public abstract class ClassToQueryDTO
{
  /**
   * The session id requesting the QueryDTO
   */
  private String   sessionId;

  /**
   * The MdTypeIF object that will populate the QueryDTO
   */
  private MdClassDAOIF mdClassIF;

  /**
   * The QueryDTO to be populated.
   */
  private ClassQueryDTO queryDTO;

  /**
   *
   * @param sessionId
   * @param mdClassIF
   * @param queryDTO
   */
  public ClassToQueryDTO(String sessionId, MdClassDAOIF mdClassIF, ClassQueryDTO queryDTO)
  {
    this.sessionId = sessionId;
    this.mdClassIF = mdClassIF;
    this.queryDTO = queryDTO;
  }

  /**
   * Returns the source mdClassIF
   *
   * @return
   */
  protected MdClassDAOIF getMdClassIF()
  {
    return mdClassIF;
  }

  /**
   * Returns the ClassQueryDTO that is being constructed.
   * @return
   */
  protected ClassQueryDTO getClassQueryDTO()
  {
    return queryDTO;
  }

  /**
   * Returns the session id that is requesting the conversion.
   *
   * @return
   */
  protected String getSessionId()
  {
    return sessionId;
  }

  public ClassQueryDTO populate()
  {
    loadMdClassMdAttributes();

    loadInheritance();

    return getClassQueryDTO();
  }

  /**
   * Loads the inheritance tree defined by an MdEntityIF
   *
   * @param mdEntityIF
   */
  private void loadInheritance()
  {
    ClassQueryDTO queryDTO =  getClassQueryDTO();
    MdClassDAOIF mdClassIF = getMdClassIF();

    List<? extends MdClassDAOIF> subclasses = mdClassIF.getAllSubClasses();

    // load subclasses
    for(MdClassDAOIF subClass : subclasses)
    {
      String subclassType = subClass.definesType();
      List<? extends MdClassDAOIF> superClasses = subClass.getSuperClasses();
      List<String> superClassTypes = new LinkedList<String>();
      if(superClasses != null)
      {
        for(MdClassDAOIF superClass : superClasses)
        {
          superClassTypes.add(superClass.definesType());
        }
      }

      queryDTO.addClassType(subclassType, superClassTypes);
    }
  }


  /**
   * Loads MdAttribute information defined by an MdEntityIF
   *
   * @param mdEntityIF
   * @param queryDTO
   */
  private void loadMdClassMdAttributes()
  {
    MdClassDAOIF mdClassIF = getMdClassIF();
    ClassQueryDTO queryDTO = getClassQueryDTO();

    List<? extends MdAttributeDAOIF> mdAttributes = mdClassIF.getAllDefinedMdAttributes();
    for (MdAttributeDAOIF mdAttribute : mdAttributes)
    {
      if (PermissionFacade.checkAttributeReadAccess(getSessionId(), mdAttribute) &&
          mdAttribute.getGetterVisibility() == VisibilityModifier.PUBLIC)
      {
        ComponentDTOFacade.addAttributeQueryDTO(queryDTO, loadMdAttribute(getSessionId(), mdAttribute));
      }
    }
  }

  /**
   * Loads MdAttribute information for an MdStruct.
   *
   * @param mdStruct
   * @param structDTO
   */
  private static Map<String, AttributeDTO> loadMdStructMdAttributes(String sessionId, MdStructDAOIF mdStruct)
  {
    Map<String, AttributeDTO> attributeDTOmap = new HashMap<String, AttributeDTO>();

    List<? extends MdAttributeConcreteDAOIF> mdAttributes = mdStruct.getAllDefinedMdAttributes();
    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
    {
      if (PermissionFacade.checkAttributeReadAccess(sessionId, mdAttribute))
      {
        AttributeDTO attributeDTO = loadMdAttribute(sessionId, mdAttribute);
        attributeDTOmap.put(attributeDTO.getName(), attributeDTO);
      }
    }

    return attributeDTOmap;
  }

  /**
   * Loads the attributes
   */
  private static AttributeDTO loadMdAttribute(String sessionId, MdAttributeDAOIF mdAttributeIF)
  {
    // sets the
    String name = mdAttributeIF.definesAttribute();
    String defaultValue = mdAttributeIF.getAttributeInstanceDefaultValue();
    boolean writable = SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttributeIF);
    AttributeDTO attributeDTO = AttributeDTOFactory.createAttributeDTO(name, mdAttributeIF.getMdAttributeConcrete().getType(), defaultValue,
        true, writable, false);

    // set the basic metadata on the attribute
    AttributeMdDTO mdDTO = attributeDTO.getAttributeMdDTO();

    MdAttributeConcreteDAOIF mdAttributeConcreteIF = mdAttributeIF.getMdAttributeConcrete();

    if (mdAttributeConcreteIF instanceof MdAttributeEnumerationDAOIF)
    {
      // load all enumeration items
      MdAttributeEnumerationDAOIF mdAttributeEnumeration = (MdAttributeEnumerationDAOIF) mdAttributeConcreteIF;
      for (BusinessDAOIF item : mdAttributeEnumeration.getMdEnumerationDAO().getAllEnumItems())
      {
        AttributeEnumerationDTO attributeEnumerationDTO = (AttributeEnumerationDTO) attributeDTO;

        String enumName = item.getAttributeIF(EnumerationMasterInfo.NAME).getValue();
        AttributeDTOFacade.addEnumItemInternal(attributeEnumerationDTO, enumName);
      }

      ServerAttributeFacade.setEnumerationMetadata(mdAttributeEnumeration, (AttributeEnumerationMdDTO) mdDTO);
    }
    else if (mdAttributeConcreteIF instanceof MdAttributeStructDAOIF)
    {
      MdAttributeStructDAOIF mdAttributeStructIF = (MdAttributeStructDAOIF) mdAttributeConcreteIF;
      AttributeStructDTO attributeStructDTO = (AttributeStructDTO) attributeDTO;

      ServerAttributeFacade.setStructMetadata(mdAttributeIF,(AttributeStructMdDTO) mdDTO);

      // load the MdAttribute information tha the struct defines
      Map<String, AttributeDTO> attributeDTOmap = loadMdStructMdAttributes(sessionId, mdAttributeStructIF.getMdStructDAOIF());

      String structType = mdAttributeStructIF.getMdStructDAOIF().definesType();

      TypeMd typeMd = new TypeMd(mdAttributeStructIF.getDisplayLabel(Session.getCurrentLocale()), mdAttributeStructIF.getDescription(Session.getCurrentLocale()), mdAttributeStructIF.getId());

      StructDTO structDTO = ComponentDTOFacade.buildStructDTO(null, structType, attributeDTOmap, typeMd);

      attributeStructDTO.setStructDTO(structDTO);

    }
    else if(mdAttributeConcreteIF instanceof MdAttributeBooleanDAOIF)
    {
      // change the 1 or 0 value into a true/false
      if(attributeDTO.getValue().equals(MdAttributeBooleanDAOIF.DB_TRUE))
      {
        attributeDTO.setValue(MdAttributeBooleanInfo.TRUE);
      }
      else
      {
        attributeDTO.setValue(MdAttributeBooleanInfo.FALSE);
      }
      ServerAttributeFacade.setBooleanMetadata(mdAttributeIF,(AttributeBooleanMdDTO) mdDTO);
    }
    else if (mdAttributeConcreteIF instanceof MdAttributeEncryptionDAOIF)
    {
      ServerAttributeFacade.setEncryptionMetadata(mdAttributeIF, (AttributeEncryptionMdDTO) mdDTO);
    }
    else if (mdAttributeConcreteIF instanceof MdAttributeDecDAOIF)
    {
      ServerAttributeFacade.setDecMetadata(mdAttributeIF, (AttributeDecMdDTO) mdDTO);
    }
    else if (mdAttributeConcreteIF instanceof MdAttributeNumberDAOIF)
    {
      ServerAttributeFacade.setNumberMetadata(mdAttributeIF,(AttributeNumberMdDTO) mdDTO);
    }
    else if (mdAttributeConcreteIF instanceof MdAttributeCharacterDAOIF)
    {
      ServerAttributeFacade.setCharacterMetadata(mdAttributeIF, (AttributeCharacterMdDTO) mdDTO);
    }
    else if (mdAttributeConcreteIF instanceof MdAttributeReferenceDAOIF)
    {
      ServerAttributeFacade.setReferenceMetadata(mdAttributeIF, (AttributeReferenceMdDTO) mdDTO);
    }
    else
    {
      ServerAttributeFacade.setAttributeMetadata(mdAttributeIF, mdDTO);
    }

    return attributeDTO;
  }

  /**
   * Given an MdClassToQueryDTO, this method populates the query dto with all information
   * needed to be pushed to the client.
   *
   * @param sessionId
   * @param mdType
   * @param ClassToQueryDTO
   * @return
   */
  public static ClassToQueryDTO getConverter(String sessionId, ClassQueryDTO queryDTO)
  {
    MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(queryDTO.getType());

    if(mdClass instanceof MdBusinessDAOIF)
    {
      return new MdBusinessToQueryDTO(sessionId,(MdBusinessDAOIF) mdClass, (BusinessQueryDTO) queryDTO);
    }
    else if(mdClass instanceof MdRelationshipDAOIF)
    {
      return new MdRelationshipToQueryDTO(sessionId,(MdRelationshipDAOIF) mdClass, (RelationshipQueryDTO) queryDTO);
    }
    else if(mdClass instanceof MdStructDAOIF)
    {
      return new MdStructToQueryDTO(sessionId,(MdStructDAOIF) mdClass, (StructQueryDTO) queryDTO);
    }
    else if(mdClass instanceof MdViewDAOIF)
    {
      return new MdViewToQueryDTO(sessionId,(MdViewDAOIF) mdClass, (ViewQueryDTO) queryDTO);
    }
    else
    {
      String error = "The type ["+mdClass.definesType()+"] cannot be queried.";
      throw new ConversionException(error);
    }
  }

  /**
   * Returns the proper subclass of MdClassToQueryDTO to convert the given MdClassIF
   * into the appropriate ClassQueryDTO.
   *
   * @param mdType
   * @return
   */
  public static ClassToQueryDTO getConverter(String sessionId, MdClassDAOIF mdClassIF)
  {
    if(mdClassIF instanceof MdBusinessDAOIF)
    {
      BusinessQueryDTO queryDTO = ComponentDTOFacade.buildBusinessQueryDTO(mdClassIF.definesType());
      return new MdBusinessToQueryDTO(sessionId,(MdBusinessDAOIF) mdClassIF, queryDTO);
    }
    else if(mdClassIF instanceof MdRelationshipDAOIF)
    {
      RelationshipQueryDTO queryDTO = ComponentDTOFacade.buildRelationshipQueryDTO(mdClassIF.definesType());
      return new MdRelationshipToQueryDTO(sessionId,(MdRelationshipDAOIF) mdClassIF, queryDTO);
    }
    else if(mdClassIF instanceof MdStructDAOIF)
    {
      StructQueryDTO queryDTO = ComponentDTOFacade.buildStructQueryDTO(mdClassIF.definesType());
      return new MdStructToQueryDTO(sessionId,(MdStructDAOIF) mdClassIF, queryDTO);
    }
    else if (mdClassIF instanceof MdViewDAOIF)
    {
      ViewQueryDTO queryDTO = ComponentDTOFacade.buildViewQueryDTO(mdClassIF.definesType());
      return new MdViewToQueryDTO(sessionId,(MdViewDAOIF) mdClassIF, queryDTO);
    }
    else
    {
      String error = "The type ["+mdClassIF.definesType()+"] cannot be queried.";
      throw new ConversionException(error);
    }
  }
}
