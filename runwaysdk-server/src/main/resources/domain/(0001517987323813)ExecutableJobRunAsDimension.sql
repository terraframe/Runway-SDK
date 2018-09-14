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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-02-07 00:40:01'  WHERE oid='b12fe228-95fc-389e-9c08-826d148b0058';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('xeribdhgj94gvi9r69qa7i5158q0wrui', '9ebbb1e6-7746-321d-97bd-344a323e0085');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('1v1h9ukjtleg1evvc43ccjswj35gmpux', '78a04c02-65e4-3aef-b74c-318f28360113');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('01rnxsfgjbgor5h7e58ah73d52c4eai0', '78a04c02-65e4-3aef-b74c-318f28360113');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('aea61825-4191-3110-bd24-f5c373af0287', 'aea61825-4191-3110-bd24-f5c373af0287', 'www.runwaysdk.com', 'runAsDimension');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('645b0b59-73ef-3c40-b2d1-f42aa1b50287', '645b0b59-73ef-3c40-b2d1-f42aa1b50287', 'www.runwaysdk.com', 'A dimension to run the job as.');
INSERT INTO md_attribute_reference ( default_value, md_business, oid) VALUES (NULL, 'f462d0eb-d815-3c97-b5d6-310842240058', '071c62bb-e2ff-3f78-a502-a5c7a3240218');
INSERT INTO md_attribute_ref ( oid) VALUES ('071c62bb-e2ff-3f78-a502-a5c7a3240218');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('b12fe228-95fc-389e-9c08-826d148b0058', 'run_as_dimension', 'xeribdhgj94gvi9r69qa7i5158q0wrui', '9ebbb1e6-7746-321d-97bd-344a323e0085', 'runAsDimension', '1v1h9ukjtleg1evvc43ccjswj35gmpux', '78a04c02-65e4-3aef-b74c-318f28360113', 0, 'aqpsxgeqs9j5s90079eklut20eytm3', '01rnxsfgjbgor5h7e58ah73d52c4eai0', '78a04c02-65e4-3aef-b74c-318f28360113', 0, 1, 'aea61825-4191-3110-bd24-f5c373af0287', 0, '071c62bb-e2ff-3f78-a502-a5c7a3240218');
INSERT INTO md_attribute ( oid) VALUES ('071c62bb-e2ff-3f78-a502-a5c7a3240218');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2018-02-07 00:40:01', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', 'a18798db-4bc4-3584-ad5e-844b7b760060', '2018-02-07 00:40:01', 'a18798db-4bc4-3584-ad5e-844b7b760060', '645b0b59-73ef-3c40-b2d1-f42aa1b50287', NULL, 1, 'a18798db-4bc4-3584-ad5e-844b7b760060', NULL, '071c62bb-e2ff-3f78-a502-a5c7a3240218', 'com.runwaysdk.system.scheduler.ExecutableJob.runAsDimension', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('7d728a81-9d8f-35ee-8ea4-e58e60c60245', 'b12fe228-95fc-389e-9c08-826d148b0058', '071c62bb-e2ff-3f78-a502-a5c7a3240218');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('7d728a81-9d8f-35ee-8ea4-e58e60c60245', 'b12fe228-95fc-389e-9c08-826d148b0058', '071c62bb-e2ff-3f78-a502-a5c7a3240218');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('7d728a81-9d8f-35ee-8ea4-e58e60c60245', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-02-07 00:40:01', 'www.runwaysdk.com', 'a18798db-4bc4-3584-ad5e-844b7b760060', 4925, 'com.runwaysdk.system.scheduler.ExecutableJob.runAsDimension', NULL, NULL, '2018-02-07 00:40:01', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'b12fe228-95fc-389e-9c08-826d148b0058', '071c62bb-e2ff-3f78-a502-a5c7a3240218');
ALTER TABLE executable_job ADD COLUMN run_as_dimension  uuid;
CREATE INDEX aqpsxgeqs9j5s90079eklut20eytm3 ON executable_job (run_as_dimension);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-02-07 00:40:01'  WHERE oid='b12fe228-95fc-389e-9c08-826d148b0058';
