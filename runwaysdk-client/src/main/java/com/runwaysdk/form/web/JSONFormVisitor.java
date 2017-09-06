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
package com.runwaysdk.form.web;

import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.constants.BasicConditionInfo;
import com.runwaysdk.constants.CharacterConditionInfo;
import com.runwaysdk.constants.CompositeFieldConditionInfo;
import com.runwaysdk.constants.DateConditionInfo;
import com.runwaysdk.constants.DoubleConditionInfo;
import com.runwaysdk.constants.LongConditionInfo;
import com.runwaysdk.constants.MdWebAttributeInfo;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebDecInfo;
import com.runwaysdk.constants.MdWebFieldInfo;
import com.runwaysdk.constants.MdWebGeoInfo;
import com.runwaysdk.constants.MdWebNumberInfo;
import com.runwaysdk.constants.MdWebPrimitiveInfo;
import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.constants.MdWebSingleTermInfo;
import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.form.FormMd;
import com.runwaysdk.form.web.condition.AndFieldCondition;
import com.runwaysdk.form.web.condition.BasicCondition;
import com.runwaysdk.form.web.condition.CharacterCondition;
import com.runwaysdk.form.web.condition.CompositeFieldCondition;
import com.runwaysdk.form.web.condition.Condition;
import com.runwaysdk.form.web.condition.DateCondition;
import com.runwaysdk.form.web.condition.DoubleCondition;
import com.runwaysdk.form.web.condition.LongCondition;
import com.runwaysdk.form.web.field.WebAttribute;
import com.runwaysdk.form.web.field.WebBoolean;
import com.runwaysdk.form.web.field.WebBreak;
import com.runwaysdk.form.web.field.WebCharacter;
import com.runwaysdk.form.web.field.WebComment;
import com.runwaysdk.form.web.field.WebDate;
import com.runwaysdk.form.web.field.WebDateTime;
import com.runwaysdk.form.web.field.WebDec;
import com.runwaysdk.form.web.field.WebDecimal;
import com.runwaysdk.form.web.field.WebDouble;
import com.runwaysdk.form.web.field.WebField;
import com.runwaysdk.form.web.field.WebFloat;
import com.runwaysdk.form.web.field.WebGeo;
import com.runwaysdk.form.web.field.WebHeader;
import com.runwaysdk.form.web.field.WebInteger;
import com.runwaysdk.form.web.field.WebLong;
import com.runwaysdk.form.web.field.WebMultipleTerm;
import com.runwaysdk.form.web.field.WebNumber;
import com.runwaysdk.form.web.field.WebPrimitive;
import com.runwaysdk.form.web.field.WebReference;
import com.runwaysdk.form.web.field.WebSingleTerm;
import com.runwaysdk.form.web.field.WebSingleTermGrid;
import com.runwaysdk.form.web.field.WebText;
import com.runwaysdk.form.web.field.WebTime;
import com.runwaysdk.form.web.metadata.ConditionMd;
import com.runwaysdk.form.web.metadata.WebAttributeMd;
import com.runwaysdk.form.web.metadata.WebBooleanMd;
import com.runwaysdk.form.web.metadata.WebCharacterMd;
import com.runwaysdk.form.web.metadata.WebDateMd;
import com.runwaysdk.form.web.metadata.WebDecMd;
import com.runwaysdk.form.web.metadata.WebFieldMd;
import com.runwaysdk.form.web.metadata.WebGeoMd;
import com.runwaysdk.form.web.metadata.WebNumberMd;
import com.runwaysdk.form.web.metadata.WebPrimitiveMd;
import com.runwaysdk.form.web.metadata.WebReferenceMd;
import com.runwaysdk.form.web.metadata.WebSingleTermMd;
import com.runwaysdk.form.web.metadata.WebTextMd;
import com.runwaysdk.format.AbstractFormatFactory;
import com.runwaysdk.format.FormatFactory;
import com.runwaysdk.transport.conversion.ConversionExceptionDTO;

/**
 * Visits all components of a web form and generates the JSON to be sent to the
 * browser.
 */
public class JSONFormVisitor implements WebFormVisitor, JSONWebFieldConstants
{
  /**
   * Interface that defines what a FieldBuilder plugin must implement to provide
   * custom field classes.
   */
  public static interface PluginIF
  {
    public String getModuleIdentifier();

