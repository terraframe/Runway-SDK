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
package com.runwaysdk.transport.conversion.dom;

import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DateConverter
{
  public static Element getDocument(Date date, Document document)
  {
    String time = Long.toString(date.getTime());

    // set the Date root element
    Element root = document.createElement(Elements.DATE.getLabel());
    
    //Add the className value to the root element
    Element dateLongElement = document.createElement(Elements.DATE_LONG.getLabel());
    dateLongElement.appendChild(document.createTextNode(time));
    root.appendChild(dateLongElement);
    
    return root;
  }
  
  public static Date getDate(Node parentNode)
  {
    Element root = (Element) parentNode;
    
    Node dateLongNode = root.getElementsByTagName(Elements.DATE_LONG.getLabel()).item(0);

    String value = dateLongNode.getFirstChild().getNodeValue();
    long time = Long.parseLong(value);
    
    return new Date(time);
 }
}
