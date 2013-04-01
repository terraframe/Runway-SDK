
/*
 * Creates an id.  It requires a sequence number and a timestamp.
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