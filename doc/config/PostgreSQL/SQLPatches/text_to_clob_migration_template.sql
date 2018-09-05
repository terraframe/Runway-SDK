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
-- Old Id:6ac06649-27b6-3e54-af9c-66a018240068
-- New Id:0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144
--                                        20101231NM0000000000000000000010

-- Dimension Ids:
-- MALARIA:6821d1d8-35df-3ce4-a8e9-fc229a760150
-- DENGUE :e58e2ac8-a02d-37e8-8d7b-b9d743ce0150
-- converts the text attribute md_attribute_enumeration.cache_column_name to a character attribute.

-- site_master:dss.vector.solutions

BEGIN;

-- Create the new record
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '6ac06649-27b6-3e54-af9c-66a018240068';

-- Update the ids and the type fields in the object tables
UPDATE md_attribute_character
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE md_attribute_char
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE md_attribute_primitive
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE md_attribute_concrete
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE md_attribute
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE metadata
   SET oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '6ac06649-27b6-3e54-af9c-66a018240068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE problem_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE dimension_has_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE dimension_has_class
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE view_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE business_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE md_method_method_actor
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE index_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE class_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE warning_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE entity_index
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE enumeration_attribute_item
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE relationship_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE information_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE class_attribute_virtual
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE exception_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE enumeration_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE util_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE class_inheritance
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE attribute_has_dimension
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE virtualize_attribute
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE class_has_dimension
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE class_attribute_concrete
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE metadata_parameter
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE metadata_relationship
   SET parent_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE parent_oid = '6ac06649-27b6-3e54-af9c-66a018240068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE child_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE index_attribute
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE child_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE class_attribute
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE child_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE type_permissions
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE child_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE class_attribute_virtual
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE child_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE class_attribute_concrete
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE child_oid = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE metadata_relationship
   SET child_oid = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE child_oid = '6ac06649-27b6-3e54-af9c-66a018240068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE sort_md_attribute = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE object_tuple
   SET md_attribute_concrete = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE md_attribute_concrete = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE type_tuple
   SET metadata = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE metadata = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE md_parameter
   SET metadata = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE metadata = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE md_attribute_concrete = '6ac06649-27b6-3e54-af9c-66a018240068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144'
 WHERE defining_md_attribute = '6ac06649-27b6-3e54-af9c-66a018240068';

-- Updating item_ids in MdEnumeration tables.
 
