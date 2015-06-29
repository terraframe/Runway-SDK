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
package com.runwaysdk.dataaccess.schemamanager.view;

import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.graph.layout.GraphLayout;
import org.netbeans.api.visual.graph.layout.GraphLayoutFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.SceneLayout;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

import com.runwaysdk.dataaccess.io.dataDefinition.XMLTags;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaClass;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaObserver;
import com.runwaysdk.dataaccess.schemamanager.model.SchemaRelationship;

public class SchemaScene extends GraphScene<SchemaClass, SchemaRelationship> implements SchemaObserver
{
  private LayerWidget mainLayer;

  private LayerWidget connectionLayer;

  private Router      router;

  public SchemaScene()
  {
    mainLayer = new LayerWidget(this);
    addChild(mainLayer);
    connectionLayer = new LayerWidget(this);
    addChild(connectionLayer);
    router = RouterFactory.createOrthogonalSearchRouter(mainLayer, connectionLayer);
    GraphLayout<SchemaClass, SchemaRelationship> graphLayout = GraphLayoutFactory.createOrthogonalGraphLayout(this, true);
    SceneLayout sceneLayout = LayoutFactory.createSceneGraphLayout(this, graphLayout);
    sceneLayout.invokeLayout();
  }

  @Override
  protected void attachEdgeSourceAnchor(SchemaRelationship relationship, SchemaClass oldClass, SchemaClass newClass)
  {
    Widget sourceClassWidget = findWidget(newClass);
    Anchor sourceAnchor = AnchorFactory.createRectangularAnchor(sourceClassWidget);
    ConnectionWidget connectionWidget = (ConnectionWidget) findWidget(relationship);
    connectionWidget.setSourceAnchor(sourceAnchor);

  }

  @Override
  protected void attachEdgeTargetAnchor(SchemaRelationship relationship, SchemaClass oldClass, SchemaClass newClass)
  {
    Widget targetClassWidget = findWidget(newClass);
    Anchor targetAnchor = AnchorFactory.createRectangularAnchor(targetClassWidget);
    ConnectionWidget connectionWidget = (ConnectionWidget) findWidget(relationship);
    connectionWidget.setTargetAnchor(targetAnchor);
  }

  @Override
  protected Widget attachEdgeWidget(SchemaRelationship relationship)
  {
    ConnectionWidget connectionWidget = new SchemaRelationshipWidget(this, relationship);
    connectionWidget.setRouter(router);
    connectionLayer.addChild(connectionWidget);
    return connectionWidget;

  }

  @Override
  protected Widget attachNodeWidget(SchemaClass mdClass)
  {
    SchemaClassWidget schemaClassWidget = null;

    schemaClassWidget = new SchemaClassWidget(this, mdClass);

    mainLayer.addChild(schemaClassWidget);

    return schemaClassWidget;
  }

  public void notifyClassCreated(SchemaClass mdClass)
  {
    // if (!isComponentAlreadyRendered(mdClass))
    // {
    // addNode(mdClass);
    // }

  }

  public void notifyRelationshipCreated(SchemaRelationship mdRelationship)
  {
    // if (!isComponentAlreadyRendered(mdRelationship))
    // {
    // addNode(mdRelationship);
    addEdge(mdRelationship);
    // setEdgeSource(mdRelationship,
    // mdRelationship.relationshipParent().getType());
    // setEdgeTarget(mdRelationship,
    // mdRelationship.relationshipChild().getType());
    // }

  }

  private static SchemaScene INSTANCE = null;

  public static SchemaScene instance()
  {
    if (INSTANCE == null)
    {
      INSTANCE = new SchemaScene();
    }
    return INSTANCE;
  }

  /*
   * public void notifyClassAttributeAdded(SchemaClass mdClass, SchemaAttribute
   * attribute) { SchemaClassWidget widget = (SchemaClassWidget)
   * findWidget(mdClass); widget.refreshAttributes(mdClass); }
   * 
   * public void notifyClassMethodAdded(SchemaClass mdClass, SchemaMethod
   * method) { SchemaClassWidget widget = (SchemaClassWidget)
   * findWidget(mdClass); widget.refreshMethods(mdClass); }
   */

  /*
   * public void notifyRelationshipSourceCreated(SchemaRelationship
   * mdRelationship) { if (isComponentAlreadyRendered(mdRelationship) &&
   * isComponentAlreadyRendered(mdRelationship.relationshipParent())) {
   * setEdgeSource(mdRelationship,
   * mdRelationship.relationshipParent().getType()); }
   * 
   * }
   * 
   * public void notifyRelationshipTargetCreated(SchemaRelationship
   * mdRelationship) { if (isComponentAlreadyRendered(mdRelationship) &&
   * isComponentAlreadyRendered(mdRelationship.relationshipChild())) {
   * setEdgeTarget(mdRelationship,
   * mdRelationship.relationshipChild().getType()); }
   * 
   * }
   */

  public boolean isAcceptable(SchemaClass schemaClass)
  {
    String tag = schemaClass.getTag();

    return tag.equals(XMLTags.MD_BUSINESS_TAG) || tag.equals(XMLTags.MD_TERM_TAG);

  }

  public boolean isAcceptable(SchemaRelationship relationship)
  {
    return false; // isAcceptable(relationship.relationshipParent().getType())
                  // &&
                  // isAcceptable(relationship.relationshipChild().getType());
  }

}
