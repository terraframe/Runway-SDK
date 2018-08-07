--
-- Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
--   
-- This file is part of Runway SDK(tm).
-- 
-- Runway SDK(tm) is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Lesser General Public License as
-- published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version.
-- 
-- Runway SDK(tm) is distributed in the hope that it will be useful, but
-- WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU Lesser General Public License for more details.
-- 
-- You should have received a copy of the GNU Lesser General Public
-- License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
--

/*********************************************************************
 * Converting text attribute to the clob datatype
 *********************************************************************/

-- converts the text attribute md_facade.stub_source to a character attribute.
-- Create the new record
-- Old Id:0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139
-- New Id:0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010
--                                        20101231NM0000000000000000000010

-- Dimension Ids:
-- MALARIA:7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73
-- DENGUE :0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73
-- converts the text attribute md_attribute_enumeration.cache_column_name to a character attribute.

-- site_master:dss.vector.solutions

BEGIN;

-- Create the new record
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

-- Update the ids and the type fields in the object tables
UPDATE md_attribute_character
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE md_attribute_char
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE md_attribute_primitive
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE md_attribute_concrete
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE md_attribute
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE metadata
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE problem_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE dimension_def_struct_attr
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE dimension_has_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE dimension_has_class
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE view_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE business_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE md_method_method_actor
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE index_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE class_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE warning_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE entity_index
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE enumeration_attribute_item
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE relationship_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE information_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE class_attribute_virtual
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE exception_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE enumeration_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE util_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE class_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE attribute_has_dimension
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE virtualize_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE class_has_dimension
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE class_attribute_concrete
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE metadata_parameter
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE metadata_relationship
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE index_attribute
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE class_attribute
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE type_permissions
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE class_attribute_virtual
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE class_attribute_concrete
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE metadata_relationship
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE sort_md_attribute = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE object_tuple
   SET md_attribute_concrete = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE md_attribute_concrete = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE type_tuple
   SET metadata = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE metadata = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE md_parameter
   SET metadata = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE metadata = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE md_attribute_concrete = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010'
 WHERE defining_md_attribute = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue00000000000000000000000000000139';

-- Updating item_ids in MdEnumeration tables.
 
