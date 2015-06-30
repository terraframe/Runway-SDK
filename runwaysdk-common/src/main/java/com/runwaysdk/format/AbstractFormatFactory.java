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
package com.runwaysdk.format;

import java.util.Locale;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;

/**
 * Abstract factory that returns FormatFactory objects.
 */
public class AbstractFormatFactory
{

  /**
   * Denotes if the formatFactory should delegate to the default
   * {@link StandardFormat} if an error occurs or no {@link Format} match is
   * found.
   */
  private Boolean         delegate;
  
  /**
   * The locale used for formatting if one is not specified. Although this can be changed
   * in common.properties it should equal Locale.ENGLISH for the most predictable behavior.
   */
  private Locale locale;

  /**
   * Reference to the internal factory.
   */
  private InternalFactory internalFactory;

  /**
   * Internal factory that is auto-wired (injected)
   */
  private static class InternalFactory
  {
    /**
     * The application's FormatFactory. This will default to
     * {@link StandardFormat} if no value is specified.
     */
    @Inject
    private volatile FormatFactory formatFactory;
  }

  /**
   * Singleton class to manage the static instance.
   */
  private static class Singleton
  {
    private static final AbstractFormatFactory Instance = new AbstractFormatFactory();
  }

  /**
   * Guide module to set up the module.
   */
  public class FormatFactoryModule implements Module
  {

    /**
     * Loads the class from common properties.
     */
    @Override
    public void configure(Binder binder)
    {
      String className = CommonProperties.getFormatFactoryClass();
      if (className != null && className.trim().length() > 0)
      {
        Class<?> clazz = LoaderDecorator.load(className);
        if (FormatFactory.class.isAssignableFrom(clazz))
        {
          binder.bind(FormatFactory.class).to(clazz.asSubclass(FormatFactory.class));
        }
        else
        {
          String msg = "Unable to instantiate the FormatFactory [" + clazz.getName() + "].";
          CommonExceptionProcessor.processException(
              ExceptionConstants.ConfigurationException.getExceptionClass(), msg);
        }
      }
    }
  }

  /**
   * Initializes this factory.
   */
  private AbstractFormatFactory()
  {
    Module module = new FormatFactoryModule();
    Injector injector = Guice.createInjector(module);
    this.internalFactory = injector.getInstance(InternalFactory.class);
    this.locale = CommonProperties.getFormatFactoryLocale();
    this.delegate = CommonProperties.isFormatFactoryDelegate();
    
    // enable delegation by wrapping the formatFactory, but only
    // if the formatFactory is not the default (otherwise it would
    // delegate to itself).
    if(this.delegate && !internalFactory.formatFactory.getClass().equals(StandardFormat.class))
    {
      this.internalFactory.formatFactory = new DelegateFormatFactory(internalFactory.formatFactory, new StandardFormat());
    }
  }

  private FormatFactory getInternalFormatFactory()
  {
    return internalFactory.formatFactory;
  }

  public static synchronized boolean isDelegating()
  {
    return Singleton.Instance.delegate;
  }
  
  /**
   * Sets the ability for the factories to delegate.
   * 
   * TODO auto-reset the formatFactory if this is set/unset.
   * @param delegate
   */
  public static synchronized void setDelegating(boolean delegate)
  {
    Singleton.Instance.delegate = delegate;
  }
  
  /**
   * Returns the current FormatFactory.
   * 
   * @return
   */
  public static synchronized FormatFactory getFormatFactory()
  {
    return Singleton.Instance.getInternalFormatFactory();
  }
  
  /**
   * Returns the default Locale used for formatting when a Locale
   * isn't provided.
   */
  public static Locale getLocale()
  {
    return Singleton.Instance.locale;
  }

  /**
   * Programmatic override to set the FormatFactory.
   * 
   * @param formatFactory
   */
  public static synchronized void setFormatFactory(FormatFactory customFactory)
  {
    // enable delegation by wrapping the given formatFactory, but only
    // if the formatFactory is not delegating to itself.
    if(Singleton.Instance.delegate && !customFactory.getClass().equals(StandardFormat.class))
    {
      Singleton.Instance.internalFactory.formatFactory = new DelegateFormatFactory(customFactory, new StandardFormat());
    }
    else
    {
      Singleton.Instance.internalFactory.formatFactory = customFactory;
    }
  }
}