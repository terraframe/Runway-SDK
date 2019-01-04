package com.runwaysdk.configuration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.file.Path;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.configuration.ConfigurationManager.ConfigGroup;
import com.runwaysdk.configuration.ConfigurationManager.ConfigGroupIF;

/**
 * Resolves configuration files from configuration sets referred to as 'appcfg' which are specific to an environment.
 * 
 * The hierarchy of configuration is as follows: envcfg -> appcfg -> properties file
 * 
 * The environment configuration (envcfg) is used to resolve the application's runtime configuration set (appcfg).
 * 
 * All of these resources are specified external to the deployable artifact and as such not resolvable directly from the classpath.
 * 
 * @author rrowlands
 */
public class EnvironmentConfigurationResolver extends CommonsConfigurationResolver
{
  private static Logger logger = LoggerFactory.getLogger(EnvironmentConfigurationResolver.class);
  
  /**
   * Contains the path to the resolved application configuration directory for the runtime application.
   */
  private File appCfg;
  
  private PropertiesConfiguration pEnvCfg;
  
  private Boolean isInitialized = false;
  
  public static final String DEFAULT_APP_CFG = "dev";

  public EnvironmentConfigurationResolver()
  {
    cconfig = new CompositeConfiguration();
    cconfig.addConfiguration(CommonsConfigurationResolver.getInMemoryConfigurator().getImpl());

    if (CommonsConfigurationResolver.getIncludeRuntimeProperties())
    {
      cconfig.addConfiguration(this.getRuntimeProperties());
    }
    
    load();
  }
  
  private synchronized void initialize()
  {
    if (isInitialized)
    {
      return;
    }
    else
    {
      isInitialized = true;
    }
    
    if (appCfg == null)
    {
      logger.error("The application's configuration directory (appcfg) has not been resolved! Using default Java resource loader strategy.");
    }
    else
    {
      logger.info("The application's configuration directory (appcfg) has been resolved to [" + appCfg.getAbsolutePath() + "].");
      
      if (pEnvCfg != null)
      {
        cconfig.addConfiguration(pEnvCfg);
      }
    }
  }
  
  private void load()
  {
    String sEnvCfg = System.getProperty("envcfg");
    if (sEnvCfg != null && sEnvCfg.length() > 0)
    {
      File fEnvCfg = new File(sEnvCfg);
      
      this.setEnvCfg(fEnvCfg);
    }
    
    String sAppCfg = System.getProperty("appcfg");
    if (sAppCfg != null && sAppCfg.length() > 0)
    {
      File fAppCfg = new File(sAppCfg);
      
      this.setAppCfg(fAppCfg);
    }
  }
  
  /**
   * Sets to location to the 'envcfg' directory, which should contain a 'envcfg.properties' file as well as
   * sub configuration directories per environment. The application's configuration will be resolved from
   * 'envcfg.properties' and any containing override properties will be loaded.
   */
  public void setEnvCfg(File envCfg)
  {
    if (!envCfg.exists())
    {
      return;
    }
    
    File fEnvCfgProps = new File(envCfg, "envcfg.properties");
    
    try
    {
      String sAppCfg = DEFAULT_APP_CFG;
      if (fEnvCfgProps.exists())
      {
        this.pEnvCfg = new PropertiesConfiguration(fEnvCfgProps);
        
        String sPropAppCfg = pEnvCfg.getString("appcfg");
        if (sPropAppCfg != null && sPropAppCfg.length() > 0)
        {
          sAppCfg = sPropAppCfg;
        }
      }
      
      File fAppCfg = new File(envCfg, sAppCfg);
      if (!fAppCfg.exists() || !fAppCfg.isDirectory())
      {
        return;
      }
      
      this.appCfg = fAppCfg;
    }
    catch (ConfigurationException e)
    {
      logger.error(e.getLocalizedMessage(), e);
    }
  }
  
  /**
   * Sets the location to the resolved application configuration directory, which has already been resolved to a particular environment.
   */
  public void setAppCfg(File appCfg)
  {
    if (!appCfg.exists() || !appCfg.isDirectory())
    {
      return;
    }
    
    this.appCfg = appCfg;
  }

  @Override
  public URL getResource(ConfigGroupIF location, String name)
  {
    initialize();
    
    URL urlBase = null;
    RunwayConfigurationException ex = null;
    try
    {
      urlBase = super.getResource(location, name);
    }
    catch (RunwayConfigurationException e)
    {
      ex = e;
    }
    
    if (appCfg != null)
    {
      File fOverride = new File(appCfg, location.getPath() + name);
      
      if (fOverride.exists())
      {
        try
        {
          if (urlBase == null)
          {
            return fOverride.toURI().toURL();
          }
          
          MergeUtility merger = new MergeUtility();
          InputStream urlBaseStream = urlBase.openStream();
          InputStream baseStream = urlBaseStream;
          FileInputStream overrideStream = new FileInputStream(fOverride);
          
          try
          {
            // Resolve includes
//            Properties baseProps = new Properties();
//            baseProps.load(baseStream);
//            
//            Iterator<Object> i = baseProps.keySet().iterator();
//            while (i.hasNext())
//            {
//              String key = (String) i.next();
//              
//              String value = baseProps.getProperty(key);
//              if (key.equals("include") && !value.equals("$REMOVE$"))
//              {
//                ByteArrayOutputStream noInclude = new ByteArrayOutputStream();
//                baseProps.remove(key);
//                baseProps.store(noInclude, null);
//                baseStream = new ByteArrayInputStream(noInclude.toByteArray());
//                
//                ByteArrayOutputStream resolvedInclude = new ByteArrayOutputStream();
//                URL includeURL = this.getResource(GeoprismConfigGroup.ROOT, location.getPath() + value);
//                merger.mergeStreams(includeURL.openStream(), baseStream, resolvedInclude, FilenameUtils.getExtension(fOverride.getAbsolutePath()));
//                baseStream = new ByteArrayInputStream(resolvedInclude.toByteArray());
//              }
//            }
          
            // Override
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            merger.mergeStreams(baseStream, overrideStream, os, FilenameUtils.getExtension(fOverride.getAbsolutePath()));
            
            InputStream is = new ByteArrayInputStream(os.toByteArray());
            
            return new URL(null, "inputstream://" + name, new InputStreamURLStreamHandler(is));
          }
          finally
          {
            // Byte array streams don't need to be closed, but our original streams do.
            urlBaseStream.close();
            overrideStream.close();
          }
        }
        catch (IOException e)
        {
          logger.error("Unexpected error.", e);
        }
      }
    }
    
    if (urlBase == null && ex != null)
    {
      throw ex;
    }
    
    return urlBase;
  }

  private class InputStreamURLStreamHandler extends URLStreamHandler {
    InputStream is;
    
    public InputStreamURLStreamHandler(InputStream is)
    {
      this.is = is;
    }
    
    @Override
    protected URLConnection openConnection(URL u) throws IOException
    {
      return new InputStreamURLConnection(is, u);
    }
  }

  private class InputStreamURLConnection extends URLConnection
  {
    InputStream is;
    
    public InputStreamURLConnection(InputStream is, URL url)
    {
      super(url);
      
      this.is = is;
    }

    @Override
    public void connect() throws IOException
    {
      
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
      return is;
    }
  }
}
