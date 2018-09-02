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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-02-07 00:40:01'  WHERE oid='8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('xeribdhgj94gvi9r69qa7i5158q0wrui', '0000000000000000000000000000040000000000000000000000000000000403');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('1v1h9ukjtleg1evvc43ccjswj35gmpux', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('01rnxsfgjbgor5h7e58ah73d52c4eai0', '20071109NM000000000000000000000520071109NM0000000000000000000001');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('r8g4uu6h0hdjghpo6vt91lrzi1gqswfrNM200904120000000000000000000030', 'r8g4uu6h0hdjghpo6vt91lrzi1gqswfrNM200904120000000000000000000030', 'www.runwaysdk.com', 'runAsDimension');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('7a29umgdv5xo2jor6n3aqld1l2gey350NM200904120000000000000000000030', '7a29umgdv5xo2jor6n3aqld1l2gey350NM200904120000000000000000000030', 'www.runwaysdk.com', 'A dimension to run the job as.');
INSERT INTO md_attribute_reference ( default_value, md_business, oid) VALUES (NULL, '6nyuo8p2bb4now7x8owuvvhhekl28k730058', 'iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000186');
INSERT INTO md_attribute_ref ( oid) VALUES ('iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000186');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058', 'run_as_dimension', 'xeribdhgj94gvi9r69qa7i5158q0wrui', '0000000000000000000000000000040000000000000000000000000000000403', 'runAsDimension', '1v1h9ukjtleg1evvc43ccjswj35gmpux', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 'aqpsxgeqs9j5s90079eklut20eytm3', '01rnxsfgjbgor5h7e58ah73d52c4eai0', '20071109NM000000000000000000000520071109NM0000000000000000000001', 0, 1, 'r8g4uu6h0hdjghpo6vt91lrzi1gqswfrNM200904120000000000000000000030', 0, 'iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000186');
INSERT INTO md_attribute ( oid) VALUES ('iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000186');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2018-02-07 00:40:01', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', '0000000000000000000000000000001000000000000000000000000000000003', '2018-02-07 00:40:01', '0000000000000000000000000000001000000000000000000000000000000003', '7a29umgdv5xo2jor6n3aqld1l2gey350NM200904120000000000000000000030', NULL, 1, '0000000000000000000000000000001000000000000000000000000000000003', NULL, 'iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000186', 'com.runwaysdk.system.scheduler.ExecutableJob.runAsDimension', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000073', '8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058', 'iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000186');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000073', '8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058', 'iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000186');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000073', '0000000000000000000000000000001000000000000000000000000000000003', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-02-07 00:40:01', 'www.runwaysdk.com', '0000000000000000000000000000001000000000000000000000000000000003', 4925, 'com.runwaysdk.system.scheduler.ExecutableJob.runAsDimension', NULL, NULL, '2018-02-07 00:40:01', '0000000000000000000000000000001000000000000000000000000000000003', '8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058', 'iep2g0n77ibwh5u040axtxkuwqkjev6h00000000000000000000000000000186');
ALTER TABLE executable_job ADD COLUMN run_as_dimension  char(64);
CREATE INDEX aqpsxgeqs9j5s90079eklut20eytm3 ON executable_job (run_as_dimension);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-02-07 00:40:01'  WHERE oid='8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058';
