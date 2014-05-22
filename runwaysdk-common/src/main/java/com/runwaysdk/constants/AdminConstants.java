/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.constants;

import com.runwaysdk.web.view.html.EscapeUtil;

/**
 * An enum of items that are used to generate HTML and javascript.
 */
public enum AdminConstants
{
  NEW_INSTANCE("newInstance"),
  
  READ("read"),
  
  UPDATE("update"),
  
  LOCK("lock"),
  
  UNLOCK("unlock"),
  
  CREATE("create"),
  
  NODE_SEARCH_RESULTS("nodeSearchResults"),
  
  REFRESH_NODE_SEARCH_RESULTS__SEARCH("refreshNodeSearchResults_search"),
  
  REFRESH_NODE_SEARCH_RESULTS__GROOVY("refreshNodeSearchResults_groovy"),
  
  REFRESH_NODE_SEARCH_RESULTS("refreshNodeSearchResults"),
  
  CHILD_SEARCH_RESULTS("childSearchResults"),
  
  REFRESH_CHILD_SEARCH_RESULTS__SEARCH("refreshChildSearchResults_search"),
  
  REFRESH_CHILD_SEARCH_RESULTS__GROOVY("refreshChildSearchResults_groovy"),
  
  REFRESH_CHILD_SEARCH_RESULTS("refreshChildSearchResults"),
  
  SELECT_NODE("selectNode"),
  
  REFRESH_PARENT_SEARCH_RESULTS__SEARCH("refreshParentSearchResults_search"),
  
  REFRESH_PARENT_SEARCH_RESULTS__GROOVY("refreshParentSearchResults_groovy"),
  
  REFRESH_PARENT_SEARCH_RESULTS("refreshParentSearchResults"),
  
  PARENT_SEARCH_RESULTS("parentSearchResults"),
  
  ADD_CHILD("addChild"),
  
  ADD_PARENT("addParent"),
  
  READ_NODE("readNode"),
  
  CLEAR_NODE("clearNode"),
  
  DELETE_CHILD("deleteChild"),
  
  DELETE_PARENT("deleteParent"),
  
  DELETE("remove"),
  
  REFRESH_VIEW_ALL_RESULTS("refreshViewAllResults"),
  
  REFRESH_VIEW_ALL_RESULTS__SEARCH("refreshViewAllResults_search"),
  
  REFRESH_VIEW_ALL_RESULTS__GROOVY("refreshViewAllResults_groovy"),
 
  MINIMIZE_WINDOW("minimizeWindow"),
  
  MAXIMIZE_WINDOW("maximizeWindow"),
  
  CLOSE_WINDOW("closeWindow"),
  
  REFRESH_REFERENCE_SEARCH_RESULTS("refreshReferenceSearchResults"),
  
  REFRESH_REFERENCE_SEARCH_RESULTS__SEARCH("refreshReferenceSearchResults_search"),
  
  REFRESH_REFERENCE_SEARCH_RESULTS__GROOVY("refreshReferenceSearchResults_groovy"),
  
  REFERENCE_SEARCH_RESULTS("referenceSearchResults"),
  
  REFRESH_VALUE_QUERY_RESULTS("refreshValueQueryResults"),
  
  REFRESH_VALUE_QUERY_RESULTS__GROOVY("refreshValueQueryResults_groovy"),
  
  SELECT_REFERENCE("selectReference"),
  
  VIEW_REFERENCE("viewReference"),
 
  ADD_SUPERTYPES_TO_TYPE_CACHE("superTypes.addSuperTypes"),
  
  ADD_TO_WINDOW_CACHE("WindowCache.add"),
  
  OPEN_FILE_UPLOAD("openFileUpload"),
  
  DOWNLOAD_SECURE_FILE("downloadSecureFile"),
  
  CLEAR_REFERENCE("clearReference"),
  
  SET_GRID("Grids.set"),
  
  UNSET_GRID("Grids.unset"),
  
  VIEW_GRID_TEXT("viewGridText"),
  
  GET_TREEGRID("TreeGrids.get"),
  
  REPLACE_TREEGRID("TreeGrids.replace"),
  
  UNSET_TREEGRID("TreeGrids.unset"),
  
  SET_TABBAR("Tabbars.set"),
  
  SELECT_RELATIONSHIP_TYPE("selectRelationshipType"),
  
  GET_RELATIONSHIP_NODES("getRelationshipNodes"),
  
  PROMOTE("promote"),
  
  TOGGLE_SEARCH("toggleSearch"),
  
  INIT_DATE_FIELDS("initDateFields"),
  
