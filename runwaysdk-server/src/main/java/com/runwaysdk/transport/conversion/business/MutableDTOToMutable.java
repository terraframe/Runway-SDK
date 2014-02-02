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
package com.runwaysdk.transport.conversion.business;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.AttributeBooleanParseException;
import com.runwaysdk.AttributeDateTimeParseException;
import com.runwaysdk.AttributeDecimalParseException;
import com.runwaysdk.AttributeIntegerParseException;
import com.runwaysdk.RunwayException;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.Element;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.LocalStruct;
import com.runwaysdk.business.LocalStructDTO;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.SessionComponent;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.SmartException;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.Util;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.View;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdAttributeBlobDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeMultiReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.metadata.MdAttributeMomentDAO;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Session;
import com.runwaysdk.session.SessionIF;
import com.runwaysdk.transport.attributes.AttributeLocalDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;

public abstract class MutableDTOToMutable
{
  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * The source MutableDTO
   */
  protected MutableDTO mutableDTO;

  /**
   * The destination Component
   */
  private Mutable      mutable;

  /**
   * Denotes if the Component is type-safe
   */
  private boolean      typeSafe;

  /**
   * The sessionId that requires this conversion.
   */
  private String       sessionId;

  /**
   * 
   * @param entityDTO
   * @param entity
   */
  public MutableDTOToMutable(String sessionId, MutableDTO mutableDTO, Mutable component)
  {
    this.mutableDTO = mutableDTO;
    this.mutable = component;
    this.sessionId = sessionId;

    // Map the id of the new instance to the id that will be used should this
    // new instance be applied.
    if (mutableDTO.isNewInstance())
    {
      Session.mapNewInstanceTempId(this.mutableDTO.getId(), this.mutable.getId());
    }

    // check for type-safety
    this.typeSafe = component.getClass().getName().equals(component.getType());
  }

  public boolean isTypeSafe()
  {
    return this.typeSafe;
  }

  public String getSessionId()
  {
    return this.sessionId;
  }

  public Mutable populate()
  {
    setAttributes();

    return mutable;
  }

  /**
   * Copies the attributes from the ComponentDTO to the ComponentIF
   */
  protected void setAttributes()
  {
    List<String> availableAttributes = mutableDTO.getAttributeNames();
    for (String attributeName : availableAttributes)
    {
      MdAttributeDAOIF mdAttributeIF = mutable.getMdAttributeDAO(attributeName);

      MdAttributeConcreteDAOIF mdAttributeConcrete = mdAttributeIF.getMdAttributeConcrete();

      // Only set the attribute if it's defined, and it is not a system
      // attribute
      if (this.copyAttribute(mdAttributeIF))
      {
        if (mdAttributeConcrete instanceof MdAttributeEnumerationDAOIF)
        {
          setAttributeEnumeration(mdAttributeIF);
        }
        else if (mdAttributeConcrete instanceof MdAttributeMultiReferenceDAOIF)
        {
          setAttributeMultiReference(mdAttributeIF);
        }
        else if (mdAttributeConcrete instanceof MdAttributeLocalDAOIF)
        {
          setAttributeLocalStruct(mdAttributeIF);
        }
        else if (mdAttributeConcrete instanceof MdAttributeStructDAOIF)
        {
          setAttributeStruct(mdAttributeIF);
        }
        else if (mdAttributeConcrete instanceof MdAttributeReferenceDAOIF)
        {
          setAttributeReference(mdAttributeIF);
        }
        else if (mdAttributeConcrete instanceof MdAttributeBlobDAOIF)
        {
          setAttributeBlob((MdAttributeBlobDAOIF) mdAttributeIF);
        }
        else
        {
          setAttribute(mdAttributeIF);
        }
      }
    }
  }

  /**
   * Returns true if the attribute should be copied from the DTO to the Business
   * layer class, false otherwise.
   * 
   * @param mdAttributeIF
   * @return true if the attribute should be copied from the DTO to the Business
   *         layer class, false otherwise.
   */
  protected abstract boolean copyAttribute(MdAttributeDAOIF mdAttributeIF);

  /**
   * Sets a blob attribute
   * 
   * @param mdAttributeIF
   */
  protected void setAttributeBlob(MdAttributeDAOIF mdAttributeIF)

  {
    if (typeSafe)
    {
      String methodName = "set" + CommonGenerationUtil.upperFirstCharacter(mdAttributeIF.definesAttribute());
      Object param = mutableDTO.getBlob(mdAttributeIF.definesAttribute());
      invokeSetter(methodName, byte[].class, param, false, mdAttributeIF);
    }
    else
    {
      String attributeName = mdAttributeIF.definesAttribute();
      this.mutable.setBlob(attributeName, mutableDTO.getBlob(attributeName));
    }
  }

