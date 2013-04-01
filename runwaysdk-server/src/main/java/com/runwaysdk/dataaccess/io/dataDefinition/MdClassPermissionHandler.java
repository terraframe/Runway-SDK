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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.runwaysdk.business.rbac.ActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.XMLHandler;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;

public class MdClassPermissionHandler extends AbstractPermissionHandler
{
  /**
   * The current Metadata definition on which permissions are being set
   */
  private MetadataDAOIF                          metadata;

  /**
   * Root mdEntity on which the permissions are being added
   */
  private MdClassDAOIF                           mdClass;

  /**
   * Map of all attributes defined by or inherited by the class. Key: attribute
   * name Value: MdAttributeIF
   */
  public Map<String, ? extends MdAttributeDAOIF> mdAttributeMap;

  /**
   * Name of the tag being parsed
   */
  private String                                 tag;

  /**
   * Constructor - Creates a MdBusiness BusinessDAO and sets the parameters
   * according to the attributes parse
   * 
   * @param attributes
   *          The attibutes of the class tag
   * @param reader
   *          The XMLReader stream
   * @param previousHandler
   *          The Handler which passed control
   * @param manager
   *          ImportManager which provides communication between handlers for a
   *          single import
   * @param actors
   *          The actor on which to import permissions
   * @param tag
   *          Name of the tag being parsed
   * @param action
   *          TODO
   */
  public MdClassPermissionHandler(Attributes attributes, XMLReader reader, XMLHandler previousHandler, ImportManager manager, List<ActorDAO> actors, String tag, PermissionAction action)
  {
    super(reader, previousHandler, manager, actors, action);

    this.tag = tag;

    String type = attributes.getValue(XMLTags.TYPE_ATTRIBUTE);

    this.mdClass = MdClassDAO.getMdClassDAO(type);
    this.mdAttributeMap = mdClass.getAllDefinedMdAttributeMap();

    this.mdAttributeMap = this.mdClass.getAllDefinedMdAttributeMap();

    // By default the permissions are going to be set on the root MdClass
    // However, metadata will change to an attribute defined by the root MdClass
    this.metadata = this.mdClass;
  }

  /**
   * Parses the attributes tag Inherited from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
   *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
   */
  public void startElement(String namespaceURI, String localName, String fullName, Attributes attributes) throws SAXException
  {
    if (localName.equals(XMLTags.OPERATION_TAG))
    {
      String operationName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);

      this.setPermission(operationName, mdClass, metadata);
    }
    else if (localName.equals(XMLTags.OPERATION_DIMENSION_TAG))
    {
      String operationName = attributes.getValue(XMLTags.NAME_ATTRIBUTE);
      String dimension = attributes.getValue(XMLTags.DIMENSION_ATTRIBUTE);

      this.setPermission(operationName, dimension);
    }
    else if (localName.equals(XMLTags.ATTRIBUTE_PERMISSION_TAG))
    {
      String attributeName = attributes.getValue(XMLTags.PERMISSION_ATTRIBUTE_NAME);
      String dimension = attributes.getValue(XMLTags.DIMENSION_ATTRIBUTE);
      
      MdAttributeDAOIF mdAttribute = this.getMdAttribute(attributeName);
      
      if(dimension != null && dimension.length() > 0)
      {
        MdAttributeDimensionDAOIF mdAttributeDimension = this.getMdAttributeDimension(mdAttribute, dimension);
        
        metadata = mdAttributeDimension;
      }
      else
      {
        metadata = mdAttribute;
      }
    }
    else if (localName.equals(XMLTags.MD_METHOD_PERMISSION_TAG))
    {
      String methodName = attributes.getValue(XMLTags.METHOD_PERMISSION_NAME_ATTRIBUTE);
      metadata = mdClass.getMdMethod(methodName);
    }
    // MdBusiness can set permissions according to the state of the type
    else if ( ( mdClass instanceof MdBusinessDAOIF ) && localName.equals(XMLTags.STATE_PERMISSION_TAG))
    {
      StatePermissionHandler handler = new StatePermissionHandler(attributes, reader, this, manager, actors, (MdBusinessDAOIF) mdClass, action);
      reader.setContentHandler(handler);
      reader.setErrorHandler(handler);
    }

  }

  private void setPermission(String operationName, String dimension)
  {
    if (operationName.equals(XMLTags.READ_ALL_ATTRIBUTES))
    {
      this.setDimensionPermission(Operation.READ, dimension);
    }
    else if (operationName.equals(XMLTags.WRITE_ALL_ATTRIBUTES))
    {
      this.setDimensionPermission(Operation.WRITE, dimension);
    }
  }

  private void setDimensionPermission(Operation operation, String dimension)
  {
    for (MdAttributeDAOIF mdAttribute : mdClass.definesAttributes())
    {
      if (dimension.equalsIgnoreCase("*"))
      {
        List<MdAttributeDimensionDAOIF> list = mdAttribute.getMdAttributeDimensions();

        for (MdAttributeDimensionDAOIF mdAttributeDimension : list)
        {
          this.setPermission(operation, mdAttributeDimension.getId());
        }
      }
      else
      {
        MdAttributeDimensionDAOIF mdAttributeDimension = getMdAttributeDimension(mdAttribute, dimension);
        
        this.setPermission(operation, mdAttributeDimension.getId());
      }
    }
  }

  private MdAttributeDimensionDAOIF getMdAttributeDimension(MdAttributeDAOIF mdAttribute, String dimension)
  {
    String key = MdDimensionDAO.buildKey(dimension);
    MdDimensionDAOIF mdDimension = MdDimensionDAO.get(MdDimensionInfo.CLASS, key);

    return mdAttribute.getMdAttributeDimension(mdDimension);
  }

  /**
   * When the class tag is closed: Returns parsing control back to the Handler
   * which passed control
   * 
   * Inherits from ContentHandler (non-Javadoc)
   * 
   * @see org.xml.sax.ContentHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String namespaceURI, String localName, String fullName)
  {
    if (localName.equals(tag))
    {
      reader.setContentHandler(previousHandler);
      reader.setErrorHandler(previousHandler);
    }
  }

  /**
   * Returns the MdAttribute that defines the attribute with the given names.
   * 
   * @param attributeName
   * @return MdAttribute that defines the attribute with the given names.
   */
  private MdAttributeDAOIF getMdAttribute(String attributeName)
  {
    MdAttributeDAOIF mdAttributeIF = mdAttributeMap.get(attributeName.toLowerCase());
    if (mdAttributeIF == null)
    {
      String errMsg = "Attribute [" + attributeName + "] is not defined by class [" + this.mdClass.definesType() + "]";
      throw new AttributeDoesNotExistException(errMsg, attributeName, this.mdClass);
    }
    return mdAttributeIF;
  }
}
