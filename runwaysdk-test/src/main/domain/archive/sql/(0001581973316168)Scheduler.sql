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

UPDATE metadata SET seq= 5754 , last_update_date= '2020-02-17 17:36:41'  WHERE oid='c73b3365-8784-3c09-834f-7afca400003a';
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('6a1dd6fb-658d-4848-be6b-5c0a5400011f', 'errorJson', '6a1dd6fb-658d-4848-be6b-5c0a5400011f', 'www.runwaysdk.com');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('3c883103-ebf4-4763-bb68-8e72ea', '9a7f73ee-81a9-32e9-884e-c4be61000055');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('fd25eacb-4b1a-4437-9faf-02fb58', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('a14a8458-4e86-4b73-99cd-ca6541', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('8e167a83-5481-4ad7-8b5d-2789ee00011f', NULL, '8e167a83-5481-4ad7-8b5d-2789ee00011f', 'www.runwaysdk.com');
INSERT INTO md_attribute_text ( default_value, oid) VALUES (NULL, 'c7b4d0b5-e301-3398-88bd-8d72bb000044');
INSERT INTO md_attribute_char ( oid) VALUES ('c7b4d0b5-e301-3398-88bd-8d72bb000044');
INSERT INTO md_attribute_primitive ( is_expression, expression, oid) VALUES (0, NULL, 'c7b4d0b5-e301-3398-88bd-8d72bb000044');
INSERT INTO md_attribute_concrete ( display_label, index_name, attribute_name, immutable, system, required, index_type, index_type_c, column_name, generate_accessor, getter_visibility, getter_visibility_c, defining_md_class, setter_visibility, setter_visibility_c, oid) VALUES ('6a1dd6fb-658d-4848-be6b-5c0a5400011f', 'ac170853c7026380884f30f36d4', 'errorJson', 0, 0, 0, '3c883103-ebf4-4763-bb68-8e72ea', '9a7f73ee-81a9-32e9-884e-c4be61000055', 'error_json', 1, 'fd25eacb-4b1a-4437-9faf-02fb58', '02d46938-021e-3bb4-bb8f-4a31c5000071', 'c73b3365-8784-3c09-834f-7afca400003a', 'a14a8458-4e86-4b73-99cd-ca6541', '02d46938-021e-3bb4-bb8f-4a31c5000071', 'c7b4d0b5-e301-3398-88bd-8d72bb000044');
INSERT INTO md_attribute ( oid) VALUES ('c7b4d0b5-e301-3398-88bd-8d72bb000044');
INSERT INTO metadata ( seq, owner, created_by, entity_domain, type, locked_by, site_master, last_updated_by, create_date, oid, key_name, remove, last_update_date, description) VALUES (5755, '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'com.runwaysdk.system.metadata.MdAttributeText', NULL, 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-17 17:36:41', 'c7b4d0b5-e301-3398-88bd-8d72bb000044', 'com.runwaysdk.system.scheduler.JobHistory.errorJson', 1, '2020-02-17 17:36:41', '8e167a83-5481-4ad7-8b5d-2789ee00011f');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('c7b4d0b5-e301-3398-88bd-8d72bb0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', 'c7b4d0b5-e301-3398-88bd-8d72bb000044');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('c7b4d0b5-e301-3398-88bd-8d72bb0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', 'c7b4d0b5-e301-3398-88bd-8d72bb000044');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.JobHistory.errorJson', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-17 17:36:41', 5756, '2020-02-17 17:36:41', NULL, 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '4f664e38-0546-31c7-a379-5492ef00003c', 'c7b4d0b5-e301-3398-88bd-8d72bb0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', 'c7b4d0b5-e301-3398-88bd-8d72bb000044');
ALTER TABLE job_history ADD COLUMN error_json  text;
