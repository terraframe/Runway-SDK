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
package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = -685695045)
public abstract class TestClassDTOBase extends com.runwaysdk.business.BusinessDTO implements com.runwaysdk.generation.loader.
{
  public final static String CLASS = "com.runwaysdk.jstest.TestClass";
  private static final long serialVersionUID = -685695045;
  
  protected TestClassDTOBase(com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(clientRequest);
  }
  
  /**
  * Copy Constructor: Duplicates the values and attributes of the given BusinessDTO into a new DTO.
  * 
  * @param businessDTO The BusinessDTO to duplicate
  * @param clientRequest The clientRequest this DTO should use to communicate with the server.
  */
  protected TestClassDTOBase(com.runwaysdk.business.BusinessDTO businessDTO, com.runwaysdk.constants.ClientRequestIF clientRequest)
  {
    super(businessDTO, clientRequest);
  }
  
  protected java.lang.String getDeclaredType()
  {
    return CLASS;
  }
  
  public static java.lang.String CELLPHONE = "cellPhone";
  public static java.lang.String CREATEDATE = "createDate";
  public static java.lang.String CREATEDBY = "createdBy";
  public static java.lang.String ENTITYDOMAIN = "entityDomain";
  public static java.lang.String HOMEPHONE = "homePhone";
  public static java.lang.String OID = "oid";
  public static java.lang.String KEYNAME = "keyName";
  public static java.lang.String LASTUPDATEDATE = "lastUpdateDate";
  public static java.lang.String LASTUPDATEDBY = "lastUpdatedBy";
  public static java.lang.String LOCKEDBY = "lockedBy";
  public static java.lang.String MULTIPLESTATE = "multipleState";
  public static java.lang.String OWNER = "owner";
  public static java.lang.String SEQ = "seq";
  public static java.lang.String SINGLESTATE = "singleState";
  public static java.lang.String SITEMASTER = "siteMaster";
  public static java.lang.String TESTBLOB = "testBlob";
  public static java.lang.String TESTBOOLEAN = "testBoolean";
  public static java.lang.String TESTCHARACTER = "testCharacter";
  public static java.lang.String TESTDATE = "testDate";
  public static java.lang.String TESTDATETIME = "testDateTime";
  public static java.lang.String TESTDECIMAL = "testDecimal";
  public static java.lang.String TESTDOUBLE = "testDouble";
  public static java.lang.String TESTFLOAT = "testFloat";
  public static java.lang.String TESTHASH = "testHash";
  public static java.lang.String TESTINTEGER = "testInteger";
  public static java.lang.String TESTLONG = "testLong";
  public static java.lang.String TESTREFERENCEOBJECT = "testReferenceObject";
  public static java.lang.String TESTSYMMETRIC = "testSymmetric";
  public static java.lang.String TESTTEXT = "testText";
  public static java.lang.String TESTTIME = "testTime";
  public static java.lang.String TYPE = "type";
  public static java.lang.String WORKPHONE = "workPhone";
  public com.runwaysdk.system.PhoneNumberDTO getCellPhone()
  {
    return (com.runwaysdk.system.PhoneNumberDTO) this.getAttributeStructDTO(CELLPHONE).getStructDTO();
  }
  
  public boolean isCellPhoneWritable()
  {
    return isWritable(CELLPHONE);
  }
  
  public boolean isCellPhoneReadable()
  {
    return isReadable(CELLPHONE);
  }
  
  public boolean isCellPhoneModified()
  {
    return isModified(CELLPHONE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeStructMdDTO getCellPhoneMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeStructMdDTO) getAttributeDTO(CELLPHONE).getAttributeMdDTO();
  }
  
