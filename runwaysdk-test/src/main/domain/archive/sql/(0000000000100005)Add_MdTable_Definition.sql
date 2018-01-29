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
INSERT INTO class_cache (set_id, item_id)  VALUES  ('xrf77gqotc4djb5l2n6fvldepphb9bv4', '0000000000000000000000000000023100000000000000000000000000000222');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('xvni2jh5i0c54qidszaa6pj1v8tr5bd4NM200904120000000000000000000030', 'xvni2jh5i0c54qidszaa6pj1v8tr5bd4NM200904120000000000000000000030', 'www.runwaysdk.com', 'MdTable');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('xfcn10lu08ylh4xf84rw86mo3bwnthe7NM200904120000000000000000000030', 'xfcn10lu08ylh4xf84rw86mo3bwnthe7NM200904120000000000000000000030', 'www.runwaysdk.com', 'Metadata for relational database tables for entities whose lifecylce is not direclty managed');
INSERT INTO md_business ( super_md_business, cache_algorithm, cache_algorithm_c, id) VALUES ('20070920NM000000000000000000000100000000000000000000000000000001', 'xrf77gqotc4djb5l2n6fvldepphb9bv4', '0000000000000000000000000000023100000000000000000000000000000222', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_element ( is_abstract, extendable, id) VALUES (0, 0, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_entity ( cache_size, query_dto_source, has_deterministic_ids, enforce_site_master, query_source, table_name, id) VALUES (0, NULL, 0, 1, NULL, 'md_table', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_class ( stub_source, publish, stub_dto_source, id) VALUES (NULL, 1, NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_type ( type_name, js_stub, display_label, exported, generate_source, js_base, base_source, package_name, root_id, dto_source, id) VALUES ('MdTable', NULL, 'xvni2jh5i0c54qidszaa6pj1v8tr5bd4NM200904120000000000000000000030', 1, 1, NULL, NULL, 'com.runwaysdk.system.metadata', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa', NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, id, key_name, site_master) VALUES ('2017-04-23 16:34:47', 4848, 'com.runwaysdk.system.metadata.MdBusiness', '0000000000000000000000000000001000000000000000000000000000000003', '2017-04-23 16:34:47', '0000000000000000000000000000001000000000000000000000000000000003', 'xfcn10lu08ylh4xf84rw86mo3bwnthe7NM200904120000000000000000000030', NULL, 0, '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'com.runwaysdk.system.metadata.MdTable', 'www.runwaysdk.com');
CREATE TABLE md_table ( id CHAR(64) NOT NULL PRIMARY KEY );
INSERT INTO business_inheritance ( id, parent_id, child_id) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa20070926NM0000000000000000000004', '20070920NM000000000000000000000100000000000000000000000000000001', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO class_inheritance ( id, parent_id, child_id) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa20070926NM0000000000000000000004', '20070920NM000000000000000000000100000000000000000000000000000001', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO metadata_relationship ( id, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_id, child_id) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa20070926NM0000000000000000000004', '0000000000000000000000000000001000000000000000000000000000000003', 'com.runwaysdk.system.metadata.BusinessInheritance', '2017-04-23 16:34:47', 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 4849, 'com.runwaysdk.system.metadata.MdTable', NULL, NULL, '2017-04-23 16:34:47', '0000000000000000000000000000001000000000000000000000000000000003', '20070920NM000000000000000000000100000000000000000000000000000001', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('93gc3e178316vh6cv7obrjnzw39pfttb', '0000000000000000000000000000040000000000000000000000000000000403');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('6tfb5gw15pnpypo9wgkgh022qnau0ktk', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('zjwuykqciu76jyl34l5pmw5orwcovo45', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('xd297xx9fv8jrgierz4s1qicktope4p8NM200904120000000000000000000030', 'xd297xx9fv8jrgierz4s1qicktope4p8NM200904120000000000000000000030', 'www.runwaysdk.com', 'Table Name');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('x98887cbfvrhotnsnasld6a6l3giccf2NM200904120000000000000000000030', 'x98887cbfvrhotnsnasld6a6l3giccf2NM200904120000000000000000000030', 'www.runwaysdk.com', 'Name of the table in the database');
INSERT INTO md_attribute_character ( default_value, database_size, id) VALUES (NULL, 128, 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO md_attribute_char ( id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO md_attribute_primitive ( is_expression, expression, id) VALUES (0, NULL, 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');

INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, id) 
VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'table_name', '93gc3e178316vh6cv7obrjnzw39pfttb', '0000000000000000000000000000040000000000000000000000000000000403', 'tableName', '6tfb5gw15pnpypo9wgkgh022qnau0ktk', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 'ahrctx4hpf4yozhza0132h51pww2un', 'zjwuykqciu76jyl34l5pmw5orwcovo45', '20071109NM000000000000000000000520071109NM0000000000000000000001', 1, 1, 'xd297xx9fv8jrgierz4s1qicktope4p8NM200904120000000000000000000030', 1, 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');

INSERT INTO md_attribute ( id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, id, key_name, site_master) VALUES ('2017-04-23 16:34:47', 4850, 'com.runwaysdk.system.metadata.MdAttributeCharacter', '0000000000000000000000000000001000000000000000000000000000000003', '2017-04-23 16:34:47', '0000000000000000000000000000001000000000000000000000000000000003', 'x98887cbfvrhotnsnasld6a6l3giccf2NM200904120000000000000000000030', NULL, 0, '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138', 'com.runwaysdk.system.metadata.MdTable.tableName', 'www.runwaysdk.com');
ALTER TABLE md_table ADD COLUMN table_name  varchar(128);
INSERT INTO class_attribute_concrete ( id, parent_id, child_id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000073', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO class_attribute ( id, parent_id, child_id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000073', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO metadata_relationship ( id, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_id, child_id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2017-04-23 16:34:47', 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 4851, 'com.runwaysdk.system.metadata.MdTable.tableName', NULL, NULL, '2017-04-23 16:34:47', '0000000000000000000000000000001000000000000000000000000000000003', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
CREATE INDEX ahrctx4hpf4yozhza0132h51pww2un ON md_table (table_name);
UPDATE md_business SET super_md_business= '20070920NM000000000000000000000100000000000000000000000000000001' , cache_algorithm= 'xrf77gqotc4djb5l2n6fvldepphb9bv4' , cache_algorithm_c= '0000000000000000000000000000023100000000000000000000000000000222' , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
UPDATE md_element SET extendable= 0 , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
UPDATE md_entity SET table_name= 'md_table' , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
UPDATE md_type SET type_name= 'MdTable' , display_label= 'xvni2jh5i0c54qidszaa6pj1v8tr5bd4NM200904120000000000000000000030' , package_name= 'com.runwaysdk.system.metadata' , root_id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa' , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
UPDATE metadata SET create_date= '2017-04-23 16:34:47' , seq= 4852 , type= 'com.runwaysdk.system.metadata.MdBusiness' , created_by= '0000000000000000000000000000001000000000000000000000000000000003' , last_update_date= '2017-04-23 16:34:47' , last_updated_by= '0000000000000000000000000000001000000000000000000000000000000003' , description= 'xfcn10lu08ylh4xf84rw86mo3bwnthe7NM200904120000000000000000000030' , remove= 0 , owner= '0000000000000000000000000000001000000000000000000000000000000003' , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001' , key_name= 'com.runwaysdk.system.metadata.MdTable' , site_master= 'www.runwaysdk.com'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
