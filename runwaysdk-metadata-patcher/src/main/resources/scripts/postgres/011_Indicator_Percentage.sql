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

INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('shnf2uzdw0wxg7ujbciaongcbqyokc8k0287', 'shnf2uzdw0wxg7ujbciaongcbqyokc8k0287', 'www.runwaysdk.com', 'true');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('6w17mn4wd0rpluf2znlisx0gm5ghialp0287', '6w17mn4wd0rpluf2znlisx0gm5ghialp0287', 'www.runwaysdk.com', 'false');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('qr0auotj70cviabpx9098azqbm1kdp7n', '000000000000000000000000000004010085');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('rc8s0yeh2ss05seb48pcyyd7yrg4peei', '20071109NM00000000000000000000050113');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('svw0mp5w3y6nbv1ryeld9q7e2ybb9z2w', '20071109NM00000000000000000000050113');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('2fwydq9h5c5usdfylcg6dumt3yczdnh40287', '2fwydq9h5c5usdfylcg6dumt3yczdnh40287', 'www.runwaysdk.com', 'Percentage');
INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('t2btc03mllcbqajzdxo5ohimnniw62dk0287', 't2btc03mllcbqajzdxo5ohimnniw62dk0287', 'www.runwaysdk.com', NULL);
INSERT INTO md_attribute_boolean ( positive_display_label, negative_display_label, default_value, oid) VALUES ('shnf2uzdw0wxg7ujbciaongcbqyokc8k0287', '6w17mn4wd0rpluf2znlisx0gm5ghialp0287', 0, 'ih1vcq4qostv9woneqpnvqrsfcm0bhno0074');
INSERT INTO md_attribute_primitive ( is_expression, expression, oid) VALUES (0, NULL, 'ih1vcq4qostv9woneqpnvqrsfcm0bhno0074');
INSERT INTO md_attribute_concrete ( defining_md_class, column_name, index_type, index_type_c, attribute_name, getter_visibility, getter_visibility_c, system, index_name, setter_visibility, setter_visibility_c, required, generate_accessor, display_label, immutable, oid) VALUES ('ijx2qpvkmeubmt67o8t8ac0a5nd4x8pc0058', 'percentage', 'qr0auotj70cviabpx9098azqbm1kdp7n', '000000000000000000000000000004010085', 'percentage', 'rc8s0yeh2ss05seb48pcyyd7yrg4peei', '20071109NM00000000000000000000050113', 0, 'am2jn1ahfpp3oqwqjvfmitop79vqmo', 'svw0mp5w3y6nbv1ryeld9q7e2ybb9z2w', '20071109NM00000000000000000000050113', 0, 1, '2fwydq9h5c5usdfylcg6dumt3yczdnh40287', 0, 'ih1vcq4qostv9woneqpnvqrsfcm0bhno0074');
INSERT INTO md_attribute ( oid) VALUES ('ih1vcq4qostv9woneqpnvqrsfcm0bhno0074');
INSERT INTO metadata ( create_date, seq, type, created_by, last_update_date, last_updated_by, description, locked_by, remove, owner, entity_domain, oid, key_name, site_master) VALUES ('2017-06-30 13:06:53', 4921, 'com.runwaysdk.system.metadata.MdAttributeBoolean', '000000000000000000000000000000100060', '2017-06-30 13:06:53', '000000000000000000000000000000100060', 't2btc03mllcbqajzdxo5ohimnniw62dk0287', NULL, 1, '000000000000000000000000000000100060', NULL, 'ih1vcq4qostv9woneqpnvqrsfcm0bhno0074', 'com.runwaysdk.system.metadata.IndicatorComposite.percentage', 'www.runwaysdk.com');
ALTER TABLE indicator_composite ADD COLUMN percentage  int;
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('ih1vcq4qostv9woneqpnvqrsfcm0bhno0245', 'ijx2qpvkmeubmt67o8t8ac0a5nd4x8pc0058', 'ih1vcq4qostv9woneqpnvqrsfcm0bhno0074');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('ih1vcq4qostv9woneqpnvqrsfcm0bhno0245', 'ijx2qpvkmeubmt67o8t8ac0a5nd4x8pc0058', 'ih1vcq4qostv9woneqpnvqrsfcm0bhno0074');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('ih1vcq4qostv9woneqpnvqrsfcm0bhno0245', '000000000000000000000000000000100060', 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '2017-06-30 13:06:53', 'www.runwaysdk.com', '000000000000000000000000000000100060', 4922, 'com.runwaysdk.system.metadata.IndicatorComposite.percentage', NULL, NULL, '2017-06-30 13:06:53', '000000000000000000000000000000100060', 'ijx2qpvkmeubmt67o8t8ac0a5nd4x8pc0058', 'ih1vcq4qostv9woneqpnvqrsfcm0bhno0074');
UPDATE metadata SET seq= 4923  WHERE oid='ijx2qpvkmeubmt67o8t8ac0a5nd4x8pc0058';
