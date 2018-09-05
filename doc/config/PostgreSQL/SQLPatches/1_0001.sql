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

BEGIN;

-- converts the text attribute md_attribute_enumeration.cache_column_name to a character attribute.
-- Create the new record
INSERT INTO md_attribute_character (oid, default_value, database_size)
  VALUES ('20061126NM00000000000000000000010067', NULL, 32);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';
-- Copy the existig values over to the new colum
ALTER TABLE md_attribute_enumeration ADD COLUMN cache_column_name_t VARCHAR(32);
UPDATE md_attribute_enumeration
   SET cache_column_name_t = cache_column_name;
ALTER TABLE md_attribute_enumeration DROP COLUMN cache_column_name;
ALTER TABLE md_attribute_enumeration ADD COLUMN cache_column_name VARCHAR(32);
UPDATE md_attribute_enumeration
   SET cache_column_name = cache_column_name_t;
ALTER TABLE md_attribute_enumeration DROP COLUMN cache_column_name_t;

-- Update the ids and the type fields in the object tables
UPDATE md_attribute_character
   SET oid = '20061126NM00000000000000000000010067'
 WHERE oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE md_attribute_char
   SET oid = '20061126NM00000000000000000000010067'
 WHERE oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE md_attribute_primitive
   SET oid = '20061126NM00000000000000000000010067'
 WHERE oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE md_attribute_concrete
   SET oid = '20061126NM00000000000000000000010067'
 WHERE oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE md_attribute
   SET oid = '20061126NM00000000000000000000010067'
 WHERE oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE metadata
   SET oid = '20061126NM00000000000000000000010067',
       type = 'com.runwaysdk.system.metadata.MdAttributeCharacter'
 WHERE oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE problem_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE dimension_has_attribute
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE dimension_has_class
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE view_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE business_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE md_method_method_actor
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE index_attribute
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE class_attribute
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE warning_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE entity_index
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE relationship_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE information_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE class_attribute_virtual
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE exception_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE enumeration_attribute
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE util_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE class_inheritance
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE attribute_has_dimension
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE virtualize_attribute
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE class_has_dimension
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE class_attribute_concrete
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE metadata_parameter
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE metadata_relationship
   SET parent_oid = '20061126NM00000000000000000000010067'
 WHERE parent_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20061126NM00000000000000000000010067'
 WHERE child_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE index_attribute
   SET child_oid = '20061126NM00000000000000000000010067'
 WHERE child_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE class_attribute
   SET child_oid = '20061126NM00000000000000000000010067'
 WHERE child_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE type_permissions
   SET child_oid = '20061126NM00000000000000000000010067'
 WHERE child_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE class_attribute_virtual
   SET child_oid = '20061126NM00000000000000000000010067'
 WHERE child_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE class_attribute_concrete
   SET child_oid = '20061126NM00000000000000000000010067'
 WHERE child_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE metadata_relationship
   SET child_oid = '20061126NM00000000000000000000010067'
 WHERE child_oid = '5eccdcd3-4897-3a9c-8c05-299ce3900068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20061126NM00000000000000000000010067'
 WHERE sort_md_attribute = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE object_tuple
   SET md_attribute_concrete = '20061126NM00000000000000000000010067'
 WHERE md_attribute_concrete = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE type_tuple
   SET metadata = '20061126NM00000000000000000000010067'
 WHERE metadata = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE md_parameter
   SET metadata = '20061126NM00000000000000000000010067'
 WHERE metadata = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20061126NM00000000000000000000010067'
 WHERE md_attribute_concrete = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20061126NM00000000000000000000010067'
 WHERE defining_md_attribute = '5eccdcd3-4897-3a9c-8c05-299ce3900068';

-- Updating item_ids in MdEnumeration tables.

-- Adding the attribute type MdAttributeClob
-- Root oid      70b3cklmgdsvs73u458bqnz1hanqrai5
-- New Root oid  20101231NM0000000000000000000010

-- xwm936z3bulbeutdxq0ei15b1j59srtp
-- 20101231NM0000000000000000000011

-- 6qsietylgx1j1v28jj1sg515ysfbhy91
-- 20101231NM0000000000000000000012

-- zmgwlkw073c1xf4in45khyhaky41sww3
-- 20101231NM0000000000000000000013

INSERT INTO metadata_display_label (default_locale, site_master, key_name, oid) VALUES ('Metadata Attribute Clob', 'www.runwaysdk.com', '6qs6b1xwech58ydevkbvigzwj9eend2m0287', '6qs6b1xwech58ydevkbvigzwj9eend2m0287');
INSERT INTO metadata_display_label (default_locale, site_master, key_name, oid) VALUES ('Attribute clob metadata definition', 'www.runwaysdk.com', 're1t4xh5jz6z63z5sfdkkxwn99beyc4n0287', 're1t4xh5jz6z63z5sfdkkxwn99beyc4n0287');
INSERT INTO md_business (cache_algorithm, cache_algorithm_c, super_md_business, oid) VALUES ('xj03zkahg7k8d3pww6iu279g66xbi68m', '000000000000000000000000000002310077', '000000000000000000000000000001370058', '20101231NM00000000000000000000100058');

INSERT INTO class_cache (set_id, item_id) VALUES('xj03zkahg7k8d3pww6iu279g66xbi68m', '000000000000000000000000000002310077');

