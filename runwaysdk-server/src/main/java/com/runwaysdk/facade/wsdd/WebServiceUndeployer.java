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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.axis.client.AdminClient;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DeployProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;

public class WebServiceUndeployer
{
  /**
   * List of web services to deploy.
   */
  private List<Service> services;

  /**
   * Constructor.
   */
  public WebServiceUndeployer()
  {
    services = new LinkedList<Service>();
  }

  /**
   * Deploys the web service definitions.
   */
  public void undeploy()
  {
    // boolean enabled = CommonProperties.getContainerWebServiceEnabled();

    // only deploy the services if web services is flagged as enabled
    // if (enabled && services.size() > 0)
    if (services.size() > 0)
    {
      try
      {

        String urlString = CommonProperties.getContainerWebServiceDeployURL();
        if (!urlString.endsWith("/"))
        {
          urlString += "/";
        }

        // make sure each service exists ... remove the undeploy request
        // otherwise
        for (int i = 0; i < services.size(); i++)
        {
          Service service = services.get(i);
          try
          {
            URL url = new URL(urlString + service.getServiceName());

            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setReadTimeout(5000);
            httpConnection.setConnectTimeout(5000);

            httpConnection.connect();

            int code = httpConnection.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK)
            {
              services.remove(i);
            }
            httpConnection.disconnect();
          }
          catch (IOException e)
          {
            // error occured ... no service to undeploy
            services.remove(i);
          }
        }

        // undeploy any services
        if (services.size() > 0)
        {
          URL url = new URL(urlString + WebServiceConstants.DEPLOY_SERVICE);

          String username = DeployProperties.getContainerUsername();
          String password = DeployProperties.getContainerPassword();

          AdminClient adminClient = new AdminClient();
          adminClient.setLogin(username, password);
          adminClient.setTargetEndpointAddress(url);

          for (Service service : services)
          {
            adminClient.undeployService(service.getServiceName());
          }

        }
      }
      catch (Exception e)
      {
        // we were unable to undeploy web services
        throw new ProgrammingErrorException(e);
      }
    }
  }

}
