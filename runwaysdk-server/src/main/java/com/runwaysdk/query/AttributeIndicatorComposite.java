package com.runwaysdk.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MathOperatorInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
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

  
  protected AttributeIndicatorComposite(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeNamespace, String _definingTableName, String _definingTableAlias, ComponentQuery _rootQuery, AttributeIndicator _leftOperand, EnumerationItemDAOIF _operator, AttributeIndicator _rightOperand)
  {
    super(_mdAttributeIndicator, _definingTableName, _definingTableAlias, _rootQuery);
    this.init(_attributeNamespace, _leftOperand, _operator, _rightOperand);
  }
  
  protected AttributeIndicatorComposite(MdAttributeIndicatorDAOIF _mdAttributeIndicator, String _attributeNamespace, String _definingTableName, String _definingTableAlias, ComponentQuery _rootQuery, AttributeIndicator _leftOperand, EnumerationItemDAOIF _operator, AttributeIndicator _rightOperand, String _userDefinedAlias, String _userDefinedDisplayLabel)
  {
    super(_mdAttributeIndicator, _definingTableName, _definingTableAlias, _userDefinedAlias, _userDefinedDisplayLabel, _rootQuery);
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
    if (this.attributeName == null)
    {
      this.attributeName = this.getRootQuery().getColumnAlias(this.getAttributeNameSpace() + this.calculateName(), this.getMdAttributeIF().definesAttribute());
    }
    return this.attributeName;
  }

  /**
   * Calculates a name for the result set.
   * 
   * @return a name for the result set.
   */
  protected String calculateName()
  {
    String displayLabel = this.leftOperand.calculateName()+operator.getAttributeIF(EnumerationMasterInfo.NAME)+this.rightOperand.calculateName();
    
    return displayLabel;
  }
  
  @Override
  public String _getAttributeName()
  {
    return this.getDbColumnName();
  }


  @Override
  public String getColumnAlias()
  {
    return this.columnAlias;
  }

  @Override
  public String getResultAttributeName()
  {
    if (this.userDefinedAlias.trim().length() != 0)
    {
      return this.userDefinedAlias;
    }
    else
    {
      return this._getAttributeName();
    }
  }

  @Override
  public void setColumnAlias(String alias)
  {
    this.columnAlias = alias;
  }

  @Override
  public String getDbQualifiedName()
  {
    return this.getDefiningTableAlias() + "." + this.getDbColumnName();
  }

  @Override
  public String getAttributeNameSpace()
  {
    return this.attributeNamespace;
  }

  @Override
  public String getFullyQualifiedNameSpace()
  {
    return this.getAttributeNameSpace() + "." + this._getAttributeName();
  }

  @Override
  public MdAttributeConcreteDAOIF getMdAttributeIF()
  {
    // TODO Auto-generated method stub
    return this.mdAttributeIndicator;
  }

  @Override
  public Set<MdAttributeConcreteDAOIF> getAllEntityMdAttributes()
  {
    Set<MdAttributeConcreteDAOIF> mdAttributeList = new HashSet<MdAttributeConcreteDAOIF>();
    mdAttributeList.addAll(this.leftOperand.getAllEntityMdAttributes());
    mdAttributeList.addAll(this.rightOperand.getAllEntityMdAttributes());
    return mdAttributeList;
  }


  /**
   * Every Selectable eventually boils down to an attribute.
   * 
   * @return bottom most attribute.
   */
  @Override
  public Attribute getAttribute()
  {
    return this.leftOperand.getAttribute();
  }

  @Override
  public boolean isAggregateFunction()
  {
    return true;
  }

  @Override
  public SelectableAggregate getAggregateFunction()
  {
    SelectableAggregate selectableAggregate1 = this.leftOperand.getAggregateFunction();

    if (selectableAggregate1 != null)
    {
      return selectableAggregate1;
    }

    if (this.rightOperand != null)
    {
      SelectableAggregate selectableAggregate2 = this.rightOperand.getAggregateFunction();

      if (selectableAggregate2 != null)
      {
        return selectableAggregate2;
      }
    }

    // Base case
    return null;
  }
  
  @Override
  public String getSQL()
  {
    String sql = this.leftOperand.getSQL();
    sql += " "+this.operator.getValue(MathOperatorInfo.OPERATOR_SYMBOL) + " ";
    sql += "NULLIF("+this.rightOperand.getSQL()+", 0)";
        
    return sql;
  }

  /**
   *
   */
  public String getSQLIgnoreCase()
  {
    String sql = this.leftOperand.getSQLIgnoreCase();
    sql += " "+this.operator.getValue(MathOperatorInfo.OPERATOR_SYMBOL) + " ";
    sql += "NULLIF("+this.rightOperand.getSQLIgnoreCase()+", 0)";
        
    return sql;
  }
  
  @Override
  public String getSubSelectSQL()
  {
    return this.getSQL();
  }

  @Override
  public Set<Join> getJoinStatements()
  {
    Set<Join> joinSet = new HashSet<Join>();
    joinSet.addAll(this.leftOperand.getJoinStatements());
    joinSet.addAll(this.rightOperand.getJoinStatements());
    return joinSet;
  }

  @Override
  public Map<String, String> getFromTableMap()
  {
    Map<String, String> fromTableMap = new HashMap<String, String>();
    fromTableMap.putAll(this.leftOperand.getFromTableMap());
    fromTableMap.putAll(this.rightOperand.getFromTableMap());
    return fromTableMap;
  }

  @Override
  public void accept(Visitor visitor)
  {
    this.leftOperand.accept(visitor);
    this.rightOperand.accept(visitor);
  }

  @Override
  public Condition getCondition(String operator, String value)
  {
    return QueryUtil.getCondition(this, operator, value);
  }

  @Override
  public ColumnInfo getColumnInfo()
  {
    return new ColumnInfo(this.getDefiningTableName(), this.getDefiningTableAlias(), this.getDbColumnName(), this.getColumnAlias());
  }

  @Override
  public List<ColumnInfo> getColumnInfoList()
  {
    List<ColumnInfo> columnInfoList = new LinkedList<ColumnInfo>();
    columnInfoList.add(this.getColumnInfo());
    return columnInfoList;
  }

  /**
   * Returns the Selectable that is a parameter to the function.
   * 
   * @return Selectable that is a parameter to the function.
   */
  public Selectable getSelectable()
  {
    return this.leftOperand.getSelectable();
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