  protected void setAttributeReference(MdAttributeDAOIF mdAttributeIF)
  {
    // just set the id since DTO's don't hold the actual references
    String attributeName = mdAttributeIF.definesAttribute();
    this.mutable.setValue(attributeName, mutableDTO.getValue(attributeName));
  }

  protected void setAttributeMultiReference(MdAttributeDAOIF mdAttributeIF)
  {
    // just set the id since DTO's don't hold the actual references
    String attributeName = mdAttributeIF.definesAttribute();

    List<String> itemIds = mutableDTO.getMultiItems(attributeName);

    for (String itemId : itemIds)
    {
      this.mutable.addMultiItem(attributeName, itemId);
    }
  }

  /**
   * Sets a struct attribute
   * 
   * @param mdAttributeIF
   */
  private void setAttributeStruct(MdAttributeDAOIF mdAttributeIF)
  {
    this.setAttributeStructInternal(mdAttributeIF);
  }

  /**
   * Sets a struct attribute
   * 
   * @param mdAttributeIF
   */
  private void setAttributeLocalStruct(MdAttributeDAOIF mdAttributeIF)
  {
    LocalStruct localStruct = (LocalStruct) this.setAttributeStructInternal(mdAttributeIF);

    AttributeLocalDTO attributeLocalDTO = (AttributeLocalDTO) ComponentDTOFacade.getAttributeDTO(mutableDTO, mdAttributeIF.definesAttribute());

    LocalStructDTO localStructDTO = (LocalStructDTO) attributeLocalDTO.getStructDTO();

    if (localStructDTO.isLocalizedValueModified())
    {
      String localizedValue = localStructDTO.getValue();
      localStruct.setValue(localizedValue);
    }
  }

