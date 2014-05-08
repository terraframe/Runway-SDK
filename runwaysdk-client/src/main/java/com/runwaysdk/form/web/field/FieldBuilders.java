/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.form.web.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.runwaysdk.business.ComponentDTO;
import com.runwaysdk.business.ComponentDTOIF;
import com.runwaysdk.constants.MdWebBooleanInfo;
import com.runwaysdk.constants.MdWebBreakInfo;
import com.runwaysdk.constants.MdWebCharacterInfo;
import com.runwaysdk.constants.MdWebCommentInfo;
import com.runwaysdk.constants.MdWebDateInfo;
import com.runwaysdk.constants.MdWebDateTimeInfo;
import com.runwaysdk.constants.MdWebDecimalInfo;
import com.runwaysdk.constants.MdWebDoubleInfo;
import com.runwaysdk.constants.MdWebFloatInfo;
import com.runwaysdk.constants.MdWebGeoInfo;
import com.runwaysdk.constants.MdWebHeaderInfo;
import com.runwaysdk.constants.MdWebIntegerInfo;
import com.runwaysdk.constants.MdWebLongInfo;
import com.runwaysdk.constants.MdWebMultipleTermInfo;
import com.runwaysdk.constants.MdWebReferenceInfo;
import com.runwaysdk.constants.MdWebSingleTermGridInfo;
import com.runwaysdk.constants.MdWebSingleTermInfo;
import com.runwaysdk.constants.MdWebTextInfo;
import com.runwaysdk.constants.MdWebTimeInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.form.web.condition.Condition;
import com.runwaysdk.form.web.condition.ConditionBuilders;
import com.runwaysdk.form.web.condition.ConditionBuilders.ConditionBuilder;
import com.runwaysdk.form.web.metadata.FieldMdBuilders;
import com.runwaysdk.form.web.metadata.FieldMdBuilders.WebBooleanMdBuilder;
import com.runwaysdk.form.web.metadata.FieldMdBuilders.WebFieldMdBuilder;
import com.runwaysdk.form.web.metadata.FieldMdBuilders.WebReferenceMdBuilder;
import com.runwaysdk.form.web.metadata.WebBooleanMd;
import com.runwaysdk.form.web.metadata.WebBreakMd;
import com.runwaysdk.form.web.metadata.WebCharacterMd;
import com.runwaysdk.form.web.metadata.WebCommentMd;
import com.runwaysdk.form.web.metadata.WebDateMd;
import com.runwaysdk.form.web.metadata.WebDateTimeMd;
import com.runwaysdk.form.web.metadata.WebDecimalMd;
import com.runwaysdk.form.web.metadata.WebDoubleMd;
import com.runwaysdk.form.web.metadata.WebFloatMd;
import com.runwaysdk.form.web.metadata.WebGeoMd;
import com.runwaysdk.form.web.metadata.WebHeaderMd;
import com.runwaysdk.form.web.metadata.WebIntegerMd;
import com.runwaysdk.form.web.metadata.WebLongMd;
import com.runwaysdk.form.web.metadata.WebMultipleTermMd;
import com.runwaysdk.form.web.metadata.WebReferenceMd;
import com.runwaysdk.form.web.metadata.WebSingleTermGridMd;
import com.runwaysdk.form.web.metadata.WebSingleTermMd;
import com.runwaysdk.form.web.metadata.WebTextMd;
import com.runwaysdk.form.web.metadata.WebTimeMd;
import com.runwaysdk.generation.CommonGenerationUtil;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.system.metadata.MdFieldDTO;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.system.metadata.MdWebCommentDTO;
import com.runwaysdk.system.metadata.MdWebFieldDTO;
import com.runwaysdk.system.metadata.MdWebGroupDTO;
import com.runwaysdk.system.metadata.MdWebHeaderDTO;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDateDTO;
import com.runwaysdk.transport.attributes.AttributeDateTimeDTO;
import com.runwaysdk.transport.attributes.AttributeDecimalDTO;
import com.runwaysdk.transport.attributes.AttributeDoubleDTO;
import com.runwaysdk.transport.attributes.AttributeFloatDTO;
import com.runwaysdk.transport.attributes.AttributeIntegerDTO;
import com.runwaysdk.transport.attributes.AttributeLongDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeTextDTO;
import com.runwaysdk.transport.attributes.AttributeTimeDTO;
import com.runwaysdk.transport.conversion.ConversionExceptionDTO;

