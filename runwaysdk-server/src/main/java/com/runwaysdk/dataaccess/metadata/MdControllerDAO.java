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
package com.runwaysdk.dataaccess.metadata;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.business.generation.ClassManager;
import com.runwaysdk.business.generation.GenerationFacade;
import com.runwaysdk.business.generation.GenerationUtil;
import com.runwaysdk.business.generation.GeneratorIF;
import com.runwaysdk.business.generation.JavaArtifactMdControllerCommand;
import com.runwaysdk.business.generation.ProviderFactory;
import com.runwaysdk.business.generation.facade.ControllerBaseGenerator;
import com.runwaysdk.business.generation.facade.ControllerGenerator;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdControllerInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdActionDAOIF;
import com.runwaysdk.dataaccess.MdControllerDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.attributes.entity.Attribute;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.util.FileIO;

public class MdControllerDAO extends MdTypeDAO implements MdControllerDAOIF
{
  /**
   *
   */
  private static final long serialVersionUID = -5280718879323820294L;

  /**
   * Flag denoting that this MdControllerDAO was auto defined for a MdEntity
   */
  private MdEntityDAOIF     mdEntity;

  /**
   * The default constructor, does not set any attributes
   */
  public MdControllerDAO()
  {
    super();

    this.mdEntity = null;
  }

  /**
   * Returns the signature of the metadata.
   * 
   * @return signature of the metadata.
   */
  public String getSignature()
  {
    String signature = super.getSignature() + " Actions[";

    boolean firstIteration = true;
    for (MdActionDAOIF mdActionDAOIF : this.getMdActionDAOsOrdered())
    {
      if (!firstIteration)
      {
        signature += ", ";
      }
      else
      {
        firstIteration = false;
      }
      signature += mdActionDAOIF.getSignature();
    }
    signature += "]";

    return signature;
  }

