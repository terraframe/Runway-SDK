package com.runwaysdk.dataaccess.metadata;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.DataAccessException;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.database.Database;

public class MdAttributeIndicator_E extends MdAttributeConcrete_E
{
  /**
   * 
   */
  private static final long serialVersionUID = -927194333693014887L;

  /**
   * @param mdAttribute
   */
  public MdAttributeIndicator_E(MdAttributeConcreteDAO mdAttribute)
  {
    super(mdAttribute);
  }
  
  /**
   * Returns the formated DB column type used in the database in the syntax of the current DB vendor.
   * @return formated DB column type used in the database in the syntax of the current DB vendor.
   */
  protected String getDbColumnType()
  {
    String dbType = DatabaseProperties.getDatabaseType(this.getMdAttribute());
    return Database.formatCharacterField(dbType, Database.DATABASE_ID_SIZE);
  }
  
  /**
   * Adds a column in the database to the given table.
   *
   * <br/><b>Precondition:</b>  tableName != null
   * <br/><b>Precondition:</b>  !tableName.trim().equals("")
   *
   */
  protected void createDbColumn(String tableName)
  {
    if(!this.appliedToDB)
    {
      Database.addField(tableName, this.getMdAttribute().getColumnName(), this.dbColumnType, this.getMdAttribute());
    }
  }
  
  /**
   *Validates this metadata object.
   *
   * @throws DataAccessException when this MetaData object is not valid.
   */
  protected void validate()
  {
    if (this.getMdAttribute().isNew())
    {
      this.validateNew();
    }

    super.validate();
  }
  
  /**
   * Cannot reference instances of a structs.
   *
   */
  private void validateNew()
  {
    MdEntityDAOIF mdEntityIF = this.definedByClass();
    if (mdEntityIF instanceof MdStructDAOIF)
    {
      String errMsg = "[" + mdEntityIF.definesType()
          + "] is a StructDAO and cannot have a reference attribute.";
      throw new AttributeOfWrongTypeForClassException(errMsg, this.getMdAttribute(), mdEntityIF);
    }
  }
}