-- create MALARIA dimension records for GeoEntity.geoData
/*
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('zlbl7sdycunvvub5d6wcsea3o2irhvap0287', 'dss.vector.solutions', 'zlbl7sdycunvvub5d6wcsea3o2irhvap0287', NULL, NULL, NULL);
INSERT INTO md_attribute_dimension (required, defining_md_dimension, defining_md_attribute, default_value, oid) VALUES (0, '6821d1d8-35df-3ce4-a8e9-fc229a760150', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144', NULL, '8290d208-99b8-331e-9b1c-7bc62ded0229');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308817, 'com.runwaysdk.system.metadata.MdAttributeDimension', '000000000000000000000000000000100060', NULL, '8290d208-99b8-331e-9b1c-7bc62ded0229', '2011-01-13 11:42:36', 'zlbl7sdycunvvub5d6wcsea3o2irhvap0287', '000000000000000000000000000000100060', 1, NULL, '2011-01-13 11:42:36', '8290d208-99b8-331e-9b1c-7bc62ded0229', '000000000000000000000000000000100060', 'dss.vector.solutions');
INSERT INTO dimension_has_attribute (oid, parent_oid, child_oid) VALUES ('7aa9ad3f-1cfb-383a-90c8-af52345dnull', '6821d1d8-35df-3ce4-a8e9-fc229a760150', '8290d208-99b8-331e-9b1c-7bc62ded0229');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasAttribute', NULL, '2011-01-13 11:42:36', '7aa9ad3f-1cfb-383a-90c8-af52345dnull', '000000000000000000000000000000100060', 308818, NULL, '7aa9ad3f-1cfb-383a-90c8-af52345dnull', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 11:42:36', '6821d1d8-35df-3ce4-a8e9-fc229a760150', '8290d208-99b8-331e-9b1c-7bc62ded0229');
INSERT INTO attribute_has_dimension (oid, parent_oid, child_oid) VALUES ('4c6ebe8e-56bf-303a-ac1b-53d15e00null', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144', '8290d208-99b8-331e-9b1c-7bc62ded0229');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.AttributeHasDimension', NULL, '2011-01-13 11:42:36', '4c6ebe8e-56bf-303a-ac1b-53d15e00null', '000000000000000000000000000000100060', 308819, NULL, '4c6ebe8e-56bf-303a-ac1b-53d15e00null', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 11:42:36', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144', '8290d208-99b8-331e-9b1c-7bc62ded0229');

-- create DENGUE dimension records
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('quxfvs49q3che2zo2plrh34v084b0lwr0287', 'dss.vector.solutions', 'quxfvs49q3che2zo2plrh34v084b0lwr0287', NULL, NULL, NULL);
INSERT INTO md_attribute_dimension (required, defining_md_dimension, defining_md_attribute, default_value, oid) VALUES (0, 'e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144', NULL, '29de264c-c723-342b-b60f-f29cb36a0229');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308820, 'com.runwaysdk.system.metadata.MdAttributeDimension', '000000000000000000000000000000100060', NULL, '29de264c-c723-342b-b60f-f29cb36a0229', '2011-01-13 11:42:36', 'quxfvs49q3che2zo2plrh34v084b0lwr0287', '000000000000000000000000000000100060', 1, NULL, '2011-01-13 11:42:36', '29de264c-c723-342b-b60f-f29cb36a0229', '000000000000000000000000000000100060', 'dss.vector.solutions');
INSERT INTO dimension_has_attribute (oid, parent_oid, child_oid) VALUES ('136b2aa7-d2fd-3f9c-94c3-0992255enull', 'e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '29de264c-c723-342b-b60f-f29cb36a0229');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasAttribute', NULL, '2011-01-13 11:42:36', '136b2aa7-d2fd-3f9c-94c3-0992255enull', '000000000000000000000000000000100060', 308821, NULL, '136b2aa7-d2fd-3f9c-94c3-0992255enull', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 11:42:36', 'e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '29de264c-c723-342b-b60f-f29cb36a0229');
INSERT INTO attribute_has_dimension (oid, parent_oid, child_oid) VALUES ('a3e0db33-5107-394d-b81c-1e8af916null', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144', '29de264c-c723-342b-b60f-f29cb36a0229');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.AttributeHasDimension', NULL, '2011-01-13 11:42:36', 'a3e0db33-5107-394d-b81c-1e8af916null', '000000000000000000000000000000100060', 308822, NULL, 'a3e0db33-5107-394d-b81c-1e8af916null', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 11:42:36', '0g1xqd1rjg90olrjo830tw3l2vd3u4ue0144', '29de264c-c723-342b-b60f-f29cb36a0229');
*/
 
-- update dimension records for MdAttributeClob

-- malaria 
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('2bcmrc45fdrtfq0jk7w923yo7za1uz5q0287', 'dss.vector.solutions', '2bcmrc45fdrtfq0jk7w923yo7za1uz5q0287', NULL, NULL, NULL);
INSERT INTO md_class_dimension (defining_md_dimension, defining_md_class, oid) VALUES ('6821d1d8-35df-3ce4-a8e9-fc229a760150', '20101231NM00000000000000000000100058', '51b8fb5b-ac78-3600-9719-d0014f4b0142');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308829, 'com.runwaysdk.system.metadata.MdClassDimension', '000000000000000000000000000000100060', NULL, '51b8fb5b-ac78-3600-9719-d0014f4b0142', '2011-01-13 13:25:12', '2bcmrc45fdrtfq0jk7w923yo7za1uz5q0287', '000000000000000000000000000000100060', 1, NULL, '2011-01-13 13:25:12', '51b8fb5b-ac78-3600-9719-d0014f4b0142', '000000000000000000000000000000100060', 'dss.vector.solutions');
INSERT INTO dimension_has_class (oid, parent_oid, child_oid) VALUES ('fd790ed1-efb9-334a-aa71-babb37390272', '6821d1d8-35df-3ce4-a8e9-fc229a760150', '51b8fb5b-ac78-3600-9719-d0014f4b0142');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasClass', NULL, '2011-01-13 13:25:12', 'fd790ed1-efb9-334a-aa71-babb37390272', '000000000000000000000000000000100060', 308830, NULL, 'fd790ed1-efb9-334a-aa71-babb37390272', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 13:25:12', '6821d1d8-35df-3ce4-a8e9-fc229a760150', '51b8fb5b-ac78-3600-9719-d0014f4b0142');
INSERT INTO class_has_dimension (oid, parent_oid, child_oid) VALUES ('04bdbdc6-24d7-3c9f-bb3f-133b62ea0273', '20101231NM00000000000000000000100058', '51b8fb5b-ac78-3600-9719-d0014f4b0142');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.ClassHasDimension', NULL, '2011-01-13 13:25:12', '04bdbdc6-24d7-3c9f-bb3f-133b62ea0273', '000000000000000000000000000000100060', 308831, NULL, '04bdbdc6-24d7-3c9f-bb3f-133b62ea0273', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 13:25:12', '20101231NM00000000000000000000100058', '51b8fb5b-ac78-3600-9719-d0014f4b0142');

