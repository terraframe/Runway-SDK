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
package com.runwaysdk.transport.conversion.dom;

public enum Elements
{
    RUNWAY_WEBSERVICE("RunwayWebService"),

    /*----------------- Primitive ------------------*/

    /**
     * Primitive root element
     */
    PRIMITIVE("primitive"),

    /**
     * The type of the primitive
     */
    PRIMITIVE_TYPE("type"),

    /**
     * The value of a primitive.
     */
    PRIMITIVE_VALUE("value"),

    /*------------------- ComponentDTO ----------------*/

    PROPERTIES("properties"),

    /**
     *
     */
    COMPONENT_ATTRIBUTE_LIST("attributeList"),

    /*------------------- EntityDTO ----------------*/

    /**
     *
     */
    TYPE_MD("metadata"),

    TYPE_MD_DISPLAY_LABEL("displayLabel", Elements.TYPE_MD.getXpath()+"/"),

    TYPE_MD_DESCRIPTION("description", Elements.TYPE_MD.getXpath()+"/"),

    TYPE_MD_ID("id", Elements.TYPE_MD.getXpath()+"/"),

    RELATIONSHIP_MD_PARENT_MD_BUSINESS("parentMdBusiness",  Elements.TYPE_MD.getXpath()+"/"),

    RELATIONSHIP_MD_CHILD_MD_BUSINESS("childMdBusiness",  Elements.TYPE_MD.getXpath()+"/"),

    /**
     *
     */
    ENTITY_ID("id"),

