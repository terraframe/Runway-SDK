/*
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
/**
 * This test suite tests RunwaySDK_Core.js
 * 
 * @author Terraframe
 */

//define(["./TestFramework"], function(TestFramework){
(function(){

var Y = YUI().use("*");
var SUITE_NAME = "RunwaySDK_Core";

TestFramework.newSuite(SUITE_NAME);

TestFramework.defineSuiteSetUp(SUITE_NAME, function ()
{
  Mojo.Meta.newClass(TestFramework.PACKAGE + 'GrandParent', {
		
		Instance: {	
		
			fReturnString: function() { return "string"; },
			
			fTestOverride: function() { return "GrandParent"; },
			
			fReturnArgument: function(arg) { return arg; },
			
			multiSuperTest : function(x)
      {
        return this.multiSuperTest2(x + 1);
      },
      
      skipSuper : function()
      {
        return 3;
      }
			
		},
	
		Static: {
			
			fStaticReturnString: function () { return "string"; }
		},
		
		Constants: {
			
			ONE: "GrandParent one"
			
		}
	
	});
	
	Mojo.Meta.newClass(TestFramework.PACKAGE + 'Parent', {
		
    Extends: TestFramework.PACKAGE + "GrandParent",
    
		IsAbstract: true,
		
		Instance: {
			
			fTestSuper: function () { return "GrandParent string"; },
			
			fAbstract: { IsAbstract: true },
			
			fTestOverride: function() { return "Parent"; },
			
			getId : function(){
			  return this.getHashCode();
			},
			
			multiSuperTest : function(x)
			{
			  return this.$multiSuperTest(x + 1);
			},
			
			multiSuperTest2 : function(x)
      {
        return this.multiSuperTest3(x + 1);
      },
			
		},
		
		Static: {
			
			fStaticReturnString: function () { return "override string" }
		
		},
		
		Constants: {
			
			ZERO: "zero",
			ONE: "one",
			TWO: "two",
			THREE: "three"
			
		}
		
	});
	
	Mojo.Meta.newClass(TestFramework.PACKAGE + 'Child', {
		
    Extends: TestFramework.PACKAGE + "Parent",
    
		Implements : com.runwaysdk.Serializable,

		Instance: {
		
			fTestSuper: function(shouldSuper)
			{
				if (shouldSuper)
					return this.$fTestSuper();
				else
					return "Parent string";
			},
			
			multiSuperTest : function()
			{
			  return this.$multiSuperTest(1);
			},
			
			multiSuperTest2 : function(x)
			{
			  return this.$multiSuperTest2(x + 1);
			},
			
			multiSuperTest3 : function(x)
			{
			  return x + 1;
			},
			
			skipSuper : function()
			{
			  return this.$skipSuper();
			},
			
			fAbstract: function () { },
			
			toJSON : function(key)
      {
        return new com.runwaysdk.StandardSerializer(this).toJSON(key);
      }
			
		}
		
		
	});
	
	Mojo.Meta.newClass(TestFramework.PACKAGE + 'GrandChild', {
		
    Extends: TestFramework.PACKAGE + "Child",
    
		IsSingleton: true
		
	});
	
});

TestFramework.newTestCase(SUITE_NAME, {
 
	name: "ObjectOrientedTests",
	
	_should: {
		error: {
      testUnknownClassProperty : com.runwaysdk.Exception,
			testInstantiateAbstractClass: com.runwaysdk.Exception,
			testInstantiateSingleton: com.runwaysdk.Exception,
			testCreateConcreteClassWithAbstractMethod: com.runwaysdk.Exception,
			testCreateClassWithUnimplementedAbstractMethod: com.runwaysdk.Exception,
			testCreateClassWithStaticAbstractMethod: com.runwaysdk.Exception,
			testInvokeAbstractMethod: com.runwaysdk.Exception,
      testExtendUndefinedClass: com.runwaysdk.Exception,
      testImplementUndefinedInterface: com.runwaysdk.Exception
		}
	},
	
	testUnknownClassProperty : function()
	{
	  Mojo.Meta.newClass(TestFramework.PACKAGE+'BadClass', {
	    
	    NotALegitProperty:true,
	    
	    Instance : {
	      initialize : function(){}
	    }
	    
	  });
	},
	
	testBasicClass : function()
	{
	  var bcRef = Mojo.Meta.newClass(TestFramework.PACKAGE+'BasicClass');
	  
	  var bc = Mojo.Meta.newInstance(TestFramework.PACKAGE + "BasicClass");
	  Y.Assert.areEqual(bc.constructor, bcRef);
	},
	
	/**
	 * The term "inner class" is loosely defined here to mean a class that
	 * is appended to another class (i.e, a Function).
	 */
	testInnerClass : function()
	{
	  var outer = Mojo.Meta.newClass(TestFramework.PACKAGE+'OuterClass');
	  var inner = Mojo.Meta.newClass(outer.getMetaClass().getQualifiedName()+'.InnerClass');
	  
	  var o = Mojo.Meta.newInstance(outer.getMetaClass().getQualifiedName());
	  var i = Mojo.Meta.newInstance(inner.getMetaClass().getQualifiedName());

	  Y.Assert.areEqual(o.constructor, outer);
	  Y.Assert.areEqual(i.constructor, inner);
	},
	
	testInheritance: function ()
	{
		var grandChild = Mojo.Meta.findClass(TestFramework.PACKAGE + "GrandChild").getInstance();
		
		Y.assert( grandChild instanceof Mojo.Meta.findClass(TestFramework.PACKAGE + "Child"), "Single level inheritance failed." );
		Y.assert( grandChild instanceof Mojo.Meta.findClass(TestFramework.PACKAGE + "Parent"), "Second level inheritance failed." );
		Y.assert( grandChild instanceof Mojo.Meta.findClass(TestFramework.PACKAGE + "GrandParent"), "Third level inheritance failed." );
		Y.Assert.areEqual( "string", grandChild.fReturnString(), "Third level inheritance instance method returned incorrect value." );
	},
	
	testSuperingUp: function ()
	{
		var child = Mojo.Meta.newInstance(TestFramework.PACKAGE + "Child");
		
		Y.Assert.areEqual( "Parent string", child.fTestSuper(false), "The supering function did not return the correct value." );
		Y.Assert.areEqual( "GrandParent string", child.fTestSuper(true), "The supering function did not return the correct value." );
	},
	
	testMultiSuper : function ()
  {
    var child = Mojo.Meta.newInstance(TestFramework.PACKAGE + "Child");
    
    Y.Assert.areEqual( 6, child.multiSuperTest(), "The supering function did not return the correct value." );
  },
  
  testSkipSuper : function()
  {
    var child = Mojo.Meta.newInstance(TestFramework.PACKAGE + "Child");
    
    Y.Assert.areEqual( 3, child.skipSuper(), "The supering function did not return the correct value." );
  },
	
	testInstantiateAbstractClass: function ()
	{
		var parent = Mojo.Meta.newInstance(TestFramework.PACKAGE + "Parent");
	},
	
	testInstantiateSingleton: function ()
	{
		var parent1 = Mojo.Meta.newInstance(TestFramework.PACKAGE + "GrandChild");
	},
	
	testInvokeAbstractMethod: function ()
	{
		var c = Mojo.Meta.newInstance(TestFramework.PACKAGE + "Child");
		Mojo.Meta.findClass(TestFramework.PACKAGE+"Parent").prototype.fAbstract.call(c);
	},
  
	testCreateConcreteClassWithAbstractMethod: function ()
	{
		Mojo.Meta.newClass(TestFramework.PACKAGE + 'ConcreteClassWithAbstractMethod', {
			
			Instance: {
				fAbstract: { IsAbstract: true }
			}
		
		});
	},
  
  testBadClassProperlyDropped : function() {
    try {
      // This should fail
      Mojo.Meta.newClass(TestFramework.PACKAGE + 'ConcreteClassWithAbstractMethod', {
      
        Instance: {
          fAbstract: {
            IsAbstract: true
          }
        }
      
      });
    }
    catch (e) {
      Y.Assert.areEqual(undefined, Mojo.Meta.findClass(TestFramework.PACKAGE + 'ConcreteClassWithAbstractMethod'), "The class still exists on the private _classes hash map.");
      Y.Assert.areEqual(undefined, Mojo.Meta.findClass(TestFramework.PACKAGE + 'ConcreteClassWithAbstractMethod', Mojo.GLOBAL), "The class still exists on the global namespace.");
      Y.Assert.areEqual(undefined, Mojo.Meta.findClass(TestFramework.PACKAGE + 'ConcreteClassWithAbstractMethod', Mojo.$), "The class still exists on the Mojo.$ namespace.");
      return;
    }
    Y.Assert.fail("Attempting to create a bad class did not throw an exception when it should have.");
  },
	
	testCreateClassWithUnimplementedAbstractMethod: function ()
	{
		Mojo.Meta.newClass(TestFramework.PACKAGE + 'ClassWithUnimplementedAbstractMethod', {
				
			Static: { fStaticAbstract: function () { } },
				
			Extends: TestFramework.PACKAGE + "Parent"
				
		});
	},
	
	testUniquenessOfSingletonGetInstanceMethod: function ()
	{
		Y.assert( Mojo.Meta.findClass(TestFramework.PACKAGE + "GrandChild").getInstance().equals( Mojo.Meta.findClass(TestFramework.PACKAGE + "GrandChild").getInstance() ), "GrandChild.getInstance was called twice and the returned objects were not equal to eachother" );
	},
  
  testExtendUndefinedClass : function() {
    Mojo.Meta.newClass(TestFramework.PACKAGE+'BadClass', {
      
      Extends: undefined,
      
      Instance : {
        initialize : function(){}
      }
      
    });
  },
  
  testImplementUndefinedInterface : function() {
    Mojo.Meta.newClass(TestFramework.PACKAGE+'BadClass', {
      
      Implements: undefined,
      
      Instance : {
        initialize : function(){}
      }
      
    });
  }
});

TestFramework.newTestCase(SUITE_NAME, {
  
  name : "InterfaceTests",
  
  _should: {
    error: {
      testInterfaceEnformentNeg : com.runwaysdk.Exception,
      testInterfaceInheritanceNeg : com.runwaysdk.Exception,
      testInterfaceEnforcementWithClassInheritanceNeg : com.runwaysdk.Exception,
      testAnonymousInnerClassNeg : com.runwaysdk.Exception,
      testComplexAnonymousInnerClassNeg : com.runwaysdk.Exception
    }
  },
  
  testNewInterface : function()
  {
    var klass = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'SomeInterface', {
      Constants : {
        IF_CONSTANT1: 'one',
        IF_CONSTANT2: 'two'
      },
      
      Instance : {
        foo1 : function(){},
        foo2 : function(){}
      }
    });
    
    Y.Assert.isTrue(klass.getMetaClass().isInterface());
    var methods = klass.getMetaClass().getInstanceMethods(true);
    Y.Assert.isTrue('foo1' in methods);
    Y.Assert.isTrue('foo2' in methods);
    
    var constants = klass.getMetaClass().getConstants(true);
    Y.Assert.isTrue('IF_CONSTANT1' in constants);
    Y.Assert.isTrue('IF_CONSTANT2' in constants);
    
    Y.Assert.areEqual(klass.IF_CONSTANT1, 'one');
    Y.Assert.areEqual(klass.IF_CONSTANT2, 'two');
  },
  
  testInterfaceInheritance : function()
  {
    var level1 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'Level1IF', {
      Constants : {
        LEVEL_1: 'level1',
      },
      
      Instance : {
        l1 : function(){}
      }
    });
    
    var level2 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'Level2IF', {
      
      Extends : level1,
      
      Constants : {
        LEVEL_2: 'level2',
      },
      
      Instance : {
        l2 : function(){}
      }
    });
    
    var level3 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'Level3IF', {

      Extends : level2,
      
      Constants : {
        LEVEL_3: 'level3',
      },
      
      Instance : {
        l3 : function(){}
      }
    });
    
    Y.Assert.isTrue(level3.getMetaClass().isSubClassOf(level2));
    Y.Assert.isTrue(level3.getMetaClass().isSubClassOf(level1));
    
    var methods = level3.getMetaClass().getInstanceMethods(true);
    Y.Assert.isTrue('l3' in methods);
    Y.Assert.isTrue('l2' in methods);
    Y.Assert.isTrue('l1' in methods);
    
    var constants = level3.getMetaClass().getConstants(true);
    Y.Assert.isTrue('LEVEL_3' in constants);
    Y.Assert.isTrue('LEVEL_2' in constants);
    Y.Assert.isTrue('LEVEL_1' in constants);
  },
  
  testInterfaceEnformentPos : function()
  {
    var IF = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestPosIF', {
      
      Instance : {
        foo1 : function(){},
        foo2 : function(){}
      }
    });
    
    var posClass = Mojo.Meta.newClass(TestFramework.PACKAGE + 'PosClass', {
      
      Implements : IF,
      
      Instance : {
        foo1 : function(){},
        foo2 : function(){}
      }
    });
    
    var pos = new posClass();
    Y.Assert.isTrue(IF.getMetaClass().isInstance(pos));
    Y.Assert.isTrue(pos.getMetaClass().doesImplement(IF));
  },
  
  testInterfaceEnformentNeg : function()
  {
    var IF = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestNegIF', {
      
      Instance : {
      foo1 : function(){},
      foo2 : function(){}
    }
    });
    
    var negClass = Mojo.Meta.newClass(TestFramework.PACKAGE + 'NegClass', {
      
      Implements : IF,
      
      Instance : {
      foo1 : function(){}
      // ERROR: missing foo2() declaration
    }
    });
  },
  
  testInterfaceInheritancePos : function()
  {
    var IF1 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestPosIF1', {
      
      Instance : {
        foo1 : function(){}
      }
    });

    var IF2 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestPosIF2', {
      Extends : IF1,
      Instance : {
      foo2 : function(){}
    }
    });
    
    var posClass = Mojo.Meta.newClass(TestFramework.PACKAGE + 'PosClass', {
      
      Implements : IF2,
      
      Instance : {
        foo1 : function(){},
        foo2 : function(){}
      }
    });
    
    var pos = new posClass();
    Y.Assert.isTrue(IF1.getMetaClass().isInstance(pos));
    Y.Assert.isTrue(IF2.getMetaClass().isInstance(pos));
    Y.Assert.isTrue(pos.getMetaClass().doesImplement(IF1));
    Y.Assert.isTrue(pos.getMetaClass().doesImplement(IF2));
  },
  
  testInterfaceInheritanceNeg : function()
  {
    var IF1 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestNegIF1', {
      
      Instance : {
      foo1 : function(){}
    }
    });
    
    var IF2 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestNegIF2', {
      Extends : IF1,
      Instance : {
      foo2 : function(){}
    }
    });
    
    var negClass = Mojo.Meta.newClass(TestFramework.PACKAGE + 'NegClass', {
      
      Implements : IF2,
      
      Instance : {
      // ERROR: does not declare foo1()
      foo2 : function(){}
    }
    });
  },
  
  testDontEnforceOnAbstractClass : function()
  {
    var IF = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestPosIF', {
    
    Instance : {
      foo1 : function(){},
      foo2 : function(){}
    }
    });
  
    var klass = Mojo.Meta.newClass(TestFramework.PACKAGE + 'SomeAbstractClass', {
    Implements : IF,
    IsAbstract : true,
    Instance : {
      bar : { IsAbstract: true }
    }
    });
  
    Y.Assert.isTrue(klass.getMetaClass().doesImplement(IF));    
  },
  
  testInterfaceEnforcementWithClassInheritancePos : function()
  {
    var IF = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestPosIF', {
      
      Instance : {
        foo1 : function(){},
        foo2 : function(){}
      }
    });
    
    var posClass1 = Mojo.Meta.newClass(TestFramework.PACKAGE + 'PosClass1', {
      IsAbstract: true,
      Implements : IF,
      
      Instance : {
        foo1 : function(){}
      }
    });

    var posClass2 = Mojo.Meta.newClass(TestFramework.PACKAGE + 'PosClass2', {
      
      Extends : posClass1,
      Instance : {
      foo2 : function(){}
    }
    });
    
    Y.Assert.isTrue(posClass2.getMetaClass().doesImplement(IF));
    Y.Assert.isTrue(posClass1.getMetaClass().doesImplement(IF));
  },

  testInterfaceEnforcementWithClassInheritanceNeg : function()
  {
    var IF = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestNegIF', {
      
      Instance : {
      foo1 : function(){},
      foo2 : function(){}
    }
    });
    
    var negClass1 = Mojo.Meta.newClass(TestFramework.PACKAGE + 'NegClass1', {
      IsAbstract: true,
      Implements : IF,
      
      Instance : {
      foo1 : function(){}
    }
    });
    
    var negClass2 = Mojo.Meta.newClass(TestFramework.PACKAGE + 'NegClass2', {
      
      Extends : negClass1,
      Instance : {
      // ERROR: does not implement foo2()
    }
    });
  },
  
  testInterfaceComplexInheritancePos : function()
  {
    var IF1 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestPosComplexIF1', {
      
      Instance : {
        foo1 : function(){}
      }
    });
    
    var IF2 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestPosComplexIF2', {
      Extends: IF1,
      Instance : {
      foo2 : function(){}
    }
    });
    
    var IF3 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'TestPosComplexIF3', {
      Extends: IF2,
      Instance : {
      foo3 : function(){}
    }
    });
    
    var complex1 = Mojo.Meta.newClass(TestFramework.PACKAGE + 'Complex1', {
      IsAbstract: true,
      Implements : IF2,
      
      Instance : {
        foo1 : function(){} // satisfies IF1
      }
    });
    
    var complex2 = Mojo.Meta.newClass(TestFramework.PACKAGE + 'Complex2', {
      Extends: complex1,      
      Instance : {
      foo2 : function(){} // satisfies IF2
    }
    });

    var complex3 = Mojo.Meta.newClass(TestFramework.PACKAGE + 'Complex3', {
      Implements : IF3,
      Extends: complex2,
      Instance : {
      foo3 : function(){} // satifies IF3
    }
    });

    var c2 = new complex2();
    Y.Assert.isTrue(c2.getMetaClass().doesImplement(IF1));
    Y.Assert.isTrue(c2.getMetaClass().doesImplement(IF2));
    Y.Assert.isFalse(c2.getMetaClass().doesImplement(IF3));
    
    Y.Assert.isTrue(IF1.getMetaClass().isInstance(c2));
    Y.Assert.isTrue(IF2.getMetaClass().isInstance(c2));
    Y.Assert.isFalse(IF3.getMetaClass().isInstance(c2));
    
    var c3 = new complex3();
    Y.Assert.isTrue(c3.getMetaClass().doesImplement(IF1));
    Y.Assert.isTrue(c3.getMetaClass().doesImplement(IF2));
    Y.Assert.isTrue(c3.getMetaClass().doesImplement(IF3));
    
    Y.Assert.isTrue(IF1.getMetaClass().isInstance(c3));
    Y.Assert.isTrue(IF2.getMetaClass().isInstance(c3));
    Y.Assert.isTrue(IF3.getMetaClass().isInstance(c3));
  },

  testAnonymousInnerClassPos : function()
  {
    var IF1 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'AnonPosIF', {
      
      Instance : {
        foo : function(){}
      }
    });
    
    var if1 = new IF1({
      value : 3,
      foo : function(){ return this.value; }
    });
    
    Y.Assert.areEqual(3, if1.foo());
    Y.Assert.isTrue(if1 instanceof IF1);
    Y.Assert.isTrue(IF1.getMetaClass().isInstance(if1));
    Y.Assert.isTrue(if1.getMetaClass().doesImplement(IF1));
  },
  
  testAnonymousInnerClassNeg : function()
  {
    var IF1 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'AnonNegIF', {
      
      Instance : {
        foo : function(){}
      }
    });
    
    var if1 = new IF1({
      value : 3,
      // ERROR: no foo() method
    });
  },
  
  testComplexAnonymousInnerClassPos : function()
  {
    var IF1 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'ComplexAnonPosIF1', {
      
      Instance : {
        foo1 : function(){}
      }
    });
    
    var IF2 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'ComplexAnonPosIF2', {
      Extends: IF1,
      Instance : {
      foo2 : function(){}
    }
    });
    
    var IF3 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'ComplexAnonPosIF3', {
      Extends: IF2,
      Instance : {
      foo3 : function(){}
    }
    });
    
    var if3 = new IF3({
      value1 : 1,
      value2 : 2,
      value3 : 3,
      foo1 : function(){ return this.value1; },
      foo2 : function(){ return this.value2; },
      foo3 : function(){ return this.value3; }
    });
    
    Y.Assert.areEqual(1, if3.foo1());
    Y.Assert.areEqual(2, if3.foo2());
    Y.Assert.areEqual(3, if3.foo3());
    
    Y.Assert.isTrue(if3 instanceof IF1);
    Y.Assert.isTrue(if3 instanceof IF2);
    Y.Assert.isTrue(if3 instanceof IF3);
    
    Y.Assert.isTrue(IF1.getMetaClass().isInstance(if3));
    Y.Assert.isTrue(IF2.getMetaClass().isInstance(if3));
    Y.Assert.isTrue(IF3.getMetaClass().isInstance(if3));
    
    Y.Assert.isTrue(if3.getMetaClass().doesImplement(IF1));
    Y.Assert.isTrue(if3.getMetaClass().doesImplement(IF2));
    Y.Assert.isTrue(if3.getMetaClass().doesImplement(IF3));
  },
  
  testComplexAnonymousInnerClassNeg : function()
  {
    var IF1 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'ComplexAnonNegIF1', {
      
      Instance : {
        foo1 : function(){}
      }
    });
    
    var IF2 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'ComplexAnonNegIF2', {
      Extends: IF1,
      Instance : {
      foo2 : function(){}
    }
    });
    
    var IF3 = Mojo.Meta.newInterface(TestFramework.PACKAGE + 'ComplexAnonNegIF3', {
      Extends: IF2,
      Instance : {
      foo3 : function(){}
    }
    });
    
    var if3 = new IF3({
      value1 : 1,
      value2 : 2,
      value3 : 3,
      foo1 : function(){ return this.value1; },
//      foo2 : function(){ return this.value2; }, ERROR: omits foo2() method
      foo3 : function(){ return this.value3; }
    });
  }
  
});