public class FieldBuilders
{
  /**
   * Interface that defines what a FieldBuilder plugin must implement to provide
   * custom field classes.
   */
  public static interface PluginIF
  {
    public String getModuleIdentifier();

    /**
     * Gets the builder for the given type or returns null if no builder was
     * found.
     * 
     * @param type
     * @return
     */
    public WebFieldBuilder getBuilder(String type);
  }

  private static Map<String, PluginIF> pluginMap = new ConcurrentHashMap<String, PluginIF>();

  public static void registerPlugin(PluginIF pluginFactory)
  {
    pluginMap.put(pluginFactory.getModuleIdentifier(), pluginFactory);
  }

  private static Map<String, WebFieldBuilder> builders = new HashMap<String, WebFieldBuilder>();

  static
  {
    builders.put(MdWebBooleanInfo.CLASS, new WebBooleanBuilder());
    builders.put(MdWebBreakInfo.CLASS, new WebBreakBuilder());
    builders.put(MdWebCharacterInfo.CLASS, new WebCharacterBuilder());
    builders.put(MdWebCommentInfo.CLASS, new WebCommentBuilder());
    builders.put(MdWebDateInfo.CLASS, new WebDateBuilder());
    builders.put(MdWebDateTimeInfo.CLASS, new WebDateTimeBuilder());
    builders.put(MdWebTimeInfo.CLASS, new WebTimeBuilder());
    builders.put(MdWebDoubleInfo.CLASS, new WebDoubleBuilder());
    builders.put(MdWebDecimalInfo.CLASS, new WebDecimalBuilder());
    builders.put(MdWebFloatInfo.CLASS, new WebFloatBuilder());
    builders.put(MdWebGeoInfo.CLASS, new WebGeoBuilder());
    builders.put(MdWebIntegerInfo.CLASS, new WebIntegerBuilder());
    builders.put(MdWebLongInfo.CLASS, new WebLongBuilder());
    builders.put(MdWebHeaderInfo.CLASS, new WebHeaderBuilder());
    builders.put(MdWebMultipleTermInfo.CLASS, new WebMultipleTermBuilder());
    builders.put(MdWebSingleTermInfo.CLASS, new WebSingleTermBuilder());
    builders.put(MdWebSingleTermGridInfo.CLASS, new WebSingleTermGridBuilder());
    builders.put(MdWebTextInfo.CLASS, new WebTextBuilder());
    builders.put(MdWebReferenceInfo.CLASS, new WebReferenceBuilder());
  }

  /**
   * Returns the appropriate BuilderIF for the given field type.
   * 
   * @param type
   * @return
   */
  public static WebFieldBuilder getBuilder(MdFieldDTO field)
  {
    String type = field.getClassName();
    if (builders.containsKey(type))
    {
      return builders.get(type);
    }
    else
    {
      for (PluginIF plugin : pluginMap.values())
      {
        WebFieldBuilder builder = plugin.getBuilder(type);
        if (builder != null)
        {
          return builder;
        }
      }
    }

    String msg = "Could not find the builder for the field type [" + type + "].";
    throw new ConversionExceptionDTO(msg);
  }

  /**
   * Builder class to construct a WebField from an MdWebFieldDTO.
   * 
   */
  public static abstract class WebFieldBuilder
  {
    private WebFieldMdBuilder mdBuilder;

    protected WebFieldBuilder(WebFieldMdBuilder mdBuilder)
    {
      super();
      this.mdBuilder = mdBuilder;
    }

    protected WebFieldMdBuilder getMdBuilder()
    {
      return this.mdBuilder;
    }

    public abstract WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs);

    private MdWebGroupDTO getParentGroup(MdWebFieldDTO child)
    {
      List<? extends MdWebGroupDTO> groups = child.getAllGroupFields();
      return groups.isEmpty() ? null : groups.get(0);
    }

    private Condition getCondition(MdWebFieldDTO original, MdWebFieldDTO mdField)
    {
      if (mdField.getFieldCondition() != null)
      {
        ConditionBuilder cb = ConditionBuilders.getBuilder(mdField);
        return cb.create(mdField.getDefiningMdForm(), original, mdField.getFieldCondition());
      }
      else
      {
        return null;
      }
    }

