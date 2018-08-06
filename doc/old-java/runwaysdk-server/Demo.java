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
import java.util.Locale;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdFieldInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdMobileFieldInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.TransactionItemInfo;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.format.AbstractFormatFactory;
import com.runwaysdk.format.FormatFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdAttributeText;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdClass;
import com.runwaysdk.system.metadata.MdField;
import com.runwaysdk.system.metadata.MdRelationship;
import com.runwaysdk.system.metadata.MdType;
import com.runwaysdk.system.metadata.Metadata;

public class Demo
{
//  private static Main    compiler;

  private static boolean logging = true;

  private static int     count   = 0;

  @Request
  public static void main(String[] args) throws Exception
  {
    
  }
  
  @Transaction
  public static void addDateValidation()
  {
    MdClassDAOIF mdAttributeDate = MdBusinessDAO.getMdClassDAO(MdAttributeDateInfo.CLASS);

    MdAttributeDateDAO startDate = MdAttributeDateDAO.newInstance();
    startDate.setValue(MdAttributeDateInfo.NAME, "startDate");
    startDate.setValue(MdAttributeDateInfo.REQUIRED, "false");
    startDate.setValue(MdAttributeDateInfo.REMOVE, "false");
    startDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdAttributeDate.getOid());
    startDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
    startDate.apply();