TestFramework.newTestCase(SUITE_NAME, {
	
	name: "MetaDataTests",
	
	meta: null,
	
	_should: {
		error: {
			
		}
	},
	
	caseSetUp: function ()
	{
		this.meta = Mojo.Meta.findClass(TestFramework.PACKAGE + "Parent").getMetaClass();
	},
	
	testAlias: function ()
	{
		var didPass = true;
		try {
			Mojo.Meta.alias("com.runwaysdk.*", window);
			didPass = false;
		} catch (e) {}
		if (!didPass)
			Y.Assert.fail("Alias allowed appending to DOM window");
		
		try {
			Mojo.Meta.alias("com.runwaysdk.*.failtown");
			didPass = false;
		} catch (e) {}
		if (!didPass)
			Y.Assert.fail("Invalid package name aliased.");
		
		var obj = Mojo.Meta.alias(TestFramework.PACKAGE+"*");
		var grandChild = obj.GrandChild.getInstance();
		Y.Assert.areEqual( "string", grandChild.fReturnString(), "Unable to reference aliased class." );
		
		var obj2 = Mojo.Meta.alias("com.*");
		Y.Assert.areEqual( undefined, obj2.runwaysdk, "Alias included subpackages that it should not have." );
	},
	
	testIsSingleton: function ()
	{
		Y.Assert.areEqual( false, this.meta.isSingleton(), "isSingleton meta data is incorrect on class Parent." );
	},
	
	testHasConstant: function ()
	{
		Y.Assert.areEqual( false, this.meta.hasConstant(), "hasConstant meta data is incorrect on class Parent" );
	},
	
	testGetMethod: function ()
	{
		Y.Assert.areEqual( "string", this.meta.getMethod("fReturnString").getMethod()(), "getMethod meta data is incorrect on class Parent" );
	},
	
	testHasInstanceMethod: function ()
	{
		Y.Assert.areEqual( true, this.meta.hasInstanceMethod("fReturnString"), "hasInstanceMethod meta data is incorrect on class Parent" );
		Y.Assert.areEqual( true, this.meta.hasInstanceMethod("fAbstract"), "hasInstanceMethod meta data is incorrect on class Parent. It doesn't recognize an abstract method as a method." );
	},
	
	testGetInstanceMethod: function ()
	{
		Y.Assert.areEqual( "string", this.meta.getInstanceMethod("fReturnString").getMethod()(), "getInstanceMethod meta data is incorrect on class Parent" );
	},
	
	testHasStaticMethod: function ()
	{
		Y.Assert.areEqual( true, this.meta.hasStaticMethod("fStaticReturnString"), "hasStaticMethod meta data is incorrect on class Parent" );
	},
	
	testGetStaticMethod: function ()
	{
		Y.Assert.areEqual( "override string", this.meta.getStaticMethod("fStaticReturnString").getMethod()(), "getStaticMethod meta data is incorrect on class Parent" );
	},
	
	testIsAbstract: function ()
	{
		Y.Assert.areEqual( true, this.meta.isAbstract(), "isAbstract meta data is incorrect on class Parent" );
	},
	
	testGetPackage: function ()
	{
		Y.Assert.areEqual( TestFramework.PACKAGE, this.meta.getPackage()+".", "getPackage meta data is incorrect on class Parent" );
	},
	
	testGetName: function ()
	{
		Y.Assert.areEqual( "Parent", this.meta.getName(), "getName meta data is incorrect on class Parent" );
	},
	
	testGetQualifiedName: function ()
	{
		Y.Assert.areEqual( TestFramework.PACKAGE + "Parent", this.meta.getQualifiedName(), "getQualifiedName meta data is incorrect on class Parent" );
	},
	
	testIsSuperClassOf: function ()
	{
		Y.Assert.areEqual( true, this.meta.isSuperClassOf(TestFramework.PACKAGE + "Child"), "isSuperClassOf meta data is incorrect on class Parent" );
	},
	
	testIsSubClassOf: function ()
	{
		Y.Assert.areEqual( true, this.meta.isSubClassOf(TestFramework.PACKAGE + "GrandParent"), "isSubClassOf meta data is incorrect on class Parent" );
	},
	
	testGetSuperClass: function ()
	{
		Y.Assert.areEqual( "string", this.meta.getSuperClass().fStaticReturnString(), "getSuperClass meta data is incorrect on class Parent" );
	},
	
	testIsInstance : function()
	{
	  var obj = Mojo.Meta.newInstance(TestFramework.PACKAGE + "Child");
	  Y.Assert.isTrue(this.meta.isInstance(obj));
	},
	
	testGetFunction : function()
	{
	  var klass = Mojo.Meta.findClass(TestFramework.PACKAGE + "Child");
	  Y.Assert.areEqual(klass, klass.getMetaClass().getFunction());
	},
	
	/*
	testIsNative: function ()
	{
		Y.Assert.areEqual( false, this.meta.isNative(), "isNative meta data is incorrect on class Parent" );
	},
	*/
	
	testGetMethods: function ()
	{
		var methods = this.meta.getMethods();
		var obj;
		
		for (key in methods)
		{
			obj = methods[key];
			
//			if (obj.getDefiningClass().getMetaClass().isNative())
//			{
//				Y.Assert.areEqual( "Base", obj.getDefiningClass().getMetaClass().getName(), "getMethods meta data is incorrect on native method by the name of " + obj.getDefiningClass().getMetaClass().getName() );
//			}
//			else
//			{
				Y.Assert.areEqual( true, obj.getDefiningClass().getMetaClass().isSuperClassOf(TestFramework.PACKAGE + "GrandChild"), "getMethods meta data is incorrect on non-native method by the name of " + obj.getDefiningClass().getMetaClass().getName() );
//			}
		}
	},
	
	testGetInstanceMethods: function ()
	{
		var methods = this.meta.getInstanceMethods();
		var obj;
		
		for (key in methods)
		{
			obj = methods[key];
			
//			if (false && obj.getDefiningClass().getMetaClass().isNative())
//			{
//				Y.Assert.areEqual( "Base", obj.getDefiningClass().getMetaClass().getName(), "getInstanceMethods meta data is incorrect on native method by the name of " + obj.getDefiningClass().getMetaClass().getName() );
//			}
//			else
//			{
				Y.Assert.areEqual( true, obj.getDefiningClass().getMetaClass().isSuperClassOf(TestFramework.PACKAGE + "GrandChild"), "getInstanceMethods meta data is incorrect on non-native method by the name of " + obj.getDefiningClass().getMetaClass().getName() );
//			}
		}
	},
	
	testGetStaticMethods: function ()
	{
		var methods = this.meta.getStaticMethods();
		var obj;
		
		for (key in methods)
		{
			obj = methods[key];
			
//			if (obj.getDefiningClass().getMetaClass().isNative())
//			{
//				Y.Assert.areEqual( "Base", obj.getDefiningClass().getMetaClass().getName(), "getStaticMethods meta data is incorrect on native method by the name of " + obj.getDefiningClass().getMetaClass().getName() );
//			}
//			else
//			{
			
				Y.Assert.areEqual( true, obj.getDefiningClass().getMetaClass().isSuperClassOf(TestFramework.PACKAGE + "GrandChild"), "getStaticMethods meta data is incorrect on non-native method by the name of " + obj.getDefiningClass().getMetaClass().getName() );
//			}
		}
	},
	
	testGetSubClasses: function ()
	{
		Y.Assert.areEqual( "Child", this.meta.getSubClasses()[0].getMetaClass().getName(), "getSubClasses meta data is incorrect on class Parent.");
	},
	
	testGetConstants: function ()
	{
		var constants = this.meta.getConstants();
		
		for (k in constants)
		{
			var v = constants[k];
			
			Y.Assert.areEqual( v.getName().toLowerCase(), v.getValue(), "getConstants meta data is incorrect on class Parent." );
		}
	},
	
	testGetConstant: function ()
	{
		Y.Assert.areEqual( "one", this.meta.getConstant("ONE").getValue(), "getConstant meta data is incorrect on class Parent." );
	},
	
	testHasConstant: function ()
	{
		Y.Assert.areEqual( true, this.meta.hasConstant("ONE"), "hasConstant meta data is incorrect on class Parent." );
	}
	
});