  /**
   * Sets a struct attribute
   * 
   * @param mdAttributeIF
   */
  private Struct setAttributeStructInternal(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();

    AttributeStructDTO attributeStructDTO = (AttributeStructDTO) ComponentDTOFacade.getAttributeDTO(mutableDTO, attributeName);

    Struct struct;

    if (isTypeSafe())
    {
      try
      {
        // get the type-safe struct
        String structGetter = "get" + CommonGenerationUtil.upperFirstCharacter(attributeName);
        Class<?> clazz = mutable.getClass();
        struct = (Struct) clazz.getMethod(structGetter).invoke(mutable);
        StructDTOToStruct dtoToEntity = new StructDTOToStruct(getSessionId(), attributeStructDTO.getStructDTO(), struct);
        dtoToEntity.populate();
      }
      catch (SecurityException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (NoSuchMethodException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (IllegalArgumentException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (IllegalAccessException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (InvocationTargetException e)
      {
        if (e.getTargetException() instanceof RunwayException)
        {
          RunwayException fwEx = (RunwayException) e.getTargetException();
          throw fwEx;
        }
        else if (e.getTargetException() instanceof SmartException)
        {
          throw (SmartException) e.getTargetException();
        }
        else
        {
          throw new ProgrammingErrorException(e);
        }
      }
    }
    else
    {
      // safe cast since only elements can define structs
      Element element = (Element) mutable;
      struct = element.getGenericStruct(attributeName);

      List<? extends MdAttributeConcreteDAOIF> structMdAttributes = ( (MdAttributeStructDAOIF) mdAttributeIF ).getMdStructDAOIF().definesAttributes();

      for (MdAttributeConcreteDAOIF structAttribute : structMdAttributes)
      {
        String name = structAttribute.definesAttribute();

        // copy all enumeration items if applicable
        if (structAttribute instanceof MdAttributeEnumerationDAOIF && !structAttribute.isSystem())
        {
          MdAttributeEnumerationDAOIF structEnumAttribute = (MdAttributeEnumerationDAOIF) structAttribute;

          String masterType = structEnumAttribute.getMdEnumerationDAO().getMasterListMdBusinessDAO().definesType();

          struct.clearEnum(name);

          List<String> enumNames = attributeStructDTO.getEnumNames(name);
          for (String enumName : enumNames)
          {
            EnumerationItemDAO enumerationItem = EnumerationItemDAO.getEnumeration(masterType, enumName);
            struct.addEnumItem(name, enumerationItem.getId());
          }
        }
        else if (!structAttribute.isSystem())
        {
          // copy the struct values from the DTO to the Entity
          String value = attributeStructDTO.getValue(name);

          struct.setValue(name, value);
        }
      }
    }

    return struct;
  }

  /**
   * Sets an enumeration attribute
   * 
   * @param mdAttributeIF
   */
  protected void setAttributeEnumeration(MdAttributeDAOIF mdAttributeIF)
  {
    MdAttributeEnumerationDAOIF mdAttributeConcrete = (MdAttributeEnumerationDAOIF) mdAttributeIF.getMdAttributeConcrete();
    MdEnumerationDAOIF mdEnumerationIF = mdAttributeConcrete.getMdEnumerationDAO();
    String mdEnumerationType = mdEnumerationIF.definesType();
    String attributeName = mdAttributeIF.definesAttribute();
    List<String> names = mutableDTO.getEnumNames(attributeName);

    if (typeSafe)
    {
      if (mdAttributeConcrete.selectMultiple())
      {
        String clearName = "clear" + CommonGenerationUtil.upperFirstCharacter(mdAttributeIF.definesAttribute());
        invokeSetter(clearName, null, null, true, mdAttributeIF);
      }

      // Loop through all set enumeration items.
      for (int i = 0; i < names.size(); i++)
      {

        String name = names.get(i);

        if (name == null || name.equals(""))
          continue; // empty strings for item ids are invalid (don't set)

        String addName = "add" + CommonGenerationUtil.upperFirstCharacter(mdAttributeIF.definesAttribute());

        try
        {
          Class<?> clazz = LoaderDecorator.load(mdEnumerationType);
          Object enumItem = clazz.getMethod("valueOf", String.class).invoke(null, name);
          invokeSetter(addName, enumItem.getClass(), enumItem, false, mdAttributeIF);
        }
        catch (SecurityException e)
        {
          throw new ProgrammingErrorException(e);
        }
        catch (NoSuchMethodException e)
        {
          throw new ProgrammingErrorException(e);
        }
        catch (IllegalArgumentException e)
        {
          throw new ProgrammingErrorException(e);
        }
        catch (IllegalAccessException e)
        {
          throw new ProgrammingErrorException(e);
        }
        catch (InvocationTargetException e)
        {
          if (e.getTargetException() instanceof RunwayException)
          {
            RunwayException fwEx = (RunwayException) e.getTargetException();
            throw fwEx;
          }
          else if (e.getTargetException() instanceof SmartException)
          {
            throw (SmartException) e.getTargetException();
          }
          else
          {
            throw new ProgrammingErrorException(e);
          }
        }
      }
    }
    else
    {
      if (mdAttributeConcrete.selectMultiple())
      {
        mutable.clearEnum(attributeName);
      }

      MdBusinessDAOIF masterMdBusinessIF = mdEnumerationIF.getMasterListMdBusinessDAO();
      String masterListType = masterMdBusinessIF.definesType();
      // Loop through all set enumeration items.
      for (int i = 0; i < names.size(); i++)
      {
        mutable.addEnumItem(attributeName, EnumerationItemDAO.getEnumeration(masterListType, names.get(i)).getId());
      }
    }
  }

  /**
   * Sets an attribute. Some attributes map to java classes that cannot handle
   * empty string values (like Integer). In those cases, the attribute value
   * becomes null.
   * 
   * @param mdAttributeIF
   */
  protected void setAttribute(MdAttributeDAOIF mdAttributeIF)
  {
    String attributeName = mdAttributeIF.definesAttribute();
    Object value = mutableDTO.getObjectValue(attributeName);

    if (typeSafe)
    {
      String attributeType = mdAttributeIF.javaType(false);

      Class<?> paramClass = null;
      Object param = null;
      if (attributeType.equals(String.class.getSimpleName()))
      {
        paramClass = String.class;
        param = value;
      }
      else if (attributeType.equals(Long.class.getSimpleName()))
      {
        paramClass = Long.class;
        if (value != null && !value.equals(""))
        {
          try
          {
            param = new Long((String) value);
          }
          catch (Throwable e)
          {
            String errMsg = "[" + (String) value + "] is not a valid [" + Long.class.getName() + "]";
            throw new AttributeIntegerParseException(errMsg, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), (String) value);
          }
        }
      }
      else if (attributeType.equals(Float.class.getSimpleName()))
      {
        paramClass = Float.class;
        if (value != null && !value.equals(""))
        {
          try
          {
            param = new Float((String) value);
          }
          catch (Throwable e)
          {
            String errMsg = "[" + (String) value + "] is not a valid [" + Float.class.getName() + "]";
            throw new AttributeDecimalParseException(errMsg, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), (String) value);
          }
        }
      }
      else if (attributeType.equals(Double.class.getSimpleName()))
      {
        paramClass = Double.class;
        if (value != null && !value.equals(""))
        {
          try
          {
            param = new Double((String) value);
          }
          catch (Throwable e)
          {
            String errMsg = "[" + (String) value + "] is not a valid [" + Double.class.getName() + "]";
            throw new AttributeDecimalParseException(errMsg, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), (String) value);
          }
        }
      }
      else if (attributeType.equals(BigDecimal.class.getName()))
      {
        paramClass = BigDecimal.class;
        if (value != null && !value.equals(""))
        {
          try
          {
            param = new BigDecimal((String) value);
          }
          catch (Throwable e)
          {
            String errMsg = "[" + (String) value + "] is not a valid [" + BigDecimal.class.getName() + "]";
            throw new AttributeDecimalParseException(errMsg, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), (String) value);
          }
        }
      }
      else if (attributeType.equals(Integer.class.getSimpleName()))
      {
        paramClass = Integer.class;
        if (value != null && !value.equals(""))
        {
          try
          {
            param = new Integer((String) value);
          }
          catch (Throwable e)
          {
            String errMsg = "[" + (String) value + "] is not a valid [" + Double.class.getName() + "]";
            throw new AttributeIntegerParseException(errMsg, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), (String) value);
          }
        }
      }
      else if (attributeType.equals(Boolean.class.getSimpleName()))
      {
        paramClass = Boolean.class;
        if (value != null && !value.equals(""))
        {
          try
          {
            param = Boolean.valueOf((String) value);
          }
          catch (Throwable e)
          {
            String errMsg = "[" + (String) value + "] is not a valid [" + Boolean.class.getName() + "]";
            throw new AttributeBooleanParseException(errMsg, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), (String) value);
          }
        }
      }
      else if (attributeType.equals(java.util.Date.class.getName()))
      {
        paramClass = java.util.Date.class;

        if (value != null && !value.equals(""))
        {
          String format = ( (MdAttributeMomentDAO) mdAttributeIF.getMdAttributeConcrete() ).getFormat();
          DateFormat dateFormat = new SimpleDateFormat(format);
          try
          {
            param = dateFormat.parse((String) value);
          }
          catch (ParseException e)
          {
            String error = "The value [" + (String) value + "] is not a valid [" + mdAttributeIF.getClass().getName() + "]";
            throw new AttributeDateTimeParseException(error, mdAttributeIF.getDisplayLabel(Session.getCurrentLocale()), (String) value, format);
          }
        }
      }

