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

INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('803df42e-8084-3122-b13f-c0eaff00011f', '803df42e-8084-3122-b13f-c0eaff00011f', 'www.runwaysdk.com', 'Warning');
INSERT INTO job_status ( oid) VALUES ('4f0ffc6e-2f20-3454-aabc-a83dd00000e7');
INSERT INTO enumeration_master ( site_master, last_updated_by, owner, locked_by, key_name, type, enum_name, display_label, oid, last_update_date, create_date, created_by, entity_domain, seq) VALUES ('www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 'com.runwaysdk.system.scheduler.JobStatus.WARNING', 'com.runwaysdk.system.scheduler.JobStatus', 'WARNING', '803df42e-8084-3122-b13f-c0eaff00011f', '4f0ffc6e-2f20-3454-aabc-a83dd00000e7', '2018-05-21 13:41:09', '2018-05-21 13:41:09', '4f664e38-0546-31c7-a379-5492ef00003c', NULL, 4923);
INSERT INTO enumeration_attribute_item ( oid, parent_oid, child_oid) VALUES ('dde75d82-5943-3e90-98f4-67c4e70000f7', '0d0d6963-bd58-3b1f-aab8-ab0c1100004b', '4f0ffc6e-2f20-3454-aabc-a83dd00000e7');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('dde75d82-5943-3e90-98f4-67c4e70000f7', '4f664e38-0546-31c7-a379-5492ef00003c', 'com.runwaysdk.system.metadata.EnumerationAttributeItem', '2018-05-21 13:41:09', 'www.runwaysdk.com', '4f664e38-0546-31c7-a379-5492ef00003c', 4924, 'com.runwaysdk.system.scheduler.AllJobStatus.WARNING', NULL, NULL, '2018-05-21 13:41:09', '4f664e38-0546-31c7-a379-5492ef00003c', '0d0d6963-bd58-3b1f-aab8-ab0c1100004b', '4f0ffc6e-2f20-3454-aabc-a83dd00000e7');
UPDATE metadata SET seq= 4925  WHERE oid='0d0d6963-bd58-3b1f-aab8-ab0c1100004b';