-- create MALARIA dimension records for GeoEntity.geoData
/*
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('zlbl7sdycunvvub5d6wcsea3o2irhvapNM200904120000000000000000000030', 'dss.vector.solutions', 'zlbl7sdycunvvub5d6wcsea3o2irhvapNM200904120000000000000000000030', NULL, NULL, NULL);
INSERT INTO md_attribute_dimension (required, defining_md_dimension, defining_md_attribute, default_value, oid) VALUES (0, '7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010', NULL, 'rccixp0hjlue0te2x2dkhwpgn9ogj3z0szii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308817, 'com.runwaysdk.system.metadata.MdAttributeDimension', '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'rccixp0hjlue0te2x2dkhwpgn9ogj3z0szii8pjdf8hmj16pi3csomvwxphpzp74', '2011-01-13 11:42:36', 'zlbl7sdycunvvub5d6wcsea3o2irhvapNM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', 1, NULL, '2011-01-13 11:42:36', 'rccixp0hjlue0te2x2dkhwpgn9ogj3z0szii8pjdf8hmj16pi3csomvwxphpzp74', '0000000000000000000000000000001000000000000000000000000000000003', 'dss.vector.solutions');
INSERT INTO dimension_has_attribute (oid, parent_oid, child_oid) VALUES ('xebtp0zpfow339v13ipbmjj92cyu4c1mqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', 'rccixp0hjlue0te2x2dkhwpgn9ogj3z0szii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasAttribute', NULL, '2011-01-13 11:42:36', 'xebtp0zpfow339v13ipbmjj92cyu4c1mqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0000000000000000000000000000001000000000000000000000000000000003', 308818, NULL, 'xebtp0zpfow339v13ipbmjj92cyu4c1mqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 11:42:36', '7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', 'rccixp0hjlue0te2x2dkhwpgn9ogj3z0szii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO attribute_has_dimension (oid, parent_oid, child_oid) VALUES ('xjdrh1fxx5gb05zptvbpre4h7id13cp1088krl4llbs2hzg1n65ub3mavye46rhe', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010', 'rccixp0hjlue0te2x2dkhwpgn9ogj3z0szii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.AttributeHasDimension', NULL, '2011-01-13 11:42:36', 'xjdrh1fxx5gb05zptvbpre4h7id13cp1088krl4llbs2hzg1n65ub3mavye46rhe', '0000000000000000000000000000001000000000000000000000000000000003', 308819, NULL, 'xjdrh1fxx5gb05zptvbpre4h7id13cp1088krl4llbs2hzg1n65ub3mavye46rhe', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 11:42:36', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010', 'rccixp0hjlue0te2x2dkhwpgn9ogj3z0szii8pjdf8hmj16pi3csomvwxphpzp74');

-- create DENGUE dimension records
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('quxfvs49q3che2zo2plrh34v084b0lwrNM200904120000000000000000000030', 'dss.vector.solutions', 'quxfvs49q3che2zo2plrh34v084b0lwrNM200904120000000000000000000030', NULL, NULL, NULL);
INSERT INTO md_attribute_dimension (required, defining_md_dimension, defining_md_attribute, default_value, oid) VALUES (0, '0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010', NULL, '8b5mwgzbdw835bv4x0wpv1s39vsel2vjszii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308820, 'com.runwaysdk.system.metadata.MdAttributeDimension', '0000000000000000000000000000001000000000000000000000000000000003', NULL, '8b5mwgzbdw835bv4x0wpv1s39vsel2vjszii8pjdf8hmj16pi3csomvwxphpzp74', '2011-01-13 11:42:36', 'quxfvs49q3che2zo2plrh34v084b0lwrNM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', 1, NULL, '2011-01-13 11:42:36', '8b5mwgzbdw835bv4x0wpv1s39vsel2vjszii8pjdf8hmj16pi3csomvwxphpzp74', '0000000000000000000000000000001000000000000000000000000000000003', 'dss.vector.solutions');
INSERT INTO dimension_has_attribute (oid, parent_oid, child_oid) VALUES ('r9md68kptuicegqet807f5x4rmsjgaybqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', '8b5mwgzbdw835bv4x0wpv1s39vsel2vjszii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasAttribute', NULL, '2011-01-13 11:42:36', 'r9md68kptuicegqet807f5x4rmsjgaybqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0000000000000000000000000000001000000000000000000000000000000003', 308821, NULL, 'r9md68kptuicegqet807f5x4rmsjgaybqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 11:42:36', '0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', '8b5mwgzbdw835bv4x0wpv1s39vsel2vjszii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO attribute_has_dimension (oid, parent_oid, child_oid) VALUES ('zit0zz2growxwkr52jkdregqwkwlgles088krl4llbs2hzg1n65ub3mavye46rhe', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010', '8b5mwgzbdw835bv4x0wpv1s39vsel2vjszii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.AttributeHasDimension', NULL, '2011-01-13 11:42:36', 'zit0zz2growxwkr52jkdregqwkwlgles088krl4llbs2hzg1n65ub3mavye46rhe', '0000000000000000000000000000001000000000000000000000000000000003', 308822, NULL, 'zit0zz2growxwkr52jkdregqwkwlgles088krl4llbs2hzg1n65ub3mavye46rhe', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 11:42:36', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue20101231NM0000000000000000000010', '8b5mwgzbdw835bv4x0wpv1s39vsel2vjszii8pjdf8hmj16pi3csomvwxphpzp74');
*/
 
-- update dimension records for MdAttributeClob

