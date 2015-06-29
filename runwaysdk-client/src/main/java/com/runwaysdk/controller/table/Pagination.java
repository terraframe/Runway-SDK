/**
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
 */
package com.runwaysdk.controller.table;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.runwaysdk.business.ComponentQueryDTO;

public class Pagination implements Iterator<Pagination.Page>
{
  public static final String PAGE_MARKER = "####<page>####";

  public class Page
  {
    private Integer      pageNumber;

    private boolean      isCurrentPage;

    private boolean      isLeftGap;

    private boolean      isRightGap;

    private StringWriter writer;

    private Page()
    {
      this(false, 0);
    }

    private Page(boolean isCurrentPage, int pageNumber)
    {
      this.isCurrentPage = isCurrentPage;
      this.pageNumber = pageNumber;
      this.isLeftGap = false;
      this.isRightGap = false;
      this.writer = new StringWriter();
    }

    public void markLeftGap()
    {
      isLeftGap = true;
    }

    public void markRightGap()
    {
      isRightGap = true;
    }

    public boolean getLeftGap()
    {
      return isLeftGap;
    }

    public boolean getRightGap()
    {
      return isRightGap;
    }

    public int getPageNumber()
    {
      return pageNumber;
    }

    public boolean getCurrentPage()
    {
      return isCurrentPage;
    }

    public StringWriter getWriter()
    {
      return writer;
    }

    String generateLink(Context context)
    {
      String  pageContent = context.generateLink(pageNumber);
      String template = writer.getBuffer().toString();

      return template.replace(PAGE_MARKER, pageContent).trim();
    }
  }

  private static int        MAX_DISPLAY_PAGES = 10;

  private List<Page>        pages;

  private int               counter;

  private ComponentQueryDTO query;

  private StringBuffer      buffer;
  
  private boolean isAsynchronous;
  
  public Pagination(ComponentQueryDTO query, boolean isAsynchronous)
  {
    this.query = query;
    this.buffer = new StringBuffer();
    this.counter = 0;
    this.pages = new LinkedList<Page>();
    this.isAsynchronous = isAsynchronous;
    
    initialize();
  }
  
  boolean isAsynchronous()
  {
    return isAsynchronous;
  }
  
  private final void initialize()
  {
    // can't paginate an empty result set
    long count = query.getCount();

    if (count == 0)
    {
      return;
    }

    // Calculate the number of links to display
    int pageSize = query.getPageSize();
    int pageNumber = query.getPageNumber();
    
    if(pageSize == 0 || pageNumber == 0)
    {
      pageSize = (int) count;
      pageNumber = 1;
    }

    int totalPages = (int) Math.ceil( ( (double) count / pageSize ));
    
    int l = Math.max(pageNumber - 4, 1);
    int u = Math.min(pageNumber + 4, totalPages);
    int lowerBound = Math.max(1, Math.min(pageNumber-4, u-MAX_DISPLAY_PAGES));
    int upperBound = Math.min(Math.max(pageNumber+4, l+MAX_DISPLAY_PAGES), totalPages);
    
    if (lowerBound != 1)
    {
      // Generate the first page
      buffer.append("1");
      pages.add(new Page(false, 1));

      // Generate the marker page
      if (lowerBound != 2)
      {
        buffer.append(" ... ");

        Page page = new Page();
        page.markLeftGap();
        pages.add(page);
      }
    }

    for (int i = lowerBound; i <= upperBound; i++)
    {
      pages.add(new Page( ( pageNumber == i ), i));
      buffer.append(" " + new Integer(i).toString() + " ");
    }

    if (upperBound != totalPages)
    {
      // Generate marker page
      if (upperBound != totalPages - 1)
      {
        buffer.append(" ... ");

        Page page = new Page();
        page.markRightGap();
        pages.add(page);
      }

      // Generate last page
      buffer.append(totalPages);
      pages.add(new Page(false, totalPages));
    }
  }

  /* (non-Javadoc)
   * @see java.util.Iterator#hasNext()
   */
  public synchronized boolean hasNext()
  {
    return counter < pages.size();
  }

  /* (non-Javadoc)
   * @see java.util.Iterator#next()
   */
  public synchronized Page next()
  {
    return pages.get(counter++);
  }

  /**
   * Resets the Pages iterator
   */
  public synchronized void reset()
  {
    counter = 0;
  }

  /**
   * Remove method that will always throw an
   * {@link UnsupportedOperationException}.
   * 
   * @throws UnsupportedOperationException
   */
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  public String toString()
  {
    return buffer.toString();
  }
}