    MdAttributeDateDAO endDate = MdAttributeDateDAO.newInstance();
    endDate.setValue(MdAttributeDateInfo.NAME, "endDate");
    endDate.setValue(MdAttributeDateInfo.REQUIRED, "false");
    endDate.setValue(MdAttributeDateInfo.REMOVE, "false");
    endDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdAttributeDate.getOid());
    endDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
    endDate.apply();

    MdAttributeBooleanDAO beforeTodayInclusive = MdAttributeBooleanDAO.newInstance();
    beforeTodayInclusive.setValue(MdAttributeBooleanInfo.NAME, "beforeTodayInclusive");
    beforeTodayInclusive.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    beforeTodayInclusive.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    beforeTodayInclusive.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttributeDate.getOid());
    beforeTodayInclusive.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Before today (inclusive)");
    beforeTodayInclusive.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    beforeTodayInclusive.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    beforeTodayInclusive.apply();

    MdAttributeBooleanDAO afterTodayInclusive = MdAttributeBooleanDAO.newInstance();
    afterTodayInclusive.setValue(MdAttributeBooleanInfo.NAME, "afterTodayInclusive");
    afterTodayInclusive.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    afterTodayInclusive.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    afterTodayInclusive.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttributeDate.getOid());
    afterTodayInclusive.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "After today (inclusive)");
    afterTodayInclusive.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    afterTodayInclusive.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    afterTodayInclusive.apply();

    MdAttributeBooleanDAO beforeTodayExclusive = MdAttributeBooleanDAO.newInstance();
    beforeTodayExclusive.setValue(MdAttributeBooleanInfo.NAME, "beforeTodayExclusive");
    beforeTodayExclusive.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    beforeTodayExclusive.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    beforeTodayExclusive.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttributeDate.getOid());
    beforeTodayExclusive.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Before today (exclusive)");
    beforeTodayExclusive.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    beforeTodayExclusive.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    beforeTodayExclusive.apply();

    MdAttributeBooleanDAO afterTodayExclusive = MdAttributeBooleanDAO.newInstance();
    afterTodayExclusive.setValue(MdAttributeBooleanInfo.NAME, "afterTodayExclusive");
    afterTodayExclusive.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    afterTodayExclusive.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    afterTodayExclusive.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdAttributeDate.getOid());
    afterTodayExclusive.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "After today (exclusive)");
    afterTodayExclusive.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    afterTodayExclusive.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    afterTodayExclusive.apply();
  }

  @Transaction
  public static void addWebDateValidation()
  {
    MdClassDAOIF mdWebDate = MdBusinessDAO.getMdClassDAO(MdWebDateInfo.CLASS);

    MdAttributeDateDAO startDate = MdAttributeDateDAO.newInstance();
    startDate.setValue(MdAttributeDateInfo.NAME, "startDate");
    startDate.setValue(MdAttributeDateInfo.REQUIRED, "false");
    startDate.setValue(MdAttributeDateInfo.REMOVE, "false");
    startDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdWebDate.getOid());
    startDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Date");
    startDate.apply();

    MdAttributeDateDAO endDate = MdAttributeDateDAO.newInstance();
    endDate.setValue(MdAttributeDateInfo.NAME, "endDate");
    endDate.setValue(MdAttributeDateInfo.REQUIRED, "false");
    endDate.setValue(MdAttributeDateInfo.REMOVE, "false");
    endDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdWebDate.getOid());
    endDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Date");
    endDate.apply();

    MdAttributeBooleanDAO beforeTodayInclusive = MdAttributeBooleanDAO.newInstance();
    beforeTodayInclusive.setValue(MdAttributeBooleanInfo.NAME, "beforeTodayInclusive");
    beforeTodayInclusive.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    beforeTodayInclusive.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    beforeTodayInclusive.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdWebDate.getOid());
    beforeTodayInclusive.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Before today (inclusive)");
    beforeTodayInclusive.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    beforeTodayInclusive.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    beforeTodayInclusive.apply();

    MdAttributeBooleanDAO afterTodayInclusive = MdAttributeBooleanDAO.newInstance();
    afterTodayInclusive.setValue(MdAttributeBooleanInfo.NAME, "afterTodayInclusive");
    afterTodayInclusive.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    afterTodayInclusive.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    afterTodayInclusive.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdWebDate.getOid());
    afterTodayInclusive.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "After today (inclusive)");
    afterTodayInclusive.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    afterTodayInclusive.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    afterTodayInclusive.apply();

    MdAttributeBooleanDAO beforeTodayExclusive = MdAttributeBooleanDAO.newInstance();
    beforeTodayExclusive.setValue(MdAttributeBooleanInfo.NAME, "beforeTodayExclusive");
    beforeTodayExclusive.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    beforeTodayExclusive.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    beforeTodayExclusive.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdWebDate.getOid());
    beforeTodayExclusive.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Before today (exclusive)");
    beforeTodayExclusive.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    beforeTodayExclusive.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    beforeTodayExclusive.apply();

    MdAttributeBooleanDAO afterTodayExclusive = MdAttributeBooleanDAO.newInstance();
    afterTodayExclusive.setValue(MdAttributeBooleanInfo.NAME, "afterTodayExclusive");
    afterTodayExclusive.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    afterTodayExclusive.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    afterTodayExclusive.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdWebDate.getOid());
    afterTodayExclusive.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "After today (exclusive)");
    afterTodayExclusive.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    afterTodayExclusive.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    afterTodayExclusive.apply();
  }

  @Transaction
  public static void addNumberRanges()
  {
    MdClassDAOIF mdAttributeInteger = MdBusinessDAO.getMdClassDAO(MdAttributeIntegerInfo.CLASS);

    MdAttributeIntegerDAO startRangeInteger = MdAttributeIntegerDAO.newInstance();
    startRangeInteger.setValue(MdAttributeIntegerInfo.NAME, "startRange");
    startRangeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, "false");
    startRangeInteger.setValue(MdAttributeIntegerInfo.REMOVE, "false");
    startRangeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdAttributeInteger.getOid());
    startRangeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Range");
    startRangeInteger.apply();

    MdAttributeIntegerDAO endRangeInteger = MdAttributeIntegerDAO.newInstance();
    endRangeInteger.setValue(MdAttributeIntegerInfo.NAME, "endRange");
    endRangeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, "false");
    endRangeInteger.setValue(MdAttributeIntegerInfo.REMOVE, "false");
    endRangeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdAttributeInteger.getOid());
    endRangeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Range");
    endRangeInteger.apply();

    MdClassDAOIF mdAttributeLong = MdBusinessDAO.getMdClassDAO(MdAttributeLongInfo.CLASS);

    MdAttributeLongDAO startRangeLong = MdAttributeLongDAO.newInstance();
    startRangeLong.setValue(MdAttributeLongInfo.NAME, "startRange");
    startRangeLong.setValue(MdAttributeLongInfo.REQUIRED, "false");
    startRangeLong.setValue(MdAttributeLongInfo.REMOVE, "false");
    startRangeLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdAttributeLong.getOid());
    startRangeLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Range");
    startRangeLong.apply();

    MdAttributeLongDAO endRangeLong = MdAttributeLongDAO.newInstance();
    endRangeLong.setValue(MdAttributeLongInfo.NAME, "endRange");
    endRangeLong.setValue(MdAttributeLongInfo.REQUIRED, "false");
    endRangeLong.setValue(MdAttributeLongInfo.REMOVE, "false");
    endRangeLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdAttributeLong.getOid());
    endRangeLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Range");
    endRangeLong.apply();

    MdClassDAOIF mdAttributeDecimal = MdBusinessDAO.getMdClassDAO(MdAttributeDecimalInfo.CLASS);

    MdAttributeDecimalDAO startRangeDecimal = MdAttributeDecimalDAO.newInstance();
    startRangeDecimal.setValue(MdAttributeDecimalInfo.NAME, "startRange");
    startRangeDecimal.setValue(MdAttributeDecimalInfo.LENGTH, "24");
    startRangeDecimal.setValue(MdAttributeDecimalInfo.DECIMAL, "6");
    startRangeDecimal.setValue(MdAttributeDecimalInfo.REQUIRED, "false");
    startRangeDecimal.setValue(MdAttributeDecimalInfo.REMOVE, "false");
    startRangeDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, mdAttributeDecimal.getOid());
    startRangeDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Range");
    startRangeDecimal.apply();

    MdAttributeDecimalDAO endRangeDecimal = MdAttributeDecimalDAO.newInstance();
    endRangeDecimal.setValue(MdAttributeDecimalInfo.NAME, "endRange");
    endRangeDecimal.setValue(MdAttributeDecimalInfo.LENGTH, "24");
    endRangeDecimal.setValue(MdAttributeDecimalInfo.DECIMAL, "6");
    endRangeDecimal.setValue(MdAttributeDecimalInfo.REQUIRED, "false");
    endRangeDecimal.setValue(MdAttributeDecimalInfo.REMOVE, "false");
    endRangeDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, mdAttributeDecimal.getOid());
    endRangeDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Range");
    endRangeDecimal.apply();

    MdClassDAOIF mdAttributeDouble = MdBusinessDAO.getMdClassDAO(MdAttributeDoubleInfo.CLASS);

    MdAttributeDoubleDAO startRangeDouble = MdAttributeDoubleDAO.newInstance();
    startRangeDouble.setValue(MdAttributeDoubleInfo.NAME, "startRange");
    startRangeDouble.setValue(MdAttributeDoubleInfo.LENGTH, "24");
    startRangeDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "6");
    startRangeDouble.setValue(MdAttributeDoubleInfo.REQUIRED, "false");
    startRangeDouble.setValue(MdAttributeDoubleInfo.REMOVE, "false");
    startRangeDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdAttributeDouble.getOid());
    startRangeDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Range");
    startRangeDouble.apply();

    MdAttributeDoubleDAO endRangeDouble = MdAttributeDoubleDAO.newInstance();
    endRangeDouble.setValue(MdAttributeDoubleInfo.NAME, "endRange");
    endRangeDouble.setValue(MdAttributeDoubleInfo.LENGTH, "24");
    endRangeDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "6");
    endRangeDouble.setValue(MdAttributeDoubleInfo.REQUIRED, "false");
    endRangeDouble.setValue(MdAttributeDoubleInfo.REMOVE, "false");
    endRangeDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdAttributeDouble.getOid());
    endRangeDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Range");
    endRangeDouble.apply();

    MdClassDAOIF mdAttributeFloat = MdBusinessDAO.getMdClassDAO(MdAttributeFloatInfo.CLASS);

    MdAttributeFloatDAO startRangeFloat = MdAttributeFloatDAO.newInstance();
    startRangeFloat.setValue(MdAttributeFloatInfo.NAME, "startRange");
    startRangeFloat.setValue(MdAttributeFloatInfo.LENGTH, "24");
    startRangeFloat.setValue(MdAttributeFloatInfo.DECIMAL, "6");
    startRangeFloat.setValue(MdAttributeFloatInfo.REQUIRED, "false");
    startRangeFloat.setValue(MdAttributeFloatInfo.REMOVE, "false");
    startRangeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, mdAttributeFloat.getOid());
    startRangeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Range");
    startRangeFloat.apply();

    MdAttributeFloatDAO endRangeFloat = MdAttributeFloatDAO.newInstance();
    endRangeFloat.setValue(MdAttributeFloatInfo.NAME, "endRange");
    endRangeFloat.setValue(MdAttributeFloatInfo.LENGTH, "24");
    endRangeFloat.setValue(MdAttributeFloatInfo.DECIMAL, "6");
    endRangeFloat.setValue(MdAttributeFloatInfo.REQUIRED, "false");
    endRangeFloat.setValue(MdAttributeFloatInfo.REMOVE, "false");
    endRangeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, mdAttributeFloat.getOid());
    endRangeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Range");
    endRangeFloat.apply();
  }

  @Request
  public static void setupInstance()
  {
    try
    {

      // WebClientSession session = WebClientSession.createUserSession("SYSTEM",
      // "SYSTEM",
      // new Locale[] { Locale.ENGLISH });
      // ClientRequestIF request = session.getRequest();
      // String mdFormId = new MdFormQuery(new
      // QueryFactory()).getIterator().next().getOid();
      // MdFormDTO mdFormDTO = MdFormDTO.get(request, mdFormId);
      //
      // WebFormObject fo = (WebFormObject)
      // WebFormObject.newInstance(mdFormDTO);
      //
      // JSONFormVisitor visitor2 = new JSONFormVisitor();
      // JSONFormVisitor visitor = visitor2;
      // fo.accept(visitor);
      // JSONObject json = visitor.getJSON();
      //      
      // String input = json.toString(2);
      // System.out.println(input);
      //
      // WebFormObject f = new MofoParser(request, input).getFormObject();
      // f.accept(visitor2);
      // System.out.println(visitor2.getJSON().toString(2));
    }
    catch (Throwable t)
    {
      t.printStackTrace(System.out);
      throw new RuntimeException(t);
    }
    finally
    {
      CacheShutdown.shutdown();
    }
  }

  @Request
  public static void setupMetadataR()
  {
    Demo.setupMetadataTransaction();
  }

  @Transaction
  private static void setupMetadataTransaction()
  {
    if (logging)
    {
      Database.enableLoggingDMLAndDDLstatements(true);
    }
    
    // updateDefiningForm();
    // addFieldReference();
    // setupConditionMetadata();
    // setupMetadata();
    // addNumberRanges();
    // addDateValidation();
    // addWebDateValidation();
    // addWebBooleanDefaultValue();
//    addTransactionItemIgnoreSequence();
  }
  
  public static void addTransactionItemIgnoreSequence()
  {
    MdClassDAOIF mdTransactionItem = MdBusinessDAO.getMdClassDAO(TransactionItemInfo.CLASS);
    
    MdAttributeBooleanDAO ignoreSequenceNumber = MdAttributeBooleanDAO.newInstance();
    ignoreSequenceNumber.setValue(MdAttributeBooleanInfo.NAME, "ignoreSequenceNumber");
    ignoreSequenceNumber.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    ignoreSequenceNumber.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    ignoreSequenceNumber.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdTransactionItem.getOid());
    ignoreSequenceNumber.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Ignore sequence number");
    ignoreSequenceNumber.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    ignoreSequenceNumber.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    ignoreSequenceNumber.apply();
  }
  
  public static void addWebBooleanDefaultValue()
  {
    MdClassDAOIF mdWebBoolean = MdBusinessDAO.getMdClassDAO(MdWebBooleanInfo.CLASS);

    MdAttributeBooleanDAO defaultValue = MdAttributeBooleanDAO.newInstance();
    defaultValue.setValue(MdAttributeBooleanInfo.NAME, "defaultValue");
    defaultValue.setValue(MdAttributeBooleanInfo.REQUIRED, "false");
    defaultValue.setValue(MdAttributeBooleanInfo.REMOVE, "false");
    defaultValue.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdWebBoolean.getOid());
    defaultValue.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Default value");
    defaultValue.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    defaultValue.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    defaultValue.apply();
  }

  public static void updateDefiningForm()
  {
    MdAttributeDAO definingMdForm = (MdAttributeDAO) MdAttributeReferenceDAO.get(MdAttributeReferenceInfo.CLASS, Constants.METADATA_PACKAGE + ".MdWebField" + ".definingMdForm").getBusinessDAO();
    definingMdForm.setValue(MdAttributeReference.REQUIRED, MdAttributeBooleanInfo.FALSE);
    definingMdForm.apply();
  }

  public static void addFieldReference()
  {
    MdBusinessDAO mdWebReference = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebReference.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdWebReference.setValue(MdBusinessInfo.NAME, "MdWebReference");
    mdWebReference.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, MdBusinessDAO.getMdBusinessDAO(MdWebAttributeInfo.CLASS).getOid());
    mdWebReference.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference");
    mdWebReference.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference");
    mdWebReference.setGenerateMdController(false);
    mdWebReference.apply();

    MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, MdBusinessDAO.getMdBusinessDAO(MdWebCharacterInfo.CLASS).getOid());
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, "unique");
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.COLUMN_NAME, "uniqueIndex");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Unique");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Unique");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    mdAttributeBoolean.apply();
  }

  public static void setupConditionMetadata()
  {
    MdBusinessDAO operationMaster = MdBusinessDAO.newInstance();
    operationMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS).getOid());
    operationMaster.setValue(MdBusinessInfo.PACKAGE, Constants.SYSTEM_PACKAGE);
    operationMaster.setValue(MdBusinessInfo.NAME, "FieldOperation");
    operationMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    operationMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    operationMaster.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Condition Operation");
    operationMaster.apply();

    EnumerationItemDAO eq = EnumerationItemDAO.newInstance(operationMaster.definesType());
    eq.setValue(EnumerationMasterInfo.NAME, "EQ");
    eq.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Equals");
    eq.apply();

    EnumerationItemDAO neq = EnumerationItemDAO.newInstance(operationMaster.definesType());
    neq.setValue(EnumerationMasterInfo.NAME, "NEQ");
    neq.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Not equals");
    neq.apply();

    EnumerationItemDAO gt = EnumerationItemDAO.newInstance(operationMaster.definesType());
    gt.setValue(EnumerationMasterInfo.NAME, "GT");
    gt.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Greater than");
    gt.apply();

    EnumerationItemDAO gte = EnumerationItemDAO.newInstance(operationMaster.definesType());
    gte.setValue(EnumerationMasterInfo.NAME, "GTE");
    gte.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Greater than or equals");
    gte.apply();

    EnumerationItemDAO lt = EnumerationItemDAO.newInstance(operationMaster.definesType());
    lt.setValue(EnumerationMasterInfo.NAME, "LT");
    lt.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Less than");
    lt.apply();

    EnumerationItemDAO lte = EnumerationItemDAO.newInstance(operationMaster.definesType());
    lte.setValue(EnumerationMasterInfo.NAME, "LTE");
    lte.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Less than or equals");
    lte.apply();

    MdEnumerationDAO characterOperations = MdEnumerationDAO.newInstance();
    characterOperations.setValue(MdEnumerationInfo.PACKAGE, Constants.SYSTEM_PACKAGE);
    characterOperations.setValue(MdEnumerationInfo.NAME, "CharacterOperation");
    characterOperations.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
    characterOperations.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, operationMaster.getOid());
    characterOperations.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character operation");
    characterOperations.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character condition operation");
    characterOperations.apply();

    characterOperations.addEnumItem(eq);
    characterOperations.addEnumItem(neq);

    MdEnumerationDAO allOperations = MdEnumerationDAO.newInstance();
    allOperations.setValue(MdEnumerationInfo.PACKAGE, Constants.SYSTEM_PACKAGE);
    allOperations.setValue(MdEnumerationInfo.NAME, "AllOperation");
    allOperations.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    allOperations.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, operationMaster.getOid());
    allOperations.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    allOperations.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Condition operation");
    allOperations.apply();

    MdBusinessDAOIF mdField = MdBusinessDAO.getMdBusinessDAO(MdFieldInfo.CLASS);

    MdBusinessDAO condition = MdBusinessDAO.newInstance();
    condition.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    condition.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    condition.setValue(MdBusinessInfo.NAME, "FieldCondition");
    condition.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Condition");
    condition.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A condition on work flow for between fields");
    condition.apply();

    MdBusinessDAO compositeCondition = MdBusinessDAO.newInstance();
    compositeCondition.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, condition.getOid());
    compositeCondition.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    compositeCondition.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    compositeCondition.setValue(MdBusinessInfo.NAME, "CompositeFieldCondition");
    compositeCondition.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Composite Condition");
    compositeCondition.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A composite of two conditions");
    compositeCondition.apply();

    MdAttributeReferenceDAO firstCondition = MdAttributeReferenceDAO.newInstance();
    firstCondition.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, compositeCondition.getOid());
    firstCondition.setValue(MdAttributeReferenceInfo.NAME, "firstCondition");
    firstCondition.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    firstCondition.setValue(MdAttributeReferenceInfo.REF_MD_BUSINESS, condition.getOid());
    firstCondition.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "First Condition");
    firstCondition.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "First condition of a composite condition");
    firstCondition.apply();

    MdAttributeReferenceDAO secondCondition = MdAttributeReferenceDAO.newInstance();
    secondCondition.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, compositeCondition.getOid());
    secondCondition.setValue(MdAttributeReferenceInfo.NAME, "secondCondition");
    secondCondition.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    secondCondition.setValue(MdAttributeReferenceInfo.REF_MD_BUSINESS, condition.getOid());
    secondCondition.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Second Condition");
    secondCondition.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Second condition of a composite condition");
    secondCondition.apply();

    MdBusinessDAO andCondition = MdBusinessDAO.newInstance();
    andCondition.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, compositeCondition.getOid());
    andCondition.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    andCondition.setValue(MdBusinessInfo.NAME, "AndFieldCondition");
    andCondition.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "And Condition");
    andCondition.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An AND condition");
    andCondition.apply();

    MdBusinessDAO characterCondition = MdBusinessDAO.newInstance();
    characterCondition.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, condition.getOid());
    characterCondition.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    characterCondition.setValue(MdBusinessInfo.NAME, "CharacterCondition");
    characterCondition.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character condition");
    characterCondition.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A condition which check a field value again a simple string");
    characterCondition.apply();

    MdAttributeReferenceDAO definingMdField = MdAttributeReferenceDAO.newInstance();
    definingMdField.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, characterCondition.getOid());
    definingMdField.setValue(MdAttributeReferenceInfo.NAME, "definingMdField");
    definingMdField.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    definingMdField.setValue(MdAttributeReferenceInfo.REF_MD_BUSINESS, mdField.getOid());
    definingMdField.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Defining Field");
    definingMdField.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Field upon which the criteria is checked");
    definingMdField.apply();

    MdAttributeCharacterDAO characterValue = MdAttributeCharacterDAO.newInstance();
    characterValue.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, characterCondition.getOid());
    characterValue.setValue(MdAttributeCharacterInfo.NAME, "value");
    characterValue.setValue(MdAttributeCharacterInfo.COLUMN_NAME, "fieldValue");
    characterValue.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    characterValue.setValue(MdAttributeCharacterInfo.SIZE, "2000");
    characterValue.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value");
    characterValue.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value to check against");
    characterValue.apply();

    MdAttributeEnumerationDAO characterOperation = MdAttributeEnumerationDAO.newInstance();
    characterOperation.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, characterCondition.getOid());
    characterOperation.setValue(MdAttributeEnumerationInfo.NAME, "operation");
    characterOperation.setValue(MdAttributeEnumerationInfo.COLUMN_NAME, "conditionOperation");
    characterOperation.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    characterOperation.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, characterOperations.getOid());
    characterOperation.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    characterOperation.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    characterOperation.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    characterOperation.apply();

    MdBusinessDAO dateCondition = MdBusinessDAO.newInstance();
    dateCondition.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, condition.getOid());
    dateCondition.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    dateCondition.setValue(MdBusinessInfo.NAME, "DateCondition");
    dateCondition.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character condition");
    dateCondition.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A condition which check a field value again a simple string");
    dateCondition.apply();

    MdAttributeReferenceDAO dateDefiningMdField = MdAttributeReferenceDAO.newInstance();
    dateDefiningMdField.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, dateCondition.getOid());
    dateDefiningMdField.setValue(MdAttributeReferenceInfo.NAME, "definingMdField");
    dateDefiningMdField.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    dateDefiningMdField.setValue(MdAttributeReferenceInfo.REF_MD_BUSINESS, mdField.getOid());
    dateDefiningMdField.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Defining Field");
    dateDefiningMdField.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Field upon which the criteria is checked");
    dateDefiningMdField.apply();

    MdAttributeDateDAO dateValue = MdAttributeDateDAO.newInstance();
    dateValue.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, dateCondition.getOid());
    dateValue.setValue(MdAttributeDateInfo.NAME, "value");
    dateValue.setValue(MdAttributeDateInfo.COLUMN_NAME, "fieldValue");
    dateValue.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    dateValue.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value");
    dateValue.setStructValue(MdAttributeDateInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value to check against");
    dateValue.apply();

    MdAttributeEnumerationDAO dateOperation = MdAttributeEnumerationDAO.newInstance();
    dateOperation.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, dateCondition.getOid());
    dateOperation.setValue(MdAttributeEnumerationInfo.NAME, "operation");
    dateOperation.setValue(MdAttributeEnumerationInfo.COLUMN_NAME, "conditionOperation");
    dateOperation.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    dateOperation.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, allOperations.getOid());
    dateOperation.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    dateOperation.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    dateOperation.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    dateOperation.apply();

    MdBusinessDAO doubleCondition = MdBusinessDAO.newInstance();
    doubleCondition.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, condition.getOid());
    doubleCondition.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    doubleCondition.setValue(MdBusinessInfo.NAME, "DoubleCondition");
    doubleCondition.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double condition");
    doubleCondition.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A condition which check a field value against a double value");
    doubleCondition.apply();

    MdAttributeReferenceDAO doubleDefiningMdField = MdAttributeReferenceDAO.newInstance();
    doubleDefiningMdField.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, doubleCondition.getOid());
    doubleDefiningMdField.setValue(MdAttributeReferenceInfo.NAME, "definingMdField");
    doubleDefiningMdField.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    doubleDefiningMdField.setValue(MdAttributeReferenceInfo.REF_MD_BUSINESS, mdField.getOid());
    doubleDefiningMdField.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Defining Field");
    doubleDefiningMdField.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Field upon which the criteria is checked");
    doubleDefiningMdField.apply();

    MdAttributeDoubleDAO doubleValue = MdAttributeDoubleDAO.newInstance();
    doubleValue.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, doubleCondition.getOid());
    doubleValue.setValue(MdAttributeDoubleInfo.NAME, "value");
    doubleValue.setValue(MdAttributeDoubleInfo.COLUMN_NAME, "fieldValue");
    doubleValue.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    doubleValue.setValue(MdAttributeDoubleInfo.DECIMAL, "10");
    doubleValue.setValue(MdAttributeDoubleInfo.LENGTH, "64");
    doubleValue.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    doubleValue.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value");
    doubleValue.setStructValue(MdAttributeDoubleInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value to check against");
    doubleValue.apply();

    MdAttributeEnumerationDAO doubleOperation = MdAttributeEnumerationDAO.newInstance();
    doubleOperation.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, doubleCondition.getOid());
    doubleOperation.setValue(MdAttributeEnumerationInfo.NAME, "operation");
    doubleOperation.setValue(MdAttributeEnumerationInfo.COLUMN_NAME, "conditionOperation");
    doubleOperation.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    doubleOperation.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, allOperations.getOid());
    doubleOperation.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    doubleOperation.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    doubleOperation.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    doubleOperation.apply();

    MdBusinessDAO longCondition = MdBusinessDAO.newInstance();
    longCondition.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, condition.getOid());
    longCondition.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    longCondition.setValue(MdBusinessInfo.NAME, "LongCondition");
    longCondition.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long condition");
    longCondition.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A condition which check a field value against a long value");
    longCondition.apply();

    MdAttributeReferenceDAO longDefiningMdField = MdAttributeReferenceDAO.newInstance();
    longDefiningMdField.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, longCondition.getOid());
    longDefiningMdField.setValue(MdAttributeReferenceInfo.NAME, "definingMdField");
    longDefiningMdField.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    longDefiningMdField.setValue(MdAttributeReferenceInfo.REF_MD_BUSINESS, mdField.getOid());
    longDefiningMdField.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Defining Field");
    longDefiningMdField.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Field upon which the criteria is checked");
    longDefiningMdField.apply();

    MdAttributeLongDAO longValue = MdAttributeLongDAO.newInstance();
    longValue.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, longCondition.getOid());
    longValue.setValue(MdAttributeLongInfo.NAME, "value");
    longValue.setValue(MdAttributeLongInfo.COLUMN_NAME, "fieldValue");
    longValue.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    longValue.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    longValue.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value");
    longValue.setStructValue(MdAttributeLongInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Value to check against");
    longValue.apply();

    MdAttributeEnumerationDAO longOperation = MdAttributeEnumerationDAO.newInstance();
    longOperation.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, longCondition.getOid());
    longOperation.setValue(MdAttributeEnumerationInfo.NAME, "operation");
    longOperation.setValue(MdAttributeEnumerationInfo.COLUMN_NAME, "conditionOperation");
    longOperation.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    longOperation.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, allOperations.getOid());
    longOperation.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    longOperation.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    longOperation.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operation");
    longOperation.apply();

    MdAttributeReferenceDAO conditionAttributes = MdAttributeReferenceDAO.newInstance();
    conditionAttributes.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdField.getOid());
    conditionAttributes.setValue(MdAttributeReferenceInfo.NAME, "fieldCondition");
    conditionAttributes.setValue(MdAttributeReferenceInfo.COLUMN_NAME, "fieldCondition");
    conditionAttributes.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    conditionAttributes.setValue(MdAttributeReferenceInfo.REF_MD_BUSINESS, condition.getOid());
    conditionAttributes.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Condition");
    conditionAttributes.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The condition which must be satisfied");
    conditionAttributes.apply();

    MdBusinessDAO mdWebGroup = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebGroup.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdWebGroup.setValue(MdBusinessInfo.NAME, "MdWebGroup");
    mdWebGroup.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, MdBusinessDAO.getMdBusinessDAO(MdWebFieldInfo.CLASS).getOid());
    mdWebGroup.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group");
    mdWebGroup.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group");
    mdWebGroup.apply();

    MdTreeDAO mdWebTree = MdTreeDAO.newInstance();
    mdWebTree.setValue(MdTreeInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdWebTree.setValue(MdTreeInfo.NAME, "WebGroupField");
    mdWebTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, RelationshipTypes.METADATA_RELATIONSHIP.getOid());
    mdWebTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "FormField");
    mdWebTree.setStructValue(MdTreeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Form Field");
    mdWebTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdWebGroup.getOid());
    mdWebTree.setValue(MdTreeInfo.PARENT_METHOD, "GroupFields");
    mdWebTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdWebTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group");
    mdWebTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, MdBusinessDAO.getMdBusinessDAO(MdWebFieldInfo.CLASS).getOid());
    mdWebTree.setValue(MdTreeInfo.CHILD_METHOD, "WebGroups");
    mdWebTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdWebTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Fields");
    mdWebTree.apply();

    MdBusinessDAO mdMobileGroup = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileGroup.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdMobileGroup.setValue(MdBusinessInfo.NAME, "MdMobileGroup");
    mdMobileGroup.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, MdBusinessDAO.getMdBusinessDAO(MdMobileFieldInfo.CLASS).getOid());
    mdMobileGroup.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group");
    mdMobileGroup.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group");
    mdMobileGroup.apply();

    MdTreeDAO mdMobileTree = MdTreeDAO.newInstance();
    mdMobileTree.setValue(MdTreeInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdMobileTree.setValue(MdTreeInfo.NAME, "MobileGroupField");
    mdMobileTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, RelationshipTypes.METADATA_RELATIONSHIP.getOid());
    mdMobileTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group field");
    mdMobileTree.setStructValue(MdTreeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group field");
    mdMobileTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdMobileGroup.getOid());
    mdMobileTree.setValue(MdTreeInfo.PARENT_METHOD, "GroupFields");
    mdMobileTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdMobileTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Group");
    mdMobileTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, MdBusinessDAO.getMdBusinessDAO(MdMobileFieldInfo.CLASS).getOid());
    mdMobileTree.setValue(MdTreeInfo.CHILD_METHOD, "MobileGroups");
    mdMobileTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdMobileTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Fields");
    mdMobileTree.apply();
  }

  @Transaction
  public static void setupMetadata()
  {
    String pck = Constants.METADATA_PACKAGE;

    MdType mdType = MdBusiness.getMdBusiness(MdType.CLASS);
    MdClass mdClass = MdBusiness.getMdBusiness(MdClass.CLASS);

    // MdForm
    String mdFormName = "MdForm";
    MdBusinessDAO mdForm = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdForm.setValue(MdBusiness.ISABSTRACT, "true");
    mdForm.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdForm.setValue(MdType.TYPENAME, mdFormName);
    mdForm.setValue(MdBusiness.SUPERMDBUSINESS, mdType.getOid());
    mdForm.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdForm");
    mdForm.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Form");
    apply(mdForm);

    // formMdClass
    MdAttributeDAO formMdClass = (MdAttributeDAO) MdAttributeReferenceDAO.newInstance();
    formMdClass.setValue(MdAttributeReference.ATTRIBUTENAME, "formMdClass");
    formMdClass.setValue(MdAttributeReference.REQUIRED, "true");
    formMdClass.setValue(MdAttributeReference.MDBUSINESS, mdClass.getOid());
    formMdClass.setValue(MdAttributeReference.DEFININGMDCLASS, mdForm.getOid());
    formMdClass.setStructValue(MdAttributeReference.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Form Class");
    formMdClass.setStructValue(MdAttributeReference.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The underlying MdClass of this Form");
    apply(formMdClass);

    // form name
    MdAttributeDAO formName = (MdAttributeDAO) MdAttributeCharacterDAO.newInstance();
    formName.setValue(MdAttributeCharacter.ATTRIBUTENAME, "formName");
    formName.setValue(MdAttributeCharacter.REQUIRED, "true");
    formName.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Form Name");
    formName.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The name of the form");
    formName.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdForm.getOid());
    formName.setValue(MdAttributeCharacter.DATABASESIZE, "100");
    apply(formName);

    // ///////////

    // MdWebForm
    String mdWebFormName = "MdWebForm";
    MdBusinessDAO mdWebForm = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebForm.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebForm.setValue(MdType.TYPENAME, mdWebFormName);
    mdWebForm.setValue(MdBusiness.SUPERMDBUSINESS, mdForm.getOid());
    mdWebForm.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdWebForm");
    mdWebForm.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Form");
    apply(mdWebForm);

    // MdMobileForm
    String mdMobileFormName = "MdMobileForm";
    MdBusinessDAO mdMobileForm = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileForm.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileForm.setValue(MdType.TYPENAME, mdMobileFormName);
    mdMobileForm.setValue(MdBusiness.SUPERMDBUSINESS, mdForm.getOid());
    mdMobileForm.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdMobileForm");
    mdMobileForm.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Form");
    apply(mdMobileForm);

    // MdField
    MdBusinessDAO metadata = (MdBusinessDAO) MdBusinessDAO.getMdBusinessDAO(Metadata.CLASS);

    String mdFieldName = "MdField";
    MdBusinessDAO mdField = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdField.setValue(MdBusiness.SUPERMDBUSINESS, metadata.getOid());
    mdField.setValue(MdBusiness.ISABSTRACT, "true");
    mdField.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdField.setValue(MdType.TYPENAME, mdFieldName);
    mdField.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Field");
    mdField.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Field");
    apply(mdField);

    // fieldName
    MdAttributeDAO fieldName = (MdAttributeDAO) MdAttributeCharacterDAO.newInstance();
    fieldName.setValue(MdAttributeCharacter.ATTRIBUTENAME, "fieldName");
    fieldName.setValue(MdAttributeCharacter.REQUIRED, "true");
    fieldName.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Field Name");
    fieldName.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The name of the field.");
    fieldName.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdField.getOid());
    fieldName.setValue(MdAttributeCharacter.DATABASESIZE, "50");
    apply(fieldName);

    // required
    MdAttributeDAO required = MdAttributeBooleanDAO.newInstance();
    required.setValue(MdAttributeBoolean.ATTRIBUTENAME, "required");
    required.setValue(MdAttributeBoolean.REQUIRED, "true");
    required.setValue(MdAttributeBoolean.DEFAULTVALUE, "false");
    required.setStructValue(MdAttributeBoolean.POSITIVEDISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "true");
    required.setStructValue(MdAttributeBoolean.NEGATIVEDISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "false");
    required.setStructValue(MdAttributeBoolean.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Required");
    required.setStructValue(MdAttributeBoolean.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Denotes if this field is required");
    required.setValue(MdAttributeBoolean.DEFININGMDCLASS, mdField.getOid());
    apply(required);

    // order
    MdAttributeDAO order = (MdAttributeDAO) MdAttributeIntegerDAO.newInstance();
    order.setValue(MdAttributeInteger.ATTRIBUTENAME, "fieldOrder");
    order.setValue(MdAttributeInteger.REQUIRED, "true");
    order.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Field Order");
    order.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The order of the field in the form");
    order.setValue(MdAttributeInteger.DEFININGMDCLASS, mdField.getOid());
    apply(order);

    // display label
    MdAttributeDAO fieldDisplay = (MdAttributeDAO) MdAttributeLocalCharacterDAO.newInstance();
    fieldDisplay.setValue(MdAttributeCharacter.ATTRIBUTENAME, "displayLabel");
    fieldDisplay.setValue(MdAttributeCharacter.REQUIRED, "true");
    fieldDisplay.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Label");
    fieldDisplay.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The display label of the form");
    fieldDisplay.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdField.getOid());
    apply(fieldDisplay);

    // /////////////////////////

    // MdWebField
    String mdWebFieldName = "MdWebField";
    MdBusinessDAO mdWebField = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebField.setValue(MdBusiness.ISABSTRACT, "true");
    mdWebField.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebField.setValue(MdType.TYPENAME, mdWebFieldName);
    mdWebField.setValue(MdBusiness.SUPERMDBUSINESS, mdField.getOid());
    mdWebField.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Web Field");
    mdWebField.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Field");
    apply(mdWebField);

    MdAttributeDAO definingMdForm = (MdAttributeDAO) MdAttributeReferenceDAO.newInstance();
    definingMdForm.setValue(MdAttributeReference.ATTRIBUTENAME, "definingMdForm");
    definingMdForm.setValue(MdAttributeReference.REQUIRED, "true");
    definingMdForm.setStructValue(MdAttributeReference.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Defining MdForm");
    definingMdForm.setStructValue(MdAttributeReference.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The defining MdForm of this Field");
    definingMdForm.setValue(MdAttributeReference.MDBUSINESS, mdWebForm.getOid());
    definingMdForm.setValue(MdAttributeReference.DEFININGMDCLASS, mdWebField.getOid());
    apply(definingMdForm);

    defineWebFormFields(mdWebField);

    // MdMobileField
    String mdMobileFieldName = "MdMobileField";
    MdBusinessDAO mdMobileField = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileField.setValue(MdBusiness.ISABSTRACT, "true");
    mdMobileField.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileField.setValue(MdType.TYPENAME, mdMobileFieldName);
    mdMobileField.setValue(MdBusiness.SUPERMDBUSINESS, mdField.getOid());
    mdMobileField.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Field");
    mdMobileField.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Field");
    apply(mdMobileField);

    // definingMdForm
    definingMdForm = (MdAttributeDAO) MdAttributeReferenceDAO.newInstance();
    definingMdForm.setValue(MdAttributeReference.ATTRIBUTENAME, "definingMdForm");
    definingMdForm.setValue(MdAttributeReference.REQUIRED, "true");
    definingMdForm.setStructValue(MdAttributeReference.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Defining MdForm");
    definingMdForm.setStructValue(MdAttributeReference.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The defining MdForm of this Field");
    definingMdForm.setValue(MdAttributeReference.MDBUSINESS, mdMobileForm.getOid());
    definingMdForm.setValue(MdAttributeReference.DEFININGMDCLASS, mdMobileField.getOid());
    apply(definingMdForm);

    defineMobileFormFields(mdMobileField);

    // FormField
    String formFieldName = "FormField";
    MdRelationshipDAO formField = (MdRelationshipDAO) MdRelationshipDAO.newInstance();
    formField.setValue(MdRelationship.ISABSTRACT, "true");
    formField.setValue(MdRelationship.PACKAGENAME, pck);
    formField.setValue(MdRelationship.TYPENAME, formFieldName);
    formField.setStructValue(MdRelationship.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "FormField");
    formField.setStructValue(MdRelationship.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Form Field");
    formField.setValue(MdRelationship.PARENTMDBUSINESS, mdForm.getOid());
    formField.setValue(MdRelationship.PARENTMETHOD, "MdForm");
    formField.setValue(MdRelationship.PARENTCARDINALITY, "1");
    formField.setValue(MdRelationship.PARENTDISPLAYLABEL, "MdForm Parent");
    formField.setValue(MdRelationship.CHILDMDBUSINESS, mdField.getOid());
    formField.setValue(MdRelationship.CHILDMETHOD, "MdFields");
    formField.setValue(MdRelationship.CHILDCARDINALITY, "*");
    formField.setValue(MdRelationship.CHILDDISPLAYLABEL, "MdField Child");
    apply(formField);

    // WebFormField (MdWebForm <-> MdWebField)
    String webFormFieldName = "WebFormField";
    MdRelationshipDAO webFormField = (MdRelationshipDAO) MdRelationshipDAO.newInstance();
    webFormField.setValue(MdRelationship.SUPERMDRELATIONSHIP, formField.getOid());
    webFormField.setValue(MdRelationship.PACKAGENAME, pck);
    webFormField.setValue(MdRelationship.TYPENAME, webFormFieldName);
    webFormField.setStructValue(MdRelationship.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "WebFormField");
    webFormField.setStructValue(MdRelationship.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Web Form Field");
    webFormField.setValue(MdRelationship.PARENTMDBUSINESS, mdWebForm.getOid());
    webFormField.setValue(MdRelationship.PARENTMETHOD, "MdForm");
    webFormField.setValue(MdRelationship.PARENTCARDINALITY, "1");
    webFormField.setValue(MdRelationship.PARENTDISPLAYLABEL, "MdWebForm Parent");
    webFormField.setValue(MdRelationship.CHILDMDBUSINESS, mdWebField.getOid());
    webFormField.setValue(MdRelationship.CHILDMETHOD, "MdFields");
    webFormField.setValue(MdRelationship.CHILDCARDINALITY, "*");
    webFormField.setValue(MdRelationship.CHILDDISPLAYLABEL, "MdWebField Child");
    apply(webFormField);

    // MobileFormField (MdMobileForm <-> MdMobileField)
    String mobileFormField = "MobileFormField";
    MdRelationshipDAO mdFormField = (MdRelationshipDAO) MdRelationshipDAO.newInstance();
    mdFormField.setValue(MdRelationship.SUPERMDRELATIONSHIP, formField.getOid());
    mdFormField.setValue(MdRelationship.PACKAGENAME, pck);
    mdFormField.setValue(MdRelationship.TYPENAME, mobileFormField);
    mdFormField.setStructValue(MdRelationship.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MobileFormField");
    mdFormField.setStructValue(MdRelationship.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Form Field");
    mdFormField.setValue(MdRelationship.PARENTMDBUSINESS, mdMobileForm.getOid());
    mdFormField.setValue(MdRelationship.PARENTMETHOD, "MdForm");
    mdFormField.setValue(MdRelationship.PARENTCARDINALITY, "1");
    mdFormField.setValue(MdRelationship.PARENTDISPLAYLABEL, "MdWebForm Parent");
    mdFormField.setValue(MdRelationship.CHILDMDBUSINESS, mdMobileField.getOid());
    mdFormField.setValue(MdRelationship.CHILDMETHOD, "MdFields");
    mdFormField.setValue(MdRelationship.CHILDCARDINALITY, "*");
    mdFormField.setValue(MdRelationship.CHILDDISPLAYLABEL, "MdWebField Child");
    apply(mdFormField);

    MdMethodDAO getOrderedMdFields = MdMethodDAO.newInstance();
    getOrderedMdFields.setValue(MdMethodInfo.NAME, "getOrderedMdFields");
    getOrderedMdFields.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "GetOrderedMdFields");
    getOrderedMdFields.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns the MdFields in proper field order.");
    getOrderedMdFields.setValue(MdMethodInfo.RETURN_TYPE, MdField.CLASS + "[]");
    getOrderedMdFields.setValue(MdMethodInfo.IS_STATIC, "false");
    getOrderedMdFields.setValue(MdMethodInfo.REF_MD_TYPE, mdForm.getOid());
    apply(getOrderedMdFields);
  }

  private static void defineWebFormFields(MdBusinessDAO mdWebField)
  {
    MdBusinessDAOIF mdAttribute = MdBusinessDAO.getMdBusinessDAO(MdAttributeInfo.CLASS);

    MdBusinessDAO mdWebAttribute = MdBusinessDAO.newInstance();
    mdWebAttribute.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebAttribute.setValue(MdBusiness.ISABSTRACT, "true");
    mdWebAttribute.setValue(MdType.TYPENAME, "MdWebAttribute");
    mdWebAttribute.setValue(MdBusiness.SUPERMDBUSINESS, mdWebField.getOid());
    mdWebAttribute.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdWebAttribute");
    mdWebAttribute.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Attribute");
    apply(mdWebAttribute);

    // definingMdAttribute
    MdAttributeDAO definingMdAttribute = (MdAttributeDAO) MdAttributeReferenceDAO.newInstance();
    definingMdAttribute.setValue(MdAttributeReference.ATTRIBUTENAME, "definingMdAttribute");
    definingMdAttribute.setValue(MdAttributeReference.REQUIRED, "true");
    definingMdAttribute.setStructValue(MdAttributeReference.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Defining MdAttribute");
    definingMdAttribute.setStructValue(MdAttributeReference.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The defining MdAttribute of this Field");
    definingMdAttribute.setValue(MdAttributeReference.MDBUSINESS, mdAttribute.getOid());
    definingMdAttribute.setValue(MdAttributeReference.DEFININGMDCLASS, mdWebAttribute.getOid());
    apply(definingMdAttribute);

    MdBusinessDAO mdPrimitive = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdPrimitive.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdPrimitive.setValue(MdBusiness.ISABSTRACT, "true");
    mdPrimitive.setValue(MdType.TYPENAME, "MdWebPrimitive");
    mdPrimitive.setValue(MdBusiness.SUPERMDBUSINESS, mdWebAttribute.getOid());
    mdPrimitive.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Primitive Web Field");
    mdPrimitive.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Primitive Field");
    apply(mdPrimitive);

    // MdWebCharacter
    MdBusinessDAO mdWebCharacter = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebCharacter.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebCharacter.setValue(MdType.TYPENAME, "MdWebCharacter");
    mdWebCharacter.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdWebCharacter.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Field");
    mdWebCharacter.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Character Field");
    apply(mdWebCharacter);

    MdAttributeDAO maxLength = (MdAttributeDAO) MdAttributeIntegerDAO.newInstance();
    maxLength.setValue(MdAttributeInteger.ATTRIBUTENAME, "maxLength");
    maxLength.setValue(MdAttributeInteger.REQUIRED, "true");
    maxLength.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Max Length");
    maxLength.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The maximum length of the field value.");
    maxLength.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    maxLength.setValue(MdAttributeInteger.DEFININGMDCLASS, mdWebCharacter.getOid());
    apply(maxLength);

    MdAttributeDAO displayLength = (MdAttributeDAO) MdAttributeIntegerDAO.newInstance();
    displayLength.setValue(MdAttributeInteger.ATTRIBUTENAME, "displayLength");
    displayLength.setValue(MdAttributeInteger.REQUIRED, "true");
    displayLength.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Length");
    displayLength.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The displayed length of the field value.");
    displayLength.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    displayLength.setValue(MdAttributeInteger.DEFININGMDCLASS, mdWebCharacter.getOid());
    apply(displayLength);

    // MdWebText
    MdBusinessDAO mdWebText = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebText.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebText.setValue(MdType.TYPENAME, "MdWebText");
    mdWebText.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdWebText.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text Field");
    mdWebText.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Text Field");
    apply(mdWebText);

    MdAttributeDAO height = (MdAttributeDAO) MdAttributeIntegerDAO.newInstance();
    height.setValue(MdAttributeInteger.ATTRIBUTENAME, "height");
    height.setValue(MdAttributeInteger.REQUIRED, "true");
    height.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Height");
    height.setValue(MdAttributeInteger.DEFAULTVALUE, "10");
    height.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The height of the text field.");
    height.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    height.setValue(MdAttributeInteger.REJECTZERO, "true");
    height.setValue(MdAttributeInteger.DEFININGMDCLASS, mdWebText.getOid());
    apply(height);

    MdAttributeDAO width = (MdAttributeDAO) MdAttributeIntegerDAO.newInstance();
    width.setValue(MdAttributeInteger.ATTRIBUTENAME, "width");
    width.setValue(MdAttributeInteger.REQUIRED, "true");
    width.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Width");
    width.setValue(MdAttributeInteger.DEFAULTVALUE, "50");
    width.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The width of the text field.");
    width.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    width.setValue(MdAttributeInteger.REJECTZERO, "true");
    width.setValue(MdAttributeInteger.DEFININGMDCLASS, mdWebText.getOid());
    apply(width);

    // MdWebMoment
    MdBusinessDAO mdWebMoment = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebMoment.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebMoment.setValue(MdBusiness.ISABSTRACT, "true");
    mdWebMoment.setValue(MdType.TYPENAME, "MdWebMoment");
    mdWebMoment.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdWebMoment.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Moment Field");
    mdWebMoment.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Moment Field");
    apply(mdWebMoment);

    // MdWebDate
    MdBusinessDAO mdWebDate = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebDate.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebDate.setValue(MdType.TYPENAME, "MdWebDate");
    mdWebDate.setValue(MdBusiness.SUPERMDBUSINESS, mdWebMoment.getOid());
    mdWebDate.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Field");
    mdWebDate.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Date Field");
    apply(mdWebDate);

    // MdWebDateTime
    MdBusinessDAO mdWebDateTime = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebDateTime.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebDateTime.setValue(MdType.TYPENAME, "MdWebDateTime");
    mdWebDateTime.setValue(MdBusiness.SUPERMDBUSINESS, mdWebMoment.getOid());
    mdWebDateTime.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Time Field");
    mdWebDateTime.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web DateTime Field");
    apply(mdWebDateTime);

    // MdWebTime
    MdBusinessDAO mdWebTime = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebTime.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebTime.setValue(MdType.TYPENAME, "MdWebTime");
    mdWebTime.setValue(MdBusiness.SUPERMDBUSINESS, mdWebMoment.getOid());
    mdWebTime.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Time Field");
    mdWebTime.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Time Field");
    apply(mdWebTime);

    // MdWebNumber
    MdBusinessDAO mdWebNumber = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebNumber.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebNumber.setValue(MdBusiness.ISABSTRACT, "true");
    mdWebNumber.setValue(MdType.TYPENAME, "MdWebNumber");
    mdWebNumber.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdWebNumber.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Number Field");
    mdWebNumber.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Number Field");
    apply(mdWebNumber);

    // startRange
    MdAttributeDAO startRange = MdAttributeCharacterDAO.newInstance();
    startRange.setValue(MdAttributeCharacter.ATTRIBUTENAME, "startRange");
    startRange.setValue(MdAttributeCharacter.DATABASESIZE, "100");
    startRange.setValue(MdAttributeCharacter.REQUIRED, "false");
    startRange.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Range");
    startRange.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The number start range.");
    startRange.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdWebNumber.getOid());
    apply(startRange);

    // endRange
    MdAttributeDAO endRange = MdAttributeCharacterDAO.newInstance();
    endRange.setValue(MdAttributeCharacter.ATTRIBUTENAME, "endRange");
    endRange.setValue(MdAttributeCharacter.DATABASESIZE, "100");
    endRange.setValue(MdAttributeCharacter.REQUIRED, "false");
    endRange.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Range");
    endRange.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The number end range.");
    endRange.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdWebNumber.getOid());
    apply(endRange);

    // MdWebInteger
    MdBusinessDAO mdWebInteger = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebInteger.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebInteger.setValue(MdType.TYPENAME, "MdWebInteger");
    mdWebInteger.setValue(MdBusiness.SUPERMDBUSINESS, mdWebNumber.getOid());
    mdWebInteger.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer Field");
    mdWebInteger.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Integer Field");
    apply(mdWebInteger);

    // MdWebLong
    MdBusinessDAO mdWebLong = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebLong.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebLong.setValue(MdType.TYPENAME, "MdWebLong");
    mdWebLong.setValue(MdBusiness.SUPERMDBUSINESS, mdWebNumber.getOid());
    mdWebLong.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long Field");
    mdWebLong.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Long Field");
    apply(mdWebLong);

    // MdWebDec
    MdBusinessDAO mdWebDec = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebDec.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebDec.setValue(MdBusiness.ISABSTRACT, "true");
    mdWebDec.setValue(MdType.TYPENAME, "MdWebDec");
    mdWebDec.setValue(MdBusiness.SUPERMDBUSINESS, mdWebNumber.getOid());
    mdWebDec.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Dec Field");
    mdWebDec.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Dec Field");
    apply(mdWebDec);

    // precision
    MdAttributeDAO precision = MdAttributeIntegerDAO.newInstance();
    precision.setValue(MdAttributeInteger.ATTRIBUTENAME, "decPrecision");
    precision.setValue(MdAttributeInteger.REQUIRED, "true");
    precision.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Precision");
    precision.setValue(MdAttributeInteger.DEFAULTVALUE, "6");
    precision.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The number of signifcant digits.");
    precision.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    precision.setValue(MdAttributeInteger.REJECTZERO, "true");
    precision.setValue(MdAttributeInteger.DEFININGMDCLASS, mdWebDec.getOid());
    apply(precision);

    // scale
    MdAttributeDAO scale = MdAttributeIntegerDAO.newInstance();
    scale.setValue(MdAttributeInteger.ATTRIBUTENAME, "decScale");
    scale.setValue(MdAttributeInteger.REQUIRED, "false");
    scale.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Scale");
    scale.setValue(MdAttributeInteger.DEFAULTVALUE, "1");
    scale.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The number of decimal digits.");
    scale.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    scale.setValue(MdAttributeInteger.DEFININGMDCLASS, mdWebDec.getOid());
    apply(scale);

    // MdWebDouble
    MdBusinessDAO mdWebDouble = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebDouble.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebDouble.setValue(MdType.TYPENAME, "MdWebDouble");
    mdWebDouble.setValue(MdBusiness.SUPERMDBUSINESS, mdWebDec.getOid());
    mdWebDouble.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double Field");
    mdWebDouble.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Double Field");
    apply(mdWebDouble);

    // MdWebFloat
    MdBusinessDAO mdWebFloat = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebFloat.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebFloat.setValue(MdType.TYPENAME, "MdWebFloat");
    mdWebFloat.setValue(MdBusiness.SUPERMDBUSINESS, mdWebDec.getOid());
    mdWebFloat.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float Field");
    mdWebFloat.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Float Field");
    apply(mdWebFloat);

    // MdWebDecimal
    MdBusinessDAO mdWebDecimal = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebDecimal.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebDecimal.setValue(MdType.TYPENAME, "MdWebDecimal");
    mdWebDecimal.setValue(MdBusiness.SUPERMDBUSINESS, mdWebDec.getOid());
    mdWebDecimal.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Decimal Field");
    mdWebDecimal.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Decimal Field");
    apply(mdWebDecimal);

    // MdWebBoolean
    MdBusinessDAO mdWebBoolean = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebBoolean.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebBoolean.setValue(MdType.TYPENAME, "MdWebBoolean");
    mdWebBoolean.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdWebBoolean.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Boolean Field");
    mdWebBoolean.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Boolean Field");
    apply(mdWebBoolean);

    // MdWebSingleTerm
    MdBusinessDAO mdWebSingleTerm = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebSingleTerm.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebSingleTerm.setValue(MdType.TYPENAME, "MdWebSingleTerm");
    mdWebSingleTerm.setValue(MdBusiness.SUPERMDBUSINESS, mdWebAttribute.getOid());
    mdWebSingleTerm.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Field");
    mdWebSingleTerm.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web SingleTerm Field");
    apply(mdWebSingleTerm);

    // MdWebMultipleTerm
    MdBusinessDAO mdWebMultipleTerm = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebMultipleTerm.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebMultipleTerm.setValue(MdType.TYPENAME, "MdWebMultipleTerm");
    mdWebMultipleTerm.setValue(MdBusiness.SUPERMDBUSINESS, mdWebAttribute.getOid());
    mdWebMultipleTerm.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Multiple Term Field");
    mdWebMultipleTerm.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web MultipleTerm Field");
    apply(mdWebMultipleTerm);

    // MdWebSingleTermGrid
    MdBusinessDAO mdWebSingleTermGrid = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebSingleTermGrid.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebSingleTermGrid.setValue(MdType.TYPENAME, "MdWebSingleTermGrid");
    mdWebSingleTermGrid.setValue(MdBusiness.SUPERMDBUSINESS, mdWebAttribute.getOid());
    mdWebSingleTermGrid.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Grid Field");
    mdWebSingleTermGrid.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web SingleTermGrid Field");
    apply(mdWebSingleTermGrid);

    MdRelationshipDAO webGridField = (MdRelationshipDAO) MdRelationshipDAO.newInstance();
    // mdFormField.setValue(MdRelationship.SUPERMDRELATIONSHIP,
    // GridField.getOid());
    webGridField.setValue(MdRelationship.PACKAGENAME, Constants.METADATA_PACKAGE);
    webGridField.setValue(MdRelationship.TYPENAME, "WebGridField");
    webGridField.setStructValue(MdRelationship.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "WebGridField");
    webGridField.setStructValue(MdRelationship.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Web Grid Field");
    webGridField.setValue(MdRelationship.PARENTMDBUSINESS, mdWebSingleTermGrid.getOid());
    webGridField.setValue(MdRelationship.PARENTMETHOD, "Grid");
    webGridField.setValue(MdRelationship.PARENTCARDINALITY, "1");
    webGridField.setValue(MdRelationship.PARENTDISPLAYLABEL, "Defining Grid Parent");
    webGridField.setValue(MdRelationship.CHILDMDBUSINESS, mdPrimitive.getOid());
    webGridField.setValue(MdRelationship.CHILDMETHOD, "MdFields");
    webGridField.setValue(MdRelationship.CHILDCARDINALITY, "*");
    webGridField.setValue(MdRelationship.CHILDDISPLAYLABEL, "MdWebField Child");
    apply(webGridField);

    // MdWebGeo
    MdBusinessDAO mdWebGeo = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebGeo.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebGeo.setValue(MdType.TYPENAME, "MdWebGeo");
    mdWebGeo.setValue(MdBusiness.SUPERMDBUSINESS, mdWebAttribute.getOid());
    mdWebGeo.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Geo Field");
    mdWebGeo.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web MdWebGeo Field");
    apply(mdWebGeo);

    MdBusinessDAO mdDisplayField = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdDisplayField.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdPrimitive.setValue(MdBusiness.ISABSTRACT, "true");
    mdPrimitive.setValue(MdType.TYPENAME, "MdWebPrimitive");
    mdPrimitive.setValue(MdBusiness.SUPERMDBUSINESS, mdWebAttribute.getOid());
    mdPrimitive.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Primitive Field");
    mdPrimitive.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Primitive Field");
    apply(mdPrimitive);

    // MdWebHeader
    MdBusinessDAO mdWebHeader = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebHeader.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebHeader.setValue(MdType.TYPENAME, "MdWebHeader");
    mdWebHeader.setValue(MdBusiness.SUPERMDBUSINESS, mdWebField.getOid());
    mdWebHeader.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Header");
    mdWebHeader.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Header Field");
    apply(mdWebHeader);

    // header text
    MdAttributeDAO headerText = (MdAttributeDAO) MdAttributeLocalCharacterDAO.newInstance();
    headerText.setValue(MdAttributeCharacter.ATTRIBUTENAME, "headerText");
    headerText.setValue(MdAttributeCharacter.REQUIRED, "false");
    headerText.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Header Text");
    headerText.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The text data for the header.");
    headerText.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdWebHeader.getOid());
    apply(headerText);

    // MdWebBreak
    MdBusinessDAO mdWebBreak = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebBreak.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebBreak.setValue(MdType.TYPENAME, "MdWebBreak");
    mdWebBreak.setValue(MdBusiness.SUPERMDBUSINESS, mdWebField.getOid());
    mdWebBreak.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Break");
    mdWebBreak.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Break Field");
    apply(mdWebBreak);

    // MdWebComment
    MdBusinessDAO mdWebComment = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdWebComment.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdWebComment.setValue(MdType.TYPENAME, "MdWebComment");
    mdWebComment.setValue(MdBusiness.SUPERMDBUSINESS, mdWebField.getOid());
    mdWebComment.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Comment");
    mdWebComment.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Web Comment Field");
    apply(mdWebComment);

    // comment
    MdAttributeDAO comment = (MdAttributeDAO) MdAttributeLocalTextDAO.newInstance();
    comment.setValue(MdAttributeText.ATTRIBUTENAME, "commentText");
    comment.setValue(MdAttributeText.REQUIRED, "false");
    comment.setStructValue(MdAttributeText.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Comment Text");
    comment.setStructValue(MdAttributeText.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The text data for the comment.");
    comment.setValue(MdAttributeText.DEFININGMDCLASS, mdWebComment.getOid());
    apply(comment);
  }

  private static void defineMobileFormFields(MdBusinessDAO mdMobileField)
  {
    MdBusinessDAOIF mdAttribute = MdBusinessDAO.getMdBusinessDAO(MdAttributeInfo.CLASS);

    MdBusinessDAO mdMobileAttribute = MdBusinessDAO.newInstance();
    mdMobileAttribute.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileAttribute.setValue(MdBusiness.ISABSTRACT, "true");
    mdMobileAttribute.setValue(MdType.TYPENAME, "MdMobileAttribute");
    mdMobileAttribute.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileField.getOid());
    mdMobileAttribute.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Attribute");
    mdMobileAttribute.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Attribute");
    apply(mdMobileAttribute);

    // definingMdAttribute
    MdAttributeDAO definingMdAttribute = (MdAttributeDAO) MdAttributeReferenceDAO.newInstance();
    definingMdAttribute.setValue(MdAttributeReference.ATTRIBUTENAME, "definingMdAttribute");
    definingMdAttribute.setValue(MdAttributeReference.REQUIRED, "true");
    definingMdAttribute.setStructValue(MdAttributeReference.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Defining MdAttribute");
    definingMdAttribute.setStructValue(MdAttributeReference.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The defining MdAttribute of this Field");
    definingMdAttribute.setValue(MdAttributeReference.MDBUSINESS, mdAttribute.getOid());
    definingMdAttribute.setValue(MdAttributeReference.DEFININGMDCLASS, mdMobileAttribute.getOid());
    apply(definingMdAttribute);

    MdBusinessDAO mdPrimitive = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdPrimitive.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdPrimitive.setValue(MdBusiness.ISABSTRACT, "true");
    mdPrimitive.setValue(MdType.TYPENAME, "MdMobilePrimitive");
    mdPrimitive.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileAttribute.getOid());
    mdPrimitive.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Primitive Field");
    mdPrimitive.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Primitive Field");
    apply(mdPrimitive);

    // MdMobileCharacter
    MdBusinessDAO mdMobileCharacter = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileCharacter.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileCharacter.setValue(MdType.TYPENAME, "MdMobileCharacter");
    mdMobileCharacter.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdMobileCharacter.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Character Field");
    mdMobileCharacter.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Character Field");
    apply(mdMobileCharacter);

    MdAttributeDAO maxLength = (MdAttributeDAO) MdAttributeIntegerDAO.newInstance();
    maxLength.setValue(MdAttributeInteger.ATTRIBUTENAME, "maxLength");
    maxLength.setValue(MdAttributeInteger.REQUIRED, "true");
    maxLength.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Max Length");
    maxLength.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The maximum length of the field value.");
    maxLength.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    maxLength.setValue(MdAttributeInteger.DEFININGMDCLASS, mdMobileCharacter.getOid());
    apply(maxLength);

    MdAttributeDAO displayLength = (MdAttributeDAO) MdAttributeIntegerDAO.newInstance();
    displayLength.setValue(MdAttributeInteger.ATTRIBUTENAME, "displayLength");
    displayLength.setValue(MdAttributeInteger.REQUIRED, "true");
    displayLength.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Length");
    displayLength.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The displayed length of the field value.");
    displayLength.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    displayLength.setValue(MdAttributeInteger.DEFININGMDCLASS, mdMobileCharacter.getOid());
    apply(displayLength);

    // MdMobileText
    MdBusinessDAO mdMobileText = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileText.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileText.setValue(MdType.TYPENAME, "MdMobileText");
    mdMobileText.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdMobileText.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Text Field");
    mdMobileText.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Text Field");
    apply(mdMobileText);

    // MdMobileMoment
    MdBusinessDAO mdMobileMoment = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileMoment.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileMoment.setValue(MdBusiness.ISABSTRACT, "true");
    mdMobileMoment.setValue(MdType.TYPENAME, "MdMobileMoment");
    mdMobileMoment.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdMobileMoment.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Moment Field");
    mdMobileMoment.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Moment Field");
    apply(mdMobileMoment);

    // MdMobileDate
    MdBusinessDAO mdMobileDate = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileDate.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileDate.setValue(MdType.TYPENAME, "MdMobileDate");
    mdMobileDate.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileMoment.getOid());
    mdMobileDate.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Date Field");
    mdMobileDate.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Date Field");
    apply(mdMobileDate);

    // MdMobileDateTime
    MdBusinessDAO mdMobileDateTime = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileDateTime.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileDateTime.setValue(MdType.TYPENAME, "MdMobileDateTime");
    mdMobileDateTime.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileMoment.getOid());
    mdMobileDateTime.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Date Time Field");
    mdMobileDateTime.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile DateTime Field");
    apply(mdMobileDateTime);

    // MdMobileTime
    MdBusinessDAO mdMobileTime = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileTime.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileTime.setValue(MdType.TYPENAME, "MdMobileTime");
    mdMobileTime.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileMoment.getOid());
    mdMobileTime.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Time Field");
    mdMobileTime.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Time Field");
    apply(mdMobileTime);

    // MdMobileNumber
    MdBusinessDAO mdMobileNumber = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileNumber.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileNumber.setValue(MdBusiness.ISABSTRACT, "true");
    mdMobileNumber.setValue(MdType.TYPENAME, "MdMobileNumber");
    mdMobileNumber.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdMobileNumber.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Number Field");
    mdMobileNumber.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Number Field");
    apply(mdMobileNumber);

    // startRange
    MdAttributeDAO startRange = MdAttributeCharacterDAO.newInstance();
    startRange.setValue(MdAttributeCharacter.ATTRIBUTENAME, "startRange");
    startRange.setValue(MdAttributeCharacter.DATABASESIZE, "100");
    startRange.setValue(MdAttributeCharacter.REQUIRED, "false");
    startRange.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Range");
    startRange.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The number start range.");
    startRange.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdMobileNumber.getOid());
    apply(startRange);

    // endRange
    MdAttributeDAO endRange = MdAttributeCharacterDAO.newInstance();
    endRange.setValue(MdAttributeCharacter.ATTRIBUTENAME, "endRange");
    endRange.setValue(MdAttributeCharacter.DATABASESIZE, "100");
    endRange.setValue(MdAttributeCharacter.REQUIRED, "false");
    endRange.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "End Range");
    endRange.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The number end range.");
    endRange.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdMobileNumber.getOid());
    apply(endRange);

    // MdMobileInteger
    MdBusinessDAO mdMobileInteger = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileInteger.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileInteger.setValue(MdType.TYPENAME, "MdMobileInteger");
    mdMobileInteger.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileNumber.getOid());
    mdMobileInteger.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Integer Field");
    mdMobileInteger.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Integer Field");
    apply(mdMobileInteger);

    // MdMobileLong
    MdBusinessDAO mdMobileLong = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileLong.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileLong.setValue(MdType.TYPENAME, "MdMobileLong");
    mdMobileLong.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileNumber.getOid());
    mdMobileLong.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Long Field");
    mdMobileLong.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Long Field");
    apply(mdMobileLong);

    // MdMobileDec
    MdBusinessDAO mdMobileDec = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileDec.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileDec.setValue(MdBusiness.ISABSTRACT, "true");
    mdMobileDec.setValue(MdType.TYPENAME, "MdMobileDec");
    mdMobileDec.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileNumber.getOid());
    mdMobileDec.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Dec Field");
    mdMobileDec.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Dec Field");
    apply(mdMobileDec);

    // precision
    MdAttributeDAO precision = MdAttributeIntegerDAO.newInstance();
    precision.setValue(MdAttributeInteger.ATTRIBUTENAME, "decPrecision");
    precision.setValue(MdAttributeInteger.REQUIRED, "true");
    precision.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Precision");
    precision.setValue(MdAttributeInteger.DEFAULTVALUE, "6");
    precision.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The number of signifcant digits.");
    precision.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    precision.setValue(MdAttributeInteger.REJECTZERO, "true");
    precision.setValue(MdAttributeInteger.DEFININGMDCLASS, mdMobileDec.getOid());
    apply(precision);

    // scale
    MdAttributeDAO scale = MdAttributeIntegerDAO.newInstance();
    scale.setValue(MdAttributeInteger.ATTRIBUTENAME, "decScale");
    scale.setValue(MdAttributeInteger.REQUIRED, "false");
    scale.setStructValue(MdAttributeInteger.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Scale");
    scale.setValue(MdAttributeInteger.DEFAULTVALUE, "1");
    scale.setStructValue(MdAttributeInteger.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The number of decimal digits.");
    scale.setValue(MdAttributeInteger.REJECTNEGATIVE, "true");
    scale.setValue(MdAttributeInteger.DEFININGMDCLASS, mdMobileDec.getOid());
    apply(scale);

    // MdMobileDouble
    MdBusinessDAO mdMobileDouble = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileDouble.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileDouble.setValue(MdType.TYPENAME, "MdMobileDouble");
    mdMobileDouble.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileDec.getOid());
    mdMobileDouble.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Double Field");
    mdMobileDouble.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Double Field");
    apply(mdMobileDouble);

    // MdMobileFloat
    MdBusinessDAO mdMobileFloat = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileFloat.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileFloat.setValue(MdType.TYPENAME, "MdMobileFloat");
    mdMobileFloat.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileDec.getOid());
    mdMobileFloat.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Float Field");
    mdMobileFloat.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Float Field");
    apply(mdMobileFloat);

    // MdMobileDecimal
    MdBusinessDAO mdMobileDecimal = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileDecimal.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileDecimal.setValue(MdType.TYPENAME, "MdMobileDecimal");
    mdMobileDecimal.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileDec.getOid());
    mdMobileDecimal.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Decimal Field");
    mdMobileDecimal.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Decimal Field");
    apply(mdMobileDecimal);

    // MdMobileBoolean
    MdBusinessDAO mdMobileBoolean = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileBoolean.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileBoolean.setValue(MdType.TYPENAME, "MdMobileBoolean");
    mdMobileBoolean.setValue(MdBusiness.SUPERMDBUSINESS, mdPrimitive.getOid());
    mdMobileBoolean.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Boolean Field");
    mdMobileBoolean.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Boolean Field");
    apply(mdMobileBoolean);

    // MdMobileSingleTerm
    MdBusinessDAO mdMobileSingleTerm = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileSingleTerm.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileSingleTerm.setValue(MdType.TYPENAME, "MdMobileSingleTerm");
    mdMobileSingleTerm.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileAttribute.getOid());
    mdMobileSingleTerm.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Term Field");
    mdMobileSingleTerm.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile SingleTerm Field");
    apply(mdMobileSingleTerm);

    // MdMobileMultipleTerm
    MdBusinessDAO mdMobileMultipleTerm = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileMultipleTerm.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileMultipleTerm.setValue(MdType.TYPENAME, "MdMobileMultipleTerm");
    mdMobileMultipleTerm.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileAttribute.getOid());
    mdMobileMultipleTerm.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Multiple Term Field");
    mdMobileMultipleTerm.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile MultipleTerm Field");
    apply(mdMobileMultipleTerm);

    // MdMobileSingleTermGrid
    MdBusinessDAO mdMobileSingleTermGrid = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileSingleTermGrid.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileSingleTermGrid.setValue(MdType.TYPENAME, "MdMobileSingleTermGrid");
    mdMobileSingleTermGrid.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileAttribute.getOid());
    mdMobileSingleTermGrid.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Grid Field");
    mdMobileSingleTermGrid.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile SingleTermGrid Field");
    apply(mdMobileSingleTermGrid);

    MdRelationshipDAO mobileGridField = (MdRelationshipDAO) MdRelationshipDAO.newInstance();
    // mdFormField.setValue(MdRelationship.SUPERMDRELATIONSHIP,
    // GridField.getOid());
    mobileGridField.setValue(MdRelationship.PACKAGENAME, Constants.METADATA_PACKAGE);
    mobileGridField.setValue(MdRelationship.TYPENAME, "MobileGridField");
    mobileGridField.setStructValue(MdRelationship.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MobileGridField");
    mobileGridField.setStructValue(MdRelationship.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Grid Field");
    mobileGridField.setValue(MdRelationship.PARENTMDBUSINESS, mdMobileSingleTermGrid.getOid());
    mobileGridField.setValue(MdRelationship.PARENTMETHOD, "Grid");
    mobileGridField.setValue(MdRelationship.PARENTCARDINALITY, "1");
    mobileGridField.setValue(MdRelationship.PARENTDISPLAYLABEL, "Defining Grid Parent");
    mobileGridField.setValue(MdRelationship.CHILDMDBUSINESS, mdPrimitive.getOid());
    mobileGridField.setValue(MdRelationship.CHILDMETHOD, "MdFields");
    mobileGridField.setValue(MdRelationship.CHILDCARDINALITY, "*");
    mobileGridField.setValue(MdRelationship.CHILDDISPLAYLABEL, "MdMobileField Child");
    apply(mobileGridField);

    // MdMobileGeo
    MdBusinessDAO mdMobileGeo = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileGeo.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileGeo.setValue(MdType.TYPENAME, "MdMobileGeo");
    mdMobileGeo.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileAttribute.getOid());
    mdMobileGeo.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Geo Field");
    mdMobileGeo.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile MdMobileGeo Field");
    apply(mdMobileGeo);

    MdBusinessDAO mdDisplayField = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdDisplayField.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdPrimitive.setValue(MdBusiness.ISABSTRACT, "true");
    mdPrimitive.setValue(MdType.TYPENAME, "MdMobilePrimitive");
    mdPrimitive.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileAttribute.getOid());
    mdPrimitive.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Primitive Field");
    mdPrimitive.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Primitive Field");
    apply(mdPrimitive);

    // MdMobileHeader
    MdBusinessDAO mdMobileHeader = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileHeader.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileHeader.setValue(MdType.TYPENAME, "MdMobileHeader");
    mdMobileHeader.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileField.getOid());
    mdMobileHeader.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Header");
    mdMobileHeader.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Header Field");
    apply(mdMobileHeader);

    // header text
    MdAttributeDAO headerText = (MdAttributeDAO) MdAttributeLocalCharacterDAO.newInstance();
    headerText.setValue(MdAttributeCharacter.ATTRIBUTENAME, "headerText");
    headerText.setValue(MdAttributeCharacter.REQUIRED, "false");
    headerText.setStructValue(MdAttributeCharacter.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Header Text");
    headerText.setStructValue(MdAttributeCharacter.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The text data for the header.");
    headerText.setValue(MdAttributeCharacter.DEFININGMDCLASS, mdMobileHeader.getOid());
    apply(headerText);

    // MdMobileBreak
    MdBusinessDAO mdMobileBreak = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileBreak.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileBreak.setValue(MdType.TYPENAME, "MdMobileBreak");
    mdMobileBreak.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileField.getOid());
    mdMobileBreak.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Break");
    mdMobileBreak.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Break Field");
    apply(mdMobileBreak);

    // MdMobileComment
    MdBusinessDAO mdMobileComment = (MdBusinessDAO) MdBusinessDAO.newInstance();
    mdMobileComment.setValue(MdType.PACKAGENAME, Constants.METADATA_PACKAGE);
    mdMobileComment.setValue(MdType.TYPENAME, "MdMobileComment");
    mdMobileComment.setValue(MdBusiness.SUPERMDBUSINESS, mdMobileField.getOid());
    mdMobileComment.setStructValue(MdType.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Mobile Comment");
    mdMobileComment.setStructValue(MdType.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata Mobile Comment Field");
    apply(mdMobileComment);

    // comment
    MdAttributeDAO comment = (MdAttributeDAO) MdAttributeLocalTextDAO.newInstance();
    comment.setValue(MdAttributeText.ATTRIBUTENAME, "commentText");
    comment.setValue(MdAttributeText.REQUIRED, "false");
    comment.setStructValue(MdAttributeText.DISPLAYLABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Comment Text");
    comment.setStructValue(MdAttributeText.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The text data for the comment.");
    comment.setValue(MdAttributeText.DEFININGMDCLASS, mdMobileComment.getOid());
    apply(comment);
  }

  private static void apply(MetadataDAO md)
  {
    if (md instanceof MdElementDAO)
    {
      ( (MdElementDAO) md ).setGenerateMdController(false);
    }

    if (!logging)
    {
      System.out.println("[" + ( count++ ) + "] - " + md.getDisplayLabel(Locale.ENGLISH));
    }

    md.apply();
  }

  public static void cu() throws Exception
  {
    // ICompilationUnit cu = null;
    // IJavaElement javaElement =
    // JavaCore.create("trunk/generated/bug/expose/HelloWorld.java");
    //    
    // File file = new File("trunk/generated/bug/expose/HelloWorld.java");
    // byte[] bytes = new byte[(int)file.length()];
    // FileInputStream fis = new FileInputStream(file);
    // fis.read(bytes);
    // String source = new String(bytes);
  }

  public static void ibm() throws Exception
  {
    // Create Project
    // IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    // IProject project = root.getProject("Test");
    // project.create(null);
    // project.open(null);

    // Set the Java nature
    // IProjectDescription description = project.getDescription();
    // description.setNatureIds(new String[] { JavaCore.NATURE_ID });
    // project.setDescription(null, null);
    //    
    // // Set the Java build path
    // IJavaProject javaProject = JavaCore.create(project);
    // IPath source = new Path(SystemProperties.getGeneratedJavaDirectory());
    // IClasspathEntry[] sourcePath = { JavaCore.newSourceEntry(source) };
    // IPath bin = new Path(SystemProperties.getGeneratedClassDirectory());
    //    
    // javaProject.setRawClasspath(sourcePath, bin, null);
    // javaProject.setOption(JavaCore.COMPILER_COMPLIANCE,
    // JavaCore.VERSION_1_5);
    // javaProject.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD,
    // null);
  }
}