    public WebFormComponentToJSON getBuilder(WebFormComponent component, JSONFormVisitor visitor);
  }

  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  /**
   * FormatFactory to parse user input, which may have been scraped directly
   * from a form or input in a type-unsafe manner according to locale specific
   * rules.
   */
  private FormatFactory       formatFactory;

  /**
   * The locale to use to format the values.
   */
  private Locale              locale;

  /**
   * The root JSON object of the visiting process.
   */
  private JSONObject          root;

  /**
   * The fields, in order, on the form.
   */
  private JSONArray           fields;

  protected Stack<JSONObject> stack;

  public JSONObject getJSON()
  {
    return root;
  }

  /**
   * 
   * @param locale
   */
  public JSONFormVisitor(Locale locale)
  {
    this.formatFactory = AbstractFormatFactory.getFormatFactory();

    this.locale = locale;

    root = new JSONObject();
    fields = new JSONArray();
    stack = new Stack<JSONObject>();

    try
    {
      root.put(FIELDS, fields);
    }
    catch (JSONException e)
    {
      String msg = "Could not initialize conversion from a FormObject to JSON";
      throw new ConversionExceptionDTO(msg, e);
    }
  }

  public FormatFactory getFormatFactory()
  {
    return formatFactory;
  }

  public Locale getLocale()
  {
    return locale;
  }

  public JSONObject getRoot()
  {
    return root;
  }

  public JSONArray getFields()
  {
    return fields;
  }

  public Stack<JSONObject> getStack()
  {
    return stack;
  }

  @Override
  public void visit(WebFormObject formObject)
  {
    try
    {
      put(root, JS_CLASS, formObject.getClass().getName());
      put(root, TYPE, formObject.getType());
      put(root, ID, formObject.getId());
      put(root, DATA_ID, formObject.getDataId());
      put(root, NEW_INSTANCE, formObject.isNewInstance());
      put(root, DISCONNECTED, formObject.isDisconnected());
      put(root, READABLE, formObject.isReadable());
      put(root, WRITABLE, formObject.isWritable());

      JSONObject formMd = new JSONObject();
      FormMd md = formObject.getMd();
      put(formMd, "description", md.getDescription());
      put(formMd, "displayLabel", md.getDisplayLabel());
      put(formMd, ID, md.getId());
      put(formMd, "formName", md.getFormName());
      put(formMd, "formMdClass", md.getFormMdClass());

      root.put(FORM_MD, formMd);
    }
    catch (JSONException e)
    {
      String msg = "Could not convert the form [" + formObject.getMd().getFormName() + "] to JSON";
      throw new ConversionExceptionDTO(msg, e);
    }
  }

  /**
   * Generic method that will look for plugin support. The
   */
  @Override
  public void visit(WebFormComponent component)
  {
    for (PluginIF plugin : pluginMap.values())
    {
      WebFormComponentToJSON builder = plugin.getBuilder(component, this);
      if (builder != null)
      {
        builder.populate();
        return;
      }
    }

    // Be strict so we don't accidently build incomplete JSON
    String msg = "Could not convert the WebFormComponent [" + component + "] to JSON";
    throw new ConversionExceptionDTO(msg);
  }

  @Override
  public void visit(WebCharacter webCharacter)
  {
    new WebCharacterToJSON(webCharacter, this).populate();
  }

  @Override
  public void visit(WebText webText)
  {
    new WebTextToJSON(webText, this).populate();
  }

  @Override
  public void visit(WebBoolean webBoolean)
  {
    new WebBooleanToJSON(webBoolean, this).populate();
  }

  @Override
  public void visit(WebDouble webDouble)
  {
    new WebDoubleToJSON(webDouble, this).populate();
  }

  @Override
  public void visit(WebFloat webFloat)
  {
    new WebFloatToJSON(webFloat, this).populate();
  }

  @Override
  public void visit(WebDecimal webDecimal)
  {
    new WebDecimalToJSON(webDecimal, this).populate();
  }

  @Override
  public void visit(WebInteger webInteger)
  {
    new WebIntegerToJSON(webInteger, this).populate();
  }

  @Override
  public void visit(WebLong webLong)
  {
    new WebLongToJSON(webLong, this).populate();
  }

