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

DROP INDEX ac4fac4tskng9hvkhbpbsx0yi61kqo;
DELETE FROM class_attribute_concrete WHERE oid = '1s8gzdsmp80mt72y3llxbe5psgswpexg0245';
DELETE FROM class_attribute WHERE oid = '1s8gzdsmp80mt72y3llxbe5psgswpexg0245';
DELETE FROM metadata_relationship WHERE oid = '1s8gzdsmp80mt72y3llxbe5psgswpexg0245' AND seq = 2665;
DELETE FROM md_attribute_indicies WHERE set_id='034f0hok2g7fek7v7doso5x4iw7hugxi';
DELETE FROM visibilitymodifier WHERE set_id='21n3rt68z4jhpmi86yeblp1xg79aglfy';
DELETE FROM visibilitymodifier WHERE set_id='xgyl8vjbwh027qnrcjjtmkc6m4j00ni4';
DELETE FROM metadata_display_label WHERE oid = '8v7qx528t3g0ew8zncw46d9ucmy2ge2o0287';
DELETE FROM metadata_display_label WHERE oid = '0abx24hm1wawkt83i14tua2akvb00f3i0287';
DELETE FROM md_attribute_reference WHERE oid = 'srphg7b95yay81sc6fsfhfj4a6p58vva0218';
DELETE FROM md_attribute_ref WHERE oid = 'srphg7b95yay81sc6fsfhfj4a6p58vva0218';
DELETE FROM md_attribute_concrete WHERE oid = 'srphg7b95yay81sc6fsfhfj4a6p58vva0218';
DELETE FROM md_attribute WHERE oid = 'srphg7b95yay81sc6fsfhfj4a6p58vva0218';
DELETE FROM metadata WHERE oid = 'srphg7b95yay81sc6fsfhfj4a6p58vva0218' AND seq = 3399;
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('74bng61co5bn5lf6qcp13uzntrc2axlp', '000000000000000000000000000004090085');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('0ti39ngpu43p1p9pzwamigu3d7gc8uer', '20071109NM00000000000000000000050113');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('8pude63lhxzpp7qo28vzdw37193zq5qz', '20071109NM00000000000000000000050113');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('z0d5w1jma9ttgiltfy7k8qv6pqls090j0287', 'z0d5w1jma9ttgiltfy7k8qv6pqls090j0287', 'www.runwaysdk.com', 'Term Class');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('73s709iwpakl3ju4j1ewqhk939wn67ih0287', '73s709iwpakl3ju4j1ewqhk939wn67ih0287', 'www.runwaysdk.com', NULL);
INSERT INTO md_attribute_character ( default_value, database_size, oid) VALUES (NULL, 6000, 'idym64vujmkh3um3ln8cbr2nhrb69tim0067');
INSERT INTO md_attribute_char ( oid) VALUES ('idym64vujmkh3um3ln8cbr2nhrb69tim0067');
INSERT INTO md_attribute_primitive ( is_expression, expression, oid) VALUES (0, NULL, 'idym64vujmkh3um3ln8cbr2nhrb69tim0067');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('qppm4x1l9zvlnfxki1g3lkgc19jqezp40058', 'term_class', '74bng61co5bn5lf6qcp13uzntrc2axlp', '000000000000000000000000000004090085', 'termClass', '0ti39ngpu43p1p9pzwamigu3d7gc8uer', '20071109NM00000000000000000000050113', 0, 'aqcdci60rgdjvhmrds79lu8hsdsaox', '8pude63lhxzpp7qo28vzdw37193zq5qz', '20071109NM00000000000000000000050113', 1, 1, 'z0d5w1jma9ttgiltfy7k8qv6pqls090j0287', 0, 'idym64vujmkh3um3ln8cbr2nhrb69tim0067');
INSERT INTO md_attribute ( oid) VALUES ('idym64vujmkh3um3ln8cbr2nhrb69tim0067');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2017-12-04 08:35:12', 4923, 'com.runwaysdk.system.metadata.MdAttributeCharacter', '000000000000000000000000000000100060', '2017-12-04 08:35:12', '000000000000000000000000000000100060', '73s709iwpakl3ju4j1ewqhk939wn67ih0287', NULL, 1, '000000000000000000000000000000100060', NULL, 'idym64vujmkh3um3ln8cbr2nhrb69tim0067', 'com.runwaysdk.system.metadata.ontology.OntologyStrategy.termClass', 'www.runwaysdk.com');
ALTER TABLE ontology_strategy ADD COLUMN term_class  varchar(6000);
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('idym64vujmkh3um3ln8cbr2nhrb69tim0245', 'qppm4x1l9zvlnfxki1g3lkgc19jqezp40058', 'idym64vujmkh3um3ln8cbr2nhrb69tim0067');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('idym64vujmkh3um3ln8cbr2nhrb69tim0245', 'qppm4x1l9zvlnfxki1g3lkgc19jqezp40058', 'idym64vujmkh3um3ln8cbr2nhrb69tim0067');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('idym64vujmkh3um3ln8cbr2nhrb69tim0245', '000000000000000000000000000000100060', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2017-12-04 08:35:12', 'www.runwaysdk.com', '000000000000000000000000000000100060', 4924, 'com.runwaysdk.system.metadata.ontology.OntologyStrategy.termClass', NULL, NULL, '2017-12-04 08:35:12', '000000000000000000000000000000100060', 'qppm4x1l9zvlnfxki1g3lkgc19jqezp40058', 'idym64vujmkh3um3ln8cbr2nhrb69tim0067');
CREATE UNIQUE INDEX aqcdci60rgdjvhmrds79lu8hsdsaox ON ontology_strategy (term_class);
UPDATE metadata SET seq= 4925  WHERE oid='qppm4x1l9zvlnfxki1g3lkgc19jqezp40058';
UPDATE metadata SET seq= 4926  WHERE oid='xl2eqz5dv2cq2hoogeb43gg0ohakvvsp0058';
ALTER TABLE md_term DROP strategy;