  INIT_TIME_FIELDS("initTimeFields"),
  
  INIT_DATETIME_FIELDS("initDateTimeFields"),
  
  CREATE_CALENDAR_FOR_DATE_TIME("createCalendarForDateTime"),
  
  CREATE_CALENDAR_FOR_DATE("createCalendarForDate"),
  
  UPDATE_TIME_FIELD("updateTimeField"),
  
  UPDATE_DATE_FIELD("updateDateField"),
  
  UPDATE_DATE_TIME_FIELD("updateDateTimeField"),
  
  CLEAR_FILE("clearFile"),
  
  ADD_ADVANCED_SEARCH_CRITERIA("addAdvancedSearchCriteria");
  
  /**
   * The path to the images for the admin page.
   */
  public static final String IMAGE_PATH = "images/";
  
  /**
   * The javascript admin package.
   */
  public static final String ADMIN_PACKAGE = JSON.RUNWAY_NS.getLabel() + "." + "Admin";

  /**
   * A page when viewing all instances of a type.
   */
  public static final String PAGE_CLASS = "page";
  
  /**
   * The current page when viewing all instances of a type.
   */
  public static final String CURRENT_PAGE_CLASS = "currentPage";
  
  /**
   * The outer component div class in a dialog box.
   */
  public static final String WINDOW_DIV_CLASS = "window";
  
  /**
   * The class of the div that wraps a text attribute.
   */
  public static final String TEXT_ATTRIBUTE_DIV_CLASS = "textAttributeDiv";
  
  /**
   * The div to wrap the close button
   */
  public static final String CLOSE_DIV_CLASS = "closeDiv";
  
  /**
   * The div to wrap the minimize button.
   */
  public static final String MINIMIZE_DIV_CLASS = "minimizeDiv";
  
  /**
   * The content div class.
   */
  public static final String WINDOW_CONTENT_CLASS = "winContent";
  
  /**
   * The content div id suffix.
   */
  public static final String WINDOW_CONTENT_SUFFIX = "_winContent";
  
  /**
   * The class name of the scroll handle in a dialog box.
   */
  public static final String WINDOW_DRAG_CLASS = "winDrag";
  
  /**
   * The id suffix for the div that contains the draggable area.
   */
  public static final String WINDOW_DRAG_SUFFIX = "_winDrag";

  /**
   * Suffix appended to the id of a new instance (the id is the type).
   */
  public static final String NEW_INSTANCE_SUFFIX = "_newInstance";
  
  /**
   * The suffix for a div (used as the div id)
   */
  public static final String DIV_SUFFIX = "_div";
  
  /**
   * The table id suffix that holds instances in view all mode.
   */
  public static final String TABLE_SUFFIX = "_table";
  
  /**
   * The id suffix to denote a div that is for searching for a reference.
   */
  public static final String OPEN_REFERENCE_SEARCH_DIV_SUFFIX = "_openReferenceSearch";
  
  /**
   * The id suffix to denote a div that contains reference search results.
   */
  public static final String REFERENCE_SEARCH_DIV_SUFFIX = "_referenceSearch";
  
  /**
   * The suffix appended to each search field. For example, a field to search for attribute
   * 'author' will be named 'author_searchField'.
   */
  public static final String SEARCH_FIELD_SUFFIX = "_searchField";
  
  public static final String ADVANCED_ATTRIBUTES_CLASS = "advancedAttributes";
  
  public static final String ADVANCED_CRITERIA_CLASS = "advancedCriteria";
  
  public static final String ADVANCED_SEARCH_ID_SUFFIX = "_advancedSearch";
  
  /**
   * The hidden field name that stores the id of the object that will reference a reference object.
   */
  public static final String REFERENCING_OBJECT_ID = "referencingObjectId";
  
  /**
   * The hidden field name that stores the id of the input whose value is the reference object's id.
   */
  public static final String REFERENCING_HIDDEN_REF_ID = "referencingHiddenRefId";
  
  /**
   * Suffix for the reference/file clear button.
   */
  public static final String CLEAR_SUFFIX = "_clear";
  
  /**
   * Suffix for the reference/file change button.
   */
  public static final String CHANGE_SUFFIX = "_change";
  
  /**
   * Suffix for the reference/file view button.
   */
  public static final String VIEW_SUFFIX = "_view";
  
  /**
   * The class for tr tags that contain sortable attributes.
   */
  public static final String SORTABLE_CLASS = "sortable";
  
  /**
   * The name of the input field on the file-upload form
   * that references the attribute file field (from the opening window).
   */
  public static final String UPLOAD_FILE_ATTRIBUTE_ID = "attributeFileReference";
  
