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
package com.runwaysdk.dataaccess.resolver;

import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public class MdReferenceExportBuilder extends ExportBuilder<MdBusinessDAO[]>
{
  private MdBusinessDAO mdBusiness;
  
  private MdBusinessDAO mdBusiness2;

  @Override
  protected MdBusinessDAO[] doIt()
  {
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();
    
    MdAttributeCharacterDAO mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, "true");
    mdAttributeCharacter.apply();
    
    mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();
    
    MdAttributeReferenceDAO mdAttributeReference = TestFixtureFactory.addReferenceAttribute(mdBusiness2, mdBusiness);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, "true");
    mdAttributeReference.apply();
    
    MdAttributeReferenceDAO mdAttributeReference2 = TestFixtureFactory.addReferenceAttribute(mdBusiness, mdBusiness2);
    mdAttributeReference2.setValue(MdAttributeReferenceInfo.REQUIRED, "false");
    mdAttributeReference2.apply();    
    
    return new MdBusinessDAO[]{mdBusiness, mdBusiness2};
  }

  @Override
  protected void undoIt()
  {
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusiness2);
  }

}
