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
package com.runwaysdk;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.runwaysdk.constants.AggregationFunctionInfo;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.IndicatorElementInfo;
import com.runwaysdk.constants.IndicatorCompositeInfo;
import com.runwaysdk.constants.IndicatorPrimitiveInfo;
import com.runwaysdk.constants.JobOperationInfo;
import com.runwaysdk.constants.MathOperatorInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeIndicatorInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributePrimitiveInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdTableInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.SingleActorInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.IndicatorCompositeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdRelationshipDAOIF;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTableDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.Vault;
import com.runwaysdk.system.metadata.MdAttributeBoolean;
import com.runwaysdk.system.metadata.MdAttributeCharacter;
import com.runwaysdk.system.metadata.MdAttributeDateTime;
import com.runwaysdk.system.metadata.MdAttributeEnumeration;
import com.runwaysdk.system.metadata.MdAttributeIndices;
import com.runwaysdk.system.metadata.MdAttributeInteger;
import com.runwaysdk.system.metadata.MdAttributeLocalCharacter;
import com.runwaysdk.system.metadata.MdAttributeLocalText;
import com.runwaysdk.system.metadata.MdAttributeLong;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;
import com.runwaysdk.system.metadata.MdRelationship;

public class Sandbox implements Job
{
  public static void main(String[] args) throws Exception
  {
//    ratioTest();
    importWithDiff();
    
//    System.out.println(Integer.class.getName());
  }
  
  @Request
  public static void ratioTest()
  {
//    RatioDAO ratioDAO = RatioDAO.newInstance();
  }

  @Request
  public static void importWithDiff()
  {
    Database.enableLoggingDMLAndDDLstatements(true);
    
//    ServerProperties.setAllowModificationOfMdAttribute(true);

    // Sandbox.createGenerateSourceAttribute();
//    Sandbox.deleteMdFacade();
    
//    Sandbox.changeLockedByReference();
    
//    Sandbox.createMdTable();
    
//    testMdTable();
    
    addRatioAttributes1();
  }
  
  @Transaction
  public static void addRatioAttributes2()
  {
//    MdBusinessDAOIF ratioPrimitiveMdBus = MdBusinessDAO.getMdBusinessDAO(RatioPrimitiveInfo.CLASS);
//
//    MdAttributeConcreteDAO columNameMdAttr = (MdAttributeConcreteDAO)((MdAttributeConcreteDAOIF)ratioPrimitiveMdBus.definesAttribute(RatioPrimitiveInfo.COLUMN_NAME)).getBusinessDAO();
//    columNameMdAttr.setValue(MdAttributeConcrete.REMOVE, MdAttributeBooleanInfo.TRUE);
//    columNameMdAttr.delete();
//    
//    MdAttributeConcreteDAO seqMdAttr = (MdAttributeConcreteDAO)((MdAttributeConcreteDAOIF)ratioPrimitiveMdBus.definesAttribute(RatioPrimitiveInfo.SEQUENCE)).getBusinessDAO();
//    seqMdAttr.setValue(MdAttributeConcrete.REMOVE, MdAttributeBooleanInfo.TRUE);
//    seqMdAttr.delete();
  }
  
  @Transaction
  public static void addRatioAttributes1()
  {
    // Add the math operators Enumeration Type
    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);
    
