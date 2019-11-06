package com.runwaysdk.dataaccess.graph.attributes;

import java.util.Date;

public class ValueOverTime
{
  private Date   startDate;

  private Date   endDate;

  private Object value;

  /**
   * @param startDate
   * @param endDate
   * @param value
   */
  public ValueOverTime(Date startDate, Date endDate, Object value)
  {
    super();
    this.startDate = startDate;
    this.endDate = endDate;
    this.value = value;
  }

  /**
   * @return the startDate
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * @param startDate
   *          the startDate to set
   */
  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  /**
   * @return the endDate
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  /**
   * @return the value
   */
  public Object getValue()
  {
    return value;
  }

  /**
   * @param value
   *          the value to set
   */
  public void setValue(Object value)
  {
    this.value = value;
  }

  public boolean between(Date date)
  {
    return this.startDate.before(date) && this.endDate.after(date);
  }
}