-- dengue
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('0oru1lyky2nspaz0ivsvx9lky3isr4p80287', 'dss.vector.solutions', '0oru1lyky2nspaz0ivsvx9lky3isr4p80287', NULL, NULL, NULL);
INSERT INTO md_class_dimension (defining_md_dimension, defining_md_class, oid) VALUES ('e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '20101231NM00000000000000000000100058', '14bc561c-5af8-36d7-8409-55be86060142');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308832, 'com.runwaysdk.system.metadata.MdClassDimension', '000000000000000000000000000000100060', NULL, '14bc561c-5af8-36d7-8409-55be86060142', '2011-01-13 13:25:12', '0oru1lyky2nspaz0ivsvx9lky3isr4p80287', '000000000000000000000000000000100060', 1, NULL, '2011-01-13 13:25:12', '14bc561c-5af8-36d7-8409-55be86060142', '000000000000000000000000000000100060', 'dss.vector.solutions');
INSERT INTO dimension_has_class (oid, parent_oid, child_oid) VALUES ('5959c9d0-0256-3d38-adcb-c850697d0272', 'e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '14bc561c-5af8-36d7-8409-55be86060142');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasClass', NULL, '2011-01-13 13:25:12', '5959c9d0-0256-3d38-adcb-c850697d0272', '000000000000000000000000000000100060', 308833, NULL, '5959c9d0-0256-3d38-adcb-c850697d0272', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 13:25:12', 'e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '14bc561c-5af8-36d7-8409-55be86060142');
INSERT INTO class_has_dimension (oid, parent_oid, child_oid) VALUES ('41bb9278-4a0a-3e1d-9272-c3098ee20273', '20101231NM00000000000000000000100058', '14bc561c-5af8-36d7-8409-55be86060142');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.ClassHasDimension', NULL, '2011-01-13 13:25:12', '41bb9278-4a0a-3e1d-9272-c3098ee20273', '000000000000000000000000000000100060', 308834, NULL, '41bb9278-4a0a-3e1d-9272-c3098ee20273', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 13:25:12', '20101231NM00000000000000000000100058', '14bc561c-5af8-36d7-8409-55be86060142');


-- update dimension records for MdAttributeClob.defaultValue

