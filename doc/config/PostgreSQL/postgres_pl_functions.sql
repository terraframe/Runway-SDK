--
-- Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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

/*
 * Creates an oid.  It requires a sequence number and a timestamp.
 */
CREATE OR REPLACE FUNCTION com_runwaysdk_createid
(
  _rootTypeId         VARCHAR,
  _random             BIGINT,
  _sitemaster         VARCHAR,
  _seq                BIGINT,
  _currentTimeMillis  TIMESTAMP
)
RETURNS VARCHAR AS $$


DECLARE
  _idString           VARCHAR;
  _id                 VARCHAR;

BEGIN

    _idString := _sitemaster || ':' || _currentTimeMillis || ':' || _random || ':' || _seq;
    SELECT MD5(_idString) INTO _id;
    _id := _id || _rootTypeId;

    RETURN _id;

END;
$$ LANGUAGE plpgsql;