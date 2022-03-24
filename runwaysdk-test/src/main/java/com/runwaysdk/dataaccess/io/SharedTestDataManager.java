package com.runwaysdk.dataaccess.io;

import com.runwaysdk.ComponentIF;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdDimensionInfo;
import com.runwaysdk.constants.MdDomainInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.EntityQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.RelationshipDAOQuery;
import com.runwaysdk.query.StructDAOQuery;
import com.runwaysdk.system.Users;
import com.runwaysdk.system.UsersQuery;
import com.runwaysdk.system.metadata.MdDimension;
import com.runwaysdk.system.metadata.MdDimensionQuery;
import com.runwaysdk.system.metadata.MdDomain;
import com.runwaysdk.system.metadata.MdDomainQuery;
import com.runwaysdk.system.metadata.MdMethod;
import com.runwaysdk.system.metadata.MdMethodQuery;
import com.runwaysdk.system.metadata.MdStruct;
import com.runwaysdk.system.metadata.MdStructQuery;
import com.runwaysdk.system.metadata.MdType;
import com.runwaysdk.system.metadata.MdTypeQuery;

public class SharedTestDataManager
{
  public interface MdTypeConfigurator {
    void configure(MdTypeDAO mdType);
  }
  
  public static MdStructDAO getOrCreateMdStruct(String name)
  {
    MdStructDAO mdStruct = getMdStructIfExist(name);
    
    if (mdStruct == null)
    {
      mdStruct = TestFixtureFactory.createMdStruct(name);
      mdStruct.apply();
    }
    
    return mdStruct;
  }
  
  public static MdDomainDAO getOrCreateMdDomain(String name)
  {
    MdDomainDAO mdDomain = getMdDomainIfExist(name);
    
    if (mdDomain == null)
    {
      mdDomain = MdDomainDAO.newInstance();
      mdDomain.setValue(MdDomainInfo.DOMAIN_NAME, name);
      mdDomain.setStructValue(MdDomainInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Domain");
      mdDomain.apply();
    }
    
    return mdDomain;
  }
  
  public static MdDimensionDAO getOrCreateMdDimension(String name)
  {
    MdDimensionDAO mdDimension = getMdDimensionIfExist(name);
    
    if (mdDimension == null)
    {
      mdDimension = MdDimensionDAO.newInstance();
      mdDimension.setValue(MdDimensionInfo.NAME, name);
      mdDimension.setStructValue(MdDimensionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "SharedTestDataManager Dimension " + name);
      mdDimension.apply();
    }
    
    return mdDimension;
  }
  
  public static MdBusinessDAO getOrCreateMdBusiness(String name)
  {
    return getOrCreateMdBusiness(name, null);
  }
  
  public static MdBusinessDAO getOrCreateMdBusiness(String name, MdTypeConfigurator configurator)
  {
    MdBusinessDAO mdBusiness = (MdBusinessDAO) getMdTypeIfExist(TestFixConst.TEST_PACKAGE, name);
    
    if (mdBusiness == null)
    {
      mdBusiness = TestFixtureFactory.createMdBusiness(name);
      
      if (configurator != null)
      {
        configurator.configure(mdBusiness);
      }
      
      mdBusiness.apply();
    }
    
    return mdBusiness;
  }
  
  public static MdEnumerationDAO getOrCreateMdEnumeration(String name, String masterMdBusinessOid)
  {
    return getOrCreateMdEnumeration(name, masterMdBusinessOid, null);
  }
  
  public static MdEnumerationDAO getOrCreateMdEnumeration(String name, String masterMdBusinessOid, MdTypeConfigurator configurator)
  {
    MdEnumerationDAO mdEnumeration = (MdEnumerationDAO) getMdTypeIfExist(TestFixConst.TEST_PACKAGE, name);
    
    if (mdEnumeration == null)
    {
      mdEnumeration = MdEnumerationDAO.newInstance();
      mdEnumeration.setValue(MdEnumerationInfo.NAME, name);
      mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enumeration Test");
      mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, TestFixConst.TEST_PACKAGE);
      mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, masterMdBusinessOid);
      mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
      
      if (configurator != null)
      {
        configurator.configure(mdEnumeration);
      }
      
      mdEnumeration.apply();
    }
    