  @Override
  public void visit(WebDate webDate)
  {
    new WebDateToJSON(webDate, this).populate();
  }

  @Override
  public void visit(WebDateTime webDateTime)
  {
    new WebDateTimeToJSON(webDateTime, this).populate();
  }

  @Override
  public void visit(WebTime webTime)
  {
    new WebTimeToJSON(webTime, this).populate();
  }

  @Override
  public void visit(WebHeader header)
  {
    new WebHeaderToJSON(header, this).populate();
  }

  @Override
  public void visit(WebBreak webBreak)
  {
    new WebBreakToJSON(webBreak, this).populate();
  }

  @Override
  public void visit(WebComment comment)
  {
    new WebCommentToJSON(comment, this).populate();
  }

  @Override
  public void visit(WebReference webReference)
  {
    new WebReferenceToJSON(webReference, this).populate();
  }

  @Override
  public void visit(WebGeo webGeo)
  {
    new WebGeoToJSON(webGeo, this).populate();
  }

  @Override
  public void visit(WebSingleTerm webSingleTerm)
  {
    new WebSingleTermToJSON(webSingleTerm, this).populate();
  }

  @Override
  public void visit(WebMultipleTerm webMultipleTerm)
  {
    new WebMultipleTermToJSON(webMultipleTerm, this).populate();
  }

  @Override
  public void visit(WebSingleTermGrid grid)
  {
    new WebSingleTermGridToJSON(grid, this).populate();
  }

  public void put(JSONObject obj, String key, Object value) throws JSONException
  {
    obj.put(key, value != null ? value : JSONObject.NULL);
  }

  public static abstract class WebFormComponentToJSON
  {
    private JSONFormVisitor visitor;

    public WebFormComponentToJSON(JSONFormVisitor visitor)
    {
      this.visitor = visitor;
    }

    protected abstract void populate();

    protected JSONFormVisitor getVisitor()
    {
      return visitor;
    }
  }

  public static abstract class WebFieldToJSON extends WebFormComponentToJSON
  {
    private WebField field;

    public WebFieldToJSON(WebField webField, JSONFormVisitor visitor)
    {
      super(visitor);
      this.field = webField;
    }

    protected WebField getField()
    {
      return this.field;
    }

    /**
     * Formats the value according to the current locale.
     * 
     * @return
     */
    protected String formatValue()
    {
      WebField webField = this.getField();
      if (webField instanceof WebPrimitive)
      {
        WebPrimitive primitive = (WebPrimitive) webField;
        Object value = primitive.getObjectValue();

        if (value != null)
        {
          Class<?> clazz = primitive.getJavaType();
          return this.getVisitor().getFormatFactory().getFormat(clazz).format(value, this.getVisitor().getLocale());
        }
        else
        {
          return primitive.getValue();
        }
      }
      else
      {
        return webField.getValue();
      }
    }

    protected void initField(JSONObject obj) throws JSONException
    {
      WebField webField = this.getField();

      String formatted = this.formatValue();
      this.getVisitor().put(obj, VALUE, formatted);

      this.getVisitor().put(obj, JS_CLASS, webField.getClass().getName());
      this.getVisitor().put(obj, TYPE, webField.getType());
      this.getVisitor().put(obj, READABLE, webField.isReadable());
      this.getVisitor().put(obj, WRITABLE, webField.isWritable());
      this.getVisitor().put(obj, MODIFIED, webField.isModified());
    }

    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      WebFieldMd md = this.getField().getFieldMd();

      this.getVisitor().put(obj, JS_CLASS, md.getClass().getName());
      this.getVisitor().put(obj, MdWebFieldInfo.FIELD_NAME, md.getFieldName());
      this.getVisitor().put(obj, MdWebFieldInfo.FIELD_ORDER, md.getFieldOrder());
      this.getVisitor().put(obj, MdWebFieldInfo.DISPLAY_LABEL, md.getDisplayLabel());
      this.getVisitor().put(obj, MdWebFieldInfo.DESCRIPTION, md.getDescription());
      this.getVisitor().put(obj, MdWebFieldInfo.ID, md.getId());
      this.getVisitor().put(obj, MdWebFieldInfo.REQUIRED, md.isRequired());

    }

