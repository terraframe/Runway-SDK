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

UPDATE metadata SET seq= 4923 , last_update_date= '2018-01-18 18:13:53'  WHERE oid='8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058';
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('6qxbctp6suhgz5jecc2stl1z9l251t6z', '000000000000000000000000000004000085');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8g6yidm1m25xfkxlerembn84wgatin14', '20071109NM00000000000000000000050113');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('x9nzf92x8oj8bq7k3jw2mhe6lmqwo0xr', '20071109NM00000000000000000000050113');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('s8s4mg06kfq2kf8vnoc2j5vgb95tt1ws0287', 's8s4mg06kfq2kf8vnoc2j5vgb95tt1ws0287', 'www.runwaysdk.com', 'runAsUser');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('0kr65x3hq0x44eocm30jwgzk2ndozet00287', '0kr65x3hq0x44eocm30jwgzk2ndozet00287', 'www.runwaysdk.com', 'A user to run the job as. If unspecified the job will be run as SYSTEM.');
INSERT INTO md_attribute_reference ( default_value, md_business, oid) VALUES (NULL, 'JS2007072300000000000000000015600058', 'ike7sy6e3wio8h4rzod1tsubfzkhxan20218');
INSERT INTO md_attribute_ref ( oid) VALUES ('ike7sy6e3wio8h4rzod1tsubfzkhxan20218');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058', 'run_as_user', '6qxbctp6suhgz5jecc2stl1z9l251t6z', '000000000000000000000000000004000085', 'runAsUser', '8g6yidm1m25xfkxlerembn84wgatin14', '20071109NM00000000000000000000050113', 0, 'af8ca4oqw223k7hcshd9utmpyix0in', 'x9nzf92x8oj8bq7k3jw2mhe6lmqwo0xr', '20071109NM00000000000000000000050113', 0, 1, 's8s4mg06kfq2kf8vnoc2j5vgb95tt1ws0287', 0, 'ike7sy6e3wio8h4rzod1tsubfzkhxan20218');
INSERT INTO md_attribute ( oid) VALUES ('ike7sy6e3wio8h4rzod1tsubfzkhxan20218');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2018-01-18 18:13:53', 4924, 'com.runwaysdk.system.metadata.MdAttributeReference', '000000000000000000000000000000100060', '2018-01-18 18:13:53', '000000000000000000000000000000100060', '0kr65x3hq0x44eocm30jwgzk2ndozet00287', NULL, 1, '000000000000000000000000000000100060', NULL, 'ike7sy6e3wio8h4rzod1tsubfzkhxan20218', 'com.runwaysdk.system.scheduler.ExecutableJob.runAsUser', 'www.runwaysdk.com');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('ike7sy6e3wio8h4rzod1tsubfzkhxan20245', '8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058', 'ike7sy6e3wio8h4rzod1tsubfzkhxan20218');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('ike7sy6e3wio8h4rzod1tsubfzkhxan20245', '8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058', 'ike7sy6e3wio8h4rzod1tsubfzkhxan20218');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('ike7sy6e3wio8h4rzod1tsubfzkhxan20245', '000000000000000000000000000000100060', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2018-01-18 18:13:53', 'www.runwaysdk.com', '000000000000000000000000000000100060', 4925, 'com.runwaysdk.system.scheduler.ExecutableJob.runAsUser', NULL, NULL, '2018-01-18 18:13:53', '000000000000000000000000000000100060', '8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058', 'ike7sy6e3wio8h4rzod1tsubfzkhxan20218');
ALTER TABLE executable_job ADD COLUMN run_as_user  char(64);
CREATE INDEX af8ca4oqw223k7hcshd9utmpyix0in ON executable_job (run_as_user);
UPDATE metadata SET seq= 4926 , last_update_date= '2018-01-18 18:13:53'  WHERE oid='8ndv254jvk1mw1rql6a4pqz9qbqvdzl40058';
