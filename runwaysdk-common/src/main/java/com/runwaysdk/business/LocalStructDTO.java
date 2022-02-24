/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.business;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.LocaleUtils;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.localization.LocalizationFacade;
import com.runwaysdk.localization.LocalizedValueIF;
import com.runwaysdk.transport.attributes.AttributeDTO;

public class LocalStructDTO extends StructDTO implements LocalizedValueIF
{
  /**
   *
   */
  private static final long serialVersionUID = -305642052029325191L;
  
  public final static String CLASS = Constants.SYSTEM_BUSINESS_PACKAGE+".LocalStructDTO";

  /**
   * Holds the value of the character for the session's current locale. Locale
   * can't be obtained at the clerver, so we precache this value on the server
   * side before the DTO is sent across the wire.
   */
  private String                    localizedValue;

  /**
   * Indicates if the end user has set the localized value.
   */
  boolean                   isLocalizedValueModified = false;
  
  /**
   * Generic business object. The boolean parameter is a hack to prevent
   * infinite recursion. Shrug.
   *
   * @param clientRequest
   */
  protected LocalStructDTO(ClientRequestIF clientRequest, String type)
  {
    super(clientRequest, type);
  }

  /**
   * Constructor used when the object is instantiated on the front-end.
   *
   * @param clientRequest
   */
  protected LocalStructDTO(ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }

  protected LocalStructDTO(LocalStructDTO localStructDTO, ClientRequestIF clientRequest)
  {
    super(clientRequest);

    if (!this.getDeclaredType().equals(localStructDTO.getType()))
    {
      String msg = "Cannot instaniate [" + this.getDeclaredType() + "] with a generic DTO of [" + localStructDTO.getType() + "]";

      CommonExceptionProcessor.processException(
          ExceptionConstants.ProgrammingErrorException.getExceptionClass(), msg);
    }

    this.copyProperties(localStructDTO, localStructDTO.attributeMap);
  }

  /**
   * Constructor used when the object is instantiated on the front-end or
   * back-end. If the clientRequest is null, then it is instantiated on the
   * front-end, otherwise on the back-end.
   */
  public LocalStructDTO(ClientRequestIF clientRequest, String type, Map<String, AttributeDTO> attributeMap)
  {
    super(clientRequest, type, attributeMap);
  }

  public String getValue()
  {
    return localizedValue;
  }

  /**
   * This method should be called when the end user is attempting to localize a value.
   *
   * @param value
   */
  public void setValue(String value)
  {
    this.localizedValue = value;
    this.isLocalizedValueModified = true;
  }

  /**
   * This method is used by the framework to copy localized values from
   * Business to DTO conversions

   * @param value
   */
  protected void copyLocalizedValue(String value)
  {
    this.localizedValue = value;
  }

  /**
   * Indicates if the end user has set the localized value.
   *
   * @return true if the end user has set the localized value, false otherwise.
   */
  public boolean isLocalizedValueModified()
  {
    return this.isLocalizedValueModified;
  }

  public String getValue(Locale locale)
  {
    List<String> attributeNames = getAttributeNames();
    String localeString = locale.toString().toLowerCase();

    for (int i = localeString.length(); i > 0; i = localeString.lastIndexOf('_', i - 1))
    {
      String subLocale = localeString.substring(0, i);
      if (attributeNames.contains(subLocale) && !getValue(subLocale).trim().equals(""))
      {
        return getValue(subLocale);
      }
    }

    // No matching locale found. Resort to the default
    return getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }

  public String toString()
  {
    return getValue();
  }
  
  @Override
  public void setValue(Locale locale, String value)
  {
    this.setValue(locale.toString(), value);
  }
  
  @Override
  public Map<String, String> getLocaleMap()
  {
    Map<String, String> map = new HashMap<String, String>();
    
    for (Locale locale : LocalizationFacade.getInstalledLocales())
    {
      map.put(locale.toString(), this.getValue(locale));
    }
    
    map.put(MdAttributeLocalInfo.DEFAULT_LOCALE, this.getDefaultValue());
    
    return map;
  }
  
  @Override
  public void setLocaleMap(Map<String, String> map)
  {
    for (Entry<String,String> entry : map.entrySet())
    {
      this.setValue(LocaleUtils.toLocale(entry.getKey()), entry.getValue());
    }
  }

  @Override
  public String getDefaultValue()
  {
    return this.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);
  }

  @Override
  public void setDefaultValue(String value)
  {
    this.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, value);
  }
}
