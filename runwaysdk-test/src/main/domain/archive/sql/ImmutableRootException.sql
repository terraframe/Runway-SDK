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

INSERT INTO dynamic_properties ( version_number, oid) VALUES ('0001404877087971', '000000000000000000000');
INSERT INTO md_localizable_message ( default_locale, oid, key_name, site_master) VALUES ('The Root node cannot be modified.', 'zgooawfw63bsiwqtttplrad9lfjjqxlzxox4feeesezr4kr18adyz3v3h2ntja5s', 'zgooawfw63bsiwqtttplrad9lfjjqxlzxox4feeesezr4kr18adyz3v3h2ntja5s', 'www.runwaysdk.com');
INSERT INTO metadata_display_label ( default_locale, oid, site_master, key_name) VALUES ('Immuatable root exception', '03y061z2aev031n9vum93mg60e9t6aa20287', 'www.runwaysdk.com', '03y061z2aev031n9vum93mg60e9t6aa20287');
INSERT INTO metadata_display_label ( default_locale, oid, site_master, key_name) VALUES ('Thrown when a user tries to modify a root Term.', 'r40vt2y98fj2upkll8myjznopjagoodr0287', 'www.runwaysdk.com', 'r40vt2y98fj2upkll8myjznopjagoodr0287');
INSERT INTO md_exception ( super_md_exception, oid) VALUES (NULL, 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003');
INSERT INTO md_localizable ( message, oid) VALUES ('zgooawfw63bsiwqtttplrad9lfjjqxlzxox4feeesezr4kr18adyz3v3h2ntja5s', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003');
INSERT INTO md_transient ( extendable, is_abstract, oid) VALUES (1, 0, 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003');
INSERT INTO md_class ( publish, stub_dto_source, stub_source, oid) VALUES (1, NULL, NULL, 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003');
INSERT INTO md_type ( display_label, root_id, package_name, type_name, base_source, js_base, js_stub, exported, dto_source, oid) VALUES ('03y061z2aev031n9vum93mg60e9t6aa20287', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd', 'com.runwaysdk.system.ontology', 'ImmutableRootException', NULL, NULL, NULL, 1, NULL, 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003');
INSERT INTO metadata ( description, created_by, owner, oid, create_date, seq, type, remove, site_master, locked_by, entity_domain, last_updated_by, last_update_date, key_name) VALUES ('r40vt2y98fj2upkll8myjznopjagoodr0287', '000000000000000000000000000000100060', '000000000000000000000000000000100060', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', '2014-07-08 21:42:05', 4793, 'com.runwaysdk.system.metadata.MdException', 1, 'www.runwaysdk.com', NULL, NULL, '000000000000000000000000000000100060', '2014-07-08 21:42:05', 'com.runwaysdk.system.ontology.ImmutableRootException');
 INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('6w18vrxct18dmbgn9od98y7mkalvnrr2', '20071109NM00000000000000000000050113');
INSERT INTO metadata_display_label ( default_locale, oid, site_master, key_name) VALUES ('ID', '8qg9iaqky306i66xp3evjwncwybnf97c0287', 'www.runwaysdk.com', '8qg9iaqky306i66xp3evjwncwybnf97c0287');
 INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('0efusfwlktxbqojw8kh8iy90ovbp90pg', '20071109NM00000000000000000000050113');
 INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('qr49h05k1hrhuvwmtjvvuh2tjcht5r7x', '000000000000000000000000000004090085');
INSERT INTO metadata_display_label ( default_locale, oid, site_master, key_name) VALUES ('ID of this object', '2cgf0mpty7kdq5x6mnue414ivwucmiyh0287', 'www.runwaysdk.com', '2cgf0mpty7kdq5x6mnue414ivwucmiyh0287');
INSERT INTO md_attribute_character ( default_value, database_size, oid) VALUES (NULL, 64, 'i93rdvqxgjs0g6a1fjump5o2zk517xiq0067');
INSERT INTO md_attribute_char ( oid) VALUES ('i93rdvqxgjs0g6a1fjump5o2zk517xiq0067');
INSERT INTO md_attribute_primitive ( oid) VALUES ('i93rdvqxgjs0g6a1fjump5o2zk517xiq0067');
INSERT INTO md_attribute_concrete ( column_name, defining_md_class, setter_visibility, setter_visibility_c, immutable, index_name, generate_accessor, system, required, display_label, getter_visibility, getter_visibility_c, index_type, index_type_c, attribute_name, oid) VALUES ('n_a', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', '6w18vrxct18dmbgn9od98y7mkalvnrr2', '20071109NM00000000000000000000050113', 0, NULL, 1, 1, 1, '8qg9iaqky306i66xp3evjwncwybnf97c0287', '0efusfwlktxbqojw8kh8iy90ovbp90pg', '20071109NM00000000000000000000050113', 'qr49h05k1hrhuvwmtjvvuh2tjcht5r7x', '000000000000000000000000000004090085', 'oid', 'i93rdvqxgjs0g6a1fjump5o2zk517xiq0067');
INSERT INTO md_attribute ( oid) VALUES ('i93rdvqxgjs0g6a1fjump5o2zk517xiq0067');
INSERT INTO metadata ( description, created_by, owner, oid, create_date, seq, type, remove, site_master, locked_by, entity_domain, last_updated_by, last_update_date, key_name) VALUES ('2cgf0mpty7kdq5x6mnue414ivwucmiyh0287', '000000000000000000000000000000100060', '000000000000000000000000000000100060', 'i93rdvqxgjs0g6a1fjump5o2zk517xiq0067', '2014-07-08 21:42:05', 4794, 'com.runwaysdk.system.metadata.MdAttributeCharacter', 0, 'www.runwaysdk.com', NULL, NULL, '000000000000000000000000000000100060', '2014-07-08 21:42:05', 'com.runwaysdk.system.ontology.ImmutableRootException.oid');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('i93rdvqxgjs0g6a1fjump5o2zk517xiq0245', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', 'i93rdvqxgjs0g6a1fjump5o2zk517xiq0067');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('i93rdvqxgjs0g6a1fjump5o2zk517xiq0245', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', 'i93rdvqxgjs0g6a1fjump5o2zk517xiq0067');
INSERT INTO metadata_relationship ( entity_domain, site_master, last_updated_by, oid, owner, locked_by, create_date, type, created_by, seq, key_name, last_update_date, parent_oid, child_oid) VALUES (NULL, 'www.runwaysdk.com', '000000000000000000000000000000100060', 'i93rdvqxgjs0g6a1fjump5o2zk517xiq0245', '000000000000000000000000000000100060', NULL, '2014-07-08 21:42:05', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '000000000000000000000000000000100060', 4795, 'com.runwaysdk.system.ontology.ImmutableRootException.oid', '2014-07-08 21:42:05', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', 'i93rdvqxgjs0g6a1fjump5o2zk517xiq0067');
 INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('27lgcq2cagv47v76hyw1wwi75chf55tm', '20071109NM00000000000000000000050113');
INSERT INTO metadata_display_label ( default_locale, oid, site_master, key_name) VALUES ('Root Name', 'xipiy5qwrj56pqq4691x23ahcgeffqtb0287', 'www.runwaysdk.com', 'xipiy5qwrj56pqq4691x23ahcgeffqtb0287');
 INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('93t3ortf3lly27t51qh1apt90iohjizq', '20071109NM00000000000000000000050113');
 INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('xqnx8pmqskd0sugz0g9zpdm0s2ham8wv', '000000000000000000000000000004010085');
INSERT INTO metadata_display_label ( default_locale, oid, site_master, key_name) VALUES ('Localized name of Root', 'xtoclkzo42den8uki3wmlpq30y03yvly0287', 'www.runwaysdk.com', 'xtoclkzo42den8uki3wmlpq30y03yvly0287');
INSERT INTO md_attribute_character ( default_value, database_size, oid) VALUES (NULL, 100, 'ilqko8t50vb8btvek0bza03rxq2mtj6d0067');
INSERT INTO md_attribute_char ( oid) VALUES ('ilqko8t50vb8btvek0bza03rxq2mtj6d0067');
INSERT INTO md_attribute_primitive ( oid) VALUES ('ilqko8t50vb8btvek0bza03rxq2mtj6d0067');
INSERT INTO md_attribute_concrete ( column_name, defining_md_class, setter_visibility, setter_visibility_c, immutable, index_name, generate_accessor, system, required, display_label, getter_visibility, getter_visibility_c, index_type, index_type_c, attribute_name, oid) VALUES ('n_a', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', '27lgcq2cagv47v76hyw1wwi75chf55tm', '20071109NM00000000000000000000050113', 0, NULL, 1, 0, 1, 'xipiy5qwrj56pqq4691x23ahcgeffqtb0287', '93t3ortf3lly27t51qh1apt90iohjizq', '20071109NM00000000000000000000050113', 'xqnx8pmqskd0sugz0g9zpdm0s2ham8wv', '000000000000000000000000000004010085', 'rootName', 'ilqko8t50vb8btvek0bza03rxq2mtj6d0067');
INSERT INTO md_attribute ( oid) VALUES ('ilqko8t50vb8btvek0bza03rxq2mtj6d0067');
INSERT INTO metadata ( description, created_by, owner, oid, create_date, seq, type, remove, site_master, locked_by, entity_domain, last_updated_by, last_update_date, key_name) VALUES ('xtoclkzo42den8uki3wmlpq30y03yvly0287', '000000000000000000000000000000100060', '000000000000000000000000000000100060', 'ilqko8t50vb8btvek0bza03rxq2mtj6d0067', '2014-07-08 21:42:05', 4796, 'com.runwaysdk.system.metadata.MdAttributeCharacter', 1, 'www.runwaysdk.com', NULL, NULL, '000000000000000000000000000000100060', '2014-07-08 21:42:05', 'com.runwaysdk.system.ontology.ImmutableRootException.rootName');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('ilqko8t50vb8btvek0bza03rxq2mtj6d0245', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', 'ilqko8t50vb8btvek0bza03rxq2mtj6d0067');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('ilqko8t50vb8btvek0bza03rxq2mtj6d0245', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', 'ilqko8t50vb8btvek0bza03rxq2mtj6d0067');
INSERT INTO metadata_relationship ( entity_domain, site_master, last_updated_by, oid, owner, locked_by, create_date, type, created_by, seq, key_name, last_update_date, parent_oid, child_oid) VALUES (NULL, 'www.runwaysdk.com', '000000000000000000000000000000100060', 'ilqko8t50vb8btvek0bza03rxq2mtj6d0245', '000000000000000000000000000000100060', NULL, '2014-07-08 21:42:05', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '000000000000000000000000000000100060', 4797, 'com.runwaysdk.system.ontology.ImmutableRootException.rootName', '2014-07-08 21:42:05', 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003', 'ilqko8t50vb8btvek0bza03rxq2mtj6d0067');
UPDATE md_localizable SET message= 'zgooawfw63bsiwqtttplrad9lfjjqxlzxox4feeesezr4kr18adyz3v3h2ntja5s' , oid= 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003'  WHERE oid='ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003';
UPDATE md_type SET display_label= '03y061z2aev031n9vum93mg60e9t6aa20287' , root_id= 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd' , package_name= 'com.runwaysdk.system.ontology' , type_name= 'ImmutableRootException' , oid= 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003'  WHERE oid='ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003';
UPDATE metadata SET description= 'r40vt2y98fj2upkll8myjznopjagoodr0287' , created_by= '000000000000000000000000000000100060' , owner= '000000000000000000000000000000100060' , oid= 'ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003' , create_date= '2014-07-08 21:42:05' , seq= 4798 , type= 'com.runwaysdk.system.metadata.MdException' , site_master= 'www.runwaysdk.com' , last_updated_by= '000000000000000000000000000000100060' , last_update_date= '2014-07-08 21:42:05' , key_name= 'com.runwaysdk.system.ontology.ImmutableRootException'  WHERE oid='ismjzdng4f9vu1apfz1n87yv3lyaw5bd20071129NM0000000000000000000003';
