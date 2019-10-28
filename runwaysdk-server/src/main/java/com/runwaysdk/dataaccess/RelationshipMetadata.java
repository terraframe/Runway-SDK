package com.runwaysdk.dataaccess;

import java.util.Locale;
import java.util.Map;

public interface RelationshipMetadata
{

  /**
   *Returns the display label of the role of the parents in this relationship.
   *
   *@param locale
   *
   * @return the display label of the role of the parents in this relationship.
   */
  public String getParentDisplayLabel(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getParentDisplayLabes();

  /**
   *Returns the display label of the role of the children in this relationship.
   *
   *@param locale
   *
   * @return the display label of the role of the children in this relationship.
   */
  public String getChildDisplayLabel(Locale locale);

  /**
   * Returns a map where the key is the locale and the value is the localized
   * String value.
   *
   * @return map where the key is the locale and the value is the localized
   *   String value.
   */
  public Map<String, String> getChildDisplayLabes();


}
