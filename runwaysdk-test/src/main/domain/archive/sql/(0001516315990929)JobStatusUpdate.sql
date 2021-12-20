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

INSERT INTO metadata_display_label ( key_name, oid, site_master, default_locale) VALUES ('8q46oliwdhxsb0zuqygrqoombxxi97cq0287', '8q46oliwdhxsb0zuqygrqoombxxi97cq0287', 'www.runwaysdk.com', 'Warning');
INSERT INTO job_status ( oid) VALUES ('yzixpscqsx9h3radnycxjoluvf19wko80231');
INSERT INTO enumeration_master ( site_master, last_updated_by, owner, locked_by, key_name, type, enum_name, display_label, oid, last_update_date, create_date, created_by, entity_domain, seq) VALUES ('www.runwaysdk.com', '000000000000000000000000000000100060', '000000000000000000000000000000100060', NULL, 'com.runwaysdk.system.scheduler.JobStatus.WARNING', 'com.runwaysdk.system.scheduler.JobStatus', 'WARNING', '8q46oliwdhxsb0zuqygrqoombxxi97cq0287', 'yzixpscqsx9h3radnycxjoluvf19wko80231', '2018-05-21 13:41:09', '2018-05-21 13:41:09', '000000000000000000000000000000100060', NULL, 4923);
INSERT INTO enumeration_attribute_item ( oid, parent_oid, child_oid) VALUES ('ilgngrtxxzth1auzck1is71wthg9jsii0247', 'ij1epz96kc3kazn8h799jceyjx02y6rf0075', 'yzixpscqsx9h3radnycxjoluvf19wko80231');
INSERT INTO metadata_relationship ( oid, created_by, type, last_update_date, site_master, owner, seq, key_name, entity_domain, locked_by, create_date, last_updated_by, parent_oid, child_oid) VALUES ('ilgngrtxxzth1auzck1is71wthg9jsii0247', '000000000000000000000000000000100060', 'com.runwaysdk.system.metadata.EnumerationAttributeItem', '2018-05-21 13:41:09', 'www.runwaysdk.com', '000000000000000000000000000000100060', 4924, 'com.runwaysdk.system.scheduler.AllJobStatus.WARNING', NULL, NULL, '2018-05-21 13:41:09', '000000000000000000000000000000100060', 'ij1epz96kc3kazn8h799jceyjx02y6rf0075', 'yzixpscqsx9h3radnycxjoluvf19wko80231');
UPDATE metadata SET seq= 4925  WHERE oid='ij1epz96kc3kazn8h799jceyjx02y6rf0075';