  /**
   * The name of the input to upload a file.
   */
  public static final String UPLOAD_FILE_FIELD = "uploadFile";
  
  /**
   * Class for any clickable elements.
   */
  public static final String CLICKABLE_CLASS = "clickable";
  
  public static final String LINK_CLASS = "link";
  
  /**
   * The url query parameter that specifies the file id for a file download.
   */
  public static final String FILE_DOWNLOAD_ID = "id";
  
  /**
   * The name of the servlet request attribute that specifies the file id of
   * an uploaded file.
   */
  public static final String SERVLET_ATTRIBUTE_FILE_ID = AdminConstants.class.getName()+".file_id";
  
  /**
   * The name of the sevlet request attribute that specifies the id of the element
   * that contains the id of the uploaded file (the back reference).
   */
  public static final String SERVLET_ATTRIBUTE_FILE_REFERENCE = AdminConstants.class.getName()+".file_attribiteFileReference";
  
  /**
   * The id suffix for an upload dialog div.
   */
  public static final String UPLOAD_DIALOG_SUFFIX = "_uploadDialog";
  
  /**
   * The id suffix for a div that is used to anchor Grid objects.
   */
  public static final String GRID_ANCHOR_DIV_SUFFIX = "_gridAnchor";
  
  /**
   * The class associated with the div that is used to anchor Grid objects.
   */
  public static final String GRID_ANCHOR_DIV_CLASS = "gridAnchor";
  
  /**
   * The class associated with the div that is used to anchor TreeGrid objects.
   */
  public static final String TREEGRID_ANCHOR_DIV_CLASS = "treeGridAnchor";
  
  /**
   * The div class for the footer of a grid that holds buttons and pagination info.
   */
  public static final String GRID_FOOTER_CLASS = "gridFooter";
  
  /**
   * The div class that contains the create button to create a new instance of a type.
   */
  public static final String CREATE_BUTTON_CLASS = "createButton";
  
  /**
   * The div class that contains the pagination numbers.
   */
  public static final String PAGINATION_CLASS = "paginationDiv";
  
  /**
   * The class for a column header that is using sort ascending.
   */
  public static final String SORT_ASC_CLASS = "sortAsc";
  
  /**
   * The class for a column header that is using sort descending.
   */
  public static final String SORT_DESC_CLASS = "sortDesc";
  
  /**
   * A property added to the javascript vault object whose value
   * is the id of the input that contains the vault file id from the uploaded file.
   */
  public static final String VAULT_FILE_FIELD_ID_PROPERTY = "fileFieldId";
  
  /**
   * A property added to the javascript vault object whose value
   * contains the id of the dialog div that contains the upload component.
   */
  public static final String VAULT_DIALOG_ID = "dialogId";
  
  /**
   * The suffix for any attribute that needs a toggle effect.
   * (For example, toggle a slide up/down with a text attribute.)
   */
  public static final String TOGGLE_SUFFIX = "_toggle";
  
  /**
   * The div class that contains references to the parent and child when
   * a RelationshipDTO instance is rendered.
   */
  public static final String PARENT_CHILD_DIV_CLASS = "parentAndChild";

  /**
   * The name of the tab for attributes.
   */
  public static final String ATTRIBUTES_TAB = "Attributes";
  
  /**
   * The name of the tab for relationships.
   */
  public static final String RELATIONSHIPS_TAB = "Relationships";
  
  /**
   * The id suffix of the div that contains the field to select a relationship type.
   */
  public static final String RELATIONSHIPS_SELECT_DIV_SUFFIX = "_relationshipSelectDiv";
  
  /**
   * The name of the class defined by the div that contains the information
   * to select a relationship type.
   */
  public static final String RELATIONSHIPS_SELECT_DIV_CLASS = "relationshipSelectDiv";
  
  /**
   * The class used to denote headers with an H1 tag.
   */
  public static final String HEADER_CLASS = "headerClass";
  
  /**
   * The div class that contains the read view of a Business's state.
   */
  public static final String STATE_READ_CLASS = "stateRead";
  
  /**
   * The div class that contains the transition select for a state.
   */
  public static final String STATE_TRANSITION_CLASS = "stateTransition";
  
  /**
   * The suffix for the div id that contains the node search results.
   */
  public static final String NODE_SEARCH_SUFFIX = "_nodeSearch";
  
  /**
   * The suffix for the div id that contains the child search results.
   */
  public static final String CHILD_SEARCH_SUFFIX = "_childSearch";
  