-- malaria 
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('2bcmrc45fdrtfq0jk7w923yo7za1uz5qNM200904120000000000000000000030', 'dss.vector.solutions', '2bcmrc45fdrtfq0jk7w923yo7za1uz5qNM200904120000000000000000000030', NULL, NULL, NULL);
INSERT INTO md_class_dimension (defining_md_dimension, defining_md_class, oid) VALUES ('7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', '20101231NM000000000000000000001000000000000000000000000000000001', 'qom0w1n0vsugvu7zi2vu8kzgyfpbws6wJS062220100000000000000000000005');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308829, 'com.runwaysdk.system.metadata.MdClassDimension', '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'qom0w1n0vsugvu7zi2vu8kzgyfpbws6wJS062220100000000000000000000005', '2011-01-13 13:25:12', '2bcmrc45fdrtfq0jk7w923yo7za1uz5qNM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', 1, NULL, '2011-01-13 13:25:12', 'qom0w1n0vsugvu7zi2vu8kzgyfpbws6wJS062220100000000000000000000005', '0000000000000000000000000000001000000000000000000000000000000003', 'dss.vector.solutions');
INSERT INTO dimension_has_class (oid, parent_oid, child_oid) VALUES ('0ax7wtqfj5z0j0cyaef9l98g9kny03s5JS062220100000000000000000000007', '7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', 'qom0w1n0vsugvu7zi2vu8kzgyfpbws6wJS062220100000000000000000000005');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasClass', NULL, '2011-01-13 13:25:12', '0ax7wtqfj5z0j0cyaef9l98g9kny03s5JS062220100000000000000000000007', '0000000000000000000000000000001000000000000000000000000000000003', 308830, NULL, '0ax7wtqfj5z0j0cyaef9l98g9kny03s5JS062220100000000000000000000007', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 13:25:12', '7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', 'qom0w1n0vsugvu7zi2vu8kzgyfpbws6wJS062220100000000000000000000005');
INSERT INTO class_has_dimension (oid, parent_oid, child_oid) VALUES ('qltv1dxtvee6vtjwgjbstz5qpngkrubnJS062220100000000000000000000006', '20101231NM000000000000000000001000000000000000000000000000000001', 'qom0w1n0vsugvu7zi2vu8kzgyfpbws6wJS062220100000000000000000000005');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.ClassHasDimension', NULL, '2011-01-13 13:25:12', 'qltv1dxtvee6vtjwgjbstz5qpngkrubnJS062220100000000000000000000006', '0000000000000000000000000000001000000000000000000000000000000003', 308831, NULL, 'qltv1dxtvee6vtjwgjbstz5qpngkrubnJS062220100000000000000000000006', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 13:25:12', '20101231NM000000000000000000001000000000000000000000000000000001', 'qom0w1n0vsugvu7zi2vu8kzgyfpbws6wJS062220100000000000000000000005');

