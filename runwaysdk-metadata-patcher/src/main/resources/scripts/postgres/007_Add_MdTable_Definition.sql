
/**
 * Defines a new subclass of MdClass called MdTable to represent database tables that are not managed by the Runway Lifecycle.
 */
INSERT INTO class_cache (set_id, item_id)  VALUES  ('0elkulqo1xkn5ij41sld2klf556voz97', '0000000000000000000000000000023100000000000000000000000000000222');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('0efcpoitqzvxc3h5p5bxo7vu7c870tl0NM200904120000000000000000000030', '0efcpoitqzvxc3h5p5bxo7vu7c870tl0NM200904120000000000000000000030', 'www.runwaysdk.com', 'MdTable');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('8ef35tgkahrvh5bld5ixooc7xxnwclc3NM200904120000000000000000000030', '8ef35tgkahrvh5bld5ixooc7xxnwclc3NM200904120000000000000000000030', 'www.runwaysdk.com', 'Metadata for relational database tables for entities whose lifecylce is not direclty managed');
INSERT INTO md_business ( super_md_business, cache_algorithm, cache_algorithm_c, id) VALUES ('20070920NM000000000000000000000100000000000000000000000000000001', '0elkulqo1xkn5ij41sld2klf556voz97', '0000000000000000000000000000023100000000000000000000000000000222', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_element ( is_abstract, extendable, id) VALUES (0, 0, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_entity ( cache_size, query_dto_source, has_deterministic_ids, enforce_site_master, query_source, table_name, id) VALUES (0, NULL, 0, 1, NULL, 'md_table', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_class ( stub_source, publish, stub_dto_source, id) VALUES (NULL, 1, NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO md_type ( type_name, js_stub, display_label, exported, generate_source, js_base, base_source, package_name, root_id, dto_source, id) VALUES ('MdTable', NULL, '0efcpoitqzvxc3h5p5bxo7vu7c870tl0NM200904120000000000000000000030', 1, 1, NULL, NULL, 'com.runwaysdk.system.metadata', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa', NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, id, key_name, site_master) VALUES ('2017-04-22 21:18:11', 4848, 'com.runwaysdk.system.metadata.MdBusiness', '0000000000000000000000000000001000000000000000000000000000000003', '2017-04-22 21:18:11', '0000000000000000000000000000001000000000000000000000000000000003', '8ef35tgkahrvh5bld5ixooc7xxnwclc3NM200904120000000000000000000030', NULL, 0, '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'com.runwaysdk.system.metadata.MdTable', 'www.runwaysdk.com');
CREATE TABLE md_table ( id CHAR(64) NOT NULL PRIMARY KEY );
INSERT INTO business_inheritance ( id, parent_id, child_id) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa20070926NM0000000000000000000004', '20070920NM000000000000000000000100000000000000000000000000000001', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO class_inheritance ( id, parent_id, child_id) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa20070926NM0000000000000000000004', '20070920NM000000000000000000000100000000000000000000000000000001', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');
INSERT INTO metadata_relationship ( id, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_id, child_id) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa20070926NM0000000000000000000004', '0000000000000000000000000000001000000000000000000000000000000003', 'com.runwaysdk.system.metadata.BusinessInheritance', '2017-04-22 21:18:11', 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 4849, 'com.runwaysdk.system.metadata.MdTable', NULL, NULL, '2017-04-22 21:18:11', '0000000000000000000000000000001000000000000000000000000000000003', '20070920NM000000000000000000000100000000000000000000000000000001', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001');

INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('8cuxjk88k1gooyouxttupy8fp4y76cbc', '0000000000000000000000000000040900000000000000000000000000000403');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('20tqs1ou3o8dk5zcnv8rgjw2lt8166fe', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('r8lbm6w6t9onqifmrwfbve03mf5e317x', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('1nvczgweqh3h6srniqj7zi0x45cq695dNM200904120000000000000000000030', '1nvczgweqh3h6srniqj7zi0x45cq695dNM200904120000000000000000000030', 'www.runwaysdk.com', 'Table Name');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('6yce9wuwmlzzm1lbwrv86wsjtss9mltdNM200904120000000000000000000030', '6yce9wuwmlzzm1lbwrv86wsjtss9mltdNM200904120000000000000000000030', 'www.runwaysdk.com', 'Name of the table in the database');
INSERT INTO md_attribute_character ( default_value, database_size, id) VALUES (NULL, 128, 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO md_attribute_char ( id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO md_attribute_primitive ( is_expression, expression, id) VALUES (0, NULL, 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, id) VALUES ('imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'table_name', '8cuxjk88k1gooyouxttupy8fp4y76cbc', '0000000000000000000000000000040900000000000000000000000000000403', 'tableName', '20tqs1ou3o8dk5zcnv8rgjw2lt8166fe', '20071109NM000000000000000000000520071109NM0000000000000000000001', 1, 'ahrctx4hpf4yozhza0132h51pww2un', 'r8lbm6w6t9onqifmrwfbve03mf5e317x', '20071109NM000000000000000000000520071109NM0000000000000000000001', 1, 1, '1nvczgweqh3h6srniqj7zi0x45cq695dNM200904120000000000000000000030', 1, 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO md_attribute ( id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, id, key_name, site_master) VALUES ('2017-04-22 21:18:11', 4850, 'com.runwaysdk.system.metadata.MdAttributeCharacter', '0000000000000000000000000000001000000000000000000000000000000003', '2017-04-22 21:18:11', '0000000000000000000000000000001000000000000000000000000000000003', '6yce9wuwmlzzm1lbwrv86wsjtss9mltdNM200904120000000000000000000030', NULL, 0, '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138', 'com.runwaysdk.system.metadata.MdTable.tableName', 'www.runwaysdk.com');
ALTER TABLE md_table ADD COLUMN table_name  varchar(128);
INSERT INTO class_attribute_concrete ( id, parent_id, child_id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000073', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO class_attribute ( id, parent_id, child_id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000073', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
INSERT INTO metadata_relationship ( id, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_id, child_id) VALUES ('it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2017-04-22 21:18:11', 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 4851, 'com.runwaysdk.system.metadata.MdTable.tableName', NULL, NULL, '2017-04-22 21:18:11', '0000000000000000000000000000001000000000000000000000000000000003', 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001', 'it6uecms80f6em7ebxo7z5mun8vyv53500000000000000000000000000000138');
CREATE UNIQUE INDEX ahrctx4hpf4yozhza0132h51pww2un ON md_table (table_name);
UPDATE md_business SET super_md_business= '20070920NM000000000000000000000100000000000000000000000000000001' , cache_algorithm= '0elkulqo1xkn5ij41sld2klf556voz97' , cache_algorithm_c= '0000000000000000000000000000023100000000000000000000000000000222' , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
UPDATE md_element SET extendable= 0 , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
UPDATE md_entity SET table_name= 'md_table' , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
UPDATE md_type SET type_name= 'MdTable' , display_label= '0efcpoitqzvxc3h5p5bxo7vu7c870tl0NM200904120000000000000000000030' , package_name= 'com.runwaysdk.system.metadata' , root_id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa' , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';
UPDATE metadata SET create_date= '2017-04-22 21:18:11' , seq= 4852 , type= 'com.runwaysdk.system.metadata.MdBusiness' , created_by= '0000000000000000000000000000001000000000000000000000000000000003' , last_update_date= '2017-04-22 21:18:11' , last_updated_by= '0000000000000000000000000000001000000000000000000000000000000003' , description= '8ef35tgkahrvh5bld5ixooc7xxnwclc3NM200904120000000000000000000030' , remove= 0 , owner= '0000000000000000000000000000001000000000000000000000000000000003' , id= 'imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001' , key_name= 'com.runwaysdk.system.metadata.MdTable' , site_master= 'www.runwaysdk.com'  WHERE id='imleaq8bumv0cvrtwtct36s4vcl5qqpa00000000000000000000000000000001';