INSERT INTO md_element (extendable, is_abstract, oid) VALUES (0, 0, '20101231NM00000000000000000000100058');
INSERT INTO md_entity (cache_size, query_dto_source, enforce_site_master, table_name, query_source, oid) VALUES (0, NULL, 1, 'md_attribute_clob', NULL, '20101231NM00000000000000000000100058');
INSERT INTO md_class (publish, stub_source, stub_dto_source, oid) VALUES (1, NULL, NULL, '20101231NM00000000000000000000100058');
INSERT INTO md_type (js_stub, package_name, js_base, dto_source, exported, base_source, root_id, type_name, display_label, oid) VALUES (NULL, 'com.runwaysdk.system.metadata', NULL, NULL, 1, NULL, '20101231NM0000000000000000000010', 'MdAttributeClob', '6qs6b1xwech58ydevkbvigzwj9eend2m0287', '20101231NM00000000000000000000100058');
INSERT INTO metadata (created_by, locked_by, last_update_date, oid, description, last_updated_by, entity_domain, owner, seq, site_master, type, key_name, create_date, remove) VALUES ('000000000000000000000000000000100060', NULL, '2010-12-30 18:45:08', '20101231NM00000000000000000000100058', 're1t4xh5jz6z63z5sfdkkxwn99beyc4n0287', '000000000000000000000000000000100060', NULL, '000000000000000000000000000000100060', 2741, 'www.runwaysdk.com', 'com.runwaysdk.system.metadata.MdBusiness', 'com.runwaysdk.system.metadata.MdAttributeClob', '2010-12-30 18:45:08', 0);
CREATE TABLE md_attribute_clob ( oid CHAR(64) NOT NULL PRIMARY KEY );
INSERT INTO business_inheritance (oid, parent_oid, child_oid) VALUES ('20101231NM00000000000000000000110257', '000000000000000000000000000001370058', '20101231NM00000000000000000000100058');
INSERT INTO class_inheritance (oid, parent_oid, child_oid) VALUES ('20101231NM00000000000000000000110257', '000000000000000000000000000001370058', '20101231NM00000000000000000000100058');
INSERT INTO metadata_relationship (created_by, key_name, owner, seq, entity_domain, last_update_date, create_date, type, site_master, locked_by, oid, last_updated_by, parent_oid, child_oid) VALUES ('000000000000000000000000000000100060', '20101231NM00000000000000000000110257', '000000000000000000000000000000100060', 2742, NULL, '2010-12-30 18:45:08', '2010-12-30 18:45:08', 'com.runwaysdk.system.metadata.BusinessInheritance', 'www.runwaysdk.com', NULL, '20101231NM00000000000000000000110257', '000000000000000000000000000000100060', '000000000000000000000000000001370058', '20101231NM00000000000000000000100058');
UPDATE md_business SET cache_algorithm= 'xj03zkahg7k8d3pww6iu279g66xbi68m' , cache_algorithm_c= '000000000000000000000000000002310077' , super_md_business= '000000000000000000000000000001370058'  WHERE oid='20101231NM00000000000000000000100058';
UPDATE md_element SET extendable= 0  WHERE oid='20101231NM00000000000000000000100058';
UPDATE md_entity SET table_name= 'md_attribute_clob'  WHERE oid='20101231NM00000000000000000000100058';
UPDATE md_type SET package_name= 'com.runwaysdk.system.metadata' , root_id= '20101231NM0000000000000000000010' , type_name= 'MdAttributeClob' , display_label= '6qs6b1xwech58ydevkbvigzwj9eend2m0287'  WHERE oid='20101231NM00000000000000000000100058';
UPDATE metadata SET created_by= '000000000000000000000000000000100060' , last_update_date= '2010-12-30 18:45:08' , oid= '20101231NM00000000000000000000100058' , description= 're1t4xh5jz6z63z5sfdkkxwn99beyc4n0287' , last_updated_by= '000000000000000000000000000000100060' , owner= '000000000000000000000000000000100060' , seq= 2743 , site_master= 'www.runwaysdk.com' , type= 'com.runwaysdk.system.metadata.MdBusiness' , key_name= 'com.runwaysdk.system.metadata.MdAttributeClob' , create_date= '2010-12-30 18:45:08' , remove= 0  WHERE oid='20101231NM00000000000000000000100058';

-- Adding attribute defaultValue to MdAttributeClob
INSERT INTO metadata_display_label (default_locale, site_master, key_name, oid) VALUES ('Default Value', 'www.runwaysdk.com', '7e44s98sb0ucvxgqc9u9tm8oswy9mayg0287', '7e44s98sb0ucvxgqc9u9tm8oswy9mayg0287');
INSERT INTO metadata_display_label (default_locale, site_master, key_name, oid) VALUES ('Default value attribute for default attributes', 'www.runwaysdk.com', '6zwxl6zt7zb6kd91mv5ho49pvm368xc60287', '6zwxl6zt7zb6kd91mv5ho49pvm368xc60287');
INSERT INTO md_attribute_clob (oid) VALUES ('20101231NM00000000000000000000120144');
INSERT INTO md_attribute_char (oid) VALUES ('20101231NM00000000000000000000120144');
INSERT INTO md_attribute_primitive (oid) VALUES ('20101231NM00000000000000000000120144');
INSERT INTO md_attribute_concrete (display_label, system, getter_visibility, getter_visibility_c, column_name, index_name, attribute_name, generate_accessor, immutable, defining_md_class, index_type, index_type_c, setter_visibility, setter_visibility_c, required, oid) VALUES ('7e44s98sb0ucvxgqc9u9tm8oswy9mayg0287', 0, 'swoxje5bnfdmmi85be6uncy6x3ttvxyn', '20071109NM00000000000000000000050113', 'default_value', 'abbssc8rl2x6bq7o0b5c3tzd9ht589', 'defaultValue', 1, 0, '20101231NM00000000000000000000100058', 'qsk2y15qiga72b45zz996kzfv1pjg9yt', '000000000000000000000000000004010085', '016u322utvdk7mguzotbxgp69uwtd053', '20071109NM00000000000000000000050113', 0, '20101231NM00000000000000000000120144');

INSERT INTO visibilitymodifier (set_id, item_id) VALUES('swoxje5bnfdmmi85be6uncy6x3ttvxyn', '20071109NM00000000000000000000050113');
INSERT INTO visibilitymodifier (set_id, item_id) VALUES('016u322utvdk7mguzotbxgp69uwtd053', '20071109NM00000000000000000000050113');
INSERT INTO md_attribute_indicies (set_id, item_id) VALUES('qsk2y15qiga72b45zz996kzfv1pjg9yt', '000000000000000000000000000004010085');

INSERT INTO md_attribute (oid) VALUES ('20101231NM00000000000000000000120144');
INSERT INTO metadata (created_by, locked_by, last_update_date, oid, description, last_updated_by, entity_domain, owner, seq, site_master, type, key_name, create_date, remove) VALUES ('000000000000000000000000000000100060', NULL, '2010-12-31 18:37:24', '20101231NM00000000000000000000120144', '6zwxl6zt7zb6kd91mv5ho49pvm368xc60287', '000000000000000000000000000000100060', NULL, '000000000000000000000000000000100060', 2742, 'www.runwaysdk.com', 'com.runwaysdk.system.metadata.MdAttributeClob', 'com.runwaysdk.system.metadata.MdAttributeClob.defaultValue', '2010-12-31 18:37:24', 0);

