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

DROP INDEX ac4fac4tskng9hvkhbpbsx0yi61kqo;
DELETE FROM class_attribute_concrete WHERE oid = '1cf6d53b-7d5c-3b04-b20d-aeecf8d50245';
DELETE FROM class_attribute WHERE oid = '1cf6d53b-7d5c-3b04-b20d-aeecf8d50245';
DELETE FROM metadata_relationship WHERE oid = '1cf6d53b-7d5c-3b04-b20d-aeecf8d50245' AND seq = 2665;
DELETE FROM md_attribute_indicies WHERE set_id='034f0hok2g7fek7v7doso5x4iw7hugxi';
DELETE FROM visibilitymodifier WHERE set_id='21n3rt68z4jhpmi86yeblp1xg79aglfy';
DELETE FROM visibilitymodifier WHERE set_id='xgyl8vjbwh027qnrcjjtmkc6m4j00ni4';
DELETE FROM metadata_display_label WHERE oid = '7c23b6f5-46d1-3c84-b0a5-589e8a560287';
DELETE FROM metadata_display_label WHERE oid = '4ab36de6-9574-35a3-8a2d-993fcd220287';
DELETE FROM md_attribute_reference WHERE oid = 'f05d2cf5-24b7-3aaf-800d-b752e7e90218';
DELETE FROM md_attribute_ref WHERE oid = 'f05d2cf5-24b7-3aaf-800d-b752e7e90218';
DELETE FROM md_attribute_concrete WHERE oid = 'f05d2cf5-24b7-3aaf-800d-b752e7e90218';
DELETE FROM md_attribute WHERE oid = 'f05d2cf5-24b7-3aaf-800d-b752e7e90218';
DELETE FROM metadata WHERE oid = 'f05d2cf5-24b7-3aaf-800d-b752e7e90218' AND seq = 3399;
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('74bng61co5bn5lf6qcp13uzntrc2axlp', '44284f74-6d1b-3921-87b1-32d866a30085');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('0ti39ngpu43p1p9pzwamigu3d7gc8uer', '78a04c02-65e4-3aef-b74c-318f28360113');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8pude63lhxzpp7qo28vzdw37193zq5qz', '78a04c02-65e4-3aef-b74c-318f28360113');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('ee397f65-30a8-3014-a81d-b656526f0287', 'ee397f65-30a8-3014-a81d-b656526f0287', 'www.runwaysdk.com', 'Term Class');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('b7f29291-a4b9-3678-bb1b-9e362b100287', 'b7f29291-a4b9-3678-bb1b-9e362b100287', 'www.runwaysdk.com', NULL);
INSERT INTO md_attribute_character ( default_value, database_size, oid) VALUES (NULL, 6000, 'eb81abe5-4182-3904-b631-23d7a4e80067');
INSERT INTO md_attribute_char ( oid) VALUES ('eb81abe5-4182-3904-b631-23d7a4e80067');
INSERT INTO md_attribute_primitive ( is_expression, expression, oid) VALUES (0, NULL, 'eb81abe5-4182-3904-b631-23d7a4e80067');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('eab24d74-f63d-3156-b2da-7e2aa3fb0058', 'term_class', '74bng61co5bn5lf6qcp13uzntrc2axlp', '44284f74-6d1b-3921-87b1-32d866a30085', 'termClass', '0ti39ngpu43p1p9pzwamigu3d7gc8uer', '78a04c02-65e4-3aef-b74c-318f28360113', 0, 'aqcdci60rgdjvhmrds79lu8hsdsaox', '8pude63lhxzpp7qo28vzdw37193zq5qz', '78a04c02-65e4-3aef-b74c-318f28360113', 1, 1, 'ee397f65-30a8-3014-a81d-b656526f0287', 0, 'eb81abe5-4182-3904-b631-23d7a4e80067');
INSERT INTO md_attribute ( oid) VALUES ('eb81abe5-4182-3904-b631-23d7a4e80067');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2017-12-04 08:35:12', 4923, 'com.runwaysdk.system.metadata.MdAttributeCharacter', 'a18798db-4bc4-3584-ad5e-844b7b760060', '2017-12-04 08:35:12', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'b7f29291-a4b9-3678-bb1b-9e362b100287', NULL, 1, 'a18798db-4bc4-3584-ad5e-844b7b760060', NULL, 'eb81abe5-4182-3904-b631-23d7a4e80067', 'com.runwaysdk.system.metadata.ontology.OntologyStrategy.termClass', 'www.runwaysdk.com');
ALTER TABLE ontology_strategy ADD COLUMN term_class  varchar(6000);
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('7c1356cf-168e-31f3-944e-5fe7111c0245', 'eab24d74-f63d-3156-b2da-7e2aa3fb0058', 'eb81abe5-4182-3904-b631-23d7a4e80067');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('7c1356cf-168e-31f3-944e-5fe7111c0245', 'eab24d74-f63d-3156-b2da-7e2aa3fb0058', 'eb81abe5-4182-3904-b631-23d7a4e80067');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('7c1356cf-168e-31f3-944e-5fe7111c0245', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2017-12-04 08:35:12', 'www.runwaysdk.com', 'a18798db-4bc4-3584-ad5e-844b7b760060', 4924, 'com.runwaysdk.system.metadata.ontology.OntologyStrategy.termClass', NULL, NULL, '2017-12-04 08:35:12', 'a18798db-4bc4-3584-ad5e-844b7b760060', 'eab24d74-f63d-3156-b2da-7e2aa3fb0058', 'eb81abe5-4182-3904-b631-23d7a4e80067');
CREATE UNIQUE INDEX aqcdci60rgdjvhmrds79lu8hsdsaox ON ontology_strategy (term_class);
UPDATE metadata SET seq= 4925  WHERE oid='eab24d74-f63d-3156-b2da-7e2aa3fb0058';
UPDATE metadata SET seq= 4926  WHERE oid='02c46e40-e497-33e0-8cbc-29637c130058';
ALTER TABLE md_term DROP strategy;
