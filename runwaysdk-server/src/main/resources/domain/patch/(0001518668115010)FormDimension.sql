--
-- Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-02-15 16:19:07'  WHERE oid='9325be95-db24-3191-a81c-b39a2300003a';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('stt4g9j3g0j4jxtzuym39j58n00nu03a', 'cb6c78c1-3d48-333a-af1e-1885ca000055');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('6nxk5u95obvp3g7gdfg9pj0t6zvf0l1g', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8hsmmrdkpf1f978za3sre2isiu7yz5o3', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('70af085e-449b-3fe9-a2ea-c8889500011f', '70af085e-449b-3fe9-a2ea-c8889500011f', 'www.runwaysdk.com', 'dimension');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('1f55b77d-f71a-3481-a505-49a94100011f', '1f55b77d-f71a-3481-a505-49a94100011f', 'www.runwaysdk.com', NULL);
INSERT INTO md_attribute_reference ( default_value, md_business, oid) VALUES (NULL, '83ee5238-6a6e-3b0e-af02-904f3600003a', 'eb22a7ea-0228-39cb-99db-7769780000da');
INSERT INTO md_attribute_ref ( oid) VALUES ('eb22a7ea-0228-39cb-99db-7769780000da');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('9325be95-db24-3191-a81c-b39a2300003a', 'dimension', 'stt4g9j3g0j4jxtzuym39j58n00nu03a', 'cb6c78c1-3d48-333a-af1e-1885ca000055', 'dimension', '6nxk5u95obvp3g7gdfg9pj0t6zvf0l1g', '02d46938-021e-3bb4-bb8f-4a31c5000071', 0, 'a6g4k7fkv3t0er1u3v8rhhzm72cmk1', '8hsmmrdkpf1f978za3sre2isiu7yz5o3', '02d46938-021e-3bb4-bb8f-4a31c5000071', 0, 1, '70af085e-449b-3fe9-a2ea-c8889500011f', 0, 'eb22a7ea-0228-39cb-99db-7769780000da');
INSERT INTO md_attribute ( oid) VALUES ('eb22a7ea-0228-39cb-99db-7769780000da');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2018-02-15 16:19:07', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', '4f664e38-0546-31c7-a379-5492ef00003c', '2018-02-15 16:19:07', '4f664e38-0546-31c7-a379-5492ef00003c', '1f55b77d-f71a-3481-a505-49a94100011f', NULL, 1, '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'eb22a7ea-0228-39cb-99db-7769780000da', 'com.runwaysdk.system.metadata.MdForm.dimension', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('043f33a7-9e18-3b93-ac04-bd6c470000f5', '9325be95-db24-3191-a81c-b39a2300003a', 'eb22a7ea-0228-39cb-99db-7769780000da');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('043f33a7-9e18-3b93-ac04-bd6c470000f5', '9325be95-db24-3191-a81c-b39a2300003a', 'eb22a7ea-0228-39cb-99db-7769780000da');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('043f33a7-9e18-3b93-ac04-bd6c470000f5', '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-02-15 16:19:07', 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', 4925, 'com.runwaysdk.system.metadata.MdForm.dimension', NULL, NULL, '2018-02-15 16:19:07', '4f664e38-0546-31c7-a379-5492ef00003c', '9325be95-db24-3191-a81c-b39a2300003a', 'eb22a7ea-0228-39cb-99db-7769780000da');
ALTER TABLE md_form ADD COLUMN dimension uuid;
CREATE INDEX a6g4k7fkv3t0er1u3v8rhhzm72cmk1 ON md_form (dimension);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-02-15 16:19:07'  WHERE oid='9325be95-db24-3191-a81c-b39a2300003a';
