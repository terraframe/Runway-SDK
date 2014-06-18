/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.constants;

public interface TransactionItemInfo extends BusinessInfo
{
  /**
   * Class <code>TransactionItem</code>.
   */
  public static final String CLASS                  = Constants.TRANSACTION_PACKAGE + ".TransactionItem";

  /**
   * ID of the metadata that defines this class.
   */
  public static final String ID_VALUE               = "70vh63h88xco3vu6mmr1d4segrzlwfto00000000000000000000000000000001";

  /**
   * Component Id
   */
  public static final String COMPONENT_ID           = "componentId";

  /**
   * Component Seq
   */
  public static final String COMPONENT_SEQ          = "componentSeq";

  /**
   * Component Site Master
   */
  public static final String COMPONENT_SITE         = "componentSiteMaster";

  /**
   * Transaction Record
   */
  public static final String TRANSACTION_RECORD     = "transactionRecord";

  /**
   * XML Record
   */
  public static final String XML_RECORD             = "xmlRecord";

  /**
   * Item action
   */
  public static final String ITEM_ACTION            = "itemAction";

  public static final String IGNORE_SEQUENCE_NUMBER = "ignoreSequenceNumber";
}