-- dengue
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('0oru1lyky2nspaz0ivsvx9lky3isr4p8NM200904120000000000000000000030', 'dss.vector.solutions', '0oru1lyky2nspaz0ivsvx9lky3isr4p8NM200904120000000000000000000030', NULL, NULL, NULL);
INSERT INTO md_class_dimension (defining_md_dimension, defining_md_class, oid) VALUES ('0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', '20101231NM000000000000000000001000000000000000000000000000000001', 'sys2sjm5ex8qn1kr6s4t2mt78w24ruldJS062220100000000000000000000005');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308832, 'com.runwaysdk.system.metadata.MdClassDimension', '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'sys2sjm5ex8qn1kr6s4t2mt78w24ruldJS062220100000000000000000000005', '2011-01-13 13:25:12', '0oru1lyky2nspaz0ivsvx9lky3isr4p8NM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', 1, NULL, '2011-01-13 13:25:12', 'sys2sjm5ex8qn1kr6s4t2mt78w24ruldJS062220100000000000000000000005', '0000000000000000000000000000001000000000000000000000000000000003', 'dss.vector.solutions');
INSERT INTO dimension_has_class (oid, parent_oid, child_oid) VALUES ('qv4r5tdvpofqam6avkir3peu6wj3pckfJS062220100000000000000000000007', '0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', 'sys2sjm5ex8qn1kr6s4t2mt78w24ruldJS062220100000000000000000000005');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasClass', NULL, '2011-01-13 13:25:12', 'qv4r5tdvpofqam6avkir3peu6wj3pckfJS062220100000000000000000000007', '0000000000000000000000000000001000000000000000000000000000000003', 308833, NULL, 'qv4r5tdvpofqam6avkir3peu6wj3pckfJS062220100000000000000000000007', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 13:25:12', '0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', 'sys2sjm5ex8qn1kr6s4t2mt78w24ruldJS062220100000000000000000000005');
INSERT INTO class_has_dimension (oid, parent_oid, child_oid) VALUES ('zp0nhuuatl9sfbn17esbg4h3vlp1c7r5JS062220100000000000000000000006', '20101231NM000000000000000000001000000000000000000000000000000001', 'sys2sjm5ex8qn1kr6s4t2mt78w24ruldJS062220100000000000000000000005');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.ClassHasDimension', NULL, '2011-01-13 13:25:12', 'zp0nhuuatl9sfbn17esbg4h3vlp1c7r5JS062220100000000000000000000006', '0000000000000000000000000000001000000000000000000000000000000003', 308834, NULL, 'zp0nhuuatl9sfbn17esbg4h3vlp1c7r5JS062220100000000000000000000006', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 13:25:12', '20101231NM000000000000000000001000000000000000000000000000000001', 'sys2sjm5ex8qn1kr6s4t2mt78w24ruldJS062220100000000000000000000005');


-- update dimension records for MdAttributeClob.defaultValue