    private void flattenConditions(WebField field, MdWebFieldDTO mdField)
    {
      Condition root = getCondition(mdField, mdField);

      MdWebGroupDTO parent = getParentGroup(mdField);
      while (parent != null)
      {
        Condition cond = getCondition(mdField, parent);
        if (cond != null)
        {

          // When flattening conditions, use the original field as the
          // referencingMdField and not the group
          // as there are no groups generated in the JavaScript.
          cond.getConditionMd().setReferencingMdField(mdField.getId());

          if (root != null)
          {
            root = ConditionBuilders.AndFieldConditionBuilder.newInstance(mdField, root, cond);
          }
          else
          {
            root = cond;
          }
        }
        parent = getParentGroup(parent);
      }

      if (root != null)
      {
        field.setCondition(root);
      }
    }

    /**
     * Copies the values of the attribute to the field.
     * 
     * @param attr
     * @param field
     * @param mdField
     *          TODO
     */
    protected void init(AttributeDTO attr, WebField field, MdWebFieldDTO mdField)
    {
      flattenConditions(field, mdField);

      if (attr != null)
      {
        field.setType(mdField.getType());

        field.setReadable(attr.isReadable());
        field.setWritable(attr.isWritable());
        field.setModified(attr.isModified());

        this.setValue(attr, field);
      }
      else
      {
        // there is no AttributeDTO, hence no underlying MdAttribute, so
        // this is a read-only/display field (e.g., Comment)
        field.setType(mdField.getType());

        field.setReadable(true);
        field.setWritable(false);
        field.setModified(false);

        field.setValue(null);
      }
    }

    public static Object invokeGetter(AttributeDTO attr, WebField field)
    {
      String accessor = "get" + CommonGenerationUtil.upperFirstCharacter(attr.getName());
      Object value;
      try
      {
        ComponentDTO dto = attr.getContainingDTO();
        value = LoaderDecorator.load(dto.getType() + TypeGeneratorInfo.DTO_SUFFIX).getMethod(accessor).invoke(dto);
      }
      catch (Throwable t)
      {
        String msg = "Could not copy the value of the attribute [" + attr.getName() + "] to the field [" + field.getFieldName() + "].";
        throw new ConversionExceptionDTO(msg, t);
      }

      return value;
    }