      if (paramClass == null)
      {
        for (PluginIF plugin : pluginMap.values())
        {
          paramClass = plugin.getParamClassAttribute(mdAttributeIF);
          param = plugin.parseTypeSafeAttribute(mdAttributeIF, mutableDTO, param);

          if (paramClass != null)
          {
            break;
          }

        }
      }

      if (paramClass == null)
      {
        String error = "The type [" + attributeType + "] is not supported as a parameter.";
        throw new ProgrammingErrorException(error);
      }

      String methodName = "set" + CommonGenerationUtil.upperFirstCharacter(mdAttributeIF.definesAttribute());

      invokeSetter(methodName, paramClass, param, false, mdAttributeIF);
    }
    else
    {
      mutable.setValue(attributeName, value);
    }
  }

  /**
   * Invokes a type-safe setter for an attribute.
   * 
   * @param methodName
   * @param paramClass
   * @param param
   * @param isVoid
   * @param mdAttributeIF
   *          TODO
   */
  protected void invokeSetter(String methodName, Class<?> paramClass, Object param, boolean isVoid, MdAttributeDAOIF mdAttributeIF)
  {
    try
    {
      Class<?> clazz = mutable.getClass();

      if (!mdAttributeIF.getGenerateAccessor())
      {
        if (!isVoid)
        {
          // This has to be cast because the declared type of Object invokes a
          // different setter than a declared type of String
          if (param instanceof String)
          {
            mutable.setValue(mdAttributeIF.definesAttribute(), (String) param);
          }
          else
          {
            mutable.setValue(mdAttributeIF.definesAttribute(), param);
          }
        }
      }
      else if (isVoid)
      {
        Method method = clazz.getMethod(methodName);
        method.invoke(mutable);
      }
      else
      {
        Method method = clazz.getMethod(methodName, paramClass);
        method.invoke(mutable, param);
      }
    }
    catch (IllegalArgumentException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (IllegalAccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (InvocationTargetException e)
    {
      if (e.getTargetException() instanceof RunwayException)
      {
        RunwayException fwEx = (RunwayException) e.getTargetException();
        throw fwEx;
      }
      else if (e.getTargetException() instanceof SmartException)
      {
        throw (SmartException) e.getTargetException();
      }
      else
      {
        throw new ProgrammingErrorException(e);
      }
    }
    catch (SecurityException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (NoSuchMethodException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Returns a converter of the proper type. This is provided so that clients
   * don't have to if/else on the type and new instance flag themselves.
   * 
   * @param sessionId
   * @param entity
   * @param lockEntity
   * @return
   */
  public static MutableDTOToMutable getConverter(String sessionId, MutableDTO mutableDTO)
  {
    Mutable component = null;
    MutableDTOToMutable converter = null;

    // First, a new instance of the appropriate type or an instance of an
    // existing type
    if (mutableDTO.isNewInstance())
    {

      if (mutableDTO instanceof EntityDTO)
      {
        // Business
        if (mutableDTO instanceof BusinessDTO)
        {
          component = BusinessFacade.newBusiness(mutableDTO.getType());
        }
        // Relationship
        else if (mutableDTO instanceof RelationshipDTO)
        {
          String parentId = ( (RelationshipDTO) mutableDTO ).getParentId();
          String childId = ( (RelationshipDTO) mutableDTO ).getChildId();
          component = BusinessFacade.newRelationship(parentId, childId, mutableDTO.getType());
        }
        // Struct
        else if (mutableDTO instanceof StructDTO)
        {
          component = BusinessFacade.newStruct(mutableDTO.getType());
        }
      }

      // View & Util
      else if (mutableDTO instanceof SessionDTO)
      {
        component = BusinessFacade.newSessionComponent(mutableDTO.getType());
        TransientDAO transientDAO = (TransientDAO) BusinessFacade.getTransientDAO( ( (SessionComponent) component ));
        transientDAO.setProblemNotificationId(mutableDTO.getId());
      }
    }
    else
    {
      if (mutableDTO instanceof ViewDTO)
      {
        // IMPORTANT: It is possible for a ViewDTO to be isNew == false, but not
        // be applied to the session. This occurs whenever a ViewDTO is
        // generated from a ViewQuery. As such if the ViewDTO does not exist we
        // are just going to assume it came from a ViewQuery and create a new
        // one. This provides the user the ability to invoke methods on the
        // ViewDTO.
        try
        {
          component = View.get(mutableDTO.getId());
        }
        catch (DataNotFoundException e)
        {
          component = BusinessFacade.newSessionComponent(mutableDTO.getType());
          TransientDAO transientDAO = (TransientDAO) BusinessFacade.getTransientDAO( ( (SessionComponent) component ));
          transientDAO.setProblemNotificationId(mutableDTO.getId());
        }
      }
      else if (mutableDTO instanceof UtilDTO)
      {
        component = Util.get(mutableDTO.getId());
      }
      // Assume entity
      else
      {
        component = BusinessFacade.getEntity(mutableDTO.getId());
        // Obtain an application lock on the object. This will give the Entity a
        // new cloned copy of its EntityDAO,
        // rather than one that might be reference to one in the global cache.
        if (component instanceof Element)
        {
          Element element = (Element) component;

          SessionIF session = Session.getCurrentSession();
          if (session != null)
          {
            if (element.getValue(ElementInfo.LOCKED_BY).equals(session.getUser().getId()))
            {
              element.appLock();
            }
          }
        }
      }
    }

    // now grab the converter appropriate for the conversion
    if (component instanceof Business)
    {
      converter = new BusinessDTOToBusiness(sessionId, (BusinessDTO) mutableDTO, (Business) component);
    }
    else if (component instanceof Relationship)
    {
      converter = new RelationshipDTOToRelationship(sessionId, (RelationshipDTO) mutableDTO, (Relationship) component);
    }
    else if (component instanceof Struct)
    {
      converter = new StructDTOToStruct(sessionId, (StructDTO) mutableDTO, (Struct) component);
    }
    else if (component instanceof View)
    {
      converter = new ViewDTOToView(sessionId, (ViewDTO) mutableDTO, (View) component);
    }
    else if (component instanceof Util)
    {
      converter = new UtilDTOToUtil(sessionId, (UtilDTO) mutableDTO, (Util) component);
    }

    return converter;
  }

  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public Class<?> getParamClassAttribute(MdAttributeDAOIF mdAttributeIF);

    public Object parseTypeSafeAttribute(MdAttributeDAOIF mdAttributeIF, MutableDTO mutableDTO, Object param);
  }

}
