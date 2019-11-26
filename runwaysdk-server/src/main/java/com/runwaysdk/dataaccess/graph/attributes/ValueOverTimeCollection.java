package com.runwaysdk.dataaccess.graph.attributes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

import com.runwaysdk.system.graph.ChangeFrequency;

public class ValueOverTimeCollection implements Collection<ValueOverTime>
{
  private LinkedList<ValueOverTime> valuesOverTime;
  
  private ChangeFrequency frequency;

  public ChangeFrequency getFrequency()
  {
    return this.frequency;
  }
  
  public ValueOverTimeCollection(ChangeFrequency frequency)
  {
    this.valuesOverTime = new LinkedList<ValueOverTime>();
    this.frequency = frequency;
  }
  
  public ValueOverTimeCollection()
  {
    this.valuesOverTime = new LinkedList<ValueOverTime>();
  }
  
  @Override
  public int size()
  {
    return valuesOverTime.size();
  }

  @Override
  public boolean isEmpty()
  {
    return valuesOverTime.isEmpty();
  }

  @Override
  public boolean contains(Object o)
  {
    return valuesOverTime.contains(o);
  }

  @Override
  public Iterator<ValueOverTime> iterator()
  {
    return valuesOverTime.iterator();
  }
  
  public void sort()
  {
    Collections.sort(valuesOverTime, 
        (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate()));
  }

  @Override
  public ValueOverTime[] toArray()
  {
    return (ValueOverTime[]) valuesOverTime.toArray();
  }
  
  public ValueOverTime getValueOverTime(Date startDate, Date endDate)
  {
    for (ValueOverTime vt : this.valuesOverTime)
    {
      if (vt.getStartDate().equals(startDate))
      {
        return vt;
      }
    }

    return null;
  }
  
  public Object getValueOnDate(Date date)
  {
    if (date != null)
    {
      for (ValueOverTime vt : this)
      {
        if (vt.between(date))
        {
          return vt.getValue();
        }
      }
    }
    
    return null;
  }
  
  @Override
  public boolean add(ValueOverTime vot)
  {
    Integer afterIndex = null;
    
    for (int i = 0; i < this.valuesOverTime.size(); ++i)
    {
      ValueOverTime testVot = this.valuesOverTime.get(i);
      
      if (vot.getStartDate().after(testVot.getStartDate()))
      {
        afterIndex = i;
      }
      else if (vot.getStartDate().equals(testVot.getStartDate()) && vot.getEndDate().equals(testVot.getEndDate()))
      {
        if (vot.getValue().equals(testVot.getValue()))
        {
          return false;
        }
        else
        {
          testVot.setValue(vot.getValue());
          return true;
        }
      }
    }
    
    if (afterIndex == null)
    {
      if (this.valuesOverTime.size() == 0)
      {
        vot.setEndDate(ValueOverTime.INFINITY_END_DATE);
      }
      else
      {
        LocalDate localEnd = LocalDate.fromDateFields(this.valuesOverTime.get(0).getStartDate());
        vot.setEndDate(localEnd.minusDays(1).toDate());
      }
      
      this.valuesOverTime.addFirst(vot);
      return true;
    }
    else
    {
      if (afterIndex+1 >= this.valuesOverTime.size())
      {
        LocalDate localEnd = LocalDate.fromDateFields(vot.getStartDate());
        this.valuesOverTime.get(afterIndex).setEndDate(localEnd.minusDays(1).toDate());
        vot.setEndDate(ValueOverTime.INFINITY_END_DATE);
      }
      else
      {
        Date votEnd = LocalDate.fromDateFields(this.valuesOverTime.get(afterIndex+1).getStartDate()).minusDays(1).toDate();
        vot.setEndDate(votEnd);
        
        Date afterVotEnd = LocalDate.fromDateFields(vot.getStartDate()).minusDays(1).toDate();
        this.valuesOverTime.get(afterIndex).setEndDate(afterVotEnd);
      }
      
      this.valuesOverTime.add(afterIndex + 1, vot);
      return true;
    }
  }
  
  public ValueOverTime last()
  {
    return this.get(this.valuesOverTime.size()-1);
  }
  
  public ValueOverTime get(int index)
  {
    return this.valuesOverTime.get(index);
  }
  
  public void validate()
  {
    // Validate each ValueOverTime individually (will make sure they conform to the frequency)
    if (frequency != null)
    {
      for (ValueOverTime vot : this)
      {
        vot.validate(frequency);
      }
    }
    
    // Make sure there are no gaps
    for (int i = 0; i < this.size()-1; ++i)
    {
      LocalDate left = LocalDate.fromDateFields(this.get(i).getEndDate());
      LocalDate right = LocalDate.fromDateFields(this.get(i+1).getStartDate());
      
      if (!left.plusDays(1).equals(right))
      {
        throw new RuntimeException("Gap detected in " + this.toString() + ".");
      }
    }
  }

  @Override
  public boolean remove(Object vot)
  {
    // TODO : Restructure the collection?
    
    return valuesOverTime.remove(vot);
  }

  @Override
  public boolean containsAll(Collection<?> c)
  {
    return valuesOverTime.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends ValueOverTime> c)
  {
    // TODO : Validation
    return valuesOverTime.addAll(c);
  }

  @Override
  public boolean removeAll(Collection<?> c)
  {
    boolean ret = false;
    
    for (Object o : c)
    {
      ret = (this.remove(o) || ret);
    }
    
    return ret;
  }

  @Override
  public boolean retainAll(Collection<?> c)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear()
  {
    this.valuesOverTime.clear();
  }

  @Override
  public <T> T[] toArray(T[] a)
  {
    return this.valuesOverTime.toArray(a);
  }
  
  public List<ValueOverTime> asList()
  {
    return this.valuesOverTime;
  }
  
  public String toString()
  {
    String ret = "ValueOverTimeCollection [";
    
    List<String> vots = new ArrayList<String>(); 
    for (ValueOverTime vot : this)
    {
      vots.add(vot.toString());
    }
    
    ret = ret + StringUtils.join(vots, ", ") + "]";
    
    return ret;
  }
  
}