    MdBusinessDAO aggFuncEnumMdBus = MdBusinessDAO.newInstance();
    aggFuncEnumMdBus.setValue(MdBusinessInfo.NAME, AggregationFunctionInfo.CLASS_NAME);
    aggFuncEnumMdBus.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    aggFuncEnumMdBus.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    aggFuncEnumMdBus.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Aggregation Function Master");
    aggFuncEnumMdBus.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Aggregation Function Enumeration Master List");
    aggFuncEnumMdBus.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    aggFuncEnumMdBus.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    aggFuncEnumMdBus.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    aggFuncEnumMdBus.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getId());
    aggFuncEnumMdBus.setGenerateMdController(false);
    aggFuncEnumMdBus.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    aggFuncEnumMdBus.apply();
    
    System.out.println("\n\n"+AggregationFunctionInfo.CLASS_NAME+": "+aggFuncEnumMdBus.getId()+"\n");

    BusinessDAO sumFunction = BusinessDAO.newInstance(aggFuncEnumMdBus.definesType());
    sumFunction.setValue(EnumerationMasterInfo.NAME, AggregationFunctionInfo.SUM);
    sumFunction.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sum");
    sumFunction.apply();
    
    BusinessDAO avgFunction = BusinessDAO.newInstance(aggFuncEnumMdBus.definesType());
    avgFunction.setValue(EnumerationMasterInfo.NAME, AggregationFunctionInfo.AVG);
    avgFunction.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Average");
    avgFunction.apply();
    
    BusinessDAO countFunction = BusinessDAO.newInstance(aggFuncEnumMdBus.definesType());
    countFunction.setValue(EnumerationMasterInfo.NAME, AggregationFunctionInfo.COUNT);
    countFunction.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Count");
    countFunction.apply();

    BusinessDAO minFunction = BusinessDAO.newInstance(aggFuncEnumMdBus.definesType());
    minFunction.setValue(EnumerationMasterInfo.NAME, AggregationFunctionInfo.MIN);
    minFunction.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Minimum");
    minFunction.apply();
    
    BusinessDAO maxFunction = BusinessDAO.newInstance(aggFuncEnumMdBus.definesType());
    maxFunction.setValue(EnumerationMasterInfo.NAME, AggregationFunctionInfo.MAX);
    maxFunction.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Maximum");
    maxFunction.apply();
    
    BusinessDAO sdevFunction = BusinessDAO.newInstance(aggFuncEnumMdBus.definesType());
    sdevFunction.setValue(EnumerationMasterInfo.NAME, AggregationFunctionInfo.STDEV);
    sdevFunction.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Standard Deviation");
    sdevFunction.apply();
    
    MdEnumerationDAO funcMdEnumeration = MdEnumerationDAO.newInstance();
    funcMdEnumeration.setValue(MdEnumerationInfo.NAME, AggregationFunctionInfo.INDICATOR_FUNCTION_ENUM_CLASS_NAME);
    funcMdEnumeration.setValue(MdEnumerationInfo.PACKAGE, Constants.METADATA_PACKAGE);
    funcMdEnumeration.setValue(MdEnumerationInfo.TABLE_NAME,"indicator_aggregate_function");
    funcMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Function");
    funcMdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Function Enumeration");
    funcMdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    funcMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    funcMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, aggFuncEnumMdBus.getId());
    funcMdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    funcMdEnumeration.apply();
    
    System.out.println("\n\n"+AggregationFunctionInfo.INDICATOR_FUNCTION_ENUM_CLASS_NAME+": "+funcMdEnumeration.getId()+"\n");
    
    MdBusinessDAO mathOpEnumMdBusiness = MdBusinessDAO.newInstance();
    mathOpEnumMdBusiness.setValue(MdBusinessInfo.NAME, MathOperatorInfo.CLASS_NAME);
    mathOpEnumMdBusiness.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mathOpEnumMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mathOpEnumMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operator Master");
    mathOpEnumMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Math Operators Enumeration Master List");
    mathOpEnumMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mathOpEnumMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mathOpEnumMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mathOpEnumMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getId());
    mathOpEnumMdBusiness.setGenerateMdController(false);
    mathOpEnumMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    mathOpEnumMdBusiness.apply();
  
    System.out.println("\n\n"+MathOperatorInfo.CLASS_NAME+": "+mathOpEnumMdBusiness.getId()+"\n");
    
    // Define attributes on the enumeration
    MdAttributeCharacterDAO mdAttrCharOpSymbol = MdAttributeCharacterDAO.newInstance();
    mdAttrCharOpSymbol.setValue(MdAttributeCharacterInfo.NAME, MathOperatorInfo.OPERATOR_SYMBOL);
    mdAttrCharOpSymbol.setValue(MdAttributeCharacterInfo.SIZE, "1");
    mdAttrCharOpSymbol.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operator");
    mdAttrCharOpSymbol.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttrCharOpSymbol.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrCharOpSymbol.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdAttrCharOpSymbol.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttrCharOpSymbol.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mathOpEnumMdBusiness.getId());
    mdAttrCharOpSymbol.apply();
    
    BusinessDAO divOp = BusinessDAO.newInstance(mathOpEnumMdBusiness.definesType());
    divOp.setValue(MathOperatorInfo.OPERATOR_SYMBOL, "/");
    divOp.setValue(EnumerationMasterInfo.NAME, "DIV");
    divOp.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "California");
    divOp.apply();
    
    System.out.println("\n\nDIV Enum Item Id: "+divOp.getId()+"\n");
  
    
    MdEnumerationDAO opsMdEnumeration = MdEnumerationDAO.newInstance();
    opsMdEnumeration.setValue(MdEnumerationInfo.NAME, MathOperatorInfo.INDICATOR_ENUM_CLASS_NAME);
    opsMdEnumeration.setValue(MdEnumerationInfo.PACKAGE, Constants.METADATA_PACKAGE);
    opsMdEnumeration.setValue(MdEnumerationInfo.TABLE_NAME,"indicator_operator");
    opsMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Operator");
    opsMdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Operator Enumeration");
    opsMdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    opsMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    opsMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, mathOpEnumMdBusiness.getId());
    opsMdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    opsMdEnumeration.apply();
    
    System.out.println("\n\n"+MathOperatorInfo.INDICATOR_ENUM_CLASS_NAME+": "+opsMdEnumeration.getId()+"\n");
    

    
    // Define Abstract Indicator Element Type
    MdBusinessDAO indicatorElementMdBusiness = MdBusinessDAO.newInstance();
    indicatorElementMdBusiness.setValue(MdBusinessInfo.NAME, IndicatorElementInfo.CLASS_NAME);
    indicatorElementMdBusiness.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    indicatorElementMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    indicatorElementMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Element");
    indicatorElementMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An abstract class representing components that make up an indicator definition. This is the abstract class in the composite pattern.");
    indicatorElementMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    indicatorElementMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    indicatorElementMdBusiness.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    indicatorElementMdBusiness.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    indicatorElementMdBusiness.setGenerateMdController(false);
    indicatorElementMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    indicatorElementMdBusiness.apply();
    
    System.out.println("\n\n"+IndicatorElementInfo.CLASS_NAME+": "+indicatorElementMdBusiness.getId()+"\n");
    
    
    MdBusinessDAO indicatorPrimitiveMdBusiness = MdBusinessDAO.newInstance();
    indicatorPrimitiveMdBusiness.setValue(MdBusinessInfo.NAME, IndicatorPrimitiveInfo.CLASS_NAME);
    indicatorPrimitiveMdBusiness.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    indicatorPrimitiveMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    indicatorPrimitiveMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Primitive");
    indicatorPrimitiveMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The element of an indicator that represents a primitive value. This is the concrete and non-aggregate class in the composite pattern.");
    indicatorPrimitiveMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    indicatorPrimitiveMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    indicatorPrimitiveMdBusiness.setGenerateMdController(false);
    indicatorPrimitiveMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    indicatorPrimitiveMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, indicatorElementMdBusiness.getId());
    indicatorPrimitiveMdBusiness.apply();
    
    System.out.println("\n\n"+IndicatorPrimitiveInfo.CLASS_NAME+": "+indicatorPrimitiveMdBusiness.getId()+"\n");
    
    
    MdBusinessDAOIF mdAttrPrimitiveMdBus = MdBusinessDAO.getMdBusinessDAO(MdAttributePrimitiveInfo.CLASS);
    
    MdAttributeReferenceDAO mdAttrPrimitive = MdAttributeReferenceDAO.newInstance();
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.NAME, IndicatorPrimitiveInfo.MD_ATTRIBUTE_PRIMITIVE);
    mdAttrPrimitive.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdAttributePrimitive");
    mdAttrPrimitive.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference to a primitive attribute metadata that defines the attribute that is used in this indicator element.");
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, indicatorPrimitiveMdBusiness.getId());
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.SYSTEM, MdAttributeBooleanInfo.FALSE);
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.TRUE);
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrPrimitive.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdAttrPrimitiveMdBus.getId());
    mdAttrPrimitive.apply();
    
    MdAttributeEnumerationDAO mdAttrFunction = MdAttributeEnumerationDAO.newInstance();
    mdAttrFunction.setValue(MdAttributeEnumerationInfo.NAME, IndicatorPrimitiveInfo.INDICATOR_FUNCTION);
    mdAttrFunction.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Aggregate Function");
    mdAttrFunction.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The aggregate function that is applied to a primitive attribute type in the indicator equation");
    mdAttrFunction.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrFunction.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttrFunction.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, indicatorPrimitiveMdBusiness.getId());
    mdAttrFunction.setValue(MdAttributeReferenceInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.TRUE);
    mdAttrFunction.setValue(MdAttributeReferenceInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrFunction.setValue(MdAttributeReferenceInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrFunction.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, funcMdEnumeration.getId());
    mdAttrFunction.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttrFunction.apply();
    
    MdBusinessDAO indicatorMdBusiness = MdBusinessDAO.newInstance();
    indicatorMdBusiness.setValue(MdBusinessInfo.NAME, IndicatorCompositeInfo.CLASS_NAME);
    indicatorMdBusiness.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    indicatorMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    indicatorMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator");
    indicatorMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The indicator class with a left operand, operator, and right operand. This is the concrete and aggregate class in the composite pattern.");
    indicatorMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    indicatorMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    indicatorMdBusiness.setGenerateMdController(false);
    indicatorMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    indicatorMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, indicatorElementMdBusiness.getId());
    indicatorMdBusiness.apply();
    
    System.out.println("\n\n"+IndicatorCompositeInfo.CLASS_NAME+": "+indicatorMdBusiness.getId()+"\n");
    
    MdAttributeReferenceDAO mdAttrLeftOperand = MdAttributeReferenceDAO.newInstance();
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.NAME, IndicatorCompositeInfo.LEFT_OPERAND);
    mdAttrLeftOperand.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Left Operand");
    mdAttrLeftOperand.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The left operand in the indicator equation");
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, indicatorMdBusiness.getId());
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.SYSTEM, MdAttributeBooleanInfo.FALSE);
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.TRUE);
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, indicatorElementMdBusiness.getId());
    mdAttrLeftOperand.apply();
    
    MdAttributeEnumerationDAO mdAttrOperand = MdAttributeEnumerationDAO.newInstance();
    mdAttrOperand.setValue(MdAttributeEnumerationInfo.NAME, IndicatorCompositeInfo.OPERATOR);
    mdAttrOperand.setValue(MdAttributeEnumerationInfo.COLUMN_NAME, IndicatorCompositeDAOIF.OPERATOR_COLUMN);
    mdAttrOperand.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Operator");
    mdAttrOperand.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The operator in the indicator equation");
    mdAttrOperand.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrOperand.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttrOperand.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, indicatorMdBusiness.getId());
    mdAttrOperand.setValue(MdAttributeReferenceInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.TRUE);
    mdAttrOperand.setValue(MdAttributeReferenceInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrOperand.setValue(MdAttributeReferenceInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrOperand.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, opsMdEnumeration.getId());
    mdAttrOperand.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttrOperand.apply();
    
    MdAttributeReferenceDAO mdAttrRightOperand = MdAttributeReferenceDAO.newInstance();
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.NAME, IndicatorCompositeInfo.RIGHT_OPERAND);
    mdAttrRightOperand.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Right Operand");
    mdAttrRightOperand.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The right operand in the indicator equation");
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, indicatorMdBusiness.getId());
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.SYSTEM, MdAttributeBooleanInfo.FALSE);
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.TRUE);
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, indicatorElementMdBusiness.getId());
    mdAttrRightOperand.apply();
    
    // Add the the indicator Attribute Type
    MdBusinessDAOIF mdAttrConcreteMdBus = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);
    
    MdBusinessDAO mdAttrIndicatorMdBus = MdBusinessDAO.newInstance();
    mdAttrIndicatorMdBus.setValue(MdBusinessInfo.NAME, MdAttributeIndicatorInfo.CLASS_NAME);
    mdAttrIndicatorMdBus.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttrIndicatorMdBus.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdAttrIndicatorMdBus.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdAttributeIndicator");
    mdAttrIndicatorMdBus.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator attribute metadata");
    mdAttrIndicatorMdBus.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttrIndicatorMdBus.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdAttrIndicatorMdBus.setValue(MdBusinessInfo.HAS_DETERMINISTIC_IDS, MdAttributeBooleanInfo.TRUE);
    mdAttrIndicatorMdBus.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttrConcreteMdBus.getId());
    mdAttrIndicatorMdBus.setGenerateMdController(false);
    mdAttrIndicatorMdBus.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    mdAttrIndicatorMdBus.apply();
    
    System.out.println("\n\n"+MdAttributeIndicatorInfo.CLASS_NAME+": "+mdAttrIndicatorMdBus.getId()+"\n");
    
    MdAttributeReferenceDAO indicatorAttribute = MdAttributeReferenceDAO.newInstance();
    indicatorAttribute.setValue(MdAttributeReferenceInfo.NAME, MdAttributeIndicatorInfo.INDICATOR_ELEMENT);
    indicatorAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator");
    indicatorAttribute.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference to a Indicator object");
    indicatorAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdAttrIndicatorMdBus.getId());
    indicatorAttribute.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    indicatorAttribute.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    indicatorAttribute.setValue(MdAttributeReferenceInfo.SYSTEM, MdAttributeBooleanInfo.FALSE);
    indicatorAttribute.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    indicatorAttribute.setValue(MdAttributeReferenceInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    indicatorAttribute.setValue(MdAttributeReferenceInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.TRUE);
    indicatorAttribute.setValue(MdAttributeReferenceInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    indicatorAttribute.setValue(MdAttributeReferenceInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    indicatorAttribute.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, indicatorElementMdBusiness.getId());
    indicatorAttribute.apply();
    
    
    
    MdRelationshipDAOIF metadataMdRelationship = MdRelationshipDAO.getMdRelationshipDAO(RelationshipTypes.METADATA_RELATIONSHIP.getType());
   
    
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, "AttributeIndicator");
    mdTree.setValue(MdTreeInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.TRUE);
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Indicator");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdAttrIndicatorMdBus.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeIndicatorInfo.CLASS);
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, indicatorMdBusiness.getId()); // should be indicatorElementMdBusiness.getId()
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, IndicatorCompositeInfo.CLASS);
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "getMdAttributeIndicator");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "getReferencedIndicator");
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, metadataMdRelationship.getId());
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    mdTree.apply();
    
    System.out.println("\n\n"+mdTree.definesType()+": "+mdTree.getId()+"\n");
    
    

