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

-- This file should patch most legacy databases into using the newer runway patcher. If the database is pretty old you may need to manually run some sql
-- which you can find at runwaysdk-test/src/main/domain/archive/sql. Make sure the database has all sql in the range of (0000000000100001) - (0000000000100009)


-----------------------------------------------------------------------
-------------- When you manually patch runway metadata ----------------
-----------------------------------------------------------------------
------------------- !!DO NOT LOG IN AS ROOT!! -------------------------
-----------------------------------------------------------------------

INSERT INTO dynamic_properties ( id, version_number) VALUES ('000000000000000000000', '0000000000010000'); -- universal.xml
-- INSERT INTO dynamic_properties ( id, version_number) VALUES ('000000000000000000000', '0000000000100010'); -- Change_Md_Action_cache_type
