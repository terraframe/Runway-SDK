package com.runwaysdk.dataaccess;

import java.util.List;

import com.runwaysdk.dataaccess.metadata.MdTableDAO;

public interface MdTableDAOIF extends MdClassDAOIF
{
  /**
   * Name of the table used to store instances of this class.
   */
  public static final String TABLE   = "md_table";
  
  /**
   * {@link MdTableDAOIF} does not currently support inheritance.
   */
  @Override
  public List<? extends MdTableDAOIF> getSubClasses();
  
  /**
   * {@link MdTableDAOIF} does not currently support inheritance.
   */
  @Override
  public MdTableDAOIF getSuperClass();
  
  /**
   * @see com.runwaysdk.dataaccess.BusinessDAO#getBusinessDAO()
   */
  public MdTableDAO getBusinessDAO();
  
}
