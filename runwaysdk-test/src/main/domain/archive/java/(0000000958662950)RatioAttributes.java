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
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
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
import com.runwaysdk.system.metadata.ontology.OntologyStrategy;

public class Sandbox implements Job
{
  public static void main(String[] args) throws Exception
  {
    importWithDiff();
  }

  @Request
  public static void importWithDiff()
  {
    Database.enableLoggingDMLAndDDLstatements(true);

    addRatioAttributes1();
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

    System.out.println("\n\n" + AggregationFunctionInfo.CLASS_NAME + ": " + aggFuncEnumMdBus.getId() + "\n");

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
    funcMdEnumeration.setValue(MdEnumerationInfo.TABLE_NAME, "indicator_aggregate_function");
    funcMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Function");
    funcMdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Function Enumeration");
    funcMdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    funcMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    funcMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, aggFuncEnumMdBus.getId());
    funcMdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    funcMdEnumeration.apply();

    System.out.println("\n\n" + AggregationFunctionInfo.INDICATOR_FUNCTION_ENUM_CLASS_NAME + ": " + funcMdEnumeration.getId() + "\n");

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

    System.out.println("\n\n" + MathOperatorInfo.CLASS_NAME + ": " + mathOpEnumMdBusiness.getId() + "\n");

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

    System.out.println("\n\nDIV Enum Item Id: " + divOp.getId() + "\n");

    MdEnumerationDAO opsMdEnumeration = MdEnumerationDAO.newInstance();
    opsMdEnumeration.setValue(MdEnumerationInfo.NAME, MathOperatorInfo.INDICATOR_ENUM_CLASS_NAME);
    opsMdEnumeration.setValue(MdEnumerationInfo.PACKAGE, Constants.METADATA_PACKAGE);
    opsMdEnumeration.setValue(MdEnumerationInfo.TABLE_NAME, "indicator_operator");
    opsMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Operator");
    opsMdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Indicator Operator Enumeration");
    opsMdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.FALSE);
    opsMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    opsMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, mathOpEnumMdBusiness.getId());
    opsMdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    opsMdEnumeration.apply();

    System.out.println("\n\n" + MathOperatorInfo.INDICATOR_ENUM_CLASS_NAME + ": " + opsMdEnumeration.getId() + "\n");

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

    System.out.println("\n\n" + IndicatorElementInfo.CLASS_NAME + ": " + indicatorElementMdBusiness.getId() + "\n");

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

    System.out.println("\n\n" + IndicatorPrimitiveInfo.CLASS_NAME + ": " + indicatorPrimitiveMdBusiness.getId() + "\n");

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

    System.out.println("\n\n" + IndicatorCompositeInfo.CLASS_NAME + ": " + indicatorMdBusiness.getId() + "\n");

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

    System.out.println("\n\n" + MdAttributeIndicatorInfo.CLASS_NAME + ": " + mdAttrIndicatorMdBus.getId() + "\n");

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
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, indicatorMdBusiness.getId()); // should
                                                                                // be
                                                                                // indicatorElementMdBusiness.getId()
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, IndicatorCompositeInfo.CLASS);
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "getMdAttributeIndicator");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "getReferencedIndicator");
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, metadataMdRelationship.getId());
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    mdTree.apply();

    System.out.println("\n\n" + mdTree.definesType() + ": " + mdTree.getId() + "\n");

    // MdBusinessDAOIF mdAttrConcreteMdBus =
    // MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);
    //
    // MdBusinessDAOIF mdAttrRatioMdBus =
    // MdBusinessDAO.getMdBusinessDAO(MdAttributeRatioInfo.CLASS);
    //
    // MdAttributeReferenceDAO mdAttrLeftOperand =
    // ((MdAttributeReferenceDAOIF)mdAttrRatioMdBus.definesAttribute(MdAttributeRatioInfo.LEFT_OPERAND)).getBusinessDAO();
    // mdAttrLeftOperand.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY,
    // mdAttrConcreteMdBus.getId());
    // mdAttrLeftOperand.apply();
    //
    // MdAttributeReferenceDAO mdAttrRightOperand =
    // ((MdAttributeReferenceDAOIF)mdAttrRatioMdBus.definesAttribute(MdAttributeRatioInfo.RIGHT_OPERAND)).getBusinessDAO();
    // mdAttrRightOperand.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY,
    // mdAttrConcreteMdBus.getId());
    // mdAttrRightOperand.apply();

    // MdBusinessDAOIF mdAttrConcreteMdBus =
    // MdBusinessDAO.getMdBusinessDAO(MdAttributeConcreteInfo.CLASS);
    // mdAttrConcreteMdBus.printAttributes();

  }
  
  @Transaction
  public static void addRatioAttributes2()
  {
    // MdBusinessDAOIF ratioPrimitiveMdBus =
    // MdBusinessDAO.getMdBusinessDAO(RatioPrimitiveInfo.CLASS);
    //
    // MdAttributeConcreteDAO columNameMdAttr =
    // (MdAttributeConcreteDAO)((MdAttributeConcreteDAOIF)ratioPrimitiveMdBus.definesAttribute(RatioPrimitiveInfo.COLUMN_NAME)).getBusinessDAO();
    // columNameMdAttr.setValue(MdAttributeConcrete.REMOVE,
    // MdAttributeBooleanInfo.TRUE);
    // columNameMdAttr.delete();
    //
    // MdAttributeConcreteDAO seqMdAttr =
    // (MdAttributeConcreteDAO)((MdAttributeConcreteDAOIF)ratioPrimitiveMdBus.definesAttribute(RatioPrimitiveInfo.SEQUENCE)).getBusinessDAO();
    // seqMdAttr.setValue(MdAttributeConcrete.REMOVE,
    // MdAttributeBooleanInfo.TRUE);
    // seqMdAttr.delete();
  }
}
