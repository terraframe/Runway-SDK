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
package com.runwaysdk.dataaccess.io.dataDefinition;

import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.io.dataDefinition.TimestampHandler.Action;

/**
 * Parses the {@link XMLTags#CREATE_TAG}.
 * 
 * @author Justin Smethie
 */
public class CreateHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public CreateHandler(ImportManager manager)
  {
    super(manager);

    // Setup default dispatching
    MdBusinessHandler mdBusinessHandler = new MdBusinessHandler(manager);
    MdRelationshipHandler relationshipHandler = new MdRelationshipHandler(manager);

    // Metadata handlers
    this.addHandler(XMLTags.MD_TABLE_TAG, new MdTableHandler(manager));
    this.addHandler(XMLTags.ENUMERATION_MASTER_TAG, mdBusinessHandler);
    this.addHandler(XMLTags.MD_BUSINESS_TAG, mdBusinessHandler);
    this.addHandler(XMLTags.MD_TERM_TAG, mdBusinessHandler);
    this.addHandler(XMLTags.MD_ENUMERATION_TAG, new MdEnumerationHandler(manager));
    this.addHandler(XMLTags.MD_STRUCT_TAG, new MdStructHandler(manager));
    this.addHandler(XMLTags.MD_LOCAL_STRUCT_TAG, new MdLocalStructHandler(manager));
    this.addHandler(XMLTags.MD_RELATIONSHIP_TAG, relationshipHandler);
    this.addHandler(XMLTags.MD_TREE_TAG, relationshipHandler);
    this.addHandler(XMLTags.MD_GRAPH_TAG, relationshipHandler);
    this.addHandler(XMLTags.MD_TERM_RELATIONSHIP_TAG, relationshipHandler);
    this.addHandler(XMLTags.MD_PROBLEM_TAG, new MdProblemHandler(manager));
    this.addHandler(XMLTags.MD_INFORMATION_TAG, new MdInformationHandler(manager));
    this.addHandler(XMLTags.MD_WARNING_TAG, new MdWarningHandler(manager));
    this.addHandler(XMLTags.MD_EXCEPTION_TAG, new MdExceptionHandler(manager));
    this.addHandler(XMLTags.MD_INDEX_TAG, new MdIndexHandler(manager));
    this.addHandler(XMLTags.MD_VIEW_TAG, new MdViewHandler(manager));
    this.addHandler(XMLTags.MD_UTIL_TAG, new MdUtilHandler(manager));
    this.addHandler(XMLTags.MD_VERTEX_TAG, new MdVertexHandler(manager));
    this.addHandler(XMLTags.MD_EDGE_TAG, new MdEdgeHandler(manager));
    this.addHandler(XMLTags.MD_GRAPH_EMBEDDED_TAG, new MdGraphEmbeddedHandler(manager));
    this.addHandler(XMLTags.MD_WEB_FORM_TAG, new MdWebFormHandler(manager));
    this.addHandler(XMLTags.MD_CLASSIFICATION_TAG, new MdClassificationHandler(manager));

    // Data handlers
    this.addHandler(XMLTags.OBJECT_TAG, new ObjectHandler(manager));
    this.addHandler(XMLTags.RELATIONSHIP_TAG, new RelationshipHandler(manager));
    this.addHandler(XMLTags.VERTEX_TAG, new VertexHandler(manager));
    this.addHandler(XMLTags.EDGE_TAG, new EdgeHandler(manager));
    this.addHandler(XMLTags.TIMESTAMP_TAG, new TimestampHandler(manager, Action.CREATE));
  }
}
