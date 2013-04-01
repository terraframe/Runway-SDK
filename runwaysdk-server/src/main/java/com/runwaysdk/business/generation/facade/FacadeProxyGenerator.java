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
package com.runwaysdk.business.generation.facade;

import com.runwaysdk.business.generation.AbstractClientGenerator;
import com.runwaysdk.business.generation.AbstractGenerator;
import com.runwaysdk.constants.ClientConstants;
import com.runwaysdk.constants.ClientRequestExceptionInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.dataaccess.MdFacadeDAOIF;
import com.runwaysdk.generation.loader.Reloadable;

public class FacadeProxyGenerator extends AbstractClientGenerator
{
  public FacadeProxyGenerator(MdFacadeDAOIF mdFacade)
  {
    super(mdFacade, getGeneratedName(mdFacade));
  }

  public static void generateFacadeProxy(MdFacadeDAOIF mdFacadeIF)
  {
    FacadeProxyGenerator generator = new FacadeProxyGenerator(mdFacadeIF);
    generator.go(false);
  }

  public void go(boolean forceRegeneration)
  {
    // Only in the runway development environment do we ever generate business classes for metadata.
    if (this.getMdTypeDAOIF().isSystemPackage() && !LocalProperties.isRunwayEnvironment())
    {
      return;
    }

    // Do regenerate if the existing file is symantically the same
    if (LocalProperties.isKeepBaseSource() &&
        AbstractGenerator.hashEquals(this.getSerialVersionUID(), this.getPath()))
    {
      return;
    }

    writePackage();
    addSignatureAnnotation();
    writeClassName();
    writeFields();
    writeGetFacadeProxy();
    getWriter().closeBracket();
    getWriter().close();
  }

  /**
   * @see com.runwaysdk.business.generation.AbstractGenerator#getRootSourceDirectory()
   */
  protected String getRootSourceDirectory()
  {
    return getRootClientBaseDirectory(this.getMdTypeDAOIF());
  }

  public long getSerialVersionUID()
  {
    return this.getSerialVersionUID("PROXY", this.getSignature());
  }

  @Override
  protected MdFacadeDAOIF getMdTypeDAOIF()
  {
    return (MdFacadeDAOIF)super.getMdTypeDAOIF();
  }

  /**
   * Generate the package declaration
   */
  private void writePackage()
  {
    getWriter().writeLine("package " + getMdTypeDAOIF().getPackage() + ";");
    getWriter().writeLine("");
  }

  /**
   * Generates the class name declaration
   */
  private void writeClassName()
  {
    String interfaceName = FacadeProxyIFGenerator.getGeneratedName(this.getMdTypeDAOIF());
    getWriter().write("public abstract class " + getFileName() + " implements " + interfaceName);
    if (!this.getMdTypeDAOIF().isSystemPackage())
    {
      getWriter().write(", " + Reloadable.class.getName());
    }
    getWriter().writeLine("");

    getWriter().openBracket();
  }

  private void writeFields()
  {
  }

  private void writeGetFacadeProxy()
  {
    String facadeProxyName = getGeneratedName(this.getMdTypeDAOIF());
    String javaNameQualified = this.getMdTypeDAOIF().getPackage()+"."+JavaFacadeProxyGenerator.getGeneratedName(this.getMdTypeDAOIF());
    String rmiName = RMIFacadeProxyGenerator.getGeneratedName(this.getMdTypeDAOIF());
    String webServiceName = WebServiceFacadeProxyGenerator.getGeneratedName(this.getMdTypeDAOIF());

    //Get the name of the Connection class and the Type class, since Type is an inner
    //class of Connection the '$' of .getName needs to be replaced with "."
    String connectionName = ClientConstants.CONNECTION_LABEL_CLASS;
    String typeName = ClientConstants.CONNECTION_LABEL_TYPE_CLASS.replace("$", ".");

    getWriter().writeLine("public static " + facadeProxyName + " getClientRequest()");
    getWriter().openBracket();
    getWriter().writeLine("return getClientRequest(" + ClientConstants.CONNECTION_LABEL_FACADE_CLASS + ".getConnection());");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static " + facadeProxyName + " getClientRequest(" + String.class.getName() + " label)");
    getWriter().openBracket();
    getWriter().writeLine("return getClientRequest(" + ClientConstants.CONNECTION_LABEL_FACADE_CLASS + ".getConnection(label));");
    getWriter().closeBracket();
    getWriter().writeLine("");

    getWriter().writeLine("public static " + facadeProxyName + " getClientRequest(" + connectionName + " connection)");
    getWriter().openBracket();
    getWriter().writeLine("if(" + typeName + ".JAVA.equals(connection.getType()))");
    getWriter().openBracket();
    // use reflection to avoid the JavaClientRequest -> JavaAdapter dependency
    getWriter().writeLine("try");
    getWriter().openBracket();
    getWriter().writeLine("Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load(\""+javaNameQualified+"\");");
    getWriter().writeLine("Object object = clazz.getConstructor(String.class, String.class).newInstance(connection.getLabel(), connection.getAddress());");
    getWriter().writeLine("return ("+facadeProxyName+") object;");
    getWriter().closeBracket();
    getWriter().writeLine("catch (Exception e)");
    getWriter().openBracket();
    getWriter().writeLine("throw new "+ClientRequestExceptionInfo.CLASS+"(e);");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("else if(" + typeName + ".RMI.equals(connection.getType()))");
    getWriter().openBracket();
    getWriter().writeLine("return new " + rmiName + "(connection.getLabel(), connection.getAddress());");
    getWriter().closeBracket();
    getWriter().writeLine("else");
    getWriter().openBracket();
    getWriter().writeLine("return new " + webServiceName + "(connection.getLabel(), connection.getAddress());");
    getWriter().closeBracket();
    getWriter().closeBracket();
    getWriter().writeLine("");
  }

  public static String getGeneratedName(MdFacadeDAOIF mdFacade)
  {
    return mdFacade.getValue(MdFacadeInfo.NAME) + "Proxy";
  }
}
