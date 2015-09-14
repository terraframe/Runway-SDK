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
package com.runwaysdk.dataaccess.io.dataDefinition;

import java.util.Map;

import org.xml.sax.Attributes;

import com.runwaysdk.business.state.MdStateMachineDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.dataaccess.AttributeDoesNotExistException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;

public class StatePermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  protected class AttributePermissionHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
  {
    public AttributePermissionHandler(ImportManager manager)
    {
      super(manager);

      this.addHandler(XMLTags.OPERATION_TAG, new OperationHandler(manager));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.runwaysdk.dataaccess.io.dataDefinition.AbstractPermissionHandler.OperationHandler#onStartElement(java.lang.String, org.xml.sax.Attributes,
     * com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
     */
    @Override
    public void onStartElement(String localName, Attributes attributes, TagContext context)
    {
      String attributeName = attributes.getValue(XMLTags.PERMISSION_ATTRIBUTE_NAME);
      MdBusinessDAOIF mdBusiness = (MdBusinessDAOIF) context.getObject(MdTypeInfo.CLASS);
      StateMasterDAOIF stateMaster = (StateMasterDAOIF) context.getObject("stateMaster");
      MdAttributeDAOIF mdAttribute = this.getMdAttribute(mdBusiness, attributeName);

      TypeTupleDAOIF typeTuple = TypeTupleDAO.findTuple(mdAttribute.getId(), stateMaster.getId());

      if (typeTuple == null)
      {
        TypeTupleDAO newTuple = TypeTupleDAO.newInstance();
        newTuple.setValue(TypeTupleDAOIF.METADATA, mdAttribute.getId());
        newTuple.setValue(TypeTupleDAOIF.STATE_MASTER, stateMaster.getId());
        newTuple.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdAttribute.definesAttribute() + "-" + stateMaster.getName() + " tuple");
        newTuple.apply();

        typeTuple = newTuple;
      }

      context.setObject(MetadataInfo.CLASS, typeTuple);
    }

    /**
     * Returns the MdAttribute that defines the attribute with the given names.
     * 
     * @param attributeName
     * @return MdAttribute that defines the attribute with the given names.
     */
    private MdAttributeDAOIF getMdAttribute(MdClassDAOIF mdClass, String attributeName)
    {
      Map<String, ? extends MdAttributeDAOIF> mdAttributeMap = mdClass.getAllDefinedMdAttributeMap();

      MdAttributeDAOIF mdAttributeIF = mdAttributeMap.get(attributeName.toLowerCase());
      if (mdAttributeIF == null)
      {
        String errMsg = "Attribute [" + attributeName + "] is not defined by class [" + mdClass.definesType() + "]";
        throw new AttributeDoesNotExistException(errMsg, attributeName, mdClass);
      }

      return mdAttributeIF;
    }
  }

  public StatePermissionHandler(ImportManager manager)
  {
    super(manager);

    this.addHandler(XMLTags.OPERATION_TAG, new OperationHandler(manager));
    this.addHandler(XMLTags.ATTRIBUTE_PERMISSION_TAG, new AttributePermissionHandler(manager));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandler#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    String stateName = attributes.getValue(XMLTags.STATE_NAME_ATTRIBUTE);
    MdBusinessDAOIF mdBusiness = (MdBusinessDAOIF) context.getObject(MdTypeInfo.CLASS);

    MdStateMachineDAOIF mdStateMachine = mdBusiness.definesMdStateMachine();

    StateMasterDAOIF stateMaster = mdStateMachine.definesStateMaster(stateName);

    context.setObject("stateMaster", stateMaster);
    context.setObject(MetadataInfo.CLASS, stateMaster);
  }
}
