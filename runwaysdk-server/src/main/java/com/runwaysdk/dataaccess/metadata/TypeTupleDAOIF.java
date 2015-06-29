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
package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.MetadataDAOIF;


public interface TypeTupleDAOIF extends MetadataDAOIF
{
  /**
   * The type of the TypeTuple Class
   */
  public static final String CLASS = Constants.METADATA_PACKAGE + "."+"TypeTuple";

  /**
   * Name of the attribute that stores the label of this metadata object.
   */
  public static final String DISPLAY_LABEL               = "displayLabel";

  /**
   * The name of the attribute on TypleTuple which references a MdAttribute or MdRelationship
   */
  public static final String METADATA                    = "metadata";

  /**
   * The name of the attribute on the TypeTuple which references the StateMaster
   */
  public static final String STATE_MASTER                = "stateMaster";

  /**
   * Get the StateMaster of the pairing
   * @return StateMasterIF of the State-MetaData pairing
   */
  public StateMasterDAOIF getStateMaster();

  /**
   * Get the MetaData of the pairing.  The MetaData must be an instance of MdAttribute or MdRelationship.
   * @return MetaDataIF of the State-MetaData pairing
   */
  public MetadataDAOIF getMetaData();
}
