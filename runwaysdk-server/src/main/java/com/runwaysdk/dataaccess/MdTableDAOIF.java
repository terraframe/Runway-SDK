package com.runwaysdk.dataaccess;

import java.util.List;

import com.runwaysdk.dataaccess.metadata.MdTableDAO;

public interface MdTableDAOIF extends MdTableClassIF
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
  
  /**
   * Returns the name of the table that this metadata represents.
   * @return name of the table that this metadata represents.
   */
  public String getTableName();
}
