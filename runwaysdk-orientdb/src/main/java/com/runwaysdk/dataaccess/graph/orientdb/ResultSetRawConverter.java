package com.runwaysdk.dataaccess.graph.orientdb;

public class ResultSetRawConverter extends ResultSetConverter
{
  @Override
  protected Object convertFromDAO(Object dao)
  {
    return dao;
  }
}
