/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.query.function;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math.stat.StatUtils;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.QueryMasterSetup;

public class AggregateFunctionMasterSetup
{
  protected static TypeInfo       classQueryInfo            = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "FuncQueryObject");

  protected static final String   FUNC_PREFIX               = "func";

  protected static MdBusinessDAO  mdBusiness                = null;

  protected static TypeInfo       comQueryInfo              = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ComFuncQueryObject");

  protected static final String   COM_FUNC_PREFIX           = "comFunc";

  protected static MdBusinessDAO  comMdBusiness             = null;

  protected static TypeInfo       countQueryInfo            = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "CountFuncQueryObject");

  protected static final String   COUNT_FUNC_PREFIX         = "countFunc";

  protected static MdBusinessDAO  countMdBusiness           = null;

  protected static TypeInfo       sumQueryInfo              = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "SumFuncQueryObject");

  protected static final String   SUM_FUNC_PREFIX           = "sumFunc";

  protected static MdBusinessDAO  sumMdBusiness             = null;

  private static Random           random                    = new Random();

  public static int               numOfObjects              = 20;

  public static List<BusinessDAO> classObjectList           = new LinkedList<BusinessDAO>();

  public static List<BusinessDAO> compareObjectList         = new LinkedList<BusinessDAO>();

  // FLOAT
  private static float[]          floatArray;

  private static float            minFloatValue             = (float) 0.0;

  private static float            maxFloatValue             = (float) 0.0;

  private static float            sumFloatValue             = (float) 0.0;

  private static float            avgFloatValue             = (float) 0.0;

  private static float            stdDevFloatValue          = (float) 0.0;

  private static float            varianceFloatValue        = (float) 0.0;

  public static List<Float>       floatMinGtList            = new LinkedList<Float>();

  public static List<Float>       floatMinGtEqList          = new LinkedList<Float>();

  public static List<Float>       floatMinNotEqList         = new LinkedList<Float>();

  public static List<Float>       floatMaxLtList            = new LinkedList<Float>();

  public static List<Float>       floatMaxLtEqList          = new LinkedList<Float>();

  public static List<Float>       floatMaxNotEqList         = new LinkedList<Float>();

  public static List<Float>       floatEqAvgList            = new LinkedList<Float>();

  public static List<Float>       floatGtAvgList            = new LinkedList<Float>();

  public static List<Float>       floatGtEqAvgList          = new LinkedList<Float>();

  public static List<Float>       floatLtAvgList            = new LinkedList<Float>();

  public static List<Float>       floatLtEqAvgList          = new LinkedList<Float>();

  public static List<Float>       floatNotEqAvgList         = new LinkedList<Float>();

  public static List<Float>       floatEqStdDevList         = new LinkedList<Float>();

  public static List<Float>       floatGtStdDevList         = new LinkedList<Float>();

  public static List<Float>       floatGtEqStdDevList       = new LinkedList<Float>();

  public static List<Float>       floatLtStdDevList         = new LinkedList<Float>();

  public static List<Float>       floatLtEqStdDevList       = new LinkedList<Float>();

  public static List<Float>       floatNotEqStdDevList      = new LinkedList<Float>();

  public static List<Float>       floatEqVarianceList       = new LinkedList<Float>();

  public static List<Float>       floatGtVarianceList       = new LinkedList<Float>();

  public static List<Float>       floatGtEqVarianceList     = new LinkedList<Float>();

  public static List<Float>       floatLtVarianceList       = new LinkedList<Float>();

  public static List<Float>       floatLtEqVarianceList     = new LinkedList<Float>();

  public static List<Float>       floatNotEqVarianceList    = new LinkedList<Float>();

  public static float[]           countFloatArray;

  public static List<Float>       floatEqCountList          = new LinkedList<Float>();

  public static List<Float>       floatGtCountList          = new LinkedList<Float>();

  public static List<Float>       floatGtEqCountList        = new LinkedList<Float>();

  public static List<Float>       floatLtCountList          = new LinkedList<Float>();

  public static List<Float>       floatLtEqCountList        = new LinkedList<Float>();

  public static List<Float>       floatNotEqCountList       = new LinkedList<Float>();

  // DOUBLE
  private static double[]         doubleArray;

  private static double           minDoubleValue            = (double) 0.0;

  private static double           maxDoubleValue            = (double) 0.0;

  private static double           sumDoubleValue            = (double) 0.0;

  private static double           avgDoubleValue            = (double) 0.0;

  private static double           stdDevDoubleValue         = (double) 0.0;

  private static double           varianceDoubleValue       = (double) 0.0;

  public static List<Double>      doubleMinGtList           = new LinkedList<Double>();

  public static List<Double>      doubleMinGtEqList         = new LinkedList<Double>();

  public static List<Double>      doubleMinNotEqList        = new LinkedList<Double>();

  public static List<Double>      doubleMaxLtList           = new LinkedList<Double>();

  public static List<Double>      doubleMaxLtEqList         = new LinkedList<Double>();

  public static List<Double>      doubleMaxNotEqList        = new LinkedList<Double>();

  public static List<Double>      doubleEqAvgList           = new LinkedList<Double>();

  public static List<Double>      doubleGtAvgList           = new LinkedList<Double>();

  public static List<Double>      doubleGtEqAvgList         = new LinkedList<Double>();

  public static List<Double>      doubleLtAvgList           = new LinkedList<Double>();

  public static List<Double>      doubleLtEqAvgList         = new LinkedList<Double>();

  public static List<Double>      doubleNotEqAvgList        = new LinkedList<Double>();

  public static List<Double>      doubleEqStdDevList        = new LinkedList<Double>();

  public static List<Double>      doubleGtStdDevList        = new LinkedList<Double>();

  public static List<Double>      doubleGtEqStdDevList      = new LinkedList<Double>();

  public static List<Double>      doubleLtStdDevList        = new LinkedList<Double>();

  public static List<Double>      doubleLtEqStdDevList      = new LinkedList<Double>();

  public static List<Double>      doubleNotEqStdDevList     = new LinkedList<Double>();

  public static List<Double>      doubleEqVarianceList      = new LinkedList<Double>();

  public static List<Double>      doubleGtVarianceList      = new LinkedList<Double>();

  public static List<Double>      doubleGtEqVarianceList    = new LinkedList<Double>();

  public static List<Double>      doubleLtVarianceList      = new LinkedList<Double>();

  public static List<Double>      doubleLtEqVarianceList    = new LinkedList<Double>();

  public static List<Double>      doubleNotEqVarianceList   = new LinkedList<Double>();

  public static double[]          countDoubleArray;

  public static List<Double>      doubleEqCountList         = new LinkedList<Double>();

  public static List<Double>      doubleGtCountList         = new LinkedList<Double>();

  public static List<Double>      doubleGtEqCountList       = new LinkedList<Double>();

  public static List<Double>      doubleLtCountList         = new LinkedList<Double>();

  public static List<Double>      doubleLtEqCountList       = new LinkedList<Double>();

  public static List<Double>      doubleNotEqCountList      = new LinkedList<Double>();

  // DECIMAL
  private static double[]         decimalArray;

  private static double           minDecimalValue           = 0.0;

  private static double           maxDecimalValue           = 0.0;

  private static double           sumDecimalValue           = 0.0;

  private static double           avgDecimalValue           = 0.0;

  private static double           stdDevDecimalValue        = 0.0;

  private static double           varianceDecimalValue      = 0.0;

  public static List<Double>      decimalMinGtList          = new LinkedList<Double>();

  public static List<Double>      decimalMinGtEqList        = new LinkedList<Double>();

  public static List<Double>      decimalMinNotEqList       = new LinkedList<Double>();

  public static List<Double>      decimalMaxLtList          = new LinkedList<Double>();

  public static List<Double>      decimalMaxLtEqList        = new LinkedList<Double>();

  public static List<Double>      decimalMaxNotEqList       = new LinkedList<Double>();

  public static List<Double>      decimalEqAvgList          = new LinkedList<Double>();

  public static List<Double>      decimalGtAvgList          = new LinkedList<Double>();

  public static List<Double>      decimalGtEqAvgList        = new LinkedList<Double>();

  public static List<Double>      decimalLtAvgList          = new LinkedList<Double>();

  public static List<Double>      decimalLtEqAvgList        = new LinkedList<Double>();

  public static List<Double>      decimalNotEqAvgList       = new LinkedList<Double>();

  public static List<Double>      decimalEqStdDevList       = new LinkedList<Double>();

  public static List<Double>      decimalGtStdDevList       = new LinkedList<Double>();

  public static List<Double>      decimalGtEqStdDevList     = new LinkedList<Double>();

  public static List<Double>      decimalLtStdDevList       = new LinkedList<Double>();

  public static List<Double>      decimalLtEqStdDevList     = new LinkedList<Double>();

  public static List<Double>      decimalNotEqStdDevList    = new LinkedList<Double>();

  public static List<Double>      decimalEqVarianceList     = new LinkedList<Double>();

  public static List<Double>      decimalGtVarianceList     = new LinkedList<Double>();

  public static List<Double>      decimalGtEqVarianceList   = new LinkedList<Double>();

  public static List<Double>      decimalLtVarianceList     = new LinkedList<Double>();

  public static List<Double>      decimalLtEqVarianceList   = new LinkedList<Double>();

  public static List<Double>      decimalNotEqVarianceList  = new LinkedList<Double>();

  public static double[]          countDecimalArray;

  public static List<Double>      decimalEqCountList        = new LinkedList<Double>();

  public static List<Double>      decimalGtCountList        = new LinkedList<Double>();

  public static List<Double>      decimalGtEqCountList      = new LinkedList<Double>();

  public static List<Double>      decimalLtCountList        = new LinkedList<Double>();

  public static List<Double>      decimalLtEqCountList      = new LinkedList<Double>();

  public static List<Double>      decimalNotEqCountList     = new LinkedList<Double>();

  // INTEGER
  private static int[]            integerArray;

  private static int              minIntegerValue           = 0;

  private static int              maxIntegerValue           = 0;

  private static int              sumIntegerValue           = 0;

  private static double           avgIntegerValue           = 0;

  private static double           stdDevIntegerValue        = 0.0;

  private static double           varianceIntegerValue      = 0.0;

  public static List<Integer>     integerMinGtList          = new LinkedList<Integer>();

  public static List<Integer>     integerMinGtEqList        = new LinkedList<Integer>();

  public static List<Integer>     integerMinNotEqList       = new LinkedList<Integer>();

  public static List<Integer>     integerMaxLtList          = new LinkedList<Integer>();

  public static List<Integer>     integerMaxLtEqList        = new LinkedList<Integer>();

  public static List<Integer>     integerMaxNotEqList       = new LinkedList<Integer>();

  public static List<Integer>     integerEqAvgList          = new LinkedList<Integer>();

  public static List<Integer>     integerGtAvgList          = new LinkedList<Integer>();

  public static List<Integer>     integerGtEqAvgList        = new LinkedList<Integer>();

  public static List<Integer>     integerLtAvgList          = new LinkedList<Integer>();

  public static List<Integer>     integerLtEqAvgList        = new LinkedList<Integer>();

  public static List<Integer>     integerNotEqAvgList       = new LinkedList<Integer>();

  public static List<Integer>     integerEqStdDevList       = new LinkedList<Integer>();

  public static List<Integer>     integerGtStdDevList       = new LinkedList<Integer>();

  public static List<Integer>     integerGtEqStdDevList     = new LinkedList<Integer>();

  public static List<Integer>     integerLtStdDevList       = new LinkedList<Integer>();

  public static List<Integer>     integerLtEqStdDevList     = new LinkedList<Integer>();

  public static List<Integer>     integerNotEqStdDevList    = new LinkedList<Integer>();

  public static List<Integer>     integerEqVarianceList     = new LinkedList<Integer>();

  public static List<Integer>     integerGtVarianceList     = new LinkedList<Integer>();

  public static List<Integer>     integerGtEqVarianceList   = new LinkedList<Integer>();

  public static List<Integer>     integerLtVarianceList     = new LinkedList<Integer>();

  public static List<Integer>     integerLtEqVarianceList   = new LinkedList<Integer>();

  public static List<Integer>     integerNotEqVarianceList  = new LinkedList<Integer>();

  public static int[]             countIntegerArray;

  public static List<Integer>     integerEqCountList        = new LinkedList<Integer>();

  public static List<Integer>     integerGtCountList        = new LinkedList<Integer>();

  public static List<Integer>     integerGtEqCountList      = new LinkedList<Integer>();

  public static List<Integer>     integerLtCountList        = new LinkedList<Integer>();

  public static List<Integer>     integerLtEqCountList      = new LinkedList<Integer>();

  public static List<Integer>     integerNotEqCountList     = new LinkedList<Integer>();

  public static int[]             sumIntegerArray;

  public static List<Integer>     integerEqSumList          = new LinkedList<Integer>();

  public static List<Integer>     integerGtSumList          = new LinkedList<Integer>();

  public static List<Integer>     integerGtEqSumList        = new LinkedList<Integer>();

  public static List<Integer>     integerLtSumList          = new LinkedList<Integer>();

  public static List<Integer>     integerLtEqSumList        = new LinkedList<Integer>();

  public static List<Integer>     integerNotEqSumList       = new LinkedList<Integer>();

  // LONG
  private static long[]           longArray;

  private static long             minLongValue              = 0;

  private static long             maxLongValue              = 0;

  private static long             sumLongValue              = 0;

  private static double           avgLongValue              = 0;

  private static double           stdDevLongValue           = 0.0;

  private static double           varianceLongValue         = 0.0;

  public static List<Long>        longMinGtList             = new LinkedList<Long>();

  public static List<Long>        longMinGtEqList           = new LinkedList<Long>();

  public static List<Long>        longMinNotEqList          = new LinkedList<Long>();

  public static List<Long>        longMaxLtList             = new LinkedList<Long>();

  public static List<Long>        longMaxLtEqList           = new LinkedList<Long>();

  public static List<Long>        longMaxNotEqList          = new LinkedList<Long>();

  public static List<Long>        longEqAvgList             = new LinkedList<Long>();

  public static List<Long>        longGtAvgList             = new LinkedList<Long>();

  public static List<Long>        longGtEqAvgList           = new LinkedList<Long>();

  public static List<Long>        longLtAvgList             = new LinkedList<Long>();

  public static List<Long>        longLtEqAvgList           = new LinkedList<Long>();

  public static List<Long>        longNotEqAvgList          = new LinkedList<Long>();

  public static List<Long>        longEqStdDevList          = new LinkedList<Long>();

  public static List<Long>        longGtStdDevList          = new LinkedList<Long>();

  public static List<Long>        longGtEqStdDevList        = new LinkedList<Long>();

  public static List<Long>        longLtStdDevList          = new LinkedList<Long>();

  public static List<Long>        longLtEqStdDevList        = new LinkedList<Long>();

  public static List<Long>        longNotEqStdDevList       = new LinkedList<Long>();

  public static List<Long>        longEqVarianceList        = new LinkedList<Long>();

  public static List<Long>        longGtVarianceList        = new LinkedList<Long>();

  public static List<Long>        longGtEqVarianceList      = new LinkedList<Long>();

  public static List<Long>        longLtVarianceList        = new LinkedList<Long>();

  public static List<Long>        longLtEqVarianceList      = new LinkedList<Long>();

  public static List<Long>        longNotEqVarianceList     = new LinkedList<Long>();

  public static long[]            countLongArray;

  public static List<Long>        longEqCountList           = new LinkedList<Long>();

  public static List<Long>        longGtCountList           = new LinkedList<Long>();

  public static List<Long>        longGtEqCountList         = new LinkedList<Long>();

  public static List<Long>        longLtCountList           = new LinkedList<Long>();

  public static List<Long>        longLtEqCountList         = new LinkedList<Long>();

  public static List<Long>        longNotEqCountList        = new LinkedList<Long>();

  public static long[]            sumLongArray;

  public static List<Long>        longEqSumList             = new LinkedList<Long>();

  public static List<Long>        longGtSumList             = new LinkedList<Long>();

  public static List<Long>        longGtEqSumList           = new LinkedList<Long>();

  public static List<Long>        longLtSumList             = new LinkedList<Long>();

  public static List<Long>        longLtEqSumList           = new LinkedList<Long>();

  public static List<Long>        longNotEqSumList          = new LinkedList<Long>();

  // DATE
  private static long[]           dateArray;

  private static long             minDateValue              = (long) 0;

  private static long             maxDateValue              = (long) 0;

  public static List<Long>        dateMinEqList             = new LinkedList<Long>();

  public static List<Long>        dateMinGtList             = new LinkedList<Long>();

  public static List<Long>        dateMinGtEqList           = new LinkedList<Long>();

  public static List<Long>        dateMinLtEqList           = new LinkedList<Long>();

  public static List<Long>        dateMinNotEqList          = new LinkedList<Long>();

  public static List<Long>        dateMaxGtEqList           = new LinkedList<Long>();

  public static List<Long>        dateMaxLtList             = new LinkedList<Long>();

  public static List<Long>        dateMaxLtEqList           = new LinkedList<Long>();

  public static List<Long>        dateMaxNotEqList          = new LinkedList<Long>();

  public static List<Long>        dateMaxEqList             = new LinkedList<Long>();

  public static List<Long>        dateEqAvgList             = new LinkedList<Long>();

  public static List<Long>        dateGtAvgList             = new LinkedList<Long>();

  public static List<Long>        dateGtEqAvgList           = new LinkedList<Long>();

  public static List<Long>        dateLtAvgList             = new LinkedList<Long>();

  public static List<Long>        dateLtEqAvgList           = new LinkedList<Long>();

  public static List<Long>        dateNotEqAvgList          = new LinkedList<Long>();

  public static List<Long>        dateEqStdDevList          = new LinkedList<Long>();

  public static List<Long>        dateGtStdDevList          = new LinkedList<Long>();

  public static List<Long>        dateGtEqStdDevList        = new LinkedList<Long>();

  public static List<Long>        dateLtStdDevList          = new LinkedList<Long>();

  public static List<Long>        dateLtEqStdDevList        = new LinkedList<Long>();

  public static List<Long>        dateNotEqStdDevList       = new LinkedList<Long>();

  public static List<Long>        dateEqVarianceList        = new LinkedList<Long>();

  public static List<Long>        dateGtVarianceList        = new LinkedList<Long>();

  public static List<Long>        dateGtEqVarianceList      = new LinkedList<Long>();

  public static List<Long>        dateLtVarianceList        = new LinkedList<Long>();

  public static List<Long>        dateLtEqVarianceList      = new LinkedList<Long>();

  public static List<Long>        dateNotEqVarianceList     = new LinkedList<Long>();

  // DATETIME
  private static long[]           dateTimeArray;

  private static long             minDateTimeValue          = (long) 0;

  private static long             maxDateTimeValue          = (long) 0;

  public static List<Long>        dateTimeMinEqList         = new LinkedList<Long>();

  public static List<Long>        dateTimeMinGtList         = new LinkedList<Long>();

  public static List<Long>        dateTimeMinGtEqList       = new LinkedList<Long>();

  public static List<Long>        dateTimeMinLtEqList       = new LinkedList<Long>();

  public static List<Long>        dateTimeMinNotEqList      = new LinkedList<Long>();

  public static List<Long>        dateTimeMaxGtEqList       = new LinkedList<Long>();

  public static List<Long>        dateTimeMaxLtList         = new LinkedList<Long>();

  public static List<Long>        dateTimeMaxLtEqList       = new LinkedList<Long>();

  public static List<Long>        dateTimeMaxNotEqList      = new LinkedList<Long>();

  public static List<Long>        dateTimeMaxEqList         = new LinkedList<Long>();

  public static List<Long>        dateTimeEqAvgList         = new LinkedList<Long>();

  public static List<Long>        dateTimeGtAvgList         = new LinkedList<Long>();

  public static List<Long>        dateTimeGtEqAvgList       = new LinkedList<Long>();

  public static List<Long>        dateTimeLtAvgList         = new LinkedList<Long>();

  public static List<Long>        dateTimeLtEqAvgList       = new LinkedList<Long>();

  public static List<Long>        dateTimeNotEqAvgList      = new LinkedList<Long>();

  public static List<Long>        dateTimeEqStdDevList      = new LinkedList<Long>();

  public static List<Long>        dateTimeGtStdDevList      = new LinkedList<Long>();

  public static List<Long>        dateTimeGtEqStdDevList    = new LinkedList<Long>();

  public static List<Long>        dateTimeLtStdDevList      = new LinkedList<Long>();

  public static List<Long>        dateTimeLtEqStdDevList    = new LinkedList<Long>();

  public static List<Long>        dateTimeNotEqStdDevList   = new LinkedList<Long>();

  public static List<Long>        dateTimeEqVarianceList    = new LinkedList<Long>();

  public static List<Long>        dateTimeGtVarianceList    = new LinkedList<Long>();

  public static List<Long>        dateTimeGtEqVarianceList  = new LinkedList<Long>();

  public static List<Long>        dateTimeLtVarianceList    = new LinkedList<Long>();

  public static List<Long>        dateTimeLtEqVarianceList  = new LinkedList<Long>();

  public static List<Long>        dateTimeNotEqVarianceList = new LinkedList<Long>();

  // TIME
  private static long[]           timeArray;

  private static long             minTimeValue              = (long) 0;

  private static long             maxTimeValue              = (long) 0;

  public static List<Long>        timeMinEqList             = new LinkedList<Long>();

  public static List<Long>        timeMinGtList             = new LinkedList<Long>();

  public static List<Long>        timeMinGtEqList           = new LinkedList<Long>();

  public static List<Long>        timeMinLtEqList           = new LinkedList<Long>();

  public static List<Long>        timeMinNotEqList          = new LinkedList<Long>();

  public static List<Long>        timeMaxGtEqList           = new LinkedList<Long>();

  public static List<Long>        timeMaxLtList             = new LinkedList<Long>();

  public static List<Long>        timeMaxLtEqList           = new LinkedList<Long>();

  public static List<Long>        timeMaxNotEqList          = new LinkedList<Long>();

  public static List<Long>        timeMaxEqList             = new LinkedList<Long>();

  public static List<Long>        timeEqAvgList             = new LinkedList<Long>();

  public static List<Long>        timeGtAvgList             = new LinkedList<Long>();

  public static List<Long>        timeGtEqAvgList           = new LinkedList<Long>();

  public static List<Long>        timeLtAvgList             = new LinkedList<Long>();

  public static List<Long>        timeLtEqAvgList           = new LinkedList<Long>();

  public static List<Long>        timeNotEqAvgList          = new LinkedList<Long>();

  public static List<Long>        timeEqStdDevList          = new LinkedList<Long>();

  public static List<Long>        timeGtStdDevList          = new LinkedList<Long>();

  public static List<Long>        timeGtEqStdDevList        = new LinkedList<Long>();

  public static List<Long>        timeLtStdDevList          = new LinkedList<Long>();

  public static List<Long>        timeLtEqStdDevList        = new LinkedList<Long>();

  public static List<Long>        timeNotEqStdDevList       = new LinkedList<Long>();

  public static List<Long>        timeEqVarianceList        = new LinkedList<Long>();

  public static List<Long>        timeGtVarianceList        = new LinkedList<Long>();

  public static List<Long>        timeGtEqVarianceList      = new LinkedList<Long>();

  public static List<Long>        timeLtVarianceList        = new LinkedList<Long>();

  public static List<Long>        timeLtEqVarianceList      = new LinkedList<Long>();

  public static List<Long>        timeNotEqVarianceList     = new LinkedList<Long>();

  // BOOLEAN
  private static boolean[]        booleanArray;

  private static int              sumBooleanValue           = 0;

  private static float            avgBooleanValue           = (float) 0.0;

  public static List<Boolean>     booleanMinEqList          = new LinkedList<Boolean>();

  public static List<Boolean>     booleanMinNotEqList       = new LinkedList<Boolean>();

  public static List<Boolean>     booleanMaxEqList          = new LinkedList<Boolean>();

  public static List<Boolean>     booleanMaxNotEqList       = new LinkedList<Boolean>();

  public static List<Boolean>     booleanEqAvgList          = new LinkedList<Boolean>();

  public static List<Boolean>     booleanGtAvgList          = new LinkedList<Boolean>();

  public static List<Boolean>     booleanLtAvgList          = new LinkedList<Boolean>();

  public static List<Boolean>     booleanNotEqAvgList       = new LinkedList<Boolean>();

  public AggregateFunctionMasterSetup()
  {
  }

  @Transaction
  public void setUp()
  {
    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, classQueryInfo.getTypeName());
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, classQueryInfo.getPackageName());
    mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Function Object Type");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class used to test query functions");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setGenerateMdController(false);
    mdBusiness.apply();

    QueryMasterSetup.loadAttributePrimitives(mdBusiness, FUNC_PREFIX);

    comMdBusiness = MdBusinessDAO.newInstance();
    comMdBusiness.setValue(MdBusinessInfo.NAME, comQueryInfo.getTypeName());
    comMdBusiness.setValue(MdBusinessInfo.PACKAGE, comQueryInfo.getPackageName());
    comMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    comMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Function Object Type");
    comMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class used to test query functions");
    comMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    comMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    comMdBusiness.setGenerateMdController(false);
    comMdBusiness.apply();

    QueryMasterSetup.loadAttributePrimitives(comMdBusiness, COM_FUNC_PREFIX);

    countMdBusiness = MdBusinessDAO.newInstance();
    countMdBusiness.setValue(MdBusinessInfo.NAME, countQueryInfo.getTypeName());
    countMdBusiness.setValue(MdBusinessInfo.PACKAGE, countQueryInfo.getPackageName());
    countMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    countMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Function Object Type");
    countMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class used to test query functions");
    countMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    countMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    countMdBusiness.setGenerateMdController(false);
    countMdBusiness.apply();

    QueryMasterSetup.loadAttributePrimitives(countMdBusiness, COUNT_FUNC_PREFIX);

    sumMdBusiness = MdBusinessDAO.newInstance();
    sumMdBusiness.setValue(MdBusinessInfo.NAME, sumQueryInfo.getTypeName());
    sumMdBusiness.setValue(MdBusinessInfo.PACKAGE, sumQueryInfo.getPackageName());
    sumMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    sumMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Function Object Type");
    sumMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class used to test query functions");
    sumMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    sumMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    sumMdBusiness.setGenerateMdController(false);
    sumMdBusiness.apply();

    QueryMasterSetup.loadAttributePrimitives(sumMdBusiness, SUM_FUNC_PREFIX);

    floatOracle();
    doubleOracle();
    decimalOracle();
    integerOracle();
    longOracle();
    dateOracle();
    dateTimeOracle();
    timeOracle();
    booleanOracle();

    for (int i = 0; i < numOfObjects; i++)
    {
      long dateLong = dateArray[i];
      Date date = new Date(dateLong);
      String dateString = new java.text.SimpleDateFormat(Constants.DATE_FORMAT).format(date);

      long dateTimeLong = dateTimeArray[i];
      Date dateTime = new Date(dateTimeLong);
      String dateTimeString = new java.text.SimpleDateFormat(Constants.DATETIME_FORMAT).format(dateTime);

      long timeLong = timeArray[i];
      Date time = new Date(timeLong);
      String timeString = new java.text.SimpleDateFormat(Constants.TIME_FORMAT).format(time);

      BusinessDAO classQueryObject = BusinessDAO.newInstance(classQueryInfo.getType());
      classQueryObject.setValue("funcBoolean", Boolean.toString(booleanArray[i]));
      classQueryObject.setValue("funcCharacter", "some character value");
      classQueryObject.setValue("funcText", "some text value");
      classQueryObject.setValue("funcDateTime", dateTimeString);
      classQueryObject.setValue("funcDate", dateString);
      classQueryObject.setValue("funcTime", timeString);
      classQueryObject.setValue("funcInteger", Integer.toString(integerArray[i]));
      classQueryObject.setValue("funcLong", Long.toString(longArray[i]));
      classQueryObject.setValue("funcFloat", Float.toString(floatArray[i]));
      classQueryObject.setValue("funcDecimal", Double.toString(decimalArray[i]));
      classQueryObject.setValue("funcDouble", Double.toString(doubleArray[i]));
      classQueryObject.apply();

      classObjectList.add(classQueryObject);

      BusinessDAO compareQueryObject = BusinessDAO.newInstance(comQueryInfo.getType());
      compareQueryObject.setValue("comFuncBoolean", Boolean.toString(booleanArray[i]));
      compareQueryObject.setValue("comFuncCharacter", "some character value");
      compareQueryObject.setValue("comFuncText", "some text value");
      compareQueryObject.setValue("comFuncDateTime", dateTimeString);
      compareQueryObject.setValue("comFuncDate", dateString);
      compareQueryObject.setValue("comFuncTime", timeString);
      compareQueryObject.setValue("comFuncInteger", Integer.toString(integerArray[i]));
      compareQueryObject.setValue("comFuncLong", Long.toString(longArray[i]));
      compareQueryObject.setValue("comFuncFloat", Float.toString(floatArray[i]));
      compareQueryObject.setValue("comFuncDecimal", Double.toString(decimalArray[i]));
      compareQueryObject.setValue("comFuncDouble", Double.toString(doubleArray[i]));
      compareQueryObject.apply();

      compareObjectList.add(compareQueryObject);
    }

    for (int i = 0; i < countFloatArray.length; i++)
    {
      long dateLong = dateArray[i];
      Date date = new Date(dateLong);
      String dateString = new java.text.SimpleDateFormat(Constants.DATE_FORMAT).format(date);

      long dateTimeLong = dateTimeArray[i];
      Date dateTime = new Date(dateTimeLong);
      String dateTimeString = new java.text.SimpleDateFormat(Constants.DATETIME_FORMAT).format(dateTime);

      long timeLong = timeArray[i];
      Date time = new Date(timeLong);
      String timeString = new java.text.SimpleDateFormat(Constants.TIME_FORMAT).format(time);

      BusinessDAO sumQueryObject = BusinessDAO.newInstance(countQueryInfo.getType());
      sumQueryObject.setValue("countFuncBoolean", Boolean.toString(booleanArray[i]));
      sumQueryObject.setValue("countFuncCharacter", "some character value");
      sumQueryObject.setValue("countFuncText", "some text value");
      sumQueryObject.setValue("countFuncDateTime", dateTimeString);
      sumQueryObject.setValue("countFuncDate", dateString);
      sumQueryObject.setValue("countFuncTime", timeString);
      sumQueryObject.setValue("countFuncInteger", Integer.toString(countIntegerArray[i]));
      sumQueryObject.setValue("countFuncLong", Long.toString(countLongArray[i]));
      sumQueryObject.setValue("countFuncFloat", Float.toString(countFloatArray[i]));
      sumQueryObject.setValue("countFuncDecimal", Double.toString(countDecimalArray[i]));
      sumQueryObject.setValue("countFuncDouble", Double.toString(countDoubleArray[i]));
      sumQueryObject.apply();
    }

    for (int i = 0; i < sumIntegerArray.length; i++)
    {
      long dateLong = dateArray[i];
      Date date = new Date(dateLong);
      String dateString = new java.text.SimpleDateFormat(Constants.DATE_FORMAT).format(date);

      long dateTimeLong = dateTimeArray[i];
      Date dateTime = new Date(dateTimeLong);
      String dateTimeString = new java.text.SimpleDateFormat(Constants.DATETIME_FORMAT).format(dateTime);

      long timeLong = timeArray[i];
      Date time = new Date(timeLong);
      String timeString = new java.text.SimpleDateFormat(Constants.TIME_FORMAT).format(time);

      BusinessDAO sumQueryObject = BusinessDAO.newInstance(sumQueryInfo.getType());
      sumQueryObject.setValue("sumFuncBoolean", Boolean.toString(booleanArray[i]));
      sumQueryObject.setValue("sumFuncCharacter", "some character value");
      sumQueryObject.setValue("sumFuncText", "some text value");
      sumQueryObject.setValue("sumFuncDateTime", dateTimeString);
      sumQueryObject.setValue("sumFuncDate", dateString);
      sumQueryObject.setValue("sumFuncTime", timeString);
      sumQueryObject.setValue("sumFuncInteger", Integer.toString(sumIntegerArray[i]));
      sumQueryObject.setValue("sumFuncLong", Long.toString(sumLongArray[i]));
      sumQueryObject.setValue("sumFuncFloat", Float.toString(floatArray[i]));
      sumQueryObject.setValue("sumFuncDecimal", Double.toString(decimalArray[i]));
      sumQueryObject.setValue("sumFuncDouble", Double.toString(doubleArray[i]));
      sumQueryObject.apply();
    }

    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.ORDER_BY(query.aTime("comFuncTime"), OrderBy.SortOrder.ASC);

  }

  @Transaction
  public void tearDown()
  {
    mdBusiness.delete();
    comMdBusiness.delete();
    countMdBusiness.delete();
    sumMdBusiness.delete();
  }

  private static void floatOracle()
  {
    floatArray = new float[numOfObjects];
    double[] tempDoubleArray = new double[numOfObjects];

    for (int i = 0; i < floatArray.length; i++)
    {
      float floatValue = random.nextFloat() * 1000;
      DecimalFormat df = new DecimalFormat("0.00");
      floatValue = Float.parseFloat(df.format(floatValue));

      if (minFloatValue == 0.0)
        minFloatValue = floatValue;

      if (floatValue < minFloatValue)
        minFloatValue = floatValue;
      if (floatValue > maxFloatValue)
        maxFloatValue = floatValue;

      floatArray[i] = floatValue;
      tempDoubleArray[i] = floatValue;
      sumFloatValue += floatValue;
    }
    Arrays.sort(floatArray);

    avgFloatValue = sumFloatValue / numOfObjects;
    varianceFloatValue = (float) StatUtils.variance(tempDoubleArray);
    stdDevFloatValue = (float) Math.sqrt(varianceFloatValue);

    for (int i = 0; i < floatArray.length; i++)
    {
      // Min
      if (floatArray[i] > minFloatValue)
        floatMinGtList.add(floatArray[i]);
      if (floatArray[i] >= minFloatValue)
        floatMinGtEqList.add(floatArray[i]);
      if (floatArray[i] != minFloatValue)
        floatMinNotEqList.add(floatArray[i]);
      // Max
      if (floatArray[i] < maxFloatValue)
        floatMaxLtList.add(floatArray[i]);
      if (floatArray[i] <= maxFloatValue)
        floatMaxLtEqList.add(floatArray[i]);
      if (floatArray[i] != maxFloatValue)
        floatMaxNotEqList.add(floatArray[i]);
      // Avg
      if (floatArray[i] == avgFloatValue)
        floatEqAvgList.add(floatArray[i]);
      if (floatArray[i] > avgFloatValue)
        floatGtAvgList.add(floatArray[i]);
      if (floatArray[i] >= avgFloatValue)
        floatGtEqAvgList.add(floatArray[i]);
      if (floatArray[i] < avgFloatValue)
        floatLtAvgList.add(floatArray[i]);
      if (floatArray[i] <= avgFloatValue)
        floatLtEqAvgList.add(floatArray[i]);
      if (floatArray[i] != avgFloatValue)
        floatNotEqAvgList.add(floatArray[i]);
      // StdDev
      if (floatArray[i] == stdDevFloatValue)
        floatEqStdDevList.add(floatArray[i]);
      if (floatArray[i] > stdDevFloatValue)
        floatGtStdDevList.add(floatArray[i]);
      if (floatArray[i] >= stdDevFloatValue)
        floatGtEqStdDevList.add(floatArray[i]);
      if (floatArray[i] < stdDevFloatValue)
        floatLtStdDevList.add(floatArray[i]);
      if (floatArray[i] <= stdDevFloatValue)
        floatLtEqStdDevList.add(floatArray[i]);
      if (floatArray[i] != stdDevFloatValue)
        floatNotEqStdDevList.add(floatArray[i]);
      // Variance
      if (floatArray[i] == varianceFloatValue)
        floatEqVarianceList.add(floatArray[i]);
      if (floatArray[i] > varianceFloatValue)
        floatGtVarianceList.add(floatArray[i]);
      if (floatArray[i] >= varianceFloatValue)
        floatGtEqVarianceList.add(floatArray[i]);
      if (floatArray[i] < varianceFloatValue)
        floatLtVarianceList.add(floatArray[i]);
      if (floatArray[i] <= varianceFloatValue)
        floatLtEqVarianceList.add(floatArray[i]);
      if (floatArray[i] != varianceFloatValue)
        floatNotEqVarianceList.add(floatArray[i]);
    }

    countFloatArray = new float[numOfObjects];
    for (int i = 0; i < numOfObjects - 1; i++)
    {
      int countValue;
      if (i % 2 == 0)
        countValue = numOfObjects + i;
      else
        countValue = numOfObjects - i;

      countFloatArray[i] = countValue;
    }
    countFloatArray[countFloatArray.length - 1] = numOfObjects;
    Arrays.sort(countFloatArray);

    for (int i = 0; i < countFloatArray.length; i++)
    {
      // Count
      if (countFloatArray[i] == numOfObjects)
        floatEqCountList.add(countFloatArray[i]);
      if (countFloatArray[i] > numOfObjects)
        floatGtCountList.add(countFloatArray[i]);
      if (countFloatArray[i] >= numOfObjects)
        floatGtEqCountList.add(countFloatArray[i]);
      if (countFloatArray[i] < numOfObjects)
        floatLtCountList.add(countFloatArray[i]);
      if (countFloatArray[i] <= numOfObjects)
        floatLtEqCountList.add(countFloatArray[i]);
      if (countFloatArray[i] != numOfObjects)
        floatNotEqCountList.add(countFloatArray[i]);
    }
  }

  private static void doubleOracle()
  {
    doubleArray = new double[numOfObjects];
    double[] tempDoubleArray = new double[numOfObjects];

    for (int i = 0; i < doubleArray.length; i++)
    {
      double doubleValue = random.nextDouble() * 1000000;
      DecimalFormat df = new DecimalFormat("0.00");
      doubleValue = Double.parseDouble(df.format(doubleValue));

      if (minDoubleValue == 0.0)
        minDoubleValue = doubleValue;

      if (doubleValue < minDoubleValue)
        minDoubleValue = doubleValue;
      if (doubleValue > maxDoubleValue)
        maxDoubleValue = doubleValue;

      doubleArray[i] = doubleValue;
      tempDoubleArray[i] = doubleValue;
      sumDoubleValue += doubleValue;
    }
    Arrays.sort(doubleArray);

    avgDoubleValue = sumDoubleValue / numOfObjects;
    varianceDoubleValue = (double) StatUtils.variance(tempDoubleArray);
    stdDevDoubleValue = (double) Math.sqrt(varianceDoubleValue);

    for (int i = 0; i < doubleArray.length; i++)
    {
      // Min
      if (doubleArray[i] > minDoubleValue)
        doubleMinGtList.add(doubleArray[i]);
      if (doubleArray[i] >= minDoubleValue)
        doubleMinGtEqList.add(doubleArray[i]);
      if (doubleArray[i] != minDoubleValue)
        doubleMinNotEqList.add(doubleArray[i]);
      // Max
      if (doubleArray[i] < maxDoubleValue)
        doubleMaxLtList.add(doubleArray[i]);
      if (doubleArray[i] <= maxDoubleValue)
        doubleMaxLtEqList.add(doubleArray[i]);
      if (doubleArray[i] != maxDoubleValue)
        doubleMaxNotEqList.add(doubleArray[i]);
      // Avg
      if (doubleArray[i] == avgDoubleValue)
        doubleEqAvgList.add(doubleArray[i]);
      if (doubleArray[i] > avgDoubleValue)
        doubleGtAvgList.add(doubleArray[i]);
      if (doubleArray[i] >= avgDoubleValue)
        doubleGtEqAvgList.add(doubleArray[i]);
      if (doubleArray[i] < avgDoubleValue)
        doubleLtAvgList.add(doubleArray[i]);
      if (doubleArray[i] <= avgDoubleValue)
        doubleLtEqAvgList.add(doubleArray[i]);
      if (doubleArray[i] != avgDoubleValue)
        doubleNotEqAvgList.add(doubleArray[i]);
      // StdDev
      if (doubleArray[i] == stdDevDoubleValue)
        doubleEqStdDevList.add(doubleArray[i]);
      if (doubleArray[i] > stdDevDoubleValue)
        doubleGtStdDevList.add(doubleArray[i]);
      if (doubleArray[i] >= stdDevDoubleValue)
        doubleGtEqStdDevList.add(doubleArray[i]);
      if (doubleArray[i] < stdDevDoubleValue)
        doubleLtStdDevList.add(doubleArray[i]);
      if (doubleArray[i] <= stdDevDoubleValue)
        doubleLtEqStdDevList.add(doubleArray[i]);
      if (doubleArray[i] != stdDevDoubleValue)
        doubleNotEqStdDevList.add(doubleArray[i]);
      // Variance
      if (doubleArray[i] == varianceDoubleValue)
        doubleEqVarianceList.add(doubleArray[i]);
      if (doubleArray[i] > varianceDoubleValue)
        doubleGtVarianceList.add(doubleArray[i]);
      if (doubleArray[i] >= varianceDoubleValue)
        doubleGtEqVarianceList.add(doubleArray[i]);
      if (doubleArray[i] < varianceDoubleValue)
        doubleLtVarianceList.add(doubleArray[i]);
      if (doubleArray[i] <= varianceDoubleValue)
        doubleLtEqVarianceList.add(doubleArray[i]);
      if (doubleArray[i] != varianceDoubleValue)
        doubleNotEqVarianceList.add(doubleArray[i]);
    }

    countDoubleArray = new double[numOfObjects];
    for (int i = 0; i < numOfObjects - 1; i++)
    {
      int countValue;
      if (i % 2 == 0)
        countValue = numOfObjects + i;
      else
        countValue = numOfObjects - i;

      countDoubleArray[i] = countValue;
    }
    countDoubleArray[countDoubleArray.length - 1] = numOfObjects;
    Arrays.sort(countDoubleArray);

    for (int i = 0; i < countDoubleArray.length; i++)
    {
      // Count
      if (countDoubleArray[i] == numOfObjects)
        doubleEqCountList.add(countDoubleArray[i]);
      if (countDoubleArray[i] > numOfObjects)
        doubleGtCountList.add(countDoubleArray[i]);
      if (countDoubleArray[i] >= numOfObjects)
        doubleGtEqCountList.add(countDoubleArray[i]);
      if (countDoubleArray[i] < numOfObjects)
        doubleLtCountList.add(countDoubleArray[i]);
      if (countDoubleArray[i] <= numOfObjects)
        doubleLtEqCountList.add(countDoubleArray[i]);
      if (countDoubleArray[i] != numOfObjects)
        doubleNotEqCountList.add(countDoubleArray[i]);
    }
  }

  private static void decimalOracle()
  {
    decimalArray = new double[numOfObjects];
    double[] tempDecimalArray = new double[numOfObjects];

    for (int i = 0; i < decimalArray.length; i++)
    {
      Double decimalValue = (double) ( random.nextDouble() * 1000000 );
      DecimalFormat df = new DecimalFormat("0.00");
      decimalValue = Double.parseDouble(df.format(decimalValue));

      if (minDecimalValue == 0.0)
        minDecimalValue = decimalValue;
      if (decimalValue < minDecimalValue)
        minDecimalValue = decimalValue;
      if (decimalValue > maxDecimalValue)
        maxDecimalValue = decimalValue;

      decimalArray[i] = decimalValue;
      tempDecimalArray[i] = decimalValue;
      sumDecimalValue += decimalValue;
    }
    Arrays.sort(decimalArray);

    avgDecimalValue = sumDecimalValue / numOfObjects;
    varianceDecimalValue = (double) StatUtils.variance(tempDecimalArray);
    stdDevDecimalValue = (double) Math.sqrt(varianceDecimalValue);

    for (int i = 0; i < decimalArray.length; i++)
    {
      // Min
      if (decimalArray[i] > minDecimalValue)
        decimalMinGtList.add(decimalArray[i]);
      if (decimalArray[i] >= minDecimalValue)
        decimalMinGtEqList.add(decimalArray[i]);
      if (decimalArray[i] != minDecimalValue)
        decimalMinNotEqList.add(decimalArray[i]);
      // Max
      if (decimalArray[i] < maxDecimalValue)
        decimalMaxLtList.add(decimalArray[i]);
      if (decimalArray[i] <= maxDecimalValue)
        decimalMaxLtEqList.add(decimalArray[i]);
      if (decimalArray[i] != maxDecimalValue)
        decimalMaxNotEqList.add(decimalArray[i]);
      // Avg
      if (decimalArray[i] == avgDecimalValue)
        decimalEqAvgList.add(decimalArray[i]);
      if (decimalArray[i] > avgDecimalValue)
        decimalGtAvgList.add(decimalArray[i]);
      if (decimalArray[i] >= avgDecimalValue)
        decimalGtEqAvgList.add(decimalArray[i]);
      if (decimalArray[i] < avgDecimalValue)
        decimalLtAvgList.add(decimalArray[i]);
      if (decimalArray[i] <= avgDecimalValue)
        decimalLtEqAvgList.add(decimalArray[i]);
      if (decimalArray[i] != avgDecimalValue)
        decimalNotEqAvgList.add(decimalArray[i]);
      // StdDev
      if (decimalArray[i] == stdDevDecimalValue)
        decimalEqStdDevList.add(decimalArray[i]);
      if (decimalArray[i] > stdDevDecimalValue)
        decimalGtStdDevList.add(decimalArray[i]);
      if (decimalArray[i] >= stdDevDecimalValue)
        decimalGtEqStdDevList.add(decimalArray[i]);
      if (decimalArray[i] < stdDevDecimalValue)
        decimalLtStdDevList.add(decimalArray[i]);
      if (decimalArray[i] <= stdDevDecimalValue)
        decimalLtEqStdDevList.add(decimalArray[i]);
      if (decimalArray[i] != stdDevDecimalValue)
        decimalNotEqStdDevList.add(decimalArray[i]);
      // Variance
      if (decimalArray[i] == varianceDecimalValue)
        decimalEqVarianceList.add(decimalArray[i]);
      if (decimalArray[i] > varianceDecimalValue)
        decimalGtVarianceList.add(decimalArray[i]);
      if (decimalArray[i] >= varianceDecimalValue)
        decimalGtEqVarianceList.add(decimalArray[i]);
      if (decimalArray[i] < varianceDecimalValue)
        decimalLtVarianceList.add(decimalArray[i]);
      if (decimalArray[i] <= varianceDecimalValue)
        decimalLtEqVarianceList.add(decimalArray[i]);
      if (decimalArray[i] != varianceDecimalValue)
        decimalNotEqVarianceList.add(decimalArray[i]);
    }

    countDecimalArray = new double[numOfObjects];
    for (int i = 0; i < numOfObjects - 1; i++)
    {
      int countValue;
      if (i % 2 == 0)
        countValue = numOfObjects + i;
      else
        countValue = numOfObjects - i;

      countDecimalArray[i] = countValue;
    }
    countDecimalArray[countDecimalArray.length - 1] = numOfObjects;
    Arrays.sort(countDecimalArray);

    for (int i = 0; i < countDecimalArray.length; i++)
    {
      // Count
      if (countDecimalArray[i] == numOfObjects)
        decimalEqCountList.add(countDecimalArray[i]);
      if (countDecimalArray[i] > numOfObjects)
        decimalGtCountList.add(countDecimalArray[i]);
      if (countDecimalArray[i] >= numOfObjects)
        decimalGtEqCountList.add(countDecimalArray[i]);
      if (countDecimalArray[i] < numOfObjects)
        decimalLtCountList.add(countDecimalArray[i]);
      if (countDecimalArray[i] <= numOfObjects)
        decimalLtEqCountList.add(countDecimalArray[i]);
      if (countDecimalArray[i] != numOfObjects)
        decimalNotEqCountList.add(countDecimalArray[i]);
    }
  }

  private static void integerOracle()
  {
    integerArray = new int[numOfObjects];
    double[] tempDoubleArray = new double[numOfObjects];

    for (int i = 0; i < integerArray.length; i++)
    {
      int integerValue = (int) ( random.nextFloat() * 100000 );

      if (minIntegerValue == 0.0)
        minIntegerValue = integerValue;

      if (integerValue < minIntegerValue)
        minIntegerValue = integerValue;
      if (integerValue > maxIntegerValue)
        maxIntegerValue = integerValue;

      integerArray[i] = integerValue;
      tempDoubleArray[i] = integerValue;
      sumIntegerValue += integerValue;
    }
    Arrays.sort(integerArray);

    avgIntegerValue = sumIntegerValue / numOfObjects;
    varianceIntegerValue = StatUtils.variance(tempDoubleArray);
    stdDevIntegerValue = Math.sqrt(varianceIntegerValue);

    for (int i = 0; i < integerArray.length; i++)
    {
      // Min
      if (integerArray[i] > minIntegerValue)
        integerMinGtList.add(integerArray[i]);
      if (integerArray[i] >= minIntegerValue)
        integerMinGtEqList.add(integerArray[i]);
      if (integerArray[i] != minIntegerValue)
        integerMinNotEqList.add(integerArray[i]);
      // Max
      if (integerArray[i] < maxIntegerValue)
        integerMaxLtList.add(integerArray[i]);
      if (integerArray[i] <= maxIntegerValue)
        integerMaxLtEqList.add(integerArray[i]);
      if (integerArray[i] != maxIntegerValue)
        integerMaxNotEqList.add(integerArray[i]);
      // Avg
      if (integerArray[i] == avgIntegerValue)
        integerEqAvgList.add(integerArray[i]);
      if (integerArray[i] > avgIntegerValue)
        integerGtAvgList.add(integerArray[i]);
      if (integerArray[i] >= avgIntegerValue)
        integerGtEqAvgList.add(integerArray[i]);
      if (integerArray[i] < avgIntegerValue)
        integerLtAvgList.add(integerArray[i]);
      if (integerArray[i] <= avgIntegerValue)
        integerLtEqAvgList.add(integerArray[i]);
      if (integerArray[i] != avgIntegerValue)
        integerNotEqAvgList.add(integerArray[i]);
      // StdDev
      if (integerArray[i] == stdDevIntegerValue)
        integerEqStdDevList.add(integerArray[i]);
      if (integerArray[i] > stdDevIntegerValue)
        integerGtStdDevList.add(integerArray[i]);
      if (integerArray[i] >= stdDevIntegerValue)
        integerGtEqStdDevList.add(integerArray[i]);
      if (integerArray[i] < stdDevIntegerValue)
        integerLtStdDevList.add(integerArray[i]);
      if (integerArray[i] <= stdDevIntegerValue)
        integerLtEqStdDevList.add(integerArray[i]);
      if (integerArray[i] != stdDevIntegerValue)
        integerNotEqStdDevList.add(integerArray[i]);
      // Variance
      if (integerArray[i] == varianceIntegerValue)
        integerEqVarianceList.add(integerArray[i]);
      if (integerArray[i] > varianceIntegerValue)
        integerGtVarianceList.add(integerArray[i]);
      if (integerArray[i] >= varianceIntegerValue)
        integerGtEqVarianceList.add(integerArray[i]);
      if (integerArray[i] < varianceIntegerValue)
        integerLtVarianceList.add(integerArray[i]);
      if (integerArray[i] <= varianceIntegerValue)
        integerLtEqVarianceList.add(integerArray[i]);
      if (integerArray[i] != varianceIntegerValue)
        integerNotEqVarianceList.add(integerArray[i]);
    }

    countIntegerArray = new int[numOfObjects];
    for (int i = 0; i < numOfObjects - 1; i++)
    {
      int countValue;
      if (i % 2 == 0)
        countValue = numOfObjects + i;
      else
        countValue = numOfObjects - i;

      countIntegerArray[i] = countValue;
    }
    countIntegerArray[countIntegerArray.length - 1] = numOfObjects;
    Arrays.sort(countIntegerArray);

    for (int i = 0; i < countIntegerArray.length; i++)
    {
      // Count
      if (countIntegerArray[i] == numOfObjects)
        integerEqCountList.add(countIntegerArray[i]);
      if (countIntegerArray[i] > numOfObjects)
        integerGtCountList.add(countIntegerArray[i]);
      if (countIntegerArray[i] >= numOfObjects)
        integerGtEqCountList.add(countIntegerArray[i]);
      if (countIntegerArray[i] < numOfObjects)
        integerLtCountList.add(countIntegerArray[i]);
      if (countIntegerArray[i] <= numOfObjects)
        integerLtEqCountList.add(countIntegerArray[i]);
      if (countIntegerArray[i] != numOfObjects)
        integerNotEqCountList.add(countIntegerArray[i]);
    }

    sumIntegerArray = new int[numOfObjects];
    for (int i = 0; i < numOfObjects - 1; i++)
    {
      int longValue = (int) ( random.nextFloat() * 100000 );
      if (i % 2 == 0)
        longValue = sumIntegerValue + longValue;
      else
        longValue = sumIntegerValue - longValue;

      sumIntegerArray[i] = longValue;
    }
    sumIntegerArray[sumIntegerArray.length - 1] = sumIntegerValue;
    Arrays.sort(sumIntegerArray);

    for (int i = 0; i < sumIntegerArray.length; i++)
    {
      // Sum
      if (sumIntegerArray[i] == sumIntegerValue)
        integerEqSumList.add(sumIntegerArray[i]);
      if (sumIntegerArray[i] > sumIntegerValue)
        integerGtSumList.add(sumIntegerArray[i]);
      if (sumIntegerArray[i] >= sumIntegerValue)
        integerGtEqSumList.add(sumIntegerArray[i]);
      if (sumIntegerArray[i] < sumIntegerValue)
        integerLtSumList.add(sumIntegerArray[i]);
      if (sumIntegerArray[i] <= sumIntegerValue)
        integerLtEqSumList.add(sumIntegerArray[i]);
      if (sumIntegerArray[i] != sumIntegerValue)
        integerNotEqSumList.add(sumIntegerArray[i]);
    }

  }

  private static void longOracle()
  {
    longArray = new long[numOfObjects];
    double[] tempDoubleArray = new double[numOfObjects];

    for (int i = 0; i < longArray.length; i++)
    {
      long longValue = (long) ( random.nextFloat() * 100000000 );

      if (minLongValue == 0.0)
        minLongValue = longValue;

      if (longValue < minLongValue)
        minLongValue = longValue;
      if (longValue > maxLongValue)
        maxLongValue = longValue;

      longArray[i] = longValue;
      tempDoubleArray[i] = longValue;
      sumLongValue += longValue;
    }
    Arrays.sort(longArray);

    avgLongValue = sumLongValue / numOfObjects;
    varianceLongValue = StatUtils.variance(tempDoubleArray);
    stdDevLongValue = Math.sqrt(varianceLongValue);

    for (int i = 0; i < longArray.length; i++)
    {
      // Min
      if (longArray[i] > minLongValue)
        longMinGtList.add(longArray[i]);
      if (longArray[i] >= minLongValue)
        longMinGtEqList.add(longArray[i]);
      if (longArray[i] != minLongValue)
        longMinNotEqList.add(longArray[i]);
      // Max
      if (longArray[i] < maxLongValue)
        longMaxLtList.add(longArray[i]);
      if (longArray[i] <= maxLongValue)
        longMaxLtEqList.add(longArray[i]);
      if (longArray[i] != maxLongValue)
        longMaxNotEqList.add(longArray[i]);
      // Avg
      if (longArray[i] == avgLongValue)
        longEqAvgList.add(longArray[i]);
      if (longArray[i] > avgLongValue)
        longGtAvgList.add(longArray[i]);
      if (longArray[i] >= avgLongValue)
        longGtEqAvgList.add(longArray[i]);
      if (longArray[i] < avgLongValue)
        longLtAvgList.add(longArray[i]);
      if (longArray[i] <= avgLongValue)
        longLtEqAvgList.add(longArray[i]);
      if (longArray[i] != avgLongValue)
        longNotEqAvgList.add(longArray[i]);
      // StdDev
      if (longArray[i] == stdDevLongValue)
        longEqStdDevList.add(longArray[i]);
      if (longArray[i] > stdDevLongValue)
        longGtStdDevList.add(longArray[i]);
      if (longArray[i] >= stdDevLongValue)
        longGtEqStdDevList.add(longArray[i]);
      if (longArray[i] < stdDevLongValue)
        longLtStdDevList.add(longArray[i]);
      if (longArray[i] <= stdDevLongValue)
        longLtEqStdDevList.add(longArray[i]);
      if (longArray[i] != stdDevLongValue)
        longNotEqStdDevList.add(longArray[i]);
      // Variance
      if (longArray[i] == varianceLongValue)
        longEqVarianceList.add(longArray[i]);
      if (longArray[i] > varianceLongValue)
        longGtVarianceList.add(longArray[i]);
      if (longArray[i] >= varianceLongValue)
        longGtEqVarianceList.add(longArray[i]);
      if (longArray[i] < varianceLongValue)
        longLtVarianceList.add(longArray[i]);
      if (longArray[i] <= varianceLongValue)
        longLtEqVarianceList.add(longArray[i]);
      if (longArray[i] != varianceLongValue)
        longNotEqVarianceList.add(longArray[i]);
    }

    countLongArray = new long[numOfObjects];
    for (int i = 0; i < numOfObjects - 1; i++)
    {
      long countValue;
      if (i % 2 == 0)
        countValue = numOfObjects + i;
      else
        countValue = numOfObjects - i;

      countLongArray[i] = countValue;
    }
    countLongArray[countLongArray.length - 1] = numOfObjects;
    Arrays.sort(countLongArray);

    for (int i = 0; i < countLongArray.length; i++)
    {
      // Count
      if (countLongArray[i] == numOfObjects)
        longEqCountList.add(countLongArray[i]);
      if (countLongArray[i] > numOfObjects)
        longGtCountList.add(countLongArray[i]);
      if (countLongArray[i] >= numOfObjects)
        longGtEqCountList.add(countLongArray[i]);
      if (countLongArray[i] < numOfObjects)
        longLtCountList.add(countLongArray[i]);
      if (countLongArray[i] <= numOfObjects)
        longLtEqCountList.add(countLongArray[i]);
      if (countLongArray[i] != numOfObjects)
        longNotEqCountList.add(countLongArray[i]);
    }

    sumLongArray = new long[numOfObjects];
    for (int i = 0; i < numOfObjects - 1; i++)
    {
      long longValue = (long) ( random.nextFloat() * 100000000 );
      if (i % 2 == 0)
        longValue = sumLongValue + longValue;
      else
        longValue = sumLongValue - longValue;

      sumLongArray[i] = longValue;
    }
    sumLongArray[sumLongArray.length - 1] = sumLongValue;
    Arrays.sort(sumLongArray);

    for (int i = 0; i < sumLongArray.length; i++)
    {
      // Sum
      if (sumLongArray[i] == sumLongValue)
        longEqSumList.add(sumLongArray[i]);
      if (sumLongArray[i] > sumLongValue)
        longGtSumList.add(sumLongArray[i]);
      if (sumLongArray[i] >= sumLongValue)
        longGtEqSumList.add(sumLongArray[i]);
      if (sumLongArray[i] < sumLongValue)
        longLtSumList.add(sumLongArray[i]);
      if (sumLongArray[i] <= sumLongValue)
        longLtEqSumList.add(sumLongArray[i]);
      if (sumLongArray[i] != sumLongValue)
        longNotEqSumList.add(sumLongArray[i]);
    }

  }

  private static void dateOracle()
  {
    dateArray = new long[numOfObjects];

    long now = new Date().getTime();

    for (int i = 0; i < dateArray.length; i++)
    {
      long dateLong = (long) ( random.nextFloat() * 5000000000l ) + now;
      Date truncDate = new Date(dateLong);
      String truncDateString = new java.text.SimpleDateFormat(Constants.DATE_FORMAT).format(truncDate);
      truncDate = new SimpleDateFormat(Constants.DATE_FORMAT).parse(truncDateString, new java.text.ParsePosition(0));
      dateLong = truncDate.getTime();

      if (minDateValue == 0.0)
        minDateValue = dateLong;
      if (dateLong < minDateValue)
        minDateValue = dateLong;
      if (dateLong > maxDateValue)
        maxDateValue = dateLong;

      dateArray[i] = dateLong;
    }
    Arrays.sort(dateArray);

    for (int i = 0; i < dateArray.length; i++)
    {
      // Min
      if (dateArray[i] == minDateValue)
        dateMinEqList.add(dateArray[i]);
      if (dateArray[i] > minDateValue)
        dateMinGtList.add(dateArray[i]);
      if (dateArray[i] >= minDateValue)
        dateMinGtEqList.add(dateArray[i]);
      if (dateArray[i] <= minDateValue)
        dateMinLtEqList.add(dateArray[i]);
      if (dateArray[i] != minDateValue)
        dateMinNotEqList.add(dateArray[i]);
      // Max
      if (dateArray[i] == maxDateValue)
        dateMaxEqList.add(dateArray[i]);
      if (dateArray[i] >= maxDateValue)
        dateMaxGtEqList.add(dateArray[i]);
      if (dateArray[i] < maxDateValue)
        dateMaxLtList.add(dateArray[i]);
      if (dateArray[i] <= maxDateValue)
        dateMaxLtEqList.add(dateArray[i]);
      if (dateArray[i] != maxDateValue)
        dateMaxNotEqList.add(dateArray[i]);
    }
  }

  private static void dateTimeOracle()
  {
    dateTimeArray = new long[numOfObjects];

    long now = new Date().getTime();

    for (int i = 0; i < dateTimeArray.length; i++)
    {
      long dateTimeLong = (long) ( random.nextFloat() * 5000000000l ) + now;
      Date truncDateTime = new Date(dateTimeLong);
      String truncDateTimeString = new java.text.SimpleDateFormat(Constants.DATETIME_FORMAT).format(truncDateTime);
      truncDateTime = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(truncDateTimeString, new java.text.ParsePosition(0));
      dateTimeLong = truncDateTime.getTime();

      if (minDateTimeValue == 0.0)
        minDateTimeValue = dateTimeLong;
      if (dateTimeLong < minDateTimeValue)
        minDateTimeValue = dateTimeLong;
      if (dateTimeLong > maxDateTimeValue)
        maxDateTimeValue = dateTimeLong;

      dateTimeArray[i] = dateTimeLong;
    }
    Arrays.sort(dateTimeArray);

    for (int i = 0; i < dateTimeArray.length; i++)
    {
      // Min
      if (dateTimeArray[i] == minDateTimeValue)
        dateTimeMinEqList.add(dateTimeArray[i]);
      if (dateTimeArray[i] > minDateTimeValue)
        dateTimeMinGtList.add(dateTimeArray[i]);
      if (dateTimeArray[i] >= minDateTimeValue)
        dateTimeMinGtEqList.add(dateTimeArray[i]);
      if (dateTimeArray[i] <= minDateTimeValue)
        dateTimeMinLtEqList.add(dateTimeArray[i]);
      if (dateTimeArray[i] != minDateTimeValue)
        dateTimeMinNotEqList.add(dateTimeArray[i]);
      // Max
      if (dateTimeArray[i] == maxDateTimeValue)
        dateTimeMaxEqList.add(dateTimeArray[i]);
      if (dateTimeArray[i] >= maxDateTimeValue)
        dateTimeMaxGtEqList.add(dateTimeArray[i]);
      if (dateTimeArray[i] < maxDateTimeValue)
        dateTimeMaxLtList.add(dateTimeArray[i]);
      if (dateTimeArray[i] <= maxDateTimeValue)
        dateTimeMaxLtEqList.add(dateTimeArray[i]);
      if (dateTimeArray[i] != maxDateTimeValue)
        dateTimeMaxNotEqList.add(dateTimeArray[i]);
    }
  }

  private static void timeOracle()
  {
    timeArray = new long[numOfObjects];

    long now = new Date().getTime();

    for (int i = 0; i < timeArray.length; i++)
    {
      long timeLong = (long) ( random.nextFloat() * 5000000000l ) + now;
      Date truncTime = new Date(timeLong);
      String truncTimeString = new java.text.SimpleDateFormat(Constants.TIME_FORMAT).format(truncTime);
      truncTime = new SimpleDateFormat(Constants.TIME_FORMAT).parse(truncTimeString, new java.text.ParsePosition(0));
      timeLong = truncTime.getTime();

      if (minTimeValue == 0.0)
        minTimeValue = timeLong;
      if (timeLong < minTimeValue)
        minTimeValue = timeLong;
      if (timeLong > maxTimeValue)
        maxTimeValue = timeLong;

      timeArray[i] = timeLong;
    }
    Arrays.sort(timeArray);

    for (int i = 0; i < timeArray.length; i++)
    {
      // Min
      if (timeArray[i] == minTimeValue)
        timeMinEqList.add(timeArray[i]);
      if (timeArray[i] > minTimeValue)
        timeMinGtList.add(timeArray[i]);
      if (timeArray[i] >= minTimeValue)
        timeMinGtEqList.add(timeArray[i]);
      if (timeArray[i] <= minTimeValue)
        timeMinLtEqList.add(timeArray[i]);
      if (timeArray[i] != minTimeValue)
        timeMinNotEqList.add(timeArray[i]);
      // Max
      if (timeArray[i] == maxTimeValue)
        timeMaxEqList.add(timeArray[i]);
      if (timeArray[i] >= maxTimeValue)
        timeMaxGtEqList.add(timeArray[i]);
      if (timeArray[i] < maxTimeValue)
        timeMaxLtList.add(timeArray[i]);
      if (timeArray[i] <= maxTimeValue)
        timeMaxLtEqList.add(timeArray[i]);
      if (timeArray[i] != maxTimeValue)
        timeMaxNotEqList.add(timeArray[i]);
    }
  }

  private static void booleanOracle()
  {
    booleanArray = new boolean[numOfObjects];

    for (int i = 0; i < booleanArray.length; i++)
    {
      boolean booleanValue = random.nextBoolean();
      booleanArray[i] = booleanValue;

      if (booleanValue)
        sumBooleanValue += 1;
    }

    avgBooleanValue = sumBooleanValue / (float) numOfObjects;

    for (int i = 0; i < booleanArray.length; i++)
    {
      if (booleanArray[i] == false)
      {
        booleanMinEqList.add(booleanArray[i]);
        booleanMaxNotEqList.add(booleanArray[i]);
      }
      else
      {
        booleanMinNotEqList.add(booleanArray[i]);
        booleanMaxEqList.add(booleanArray[i]);
      }

      // AVG
      if (avgBooleanValue == 1)
      {
        if (booleanArray[i] == true)
          booleanEqAvgList.add(booleanArray[i]);
        else
          booleanNotEqAvgList.add(booleanArray[i]);
      }
      else if (avgBooleanValue == 0)
      {
        if (booleanArray[i] == false)
          booleanEqAvgList.add(booleanArray[i]);
        else
          booleanNotEqAvgList.add(booleanArray[i]);
      }
      else if (avgBooleanValue >= 0.5)
      {
        if (booleanArray[i] == true)
          booleanGtAvgList.add(booleanArray[i]);
        else
          booleanLtAvgList.add(booleanArray[i]);
      }
      else if (avgBooleanValue < 0.5)
      {
        if (booleanArray[i] == false)
          booleanLtAvgList.add(booleanArray[i]);
        else
          booleanGtAvgList.add(booleanArray[i]);
      }
    }
  }

}
