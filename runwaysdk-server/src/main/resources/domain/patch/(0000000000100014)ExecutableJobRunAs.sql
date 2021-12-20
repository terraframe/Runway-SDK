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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-01-18 18:13:53'  WHERE oid='4728e016-8064-393d-91dd-c6e8a100003a';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('6qxbctp6suhgz5jecc2stl1z9l251t6z', 'cb6c78c1-3d48-333a-af1e-1885ca000055');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8g6yidm1m25xfkxlerembn84wgatin14', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('x9nzf92x8oj8bq7k3jw2mhe6lmqwo0xr', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('5c3da503-3981-3d5c-a162-ce594d00011f', '5c3da503-3981-3d5c-a162-ce594d00011f', 'www.runwaysdk.com', 'runAsUser');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('0a289afa-600a-3409-9707-4cd81c00011f', '0a289afa-600a-3409-9707-4cd81c00011f', 'www.runwaysdk.com', 'A user to run the job as. If unspecified the job will be run as SYSTEM.');
INSERT INTO md_attribute_reference ( default_value, md_business, oid) VALUES (NULL, '7d2091fd-69c2-3524-aa9b-9e203e00003a', '148ee0ea-f4bf-383b-9e8d-8908770000da');
INSERT INTO md_attribute_ref ( oid) VALUES ('148ee0ea-f4bf-383b-9e8d-8908770000da');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('4728e016-8064-393d-91dd-c6e8a100003a', 'run_as_user', '6qxbctp6suhgz5jecc2stl1z9l251t6z', 'cb6c78c1-3d48-333a-af1e-1885ca000055', 'runAsUser', '8g6yidm1m25xfkxlerembn84wgatin14', '02d46938-021e-3bb4-bb8f-4a31c5000071', 0, 'af8ca4oqw223k7hcshd9utmpyix0in', 'x9nzf92x8oj8bq7k3jw2mhe6lmqwo0xr', '02d46938-021e-3bb4-bb8f-4a31c5000071', 0, 1, '5c3da503-3981-3d5c-a162-ce594d00011f', 0, '148ee0ea-f4bf-383b-9e8d-8908770000da');
INSERT INTO md_attribute ( oid) VALUES ('148ee0ea-f4bf-383b-9e8d-8908770000da');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2018-01-18 18:13:53', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', '4f664e38-0546-31c7-a379-5492ef00003c', '2018-01-18 18:13:53', '4f664e38-0546-31c7-a379-5492ef00003c', '0a289afa-600a-3409-9707-4cd81c00011f', NULL, 1, '4f664e38-0546-31c7-a379-5492ef00003c', NULL, '148ee0ea-f4bf-383b-9e8d-8908770000da', 'com.runwaysdk.system.scheduler.ExecutableJob.runAsUser', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('1aa6e8ad-9646-34b1-b6dc-cd0b5f0000f5', '4728e016-8064-393d-91dd-c6e8a100003a', '148ee0ea-f4bf-383b-9e8d-8908770000da');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('1aa6e8ad-9646-34b1-b6dc-cd0b5f0000f5', '4728e016-8064-393d-91dd-c6e8a100003a', '148ee0ea-f4bf-383b-9e8d-8908770000da');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('1aa6e8ad-9646-34b1-b6dc-cd0b5f0000f5', '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-01-18 18:13:53', 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', 4925, 'com.runwaysdk.system.scheduler.ExecutableJob.runAsUser', NULL, NULL, '2018-01-18 18:13:53', '4f664e38-0546-31c7-a379-5492ef00003c', '4728e016-8064-393d-91dd-c6e8a100003a', '148ee0ea-f4bf-383b-9e8d-8908770000da');
ALTER TABLE executable_job ADD COLUMN run_as_user  uuid;
CREATE INDEX af8ca4oqw223k7hcshd9utmpyix0in ON executable_job (run_as_user);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-01-18 18:13:53'  WHERE oid='4728e016-8064-393d-91dd-c6e8a100003a';
