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
package com.runwaysdk.system.ontology;

@com.runwaysdk.business.ClassSignature(hash = -335631147)
public abstract class TermUtilDTOBase extends com.runwaysdk.business.UtilDTO
{
  public final static String CLASS = "com.runwaysdk.system.ontology.TermUtil";
  private static final long serialVersionUID = -335631147;
  
  protected TermUtilDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String ID = "id";
  public static final com.runwaysdk.business.RelationshipDTO addAndRemoveLink(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String childId, java.lang.String oldParentId, java.lang.String oldRelType, java.lang.String newParentId, java.lang.String newRelType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{childId, oldParentId, oldRelType, newParentId, newRelType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "addAndRemoveLink", _declaredTypes);
    return (com.runwaysdk.business.RelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.RelationshipDTO addLink(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String childId, java.lang.String parentId, java.lang.String relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{childId, parentId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "addLink", _declaredTypes);
    return (com.runwaysdk.business.RelationshipDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final void exportTerm(com.runwaysdk.constants.ClientRequestIF clientRequest, java.io.OutputStream outputStream, java.lang.String parentId, java.lang.Boolean exportParent, com.runwaysdk.system.ontology.io.TermFileFormatDTO format)
  {
    String[] _declaredTypes = new String[]{"java.io.OutputStream", "java.lang.String", "java.lang.Boolean", "com.runwaysdk.system.ontology.io.TermFileFormat"};
    Object[] _parameters = new Object[]{outputStream, parentId, exportParent, format};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "exportTerm", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ontology.TermDTO[] getAllAncestors(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String[] relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getAllAncestors", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ontology.TermDTO[] getAllDescendants(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String[] relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getAllDescendants", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ontology.TermAndRelDTO[] getDirectAncestors(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String[] relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getDirectAncestors", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermAndRelDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final com.runwaysdk.business.ontology.TermAndRelDTO[] getDirectDescendants(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String termId, java.lang.String[] relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "[Ljava.lang.String;"};
    Object[] _parameters = new Object[]{termId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getDirectDescendants", _declaredTypes);
    return (com.runwaysdk.business.ontology.TermAndRelDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.String getTimestamp(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "getTimestamp", _declaredTypes);
    return (java.lang.String) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final void removeLink(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String childId, java.lang.String parentId, java.lang.String relationshipType)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.String", "java.lang.String"};
    Object[] _parameters = new Object[]{childId, parentId, relationshipType};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.system.ontology.TermUtilDTO.CLASS, "removeLink", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static TermUtilDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String id)
  {
    com.runwaysdk.business.UtilDTO dto = (com.runwaysdk.business.UtilDTO)clientRequest.get(id);
    
    return (TermUtilDTO) dto;
  }
  
  public void apply()
  {
    if(isNewInstance())
    {
      getRequest().createSessionComponent(this);
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
  
}
