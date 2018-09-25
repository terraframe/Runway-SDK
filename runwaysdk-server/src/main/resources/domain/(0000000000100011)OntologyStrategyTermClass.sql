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
DELETE FROM class_attribute_concrete WHERE oid = '5b68d41a-f6a5-3b5a-973e-c316800000f5';
DELETE FROM class_attribute WHERE oid = '5b68d41a-f6a5-3b5a-973e-c316800000f5';
DELETE FROM metadata_relationship WHERE oid = '5b68d41a-f6a5-3b5a-973e-c316800000f5' AND seq = 2665;
DELETE FROM md_attribute_indicies WHERE set_id='034f0hok2g7fek7v7doso5x4iw7hugxi';
DELETE FROM visibilitymodifier WHERE set_id='21n3rt68z4jhpmi86yeblp1xg79aglfy';
DELETE FROM visibilitymodifier WHERE set_id='xgyl8vjbwh027qnrcjjtmkc6m4j00ni4';
DELETE FROM metadata_display_label WHERE oid = '7ba3187e-2357-3470-b749-950b5700011f';
DELETE FROM metadata_display_label WHERE oid = 'ad70b485-9371-3db7-8f23-f1f5fe00011f';
DELETE FROM md_attribute_reference WHERE oid = '0366a90a-4522-3e1b-b9e4-8d34500000da';
DELETE FROM md_attribute_ref WHERE oid = '0366a90a-4522-3e1b-b9e4-8d34500000da';
DELETE FROM md_attribute_concrete WHERE oid = '0366a90a-4522-3e1b-b9e4-8d34500000da';
DELETE FROM md_attribute WHERE oid = '0366a90a-4522-3e1b-b9e4-8d34500000da';
DELETE FROM metadata WHERE oid = '0366a90a-4522-3e1b-b9e4-8d34500000da' AND seq = 3399;
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('74bng61co5bn5lf6qcp13uzntrc2axlp', '72b5580c-2a6f-3250-9435-1be1f2000055');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('0ti39ngpu43p1p9pzwamigu3d7gc8uer', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8pude63lhxzpp7qo28vzdw37193zq5qz', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('ff74ca6f-eaba-3153-9422-60632300011f', 'ff74ca6f-eaba-3153-9422-60632300011f', 'www.runwaysdk.com', 'Term Class');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('b0b3f2e7-2838-3380-8987-d90bec00011f', 'b0b3f2e7-2838-3380-8987-d90bec00011f', 'www.runwaysdk.com', NULL);
INSERT INTO md_attribute_character ( default_value, database_size, oid) VALUES (NULL, 6000, 'e7f20e5f-6f96-3604-83cf-28dc57000043');
INSERT INTO md_attribute_char ( oid) VALUES ('e7f20e5f-6f96-3604-83cf-28dc57000043');
INSERT INTO md_attribute_primitive ( is_expression, expression, oid) VALUES (0, NULL, 'e7f20e5f-6f96-3604-83cf-28dc57000043');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('026e64f9-ae8c-3ba1-82c2-d499e900003a', 'term_class', '74bng61co5bn5lf6qcp13uzntrc2axlp', '72b5580c-2a6f-3250-9435-1be1f2000055', 'termClass', '0ti39ngpu43p1p9pzwamigu3d7gc8uer', '02d46938-021e-3bb4-bb8f-4a31c5000071', 0, 'aqcdci60rgdjvhmrds79lu8hsdsaox', '8pude63lhxzpp7qo28vzdw37193zq5qz', '02d46938-021e-3bb4-bb8f-4a31c5000071', 1, 1, 'ff74ca6f-eaba-3153-9422-60632300011f', 0, 'e7f20e5f-6f96-3604-83cf-28dc57000043');
INSERT INTO md_attribute ( oid) VALUES ('e7f20e5f-6f96-3604-83cf-28dc57000043');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2017-12-04 08:35:12', 4923, 'com.runwaysdk.system.metadata.MdAttributeCharacter', '4f664e38-0546-31c7-a379-5492ef00003c', '2017-12-04 08:35:12', '4f664e38-0546-31c7-a379-5492ef00003c', 'b0b3f2e7-2838-3380-8987-d90bec00011f', NULL, 1, '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'e7f20e5f-6f96-3604-83cf-28dc57000043', 'com.runwaysdk.system.metadata.ontology.OntologyStrategy.termClass', 'www.runwaysdk.com');
ALTER TABLE ontology_strategy ADD COLUMN term_class  varchar(6000);
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('a11b4c28-904c-353f-89e6-049e4d0000f5', '026e64f9-ae8c-3ba1-82c2-d499e900003a', 'e7f20e5f-6f96-3604-83cf-28dc57000043');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('a11b4c28-904c-353f-89e6-049e4d0000f5', '026e64f9-ae8c-3ba1-82c2-d499e900003a', 'e7f20e5f-6f96-3604-83cf-28dc57000043');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('a11b4c28-904c-353f-89e6-049e4d0000f5', '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2017-12-04 08:35:12', 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', 4924, 'com.runwaysdk.system.metadata.ontology.OntologyStrategy.termClass', NULL, NULL, '2017-12-04 08:35:12', '4f664e38-0546-31c7-a379-5492ef00003c', '026e64f9-ae8c-3ba1-82c2-d499e900003a', 'e7f20e5f-6f96-3604-83cf-28dc57000043');
CREATE UNIQUE INDEX aqcdci60rgdjvhmrds79lu8hsdsaox ON ontology_strategy (term_class);
UPDATE metadata SET seq= 4925  WHERE oid='026e64f9-ae8c-3ba1-82c2-d499e900003a';
UPDATE metadata SET seq= 4926  WHERE oid='85a39b1f-871a-3b8e-9d8b-3a741600003a';
ALTER TABLE md_term DROP strategy;
