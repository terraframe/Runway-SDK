package com.runwaysdk.query;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.dataaccess.EnumerationItemDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeIndicatorDAOIF;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.database.Database;

public class AttributeIndicatorComposite extends AttributeIndicator
{
  private AttributeIndicator    leftOperand;
  
  private EnumerationItemDAOIF  operator;
  
  private AttributeIndicator    rightOperand;
  
  private String                attributeName;
  
  private String                attributeNamespace;
  
  protected String              columnAlias;

  
  protected AttributeIndicatorComposite(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeNamespace, AttributeIndicator _leftOperand, EnumerationItemDAOIF _operator, AttributeIndicator _rightOperand)
  {
    super(_mdAttributeIndicator);
    this.init(_attributeNamespace, _leftOperand, _operator, _rightOperand);
  }
  
  protected AttributeIndicatorComposite(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeNamespace, AttributeIndicator _leftOperand, EnumerationItemDAOIF _operator, AttributeIndicator _rightOperand, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    super(_mdAttributeIndicator, _userDefinedAlias, _userDefinedDisplayLabel);
    this.init(_attributeNamespace, _leftOperand, _operator, _rightOperand);
  }
  

  private void init(String _attributeNamespace, AttributeIndicator _leftOperand, EnumerationItemDAOIF _operator, AttributeIndicator _rightOperand)
  {
    this.attributeNamespace = _attributeNamespace;
    this.leftOperand        = _leftOperand;
    this.operator           = _operator;
    this.rightOperand       = _rightOperand;
    
    this.columnAlias = this.getRootQuery().getColumnAlias(this.getAttributeNameSpace(), this.getDbColumnName());

  }

  @Override
  public Condition EQ(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionEq(this, statementPrimitive);
  }

  @Override
  public Condition NE(String statement)
  {
    StatementPrimitive statementPrimitive = this.formatAndValidate(statement);
    return new BasicConditionNotEq(this, statementPrimitive);
  }

  @Override
  public AttributeCondition SUBSELECT_IN(Selectable selectable)
  {
    return new SubSelectExplicit_IN_Condition(this, selectable);
  }

  @Override
  public AttributeCondition SUBSELECT_NOT_IN(Selectable selectable)
  {
    return new SubSelectExplicit_NOT_IN_Condition(this, selectable);
  }

  @Override
  public String getDbColumnName()
  {
//    if (this.attributeName == null)
//    {
//      this.attributeName = this.getRootQuery().getColumnAlias(this.getAttributeNameSpace() + this.calculateName(), this.getFunctionName());
//    }
    return this.attributeName;
  }

//  /**
//   * Calculates a name for the result set.
//   * 
//   * @return a name for the result set.
//   */
//  protected String calculateName()
//  {
//    String displayLabel;
//
//    // selectable is a function
//    if (this.selectable instanceof Function)
//    {
//      displayLabel = this.getFunctionName() + "(" + ( (Function) this.selectable ).calculateName() + ")";
//    }
//    else // its an attribute or selectableSQL
//    {
//      displayLabel = this.getFunctionName() + "(" + this.selectable._getAttributeName() + ")";
//    }
//
//    return displayLabel;
//  }
  
  @Override
  public String _getAttributeName()
  {
    return this.getDbColumnName();
  }

  @Override
  public String getUserDefinedAlias()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setUserDefinedAlias(String userDefinedAlias)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public String getUserDefinedDisplayLabel()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setUserDefinedDisplayLabel(String userDefinedDisplayLabel)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public String getColumnAlias()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getResultAttributeName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setColumnAlias(String alias)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public String getDbQualifiedName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getAttributeNameSpace()
  {
    return this.attributeNamespace;
  }

  @Override
  public String getFullyQualifiedNameSpace()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MdAttributeConcreteDAOIF getMdAttributeIF()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setAdditionalEntityMdAttributes(List<MdAttributeConcreteDAOIF> mdAttributeConcreteDAOIFList)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public String getDefiningTableName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDefiningTableAlias()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Attribute getAttribute()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ComponentQuery getRootQuery()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SelectableAggregate getAggregateFunction()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isAggregateFunction()
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getSQL()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getSubSelectSQL()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Set<Join> getJoinStatements()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, String> getFromTableMap()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void accept(Visitor visitor)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public Condition getCondition(String operator, String value)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ColumnInfo getColumnInfo()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<ColumnInfo> getColumnInfoList()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setData(Object data)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public Object getData()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Validates and formats the given string into an integer format for the
   * current database.
   * @param statement
   * @return StatementPrimitive
   */
  protected StatementPrimitive formatAndValidate(String statement)
  {
    String javaType = this.mdAttributeIndicator.javaType(false);
    
    StatementPrimitive statementPrimitive = null;
    
    if (javaType.equals(Boolean.class.getName()))
    {
      statementPrimitive = this.formatAndValidateBoolean(statement);
    }
    else if (javaType.equals(Long.class.getName()))
    {
      statementPrimitive = this.formatAndValidateLong(statement);
    }
    else if (javaType.equals(Double.class.getName()))
    {
      statementPrimitive = this.formatAndValidateDouble(statement);
    }
    else // BigDecimal
    {
      statementPrimitive = this.formatAndValidateDecimal(statement);
    }
    
    return statementPrimitive;
  }
  
  /**
   * Validates and formats the given string into a boolean format for the
   * current database.
   * @param statement
   * @return StatementPrimitive
   */
  protected StatementPrimitive formatAndValidateBoolean(String statement)
  {
    return QueryUtil.formatAndValidateBoolean(statement);
  }
  
  /**
   * Validates and formats the given string into a decimal format for the
   * current database.
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidateDecimal(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDecimal.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";
      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeDecimalInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }
  
  /**
   * Validates and formats the given string into a long format for the
   * current database.
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidateLong(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeLong.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";
      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeLongInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }
  
  /**
   * Validates and formats the given string into a double format for the
   * current database.
   * @param statement
   * @return
   */
  protected StatementPrimitive formatAndValidateDouble(String statement)
  {
    // Throw an exception if this value is invalid
    if (!com.runwaysdk.dataaccess.attributes.entity.AttributeDouble.isValid(statement))
    {
      String error = "Value [" + statement + "] is not valid for query attribute [" + this._getAttributeName() + "]";
      throw new AttributeValueException(error, statement);
    }

    String formattedValue = Database.formatJavaToSQL(statement, MdAttributeDoubleInfo.CLASS, false);
    return new StatementPrimitive(formattedValue);
  }
}
