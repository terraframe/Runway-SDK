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

INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('dbe3c0c8-df62-4fa3-b42f-4afef600011f', 'Queued', 'dbe3c0c8-df62-4fa3-b42f-4afef600011f', 'www.runwaysdk.com');
INSERT INTO job_status ( oid) VALUES ('6a4a42dc-89ff-451c-ab24-6714e00000e7');
INSERT INTO enumeration_master ( entity_domain, last_updated_by, created_by, owner, last_update_date, site_master, display_label, enum_name, locked_by, create_date, oid, type, seq, key_name) VALUES (NULL, '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-17 17:35:52', 'www.runwaysdk.com', 'dbe3c0c8-df62-4fa3-b42f-4afef600011f', 'QUEUED', NULL, '2020-02-17 17:35:52', '6a4a42dc-89ff-451c-ab24-6714e00000e7', 'com.runwaysdk.system.scheduler.JobStatus', 5750, 'com.runwaysdk.system.scheduler.JobStatus.QUEUED');
INSERT INTO enumeration_attribute_item ( oid, parent_oid, child_oid) VALUES ('458bfc01-9be8-3411-8a19-bbab6d0000f7', '0d0d6963-bd58-3b1f-aab8-ab0c1100004b', '6a4a42dc-89ff-451c-ab24-6714e00000e7');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.AllJobStatus.QUEUED', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-17 17:35:52', 5751, '2020-02-17 17:35:52', NULL, 'com.runwaysdk.system.metadata.EnumerationAttributeItem', '4f664e38-0546-31c7-a379-5492ef00003c', '458bfc01-9be8-3411-8a19-bbab6d0000f7', '0d0d6963-bd58-3b1f-aab8-ab0c1100004b', '6a4a42dc-89ff-451c-ab24-6714e00000e7');
INSERT INTO metadata_display_label ( oid, default_locale, key_name, site_master) VALUES ('8bb8330c-ec85-4ce3-ad6f-9fce8c00011f', 'New', '8bb8330c-ec85-4ce3-ad6f-9fce8c00011f', 'www.runwaysdk.com');
INSERT INTO job_status ( oid) VALUES ('49a246f4-fc9b-487d-9ddf-0fa1850000e7');
INSERT INTO enumeration_master ( entity_domain, last_updated_by, created_by, owner, last_update_date, site_master, display_label, enum_name, locked_by, create_date, oid, type, seq, key_name) VALUES (NULL, '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-17 17:35:52', 'www.runwaysdk.com', '8bb8330c-ec85-4ce3-ad6f-9fce8c00011f', 'NEW', NULL, '2020-02-17 17:35:52', '49a246f4-fc9b-487d-9ddf-0fa1850000e7', 'com.runwaysdk.system.scheduler.JobStatus', 5752, 'com.runwaysdk.system.scheduler.JobStatus.NEW');
INSERT INTO enumeration_attribute_item ( oid, parent_oid, child_oid) VALUES ('33ffbec7-e3ea-308f-bb45-5fe8140000f7', '0d0d6963-bd58-3b1f-aab8-ab0c1100004b', '49a246f4-fc9b-487d-9ddf-0fa1850000e7');
INSERT INTO metadata_relationship ( site_master, entity_domain, last_updated_by, key_name, created_by, create_date, seq, last_update_date, locked_by, type, owner, oid, parent_oid, child_oid) VALUES ('www.runwaysdk.com', NULL, '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.scheduler.AllJobStatus.NEW', '4f664e38-0546-31c7-a379-5492ef00003c', '2020-02-17 17:35:52', 5753, '2020-02-17 17:35:52', NULL, 'com.runwaysdk.system.metadata.EnumerationAttributeItem', '4f664e38-0546-31c7-a379-5492ef00003c', '33ffbec7-e3ea-308f-bb45-5fe8140000f7', '0d0d6963-bd58-3b1f-aab8-ab0c1100004b', '49a246f4-fc9b-487d-9ddf-0fa1850000e7');
