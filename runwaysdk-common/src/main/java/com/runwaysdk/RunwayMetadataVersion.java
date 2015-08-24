/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk;

public class RunwayMetadataVersion
{
  static final Integer FIX_VERSION       = 0;

  static final Integer MINOR_VERSION     = 18;

  static final Integer MAJOR_VERSION     = 1;

  static final String  VERSION_DELIMETER = ".";

  private Integer      majorVersion;

  private Integer      minorVersion;

  private Integer      fixVersion;

  /**
   * @param majorVersion
   * @param minorVersion
   * @param fixVersion
   */
  public RunwayMetadataVersion(Integer majorVersion, Integer minorVersion, Integer fixVersion)
  {
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
    this.fixVersion = fixVersion;
  }

  public RunwayMetadataVersion(String version)
  {
    this.majorVersion = 0;
    this.minorVersion = 0;
    this.fixVersion = 0;

    try
    {
      String[] versions = version.split("\\.");

      if (versions.length > 0)
      {
        this.majorVersion = Integer.parseInt(versions[0]);
      }

      if (versions.length > 1)
      {
        this.minorVersion = Integer.parseInt(versions[1]);
      }

      if (versions.length > 2)
      {
        this.fixVersion = Integer.parseInt(versions[2]);
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException("Invalid format of a runway version");
    }
  }

  @Override
  public String toString()
  {
    return this.majorVersion + VERSION_DELIMETER + this.minorVersion + VERSION_DELIMETER + this.fixVersion;
  }

  public static RunwayMetadataVersion getCurrentVersion()
  {
    return new RunwayMetadataVersion(MAJOR_VERSION, MINOR_VERSION, FIX_VERSION);
  }

  public Integer getMajorVersion()
  {
    return this.majorVersion;
  }

  public Integer getMinorVersion()
  {
    return this.minorVersion;
  }

  public Integer getFixVersion()
  {
    return this.fixVersion;
  }

  /**
   * Method to determine if one RunwayVersion is greater than another
   * RunwayVersion. However, this method does not take into account the fix
   * version number when determining which version is greater. The algorithm for
   * determing which version is higher is majorVersion then minorVersion.
   * 
   * @param version
   * @return
   */
  public boolean isGreater(RunwayMetadataVersion version)
  {
    if (this.majorVersion > version.getMajorVersion())
    {
      return true;
    }

    if (this.minorVersion > version.getMinorVersion())
    {
      return true;
    }

    return false;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof RunwayMetadataVersion)
    {
      RunwayMetadataVersion version = (RunwayMetadataVersion) obj;

      return ( majorVersion.equals(version.getMajorVersion()) && minorVersion.equals(version.getMinorVersion()) && fixVersion.equals(version.getFixVersion()) );
    }

    return false;
  }
}