TestFramework.newTestCase(SUITE_NAME, {
	
	name: "MethodObjectTests",
	
	meta: null,
	
	caseSetUp: function()
	{
		this.meta = Mojo.Meta.findClass(TestFramework.PACKAGE + "Parent").getMetaClass();
	},
	
	testIsAbstract: function()
	{
		Y.Assert.areEqual( true, this.meta.getMethod("fAbstract").isAbstract(), "isAbstract on method fAbstract returned an incorrect boolean value." );
		Y.Assert.areEqual( false, this.meta.getMethod("fReturnString").isAbstract(), "isAbstract on method fReturnString returned an incorrect boolean value." );
	},
	
	testIsConstructor: function ()
	{
		Y.Assert.areEqual( true, this.meta.getMethod("initialize").isConstructor(), "isConstructor on method Parent returned an incorrect boolean value." );
		Y.Assert.areEqual( false, this.meta.getMethod("fReturnString").isConstructor(), "isConstructor on method Parent returned an incorrect boolean value." );
	},
	
	testGetArity: function ()
	{
		Y.Assert.areEqual( 0, this.meta.getMethod("fReturnString").getArity(), "getArity on method fReturnString returned an incorrect integer value." );
		Y.Assert.areEqual( 1, this.meta.getMethod("fReturnArgument").getArity(), "getArity on method fReturnArgument returned an incorrect integer value." );
	},
	
	testIsOverride: function ()
	{
		Y.Assert.areEqual( true, this.meta.getMethod("fTestOverride").isOverride(), "isOverride on method fTestOverride returned an incorrect boolean value." );
		Y.Assert.areEqual( false, this.meta.getMethod("fStaticReturnString").isOverride(), "isOverride on method fTestOverride returned an incorrect boolean value." );
	},
	
	testIsHiding: function ()
	{
		Y.Assert.areEqual( true, this.meta.getMethod("fStaticReturnString").isHiding(), "isHiding on method fStaticReturnString returned an incorrect boolean value." );
		Y.Assert.areEqual( false, this.meta.getMethod("fReturnString").isHiding(), "isHiding on method fReturnString returned an incorrect boolean value." );
	},
	
	testGetOverrideClass: function ()
	{
		Y.Assert.areEqual( this.meta.getName(), this.meta.getMethod("fTestOverride").getOverrideClass().getMetaClass().getName(), "getOverrideClass on method fTestOverride returned an incorrect class." );
	},
	
	testGetMethod: function ()
	{
		Y.Assert.areEqual( "string", this.meta.getMethod("fReturnString").getMethod()(), "getMethod on method fReturnString returned an incorrect method." );
	},
	
	testGetName: function ()
	{
		Y.Assert.areEqual( "fReturnString", this.meta.getMethod("fReturnString").getName(), "getName on method fReturnString returned an incorrect name." );
	},
	
	testIsStatic: function ()
	{
		Y.Assert.areEqual( true, this.meta.getMethod("fStaticReturnString").isStatic(), "isStatic on method fStaticReturnString returned an incorrect boolean value" );
	},
	
	testGetDefiningClass: function ()
	{
		Y.Assert.areEqual( "GrandParent", this.meta.getMethod("fTestOverride").getDefiningClass().getMetaClass().getName(), "getDefiningClass on method fTestOverride returned an incorrect class." );
	},
	
	testEquals: function ()
	{
		Y.Assert.areEqual( true, this.meta.getMethod("fReturnString").equals(this.meta.getMethod("fReturnString")), "getMethod on method fReturnString returned an incorrect boolean value." );
		Y.Assert.areEqual( false, this.meta.getMethod("fReturnString").equals(this.meta.getMethod("fStaticReturnString")), "getMethod on method fReturnString returned an incorrect boolean value." );
	}
	
});

