--
-- Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
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


/**
 * Defines a new subclass of MdClass called MdTable to represent database tables that are not managed by the Runway Lifecycle.
 */
INSERT INTO class_cache (set_id, item_id)  VALUES  ('xrf77gqotc4djb5l2n6fvldepphb9bv4', '000000000000000000000000000002310077');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('xvni2jh5i0c54qidszaa6pj1v8tr5bd40287', 'xvni2jh5i0c54qidszaa6pj1v8tr5bd40287', 'www.runwaysdk.com', 'MdTable');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('xfcn10lu08ylh4xf84rw86mo3bwnthe70287', 'xfcn10lu08ylh4xf84rw86mo3bwnthe70287', 'www.runwaysdk.com', 'Metadata for relational database tables for entities whose lifecylce is not direclty managed');
INSERT INTO md_business ( super_md_business, cache_algorithm, cache_algorithm_c, oid) VALUES ('20070920NM00000000000000000000010058', 'xrf77gqotc4djb5l2n6fvldepphb9bv4', '000000000000000000000000000002310077', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058');
INSERT INTO md_element ( is_abstract, extendable, oid) VALUES (0, 0, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058');
INSERT INTO md_entity ( cache_size, query_dto_source, has_deterministic_ids, enforce_site_master, query_source, table_name, oid) VALUES (0, NULL, 0, 1, NULL, 'md_table', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058');
INSERT INTO md_class ( stub_source, publish, stub_dto_source, oid) VALUES (NULL, 1, NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058');
INSERT INTO md_type ( type_name, js_stub, display_label, exported, generate_source, js_base, base_source, package_name, root_id, dto_source, oid) VALUES ('MdTable', NULL, 'xvni2jh5i0c54qidszaa6pj1v8tr5bd40287', 1, 1, NULL, NULL, 'com.runwaysdk.system.metadata', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa', NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2017-04-23 16:34:47', 4848, 'com.runwaysdk.system.metadata.MdBusiness', '000000000000000000000000000000100060', '2017-04-23 16:34:47', '000000000000000000000000000000100060', 'xfcn10lu08ylh4xf84rw86mo3bwnthe70287', NULL, 0, '000000000000000000000000000000100060', NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058', 'com.runwaysdk.system.metadata.MdTable', 'www.runwaysdk.com');
CREATE TABLE md_table ( oid CHAR(64) NOT NULL PRIMARY KEY );
INSERT INTO business_inheritance ( oid, parent_oid, child_oid) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa0257', '20070920NM00000000000000000000010058', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058');
INSERT INTO class_inheritance ( oid, parent_oid, child_oid) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa0257', '20070920NM00000000000000000000010058', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa0257', '000000000000000000000000000000100060', 'com.runwaysdk.system.metadata.BusinessInheritance', '2017-04-23 16:34:47', 'www.runwaysdk.com', '000000000000000000000000000000100060', 4849, 'com.runwaysdk.system.metadata.MdTable', NULL, NULL, '2017-04-23 16:34:47', '000000000000000000000000000000100060', '20070920NM00000000000000000000010058', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('93gc3e178316vh6cv7obrjnzw39pfttb', '000000000000000000000000000004000085');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('6tfb5gw15pnpypo9wgkgh022qnau0ktk', '20071109NM00000000000000000000050113');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('zjwuykqciu76jyl34l5pmw5orwcovo45', '20071109NM00000000000000000000050113');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('xd297xx9fv8jrgierz4s1qicktope4p80287', 'xd297xx9fv8jrgierz4s1qicktope4p80287', 'www.runwaysdk.com', 'Table Name');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('x98887cbfvrhotnsnasld6a6l3giccf20287', 'x98887cbfvrhotnsnasld6a6l3giccf20287', 'www.runwaysdk.com', 'Name of the table in the database');
INSERT INTO md_attribute_character ( default_value, database_size, oid) VALUES (NULL, 128, 'it6uecms80f6em7ebxo7z5mun8vyv5350067');
INSERT INTO md_attribute_char ( oid) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv5350067');
INSERT INTO md_attribute_primitive ( is_expression, expression, oid) VALUES (0, NULL, 'it6uecms80f6em7ebxo7z5mun8vyv5350067');

INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) 
VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa0058', 'table_name', '93gc3e178316vh6cv7obrjnzw39pfttb', '000000000000000000000000000004000085', 'tableName', '6tfb5gw15pnpypo9wgkgh022qnau0ktk', '20071109NM00000000000000000000050113', 0, 'ahrctx4hpf4yozhza0132h51pww2un', 'zjwuykqciu76jyl34l5pmw5orwcovo45', '20071109NM00000000000000000000050113', 1, 1, 'xd297xx9fv8jrgierz4s1qicktope4p80287', 1, 'it6uecms80f6em7ebxo7z5mun8vyv5350067');

INSERT INTO md_attribute ( oid) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv5350067');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2017-04-23 16:34:47', 4850, 'com.runwaysdk.system.metadata.MdAttributeCharacter', '000000000000000000000000000000100060', '2017-04-23 16:34:47', '000000000000000000000000000000100060', 'x98887cbfvrhotnsnasld6a6l3giccf20287', NULL, 0, '000000000000000000000000000000100060', NULL, 'it6uecms80f6em7ebxo7z5mun8vyv5350067', 'com.runwaysdk.system.metadata.MdTable.tableName', 'www.runwaysdk.com');
ALTER TABLE md_table ADD COLUMN table_name  varchar(128);
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv5350245', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058', 'it6uecms80f6em7ebxo7z5mun8vyv5350067');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv5350245', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058', 'it6uecms80f6em7ebxo7z5mun8vyv5350067');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv5350245', '000000000000000000000000000000100060', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2017-04-23 16:34:47', 'www.runwaysdk.com', '000000000000000000000000000000100060', 4851, 'com.runwaysdk.system.metadata.MdTable.tableName', NULL, NULL, '2017-04-23 16:34:47', '000000000000000000000000000000100060', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058', 'it6uecms80f6em7ebxo7z5mun8vyv5350067');
CREATE INDEX ahrctx4hpf4yozhza0132h51pww2un ON md_table (table_name);
UPDATE md_business SET super_md_business= '20070920NM00000000000000000000010058' , cache_algorithm= 'xrf77gqotc4djb5l2n6fvldepphb9bv4' , cache_algorithm_c= '000000000000000000000000000002310077' , oid= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058'  WHERE oid='imleaq8bumv0cvrtwtct36s4vcl5qqpa0058';
UPDATE md_element SET extendable= 0 , oid= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058'  WHERE oid='imleaq8bumv0cvrtwtct36s4vcl5qqpa0058';
UPDATE md_entity SET table_name= 'md_table' , oid= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058'  WHERE oid='imleaq8bumv0cvrtwtct36s4vcl5qqpa0058';
UPDATE md_type SET type_name= 'MdTable' , display_label= 'xvni2jh5i0c54qidszaa6pj1v8tr5bd40287' , package_name= 'com.runwaysdk.system.metadata' , root_id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa' , oid= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058'  WHERE oid='imleaq8bumv0cvrtwtct36s4vcl5qqpa0058';
UPDATE metadata SET create_date= '2017-04-23 16:34:47' , seq= 4852 , type= 'com.runwaysdk.system.metadata.MdBusiness' , created_by= '000000000000000000000000000000100060' , last_update_date= '2017-04-23 16:34:47' , last_updated_by= '000000000000000000000000000000100060' , description= 'xfcn10lu08ylh4xf84rw86mo3bwnthe70287' , remove= 0 , owner= '000000000000000000000000000000100060' , oid= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa0058' , key_name= 'com.runwaysdk.system.metadata.MdTable' , site_master= 'www.runwaysdk.com'  WHERE oid='imleaq8bumv0cvrtwtct36s4vcl5qqpa0058';
