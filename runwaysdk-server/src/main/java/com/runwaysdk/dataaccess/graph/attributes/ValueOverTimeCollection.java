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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

public class ValueOverTimeCollection implements Collection<ValueOverTime>
{
  private final class ValueOverTimeComparator implements Comparator<ValueOverTime>
  {
    @Override
    public int compare(ValueOverTime o1, ValueOverTime o2)
    {
      Date d1 = o1.getStartDate();
      Date d2 = o2.getStartDate();

      return d1.compareTo(d2);
    }
  }

  private SortedSet<ValueOverTime> valuesOverTime;

  public ValueOverTimeCollection()
  {
    this.valuesOverTime = new TreeSet<ValueOverTime>(new ValueOverTimeComparator());
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

  @Override
  public ValueOverTime[] toArray()
  {
    return (ValueOverTime[]) valuesOverTime.toArray();
  }
  
  public ValueOverTime get(int i)
  {
    if (i < 0 || i >= size())
    {
      throw new IndexOutOfBoundsException();
    }
    
    int j = 0;
    for (ValueOverTime vot : this.valuesOverTime)
    {
      if (j == i)
      {
        return vot;
      }
      
      j++;
    }
    
    return null;
  }

  public ValueOverTime getValueOverTime(Date startDate, Date endDate)
  {
    for (ValueOverTime vt : this.valuesOverTime)
    {
      if (vt.getStartDate().equals(startDate) && vt.getEndDate().equals(endDate))
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
    return add(vot, true);
  }

  public boolean add(ValueOverTime vot, boolean updateOnCollision)
  {
    this.calculateStartDates(vot.getStartDate(), vot.getEndDate());

    boolean success = this.valuesOverTime.add(vot);

    // Check if the value needs to be overwritten
    if (updateOnCollision && !success && this.valuesOverTime.remove(vot))
    {
      success = this.valuesOverTime.add(vot);
    }

    // if (success)
    // {
    // this.calculateEndDates();
    // }

    return success;
  }

  private void calculateStartDates(Date startDate, Date endDate)
  {
    if (startDate != null && endDate != null)
    {
      Iterator<ValueOverTime> it = this.valuesOverTime.iterator();
      LocalDate iStartDate = startDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
      LocalDate iEndDate = endDate.toInstant().atZone(ZoneId.of("Z")).toLocalDate();

      List<ValueOverTime> segments = new LinkedList<ValueOverTime>();

      while (it.hasNext())
      {
        ValueOverTime vot = it.next();

        LocalDate vStartDate = vot.getLocalStartDate();
        LocalDate vEndDate = vot.getLocalEndDate();

        // Remove the entry completely if its a subset of the startDate and
        // endDate range
        if (isAfterOrEqual(vStartDate, iStartDate) && isBeforeOrEqual(vEndDate, iEndDate))
        {
          it.remove();
        }
        // If the entry is extends after the end date then move the start
        // date of the entry to the end of the clear range
        else if (isAfterOrEqual(vStartDate, iStartDate) && vEndDate.isAfter(iEndDate) && isAfterOrEqual(iEndDate, vStartDate))
        {
          vot.setStartDate(this.calculateDatePlusOneDay(endDate));
        }
        // If the entry is extends before the start date then move the end
        // date of the entry to the start of the clear range
        else if (vStartDate.isBefore(iStartDate) && isBeforeOrEqual(vEndDate, iEndDate) && isBeforeOrEqual(iStartDate, vEndDate))
        {
          vot.setEndDate(this.calculateDateMinusOneDay(startDate));
        }
        else if (vStartDate.isBefore(iStartDate) && vEndDate.isAfter(iEndDate))
        {
          it.remove();

          segments.add(new ValueOverTime(vot.getStartDate(), this.calculateDateMinusOneDay(startDate), vot.getValue()));
          segments.add(new ValueOverTime(this.calculateDatePlusOneDay(endDate), vot.getEndDate(), vot.getValue()));
        }
      }

      for (ValueOverTime vot : segments)
      {
        this.valuesOverTime.add(vot);
      }

      this.reorder();
    }
  }

  private boolean isAfterOrEqual(LocalDate date1, LocalDate date2)
  {
    return date1.isAfter(date2) || date2.isEqual(date1);
  }

  private boolean isBeforeOrEqual(LocalDate date1, LocalDate date2)
  {
    return date1.isBefore(date2) || date2.isEqual(date1);
  }

  private void calculateEndDates()
  {
    if (this.valuesOverTime.size() > 0)
    {
      ValueOverTime prev = null;

      for (ValueOverTime current : this.valuesOverTime)
      {
        if (prev != null)
        {
          prev.setEndDate(this.calculateDateMinusOneDay(current.getStartDate()));
        }

        prev = current;
      }

      this.valuesOverTime.last().setEndDate(ValueOverTime.INFINITY_END_DATE);
    }
  }

  protected Date calculateDateMinusOneDay(Date source)
  {
    LocalDate localEnd = source.toInstant().atZone(ZoneId.of("Z")).toLocalDate().minusDays(1);
    Instant instant = localEnd.atStartOfDay().atZone(ZoneId.of("Z")).toInstant();

    return Date.from(instant);
  }

  protected Date calculateDatePlusOneDay(Date source)
  {
    LocalDate localEnd = source.toInstant().atZone(ZoneId.of("Z")).toLocalDate().plusDays(1);
    Instant instant = localEnd.atStartOfDay().atZone(ZoneId.of("Z")).toInstant();

    return Date.from(instant);
  }

  public ValueOverTime last()
  {
    return this.valuesOverTime.last();
  }

  public ValueOverTime first()
  {
    return this.valuesOverTime.first();
  }

  public void validate()
  {
    // Make sure there are no gaps
    // ValueOverTime prev = null;
    //
    // for (ValueOverTime current : this.valuesOverTime)
    // {
    // if (prev != null)
    // {
    // LocalDate left =
    // prev.getEndDate().toInstant().atZone(ZoneId.of("Z")).toLocalDate();
    // LocalDate right =
    // current.getStartDate().toInstant().atZone(ZoneId.of("Z")).toLocalDate();
    //
    // if (!left.plusDays(1).equals(right))
    // {
    // throw new RuntimeException("Gap detected in " + this.toString() + ".");
    // }
    // }
    //
    // prev = current;
    // }

    // Allow gaps
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
      ret = ( this.remove(o) || ret );
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

  public void reorder()
  {
    SortedSet<ValueOverTime> valuesOverTime = new TreeSet<ValueOverTime>(new ValueOverTimeComparator());
    valuesOverTime.addAll(this.valuesOverTime);

    this.valuesOverTime = valuesOverTime;
    //
    // this.calculateEndDates();
  }

  public List<ValueOverTime> asList()
  {
    return new LinkedList<ValueOverTime>(this.valuesOverTime);
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
