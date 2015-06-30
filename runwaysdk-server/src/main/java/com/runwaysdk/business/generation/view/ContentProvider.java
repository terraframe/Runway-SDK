/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.business.generation.view;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.SystemException;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeLocalDAOIF;
import com.runwaysdk.dataaccess.MdAttributeStructDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.util.FileIO;

/**
 * Concrete implementation of {@link ContentProviderIF}
 * 
 * @author Justin Smethie
 */
public class ContentProvider implements ContentProviderIF
{
  /**
   * Synchronized list of the registered listeners
   */
  private Collection<ContentListener> listeners;

  /**
   * Constructs a {@link ContentProvider} with an empty list of registered
   * {@link ContentListener}
   */
  public ContentProvider()
  {
    this.listeners = Collections.synchronizedCollection(new LinkedList<ContentListener>());
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.runwaysdk.business.generation.view.ContentProviderIF#
   * registerContentListener
   * (com.runwaysdk.business.generation.view.ContentListener)
   */
  public void registerContentListener(ContentListener listener)
  {
    listeners.add(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @seecom.runwaysdk.business.generation.view.ContentProviderIF#
   * unregisterContentListener
   * (com.runwaysdk.business.generation.view.ContentListener)
   */
  public void unregisterContentListener(ContentListener listener)
  {
    listeners.remove(listener);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.ContentProviderIF#generateContent ()
   */
  public void generateContent()
  {
    this.generateContent(false);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.ContentProviderIF#generateContent
   * (boolean)
   */
  public void generateContent(boolean forceRegeneration)
  {
    for (ContentListener listener : listeners)
    {
      if (!listener.alreadyExists() || forceRegeneration)
      {
        MdEntityDAOIF mdEntity = listener.getMdEntity();

        // Fire header events
        listener.header();
        listener.afterHeader();

        // Fire open form events
        listener.beforeForm();
        listener.form();
        listener.afterForm();

        // Fire open Component event
        listener.beforeComponent();
        listener.component();
        listener.afterComponent();

        // Fire relationship events
        if (mdEntity instanceof MdRelationshipDAOIF)
        {
          MdRelationshipDAOIF mdRelationship = (MdRelationshipDAOIF) mdEntity;

          RelationshipEventIF relationshipEvent = new RelationshipEvent(mdRelationship.getParentMdBusiness(), mdRelationship.getChildMdBusiness());

          listener.parent(relationshipEvent);
          listener.child(relationshipEvent);
        }

        // Fire the attributes events
        listener.beforeAttributes();

        for (MdEntityDAOIF superEntity : mdEntity.getSuperClasses())
        {
          fireAttributeEvents(listener, superEntity);
        }

        listener.afterAttributes();

        // Fire the close component events
        listener.beforeCloseComponent();
        listener.closeComponent();
        listener.afterCloseComponent();

        // Fire the close form events
        listener.beforeCloseForm();
        listener.closeForm();
        listener.afterCloseForm();

        listener.footer();
      }
    }
  }

  /**
   * Fires all of the attribute events for a {@link MdEntityDAOIF} to a single
   * {@link ContentListener}. Uses recursion to handle the
   * {@link MdAttributeStructDAOIF} events.
   * 
   * @param listener
   *          {@link ContentListener} on which to fire events
   * @param superEntity
   *          {@link MdEntityDAOIF} to retrieve {@link MdAttributeDAOIF} from.
   */
  private void fireAttributeEvents(ContentListener listener, MdEntityDAOIF superEntity)
  {
    for (MdAttributeDAOIF mdAttribute : superEntity.definesAttributesOrdered())
    {
      AttributeEventIF attributeEvent = new AttributeEvent(mdAttribute);

      if (mdAttribute instanceof MdAttributeLocalDAOIF)
      {
        // Fire the individual attribute events
        listener.beforeAttribute(attributeEvent);
        listener.attribute(attributeEvent);
        listener.afterAttribute(attributeEvent);
      }
      else if (mdAttribute instanceof MdAttributeStructDAOIF)
      {
        MdAttributeStructDAOIF mdStruct = (MdAttributeStructDAOIF) mdAttribute;

        listener.beforeStructAttribute(attributeEvent);

        fireAttributeEvents(listener, mdStruct.getMdStructDAOIF());

        listener.afterStructAttribute(attributeEvent);
      }
      else
      {
        // Fire the individual attribute events
        listener.beforeAttribute(attributeEvent);
        listener.attribute(attributeEvent);
        listener.afterAttribute(attributeEvent);
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.ContentProviderIF#getGeneratedFilePaths
   * ()
   */
  public List<String> getGeneratedFilePaths()
  {
    List<String> list = new LinkedList<String>();

    for (ContentListener listener : listeners)
    {
      list.add(listener.getPath());
    }

    return list;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.runwaysdk.business.generation.view.ContentProviderIF#deleteContent()
   */
  public void deleteContent()
  {
    try
    {
      for (String path : this.getGeneratedFilePaths())
      {
        File file = new File(path);

        if (file.exists() && file.isFile())
        {
          FileIO.deleteFile(file);
        }
      }
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  public HashMap<String, String> getContent()
  {
    HashMap<String, String> map = new HashMap<String, String>();

    return map;
  }

  @Override
  public void backupContent()
  {
    try
    {
      for (String path : this.getGeneratedFilePaths())
      {
        File source = new File(path);

        if (source.exists())
        {
          File dest = new File(path + ".backup");
          FileIO.write(dest, FileIO.readString(source));
        }
      }
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }

  
  @Override
  public void deleteBackup()
  {
    try
    {
      for (String path : this.getGeneratedFilePaths())
      {
        File source = new File(path  + ".backup");
        
        if (source.exists())
        {
          FileIO.deleteFile(source);
        }
      }
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }
  
  @Override
  public void reloadContent()
  {
    try
    {
      for (String path : this.getGeneratedFilePaths())
      {
        File content = new File(path + ".backup");

        if (content.exists())
        {
          FileIO.write(new File(path), FileIO.readString(content));
        }
      }
    }
    catch (IOException e)
    {
      throw new SystemException(e.getMessage());
    }
  }
}
