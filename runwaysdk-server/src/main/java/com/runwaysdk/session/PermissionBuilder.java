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
package com.runwaysdk.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import com.runwaysdk.business.rbac.ActorDAOIF;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.OperationManager;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.io.FileWriteException;
import com.runwaysdk.util.FileIO;

public final class PermissionBuilder extends AbstractPermissionBuilder implements PermissionBuilderIF
{
  public PermissionBuilder(ActorDAOIF actor)
  {
    super(actor);
  }

  public PermissionBuilder(ActorDAOIF actor, PermissionManager manager)
  {
    super(actor, manager);
  }

  /**
   * Appends the permission set of a given user onto the existing permissions
   * 
   * @param user
   *          The user to append the permissions of
   */
  public PermissionMap build()
  {
    File file = new File(this.getSerializedFilePath());

    if (file.exists())
    {
      try
      {
        return this.deserialize(file);
      }
      catch (Exception e)
      {
        return this.buildFromMemory();
      }
    }

    return this.buildFromMemory();
  }

  private PermissionMap buildFromMemory()
  {
    // Get a list of all objects the user has permissions on
    HashMap<String, RelationshipDAOIF> permissions = this.getManager().getPermissions(this.getActor());
    Set<String> keySet = permissions.keySet();

    for (String key : keySet)
    {
      RelationshipDAOIF reference = permissions.get(key);

      this.addPermissions(this.getActor(), reference);
    }

    return this.getMap();
  }

  private PermissionMap deserialize(File file) throws IOException, FileNotFoundException, ClassNotFoundException
  {
    ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));

    try
    {
      // deserialize the List
      return (PermissionMap) input.readObject();
    }
    finally
    {
      input.close();
    }
  }

  private void addPermissions(ActorDAOIF actor, RelationshipDAOIF reference)
  {
    MetadataDAOIF metadata = (MetadataDAOIF) reference.getChild();
    Set<Operation> operations = OperationManager.getOperations(reference);

    this.addPermissions(actor, metadata, operations);
  }

  private void addPermissions(ActorDAOIF actor, MetadataDAOIF metadataDAO, Set<Operation> operations)
  {
    Stack<MetadataDAOIF> stack = new Stack<MetadataDAOIF>();
    stack.push(metadataDAO);

    // Get a list of all operations a user has permissions for each object

    while (!stack.isEmpty())
    {
      MetadataDAOIF metadata = stack.pop();

      if (metadata != null)
      {
        String key = metadata.getPermissionKey();

        map.insert(key, operations);

        if (operations.contains(Operation.READ_ALL) || operations.contains(Operation.WRITE_ALL))
        {
          this.addAttributePermisisons(actor, metadata, operations);
        }

        // Give the permissions to all of the children in "child's" inheritance
        // hierarchy
        if (metadata instanceof MdClassDAOIF)
        {
          MdClassDAOIF mdClass = (MdClassDAOIF) metadata;

          // Get all of the SubEntities and ensure that the parent excluded from
          // the list.
          List<? extends MdClassDAOIF> subClasses = mdClass.getSubClasses();
          subClasses.remove(metadata);

          for (MdClassDAOIF subClass : subClasses)
          {
            stack.push(subClass);
          }
        }
        else if (metadata instanceof MdClassDimensionDAOIF)
        {
          MdClassDimensionDAOIF mdClassDimension = (MdClassDimensionDAOIF) metadata;
          MdClassDAOIF mdClass = mdClassDimension.definingMdClass();
          MdDimensionDAOIF mdDimension = mdClassDimension.definingMdDimension();

          List<? extends MdClassDAOIF> subClasses = mdClass.getSubClasses();
          subClasses.remove(metadata);

          for (MdClassDAOIF subClass : subClasses)
          {
            stack.push(subClass.getMdClassDimension(mdDimension));
          }
        }
      }
    }
  }

  private void addAttributePermisisons(ActorDAOIF actor, MetadataDAOIF metadata, Set<Operation> _operations)
  {
    Set<Operation> operations = new TreeSet<Operation>();

    if (_operations.contains(Operation.READ_ALL))
    {
      operations.add(Operation.READ);
    }

    if (_operations.contains(Operation.WRITE_ALL))
    {
      operations.add(Operation.WRITE);
    }

    if (metadata instanceof MdClassDAOIF)
    {
      MdClassDAOIF mdClass = (MdClassDAOIF) metadata;

      List<? extends MdAttributeDAOIF> mdAttributes = mdClass.getAllDefinedMdAttributes();

      for (MdAttributeDAOIF mdAttribute : mdAttributes)
      {
        // IMPORTANT: The desired behavior is that specific attribute
        // permissions have a higher precedence than the READ_ALL or WRITE_ALL
        // permissions. As such we need to ensure that the individual attribute
        // permissions are loaded before the ALL permissions are assigned to an
        // attribute. This is due to the fact that an ordering dependency occurs
        // when specific attribute permissions negate the READ_ALL or WRITE_ALL
        // permission
        RelationshipDAOIF attributePermissions = this.getManager().getPermissions(actor, mdAttribute);

        if (attributePermissions != null)
        {
          this.addPermissions(actor, attributePermissions);
        }

        map.insert(mdAttribute.getPermissionKey(), operations);
      }
    }
    else if (metadata instanceof MdClassDimensionDAOIF)
    {
      MdClassDimensionDAOIF mdClassDimension = (MdClassDimensionDAOIF) metadata;
      MdClassDAOIF mdClass = mdClassDimension.definingMdClass();
      MdDimensionDAOIF mdDimension = mdClassDimension.definingMdDimension();

      List<? extends MdAttributeDAOIF> mdAttributes = mdClass.getAllDefinedMdAttributes();

      for (MdAttributeDAOIF mdAttribute : mdAttributes)
      {
        MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);

        if (mdAttributeDimension != null)
        {

          // IMPORTANT: The desired behavior is that specific attribute
          // permissions have a higher precedence than the READ_ALL or WRITE_ALL
          // permissions. As such we need to ensure that the individual
          // attribute permissions are loaded before the ALL permissions are
          // assigned to an attribute. This is due to the fact that an ordering
          // dependency occurs when specific attribute permissions negate the
          // READ_ALL or WRITE_ALL permission
          RelationshipDAOIF attributePermissions = this.getManager().getPermissions(actor, mdAttributeDimension);

          if (attributePermissions != null)
          {
            this.addPermissions(actor, attributePermissions);
          }

          map.insert(mdAttributeDimension.getPermissionKey(), operations);
        }
      }
    }
  }

  public void serialize()
  {
    File file = new File(this.getSerializedFilePath());

    try
    {
      file.getParentFile().mkdirs();
      
      ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

      try
      {
        output.writeObject(this.buildFromMemory());
      }
      finally
      {
        output.close();
      }
    }
    catch (IOException ex)
    {
      throw new FileWriteException(file, ex);
    }
  }
  
  public void cleanup()
  {
    File file = new File(this.getSerializedFilePath());
    
    if(file.exists())
    {
      try
      {
        FileIO.deleteFile(file);      
      }
      catch(IOException e)
      {
        throw new FileWriteException(file, e);
      }
    }
  }

  private String getSerializedFilePath()
  {
    return LocalProperties.getPermissionCacheDirectory() + File.separator + this.getActor().getId() + ".ser";
  }
}
