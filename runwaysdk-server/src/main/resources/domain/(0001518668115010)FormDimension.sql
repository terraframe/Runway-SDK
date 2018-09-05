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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-02-15 16:19:07'  WHERE oid='fa678780-f2c4-336b-bfc7-bf724ceb0058';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('stt4g9j3g0j4jxtzuym39j58n00nu03a', '9ebbb1e6-7746-321d-97bd-344a323e0085');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('6nxk5u95obvp3g7gdfg9pj0t6zvf0l1g', '78a04c02-65e4-3aef-b74c-318f28360113');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8hsmmrdkpf1f978za3sre2isiu7yz5o3', '78a04c02-65e4-3aef-b74c-318f28360113');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('683e26cf-d5f4-3e10-a3b5-c42b20230287', '683e26cf-d5f4-3e10-a3b5-c42b20230287', 'www.runwaysdk.com', 'dimension');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('cf7b6a0b-2bc8-32b2-a7e6-0b20620f0287', 'cf7b6a0b-2bc8-32b2-a7e6-0b20620f0287', 'www.runwaysdk.com', NULL);
INSERT INTO md_attribute_reference ( default_value, md_business, oid) VALUES (NULL, 'f462d0eb-d815-3c97-b5d6-310842240058', '73bfd2ed-bf7f-3ea6-9968-db73217e0218');
INSERT INTO md_attribute_ref ( oid) VALUES ('73bfd2ed-bf7f-3ea6-9968-db73217e0218');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('fa678780-f2c4-336b-bfc7-bf724ceb0058', 'dimension', 'stt4g9j3g0j4jxtzuym39j58n00nu03a', '9ebbb1e6-7746-321d-97bd-344a323e0085', 'dimension', '6nxk5u95obvp3g7gdfg9pj0t6zvf0l1g', '78a04c02-65e4-3aef-b74c-318f28360113', 0, 'a6g4k7fkv3t0er1u3v8rhhzm72cmk1', '8hsmmrdkpf1f978za3sre2isiu7yz5o3', '78a04c02-65e4-3aef-b74c-318f28360113', 0, 1, '683e26cf-d5f4-3e10-a3b5-c42b20230287', 0, '73bfd2ed-bf7f-3ea6-9968-db73217e0218');
INSERT INTO md_attribute ( oid) VALUES ('73bfd2ed-bf7f-3ea6-9968-db73217e0218');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2018-02-15 16:19:07', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', 'a18798db-4bc4-3584-ad5e-844b7b760060', '2018-02-15 16:19:07', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'cf7b6a0b-2bc8-32b2-a7e6-0b20620f0287', NULL, 1, 'a18798db-4bc4-3584-ad5e-844b7b760060', NULL, '73bfd2ed-bf7f-3ea6-9968-db73217e0218', 'com.runwaysdk.system.metadata.MdForm.dimension', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('32083781-c249-3ae7-b3f0-967429500245', 'fa678780-f2c4-336b-bfc7-bf724ceb0058', '73bfd2ed-bf7f-3ea6-9968-db73217e0218');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('32083781-c249-3ae7-b3f0-967429500245', 'fa678780-f2c4-336b-bfc7-bf724ceb0058', '73bfd2ed-bf7f-3ea6-9968-db73217e0218');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('32083781-c249-3ae7-b3f0-967429500245', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-02-15 16:19:07', 'www.runwaysdk.com', 'a18798db-4bc4-3584-ad5e-844b7b760060', 4925, 'com.runwaysdk.system.metadata.MdForm.dimension', NULL, NULL, '2018-02-15 16:19:07', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'fa678780-f2c4-336b-bfc7-bf724ceb0058', '73bfd2ed-bf7f-3ea6-9968-db73217e0218');
ALTER TABLE md_form ADD COLUMN dimension  char(64);
CREATE INDEX a6g4k7fkv3t0er1u3v8rhhzm72cmk1 ON md_form (dimension);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-02-15 16:19:07'  WHERE oid='fa678780-f2c4-336b-bfc7-bf724ceb0058';