ALTER TABLE md_attribute_clob ADD COLUMN default_value  text;
INSERT INTO class_attribute_concrete (oid, parent_oid, child_oid) VALUES ('20101231NM00000000000000000000130245', '20101231NM00000000000000000000100058', '20101231NM00000000000000000000120144');
INSERT INTO class_attribute (oid, parent_oid, child_oid) VALUES ('20101231NM00000000000000000000130245', '20101231NM00000000000000000000100058', '20101231NM00000000000000000000120144');
INSERT INTO metadata_relationship (created_by, key_name, owner, seq, entity_domain, last_update_date, create_date, type, site_master, locked_by, oid, last_updated_by, parent_oid, child_oid) VALUES ('000000000000000000000000000000100060', 'com.runwaysdk.system.metadata.MdAttributeClob.defaultValue', '000000000000000000000000000000100060', 2743, NULL, '2010-12-31 18:37:24', '2010-12-31 18:37:24', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', 'www.runwaysdk.com', NULL, '20101231NM00000000000000000000130245', '000000000000000000000000000000100060', '20101231NM00000000000000000000100058', '20101231NM00000000000000000000120144');
UPDATE metadata SET seq= 2744  WHERE oid='20101231NM00000000000000000000100058';

/*********************************************************************
 * Converting code source attributes to the clob datatype
 *********************************************************************/

-- converts the text attribute md_facade.stub_source to a character attribute.
-- Create the new record
-- Old Id:c5857504-9a3b-3754-a4c9-52fbbcae0068
-- New Id:200706016NM0000000000000000000020144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('200706016NM0000000000000000000020144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

-- converts the text attribute md_class.stub_source to a character attribute.
-- Create the new record
-- Old Id:ae881748-7ed8-3060-9b3b-8657eeae0068
-- New Id:20070923NM00000000000000000000010144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('20070923NM00000000000000000000010144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

-- converts the text attribute md_class.stub_dto_source to a character attribute.
-- Create the new record
-- Old Id:fe580a39-a22c-36a7-9af3-981db95f0068
-- New Id:20070923NM00000000000000000000040144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('20070923NM00000000000000000000040144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

-- converts the text attribute md_entity.query_source to a character attribute.
-- Create the new record
-- Old Id:8dea6740-ff4f-36da-a5b5-b4d413800068
-- New Id:20070923NM00000000000000000000100144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('20070923NM00000000000000000000100144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

-- converts the text attribute md_entity.query_dto_source to a character attribute.
-- Create the new record
-- Old Id:39278cbd-d0ab-36e8-a8ef-7b6137410068
-- New Id:20080408NM00000000000000000000010144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('20080408NM00000000000000000000010144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

-- converts the text attribute md_view.query_dto_source to a character attribute.
-- Create the new record
-- Old Id:c7cb5f00-6342-30b3-b126-271e14990068
-- New Id:20080429NM00000000000000000000010144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('20080429NM00000000000000000000010144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

-- converts the text attribute md_view.query_stub_source to a character attribute.
-- Create the new record
-- Old Id:0972db6d-e588-366c-bd34-ee3e6f7d0068
-- New Id:20080423NM00000000000000000000010144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('20080423NM00000000000000000000010144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

-- converts the text attribute md_view.query_base_source to a character attribute.
-- Create the new record
-- Old Id:276b512f-4618-3fec-9274-273ebb4c0068
-- New Id:20080326NM00000000000000000000020144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('20080326NM00000000000000000000020144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '276b512f-4618-3fec-9274-273ebb4c0068';

-- converts the text attribute md_controller.stub_source to a character attribute.
-- Create the new record
-- Old Id:fae4c6ab-bc07-3a08-a6be-1a82582a0068
-- New Id:NM2008101100000000000000000000750144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('NM2008101100000000000000000000750144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

-- converts the text attribute transaction_item.xml_record to a character attribute.
-- Create the new record
-- Old Id:3af9488e-67a9-3021-b6f3-f03c9a990068
-- New Id:0d3j6jnotv7bcwuv83j1w7o4tltte71z0144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('0d3j6jnotv7bcwuv83j1w7o4tltte71z0144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

-- converts the text attribute md_type.js_base to a character attribute.
-- Create the new record
-- Old Id:78f76f2c-6cb0-3e9d-8701-5a44d7370068
-- New Id:sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

-- converts the text attribute md_type.js_stub to a character attribute.
-- Create the new record
-- Old Id:a5627b60-1e30-3a39-ab70-f07af2a70068
-- New Id:6ozempoo35sdmp69nkuagmeb0kejpe5r0144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('6ozempoo35sdmp69nkuagmeb0kejpe5r0144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

-- converts the text attribute md_type.dto_source to a character attribute.
-- Create the new record
-- Old Id:1459e6fa-501a-3fba-b51e-a5b05c240068
-- New Id:NM2007041800000000000000000000400144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('NM2007041800000000000000000000400144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

-- converts the text attribute md_type.base_source to a character attribute.
-- Create the new record
-- Old Id:245524a7-04fe-3c7c-8dff-eda3863d0068
-- New Id:20070320EG00000000000000000014540144
--                                        20101231NM0000000000000000000010
INSERT INTO md_attribute_clob (oid, default_value)
  VALUES ('20070320EG00000000000000000014540144', NULL);
-- Delete the old
DELETE FROM md_attribute_text
 WHERE oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

 -- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '200706016NM0000000000000000000020144'
 WHERE oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE md_attribute_char
   SET oid = '200706016NM0000000000000000000020144'
 WHERE oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE md_attribute_primitive
   SET oid = '200706016NM0000000000000000000020144'
 WHERE oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE md_attribute_concrete
   SET oid = '200706016NM0000000000000000000020144'
 WHERE oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE md_attribute
   SET oid = '200706016NM0000000000000000000020144'
 WHERE oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE metadata
   SET oid = '200706016NM0000000000000000000020144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE problem_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE dimension_has_attribute
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE dimension_has_class
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE view_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE business_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE md_method_method_actor
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE index_attribute
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE class_attribute
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE warning_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE entity_index
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE enumeration_attribute_item
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE relationship_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE information_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE class_attribute_virtual
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE exception_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE enumeration_attribute
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE util_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE class_inheritance
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE attribute_has_dimension
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE virtualize_attribute
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE class_has_dimension
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE class_attribute_concrete
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE metadata_parameter
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE metadata_relationship
   SET parent_oid = '200706016NM0000000000000000000020144'
 WHERE parent_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '200706016NM0000000000000000000020144'
 WHERE child_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE index_attribute
   SET child_oid = '200706016NM0000000000000000000020144'
 WHERE child_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE class_attribute
   SET child_oid = '200706016NM0000000000000000000020144'
 WHERE child_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE type_permissions
   SET child_oid = '200706016NM0000000000000000000020144'
 WHERE child_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE class_attribute_virtual
   SET child_oid = '200706016NM0000000000000000000020144'
 WHERE child_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE class_attribute_concrete
   SET child_oid = '200706016NM0000000000000000000020144'
 WHERE child_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE metadata_relationship
   SET child_oid = '200706016NM0000000000000000000020144'
 WHERE child_oid = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '200706016NM0000000000000000000020144'
 WHERE sort_md_attribute = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE object_tuple
   SET md_attribute_concrete = '200706016NM0000000000000000000020144'
 WHERE md_attribute_concrete = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE type_tuple
   SET metadata = '200706016NM0000000000000000000020144'
 WHERE metadata = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE md_parameter
   SET metadata = '200706016NM0000000000000000000020144'
 WHERE metadata = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '200706016NM0000000000000000000020144'
 WHERE md_attribute_concrete = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '200706016NM0000000000000000000020144'
 WHERE defining_md_attribute = 'c5857504-9a3b-3754-a4c9-52fbbcae0068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '20070923NM00000000000000000000010144'
 WHERE oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE md_attribute_char
   SET oid = '20070923NM00000000000000000000010144'
 WHERE oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE md_attribute_primitive
   SET oid = '20070923NM00000000000000000000010144'
 WHERE oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE md_attribute_concrete
   SET oid = '20070923NM00000000000000000000010144'
 WHERE oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE md_attribute
   SET oid = '20070923NM00000000000000000000010144'
 WHERE oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE metadata
   SET oid = '20070923NM00000000000000000000010144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE problem_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE dimension_has_attribute
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE dimension_has_class
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE view_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE business_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE md_method_method_actor
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE index_attribute
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE class_attribute
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE warning_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE entity_index
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE relationship_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE information_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE class_attribute_virtual
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE exception_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE enumeration_attribute
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE util_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE class_inheritance
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE attribute_has_dimension
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE virtualize_attribute
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE class_has_dimension
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE class_attribute_concrete
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE metadata_parameter
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE metadata_relationship
   SET parent_oid = '20070923NM00000000000000000000010144'
 WHERE parent_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20070923NM00000000000000000000010144'
 WHERE child_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE index_attribute
   SET child_oid = '20070923NM00000000000000000000010144'
 WHERE child_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE class_attribute
   SET child_oid = '20070923NM00000000000000000000010144'
 WHERE child_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE type_permissions
   SET child_oid = '20070923NM00000000000000000000010144'
 WHERE child_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE class_attribute_virtual
   SET child_oid = '20070923NM00000000000000000000010144'
 WHERE child_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE class_attribute_concrete
   SET child_oid = '20070923NM00000000000000000000010144'
 WHERE child_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE metadata_relationship
   SET child_oid = '20070923NM00000000000000000000010144'
 WHERE child_oid = 'ae881748-7ed8-3060-9b3b-8657eeae0068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20070923NM00000000000000000000010144'
 WHERE sort_md_attribute = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE object_tuple
   SET md_attribute_concrete = '20070923NM00000000000000000000010144'
 WHERE md_attribute_concrete = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE type_tuple
   SET metadata = '20070923NM00000000000000000000010144'
 WHERE metadata = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE md_parameter
   SET metadata = '20070923NM00000000000000000000010144'
 WHERE metadata = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20070923NM00000000000000000000010144'
 WHERE md_attribute_concrete = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20070923NM00000000000000000000010144'
 WHERE defining_md_attribute = 'ae881748-7ed8-3060-9b3b-8657eeae0068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '20070923NM00000000000000000000040144'
 WHERE oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE md_attribute_char
   SET oid = '20070923NM00000000000000000000040144'
 WHERE oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE md_attribute_primitive
   SET oid = '20070923NM00000000000000000000040144'
 WHERE oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE md_attribute_concrete
   SET oid = '20070923NM00000000000000000000040144'
 WHERE oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE md_attribute
   SET oid = '20070923NM00000000000000000000040144'
 WHERE oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE metadata
   SET oid = '20070923NM00000000000000000000040144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE problem_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE dimension_has_attribute
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE dimension_has_class
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE view_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE business_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE md_method_method_actor
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE index_attribute
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE class_attribute
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE warning_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE entity_index
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE relationship_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE information_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE class_attribute_virtual
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE exception_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE enumeration_attribute
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE util_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE class_inheritance
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE attribute_has_dimension
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE virtualize_attribute
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE class_has_dimension
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE class_attribute_concrete
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE metadata_parameter
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE metadata_relationship
   SET parent_oid = '20070923NM00000000000000000000040144'
 WHERE parent_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20070923NM00000000000000000000040144'
 WHERE child_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE index_attribute
   SET child_oid = '20070923NM00000000000000000000040144'
 WHERE child_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE class_attribute
   SET child_oid = '20070923NM00000000000000000000040144'
 WHERE child_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE type_permissions
   SET child_oid = '20070923NM00000000000000000000040144'
 WHERE child_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE class_attribute_virtual
   SET child_oid = '20070923NM00000000000000000000040144'
 WHERE child_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE class_attribute_concrete
   SET child_oid = '20070923NM00000000000000000000040144'
 WHERE child_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE metadata_relationship
   SET child_oid = '20070923NM00000000000000000000040144'
 WHERE child_oid = 'fe580a39-a22c-36a7-9af3-981db95f0068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20070923NM00000000000000000000040144'
 WHERE sort_md_attribute = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE object_tuple
   SET md_attribute_concrete = '20070923NM00000000000000000000040144'
 WHERE md_attribute_concrete = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE type_tuple
   SET metadata = '20070923NM00000000000000000000040144'
 WHERE metadata = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE md_parameter
   SET metadata = '20070923NM00000000000000000000040144'
 WHERE metadata = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20070923NM00000000000000000000040144'
 WHERE md_attribute_concrete = 'fe580a39-a22c-36a7-9af3-981db95f0068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20070923NM00000000000000000000040144'
 WHERE defining_md_attribute = 'fe580a39-a22c-36a7-9af3-981db95f0068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '20070923NM00000000000000000000100144'
 WHERE oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE md_attribute_char
   SET oid = '20070923NM00000000000000000000100144'
 WHERE oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE md_attribute_primitive
   SET oid = '20070923NM00000000000000000000100144'
 WHERE oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE md_attribute_concrete
   SET oid = '20070923NM00000000000000000000100144'
 WHERE oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE md_attribute
   SET oid = '20070923NM00000000000000000000100144'
 WHERE oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE metadata
   SET oid = '20070923NM00000000000000000000100144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE problem_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE dimension_has_attribute
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE dimension_has_class
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE view_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE business_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE md_method_method_actor
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE index_attribute
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE class_attribute
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE warning_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE entity_index
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE relationship_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE information_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE class_attribute_virtual
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE exception_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE enumeration_attribute
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE util_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE class_inheritance
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE attribute_has_dimension
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE virtualize_attribute
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE class_has_dimension
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE class_attribute_concrete
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE metadata_parameter
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE metadata_relationship
   SET parent_oid = '20070923NM00000000000000000000100144'
 WHERE parent_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20070923NM00000000000000000000100144'
 WHERE child_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE index_attribute
   SET child_oid = '20070923NM00000000000000000000100144'
 WHERE child_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE class_attribute
   SET child_oid = '20070923NM00000000000000000000100144'
 WHERE child_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE type_permissions
   SET child_oid = '20070923NM00000000000000000000100144'
 WHERE child_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE class_attribute_virtual
   SET child_oid = '20070923NM00000000000000000000100144'
 WHERE child_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE class_attribute_concrete
   SET child_oid = '20070923NM00000000000000000000100144'
 WHERE child_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE metadata_relationship
   SET child_oid = '20070923NM00000000000000000000100144'
 WHERE child_oid = '8dea6740-ff4f-36da-a5b5-b4d413800068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20070923NM00000000000000000000100144'
 WHERE sort_md_attribute = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE object_tuple
   SET md_attribute_concrete = '20070923NM00000000000000000000100144'
 WHERE md_attribute_concrete = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE type_tuple
   SET metadata = '20070923NM00000000000000000000100144'
 WHERE metadata = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE md_parameter
   SET metadata = '20070923NM00000000000000000000100144'
 WHERE metadata = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20070923NM00000000000000000000100144'
 WHERE md_attribute_concrete = '8dea6740-ff4f-36da-a5b5-b4d413800068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20070923NM00000000000000000000100144'
 WHERE defining_md_attribute = '8dea6740-ff4f-36da-a5b5-b4d413800068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '20080408NM00000000000000000000010144'
 WHERE oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE md_attribute_char
   SET oid = '20080408NM00000000000000000000010144'
 WHERE oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE md_attribute_primitive
   SET oid = '20080408NM00000000000000000000010144'
 WHERE oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE md_attribute_concrete
   SET oid = '20080408NM00000000000000000000010144'
 WHERE oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE md_attribute
   SET oid = '20080408NM00000000000000000000010144'
 WHERE oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE metadata
   SET oid = '20080408NM00000000000000000000010144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE problem_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE dimension_has_attribute
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE dimension_has_class
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE view_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE business_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE md_method_method_actor
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE index_attribute
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE class_attribute
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE warning_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE entity_index
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE relationship_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE information_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE class_attribute_virtual
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE exception_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE enumeration_attribute
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE util_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE class_inheritance
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE attribute_has_dimension
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE virtualize_attribute
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE class_has_dimension
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE class_attribute_concrete
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE metadata_parameter
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE metadata_relationship
   SET parent_oid = '20080408NM00000000000000000000010144'
 WHERE parent_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20080408NM00000000000000000000010144'
 WHERE child_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE index_attribute
   SET child_oid = '20080408NM00000000000000000000010144'
 WHERE child_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE class_attribute
   SET child_oid = '20080408NM00000000000000000000010144'
 WHERE child_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE type_permissions
   SET child_oid = '20080408NM00000000000000000000010144'
 WHERE child_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE class_attribute_virtual
   SET child_oid = '20080408NM00000000000000000000010144'
 WHERE child_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE class_attribute_concrete
   SET child_oid = '20080408NM00000000000000000000010144'
 WHERE child_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE metadata_relationship
   SET child_oid = '20080408NM00000000000000000000010144'
 WHERE child_oid = '39278cbd-d0ab-36e8-a8ef-7b6137410068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20080408NM00000000000000000000010144'
 WHERE sort_md_attribute = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE object_tuple
   SET md_attribute_concrete = '20080408NM00000000000000000000010144'
 WHERE md_attribute_concrete = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE type_tuple
   SET metadata = '20080408NM00000000000000000000010144'
 WHERE metadata = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE md_parameter
   SET metadata = '20080408NM00000000000000000000010144'
 WHERE metadata = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20080408NM00000000000000000000010144'
 WHERE md_attribute_concrete = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20080408NM00000000000000000000010144'
 WHERE defining_md_attribute = '39278cbd-d0ab-36e8-a8ef-7b6137410068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '20080429NM00000000000000000000010144'
 WHERE oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE md_attribute_char
   SET oid = '20080429NM00000000000000000000010144'
 WHERE oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE md_attribute_primitive
   SET oid = '20080429NM00000000000000000000010144'
 WHERE oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE md_attribute_concrete
   SET oid = '20080429NM00000000000000000000010144'
 WHERE oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE md_attribute
   SET oid = '20080429NM00000000000000000000010144'
 WHERE oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE metadata
   SET oid = '20080429NM00000000000000000000010144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = 'c7cb5f00-6342-30b3-b126-271e14990068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE problem_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE dimension_has_attribute
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE dimension_has_class
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE view_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE business_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE md_method_method_actor
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE index_attribute
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE class_attribute
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE warning_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE entity_index
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE relationship_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE information_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE class_attribute_virtual
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE exception_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE enumeration_attribute
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE util_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE class_inheritance
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE attribute_has_dimension
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE virtualize_attribute
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE class_has_dimension
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE class_attribute_concrete
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE metadata_parameter
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE metadata_relationship
   SET parent_oid = '20080429NM00000000000000000000010144'
 WHERE parent_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20080429NM00000000000000000000010144'
 WHERE child_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE index_attribute
   SET child_oid = '20080429NM00000000000000000000010144'
 WHERE child_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE class_attribute
   SET child_oid = '20080429NM00000000000000000000010144'
 WHERE child_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE type_permissions
   SET child_oid = '20080429NM00000000000000000000010144'
 WHERE child_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE class_attribute_virtual
   SET child_oid = '20080429NM00000000000000000000010144'
 WHERE child_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE class_attribute_concrete
   SET child_oid = '20080429NM00000000000000000000010144'
 WHERE child_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE metadata_relationship
   SET child_oid = '20080429NM00000000000000000000010144'
 WHERE child_oid = 'c7cb5f00-6342-30b3-b126-271e14990068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20080429NM00000000000000000000010144'
 WHERE sort_md_attribute = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE object_tuple
   SET md_attribute_concrete = '20080429NM00000000000000000000010144'
 WHERE md_attribute_concrete = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE type_tuple
   SET metadata = '20080429NM00000000000000000000010144'
 WHERE metadata = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE md_parameter
   SET metadata = '20080429NM00000000000000000000010144'
 WHERE metadata = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20080429NM00000000000000000000010144'
 WHERE md_attribute_concrete = 'c7cb5f00-6342-30b3-b126-271e14990068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20080429NM00000000000000000000010144'
 WHERE defining_md_attribute = 'c7cb5f00-6342-30b3-b126-271e14990068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '20080423NM00000000000000000000010144'
 WHERE oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE md_attribute_char
   SET oid = '20080423NM00000000000000000000010144'
 WHERE oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE md_attribute_primitive
   SET oid = '20080423NM00000000000000000000010144'
 WHERE oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE md_attribute_concrete
   SET oid = '20080423NM00000000000000000000010144'
 WHERE oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE md_attribute
   SET oid = '20080423NM00000000000000000000010144'
 WHERE oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE metadata
   SET oid = '20080423NM00000000000000000000010144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE problem_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE dimension_has_attribute
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE dimension_has_class
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE view_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE business_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE md_method_method_actor
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE index_attribute
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE class_attribute
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE warning_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE entity_index
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE relationship_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE information_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE class_attribute_virtual
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE exception_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE enumeration_attribute
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE util_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE class_inheritance
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE attribute_has_dimension
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE virtualize_attribute
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE class_has_dimension
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE class_attribute_concrete
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE metadata_parameter
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE metadata_relationship
   SET parent_oid = '20080423NM00000000000000000000010144'
 WHERE parent_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20080423NM00000000000000000000010144'
 WHERE child_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE index_attribute
   SET child_oid = '20080423NM00000000000000000000010144'
 WHERE child_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE class_attribute
   SET child_oid = '20080423NM00000000000000000000010144'
 WHERE child_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE type_permissions
   SET child_oid = '20080423NM00000000000000000000010144'
 WHERE child_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE class_attribute_virtual
   SET child_oid = '20080423NM00000000000000000000010144'
 WHERE child_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE class_attribute_concrete
   SET child_oid = '20080423NM00000000000000000000010144'
 WHERE child_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE metadata_relationship
   SET child_oid = '20080423NM00000000000000000000010144'
 WHERE child_oid = '0972db6d-e588-366c-bd34-ee3e6f7d0068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20080423NM00000000000000000000010144'
 WHERE sort_md_attribute = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE object_tuple
   SET md_attribute_concrete = '20080423NM00000000000000000000010144'
 WHERE md_attribute_concrete = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE type_tuple
   SET metadata = '20080423NM00000000000000000000010144'
 WHERE metadata = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE md_parameter
   SET metadata = '20080423NM00000000000000000000010144'
 WHERE metadata = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20080423NM00000000000000000000010144'
 WHERE md_attribute_concrete = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20080423NM00000000000000000000010144'
 WHERE defining_md_attribute = '0972db6d-e588-366c-bd34-ee3e6f7d0068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '20080326NM00000000000000000000020144'
 WHERE oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE md_attribute_char
   SET oid = '20080326NM00000000000000000000020144'
 WHERE oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE md_attribute_primitive
   SET oid = '20080326NM00000000000000000000020144'
 WHERE oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE md_attribute_concrete
   SET oid = '20080326NM00000000000000000000020144'
 WHERE oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE md_attribute
   SET oid = '20080326NM00000000000000000000020144'
 WHERE oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE metadata
   SET oid = '20080326NM00000000000000000000020144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '276b512f-4618-3fec-9274-273ebb4c0068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE problem_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE dimension_has_attribute
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE dimension_has_class
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE view_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE business_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE md_method_method_actor
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE index_attribute
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE class_attribute
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE warning_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE entity_index
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE relationship_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE information_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE class_attribute_virtual
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE exception_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE enumeration_attribute
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE util_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE class_inheritance
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE attribute_has_dimension
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE virtualize_attribute
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE class_has_dimension
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE class_attribute_concrete
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE metadata_parameter
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE metadata_relationship
   SET parent_oid = '20080326NM00000000000000000000020144'
 WHERE parent_oid = '276b512f-4618-3fec-9274-273ebb4c0068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20080326NM00000000000000000000020144'
 WHERE child_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE index_attribute
   SET child_oid = '20080326NM00000000000000000000020144'
 WHERE child_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE class_attribute
   SET child_oid = '20080326NM00000000000000000000020144'
 WHERE child_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE type_permissions
   SET child_oid = '20080326NM00000000000000000000020144'
 WHERE child_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE class_attribute_virtual
   SET child_oid = '20080326NM00000000000000000000020144'
 WHERE child_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE class_attribute_concrete
   SET child_oid = '20080326NM00000000000000000000020144'
 WHERE child_oid = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE metadata_relationship
   SET child_oid = '20080326NM00000000000000000000020144'
 WHERE child_oid = '276b512f-4618-3fec-9274-273ebb4c0068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20080326NM00000000000000000000020144'
 WHERE sort_md_attribute = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE object_tuple
   SET md_attribute_concrete = '20080326NM00000000000000000000020144'
 WHERE md_attribute_concrete = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE type_tuple
   SET metadata = '20080326NM00000000000000000000020144'
 WHERE metadata = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE md_parameter
   SET metadata = '20080326NM00000000000000000000020144'
 WHERE metadata = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20080326NM00000000000000000000020144'
 WHERE md_attribute_concrete = '276b512f-4618-3fec-9274-273ebb4c0068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20080326NM00000000000000000000020144'
 WHERE defining_md_attribute = '276b512f-4618-3fec-9274-273ebb4c0068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = 'NM2008101100000000000000000000750144'
 WHERE oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE md_attribute_char
   SET oid = 'NM2008101100000000000000000000750144'
 WHERE oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE md_attribute_primitive
   SET oid = 'NM2008101100000000000000000000750144'
 WHERE oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE md_attribute_concrete
   SET oid = 'NM2008101100000000000000000000750144'
 WHERE oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE md_attribute
   SET oid = 'NM2008101100000000000000000000750144'
 WHERE oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE metadata
   SET oid = 'NM2008101100000000000000000000750144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE problem_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE dimension_def_struct_attr
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE dimension_has_attribute
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE dimension_has_class
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE view_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE business_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE md_method_method_actor
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE index_attribute
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE class_attribute
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE warning_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE entity_index
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE enumeration_attribute_item
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE relationship_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE information_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE class_attribute_virtual
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE exception_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE enumeration_attribute
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE util_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE class_inheritance
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE attribute_has_dimension
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE virtualize_attribute
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE class_has_dimension
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE class_attribute_concrete
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE metadata_parameter
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE metadata_relationship
   SET parent_oid = 'NM2008101100000000000000000000750144'
 WHERE parent_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = 'NM2008101100000000000000000000750144'
 WHERE child_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE index_attribute
   SET child_oid = 'NM2008101100000000000000000000750144'
 WHERE child_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE class_attribute
   SET child_oid = 'NM2008101100000000000000000000750144'
 WHERE child_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE type_permissions
   SET child_oid = 'NM2008101100000000000000000000750144'
 WHERE child_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE class_attribute_virtual
   SET child_oid = 'NM2008101100000000000000000000750144'
 WHERE child_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE class_attribute_concrete
   SET child_oid = 'NM2008101100000000000000000000750144'
 WHERE child_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE metadata_relationship
   SET child_oid = 'NM2008101100000000000000000000750144'
 WHERE child_oid = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = 'NM2008101100000000000000000000750144'
 WHERE sort_md_attribute = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE object_tuple
   SET md_attribute_concrete = 'NM2008101100000000000000000000750144'
 WHERE md_attribute_concrete = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE type_tuple
   SET metadata = 'NM2008101100000000000000000000750144'
 WHERE metadata = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE md_parameter
   SET metadata = 'NM2008101100000000000000000000750144'
 WHERE metadata = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = 'NM2008101100000000000000000000750144'
 WHERE md_attribute_concrete = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = 'NM2008101100000000000000000000750144'
 WHERE defining_md_attribute = 'fae4c6ab-bc07-3a08-a6be-1a82582a0068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE md_attribute_char
   SET oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE md_attribute_primitive
   SET oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE md_attribute_concrete
   SET oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE md_attribute
   SET oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE metadata
   SET oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE problem_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE dimension_has_attribute
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE dimension_has_class
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE view_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE business_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE md_method_method_actor
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE index_attribute
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE class_attribute
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE warning_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE entity_index
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE enumeration_attribute_item
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE relationship_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE information_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE class_attribute_virtual
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE exception_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE enumeration_attribute
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE util_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE class_inheritance
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE attribute_has_dimension
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE virtualize_attribute
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE class_has_dimension
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE class_attribute_concrete
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE metadata_parameter
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE metadata_relationship
   SET parent_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE parent_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE child_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE index_attribute
   SET child_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE child_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE class_attribute
   SET child_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE child_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE type_permissions
   SET child_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE child_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE class_attribute_virtual
   SET child_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE child_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE class_attribute_concrete
   SET child_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE child_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE metadata_relationship
   SET child_oid = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE child_oid = '3af9488e-67a9-3021-b6f3-f03c9a990068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE sort_md_attribute = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE object_tuple
   SET md_attribute_concrete = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE md_attribute_concrete = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE type_tuple
   SET metadata = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE metadata = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE md_parameter
   SET metadata = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE metadata = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE md_attribute_concrete = '3af9488e-67a9-3021-b6f3-f03c9a990068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '0d3j6jnotv7bcwuv83j1w7o4tltte71z0144'
 WHERE defining_md_attribute = '3af9488e-67a9-3021-b6f3-f03c9a990068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE md_attribute_char
   SET oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE md_attribute_primitive
   SET oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE md_attribute_concrete
   SET oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE md_attribute
   SET oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE metadata
   SET oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE problem_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE dimension_def_struct_attr
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE dimension_has_attribute
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE dimension_has_class
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE view_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE business_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE md_method_method_actor
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE index_attribute
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE class_attribute
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE warning_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE entity_index
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE enumeration_attribute_item
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE relationship_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE information_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE class_attribute_virtual
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE exception_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE enumeration_attribute
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE util_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE class_inheritance
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE attribute_has_dimension
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE virtualize_attribute
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE class_has_dimension
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE class_attribute_concrete
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE metadata_parameter
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE metadata_relationship
   SET parent_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE parent_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE child_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE index_attribute
   SET child_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE child_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE class_attribute
   SET child_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE child_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE type_permissions
   SET child_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE child_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE class_attribute_virtual
   SET child_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE child_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE class_attribute_concrete
   SET child_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE child_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE metadata_relationship
   SET child_oid = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE child_oid = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE sort_md_attribute = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE object_tuple
   SET md_attribute_concrete = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE md_attribute_concrete = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE type_tuple
   SET metadata = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE metadata = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE md_parameter
   SET metadata = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE metadata = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE md_attribute_concrete = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = 'sx1bsc4gxk1lrjkl544m7fak7sg2t1j80144'
 WHERE defining_md_attribute = '78f76f2c-6cb0-3e9d-8701-5a44d7370068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE md_attribute_char
   SET oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE md_attribute_primitive
   SET oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE md_attribute_concrete
   SET oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE md_attribute
   SET oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE metadata
   SET oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE problem_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE dimension_has_attribute
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE dimension_has_class
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE view_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE business_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE md_method_method_actor
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE index_attribute
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE class_attribute
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE warning_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE entity_index
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE enumeration_attribute_item
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE relationship_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE information_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE class_attribute_virtual
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE exception_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE enumeration_attribute
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE util_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE class_inheritance
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE attribute_has_dimension
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE virtualize_attribute
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE class_has_dimension
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE class_attribute_concrete
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE metadata_parameter
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE metadata_relationship
   SET parent_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE parent_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE child_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE index_attribute
   SET child_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE child_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE class_attribute
   SET child_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE child_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE type_permissions
   SET child_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE child_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE class_attribute_virtual
   SET child_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE child_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE class_attribute_concrete
   SET child_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE child_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE metadata_relationship
   SET child_oid = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE child_oid = 'a5627b60-1e30-3a39-ab70-f07af2a70068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE sort_md_attribute = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE object_tuple
   SET md_attribute_concrete = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE md_attribute_concrete = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE type_tuple
   SET metadata = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE metadata = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE md_parameter
   SET metadata = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE metadata = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE md_attribute_concrete = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '6ozempoo35sdmp69nkuagmeb0kejpe5r0144'
 WHERE defining_md_attribute = 'a5627b60-1e30-3a39-ab70-f07af2a70068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = 'NM2007041800000000000000000000400144'
 WHERE oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE md_attribute_char
   SET oid = 'NM2007041800000000000000000000400144'
 WHERE oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE md_attribute_primitive
   SET oid = 'NM2007041800000000000000000000400144'
 WHERE oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE md_attribute_concrete
   SET oid = 'NM2007041800000000000000000000400144'
 WHERE oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE md_attribute
   SET oid = 'NM2007041800000000000000000000400144'
 WHERE oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE metadata
   SET oid = 'NM2007041800000000000000000000400144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE problem_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE dimension_def_struct_attr
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE dimension_has_attribute
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE dimension_has_class
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE view_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE business_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE md_method_method_actor
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE index_attribute
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE class_attribute
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE warning_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE entity_index
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE enumeration_attribute_item
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE relationship_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE information_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE class_attribute_virtual
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE exception_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE enumeration_attribute
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE util_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE class_inheritance
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE attribute_has_dimension
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE virtualize_attribute
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE class_has_dimension
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE class_attribute_concrete
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE metadata_parameter
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE metadata_relationship
   SET parent_oid = 'NM2007041800000000000000000000400144'
 WHERE parent_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = 'NM2007041800000000000000000000400144'
 WHERE child_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE index_attribute
   SET child_oid = 'NM2007041800000000000000000000400144'
 WHERE child_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE class_attribute
   SET child_oid = 'NM2007041800000000000000000000400144'
 WHERE child_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE type_permissions
   SET child_oid = 'NM2007041800000000000000000000400144'
 WHERE child_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE class_attribute_virtual
   SET child_oid = 'NM2007041800000000000000000000400144'
 WHERE child_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE class_attribute_concrete
   SET child_oid = 'NM2007041800000000000000000000400144'
 WHERE child_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE metadata_relationship
   SET child_oid = 'NM2007041800000000000000000000400144'
 WHERE child_oid = '1459e6fa-501a-3fba-b51e-a5b05c240068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = 'NM2007041800000000000000000000400144'
 WHERE sort_md_attribute = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE object_tuple
   SET md_attribute_concrete = 'NM2007041800000000000000000000400144'
 WHERE md_attribute_concrete = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE type_tuple
   SET metadata = 'NM2007041800000000000000000000400144'
 WHERE metadata = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE md_parameter
   SET metadata = 'NM2007041800000000000000000000400144'
 WHERE metadata = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = 'NM2007041800000000000000000000400144'
 WHERE md_attribute_concrete = '1459e6fa-501a-3fba-b51e-a5b05c240068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = 'NM2007041800000000000000000000400144'
 WHERE defining_md_attribute = '1459e6fa-501a-3fba-b51e-a5b05c240068';

-- Updating item_ids in MdEnumeration tables.
-- Update the ids and the type fields in the object tables
UPDATE md_attribute_clob
   SET oid = '20070320EG00000000000000000014540144'
 WHERE oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE md_attribute_char
   SET oid = '20070320EG00000000000000000014540144'
 WHERE oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE md_attribute_primitive
   SET oid = '20070320EG00000000000000000014540144'
 WHERE oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE md_attribute_concrete
   SET oid = '20070320EG00000000000000000014540144'
 WHERE oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE md_attribute
   SET oid = '20070320EG00000000000000000014540144'
 WHERE oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE metadata
   SET oid = '20070320EG00000000000000000014540144',
       type = 'com.runwaysdk.system.metadata.MdAttributeClob'
 WHERE oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';


--Parent Relationships
UPDATE type_method
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE problem_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE dimension_def_struct_attr
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE dimension_has_attribute
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE dimension_has_class
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE view_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE business_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE md_method_method_actor
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE index_attribute
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE class_attribute
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE warning_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE entity_index
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE enumeration_attribute_item
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE relationship_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE information_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE class_attribute_virtual
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE exception_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE enumeration_attribute
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE util_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE class_inheritance
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE attribute_has_dimension
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE virtualize_attribute
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE class_has_dimension
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE class_attribute_concrete
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE metadata_parameter
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE metadata_relationship
   SET parent_oid = '20070320EG00000000000000000014540144'
 WHERE parent_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';


--Child Relationships
UPDATE dimension_def_struct_attr
   SET child_oid = '20070320EG00000000000000000014540144'
 WHERE child_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE index_attribute
   SET child_oid = '20070320EG00000000000000000014540144'
 WHERE child_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE class_attribute
   SET child_oid = '20070320EG00000000000000000014540144'
 WHERE child_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE type_permissions
   SET child_oid = '20070320EG00000000000000000014540144'
 WHERE child_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE class_attribute_virtual
   SET child_oid = '20070320EG00000000000000000014540144'
 WHERE child_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE class_attribute_concrete
   SET child_oid = '20070320EG00000000000000000014540144'
 WHERE child_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE metadata_relationship
   SET child_oid = '20070320EG00000000000000000014540144'
 WHERE child_oid = '245524a7-04fe-3c7c-8dff-eda3863d0068';


-- Reference Attributes
UPDATE md_relationship
   SET sort_md_attribute = '20070320EG00000000000000000014540144'
 WHERE sort_md_attribute = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE object_tuple
   SET md_attribute_concrete = '20070320EG00000000000000000014540144'
 WHERE md_attribute_concrete = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE type_tuple
   SET metadata = '20070320EG00000000000000000014540144'
 WHERE metadata = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE md_parameter
   SET metadata = '20070320EG00000000000000000014540144'
 WHERE metadata = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE md_attribute_virtual
   SET md_attribute_concrete = '20070320EG00000000000000000014540144'
 WHERE md_attribute_concrete = '245524a7-04fe-3c7c-8dff-eda3863d0068';

UPDATE md_attribute_dimension
   SET defining_md_attribute = '20070320EG00000000000000000014540144'
 WHERE defining_md_attribute = '245524a7-04fe-3c7c-8dff-eda3863d0068';

-- Updating item_ids in MdEnumeration tables.


COMMIT;

