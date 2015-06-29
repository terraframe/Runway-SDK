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
package com.runwaysdk.business.generation.json;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.facade.FacadeException;

/**
 * Represents a type hierarchy tree.
 */
public class TypeTree
{
  /**
   * An individual node in the tree.
   */
  private class Node
  {
    /**
     * The MdTypeIF value of this node.
     */
    private MdTypeDAOIF   mdTypeIF;

    /**
     * A map of all child nodes.
     */
    private Map<String, Node> children;

    /**
     * Constructs a completely empty node with no identity. This is for the root
     * node.
     */
    private Node()
    {
      this(null);
    }

    /**
     * Node constructor
     *
     * @param parent
     * @param mdType
     */
    private Node(MdTypeDAOIF mdTypeIF)
    {
      this.mdTypeIF = mdTypeIF;
      this.children = new HashMap<String, Node>();
    }

    /**
     * Adds a child node to this node. The key in
     * the children hashmap is the type that the MdTypeIF
     * defines (The MdType is the node value).
     *
     * @param node
     */
    private void addChild(Node child)
    {
      children.put(child.getMdTypeIF().definesType(), child);
    }

    /**
     *
     * @param child
     * @return
     */
    private boolean hasChild(String key)
    {
      return children.containsKey(key);
    }

    /**
     *
     * @param key
     * @return
     */
    private Node getChild(String key)
    {
      return children.get(key);
    }

    /**
     * Returns all children of this node.
     *
     * @return
     */
    private Collection<Node> getChildren()
    {
      return children.values();
    }

    /**
     *
     * @return
     */
    private MdTypeDAOIF getMdTypeIF()
    {
      return mdTypeIF;
    }
  }

  /**
   * MdEnumerations are a special case because they don't partake in
   * hierarchies, yet they are dependent on classes that do. The Node
   * class is for classes in hierarchies, which doesn't apply to MdEnumerations.
   * Because of this, all MdEnumerations are stored in this List and will be
   * appended to the very end of the total list as returned by getOrderedTypes().
   * Doing so will ensure that all the classes the MdEnumerations are dependent on will
   * already be loaded.
   */
  private List<MdEnumerationDAOIF> mdEnumerations;

  /**
   * The root node.
   */
  private Node root;

  /**
   * Constructs a new tree with the default hierarchy already in place. This
   * default hierarchy includes the statically coded dto types, such as
   * EntityDTO, BusinessDTO, EnumerationDTOIF, and so on.
   */
  TypeTree()
  {
    root = new Node();
    mdEnumerations = new LinkedList<MdEnumerationDAOIF>();
  }

  /**
   * Inserts a new node into the tree.
   *
   * @param mdTypeIF
   */
  public void insert(MdTypeDAOIF mdTypeIF)
  {
    // quick check for a hierarchy root
    if(mdTypeIF instanceof MdClassDAOIF)
    {
      MdClassDAOIF mdClassIF = (MdClassDAOIF) mdTypeIF;

      if(mdClassIF.getSuperClass() == null && !root.hasChild(mdClassIF.definesType()))
      {
        Node node = new Node(mdClassIF);
        root.addChild(node);
      }
      else
      {
        // add one parent at a time (if not already added)
        List<? extends MdClassDAOIF> parents = mdClassIF.getSuperClasses();
        Node search = root;
        for(int i=parents.size()-1; i >= 0; i--)
        {
          MdClassDAOIF mdElement = parents.get(i);
          checkPublish(mdClassIF);
          search = search(search, mdElement);
        }
      }
    }
    else if(mdTypeIF instanceof MdEnumerationDAOIF)
    {
      // load all classes the MdEnumeration depends on.
      MdEnumerationDAOIF mdEnumerationIF = (MdEnumerationDAOIF) mdTypeIF;
      MdBusinessDAOIF masterMdBusiness = mdEnumerationIF.getMasterListMdBusinessDAO();
      insert(masterMdBusiness);

      mdEnumerations.add(mdEnumerationIF);
    }
    else
    {
      Node node = new Node(mdTypeIF);
      root.addChild(node);
    }
  }

  /**
   * Checks if a given class is published.
   *
   * @throws FacadeException if the class is not published.
   * @param mdTypeIF
   */
  private void checkPublish(MdTypeDAOIF mdTypeIF)
  {
    if(mdTypeIF instanceof MdClassDAOIF && !((MdClassDAOIF)mdTypeIF).isPublished())
    {
      String error = "Cannot generate Javascript for type ["+mdTypeIF.definesType()+"] because it is not published.";
      throw new FacadeException(error);
    }
  }

  /**
   *
   * @param search
   * @param mdTypeIF
   * @return
   */
  private Node search(Node search, MdTypeDAOIF mdTypeIF)
  {
    // add the child if it does not yet exist
    if(search.hasChild(mdTypeIF.definesType()))
    {
      return search.getChild(mdTypeIF.definesType());
    }
    else
    {
      Node node = new Node(mdTypeIF);
      search.addChild(node);
      return node;
    }
  }

  /**
   * Returns an ordered list of types to generate javascript for.
   *
   * @return
   */
  public List<MdTypeDAOIF> getOrderedTypes()
  {
    Map<String, MdTypeDAOIF> ordered = new LinkedHashMap<String, MdTypeDAOIF>();

    getOrderedTypes(root, ordered);

    // append the MdEnumerations at the end.
    for(MdEnumerationDAOIF mdEnumerationIF : mdEnumerations)
    {
      ordered.put(mdEnumerationIF.getId(), mdEnumerationIF);
    }

    return new LinkedList<MdTypeDAOIF>(ordered.values());
  }

  /**
   *
   *
   * @param parent
   * @param ordered
   */
  private void getOrderedTypes(Node parent, Map<String, MdTypeDAOIF> ordered)
  {
    for(Node child : parent.getChildren())
    {
      if(child != null)
      {
        if(child.getMdTypeIF() != null)
        {
          MdTypeDAOIF mdTypeIF = child.getMdTypeIF();
          ordered.put(mdTypeIF.getId(), mdTypeIF);
        }

        getOrderedTypes(child, ordered);
      }
    }
  }
}
