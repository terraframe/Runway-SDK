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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-01-18 18:13:53'  WHERE oid='b12fe228-95fc-389e-9c08-826d148b0058';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('6qxbctp6suhgz5jecc2stl1z9l251t6z', '9ebbb1e6-7746-321d-97bd-344a323e0085');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8g6yidm1m25xfkxlerembn84wgatin14', '78a04c02-65e4-3aef-b74c-318f28360113');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('x9nzf92x8oj8bq7k3jw2mhe6lmqwo0xr', '78a04c02-65e4-3aef-b74c-318f28360113');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('2e50d311-f469-3282-ad41-2a9028590287', '2e50d311-f469-3282-ad41-2a9028590287', 'www.runwaysdk.com', 'runAsUser');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('bdb4ad1c-25d0-3205-83cf-05bafe4b0287', 'bdb4ad1c-25d0-3205-83cf-05bafe4b0287', 'www.runwaysdk.com', 'A user to run the job as. If unspecified the job will be run as SYSTEM.');
INSERT INTO md_attribute_reference ( default_value, md_business, oid) VALUES (NULL, 'f6dfa602-a926-38c7-8da2-4182faf00058', '3d6a38b9-b30a-3334-bdb4-838015d20218');
INSERT INTO md_attribute_ref ( oid) VALUES ('3d6a38b9-b30a-3334-bdb4-838015d20218');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('b12fe228-95fc-389e-9c08-826d148b0058', 'run_as_user', '6qxbctp6suhgz5jecc2stl1z9l251t6z', '9ebbb1e6-7746-321d-97bd-344a323e0085', 'runAsUser', '8g6yidm1m25xfkxlerembn84wgatin14', '78a04c02-65e4-3aef-b74c-318f28360113', 0, 'af8ca4oqw223k7hcshd9utmpyix0in', 'x9nzf92x8oj8bq7k3jw2mhe6lmqwo0xr', '78a04c02-65e4-3aef-b74c-318f28360113', 0, 1, '2e50d311-f469-3282-ad41-2a9028590287', 0, '3d6a38b9-b30a-3334-bdb4-838015d20218');
INSERT INTO md_attribute ( oid) VALUES ('3d6a38b9-b30a-3334-bdb4-838015d20218');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2018-01-18 18:13:53', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', 'a18798db-4bc4-3584-ad5e-844b7b760060', '2018-01-18 18:13:53', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'bdb4ad1c-25d0-3205-83cf-05bafe4b0287', NULL, 1, 'a18798db-4bc4-3584-ad5e-844b7b760060', NULL, '3d6a38b9-b30a-3334-bdb4-838015d20218', 'com.runwaysdk.system.scheduler.ExecutableJob.runAsUser', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('655dccc7-19af-3727-b31c-2bd81aec0245', 'b12fe228-95fc-389e-9c08-826d148b0058', '3d6a38b9-b30a-3334-bdb4-838015d20218');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('655dccc7-19af-3727-b31c-2bd81aec0245', 'b12fe228-95fc-389e-9c08-826d148b0058', '3d6a38b9-b30a-3334-bdb4-838015d20218');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('655dccc7-19af-3727-b31c-2bd81aec0245', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-01-18 18:13:53', 'www.runwaysdk.com', 'a18798db-4bc4-3584-ad5e-844b7b760060', 4925, 'com.runwaysdk.system.scheduler.ExecutableJob.runAsUser', NULL, NULL, '2018-01-18 18:13:53', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'b12fe228-95fc-389e-9c08-826d148b0058', '3d6a38b9-b30a-3334-bdb4-838015d20218');
ALTER TABLE executable_job ADD COLUMN run_as_user  uuid;
CREATE INDEX af8ca4oqw223k7hcshd9utmpyix0in ON executable_job (run_as_user);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-01-18 18:13:53'  WHERE oid='b12fe228-95fc-389e-9c08-826d148b0058';
