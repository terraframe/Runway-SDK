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

import java.util.List;

import com.runwaysdk.business.generation.facade.WebServiceAdapterGenerator;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.MdMethodDAOIF;

/**
 * Represents a webservice for an MdFacade.
 */
public class MdFacadeService implements Service {

	/**
	 * The MdFacade representing this service.
	 */
	private MdFacadeDAOIF mdFacade;
	
	private String serviceName;
	
	private String className;
	
	/**
	 * Constructor to set the MdFacade that this Service will
	 * represent.
	 * 
	 * @param mdFacade
	 */
	public MdFacadeService(MdFacadeDAOIF mdFacade)
	{
		this.mdFacade = mdFacade;
		
		serviceName = mdFacade.getPackage() + "." + WebServiceAdapterGenerator.getGeneratedName(mdFacade);
		className = serviceName; // as of now, the convention is that the service and class name are the same
	}

	/**
	 * Returns the class name.
	 * 
	 * @return
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * Returns the service name.
	 * 
	 * @return
	 */
	public String getServiceName()
	{
		return serviceName;
	}

	/**
	 * Returns a comma delimited list of the publishable methods
	 * for this service.
	 * 
	 * @return
	 */
	public String getMethods() {
		
		String methods = "";
		List<MdMethodDAOIF> mdMethods = mdFacade.getMdMethods();
		for(int i=0; i<mdMethods.size(); i++)
		{
			methods += mdMethods.get(i).getName();
			
			// append a comma if we're not on the last item
			if(i < mdMethods.size()-1)
			{
				methods += ",";
			}
		}
		
		return methods;
	}
	
}