  /**
   * Constructs a BusinessDAO from the given hashtable of Attributes.
   * 
   * <br/>
   * <b>Precondition:</b> attributeMap != null <br/>
   * <b>Precondition:</b> type != null <br/>
   * <b>Precondition:</b> ObjectCache.isSubClass(type, Constants.MD_TYPE)
   * 
   * @param attributeMap
   * @param type
   * @param useCache
   */
  public MdControllerDAO(Map<String, Attribute> attributeMap, String type)
  {
    super(attributeMap, type);

    this.mdEntity = null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#create(java.util.Hashtable)
   */
  public MdControllerDAO create(Map<String, Attribute> attributeMap, String classType)
  {
    return new MdControllerDAO(attributeMap, MdControllerInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdControllerDAO getBusinessDAO()
  {
    return (MdControllerDAO) super.getBusinessDAO();
  }

  public static MdControllerDAO newInstance()
  {
    return (MdControllerDAO) BusinessDAO.newInstance(MdControllerInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.BusinessDAO#get(java.lang.String)
   */
  public static MdControllerDAOIF get(String id)
  {
    return (MdControllerDAOIF) BusinessDAO.get(id);
  }

  /**
   * Returns a command object that either creates or updates Java artifacts for
   * this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that either creates or updates Java artifacts for
   *         this type.
   */
  public Command getCreateUpdateJavaArtifactCommand(Connection conn)
  {
    Command command;

    if (this.isNew())
    {
      command = new JavaArtifactMdControllerCommand(this, JavaArtifactMdControllerCommand.Operation.CREATE, conn);
    }
    else
    {
      command = new JavaArtifactMdControllerCommand(this, JavaArtifactMdControllerCommand.Operation.UPDATE, conn);
    }

    return command;
  }

  /**
   * Returns a command object that deletes Java artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that deletes Java artifacts for this type.
   */
  public Command getDeleteJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdControllerCommand(this, JavaArtifactMdControllerCommand.Operation.DELETE, conn);
  }

  /**
   * Returns a command object that cleans Java artifacts for this type.
   * 
   * @param conn
   *          database connection object.
   * 
   * @return command object that cleans Java artifacts for this type.
   */
  public Command getCleanJavaArtifactCommand(Connection conn)
  {
    return new JavaArtifactMdControllerCommand(this, JavaArtifactMdControllerCommand.Operation.CLEAN, conn);
  }

  @Override
  public void writeFileArtifactsToDatabaseAndObjects(Connection conn)
  {
    if (!this.isSystemPackage())
    {
      List<GeneratorIF> generators = this.getGenerators();

      for (GeneratorIF gen : generators)
      {
        ControllerGenerator generator = (ControllerGenerator) gen;

        String classAttribute = generator.getClassAttribute();
        String classColumnName = generator.getClassColumnName();

        String sourceField = generator.getSourceAttribute();
        String sourceColumnName = generator.getSourceColumnName();

        // Get the name of the table on which the class and source fields column
        MdEntityDAOIF definedBy = (MdEntityDAOIF) this.getMdAttributeDAO(classAttribute).definedByClass();
        String tableName = definedBy.getTableName();

        byte[] bytes = GenerationFacade.getClassBytes((AbstractGenerator) generator);
        String source = GenerationFacade.getSource((AbstractGenerator) generator);

        if (bytes != null && source != null)
        {
          Database.updateClassAndSource(this.getId(), tableName, classColumnName, bytes, sourceColumnName, source, conn);

          // Only update the source.
          this.getAttribute(sourceField).setValue(source);
          // Mark the class artifacts as modified, so that their values will be
          // logged (if enabled)
          this.getAttribute(classAttribute).setModified(true);
        }
      }
    }
  }

  /**
   * Returns true if an attribute that stores source or class has been modified.
   * 
   * @return true if an attribute that stores source or class has been modified.
   */
  @Override
  public boolean javaArtifactsModifiedOnObject()
  {
    if (!this.isSystemPackage())
    {
      List<GeneratorIF> generators = this.getGenerators();

      for (GeneratorIF gen : generators)
      {
        ControllerGenerator generator = (ControllerGenerator) gen;

        String classField = generator.getClassAttribute();
        String sourceField = generator.getSourceAttribute();

        if (this.getAttribute(sourceField).isModified() || this.getAttribute(classField).isModified())
        {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public void writeJavaToFile()
  {
    List<GeneratorIF> generators = this.getGenerators();

    for (GeneratorIF gen : generators)
    {
      ControllerGenerator generator = (ControllerGenerator) gen;

      byte[] bytes = this.getBlob(generator.getClassAttribute());
      String source = this.getAttribute(generator.getSourceAttribute()).getValue();

      try
      {
        ClassManager.writeClasses(new File(generator.getClassFile()).getParentFile(), bytes);

        FileIO.write(generator.getPath(), source);
      }
      catch (IOException e)
      {
        throw new SystemException(e);
      }
    }
  }

  public List<GeneratorIF> getGenerators()
  {
    List<GeneratorIF> list = new LinkedList<GeneratorIF>();

    // Dont generate reserved types
    if (GenerationUtil.isReservedType(this))
    {
      return list;
    }

    list.add(new ProviderFactory().getControllerStubGenerator(this));
    list.add(new ControllerBaseGenerator(this));

    return list;
  }

  public List<MdActionDAOIF> getMdActionDAOs()
  {
    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.CONTROLLER_ACTION.getType());
    List<MdActionDAOIF> list = new LinkedList<MdActionDAOIF>();

    for (RelationshipDAOIF relationship : relationships)
    {
      MdActionDAOIF mdAction = (MdActionDAOIF) relationship.getChild();

      list.add(mdAction);
    }

    return list;
  }

  public List<MdActionDAOIF> getMdActionDAOsOrdered()
  {
    List<MdActionDAOIF> actions = this.getMdActionDAOs();

    // Sort the MdParamters into ascending order by the parameter order
    Collections.sort(actions, new Comparator<MdActionDAOIF>()
    {
      public int compare(MdActionDAOIF p1, MdActionDAOIF p2)
      {
        return p1.getName().compareTo(p2.getName());
      }
    });

    return actions;
  }

  public MdActionDAOIF definesMdAction(String actionName)
  {
    List<MdActionDAOIF> actions = this.getMdActionDAOs();

    for (MdActionDAOIF mdActionIF : actions)
    {
      if (mdActionIF.getName().equals(actionName))
      {
        return mdActionIF;
      }
    }

    return null;
  }

  @Override
  public String save(boolean validateRequired)
  {   
    boolean isAppliedToDB = this.isAppliedToDB();
    
    String id = super.save(validateRequired);
   
    if (!this.isNew() || isAppliedToDB)
    {
      Attribute keyAttribute = this.getAttribute(MdClassInfo.KEY);
      
      if (keyAttribute.isModified())
      {
        List<MdActionDAOIF> mdActions = this.getMdActionDAOs();

        for (MdActionDAOIF mdActionDAOIF : mdActions)
        {
          // The apply method will update the key
          (mdActionDAOIF.getBusinessDAO()).apply();
        }
      }
    }
   
    return id;
  }
  
  @Override
  public void delete(boolean businessContext)
  {
    // 1. Delete the MdActions defined by this MdControllerDAO
    List<RelationshipDAOIF> relationships = this.getChildren(RelationshipTypes.CONTROLLER_ACTION.getType());

    for (RelationshipDAOIF relationship : relationships)
    {
      MdActionDAO mdAction = ( (MdActionDAOIF) relationship.getChild() ).getBusinessDAO();
      mdAction.delete(businessContext);
    }

    super.delete(businessContext);
  }

  public static MdControllerDAOIF getMdControllerDAO(String type)
  {
    return ObjectCache.getMdControllerDAO(type);
  }

  public MdEntityDAOIF getMdEntity()
  {
    return mdEntity;
  }

  public void setMdEntity(MdEntityDAOIF mdEntity)
  {
    this.mdEntity = mdEntity;
  }

}