  /**
   * The suffix for the div id that contains the parent search results.
   */
  public static final String PARENT_SEARCH_SUFFIX = "_parentSearch";
  
  /**
   * The suffix for the div id that contains the search dialog for a relationship node.
   */
  public static final String OPEN_NODE_SEARCH_SUFFIX = "_openNodeSearch";
  
  /**
   * The id suffix of the div that wraps relationship information.
   */
  public static final String RELATIONSHIPS_WRAPPER_SUFFIX = "_relationshipsWrapper";
  
  /**
   * The class of the div that wraps relationship information while viewing a BusinessDTO.
   */
  public static final String RELATIONSHIPS_WRAPPER_DIV_CLASS = "relationshipsWrapper";
  
  /**
   * The id suffix of the div that contains a dialog to search for all instances of a type.
   */
  public static final String VIEW_ALL_SEARCH_SUFFIX = "_viewAllSearch";
  
  /**
   * The id delimiter to separate the base row id from the component div id.
   * (When combined, forms the unique row id).
   */
  public static final String GRID_ROW_ID_DELIM = "-";
  
  /**
   * The class name whose div contains the toString value of the window.
   */
  public static final String WINDOW_NAME_CLASS = "windowName";
  
  /**
   * The class name whose div contains the minimize/close buttons.
   */
  public static final String WINDOW_TOGGLES = "winToggles";
  
  /**
   * The class name whose div contains items (mainly fieldsets).
   */
  public static final String WINDOW_ITEMS = "winItems";
  
  /**
   * The class anme whose div contains buttons (e.g., update, cancel).
   */
  public static final String WINDOW_BUTTONS = "winButtons";
  
  /**
   * The class of the div that contains the loading graphic for ajax requests.
   */
  public static final String WINDOW_LOADER = "winLoader";
  
  /**
   * The class of the div that contains the content of the div (the true dynamic content,
   * such as attribute information or relationships).
   */
  public static final String WINDOW_SUBCONTENT = "subContent";
  
  /**
   * The class name of the fieldset that contains special that needs to be marked differently than
   * regular fieldset information. For example, this class is used for struct attributes relationship info.
   */
  public static final String FIELDSET_SPECIAL_CLASS = "fieldsetSpecial";
  
  /**
   * A basic delimiter that is used by many things to 
   * offer a namespace or seperation of values in a unique id.
   */
  public static final String DELIM = "-";
  
  public static final String JSON_PACKAGE_ID_PROPERTY = "windowId";
  
  public static final String JSON_PACKAGE_TO_STRING_PROPERTY = "_toString";
  
  public static final String JSON_PACKAGE_TYPE_PROPERTY = "type";
  
  public static final String JSON_PACKAGE_VIEW_PROPERTY = "view";
  
  public static final String JSON_PACKAGE_QUERY_STRING_PROPERTY = "queryString";
  
  public static final String JSON_PACKAGE_CACHE_UPDATE = "cacheUpdate";
  
  /**
   * The class whose div contains the search tabbar for searching on
   * a type.
   */
  public static final String SEARCH_TABBAR_CLASS = "searchTabbar";
  
  /**
   * The id suffix whose div contains the search tabbar for searching on a type.
   */
  public static final String SEARCH_TABBAR_SUFFIX = "_searchTabbar";
  
  /**
   * The class that belongs to the element to toggle the search tabbar open/close.
   */
  public static final String SEARCH_TABBAR_HANDLE_CLASS = "searchTabbarHandle";
  
  /**
   * The id suffix of the element that toggles the search tabbar open/close.
   */
  public static final String SEARCH_TABBAR_HANDLE_SUFFIX = "_searchTabbarHandle";
  
  /**
   * The id suffix of the div that contains reloadable content (i.e., a Grid
   * in a CollectionDiv).
   */
  public static final String RELOADABLE_DIV_SUFFIX = "_reloadableDiv";
  
  /**
   * The class of the div that contains reloadable content (i.e., a Grid
   * in a CollectionDiv).
   */
  public static final String RELOADABLE_DIV_CLASS = "reloadableDiv";
  
  public static final String SEARCH_COLUMN_CLASS = "searchColumn";
  
  /**
   * The class of the div that contains the search attributes in the left column.
   */
  public static final String LEFT_SEARCH_COLUMN_CLASS = "leftSearchColumn";
  
  /**
   * The class of the div that contains the search attributes in the right column.
   */
  public static final String RIGHT_SEARCH_COLUMN_CLASS = "rightSearchColumn";
  
  /**
   * The class of the div that contains the actual search contents (field inputs and groovy query).
   */
  public static final String SEARCH_CONTENTS_CLASS = "searchContents";
  