    /**
     *
     */
    COMPONENT_TYPE("type", Elements.PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    COMPONENT_NEW_INSTANCE("newInstance", Elements.PROPERTIES.getXpath()+"/"),


    /**
     *
     */
    COMPONENT_READABLE("readable", Elements.PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    COMPONENT_WRITABLE("writable", Elements.PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    COMPONENT_MODIFIED("modified", Elements.PROPERTIES.getXpath()+"/"),

    MUTABLE_DTO_TOSTRING("toString", Elements.PROPERTIES.getXpath()+"/"),

    /**
     * Only for BusinessDTO and RelationshipDTO
     */
    ENTITY_LOCKED_BY_CURRENT_USER("lockedByCurrentUser", Elements.PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    ATTRIBUTE_PROPERTIES("properties"),

    /**
     *
     */
    ATTRIBUTE_NAME("name", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    ATTRIBUTE_TYPE("type", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    ATTRIBUTE_VALUE("value", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    ATTRIBUTE_READABLE("readable", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    ATTRIBUTE_WRITABLE("writable", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    ATTRIBUTE_MODIFIED("modified", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    /*------------------- BusinessDTO ------------------*/

    /**
     *
     */
    BUSINESS_DTO("businessDTO"),

    /**
     *
     */
    BUSINESS_STATE("state", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    BUSINESS_TRANSITIONS("transitions", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    BUSINESS_TRANSITION("transition", Elements.BUSINESS_TRANSITIONS.getXpath()+"/"),

    /*------------------- RelationshipDTO --------------*/

    /**
     *
     */
    RELATIONSHIP_DTO("relationshipDTO"),

    /**
     *
     */
    RELATIONSHIP_PARENT_ID("parentId", Elements.PROPERTIES.getXpath()+"/"),

    /**
     *
     */
    RELATIONSHIP_CHILD_ID("childId", Elements.PROPERTIES.getXpath()+"/"),

    /*------------------- StructDTO --------------*/

    STRUCT_DTO("structDTO"),

    /*------------------- UtilDTO --------------*/

    UTIL_DTO("utilDTO"),

    /*------------------- ExceptionDTO ------------------*/
    /**
     *
     */
    EXCEPTION_DTO("exceptionDTO"),

    EXCEPTION_LOCALIZED_MESSAGE("localizedMessage"),

    EXCEPTION_DEVELOPER_MESSAGE("developerMessage"),

    /*------------------- ViewDTO --------------*/

    VIEW_DTO("viewDTO"),

    /*------------------- MessageDTO --------------*/
    MESSAGE_LOCALIZED_MESSAGE("localizedMessage"),

    /*------------------- WarningDTO --------------*/

    WARNING_DTO("warningDTO"),

    /*------------------- InformationDTO --------------*/

    INFORMATION_DTO("informationDTO"),

    /*------------------- MessageExceptionDTO --------------*/

    RETURN_OBJECT("returnObject"),

    MESSAGEEXCEPTION_DTO("messageExceptionDTO"),

    MESSAGE_DTO("messageDTO"),

    /*------------------- ProblemExceptionDTO --------------*/

    PROBLEMEXCEPTION_DTO("problemExceptionDTO"),

    PROBLEMEXCEPTION_LOCALIZED_MESSAGE("localizedMessage"),

    /*------------------- ProblemDTO --------------*/

    PROBLEM_DTO("problemDTO"),

    /*------------------- SmartProblemDTO --------------*/

    SMART_PROBLEM_DTO("smartProblemDTO"),

    SMART_PROBLEM_LOCALIZED_MESSAGE("localizedMessage"),

    SMART_PROBLEM_DEVELOPER_MESSAGE("developerMessage"),

    /*------------------- RunwayProblemDTO --------------*/

    RUNWAYPROBLEM_TYPE("type"),

    RUNWAYPROBLEM_LOCALIZED_MESSAGE("localizedMessage"),

    RUNWAYPROBLEM_DEVELOPER_MESSAGE("developerMessage"),

    /*------------------- AttributeProblemDTO --------------*/

    ATTRIBUTEPROBLEM_DTO("attributeProblemDTO"),

    ATTRIBUTEPROBLEM_COMPONENT_ID("componentId"),

    ATTRIBUTEPROBLEM_DEFINING_TYPE("definingType"),

    ATTRIBUTEPROBLEM_DEFINING_TYPE_DISPLAY_LABEL("definingTypeDisplayLabel"),

    ATTRIBUTEPROBLEM_ATTRIBUTE_NAME("attributeName"),
    
    ATTRIBUTEPROBLEM_ATTRIBUTE_ID("attributeId"),

    ATTRIBUTEPROBLEM_ATTRIBUTE_DISPLAYLABEL("attributeDisplayLabel"),

    /*------------------- ExcelProblemDTO --------------*/

    EXCELPROBLEM_DTO("excelProblemDTO"),

    EXCELPROBLEM_ROWNUMBER("rowNumber"),

    EXCELPROBLEM_COLUMN("column"),

    /*------------------- RunwayExceptionDTO --------------*/

    RUNWAYEXCEPTION_DTO("runwayExceptionDTO"),

    RUNWAYEXCEPTION_DTO_TYPE("type"),

    RUNWAYEXCEPTION_LOCALIZED_MESSAGE("localizedMessage"),

    RUNWAYEXCEPTION_DEVELOPER_MESSAGE("developerMessage"),


    /*------------------- ValueObjectDTO --------------*/
    VALUE_DTO("valueObjectDTO"),

    /*------------------- QueryDTO --------------*/
    GROOVY_QUERY_STRING("groovyQuery"),

    JSON_QUERY_STRING("jsonQuery"),

    QUERY_DTO_DEFINED_ATTRIBUTES("definedAttributes"),

    QUERY_DTO_CLASSES("classes"),

    QUERY_DTO_CONDITION_LIST("conditionList"),

    QUERY_DTO_PAGE_NUMBER("pageNumber"),

    QUERY_DTO_PAGE_SIZE("pageSize"),

    QUERY_DTO_COUNT("count"),

    QUERY_DTO_COUNT_ENABLED("countEnabled"),

    QUERY_DTO_RESULT_SET("resultSet"),

    /*--------------- EntityQueryDTO ------------*/

    QUERY_DTO_TYPE("queryType"),

    /*--------------- ElementQueryDTO ------------*/

    ELEMENT_QUERY_DTO_ISABSTRACT("isAbstract"),

    /*---------------- BusinessQueryDTO -----------------------*/

    BUSINESS_QUERY_DTO("businessQueryDTO"),

    BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_PARENT("typeInMdRelationshipAsParent"),

    BUSINESS_QUERY_DTO_TYPE_IN_REL_PARENT_DISPLAY_LABEL("parentDisplayLabel"),

    BUSINESS_QUERY_DTO_TYPE_IN_REL_INSTANCE("typeInInstance"),

    BUSINESS_QUERY_DTO_TYPE_IN_RELATIONSHIP_TYPE("relationshipType"),

    BUSINESS_QUERY_DTO_TYPE_IN_REL_AS_CHILD("typeInMdRelationshipAsChild"),

    BUSINESS_QUERY_DTO_TYPE_IN_REL_CHILD_DISPLAY_LABEL("childDisplayLabel"),

    /*--------------- MdRelationshipQueryDO -------------------------*/

    RELATIONSHIP_QUERY_DTO("relationshipQueryDTO"),

    RELATIONSHIP_QUERY_DTO_PARENT_MD_BUSINESS("parentMdBusiness"),

    RELATIONSHIP_QUERY_DTO_CHILD_MD_BUSINESS("childMdBusiness"),

    /*--------------- MdStructQueryDO -------------------------*/

    STRUCT_QUERY_DTO("structQueryDTO"),

    /*--------------- MdViewQueryDO -------------------------*/

    VIEW_QUERY_DTO("viewQueryDTO"),

    /*--------------- ViewQueryDTO ------------*/

    VIEW_QUERY_DTO_ISABSTRACT("isAbstract"),


    /*--------------- ValueQueryDO -------------------------*/

    VALUE_QUERY_DTO("valueQueryDTO"),

    /**
     * A class element whose children contain
     * data to represent the class's supertypes.
     */
    QUERY_DTO_CLASS("class"),

    QUERY_DTO_CLASS_TYPE("type"),

    QUERY_DTO_CLASS_SUPERCLASSES("superClasses"),

    QUERY_DTO_CLASS_SUPERCLASS("superClass"),

    /**
     * A condition element whose children contain
     * data for a single query condition.
     */
    QUERY_DTO_CONDITION("condition"),

    QUERY_DTO_CONDITION_ATTRIBUTE("attribute"),

    QUERY_DTO_CONDITION_TYPE("type"),

    QUERY_DTO_CONDITION_VALUE("value"),

    /*-------------------- List -----------------*/

    /**
     *
     */
    ARRAY("array"),

    /**
     *
     */
    ARRAY_TYPE_ATTRIBUTE("type"),

    /**
     *
     */
    ARRAY_ITEM("item"),

    /*------------------- Attribute Metadata -----------*/
    ATTRIBUTE_METADATA("metadata"),

    ATTRIBUTE_METADATA_DISPLAY_LABEL("displayLabel", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ATTRIBUTE_METADATA_DESCRIPTION("description", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ATTRIBUTE_METADATA_REQUIRED("required", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ATTRIBUTE_METADATA_IMMUTABLE("immutable", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ATTRIBUTE_METADATA_ID("id", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),
    
    ATTRIBUTE_METADATA_GENERATE_ACCESSOR("generateAccessor", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ATTRIBUTE_METADATA_SYSTEM("system", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ATTRIBUTE_METADATA_NAME("name", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    /*------------------- Attribute Number ----*/

    NUMBER_METADATA_REJECT_ZERO("rejectZero", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    NUMBER_METADATA_REJECT_NEGATIVE("rejectNegative", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    NUMBER_METADATA_REJECT_POSITIVE("rejectPositive", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    /*------------------- Attribute Dec -------*/

    DEC_METADATA_TOTAL_LENGTH("totalLength", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    DEC_METADATA_DECIMAL_LENGTH("decimalLength", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    /*------------------- Attribute Enumeration -------*/

    ENUMERATION_METADATA_SELECT_MULTIPLE("selectMultiple", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ENUMERATION_METADATA_REFERENCED_MD_ENUMERATION("referenceMdEnumeration", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ENUMERATION_METADATA_ENUM_ITEMS("enumItems", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    ENUMERATION_METADATA_ENUM_ITEM("enumItem", Elements.ENUMERATION_METADATA_ENUM_ITEMS.getXpath()+"/"),

    ENUMERATION_METADATA_ENUM_NAME("name", Elements.ENUMERATION_METADATA_ENUM_ITEM.getXpath()+"/"),

    ENUMERATION_METADATA_ENUM_DISPLAY_LABEL("displayLabel", Elements.ENUMERATION_METADATA_ENUM_ITEM.getXpath()+"/"),

    ENUMERATION_ENUM_ITEMS("enumItems", Elements.ATTRIBUTE_PROPERTIES.getXpath()+"/"),

    ENUMERATION_ENUM_NAME("enumName", Elements.ENUMERATION_ENUM_ITEMS.getXpath()+"/"),

    /*------------------- Attribute Struct ----*/

    STRUCT_STRUCT_DTO(STRUCT_DTO.getLabel()),

    STRUCT_METADATA_DEFINING_MDSTRUCT("definingMdStruct", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),


    /*------------------- Attribute Character ----*/

    CHARACTER_METADATA_SIZE("size", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),


    /*------------------- Attribute Boolean ----*/

    BOOLEAN_METADATA_POSITIVE("positiveDisplayLabel", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    /*------------------- Attribute Boolean ----*/

    BOOLEAN_METADATA_NEGATIVE("negativeDisplayLabel", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    /*------------------- Attribute Encryption ----*/

    ENCRYPTION_METADATA_ENCRYPTION_METHOD("encryptionMethod", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    /*------------------- Attribute Reference -------*/

    REFERENCE_METADATA_REFERENCED_MD_BUSINESS("referencedMdBusiness", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),
    
    REFERENCE_METADATA_REFERENCED_LABEL("referencedDisplayLabel", Elements.ATTRIBUTE_METADATA.getXpath()+"/"),

    /*------------------MethodMetaData-------------*/

    /**
     * Root tag of the MethodMetaData Document
     */
    METHOD_METADATA("methodMetadata"),

    /**
     * Name of the className tag for the MethodMetaData Document
     */
    METHOD_CLASS_NAME("className"),

    /**
     * Name of the methodName tag for the MethodMetaData Document
     */
    METHOD_NAME("methodName"),

    /**
     * Name of the declaredTypes tag for the MethodMetaData Document
     */
    METHOD_DECLARE_TYPES("declaredTypes"),

    /**
     * Name of the actualTypes tag for the MethodMetaData Document
     */
    METHOD_ACTUAL_TYPES("actualTypes"),

    /**
     * Name of the Type tag for MethodMetaDataDocument
     */
    METHOD_TYPE("type"),

    /*------------------ EnumDTO ------------------*/

    /**
     * Root tag of the enumDTO document
     */
    ENUM_DTO("enumDTO"),

    /**
     * Name of the enumName tag
     */
    ENUM_DTO_NAME("enumName"),

    /**
     * Name of the enumType tag
     */
    ENUM_DTO_TYPE("enumType"),

    /**
     * Name of the enumValues tags
     */

    /*----------------- Date ---------------------*/

    /**
     * Root tag of date document
     */
    DATE("date"),

    /**
     * name of the long representation of the date
     */
    DATE_LONG("dateLong"),

    /*------------------ Null ---------------------*/

    /**
     * Null root element
     */
    NULL("null");


    /**
     * The string label representing an element.
     */
    private String label;

    /**
     * xPath to the node.
     */
    private String path;

    /**
     * Constructor
     * @param label
     */
    private Elements(String label)
    {
      this.label = label;
      this.path = "";
    }

    /**
     * Constructor
     * @param label
     * @param path
     */
    private Elements(String label, String path)
    {
      this.label = label;
      this.path = path;
    }

    /**
     * Returns the label of an element.
     * @return String representation of an element.
     */
    public String getLabel()
    {
      return label;
    }

    /**
     * Returns the xPath of an element.
     * @return String representation of an element.
     */
    public String getXpath()
    {
      return this.path+this.label;
    }
}