TestFramework.newTestCase(SUITE_NAME, {
	
	name: "ConstantObjectTests",
	
	meta: null,
	
	caseSetUp: function ()
	{
		this.meta = Mojo.Meta.findClass(TestFramework.PACKAGE + "Parent").getMetaClass();
	},
	
	testGetName: function ()
	{
		Y.Assert.areEqual( "ONE", this.meta.getConstant("ONE").getName(), "getName on constant ONE returned an incorrect name." );
	},
	
	testGetValue: function ()
	{
		Y.Assert.areEqual( "one", this.meta.getConstant("ONE").getValue(), "getValue on constant ONE returned an incorrect value.");
	},
	
	testGetDefiningClass: function ()
	{
		Y.Assert.areEqual( "GrandParent", this.meta.getConstant("ONE").getDefiningClass().getMetaClass().getName(), "getDefiningClass on constant ONE returned an incorrect value." );
		Y.Assert.areEqual( "Parent", this.meta.getConstant("TWO").getDefiningClass().getMetaClass().getName(), "getDefiningClass on constant ONE returned an incorrect value." );
	},
	
	testIsOverride: function ()
	{
		Y.Assert.areEqual( true, this.meta.getConstant("ONE").isOverride(), "isOverride on constant ONE returned an incorrect boolean value." );
		Y.Assert.areEqual( false, this.meta.getConstant("TWO").isOverride(), "isOverride on constant TWO returned an incorrect boolean value." );
	},
	
	testGetOverrideClass: function ()
	{
		Y.Assert.areEqual( "Parent", this.meta.getConstant("ONE").getOverrideClass().getMetaClass().getName(), "getOverrideClass on constant ONE returned an incorrect class." );
	},
	
	testEquals: function ()
	{
		Y.Assert.areEqual( true, this.meta.getConstant("ONE").equals(this.meta.getConstant("ONE")), "equals on constant ONE returned an incorrect boolean value." );
		Y.Assert.areEqual( false, this.meta.getConstant("ONE").equals(this.meta.getConstant("TWO")), "equals on constant ONE returned an incorrect boolean value." );
	}
	
});

TestFramework.newTestCase(SUITE_NAME, {
	
	name: "JSONTests",
  
  setUp : function ()
  {
    this.oldValue = Mojo.ClientSession.isNativeParsingEnabled();
    Mojo.ClientSession.setNativeParsingEnabled(true);
  },
  
  tearDown : function ()
  {
    Mojo.ClientSession.setNativeParsingEnabled(this.oldValue);
  },
	
	caseSetUp : function()
	{
    this.child = Mojo.Meta.newInstance(TestFramework.PACKAGE + "Child");
    this.child.foo = "bar";
    this.child.whiteNoise = {x:1,arr:[1]};
	},
	
	caseTearDown : function()
	{
    delete this.child;
	},
	
	testNativeToJSON : function()
	{
	  Y.Assert.isTrue(Mojo.SUPPORTS_NATIVE_PARSING, "Unable to test native parsing, the browser does not support it.");
	  Mojo.ClientSession.setNativeParsingEnabled(true);
    
    this.doToJSON(true);
	},
	
	testNonNativeToJSON: function()
  {
    Mojo.ClientSession.setNativeParsingEnabled(false);
    
   this.doToJSON(false);
  },
  
  doToJSON: function (isNative)
  {
    Y.Assert.areEqual(isNative, Mojo.ClientSession.isNativeParsingEnabled(), "Mojo.ClientSession.isNativeParsingEnabled did not return the expected value.");
    
    var json;
    var basicTypes = { x:1, foo:"bar", arr:[3,-2,1.6], obj: {
      lykNullz: null,
      bool: false,
      unfoo: "unbar",
      lykUndefindz: undefined,
      func: function(){
        Y.assert.fail("func was executed!");
      }
    } };
    var nestedStuff = [{arr:[{arr2:[1,{bool:true,obj4:{arr3:[1,2,{},[]]}}]}]}];
    var nestedStuff2 = { obj:{toJSON:function(){return {"foo":"bar", "one":1};}} };
    
    json = Mojo.Util.toJSON(basicTypes);
    Y.Assert.areEqual( '{"x":1,"foo":"bar","arr":[3,-2,1.6],"obj":{"lykNullz":null,"bool":false,"unfoo":"unbar"}}', json, '(Test 1) The returned JSON string did not match the expected value.' );
    
    json = this.child.toJSON();
    Y.Assert.areEqual( '{"foo":"bar","whiteNoise":{"x":1,"arr":[1]}}', json, '(Test 2) The returned JSON string does not match the expected value.' );
    
    json = Mojo.Util.toJSON(this.child);
    Y.Assert.areEqual( '{"foo":"bar","whiteNoise":{"x":1,"arr":[1]}}', json, '(Test 3) The returned JSON string does not match the expected value.' );
    
    json = Mojo.Util.toJSON(nestedStuff);
    Y.Assert.areEqual('[{"arr":[{"arr2":[1,{"bool":true,"obj4":{"arr3":[1,2,{},[]]}}]}]}]', json, "(Test 4) The returned JSON string does not match the expected value.");
    
    json = Mojo.Util.toJSON(nestedStuff2);
    Y.Assert.areEqual('{"obj":{"foo":"bar","one":1}}', json, "(Test 5) The returned JSON string does not match the expected value.");
    
    // Test with replacer
    var replace = function(k, v){
      if (v == "bar") {
        return "unbar";
      }
      else if (v == "unbar") {
        return "bar";
      }
      return v;
    };
    
    var json = Mojo.Util.toJSON(basicTypes, replace);
    Y.Assert.areEqual('{"x":1,"foo":"unbar","arr":[3,-2,1.6],"obj":{"lykNullz":null,"bool":false,"unfoo":"bar"}}', json, '(Test 6) toJSON with Replacer failed. The returned JSON string did not match the expected value.');
  },
	
	testCustomToJSON : function()
	{
    this.child.table = { toJSON: function(k,v) { return 'SomeComplexObject'; } };
    
    var json = this.child.toJSON();
    Y.Assert.areNotEqual( -1, json.search('"table":"SomeComplexObject"'), 'Unable to find an expected string ("table":"SomeComplexObject") in the returned JSON: \n' + json );
	},
  
  testNativeToObject: function()
  {
    Y.Assert.isTrue(Mojo.SUPPORTS_NATIVE_PARSING, "Unable to test native parsing, the browser does not support it.");
    Mojo.ClientSession.setNativeParsingEnabled(true);
    
    this.doToObject();
  },
  
  testNonNativeToObject: function()
  {
    Mojo.ClientSession.setNativeParsingEnabled(false);
    
    this.doToObject();
  },
  
  doToObject : function ()
  {
    var json = '{"foo":"bar"}';
    
    // Test without replacer
    Y.Assert.areEqual("bar", Mojo.Util.toObject(json).foo, "The returned object did not contain an expected value.");
    
    // Test with replacer
    var replacer = function(k, v) {
      if (v == "bar") 
        return "unbar";
      return v;
    }
    Y.Assert.areEqual("unbar", Mojo.Util.toObject(json,replacer).foo, "The returned object did not contain an expected value.");
  },
  
  testFullConversion : function()
  {
    var json1 = this.child.toJSON();
    var obj = Mojo.Util.getObject(json1);
    var json2 = Mojo.Util.toJSON(obj);
    
    Y.Assert.areEqual(json1, json2);
  }
});

TestFramework.newTestCase(SUITE_NAME, {
	
  name: "StructureTest",

  _should: {
    error: {
      testLinkedHashMapInsertInvalid : com.runwaysdk.Exception,
      testLinkedHashMapInsertEmpty: com.runwaysdk.Exception,
      testLinkedHashMapInsertDuplicate: com.runwaysdk.Exception
    }
  },
  
  caseSetUp : function() {
    this.struct = Mojo.Meta.alias("com.runwaysdk.structure.*");
  },

  _testMap : function(map){
  
    var arr = [
      new com.runwaysdk.test.Child(),
      new com.runwaysdk.test.Child(),
      new com.runwaysdk.test.Child()
    ];
    
    var c1 = arr[0];
    var c2 = arr[1];
    var c3 = arr[2];
    var c4 = new com.runwaysdk.test.Child();
    
    // put
    map.put(c1.getId(), c1); // string-based
    map.put(c2, c2); // object based
    map.put(c3, c3);
    
    // get
    Y.Assert.isTrue(c1.equals(map.get(c1.getId()))); // string-based
    Y.Assert.isTrue(c2.equals(map.get(c2))); // object-based
    Y.Assert.isTrue(c3.equals(map.get(c3)));
    Y.Assert.isNull(map.get(c4));
    
    // size/isEmpty
    Y.Assert.areEqual(arr.length, map.size());
    Y.Assert.isFalse(map.isEmpty());
    
    // containsKey
    Y.Assert.isTrue(map.containsKey(c1.getId())); // string-based
    Y.Assert.isTrue(map.containsKey(c1)); // object-based
    Y.Assert.isFalse(map.containsKey(c4));
    
    // containsValue
    Y.Assert.isTrue(map.containsValue(c1));
    Y.Assert.isFalse(map.containsValue(c4));

    // remove
    map.remove(c1.getId());
    Y.Assert.isFalse(map.containsKey(c1));
    map.remove(c2);
    Y.Assert.isFalse(map.containsKey(c2));
    Y.Assert.areEqual(1, map.size());

    // clear
    map.clear();
    Y.Assert.areEqual(0, map.size());
    Y.Assert.isTrue(map.isEmpty());
    
    // putAll
    map.putAll(arr);
    Y.Assert.areEqual(3, map.size());
    
    return arr;
  },
  
  testHashMap : function(){
    var map = new com.runwaysdk.structure.HashMap();
    var arr = this._testMap(map);
    
    // keySet and values (order doesn't matter)
    var keySet = map.keySet(); // returns Child[] because they keys were inserted as objects
    var keys = new com.runwaysdk.structure.HashSet(keySet);
    for(var i=0; i<arr.length; i++){
      var child = arr[i];
      if(!keys.contains(child)){
        Y.Assert.fail('The object ['+child+'] was not found in the Map keyset.');
      }
    }
    
    // values
    var values = map.values(); // returns Child[]
    var valuesSet = new com.runwaysdk.structure.HashSet(values);
    for(var i=0; i<arr.length; i++){
      var child = arr[i];
      if(!valuesSet.contains(child)){
        Y.Assert.fail('The object ['+child+'] was not found in the Map values.');
      }
    }
  },
 
  testLinkedHashMap : function(){
    var map = new com.runwaysdk.structure.LinkedHashMap();
    var arr = this._testMap(map);
    
    // insert two new objects and ensure proper ordering
    var first = new com.runwaysdk.test.Child();
    map.insert(first, first, arr[0]);
    arr.unshift(first);
    
    
    var secondToLast = new com.runwaysdk.test.Child();
    map.insert(secondToLast, secondToLast, arr[arr.length-1]);
    arr.splice(arr.length-1, 0, secondToLast);
    
    // keySet
    var keySet = map.keySet(); // returns Child[] because they keys were inserted as objects
    for(var i=0; i<arr.length; i++){
      var child = arr[i];
      if(!arr[i].equals(keySet[i])){
        Y.Assert.fail('The object ['+child+'] was not found in the Map keyset.');
      }
    }
    
    // values
    var values = map.values(); // returns Child[]
    for(var i=0; i<arr.length; i++){
      var child = arr[i];
      if(!arr[i].equals(values[i])){
        Y.Assert.fail('The object ['+child+'] was not found in the Map values.');
      }
    }
  },
  
  /**
   * Tests the replace functionality of the LinkedHashMap.
   */
  testLinkedHashMapReplace : function(){
    var map = new com.runwaysdk.structure.LinkedHashMap();
    var child2 = new com.runwaysdk.test.Child();
    var child1 = new com.runwaysdk.test.Child();
    map.put(child1, child1);
    map.put(child2, child2);
    
    // the list has one item but we insert before a non-existent entry
    var child3 = new com.runwaysdk.test.Child();
    map.replace(child3, child3, child1);
    
    var values = map.values();
    Y.Assert.areEqual(2, map.size());
    Y.Assert.isFalse(map.containsKey(child1));
    Y.Assert.isTrue(values[0].equals(child3));  
    Y.Assert.isTrue(values[1].equals(child2));  
  },
  
  /**
   * Tests that an error is thrown when inserting before an non-existent entry.
   */
  testLinkedHashMapInsertInvalid : function(){
  
    var map = new com.runwaysdk.structure.LinkedHashMap();
    var child1 = new com.runwaysdk.test.Child();
    map.put(child1, child1);
    var child2 = new com.runwaysdk.test.Child();
    
    // the list has one item but we insert before a non-existent entry
    map.insert(child2, child2, Mojo.Util.generateId(16));
  },
  
  /**
   * Tests that an error is thrown when inserting an entry into an empty list.
   */
  testLinkedHashMapInsertEmpty : function(){
  
    var map = new com.runwaysdk.structure.LinkedHashMap();
    var newChild = new com.runwaysdk.test.Child();
    map.insert(newChild, newChild, Mojo.Util.generateId(16));
  },
  
  testLinkedHashMapInsertDuplicate : function(){
    var map = new com.runwaysdk.structure.LinkedHashMap();
    var child1 = new com.runwaysdk.test.Child();
    map.put(child1, child1);
    var child2 = new com.runwaysdk.test.Child();
    
    // the list has one item but we insert before a non-existent entry
    map.insert(child2, child2, child1);
    map.insert(child2, child2, child1);
  },
  
  testHashSet : function(){
    Y.Assert.fail('not implemented');
  },
  
  testTaskQueue : function(){
    var taskQueue = new this.struct.TaskQueue();
    
    var count = 0;
    var wait_count = 0;
    var start_count = 0;
    var finish_count = 0;
    var startQ_count = 0;
    var finishQ_count = 0;
    var yuiTest = this;
    
    var MyTaskQueueListener = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+"MyTaskQueueListener", {
      Extends: this.struct.TaskListener,
      
      Instance : {
        onStart : function(){startQ_count++;},
        onFinish : function(){finishQ_count++},
        onStop : function(){}
      }
    });
    var MyTaskListener = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+"MyTaskListener", {
      Extends: this.struct.TaskListener,
      
      Instance : {
        onStart : function(){start_count++;},
        onFinish : function(){finish_count++},
        onStop : function(){}
      }
    });
    taskQueue.addTaskListener(new MyTaskListener());
    taskQueue.addTaskQueueListener(new MyTaskQueueListener());
    
    taskQueue.addTask(new this.struct.TaskIF({
      start: function(tq){
        
        count++;
        setTimeout(function(){
          wait_count++;
          taskQueue.next();
        }, 100);
      }
    }));
    
    taskQueue.addTask(new this.struct.TaskIF({
      start: function(tq){
        
        count++;
        setTimeout(function(){
          wait_count++;
          taskQueue.next();
        }, 100);
      }
    }));
    
    taskQueue.addTask(new this.struct.TaskIF({
      start: function(tq){
        
        count++;
        setTimeout(function(){
          wait_count++;
          taskQueue.next();
        }, 100);
      }
    }));
    
    taskQueue.addTask(new this.struct.TaskIF({
      start: function(tq){
        
        count++;
        
        setTimeout(function(){
          yuiTest.resume(function(){
            Y.Assert.areEqual(count, 4, "The task queue did not run all of the tasks.");
            Y.Assert.areEqual(wait_count, 3, "The task queue did wait for the asynchronous taskQueue.next() call to run the tasks.");
            Y.Assert.areEqual(start_count, 4, "The task queue did not fire the TStartEvent the expected number of times.");
            Y.Assert.areEqual(finish_count, 4, "The task queue did not fire the TFinishEvent the expected number of times.");
            
            Y.Assert.areEqual(startQ_count, 1, "The task queue did not fire the TQStartEvent the expected number of times.");
            Y.Assert.areEqual(finishQ_count, 1, "The task queue did not fire the TQFinishEvent the expected number of times.");
          }, 100);
        });
        
        taskQueue.next();
      }
    }));
    
    taskQueue.start();
    
    yuiTest.wait(1000);
  }
 
});