  /**
   * The class for spans that wrap the required star for required attributes.
   */
  public static final String REQUIRED_CLASS = "required";
  
  /**
   * The id suffix of the field that contains the groovy query.
   */
  public static final String GROOVY_FIELD_SUFFIX = "_groovyField";
  
  /**
   * Class for the element that contains the message stating that no
   * results have been found for a search.
   */
  public static final String NO_RESULTS_CLASS = "noResults";
  
  /**
   * The class name of the input that stores attribute date/datetime information.
   */
  public static final String ATTRIBUTE_DATE_YEAR_FIELD_CLASS = "dateYearField";
  
  /**
   * The class name of the input that stores attribute date/datetime information.
   */
  public static final String ATTRIBUTE_DATE_FIELD_CLASS = "dateField";
  
  /**
   * The class name of the input that stores attribute time information.
   */
  public static final String ATTRIBUTE_TIME_FIELD_CLASS = "timeField";
  
  /**
   * The id suffix of the grid cell that contains an attribute text.
   */
  public static final String GRID_TEXT_SUFFIX = "_gridText";
  
  /**
   * The span class that contains the date delimiter.
   */
  public static final String DATE_DELIM_CLASS = "dateDelim";
  
  /**
   * The span class that contains the time delimiter.
   */
  public static final String TIME_DELIM_CLASS = "timeDelim";
  
  /**
   * The class name of the element that contains a calendar.
   */
  public static final String CALENDAR_CLASS = "calendar";
  
  /**
   * The class of the div that contains the select button in the relationships div for a business view.
   */
  public static final String RELATIONSHIP_SELECT_BUTTON_CLASS = "relationshipSelectButton";
  
  /**
   * The class of the div that contains the refresh button in the relationships div for a business view.
   */
  public static final String RELATIONSHIP_REFRESH_BUTTON_CLASS = "relationshipRefreshButton";
  
  /**
   * The class of the div that contains the search button.
   */
  public static final String SEARCH_CONTENTS_SEARCH_BUTTON_CLASS = "searchButton";
  
  public static final String ASSISTANT_WINDOW = ADMIN_PACKAGE+".AssistantWindow";
  
  public static final String TYPE_WINDOW = ADMIN_PACKAGE+".TypeWindow";
  
  public static final String OBJECT_WINDOW = ADMIN_PACKAGE+".ObjectWindow";
  
  /**
   * The name suffix of the left field in an attribute comparison search.
   */
  public static final String LEFT_SEARCH_FIELD_SUFFIX = "leftCompare";
  
  /**
   * The name suffix of the right field in an attribute comparison search.
   */
  public static final String RIGHT_SEARCH_FIELD_SUFFIX = "rightCompare";
  
  /**
   * The class of the left field in an attribute comparison search.
   */
  public static final String LEFT_SEARCH_FIELD = "leftSearchField";
  
  /**
   * The class of the right field in an attribute comparison search.
   */
  public static final String RIGHT_SEARCH_FIELD = "rightSearchField";
  
  /**
   * The root namespace delim for input fields.
   */
  public static final String NAMESPACE_DELIM = "--";
  
  /*----------- START: Constants for admin login/logout ---------------*/
  
  public static final String LOGIN_PAGE = "/login.jsp";
  
  public static final String ADMIN_PAGE = "/admin.jsp";
  
  public static final String LOGIN_ERROR = "loginError";
  
  public static final String LOGIN_PATH = "loginPath";
  
  /*----------- END: Constants for admin login/logout ---------------*/
  
  public static final String JSON_QUERY_OPERATOR = "operator";
  
  public static final String JSON_QUERY_ATTRIBUTE = "attribute";
  
  public static final String JSON_QUERY_CONDITION = "condition";
  
  public static final String JSON_QUERY_VALUE = "value";
  
  private String methodName;
  
  private AdminConstants(String methodName)
  {
    this.methodName = methodName;
  }
  
  public String createCustomCall(String params)
  {
    String call = ADMIN_PACKAGE + "." + methodName + "("+params+")";
    return call;
  }
  
  public String createCall(String ... params)
  {
    // creates the javascript call, escapes single quotes, and replaces any dangling commas
    String call = ADMIN_PACKAGE + "." + methodName + "(";
    
    for(String param : params)
    {
      param = param.replace("'", "\'");
      param = EscapeUtil.escapeJS(param);
      call += "'" + param + "',";
    }
    
    call = call.replaceFirst(",$", "");
    call += ")";
    
    return call;
  }
}
