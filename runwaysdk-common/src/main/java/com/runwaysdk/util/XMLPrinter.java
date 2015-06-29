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
package com.runwaysdk.util;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class XMLPrinter {
    
    public static void serialize(Document[] docs)
    {
      for(Document doc : docs)
      {
        serialize(doc);
      }
    }
    
    public static void serialize(Document document) {
      
      TransformerFactory tfactory = TransformerFactory.newInstance();
      Transformer serializer;
      try {
          serializer = tfactory.newTransformer();
          //Setup indenting to "pretty print"
          serializer.setOutputProperty(OutputKeys.INDENT, "yes");
          serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
          
          serializer.transform(new DOMSource(document), new StreamResult(System.out));
      } catch (TransformerException e) {
          // this is fatal, just dump the stack
          e.printStackTrace();
      }
  }
}