TestFramework.newTestCase(SUITE_NAME, {
  
  name: "LoggingTests",
  
  _should: {
    error: {
      
    }
  },
  
  caseSetUp: function ()
  {
    this._log = com.runwaysdk.log;
  },
  
  testBasicLogs : function() {
    var log = this._log.Factory.getLog("testLog");
    
    var consoleAppender = new this._log.ConsoleAppender();
    log.addAppender(consoleAppender);
    consoleAppender.toggle();
    
    var browserAppender = new this._log.BrowserConsoleAppender();
    log.addAppender(browserAppender);
    
    log.trace("This is a trace.");
    log.debug("This is a debug.");
    log.info("This is a info.");
    log.warn("This is a warn.");
    log.error("This is a error.");
    log.fatal("This is a fatal.");
  },
  
  testLogFactoryTypes : function() {
    // If we run the test twice without refreshing the browser this test will fail unless we do this check
    var NotDefaultLog = Mojo.Meta.findClass(TestFramework.PACKAGE + 'NotDefaultLog');
    if (NotDefaultLog == null) {
      NotDefaultLog = Mojo.Meta.newClass(TestFramework.PACKAGE + 'NotDefaultLog', {
        Extends: com.runwaysdk.log.DefaultLog,
        Instance: {
          initialize: function(name){
            this.$initialize(name)
          }
        }
      });
    }
    
    var log = this._log.Factory.getLog("testType", NotDefaultLog);
    
    Y.Assert.areEqual(true, log instanceof NotDefaultLog, "We specified a log type in Factory.getLog, but the method did not return an object of the type we specified.");
    Y.Assert.areEqual(this._log.Factory.getLog("testType"), log, "Getting the same log twice did not return the same log (a new one was created instead).");
    
    var didFail = false;
    try {
      this._log.Factory.getLog("testType", com.runwaysdk.log.DefaultLog);
      didFail = true;
    }
    catch (e) {
      
    }
    
    if (didFail) {
      Y.Assert.fail("Getting an already existing log with a different type did not throw an exception like it should have.");
    }
  },
  
  testLogLevels : function() {
    var log = this._log.Factory.getLog("testLog");
    
    var consoleAppender = new this._log.ConsoleAppender();
    log.addAppender(consoleAppender);
    consoleAppender.toggle();
    
    var browserAppender = new this._log.BrowserConsoleAppender();
    log.addAppender(browserAppender);
    
    log.info("First let's change the log's level to error and make sure that it can only log error and fatal statements.");
    
    log.setLevel(this._log.level.ERROR);
    
    Y.Assert.areEqual(false, log.canLog(this._log.level.TRACE));
    Y.Assert.areEqual(false, log.canLog(this._log.level.DEBUG));
    Y.Assert.areEqual(false, log.canLog(this._log.level.INFO));
    Y.Assert.areEqual(false, log.canLog(this._log.level.WARN));
    Y.Assert.areEqual(true, log.canLog(this._log.level.ERROR));
    Y.Assert.areEqual(true, log.canLog(this._log.level.FATAL));
    
    log.trace("The test failed. You should not be able to see this log statement.");
    log.debug("The test failed. You should not be able to see this log statement.");
    log.info("The test failed. You should not be able to see this log statement.");
    log.warn("The test failed. You should not be able to see this log statement.");
    log.error("Congratulations if you can see me, because you absolutely should be able to!");
    log.fatal("Congratulations if you can see me, because you absolutely should be able to!");
    
    log.setLevel(this._log.level.TRACE);
    log.info("After setting the log's level back to trace, I will next set the root log level to Warn and log a bunch of statements. You should see exactly 3 logs from a warn, an error, and a fatal or this test failed.");
    this._log.Factory.getRootLog().setLevel(this._log.level.WARN);
    
    Y.Assert.areEqual(false, log.canLog(this._log.level.TRACE));
    Y.Assert.areEqual(false, log.canLog(this._log.level.DEBUG));
    Y.Assert.areEqual(false, log.canLog(this._log.level.INFO));
    Y.Assert.areEqual(true, log.canLog(this._log.level.WARN));
    Y.Assert.areEqual(true, log.canLog(this._log.level.ERROR));
    Y.Assert.areEqual(true, log.canLog(this._log.level.FATAL));
    
    log.trace("The test failed. You should not be able to see this log statement.");
    log.debug("The test failed. You should not be able to see this log statement.");
    log.info("The test failed. You should not be able to see this log statement.");
    log.warn("Congratulations if you can see me, because you absolutely should be able to!");
    log.error("Congratulations if you can see me, because you absolutely should be able to!");
    log.fatal("Congratulations if you can see me, because you absolutely should be able to!");
  },
  
  testAjaxLog : function() {
    throw new com.runwaysdk.Exception("Implement me plox!");
  },
  
  testAlertAppender : function() {
    var log = this._log.Factory.getLog("testLog");
    
    var alertAppender = new this._log.JSAlertAppender();
    log.addAppender(alertAppender);
    
    log.warn("This is a warn.");
  }
  
});

