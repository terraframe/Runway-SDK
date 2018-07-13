
/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK GIS(tm). If not, see <http://www.gnu.org/licenses/>.
 */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.runwaysdk.business.generation.GenerationManager;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.patcher.RunwayPatcher;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;
import com.runwaysdk.system.metadata.MdType;
import com.runwaysdk.system.metadata.MdTypeQuery;

public class Sandbox
{

  private static Log     log     = LogFactory.getLog(Sandbox.class);

  private static boolean logging = true;

  public static void main(String[] args) throws Exception
  {
    // test();
    RunwayPatcher.main(new String[] { "postgres", "postgres", "postgres", "true" });
  }

  @Request
  private static void test()
  {
    System.out.println("Starting");

    MdTypeQuery mtq = new MdTypeQuery(new QueryFactory());
    mtq.WHERE(mtq.getPackageName().EQ("com.runwaysdk.system.gis.geo"));
    OIterator<? extends MdType> it = mtq.getIterator();
    while (it.hasNext())
    {
      System.out.println("Regenerating " + mtq.getTypeName());

      MdType mdt = it.next();
      GenerationManager.generate(MdTypeDAO.get(mdt.getId()));
    }

    System.out.println("Ending");
  }

  @Request
  private static void buildMetadata()
  {
    // buildMetadataInTransaction();
  }

