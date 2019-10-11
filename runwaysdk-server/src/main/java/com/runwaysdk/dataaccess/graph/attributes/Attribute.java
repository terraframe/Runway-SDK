package com.runwaysdk.dataaccess.graph.attributes;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.graph.GraphObjectDAO;
import com.runwaysdk.dataaccess.graph.GraphObjectDAOIF;

public abstract class Attribute implements AttributeIF
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -94524589150108217L;
  
  /**
   * The {@link GraphObjectDAO}  that contains this Attribute. Not defined on creation, but set by the
   * containing {@link GraphObjectDAO. <br>
   * <b>invariant </b> graphObjectDAO != null
   */
  private GraphObjectDAO    graphObjectDAO;
  
  /**
   * Qualified name of the class that defines this attribute. <br>
   * <b>invariant </b> definingTransientType != null <br>
   * <b>invariant </b> !definingTransientType.equals("")
   */
//  final private String definingGraphType;

  protected String mdAttributeKey;
  
  /**
   * Value of the attribute. <br>
   * <b>invariant </b> value != null
   */
  protected Object       value;
  
  /**
   * Indicates if the value of this Attribute has been modified since it was last applied
   * to the database.
   */
//  private boolean    isModified = false;
  
  /**
   * Creates an attribute with the given name and initializes the value to blank.
   *
   * <br>
   * <b>Precondition: </b> name != null <br>
   * <b>Precondition: </b> !name.trim().equals("") <br>
   * <b>Precondition: </b> definingTransientType != null <br>
   * <b>Precondition: </b> !definingTransientType().equals("") <br>
   * <b>Precondition: </b> definingTransientType is the name of a class that defines an attribute with
   * this name
   *
   * @param name name of the attribute
   * @param mdAttributeKey key of the defining metadata.
   * @param definingTransientType name of the class that defines this attribute
   */
  protected Attribute(String name, String mdAttributeKey, String definingTransientType)
  {
    super();
//    this.name = name;
//    this.mdAttributeKey = mdAttributeKey;
//    this.definingTransientType = definingTransientType;
//    this.value = "";
//    this.containingTransientDAO = null;
  }

  @Override
  public GraphObjectDAOIF getContainingComponent()
  {
    return this.graphObjectDAO;
  }

  @Override
  public String getDefiningClassType()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MdAttributeDAOIF getMdAttribute()
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
  public MdAttributeDAOIF getMdAttributeConcrete()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getName()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDisplayLabel(Locale locale)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Map<String, String> getDisplayLabes()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getValue()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object getObjectValue()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getRawValue()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isModified()
  {
    // TODO Auto-generated method stub
    return false;
  }

}
