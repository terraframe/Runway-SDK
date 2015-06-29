/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.constants;

public enum JSON {

  /* Namespaces, utility functions, Classes */
  /**
   * Primary namespace
   */
  RUNWAY_NS("Mojo"),

  RUNWAY_META(RUNWAY_NS.getLabel() + ".Meta"),

  /**
   * Package namespace for generated types.
   */
  RUNWAY_PACKAGE_NS(RUNWAY_NS.getLabel() + ".$"),

  /**
   * DTO package
   */
  // RUNWAY_DTO(RUNWAY_NS.getLabel()+".dto"),

  /**
   * Util package
   */
  RUNWAY_UTIL(RUNWAY_NS.getLabel() + ".Util"),

  RUNWAY_FACADE(RUNWAY_PACKAGE_NS.getLabel() + "." + Constants.ROOT_PACKAGE + ".Facade"),

  RUNWAY_DTO_META(RUNWAY_NS.getLabel() + ".Meta"),

  /**
   * DTO extend function
   */
  RUNWAY_DTO_CLASS_EXTEND(RUNWAY_DTO_META.getLabel() + ".extend"),

  RUNWAY_CONVERT_MAP_TO_QUERY_STRING(RUNWAY_UTIL.getLabel() + ".convertMapToQueryString"),

  /**
   * DTO class create
   */
  RUNWAY_NEW_CLASS(RUNWAY_DTO_META.getLabel() + ".newClass"),

  RUNWAY_DTO_GET_OBJECT(RUNWAY_UTIL.getLabel() + ".getObject"),

  RUNWAY_META_CLASS_EXISTS(RUNWAY_META.getLabel() + ".classExists"),

  RUNWAY_IS_FUNCTION(RUNWAY_UTIL.getLabel() + ".isFunction"),

  RUNWAY_IS_STRING(RUNWAY_UTIL.getLabel() + ".isString"),

  RUNWAY_IS_OBJECT(RUNWAY_UTIL.getLabel() + ".isObject"),

  RUNWAY_GET_VALUES(RUNWAY_UTIL.getLabel() + ".getValues"),

  /**
   * DTO getJSON function
   */
  RUNWAY_DTO_GET_JSON(RUNWAY_UTIL.getLabel() + ".getJSON"),

  /**
   * DTO build package function
   */
  RUNWAY_DTO_BUILD_PACKAGE(RUNWAY_UTIL.getLabel() + ".buildPackage"),

  RUNWAY_COLLECT_FORM_VALUES(RUNWAY_UTIL.getLabel() + ".collectFormValues"),

  /**
   * DTO Exception (to be thrown/caught in Javascript)
   */
  // RUNWAY_DTO_EXCEPTION(RUNWAY_DTO.getLabel()+".Exception"),

  /*---------- DTO (general) -------------*/

  /**
   * property denoting what type of DTO this is. We require this because the
   * JSON strings come into the controllers type-unsafe. Therefore, we require a
   * string to perform a comparison against to detect what kind of DTO we're
   * dealing with (i.e., BUSINESS_DTO, RELATIONSHIP_DTO, ENUM_DTO)
   */
  DTO_TYPE("dto_type"),

  /* EnumDTO */

  // ENUM_DTO(RUNWAY_DTO.getLabel()+".EnumDTO"),

  ENUM_DTO_ID("id"),

  ENUM_DTO_TYPE("enumType"),

  ENUM_DTO_NAME("enumName"),

  ENUM_DTO_DISPLAY_LABEL("displayLabel"),

  // ENUM_DTO_BUSINESS_DTO("dto"),

  /*----------------- Enumeration getters --------------*/

  /*----------------- EnumerationDTOIF -------*/

  // ENUMERATION_DTO_IF(RUNWAY_DTO.getLabel()+".EnumerationDTOIF"),

  ENUMERATION_DTO_IF_NAME("_name"),

  /*----------------- TypeMd ---------------*/

  TYPE_MD("_typeMd"),

  TYPE_MD_DISPLAY_LABEL("displayLabel"),

  TYPE_MD_DESCRIPTION("description"),

  TYPE_MD_ID("id"),

  /*------------------ RelationshipMd -------------*/

  RELATIONSHIP_MD("_relationshipMd"),

  RELATIONSHIP_MD_PARENT_MD_BUSINESS("parentMdBusiness"),

  RELATIONSHIP_MD_CHILD_MD_BUSINESS("childMdBusiness"),

  /*------------------- EntityDTO ----------------*/

  ENTITY_DTO_TO_STRING("_toString"),

  /**
     * 
     */
  ENTITY_DTO_ID("id"),