    protected Object setValue(AttributeDTO attr, WebField field)
    {
      if (attr.isReadable())
      {
        Object retValue;
        Object setValue;

        if (field instanceof WebReference)
        {
          // Don't invoke the type-safe getter for a reference or complex
          // because that's
          // an expensive call that fetches the referenced object. Instead we
          // simply want the id of the referenced object, which the generic
          // getter
          // will return.
          setValue = attr.getValue();
          retValue = setValue;
        }
        else if (field instanceof WebGeo || field instanceof WebSingleTerm)
        {
          // we want to return the full value but only set the id
          retValue = invokeGetter(attr, field);
          setValue = attr.getValue();
        }
        else
        {
          // all other attributes. The return value is the same as the set
          // value.
          setValue = invokeGetter(attr, field);
          retValue = setValue;
        }

        field.setValue(setValue);

        return retValue;
      }

      return null;
    }
  }

  private static class WebBreakBuilder extends WebFieldBuilder
  {
    public WebBreakBuilder()
    {
      super(new FieldMdBuilders.WebBreakMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebBreakMd fMd = (WebBreakMd) this.getMdBuilder().create(mdForm, mdField);
      WebBreak f = new WebBreak(fMd);

      this.init(null, f, mdField);

      return f;
    }
  }

  private static class WebCommentBuilder extends WebFieldBuilder
  {
    public WebCommentBuilder()
    {
      super(new FieldMdBuilders.WebCommentMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebCommentMd fMd = (WebCommentMd) this.getMdBuilder().create(mdForm, mdField);
      WebComment f = new WebComment(fMd);

      this.init(null, f, mdField);

      String comment = ( (MdWebCommentDTO) mdField ).getCommentText().getValue();
      f.setValue(comment);

      return f;
    }
  }

  private static class WebGeoBuilder extends WebFieldBuilder
  {
    public WebGeoBuilder()
    {
      super(new FieldMdBuilders.WebGeoMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebGeoMd fMd = (WebGeoMd) this.getMdBuilder().create(mdForm, mdField);
      WebGeo f = new WebGeo(fMd);

      AttributeReferenceDTO attr = (AttributeReferenceDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());

      this.init(attr, f, mdField);

      return f;
    }

    @Override
    protected Object setValue(AttributeDTO attr, WebField field)
    {
      Object geo = super.setValue(attr, field);
      WebGeoMd fMd = (WebGeoMd) field.getFieldMd();

      String geoDisplayLabel = geo != null ? geo.toString() : null;
      fMd.setGeoDisplayLabel(geoDisplayLabel);

      return geo;
    }
  }

  private static class WebHeaderBuilder extends WebFieldBuilder
  {
    public WebHeaderBuilder()
    {
      super(new FieldMdBuilders.WebHeaderMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebHeaderMd fMd = (WebHeaderMd) this.getMdBuilder().create(mdForm, mdField);
      WebHeader f = new WebHeader(fMd);

      this.init(null, f, mdField);

      String header = ( (MdWebHeaderDTO) mdField ).getHeaderText().getValue();
      f.setValue(header);

      return f;
    }
  }

  private static class WebSingleTermBuilder extends WebFieldBuilder
  {
    public WebSingleTermBuilder()
    {
      super(new FieldMdBuilders.WebSingleTermMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {

      WebSingleTermMd fMd = (WebSingleTermMd) this.getMdBuilder().create(mdForm, mdField);
      WebSingleTerm f = new WebSingleTerm(fMd);

      AttributeReferenceDTO attr = (AttributeReferenceDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());

      this.init(attr, f, mdField);

      return f;
    }

    @Override
    protected Object setValue(AttributeDTO attr, WebField field)
    {
      Object term = super.setValue(attr, field);
      WebSingleTermMd fMd = (WebSingleTermMd) field.getFieldMd();

      String termDisplayLabel = term != null ? term.toString() : null;
      fMd.setTermDisplayLabel(termDisplayLabel);

      return term;
    }
  }

  private static class WebMultipleTermBuilder extends WebFieldBuilder
  {
    public WebMultipleTermBuilder()
    {
      super(new FieldMdBuilders.WebMultipleTermMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebMultipleTermMd fMd = (WebMultipleTermMd) this.getMdBuilder().create(mdForm, mdField);
      WebMultipleTerm f = new WebMultipleTerm(fMd);

      AttributeReferenceDTO attr = (AttributeReferenceDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());

      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebSingleTermGridBuilder extends WebFieldBuilder
  {
    public WebSingleTermGridBuilder()
    {
      super(new FieldMdBuilders.WebSingleTermGridMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebSingleTermGridMd fMd = (WebSingleTermGridMd) this.getMdBuilder().create(mdForm, mdField);
      WebSingleTermGrid f = new WebSingleTermGrid(fMd);

      AttributeReferenceDTO attr = (AttributeReferenceDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());

      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebReferenceBuilder extends WebFieldBuilder
  {
    private WebReferenceBuilder()
    {
      super(new FieldMdBuilders.WebReferenceMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebReferenceMdBuilder mdBuilder = (WebReferenceMdBuilder) this.getMdBuilder();

      WebReferenceMd fMd = (WebReferenceMd) mdBuilder.create(mdForm, mdField);

      WebReference f = new WebReference(fMd);

      AttributeReferenceDTO attr = (AttributeReferenceDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      mdBuilder.setReferenceMetadata(fMd, attr.getAttributeMdDTO().getReferencedMdBusiness(), attr.getAttributeMdDTO().getReferencedDisplayLabel());

      this.init(attr, f, mdField);

      return f;
    }

  }

  private static abstract class WebPrimitiveBuilder extends WebFieldBuilder
  {
    public WebPrimitiveBuilder(FieldMdBuilders.WebPrimitiveMdBuilder builder)
    {
      super(builder);
    }
  }

  private static class WebBooleanBuilder extends WebPrimitiveBuilder
  {
    public WebBooleanBuilder()
    {
      super(new FieldMdBuilders.WebBooleanMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebBooleanMdBuilder mdBuilder = (WebBooleanMdBuilder) this.getMdBuilder();

      WebBooleanMd fMd = (WebBooleanMd) mdBuilder.create(mdForm, mdField);

      WebBoolean f = new WebBoolean(fMd);

      AttributeBooleanDTO attr = (AttributeBooleanDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      mdBuilder.setBooleanLabels(fMd, attr.getAttributeMdDTO().getPositiveDisplayLabel(), attr.getAttributeMdDTO().getNegativeDisplayLabel());

      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebCharacterBuilder extends WebPrimitiveBuilder
  {
    public WebCharacterBuilder()
    {
      super(new FieldMdBuilders.WebCharacterMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebCharacterMd fMd = (WebCharacterMd) this.getMdBuilder().create(mdForm, mdField);
      WebCharacter f = new WebCharacter(fMd);

      AttributeCharacterDTO attr = (AttributeCharacterDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebTextBuilder extends WebPrimitiveBuilder
  {
    public WebTextBuilder()
    {
      super(new FieldMdBuilders.WebTextMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebTextMd fMd = (WebTextMd) this.getMdBuilder().create(mdForm, mdField);
      WebText f = new WebText(fMd);

      AttributeTextDTO attr = (AttributeTextDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebDoubleBuilder extends WebPrimitiveBuilder
  {
    public WebDoubleBuilder()
    {
      super(new FieldMdBuilders.WebDoubleMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebDoubleMd fMd = (WebDoubleMd) this.getMdBuilder().create(mdForm, mdField);
      WebDouble f = new WebDouble(fMd);

      AttributeDoubleDTO attr = (AttributeDoubleDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebDecimalBuilder extends WebPrimitiveBuilder
  {
    public WebDecimalBuilder()
    {
      super(new FieldMdBuilders.WebDecimalMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebDecimalMd fMd = (WebDecimalMd) this.getMdBuilder().create(mdForm, mdField);
      WebDecimal f = new WebDecimal(fMd);

      AttributeDecimalDTO attr = (AttributeDecimalDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebLongBuilder extends WebPrimitiveBuilder
  {
    public WebLongBuilder()
    {
      super(new FieldMdBuilders.WebLongMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebLongMd fMd = (WebLongMd) this.getMdBuilder().create(mdForm, mdField);
      WebLong f = new WebLong(fMd);

      AttributeLongDTO attr = (AttributeLongDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebFloatBuilder extends WebPrimitiveBuilder
  {
    public WebFloatBuilder()
    {
      super(new FieldMdBuilders.WebFloatMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebFloatMd fMd = (WebFloatMd) this.getMdBuilder().create(mdForm, mdField);
      WebFloat f = new WebFloat(fMd);

      AttributeFloatDTO attr = (AttributeFloatDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebIntegerBuilder extends WebPrimitiveBuilder
  {
    public WebIntegerBuilder()
    {
      super(new FieldMdBuilders.WebIntegerMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebIntegerMd fMd = (WebIntegerMd) this.getMdBuilder().create(mdForm, mdField);
      WebInteger f = new WebInteger(fMd);

      AttributeIntegerDTO attr = (AttributeIntegerDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebDateBuilder extends WebPrimitiveBuilder
  {
    public WebDateBuilder()
    {
      super(new FieldMdBuilders.WebDateMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebDateMd fMd = (WebDateMd) this.getMdBuilder().create(mdForm, mdField);
      WebDate f = new WebDate(fMd);

      AttributeDateDTO attr = (AttributeDateDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebDateTimeBuilder extends WebPrimitiveBuilder
  {
    public WebDateTimeBuilder()
    {
      super(new FieldMdBuilders.WebDateTimeMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebDateTimeMd fMd = (WebDateTimeMd) this.getMdBuilder().create(mdForm, mdField);
      WebDateTime f = new WebDateTime(fMd);

      AttributeDateTimeDTO attr = (AttributeDateTimeDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }

  private static class WebTimeBuilder extends WebPrimitiveBuilder
  {
    public WebTimeBuilder()
    {
      super(new FieldMdBuilders.WebTimeMdBuilder());
    }

    @Override
    public WebField create(MdFormDTO mdForm, MdWebFieldDTO mdField, ComponentDTOIF data, Map<String, AttributeDTO> mdIdToAttrDTOs)
    {
      WebTimeMd fMd = (WebTimeMd) this.getMdBuilder().create(mdForm, mdField);
      WebTime f = new WebTime(fMd);

      AttributeTimeDTO attr = (AttributeTimeDTO) mdIdToAttrDTOs.get(fMd.getDefiningMdAttribute());
      this.init(attr, f, mdField);

      return f;
    }
  }
}