TestFramework.newTestCase(SUITE_NAME, {
  
  name: "EventTests",

  caseSetUp: function ()
  {
    var Publication = Mojo.Meta.newClass(TestFramework.PACKAGE+'Publication', {
      Instance : {
        initialize : function(title, author)
        {
          this.$initialize();
          this._title = title;
          this._author = author;
        },
        getTitle : function(){ return this._title; },
        getAuthor : function() { return this._author; },
        dispatchEditEvent : function() {
          var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
          this.dispatchEvent(new EditEvent(this));
        },
        dispatchFinalEditEvent : function(approvingEditor) {
          var FinalEditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'FinalEditEvent');
          this.dispatchEvent(new FinalEditEvent(this, approvingEditor));
        },
        dispatchPublishEvent : function(publishDate) {
          var PublishEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'PublishEvent');
          this.dispatchEvent(new PublishEvent(this, publishDate));
        }
      }
    });
    
    var EditEvent = Mojo.Meta.newClass(TestFramework.PACKAGE+'EditEvent', {
      Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
      Instance : {
        initialize : function(doc)
        {
          this.$initialize();
          this._doc = doc;
        },
        getDocument : function() { return this._doc; },
        initCustomEvent : function(eventType, bubbles, cancelable, doc)
        {
          this.$initCustomEvent(eventType, bubbles, cancelable);
          this._doc = doc;
        }
      }
    });
    
    var FinalEditEvent = Mojo.Meta.newClass(TestFramework.PACKAGE+'FinalEditEvent', {
      Extends : EditEvent,
      Instance : {
        initialize : function(doc, approvingEditor)
        {
          this.$initialize(doc);
          this._approvingEditor = approvingEditor;
        },
        getApprovingEditor : function() { return this._approvingEditor; }
      }
    });
    
    var PublishEvent = Mojo.Meta.newClass(TestFramework.PACKAGE+'PublishEvent', {
      Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
      Instance : {
        initialize : function(doc, publishDate)
        {
          this.$initialize();
          this._doc = doc;
          this._publishDate = publishDate;
        },
        getDocument : function() { return this._doc; },
        getPublishDate : function() { return this._publishDate; }
      }
    });
    
    var ParentComponent = Mojo.Meta.newClass(TestFramework.PACKAGE+'ParentComponent', {
      Extends : Mojo.$.com.runwaysdk.ui.Component,
      Instance : {
        initialize : function()
        {
          this.$initialize();
        },
        getChildren : function(){},
        getChild : function(){},
        hasChild : function(){}
      }
    });
    
    var ChildComponent = Mojo.Meta.newClass(TestFramework.PACKAGE+'ChildComponent', {
      Extends : Mojo.$.com.runwaysdk.ui.Component,
      Instance : {
        initialize : function()
        {
          this.$initialize();
        },
        getChildren : function(){},
        getChild : function(){},
        hasChild : function(){}
      }
    });
    
    var ComEvent = Mojo.Meta.newClass(TestFramework.PACKAGE+'ComEvent', {
      Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
      Instance : {
        initialize : function()
        {
          this.$initialize();
        }
      }
    });
    
    // Interface used in conjunction with DefaultActionEvent to test that a 
    // default action is fired correctly. 
    var DefaultActionHandlerIF = Mojo.Meta.newInterface(TestFramework.PACKAGE+'DefaultActionHandlerIF', {
      Instance : {
        defaultActionHandler : function(evt){}
      }
    });
    
    var DefaultActionEvent = Mojo.Meta.newClass(TestFramework.PACKAGE+'DefaultActionEvent', {
      Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
      Instance : {
        initialize : function(defaultActionHandlerIF)
        {
          this.$initialize();
          this._handler = defaultActionHandlerIF;
        },
        defaultAction : function()
        {
          this._handler.defaultActionHandler(this);
        },
        initCustomEvent : function(eventType, bubbles, cancelable, handler)
        {
          this.$initCustomEvent(eventType, bubbles, cancelable);
          this._handler = handler;
        }
      }
    });
  },
  
  /**
   * Tests a mapping of one event to one listener.
   */
  testOneToOne_object : function()
  {
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p = new Publication('Pub1', 'Author1');
  
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
  
      var success = false;
      var listener = new com.runwaysdk.event.EventListener({
        handleEvent : function(e)
        {
          success = e instanceof EditEvent && e.getDocument() === p;
        }
      });
  
  
      p.addEventListener(EditEvent, listener);
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success);
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that one EventTarget can dispatch to one
   * object listener many times.
   */
  testOneToOneMultipleDispatch_object : function()
  {
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p = new Publication('Pub1', 'Author1');
  
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
  
      var goal = 10;
      var listener = new com.runwaysdk.event.EventListener({
        counter : 0,
        handleEvent : function(e)
        {
          if(e instanceof EditEvent && e.getDocument() === p)
          {
            this.counter++;
          }
        }
      });
  
      p.addEventListener(EditEvent, listener);
      
      for(var i=0; i<goal; i++)
      {
        p.dispatchEditEvent();
      }
      
      Y.Assert.areEqual(goal, listener.counter);
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests a mapping of one event to one listener.
   */
  testOneToOne_function : function()
  {
    try
    {  
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p = new Publication('Pub1', 'Author1');
  
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
  
      var success = false;
  
      p.addEventListener(EditEvent, function(e){
          success = e instanceof EditEvent && e.getDocument() === p;
      });
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success);
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that one EventTarget can dispatch to one
   * function listener many times.
   */  
  testOneToOneMultipleDispatch_function : function()
  {
    try
    {  
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p = new Publication('Pub1', 'Author1');
  
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
  
      var goal = 10;
      var counter = 0;  
      p.addEventListener(EditEvent, function(e){
          if(e instanceof EditEvent && e.getDocument() === p)
          {
            counter++;
          }
      });
      
      for(var i=0; i<goal; i++)
      {
        p.dispatchEditEvent();
      }
      
      Y.Assert.areEqual(goal, counter);
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }  
  },
  
  /**
   * Tests a mapping of one event to many listeners.
   */
  testOneToMany_object : function()
  {
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p = new Publication('Pub1', 'Author1');
  
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
  
      var success1 = false;
      var success2 = false;
      var success3 = false;
      
      var listener1 = new com.runwaysdk.event.EventListener({
        handleEvent : function(e)
        {
          success1 = e instanceof EditEvent && e.getDocument() === p;
        }
      });
      var listener2 = new com.runwaysdk.event.EventListener({
        handleEvent : function(e)
        {
          success2 = e instanceof EditEvent && e.getDocument() === p;
        }
      });
      var listener3 = new com.runwaysdk.event.EventListener({
        handleEvent : function(e)
        {
          success3 = e instanceof EditEvent && e.getDocument() === p;
        }
      });
  
  
      p.addEventListener(EditEvent, listener1);
      p.addEventListener(EditEvent, listener2);
      p.addEventListener(EditEvent, listener3);
      
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success1);
      Y.Assert.isTrue(success2);
      Y.Assert.isTrue(success3);
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }    
  },

  /**
   * Tests a mapping of one event to many listeners.
   */
  testOneToMany_function : function()
  {
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p = new Publication('Pub1', 'Author1');
  
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
  
      var success1 = false;
      var success2 = false;
      var success3 = false;
      
      p.addEventListener(EditEvent, function(e){
          success1 = e instanceof EditEvent && e.getDocument() === p;
      });
      p.addEventListener(EditEvent, function(e){
          success2 = e instanceof EditEvent && e.getDocument() === p;
      });
      p.addEventListener(EditEvent, function(e){
          success3 = e instanceof EditEvent && e.getDocument() === p;
      });
      
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success1);
      Y.Assert.isTrue(success2);
      Y.Assert.isTrue(success3);
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }    
  },
  
  /**
   * Tests many events firing to one listener.
   */
  testManyToOne_object : function()
  {
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p1 = new Publication('Pub1', 'Author1');
      var p2 = new Publication('Pub2', 'Author2');
      var p3 = new Publication('Pub3', 'Author3');
  
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
  
      var success1 = false;
      var success2 = false;
      var success3 = false;
      
      var listener = new com.runwaysdk.event.EventListener({
        handleEvent : function(e)
        {
          if(e instanceof EditEvent && e.getDocument() === p1)
          {
            success1 = true;
          }
          if(e instanceof EditEvent && e.getDocument() === p2)
          {
            success2 = true;
          }
          if(e instanceof EditEvent && e.getDocument() === p3)
          {
            success3 = true;
          }
        }
      });
      
      p1.addEventListener(EditEvent,listener);
      p2.addEventListener(EditEvent,listener);
      p3.addEventListener(EditEvent,listener);
      
      p1.dispatchEditEvent();
      p2.dispatchEditEvent();
      p3.dispatchEditEvent();
      
      Y.Assert.isTrue(success1);
      Y.Assert.isTrue(success2);
      Y.Assert.isTrue(success3);
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      if(Mojo.Util.isObject(p1))
      {
        p1.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(p2))
      {
        p2.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(p3))
      {
        p3.removeAllEventListeners();
      }
    } 
  },
  
  /**
   * Tests many events firing to one listener.
   */
  testManyToOne_function : function()
  {
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p1 = new Publication('Pub1', 'Author1');
      var p2 = new Publication('Pub2', 'Author2');
      var p3 = new Publication('Pub3', 'Author3');
  
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
  
      var success1 = false;
      var success2 = false;
      var success3 = false;
      
      var listener =  function(e)
      {
        if(e instanceof EditEvent && e.getDocument() === p1)
        {
          success1 = true;
        }
        if(e instanceof EditEvent && e.getDocument() === p2)
        {
          success2 = true;
        }
        if(e instanceof EditEvent && e.getDocument() === p3)
        {
          success3 = true;
        }
      };
      
      p1.addEventListener(EditEvent,listener);
      p2.addEventListener(EditEvent,listener);
      p3.addEventListener(EditEvent,listener);
      
      p1.dispatchEditEvent();
      p2.dispatchEditEvent();
      p3.dispatchEditEvent();
      
      Y.Assert.isTrue(success1);
      Y.Assert.isTrue(success2);
      Y.Assert.isTrue(success3);
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      if(Mojo.Util.isObject(p1))
      {
        p1.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(p2))
      {
        p2.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(p3))
      {
        p3.removeAllEventListeners();
      }
    } 
  },
  
  /**
   * Tests many events firing to many listeners. This is ensure
   * the correct mapping between event targets and listeners.
   */
  testManyToMany_object : function()
  {
    try
    {
      var size = 3;
      var results = []; // 2d array containings results for [i][j]
      var docs = [];
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      for(var i=0; i<size; i++)
      {
        results[i] = [];
        var p = new Publication('Pub'+i, 'Author'+i);
        
        for(var j=0; j<size; j++)
        {
          results[i][j] = false;
          var l = new com.runwaysdk.event.EventListener({
            pCount : i,
            lCount : j,
            handleEvent : function(e)
            {
              if(e instanceof EditEvent && e.getDocument() === docs[this.pCount])
              {
                results[this.pCount][this.lCount] = true;
              }
            }
          });
          
          p.addEventListener(EditEvent, l);
        }
        
        docs[i] = p;
      }
    
      for(var i=0; i<docs.length; i++)
      {
        docs[i].dispatchEditEvent();
      }
      
      for(var i=0; i<size; i++)
      {
        for(var j=0; j<size; j++)
        {
          Y.Assert.isTrue(results[i][j]);
        }
      }
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      for(var i=0; i<docs.length; i++)
      {
        if(Mojo.Util.isObject(docs[i]))
        {
          docs[i].removeAllEventListeners();
        }
      }
    } 
  },

  /**
   * Tests many events firing to many listeners. This is ensure
   * the correct mapping between event targets and listeners.
   */
  testManyToMany_function : function()
  {
    try
    {
      var size = 3;
      var results = []; // 2d array containings results for [i][j]
      var docs = [];
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      for(var i=0; i<size; i++)
      {
        results[i] = [];
        var p = new Publication('Pub'+i, 'Author'+i);
        
        for(var j=0; j<size; j++)
        {
          results[i][j] = false;
          
          var f = (function(pCount, lCount){
            p.addEventListener(EditEvent, function(e){
              if(e instanceof EditEvent && e.getDocument() === docs[pCount])
              {
                results[pCount][lCount] = true;
              }
            });
          
          })(i, j);
        }
        
        docs[i] = p;
      }
    
      for(var i=0; i<docs.length; i++)
      {
        docs[i].dispatchEditEvent();
      }
      
      for(var i=0; i<size; i++)
      {
        for(var j=0; j<size; j++)
        {
          Y.Assert.isTrue(results[i][j]);
        }
      }
    }
    catch(e)
    {
      Y.Assert.fail(e.message);
    }
    finally
    {
      for(var i=0; i<docs.length; i++)
      {
        if(Mojo.Util.isObject(docs[i]))
        {
          docs[i].removeAllEventListeners();
        }
      }
    } 
  },
  
  /**
   * Tests many different event types being fired from the
   * same EventTarget to different object listeners. 
   */
  testDispatchMixedEvents_object : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
      var FinalEditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'FinalEditEvent');
      var PublishEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'PublishEvent');
      
      p = new Publication('Pub', 'Auth');
      
      var editSuccess = false;
      var finalEditSuccess = false;
      var publishSuccess = false;
      
      var editor = 'John P. Editor';
      var publishDate = new Date();
      
      p.addEventListener(EditEvent, new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              editSuccess = e instanceof EditEvent && e.getDocument() === p;
            }
          }));
          
      p.addEventListener(FinalEditEvent, new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              finalEditSuccess = e instanceof FinalEditEvent && e.getDocument() === p
                && e.getApprovingEditor() === editor;
            }
          }));
          
      p.addEventListener(PublishEvent, new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              publishSuccess = e instanceof PublishEvent && e.getDocument() === p
                && e.getPublishDate() === publishDate;
            }
          }));
          
      p.dispatchEditEvent();
      p.dispatchFinalEditEvent(editor);
      p.dispatchPublishEvent(publishDate);
      
      Y.Assert.isTrue(editSuccess);
      Y.Assert.isTrue(finalEditSuccess);
      Y.Assert.isTrue(publishSuccess);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },

  /**
   * Tests many different event types being fired from the
   * same EventTarget to different function listeners. 
   */
  testDispatchMixedEvents_function : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
      var FinalEditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'FinalEditEvent');
      var PublishEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'PublishEvent');
      
      p = new Publication('Pub', 'Auth');
      
      var editSuccess = false;
      var finalEditSuccess = false;
      var publishSuccess = false;
      
      var editor = 'John P. Editor';
      var publishDate = new Date();
      
      p.addEventListener(EditEvent, function(e){
        editSuccess = e instanceof EditEvent && e.getDocument() === p;
      });
          
      p.addEventListener(FinalEditEvent, function(e){
        finalEditSuccess = e instanceof FinalEditEvent && e.getDocument() === p
          && e.getApprovingEditor() === editor;
      });
          
      p.addEventListener(PublishEvent, function(e){
        publishSuccess = e instanceof PublishEvent && e.getDocument() === p
          && e.getPublishDate() === publishDate;
      });
          
      p.dispatchEditEvent();
      p.dispatchFinalEditEvent(editor);
      p.dispatchPublishEvent(publishDate);
      
      Y.Assert.isTrue(editSuccess);
      Y.Assert.isTrue(finalEditSuccess);
      Y.Assert.isTrue(publishSuccess);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests the default *this* context of an event listener, which
   * defaults to the *this* of the listener in the case of an object.
   */
  testDefaultContext_object : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var success = false;
      
      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              success = this === l;
            }
          });
          
      p.addEventListener(EditEvent, l);
      
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },

  /**
   * Tests the default *this* context of a function event listener, which
   * is the *this* of the currentTarget.
   */
  testDefaultContext_function : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var success = false;
      
      p = new Publication('Pub', 'Auth');
      var l = function(e){
        success = this === p && p === e.getCurrentTarget();
      };
          
      p.addEventListener(EditEvent, l);
      
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }    
  },
  
  /**
   * Tests a custom *this* context of an object event listener, which
   * must be the *this* of the event listener itself (to avoid sloppy "method stealing").
   */
  testCustomContextValid_object : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var success = false;
      
      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              success = this === l;
            }
          });
          
      p.addEventListener(EditEvent, l, null, l);
      
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that an error is thrown when the custom context of an event listener
   * is anything other than the event listener itself.
   */
  testCustomContextInvalid_function : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
            }
          });
      
      try
      {
        p.addEventListener(EditEvent, l, null, {});
        Y.Assert.fail('An event listener object was able to set a custom *this* reference incorrectly.');
      }
      catch(e)
      {
        // success
      }
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests a custom *this* context of an event listener.
   */
  testCustomContext_function : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var success = false;
      
      p = new Publication('Pub', 'Auth');
      var context = {};
      var l = function(e){
        success = this === context;
      };
          
      p.addEventListener(EditEvent, l, null, context);
      
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    } 
  },
  
  /**
   * Tests setting and receiving a custom
   * argument when registering an object
   * EventListener.
   */
  testCustomArg_object : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var success = false;
      
      p = new Publication('Pub', 'Auth');
      var obj = {a:1};
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e, arg)
            {
              success = arg === obj && arg.a === obj.a;
            }
          });
          
      p.addEventListener(EditEvent, l, obj);
      
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },

  /**
   * Tests setting and receiving a custom
   * argument when registering a function
   * EventListener.
   */  
  testCustomArg_function : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var success = false;
      
      p = new Publication('Pub', 'Auth');
      var obj = {a:1};
      var l = function(e, arg){
        success = arg === obj && arg.a === obj.a;
      };
          
      p.addEventListener(EditEvent, l, obj);
      
      p.dispatchEditEvent();
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that an error occurs when a listener attempts to capture
   * on a custom event, which is not allowed.
   */
  testFailOnCapture : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
            }
          });
      
      try
      {
        p.addEventListener(EditEvent, l, null, l, true);
        Y.Assert.fail('An event listener object for a custom event was able to use capturing.');
      }
      catch(e)
      {
        // success
      }
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * The DOM Event specification states that the type of an event
   * cannnot start with the string "DOM" (case-insensitive).
   */
  testErrorOnDOMPrefix : function()
  {
    var className = 'DomCustomEvent';
    try
    {
      var InvalidEvent = Mojo.Meta.newClass(className, {
        Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
        Instance : {
          initialize : function(doc, publishDate)
          {
            this.$initialize();
          }
        }
      });
      
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var p = new Publication('Pub', 'Auth');
      
      try
      {
        p.addEventListener(InvalidEvent, function(e){});
        Y.Assert.fail('A custom event was able to use the prefix "DOM" in the qualified type name.');
      }
      catch(e)
      {
        // success
      }
    }
    finally
    {
      Mojo.Meta.dropClass(className);
    }
  },
  
  /**
   * Tests that duplicate listeners cannot be added to the same event target
   * and event type, as required by the spec. The duplicate should be discarded
   * instead of firing twice.
   */
  testDuplicateListeners_object : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var fired = 0;
      
      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              fired++;
            }
          });
          
      p.addEventListener(EditEvent, l);
      p.addEventListener(EditEvent, l);
      
      p.dispatchEditEvent();
      
      Y.Assert.areEqual(fired, 1);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that duplicate listeners cannot be added to the same event target
   * and event type, as required by the spec. The duplicate should be discarded
   * instead of firing twice.
   */
  testDuplicateListeners_function : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var fired = 0;
      
      p = new Publication('Pub', 'Auth');
      var l = function(e){
              fired++;
          };
          
      p.addEventListener(EditEvent, l);
      p.addEventListener(EditEvent, l);
      
      p.dispatchEditEvent();
      
      Y.Assert.areEqual(fired, 1);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that an event listener is properly removed.
   */
  testRemoveEventListener_object : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var fired = 0;
      
      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              fired++;
            }
          });
          
      p.addEventListener(EditEvent, l);
      
      p.dispatchEditEvent();
      
      p.removeEventListener(EditEvent, l);
      
      p.dispatchEditEvent();

      Y.Assert.areEqual(fired, 1);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that an event listener is properly removed.
   */
  testRemoveEventListener_function : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var fired = 0;
      
      p = new Publication('Pub', 'Auth');
      var l = function(e){
              fired++;
          };
          
      p.addEventListener(EditEvent, l);
      
      p.dispatchEditEvent();
      
      p.removeEventListener(EditEvent, l);
      
      p.dispatchEditEvent();

      Y.Assert.areEqual(1, fired);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests removing all event listeners on an event target
   * for a given event type.
   */
  testRemoveEventListeners : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var fired = 0;
      
      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              fired++;
            }
          });

      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              fired++;
            }
          });
          
      p.addEventListener(EditEvent, l);
      p.addEventListener(EditEvent, l2);
      
      p.dispatchEditEvent();
      
      p.removeEventListeners(EditEvent);
      
      p.dispatchEditEvent();

      Y.Assert.areEqual(2, fired);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests removing all event listeners on an event target.
   */
  testRemoveAllEventListeners : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
      var FinalEditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'FinalEditEvent');

      var fired = 0;
      
      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              fired++;
            }
          });

      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              fired++;
            }
          });
          
      var l3 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              fired++;
            }
          });
          
      p.addEventListener(EditEvent, l);
      p.addEventListener(EditEvent, l2);
      p.addEventListener(FinalEditEvent, l3);
      
      p.dispatchEditEvent();
      p.dispatchFinalEditEvent('John P. Editor');
      
      p.removeAllEventListeners();
      
      p.dispatchEditEvent();
      p.dispatchFinalEditEvent('John P. Editor');

      Y.Assert.areEqual(3, fired);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests a query for a specific event listener on an event target.
   */
  testHasEventListener_object : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      p = new Publication('Pub', 'Auth');
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
            }
          });
      
      Y.Assert.isFalse(p.hasEventListener(EditEvent, l));
      
      p.addEventListener(EditEvent, l);
      
      Y.Assert.isTrue(p.hasEventListener(EditEvent, l));
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests a query for a specific event listener on an event target.
   */
  testHasEventListener_function : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      p = new Publication('Pub', 'Auth');
      var l = function(e){
          };
      
      Y.Assert.isFalse(p.hasEventListener(EditEvent, l));
      
      p.addEventListener(EditEvent, l);
      
      Y.Assert.isTrue(p.hasEventListener(EditEvent, l));
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests creating an event via the DocumentEvent.createEvent() method
   * and initializing the custom event with CustomEvent.initCustomEvent().
   */
  testCreateEvent_custom : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      p = new Publication('Pub', 'Auth');
      

      var success = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              success = e instanceof EditEvent && e.getDocument() === p;
            }
          });
          
      p.addEventListener(EditEvent, l);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(EditEvent);
      evt.initCustomEvent(EditEvent, true, true, p);
      p.dispatchEvent(evt);
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that attempting to change the type of a custom event
   * via CustomEvent.initCustomEvent() throws an error.
   */
  testChangingCustomEventTypeFails : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');
      var FinalEditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'FinalEditEvent');

      p = new Publication('Pub', 'Auth');
      

      var success = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              success = e instanceof EditEvent && e.getDocument() === p;
            }
          });
          
      p.addEventListener(EditEvent, l);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(EditEvent);
      
      try
      {
        evt.initCustomEvent(FinalEditEvent, true, true, p);
        Y.Assert.fail('A custom event was able to change its type.');
      }
      catch(e)
      {
        // success
      }
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }    
  },
  
  /**
   * Tests that events marked as bubbling propagate correctly.
   */
  testBubblingEnabled : function()
  {
    var p = null;
    var c = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');

      p = new ParentComponent();
      c = new ChildComponent();
      
      c.setParent(p);

      var success = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              success = e instanceof ComEvent && e.getTarget() === c
                && e.getCurrentTarget() === p;
            }
          });
          
      p.addEventListener(ComEvent, l);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(ComEvent);
      evt.initCustomEvent(ComEvent, true, true);
      
      c.dispatchEvent(evt);
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that events marked as not bubbling do not propagate.
   */
  testBubblingDisabled : function()
  {
    var p = null;
    var c = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');

      p = new ParentComponent();
      c = new ChildComponent();
      
      c.setParent(p);

      var fired = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              fired = true;
            }
          });
          
      p.addEventListener(ComEvent, l);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(ComEvent);
      evt.initCustomEvent(ComEvent, false, true);
      
      c.dispatchEvent(evt);
      
      Y.Assert.isFalse(fired);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * 
   */
  testDefaultAction : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var DefaultActionHandlerIF = Mojo.Meta.findClass(TestFramework.PACKAGE+'DefaultActionHandlerIF');
      var DefaultActionEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'DefaultActionEvent');

      p = new Publication('Pub', 'Auth');
      
      var success = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              // just to test adherence to the spec, calling either one of these
              // propagation methods should not prevent the default action.
              e.stopImmediatePropagation();
              e.stopPropagation();
            }
          });
          
      var handler = new DefaultActionHandlerIF({
        defaultActionHandler : function(e)
        {
          success = evt === e;
        }
      });
      p.addEventListener(DefaultActionEvent, l);
      
      var evt = new DefaultActionEvent(handler);
      p.dispatchEvent(evt);
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that an event marked as cancelable can be canceled.
   */
  testPreventDefaultCancelEnabled : function()
  {
    // NOTE: a canceled event must still have its listeners called and propagate.
    // check Event.getDefaultPrevented()
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var DefaultActionHandlerIF = Mojo.Meta.findClass(TestFramework.PACKAGE+'DefaultActionHandlerIF');
      var DefaultActionEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'DefaultActionEvent');

      p = new Publication('Pub', 'Auth');
      
      var called = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              e.preventDefault();
            }
          });
          
      var handler = new DefaultActionHandlerIF({
        defaultActionHandler : function(e)
        {
          called = true;
        }
      });
      p.addEventListener(DefaultActionEvent, l);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(DefaultActionEvent);
      evt.initCustomEvent(DefaultActionEvent, true, true, handler);
      p.dispatchEvent(evt);
      
      Y.Assert.isFalse(called);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that an event marked as not cancelable can't be canceled and
   * listener processing resumes as normal.
   */
  testPreventDefaultCancelDisabled : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var DefaultActionHandlerIF = Mojo.Meta.findClass(TestFramework.PACKAGE+'DefaultActionHandlerIF');
      var DefaultActionEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'DefaultActionEvent');

      p = new Publication('Pub', 'Auth');
      
      var success = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              e.preventDefault(); // should have no effect
            }
          });
          
      var handler = new DefaultActionHandlerIF({
        defaultActionHandler : function(e)
        {
          success = true;
        }
      });
      p.addEventListener(DefaultActionEvent, l);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(DefaultActionEvent);
      evt.initCustomEvent(DefaultActionEvent, true, false, handler);
      p.dispatchEvent(evt);
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that an event that is cancelled still bubbles and propagates correctly.
   */
  testPreventDefaultStillPropagates : function()
  {
    // NOTE: a canceled event must still have its listeners called and propagate.
    // check Event.getDefaultPrevented()
    var p = null;
    var c = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var DefaultActionHandlerIF = Mojo.Meta.findClass(TestFramework.PACKAGE+'DefaultActionHandlerIF');
      var DefaultActionEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'DefaultActionEvent');

      p = new ParentComponent();
      c = new ChildComponent();
      
      c.setParent(p);
      
      var success = false;
      var called = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              e.preventDefault();
            }
          });
      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              success = true;
            }
          });
          
      var handler = new DefaultActionHandlerIF({
        defaultActionHandler : function(e)
        {
          called = true;
        }
      });
      c.addEventListener(DefaultActionEvent, l);
      p.addEventListener(DefaultActionEvent, l2);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(DefaultActionEvent);
      evt.initCustomEvent(DefaultActionEvent, true, true, handler);
      
      c.dispatchEvent(evt);
      
      Y.Assert.isFalse(called);
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(c))
      {
        c.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that custom events have their event phase set correctly as
   * at target or bubbling.
   */
  testEventPhase : function()
  {
    var p = null;
    var c = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');
      var EventIF = Mojo.$.com.runwaysdk.event.EventIF;

      p = new ParentComponent();
      c = new ChildComponent();
      
      c.setParent(p);

      var targetPhase = false;
      var bubblePhase = false;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              targetPhase = e.getEventPhase() === EventIF.AT_TARGET;
            }
          });
      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              bubblePhase = e.getEventPhase() === EventIF.BUBBLING_PHASE;
            }
          });
          
      c.addEventListener(ComEvent, l);
      p.addEventListener(ComEvent, l2);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(ComEvent);
      evt.initCustomEvent(ComEvent, true, true);
      
      c.dispatchEvent(evt);
      
      Y.Assert.isTrue(targetPhase);
      Y.Assert.isTrue(bubblePhase);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(c))
      {
        c.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that the event type is the qualified name of the custom event class.
   */
  testEventType : function()
  {
    var p = null;
    try
    {
      var Publication = Mojo.Meta.findClass(TestFramework.PACKAGE+'Publication');
      var EditEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'EditEvent');

      var check1 = false;
      var check2 = false;
      
      p = new Publication('Pub', 'Auth');
      var l = function(e){
        check1 = e.getType() === EditEvent.getMetaClass().getQualifiedName();
      };
      
          
      p.addEventListener(EditEvent, l);
      p.dispatchEditEvent();

      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(EditEvent);
      check2 = evt.getType() === EditEvent.getMetaClass().getQualifiedName();
      
      Y.Assert.isTrue(check1);
      Y.Assert.isTrue(check2);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }  
  },
  
  /**
   * Tests that the correct event target is set on the event object.
   */
  testTarget : function()
  {
    var p = null;
    var c = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');

      p = new ParentComponent();
      c = new ChildComponent();
      
      c.setParent(p);

      var foundCount = 0;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              if(e.getTarget() === c)
              {
                foundCount++;
              }
            }
          });
      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              if(e.getTarget() === c)
              {
                foundCount++;
              }
            }
          });
          
      p.addEventListener(ComEvent, l);
      c.addEventListener(ComEvent, l2);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(ComEvent);
      evt.initCustomEvent(ComEvent, true, true);
      
      c.dispatchEvent(evt);
      
      Y.Assert.areEqual(2, foundCount);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(c))
      {
        c.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that the correct current target is set on the event object
   * as the event bubbles.
   */
  testCurrentTarget : function()
  {
    var p = null;
    var c = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');

      p = new ParentComponent();
      c = new ChildComponent();
      
      c.setParent(p);

      var foundCount = 0;
      
      var l = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              if(e.getCurrentTarget() === p)
              {
                foundCount++;
              }
            }
          });
      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              if(e.getCurrentTarget() === c)
              {
                foundCount++;
              }
            }
          });
          
      p.addEventListener(ComEvent, l);
      c.addEventListener(ComEvent, l2);
      
      var evt = Mojo.$.com.runwaysdk.event.DocumentEvent.getInstance().createEvent(ComEvent);
      evt.initCustomEvent(ComEvent, true, true);
      
      c.dispatchEvent(evt);
      
      Y.Assert.areEqual(2, foundCount);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(c))
      {
        c.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that stopPropagation() prevents further propagation beyond the current target.
   */
  testStopPropagation : function()
  {
    var p = null;
    var c = null;
    var c2 = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');

      p = new ParentComponent();
      c = new ChildComponent();
      
      c.setParent(p);

      var parentCalled = false;
      var child1Called = false;
      var child2Called = false;
      
      var l1 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              child1Called = true;
              e.stopPropagation();
            }
          });
      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              child2Called = true;
            }
          });
      var l3 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              parentCalled = true;
            }
          });
      
      // NOTE: according to the spec a stopped event still notifies the remaining listeners
      // for the current event target, so l2 should be called but not l3.
      c.addEventListener(ComEvent, l1);
      c.addEventListener(ComEvent, l2);
      p.addEventListener(ComEvent, l3);
      
      c.dispatchEvent(new ComEvent());
      
      Y.Assert.isTrue(child1Called);
      Y.Assert.isTrue(child2Called);
      Y.Assert.isFalse(parentCalled);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(c))
      {
        c.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that stopImmediatePropagation() prevents other listeners from being triggered.
   */
  testStopImmediatePropagation : function()
  {
    var p = null;
    var c = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');

      p = new ParentComponent();
      c = new ChildComponent();
      
      c.setParent(p);

      var parentCalled = false;
      var child1Called = false;
      var child2Called = false;
      
      var l1 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              child1Called = true;
              e.stopImmediatePropagation();
            }
          });
      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              child2Called = true;
            }
          });
      var l3 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              parentCalled = true;
            }
          });
      
      // NOTE: according to the spec a stopped event still notifies the remaining listeners
      // for the current event target, so l2 should be called but not l3.
      c.addEventListener(ComEvent, l1);
      c.addEventListener(ComEvent, l2);
      p.addEventListener(ComEvent, l3);
      
      c.dispatchEvent(new ComEvent());
      
      Y.Assert.isTrue(child1Called);
      Y.Assert.isFalse(child2Called);
      Y.Assert.isFalse(parentCalled);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
      if(Mojo.Util.isObject(c))
      {
        c.removeAllEventListeners();
      }
    }
  },   
  
  /**
   * Tests that the optional handleException is invoked if defined.
   */
  testHandleException : function()
  {
    var p = null;
    try
    {
      var ParentComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ParentComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');

      p = new ParentComponent();
      
      var success = false;
      
      var l1 = new com.runwaysdk.event.EventListener({
            handleEvent : function(evt)
            {
              throw new Mojo.$.com.runwaysdk.Exception(evt.getType());
            },
            handleError : function(e)
            {
              success = e.getMessage() === ComEvent.getMetaClass().getQualifiedName();;
            }
          });
      
      p.addEventListener(ComEvent, l1);
      
      p.dispatchEvent(new ComEvent());
      
      Y.Assert.isTrue(success);
    }
    finally
    {
      if(Mojo.Util.isObject(p))
      {
        p.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests that listeners are still processed even if an unhandled exception
   * occurs within a listener. This is required by the spec.
   */
  testListenerProcessingOnException : function()
  {
    var c = null;
    try
    {
      var ChildComponent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ChildComponent');
      var ComEvent = Mojo.Meta.findClass(TestFramework.PACKAGE+'ComEvent');

      c = new ChildComponent();
      
      var child1Error = false;
      var child2Called = false;
      
      var l1 = new com.runwaysdk.event.EventListener({
            handleEvent : function(evt)
            {
              throw new Mojo.$.com.runwaysdk.Exception(evt.getType());
            },
            handleError : function(e)
            {
              child1Error = e.getMessage() === ComEvent.getMetaClass().getQualifiedName();
            }
          });
      var l2 = new com.runwaysdk.event.EventListener({
            handleEvent : function(e)
            {
              child2Called = true;
            }
          });
      
      // NOTE: according to the spec a stopped event still notifies the remaining listeners
      // for the current event target, so l2 should be called but not l3.
      c.addEventListener(ComEvent, l1);
      c.addEventListener(ComEvent, l2);
      
      c.dispatchEvent(new ComEvent());
      
      Y.Assert.isTrue(child1Error);
      Y.Assert.isTrue(child2Called);
    }
    finally
    {
      if(Mojo.Util.isObject(c))
      {
        c.removeAllEventListeners();
      }
    }
  },
  
  /**
   * Tests Listener.stopListener() to ensure a listener can unregister itself.
   */
  testStopListening : function()
  {
    // Listener.stopListening()
    Y.Assert.fail('not implemented');
  },
  
  /**
   * Tests that an event marked as fireOnce does not fire more than once.
   */
  testFireOnce : function()
  {
    Y.Assert.fail('not implemented');
  },
  
  testUIEvent : function()
  {

  },
  
  testMouseEvent : function()
  {
    Y.Assert.fail('not implemented');
  },
  
  testMutationEvent : function()
  {
    Y.Assert.fail('not implemented');
  },
  
  testMutationNameEvent : function()
  {
    Y.Assert.fail('not implemented');
  },
  
  testFocusEvent : function()
  {
    Y.Assert.fail('not implemented');
  },
  
  testCompositionEvent : function()
  {
    Y.Assert.fail('not implemented');
  },
  
  testKeyboardEvent : function()
  {
    Y.Assert.fail('not implemented');
  },
  
  testCanDispatch : function()
  {
    // DocumentEvent.canDispatch()
    Y.Assert.fail('not implemented');
  },
  
  /**
   * Tests that an event that implements the GlobalEvent interface can be registered
   * outside the scope of a single object.
   */
  testGlobalEnabled : function()
  {
    Y.Assert.fail('not implemented');
  },
  
  /**
   * Tests that an error is thrown if a listener tries to globally register with an event
   * that does implement the GlobalEvent interface.
   */
  testGlobalDisabled : function()
  {
    Y.Assert.fail('not implemented');
    
  },
  
  /**
   * Tests that dispatchEvent(evt) properly throws an exception when an error occurs
   * during invocation.
   */
  testEventException : function()
  {
    Y.Assert.fail('not implemented');
    
  }

 });
})();
