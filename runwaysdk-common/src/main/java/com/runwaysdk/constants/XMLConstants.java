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
package com.runwaysdk.constants;

import com.runwaysdk.configuration.ConfigurationManager;

public class XMLConstants
{
  public static final String JAXP_SCHEMA_LANGUAGE     = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
  public static final String JAXP_SCHEMA_SOURCE       = "http://java.sun.com/xml/jaxp/properties/schemaSource";
  public static final String W3C_XML_SCHEMA           = javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
  public static final String W3C_XML_SCHEMA_INSTANCE  = javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
  public static final String DATATYPE_XSD             = "/" + ConfigurationManager.ConfigGroup.XSD.getPath() + "datatype.xsd";
  public static final String VERSION_XSD              = "/" + ConfigurationManager.ConfigGroup.XSD.getPath() + "version.xsd";
  public static final String INSTANCE_XSD             = "/" + ConfigurationManager.ConfigGroup.XSD.getPath() + "instance.xsd";
  public static final String TRANSACTIONITEM_XSD      = "/" + ConfigurationManager.ConfigGroup.XSD.getPath() + "transactionItem.xsd";
  public static final String TRANSACTIONRECORD_XSD    = "/" + ConfigurationManager.ConfigGroup.XSD.getPath() + "transactionRecord.xsd";
  public static final String TRANSACTION_XSD          = "/" + ConfigurationManager.ConfigGroup.XSD.getPath() + "transaction.xsd";
}
