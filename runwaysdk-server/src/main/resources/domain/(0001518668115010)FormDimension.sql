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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-02-15 16:19:07'  WHERE id='1yd2fgkyvjxstizcgyfqsu97qbpvpiru00000000000000000000000000000001';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('stt4g9j3g0j4jxtzuym39j58n00nu03a', '0000000000000000000000000000040000000000000000000000000000000403');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('6nxk5u95obvp3g7gdfg9pj0t6zvf0l1g', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8hsmmrdkpf1f978za3sre2isiu7yz5o3', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('z7sxexsbiin1s91ba4lhn8u99kthe6bhNM200904120000000000000000000030', 'z7sxexsbiin1s91ba4lhn8u99kthe6bhNM200904120000000000000000000030', 'www.runwaysdk.com', 'dimension');
INSERT INTO metadata_display_label ( key_name, id, site_master, default_locale) VALUES ('6x275x7w5fn40yyefeg1te06w3vjwc3lNM200904120000000000000000000030', '6x275x7w5fn40yyefeg1te06w3vjwc3lNM200904120000000000000000000030', 'www.runwaysdk.com', NULL);
INSERT INTO md_attribute_reference ( default_value, md_business, id) VALUES (NULL, '6nyuo8p2bb4now7x8owuvvhhekl28k7300000000000000000000000000000001', 'ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000186');
INSERT INTO md_attribute_ref ( id) VALUES ('ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000186');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, id) VALUES ('1yd2fgkyvjxstizcgyfqsu97qbpvpiru00000000000000000000000000000001', 'dimension', 'stt4g9j3g0j4jxtzuym39j58n00nu03a', '0000000000000000000000000000040000000000000000000000000000000403', 'dimension', '6nxk5u95obvp3g7gdfg9pj0t6zvf0l1g', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 'a6g4k7fkv3t0er1u3v8rhhzm72cmk1', '8hsmmrdkpf1f978za3sre2isiu7yz5o3', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 1, 'z7sxexsbiin1s91ba4lhn8u99kthe6bhNM200904120000000000000000000030', 0, 'ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000186');
INSERT INTO md_attribute ( id) VALUES ('ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000186');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, id, key_name, site_master) VALUES ('2018-02-15 16:19:07', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', '0000000000000000000000000000001000000000000000000000000000000003', '2018-02-15 16:19:07', '0000000000000000000000000000001000000000000000000000000000000003', '6x275x7w5fn40yyefeg1te06w3vjwc3lNM200904120000000000000000000030', NULL, 1, '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000186', 'com.runwaysdk.system.metadata.MdForm.dimension', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( id, parent_id, child_id) VALUES ('ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000073', '1yd2fgkyvjxstizcgyfqsu97qbpvpiru00000000000000000000000000000001', 'ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000186');
INSERT INTO class_attribute ( id, parent_id, child_id) VALUES ('ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000073', '1yd2fgkyvjxstizcgyfqsu97qbpvpiru00000000000000000000000000000001', 'ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000186');
INSERT INTO metadata_relationship ( id, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_id, child_id) VALUES ('ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-02-15 16:19:07', 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 4925, 'com.runwaysdk.system.metadata.MdForm.dimension', NULL, NULL, '2018-02-15 16:19:07', '0000000000000000000000000000001000000000000000000000000000000003', '1yd2fgkyvjxstizcgyfqsu97qbpvpiru00000000000000000000000000000001', 'ipe4y9a0jd39pobw45iqy7xk4j8a57of00000000000000000000000000000186');
ALTER TABLE md_form ADD COLUMN dimension  char(64);
CREATE INDEX a6g4k7fkv3t0er1u3v8rhhzm72cmk1 ON md_form (dimension);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-02-15 16:19:07'  WHERE id='1yd2fgkyvjxstizcgyfqsu97qbpvpiru00000000000000000000000000000001';
