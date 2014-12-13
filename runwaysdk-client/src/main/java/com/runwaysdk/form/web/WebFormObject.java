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
package com.runwaysdk.form.web;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.DatabaseInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.form.FormMd;
import com.runwaysdk.form.FormObject;
import com.runwaysdk.form.field.FieldIF;
import com.runwaysdk.form.web.field.FieldBuilders;
import com.runwaysdk.form.web.field.WebField;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.system.metadata.MdClassDTO;
import com.runwaysdk.system.metadata.MdFieldDTO;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.system.metadata.MdWebFieldDTO;
import com.runwaysdk.system.metadata.MdWebGroupDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.conversion.ConversionExceptionDTO;
import com.runwaysdk.util.IDGenerator;

public class WebFormObject extends FormObject implements WebFormComponent
{
  private WebFormObject(FormMd formMd, Map<String, FieldIF> fields)
  {
    super(formMd, fields);
  }

  /**
   * Creates and returns a WebFormObject based on the given metadata and
   * underlying data object.
   * 
   * @param mdFormDTO
   * @param formData
   * @return
   */
  private static WebFormObject convertToWebFormObject(MdFormDTO mdFormDTO, ComponentDTOIF formData)
  {
    // Create a new Field for each MdField definition
    MdFieldDTO[] mdFields = mdFormDTO.getOrderedMdFields();

    // Map of MdAttribute ids to the AttributeDTO representation
    Map<String, AttributeDTO> mdIdToAttrDTOs = ComponentDTOFacade.mapMdAttributeIdToAttributeDTOs(formData);

    // use LinkedHashMap to preserve field order as sent by the server
    Map<String, FieldIF> fields = new LinkedHashMap<String, FieldIF>();

    for (MdFieldDTO mdField : mdFields)
    {
      if (! ( mdField instanceof MdWebGroupDTO ))
      {
        FieldBuilders.WebFieldBuilder builder = FieldBuilders.getBuilder(mdField);
        FieldIF field = builder.create(mdFormDTO, (MdWebFieldDTO) mdField, formData, mdIdToAttrDTOs);
        fields.put(field.getFieldName(), field);
      }
    }

    WebFormMd md = WebFormMd.newInstance(mdFormDTO);
    WebFormObject formObject = new WebFormObject(md, fields);

    // Set standard fields on the form describing the data it wraps
    String id = IDGenerator.nextID() + mdFormDTO.getMd().getId().substring(0, new Integer(DatabaseInfo.ROOT_ID_SIZE).intValue());

    formObject.setId(id);
    formObject.setDataId(formData.getId());
    formObject.setType(mdFormDTO.getType());
    formObject.setReadable(formData.isReadable());

    if (formData instanceof MutableDTO)
    {
      MutableDTO mDTO = (MutableDTO) formData;
      formObject.setNewInstance(mDTO.isNewInstance());
      formObject.setWritable(mDTO.isWritable());
    }
    else
    {
      formObject.setNewInstance(true);
      formObject.setWritable(MutableDTO.WRITABLE_DEFAULT);
    }

    if (formData instanceof EntityDTO)
    {
      EntityDTO entity = (EntityDTO) formData;
      formObject.setDisconnected(entity.isDisconnected());
    }
    else
    {
      formObject.setDisconnected(false);
    }

    return formObject;
  }

  /**
   * Gets the underlying data Class that the MdFormDTO represents.
   * 
   * @param mdForm
   * @return
   */
  private static Class<?> getDTOClass(MdFormDTO mdFormDTO)
  {
    String type = getType(mdFormDTO) + TypeGeneratorInfo.DTO_SUFFIX;

    // Instantiate a type-safe object to use as the data
    Class<?> clazz = LoaderDecorator.load(type);

    return clazz;
  }

  /**
   * Gets the fully qualified type of the underlying class that the MdFormDTO
   * represents.
   * 
   * @param mdFormDTO
   * @return
   */
  private static String getType(MdFormDTO mdFormDTO)
  {
    MdClassDTO mdClass = mdFormDTO.getFormMdClass();

    return mdClass.getPackageName() + "." + mdClass.getTypeName();
  }

  /**
   * Constructs a FormObject based on the data object with the given id.
   * 
   * @param mdFormDTO
   * @param dataId
   * @return
   */
  public static WebFormObject getInstance(MdFormDTO mdFormDTO, String dataId)
  {

    Class<?> dtoClass = getDTOClass(mdFormDTO);
    ClientRequestIF request = mdFormDTO.getRequest();
    ComponentDTOIF formData;
    try
    {
      formData = (ComponentDTOIF) dtoClass.getMethod(CommonGenerationUtil.GET, ClientRequestIF.class, String.class).invoke(null, request, dataId);
    }
    catch (Throwable t)
    {
      String msg = "Could not get the object [" + dataId + "] of type [" + dtoClass.getName() + "] to populate the form [" + mdFormDTO.getFormName() + "]";
      throw new ConversionExceptionDTO(msg, t);
    }

    return convertToWebFormObject(mdFormDTO, formData);
  }

  /**
   * Creates a new FormObject with a new instance that the
   * 
   * @param mdFormDTO
   * @return
   */
  public static WebFormObject newInstance(MdFormDTO mdFormDTO)
  {
    Class<?> dtoClass = getDTOClass(mdFormDTO);

    ClientRequestIF request = mdFormDTO.getRequest();
    ComponentDTOIF formData;
    try
    {
      formData = (ComponentDTOIF) dtoClass.getConstructor(ClientRequestIF.class).newInstance(request);
    }
    catch (Throwable t)
    {
      while (t instanceof InvocationTargetException)
      {
        t = t.getCause();
      }

      if (t instanceof RunwayExceptionDTO)
      {
        throw (RunwayExceptionDTO) t;
      }

      String msg = "Could not instantiate [" + dtoClass.getName() + "] to populate the form [" + mdFormDTO.getFormName() + "]";
      throw new ConversionExceptionDTO(msg, t);
    }

    return WebFormObject.newInstance(mdFormDTO, formData);
  }

  /**
   * Creates a new FormObject with a new instance that the
   * 
   * @param mdFormDTO
   * @return
   */
  public static WebFormObject newInstance(MdFormDTO mdFormDTO, ComponentDTOIF formData)
  {
    WebFormObject formObject = convertToWebFormObject(mdFormDTO, formData);
    formObject.setDisconnected(false);

    return formObject;
  }

  /**
   * Creates a new FormObject with a new instance that the
   * 
   * @param mdFormDTO
   * @return
   */
  public static WebFormObject newDisconnectedInstance(MdFormDTO mdFormDTO)
  {
    String type = getType(mdFormDTO);
    ClientRequestIF request = mdFormDTO.getRequest();
    try
    {
      EntityDTO formData = request.newDisconnectedEntity(type);

      return convertToWebFormObject(mdFormDTO, formData);
    }
    catch (Throwable t)
    {
      while (t instanceof InvocationTargetException)
      {
        t = t.getCause();
      }

      if (t instanceof RunwayExceptionDTO)
      {
        throw (RunwayExceptionDTO) t;
      }

      String msg = "Could not instantiate [" + type + "] to populate the form [" + mdFormDTO.getFormName() + "]";
      throw new ConversionExceptionDTO(msg, t);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, ? extends WebField> getFieldMap()
  {
    return (Map<String, WebField>) super.getFieldMap();
  }

  /**
   * Accepts a visitor to traverse the fields and the form itself.
   */
  public void accept(WebFormVisitor visitor)
  {
    for (WebField field : this.getFieldMap().values())
    {
      field.accept(visitor);
    }

    visitor.visit(this);
  }

}
