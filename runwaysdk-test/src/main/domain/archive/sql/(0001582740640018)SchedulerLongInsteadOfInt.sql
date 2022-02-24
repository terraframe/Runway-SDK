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

INSERT INTO dynamic_properties ( version_number, oid) VALUES ('0001582740640018', '000000000000000000000');
DELETE FROM class_attribute_virtual WHERE oid = '057c32f5-4379-3c27-86b0-609b6e0000f2';
DELETE FROM class_attribute WHERE oid = '057c32f5-4379-3c27-86b0-609b6e0000f2';
DELETE FROM metadata_relationship WHERE oid = '057c32f5-4379-3c27-86b0-609b6e0000f2' AND seq = 2849;
DELETE FROM virtualize_attribute WHERE oid = '1a3a782f-23b8-3feb-ba0e-4b821100010d';
DELETE FROM metadata_relationship WHERE oid = '1a3a782f-23b8-3feb-ba0e-4b821100010d' AND seq = 2850;
DELETE FROM metadata_display_label WHERE oid = 'e15c4753-1a06-3cfb-af61-a1e84c00011f';
DELETE FROM metadata_display_label WHERE oid = '4964e4ba-cc74-3884-bbde-8612d800011f';
DELETE FROM md_attribute_virtual WHERE oid = 'f7bc0518-2ef0-3708-9cdb-9a5092000084';
DELETE FROM md_attribute WHERE oid = 'f7bc0518-2ef0-3708-9cdb-9a5092000084';
DELETE FROM metadata WHERE oid = 'f7bc0518-2ef0-3708-9cdb-9a5092000084' AND seq = 4208;
DELETE FROM class_attribute_virtual WHERE oid = '13cbd9fb-94d9-3858-908a-fd4ba60000f2';
DELETE FROM class_attribute WHERE oid = '13cbd9fb-94d9-3858-908a-fd4ba60000f2';
DELETE FROM metadata_relationship WHERE oid = '13cbd9fb-94d9-3858-908a-fd4ba60000f2' AND seq = 5687;
DELETE FROM virtualize_attribute WHERE oid = 'a31b5077-c4f3-32ba-9775-49320a00010d';
DELETE FROM metadata_relationship WHERE oid = 'a31b5077-c4f3-32ba-9775-49320a00010d' AND seq = 5688;
DELETE FROM metadata_display_label WHERE oid = '85bada07-8779-4eba-ba41-68edff00011f';
DELETE FROM metadata_display_label WHERE oid = '8e468225-8e1e-4feb-999d-95f56f00011f';
DELETE FROM md_attribute_virtual WHERE oid = '52403c24-7e7f-39ab-a7ee-91944d000084';
DELETE FROM md_attribute WHERE oid = '52403c24-7e7f-39ab-a7ee-91944d000084';
DELETE FROM metadata WHERE oid = '52403c24-7e7f-39ab-a7ee-91944d000084' AND seq = 5686;
DELETE FROM class_attribute_concrete WHERE oid = '21f78e4d-834c-3710-aba5-cbbd520000f5';
DELETE FROM class_attribute WHERE oid = '21f78e4d-834c-3710-aba5-cbbd520000f5';
DELETE FROM metadata_relationship WHERE oid = '21f78e4d-834c-3710-aba5-cbbd520000f5' AND seq = 2839;
DELETE FROM metadata_display_label WHERE oid = '053b8df0-96ce-43cf-b65d-00916cb70287';
DELETE FROM md_attribute_indicies WHERE set_id='slwj8vikj4akebjie8jd8ywtzq7gh780';
DELETE FROM visibilitymodifier WHERE set_id='shut8z1nnjd6lk87ikdwbihk1qaezbza';
DELETE FROM visibilitymodifier WHERE set_id='qvdf7iigsy8w9brxm6wff50c19b4zcvp';
DELETE FROM metadata_display_label WHERE oid = 'cf525406-6e19-428a-a80e-00f4bbf40287';
DELETE FROM md_attribute_integer WHERE oid = 'a96c4c92-ba32-3013-84de-44a4d600004f';
DELETE FROM md_attribute_int WHERE oid = 'a96c4c92-ba32-3013-84de-44a4d600004f';
DELETE FROM md_attribute_number WHERE oid = 'a96c4c92-ba32-3013-84de-44a4d600004f';
DELETE FROM md_attribute_primitive WHERE oid = 'a96c4c92-ba32-3013-84de-44a4d600004f';
DELETE FROM md_attribute_concrete WHERE oid = 'a96c4c92-ba32-3013-84de-44a4d600004f';
DELETE FROM md_attribute WHERE oid = 'a96c4c92-ba32-3013-84de-44a4d600004f';
DELETE FROM metadata WHERE oid = 'a96c4c92-ba32-3013-84de-44a4d600004f' AND seq = 3449;
DELETE FROM class_attribute_concrete WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac0000f5';
DELETE FROM class_attribute WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac0000f5';
DELETE FROM metadata_relationship WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac0000f5' AND seq = 5682;
DELETE FROM metadata_display_label WHERE oid = '2b11bee7-39ca-4b3c-9a4d-1aa0b900011f';
DELETE FROM md_attribute_indicies WHERE set_id='5fcf95d5-35dc-43be-83e5-333fe1  ';
DELETE FROM visibilitymodifier WHERE set_id='7389988d-eaaa-402e-82d2-3f72f3  ';
DELETE FROM visibilitymodifier WHERE set_id='be4bb16c-bc84-4e35-beaa-fdf093  ';
DELETE FROM metadata_display_label WHERE oid = '496cce6c-ced5-49de-9f42-3b5ed000011f';
DELETE FROM md_attribute_integer WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac00004f';
DELETE FROM md_attribute_int WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac00004f';
DELETE FROM md_attribute_number WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac00004f';
DELETE FROM md_attribute_primitive WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac00004f';
DELETE FROM md_attribute_concrete WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac00004f';
DELETE FROM md_attribute WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac00004f';
DELETE FROM metadata WHERE oid = '33b1b276-0e24-3a81-8823-bc1aac00004f' AND seq = 5681;
UPDATE metadata SET seq= 5759 , last_update_date= '2020-02-26 16:28:05'  WHERE oid='c73b3365-8784-3c09-834f-7afca400003a';
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('3a08c65f-65bf-4e8d-9d67-10f78200011f', 'Work Progress', '3a08c65f-65bf-4e8d-9d67-10f78200011f', 'www.runwaysdk.com');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('07ab20c6-b6d5-45c9-b443-db2200', '9a7f73ee-81a9-32e9-884e-c4be61000055');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('c3ebbe2e-13ec-4bba-8b28-cfe7d4', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('b8c3c5c4-e9d4-477f-95f4-72b868', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('19784a2b-0ec7-4342-aa7e-68311400011f', NULL, '19784a2b-0ec7-4342-aa7e-68311400011f', 'www.runwaysdk.com');
INSERT INTO md_attribute_long ( default_value, start_range, end_range, oid) VALUES (0, NULL, NULL, 'd0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO md_attribute_int ( oid) VALUES ('d0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO md_attribute_number ( reject_negative, reject_zero, reject_positive, oid) VALUES (0, 0, 0, 'd0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO md_attribute_primitive ( is_expression, expression, oid) VALUES (0, NULL, 'd0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO md_attribute_concrete ( display_label, index_name, attribute_name, immutable, system, required, index_type, index_type_c, column_name, generate_accessor, getter_visibility, getter_visibility_c, defining_md_class, setter_visibility, setter_visibility_c, oid) VALUES ('3a08c65f-65bf-4e8d-9d67-10f78200011f', 'a12596d7e6a6b3b8990aeeee9b3', 'workProgress', 0, 0, 0, '07ab20c6-b6d5-45c9-b443-db2200', '9a7f73ee-81a9-32e9-884e-c4be61000055', 'work_progress', 1, 'c3ebbe2e-13ec-4bba-8b28-cfe7d4', '02d46938-021e-3bb4-bb8f-4a31c5000071', 'c73b3365-8784-3c09-834f-7afca400003a', 'b8c3c5c4-e9d4-477f-95f4-72b868', '02d46938-021e-3bb4-bb8f-4a31c5000071', 'd0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO md_attribute ( oid) VALUES ('d0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO metadata ( seq, owner, created_by, entity_domain, type, locked_by, site_master, last_updated_by, create_date, oid, key_name, remove, last_update_date, description) VALUES (5760, '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'com.runwaysdk.system.metadata.MdAttributeLong', NULL, 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', 'd0fb84b6-f5ee-31a1-926d-de918c000050', 'com.runwaysdk.system.scheduler.JobHistory.workProgress', 1, '2020-02-26 16:28:05', '19784a2b-0ec7-4342-aa7e-68311400011f');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('d0fb84b6-f5ee-31a1-926d-de918c0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', 'd0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('d0fb84b6-f5ee-31a1-926d-de918c0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', 'd0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.JobHistory.workProgress', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', 5761, '2020-02-26 16:28:05', NULL, 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '4f664e38-0546-31c7-a379-5492ef00003c', 'd0fb84b6-f5ee-31a1-926d-de918c0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', 'd0fb84b6-f5ee-31a1-926d-de918c000050');
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('60ab0a32-9917-471a-b2a1-d05da100011f', 'Work Progress', '60ab0a32-9917-471a-b2a1-d05da100011f', 'www.runwaysdk.com');
INSERT INTO md_attribute_indicies (set_id, item_id)  VALUES  ('7c02ed97-71f8-438e-9112-0becd2', '9a7f73ee-81a9-32e9-884e-c4be61000055');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('eb56fc4c-0dc2-4e85-97ce-0a661c', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO visibilitymodifier (set_id, item_id)  VALUES  ('1555bd8b-670b-4e3d-8b54-c95de8', '02d46938-021e-3bb4-bb8f-4a31c5000071');
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('7d040553-d872-4898-a69f-897bdc00011f', NULL, '7d040553-d872-4898-a69f-897bdc00011f', 'www.runwaysdk.com');
INSERT INTO md_attribute_long ( default_value, start_range, end_range, oid) VALUES (0, NULL, NULL, '33b1b276-0e24-3a81-8823-bc1aac000050');
INSERT INTO md_attribute_int ( oid) VALUES ('33b1b276-0e24-3a81-8823-bc1aac000050');
INSERT INTO md_attribute_number ( reject_negative, reject_zero, reject_positive, oid) VALUES (0, 0, 0, '33b1b276-0e24-3a81-8823-bc1aac000050');
INSERT INTO md_attribute_primitive ( is_expression, expression, oid) VALUES (0, NULL, '33b1b276-0e24-3a81-8823-bc1aac000050');
INSERT INTO md_attribute_concrete ( display_label, index_name, attribute_name, immutable, system, required, index_type, index_type_c, column_name, generate_accessor, getter_visibility, getter_visibility_c, defining_md_class, setter_visibility, setter_visibility_c, oid) VALUES ('60ab0a32-9917-471a-b2a1-d05da100011f', 'a37764b7d8fd13762add01e97d6', 'workTotal', 0, 0, 0, '7c02ed97-71f8-438e-9112-0becd2', '9a7f73ee-81a9-32e9-884e-c4be61000055', 'work_total', 1, 'eb56fc4c-0dc2-4e85-97ce-0a661c', '02d46938-021e-3bb4-bb8f-4a31c5000071', 'c73b3365-8784-3c09-834f-7afca400003a', '1555bd8b-670b-4e3d-8b54-c95de8', '02d46938-021e-3bb4-bb8f-4a31c5000071', '33b1b276-0e24-3a81-8823-bc1aac000050');
INSERT INTO md_attribute ( oid) VALUES ('33b1b276-0e24-3a81-8823-bc1aac000050');
INSERT INTO metadata ( seq, owner, created_by, entity_domain, type, locked_by, site_master, last_updated_by, create_date, oid, key_name, remove, last_update_date, description) VALUES (5762, '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'com.runwaysdk.system.metadata.MdAttributeLong', NULL, 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', '33b1b276-0e24-3a81-8823-bc1aac000050', 'com.runwaysdk.system.scheduler.JobHistory.workTotal', 1, '2020-02-26 16:28:05', '7d040553-d872-4898-a69f-897bdc00011f');
INSERT INTO class_attribute_concrete ( oid, parent_oid, child_oid) VALUES ('33b1b276-0e24-3a81-8823-bc1aac0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', '33b1b276-0e24-3a81-8823-bc1aac000050');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('33b1b276-0e24-3a81-8823-bc1aac0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', '33b1b276-0e24-3a81-8823-bc1aac000050');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.JobHistory.workTotal', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', 5763, '2020-02-26 16:28:05', NULL, 'com.runwaysdk.system.metadata.ClassAttributeConcrete', '4f664e38-0546-31c7-a379-5492ef00003c', '33b1b276-0e24-3a81-8823-bc1aac0000f5', 'c73b3365-8784-3c09-834f-7afca400003a', '33b1b276-0e24-3a81-8823-bc1aac000050');
ALTER TABLE job_history ADD COLUMN a352d57359d074e3eb855057c9185b  bigint, ADD COLUMN ae6c1c19c131d47468efbc3103c0d6  bigint;
UPDATE metadata SET seq= 5764 , last_update_date= '2020-02-26 16:28:05'  WHERE oid='a9933b98-f2e6-3ed8-aa20-7c7584000074';
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('3e129db3-8975-4d5d-a3b2-686ba700011f', NULL, '3e129db3-8975-4d5d-a3b2-686ba700011f', 'www.runwaysdk.com');
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('3811c6b7-0e57-43a0-b967-4acce900011f', NULL, '3811c6b7-0e57-43a0-b967-4acce900011f', 'www.runwaysdk.com');
INSERT INTO md_attribute_virtual ( display_label, md_attribute_concrete, required, attribute_name, defining_md_view, oid) VALUES ('3e129db3-8975-4d5d-a3b2-686ba700011f', 'd0fb84b6-f5ee-31a1-926d-de918c000050', NULL, 'workProgress', 'a9933b98-f2e6-3ed8-aa20-7c7584000074', '1352a58d-289b-3d35-9dbf-f542e9000084');
INSERT INTO md_attribute ( oid) VALUES ('1352a58d-289b-3d35-9dbf-f542e9000084');
INSERT INTO metadata ( seq, owner, created_by, entity_domain, type, locked_by, site_master, last_updated_by, create_date, oid, key_name, remove, last_update_date, description) VALUES (5765, '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'com.runwaysdk.system.metadata.MdAttributeVirtual', NULL, 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', '1352a58d-289b-3d35-9dbf-f542e9000084', 'com.runwaysdk.system.scheduler.JobHistoryView.workProgress', 1, '2020-02-26 16:28:05', '3811c6b7-0e57-43a0-b967-4acce900011f');
INSERT INTO class_attribute_virtual ( oid, parent_oid, child_oid) VALUES ('527787a4-21ae-3392-b0b4-c3ec010000f2', 'a9933b98-f2e6-3ed8-aa20-7c7584000074', '1352a58d-289b-3d35-9dbf-f542e9000084');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('527787a4-21ae-3392-b0b4-c3ec010000f2', 'a9933b98-f2e6-3ed8-aa20-7c7584000074', '1352a58d-289b-3d35-9dbf-f542e9000084');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.JobHistoryView.workProgress.class_attribute_virtual', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', 5766, '2020-02-26 16:28:05', NULL, 'com.runwaysdk.system.metadata.ClassAttributeVirtual', '4f664e38-0546-31c7-a379-5492ef00003c', '527787a4-21ae-3392-b0b4-c3ec010000f2', 'a9933b98-f2e6-3ed8-aa20-7c7584000074', '1352a58d-289b-3d35-9dbf-f542e9000084');
INSERT INTO virtualize_attribute ( oid, parent_oid, child_oid) VALUES ('ba403152-2062-323f-80b8-1da42800010d', 'd0fb84b6-f5ee-31a1-926d-de918c000050', '1352a58d-289b-3d35-9dbf-f542e9000084');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.JobHistoryView.workProgress.virtualize_attribute', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', 5767, '2020-02-26 16:28:05', NULL, 'com.runwaysdk.system.metadata.VirtualizeAttribute', '4f664e38-0546-31c7-a379-5492ef00003c', 'ba403152-2062-323f-80b8-1da42800010d', 'd0fb84b6-f5ee-31a1-926d-de918c000050', '1352a58d-289b-3d35-9dbf-f542e9000084');
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('cc9ce881-5126-4682-b3a9-65ab4000011f', NULL, 'cc9ce881-5126-4682-b3a9-65ab4000011f', 'www.runwaysdk.com');
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('a3623af4-6003-40d9-a0b2-39243100011f', NULL, 'a3623af4-6003-40d9-a0b2-39243100011f', 'www.runwaysdk.com');
INSERT INTO md_attribute_virtual ( display_label, md_attribute_concrete, required, attribute_name, defining_md_view, oid) VALUES ('cc9ce881-5126-4682-b3a9-65ab4000011f', '33b1b276-0e24-3a81-8823-bc1aac000050', NULL, 'workTotal', 'a9933b98-f2e6-3ed8-aa20-7c7584000074', '52403c24-7e7f-39ab-a7ee-91944d000084');
INSERT INTO md_attribute ( oid) VALUES ('52403c24-7e7f-39ab-a7ee-91944d000084');
INSERT INTO metadata ( seq, owner, created_by, entity_domain, type, locked_by, site_master, last_updated_by, create_date, oid, key_name, remove, last_update_date, description) VALUES (5768, '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'com.runwaysdk.system.metadata.MdAttributeVirtual', NULL, 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', '52403c24-7e7f-39ab-a7ee-91944d000084', 'com.runwaysdk.system.scheduler.JobHistoryView.workTotal', 1, '2020-02-26 16:28:05', 'a3623af4-6003-40d9-a0b2-39243100011f');
INSERT INTO class_attribute_virtual ( oid, parent_oid, child_oid) VALUES ('13cbd9fb-94d9-3858-908a-fd4ba60000f2', 'a9933b98-f2e6-3ed8-aa20-7c7584000074', '52403c24-7e7f-39ab-a7ee-91944d000084');
INSERT INTO class_attribute ( oid, parent_oid, child_oid) VALUES ('13cbd9fb-94d9-3858-908a-fd4ba60000f2', 'a9933b98-f2e6-3ed8-aa20-7c7584000074', '52403c24-7e7f-39ab-a7ee-91944d000084');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.JobHistoryView.workTotal.class_attribute_virtual', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', 5769, '2020-02-26 16:28:05', NULL, 'com.runwaysdk.system.metadata.ClassAttributeVirtual', '4f664e38-0546-31c7-a379-5492ef00003c', '13cbd9fb-94d9-3858-908a-fd4ba60000f2', 'a9933b98-f2e6-3ed8-aa20-7c7584000074', '52403c24-7e7f-39ab-a7ee-91944d000084');
INSERT INTO virtualize_attribute ( oid, parent_oid, child_oid) VALUES ('a31b5077-c4f3-32ba-9775-49320a00010d', '33b1b276-0e24-3a81-8823-bc1aac000050', '52403c24-7e7f-39ab-a7ee-91944d000084');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.JobHistoryView.workTotal.virtualize_attribute', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-26 16:28:05', 5770, '2020-02-26 16:28:05', NULL, 'com.runwaysdk.system.metadata.VirtualizeAttribute', '4f664e38-0546-31c7-a379-5492ef00003c', 'a31b5077-c4f3-32ba-9775-49320a00010d', '33b1b276-0e24-3a81-8823-bc1aac000050', '52403c24-7e7f-39ab-a7ee-91944d000084');
ALTER TABLE job_history DROP work_progress;
ALTER TABLE job_history ADD COLUMN work_progress  bigint;
UPDATE job_history
 SET work_progress = a352d57359d074e3eb855057c9185b;
ALTER TABLE job_history DROP a352d57359d074e3eb855057c9185b;
ALTER TABLE job_history DROP work_total;
ALTER TABLE job_history ADD COLUMN work_total  bigint;
UPDATE job_history
 SET work_total = ae6c1c19c131d47468efbc3103c0d6;
ALTER TABLE job_history DROP ae6c1c19c131d47468efbc3103c0d6;