//    MdBusinessDAOIF mdAttrConcreteMdBus = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);
//    
//    MdBusinessDAOIF mdAttrRatioMdBus = MdBusinessDAO.getMdBusinessDAO(MdAttributeRatioInfo.CLASS);
//
//    MdAttributeReferenceDAO mdAttrLeftOperand = ((MdAttributeReferenceDAOIF)mdAttrRatioMdBus.definesAttribute(MdAttributeRatioInfo.LEFT_OPERAND)).getBusinessDAO();
//    mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdAttrConcreteMdBus.getId());
//    mdAttrLeftOperand.apply();
//    
//    MdAttributeReferenceDAO mdAttrRightOperand = ((MdAttributeReferenceDAOIF)mdAttrRatioMdBus.definesAttribute(MdAttributeRatioInfo.RIGHT_OPERAND)).getBusinessDAO();
//    mdAttrRightOperand.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdAttrConcreteMdBus.getId());
//    mdAttrRightOperand.apply();
    
    
//    MdBusinessDAOIF mdAttrConcreteMdBus = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);
//    mdAttrConcreteMdBus.printAttributes();

  }
  
  public static void testMdTable()
  {
//    MdTableDAO mdTable = MdTableDAO.newInstance();
//    mdTable.setValue(MdTableInfo.NAME, "TestTable");
//    mdTable.setValue(MdTableInfo.PACKAGE, "some.package");
//    mdTable.setStructValue(MdTableInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdTable");
//    mdTable.setValue(MdTableInfo.TABLE_NAME, "md_class");
//    
//    String id = mdTable.apply();
//    
//    System.out.println();
    
    
//    MdBusinessDAO mdTable = MdBusinessDAO.getMdBusinessDAO(MdTableInfo.CLASS).getBusinessDAO();
//    
//    MdAttributeConcreteDAO mdAttribute = (MdAttributeConcreteDAO)mdTable.getMdAttributeDAO(MdTableInfo.TABLE_NAME).getBusinessDAO();
//    mdAttribute.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
//    mdAttribute.apply();
    
//    mdTable.apply();
//    mdTable.printAttributes();
//    
//    MdBusinessDAOIF mdClass = MdBusinessDAO.getMdBusinessDAO(MdClassInfo.CLASS);
    
    MdTableDAO mdTable = MdTableDAO.newInstance();
    mdTable.printAttributes();
    
  }
  
  @Transaction
  public static void createMdTable()
  {
    MdBusinessDAOIF mdClass = MdBusinessDAO.getMdBusinessDAO(MdClassInfo.CLASS);
    
    MdBusinessDAO mdTable = MdBusinessDAO.newInstance();
    mdTable.setValue(MdBusinessInfo.NAME, "MdTable");
    mdTable.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdTable.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdTable");
    mdTable.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata for relational database tables for entities whose lifecylce is not direclty managed");
    mdTable.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdClass.getId());
    mdTable.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTable.setValue(MdBusinessInfo.EXPORTED, MdAttributeBooleanInfo.TRUE);
    mdTable.setValue(MdBusinessInfo.PUBLISH, MdAttributeBooleanInfo.TRUE);
    mdTable.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    mdTable.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    
    String mdTableMdId = mdTable.apply();
    
    System.out.println("MdTable ID: "+mdTableMdId);
   
    
    MdAttributeCharacter tableName = new MdAttributeCharacter();
    tableName.setValue(MdAttributeCharacterInfo.NAME, MdTableInfo.TABLE_NAME);
    tableName.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    tableName.setValue(MdAttributeCharacterInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdTableMdId);
    tableName.setValue(MdAttributeCharacterInfo.COLUMN_NAME, MdTableInfo.COLUMN_TABLE_NAME);
    tableName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table Name");
    tableName.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Name of the table in the database");
    tableName.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.SYSTEM, MdAttributeBooleanInfo.FALSE);
    tableName.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NON_UNIQUE_INDEX.getId());
    tableName.setValue(MdAttributeCharacterInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    tableName.setValue(MdAttributeCharacterInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
    tableName.setValue(MdAttributeCharacterInfo.SIZE, MdTableInfo.MAX_TABLE_NAME);
    tableName.apply();
  }
  
  
  @Transaction
  public static void changeLockedByReference()
  {
    MdBusinessDAOIF mdSingleActor = MdBusinessDAO.getMdBusinessDAO(SingleActorInfo.CLASS);

    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(MdAttributeReferenceInfo.CLASS);
    query.WHERE(query.aCharacter(MdAttributeConcreteInfo.NAME).EQ(MdElementInfo.LOCKED_BY));
    query.AND(query.aReference(MdAttributeReferenceInfo.REF_MD_ENTITY).NE(mdSingleActor));

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    while (iterator.hasNext())
    {
      MdAttributeConcreteDAO lockedBy = (MdAttributeConcreteDAO) iterator.next().getBusinessDAO();
      lockedBy.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdSingleActor.getId());
      lockedBy.apply();
      
//      MdClassDAOIF mdClass = lockedBy.definedByClass();
//      
//      System.out.println(mdClass.getKey());
//      
//      GenerationManager.generate(mdClass);
    }

    // MdBusinessDAOIF mdBusinessDAO =
    // MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);
    // MdAttributeConcreteDAO lockedBy = (MdAttributeConcreteDAO)
    // mdBusinessDAO.definesAttribute(MdElementInfo.LOCKED_BY).getBusinessDAO();
    // lockedBy.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY,
    // mdSingleActor.getId());
    // lockedBy.apply();
  }

  @Request
  public static void changeMdViewCacheType()
  {
    MdBusinessDAO mdBusinessDAO = MdBusinessDAO.getMdBusinessDAO(MdViewInfo.CLASS).getBusinessDAO();

    List<MdBusinessDAOIF> superCasses = mdBusinessDAO.getSuperClasses();

    for (MdBusinessDAOIF mdBusinessDAOIF : superCasses)
    {
      BusinessDAOIF cacheEnumItem = mdBusinessDAOIF.getCacheAlgorithm();

      int cacheCode = new Integer(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue()).intValue();
      System.out.println(mdBusinessDAOIF.definesType() + " " + cacheCode);
    }

    // QueryFactory qf = new QueryFactory();
    // BusinessDAOQuery q = qf.businessDAOQuery(MdElementInfo.CLASS);
    //
    // OIterator<BusinessDAOIF> i = q.getIterator();
    //
    // for (BusinessDAOIF businessDAOIF : i)
    // {
    // MdElementDAOIF mdElementDAOIF = (MdElementDAOIF)businessDAOIF;
    // BusinessDAOIF cacheEnumItem = mdElementDAOIF.getCacheAlgorithm();
    //
    // int cacheCode = new
    // Integer(cacheEnumItem.getAttributeIF(EntityCacheMaster.CACHE_CODE).getValue()).intValue();
    // System.out.println(mdElementDAOIF.definesType()+" "+cacheCode);
    // }

  }

