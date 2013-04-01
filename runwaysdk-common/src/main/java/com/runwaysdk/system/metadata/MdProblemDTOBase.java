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
package com.runwaysdk.system.metadata;

@com.runwaysdk.business.ClassSignature(hash = -409633334)
public abstract class MdProblemDTOBase extends com.runwaysdk.system.metadata.MdNotificationDTO
{
  public final static String CLASS = "com.runwaysdk.system.metadata.MdProblem";
  private static final long serialVersionUID = -409633334;
  
  protected MdProblemDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected MdProblemDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String SUPERMDPROBLEM = "superMdProblem";
  public com.runwaysdk.system.metadata.MdProblemDTO getSuperMdProblem()
  {
    if(getValue(SUPERMDPROBLEM) == null || getValue(SUPERMDPROBLEM).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdProblemDTO.get(getRequest(), getValue(SUPERMDPROBLEM));
    }
  }
  
  public String getSuperMdProblemId()
  {
    return getValue(SUPERMDPROBLEM);
  }
  
  public void setSuperMdProblem(com.runwaysdk.system.metadata.MdProblemDTO value)
  {
    if(value == null)
    {
      setValue(SUPERMDPROBLEM, "");
    }
    else
    {
      setValue(SUPERMDPROBLEM, value.getId());
    }
  }
  
  public boolean isSuperMdProblemWritable()
  {
    return isWritable(SUPERMDPROBLEM);
  }
  
  public boolean isSuperMdProblemReadable()
  {
    return isReadable(SUPERMDPROBLEM);
  }
  
  public boolean isSuperMdProblemModified()
  {
    return isModified(SUPERMDPROBLEM);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getSuperMdProblemMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(SUPERMDPROBLEM).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdProblemDTO> getAllChildProblems()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdProblemDTO>) getRequest().getChildren(this.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdProblemDTO> getAllChildProblems(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdProblemDTO>) clientRequestIF.getChildren(id, com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ProblemInheritanceDTO> getAllChildProblemsRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ProblemInheritanceDTO>) getRequest().getChildRelationships(this.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ProblemInheritanceDTO> getAllChildProblemsRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ProblemInheritanceDTO>) clientRequestIF.getChildRelationships(id, com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ProblemInheritanceDTO addChildProblems(com.runwaysdk.system.metadata.MdProblemDTO child)
  {
    return (com.runwaysdk.system.metadata.ProblemInheritanceDTO) getRequest().addChild(this.getId(), child.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ProblemInheritanceDTO addChildProblems(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdProblemDTO child)
  {
    return (com.runwaysdk.system.metadata.ProblemInheritanceDTO) clientRequestIF.addChild(id, child.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public void removeChildProblems(com.runwaysdk.system.metadata.ProblemInheritanceDTO relationship)
  {
    getRequest().deleteChild(relationship.getId());
  }
  
  public static void removeChildProblems(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ProblemInheritanceDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getId());
  }
  
  public void removeAllChildProblems()
  {
    getRequest().deleteChildren(this.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public static void removeAllChildProblems(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteChildren(id, com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.MdProblemDTO> getAllParentProblem()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdProblemDTO>) getRequest().getParents(this.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.MdProblemDTO> getAllParentProblem(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.MdProblemDTO>) clientRequestIF.getParents(id, com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.system.metadata.ProblemInheritanceDTO> getAllParentProblemRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ProblemInheritanceDTO>) getRequest().getParentRelationships(this.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.system.metadata.ProblemInheritanceDTO> getAllParentProblemRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    return (java.util.List<? extends com.runwaysdk.system.metadata.ProblemInheritanceDTO>) clientRequestIF.getParentRelationships(id, com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public com.runwaysdk.system.metadata.ProblemInheritanceDTO addParentProblem(com.runwaysdk.system.metadata.MdProblemDTO parent)
  {
    return (com.runwaysdk.system.metadata.ProblemInheritanceDTO) getRequest().addParent(parent.getId(), this.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.ProblemInheritanceDTO addParentProblem(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id, com.runwaysdk.system.metadata.MdProblemDTO parent)
  {
    return (com.runwaysdk.system.metadata.ProblemInheritanceDTO) clientRequestIF.addParent(parent.getId(), id, com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public void removeParentProblem(com.runwaysdk.system.metadata.ProblemInheritanceDTO relationship)
  {
    getRequest().deleteParent(relationship.getId());
  }
  
  public static void removeParentProblem(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.system.metadata.ProblemInheritanceDTO relationship)
  {
    clientRequestIF.deleteParent(relationship.getId());
  }
  
  public void removeAllParentProblem()
  {
    getRequest().deleteParents(this.getId(), com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public static void removeAllParentProblem(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String id)
  {
    clientRequestIF.deleteParents(id, com.runwaysdk.system.metadata.ProblemInheritanceDTO.CLASS);
  }
  
  public static com.runwaysdk.system.metadata.MdProblemDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(id);
    
    return (com.runwaysdk.system.metadata.MdProblemDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createBusiness(this);
    }
    else
    {
      getRequest().update(this);
    }
  }
  public void delete()
  {
    getRequest().delete(this.getId());
  }
  
  public static com.runwaysdk.system.metadata.MdProblemQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.system.metadata.MdProblemQueryDTO) clientRequest.getAllInstances(com.runwaysdk.system.metadata.MdProblemDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdProblemDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdProblemDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdProblemDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.system.metadata.MdProblemDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String id)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{id};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.metadata.MdProblemDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.system.metadata.MdProblemDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
