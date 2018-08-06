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
package com.runwaysdk.form.web.metadata;

import com.runwaysdk.system.metadata.MdAttributeConcreteDTO;
import com.runwaysdk.system.metadata.MdClassDTO;
import com.runwaysdk.system.metadata.MdFormDTO;
import com.runwaysdk.system.metadata.MdWebAttributeDTO;
import com.runwaysdk.system.metadata.MdWebCharacterDTO;
import com.runwaysdk.system.metadata.MdWebDateDTO;
import com.runwaysdk.system.metadata.MdWebDecDTO;
import com.runwaysdk.system.metadata.MdWebFieldDTO;
import com.runwaysdk.system.metadata.MdWebNumberDTO;
import com.runwaysdk.system.metadata.MdWebPrimitiveDTO;
import com.runwaysdk.system.metadata.MdWebTextDTO;

/**
 * Creates
 * 
 * @author justin
 * 
 */
public class FieldMdBuilders
{
  /**
   * MdBuilder class to construct a WebField from an MdWebFieldDTO.
   * 
   */
  public static abstract class WebFieldMdBuilder
  {
    public abstract WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField);

    /**
     * Sets the properties from the source MdWebFieldDTO to the WebFieldMd
     * destination object..
     * 
     * @param mdField
     */
    protected void init(MdWebFieldDTO mdField, WebFieldMd md)
    {
      md.setOid(mdField.getOid());
      md.setDefiningMdForm(mdField.getDefiningMdFormId());
      md.setDisplayLabel(mdField.getDisplayLabel().getValue());
      md.setDescription(mdField.getDescription().getValue());
      md.setFieldName(mdField.getFieldName());
      md.setFieldOrder(mdField.getFieldOrder());

      // FIXME do proper override mechanism
      md.setRequired(mdField.getRequired()); // defaults to false for now
    }
  }

  public static class WebReferenceMdBuilder extends WebAttributeMdBuilder
  {
    /**
     * FIXME remove this method once the metadata is defined on MdWebReference.
     * 
     */
    public void setReferenceMetadata(WebReferenceMd fMd, String referencedMdBusiness, String referencedDisplayLabel)
    {
      fMd.setReferencedMdBusiness(referencedMdBusiness);
      fMd.setReferencedDisplayLabel(referencedDisplayLabel);
    }

    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebReferenceMd fMd = new WebReferenceMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebBreakMdBuilder extends WebFieldMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebBreakMd fMd = new WebBreakMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebCommentMdBuilder extends WebFieldMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebCommentMd fMd = new WebCommentMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebGeoMdBuilder extends WebAttributeMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebGeoMd fMd = new WebGeoMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebHeaderMdBuilder extends WebFieldMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebHeaderMd fMd = new WebHeaderMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebSingleTermMdBuilder extends WebAttributeMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebSingleTermMd fMd = new WebSingleTermMd();

      this.init(mdField, fMd);

      return fMd;
    }

  }

  public static class WebMultipleTermMdBuilder extends WebAttributeMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebMultipleTermMd fMd = new WebMultipleTermMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebSingleTermGridMdBuilder extends WebAttributeMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebSingleTermGridMd fMd = new WebSingleTermGridMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static abstract class WebAttributeMdBuilder extends WebFieldMdBuilder
  {
    @Override
    protected void init(MdWebFieldDTO mdField, WebFieldMd md)
    {
      super.init(mdField, md);

      WebAttributeMd mdP = (WebAttributeMd) md;

      MdWebAttributeDTO webAttr = (MdWebAttributeDTO) mdField;
      String definingMdAttrId = webAttr.getDefiningMdAttributeId();

      MdAttributeConcreteDTO attrDTO = MdAttributeConcreteDTO.get(mdField.getRequest(), definingMdAttrId);
      MdClassDTO definingClass = attrDTO.getDefiningMdClass();
      String clazz = definingClass.getPackageName() + "." + definingClass.getTypeName();
      String name = attrDTO.getAttributeName();

      mdP.setDefiningMdAttribute(definingMdAttrId);
      mdP.setDefiningAttribute(name);
      mdP.setDefiningClass(clazz);
      mdP.setShowOnSearch(webAttr.getShowOnSearch());
      mdP.setShowOnViewAll(webAttr.getShowOnViewAll());
    }
  }

  public static abstract class WebPrimitiveMdBuilder extends WebAttributeMdBuilder
  {
    @Override
    protected void init(MdWebFieldDTO mdField, WebFieldMd md)
    {
      super.init(mdField, md);

      MdWebPrimitiveDTO mdFieldC = (MdWebPrimitiveDTO) mdField;
      WebPrimitiveMd mdC = (WebPrimitiveMd) md;

      mdC.setIsExpression(mdFieldC.getIsExpression());
      mdC.setExpression(mdFieldC.getExpression());
    }
  }

  public static class WebBooleanMdBuilder extends WebPrimitiveMdBuilder
  {

    /**
     * FIXME remove this method once the metadata defines a positive/negative
     * label for MdWebBoolean.
     * 
     * @param md
     * @param positive
     * @param negative
     */
    public void setBooleanLabels(WebBooleanMd md, String positive, String negative)
    {
      md.setPositiveDisplayLabel(positive);
      md.setNegativeDisplayLabel(negative);
    }

    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebBooleanMd fMd = new WebBooleanMd();

      this.init(mdField, fMd);

      return fMd;
    }
  }

  public static class WebCharacterMdBuilder extends WebPrimitiveMdBuilder
  {
    @Override
    protected void init(MdWebFieldDTO mdField, WebFieldMd md)
    {
      super.init(mdField, md);

      MdWebCharacterDTO mdFieldC = (MdWebCharacterDTO) mdField;
      WebCharacterMd mdC = (WebCharacterMd) md;

      mdC.setDisplayLength(mdFieldC.getDisplayLength());
      mdC.setMaxLength(mdFieldC.getMaxLength());
    }

    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebCharacterMd fMd = new WebCharacterMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebTextMdBuilder extends WebPrimitiveMdBuilder
  {
    @Override
    protected void init(MdWebFieldDTO mdField, WebFieldMd md)
    {
      super.init(mdField, md);

      MdWebTextDTO mdFieldT = (MdWebTextDTO) mdField;
      WebTextMd mdT = (WebTextMd) md;

      mdT.setWidth(mdFieldT.getWidth());
      mdT.setHeight(mdFieldT.getHeight());
    }

    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebTextMd fMd = new WebTextMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  private abstract static class WebNumberMdBuilder extends WebPrimitiveMdBuilder
  {
    @Override
    protected void init(MdWebFieldDTO mdField, WebFieldMd md)
    {
      super.init(mdField, md);

      MdWebNumberDTO mdFieldT = (MdWebNumberDTO) mdField;
      WebNumberMd mdT = (WebNumberMd) md;

      mdT.setStartRange(mdFieldT.getStartRange());
      mdT.setEndRange(mdFieldT.getEndRange());
    }
  }

  public abstract static class WebDecMdBuilder extends WebNumberMdBuilder
  {

    @Override
    protected void init(MdWebFieldDTO mdField, WebFieldMd md)
    {
      super.init(mdField, md);

      MdWebDecDTO mdFieldT = (MdWebDecDTO) mdField;
      WebDecMd mdT = (WebDecMd) md;

      mdT.setDecPrecision(mdFieldT.getDecPrecision());
      mdT.setDecScale(mdFieldT.getDecScale());
    }

  }

  public static class WebDoubleMdBuilder extends WebDecMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebDoubleMd fMd = new WebDoubleMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebDecimalMdBuilder extends WebDecMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebDecimalMd fMd = new WebDecimalMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebFloatMdBuilder extends WebDecMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebFloatMd fMd = new WebFloatMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebIntegerMdBuilder extends WebNumberMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebIntegerMd fMd = new WebIntegerMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebLongMdBuilder extends WebNumberMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebLongMd fMd = new WebLongMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebDateMdBuilder extends WebPrimitiveMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebDateMd fMd = new WebDateMd();

      this.init(mdField, fMd);
      return fMd;
    }

    @Override
    protected void init(MdWebFieldDTO mdField, WebFieldMd md)
    {
      super.init(mdField, md);

      MdWebDateDTO mdFieldT = (MdWebDateDTO) mdField;
      WebDateMd mdT = (WebDateMd) md;

      mdT.setAfterTodayExclusive(mdFieldT.getAfterTodayExclusive());
      mdT.setAfterTodayInclusive(mdFieldT.getAfterTodayInclusive());
      mdT.setBeforeTodayExclusive(mdFieldT.getBeforeTodayExclusive());
      mdT.setBeforeTodayInclusive(mdFieldT.getBeforeTodayInclusive());
      mdT.setStartDate(mdFieldT.getStartDate());
      mdT.setEndDate(mdFieldT.getEndDate());
    }
  }

  public static class WebDateTimeMdBuilder extends WebPrimitiveMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebDateTimeMd fMd = new WebDateTimeMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

  public static class WebTimeMdBuilder extends WebPrimitiveMdBuilder
  {
    @Override
    public WebFieldMd create(MdFormDTO mdForm, MdWebFieldDTO mdField)
    {
      WebTimeMd fMd = new WebTimeMd();

      this.init(mdField, fMd);
      return fMd;
    }
  }

}
