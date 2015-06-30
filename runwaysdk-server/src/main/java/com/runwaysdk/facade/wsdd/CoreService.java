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
package com.runwaysdk.facade.wsdd;

public class CoreService implements Service {
	
	/**
	 * The Java Class that represents the service.
	 */
	private Class<?> service;
	
	
	/**
	 * All methods in the core facades can be published.
	 * Use the wildcard * to denote this (Apache Axis understands this).
	 */
	private final String methods = "*";
	
	/**
	 * Constructor
	 * 
	 * @param service
	 */
	public CoreService(Class<?> service)
	{
		this.service = service;
	}
	
	/**
	 * Returns the service name.
	 * 
	 * @return
	 */
	public String getServiceName()
	{
		return service.getName();
	}
	
	/**
	 * Returns the class name.
	 * 
	 * @return
	 */
	public String getClassName()
	{
		return service.getName();
	}
	
	/**
	 * Returns the publishable methods.
	 * 
	 * @return
	 */
	public String getMethods()
	{
		return methods;
	}
}
