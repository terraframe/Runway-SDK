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

import java.lang.reflect.Array;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ClassQueryDTO;
import com.runwaysdk.business.EnumDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RunwayProblemDTO;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.UtilDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class ArrayConverter
{

  public static Element getArrayDOM(Object[] objectArray, Document document, boolean convertMetaData)
  {
    Element root = document.createElement(Elements.ARRAY.getLabel());

    // set the class name as an attribute on the array root element
    // we need this to know what the array is storing
    Class<?> component = objectArray.getClass().getComponentType();

    root.setAttribute(Elements.ARRAY_TYPE_ATTRIBUTE.getLabel(), component.getName());

    // loop through each object in the array and discover which
    for (Object object : objectArray)
    {
      Node item = document.createElement(Elements.ARRAY_ITEM.getLabel());

      if (object == null)
      {
        item.appendChild(NullConverter.getNullDOM(document));
      }
      else if (object instanceof BusinessDTO)
      {
        BusinessDTOtoDoc businessDTOToDoc = new BusinessDTOtoDoc((BusinessDTO) object, document, convertMetaData);
        item.appendChild(businessDTOToDoc.populate());
      }
      else if (object instanceof RelationshipDTO)
      {
        RelationshipDTOtoDoc relationshipDTOToDoc = new RelationshipDTOtoDoc((RelationshipDTO) object, document, convertMetaData);
        item.appendChild(relationshipDTOToDoc.populate());
      }
      else if (object instanceof StructDTO)
      {
        StructDTOtoDoc structDTOToDoc = new StructDTOtoDoc((StructDTO) object, document, convertMetaData);
        item.appendChild(structDTOToDoc.populate());
      }
      else if (object instanceof EnumDTO)
      {
        item.appendChild(EnumDTOConverter.getDocument((EnumDTO) object, document, convertMetaData));
      }
      else if (object.getClass().isArray())
      {
        item.appendChild(getArrayDOM((Object[]) object, document, convertMetaData));
      }
      else if (object instanceof Number || object instanceof String || object instanceof Character
          || object instanceof Boolean)
      {
        item.appendChild(PrimitiveConverter.getPrimitiveDOM(object, document));
      }
      else if(object instanceof Date)
      {
        item.appendChild(DateConverter.getDocument((Date) object, document));
      }
      else if (object instanceof UtilDTO)
      {
        UtilDTOtoDoc utilDTOToDoc = new UtilDTOtoDoc((UtilDTO) object, document, convertMetaData);
        item.appendChild(utilDTOToDoc.populate());
      }
      else if (object instanceof ViewDTO)
      {
        ViewDTOtoDoc viewDTOToDoc = new ViewDTOtoDoc((ViewDTO) object, document, convertMetaData);
        item.appendChild(viewDTOToDoc.populate());
      }
      else if (object instanceof SmartExceptionDTO)
      {
        SmartExceptionDTOtoDoc exceptionDTOtoDoc = new SmartExceptionDTOtoDoc((SmartExceptionDTO) object, document, convertMetaData);
        item.appendChild(exceptionDTOtoDoc.populate());
      }
      else if (object instanceof ClassQueryDTO)
      {
        ComponentQueryDTOtoDoc componentQueryDTOtoDoc = ComponentQueryDTOtoDoc.getConverter((ClassQueryDTO)object, document);
        item.appendChild(componentQueryDTOtoDoc.populate());
      }
      else if (object instanceof ProblemDTO)
      {
        ProblemDTOtoDoc problemDTOtoDoc = new ProblemDTOtoDoc((ProblemDTO) object, document, convertMetaData);
        item.appendChild(problemDTOtoDoc.populate());
      }
      else if (object instanceof RunwayProblemDTO)
      {
        RunwayProblemDTOtoDoc runwayProblemDTOtoDoc = RunwayProblemDTOtoDoc.getConverter((RunwayProblemDTO) object, document);
        item.appendChild(runwayProblemDTOtoDoc.populate());
      }
      else if (object instanceof WarningDTO)
      {
        WarningDTOtoDoc warningDTOtoDoc = new WarningDTOtoDoc((WarningDTO) object, document, convertMetaData);
        item.appendChild(warningDTOtoDoc.populate());
      }
      else if (object instanceof InformationDTO)
      {
        InformationDTOtoDoc informationDTOtoDoc = new InformationDTOtoDoc((InformationDTO) object, document, convertMetaData);
        item.appendChild(informationDTOtoDoc.populate());
      }
      else
      {
        String error = "Objects of type [" + object.getClass().getName()
            + "] are not supported for DOM Document conversion.";
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      }
      // add the item to the root of the array
      root.appendChild(item);
    }

    return root;
  }

  public static Object[] getArray(ClientRequestIF clientRequest, Node parent)
  {
    Object[] objectArray = null;

    // get all Array items in document
    NodeList items = parent.getChildNodes();

    // instantiate an array of the proper type (given as an attribute in the
    // "array" element)
    String arrayType = ( (Element) parent ).getAttribute(Elements.ARRAY_TYPE_ATTRIBUTE.getLabel());
    Class<?> clazz = LoaderDecorator.load(arrayType);
    objectArray = (Object[]) Array.newInstance(clazz, items.getLength());

    // loop through all items
    Object objectItem = null;
    for (int i = 0; i < items.getLength(); i++)
    {
      Node item = items.item(i);

      // use the first child of "item" because "item" is just a placeholder, not
      // the actual parent node
      // that the below get[some object](Node parent) method require.
      Node objectParent = item.getFirstChild();

      // discover the item type
      String itemType = item.getFirstChild().getNodeName();

      if (itemType.equals(Elements.NULL.getLabel()))
      {
        objectItem = NullConverter.getNull(objectParent);
      }
      else if (itemType.equals(Elements.PRIMITIVE.getLabel()))
      {
        objectItem = PrimitiveConverter.getPrimitiveObject(objectParent);
      }
      else if (itemType.equals(Elements.BUSINESS_DTO.getLabel()))
      {
        DocToBusinessDTO docToBusinessDTO = new DocToBusinessDTO(clientRequest, (Element)objectParent);
        objectItem = docToBusinessDTO.populate();
      }
      else if (itemType.equals(Elements.STRUCT_DTO.getLabel()))
      {
        DocToStructDTO docToStructDTO = new DocToStructDTO(clientRequest, (Element)objectParent);
        objectItem = docToStructDTO.populate();
      }
      else if (itemType.equals(Elements.RELATIONSHIP_DTO.getLabel()))
      {
        DocToRelationshipDTO docToRelationshipDTO = new DocToRelationshipDTO(clientRequest, (Element)objectParent);
        objectItem = docToRelationshipDTO.populate();
      }
      else if (itemType.equals(Elements.ARRAY.getLabel()))
      {
        Object temp = ArrayConverter.getArray(clientRequest, objectParent);
        objectItem = temp;
      }
      else if (itemType.equals(Elements.ENUM_DTO.getLabel()))
      {
        objectItem = EnumDTOConverter.getEnumDTO(clientRequest, objectParent);
      }
      else if (itemType.equals(Elements.DATE.getLabel()))
      {
        objectItem = DateConverter.getDate(objectParent);
      }
      else if (itemType.equals(Elements.UTIL_DTO.getLabel()))
      {
        DocToUtilDTO docToUtilDTO = new DocToUtilDTO(clientRequest, (Element)objectParent);
        objectItem = docToUtilDTO.populate();
      }
      else if (itemType.equals(Elements.VIEW_DTO.getLabel()))
      {
        DocToViewDTO docToViewDTO = new DocToViewDTO(clientRequest, (Element)objectParent);
        objectItem = docToViewDTO.populate();
      }
      else if (itemType.equals(Elements.BUSINESS_QUERY_DTO.getLabel()) ||
               itemType.equals(Elements.RELATIONSHIP_QUERY_DTO.getLabel()) ||
               itemType.equals(Elements.STRUCT_QUERY_DTO.getLabel()) ||
               itemType.equals(Elements.VIEW_QUERY_DTO.getLabel()))
      {
        DocToComponentQueryDTO docToComponentQueryDTO = DocToComponentQueryDTO.getConverter(clientRequest, (Element)objectParent, false);
        objectItem = docToComponentQueryDTO.populate();
      }
      else if (itemType.equals(Elements.EXCEPTION_DTO.getLabel()))
      {
        DocToExceptionDTO docToSmartExceptionDTO = new DocToExceptionDTO(clientRequest, (Element)objectParent);
        objectItem = docToSmartExceptionDTO.populate();
      }
      else if (itemType.equals(Elements.SMART_PROBLEM_DTO.getLabel()))
      {
        DocToProblemDTO docToProblemDTO = new DocToProblemDTO(clientRequest, (Element)objectParent);
        objectItem = docToProblemDTO.populate();
      }
      else if (itemType.equals(Elements.ATTRIBUTEPROBLEM_DTO.getLabel()))
      {
        DocToAttributeProblemDTO docToProblemDTO = new DocToAttributeProblemDTO((Element)objectParent);
        objectItem = docToProblemDTO.populate();
      }
      else if (itemType.equals(Elements.EXCELPROBLEM_DTO.getLabel()))
      {
        DocToExcelProblemDTO docToExcelProblemDTO = new DocToExcelProblemDTO((Element)objectParent);
        objectItem = docToExcelProblemDTO.populate();
      }
      else if (itemType.equals(Elements.WARNING_DTO.getLabel()))
      {
        DocToWarningDTO docToWarningDTO = new DocToWarningDTO(clientRequest, (Element)objectParent);
        objectItem = docToWarningDTO.populate();
      }
      else if (itemType.equals(Elements.INFORMATION_DTO.getLabel()))
      {
        DocToInformationDTO docToInformationDTO = new DocToInformationDTO(clientRequest, (Element)objectParent);
        objectItem = docToInformationDTO.populate();
      }
      else
      {
        String error = "DOM Documents containing a [" + itemType
            + "] root are not supported for Object Array conversion.";
        CommonExceptionProcessor.processException(ExceptionConstants.ConversionException.getExceptionClass(), error);
      }
      objectArray[i] = objectItem;
    }

    return objectArray;
  }

  protected static Object[] getArray(ClientRequestIF clientRequest, Document document)
  {
    Element root = document.getDocumentElement();
    return getArray(clientRequest, root);
  }
}