-- malaria
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('2h59ih7d3bew9wshtddhavk14j9h53pp0287', 'dss.vector.solutions', '2h59ih7d3bew9wshtddhavk14j9h53pp0287', NULL, NULL, NULL);
INSERT INTO md_attribute_dimension (required, defining_md_dimension, defining_md_attribute, default_value, oid) VALUES (0, '6821d1d8-35df-3ce4-a8e9-fc229a760150', '20101231NM00000000000000000000120144', NULL, '9745e78e-d71d-3631-9527-a386561e0229');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308835, 'com.runwaysdk.system.metadata.MdAttributeDimension', '000000000000000000000000000000100060', NULL, '9745e78e-d71d-3631-9527-a386561e0229', '2011-01-13 13:51:32', '2h59ih7d3bew9wshtddhavk14j9h53pp0287', '000000000000000000000000000000100060', 1, NULL, '2011-01-13 13:51:32', '9745e78e-d71d-3631-9527-a386561e0229', '000000000000000000000000000000100060', 'dss.vector.solutions');
INSERT INTO dimension_has_attribute (oid, parent_oid, child_oid) VALUES ('c444b653-e7d7-3678-bf48-3fec6603null', '6821d1d8-35df-3ce4-a8e9-fc229a760150', '9745e78e-d71d-3631-9527-a386561e0229');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasAttribute', NULL, '2011-01-13 13:51:32', 'c444b653-e7d7-3678-bf48-3fec6603null', '000000000000000000000000000000100060', 308836, NULL, 'c444b653-e7d7-3678-bf48-3fec6603null', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 13:51:32', '6821d1d8-35df-3ce4-a8e9-fc229a760150', '9745e78e-d71d-3631-9527-a386561e0229');
INSERT INTO attribute_has_dimension (oid, parent_oid, child_oid) VALUES ('b37586ce-e831-37dd-9298-3da456d7null', '20101231NM00000000000000000000120144', '9745e78e-d71d-3631-9527-a386561e0229');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.AttributeHasDimension', NULL, '2011-01-13 13:51:32', 'b37586ce-e831-37dd-9298-3da456d7null', '000000000000000000000000000000100060', 308837, NULL, 'b37586ce-e831-37dd-9298-3da456d7null', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 13:51:32', '20101231NM00000000000000000000120144', '9745e78e-d71d-3631-9527-a386561e0229');

-- dengue
INSERT INTO metadata_display_label (oid, site_master, key_name, default_locale, m_alaria_default_locale, d_engue_default_locale) VALUES ('qy5zt1fuh8ghy9u6jo6ow8v8h5ng9umz0287', 'dss.vector.solutions', 'qy5zt1fuh8ghy9u6jo6ow8v8h5ng9umz0287', NULL, NULL, NULL);
INSERT INTO md_attribute_dimension (required, defining_md_dimension, defining_md_attribute, default_value, oid) VALUES (0, 'e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '20101231NM00000000000000000000120144', NULL, '0ce13b47-2995-33ff-8c9a-47cd58050229');
INSERT INTO metadata (seq, type, created_by, entity_domain, key_name, last_update_date, description, last_updated_by, remove, locked_by, create_date, oid, owner, site_master) VALUES (308838, 'com.runwaysdk.system.metadata.MdAttributeDimension', '000000000000000000000000000000100060', NULL, '0ce13b47-2995-33ff-8c9a-47cd58050229', '2011-01-13 13:51:32', 'qy5zt1fuh8ghy9u6jo6ow8v8h5ng9umz0287', '000000000000000000000000000000100060', 1, NULL, '2011-01-13 13:51:32', '0ce13b47-2995-33ff-8c9a-47cd58050229', '000000000000000000000000000000100060', 'dss.vector.solutions');
INSERT INTO dimension_has_attribute (oid, parent_oid, child_oid) VALUES ('469c315f-52f2-3808-b966-ccd5159cnull', 'e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '0ce13b47-2995-33ff-8c9a-47cd58050229');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.DimensionHasAttribute', NULL, '2011-01-13 13:51:32', '469c315f-52f2-3808-b966-ccd5159cnull', '000000000000000000000000000000100060', 308839, NULL, '469c315f-52f2-3808-b966-ccd5159cnull', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 13:51:32', 'e58e2ac8-a02d-37e8-8d7b-b9d743ce0150', '0ce13b47-2995-33ff-8c9a-47cd58050229');
INSERT INTO attribute_has_dimension (oid, parent_oid, child_oid) VALUES ('38bfd0f4-4870-3833-a0e8-4e46319fnull', '20101231NM00000000000000000000120144', '0ce13b47-2995-33ff-8c9a-47cd58050229');
INSERT INTO metadata_relationship (site_master, type, entity_domain, create_date, oid, last_updated_by, seq, locked_by, key_name, owner, created_by, last_update_date, parent_oid, child_oid) VALUES ('dss.vector.solutions', 'com.runwaysdk.system.metadata.AttributeHasDimension', NULL, '2011-01-13 13:51:32', '38bfd0f4-4870-3833-a0e8-4e46319fnull', '000000000000000000000000000000100060', 308840, NULL, '38bfd0f4-4870-3833-a0e8-4e46319fnull', '000000000000000000000000000000100060', '000000000000000000000000000000100060', '2011-01-13 13:51:32', '20101231NM00000000000000000000120144', '0ce13b47-2995-33ff-8c9a-47cd58050229');

COMMIT;