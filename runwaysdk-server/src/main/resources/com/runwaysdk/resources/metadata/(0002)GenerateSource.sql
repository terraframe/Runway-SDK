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

INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('1zm0f1lbs4aqb1a2z3sdg2ge4rexx4ohNM200904120000000000000000000030', '1zm0f1lbs4aqb1a2z3sdg2ge4rexx4ohNM200904120000000000000000000030', 'www.runwaysdk.com', 'True');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('0gez1iyxq9vie24c8g33ddp7ysqxooduNM200904120000000000000000000030', '0gez1iyxq9vie24c8g33ddp7ysqxooduNM200904120000000000000000000030', 'www.runwaysdk.com', 'False');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('7d02l8miuwrzas49lw4uq6n84z6n6thf', '0000000000000000000000000000040100000000000000000000000000000403');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('sctntjsm7rn8dmmdlyvgipiozwzn7pho', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8ww3qlnkptnjzxlwa1bakf3idmzvzjyv', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('xvk7k49z2qig0b7qmhwhtcu6zk6pqhzoNM200904120000000000000000000030', 'xvk7k49z2qig0b7qmhwhtcu6zk6pqhzoNM200904120000000000000000000030', 'www.runwaysdk.com', 'Generate source');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('zc74cnoilbg1pqrryvin3qss0t7a2lgjNM200904120000000000000000000030', 'zc74cnoilbg1pqrryvin3qss0t7a2lgjNM200904120000000000000000000030', 'www.runwaysdk.com', 'Flag indicating if source should be generate for this type');
INSERT INTO md_attribute_boolean ( positive_display_label, negative_display_label, default_value, id) VALUES ('1zm0f1lbs4aqb1a2z3sdg2ge4rexx4ohNM200904120000000000000000000030', '0gez1iyxq9vie24c8g33ddp7ysqxooduNM200904120000000000000000000030', 1, 'isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000194');
INSERT INTO md_attribute_primitive ( is_expression, expression, id) VALUES (0, NULL, 'isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000194');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, id) VALUES ('0000000000000000000000000000018000000000000000000000000000000001', 'generate_source', '7d02l8miuwrzas49lw4uq6n84z6n6thf', '0000000000000000000000000000040100000000000000000000000000000403', 'generateSource', 'sctntjsm7rn8dmmdlyvgipiozwzn7pho', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 'ald5kgp7ug3cj1z6p2zveo7tna5top', '8ww3qlnkptnjzxlwa1bakf3idmzvzjyv', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 1, 'xvk7k49z2qig0b7qmhwhtcu6zk6pqhzoNM200904120000000000000000000030', 0, 'isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000194');
INSERT INTO md_attribute ( id) VALUES ('isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000194');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, id, key_name, site_master) VALUES ('2016-02-22 11:32:48', 4808, 'com.runwaysdk.system.metadata.MdAttributeBoolean', '0000000000000000000000000000001000000000000000000000000000000003', '2016-02-22 11:32:48', '0000000000000000000000000000001000000000000000000000000000000003', 'zc74cnoilbg1pqrryvin3qss0t7a2lgjNM200904120000000000000000000030', NULL, 1, '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000194', 'com.runwaysdk.system.metadata.MdType.generateSource', 'www.runwaysdk.com');
ALTER TABLE md_type ADD COLUMN generate_source  int;
INSERT INTO class_attribute_concrete ( id, parent_id, child_id) VALUES ('isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000073', '0000000000000000000000000000018000000000000000000000000000000001', 'isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000194');
INSERT INTO class_attribute ( id, parent_id, child_id) VALUES ('isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000073', '0000000000000000000000000000018000000000000000000000000000000001', 'isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000194');
INSERT INTO metadata_relationship ( id, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_id, child_id) VALUES ('isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2016-02-22 11:32:48', 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 4809, 'com.runwaysdk.system.metadata.MdType.generateSource', NULL, NULL, '2016-02-22 11:32:48', '0000000000000000000000000000001000000000000000000000000000000003', '0000000000000000000000000000018000000000000000000000000000000001', 'isrngp1e1q4obp5y5atb7yv9lby4nuji00000000000000000000000000000194');
