package com.runwaysdk.build.domain;

import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.ServerProperties;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdAttributeReference;
import com.runwaysdk.system.metadata.MdBusiness;

public class MdAttributeReferenceImmutable
{
  public static void main(String[] args)
  {
    doIt();
  }
  
  public static void doIt()
  {
    ServerProperties.setIgnoreSiteMaster(true);
    
    MdBusinessDAO mdbiz = (MdBusinessDAO) MdBusinessDAO.getMdBusinessDAO(MdAttributeReference.CLASS);
    
    MdAttributeConcreteDAO ref = (MdAttributeConcreteDAO) mdbiz.getDefinedMdAttributeMap().get("mdbusiness");
    
    ref.getAttribute(MdAttributeConcreteInfo.IMMUTABLE).setValue(false);
    
    ref.apply();
    
    ServerProperties.setIgnoreSiteMaster(false);
  }
}
