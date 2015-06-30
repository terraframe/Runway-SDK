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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.axis.client.AdminClient;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.facade.WebServiceAdapter;
import com.runwaysdk.web.json.JSONWebServiceAdapter;

/**
 * Deploys web services for core facades and MdFacades.
 * Each service can be added by calling the appropriate addService() methods.
 */
public class WebServiceDeployer {

	/**
	 * List of web services to deploy.
	 */
	private List<Service> services;

	/**
	 * Constructor.
	 */
	public WebServiceDeployer()
	{
		services = new LinkedList<Service>();
	}

	/**
	 * Adds a service given a class file.
	 * This should only be used for the services provided by the core and
	 * not MdFacade services.
	 *
	 * @param service
	 */
	public void addService(Class<?> service)
	{
		services.add(new CoreService(service));
	}

	/**
	 * Adds a service to this ServiceDeployer based
	 * on an MdFacade.
	 *
	 * @pre The MdFacade must already exist on the filesystem
	 *
	 * @param service
	 * @throws ClassNotFoundException
	 * @throws MalformedURLException
	 */
	public void addService(MdFacadeDAOIF mdFacade)
	{
		Service service = new MdFacadeService(mdFacade);
		services.add(service);
	}

	/**
	 * Adds services to this ServiceDeployer based on a list
	 * of MdFacades.
	 *
	 * @param mdFacades
	 */
	public void addServices(Set<MdFacadeDAOIF> mdFacades)
	{
		for(MdFacadeDAOIF mdFacade : mdFacades)
		{
			addService(mdFacade);
		}
	}

	/**
	 * Deploys the web service definitions. All MdFacades must be
	 * compiled and loaded into the JVM before this call.
	 */
	public void deploy()
	{
//		boolean enabled = CommonProperties.getContainerWebServiceEnabled();

		// only deploy the services if web services is flagged as enabled
//		if(enabled && services.size() > 0)
		if(services.size() > 0)
		{
			try {

				WSDD_DeployTemplate wsdd = new WSDD_DeployTemplate(services);

				String username = DeployProperties.getContainerUsername();
				String password = DeployProperties.getContainerPassword();

				String url = CommonProperties.getContainerWebServiceDeployURL();
				if(!url.endsWith("/"))
				{
				  url += "/";
				}
				url += WebServiceConstants.DEPLOY_SERVICE;

				AdminClient adminClient = new AdminClient();
				adminClient.setLogin(username, password);
				adminClient.setTargetEndpointAddress(new URL(url));

				InputStream stream = wsdd.getInputStream();
				adminClient.process(stream);

			} catch (Exception e) {
				// we were unable to deploy web services
				throw new ProgrammingErrorException(e);
			}
		}
	}

	/**
	 * Callable by ANT (or programatically) to define the web services for
	 * the core. This will deploy the default services: WebServiceAdapter and
	 * JSONWebServiceAdapter.
	 * @throws Exception
	 */
	public static void main(String[] args)
	{
		WebServiceDeployer serviceDeployer = new WebServiceDeployer();

		serviceDeployer.addService(WebServiceAdapter.class);
		serviceDeployer.addService(JSONWebServiceAdapter.class);

		serviceDeployer.deploy();
	}
}
