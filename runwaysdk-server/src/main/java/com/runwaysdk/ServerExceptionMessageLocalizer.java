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
import com.terraframe.utf8.UTF8ResourceBundle;

/**
 * Provides typesafe getter for access to localized business-layer error
 * messages.
 * 
 * @author Eric Grunzke
 */
public class ServerExceptionMessageLocalizer extends ExceptionMessageLocalizer
{
  private static final String BUNDLE = "serverExceptions";

  /**
   * Fetches the parameterized, localized error message template for the given
   * exception. The variable String arguments represent the parameters in the
   * template string. For example, given the template "The {0} in the {1}." and
   * arguments "cat" and "hat", the final String will be "The cat in the hat."
   * 
   * @param locale
   *          The desired locale of the message
   * @param key
   *          The name of the Exception whose message is being retrieved
   * @param params
   *          The array of parameters to plug into the template string
   */
  protected static String getMessage(Locale locale, String key, String... params)
  {
    String hashkey = BUNDLE;

    if (locale != null)
    {
      hashkey += "-" + locale.toString();

      if (!props.containsKey(hashkey))
      {
        props.put(hashkey, UTF8ResourceBundle.getBundle(BUNDLE, locale));
      }
    }
    else if (!props.containsKey(hashkey))
    {
      props.put(hashkey, UTF8ResourceBundle.getBundle(BUNDLE));
    }

    String template = props.get(hashkey).getString(key);

    return parseMessage(template, params);
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
  public static String invalidIdException(Locale locale, String oid)
  {
    return getMessage(locale, "InvalidIdException", oid);
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
    return getMessage(locale, "AdminScreenAccessException", user.getSingleActorName());
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
    return getMessage(locale, "ProblemCollectionException");
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
    return getMessage(locale, "AbstractInstantiationException", mdClass.getDisplayLabel(locale));
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
    return getMessage(locale, "AttributeDefinitionException", mdAttribute.getDisplayLabel(locale));
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
    return getMessage(locale, "DuplicateAttributeInInheritedHierarchy", mdAttribute.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale), parentMdClassIF.getDisplayLabel(locale));
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
    return getMessage(locale, "DuplicateAttributeDefinedInSubclass", mdAttribute.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale), childMdClassIF.getDisplayLabel(locale));
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
    return getMessage(locale, "DuplicateAttributeDefinition", mdAttribute.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale));
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
    return getMessage(locale, "CannotAddAttriubteToClassException", mdAttribute.getDisplayLabel(locale), forbiddenMdClassIF.getDisplayLabel(locale));
  }

  /**
   * Thrown when the class cannot define attributes of the given type.
   * 
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param class that defines the type of the mdAttribute.
   * @param mdClassIF
   *          class that a new attribute is being added to.
   */
  public static String attributeOfWrongTypeForClassException(Locale locale, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF mdAttributeDefiningClass, MdClassDAOIF mdClassIF)
  {
    return getMessage(locale, "AttributeOfWrongTypeForClassException", mdAttribute.getDisplayLabel(locale), mdAttributeDefiningClass.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale));
  }

  /**
   * Thrown when a reference attribute type has not been configured to reference
   * a class.
   * 
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param class that defines the type of the mdAttribute.
   * @param mdClassIF
   *          class that a new attribute is being added to.
   */
  public static String referenceAttributeNotReferencingClassException(Locale locale, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF mdClassIF)
  {
    return getMessage(locale, "ReferenceAttributeNotReferencingClassException", mdAttribute.getDisplayLabel(locale), mdClassIF.getDisplayLabel(locale));
  }

  /**
   * Thrown when an attribute of a certain type cannot participate in a
   * uniqueness constraint.
   * 
   * @param devMessage
   * @param mdAttributeConcreteIF
   *          metadata of the attribute that is already defined.
   * @param class that defines the type of the mdAttribute.
   */
  public static String attributeInvalidUniquenessConstraintException(Locale locale, MdAttributeConcreteDAOIF mdAttribute, MdClassDAOIF mdAttributeDefiningClass)
  {
    return getMessage(locale, "AttributeInvalidUniquenessConstraintException", mdAttribute.getDisplayLabel(locale), mdAttributeDefiningClass.getDisplayLabel(locale));
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
    return getMessage(locale, "RequiredUniquenessConstraintException", mdAttribute.getDisplayLabel(locale));
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
    return getMessage(locale, "ClassPublishException", mdClassIF.getDisplayLabel(locale));
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
    return getMessage(locale, "MethodDefinitionException_NameExists", definingMdType.getDisplayLabel(locale), mdMethod.getName());
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
    return getMessage(locale, "MethodDefinitionException_InvalidParentReference", mdMethod.getDisplayLabel(locale), definingMdTypeIF.getDisplayLabel(locale), mdClassMdBusiness.getDisplayLabel(locale));
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
    return getMessage(locale, "MethodDefinitionException_InvalidReturnType", returnType, mdMethod.getDisplayLabel(locale));
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
    return getMessage(locale, "ParameterDefinitionException_NameExists", mdMethod.getDisplayLabel(locale), mdParameter.getParameterName());
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
    return getMessage(locale, "ParameterDefinitionException_OrderExists", metadata.getDisplayLabel(locale), existingMdParameter.getParameterName(), mdParameter.getParameterOrder());
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
    return getMessage(locale, "ParameterDefinitionException_InvalidType", mdParameter.getDisplayLabel(locale), mdParameter.getParameterName(), marker.getDisplayLabel(locale));
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
      unitString = getMessage(locale, "AttributeLengthException.Unit.Characters");
    }
    else
    {
      unitString = getMessage(locale, "AttributeLengthException.Unit.Character");
    }

    return getMessage(locale, "AttributeLengthException", attribute.getDisplayLabel(locale), maxLength.toString(), unitString);
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
      unitString = getMessage(locale, "AttributeLengthException.Unit.Bytes");
    }
    else
    {
      unitString = getMessage(locale, "AttributeLengthException.Unit.Byte");
    }

    return getMessage(locale, "AttributeLengthException", attribute.getDisplayLabel(locale), maxLength.toString(), unitString);
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
    return getMessage(locale, "AttributeTypeException", attribute.getDisplayLabel(locale));
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
    return getMessage(locale, "AttributeValueException", value, attribute.getDisplayLabel(locale));
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
    return getMessage(locale, "AttributeValueExceptionGeneric", value);
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
    return getMessage(locale, "AttributeValueAboveRangeProblem", value, attribute.getDisplayLabel(locale), limit);
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
    return getMessage(locale, "AttributeValueBelowRangeProblem", value, attribute.getDisplayLabel(locale), limit);
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
    return getMessage(locale, "AttributeValueCannotBeNegativeProblem", value, attribute.getDisplayLabel(locale));
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
    return getMessage(locale, "AttributeValueCannotBePositiveProblem", value, attribute.getDisplayLabel(locale));
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
    return getMessage(locale, "AttributeValueCannotBeZeroProblem", value, attribute.getDisplayLabel(locale));
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
      return getMessage(locale, "CacheCodeException", child.getDisplayLabel(locale), childCacheAlgorithmDisplayLabel, parent.getDisplayLabel(locale), parentCacheAlgorithmDisplayLabel);
    }

    // We don't have a parent type, so get the shortened message
    return getMessage(locale, "CacheCodeExceptionShort", child.getDisplayLabel(locale), childCacheAlgorithmDisplayLabel);
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
    return getMessage(locale, "MetadataCannotBeDeletedException", metadata.getDisplayLabel(locale));
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
    return getMessage(locale, "DeleteUnappliedObjectException", entity.toString());
  }

  /**
   * @param locale
   * @return
   */
  public static String disconnectedEntityException(Locale locale)
  {
    return getMessage(locale, "DisconnectedEntityException");
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
    return getMessage(locale, "CannotDeleteReferencedObject", entity.toString(), refMdAttributeIF.getDisplayLabel(locale), refMdEntityIF.getDisplayLabel(locale));
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
    return getMessage(locale, "RoleManagementException_ADD");
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
    return getMessage(locale, "RoleManagementException_REMOVE");
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
    return getMessage(locale, "ClassLoaderException", metadata.getDisplayLabel(locale));
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
    return getMessage(locale, "CompilerException", errors);
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
    return getMessage(locale, "DataNotFoundException");
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
    return getMessage(locale, "DataNotFoundException", metadata.getDisplayLabel(locale));
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
    return getMessage(locale, "MissingKeyNameValue", typeDisplayLabel, attributeDisplayLabel);
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
    return getMessage(locale, "DuplicateDataException");
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
    return getMessage(locale, "DuplicateDataExceptionSingle", definingTypeLabel, value, attributeDisplayLabel);
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
    return getMessage(locale, "DuplicateDataExceptionSingleNoValue", definingTypeLabel, attributeDisplayLabel);
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
    return getMessage(locale, "DuplicateDataExceptionMultiple", definingTypeLabel, values, attributeDisplayLabels);
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
    return getMessage(locale, "DuplicateDataExceptionMultipleNoValues", definingTypeLabel, attributeDisplayLabels);
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
    return getMessage(locale, "EmptyValueProblem", attributeDisplayLabel);
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
    return getMessage(locale, "EncryptionException", attribute.getDisplayLabel(locale));
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
    return getMessage(locale, "RunwayException");
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
    return getMessage(locale, "SiteException", entityToString, entitySite, currentSite);
  }

  public static String sourceElementNotDeclaredException(Locale locale, String value)
  {
    return getMessage(locale, "SourceElementNotDeclaredException", value);
  }

  public static String xsdDefinitionNotResolvedException(Locale locale, String value)
  {
    return getMessage(locale, "XSDDefinitionNotResolvedException", value);
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
    return getMessage(locale, "ImmutableAttributeProblem", attributeDisplayLabel);
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
    return getMessage(locale, "SystemAttributeProblem", attributeDisplayLabel);
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
    return getMessage(locale, "InheritanceException");
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
    return getMessage(locale, "InvalidDefinitionException");
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
    return getMessage(locale, "IdenticalIndexException");
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
    return getMessage(locale, "AttributeDoesNotExistExceptionNoType", attributeName);
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
    return getMessage(locale, "AttributeDoesNotExistExceptionWithType", attributeName, classDisplayLabel);
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
    return getMessage(locale, "DuplicateMdEnumerationDefinitionException", mdEnumerationLabel, masterClassType, duplicateTypeName);
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
    return getMessage(locale, "InvalidEnumerationName", enumName, mdEnumerationIF.getDisplayLabel(locale));
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
    return getMessage(locale, "InvalidAttributeForIndexDefinitionException", definesType, attributeDisplayLabel);
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
    return getMessage(locale, "NoAttributeOnIndexException", definesType);
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
    return getMessage(locale, "InvalidReferenceException", attribute.getDisplayLabel(locale), referenceDisplayLabel);
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
    return getMessage(locale, "InvalidAttributeTypeException", attributeDisplayLabel, definingTypeLabel);
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
    return getMessage(locale, errorProperty, entity.toString());
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
    return getMessage(locale, "NameConventionException", name);
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
    return getMessage(locale, "CreatePermissionException", componentIF.toString());
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
    return getMessage(locale, "DeletePermissionException", componentIF.toString());
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
    return getMessage(locale, "ReadPermissionException", componentIF.toString());
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
    return getMessage(locale, "ReadTypePermissionException", typeDisplayLabel);
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
    return getMessage(locale, "DeleteTypePermissionException", typeDisplayLabel);
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
    return getMessage(locale, "GroovyQueryExecuteException", requiredRoleName);
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
    return getMessage(locale, "WritePermissionException", componentIF.toString());
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
    return getMessage(locale, "GrantTypePermissionException", mdTypeIF.getDisplayLabel(locale));
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
    return getMessage(locale, "GrantMethodPermissionException", mdTypeIF.getDisplayLabel(locale), mdMethodIF.getDisplayLabel(locale));
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
    return getMessage(locale, "GrantAttributePermissionException", mdClassIF.getDisplayLabel(locale), mdAttributeIF.getDisplayLabel(locale));
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
    return getMessage(locale, "RevokeTypePermissionException", mdTypeIF.getDisplayLabel(locale));
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
    return getMessage(locale, "RevokeMethodPermissionException", mdTypeIF.getDisplayLabel(locale), mdMethodIF.getDisplayLabel(locale));
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
    return getMessage(locale, "RevokeAttributePermissionException", mdClassIF.getDisplayLabel(locale), mdAttributeIF.getDisplayLabel(locale));
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
    return getMessage(locale, "ExecuteStaticPermissionException", mdTypeDisplayLabel, mdMethodDislpayLabel);
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
    return getMessage(locale, "ExecuteInstancePermissionException", mutable.toString(), mdMethodDislpayLabel);
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
    return getMessage(locale, "PromotePermissionException", busines.toString(), stateLabel);
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
    return getMessage(locale, "AddChildPermissionException", childBusiness.toString(), parentBusiness.toString(), childRelDisplayLabel);
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
    return getMessage(locale, "AddParentPermissionException", parentBusiness.toString(), childBusiness.toString(), childRelDisplayLabel);
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
    return getMessage(locale, "DeleteChildPermissionException", childBusiness.toString(), parentBusiness.toString(), childRelDisplayLabel);
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
    return getMessage(locale, "DeleteParentPermissionException", parentBusiness.toString(), childBusiness.toString(), parentRelDisplayLabel);
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
    return getMessage(locale, "ReadChildPermissionException", parentBusiness.toString(), childRelDisplayLabel);
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
    return getMessage(locale, "ReadParentPermissionException", childBusiness.toString(), parentRelDisplayLabel);
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
    return getMessage(locale, "AttributeWritePermissionException", componentIF.toString(), mdAttribute.definesAttribute(), user.getSingleActorName());
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
    return getMessage(locale, "RelationshipConstraintException");
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
    return getMessage(locale, "RelationshipRecursionException", treeLabel, parentUniqueLabel, childUniqueLabel);
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
    return getMessage(locale, "RelationshipInvalidParentDefException1");
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
    return getMessage(locale, "RelationshipInvalidParentDefException2");
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
    return getMessage(locale, "RelationshipInvalidObjectException", structTypeLabel, relationshipTypeLabel);
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
    return getMessage(locale, "RelationshipCardinalityException", objectLabel, cardinality, relationshipLabel, instanceLabel, otherObjectLabel);
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
    return getMessage(locale, "DuplicateGraphPathException", parentUniqueString, childRelLabel, childUniqueString);
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
    return getMessage(locale, "ReservedWordException" + exceptionOrigin.getKey(), reservedWord);
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
    return getMessage(locale, "InvalidIdentifierException", invalidIdentifier);
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
    return getMessage(locale, "InvalidColumnNameException", invalidColumnName);
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
    return getMessage(locale, "InvalidSessionException");
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
    return getMessage(locale, "StaleEntityException", entity.toString());
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
    return getMessage(locale, "StateException");
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
    return getMessage(locale, "DuplicateStateDefinitionException", newState, type);
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
    return getMessage(locale, "DefaultStateExistsException", type);
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
    return getMessage(locale, "InvalidEntryStateException", newState, type);
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
    return getMessage(locale, "UnexpectedTypeException");
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
    return getMessage(locale, "UnimplementedStubException", type, method);
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
    return getMessage(locale, "VaultException");
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
    return getMessage(locale, "XMLException");
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
    return getMessage(locale, "InvalidMRUCacheException", mdBusiness.definesType(), String.valueOf(mdBusiness.getCacheSize()));
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
    return getMessage(locale, "InvalidLoginException");
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
    return getMessage(locale, "MaximumSessionsException", user.getSingleActorName());
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
    return getMessage(locale, "InactiveUserException", user.getSingleActorName());
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
    return getMessage(locale, "GroovyQueryException", groovyQuery, queryError);
  }

  public static String importDomainException(Locale locale, String requiredRole)
  {
    return getMessage(locale, "ImportDomainExecuteException", requiredRole);
  }

  public static String rBACExceptionInvalidStateMachine(Locale locale, String stateMachineDisplayLabel, String stateMachineOwnerDisplayLabel)
  {
    return getMessage(locale, "RBACExceptionInvalidStateMachine", stateMachineDisplayLabel, stateMachineOwnerDisplayLabel);
  }

  public static String rBACExceptionInvalidOperation(Locale locale, String operation, String classDisplayLabel)
  {
    return getMessage(locale, "RBACExceptionInvalidOperation", operation, classDisplayLabel);
  }

  public static String rBACExceptionOwnerRole(Locale locale, String singleActorName, String roleName)
  {
    return getMessage(locale, "RBACExceptionOwnerRole", singleActorName, roleName);
  }

  public static String rBACExceptionSingleActorConflictingRole(Locale locale, String singleActorName, String roleName, String conflictingRoleName)
  {
    return getMessage(locale, "RBACExceptionSingleActorConflictingRole", singleActorName, roleName, conflictingRoleName);
  }

  public static String rBACExceptionInheritance(Locale locale, String roleName, String conflictingRoleName)
  {
    return getMessage(locale, "RBACExceptionInheritance", roleName, conflictingRoleName);
  }

  public static String rBACExceptionInvalidSSDConstraint(Locale locale, String roleName, String ssdName)
  {
    return getMessage(locale, "RBACExceptionInvalidSSDConstraint", roleName, ssdName);
  }

  public static String rBACExceptionInvalidSSDCardinality(Locale locale, int cardinality, String ssdName)
  {
    return getMessage(locale, "RBACExceptionInvalidSSDCardinality", Integer.toString(cardinality), ssdName);
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

    return getMessage(locale, "ExistingMdMethodReferenceException", mdEntity.definesType(), methodNames.toString());
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
    return getMessage(locale, "InvalidAggregateOperatorException", invalidOperator);
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
    return getMessage(locale, "InvalidMinMaxOperatorException", invalidOperator);
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
    return getMessage(locale, "InvalidNumberOperatorException", invalidOperator);
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
    return getMessage(locale, "InvalidStringOperatorException", invalidOperator);
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
    return getMessage(locale, "InvalidBooleanOperatorException", invalidOperator);
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
    return getMessage(locale, "InvalidMomentOperatorException", invalidOperator);
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
    return getMessage(locale, "InvalidBlobOperatorException", invalidOperator);
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
    return getMessage(locale, "InvalidStructOperatorException", invalidOperator);
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
    return getMessage(locale, "InvalidRefOperatorException", invalidOperator);
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
    return getMessage(locale, "NoAggregateInGroupByException");
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
    return getMessage(locale, "SubSelectReturnedMultipleRowsException");
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
    return getMessage(locale, "InvalidOrderByPrimitiveException");
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
    return getMessage(locale, "InvalidNumericSelectableException", attributeDisplayLabel);
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
    return getMessage(locale, "InvalidMomentSelectableException", attributeDisplayLabel);
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
    return getMessage(locale, "ValueQueryMissingSelectCaluseException");
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
    return getMessage(locale, "AmbiguousAttributeException", attributeName);
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
    return getMessage(locale, "MissingAttributeInSelectForGroupByException", attributeDisplayLabel);
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
    return getMessage(locale, "MissingHavingClauseAttributeException", attributeDisplayLabel, definingTypeDisplayLabel);
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
    return getMessage(locale, "AttributeNotInGroupByOrAggregate", attributeDisplayLabel);
  }

  /**
   * Message stating that the database is being backed up.
   * 
   * @return localized message
   */
  public static String backingUpDatabaseMessage(Locale locale)
  {
    return getMessage(locale, "BackingupDatabaseMessage");
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
    return getMessage(locale, "BackingUpFileMessage", filePathAndName);
  }

  /**
   * Message stating that the application is being restored.
   * 
   * @param locale
   * @return localized message
   */
  public static String restoringApplicationMessage(Locale locale)
  {
    return getMessage(locale, "RestoringApplicationMessage");
  }

  /**
   * Message stating that files are being extracted from the backup.
   * 
   * @param locale
   * @return localized message
   */
  public static String extractingFilesMessage(Locale locale)
  {
    return getMessage(locale, "ExtractingFilesMessage");
  }

  /**
   * Message stating that tables are being dropped.
   * 
   * @param locale
   * @return localized message
   */
  public static String droppingTablesMessage(Locale locale)
  {
    return getMessage(locale, "DroppingTablesMessage");
  }

  /**
   * Message stating that database records are being imported.
   * 
   * @param locale
   * @return localized message
   */
  public static String importingDatabaseRecords(Locale locale)
  {
    return getMessage(locale, "ImportingDatabaseRecords");
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
    return getMessage(locale, "ExtractingFileMessage", filePathAndName);
  }

  /**
   * Message stating that the backup is complete.
   * 
   * @param locale
   * @return localized message
   */
  public static String restoreCompleteMessage(Locale locale)
  {
    return getMessage(locale, "RestoreCompleteMessage");
  }

  /**
   * Message stating that the backup is complete.
   * 
   * @param locale
   * @return localized message
   */
  public static String backupCompleteMessage(Locale locale)
  {
    return getMessage(locale, "BackupCompleteMessage");
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
    return getMessage(locale, "CleaningWebappFolder", webappPath.getAbsolutePath());
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
    return getMessage(locale, "RoleNamespaceException", rolename);
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
    return getMessage(locale, "NoLocaleException", user.getSingleActorName());
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
    return getMessage(locale, "InvalidLocaleFormatException", localeString);
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
    return getMessage(locale, "SynchronizationSequenceGapException", importSite, lastImportedSequence.toString(), firstExportSequence.toString());
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
    return getMessage(locale, "TransactionImportInvalidItem", componentSiteMaster, localizedMessage);
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
    return getMessage(locale, "TransactionVersionException", expectedVersion, actualVersion);
  }

  public static String fieldValidationProblem(Locale locale, String attributeLabel, String condition)
  {
    return getMessage(locale, "FieldValidationProblem", attributeLabel, condition);
  }

  public static String fieldCondition(Locale locale, String fieldLabel, String operationLabel, String value)
  {
    return getMessage(locale, "FieldCondition", fieldLabel, operationLabel, value);
  }

  public static String andCondition(Locale locale, String firstCondition, String secondCondition)
  {
    return getMessage(locale, "AndCondition", firstCondition, secondCondition);
  }

  public static String attributeDefinitionLengthException(Locale locale, MdAttributeDAOIF mdAttribute, int length)
  {
    return getMessage(locale, "AttributeDefinitionLengthException", mdAttribute.getDisplayLabel(locale), new Integer(length).toString());
  }

  public static String attributeDefinitionDecimalException(Locale locale, MdAttributeDAOIF mdAttribute, int length, int decimal)
  {
    return getMessage(locale, "AttributeDefinitionDecimalException", mdAttribute.getDisplayLabel(locale), new Integer(length).toString(), new Integer(decimal).toString());
  }

  public static String numericFieldOverflowException(Locale locale)
  {
    return getMessage(locale, "NumericFieldOverflowException");
  }

  public static String compressingDirectoryMessage(Locale locale, String backupFileLocation)
  {
    return getMessage(locale, "CompressingDirectoryMessage", backupFileLocation);
  }

  public static String backingUpCacheMessage(Locale locale)
  {
    return getMessage(locale, "BackingUpCacheMessage");
  }

  public static String fieldConversionException(Locale locale, String fieldLabel)
  {
    return getMessage(locale, "FieldConversionException", fieldLabel);
  }
  
  /**
   * Localized message whenever an <code>InvalidExpressionSyntaxException</code> is thrown.
   * 
   * @param locale
   * @param expressionExceptionMessage
   * @return Localized message whenever an <code>InvalidExpressionSyntaxException</code> is thrown.
   */
  public static String invalidExpressionSyntaxException(Locale locale, MdAttributeDAOIF mdAttributeDAOIF, String expressionExceptionMessage)
  {
    return getMessage(locale, "InvalidExpressionSyntaxException", mdAttributeDAOIF.getDisplayLabel(locale), expressionExceptionMessage);
  }

  /**
   * Localized message whenever an <code>ExpressionException</code> is thrown.
   * 
   * @param locale
   * @param expressionExceptionMessage
   * @return Localized message whenever an <code>ExpressionException</code> is thrown.
   */
  public static String expressionException(Locale locale, MdAttributeDAOIF mdAttributeDAOIF, String expressionExceptionMessage)
  {
    return getMessage(locale, "ExpressionException", mdAttributeDAOIF.getDisplayLabel(locale), expressionExceptionMessage);
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
    return getMessage(locale, "LoginNotSupportedException", user.getType());
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
    return getMessage(locale, "InvalidIndicatorDefinition", _localizedIndicatorDisplayLabel);
  }

}