  /**
     * 
     */
  COMPONENT_DTO_TYPE("_type"),

  /**
     * 
     */
  ENTITY_DTO_READABLE("readable"),

  /**
     * 
     */
  ENTITY_DTO_WRITABLE("writable"),

  /**
     * 
     */
  ENTITY_DTO_NEW_INSTANCE("newInstance"),

  /**
     * 
     */
  ENTITY_DTO_MODIFIED("modified"),

  /**
     * 
     */
  ENTITY_DTO_LOCKED_BY_CURRENT_USER("lockedByCurrentUser"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_MAP("attributeMap"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE("attribute"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_NAME("attributeName"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_TYPE("type"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_DTO_TYPE("dtoType"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_VALUE("value"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_READABLE("readable"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_WRITABLE("writable"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_MODIFIED("modified"),

  /**
     * 
     */
  ENTITY_DTO_ATTRIBUTE_ATTRIBUTE_MD_DTO("attributeMdDTO"),

  /*------------------- BusinessDTO ------------------*/

  /**
   * 
   BUSINESS_DTO(RUNWAY_DTO.getLabel()+".BusinessDTO"),
   */

  /**
     * 
     */
  BUSINESS_DTO_STATE("state"),

  BUSINESS_DTO_TRANSITIONS("transitions"),

  /*------------------- RelationshipDTO --------------*/

  /**
   * 
   RELATIONSHIP_DTO(RUNWAY_DTO.getLabel()+".RelationshipDTO"),
   */

  /**
     * 
     */
  RELATIONSHIP_DTO_PARENT_ID("parentId"),

  /**
     * 
     */
  RELATIONSHIP_DTO_CHILD_ID("childId"),

  /*------------------- StructDTO -------------------*/

  // STRUCT_DTO(RUNWAY_DTO.getLabel()+".StructDTO"),

  /*------------------- ElementDTO -----------------*/

  // ELEMENT_DTO(RUNWAY_DTO.getLabel()+".ElementDTO"),

  /*------------------- MutableDTO -----------------*/

  // MUTABLE_DTO(RUNWAY_DTO.getLabel()+".MutableDTO"),

  /*------------------- Attribute Metadata -----------*/

  ATTRIBUTE_METADATA_DISPLAY_LABEL("displayLabel"),

  ATTRIBUTE_METADATA_DESCRIPTION("description"),

  ATTRIBUTE_METADATA_IMMUTABLE("immutable"),

  ATTRIBUTE_METADATA_REQUIRED("required"),

  ATTRIBUTE_METADATA_ID("id"),

  ATTRIBUTE_METADATA_NAME("name"),

  ATTRIBUTE_METADATA_SYSTEM("system"),

  /*------------------- Attribute Number ----*/

  NUMBER_METADATA_REJECT_ZERO("_rejectZero"),

  NUMBER_METADATA_REJECT_NEGATIVE("_rejectNegative"),

  NUMBER_METADATA_REJECT_POSITIVE("_rejectPositive"),

  /*------------------- Attribute Dec -------*/

  DEC_METADATA_TOTAL_LENGTH("totalLength"),

  DEC_METADATA_DECIMAL_LENGTH("decimalLength"),

  /*------------------- Attribute Enumeration -------*/

  ENUMERATION_METADATA_SELECT_MULTIPLE("_selectMultiple"),

  ENUMERATION_METADATA_REFERENCED_MD_ENUMERATION("referencedMdEnumeration"),

  ENUMERATION_METADATA_ENUM_NAMES("enumNames"),

  ENUMERATION_METADATA_ENUM_ITEM("item"),

  ENUMERATION_METADATA_ENUM_ID("enumId"),

  ENUMERATION_ENUM_NAMES("enumNames"),

  /*------------------- Attribute Multi reference ----*/

  MULTI_REFERENCE_ITEM_IDS("itemIds"),

  /*------------------- Attribute Struct ----*/

  /**
   * The struct object on an attribute struct.
   */
  STRUCT_STRUCT_DTO("structDTO"),

  STRUCT_METADATA_DEFINING_MDSTRUCT("definingMdStruct"),

  STRUCT_ID("id"),

  /*------------------- Attribute Character ----*/

  CHARACTER_METADATA_SIZE("size"),

  /*------------------- Attribute Boolean ------*/

  BOOLEAN_POSITIVE_DISPLAY_LABEL("positiveDisplayLabel"),

  BOOLEAN_NEGATIVE_DISPLAY_LABEL("negativeDisplayLabel"),

  /*------------------- Attribute Encryption ----*/

  ENCRYPTION_METADATA_ENCRYPTION_METHOD("encryptionMethod"),

  /*-------------------- Attribute Reference -----------*/

  REFERENCE_METADATA_REFERENCED_MD_BUSINESS("referencedMdBusiness"),

  /*--------------------- MethodMetadata ---------------------*/

  METHOD_METADATA_CLASSNAME("className"),

  METHOD_METADATA_METHODNAME("methodName"),

  METHOD_METADATA_DECLARED_TYPES("declaredTypes"),

  /*---------------- ComponentQueryDTO ----------------*/
  COMPONENT_QUERY_DTO_GROOVY_QUERY("groovyQuery"),

  /*----------------- ClassQueryDTO -----------------*/

  CLASS_QUERY_DTO_QUERY_TYPE("queryType"),

  CLASS_QUERY_DTO_PAGE_NUMBER("pageNumber"),

  CLASS_QUERY_DTO_PAGE_SIZE("pageSize"),

  CLASS_QUERY_DTO_COUNT("count"),

  CLASS_QUERY_DTO_COUNT_ENABLED("countEnabled"),

  CLASS_QUERY_DTO_RESULT_SET("resultSet"),

  CLASS_QUERY_DTO_DEFINED_ATTRIBUTES("definedAttributes"),

  // CLASS_QUERY_DTO(RUNWAY_DTO.getLabel()+".ClassQueryDTO"),

  CLASS_QUERY_DTO_CLASSES("classes"),

  /**
   * A class element whose children contain data to represent the class's
   * supertypes.
   * 
   * 
   */
  CLASS_QUERY_DTO_CLASS("class"),

  CLASS_QUERY_DTO_CLASS_TYPE("classType"),

  CLASS_QUERY_DTO_CLASS_SUPERCLASSES("superClasses"),

  CLASS_QUERY_DTO_CLASS_SUPERCLASS("superClass"),

  /*----------------- EntityQueryDTO ---------------*/

  // ENTITY_QUERY_DTO(RUNWAY_DTO.getLabel()+".EntityQueryDTO"),

  ENTITY_QUERY_DTO_CONDITIONS("conditions"),

  ENTITY_QUERY_DTO_CONDITION_ATTRIBUTE("attribute"),

  ENTITY_QUERY_DTO_CONDITION_CONDITION("condition"),

  ENTITY_QUERY_DTO_CONDITION_VALUE("value"),

  ENTITY_QUERY_DTO_ORDER_BY_LIST("orderByList"),

  ENTITY_QUERY_DTO_ORDER_BY_ATTRIBUTE("attribute"),

  ENTITY_QUERY_DTO_ORDER_BY_ORDER("order"),

  /*----------------- ElementQueryDTO ---------------*/

  // ELEMENT_QUERY_DTO(RUNWAY_DTO.getLabel()+".ElementQueryDTO"),

  ELEMENT_QUERY_DTO_ISABSTRACT("_isAbstract"),

  ELEMENT_QUERY_DTO_ORDER_BY_ATTRIBUTE_STRUCT("attributeStruct"),

  /*----------------- StructQueryDTO -----------------*/

  // STRUCT_QUERY_DTO(RUNWAY_DTO.getLabel()+".StructQueryDTO"),

  /*----------------- BusinessQueryDTO --------------*/

  // BUSINESS_QUERY_DTO(RUNWAY_DTO.getLabel()+".BusinessQueryDTO"),

  BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_PARENT("typeInMdRelationshipAsParentList"),

  BUSINESS_QUERY_DTO_TYPE_IN_REL_PARENT_DISPLAY_LABEL("parentDisplayLabel"),

  BUSINESS_QUERY_DTO_TYPE_IN_REL_RELATIONSHIP_TYPE("relationshipType"),

  BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_CHILD("typeInMdRelationshipAsChildList"),

  BUSINSS_QUERY_DTO_TYPE_IN_REL_CHILD_DISPLAY_LABEL("childDisplayLabel"),

  /*----------------- RelationshipQueryDTO ------------*/

  // RELATIONSHIP_QUERY_DTO(RUNWAY_DTO.getLabel()+".RelationshipQueryDTO"),

  RELATIONSHIP_QUERY_DTO_PARENT_MDBUSINESS("parentMdBusiness"),

  RELATIONSHIP_QUERY_DTO_CHILD_MDBUSINESS("childMdBusiness"),

  /*----------------- ViewQueryDTO -----------------*/

  // VIEW_QUERY_DTO(RUNWAY_DTO.getLabel()+".ViewQueryDTO"),

  /*----------------- ValueQueryDTO --------------------*/

  // VALUE_QUERY_DTO(RUNWAY_DTO.getLabel()+".ValueQueryDTO"),

  /*----------------- ValueObjectDTO ------------------*/

  // VALUE_OBJECT_DTO(RUNWAY_DTO.getLabel()+".ValueObjectDTO"),

  /*----------------- UtilDTO ---------------------*/

  // UTIL_DTO(RUNWAY_DTO.getLabel()+".UtilDTO"),

  /*------- Exception (generic properties) -----*/

  EXCEPTION_LOCALIZED_MESSAGE("localizedMessage"),

  EXCEPTION_DEVELOPER_MESSAGE("developerMessage"),

  /*----------------- ExceptionDTO ------------*/

  // RUNWAY_EXCEPTION_DTO(RUNWAY_DTO.getLabel()+".RunwayExceptionDTO"),

  EXCEPTION_DTO_WRAPPED_EXCEPTION("wrappedException"),

  /*----------------- ProblemExceptionDTO -------*/

  // PROBLEM_EXCEPTION_DTO(RUNWAY_DTO.getLabel()+".ProblemExceptionDTO"),

  PROBLEM_EXCEPTION_DTO_PROBLEM_LIST("problemList"),

  /*----------------- ProblemDTO ---------------*/

  // PROBLEM_DTO(RUNWAY_DTO.getLabel()+".ProblemDTO"),

  /*----------------- RunwayProblemDTO -----------*/

  // RUNWAY_PROBLEM_DTO(RUNWAY_DTO.getLabel()+".RunwayProblemDTO"),

  /*----------------- MessageDTO ----------------*/

  // MESSAGE_DTO(RUNWAY_DTO.getLabel()+".MessageDTO"),

  MESSAGE_DTO_LOCALIZED_MESSAGE("localizedMessage"),

  /*----------------- WarningDTO -----------------*/

  // WARNING_DTO(RUNWAY_DTO.getLabel()+".WarningDTO"),

  /*----------------- InformationDTO ---------------*/

  // INFORMATION_DTO(RUNWAY_DTO.getLabel()+".InformationDTO"),

  /*----------------- AttributeProblemDTO ------------*/

  // ATTRIBUTE_PROBLEM_DTO(RUNWAY_DTO.getLabel()+".AttributeProblemDTO"),

  ATTRIBUTE_PROBLEM_DTO_COMPONENT_ID("componentId"),

  ATTRIBUTE_PROBLEM_DTO_DEFINING_TYPE("definingType"),

  ATTRIBUTE_PROBLEM_DTO_DEFINING_TYPE_DISPLAY_LABEL("definingTypeDisplayLabel"),

  ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_NAME("attributeName"),

  ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_ID("attributeId"),

  ATTRIBUTE_PROBLEM_DTO_ATTRIBUTE_DISPLAY_LABEL("attributeDisplayLabel"),

  LOCALIZED_VALE("localizedValue");

  /*----------------- EmptyValueProblemDTO -----------------*/

  // EMPTY_VALUE_PROBLEM_DTO(RUNWAY_DTO.getLabel()+".EmptyValueProblemDTO"),

  /*----------------- ImmutableAttributeProblemDTO -----------------*/

  // IMMUTABLE_ATTRIBUTE_PROBLEM_DTO(RUNWAY_DTO.getLabel()+".ImmutableAttributeProblemDTO"),

  /*----------------- SystemAttributeProblemDTO -----------------*/

  // SYSTEM_ATTRIBUTE_PROBLEM_DTO(RUNWAY_DTO.getLabel()+".SystemAttributeProblemDTO"),

  /*----------------- SmartExceptionDTO -------*/

  // SMART_EXCEPTION_DTO(RUNWAY_DTO.getLabel()+".SmartExceptionDTO"),

  /*----------------- ViewDTO ----------------*/

  // VIEW_DTO(RUNWAY_DTO.getLabel()+".ViewDTO");

  /**
   * The string label representing an element.
   */
  private String label;

  /**
   * Constructor
   * 
   * @param label
   */
  private JSON(String label)
  {
    this.label = label;
  }

  /**
   * Returns the label of an element.
   * 
   * @return String representation of an element.
   */
  public String getLabel()
  {
    return label;
  }

  public String toString()
  {
    return label;
  }

  public static String createControllerNotifyListenerCall(String qualifiedAction)
  {
    String baseAction = qualifiedAction.replaceFirst("\\" + MdActionInfo.ACTION_SUFFIX + "$", "");
    int ind = baseAction.lastIndexOf(".");
    String base = baseAction.substring(0, ind);
    String action = baseAction.substring(ind + 1);
    String upperActionName = action.substring(0, 1).toUpperCase() + action.substring(1);

    return RUNWAY_PACKAGE_NS.getLabel() + "." + base + "._notify" + upperActionName + "Listener";
  }
}
