/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk;

import java.io.File;
import java.util.List;
import java.util.Locale;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.ClassLoaderException;
import com.runwaysdk.business.DeleteTypePermissionException;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.LockException;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.UnimplementedStubException;
import com.runwaysdk.business.generation.CompilerException;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.AttributeIF;
import com.runwaysdk.dataaccess.AttributeLocalIF;
import com.runwaysdk.dataaccess.CannotDeleteReferencedObject;
import com.runwaysdk.dataaccess.DeleteUnappliedObjectException;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.DuplicateGraphPathException;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeEncryptionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeRefDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdElementDAOIF;
import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;
import com.runwaysdk.dataaccess.MdParameterDAOIF;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.MetadataDAOIF;
import com.runwaysdk.dataaccess.MissingKeyNameValue;
import com.runwaysdk.dataaccess.RelationshipConstraintException;
import com.runwaysdk.dataaccess.StaleEntityException;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.attributes.AttributeTypeException;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblem;
import com.runwaysdk.dataaccess.attributes.InvalidAttributeTypeException;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.SystemAttributeProblem;
import com.runwaysdk.dataaccess.cache.CacheCodeException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.AbstractInstantiationException;
import com.runwaysdk.dataaccess.io.XMLException;
import com.runwaysdk.dataaccess.metadata.AttributeDefinitionException;
import com.runwaysdk.dataaccess.metadata.ClassPublishException;
import com.runwaysdk.dataaccess.metadata.DuplicateMdEnumerationDefinitionException;
import com.runwaysdk.dataaccess.metadata.IdenticalIndexException;
import com.runwaysdk.dataaccess.metadata.InheritanceException;
import com.runwaysdk.dataaccess.metadata.InvalidColumnNameException;
import com.runwaysdk.dataaccess.metadata.InvalidDefinitionException;
import com.runwaysdk.dataaccess.metadata.InvalidIdentifierException;
import com.runwaysdk.dataaccess.metadata.InvalidIndicatorDefinition;
import com.runwaysdk.dataaccess.metadata.InvalidMRUCacheSizeException;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEntityDAO;
import com.runwaysdk.dataaccess.metadata.MetadataCannotBeDeletedException;
import com.runwaysdk.dataaccess.metadata.MetadataDAO;
import com.runwaysdk.dataaccess.metadata.MethodDefinitionException;
import com.runwaysdk.dataaccess.metadata.MethodDefinitionException_DefiningType;
import com.runwaysdk.dataaccess.metadata.NameConventionException;
import com.runwaysdk.dataaccess.metadata.NoAttributeOnIndexException;
import com.runwaysdk.dataaccess.metadata.ParameterDefinitionException_InvalidType;
import com.runwaysdk.dataaccess.metadata.ParameterDefinitionException_NameExists;
import com.runwaysdk.dataaccess.metadata.ParameterMarker;
import com.runwaysdk.dataaccess.metadata.ReservedWordException;
import com.runwaysdk.dataaccess.transaction.SynchronizationSequenceGapException;
import com.runwaysdk.dataaccess.transaction.TransactionImportInvalidItem;
import com.runwaysdk.dataaccess.transaction.TransactionVersionException;
import com.runwaysdk.facade.GroovyQueryException;
import com.runwaysdk.query.AmbiguousAttributeException;
import com.runwaysdk.query.AttributeNotInGroupByOrAggregate;
import com.runwaysdk.query.InvalidMomentSelectableException;
import com.runwaysdk.query.InvalidNumericSelectableException;
import com.runwaysdk.query.InvalidOrderByPrimitiveException;
import com.runwaysdk.query.MissingAttributeInSelectForGroupByException;
import com.runwaysdk.query.MissingHavingClauseAttributeException;
import com.runwaysdk.query.NoAggregateInGroupByException;
import com.runwaysdk.query.SubSelectReturnedMultipleRowsException;
import com.runwaysdk.query.ValueQueryMissingSelectCaluseException;
import com.runwaysdk.session.GroovyQueryExecuteException;
import com.runwaysdk.session.InactiveUserException;
import com.runwaysdk.session.InvalidLoginException;
import com.runwaysdk.session.InvalidSessionException;
import com.runwaysdk.session.LoginNotSupportedException;
import com.runwaysdk.session.MaximumSessionsException;
import com.runwaysdk.session.PermissionException;
import com.runwaysdk.session.RoleManagementException_ADD;
import com.runwaysdk.session.RoleManagementException_REMOVE;
import com.runwaysdk.session.Session;
import com.runwaysdk.vault.VaultException;
import com.runwaysdk.web.AdminScreenAccessException;

/**
 * Provides typesafe getter for access to localized business-layer error
 * messages.
 * 
 * @author Richard Rowlands
 */
public class ServerExceptionMessageLocalizer
{
  /**
   * Gets the localized {@link AbstractInstantiationException} message, which
   * indicates an error in a type or attribute definition.
   * 
   * @param locale
   *          The desired locale
   * @param mdEntity
   *          The metadata describing the abstrat type that the core tried to
   *          instantiate
   * @return The localized error message
   */
  public static String invalidIdException(Locale locale, String oid)
  {
    return LocalizationFacade.getMessage(locale, "InvalidIdException", "Object ID [{0}] is not a valid ID.", oid);
  }

  /**
   * Gets the localized message for the {@link AdminScreenAccessException},
   * which is thrown when a user tries to access the admin screen without proper
   * role access.
   * 
   * @param locale
   * @param user
   * @return
   */
  public static String adminScreenAccessException(Locale locale, SingleActorDAOIF user)
  {
    return LocalizationFacade.getMessage(locale, "AdminScreenAccessException", "The user [{0}] does not have access to the Admin Screen.", user.getSingleActorName());
  }

