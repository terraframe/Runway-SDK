/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.facade.wsdd;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class WSDD_UndeployTemplate {
	
	/**
	 * The root WSDD element start.
	 */
	private static final String ROOT_START = "<undeployment xmlns='http://xml.apache.org/axis/wsdd/'>";
	
	/**
	 * The root WSDD element end.
	 */
	private static final String ROOT_END = "</undeployment>";
	
	/**
	 * A list of services to add to the WSDD template.
	 */
	private List<Service> services;
	
	/**
	 * Constructor to set the list of services.
	 * 
	 * @param services
	 */
	public WSDD_UndeployTemplate(List<Service> services)
	{
		this.services = services;
	}
	
	/**
	 * Returns a string the defines a single service.
	 * 
	 * @param service
	 * @return
	 */
	private String defineService(Service service)
	{
		String serviceName = service.getServiceName();
		
		return "<service name='"+serviceName+"'/>";
	}

	/**
	 * Returns the WSDD template as an InputStream.
	 * 
	 * @return
	 */
	public InputStream getInputStream()
	{
		String wsdd = ROOT_START;
		for(Service service : services)
		{
			// append the service to deploy list
			wsdd += defineService(service);
		}
		wsdd += ROOT_END;
		
		return new ByteArrayInputStream(wsdd.getBytes());
	}
}
