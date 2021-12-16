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
package com.runwaysdk.dataaccess.graph.attributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class ValueOverTimeTest
{
  private DateFormat dateFormat;

  public ValueOverTimeTest()
  {
    this.dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    this.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
  }
  
  @Test
  public void testMultiValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();

    List<ValueOverTime> testValues = new LinkedList<ValueOverTime>();

    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("12-31-1990"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1991"), dateFormat.parse("12-31-1991"), 1));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1992"), dateFormat.parse("12-31-1992"), 2));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1993"), dateFormat.parse("12-31-1993"), 3));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1994"), dateFormat.parse("12-31-1994"), 4));

    genericTest(valuesOverTime, testValues);
  }

  @Test
  public void testOverwrite() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 6));
    
    Assert.assertEquals(1, valuesOverTime.size());
    Assert.assertEquals(6, valuesOverTime.get(0).getValue());
  }

  @Test
  public void testAddSuperset() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1989"), dateFormat.parse("01-17-1990"), 6));

    Assert.assertEquals(1, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);

    Assert.assertEquals(dateFormat.parse("01-01-1989"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-17-1990"), vot1.getEndDate());
  }

  @Test
  public void testAddSuperset_Edge() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1989"), dateFormat.parse("01-15-1990"), 6));
    
    Assert.assertEquals(1, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    
    Assert.assertEquals(dateFormat.parse("01-01-1989"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot1.getEndDate());
  }
  
  @Test
  public void testAddSubsetDifferentValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-05-1990"), dateFormat.parse("01-10-1990"), 6));

    Assert.assertEquals(3, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    ValueOverTime vot2 = list.get(1);
    ValueOverTime vot3 = list.get(2);

    Assert.assertEquals(dateFormat.parse("01-01-1990"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-04-1990"), vot1.getEndDate());

    Assert.assertEquals(dateFormat.parse("01-05-1990"), vot2.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-10-1990"), vot2.getEndDate());
    
    Assert.assertEquals(dateFormat.parse("01-11-1990"), vot3.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot3.getEndDate());
  }
  
  @Test
  public void testAddSubsetSameValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-05-1990"), dateFormat.parse("01-10-1990"), 0));

    Assert.assertEquals(1, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);

    Assert.assertEquals(0, vot1.getValue());
    Assert.assertEquals(dateFormat.parse("01-01-1990"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot1.getEndDate());
  }

  @Test
  public void testAddExtendAfterDifferentValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-05-1990"), dateFormat.parse("01-17-1990"), 6));

    Assert.assertEquals(2, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    ValueOverTime vot2 = list.get(1);

    Assert.assertEquals(0, vot1.getValue());
    Assert.assertEquals(dateFormat.parse("01-01-1990"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-04-1990"), vot1.getEndDate());

    Assert.assertEquals(6, vot2.getValue());
    Assert.assertEquals(dateFormat.parse("01-05-1990"), vot2.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-17-1990"), vot2.getEndDate());
  }

  @Test
  public void testAddExtendAfter_EdgeDifferentValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-15-1990"), dateFormat.parse("01-17-1990"), 6));
    
    Assert.assertEquals(2, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    ValueOverTime vot2 = list.get(1);
    
    Assert.assertEquals(0, vot1.getValue());
    Assert.assertEquals(dateFormat.parse("01-01-1990"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-14-1990"), vot1.getEndDate());
    
    Assert.assertEquals(6, vot2.getValue());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot2.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-17-1990"), vot2.getEndDate());
  }
  
  @Test
  public void testAddExtendAfter_EdgeSameValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-15-1990"), dateFormat.parse("01-17-1990"), 0));
    
    Assert.assertEquals(1, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    
    Assert.assertEquals(0, vot1.getValue());
    Assert.assertEquals(dateFormat.parse("01-01-1990"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-17-1990"), vot1.getEndDate());
  }
  
  @Test
  public void testAddExtendBeforeDifferentValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1989"), dateFormat.parse("01-10-1990"), 6));

    Assert.assertEquals(2, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    ValueOverTime vot2 = list.get(1);

    Assert.assertEquals(6, vot1.getValue());
    Assert.assertEquals(dateFormat.parse("01-01-1989"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-10-1990"), vot1.getEndDate());

    Assert.assertEquals(0, vot2.getValue());
    Assert.assertEquals(dateFormat.parse("01-11-1990"), vot2.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot2.getEndDate());
  }
  
  @Test
  public void testAddExtendBeforeSameValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1989"), dateFormat.parse("01-10-1990"), 0));

    Assert.assertEquals(1, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);

    Assert.assertEquals(0, vot1.getValue());
    Assert.assertEquals(dateFormat.parse("01-01-1989"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot1.getEndDate());
  }

  @Test
  public void testAddExtendBefore_EdgeDifferentValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1989"), dateFormat.parse("01-01-1990"), 6));
    
    Assert.assertEquals(2, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    ValueOverTime vot2 = list.get(1);
    
    Assert.assertEquals(6, vot1.getValue());
    Assert.assertEquals(dateFormat.parse("01-01-1989"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-01-1990"), vot1.getEndDate());
    
    Assert.assertEquals(0, vot2.getValue());
    Assert.assertEquals(dateFormat.parse("01-02-1990"), vot2.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot2.getEndDate());
  }
  
  @Test
  public void testAddExtendBefore_EdgeSameValue() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1989"), dateFormat.parse("01-01-1990"), 0));

    Assert.assertEquals(1, valuesOverTime.size());
    
    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);

    Assert.assertEquals(0, vot1.getValue());
    Assert.assertEquals(dateFormat.parse("01-01-1989"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot1.getEndDate());
  }
  
  @Test
  public void testAddNonIntersect() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1991"), dateFormat.parse("01-15-1991"), 0));

    Assert.assertEquals(2, valuesOverTime.size());

    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    ValueOverTime vot2 = list.get(1);

    Assert.assertEquals(dateFormat.parse("01-01-1990"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot1.getEndDate());

    Assert.assertEquals(dateFormat.parse("01-01-1991"), vot2.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1991"), vot2.getEndDate());
  }

  @Test
  public void testAddNonIntersect_Reverse() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection();
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1991"), dateFormat.parse("01-15-1991"), 0));
    valuesOverTime.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));

    Assert.assertEquals(2, valuesOverTime.size());

    List<ValueOverTime> list = valuesOverTime.asList();
    ValueOverTime vot1 = list.get(0);
    ValueOverTime vot2 = list.get(1);

    Assert.assertEquals(dateFormat.parse("01-01-1990"), vot1.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1990"), vot1.getEndDate());

    Assert.assertEquals(dateFormat.parse("01-01-1991"), vot2.getStartDate());
    Assert.assertEquals(dateFormat.parse("01-15-1991"), vot2.getEndDate());
  }

  private void genericTest(ValueOverTimeCollection valuesOverTime, List<ValueOverTime> testValues)
  {
    for (int i : new int[] { 0, 1, 2, 3, 4 })
    {
      ValueOverTime vot = testValues.get(i);
      valuesOverTime.add(new ValueOverTime(vot.getStartDate(), vot.getEndDate(), 0));
    }
    this.validate(valuesOverTime, testValues);

    valuesOverTime.clear();
    for (int i : new int[] { 4, 3, 2, 1, 0 })
    {
      ValueOverTime vot = testValues.get(i);
      valuesOverTime.add(new ValueOverTime(vot.getStartDate(), vot.getEndDate(), 0));
    }
    this.validate(valuesOverTime, testValues);

    valuesOverTime.clear();
    for (int i : new int[] { 4, 2, 1, 3, 0 })
    {
      ValueOverTime vot = testValues.get(i);
      valuesOverTime.add(new ValueOverTime(vot.getStartDate(), vot.getEndDate(), 0));
    }
    this.validate(valuesOverTime, testValues);

    valuesOverTime.clear();
    for (int i : new int[] { 3, 1, 2, 0, 4 })
    {
      ValueOverTime vot = testValues.get(i);
      valuesOverTime.add(new ValueOverTime(vot.getStartDate(), vot.getEndDate(), 0));
    }
    this.validate(valuesOverTime, testValues);

    valuesOverTime.clear();
    for (int i : new int[] { 1, 0, 3, 2, 4 })
    {
      ValueOverTime vot = testValues.get(i);
      valuesOverTime.add(new ValueOverTime(vot.getStartDate(), vot.getEndDate(), 0));
    }
    this.validate(valuesOverTime, testValues);
  }

  // private void printTestValues(ValueOverTimeCollection valuesOverTime,
  // DateFormat format)
  // {
  // for (ValueOverTime vot : valuesOverTime)
  // {
  // System.out.println(format.format(vot.getStartDate()) + " to " +
  // format.format(vot.getEndDate()));
  // }
  // }

  private void validate(ValueOverTimeCollection valuesOverTime, List<ValueOverTime> testValues)
  {
    // this.printTestValues(valuesOverTime, dateFormat);
    valuesOverTime.validate();

    // List<ValueOverTime> list = valuesOverTime.asList();
    //
    // for (int i = 0; i < testValues.size(); ++i)
    // {
    // Assert.assertEquals(testValues.get(i).getStartDate(),
    // list.get(i).getStartDate());
    //
    // if (testValues.size() == i + 1)
    // {
    // Assert.assertEquals(ValueOverTime.INFINITY_END_DATE,
    // list.get(i).getEndDate());
    // }
    // else
    // {
    // Assert.assertEquals(testValues.get(i).getEndDate(),
    // list.get(i).getEndDate());
    // }
    // }
  }
}