  /**
   * Gets the localized message for the {@link ProblemExceptionDTO}, which is
   * thrown when a user tries to access the admin screen without proper role
   * access.
   * 
   * @param locale
   * @param user
   * @return
   */
  public static String problemCollectionException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "ProblemCollectionException", "A problem occurred and your operation could not be completed.");
  }

  /**
   * Gets the localized {@link AbstractInstantiationException} message, which
   * indicates an error in a type or attribute definition.
   * 
   * @param locale
   *          The desired locale
   * @param mdEntity
   *          The metadata describing the abstrat type that the core tried to
   *          instantiate
   * @return The localized error message
   */
  public static String abstractInstantiationException(Locale locale, MdClassDAOIF mdClass)
  {
    return LocalizationFacade.getMessage(locale, "AbstractInstantiationException", "The type [{0}] is abstract, and cannot be instantiated.", mdClass.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link AttributeDefinitionException} message, which
   * indicates an error in an attribute definition
   * 
   * @param locale
   *          The desired locale
   * @param mdAttribute
   *          The metadata describing the abstrat type that the core tried to
   *          instantiate
   * @return The localized error message
   */
  public static String attributeDefinitionException(Locale locale, MdAttributeDAOIF mdAttribute)
  {
    return LocalizationFacade.getMessage(locale, "AttributeDefinitionException", "The definition of attribute [{0}] contains an error.  Please check your input and try again. If the problem continues, please contact your support staff.", mdAttribute.getDisplayLabel(locale));
  }

  /**
   * Constructor to set the developer message and the MdAttributeIF of the
   * attribute that is already defined in the inheritance hierarchy of the given
   * class.
   * 
   * @param locale
   *          The desired locale
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param mdClassIF
   *          class that a new attribute is being added to.
   * @param parentMdClassIF
   *          class that already has the attribute defined.
   */
  public static String duplicateAttributeInInheritedHierarchy(Locale locale, MdAttributeDAOIF mdAttribute, MdClassDAOIF mdClassIF, MdClassDAOIF parentMdClassIF)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateAttributeInInheritedHierarchy", "Cannot add an attribute named [{0}] to class [{1}] because its parent class [{2}] already defines an attribute with that name.", mdAttribute.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale), parentMdClassIF.getDisplayLabel(locale));
  }

  /**
   * Constructor to set the developer message and the MdAttributeIF of the
   * attribute that is already defined by a subclass of the given class.
   * 
   * @param locale
   *          The desired locale
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param mdClassIF
   *          class that a new attribute is being added to.
   * @param childMdClassIF
   *          class that already has the attribute defined.
   */
  public static String duplicateAttributeDefinedInSubclass(Locale locale, MdAttributeDAOIF mdAttribute, MdClassDAOIF mdClassIF, MdClassDAOIF childMdClassIF)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateAttributeDefinedInSubclass", "Cannot add an attribute named [{0}] to class [{1}] because a child class [{3}] already defines an attribute with that name.", mdAttribute.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale), childMdClassIF.getDisplayLabel(locale));
  }

  /**
   * Constructor to set the developer message and the MdAttributeIF of the
   * attribute that is already defined by the given class.
   * 
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param mdClassIF
   *          class that a new attribute is being added to.
   */
  public static String duplicateAttributeDefinition(Locale locale, MdAttributeDAOIF mdAttribute, MdClassDAOIF mdClassIF)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateAttributeDefinition", "Cannot add an attribute named [{0}] to class [{1}] because that class already defines an attribute with that name.", mdAttribute.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale));
  }

  /**
   * Thrown when an attribute is added to a type that cannot have attributes to
   * it at runtime.
   * 
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param forbiddenMdClassIF
   *          class that a new attribute is being added to.
   */
  public static String cannotAddAttriubteToClassException(Locale locale, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF forbiddenMdClassIF)
  {
    return LocalizationFacade.getMessage(locale, "CannotAddAttriubteToClassException", "Cannot add attribute [{0}] to class [{1}] or one of its sublasses because attributes cannot be added to that class at runtime.", mdAttribute.getDisplayLabel(locale), forbiddenMdClassIF.getDisplayLabel(locale));
  }

  /**
   * Thrown when the class cannot define attributes of the given type.
   * 
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param class
   *          that defines the type of the mdAttribute.
   * @param mdClassIF
   *          class that a new attribute is being added to.
   */
  public static String attributeOfWrongTypeForClassException(Locale locale, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF mdAttributeDefiningClass, MdClassDAOIF mdClassIF)
  {
    return LocalizationFacade.getMessage(locale, "AttributeOfWrongTypeForClassException", "Cannot add attribute [{0}] to class [{2}].  Class [{2}] cannot define an attribute of type [{1}].", mdAttribute.getDisplayLabel(locale), mdAttributeDefiningClass.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale));
  }

  /**
   * Thrown when a reference attribute type has not been configured to reference
   * a class.
   * 
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param class
   *          that defines the type of the mdAttribute.
   * @param mdClassIF
   *          class that a new attribute is being added to.
   */
  public static String referenceAttributeNotReferencingClassException(Locale locale, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF mdClassIF)
  {
    return LocalizationFacade.getMessage(locale, "ReferenceAttributeNotReferencingClassException", "Attribute [{0}] on class [{1}] is a reference attribute but is not configured to reference anything. It cannot contain a value.", mdAttribute.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale));
  }

  /**
   * Thrown when an attribute of a certain type cannot participate in a
   * uniqueness constraint.
   * 
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param class
   *          that defines the type of the mdAttribute.
   */
  public static String attributeInvalidUniquenessConstraintException(Locale locale, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF mdAttributeDefiningClass)
  {
    return LocalizationFacade.getMessage(locale, "AttributeInvalidUniquenessConstraintException", "Attribute [{0}] cannot participate in a uniqueness constraint.  Attributes of type [{1}] cannot be unique.", mdAttribute.getDisplayLabel(locale), mdAttributeDefiningClass.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link AttributeDefinitionException} message, which
   * indicates an error in an attribute definition
   * 
   * @param locale
   *          The desired locale
   * @param mdAttribute
   *          The metadata describing the abstrat type that the core tried to
   *          instantiate
   * @return The localized error message
   */
  public static String requiredUniquenessConstraintException(Locale locale, MdAttributeConcreteDAOIF mdAttribute)
  {
    return LocalizationFacade.getMessage(locale, "RequiredUniquenessConstraintException", "Attribute [{0}] cannot participate in a uniqueness constraint because it isn't required.", mdAttribute.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link ClassPublishException} message, which indicates
   * an error in an attribute definition
   * 
   * @param locale
   *          The desired locale
   * @param mdClassIF
   *          Metadata containing the invalid definitio
   * @return The localized error message
   */
  public static String classPublishException(Locale locale, MdClassDAOIF mdClassIF)
  {
    return LocalizationFacade.getMessage(locale, "ClassPublishException", "Only the root of a class hierarchy can be published.  Class [{0}] is not the root of a class hierarchy.", mdClassIF.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link MethodDefinitionException} message, which
   * indicates an error in an method definition
   * 
   * @param locale
   *          The desired locale
   * @param mdMethod
   *          Metadata containing the invalid definition.
   * @param definingMdType
   *          Type that defines the method.
   * @return The localized error message
   */

  public static String methodDefinitionException_NameExists(Locale locale, MdMethodDAOIF mdMethod, MdTypeDAOIF definingMdType)
  {
    return LocalizationFacade.getMessage(locale, "MethodDefinitionException_NameExists", "[{0}] already has a method with the name [{1}].", definingMdType.getDisplayLabel(locale), mdMethod.getName());
  }

  /**
   * Gets the localized {@link MethodDefinitionException_DefiningType} message,
   * which indicates an error in an method definition
   * 
   * @param locale
   *          The desired locale
   * @param mdMethod
   *          Metadata containing the invalid definition.e.
   * @param definingMdTypeIF
   *          Metadata of the type that the method is defined on
   * @param mdClassMdBusiness
   *          Metadata that defines MdClass
   * @return The localized error message
   */

  public static String methodDefinitionException_DefiningType(Locale locale, MdMethodDAOIF mdMethod, MdTypeDAOIF definingMdTypeIF, MdBusinessDAOIF mdClassMdBusiness)
  {
    return LocalizationFacade.getMessage(locale, "MethodDefinitionException_DefiningType", "Method [{0}] is defined on an invalid type.  [{1}] is not a [{2}] or a [{3}].", mdMethod.getDisplayLabel(locale), definingMdTypeIF.getDisplayLabel(locale), mdClassMdBusiness.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link methodDefinitionException_InvalidReturnType}
   * message, which indicates an error in an method definition
   * 
   * @param locale
   *          The desired locale
   * @param mdMethod
   *          Metadata containing the invalid definition.
   * @param returnType
   *          The invalid specified return type.
   * @return The localized error message
   */

  public static String methodDefinitionException_InvalidReturnType(Locale locale, MdMethodDAOIF mdMethod, String returnType)
  {
    return LocalizationFacade.getMessage(locale, "MethodDefinitionException_InvalidReturnType", "The return type [{0}] on method [{1}] is invalid.  Return types must be a Java primitive wrapper class or a predefined class that is published.", returnType, mdMethod.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link ParameterDefinitionException_NameExists} message,
   * which indicates an error in an method definition
   * 
   * @param locale
   *          The desired locale
   * @param mdParameter
   *          Metadata containing the invalid parameter definition.
   * @param mdMethod
   *          Metadata containing the invalid method definition
   * @return The localized error message
   */

  public static String parameterDefinitionException_NameExists(Locale locale, MdParameterDAOIF mdParameter, MdMethodDAOIF mdMethod)
  {
    return LocalizationFacade.getMessage(locale, "ParameterDefinitionException_NameExists", "[{0}] already has a parameter with the name [{1}].", mdMethod.getDisplayLabel(locale), mdParameter.getParameterName());
  }

  /**
   * Gets the localized {@link ParameterDefinitionException_OrderExists}
   * message, which indicates an error in an method definition
   * 
   * @param locale
   *          The desired locale
   * @param mdParameter
   *          Metadata containing the invalid parameter definition.
   * @param metadata
   *          Metadata containing the invalid method definition
   * @return The localized error message
   */

  public static String parameterDefinitionException_OrderExists(Locale locale, MdParameterDAOIF mdParameter, MetadataDAOIF metadata, MdParameterDAOIF existingMdParameter)
  {
    return LocalizationFacade.getMessage(locale, "ParameterDefinitionException_OrderExists", "[{0}] already has parameter [{1}] with order [{2}].", metadata.getDisplayLabel(locale), existingMdParameter.getParameterName(), mdParameter.getParameterOrder());
  }

  /**
   * Gets the localized {@link ParameterDefinitionException_InvalidType}
   * message, which indicates an error in an method definition
   * 
   * @param locale
   *          The desired locale
   * @param mdParameter
   *          Metadata containing the invalid parameter definition.
   * @param mdMethod
   *          Metadata containing the invalid method definition
   * @return The localized error message
   */

  public static String parameterDefinitionException_InvalidType(Locale locale, MdParameterDAOIF mdParameter, ParameterMarker marker)
  {
    return LocalizationFacade.getMessage(locale, "ParameterDefinitionException_InvalidType", "Type [{0}] of parameter [{1}] on method [{2}] is invalid.  Parameter types must be a Java primitive wrapper class or a predefined class that is published.", mdParameter.getDisplayLabel(locale), mdParameter.getParameterName(), marker.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link AttributeLengthProblem} message, which indicates
   * the maximum length was exceeded for a character or text attribute.
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute that is too long
   * @param maxLength
   *          The maximum length of a character attribute.
   * @return The localized error message
   */
  public static String attributeLengthCharacterException(Locale locale, AttributeIF attribute, Integer maxLength)
  {
    String unitString = "";

    if (maxLength > 1)
    {
      unitString = LocalizationFacade.getMessage(locale, "AttributeLengthException.Unit.Characters", "characters");
    }
    else
    {
      unitString = LocalizationFacade.getMessage(locale, "AttributeLengthException.Unit.Character", "character");
    }

    return LocalizationFacade.getMessage(locale, "AttributeLengthException", "[{0}] many not exceed a length of {1} {2}.", attribute.getDisplayLabel(locale), maxLength.toString(), unitString);
  }

  /**
   * Gets the localized {@link AttributeLengthProblem} message, which indicates
   * the maximum length was exceeded for a character or text attribute.
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute that is too long
   * @param maxLength
   *          The maximum length of a character attribute.
   * @return The localized error message
   */
  public static String attributeLengthByteException(Locale locale, AttributeIF attribute, Integer maxLength)
  {
    String unitString = "";

    if (maxLength > 1)
    {
      unitString = LocalizationFacade.getMessage(locale, "AttributeLengthException.Unit.Bytes", "bytes");
    }
    else
    {
      unitString = LocalizationFacade.getMessage(locale, "AttributeLengthException.Unit.Byte", "byte");
    }

    return LocalizationFacade.getMessage(locale, "AttributeLengthException", "[{0}] many not exceed a length of {1} {2}.", attribute.getDisplayLabel(locale), maxLength.toString(), unitString);
  }

  /**
   * Gets the localized {@link AttributeTypeException} message, which is thrown
   * when a type-specific attribute accessor is called for the wrong attribute
   * type
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute that is too long
   * @return The localized error message
   */
  public static String attributeTypeException(Locale locale, AttributeIF attribute)
  {
    return LocalizationFacade.getMessage(locale, "AttributeTypeException", "The attribute [{0}] on class [{1}] is of the wrong type.  The expected type is [{2}] but the given type is [{3}].", attribute.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link AttributeValueException} message, which is thrown
   * when the value set for an attribute is invalid
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute being set
   * @param value
   *          The value that isn't valid
   * @return The localized error message
   */
  public static String attributeValueException(Locale locale, AttributeIF attribute, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeValueException", "[{0}] is not a valid value for [{1}].", value, attribute.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link AttributeValueException} message, which is thrown
   * when the value set for an attribute is invalid
   * 
   * @param locale
   *          The desired locale
   * @param value
   *          The value that isn't valid
   * @return The localized error message
   */
  public static String attributeValueExceptionGeneric(Locale locale, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeValueExceptionGeneric", "[{0}] is not a valid value.", value);
  }

  /**
   * Gets the localized {@link AttributeValueException} message, which is thrown
   * when the value set for an attribute is invalid
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute being set
   * @param value
   *          The value that isn't valid
   * @param limit
   *          The upper bounds
   * @return The localized error message
   */
  public static String attributeValueAboveRangeException(Locale locale, AttributeIF attribute, String value, String limit)
  {
    return LocalizationFacade.getMessage(locale, "AttributeValueAboveRangeProblem", "[{0}] is not a valid value for [{1}]. Value cannot be greater than [{2}].", value, attribute.getDisplayLabel(locale), limit);
  }

  /**
   * Gets the localized {@link AttributeValueException} message, which is thrown
   * when the value set for a numeric attribute is below the specified range
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute being set
   * @param value
   *          The value that isn't valid
   * @param limit
   *          The lower bounds
   * @return The localized error message
   */
  public static String attributeValueBelowRangeException(Locale locale, AttributeIF attribute, String value, String limit)
  {
    return LocalizationFacade.getMessage(locale, "AttributeValueBelowRangeProblem", "[{0}] is not a valid value for [{1}]. Value cannot be less than [{2}].", value, attribute.getDisplayLabel(locale), limit);
  }

  /**
   * Gets the localized {@link AttributeValueException} message, which is thrown
   * when the value set for a numeric attribute is negative when it isn't
   * allowed
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute being set
   * @param value
   *          The value that isn't valid
   * @return The localized error message
   */
  public static String attributeValueCannotBeNegativeException(Locale locale, AttributeIF attribute, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeValueCannotBeNegativeProblem", "[{0}] is not a valid value for [{1}]. Value cannot be negative.", value, attribute.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link AttributeValueException} message, which is thrown
   * when the value set for a numeric attribute is positive when it isn't
   * allowed
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute being set
   * @param value
   *          The value that isn't valid
   * @return The localized error message
   */
  public static String attributeValueCannotBePositiveException(Locale locale, AttributeIF attribute, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeValueCannotBePositiveProblem", "[{0}] is not a valid value for [{1}]. Value cannot be positive.", value, attribute.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link AttributeValueException} message, which is thrown
   * when the value set for a numeric attribute is zero when it isn't allowed
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The attribute being set
   * @param value
   *          The value that isn't valid
   * @return The localized error message
   */
  public static String attributeValueCannotBeZeroException(Locale locale, AttributeIF attribute, String value)
  {
    return LocalizationFacade.getMessage(locale, "AttributeValueCannotBeZeroProblem", "[{0}] is not a valid value for [{1}]. Value cannot be zero.", value, attribute.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link CacheCodeException} message, which is thrown when
   * a new entity's cache algorithm is incompatible with its heirarchy.
   * 
   * @param locale
   *          The desired locale
   * @param child
   *          Metadata describing the child entity
   * @param parent
   *          Metadata describing the parent entity
   * @return The localized error message
   */
  public static String cacheCodeException(Locale locale, MdElementDAOIF child, MdElementDAOIF parent)
  {
    String childCacheAlgorithmDisplayLabel = ( (AttributeLocalIF) child.getCacheAlgorithm().getAttributeIF(EntityCacheMaster.DISPLAY_LABEL) ).getValue(locale);

    // If we have a parent
    if (parent != null)
    {
      String parentCacheAlgorithmDisplayLabel = ( (AttributeLocalIF) parent.getCacheAlgorithm().getAttributeIF(EntityCacheMaster.DISPLAY_LABEL) ).getValue(locale);
      return LocalizationFacade.getMessage(locale, "CacheCodeException", "Type [{0}] cannot cache [{1}], because it inherits from [{2}], which caches [{3}].  Please select a different caching algorithm.", child.getDisplayLabel(locale), childCacheAlgorithmDisplayLabel, parent.getDisplayLabel(locale), parentCacheAlgorithmDisplayLabel);
    }

    // We don't have a parent type, so get the shortened message
    return LocalizationFacade.getMessage(locale, "CacheCodeExceptionShort", "Type [{0}] cannot cache [{1}].  Please select a different caching algorithm.", child.getDisplayLabel(locale), childCacheAlgorithmDisplayLabel);
  }

  /**
   * Gets the localized {@link MetadataCannotBeDeletedException} message, which
   * is Thrown when delete is called for metadata that cannot be deleted.
   * 
   * @param locale
   *          The desired locale
   * @param metadata
   *          The metadata that cannot be deleted
   * @return The localized error message
   */
  public static String metadataCannotBeDeletedException(Locale locale, MetadataDAO metadata)
  {
    return LocalizationFacade.getMessage(locale, "MetadataCannotBeDeletedException", "Metadata that defines [{0}] cannot be deleted.", metadata.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link DeleteUnappliedObjectException} message, which is
   * Thrown when delete is called for metadata that cannot be deleted.
   * 
   * @param locale
   *          The desired locale
   * @param entity
   *          The entity that cannot be deleted
   * @return The localized error message
   */
  public static String deleteUnappliedObjectException(Locale locale, Entity entity)
  {
    return LocalizationFacade.getMessage(locale, "DeleteUnappliedObjectException", "[{0}] cannot be deleted because it has not yet been applied.", entity.toString());
  }

  /**
   * @param locale
   * @return
   */
  public static String disconnectedEntityException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "DisconnectedEntityException", "A disconnected entity cannot be applied.");
  }

  /**
   * Gets the localized {@link CannotDeleteReferencedObject} message, which is
   * Thrown when delete is called for metadata that cannot be deleted.
   * 
   * @param locale
   *          The desired locale
   * @param entity
   *          The entity that cannot be deleted
   * @return The localized error message
   */
  public static String cannotDeleteReferencedObject(Locale locale, Entity entity, MdEntityDAOIF refMdEntityIF, MdAttributeConcreteDAOIF refMdAttributeIF)
  {
    return LocalizationFacade.getMessage(locale, "CannotDeleteReferencedObject", "Cannot delete [{0}].  Attribute [{1}] on type [{2}] references it and is a required attribute.", entity.toString(), refMdAttributeIF.getDisplayLabel(locale), refMdEntityIF.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link RoleManagementException_ADD} message, which is
   * thrown when the user is adding someone to a role but does not have adequate
   * permissions.
   * 
   * @param locale
   *          The desired locale
   * @param metadata
   *          The metadata describing the abstrat type that the core tried to
   *          instantiate
   * @return The localized error message
   */
  public static String roleManagementException_ADD(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "RoleManagementException_ADD", "You do not have adequate permissions to add a user to a role.");
  }

  /**
   * Gets the localized {@link RoleManagementException_REMOVE} message, which is
   * thrown when the user is removing someone from a role but does not have
   * adequate permissions.
   * 
   * @param locale
   *          The desired locale
   * @param metadata
   *          The metadata describing the abstrat type that the core tried to
   *          instantiate
   * @return The localized error message
   */
  public static String roleManagementException_REMOVE(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "RoleManagementException_REMOVE", "You do not have adequate permissions to remove a user from a role.");
  }

  /**
   * Gets the localized {@link ClassLoaderException} message, which is thrown
   * when an error is encountered when dynamically loading a genereated class.
   * 
   * @param locale
   *          The desired locale
   * @param metadata
   *          The metadata describing the abstrat type that the core tried to
   *          instantiate
   * @return The localized error message
   */
  public static String classLoaderException(Locale locale, MetadataDAOIF metadata)
  {
    return LocalizationFacade.getMessage(locale, "ClassLoaderException", "Error encountered when loading [{0}].  Please try again.  If the problem continues, alert your technical support staff.", metadata.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link CompilerException} message, which is thrown when
   * an error is encountered while compiling business classes.
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String compilerException(Locale locale, String errors)
  {
    return LocalizationFacade.getMessage(locale, "CompilerException", "Your request created compile errors in business classes.\\n{0}", errors);
  }

  /**
   * Gets the localized {@link DataNotFoundException} message, which is thrown
   * when an object is requested that does not exist (the oid can not be found).
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String dataNotFoundException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "DataNotFoundException", "The requested [{0}] was not found.  Please verify that all information was correctly entered in your request and try again.");
  }

  /**
   * Gets the localized {@link DataNotFoundException} message, which is thrown
   * when an object is requested that does not exist (the oid can not be found).
   * 
   * @param locale
   *          The desired locale
   * @param metadata
   *          Metadata describing the requested object
   * @return The localized error message
   */
  public static String dataNotFoundException(Locale locale, MetadataDAOIF metadata)
  {
    return LocalizationFacade.getMessage(locale, "DataNotFoundException", "The requested [{0}] was not found.  Please verify that all information was correctly entered in your request and try again.", metadata.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link MissingKeyNameValue} message, which is thrown
   * when an object of a type with deterministic IDs does not supply a KeyName
   * value.
   * 
   * @param locale
   *          The desired locale
   * @param mdEntityDAOIF
   *          The metadata describing the type of object missing a
   *          <code>EntityInfo.KEY</code> value
   * @return The localized error message
   */
  public static String missingKeyNameValue(Locale locale, MdEntityDAOIF mdEntityDAOIF)
  {
    String typeDisplayLabel = mdEntityDAOIF.getDisplayLabel(locale);
    String attributeDisplayLabel = mdEntityDAOIF.getAttributeIF(EntityInfo.KEY).getDisplayLabel(locale);
    return LocalizationFacade.getMessage(locale, "MissingKeyNameValue", "Objects of type [{0}] require a value for the [{1}] attribute in order to generate a deterministic ID.", typeDisplayLabel, attributeDisplayLabel);
  }

  /**
   * Gets the localized {@link DuplicateDataException} message, which is thrown
   * when a new instance would conflict with existing data when there is a
   * collision with a unique attribute.
   * 
   * @param locale
   *          The desired locale.
   * @param attributePrimitive
   *          The attribute being set.
   * @param value
   *          The duplicate value
   * @return The localized error message.
   */
  public static String duplicateDataException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateDataException", "An object already exists that has values on attributes that are unique.");
  }

  /**
   * Gets the localized {@link DuplicateDataException} message, which is thrown
   * when a new instance would conflict with existing data when there is a
   * collision with a unique attribute.
   * 
   * @param locale
   *          The desired locale.
   * @param attributePrimitive
   *          The attribute being set.
   * @param value
   *          The duplicate value
   * @return The localized error message.
   */
  public static String duplicateDataExceptionSingle(Locale locale, String definingTypeLabel, String attributeDisplayLabel, String value)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateDataExceptionSingle", "An instance of [{0}] already exists with [{1}] as the value for {2} - please choose a different value.", definingTypeLabel, value, attributeDisplayLabel);
  }

  /**
   * Gets the localized {@link DuplicateDataException} message, which is thrown
   * when a new instance would conflict with existing data when there is a
   * collision with a unique attribute.
   * 
   * @param locale
   *          The desired locale.
   * @param attributePrimitive
   *          The attribute being set.
   * @return The localized error message.
   */
  public static String duplicateDataExceptionSingleNoValue(Locale locale, String definingTypeLabel, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateDataExceptionSingleNoValue", "An instance of [{0}] already exists with the same value for {1} - please choose a different value.", definingTypeLabel, attributeDisplayLabel);
  }

  /**
   * Gets the localized {@link DuplicateDataException} message, which is thrown
   * when a new instance would conflict with existing data when there is a
   * collision with a unique group of attributes.
   * 
   * @param locale
   *          The desired locale.
   * @param attributePrimitive
   *          The attribute being set.
   * @param value
   *          The duplicate value
   * @return The localized error message.
   */
  public static String duplicateDataExceptionMultiple(Locale locale, String definingTypeLabel, String attributeDisplayLabels, String values)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateDataExceptionMultiple", "A instance of [{0}] already exists with values {1} for attributes {2} - please choose different values.", definingTypeLabel, values, attributeDisplayLabels);
  }

  /**
   * Gets the localized {@link DuplicateDataException} message, which is thrown
   * when a new instance would conflict with existing data when there is a
   * collision with a unique group of attributes.
   * 
   * @param locale
   *          The desired locale.
   * @param attributePrimitive
   *          The attribute being set.
   * @return The localized error message.
   */
  public static String duplicateDataExceptionMultipleNoValues(Locale locale, String definingTypeLabel, String attributeDisplayLabels)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateDataExceptionMultipleNoValues", "A instance of [{0}] already exists with the same values for attributes {1} - please choose different values.", definingTypeLabel, attributeDisplayLabels);
  }

  /**
   * Gets the localized {@link EmptyValueProblem} message, which is thrown when
   * a required attribute has no value
   * 
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   *          The empty attribute
   * @return The localized error message
   */
  public static String emptyValueException(Locale locale, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "EmptyValueProblem", "[{0}] requires a value.", attributeDisplayLabel);
  }

  /**
   * Gets the localized {@link EmptyValueProblem} message, which is thrown when
   * a required attribute has no value
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          The invalid encrypted attribute
   * @return The localized error message
   */
  public static String encryptionException(Locale locale, MdAttributeEncryptionDAOIF attribute)
  {
    return LocalizationFacade.getMessage(locale, "EncryptionException", "Operation cancelled - there was a problem encrypting [{0}].  Please try again.", attribute.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link RunwayException} message.
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String domainPermissionException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "DomainPermissionException", "You do not have permission to access this domain.");
  }

  /**
   * Gets the localized {@link RunwayException} message. Thrown when one site
   * tries to modify an object created by another site.
   * 
   * @param locale
   *          The desired locale
   * @param entityToString
   *          business to string value of the entity object
   * @param entitySite
   *          The owner site of the object.
   * @param currentSite
   *          The current site modifying the object.
   * @return The localized error message
   */
  public static String siteException(Locale locale, String entityToString, String entitySite, String currentSite)
  {
    return LocalizationFacade.getMessage(locale, "SiteException", "An object [{0}] owned by site [{1}] cannot be modified by site [{2}].", entityToString, entitySite, currentSite);
  }

  public static String sourceElementNotDeclaredException(Locale locale, String value)
  {
    return LocalizationFacade.getMessage(locale, "SourceElementNotDeclaredException", "Cannot update a merged element which does not exist.", value);
  }

  public static String xsdDefinitionNotResolvedException(Locale locale, String value)
  {
    return LocalizationFacade.getMessage(locale, "XSDDefinitionNotResolvedException", "XSD definition with name [{0}] could not be resolved.", value);
  }

  /**
   * Gets the localized {@link ImmutableAttributeProblem} message.
   * 
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   *          The unmodifiable attribute
   * @return The localized error message
   */
  public static String immutableAttributeProblem(Locale locale, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "ImmutableAttributeProblem", "[{0}] cannot be modified.", attributeDisplayLabel);
  }

  /**
   * Gets the localized {@link SystemAttributeProblem} message.
   * 
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   *          The unmodifiable attribute
   * @return The localized error message
   */
  public static String systemAttributeProblem(Locale locale, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "SystemAttributeProblem", "[{0}] cannot be modified because it is a system attribute.", attributeDisplayLabel);
  }

  /**
   * Gets the localized {@link InheritanceException} message, which indicates an
   * error in the inheritance heirarchy
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String inheritanceException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "InheritanceException", "The requested operation would corrupt the inheritance hierarchy.");
  }

  /**
   * Gets the localized {@link InvalidDefinitionException} message, which
   * encapsulates all problems that arise from data definition (metadata).
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String invalidDefinitionException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "InvalidDefinitionException", "Your operation created or modified metadata with invalid values. Please check your input and try again. If the problem continues, please contact your support staff.");
  }

  /**
   * Gets the localized {@link IdenticalIndexException} message, which is thrown
   * when an identical group by index is defined on a entity.
   * 
   * @param locale
   *          The desired locale
   * @param mdEntityDefinesType
   * @param mdIndexDisplayLabel
   * @param attributes
   *          comma separated list of attributes
   * @return The localized error message
   */
  public static String identicalIndexException(Locale locale, String mdEntityDefinesType, String mdIndexDisplayLabel, String attributes)
  {
    return LocalizationFacade.getMessage(locale, "IdenticalIndexException", "Index [{1}] on entity [{0}] is invalid. An index is already defined on entity [{0}] with attributes [{2}].");
  }

  /**
   * Gets the localized {@link RunwayException} message.
   * 
   * @param locale
   *          The desired locale
   * @param attributeName
   *          name of the attribute that does not exist on a component
   * @param definingMdClassIF
   *          name of the defining type, if there is one (may be null)
   * @return The localized error message
   */
  public static String attributeDoesNotExistExceptionNoType(Locale locale, String attributeName)
  {
    return LocalizationFacade.getMessage(locale, "AttributeDoesNotExistExceptionNoType", "The attribute [{0}] does not exist.", attributeName);
  }

  /**
   * Gets the localized {@link RunwayException} message.
   * 
   * @param locale
   *          The desired locale
   * @param attributeName
   *          name of the attribute that does not exist on a component
   * @param definingMdClassIF
   *          name of the defining type, if there is one (may be null)
   * @return The localized error message
   */
  public static String attributeDoesNotExistExceptionWithType(Locale locale, String attributeName, String classDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "AttributeDoesNotExistExceptionWithType", "The attribute [{0}] on [{1}] does not exist.", attributeName, classDisplayLabel);
  }

  /**
   * Gets the localized {@link DuplicateMdEnumerationDefinitionException}
   * message, which encapsulates all problems that arise from data definition
   * (metadata).
   * 
   * @param locale
   *          The desired locale
   * @param mdEnumerationLabel
   *          MdEnumeration label
   * @param masterClassType
   *          Type of the master class
   * @param duplicateTYpeName
   *          Type name on the newly defined MdEnumeration
   * 
   * @return The localized error message
   */
  public static String duplicateMdEnumerationDefinitionException(Locale locale, String mdEnumerationLabel, String masterClassType, String duplicateTypeName)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateMdEnumerationDefinitionException", "A [{0}] already exists for [{1}] with type name [{2}].", mdEnumerationLabel, masterClassType, duplicateTypeName);
  }

  /**
   * Thrown when an enumeration name is not valid with respect to the given
   * enumeration.
   * 
   * @param locale
   *          The desired locale
   * @param enumName
   *          name of the enumeration that was invalid.
   * @param mdEnumerationIF
   *          enumeration.
   */
  public static String invalidEnumerationName(Locale locale, String enumName, MdEnumerationDAOIF mdEnumerationIF)
  {
    return LocalizationFacade.getMessage(locale, "InvalidEnumerationName", "The enummeration name [{0}] is not valid for enumeration [{1}].", enumName, mdEnumerationIF.getDisplayLabel(locale));
  }

  /**
   * Thrown when an attribute is specified to be on an index, yet the attribute
   * is not defined by the type that defines the table the index is on. Gets the
   * localized {@link InvalidDefinitionException} message, which encapsulates
   * all problems that arise from data definition (metadata).
   * 
   * @param locale
   *          The desired locale
   * @param definesType
   *          Type
   * @param attributeDisplayLabel
   *          Attribute display label
   * @return The localized error message
   */
  public static String invalidAttributeForIndexDefinitionException(Locale locale, String definesType, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "InvalidAttributeForIndexDefinitionException", "Attribute [{1}] is not defined by by [{0}].", definesType, attributeDisplayLabel);
  }

  /**
   * Thrown when an attribute is specified to be on an index, yet the attribute
   * is not defined by the type that defines the table the index is on. Gets the
   * localized {@link NoAttributeOnIndexException} message, which encapsulates
   * all problems that arise from data definition (metadata).
   * 
   * @param locale
   *          The desired locale
   * @param definesType
   *          Type
   * @return The localized error message
   */
  public static String noAttributeOnIndexException(Locale locale, String definesType)
  {
    return LocalizationFacade.getMessage(locale, "NoAttributeOnIndexException", "Index on [{0}] could not be applied to the database because it does not contain any attributes.", definesType);
  }

  /**
   * Gets the localized {@link InvalidReferenceException} message, which is
   * thrown when an attribute references an invalid instance.
   * 
   * @param locale
   *          The desired locale
   * @param attribute
   *          Metadata describing the reference attribute
   * @param reference
   *          Metadata describing the referenced type or attribute
   * @return The localized error message
   */
  public static String invalidReferenceException(Locale locale, MdAttributeRefDAOIF attribute, MetadataDAOIF reference)
  {
    String referenceDisplayLabel;
    if (reference == null)
    {
      referenceDisplayLabel = "Undefined";
    }
    else
    {
      referenceDisplayLabel = reference.getDisplayLabel(Session.getCurrentLocale());
    }
    return LocalizationFacade.getMessage(locale, "InvalidReferenceException", "The value for [{0}] is invalid - it must reference a [{1}].", attribute.getDisplayLabel(locale), referenceDisplayLabel);
  }

  /**
   * Gets the localized {@link InvalidAttributeTypeException} message, which is
   * thrown when an attribute is called as a reference when it is not a
   * reference attribute
   * 
   * @param locale
   *          The desired locale
   * @param attributeDisplayLabel
   *          Display label of the attribute
   * @param definingTypeLabel
   *          Display label of the type that defines the reference attribute.
   * @param expectedAttributeTypeDisplayLabel
   *          Metadata that defines the expected attribute type.
   * @param givenAttributeDisplayLabel
   *          Metadata that defines the given attribute type.
   * @return The localized error message
   */
  public static String invalidAttributeTypeException(Locale locale, String attributeDisplayLabel, String definingTypeLabel, String expectedAttributeTypeDisplayLabel, String givenAttributeTypeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "InvalidAttributeTypeException", "The attribute [{0}] on class [{1}] is of the wrong type.  The expected type is [{2}] but the given type is [{3}].", attributeDisplayLabel, definingTypeLabel);
  }

  /**
   * Gets the localized {@link LockException} message, which is thrown when an
   * object that fails to lock/unlock
   * 
   * @param locale
   *          The desired locale
   * @param entity
   *          The entity that failed to lock/unlock
   * @param errorProperty
   *          Name of the property that references the desired error message.
   * @return The localized error message
   */
  public static String lockException(Locale locale, Entity entity, String errorProperty)
  {
    return LocalizationFacade.getMessage(locale, errorProperty, errorProperty + " [{0}]", entity.toString());
  }

  /**
   * Gets the localized {@link NameConventionException} message, which is thrown
   * when an attribute or type name violates enforced naming convetions.
   * 
   * @param locale
   *          The desired locale
   * @param name
   *          The invalid name
   * @return The localized error message
   */
  public static String nameConventionException(Locale locale, String name)
  {
    return LocalizationFacade.getMessage(locale, "NameConventionException", "The name [{0}] violates a naming convention - java style conventions are enforced.  Please consult your documentation for details.", name);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.CREATE} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param componentIF
   *          The entity that was being created
   * @return The localized error message
   */
  public static String createPermissionException(Locale locale, ComponentIF componentIF)
  {
    return LocalizationFacade.getMessage(locale, "CreatePermissionException", "You do not have permission to create [{0}].", componentIF.toString());
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.DELETE} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param componentIF
   *          The entity that was being deleted
   * @return The localized error message
   */
  public static String deletePermissionException(Locale locale, ComponentIF componentIF)
  {
    return LocalizationFacade.getMessage(locale, "DeletePermissionException", "You do not have permission to delete [{0}].", componentIF.toString());
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.READ} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param componentIF
   *          The entity that was being read
   * @return The localized error message
   */
  public static String readPermissionException(Locale locale, ComponentIF componentIF)
  {
    return LocalizationFacade.getMessage(locale, "ReadPermissionException", "You do not have permission to read [{0}].", componentIF.toString());
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.READ} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param typeDisplayLabel
   *          Display label of the type the user does not have permission to
   *          read
   * @return The localized error message
   */
  public static String readTypePermissionException(Locale locale, String typeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "ReadTypePermissionException", "You do not have permission to read [{0}].", typeDisplayLabel);
  }

  /**
   * Gets the localized {@link DeleteTypePermissionException} message, which is
   * thrown when a user does not have {@link Operation.DELETE} permission for a
   * type
   * 
   * @param locale
   *          The desired locale
   * @param typeDisplayLabel
   *          Display label of the type the user does not have permission to
   *          read
   * @return The localized error message
   */
  public static String deleteTypePermissionException(Locale locale, String typeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "DeleteTypePermissionException", "You do not have permission to delete instances of type [{0}].", typeDisplayLabel);
  }

  /**
   * Gets the localized {@link GroovyQueryExecuteException} message, which is
   * thrown when a user is not in the admin role and is attempting to execute an
   * arbitrary groovy query.
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String groovyQueryExecuteException(Locale locale, String requiredRoleName)
  {
    return LocalizationFacade.getMessage(locale, "GroovyQueryExecuteException", "You must be in the [{0}] role to execute arbitrary queries.", requiredRoleName);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.WRITE} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param componentIF
   *          The entity that was being modified
   * @return The localized error message
   */
  public static String writePermissionException(Locale locale, ComponentIF componentIF)
  {
    return LocalizationFacade.getMessage(locale, "WritePermissionException", "You do not have permission to update [{0}].", componentIF.toString());
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.GRANT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param mdTypeIF
   *          Type that the user does not have permission to grant permissions
   *          on.
   * @return The localized error message
   */
  public static String grantTypePermissionException(Locale locale, MdTypeDAOIF mdTypeIF)
  {
    return LocalizationFacade.getMessage(locale, "GrantTypePermissionException", "You do not have permission to grant permissions on type [{0}].", mdTypeIF.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.GRANT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param mdTypeIF
   *          Type that defines the method.
   * @param mdMethodIF
   *          Method that the user does not have permission to grant permissions
   *          on.
   * @return The localized error message
   */
  public static String grantMethodPermissionException(Locale locale, MdTypeDAOIF mdTypeIF, MdMethodDAOIF mdMethodIF)
  {
    return LocalizationFacade.getMessage(locale, "GrantMethodPermissionException", "You do not have permission to grant permissions on method [{1}] defined by [{0}].", mdTypeIF.getDisplayLabel(locale), mdMethodIF.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.GRANT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param mdClassIF
   *          Class that the user does not have permission to grant permissions
   *          on.
   * @param mdAttributeIF
   *          Defines the attribute.
   * @return The localized error message
   */
  public static String grantAttributePermissionException(Locale locale, MdClassDAOIF mdClassIF, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    return LocalizationFacade.getMessage(locale, "GrantAttributePermissionException", "You do not have permission to grant permissions on attribute [{1}] on type [{0}].", mdClassIF.getDisplayLabel(locale), mdAttributeIF.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.GRANT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param mdTypeIF
   *          Type that the user does not have permission to revoke permissions
   *          on.
   * @return The localized error message
   */
  public static String revokeTypePermissionException(Locale locale, MdTypeDAOIF mdTypeIF)
  {
    return LocalizationFacade.getMessage(locale, "RevokeTypePermissionException", "You do not have permission to revoke permissions from type [{0}].", mdTypeIF.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.GRANT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param mdTypeIF
   *          Type that defines the method.
   * @param mdMethodIF
   *          Method that the user does not have permission to revoke
   *          permissions from.
   * @return The localized error message
   */
  public static String revokeMethodPermissionException(Locale locale, MdTypeDAOIF mdTypeIF, MdMethodDAOIF mdMethodIF)
  {
    return LocalizationFacade.getMessage(locale, "RevokeMethodPermissionException", "You do not have permission to revoke permissions on method [{1}] defined by [{0}].", mdTypeIF.getDisplayLabel(locale), mdMethodIF.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.GRANT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param mdClassIF
   *          Class that the user does not have permission to revoke permissions
   *          on.
   * @param mdAttributeIF
   *          Defines the attribute.
   * @return The localized error message
   */
  public static String revokeAttributePermissionException(Locale locale, MdClassDAOIF mdClassIF, MdAttributeConcreteDAOIF mdAttributeIF)
  {
    return LocalizationFacade.getMessage(locale, "RevokeAttributePermissionException", "You do not have permission to revoke permissions on attribute [{1}] on type [{0}].", mdClassIF.getDisplayLabel(locale), mdAttributeIF.getDisplayLabel(locale));
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.EXECUTE} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param mdMethodDislpayLabel
   *          label of the method being executed
   * @return The localized error message
   */
  public static String executeStaticPermissionException(Locale locale, String mdTypeDisplayLabel, String mdMethodDislpayLabel)
  {
    return LocalizationFacade.getMessage(locale, "ExecuteStaticPermissionException", "You do not have permission to [{1}] on [{0}].", mdTypeDisplayLabel, mdMethodDislpayLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.EXECUTE} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param mutable
   * @param mdMethodDislpayLabel
   *          label of the method being executed
   * @return The localized error message
   */
  public static String executeInstancePermissionException(Locale locale, Mutable mutable, String mdMethodDislpayLabel)
  {
    return LocalizationFacade.getMessage(locale, "ExecuteInstancePermissionException", "You do not have permission to [{1}] on [{0}].", mutable.toString(), mdMethodDislpayLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.PROMOTE} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param busines
   *          The business object being promoted
   * @return The localized error message
   */
  public static String promotePermissionException(Locale locale, Business busines, String stateLabel)
  {
    return LocalizationFacade.getMessage(locale, "PromotePermissionException", "You do not have permission to promote [{0}] to [{1}].", busines.toString(), stateLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.ADD_CHILD} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param childBusiness
   *          business child object
   * @param parentBusiness
   *          business parent object
   * @param childRelDisplayLabel
   *          display lable of the child end of the relationship
   * @return The localized error message
   */
  public static String addChildPermissionException(Locale locale, Business childBusiness, Business parentBusiness, String childRelDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "AddChildPermissionException", "You do not have permission to add [{0}] to [{1}] as [{2}].", childBusiness.toString(), parentBusiness.toString(), childRelDisplayLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.ADD_PARENT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param parentBusiness
   *          business parent object
   * @param childBusiness
   *          business child object
   * @param childRelDisplayLabel
   *          display lable of the child end of the relationship
   * @return The localized error message
   */
  public static String addParentPermissionException(Locale locale, Business parentBusiness, Business childBusiness, String childRelDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "AddParentPermissionException", "You do not have permission to add [{0}] to [{1}] as [{2}].", parentBusiness.toString(), childBusiness.toString(), childRelDisplayLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.DELETE_CHILD} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param childBusiness
   *          business child object
   * @param parentBusiness
   *          business parent object
   * @param childRelDisplayLabel
   *          display lable of the child end of the relationship
   * @return The localized error message
   */
  public static String deleteChildPermissionException(Locale locale, Business childBusiness, Business parentBusiness, String childRelDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "DeleteChildPermissionException", "You do not have permission to remove [{0}] from [{1}] as [{2}].", childBusiness.toString(), parentBusiness.toString(), childRelDisplayLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.DELETE_PARENT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param parentBusiness
   *          business parent object
   * @param childBusiness
   *          business child object
   * @param parentRelDisplayLabel
   *          display lable of the child end of the relationship
   * @return The localized error message
   */
  public static String deleteParentPermissionException(Locale locale, Business parentBusiness, Business childBusiness, String parentRelDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "DeleteParentPermissionException", "You do not have permission to remove [{0}] from [{1}] as [{2}].", parentBusiness.toString(), childBusiness.toString(), parentRelDisplayLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.READ_CHILD} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param parentBusiness
   *          business parent object
   * @param childRelDisplayLabel
   *          display lable of the child end of the relationship
   * @return The localized error message
   */
  public static String readChildPermissionException(Locale locale, Business parentBusiness, String childRelDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "ReadChildPermissionException", "You do not have permission to read [{0}] on [{1}].", parentBusiness.toString(), childRelDisplayLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have {@link Operation.READ_PARENT} permission for an
   * operation on a type
   * 
   * @param locale
   *          The desired locale
   * @param childBusiness
   *          business child object
   * @param parentRelDisplayLabel
   *          display lable of the child end of the relationship
   * @return The localized error message
   */
  public static String readParentPermissionException(Locale locale, Business childBusiness, String parentRelDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "ReadParentPermissionException", "You do not have permission to read [{0}] on [{1}].", childBusiness.toString(), parentRelDisplayLabel);
  }

  /**
   * Gets the localized {@link PermissionException} message, which is thrown
   * when a user does not have permission for an operation on an attribute
   * 
   * @param locale
   *          The desired locale
   * @param componentIF
   *          The entity that was being read/written/etc
   * @param operation
   *          The failed operation (read/write/promote/etc)
   * @param user
   *          The user attempting the operation
   * @return The localized error message
   */
  public static String attributeWritePermissionException(Locale locale, ComponentIF componentIF, MdAttributeDAOIF mdAttribute, SingleActorDAOIF user)
  {
    return LocalizationFacade.getMessage(locale, "AttributeWritePermissionException", "User [{2}] does not have permission to modify attribute [{1}] on [{0}].", componentIF.toString(), mdAttribute.definesAttribute(), user.getSingleActorName());
  }

  /**
   * Gets the localized {@link RelationshipConstraintException} message, which
   * is thrown when a relationship contraint is violated
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String relationshipConstraintException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "RelationshipConstraintException", "The requested operation would violate a constraint on Relationships.");
  }

  /**
   * Thrown when an attempt is made to define a recursive relationship on a
   * Tree. Gets the localized {@link RelationshipConstraintException} message,
   * which is thrown when a relationship contraint is violated
   * 
   * @param locale
   *          The desired locale
   * @param treeLabel
   * @param parentUniqueLabel
   * @param childUniqueLabel
   * @return The localized error message
   */
  public static String relationshipRecursionException(Locale locale, String treeLabel, String parentUniqueLabel, String childUniqueLabel)
  {
    return LocalizationFacade.getMessage(locale, "RelationshipRecursionException", "Relationship [{0}] cannot be created.  [{1}] already has the parent object [{2}] as a child. This would cause an infinite recursive relationship.", treeLabel, parentUniqueLabel, childUniqueLabel);
  }

  /**
   * Thrown when defining a relationship and the parent relationship cannot be
   * extended. Gets the localized {@link RelationshipConstraintException}
   * message, which is thrown when a relationship contraint is violated
   * 
   * @param locale
   *          The desired locale.
   * @param label
   *          Label of the parent relationship.
   * @return The localized error message
   */
  public static String relationshipInvalidParentDefException1(Locale locale, String label)
  {
    return LocalizationFacade.getMessage(locale, "RelationshipInvalidParentDefException1", "Super Relationship [{0}] cannot be extended.");
  }

  /**
   * Thrown when defining a relationship and the parent type is not a
   * relationship, rather a class. Gets the localized
   * {@link RelationshipConstraintException} message, which is thrown when a
   * relationship contraint is violated
   * 
   * @param locale
   *          The desired locale.
   * @param label
   *          Label of the parent entity.
   * @return The localized error message
   */
  public static String relationshipInvalidParentDefException2(Locale locale, String label)
  {
    return LocalizationFacade.getMessage(locale, "RelationshipInvalidParentDefException2", "Relationship types must inherit from another relationship type. The given super type [{0}] is a class.");
  }

  /**
   * Thrown when an attempt is made to add a struct object to a relationship.
   * Gets the localized {@link RelationshipInvalidObjectException} message,
   * which is thrown when a relationship contraint is violated
   * 
   * @param locale
   *          The desired locale.
   * @param structTypeLabel
   *          Struct type label.
   * @param relationshipTypeLabel
   *          Relationship label.
   * @return The localized error message
   */
  public static String relationshipInvalidObjectException(Locale locale, String structTypeLabel, String relationshipTypeLabel)
  {
    return LocalizationFacade.getMessage(locale, "RelationshipInvalidObjectException", "[{0}] is a basic object.  It cannot participate in the [{1}] relationship.", structTypeLabel, relationshipTypeLabel);
  }

  /**
   * Thrown when a relationship is defined that would violate a cardinality
   * constraint. Gets the localized {@link RelationshipInvalidObjectException}
   * message, which is thrown when a relationship contraint is violated
   * 
   * @param locale
   *          The desired locale.
   * @param objectLabel
   *          Object type label.
   * @param cardinality
   *          Cardinality.
   * @param relationshipLabel
   *          Relationship type label.
   * @param instanceLabel
   *          Object instance label.
   * @param otherObjectLabel
   *          Other object type label.
   * 
   * @return The localized error message
   */
  public static String relationshipCardinalityException(Locale locale, String objectLabel, String cardinality, String relationshipLabel, String instanceLabel, String otherObjectLabel)
  {
    return LocalizationFacade.getMessage(locale, "RelationshipCardinalityException", "A [{0}] can only be in {1} [{2}] relationships with objects of type [{4}].  {3} already has {1} [{4}].", objectLabel, cardinality, relationshipLabel, instanceLabel, otherObjectLabel);
  }

  /**
   * Thrown when a path between a child node and a parent node is created, yet
   * such a path already exists. Gets the localized
   * {@link DuplicateGraphPathException} message, which is thrown when a
   * relationship contraint is violated
   * 
   * @param locale
   *          The desired locale
   * @param parentUniqueString
   *          Unique label business object string for parent.
   * @param childRelLabel
   *          Child relationship label.
   * @param childUniqueString
   *          Unique label business object string for child.
   * 
   * @return The localized error message
   */
  public static String duplicateGraphPathException(Locale locale, String parentUniqueString, String childRelLabel, String childUniqueString)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateGraphPathException", "[{0}] already [{1}] [{2}].", parentUniqueString, childRelLabel, childUniqueString);
  }

  /**
   * Gets the localized {@link ReservedWordException} message, which is thrown
   * when an entity or attribtue name is reserved.
   * 
   * @param locale
   *          The desired locale
   * @param reservedWord
   *          The reserved (unusuable) word
   * @return The localized error message
   */
  public static String reservedWordException(Locale locale, String reservedWord, ReservedWordException.Origin exceptionOrigin)
  {
    return LocalizationFacade.getMessage(locale, "ReservedWordException" + exceptionOrigin.getKey(), "The name [{0}] is reserved - please specify a different name.", reservedWord);
  }

  /**
   * Gets the localized {@link InvalidIdentifierException} message, which is
   * thrown when an entity or attribtue name is not a valid identifier.
   * 
   * @param locale
   *          The desired locale
   * @param invalidIdentifier
   *          The reserved (unusuable) word
   * @return The localized error message
   */
  public static String invalidIdentifierException(Locale locale, String invalidIdentifier)
  {
    return LocalizationFacade.getMessage(locale, "InvalidIdentifierException", "The identifier [{0}] is not valid.  All identifiers must begin with a letter, an underscore, or a Unicode currency character. Any other symbol, such as a number, is not valid.", invalidIdentifier);
  }

  /**
   * Gets the localized {@link InvalidColumnNameException} message, which is
   * thrown when an entity or attribtue name is not a valid identifier.
   * 
   * @param locale
   *          The desired locale
   * @param invalidColumnName
   *          The reserved (unusuable) word
   * @return The localized error message
   */
  public static String invalidColumnNameException(Locale locale, String invalidColumnName)
  {
    return LocalizationFacade.getMessage(locale, "InvalidColumnNameException", "The column name [{0}] is not valid.  Column names must begin with a letter, then followed by a letter, number, or an underscore.", invalidColumnName);
  }

  /**
   * @deprecated Use CommonExceptionMessageLocalizer.invalidSessionException
   *             instead.
   * 
   *             Gets the localized {@link InvalidSessionException} message,
   *             which is thrown when there is an error in the session layer
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  @Deprecated
  public static String invalidSessionException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "InvalidSessionException", "Your session has expired.  Please log in.");
  }

  /**
   * Gets the localized {@link StaleEntityException} message, which is thrown
   * when changes are made to a stale object.
   * 
   * @param locale
   *          The desired locale
   * @param entity
   *          The stale object
   * @return The localized error message
   */
  public static String staleEntityException(Locale locale, Entity entity)
  {
    return LocalizationFacade.getMessage(locale, "StaleEntityException", "[{0}] is out of date - please update and try your operation again.", entity.toString());
  }

  public static String staleEntityException(Locale locale, String label)
  {
    return LocalizationFacade.getMessage(locale, "StaleEntityException", "[{0}] is out of date - please update and try your operation again.", label);
  }

  /**
   * Gets the localized {@link StateException} message, which encapsulates
   * errors that arise from state machines.
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String stateException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "StateException", "An error was encountered in a state machine.  Please try your operation again.  If the problem continues, alert your technical support staff.");
  }

  /**
   * Gets the localized {@link DuplicateStateDefinitionException} message, which
   * encapsulates errors that arise from trying to define a state on a machine
   * that already has a state with the same name.
   * 
   * @param locale
   *          The desired locale.
   * @param newState
   *          Name of the new state to be defined.
   * @param type
   *          Type that has a state machine.
   * @return The localized error message
   */
  public static String duplicateStateDefinitionException(Locale locale, String newState, String type)
  {
    return LocalizationFacade.getMessage(locale, "DuplicateStateDefinitionException", "A state named [{0}] already exists in the [{1}] state machine.", newState, type);
  }

  /**
   * Gets the localized {@link DefaultStateExistsException} message, which
   * encapsulates errors that arise a default state is defined on a state
   * machine that already has a default state.
   * 
   * @param locale
   *          The desired locale.
   * @param newState
   *          Name of the new state to be defined.
   * @param type
   *          Type that has a state machine.
   * @return The localized error message
   */
  public static String defaultStateExistsException(Locale locale, String type)
  {
    return LocalizationFacade.getMessage(locale, "DefaultStateExistsException", "A default state has already been defined for State Machine [{0}].", type);
  }

  /**
   * Gets the localized {@link InvalidEntryStateException} message, which
   * encapsulates errors that arise when an invalid entry state is set on an
   * object.
   * 
   * @param locale
   *          The desired locale.
   * @param newState
   *          Name of the new state to be defined.
   * @param type
   *          Type that has a state machine.
   * @return The localized error message
   */
  public static String invalidEntryStateException(Locale locale, String newState, String type)
  {
    return LocalizationFacade.getMessage(locale, "InvalidEntryStateException", "State [{0} on machine [{1}] is not an entry state.", newState, type);
  }

  /**
   * Gets the localized {@link UnexpectedTypeException} message, which is thrown
   * when a getter method is called but the object it finds is not of the
   * expected type.
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String unexpectedTypeException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "UnexpectedTypeException", "An object of an unexpected type was returned.  Please try your operation again.  If the problem continues, alert your technical support staff.");
  }

  /**
   * Gets the localized {@link UnimplementedStubException} message, which is
   * thrown when a base method is called that should have been overridden.
   * 
   * @param locale
   *          The desired locale
   * @param type
   *          The type name that contains the method
   * @param method
   *          The name of the method
   * @return The localized error message
   */
  public static String unimplementedStubException(Locale locale, String type, String method)
  {
    return LocalizationFacade.getMessage(locale, "UnimplementedStubException", "Method [{1}] on type [{0}] has not been implemented.", type, method);
  }

  /**
   * Gets the localized {@link VaultException} message, which is thrown when
   * there is a problem with a file vault.
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String vaultException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "VaultException", "There was a problem accessing the file vault.  Please try again.");
  }

  /**
   * Gets the localized {@link XMLException} message, which is thrown when there
   * is an error in an archival (non-readable) xml document (metadata.xml, for
   * example).
   * 
   * @param locale
   *          The desired locale
   * @return The localized error message
   */
  public static String xmlException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "XMLException", "A system xml file appears to be corrupt - please notify your technical support team.");
  }

  /**
   * Gets the localized {@link InvalidMRUCacheSizeException} message, which is
   * thrown when there is an invalid cache size for a Most Recently Used Cache
   * Algorithm used by an MdBusiness.
   * 
   * @param locale
   *          the desired locale
   * @param mdBusiness
   *          The localized error message
   * @return
   */
  public static String invalidMRUCacheSizeException(Locale locale, MdBusinessDAO mdBusiness)
  {
    return LocalizationFacade.getMessage(locale, "InvalidMRUCacheException", "Invalid MRU Cache size [{0}] [{1}]", mdBusiness.definesType(), String.valueOf(mdBusiness.getCacheSize()));
  }

  /**
   * Gets the localized {@link InvalidLoginException} message, which is thrown
   * when there is an invalid cache size for a Most Recently Used Cache
   * Algorithm used by an MdBusiness.
   * 
   * @param locale
   *          the desired locale
   * @param mdBusinessIF
   *          The localized error message
   * @return
   */
  public static String invalidLoginException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "InvalidLoginException", "Login failed - please try again.");
  }

  /**
   * Gets the localized {@link MaximumSessionsException} message, which is
   * thrown when there is an invalid cache size for a Most Recently Used Cache
   * Algorithm used by an MdBusiness.
   * 
   * @param locale
   *          the desired locale
   * @param mdBusinessIF
   *          The localized error message
   * @return
   */
  public static String maximumSessionsException(Locale locale, SingleActorDAOIF user)
  {
    return LocalizationFacade.getMessage(locale, "MaximumSessionsException", "The user [{0}] has the maximum number of sessions open.", user.getSingleActorName());
  }

  /**
   * Gets the localized {@link InactiveUserException} message, which is thrown
   * when there is an invalid cache size for a Most Recently Used Cache
   * Algorithm used by an MdBusiness.
   * 
   * @param locale
   *          the desired locale
   * @param mdBusinessIF
   *          The localized error message
   * @return
   */
  public static String inactiveUserException(Locale locale, UserDAOIF user)
  {
    return LocalizationFacade.getMessage(locale, "InactiveUserException", "The user [{0}] is inactive.", user.getSingleActorName());
  }

  /**
   * Gets the localized (@link {@link GroovyQueryException} message, which is
   * thrown when there is an invalid Groovy query executed through a Groovy
   * shell.
   * 
   * @param locale
   * @param groovyQuery
   * @param queryError
   * @return
   */
  public static String groovyQueryException(Locale locale, String groovyQuery, String queryError)
  {
    return LocalizationFacade.getMessage(locale, "GroovyQueryException", "The following groovy query:\n[{0}]\n Produced the following error:\n[{1}]", groovyQuery, queryError);
  }

  public static String importDomainException(Locale locale, String requiredRole)
  {
    return LocalizationFacade.getMessage(locale, "ImportDomainExecuteException", "ImportDomainExecuteException [{0}]", requiredRole);
  }

  public static String rBACExceptionInvalidStateMachine(Locale locale, String stateMachineDisplayLabel, String stateMachineOwnerDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "RBACExceptionInvalidStateMachine", "State Machine [{0}] cannot have permissions. Add them to [{1}] instead.", stateMachineDisplayLabel, stateMachineOwnerDisplayLabel);
  }

  public static String rBACExceptionInvalidOperation(Locale locale, String operation, String classDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "RBACExceptionInvalidOperation", "Operation [{0}] is not applicable for type [{1}].", operation, classDisplayLabel);
  }

  public static String rBACExceptionOwnerRole(Locale locale, String singleActorName, String roleName)
  {
    return LocalizationFacade.getMessage(locale, "RBACExceptionOwnerRole", "SingleActor [{0}] cannont be assigned to role [{1}].", singleActorName, roleName);
  }

  public static String rBACExceptionSingleActorConflictingRole(Locale locale, String singleActorName, String roleName, String conflictingRoleName)
  {
    return LocalizationFacade.getMessage(locale, "RBACExceptionSingleActorConflictingRole", "SingleActor [{0}] cannot be assigned to role [{1}] because it already belongs to conflicting role [{2}].", singleActorName, roleName, conflictingRoleName);
  }

  public static String rBACExceptionInheritance(Locale locale, String roleName, String conflictingRoleName)
  {
    return LocalizationFacade.getMessage(locale, "RBACExceptionInheritance", "Role [{0}] cannot inherit from role [{1}] because it would break existing user assignment conflict of intrest constraints.", roleName, conflictingRoleName);
  }

  public static String rBACExceptionInvalidSSDConstraint(Locale locale, String roleName, String ssdName)
  {
    return LocalizationFacade.getMessage(locale, "RBACExceptionInvalidSSDConstraint", "Role [{0}] invalidates existing constraints on SSD [{1}].", roleName, ssdName);
  }

  public static String rBACExceptionInvalidSSDCardinality(Locale locale, int cardinality, String ssdName)
  {
    return LocalizationFacade.getMessage(locale, "RBACExceptionInvalidSSDCardinality", "The new cardinality of [{0}] is invalid for SSD [{1}] due to existing user-role assignments.", Integer.toString(cardinality), ssdName);
  }

  public static String existingMdMethodReferenceException(Locale locale, MdEntityDAO mdEntity, List<ParameterMarker> markers)
  {
    StringBuffer methodNames = new StringBuffer();

    for (int i = 0; i < markers.size() - 1; i++)
    {
      ParameterMarker marker = markers.get(i);
      methodNames.append("[" + marker.getName() + "] of MdType [" + marker.getEnclosingMdTypeDAO().definesType() + "], ");
    }

    ParameterMarker lastMarker = markers.get(markers.size() - 1);
    methodNames.append("[" + lastMarker.getName() + "] of MdType [" + lastMarker.getEnclosingMdTypeDAO().definesType() + "]");

    return LocalizationFacade.getMessage(locale, "ExistingMdMethodReferenceException", "ExistingMdMethodReferenceException", mdEntity.definesType(), methodNames.toString());
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on an aggregate
   * function.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidAggregateOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidAggregateOperatorException", "Comparison operator [{0}] is not valid for an aggregate function.", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on an MIN or MAX
   * function.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidMinMaxOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidMinMaxOperatorException", "Comparison operator [{0}] is not valid for the [minimum] or [maximum] function.", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on a number.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidNumberOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidNumberOperatorException", "Comparison operator [{0}] is not valid for a number.", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on a String.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidStringOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidStringOperatorException", "Comparison operator [{0}] is not valid for text.", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on a Boolean.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidBooleanOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidBooleanOperatorException", "Comparison operator [{0}] is not valid for boolean (true/false).", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on a date, time, or
   * datetime.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidMomentOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidMomentOperatorException", "Comparison operator [{0}] is not valid for date, time, or datetime.", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on a blob.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidBlobOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidBlobOperatorException", "Comparison operator [{0}] is not valid for a blob.", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on a struct.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidStructOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidStructOperatorException", "Comparison operator [{0}] is not valid for a struct.", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link InvalidComparisonOperatorException}
   * message, which is thrown when a query is being built from a type-unsafe
   * context and an invalid comparison operator is specified on a ref.
   * 
   * @param locale
   * @param invalidOperator
   * @return localized message
   */
  public static String invalidRefOperatorException(Locale locale, String invalidOperator)
  {
    return LocalizationFacade.getMessage(locale, "InvalidRefOperatorException", "Comparison operator [{0}] is not valid for a reference.", invalidOperator);
  }

  /**
   * Gets the localized (@link {@link NoAggregateInGroupByException} message,
   * which is thrown when a query is being built from a type-unsafe context and
   * an invalid comparison operator is specified on a ref.
   * 
   * @param locale
   * @return localized message
   */
  public static String noAggregateInGroupByException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "NoAggregateInGroupByException", "You cannot group by an aggregate function.");
  }

  /**
   * Gets the localized (@link {@link SubSelectReturnedMultipleRowsException}
   * message, which is thrown when a sub query used in a select clause returns
   * more than one row.
   * 
   * @param locale
   * @return localized message
   */
  public static String subSelectReturnedMultipleRowsException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "SubSelectReturnedMultipleRowsException", "Subquery returns more than 1 row.");
  }

  /**
   * Gets the localized (@link {@link InvalidOrderByPrimitiveException} message,
   * which is thrown when a query is being built from a type-unsafe context and
   * an invalid comparison operator is specified on a ref.
   * 
   * @param locale
   * @return localized message
   */
  public static String invalidOrderByPrimitiveException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "InvalidOrderByPrimitiveException", "Only primitive attributes can participate in the ORDER BY clause.");
  }

  /**
   * Gets the localized (@link {@link InvalidNumericSelectableException}
   * message, which is thrown when a non numeric selectable is passed to a
   * numeric function.
   * 
   * @param locale
   * @param attributeDisplayLabel
   * @return localized message
   */
  public static String invalidNumericSelectableException(Locale locale, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "InvalidNumericSelectableException", "[{0}] is not a numeric value.  It was passed into a numeric function.", attributeDisplayLabel);
  }

  /**
   * Gets the localized (@link {@link InvalidMomentSelectableException} message,
   * which is thrown when a non date or time selectable is passed to a date or
   * time function.
   * 
   * @param locale
   * @param attributeDisplayLabel
   * @return localized message
   */
  public static String invalidMomentSelectableException(Locale locale, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "InvalidMomentSelectableException", "[{0}] is not a date or a time value.  It was passed into a date or a time functio", attributeDisplayLabel);
  }

  /**
   * Gets the localized (@link {@link ValueQueryMissingSelectCaluseException}
   * message, which is thrown when a value query is missing a select clause.
   * 
   * @param locale
   * @return localized message
   */
  public static String valueQueryMissingSelectCaluseException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "ValueQueryMissingSelectCaluseException", "No attributes were selected to be included in the result of the query.");
  }

  /**
   * Gets the localized (@link {@link AmbiguousAttributeException} message,
   * which is thrown when an attribute in the select clause is ambiguous.
   * 
   * @param locale
   * @param attributeName
   * @return localized message
   */
  public static String ambiguousAttributeException(Locale locale, String attributeName)
  {
    return LocalizationFacade.getMessage(locale, "AmbiguousAttributeException", "Attribute [{0}] in the select clause is ambiguous.", attributeName);
  }

  /**
   * Gets the localized (@link
   * {@link MissingAttributeInSelectForGroupByException} message, which is
   * thrown when a value query is missing a select clause.
   * 
   * @param locale
   * @param attributeDisplayLabel
   * @return localized message
   */
  public static String missingAttributeInSelectForGroupByException(Locale locale, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "MissingAttributeInSelectForGroupByException", "The attribute [{0}] specified in the GROUP BY section must also be included in the result of the query.", attributeDisplayLabel);
  }

  /**
   * Gets the localized (@link {@link MissingHavingClauseAttributeException}
   * message, which is thrown when a value query is missing a select clause.
   * 
   * @param locale
   * @param attributeDisplayLabel
   * @param definingTypeDisplayLabel
   * @return localized message
   */
  public static String missingHavingClauseAttributeException(Locale locale, String attributeDisplayLabel, String definingTypeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "MissingHavingClauseAttributeException", "The attribute [{0}] defined by [{1}] in the HAVING clause must also be included in the result of the query or in the GROUP BY section.", attributeDisplayLabel, definingTypeDisplayLabel);
  }

  /**
   * Gets the localized (@link {@link AttributeNotInGroupByOrAggregate} message,
   * which is thrown when a value query is missing a select clause.
   * 
   * @param locale
   * @param attributeDisplayLabel
   * @return localized message
   */
  public static String attributeNotInGroupByOrAggregate(Locale locale, String attributeDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "AttributeNotInGroupByOrAggregate", "When an attribute is specified as GROUP BY, all attributes must then either be GROUP BY or must be used in an aggregate function.  The attribute [{0}] is neither GROUP BY nor is used in an aggregate function.", attributeDisplayLabel);
  }

  /**
   * Message stating that the database is being backed up.
   * 
   * @return localized message
   */
  public static String backingUpDatabaseMessage(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "BackingupDatabaseMessage", "Backing up database");
  }

  /**
   * Message stating that a file is being backed up.
   * 
   * @param locale
   * @param filePathAndName
   * @return localized message
   */
  public static String backingUpFileMessage(Locale locale, String filePathAndName)
  {
    return LocalizationFacade.getMessage(locale, "BackingUpFileMessage", "Backing up file: {0}", filePathAndName);
  }

  /**
   * Message stating that the application is being restored.
   * 
   * @param locale
   * @return localized message
   */
  public static String restoringApplicationMessage(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "RestoringApplicationMessage", "Restoring application:");
  }

  /**
   * Message stating that files are being extracted from the backup.
   * 
   * @param locale
   * @return localized message
   */
  public static String extractingFilesMessage(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "ExtractingFilesMessage", "Extracting Files:");
  }

  /**
   * Message stating that tables are being dropped.
   * 
   * @param locale
   * @return localized message
   */
  public static String droppingTablesMessage(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "DroppingTablesMessage", "Dropping tables:");
  }

  /**
   * Message stating that database records are being imported.
   * 
   * @param locale
   * @return localized message
   */
  public static String importingDatabaseRecords(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "ImportingDatabaseRecords", "Importing database records:");
  }

  /**
   * Message stating that the given file is being extracted from the database.
   * 
   * @param locale
   * @param filePathAndName
   * @return localized message
   */
  public static String extractingFileMessage(Locale locale, String filePathAndName)
  {
    return LocalizationFacade.getMessage(locale, "ExtractingFileMessage", "Extracting file: {0}", filePathAndName);
  }

  /**
   * Message stating that the backup is complete.
   * 
   * @param locale
   * @return localized message
   */
  public static String restoreCompleteMessage(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "RestoreCompleteMessage", "Restore complete");
  }

  /**
   * Message stating that the backup is complete.
   * 
   * @param locale
   * @return localized message
   */
  public static String backupCompleteMessage(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "BackupCompleteMessage", "Backup complete");
  }

  /**
   * Message stating that the given webap is being cleaned.
   * 
   * @param locale
   * @param webappPath
   * @return localized message
   */
  public static String cleaningWebappFolderMessage(Locale locale, File webappPath)
  {
    return LocalizationFacade.getMessage(locale, "CleaningWebappFolder", "Cleaning webapp folder: {0}", webappPath.getAbsolutePath());
  }

  /**
   * Message stating that the given rolename must be namespaced
   * 
   * @param locale
   * @param rolename
   *          The invalid rolename
   * @return
   */
  public static String roleNamespaceException(Locale locale, String rolename)
  {
    return LocalizationFacade.getMessage(locale, "RoleNamespaceException", "The rolename [{0}] must be namespaced.", rolename);
  }

  /**
   * Message state that the user did not provide a locale when logging in
   * 
   * @param locale
   * @param user
   * 
   * @return
   */
  public static String noLocaleException(Locale locale, SingleActorDAOIF user)
  {
    return LocalizationFacade.getMessage(locale, "NoLocaleException", "The user [{0}] did not provide a locale when logging in.", user.getSingleActorName());
  }

  /**
   * A locale specified in the SupportedLocale enumeration master does not
   * conform to Locale format standards
   * 
   * @param locale
   * @param localeString
   * @return
   */
  public static String InvalidLocaleFormatException(Locale locale, String localeString)
  {
    return LocalizationFacade.getMessage(locale, "InvalidLocaleFormatException", "The locale [{0}] does not have the proper format - please notify your technical support team.", localeString);
  }

  /**
   * Gets the localized {@link SynchronizationSequenceGapException} message,
   * when a gap exists in imported sequence numbers for a given site.
   * 
   * @param locale
   *          The desired locale
   * @param importSite
   *          site being imported.
   * @param lastImportedSequence
   *          last imported sequence from the imported site.
   * @param firstExportSequence
   *          first export sequence in the import file from the given site.
   * @param neededImportSequence
   *          the
   * @return The localized error message
   */
  public static String synchronizationSequenceGapException(Locale locale, String importSite, Long lastImportedSequence, Long firstExportSequence, Long neededImportSequence)
  {
    return LocalizationFacade.getMessage(locale, "SynchronizationSequenceGapException", "There cannot be a gap in sequence numbers when importing from another site.  The last imported sequence number from site [{0}] was [{1}], but the current import starts with sequence number [{2}].  Please import from site [{0}] starting with sequence number [{3}].", importSite, lastImportedSequence.toString(), firstExportSequence.toString());
  }

  /**
   * Gets the localized {@link TransactionImportInvalidItem} message, when a
   * transaction item is imported.
   * 
   * @param locale
   *          The desired locale
   * @param componentSiteMaster
   *          site of the component that is invalid
   * @param localizedMessage
   *          localized error message
   * @return The localized error message
   */
  public static String transactionImportInvalidItem(Locale locale, String componentSiteMaster, String localizedMessage)
  {
    return LocalizationFacade.getMessage(locale, "TransactionImportInvalidItem", "An error occurred when importing a record that originated from site {0}.  The error is: {1}.", componentSiteMaster, localizedMessage);
  }

  /**
   * Gets the localized {@link TransactionVersionException} message.
   * 
   * @param locale
   *          The desired locale. Cannot be null.
   * @param expectedVersion
   *          The expected version number. Cannot be null.
   * @param actualVersion
   *          The actual version number. Cannot be null.
   * @return Localized error message
   */
  public static String transactionVersionException(Locale locale, String expectedVersion, String actualVersion)
  {
    return LocalizationFacade.getMessage(locale, "TransactionVersionException", "There was a version mismatch between the file [{1}] and the importer [{0}].  Please update the local node to [{1}] or re-export the file from a node of version [{0}].", expectedVersion, actualVersion);
  }

  public static String fieldValidationProblem(Locale locale, String attributeLabel, String condition)
  {
    return LocalizationFacade.getMessage(locale, "FieldValidationProblem", "The attribute [{0}] is not applicable when {1}.", attributeLabel, condition);
  }

  public static String fieldCondition(Locale locale, String fieldLabel, String operationLabel, String value)
  {
    return LocalizationFacade.getMessage(locale, "FieldCondition", "[{0}] is not [{1}] to [{2}].", fieldLabel, operationLabel, value);
  }

  public static String andCondition(Locale locale, String firstCondition, String secondCondition)
  {
    return LocalizationFacade.getMessage(locale, "AndCondition", "[{0}] and [{1}].", firstCondition, secondCondition);
  }

  public static String attributeDefinitionLengthException(Locale locale, MdAttributeDAOIF mdAttribute, int length)
  {
    return LocalizationFacade.getMessage(locale, "AttributeDefinitionLengthException", "The value [{1}] on attribute definition [{0}] is invalid.  It must be an integer greater than 1.", mdAttribute.getDisplayLabel(locale), new Integer(length).toString());
  }

  public static String attributeDefinitionDecimalException(Locale locale, MdAttributeDAOIF mdAttribute, int length, int decimal)
  {
    return LocalizationFacade.getMessage(locale, "AttributeDefinitionDecimalException", "The value [{1}] on attribute definition [{0}] is invalid.  It must be between [1] and [{2}].", mdAttribute.getDisplayLabel(locale), new Integer(length).toString(), new Integer(decimal).toString());
  }

  public static String numericFieldOverflowException(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "NumericFieldOverflowException", "A specified numeric value is too large for its database column");
  }

  public static String compressingDirectoryMessage(Locale locale, String backupFileLocation)
  {
    return LocalizationFacade.getMessage(locale, "CompressingDirectoryMessage", "Compressing the directory: {0}", backupFileLocation);
  }

  public static String backingUpCacheMessage(Locale locale)
  {
    return LocalizationFacade.getMessage(locale, "BackingUpCacheMessage", "Backing up cache files.");
  }

  public static String fieldConversionException(Locale locale, String fieldLabel)
  {
    return LocalizationFacade.getMessage(locale, "FieldConversionException", "FieldConversionException", fieldLabel);
  }

  /**
   * Localized message whenever an <code>InvalidExpressionSyntaxException</code>
   * is thrown.
   * 
   * @param locale
   * @param expressionExceptionMessage
   * @return Localized message whenever an
   *         <code>InvalidExpressionSyntaxException</code> is thrown.
   */
  public static String invalidExpressionSyntaxException(Locale locale, MdAttributeDAOIF mdAttributeDAOIF, String expressionExceptionMessage)
  {
    return LocalizationFacade.getMessage(locale, "InvalidExpressionSyntaxException", "The attribute [{0}] has an invalid expression:\\n{1}", mdAttributeDAOIF.getDisplayLabel(locale), expressionExceptionMessage);
  }

  /**
   * Localized message whenever an <code>ExpressionException</code> is thrown.
   * 
   * @param locale
   * @param expressionExceptionMessage
   * @return Localized message whenever an <code>ExpressionException</code> is
   *         thrown.
   */
  public static String expressionException(Locale locale, MdAttributeDAOIF mdAttributeDAOIF, String expressionExceptionMessage)
  {
    return LocalizationFacade.getMessage(locale, "ExpressionException", "The expression on attribute [{0}] has an error:\\n{1}", mdAttributeDAOIF.getDisplayLabel(locale), expressionExceptionMessage);
  }

  /**
   * Gets the localized {@link LoginNotSupportedException} message, which is
   * thrown when a user tries to login in with an invalid class
   * 
   * @param locale
   *          the desired locale
   * @param mdBusinessIF
   *          The localized error message
   * @return
   */
  public static String loginNotSupportedException(Locale locale, SingleActorDAOIF user)
  {
    return LocalizationFacade.getMessage(locale, "LoginNotSupportedException", "The class [{0}] does not support logging in.", user.getType());
  }

  /**
   * Gets the localized {@link InvalidIndicatorDefinition} message, which is
   * thrown when an invalid indicator attribute is defined.
   * 
   * @param locale
   *          the desired locale
   * @param _localizedIndicatorDisplayLabel
   *          The localized error message
   * @return
   */
  public static String invalidIndicatorDefinition(Locale locale, String _localizedIndicatorDisplayLabel)
  {
    return LocalizationFacade.getMessage(locale, "InvalidIndicatorDefinition", "The indicator attribute definition [{0}] is invalid. The left and right operands must both be either a number or a boolean.", _localizedIndicatorDisplayLabel);
  }

}
