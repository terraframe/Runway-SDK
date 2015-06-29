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
package com.runwaysdk.dataaccess.transaction;

import com.runwaysdk.ServerExceptionMessageLocalizer;

public class SynchronizationSequenceGapException extends TransactionImportException
{
  /**
   * 
   */
  private static final long serialVersionUID = 8033883994709155166L;

  private String importSite;

  private Long lastImportedSequence;

  private Long firstExportSequence;
  
  private Long neededImportSequence;
  
  /**
   * Constructs a new <code>SynchronizationSequenceGapException</code> with the specified
   * developer message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param importSite
   *          site being imported.
   * @param lastImportedSequence
   *          last imported sequence from the imported site.
   * @param firstExportSequence
   *          first export sequence in the import file from the given site.
   * @param neededImportSequence
   *          the next expected sequence number.
   *        
   */
  public SynchronizationSequenceGapException(String devMessage, 
      String importSite, Long lastImportedSequence, Long firstExportSequence, Long neededImportSequence)
  {
    super(devMessage);
    this.importSite = importSite;
    this.lastImportedSequence = lastImportedSequence;
    this.firstExportSequence = firstExportSequence;
    this.neededImportSequence = neededImportSequence;
  }

  /**
   * Constructs a new <code>SynchronizationSequenceGapException</code> with the specified
   * developer message and cause.
   * <p>
   * Note that the detail message associated with <code>cause</code> is <i>not</i>
   * automatically incorporated in this AbstractInstantiationException's detail
   * message.
   * 
   * @param devMessage
   *          The non-localized developer error message. Contains specific data
   *          access layer information useful for application debugging. The
   *          developer message is saved for later retrieval by the
   *          {@link #getMessage()} method.
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param importSite
   *          site being imported.
   * @param lastImportedSequence
   *          last imported sequence from the imported site.
   * @param firstExportSequence
   *          first export sequence in the import file from the given site.
   * @param neededImportSequence
   *          the next expected sequence number.
   */
  public SynchronizationSequenceGapException(String devMessage, Throwable cause,
      String importSite, Long lastImportedSequence, Long firstExportSequence, Long neededImportSequence)
  {
    super(devMessage, cause);
    this.importSite = importSite;
    this.lastImportedSequence = lastImportedSequence;
    this.firstExportSequence = firstExportSequence;
    this.neededImportSequence = neededImportSequence;
  }

  /**
   * Constructs a new <code>SynchronizationSequenceGapException</code> with the specified cause
   * and a developer message taken from the cause. This constructor is useful if
   * the AbstractInstantiationException is a wrapper for another throwable.
   * 
   * @param child
   *          The metadata describing the new type with the invalid caching
   *          algorithm
   * @param cause
   *          the cause (which is saved for later retrieval by the
   *          {@link #getCause()} method). (A <tt>null</tt> value is
   *          permitted, and indicates that the cause is nonexistent or
   *          unknown.)
   * @param importSite
   *          site being imported.
   * @param lastImportedSequence
   *          last imported sequence from the imported site.
   * @param firstExportSequence
   *          first export sequence in the import file from the given site.
   * @param neededImportSequence
   *          the next expected sequence number.
   */
  public SynchronizationSequenceGapException(Throwable cause,
      String importSite, Long lastImportedSequence, Long firstExportSequence, Long neededImportSequence)
  {
    super(cause);
    this.importSite = importSite;
    this.lastImportedSequence = lastImportedSequence;
    this.firstExportSequence = firstExportSequence;
    this.neededImportSequence = neededImportSequence;
  }

  /**
   * Fetches the localized message template and plugs in the correct parameters
   * to set the business error message.
   * 
   */
  public String getLocalizedMessage()
  {
    return ServerExceptionMessageLocalizer.
    synchronizationSequenceGapException(this.getLocale(), 
        this.importSite, this.lastImportedSequence, this.firstExportSequence, this.neededImportSequence);
  }
}
