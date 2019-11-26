package com.runwaysdk.dataaccess.graph.attributes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.dataaccess.graph.attributes.ValueOverTime;
import com.runwaysdk.dataaccess.graph.attributes.ValueOverTimeCollection;
import com.runwaysdk.system.graph.ChangeFrequency;

public class ValueOverTimeTest
{
  private DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
  
  public ValueOverTimeTest()
  {
  }
  
  @Test
  public void testAnnual() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection(ChangeFrequency.ANNUAL);
    
    List<ValueOverTime> testValues = new LinkedList<ValueOverTime>();
    
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("12-31-1990"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1991"), dateFormat.parse("12-31-1991"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1992"), dateFormat.parse("12-31-1992"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1993"), dateFormat.parse("12-31-1993"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1994"), dateFormat.parse("12-31-1994"), 0));
    
    genericTest(valuesOverTime, testValues);
  }
  
  @Test
  public void testQuarterly() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection(ChangeFrequency.QUARTER);
    
    List<ValueOverTime> testValues = new LinkedList<ValueOverTime>();
    
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-2019"), dateFormat.parse("03-31-2019"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("04-01-2019"), dateFormat.parse("12-31-2019"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-2020"), dateFormat.parse("03-31-2020"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("04-01-2020"), dateFormat.parse("06-30-2020"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("07-01-2020"), dateFormat.parse("12-31-2020"), 0));
    
    genericTest(valuesOverTime, testValues);
  }
  
  @Test
  public void testMonthly() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection(ChangeFrequency.MONTHLY);
    
    List<ValueOverTime> testValues = new LinkedList<ValueOverTime>();
    
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-2019"), dateFormat.parse("01-31-2019"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("02-01-2019"), dateFormat.parse("02-28-2019"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("03-01-2019"), dateFormat.parse("05-31-2019"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("06-01-2019"), dateFormat.parse("06-30-2019"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("07-01-2019"), dateFormat.parse("07-31-2019"), 0));
    
    genericTest(valuesOverTime, testValues);
  }
  
  @Test
  public void testDaily() throws ParseException
  {
    ValueOverTimeCollection valuesOverTime = new ValueOverTimeCollection(ChangeFrequency.DAILY);
    
    List<ValueOverTime> testValues = new LinkedList<ValueOverTime>();
    
    testValues.add(new ValueOverTime(dateFormat.parse("01-01-1990"), dateFormat.parse("01-15-1990"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("01-16-1990"), dateFormat.parse("03-01-1990"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("03-02-1990"), dateFormat.parse("01-31-1991"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("02-01-1991"), dateFormat.parse("03-01-1991"), 0));
    testValues.add(new ValueOverTime(dateFormat.parse("03-02-1991"), dateFormat.parse("05-01-1991"), 0));
    
    genericTest(valuesOverTime, testValues);
  }
  
  private void genericTest(ValueOverTimeCollection valuesOverTime, List<ValueOverTime> testValues)
  {
    for (int i : new int[] {0,1,2,3,4})
    {
      valuesOverTime.add(new ValueOverTime(testValues.get(i).getStartDate(), null, 0));
    }
    this.validate(valuesOverTime, testValues);
    
    valuesOverTime.clear();
    for (int i : new int[] {4,3,2,1,0})
    {
      valuesOverTime.add(new ValueOverTime(testValues.get(i).getStartDate(), null, 0));
    }
    this.validate(valuesOverTime, testValues);
    
    valuesOverTime.clear();
    for (int i : new int[] {4,2,1,3,0})
    {
      valuesOverTime.add(new ValueOverTime(testValues.get(i).getStartDate(), null, 0));
    }
    this.validate(valuesOverTime, testValues);
    
    valuesOverTime.clear();
    for (int i : new int[] {3,1,2,0,4})
    {
      valuesOverTime.add(new ValueOverTime(testValues.get(i).getStartDate(), null, 0));
    }
    this.validate(valuesOverTime, testValues);
    
    valuesOverTime.clear();
    for (int i : new int[] {1,0,3,2,4})
    {
      valuesOverTime.add(new ValueOverTime(testValues.get(i).getStartDate(), null, 0));
    }
    this.validate(valuesOverTime, testValues);
  }
  
//  private void printTestValues(ValueOverTimeCollection valuesOverTime, DateFormat format)
//  {
//    for (ValueOverTime vot : valuesOverTime)
//    {
//      System.out.println(format.format(vot.getStartDate()) + " to " + format.format(vot.getEndDate()));
//    }
//  }
  
  private void validate(ValueOverTimeCollection valuesOverTime, List<ValueOverTime> testValues)
  {
//    this.printTestValues(valuesOverTime, dateFormat);
    valuesOverTime.validate();
    
    for (int i = 0; i < testValues.size(); ++i)
    {
      Assert.assertEquals(testValues.get(i).getStartDate(), valuesOverTime.get(i).getStartDate());
      
      if (testValues.size() == i + 1)
      {
        Assert.assertEquals(ValueOverTime.INFINITY_END_DATE, valuesOverTime.get(i).getEndDate());
      }
      else
      {
        Assert.assertEquals(testValues.get(i).getEndDate(), valuesOverTime.get(i).getEndDate());
      }
    }
  }
}