-- malaria
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('2h59ih7d3bew9wshtddhavk14j9h53ppNM200904120000000000000000000030', 'dss.vector.solutions', '2h59ih7d3bew9wshtddhavk14j9h53ppNM200904120000000000000000000030', NULL, NULL, NULL);
INSERT INTO md_attribute_dimension (required, defining_md_dimension, defining_md_attribute, default_value, oid) VALUES (0, '7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', '20101231NM000000000000000000001220101231NM0000000000000000000010', NULL, '25d3aaeputc47eldlb90nqamb0bdnp96szii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308835, 'com.runwaysdk.system.metadata.MdAttributeDimension', '0000000000000000000000000000001000000000000000000000000000000003', NULL, '25d3aaeputc47eldlb90nqamb0bdnp96szii8pjdf8hmj16pi3csomvwxphpzp74', '2011-01-13 13:51:32', '2h59ih7d3bew9wshtddhavk14j9h53ppNM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', 1, NULL, '2011-01-13 13:51:32', '25d3aaeputc47eldlb90nqamb0bdnp96szii8pjdf8hmj16pi3csomvwxphpzp74', '0000000000000000000000000000001000000000000000000000000000000003', 'dss.vector.solutions');
INSERT INTO dimension_has_attribute (oid, parent_oid, child_oid) VALUES ('sgogqynqxyhagfdmx3ykhrwl0yli1mfdqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', '25d3aaeputc47eldlb90nqamb0bdnp96szii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasAttribute', NULL, '2011-01-13 13:51:32', 'sgogqynqxyhagfdmx3ykhrwl0yli1mfdqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0000000000000000000000000000001000000000000000000000000000000003', 308836, NULL, 'sgogqynqxyhagfdmx3ykhrwl0yli1mfdqs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 13:51:32', '7663nacnglqn3ept6enar9pkk177vq2b6nyuo8p2bb4now7x8owuvvhhekl28k73', '25d3aaeputc47eldlb90nqamb0bdnp96szii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO attribute_has_dimension (oid, parent_oid, child_oid) VALUES ('8o23l857unuia04chfvqio1rnrdz2mto088krl4llbs2hzg1n65ub3mavye46rhe', '20101231NM000000000000000000001220101231NM0000000000000000000010', '25d3aaeputc47eldlb90nqamb0bdnp96szii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.AttributeHasDimension', NULL, '2011-01-13 13:51:32', '8o23l857unuia04chfvqio1rnrdz2mto088krl4llbs2hzg1n65ub3mavye46rhe', '0000000000000000000000000000001000000000000000000000000000000003', 308837, NULL, '8o23l857unuia04chfvqio1rnrdz2mto088krl4llbs2hzg1n65ub3mavye46rhe', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 13:51:32', '20101231NM000000000000000000001220101231NM0000000000000000000010', '25d3aaeputc47eldlb90nqamb0bdnp96szii8pjdf8hmj16pi3csomvwxphpzp74');

-- dengue
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('qy5zt1fuh8ghy9u6jo6ow8v8h5ng9umzNM200904120000000000000000000030', 'dss.vector.solutions', 'qy5zt1fuh8ghy9u6jo6ow8v8h5ng9umzNM200904120000000000000000000030', NULL, NULL, NULL);
INSERT INTO md_attribute_dimension (required, defining_md_dimension, defining_md_attribute, default_value, oid) VALUES (0, '0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', '20101231NM000000000000000000001220101231NM0000000000000000000010', NULL, 'xnay6byu184f1q78o0qanuwp2keyl29hszii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308838, 'com.runwaysdk.system.metadata.MdAttributeDimension', '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'xnay6byu184f1q78o0qanuwp2keyl29hszii8pjdf8hmj16pi3csomvwxphpzp74', '2011-01-13 13:51:32', 'qy5zt1fuh8ghy9u6jo6ow8v8h5ng9umzNM200904120000000000000000000030', '0000000000000000000000000000001000000000000000000000000000000003', 1, NULL, '2011-01-13 13:51:32', 'xnay6byu184f1q78o0qanuwp2keyl29hszii8pjdf8hmj16pi3csomvwxphpzp74', '0000000000000000000000000000001000000000000000000000000000000003', 'dss.vector.solutions');
INSERT INTO dimension_has_attribute (oid, parent_oid, child_oid) VALUES ('zjylwckorwivqal7m6ljgcf82p9ux0x2qs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', 'xnay6byu184f1q78o0qanuwp2keyl29hszii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasAttribute', NULL, '2011-01-13 13:51:32', 'zjylwckorwivqal7m6ljgcf82p9ux0x2qs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0000000000000000000000000000001000000000000000000000000000000003', 308839, NULL, 'zjylwckorwivqal7m6ljgcf82p9ux0x2qs6a32c2wsxz7xp75r3kcxryhqx3qwui', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 13:51:32', '0etbv45cs3pfqn3fvu5le90cqvjd8m8z6nyuo8p2bb4now7x8owuvvhhekl28k73', 'xnay6byu184f1q78o0qanuwp2keyl29hszii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO attribute_has_dimension (oid, parent_oid, child_oid) VALUES ('8gp1igkn7500vtgri721hesk2h6mm5sl088krl4llbs2hzg1n65ub3mavye46rhe', '20101231NM000000000000000000001220101231NM0000000000000000000010', 'xnay6byu184f1q78o0qanuwp2keyl29hszii8pjdf8hmj16pi3csomvwxphpzp74');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.AttributeHasDimension', NULL, '2011-01-13 13:51:32', '8gp1igkn7500vtgri721hesk2h6mm5sl088krl4llbs2hzg1n65ub3mavye46rhe', '0000000000000000000000000000001000000000000000000000000000000003', 308840, NULL, '8gp1igkn7500vtgri721hesk2h6mm5sl088krl4llbs2hzg1n65ub3mavye46rhe', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000001000000000000000000000000000000003', '2011-01-13 13:51:32', '20101231NM000000000000000000001220101231NM0000000000000000000010', 'xnay6byu184f1q78o0qanuwp2keyl29hszii8pjdf8hmj16pi3csomvwxphpzp74');

COMMIT;