    protected final void populate()
    {
      try
      {
        JSONObject field = new JSONObject();
        JSONObject md = new JSONObject();

        initField(field);
        initFieldMd(md);

        // grab the condition if there is one
        if (!this.getVisitor().getStack().isEmpty())
        {
          JSONObject condition = this.getVisitor().getStack().pop();
          this.getVisitor().put(field, CONDITION, condition);
        }
        else
        {
          this.getVisitor().put(field, CONDITION, null);
        }

        this.getVisitor().put(field, FIELD_MD, md);
        this.getVisitor().getFields().put(field);
      }
      catch (JSONException e)
      {
        String msg = "Could not convert the field [" + this.getField().getFieldName() + "] to JSON";
        throw new ConversionExceptionDTO(msg, e);
      }
    }
  }

  private static class WebHeaderToJSON extends WebFieldToJSON
  {

    protected WebHeaderToJSON(WebHeader header, JSONFormVisitor visitor)
    {
      super(header, visitor);
    }

    @Override
    protected WebHeader getField()
    {
      return (WebHeader) super.getField();
    }
  }

  private static class WebCommentToJSON extends WebFieldToJSON
  {

    protected WebCommentToJSON(WebComment comment, JSONFormVisitor visitor)
    {
      super(comment, visitor);
    }

    @Override
    protected WebComment getField()
    {
      return (WebComment) super.getField();
    }
  }

  private static class WebReferenceToJSON extends WebAttributeToJSON
  {
    protected WebReferenceToJSON(WebReference webReference, JSONFormVisitor visitor)
    {
      super(webReference, visitor);
    }

    @Override
    protected WebReference getField()
    {
      return (WebReference) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebReferenceMd fMd = this.getField().getFieldMd();
      this.getVisitor().put(obj, MdWebReferenceInfo.REFERENCED_MD_BUSINESS, fMd.getReferencedMdBusiness());
      this.getVisitor().put(obj, MdWebReferenceInfo.REFERENCED_DISPLAY_LABEL, fMd.getReferencedDisplayLabel());
    }
  }

  private static class WebGeoToJSON extends WebAttributeToJSON
  {
    protected WebGeoToJSON(WebGeo webGeo, JSONFormVisitor visitor)
    {
      super(webGeo, visitor);
    }

    @Override
    protected WebGeo getField()
    {
      return (WebGeo) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebGeoMd fMd = this.getField().getFieldMd();
      this.getVisitor().put(obj, MdWebGeoInfo.GEO_DISPLAY_LABEL, fMd.getGeoDisplayLabel());
    }
  }

  private static class WebSingleTermToJSON extends WebAttributeToJSON
  {
    protected WebSingleTermToJSON(WebSingleTerm webSingleTerm, JSONFormVisitor visitor)
    {
      super(webSingleTerm, visitor);
    }

    @Override
    protected WebSingleTerm getField()
    {
      return (WebSingleTerm) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebSingleTermMd fMd = this.getField().getFieldMd();
      this.getVisitor().put(obj, MdWebSingleTermInfo.TERM_DISPLAY_LABEL, fMd.getTermDisplayLabel());
    }
  }

  private static class WebMultipleTermToJSON extends WebAttributeToJSON
  {
    protected WebMultipleTermToJSON(WebMultipleTerm webMultipleTerm, JSONFormVisitor visitor)
    {
      super(webMultipleTerm, visitor);
    }

    @Override
    protected WebMultipleTerm getField()
    {
      return (WebMultipleTerm) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      // WebMultipleTermMd fMd = this.getField().getFieldMd();
      this.getField().getFieldMd();
    }
  }

  private static class WebSingleTermGridToJSON extends WebAttributeToJSON
  {
    protected WebSingleTermGridToJSON(WebSingleTermGrid grid, JSONFormVisitor visitor)
    {
      super(grid, visitor);
    }

    @Override
    protected WebSingleTermGrid getField()
    {
      return (WebSingleTermGrid) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      // WebSingleTermGridMd fMd = this.getField().getFieldMd();
      this.getField().getFieldMd();
    }
  }

  private static class WebBreakToJSON extends WebFieldToJSON
  {

    protected WebBreakToJSON(WebBreak webBreak, JSONFormVisitor visitor)
    {
      super(webBreak, visitor);
    }

    @Override
    protected WebBreak getField()
    {
      return (WebBreak) super.getField();
    }
  }

  public static abstract class WebAttributeToJSON extends WebFieldToJSON
  {
    public WebAttributeToJSON(WebAttribute webAttribute, JSONFormVisitor visitor)
    {
      super(webAttribute, visitor);
    }

    @Override
    protected WebAttribute getField()
    {
      return (WebAttribute) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebAttributeMd primMd = this.getField().getFieldMd();
      this.getVisitor().put(obj, MdWebAttributeInfo.DEFINING_MD_ATTRIBUTE, primMd.getDefiningMdAttribute());
      this.getVisitor().put(obj, MdWebAttributeInfo.DEFINING_ATTRIBUTE, primMd.getDefiningAttribute());
      this.getVisitor().put(obj, MdWebAttributeInfo.DEFINING_CLASS, primMd.getDefiningClass());
      this.getVisitor().put(obj, MdWebAttributeInfo.SHOW_ON_SEARCH, primMd.getShowOnSearch());
      this.getVisitor().put(obj, MdWebAttributeInfo.SHOW_ON_VIEW_ALL, primMd.getShowOnViewAll());
    }
  }

  private static abstract class WebPrimitiveToJSON extends WebAttributeToJSON
  {
    public WebPrimitiveToJSON(WebPrimitive webPrimitive, JSONFormVisitor visitor)
    {
      super(webPrimitive, visitor);
    }

    @Override
    protected WebPrimitive getField()
    {
      return (WebPrimitive) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebPrimitiveMd md = this.getField().getFieldMd();

      this.getVisitor().put(obj, MdWebPrimitiveInfo.IS_EXPRESSION, md.getIsExpression());
      this.getVisitor().put(obj, MdWebPrimitiveInfo.EXPRESSION, md.getExpression());
    }
  }

  private static class WebCharacterToJSON extends WebPrimitiveToJSON
  {

    protected WebCharacterToJSON(WebCharacter webCharacter, JSONFormVisitor visitor)
    {
      super(webCharacter, visitor);
    }

    @Override
    protected WebCharacter getField()
    {
      return (WebCharacter) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebCharacterMd md = this.getField().getFieldMd();

      this.getVisitor().put(obj, MdWebCharacterInfo.DISPLAY_LENGTH, md.getDisplayLength());
      this.getVisitor().put(obj, MdWebCharacterInfo.MAX_LENGTH, md.getMaxLength());
    }
  }

  private static class WebTextToJSON extends WebPrimitiveToJSON
  {

    protected WebTextToJSON(WebText webText, JSONFormVisitor visitor)
    {
      super(webText, visitor);
    }

    @Override
    protected WebText getField()
    {
      return (WebText) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebTextMd md = this.getField().getFieldMd();

      this.getVisitor().put(obj, MdWebTextInfo.WIDTH, md.getWidth());
      this.getVisitor().put(obj, MdWebTextInfo.HEIGHT, md.getHeight());
    }
  }

  private static class WebBooleanToJSON extends WebPrimitiveToJSON
  {

    public WebBooleanToJSON(WebBoolean webBoolean, JSONFormVisitor visitor)
    {
      super(webBoolean, visitor);
    }

    @Override
    protected WebBoolean getField()
    {
      return (WebBoolean) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebBooleanMd md = this.getField().getFieldMd();

      this.getVisitor().put(obj, MdWebBooleanInfo.POSITIVE_DISPLAY_LABEL, md.getPositiveDisplayLabel());
      this.getVisitor().put(obj, MdWebBooleanInfo.NEGATIVE_DISPLAY_LABEL, md.getNegativeDisplayLabel());
    }
  }

  private static class WebDateToJSON extends WebPrimitiveToJSON
  {

    public WebDateToJSON(WebDate webDate, JSONFormVisitor visitor)
    {
      super(webDate, visitor);
    }

    @Override
    protected WebDate getField()
    {
      return (WebDate) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebDateMd md = this.getField().getFieldMd();

      this.getVisitor().put(obj, MdWebDateInfo.AFTER_TODAY_EXCLUSIVE, md.getAfterTodayExclusive());
      this.getVisitor().put(obj, MdWebDateInfo.AFTER_TODAY_INCLUSIVE, md.getAfterTodayInclusive());
      this.getVisitor().put(obj, MdWebDateInfo.BEFORE_TODAY_EXCLUSIVE, md.getBeforeTodayExclusive());
      this.getVisitor().put(obj, MdWebDateInfo.BEFORE_TODAY_INCLUSIVE, md.getBeforeTodayInclusive());
      this.getVisitor().put(obj, MdWebDateInfo.START_DATE, md.getStartDate());
      this.getVisitor().put(obj, MdWebDateInfo.END_DATE, md.getEndDate());
    }
  }

  private static class WebDateTimeToJSON extends WebPrimitiveToJSON
  {

    public WebDateTimeToJSON(WebDateTime webDateTime, JSONFormVisitor visitor)
    {
      super(webDateTime, visitor);
    }

    @Override
    protected WebDateTime getField()
    {
      return (WebDateTime) super.getField();
    }
  }

  private static class WebTimeToJSON extends WebPrimitiveToJSON
  {

    public WebTimeToJSON(WebTime webTime, JSONFormVisitor visitor)
    {
      super(webTime, visitor);
    }

    @Override
    protected WebTime getField()
    {
      return (WebTime) super.getField();
    }
  }

  private static abstract class WebNumberToJSON extends WebPrimitiveToJSON
  {
    public WebNumberToJSON(WebNumber webNumber, JSONFormVisitor visitor)
    {
      super(webNumber, visitor);
    }

    @Override
    protected WebNumber getField()
    {
      return (WebNumber) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebNumberMd md = this.getField().getFieldMd();

      this.getVisitor().put(obj, MdWebNumberInfo.STARTRANGE, md.getStartRange());
      this.getVisitor().put(obj, MdWebNumberInfo.ENDRANGE, md.getEndRange());
    }
  }

  private static abstract class WebDecToJSON extends WebNumberToJSON
  {
    public WebDecToJSON(WebDec webDec, JSONFormVisitor visitor)
    {
      super(webDec, visitor);
    }

    @Override
    protected WebDec getField()
    {
      return (WebDec) super.getField();
    }

    @Override
    protected void initFieldMd(JSONObject obj) throws JSONException
    {
      super.initFieldMd(obj);

      WebDecMd md = this.getField().getFieldMd();

      this.getVisitor().put(obj, MdWebDecInfo.DECPRECISION, md.getDecPrecision());
      this.getVisitor().put(obj, MdWebDecInfo.DECSCALE, md.getDecScale());
    }
  }

  private static class WebDoubleToJSON extends WebDecToJSON
  {

    public WebDoubleToJSON(WebDouble webDouble, JSONFormVisitor visitor)
    {
      super(webDouble, visitor);
    }

    @Override
    protected WebDouble getField()
    {
      return (WebDouble) super.getField();
    }
  }

  private static class WebDecimalToJSON extends WebDecToJSON
  {

    public WebDecimalToJSON(WebDecimal webDecimal, JSONFormVisitor visitor)
    {
      super(webDecimal, visitor);
    }

    @Override
    protected WebDecimal getField()
    {
      return (WebDecimal) super.getField();
    }
  }

  private static class WebFloatToJSON extends WebDecToJSON
  {

    public WebFloatToJSON(WebFloat webFloat, JSONFormVisitor visitor)
    {
      super(webFloat, visitor);
    }

    @Override
    protected WebFloat getField()
    {
      return (WebFloat) super.getField();
    }
  }

  private static class WebIntegerToJSON extends WebNumberToJSON
  {

    public WebIntegerToJSON(WebInteger webInteger, JSONFormVisitor visitor)
    {
      super(webInteger, visitor);
    }

    @Override
    protected WebInteger getField()
    {
      return (WebInteger) super.getField();
    }
  }

  private static class WebLongToJSON extends WebNumberToJSON
  {

    public WebLongToJSON(WebLong webLong, JSONFormVisitor visitor)
    {
      super(webLong, visitor);
    }

    @Override
    protected WebLong getField()
    {
      return (WebLong) super.getField();
    }
  }

  private static abstract class ConditionToJSON extends WebFormComponentToJSON
  {
    protected Condition condition;

    public ConditionToJSON(Condition condition, JSONFormVisitor visitor)
    {
      super(visitor);
      this.condition = condition;
    }

    protected Condition getCondition()
    {
      return this.condition;
    }

    protected abstract void initCondition(JSONObject obj) throws JSONException;

    protected abstract void initConditionMd(JSONObject obj) throws JSONException;
  }

  private static abstract class BasicConditionToJSON extends ConditionToJSON
  {
    public BasicConditionToJSON(Condition condition, JSONFormVisitor visitor)
    {
      super(condition, visitor);
    }

    protected void initCondition(JSONObject obj) throws JSONException
    {
      Condition condition = this.getCondition();
      BasicCondition basicCondition = (BasicCondition) condition;

      this.getVisitor().put(obj, JS_CLASS, basicCondition.getClass().getName());
      this.getVisitor().put(obj, BasicConditionInfo.DEFINING_MD_FIELD, basicCondition.getDefiningMdField());
      this.getVisitor().put(obj, BasicConditionInfo.OPERATION, basicCondition.getOperation());
      this.getVisitor().put(obj, BasicConditionInfo.ID, basicCondition.getId());
    }

    protected void initConditionMd(JSONObject obj) throws JSONException
    {
      ConditionMd md = this.getCondition().getConditionMd();

      this.getVisitor().put(obj, JS_CLASS, md.getClass().getName());
      this.getVisitor().put(obj, BasicConditionInfo.ID, md.getId());
      this.getVisitor().put(obj, BasicConditionInfo.REFERENCING_MD_FIELD, md.getReferencingMdField());
      this.getVisitor().put(obj, BasicConditionInfo.REFERENCING_MD_FORM, md.getReferencingMdForm());
    }

    protected void populate()
    {
      try
      {
        // check to see if we're a leaf/child node
        JSONObject condition;
        if (!this.getVisitor().getStack().isEmpty())
          condition = this.getVisitor().getStack().peek();
        else
        {
          // add to the stack so field will find us
          condition = new JSONObject();
          this.getVisitor().getStack().push(condition);
        }
        JSONObject md = new JSONObject();

        initCondition(condition);
        initConditionMd(md);

        this.getVisitor().put(condition, CONDITION_MD, md);
      }
      catch (JSONException e)
      {
        String msg = "Could not convert the condition [" + this.getCondition().toString() + "] to JSON";
        throw new ConversionExceptionDTO(msg, e);
      }
    }

  }

  private class CompositeFieldConditionToJSON extends ConditionToJSON
  {
    public CompositeFieldConditionToJSON(Condition condition, JSONFormVisitor visitor)
    {
      super(condition, visitor);
    }

    protected void initCondition(JSONObject obj) throws JSONException
    {
      Condition condition = this.getCondition();
      CompositeFieldCondition composite = (CompositeFieldCondition) condition;

      this.getVisitor().put(obj, JS_CLASS, composite.getClass().getName());
      this.getVisitor().put(obj, CompositeFieldConditionInfo.OPERATION, condition.getOperation());
      this.getVisitor().put(obj, CompositeFieldConditionInfo.DEFINING_MD_FIELD, condition.getDefiningMdField());
    }

    protected void initConditionMd(JSONObject obj) throws JSONException
    {
      ConditionMd md = this.getCondition().getConditionMd();

      this.getVisitor().put(obj, JS_CLASS, md.getClass().getName());
      this.getVisitor().put(obj, BasicConditionInfo.ID, md.getId());
    }

    protected void populate()
    {
      try
      {
        AndFieldCondition and = (AndFieldCondition) this.getCondition();
        JSONObject compositeJSON;

        if (!JSONFormVisitor.this.stack.isEmpty())
          compositeJSON = JSONFormVisitor.this.stack.peek();
        else
        {
          compositeJSON = new JSONObject();
          JSONFormVisitor.this.stack.push(compositeJSON);
        }

        initCondition(compositeJSON);
        JSONObject md = new JSONObject();
        initConditionMd(md);

        // Jump into each condition
        Condition first = and.getFirstCondition();
        JSONObject firstJSON = new JSONObject();
        JSONFormVisitor.this.stack.push(firstJSON);
        first.accept(JSONFormVisitor.this);
        JSONFormVisitor.this.stack.pop();

        Condition second = and.getSecondCondition();
        JSONObject secondJSON = new JSONObject();
        JSONFormVisitor.this.stack.push(secondJSON);
        second.accept(JSONFormVisitor.this);
        JSONFormVisitor.this.stack.pop();

        put(compositeJSON, CompositeFieldConditionInfo.FIRST_CONDITION, firstJSON);
        put(compositeJSON, CompositeFieldConditionInfo.SECOND_CONDITION, secondJSON);
      }
      catch (JSONException e)
      {
        String msg = "Could not convert the condition [" + this.getCondition().toString() + "] to JSON";
        throw new ConversionExceptionDTO(msg, e);
      }
    }
  }

  private class AndFieldConditionToJSON extends CompositeFieldConditionToJSON
  {
    public AndFieldConditionToJSON(AndFieldCondition andFieldCondition, JSONFormVisitor visitor)
    {
      super(andFieldCondition, visitor);
    }

    protected AndFieldCondition getCondition()
    {
      return (AndFieldCondition) super.getCondition();
    }
  }

  private class CharacterConditionToJSON extends BasicConditionToJSON
  {
    public CharacterConditionToJSON(CharacterCondition characterCondition, JSONFormVisitor visitor)
    {
      super(characterCondition, visitor);
    }

    @Override
    protected CharacterCondition getCondition()
    {
      return (CharacterCondition) super.getCondition();
    }

    @Override
    protected void initCondition(JSONObject obj) throws JSONException
    {
      super.initCondition(obj);
      CharacterCondition condition = this.getCondition();
      this.getVisitor().put(obj, CharacterConditionInfo.VALUE, condition.getValue());
    }
  }

  private class DateConditionToJSON extends BasicConditionToJSON
  {
    public DateConditionToJSON(DateCondition dateCondition, JSONFormVisitor visitor)
    {
      super(dateCondition, visitor);
    }

    @Override
    protected DateCondition getCondition()
    {
      return (DateCondition) super.getCondition();
    }

    @Override
    protected void initCondition(JSONObject obj) throws JSONException
    {
      super.initCondition(obj);
      DateCondition condition = this.getCondition();
      this.getVisitor().put(obj, DateConditionInfo.VALUE, condition.getObjectValue().toString());
    }
  }

  private class DoubleConditionToJSON extends BasicConditionToJSON
  {
    public DoubleConditionToJSON(DoubleCondition doubleCondition, JSONFormVisitor visitor)
    {
      super(doubleCondition, visitor);
    }

    @Override
    protected DoubleCondition getCondition()
    {
      return (DoubleCondition) super.getCondition();
    }

    @Override
    protected void initCondition(JSONObject obj) throws JSONException
    {
      super.initCondition(obj);
      DoubleCondition condition = this.getCondition();
      this.getVisitor().put(obj, DoubleConditionInfo.VALUE, condition.getObjectValue().doubleValue());
    }
  }

  private class LongConditionToJSON extends BasicConditionToJSON
  {
    public LongConditionToJSON(LongCondition longCondition, JSONFormVisitor visitor)
    {
      super(longCondition, visitor);
    }

    @Override
    protected LongCondition getCondition()
    {
      return (LongCondition) super.getCondition();
    }

    @Override
    protected void initCondition(JSONObject obj) throws JSONException
    {
      super.initCondition(obj);
      LongCondition condition = this.getCondition();
      this.getVisitor().put(obj, LongConditionInfo.VALUE, condition.getObjectValue().longValue());
    }
  }

  @Override
  public void visit(CharacterCondition characterCondition)
  {
    new CharacterConditionToJSON(characterCondition, this).populate();
  }

  @Override
  public void visit(AndFieldCondition andFieldCondition)
  {
    new AndFieldConditionToJSON(andFieldCondition, this).populate();
  }

  @Override
  public void visit(DateCondition dateCondition)
  {
    new DateConditionToJSON(dateCondition, this).populate();
  }

  @Override
  public void visit(DoubleCondition doubleCondition)
  {
    new DoubleConditionToJSON(doubleCondition, this).populate();
  }

  @Override
  public void visit(LongCondition longCondition)
  {
    new LongConditionToJSON(longCondition, this).populate();
  }
}
