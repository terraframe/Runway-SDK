/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.business.state;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DuplicateDataException;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.TransitionDAOIF;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.metadata.NameConventionException;
import com.runwaysdk.dataaccess.metadata.ReservedWordException;
import com.runwaysdk.session.Request;

import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class StateTest
{
  private static volatile MdBusinessDAO     mdBusiness  = null;

  private static volatile MdBusinessDAO     mdBusiness2 = null;

  private static volatile MdStateMachineDAO mdState     = null;

  private static volatile MdStateMachineDAO mdState2    = null;

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    // Create a new class
    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "TestClass1");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.state");
    mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdBusiness.setValue(MdBusinessInfo.DESCRIPTION, "Temporary JUnit Test Class");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    mdBusiness.apply();

    mdBusiness2 = MdBusinessDAO.newInstance();
    mdBusiness2.setValue(MdBusinessInfo.NAME, "TestClass2");
    mdBusiness2.setValue(MdBusinessInfo.PACKAGE, "test.state");
    mdBusiness2.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdBusiness2.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdBusiness2.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness2.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness2.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    mdBusiness2.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusiness2);
  }

  /**
   * No setup needed non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Request
  @Before
  protected void setUp() throws Exception
  {

  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Request
  @After
  protected void tearDown() throws Exception
  {
    if (mdState != null && !mdState.isNew())
    {
      mdState.delete();
      mdState = null;
    }

    if (mdState2 != null && !mdState2.isNew())
    {
      mdState2.delete();
      mdState2 = null;
    }
  }

  @Request
  @Test
  public void testNameLimits()
  {
    // Create a new state
    mdState = MdStateMachineDAO.newInstance();

    mdState.setValue(MdTypeInfo.NAME, "packMachine");

    String pack = "test.";

    for (int i = 0; i < MdStateMachineDAO.MAX_PACKAGE + 1; i++)
    {
      pack += "a";
    }

    // Try to set a package that is too long
    try
    {
      mdState.setValue(MdTypeInfo.PACKAGE, pack);

      Assert.fail("Able to set a package that is too long");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }

    String name = "MachineName";

    while (name.length() < 63)
    {
      name += "A";
    }

    try
    {
      mdState.setValue(MdTypeInfo.NAME, name);
      Assert.fail("Able to set a state machine name that is too long");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }

    // Try to set a package that is exact length
    pack = "test.";

    for (int i = 0; i < MdStateMachineDAO.MAX_PACKAGE - 25; i++)
    {
      pack += "a";
    }

    mdState.setValue(MdStateMachineInfo.NAME, "PackMachineAAAAAAAAAAAAAAAA");
    mdState.setValue(MdStateMachineInfo.PACKAGE, pack);
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Delete Machine Test");
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdState.apply();

    try
    {
      StateMasterDAO state = mdState.addState("PackMachineAAAAAAAAAAAAAAAAA", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state.apply();
      Assert.fail("Able to create a state with a name that is too long");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }

    try
    {
      StateMasterDAO state = mdState.addState("Invalid state name", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state.apply();

      Assert.fail("Able to create a state with spaces in the name");
    }
    catch (NameConventionException e)
    {
      // This is expected
    }

    StateMasterDAO state = mdState.addState("PackMachine_AAAAAAAAAAAAAAA", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state.apply();
    Assert.assertTrue(state.isDefaultState());

    StateMasterDAO state2 = mdState.addState("StateB", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state2.apply();

    RelationshipDAO tran = mdState.addTransition("PackMachineAAAAAAAAAAAAAAAA", state.getId(), state2.getId());
    tran.apply();
  }

  @Request
  @Test
  public void testDelete()
  {
    // Create a new state
    mdState = MdStateMachineDAO.newInstance();

    mdState.setValue(MdStateMachineInfo.NAME, "DeleteMachine");
    mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Delete Machine Test");
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdState.apply();

    StateMasterDAO state = mdState.addState("PackMachine_AAAAAAAAAAAAAAA", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state.apply();
    Assert.assertTrue(state.isDefaultState());

    StateMasterDAO state2 = mdState.addState("StateB", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state2.apply();

    RelationshipDAO tran = mdState.addTransition("PackMachineAAAAAAAAAAAAAAAA", state.getId(), state2.getId());
    tran.apply();

    String stateType = mdState.definesType();

    mdState.delete();
    mdState = null;

    Assert.assertFalse(MdTypeDAO.isDefined(stateType));

    try
    {
      // The cache should not contain any StateMasterIF of 'stateType'
      // and the type no longer exists in the database. As such an
      // Exception should be thrown.
      Assert.assertEquals("Able to get cached StateMasterIF for instances which no longer exist", 0, mdState.definesStateMasters().size());
    }
    catch (Exception e)
    {
      // This is expected.
    }

    try
    {
      // The cache should not contain any RelationshipDAOIF of 'transitionType'
      // and the type no longer exists in the database. As such an
      // Exception should be thrown.
      Assert.assertEquals("Able to get cached transitions for instances which no longer exist", 0, mdState.definesTransitions().size());
    }
    catch (Exception e)
    {
      // This is expected
    }
  }

  /**
   *
   *
   */
  @Request
  @Test
  public void testCreateMdState()
  {
    // Create a new MdState
    mdState = MdStateMachineDAO.newInstance();

    try
    {
      mdState.setValue(MdStateMachineInfo.NAME, "BookState");
      mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book State");
      mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdState.apply();

      RelationshipDAOIF owner = RelationshipDAO.get(mdBusiness.getId(), mdState.getId(), MdStateMachineInfo.DEFINES_STATE_MACHINE_RELATIONSHIP).get(0);

      Assert.assertFalse(owner == null);
      Assert.assertEquals("BookState", mdState.getValue(MdStateMachineInfo.NAME));
      Assert.assertEquals("test.state", mdState.getValue(MdStateMachineInfo.PACKAGE));
      Assert.assertEquals("Book State", mdState.getStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
      Assert.assertEquals("BookState", mdState.getValue(MdStateMachineInfo.NAME));
      Assert.assertEquals(EntityTypes.STATE_MASTER.getId(), mdState.getValue(MdStateMachineInfo.SUPER_MD_BUSINESS));

      Assert.assertEquals(mdBusiness.definesMdStateMachine(), mdState);
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
    }
  }

  /**
   *
   *
   */
  @Request
  @Test
  public void testAddState()
  {
    // Create a new MdState
    mdState = MdStateMachineDAO.newInstance();

    try
    {
      mdState.setValue(MdStateMachineInfo.NAME, "Food");
      mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Food State");
      mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdState.apply();

      StateMasterDAO state = mdState.addState("Preparing", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state.apply();
      Assert.assertTrue(state.isDefaultState());

      StateMasterDAO state1 = mdState.addState("Eating", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
      state1.apply();
      Assert.assertTrue(state1.isEntryState());

      StateMasterDAO state2 = mdState.addState("Cleaning", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
      state2.apply();
      Assert.assertTrue(!state2.isEntryState());

      List<StateMasterDAOIF> entryStates = mdState.getEntryState();

      Assert.assertEquals(2, entryStates.size());
      Assert.assertTrue(entryStates.contains(state));
      Assert.assertTrue(entryStates.contains(state1));
      Assert.assertEquals(state, mdState.getDefaultState());

      // Check duplicate default Entry states
      mdState.addState("test", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());

      Assert.fail("Duplicate default entry state test");
    }
    catch (StateException e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   *
   *
   */
  @Request
  @Test
  public void testDuplicateMdState()
  {
    // Create a new MdState
    mdState = MdStateMachineDAO.newInstance();

    // Create a new MdState
    mdState2 = MdStateMachineDAO.newInstance();

    // Set the owner relationship
    try
    {
      mdState.setValue(MdStateMachineInfo.NAME, "DupState");
      mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Dupe State");
      mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());

      mdState.apply();

      mdState2.setValue(MdStateMachineInfo.NAME, "Dup2State");
      mdState2.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState2.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Dupe Second State");
      mdState2.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdState2.apply();

      RelationshipDAO.newInstance(mdBusiness.getId(), mdState2.getId(), MdStateMachineInfo.DEFINES_STATE_MACHINE_RELATIONSHIP).apply();

      Assert.fail("Duplicate MdState for an MdBusiness test failed");
    }
    catch (DuplicateDataException e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testTransition()
  {
    // Create a new MdState
    mdState = MdStateMachineDAO.newInstance();

    mdState.setValue(MdStateMachineInfo.NAME, "Star");
    mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdState.apply();

    // Add states
    StateMasterDAO state = mdState.addState("NEBULA", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state.apply();

    StateMasterDAO state1 = mdState.addState("SUNS", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state1.apply();

    StateMasterDAO state2 = mdState.addState("RED_GAINT", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state2.apply();

    RelationshipDAO rel = mdState.addTransition("YEARS", state.getId(), state1.getId());
    rel.apply();

    Assert.assertEquals(rel.getParentId(), state.getId());
    Assert.assertEquals(rel.getChildId(), state1.getId());

    mdState.addTransition("MILLIONS", state1.getId(), state2.getId()).apply();
    mdState.addTransition("Collapse", state2.getId(), state.getId()).apply();

    List<TransitionDAOIF> transitions = mdState.definesTransitions();

    Assert.assertEquals(3, transitions.size());
  }

  /**
   *
   */
  @Request
  @Test
  public void testInvalidTransition()
  {
    // Try to create a transition from a state of an MdState to a different
    // MdState

    // Create a new MdState
    mdState = MdStateMachineDAO.newInstance();
    mdState.setValue(MdStateMachineInfo.NAME, "Simpsons");
    mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdState.apply();

    // Add states
    StateMasterDAO state = mdState.addState("Opening", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state.apply();

    // Create the MdState for the new MdBusiness
    mdState2 = MdStateMachineDAO.newInstance();
    mdState2.setValue(MdStateMachineInfo.NAME, "Reno911");
    mdState2.setValue(MdStateMachineInfo.PACKAGE, "test.state");
    mdState2.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
    mdState2.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness2.getId());
    mdState2.apply();

    // Add states
    StateMasterDAO state2 = mdState2.addState("Closing", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state2.apply();

    try
    {
      mdState.addTransition("Shower", state.getId(), state2.getId()).apply();

      Assert.fail("Invalid transition between two MdState test failed");
    }
    catch (UnexpectedTypeException e)
    {
      // This is expected - the states on are different machines
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testState()
  {
    BusinessDAO test = null;

    try
    {
      // Create a new MdState
      mdState = MdStateMachineDAO.newInstance();
      mdState.setValue(MdStateMachineInfo.NAME, "Blog");
      mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
      mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdState.apply();

      // Add states
      StateMasterDAO state = mdState.addState("Writing", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state.apply();

      StateMasterDAO state1 = mdState.addState("Editing", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
      state1.apply();

      StateMasterDAO state2 = mdState.addState("Reading", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
      state2.apply();

      mdState.addTransition("Written", state.getId(), state1.getId()).apply();
      mdState.addTransition("Edited", state1.getId(), state2.getId()).apply();

      test = BusinessDAO.newInstance(mdBusiness.definesType());
      test.apply();

      Assert.assertEquals(state, test.currentState());
      Assert.assertEquals(mdState.getMdStatus(state).getId(), test.getCurrentStatus().getId());

      test.promote("Written");

      Assert.assertEquals(state1, test.currentState());
      Assert.assertEquals(mdState.getMdStatus(state1).getId(), test.getCurrentStatus().getId());

      test.setEntryState(state2);

      Assert.assertEquals(state2, test.currentState());
      Assert.assertEquals(mdState.getMdStatus(state2).getId(), test.getCurrentStatus().getId());

      test.setEntryState(state1);

      Assert.fail("Set current state to non entry state test failed");
    }
    catch (StateException e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (test != null && !test.isNew())
      {
        test.delete();
      }
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testUnknownTransition()
  {
    BusinessDAO test = null;

    try
    {
      mdState = MdStateMachineDAO.newInstance();

      mdState.setValue(MdStateMachineInfo.NAME, "Clobb");
      mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
      mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdState.apply();

      // Add states
      StateMasterDAO state = mdState.addState("Writ", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state.apply();

      StateMasterDAO state1 = mdState.addState("Whip", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
      state1.apply();

      StateMasterDAO state2 = mdState.addState("Wote", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
      state2.apply();

      mdState.addTransition("Wrung", state.getId(), state1.getId()).apply();
      mdState.addTransition("Dung", state1.getId(), state2.getId()).apply();

      test = BusinessDAO.newInstance(mdBusiness.definesType());
      test.apply();

      Assert.assertEquals(state, test.currentState());

      // Test promote on a transition that does not exist
      try
      {
        test.promote("Invalid");

        Assert.fail("Invalid promote test failed on unknown transition");
      }
      catch (DataNotFoundException e)
      {
        // This is expected
      }

      // Test promot on a transition that does not exit the current state
      try
      {
        test.promote("Dung");

        Assert.fail("Invalid promote test failed on invalid transition");
      }
      catch (DataNotFoundException e)
      {
        // This is expected
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (test != null && !test.isNew())
      {
        test.delete();
      }
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testInvalidPromote()
  {
    // Create a new MdState
    mdState = MdStateMachineDAO.newInstance();
    // Create the MdState for the new MdBusiness
    // Create a new MdState
    mdState2 = MdStateMachineDAO.newInstance();

    BusinessDAO test = null;
    BusinessDAO test2 = null;

    try
    {
      mdState.setValue(MdStateMachineInfo.NAME, "Reon911");
      mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Pimpsons State");
      mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdState.apply();

      // Add states
      StateMasterDAO state = mdState.addState("Opening", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state.apply();

      mdState.addTransition("Replay", state.getId(), state.getId()).apply();

      mdState2.setValue(MdStateMachineInfo.NAME, "Leno911");
      mdState2.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState2.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Leno911 State");
      mdState2.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness2.getId());
      mdState2.apply();

      // Add states
      StateMasterDAO state2 = mdState2.addState("Closing", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state2.apply();

      mdState.addTransition("Shower", state2.getId(), state2.getId()).apply();

      test = BusinessDAO.newInstance(mdBusiness.definesType());
      test.apply();

      test2 = BusinessDAO.newInstance(mdBusiness.definesType());
      test2.apply();

      Assert.assertEquals(state, test.currentState());

      // Promote on a transition which belongs to a different MdState

      test.promote("Shower");

      Assert.fail("A parent of the wrong type was allowed in a relationship");
    }
    catch (UnexpectedTypeException e)
    {
      // This is expected, as the specified parent is the wrong type
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (test != null && !test.isNew())
      {
        test.delete();
      }

      if (test2 != null && !test2.isNew())
      {
        test2.delete();
      }
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testInitializeExisting()
  {
    // Create a new MdState
    mdState = MdStateMachineDAO.newInstance();

    BusinessDAO test = null;
    BusinessDAO test2 = null;

    try
    {
      test = BusinessDAO.newInstance(mdBusiness.definesType());
      test.apply();

      test2 = BusinessDAO.newInstance(mdBusiness.definesType());
      test2.apply();

      mdState.setValue(MdStateMachineInfo.NAME, "Dimpsons");
      mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Dimpsons State");
      mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdState.apply();

      // Add states
      StateMasterDAO state = mdState.addState("Drit", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state.apply();

      StateMasterDAO state1 = mdState.addState("Drite", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
      state1.apply();

      StateMasterDAO state2 = mdState.addState("Drote", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
      state2.apply();

      test2.setEntryState(state2);

      mdState.initializeExistingInstance();

      Assert.assertEquals(state, test.currentState());
      Assert.assertEquals(state2, test2.currentState());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (test != null && !test.isNew())
      {
        test.delete();
      }

      if (test2 != null && !test2.isNew())
      {
        test2.delete();
      }
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testHasState()
  {
    mdState = MdStateMachineDAO.newInstance();

    BusinessDAO businessDAO = null;

    try
    {
      businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
      businessDAO.apply();

      Assert.assertFalse(businessDAO.hasState());

      mdState.setValue(MdStateMachineInfo.NAME, "Rimpsons");
      mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
      mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Rimpsons State");
      mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
      mdState.apply();

      Assert.assertFalse(businessDAO.hasState());

      // Add states
      StateMasterDAO state = mdState.addState("Opening", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
      state.apply();

      Assert.assertFalse(businessDAO.hasState());

      businessDAO.setEntryState(state);

      Assert.assertTrue(businessDAO.hasState());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
    }
  }

  @Request
  @Test
  public void testGetStates()
  {
    mdState = MdStateMachineDAO.newInstance();

    mdState.setValue(MdStateMachineInfo.NAME, "Clobb");
    mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdState.apply();

    // Add states
    StateMasterDAO state = mdState.addState("Writ", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state.apply();

    StateMasterDAO state1 = mdState.addState("Whip", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state1.apply();

    StateMasterDAO state2 = mdState.addState("Wote", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state2.apply();

    List<StateMasterDAOIF> states = mdState.definesStateMasters();

    Assert.assertEquals(3, states.size());
    Assert.assertTrue(states.contains(state));
    Assert.assertTrue(states.contains(state1));
    Assert.assertTrue(states.contains(state2));
  }

  @Request
  @Test
  public void testReservedName()
  {
    // Create a new MdState
    mdState = MdStateMachineDAO.newInstance();
    mdState.setValue(MdStateMachineInfo.NAME, "Blog");
    mdState.setValue(MdStateMachineInfo.PACKAGE, "test.state");
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Star State");
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
    mdState.apply();

    StateMasterDAO state1 = mdState.addState("Editing", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state1.apply();

    StateMasterDAO state2 = mdState.addState("Writing", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state2.apply();

    try
    {
      mdState.addState("Return", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId()).apply();

      Assert.fail("Able to create a state with a reserved word name");
    }
    catch (ReservedWordException e)
    {
      // Expected
    }

    try
    {
      mdState.addTransition("Return", state1.getId(), state2.getId()).apply();

      Assert.fail("Able to create a transition with a reserved word name");
    }
    catch (ReservedWordException e)
    {
      // Expected.
    }
  }
}