  public java.util.Date getCreateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(CREATEDATE));
  }
  
  public boolean isCreateDateWritable()
  {
    return isWritable(CREATEDATE);
  }
  
  public boolean isCreateDateReadable()
  {
    return isReadable(CREATEDATE);
  }
  
  public boolean isCreateDateModified()
  {
    return isModified(CREATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getCreateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(CREATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getCreatedBy()
  {
    if(getValue(CREATEDBY) == null || getValue(CREATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(CREATEDBY));
    }
  }
  
  public String getCreatedById()
  {
    return getValue(CREATEDBY);
  }
  
  public boolean isCreatedByWritable()
  {
    return isWritable(CREATEDBY);
  }
  
  public boolean isCreatedByReadable()
  {
    return isReadable(CREATEDBY);
  }
  
  public boolean isCreatedByModified()
  {
    return isModified(CREATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getCreatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(CREATEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.metadata.MdDomainDTO getEntityDomain()
  {
    if(getValue(ENTITYDOMAIN) == null || getValue(ENTITYDOMAIN).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.metadata.MdDomainDTO.get(getRequest(), getValue(ENTITYDOMAIN));
    }
  }
  
  public String getEntityDomainId()
  {
    return getValue(ENTITYDOMAIN);
  }
  
  public void setEntityDomain(com.runwaysdk.system.metadata.MdDomainDTO value)
  {
    if(value == null)
    {
      setValue(ENTITYDOMAIN, "");
    }
    else
    {
      setValue(ENTITYDOMAIN, value.getOid());
    }
  }
  
  public boolean isEntityDomainWritable()
  {
    return isWritable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainReadable()
  {
    return isReadable(ENTITYDOMAIN);
  }
  
  public boolean isEntityDomainModified()
  {
    return isModified(ENTITYDOMAIN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getEntityDomainMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(ENTITYDOMAIN).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.PhoneNumberDTO getHomePhone()
  {
    return (com.runwaysdk.system.PhoneNumberDTO) this.getAttributeStructDTO(HOMEPHONE).getStructDTO();
  }
  
  public boolean isHomePhoneWritable()
  {
    return isWritable(HOMEPHONE);
  }
  
  public boolean isHomePhoneReadable()
  {
    return isReadable(HOMEPHONE);
  }
  
  public boolean isHomePhoneModified()
  {
    return isModified(HOMEPHONE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeStructMdDTO getHomePhoneMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeStructMdDTO) getAttributeDTO(HOMEPHONE).getAttributeMdDTO();
  }
  
  public String getKeyName()
  {
    return getValue(KEYNAME);
  }
  
  public void setKeyName(String value)
  {
    if(value == null)
    {
      setValue(KEYNAME, "");
    }
    else
    {
      setValue(KEYNAME, value);
    }
  }
  
  public boolean isKeyNameWritable()
  {
    return isWritable(KEYNAME);
  }
  
  public boolean isKeyNameReadable()
  {
    return isReadable(KEYNAME);
  }
  
  public boolean isKeyNameModified()
  {
    return isModified(KEYNAME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getKeyNameMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(KEYNAME).getAttributeMdDTO();
  }
  
  public java.util.Date getLastUpdateDate()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(LASTUPDATEDATE));
  }
  
  public boolean isLastUpdateDateWritable()
  {
    return isWritable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateReadable()
  {
    return isReadable(LASTUPDATEDATE);
  }
  
  public boolean isLastUpdateDateModified()
  {
    return isModified(LASTUPDATEDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getLastUpdateDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(LASTUPDATEDATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.SingleActorDTO getLastUpdatedBy()
  {
    if(getValue(LASTUPDATEDBY) == null || getValue(LASTUPDATEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.SingleActorDTO.get(getRequest(), getValue(LASTUPDATEDBY));
    }
  }
  
  public String getLastUpdatedById()
  {
    return getValue(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByWritable()
  {
    return isWritable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByReadable()
  {
    return isReadable(LASTUPDATEDBY);
  }
  
  public boolean isLastUpdatedByModified()
  {
    return isModified(LASTUPDATEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLastUpdatedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LASTUPDATEDBY).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.UsersDTO getLockedBy()
  {
    if(getValue(LOCKEDBY) == null || getValue(LOCKEDBY).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.UsersDTO.get(getRequest(), getValue(LOCKEDBY));
    }
  }
  
  public String getLockedById()
  {
    return getValue(LOCKEDBY);
  }
  
  public boolean isLockedByWritable()
  {
    return isWritable(LOCKEDBY);
  }
  
  public boolean isLockedByReadable()
  {
    return isReadable(LOCKEDBY);
  }
  
  public boolean isLockedByModified()
  {
    return isModified(LOCKEDBY);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getLockedByMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(LOCKEDBY).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.jstest.StatesDTO> getMultipleState()
  {
    return (java.util.List<com.runwaysdk.jstest.StatesDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.jstest.StatesDTO.CLASS, getEnumNames(MULTIPLESTATE));
  }
  
  public java.util.List<String> getMultipleStateEnumNames()
  {
    return getEnumNames(MULTIPLESTATE);
  }
  
  public void addMultipleState(com.runwaysdk.jstest.StatesDTO enumDTO)
  {
    addEnumItem(MULTIPLESTATE, enumDTO.toString());
  }
  
  public void removeMultipleState(com.runwaysdk.jstest.StatesDTO enumDTO)
  {
    removeEnumItem(MULTIPLESTATE, enumDTO.toString());
  }
  
  public void clearMultipleState()
  {
    clearEnum(MULTIPLESTATE);
  }
  
  public boolean isMultipleStateWritable()
  {
    return isWritable(MULTIPLESTATE);
  }
  
  public boolean isMultipleStateReadable()
  {
    return isReadable(MULTIPLESTATE);
  }
  
  public boolean isMultipleStateModified()
  {
    return isModified(MULTIPLESTATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getMultipleStateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(MULTIPLESTATE).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.ActorDTO getOwner()
  {
    if(getValue(OWNER) == null || getValue(OWNER).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.system.ActorDTO.get(getRequest(), getValue(OWNER));
    }
  }
  
  public String getOwnerId()
  {
    return getValue(OWNER);
  }
  
  public void setOwner(com.runwaysdk.system.ActorDTO value)
  {
    if(value == null)
    {
      setValue(OWNER, "");
    }
    else
    {
      setValue(OWNER, value.getOid());
    }
  }
  
  public boolean isOwnerWritable()
  {
    return isWritable(OWNER);
  }
  
  public boolean isOwnerReadable()
  {
    return isReadable(OWNER);
  }
  
  public boolean isOwnerModified()
  {
    return isModified(OWNER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getOwnerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(OWNER).getAttributeMdDTO();
  }
  
  public Long getSeq()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(SEQ));
  }
  
  public boolean isSeqWritable()
  {
    return isWritable(SEQ);
  }
  
  public boolean isSeqReadable()
  {
    return isReadable(SEQ);
  }
  
  public boolean isSeqModified()
  {
    return isModified(SEQ);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getSeqMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(SEQ).getAttributeMdDTO();
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<com.runwaysdk.jstest.StatesDTO> getSingleState()
  {
    return (java.util.List<com.runwaysdk.jstest.StatesDTO>) com.runwaysdk.transport.conversion.ConversionFacade.convertEnumDTOsFromEnumNames(getRequest(), com.runwaysdk.jstest.StatesDTO.CLASS, getEnumNames(SINGLESTATE));
  }
  
  public java.util.List<String> getSingleStateEnumNames()
  {
    return getEnumNames(SINGLESTATE);
  }
  
  public void addSingleState(com.runwaysdk.jstest.StatesDTO enumDTO)
  {
    addEnumItem(SINGLESTATE, enumDTO.toString());
  }
  
  public void removeSingleState(com.runwaysdk.jstest.StatesDTO enumDTO)
  {
    removeEnumItem(SINGLESTATE, enumDTO.toString());
  }
  
  public void clearSingleState()
  {
    clearEnum(SINGLESTATE);
  }
  
  public boolean isSingleStateWritable()
  {
    return isWritable(SINGLESTATE);
  }
  
  public boolean isSingleStateReadable()
  {
    return isReadable(SINGLESTATE);
  }
  
  public boolean isSingleStateModified()
  {
    return isModified(SINGLESTATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO getSingleStateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO) getAttributeDTO(SINGLESTATE).getAttributeMdDTO();
  }
  
  public String getSiteMaster()
  {
    return getValue(SITEMASTER);
  }
  
  public boolean isSiteMasterWritable()
  {
    return isWritable(SITEMASTER);
  }
  
  public boolean isSiteMasterReadable()
  {
    return isReadable(SITEMASTER);
  }
  
  public boolean isSiteMasterModified()
  {
    return isModified(SITEMASTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getSiteMasterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(SITEMASTER).getAttributeMdDTO();
  }
  
  public byte[] getTestBlob()
  {
    return super.getBlob(TESTBLOB);
  }
  
  public void setTestBlob(byte[] bytes)
  {
    super.setBlob(TESTBLOB, bytes);
  }
  
  public boolean isTestBlobWritable()
  {
    return isWritable(TESTBLOB);
  }
  
  public boolean isTestBlobReadable()
  {
    return isReadable(TESTBLOB);
  }
  
  public boolean isTestBlobModified()
  {
    return isModified(TESTBLOB);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBlobMdDTO getTestBlobMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBlobMdDTO) getAttributeDTO(TESTBLOB).getAttributeMdDTO();
  }
  
  public Boolean getTestBoolean()
  {
    return com.runwaysdk.constants.MdAttributeBooleanUtil.getTypeSafeValue(getValue(TESTBOOLEAN));
  }
  
  public void setTestBoolean(Boolean value)
  {
    if(value == null)
    {
      setValue(TESTBOOLEAN, "");
    }
    else
    {
      setValue(TESTBOOLEAN, java.lang.Boolean.toString(value));
    }
  }
  
  public boolean isTestBooleanWritable()
  {
    return isWritable(TESTBOOLEAN);
  }
  
  public boolean isTestBooleanReadable()
  {
    return isReadable(TESTBOOLEAN);
  }
  
  public boolean isTestBooleanModified()
  {
    return isModified(TESTBOOLEAN);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeBooleanMdDTO getTestBooleanMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeBooleanMdDTO) getAttributeDTO(TESTBOOLEAN).getAttributeMdDTO();
  }
  
  public String getTestCharacter()
  {
    return getValue(TESTCHARACTER);
  }
  
  public void setTestCharacter(String value)
  {
    if(value == null)
    {
      setValue(TESTCHARACTER, "");
    }
    else
    {
      setValue(TESTCHARACTER, value);
    }
  }
  
  public boolean isTestCharacterWritable()
  {
    return isWritable(TESTCHARACTER);
  }
  
  public boolean isTestCharacterReadable()
  {
    return isReadable(TESTCHARACTER);
  }
  
  public boolean isTestCharacterModified()
  {
    return isModified(TESTCHARACTER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeCharacterMdDTO getTestCharacterMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeCharacterMdDTO) getAttributeDTO(TESTCHARACTER).getAttributeMdDTO();
  }
  
  public java.util.Date getTestDate()
  {
    return com.runwaysdk.constants.MdAttributeDateUtil.getTypeSafeValue(getValue(TESTDATE));
  }
  
  public void setTestDate(java.util.Date value)
  {
    if(value == null)
    {
      setValue(TESTDATE, "");
    }
    else
    {
      setValue(TESTDATE, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATE_FORMAT).format(value));
    }
  }
  
  public boolean isTestDateWritable()
  {
    return isWritable(TESTDATE);
  }
  
  public boolean isTestDateReadable()
  {
    return isReadable(TESTDATE);
  }
  
  public boolean isTestDateModified()
  {
    return isModified(TESTDATE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateMdDTO getTestDateMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateMdDTO) getAttributeDTO(TESTDATE).getAttributeMdDTO();
  }
  
  public java.util.Date getTestDateTime()
  {
    return com.runwaysdk.constants.MdAttributeDateTimeUtil.getTypeSafeValue(getValue(TESTDATETIME));
  }
  
  public void setTestDateTime(java.util.Date value)
  {
    if(value == null)
    {
      setValue(TESTDATETIME, "");
    }
    else
    {
      setValue(TESTDATETIME, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.DATETIME_FORMAT).format(value));
    }
  }
  
  public boolean isTestDateTimeWritable()
  {
    return isWritable(TESTDATETIME);
  }
  
  public boolean isTestDateTimeReadable()
  {
    return isReadable(TESTDATETIME);
  }
  
  public boolean isTestDateTimeModified()
  {
    return isModified(TESTDATETIME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO getTestDateTimeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDateTimeMdDTO) getAttributeDTO(TESTDATETIME).getAttributeMdDTO();
  }
  
  public java.math.BigDecimal getTestDecimal()
  {
    return com.runwaysdk.constants.MdAttributeDecimalUtil.getTypeSafeValue(getValue(TESTDECIMAL));
  }
  
  public void setTestDecimal(java.math.BigDecimal value)
  {
    if(value == null)
    {
      setValue(TESTDECIMAL, "");
    }
    else
    {
      setValue(TESTDECIMAL, value.toString());
    }
  }
  
  public boolean isTestDecimalWritable()
  {
    return isWritable(TESTDECIMAL);
  }
  
  public boolean isTestDecimalReadable()
  {
    return isReadable(TESTDECIMAL);
  }
  
  public boolean isTestDecimalModified()
  {
    return isModified(TESTDECIMAL);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDecMdDTO getTestDecimalMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDecMdDTO) getAttributeDTO(TESTDECIMAL).getAttributeMdDTO();
  }
  
  public Double getTestDouble()
  {
    return com.runwaysdk.constants.MdAttributeDoubleUtil.getTypeSafeValue(getValue(TESTDOUBLE));
  }
  
  public void setTestDouble(Double value)
  {
    if(value == null)
    {
      setValue(TESTDOUBLE, "");
    }
    else
    {
      setValue(TESTDOUBLE, java.lang.Double.toString(value));
    }
  }
  
  public boolean isTestDoubleWritable()
  {
    return isWritable(TESTDOUBLE);
  }
  
  public boolean isTestDoubleReadable()
  {
    return isReadable(TESTDOUBLE);
  }
  
  public boolean isTestDoubleModified()
  {
    return isModified(TESTDOUBLE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDecMdDTO getTestDoubleMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDecMdDTO) getAttributeDTO(TESTDOUBLE).getAttributeMdDTO();
  }
  
  public Float getTestFloat()
  {
    return com.runwaysdk.constants.MdAttributeFloatUtil.getTypeSafeValue(getValue(TESTFLOAT));
  }
  
  public void setTestFloat(Float value)
  {
    if(value == null)
    {
      setValue(TESTFLOAT, "");
    }
    else
    {
      setValue(TESTFLOAT, java.lang.Float.toString(value));
    }
  }
  
  public boolean isTestFloatWritable()
  {
    return isWritable(TESTFLOAT);
  }
  
  public boolean isTestFloatReadable()
  {
    return isReadable(TESTFLOAT);
  }
  
  public boolean isTestFloatModified()
  {
    return isModified(TESTFLOAT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeDecMdDTO getTestFloatMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeDecMdDTO) getAttributeDTO(TESTFLOAT).getAttributeMdDTO();
  }
  
  public String getTestHash()
  {
    return getValue(TESTHASH);
  }
  
  public boolean testHashEquals(java.lang.String value, boolean alreadyEncrypted)
  {
    return getAttributeHashDTO(TESTHASH).encryptionEquals(value, alreadyEncrypted);
  }
  
  public void setTestHash(String value)
  {
    if(value == null)
    {
      setValue(TESTHASH, "");
    }
    else
    {
      setValue(TESTHASH, value);
    }
  }
  
  public boolean isTestHashWritable()
  {
    return isWritable(TESTHASH);
  }
  
  public boolean isTestHashReadable()
  {
    return isReadable(TESTHASH);
  }
  
  public boolean isTestHashModified()
  {
    return isModified(TESTHASH);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO getTestHashMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO) getAttributeDTO(TESTHASH).getAttributeMdDTO();
  }
  
  public Integer getTestInteger()
  {
    return com.runwaysdk.constants.MdAttributeIntegerUtil.getTypeSafeValue(getValue(TESTINTEGER));
  }
  
  public void setTestInteger(Integer value)
  {
    if(value == null)
    {
      setValue(TESTINTEGER, "");
    }
    else
    {
      setValue(TESTINTEGER, java.lang.Integer.toString(value));
    }
  }
  
  public boolean isTestIntegerWritable()
  {
    return isWritable(TESTINTEGER);
  }
  
  public boolean isTestIntegerReadable()
  {
    return isReadable(TESTINTEGER);
  }
  
  public boolean isTestIntegerModified()
  {
    return isModified(TESTINTEGER);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getTestIntegerMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(TESTINTEGER).getAttributeMdDTO();
  }
  
  public Long getTestLong()
  {
    return com.runwaysdk.constants.MdAttributeLongUtil.getTypeSafeValue(getValue(TESTLONG));
  }
  
  public void setTestLong(Long value)
  {
    if(value == null)
    {
      setValue(TESTLONG, "");
    }
    else
    {
      setValue(TESTLONG, java.lang.Long.toString(value));
    }
  }
  
  public boolean isTestLongWritable()
  {
    return isWritable(TESTLONG);
  }
  
  public boolean isTestLongReadable()
  {
    return isReadable(TESTLONG);
  }
  
  public boolean isTestLongModified()
  {
    return isModified(TESTLONG);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeNumberMdDTO getTestLongMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeNumberMdDTO) getAttributeDTO(TESTLONG).getAttributeMdDTO();
  }
  
  public com.runwaysdk.jstest.RefClassDTO getTestReferenceObject()
  {
    if(getValue(TESTREFERENCEOBJECT) == null || getValue(TESTREFERENCEOBJECT).trim().equals(""))
    {
      return null;
    }
    else
    {
      return com.runwaysdk.jstest.RefClassDTO.get(getRequest(), getValue(TESTREFERENCEOBJECT));
    }
  }
  
  public String getTestReferenceObjectId()
  {
    return getValue(TESTREFERENCEOBJECT);
  }
  
  public void setTestReferenceObject(com.runwaysdk.jstest.RefClassDTO value)
  {
    if(value == null)
    {
      setValue(TESTREFERENCEOBJECT, "");
    }
    else
    {
      setValue(TESTREFERENCEOBJECT, value.getOid());
    }
  }
  
  public boolean isTestReferenceObjectWritable()
  {
    return isWritable(TESTREFERENCEOBJECT);
  }
  
  public boolean isTestReferenceObjectReadable()
  {
    return isReadable(TESTREFERENCEOBJECT);
  }
  
  public boolean isTestReferenceObjectModified()
  {
    return isModified(TESTREFERENCEOBJECT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeReferenceMdDTO getTestReferenceObjectMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeReferenceMdDTO) getAttributeDTO(TESTREFERENCEOBJECT).getAttributeMdDTO();
  }
  
  public String getTestSymmetric()
  {
    return getValue(TESTSYMMETRIC);
  }
  
  public void setTestSymmetric(String value)
  {
    if(value == null)
    {
      setValue(TESTSYMMETRIC, "");
    }
    else
    {
      setValue(TESTSYMMETRIC, value);
    }
  }
  
  public boolean isTestSymmetricWritable()
  {
    return isWritable(TESTSYMMETRIC);
  }
  
  public boolean isTestSymmetricReadable()
  {
    return isReadable(TESTSYMMETRIC);
  }
  
  public boolean isTestSymmetricModified()
  {
    return isModified(TESTSYMMETRIC);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO getTestSymmetricMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO) getAttributeDTO(TESTSYMMETRIC).getAttributeMdDTO();
  }
  
  public String getTestText()
  {
    return getValue(TESTTEXT);
  }
  
  public void setTestText(String value)
  {
    if(value == null)
    {
      setValue(TESTTEXT, "");
    }
    else
    {
      setValue(TESTTEXT, value);
    }
  }
  
  public boolean isTestTextWritable()
  {
    return isWritable(TESTTEXT);
  }
  
  public boolean isTestTextReadable()
  {
    return isReadable(TESTTEXT);
  }
  
  public boolean isTestTextModified()
  {
    return isModified(TESTTEXT);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTextMdDTO getTestTextMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTextMdDTO) getAttributeDTO(TESTTEXT).getAttributeMdDTO();
  }
  
  public java.util.Date getTestTime()
  {
    return com.runwaysdk.constants.MdAttributeTimeUtil.getTypeSafeValue(getValue(TESTTIME));
  }
  
  public void setTestTime(java.util.Date value)
  {
    if(value == null)
    {
      setValue(TESTTIME, "");
    }
    else
    {
      setValue(TESTTIME, new java.text.SimpleDateFormat(com.runwaysdk.constants.Constants.TIME_FORMAT).format(value));
    }
  }
  
  public boolean isTestTimeWritable()
  {
    return isWritable(TESTTIME);
  }
  
  public boolean isTestTimeReadable()
  {
    return isReadable(TESTTIME);
  }
  
  public boolean isTestTimeModified()
  {
    return isModified(TESTTIME);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeTimeMdDTO getTestTimeMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeTimeMdDTO) getAttributeDTO(TESTTIME).getAttributeMdDTO();
  }
  
  public com.runwaysdk.system.PhoneNumberDTO getWorkPhone()
  {
    return (com.runwaysdk.system.PhoneNumberDTO) this.getAttributeStructDTO(WORKPHONE).getStructDTO();
  }
  
  public boolean isWorkPhoneWritable()
  {
    return isWritable(WORKPHONE);
  }
  
  public boolean isWorkPhoneReadable()
  {
    return isReadable(WORKPHONE);
  }
  
  public boolean isWorkPhoneModified()
  {
    return isModified(WORKPHONE);
  }
  
  public final com.runwaysdk.transport.metadata.AttributeStructMdDTO getWorkPhoneMd()
  {
    return (com.runwaysdk.transport.metadata.AttributeStructMdDTO) getAttributeDTO(WORKPHONE).getAttributeMdDTO();
  }
  
  public final java.lang.Boolean compareIntegers(java.lang.Integer num1, java.lang.Integer num2)
  {
    String[] _declaredTypes = new String[]{"java.lang.Integer", "java.lang.Integer"};
    Object[] _parameters = new Object[]{num1, num2};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "compareIntegers", _declaredTypes);
    return (java.lang.Boolean) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.Boolean compareIntegers(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid, java.lang.Integer num1, java.lang.Integer num2)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.Integer", "java.lang.Integer"};
    Object[] _parameters = new Object[]{oid, num1, num2};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "compareIntegers", _declaredTypes);
    return (java.lang.Boolean) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final com.runwaysdk.jstest.TestClassDTO[] createInstances(java.lang.Integer num)
  {
    String[] _declaredTypes = new String[]{"java.lang.Integer"};
    Object[] _parameters = new Object[]{num};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "createInstances", _declaredTypes);
    return (com.runwaysdk.jstest.TestClassDTO[]) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final com.runwaysdk.jstest.TestClassDTO[] createInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid, java.lang.Integer num)
  {
    String[] _declaredTypes = new String[]{"java.lang.String", "java.lang.Integer"};
    Object[] _parameters = new Object[]{oid, num};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "createInstances", _declaredTypes);
    return (com.runwaysdk.jstest.TestClassDTO[]) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public static final java.lang.Integer doubleAnInteger(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.Integer num)
  {
    String[] _declaredTypes = new String[]{"java.lang.Integer"};
    Object[] _parameters = new Object[]{num};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "doubleAnInteger", _declaredTypes);
    return (java.lang.Integer) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void instanceForceException()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceException", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void instanceForceException(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceException", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void instanceForceException1()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceException1", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void instanceForceException1(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceException1", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void instanceForceException2()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceException2", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void instanceForceException2(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceException2", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void instanceForceException3()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceException3", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void instanceForceException3(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceException3", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final java.lang.Integer instanceForceInformation()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceInformation", _declaredTypes);
    return (java.lang.Integer) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.Integer instanceForceInformation(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceInformation", _declaredTypes);
    return (java.lang.Integer) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final java.lang.Integer instanceForceMultipleInformations()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceMultipleInformations", _declaredTypes);
    return (java.lang.Integer) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.Integer instanceForceMultipleInformations(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceMultipleInformations", _declaredTypes);
    return (java.lang.Integer) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void instanceForceMultipleProblems()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceMultipleProblems", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void instanceForceMultipleProblems(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceMultipleProblems", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final java.lang.Integer instanceForceMultipleWarnings()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceMultipleWarnings", _declaredTypes);
    return (java.lang.Integer) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.Integer instanceForceMultipleWarnings(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceMultipleWarnings", _declaredTypes);
    return (java.lang.Integer) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void instanceForceProblem()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceProblem", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void instanceForceProblem(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceProblem", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final java.lang.Integer instanceForceWarning()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceWarning", _declaredTypes);
    return (java.lang.Integer) getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final java.lang.Integer instanceForceWarning(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceWarning", _declaredTypes);
    return (java.lang.Integer) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public final void instanceForceWarningVoid()
  {
    String[] _declaredTypes = new String[]{};
    Object[] _parameters = new Object[]{};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceWarningVoid", _declaredTypes);
    getRequest().invokeMethod(_metadata, this, _parameters);
  }
  
  public static final void instanceForceWarningVoid(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "instanceForceWarningVoid", _declaredTypes);
    clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.jstest.RefClassDTO> getAllRefClass()
  {
    return (java.util.List<? extends com.runwaysdk.jstest.RefClassDTO>) getRequest().getChildren(this.getOid(), com.runwaysdk.jstest.BefriendsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.jstest.RefClassDTO> getAllRefClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.jstest.RefClassDTO>) clientRequestIF.getChildren(oid, com.runwaysdk.jstest.BefriendsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public java.util.List<? extends com.runwaysdk.jstest.BefriendsDTO> getAllRefClassRelationships()
  {
    return (java.util.List<? extends com.runwaysdk.jstest.BefriendsDTO>) getRequest().getChildRelationships(this.getOid(), com.runwaysdk.jstest.BefriendsDTO.CLASS);
  }
  
  @SuppressWarnings("unchecked")
  public static java.util.List<? extends com.runwaysdk.jstest.BefriendsDTO> getAllRefClassRelationships(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    return (java.util.List<? extends com.runwaysdk.jstest.BefriendsDTO>) clientRequestIF.getChildRelationships(oid, com.runwaysdk.jstest.BefriendsDTO.CLASS);
  }
  
  public com.runwaysdk.jstest.BefriendsDTO addRefClass(com.runwaysdk.jstest.RefClassDTO child)
  {
    return (com.runwaysdk.jstest.BefriendsDTO) getRequest().addChild(this.getOid(), child.getOid(), com.runwaysdk.jstest.BefriendsDTO.CLASS);
  }
  
  public static com.runwaysdk.jstest.BefriendsDTO addRefClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid, com.runwaysdk.jstest.RefClassDTO child)
  {
    return (com.runwaysdk.jstest.BefriendsDTO) clientRequestIF.addChild(oid, child.getOid(), com.runwaysdk.jstest.BefriendsDTO.CLASS);
  }
  
  public void removeRefClass(com.runwaysdk.jstest.BefriendsDTO relationship)
  {
    getRequest().deleteChild(relationship.getOid());
  }
  
  public static void removeRefClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, com.runwaysdk.jstest.BefriendsDTO relationship)
  {
    clientRequestIF.deleteChild(relationship.getOid());
  }
  
  public void removeAllRefClass()
  {
    getRequest().deleteChildren(this.getOid(), com.runwaysdk.jstest.BefriendsDTO.CLASS);
  }
  
  public static void removeAllRefClass(com.runwaysdk.constants.ClientRequestIF clientRequestIF, String oid)
  {
    clientRequestIF.deleteChildren(oid, com.runwaysdk.jstest.BefriendsDTO.CLASS);
  }
  
  public static com.runwaysdk.jstest.TestClassDTO get(com.runwaysdk.constants.ClientRequestIF clientRequest, String oid)
  {
    com.runwaysdk.business.EntityDTO dto = (com.runwaysdk.business.EntityDTO)clientRequest.get(oid);
    
    return (com.runwaysdk.jstest.TestClassDTO) dto;
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
    getRequest().delete(this.getOid());
  }
  
  public static com.runwaysdk.jstest.TestClassQueryDTO getAllInstances(com.runwaysdk.constants.ClientRequestIF clientRequest, String sortAttribute, Boolean ascending, Integer pageSize, Integer pageNumber)
  {
    return (com.runwaysdk.jstest.TestClassQueryDTO) clientRequest.getAllInstances(com.runwaysdk.jstest.TestClassDTO.CLASS, sortAttribute, ascending, pageSize, pageNumber);
  }
  
  public void lock()
  {
    getRequest().lock(this);
  }
  
  public static com.runwaysdk.jstest.TestClassDTO lock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "lock", _declaredTypes);
    return (com.runwaysdk.jstest.TestClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
  public void unlock()
  {
    getRequest().unlock(this);
  }
  
  public static com.runwaysdk.jstest.TestClassDTO unlock(com.runwaysdk.constants.ClientRequestIF clientRequest, java.lang.String oid)
  {
    String[] _declaredTypes = new String[]{"java.lang.String"};
    Object[] _parameters = new Object[]{oid};
    com.runwaysdk.business.MethodMetaData _metadata = new com.runwaysdk.business.MethodMetaData(com.runwaysdk.jstest.TestClassDTO.CLASS, "unlock", _declaredTypes);
    return (com.runwaysdk.jstest.TestClassDTO) clientRequest.invokeMethod(_metadata, null, _parameters);
  }
  
}
