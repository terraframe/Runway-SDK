package com.runwaysdk.business.graph.generation;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.LinkedList;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.business.generation.DelegateCompiler;
import com.runwaysdk.business.generation.GenerationManager;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.dataaccess.Command;
import com.runwaysdk.dataaccess.MdTypeDAOIF;
import com.runwaysdk.dataaccess.io.SourceWriter;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.graph.MdVertexDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiLineStringDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributeMultiPolygonDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePointDAO;
import com.runwaysdk.gis.dataaccess.metadata.MdAttributePolygonDAO;
import com.runwaysdk.session.Request;

public class VertexObjectGeneratorTest
{
  private static MdVertexDAO                   mdVertexDAO;

  private static MdAttributeCharacterDAO       mdCharacterAttribute;

  private static MdAttributeIntegerDAO         mdIntegerAttribute;

  private static MdAttributeLongDAO            mdLongAttribute;

  private static MdAttributeFloatDAO           mdFloatAttribute;

  private static MdAttributeDoubleDAO          mdDoubleAttribute;

  private static MdAttributeBooleanDAO         mdBooleanAttribute;

  private static MdAttributeDateDAO            mdDateAttribute;

  private static MdAttributeDateTimeDAO        mdDateTimeAttribute;

  private static MdAttributeTimeDAO            mdTimeAttribute;

  private static MdAttributePointDAO           mdPointAttribute;

  private static MdAttributePolygonDAO         mdPolygonAttribute;

  private static MdAttributeLineStringDAO      mdLineStringAttribute;

  private static MdAttributeMultiPointDAO      mdMultiPointAttribute;

  private static MdAttributeMultiPolygonDAO    mdMultiPolygonAttribute;

  private static MdAttributeMultiLineStringDAO mdMultiLineStringAttribute;

  @Request
  @BeforeClass
  public static void classSetup()
  {
    LocalProperties.setSkipCodeGenAndCompile(true);

    mdVertexDAO = TestFixtureFactory.createMdVertex();
    mdVertexDAO.apply();

    mdCharacterAttribute = TestFixtureFactory.addCharacterAttribute(mdVertexDAO);
    mdCharacterAttribute.apply();

    mdIntegerAttribute = TestFixtureFactory.addIntegerAttribute(mdVertexDAO);
    mdIntegerAttribute.apply();

    mdLongAttribute = TestFixtureFactory.addLongAttribute(mdVertexDAO);
    mdLongAttribute.apply();

    mdFloatAttribute = TestFixtureFactory.addFloatAttribute(mdVertexDAO);
    mdFloatAttribute.apply();

    mdDoubleAttribute = TestFixtureFactory.addDoubleAttribute(mdVertexDAO);
    mdDoubleAttribute.apply();

    mdBooleanAttribute = TestFixtureFactory.addBooleanAttribute(mdVertexDAO);
    mdBooleanAttribute.apply();

    mdDateAttribute = TestFixtureFactory.addDateAttribute(mdVertexDAO);
    mdDateAttribute.apply();

    mdDateTimeAttribute = TestFixtureFactory.addDateTimeAttribute(mdVertexDAO);
    mdDateTimeAttribute.apply();

    mdTimeAttribute = TestFixtureFactory.addTimeAttribute(mdVertexDAO);
    mdTimeAttribute.apply();

    mdPointAttribute = TestFixtureFactory.addPointAttribute(mdVertexDAO);
    mdPointAttribute.apply();

    mdPolygonAttribute = TestFixtureFactory.addPolygonAttribute(mdVertexDAO);
    mdPolygonAttribute.apply();

    mdLineStringAttribute = TestFixtureFactory.addLineStringAttribute(mdVertexDAO);
    mdLineStringAttribute.apply();

    mdMultiPointAttribute = TestFixtureFactory.addMultiPointAttribute(mdVertexDAO);
    mdMultiPointAttribute.apply();

    mdMultiPolygonAttribute = TestFixtureFactory.addMultiPolygonAttribute(mdVertexDAO);
    mdMultiPolygonAttribute.apply();

    mdMultiLineStringAttribute = TestFixtureFactory.addMultiLineStringAttribute(mdVertexDAO);
    mdMultiLineStringAttribute.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdVertexDAO);

    LocalProperties.setSkipCodeGenAndCompile(false);
  }

  @Request
  @Test
  public void testGenerateBase()
  {
    StringWriter writer = new StringWriter();

    VertexObjectBaseGenerator generator = new VertexObjectBaseGenerator(mdVertexDAO);
    generator.setWriter(new SourceWriter(new BufferedWriter(writer)));
    generator.go(true);

    String output = writer.toString();

    Assert.assertNotNull(output);
  }

  @Request
  @Test
  public void testGenerateStub()
  {
    StringWriter writer = new StringWriter();

    VertexObjectStubGenerator generator = new VertexObjectStubGenerator(mdVertexDAO);
    generator.setWriter(new SourceWriter(new BufferedWriter(writer)));
    generator.go(true);

    String output = writer.toString();

    Assert.assertNotNull(output);
  }

  @Request
  @Test
  public void testGenerateAndCompile()
  {
    LinkedList<MdTypeDAOIF> list = new LinkedList<MdTypeDAOIF>();
    list.add(mdVertexDAO);

    GenerationManager.forceRegenerate(mdVertexDAO);

    DelegateCompiler compiler = new DelegateCompiler();
    compiler.compile(list);

    // Assert the files exist
    Command command = mdVertexDAO.getDeleteJavaArtifactCommand(null);
    command.doIt();
  }

}