//  private static void deleteMdFacade()
//  {
    // MdBusinessDAO mdFacade =
    // MdBusinessDAO.getMdBusinessDAO(MdFacadeInfo.CLASS).getBusinessDAO();
    // mdFacade.delete();
//  }

  private static int count = 0;

  /*
   * (non-Javadoc)
   * 
   * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
   */
  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException
  {
    System.out.println("JOBS: " + count++);
  }

  /*
   * private static void scheduler() { try { // Grab the Scheduler instance from
   * the Factory SchedulerManager.start();
   * 
   * // specify the job' s details.. JobDetail job =
   * JobBuilder.newJob(Sandbox.class).withIdentity("testJob").build();
   * 
   * // specify the running period of the job Trigger trigger =
   * TriggerBuilder.newTrigger
   * ().withSchedule(SimpleScheduleBuilder.simpleSchedule
   * ().withIntervalInSeconds(3).repeatForever()).build();
   * 
   * // SchedulerManager.schedule(job, trigger);
   * 
   * Thread.currentThread().sleep(10000);
   * 
   * } // catch (SchedulerException e) // { // e.printStackTrace(); // } catch
   * (InterruptedException e) { // TODO Auto-generated catch block
   * e.printStackTrace(); } finally { SchedulerManager.shutdown(); }
   * 
   * }
   */
  private static void createJobOperation(String name, String display)
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(JobOperationInfo.CLASS);
    businessDAO.setValue(EnumerationMasterInfo.NAME, name);
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, display);
    businessDAO.apply();
  }

  @Transaction
  private static void createGenerateSourceAttribute()
  {
    try
    {
      Database.enableLoggingDMLAndDDLstatements(true);

      String[] types = new String[] { MdTypeInfo.CLASS };

      for (String type : types)
      {
        MdBusinessDAO mdBusiness = MdBusinessDAO.getMdBusinessDAO(type).getBusinessDAO();

        MdAttributeBooleanDAO generateSource = MdAttributeBooleanDAO.newInstance();
        generateSource.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdBusiness.getId());
        generateSource.setValue(MdAttributeBooleanInfo.NAME, "generateSource");
        generateSource.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        generateSource.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.TRUE);
        generateSource.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Flag indicating if source should be generate for this type");
        generateSource.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Generate source");
        generateSource.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "True");
        generateSource.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "False");
        generateSource.apply();
      }
    }
    finally
    {
      Database.enableLoggingDMLAndDDLstatements(false);
    }
  }

  @Transaction
  private static void createMdFieldMetadata()
  {
    try
    {
      Database.enableLoggingDMLAndDDLstatements(true);

      String[] types = new String[] { MdWebAttributeInfo.CLASS };

      for (String type : types)
      {
        MdBusinessDAO mdField = MdBusinessDAO.getMdBusinessDAO(type).getBusinessDAO();

        MdAttributeBooleanDAO showOnViewAll = MdAttributeBooleanDAO.newInstance();
        showOnViewAll.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdField.getId());
        showOnViewAll.setValue(MdAttributeBooleanInfo.NAME, "showOnSearch");
        showOnViewAll.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        showOnViewAll.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.TRUE);
        showOnViewAll.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Flag indicating if the field should be shown on the search page.");
        showOnViewAll.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Show on search");
        showOnViewAll.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "True");
        showOnViewAll.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "False");
        showOnViewAll.apply();
      }
    }
    finally
    {
      Database.enableLoggingDMLAndDDLstatements(false);
    }
  }

  @Transaction
  private static void createSchedulerMetadata()
  {
    try
    {
      Database.enableLoggingDMLAndDDLstatements(true);

      MdBusiness enumMasterMdBusinessIF = MdBusiness.getMdBusiness(EnumerationMasterInfo.CLASS);

      // Job Operation
      MdBusinessDAO jobOperation = MdBusinessDAO.newInstance();
      jobOperation.setValue(MdBusinessInfo.NAME, "JobOperation");
      jobOperation.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      jobOperation.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Job Operation");
      jobOperation.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Job Operation");
      jobOperation.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getId());
      jobOperation.setValue(MdBusinessInfo.EXTENDABLE, "false");
      String jobOperationMdId = jobOperation.apply();

      createJobOperation("PAUSE", "Pause");
      createJobOperation("RESUME", "Resume");
      createJobOperation("START", "Start");
      createJobOperation("STOP", "Stop");
      createJobOperation("CANCEL", "Cancel");

      MdEnumerationDAO allJobOperation = MdEnumerationDAO.newInstance();
      allJobOperation.setValue(MdEnumerationInfo.NAME, "AllJobOperation");
      allJobOperation.setValue(MdEnumerationInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      allJobOperation.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Job Operations");
      allJobOperation.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Job Operations");
      allJobOperation.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      allJobOperation.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
      allJobOperation.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, jobOperationMdId);
      allJobOperation.apply();

      // AbstractJob
      MdBusinessDAO abstractJob = MdBusinessDAO.newInstance();
      abstractJob.setValue(MdBusinessInfo.NAME, "AbstractJob");
      abstractJob.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      abstractJob.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "AbstractJob");
      abstractJob.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "AbstractJob");
      abstractJob.setValue(MdBusinessInfo.ABSTRACT, "true");
      abstractJob.setValue(MdBusinessInfo.EXTENDABLE, "true");
      abstractJob.apply();

      MdBusiness abstractJobMd = MdBusiness.get(abstractJob.getId());

      // lastRun::dt
      MdAttributeDateTime lastRun = new MdAttributeDateTime();
      lastRun.setAttributeName("lastRun");
      lastRun.getDisplayLabel().setDefaultValue("Last Run");
      lastRun.getDescription().setDefaultValue("Last Run");
      lastRun.setRequired(false);
      lastRun.setDefiningMdClass(abstractJobMd);
      lastRun.apply();

      // repeat::b
      MdAttributeBoolean repeat = new MdAttributeBoolean();
      repeat.setAttributeName("repeated");
      repeat.getDisplayLabel().setDefaultValue("Repeated");
      repeat.getDescription().setDefaultValue("Repeated");
      repeat.setRequired(true);
      repeat.setDefaultValue(false);
      repeat.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      repeat.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      repeat.setDefiningMdClass(abstractJobMd);
      repeat.apply();

      // pauseable::b
      MdAttributeBoolean pauseable = new MdAttributeBoolean();
      pauseable.setAttributeName("pauseable");
      pauseable.getDisplayLabel().setDefaultValue("Pauseable");
      pauseable.getDescription().setDefaultValue("Pauseable");
      pauseable.setRequired(true);
      pauseable.setDefaultValue(false);
      pauseable.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      pauseable.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      pauseable.setDefiningMdClass(abstractJobMd);
      pauseable.apply();

      // paused::b
      MdAttributeBoolean paused = new MdAttributeBoolean();
      paused.setAttributeName("paused");
      paused.getDisplayLabel().setDefaultValue("Paused");
      paused.getDescription().setDefaultValue("Paused");
      paused.setRequired(true);
      paused.setDefaultValue(false);
      paused.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      paused.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      paused.setDefiningMdClass(abstractJobMd);
      paused.apply();

      // workTotal::i
      MdAttributeInteger workTotal = new MdAttributeInteger();
      workTotal.setAttributeName("workTotal");
      workTotal.getDisplayLabel().setDefaultValue("Work Total");
      workTotal.getDescription().setDefaultValue("Work Total");
      workTotal.setRequired(false);
      workTotal.setRejectNegative(true);
      workTotal.setDefiningMdClass(abstractJobMd);
      workTotal.apply();

      // workProgress::i
      MdAttributeInteger workProgress = new MdAttributeInteger();
      workProgress.setAttributeName("workProgress");
      workProgress.getDisplayLabel().setDefaultValue("Work Progress");
      workProgress.getDescription().setDefaultValue("Work Progress");
      workProgress.setRequired(false);
      workProgress.setRejectNegative(true);
      workProgress.setDefiningMdClass(abstractJobMd);
      workProgress.apply();

      // completed::b
      MdAttributeBoolean completed = new MdAttributeBoolean();
      completed.setAttributeName("completed");
      completed.getDisplayLabel().setDefaultValue("Completed");
      completed.getDescription().setDefaultValue("Completed");
      completed.setRequired(true);
      completed.setDefaultValue(false);
      completed.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      completed.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      completed.setDefiningMdClass(abstractJobMd);
      completed.apply();

      // removeOnComplete::b
      MdAttributeBoolean removeOnComplete = new MdAttributeBoolean();
      removeOnComplete.setAttributeName("removeOnComplete");
      removeOnComplete.getDisplayLabel().setDefaultValue("Remove On Complete");
      removeOnComplete.getDescription().setDefaultValue("Remove On Complete");
      removeOnComplete.setRequired(true);
      removeOnComplete.setDefaultValue(false);
      removeOnComplete.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      removeOnComplete.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      removeOnComplete.setDefiningMdClass(abstractJobMd);
      removeOnComplete.apply();

      // startOnCreate::b
      MdAttributeBoolean startOnCreate = new MdAttributeBoolean();
      startOnCreate.setAttributeName("startOnCreate");
      startOnCreate.getDisplayLabel().setDefaultValue("Start On Create");
      startOnCreate.getDescription().setDefaultValue("Start On Create");
      startOnCreate.setRequired(true);
      startOnCreate.setDefaultValue(false);
      startOnCreate.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      startOnCreate.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      startOnCreate.setDefiningMdClass(abstractJobMd);
      startOnCreate.apply();

      // cancelable::b
      MdAttributeBoolean cancelable = new MdAttributeBoolean();
      cancelable.setAttributeName("cancelable");
      cancelable.getDisplayLabel().setDefaultValue("Cancelable");
      cancelable.getDescription().setDefaultValue("Cancelable");
      cancelable.setRequired(true);
      cancelable.setDefaultValue(false);
      cancelable.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      cancelable.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      cancelable.setDefiningMdClass(abstractJobMd);
      cancelable.apply();

      // canceled::b
      MdAttributeBoolean canceled = new MdAttributeBoolean();
      canceled.setAttributeName("canceled");
      canceled.getDisplayLabel().setDefaultValue("Canceled");
      canceled.getDescription().setDefaultValue("Canceled");
      canceled.setRequired(true);
      canceled.setDefaultValue(false);
      canceled.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      canceled.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      canceled.setDefiningMdClass(abstractJobMd);
      canceled.apply();

      // running::b
      MdAttributeBoolean running = new MdAttributeBoolean();
      running.setAttributeName("running");
      running.getDisplayLabel().setDefaultValue("Running");
      running.getDescription().setDefaultValue("Running");
      running.setRequired(true);
      running.setDefaultValue(false);
      running.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      running.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      running.setDefiningMdClass(abstractJobMd);
      running.apply();

      // maxRetries::i
      MdAttributeInteger maxRetries = new MdAttributeInteger();
      maxRetries.setAttributeName("maxRetries");
      maxRetries.getDisplayLabel().setDefaultValue("Max Retries");
      maxRetries.getDescription().setDefaultValue("Max Retries");
      maxRetries.setRequired(false);
      maxRetries.setRejectNegative(true);
      maxRetries.setDefiningMdClass(abstractJobMd);
      maxRetries.apply();

      // retries::i
      MdAttributeInteger retries = new MdAttributeInteger();
      retries.setAttributeName("retries");
      retries.getDisplayLabel().setDefaultValue("Retries");
      retries.getDescription().setDefaultValue("Retries");
      retries.setRequired(false);
      retries.setRejectNegative(true);
      retries.setDefiningMdClass(abstractJobMd);
      retries.apply();

      // timeout::l
      MdAttributeLong timeout = new MdAttributeLong();
      timeout.setAttributeName("timeout");
      timeout.getDisplayLabel().setDefaultValue("Timeout");
      timeout.getDescription().setDefaultValue("Timeout");
      timeout.setRequired(false);
      timeout.setRejectNegative(true);
      timeout.setDefiningMdClass(abstractJobMd);
      timeout.apply();

      // cron::s
      MdAttributeCharacter cron = new MdAttributeCharacter();
      cron.setAttributeName("cronExpression");
      cron.getDisplayLabel().setDefaultValue("Cron Expression");
      cron.getDescription().setDefaultValue("Cron Expression");
      cron.setRequired(false);
      cron.setDatabaseSize(60);
      cron.setDefiningMdClass(abstractJobMd);
      cron.apply();

      // startTime::dt
      MdAttributeDateTime startTime = new MdAttributeDateTime();
      startTime.setAttributeName("startTime");
      startTime.getDisplayLabel().setDefaultValue("Start Time");
      startTime.getDescription().setDefaultValue("Start Time");
      startTime.setRequired(false);
      startTime.setDefiningMdClass(abstractJobMd);
      startTime.apply();

      // endTime::dt
      MdAttributeDateTime endTime = new MdAttributeDateTime();
      endTime.setAttributeName("endTime");
      endTime.getDisplayLabel().setDefaultValue("End Time");
      endTime.getDescription().setDefaultValue("End Time");
      endTime.setRequired(false);
      endTime.setDefiningMdClass(abstractJobMd);
      endTime.apply();

      // jobOperation::JobOperation
      MdAttributeEnumeration currentJobOperation = new MdAttributeEnumeration();
      currentJobOperation.setValue(MdAttributeEnumerationInfo.NAME, "jobOperation");
      currentJobOperation.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Job Operation");
      currentJobOperation.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "The current Job Operation called on the Job.");
      currentJobOperation.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      currentJobOperation.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      currentJobOperation.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, abstractJobMd.getId());
      currentJobOperation.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, allJobOperation.getId());
      currentJobOperation.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
      currentJobOperation.apply();

      // Job
      MdBusinessDAO executableJob = MdBusinessDAO.newInstance();
      executableJob.setValue(MdBusinessInfo.NAME, "ExecutableJob");
      executableJob.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      executableJob.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Executable Job");
      executableJob.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Executable Job");
      executableJob.setValue(MdBusinessInfo.ABSTRACT, "true");
      executableJob.setValue(MdBusinessInfo.EXTENDABLE, "true");
      /*
       * NOTE: Cache everything to avoid frequent DB fetches as events are fired
       * and need to reference the job.
       */
      executableJob.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      executableJob.setValue(MdBusiness.SUPERMDBUSINESS, abstractJob.getId());
      String executableJobMdId = executableJob.apply();

      MdBusiness jobMd = MdBusiness.get(executableJobMdId);

      /*
       * Define MdMethods for Job
       */
      MdMethodDAO startMethod = MdMethodDAO.newInstance();
      startMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      startMethod.setValue(MdMethodInfo.NAME, "start");
      startMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      startMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Job");
      startMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Start Job");
      startMethod.apply();

      MdMethodDAO stopMethod = MdMethodDAO.newInstance();
      stopMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      stopMethod.setValue(MdMethodInfo.NAME, "stop");
      stopMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      stopMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Stop Job");
      stopMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Stop Job");
      stopMethod.apply();

      MdMethodDAO cancelMethod = MdMethodDAO.newInstance();
      cancelMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      cancelMethod.setValue(MdMethodInfo.NAME, "cancel");
      cancelMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      cancelMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Cancel Job");
      cancelMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Cancel Job");
      cancelMethod.apply();

      MdMethodDAO resumeMethod = MdMethodDAO.newInstance();
      resumeMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      resumeMethod.setValue(MdMethodInfo.NAME, "resume");
      resumeMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      resumeMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Resume Job");
      resumeMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Resume Job");
      resumeMethod.apply();

      MdMethodDAO pauseMethod = MdMethodDAO.newInstance();
      pauseMethod.setValue(MdMethodInfo.REF_MD_TYPE, executableJobMdId);
      pauseMethod.setValue(MdMethodInfo.NAME, "pause");
      pauseMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
      pauseMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Pause Job");
      pauseMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Pause Job");
      pauseMethod.apply();

      // description::lc
      MdAttributeLocalCharacter description = new MdAttributeLocalCharacter();
      description.setAttributeName("description");
      description.getDisplayLabel().setDefaultValue("Description");
      description.getDescription().setDefaultValue("Description");
      description.setRequired(true);
      description.setDefiningMdClass(jobMd);
      description.apply();

      // jobId::c
      MdAttributeCharacter jobId = new MdAttributeCharacter();
      jobId.setAttributeName("jobId");
      jobId.getDisplayLabel().setDefaultValue("Job Id");
      jobId.getDescription().setDefaultValue("Job Id");
      jobId.setRequired(true);
      jobId.setDatabaseSize(64);
      jobId.setValue(MdAttributeCharacter.INDEXTYPE, MdAttributeIndices.UNIQUE_INDEX.getId());
      jobId.setDefiningMdClass(jobMd);
      jobId.apply();

      // Custom Job
      MdBusinessDAO qualifiedTypeJob = MdBusinessDAO.newInstance();
      qualifiedTypeJob.setValue(MdBusinessInfo.NAME, "QualifiedTypeJob");
      qualifiedTypeJob.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      qualifiedTypeJob.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Qualified Type Job");
      qualifiedTypeJob.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Qualified Type Job");
      qualifiedTypeJob.setValue(MdBusinessInfo.ABSTRACT, "false");
      qualifiedTypeJob.setValue(MdBusinessInfo.EXTENDABLE, "true");
      qualifiedTypeJob.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, executableJobMdId);
      qualifiedTypeJob.apply();

      MdBusiness qualifiedTypeJobMd = MdBusiness.get(qualifiedTypeJob.getId());

      // className::s
      MdAttributeCharacter className = new MdAttributeCharacter();
      className.setAttributeName("className");
      className.getDisplayLabel().setDefaultValue("Class Name");
      className.getDescription().setDefaultValue("Class Name");
      className.setRequired(true);
      className.setDatabaseSize(100);
      className.setDefiningMdClass(qualifiedTypeJobMd);
      className.apply();

      // storeHistory::b
      MdAttributeBoolean recordHistory = new MdAttributeBoolean();
      recordHistory.setAttributeName("recordHistory");
      recordHistory.getDisplayLabel().setDefaultValue("Record History");
      recordHistory.getDescription().setDefaultValue("Record History");
      recordHistory.setRequired(true);
      recordHistory.setDefaultValue(true);
      recordHistory.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
      recordHistory.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
      recordHistory.setDefiningMdClass(jobMd);
      recordHistory.apply();

      // AbstractJob
      MdBusinessDAO snapshot = MdBusinessDAO.newInstance();
      snapshot.setValue(MdBusinessInfo.NAME, "JobSnapshot");
      snapshot.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      snapshot.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JobSnapshot");
      snapshot.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JobSnapshot");
      snapshot.setValue(MdBusinessInfo.ABSTRACT, "false");
      snapshot.setValue(MdBusinessInfo.EXTENDABLE, "true");
      snapshot.setValue(MdBusiness.SUPERMDBUSINESS, abstractJob.getId());
      snapshot.apply();

      // MdBusiness snapshotMd = MdBusiness.get(snapshot.getId());

      // JobHistory
      MdBusinessDAO jobHistory = MdBusinessDAO.newInstance();
      jobHistory.setValue(MdBusinessInfo.NAME, "JobHistory");
      jobHistory.setValue(MdBusinessInfo.PACKAGE, Constants.SCHEDULER_PACKAGE);
      jobHistory.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JobHistory");
      jobHistory.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JobHistory");
      jobHistory.setValue(MdBusinessInfo.ABSTRACT, "false");
      jobHistory.setValue(MdBusinessInfo.EXTENDABLE, "true");
      jobHistory.apply();

      MdBusiness jobHistoryMd = MdBusiness.get(jobHistory.getId());

      // entryDate::datetime
      MdAttributeDateTime entryDate = new MdAttributeDateTime();
      entryDate.setAttributeName("entryDate");
      entryDate.getDisplayLabel().setDefaultValue("Entry Date");
      entryDate.getDescription().setDefaultValue("Entry Date");
      entryDate.setRequired(false);
      entryDate.setDefiningMdClass(jobMd);
      entryDate.apply();

      // historyComment::local text
      MdAttributeLocalText comment = new MdAttributeLocalText();
      comment.setAttributeName("historyComment");
      comment.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Comment");
      comment.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "User Friendly History Comment");
      comment.setRequired(false);
      comment.setDefiningMdClass(jobHistoryMd);
      comment.apply();

      // historyInformation::text
      MdAttributeLocalText historyInformation = new MdAttributeLocalText();
      historyInformation.setAttributeName("historyInformation");
      historyInformation.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "History Information");
      historyInformation.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "History Information with Data and Results");
      historyInformation.setRequired(false);
      historyInformation.setDefiningMdClass(jobHistoryMd);
      historyInformation.apply();

      MdAttributeReference jobSnapshot = new MdAttributeReference();
      jobSnapshot.setAttributeName("jobSnapshot");
      jobSnapshot.getDisplayLabel().setDefaultValue("Job Snapshot");
      jobSnapshot.getDescription().setDefaultValue("Job Snapshot");
      jobSnapshot.setRequired(false);
      jobSnapshot.setValue(MdAttributeReference.MDBUSINESS, snapshot.getId());
      jobSnapshot.setDefiningMdClass(jobHistoryMd);
      jobSnapshot.apply();

      // Rel between Job <-> JobHistory
      MdRelationship jobHistoryRel = new MdRelationship();
      jobHistoryRel.setTypeName("JobHistoryRecord");
      jobHistoryRel.setPackageName(Constants.SCHEDULER_PACKAGE);
      jobHistoryRel.getDisplayLabel().setDefaultValue("Job History Records");
      jobHistoryRel.getDescription().setDefaultValue("Job History Records");
      jobHistoryRel.setParentMdBusiness(jobMd);
      jobHistoryRel.setParentCardinality("1");
      jobHistoryRel.setParentMethod("Job");
      jobHistoryRel.getParentDisplayLabel().setDefaultValue(jobMd.getDisplayLabel().getDefaultValue());
      jobHistoryRel.setChildMdBusiness(jobHistoryMd);
      jobHistoryRel.setChildCardinality("*");
      jobHistoryRel.setChildMethod("JobHistory");
      jobHistoryRel.getChildDisplayLabel().setDefaultValue(jobHistoryMd.getDisplayLabel().getDefaultValue());
      jobHistoryRel.setComposition(true);
      jobHistoryRel.apply();
    }
    finally
    {
      Database.enableLoggingDMLAndDDLstatements(false);
    }
  }

  @Request
  private static void changeType()
  {
    changeTypeInTransaction();
  }

  @Transaction
  private static void changeTypeInTransaction()
  {
    MdBusinessDAO idMapping = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.LocalIdMapping").getBusinessDAO();
    idMapping.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    idMapping.apply();

    MdBusinessDAO sessionIdTo = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.SessionIdToMobileIdMapping").getBusinessDAO();
    sessionIdTo.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    sessionIdTo.apply();

    MdBusinessDAO linkedStack = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.mobile.LinkedStackPersistance").getBusinessDAO();
    linkedStack.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    linkedStack.apply();
  }

  @Request
  private static void createType()
  {
    createTypeInTransaction();
  }

  @Transaction
  private static void createTypeInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAO strategyStateMaster = MdBusinessDAO.newInstance();
    strategyStateMaster.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    strategyStateMaster.setValue(MdBusinessInfo.NAME, "StrategyStateMaster");
    strategyStateMaster.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State Master");
    strategyStateMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State Master");
    strategyStateMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    strategyStateMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    strategyStateMaster.setGenerateMdController(false);
    strategyStateMaster.apply();

    BusinessDAO uninitialized = BusinessDAO.newInstance(strategyStateMaster.definesType());
    uninitialized.setValue(EnumerationMasterInfo.NAME, "UNINITIALIZED");
    uninitialized.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Uninitialized");
    uninitialized.apply();

    BusinessDAO initialized = BusinessDAO.newInstance(strategyStateMaster.definesType());
    initialized.setValue(EnumerationMasterInfo.NAME, "INITIALIZED");
    initialized.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Initialized");
    initialized.apply();

    BusinessDAO initializing = BusinessDAO.newInstance(strategyStateMaster.definesType());
    initializing.setValue(EnumerationMasterInfo.NAME, "INITIALIZING");
    initializing.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Initializing");
    initializing.apply();

    MdEnumerationDAO strategyState = MdEnumerationDAO.newInstance();
    strategyState.setValue(MdEnumerationInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    strategyState.setValue(MdEnumerationInfo.NAME, "StrategyState");
    strategyState.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State");
    strategyState.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State");
    strategyState.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, strategyStateMaster.getId());
    strategyState.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    strategyState.apply();

    MdBusinessDAO ontologyStrategy = MdBusinessDAO.newInstance();
    ontologyStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    ontologyStrategy.setValue(MdBusinessInfo.NAME, "OntologyStrategy");
    ontologyStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Ontology Strategy");
    ontologyStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Ontology Strategy");
    ontologyStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    ontologyStrategy.setGenerateMdController(false);
    ontologyStrategy.apply();

    MdAttributeEnumerationDAO state = MdAttributeEnumerationDAO.newInstance();
    state.setValue(MdAttributeEnumerationInfo.NAME, "strategyState");
    state.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State");
    state.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy State");
    state.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    state.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, ontologyStrategy.getId());
    state.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, strategyState.getId());
    state.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    state.apply();

    MdBusinessDAO databaseAllPathsStrategy = MdBusinessDAO.newInstance();
    databaseAllPathsStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    databaseAllPathsStrategy.setValue(MdBusinessInfo.NAME, "DatabaseAllPathsStrategy");
    databaseAllPathsStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Database all paths strategy");
    databaseAllPathsStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Database all paths strategy");
    databaseAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    databaseAllPathsStrategy.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, ontologyStrategy.getId());
    databaseAllPathsStrategy.setGenerateMdController(false);
    databaseAllPathsStrategy.apply();

    MdBusinessDAO postgresAllPathsStrategy = MdBusinessDAO.newInstance();
    postgresAllPathsStrategy.setValue(MdBusinessInfo.PACKAGE, Constants.ONTOLOGY_PACKAGE);
    postgresAllPathsStrategy.setValue(MdBusinessInfo.NAME, "PostgresAllPathsStrategy");
    postgresAllPathsStrategy.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Postgres all paths strategy");
    postgresAllPathsStrategy.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Postgres all paths strategy");
    postgresAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    postgresAllPathsStrategy.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, databaseAllPathsStrategy.getId());
    postgresAllPathsStrategy.setGenerateMdController(false);
    postgresAllPathsStrategy.apply();

    MdBusinessDAOIF mdTerm = MdBusinessDAO.getMdBusinessDAO(MdTermInfo.CLASS);

    MdAttributeReferenceDAO strategy = MdAttributeReferenceDAO.newInstance();
    strategy.setValue(MdAttributeReferenceInfo.NAME, "strategy");
    strategy.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy");
    strategy.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Strategy");
    strategy.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    strategy.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdTerm.getId());
    strategy.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, ontologyStrategy.getId());
    strategy.apply();
  }

  // @Request
  // private static void updateStrategyType()
  // {
  // updateStrategyTypeInTransaction();
  // }

  @Transaction
  private static void updateStrategyTypeInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAO databaseAllPathsStrategy = MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.ontology.DatabaseAllPathsStrategy").getBusinessDAO();
    databaseAllPathsStrategy.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    databaseAllPathsStrategy.setGenerateMdController(false);
    databaseAllPathsStrategy.apply();

    MdBusinessDAO.getMdBusinessDAO("com.runwaysdk.system.metadata.ontology.PostgresAllPathsStrategy").getBusinessDAO().delete();
  }

  // @Request
  // private static void createMdAttributeTerm()
  // {
  // createMdAttributeTermInTransaction();
  // }

  @Transaction
  private static void createMdAttributeTermInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAO mdAttributeReference = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS).getBusinessDAO();
    mdAttributeReference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.apply();

    MdBusinessDAO mdAttributeTerm = MdBusinessDAO.newInstance();
    mdAttributeTerm.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeTerm.setValue(MdBusinessInfo.NAME, "MdAttributeTerm");
    mdAttributeTerm.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata definition for term attributes");
    mdAttributeTerm.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdAttributeTerm");
    mdAttributeTerm.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeTerm.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeReference.getId());
    mdAttributeTerm.setGenerateMdController(false);
    mdAttributeTerm.apply();
  }

  /**
   * 
   */
  @Request
  private static void createMdAttributeMultiReference()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    Sandbox.createMdAttributeMultiReferenceInTransaction();
  }

  @Transaction
  private static void createMdAttributeMultiReferenceInTransaction()
  {
    MdBusinessDAOIF mdBusiness = MdBusinessDAO.getMdBusinessDAO(MdBusinessInfo.CLASS);
    MdBusinessDAOIF mdAttributeConcrete = MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);

    MdBusinessDAO mdAttributeMultiReference = MdBusinessDAO.newInstance();
    mdAttributeMultiReference.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeMultiReference.setValue(MdBusinessInfo.NAME, "MdAttributeMultiReference");
    mdAttributeMultiReference.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata definition for multi reference attributes");
    mdAttributeMultiReference.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdAttributeMultiReference");
    mdAttributeMultiReference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeMultiReference.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeConcrete.getId());
    mdAttributeMultiReference.setGenerateMdController(false);
    mdAttributeMultiReference.apply();

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "mdBusiness");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference MdBusiness");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference MdBusiness");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, mdBusiness.getId());
    mdAttributeReference.apply();

    MdAttributeCharacterDAO tableName = MdAttributeCharacterDAO.newInstance();
    tableName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    tableName.setValue(MdAttributeCharacterInfo.NAME, MdAttributeMultiReferenceInfo.TABLE_NAME);
    tableName.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table name");
    tableName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Table name");
    tableName.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    tableName.setValue(MdAttributeCharacterInfo.SIZE, "255");
    tableName.apply();

    MdAttributeCharacterDAO defaultValue = MdAttributeCharacterDAO.newInstance();
    defaultValue.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdAttributeMultiReference.getId());
    defaultValue.setValue(MdAttributeCharacterInfo.NAME, MdAttributeMultiReferenceInfo.DEFAULT_VALUE);
    defaultValue.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Default value");
    defaultValue.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Default value");
    defaultValue.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    defaultValue.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    defaultValue.setValue(MdAttributeCharacterInfo.SIZE, "64");
    defaultValue.apply();

    MdBusinessDAO mdAttributeMultiTerm = MdBusinessDAO.newInstance();
    mdAttributeMultiTerm.setValue(MdBusinessInfo.PACKAGE, Constants.METADATA_PACKAGE);
    mdAttributeMultiTerm.setValue(MdBusinessInfo.NAME, "MdAttributeMultiTerm");
    mdAttributeMultiTerm.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Metadata definition for multi term attributes");
    mdAttributeMultiTerm.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "MdAttributeMultiTerm");
    mdAttributeMultiTerm.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeMultiTerm.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdAttributeMultiReference.getId());
    mdAttributeMultiTerm.setGenerateMdController(false);
    mdAttributeMultiTerm.apply();
  }

  @Request
  private static void updateVault()
  {
    updateVaultInTransaction();
  }

  @Transaction
  private static void updateVaultInTransaction()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    MdBusinessDAOIF vault = MdBusinessDAO.getMdBusinessDAO(Vault.CLASS);
    // MdAttributeConcreteDAOIF mdAttribute =
    // vault.definesAttribute("vaultPath");
    //
    // mdAttribute.getBusinessDAO().delete();

    MdAttributeCharacterDAO vaultName = MdAttributeCharacterDAO.newInstance();
    vaultName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, vault.getId());
    vaultName.setValue(MdAttributeCharacterInfo.NAME, VaultInfo.VAULT_NAME);
    vaultName.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Vault name");
    vaultName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Vault name");
    vaultName.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    vaultName.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    vaultName.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    vaultName.setValue(MdAttributeCharacterInfo.SIZE, "255");
    vaultName.apply();
  }

}