  // @Transaction
  // public static void buildMetadataInTransaction()
  // {
  // String PACKAGE = "com.runwaysdk.system.gis.geo";
  //
  // MdTermDAO universal = MdTermDAO.newInstance();
  // universal.setValue(MdBusinessInfo.PACKAGE, PACKAGE);
  // universal.setValue(MdBusinessInfo.NAME, "Universal");
  // universal.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale",
  // "Universal");
  // universal.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale",
  // "Universal");
  // universal.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
  // universal.setGenerateMdController(false);
  // universal.apply();
  //
  // MdAttributeCharacterDAO universalName =
  // MdAttributeCharacterDAO.newInstance();
  // universalName.setValue(MdAttributeCharacterInfo.NAME, "name");
  // universalName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Name");
  // universalName.setStructValue(MdAttributeCharacterInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Name");
  // universalName.setValue(MdAttributeCharacterInfo.INDEX_TYPE,
  // IndexTypes.UNIQUE_INDEX.getId());
  // universalName.setValue(MdAttributeCharacterInfo.REQUIRED,
  // MdAttributeBooleanInfo.TRUE);
  // universalName.setValue(MdAttributeCharacterInfo.IMMUTABLE,
  // MdAttributeBooleanInfo.FALSE);
  // universalName.setValue(MdAttributeCharacterInfo.SIZE, "100");
  // universalName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS,
  // universal.getId());
  // universalName.apply();
  //
  // MdAttributeLocalCharacterDAO displayLabel =
  // MdAttributeLocalCharacterDAO.newInstance();
  // displayLabel.setValue(MdAttributeStructInfo.NAME, "displayLabel");
  // displayLabel.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Display label");
  // displayLabel.setStructValue(MdAttributeStructInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Display label");
  // displayLabel.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS,
  // universal.getId());
  // displayLabel.setValue(MdAttributeLocalCharacterInfo.REQUIRED,
  // MdAttributeBooleanInfo.TRUE);
  // displayLabel.apply();
  //
  // MdAttributeLocalCharacterDAO description =
  // MdAttributeLocalCharacterDAO.newInstance();
  // description.setValue(MdAttributeStructInfo.NAME, "description");
  // description.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Description");
  // description.setStructValue(MdAttributeStructInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Description");
  // description.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS,
  // universal.getId());
  // description.setValue(MdAttributeLocalCharacterInfo.REQUIRED,
  // MdAttributeBooleanInfo.TRUE);
  // description.apply();
  //
  // MdTermRelationshipDAO allowedIn = MdTermRelationshipDAO.newInstance();
  // allowedIn.setValue(MdTreeInfo.NAME, "AllowedIn");
  // allowedIn.setValue(MdTreeInfo.PACKAGE, PACKAGE);
  // allowedIn.setStructValue(MdTreeInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Allowed In");
  // allowedIn.setValue(MdTreeInfo.PARENT_MD_BUSINESS, universal.getId());
  // allowedIn.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
  // allowedIn.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Universal");
  // allowedIn.setValue(MdTreeInfo.CHILD_MD_BUSINESS, universal.getId());
  // allowedIn.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
  // allowedIn.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Universal");
  // allowedIn.setValue(MdTreeInfo.PARENT_METHOD, "AllowedIn");
  // allowedIn.setValue(MdTreeInfo.CHILD_METHOD, "Contains");
  // allowedIn.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE,
  // AssociationType.TREE.getId());
  // allowedIn.setGenerateMdController(false);
  // allowedIn.apply();
  //
  // MdTermDAO geoEntity = MdTermDAO.newInstance();
  // geoEntity.setValue(MdBusinessInfo.PACKAGE, PACKAGE);
  // geoEntity.setValue(MdBusinessInfo.NAME, "GeoEntity");
  // geoEntity.setStructValue(MdBusinessInfo.DESCRIPTION, "defaultLocale", "Geo
  // Entity");
  // geoEntity.setStructValue(MdBusinessInfo.DISPLAY_LABEL, "defaultLocale",
  // "Geo Entity");
  // geoEntity.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
  // geoEntity.setGenerateMdController(false);
  // geoEntity.apply();
  //
  // MdAttributeCharacterDAO geoId = MdAttributeCharacterDAO.newInstance();
  // geoId.setValue(MdAttributeCharacterInfo.NAME, "geoId");
  // geoId.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Geo Id");
  // geoId.setStructValue(MdAttributeCharacterInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Geo Id");
  // geoId.setValue(MdAttributeCharacterInfo.INDEX_TYPE,
  // IndexTypes.UNIQUE_INDEX.getId());
  // geoId.setValue(MdAttributeCharacterInfo.REQUIRED,
  // MdAttributeBooleanInfo.TRUE);
  // geoId.setValue(MdAttributeCharacterInfo.IMMUTABLE,
  // MdAttributeBooleanInfo.FALSE);
  // geoId.setValue(MdAttributeCharacterInfo.SIZE, "50");
  // geoId.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS,
  // geoEntity.getId());
  // geoId.apply();
  //
  // MdAttributeTextDAO wtk = MdAttributeTextDAO.newInstance();
  // wtk.setValue(MdAttributeTextInfo.NAME, "wtk");
  // wtk.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "WTK");
  // wtk.setStructValue(MdAttributeTextInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "The WKT string of the geometry");
  // wtk.setValue(MdAttributeTextInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
  // wtk.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
  // wtk.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
  // wtk.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, geoEntity.getId());
  // wtk.apply();
  //
  // MdAttributeReferenceDAO universalAttribute =
  // MdAttributeReferenceDAO.newInstance();
  // universalAttribute.setValue(MdAttributeReferenceInfo.NAME, "universal");
  // universalAttribute.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Universal");
  // universalAttribute.setStructValue(MdAttributeReferenceInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Universal");
  // universalAttribute.setValue(MdAttributeReferenceInfo.INDEX_TYPE,
  // IndexTypes.NO_INDEX.getId());
  // universalAttribute.setValue(MdAttributeReferenceInfo.REQUIRED,
  // MdAttributeBooleanInfo.TRUE);
  // universalAttribute.setValue(MdAttributeReferenceInfo.IMMUTABLE,
  // MdAttributeBooleanInfo.TRUE);
  // universalAttribute.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS,
  // geoEntity.getId());
  // universalAttribute.setValue(MdAttributeReferenceInfo.REF_MD_BUSINESS,
  // universal.getId());
  // universalAttribute.apply();
  //
  // MdAttributeLocalCharacterDAO entityName =
  // MdAttributeLocalCharacterDAO.newInstance();
  // entityName.setValue(MdAttributeStructInfo.NAME, "entityName");
  // entityName.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Entity name");
  // entityName.setStructValue(MdAttributeStructInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Localized name of the geo entity");
  // entityName.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS,
  // geoEntity.getId());
  // entityName.setValue(MdAttributeLocalCharacterInfo.REQUIRED,
  // MdAttributeBooleanInfo.TRUE);
  // entityName.apply();
  //
  // MdAttributePointDAO geoPoint = MdAttributePointDAO.newInstance();
  // geoPoint.setValue(MdAttributePointInfo.NAME, "geoPoint");
  // geoPoint.setStructValue(MdAttributePointInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Geo Point");
  // geoPoint.setStructValue(MdAttributePointInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Geo Point");
  // geoPoint.setValue(MdAttributePointInfo.INDEX_TYPE,
  // IndexTypes.NO_INDEX.getId());
  // geoPoint.setValue(MdAttributePointInfo.REQUIRED,
  // MdAttributeBooleanInfo.FALSE);
  // geoPoint.setValue(MdAttributePointInfo.IMMUTABLE,
  // MdAttributeBooleanInfo.FALSE);
  // geoPoint.setValue(MdAttributePointInfo.DEFINING_MD_CLASS,
  // geoEntity.getId());
  // geoPoint.setValue(MdAttributePointInfo.SRID, "4326");
  // geoPoint.setValue(MdAttributePointInfo.DIMENSION, "2");
  // geoPoint.apply();
  //
  // MdAttributeMultiPolygonDAO geoMultiPolygon =
  // MdAttributeMultiPolygonDAO.newInstance();
  // geoMultiPolygon.setValue(MdAttributeMultiPolygonInfo.NAME,
  // "geoMultiPolygon");
  // geoMultiPolygon.setStructValue(MdAttributeMultiPolygonInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Geo Multi Polygon");
  // geoMultiPolygon.setStructValue(MdAttributeMultiPolygonInfo.DESCRIPTION,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Geo Multi Polygon");
  // geoMultiPolygon.setValue(MdAttributeMultiPolygonInfo.INDEX_TYPE,
  // IndexTypes.NO_INDEX.getId());
  // geoMultiPolygon.setValue(MdAttributeMultiPolygonInfo.REQUIRED,
  // MdAttributeBooleanInfo.FALSE);
  // geoMultiPolygon.setValue(MdAttributeMultiPolygonInfo.IMMUTABLE,
  // MdAttributeBooleanInfo.FALSE);
  // geoMultiPolygon.setValue(MdAttributeMultiPolygonInfo.DEFINING_MD_CLASS,
  // geoEntity.getId());
  // geoMultiPolygon.setValue(MdAttributeMultiPolygonInfo.SRID, "4326");
  // geoMultiPolygon.setValue(MdAttributeMultiPolygonInfo.DIMENSION, "2");
  // geoMultiPolygon.apply();
  //
  // MdTermRelationshipDAO locatedIn = MdTermRelationshipDAO.newInstance();
  // locatedIn.setValue(MdTreeInfo.NAME, "LocatedIn");
  // locatedIn.setValue(MdTreeInfo.PACKAGE, PACKAGE);
  // locatedIn.setStructValue(MdTreeInfo.DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Located In");
  // locatedIn.setValue(MdTreeInfo.PARENT_MD_BUSINESS, geoEntity.getId());
  // locatedIn.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
  // locatedIn.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent geo entity");
  // locatedIn.setValue(MdTreeInfo.CHILD_MD_BUSINESS, geoEntity.getId());
  // locatedIn.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
  // locatedIn.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL,
  // MdAttributeLocalInfo.DEFAULT_LOCALE, "Child geo entity");
  // locatedIn.setValue(MdTreeInfo.PARENT_METHOD, "LocatedIn");
  // locatedIn.setValue(MdTreeInfo.CHILD_METHOD, "Contains");
  // locatedIn.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE,
  // AssociationType.TREE.getId());
  // locatedIn.setGenerateMdController(false);
  // locatedIn.apply();
  //
  // BusinessDAO rootUniversal =
  // BusinessDAO.newInstance(universal.definesType());
  // rootUniversal.setValue("name", "ROOT");
  // rootUniversal.setValue(ComponentInfo.KEY, Universal.KEY_PREFIX + "ROOT");
  // rootUniversal.setStructValue("displayLabel",
  // MdAttributeLocalCharacterInfo.DEFAULT_LOCALE, "Root");
  // rootUniversal.setStructValue("description",
  // MdAttributeLocalCharacterInfo.DEFAULT_LOCALE, "Root");
  // rootUniversal.apply();
  //
  // BusinessDAO rootEntity = BusinessDAO.newInstance(geoEntity.definesType());
  // rootEntity.setValue("geoId", "ROOT");
  // rootEntity.setValue(ComponentInfo.KEY, "ROOT");
  // rootEntity.setStructValue("entityName",
  // MdAttributeLocalCharacterInfo.DEFAULT_LOCALE, "Root");
  // rootEntity.setValue("universal", rootUniversal.getId());
  // rootEntity.apply();
  // }
  //
}
