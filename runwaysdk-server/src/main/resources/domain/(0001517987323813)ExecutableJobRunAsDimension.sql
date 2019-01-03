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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-02-07 00:40:01'  WHERE oid='4728e016-8064-393d-91dd-c6e8a100003a';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('xeribdhgj94gvi9r69qa7i5158q0wrui', 'cb6c78c1-3d48-333a-af1e-1885ca000055');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('1v1h9ukjtleg1evvc43ccjswj35gmpux', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('01rnxsfgjbgor5h7e58ah73d52c4eai0', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('77046532-0522-3df1-872a-1c40b300011f', '77046532-0522-3df1-872a-1c40b300011f', 'www.runwaysdk.com', 'runAsDimension');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('0cad2753-43c9-334d-9de5-d19ca800011f', '0cad2753-43c9-334d-9de5-d19ca800011f', 'www.runwaysdk.com', 'A dimension to run the job as.');
INSERT INTO md_attribute_reference ( default_value, md_business, oid) VALUES (NULL, '83ee5238-6a6e-3b0e-af02-904f3600003a', 'c55fe9e6-660e-3eaa-a127-46eca40000da');
INSERT INTO md_attribute_ref ( oid) VALUES ('c55fe9e6-660e-3eaa-a127-46eca40000da');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('4728e016-8064-393d-91dd-c6e8a100003a', 'run_as_dimension', 'xeribdhgj94gvi9r69qa7i5158q0wrui', 'cb6c78c1-3d48-333a-af1e-1885ca000055', 'runAsDimension', '1v1h9ukjtleg1evvc43ccjswj35gmpux', '02d46938-021e-3bb4-bb8f-4a31c5000071', 0, 'aqpsxgeqs9j5s90079eklut20eytm3', '01rnxsfgjbgor5h7e58ah73d52c4eai0', '02d46938-021e-3bb4-bb8f-4a31c5000071', 0, 1, '77046532-0522-3df1-872a-1c40b300011f', 0, 'c55fe9e6-660e-3eaa-a127-46eca40000da');
INSERT INTO md_attribute ( oid) VALUES ('c55fe9e6-660e-3eaa-a127-46eca40000da');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2018-02-07 00:40:01', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', '4f664e38-0546-31c7-a379-5492ef00003c', '2018-02-07 00:40:01', '4f664e38-0546-31c7-a379-5492ef00003c', '0cad2753-43c9-334d-9de5-d19ca800011f', NULL, 1, '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'c55fe9e6-660e-3eaa-a127-46eca40000da', 'com.runwaysdk.system.scheduler.ExecutableJob.runAsDimension', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('1fcab225-17f3-34c6-b22f-24ca970000f5', '4728e016-8064-393d-91dd-c6e8a100003a', 'c55fe9e6-660e-3eaa-a127-46eca40000da');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('1fcab225-17f3-34c6-b22f-24ca970000f5', '4728e016-8064-393d-91dd-c6e8a100003a', 'c55fe9e6-660e-3eaa-a127-46eca40000da');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('1fcab225-17f3-34c6-b22f-24ca970000f5', '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-02-07 00:40:01', 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', 4925, 'com.runwaysdk.system.scheduler.ExecutableJob.runAsDimension', NULL, NULL, '2018-02-07 00:40:01', '4f664e38-0546-31c7-a379-5492ef00003c', '4728e016-8064-393d-91dd-c6e8a100003a', 'c55fe9e6-660e-3eaa-a127-46eca40000da');
ALTER TABLE executable_job ADD COLUMN run_as_dimension  uuid;
CREATE INDEX aqpsxgeqs9j5s90079eklut20eytm3 ON executable_job (run_as_dimension);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-02-07 00:40:01'  WHERE oid='4728e016-8064-393d-91dd-c6e8a100003a';
