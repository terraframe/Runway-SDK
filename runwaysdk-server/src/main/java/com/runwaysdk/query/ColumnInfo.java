/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.query;

import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.database.Database;


public class ColumnInfo
{
  private String tableName;
  private String tableAlias;
  private String columnName;
  private String columnAlias;
    
  /**
   * 
   * @param tableName
   * @param tableAlias
   * @param columnName
   * @param columnAlias
   */
  public ColumnInfo(String tableName, String tableAlias, String columnName, String columnAlias)
  {
    this.tableName   = tableName;
    this.tableAlias  = tableAlias;
    this.columnName  = columnName;
    this.columnAlias = columnAlias;
  }

  /**
   * 
   * @return
   */
  public String getTableName()
  {
    return tableName;
  }

  /**
   * 
   * @return
   */
  public String getTableAlias()
  {
    return tableAlias;
  }  
  
  /**
   * 
   * @return
   */
  public String getColumnName()
  {
    return columnName;
  }  
  
  /**
   * 
   * @return
   */
  public String getColumnAlias()
  {
    return columnAlias;
  }
  
  /**
   * Returns the qualified name of the attribute.
   */
  public String getDbQualifiedName()
  {
    return this.getTableAlias()+"."+this.getColumnName();
  }
  
  /**
   * Returns a string to be used in a select clause for the database 
   * column represented by this object.
   * @return string to be used in a select clause for the database 
   * column represented by this object.
   */
  public String getSelectClauseString()
  {
    return this.getDbQualifiedName()+" "+Database.formatColumnAlias(this.getColumnAlias());
  }
  
  /**
   * Returns a string to be used in a select clause for the database 
   * column represented by this object.
   * @return string to be used in a select clause for the database 
   * column represented by this object.
   */
  public String getSelectClauseString(MdAttributeConcreteDAOIF mdAttribute)
  {   
    return this.getSelectClauseNoAlias(mdAttribute)+" "+Database.formatColumnAlias(this.getColumnAlias());
  }
  
  /**
   * Returns a string to be used in a select clause for the database 
   * column represented by this object, but does not include the alias.
   * @return string to be used in a select clause for the database 
   * column represented by this object, but does not include the alias.
   */
  protected String getSelectClauseNoAlias(MdAttributeConcreteDAOIF mdAttribute)
  {
    return Database.formatSelectClauseColumn(this.getDbQualifiedName(), mdAttribute);
  }
}