    return mdEnumeration;
  }
  
  public static MdViewDAO getOrCreateMdView(String name)
  {
    MdViewDAO mdBusiness = (MdViewDAO) getMdTypeIfExist(TestFixConst.TEST_PACKAGE, name);
    
    if (mdBusiness == null)
    {
      mdBusiness = TestFixtureFactory.createMdView(name);
      mdBusiness.apply();
    }
    
    return mdBusiness;
  }
  
  public static MdMethodDAO getOrCreateMdMethod(String name, String mdTypeOid, boolean isStatic, String returnType)
  {
    MdMethodDAO mdMethod = getMdMethodIfExist(name, mdTypeOid);
    
    if (mdMethod == null)
    {
      mdMethod = MdMethodDAO.newInstance();

      mdMethod.setValue(MdMethodInfo.NAME, name);
      mdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdTypeOid);
      mdMethod.setValue(MdMethodInfo.RETURN_TYPE, returnType == null ? TestFixConst.TEST_METHOD_RETURN_TYPE : returnType);
      mdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, name);
      mdMethod.setStructValue(MdMethodInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, name);
      mdMethod.setValue(MdMethodInfo.IS_STATIC, isStatic ? MdAttributeBooleanInfo.TRUE : MdAttributeBooleanInfo.FALSE);
      
      mdMethod.apply();
    }
    
    return mdMethod;
  }
  
  public static MdRelationshipDAO getOrCreateMdRelationship(String name, MdBusinessDAO parent, MdBusinessDAO child)
  {
    MdRelationshipDAO mdRelationship = (MdRelationshipDAO) getMdTypeIfExist(TestFixConst.TEST_PACKAGE, name);
    
    if (mdRelationship == null)
    {
      mdRelationship = MdRelationshipDAO.newInstance();
      mdRelationship.setValue(MdRelationshipInfo.NAME, name);
      mdRelationship.setValue(MdRelationshipInfo.PACKAGE, TestFixConst.TEST_PACKAGE);
      mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "SharedTestData MdRelationship");
      mdRelationship.setStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "SharedTestData MdRelationship");
      mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
      mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "SharedTestData MdRelationship Parent");
      mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, parent.getOid());
      mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, TestFixConst.TEST_CLASS2);
      mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
      mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "SharedTestData MdRelationship Child");
      mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, child.getOid());
      mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, TestFixConst.TEST_CLASS1);
      mdRelationship.apply();
    }
    
    return mdRelationship;
  }
  
  public static UserDAO getOrCreateUserDAO(String username, String password, Integer sessionLimit, boolean inactive)
  {
    UserDAO user = getUserIfExist(username);
    
    if (user == null)
    {
      user = UserDAO.newInstance();
      user.setValue(UserInfo.USERNAME, username);
      user.setValue(UserInfo.PASSWORD, password);
      user.setValue(UserInfo.SESSION_LIMIT, Integer.toString(sessionLimit));
      user.setValue(UserInfo.INACTIVE, inactive ? MdAttributeBooleanInfo.TRUE : MdAttributeBooleanInfo.FALSE);
      user.apply();
    }
    
    return user;
  }
  
  public static MdStructDAO getMdStructIfExist(String name)
  {
    MdStructQuery query = new MdStructQuery(new QueryFactory());
    query.WHERE(query.get(MdStructInfo.NAME).EQ(name));
    OIterator<? extends MdStruct> it = query.getIterator();
    try
    {
      while (it.hasNext())
      {
        MdStruct struct = it.next();
        
        if (struct == null)
        {
          return null;
        }
        else
        {
          return (MdStructDAO) BusinessFacade.getEntityDAO(struct);
        }
      }
    }
    finally
    {
      it.close();
    }

    return null;
  }
  
  public static MdDomainDAO getMdDomainIfExist(String name)
  {
    MdDomainQuery query = new MdDomainQuery(new QueryFactory());
    query.WHERE(query.getDomainName().EQ(name));
    OIterator<? extends MdDomain> it = query.getIterator();
    try
    {
      while (it.hasNext())
      {
        MdDomain domain = it.next();
        
        if (domain == null)
        {
          return null;
        }
        else
        {
          return (MdDomainDAO) BusinessFacade.getEntityDAO(domain);
        }
      }
    }
    finally
    {
      it.close();
    }

    return null;
  }
  
  public static MdDimensionDAO getMdDimensionIfExist(String name)
  {
    MdDimensionQuery query = new MdDimensionQuery(new QueryFactory());
    query.WHERE(query.getName().EQ(name));
    OIterator<? extends MdDimension> it = query.getIterator();
    try
    {
      while (it.hasNext())
      {
        MdDimension business = it.next();
        
        if (business == null)
        {
          return null;
        }
        else
        {
          return (MdDimensionDAO) BusinessFacade.getEntityDAO(business);
        }
      }
    }
    finally
    {
      it.close();
    }

    return null;
  }
  
  public static MdMethodDAO getMdMethodIfExist(String name, String mdTypeOid)
  {
    MdMethodQuery query = new MdMethodQuery(new QueryFactory());
    query.WHERE(query.getMethodName().EQ(name));
    query.WHERE(query.getMdType().EQ(mdTypeOid));
    OIterator<? extends MdMethod> it = query.getIterator();
    try
    {
      while (it.hasNext())
      {
        MdMethod business = it.next();
        
        if (business == null)
        {
          return null;
        }
        else
        {
          return (MdMethodDAO) BusinessFacade.getEntityDAO(business);
        }
      }
    }
    finally
    {
      it.close();
    }

    return null;
  }
  
  public static MdTypeDAO getMdTypeIfExist(String pack, String type)
  {
    MdTypeQuery mbq = new MdTypeQuery(new QueryFactory());
    mbq.WHERE(mbq.getPackageName().EQ(pack));
    mbq.WHERE(mbq.getTypeName().EQ(type));
    OIterator<? extends MdType> it = mbq.getIterator();
    try
    {
      while (it.hasNext())
      {
        MdType business = it.next();
        
        if (business == null)
        {
          return null;
        }
        else
        {
          return (MdTypeDAO) BusinessFacade.getEntityDAO(business);
        }
      }
    }
    finally
    {
      it.close();
    }

    return null;
  }
  
  public static UserDAO getUserIfExist(String username)
  {
    UsersQuery query = new UsersQuery(new QueryFactory());
    query.WHERE(query.getUsername().EQ(username));
    OIterator<? extends Users> it = query.getIterator();
    try
    {
      while (it.hasNext())
      {
        Users business = it.next();
        
        if (business == null)
        {
          return null;
        }
        else
        {
          return (UserDAO) BusinessFacade.getEntityDAO(business);
        }
      }
    }
    finally
    {
      it.close();
    }

    return null;
  }
  
  public static void deleteAllEntityObjects(MdEntityDAOIF mdEntityIF)
  {
    EntityQuery query = new QueryFactory().entityQueryDAO(mdEntityIF);
    OIterator<? extends ComponentIF> it = query.getIterator();
    
    while (it.hasNext())
    {
      ( (Entity) it.next() ).delete();
    }
  }
  
  public static void deleteAllStructObjects(String pack, String name)
  {
    StructDAOQuery query = new QueryFactory().structDAOQuery(pack + "." + name);
    OIterator<? extends StructDAOIF> it = query.getIterator();
    
    while (it.hasNext())
    {
      ( (StructDAO) it.next() ).delete();
    }
  }
  
  public static void deleteAllRelationshipObjects(String pack, String name)
  {
    RelationshipDAOQuery query = new QueryFactory().relationshipDAOQuery(pack + "." + name);
    OIterator<? extends RelationshipDAOIF> it = query.getIterator();
    
    while (it.hasNext())
    {
      ( (RelationshipDAO) it.next() ).delete();
    }
  }
  
  public static void deleteAllBusinessObjects(String pack, String name)
  {
    BusinessDAOQuery query = new QueryFactory().businessDAOQuery(pack + "." + name);
    OIterator<? extends BusinessDAOIF> it = query.getIterator();
    
    while (it.hasNext())
    {
      ( (EntityDAO) it.next() ).delete();
    }
  }
  
//  public static MdAttributeDAO getMdAttributeIfExist()
//  {
//    MdAttributeDAO mdAttribute = getMdDomainIfExist(TestFixConst.TEST_DOMAIN);
//    
//    if (mdAttribute == null)
//    {
//      mdAttribute = TestFixtureFactory.createMdDomain();
//      mdAttribute.apply();
//    }
//    
//    return mdAttribute;
//  }
}
