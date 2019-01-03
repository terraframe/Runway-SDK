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

UPDATE md_attribute_character SET database_size= 4000 , oid= '20070405EG00000000000000000014310067'  WHERE oid='20070405EG00000000000000000014310067';
UPDATE metadata SET seq= 4852 , last_update_date= '2017-04-26 08:22:31'  WHERE oid='20070405EG00000000000000000014310067';
ALTER TABLE md_attribute_concrete ALTER COLUMN column_name TYPE varchar(4000);
