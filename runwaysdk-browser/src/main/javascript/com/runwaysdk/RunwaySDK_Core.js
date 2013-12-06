/*
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
 */
/**
 * RunwaySDK Javascript Core library.
 * 
 * @author Terraframe
 */
(function(){
  var rootPackage = 'com.runwaysdk.';
  var Mojo = {
  
    // protected namespace for all classes
    $ : {},
    
    // core constants
    ROOT_PACKAGE : rootPackage,
    STRUCTURE_PACKAGE : rootPackage+'structure.',
    EVENT_PACKAGE : rootPackage+'event.',
    
    // toString constants used for type checking
    IS_OBJECT_TO_STRING : Object.prototype.toString.call({}),
    IS_ARRAY_TO_STRING : Object.prototype.toString.call([]),
    IS_FUNCTION_TO_STRING : Object.prototype.toString.call(function(){}),
    IS_DATE_TO_STRING : Object.prototype.toString.call(new Date()),
    IS_STRING_TO_STRING : Object.prototype.toString.call(''),
    IS_NUMBER_TO_STRING : Object.prototype.toString.call(0),
    IS_BOOLEAN_TO_STRING : Object.prototype.toString.call(true),
    
    META_CLASS_GETTER : 'getMetaClass',
    
    // general purpose empty function
    emptyFunction : function(){},
    
    // reference to global object (e.g., window)
    GLOBAL : (function(){ return this; })()
  };
  
  // Make Mojo visible to the global namespace // FIXME add until end in case of error
  Mojo.GLOBAL.Mojo = Mojo;
  
  var isObject = function(o)
  {
    return  o != null && Object.prototype.toString.call(o) === Mojo.IS_OBJECT_TO_STRING;
  };

  var isArray = function(o)
  {
    return o != null && Object.prototype.toString.call(o) === Mojo.IS_ARRAY_TO_STRING;
  };

  var isFunction = function(o)
  {
    return o != null && Object.prototype.toString.call(o) === Mojo.IS_FUNCTION_TO_STRING;
  };

  var isDate = function(o)
  {
    return o != null && Object.prototype.toString.call(o) === Mojo.IS_DATE_TO_STRING;
  };

  var isString = function(o)
  {
    return o != null && Object.prototype.toString.call(o) === Mojo.IS_STRING_TO_STRING;
  };

  var isNumber = function(o)
  {
    return o != null && Object.prototype.toString.call(o) === Mojo.IS_NUMBER_TO_STRING;
  };
  
  var isBoolean = function(o)
  {
    return o != null && Object.prototype.toString.call(o) === Mojo.IS_BOOLEAN_TO_STRING;
  };
  
  var isUndefined = function(o)
  {
    return typeof o === 'undefined';
  };
  
  var isElement = function(o) {
    return Mojo.Util.isValid(o) && o instanceof Mojo.GLOBAL.Element;
  };
  
  var isValid = function(o)
  {
    return o != null;
  };
  
  Mojo.SUPPORTS_NATIVE_PARSING = Mojo.GLOBAL.JSON != null && isFunction(Mojo.GLOBAL.JSON.parse) && isFunction(Mojo.GLOBAL.JSON.stringify);
  
  var _isInitialized = false;
  var _classes = {};
  var _pseudoConstructor = function(){};
  var _native = []; // array of native bootstrapping classes
  
  var meta = {

    newInstance : function(type)
    {
      if (!Mojo.Meta.classExists(type)) 
      {
        throw new Exception("Unable to newInstance " + type + ". The specified class does not exist.");
      }
      
      var klass = _classes[type];
      var args = [].splice.call(arguments, 1, arguments.length);
      
      var obj = new klass(_pseudoConstructor);
      klass.prototype.initialize.apply(obj, args);
      
      return obj;
    },
    
    // FIXME if pattern matches explicitely on one class then return that class instead of an object
    // FIXME this doesn't return nested packages. For example, com.runwaysdk.ui.* won't include com.runwaysdk.ui.fatory.*
    alias : function(pattern, attachTo)
    {
      if (attachTo === Mojo.GLOBAL)
      {
       throw new Exception("Cannot alias classes to the global scope to avoid naming collisions.");  
      }
      
      if (pattern.match(/\*.+/))
      {
       throw new Exception("Invalid alias class specified: "+pattern);  
      }
      
      attachTo = attachTo || {};
      
      var r = '^'+pattern.replace(/\./g, '\\.').replace(/\*/g, '_?[A-Za-z0-9]+')+'$';
      var re = new RegExp(r);
      
      var classNames = Mojo.Meta.getClasses();
      for(var i=0; i<classNames.length; i++)
      {
        var className = classNames[i];
        if(re.test(className))
        {
          var klass = _classes[className];
          attachTo[klass.getMetaClass().getName()] = klass;
        }
      }
      
      return attachTo;
    },
    
    findClass : function(type, startingPackage)
    {
      if (startingPackage == null) {
        return _classes[type];
      }
      else {
        var parts = type.split(".");
        var ref = startingPackage;
      
        for (var i = 0; i < parts.length; i++) {
          if (ref != null) {
            ref = ref[parts[i]];
          }
          else {
            return null;
          }
        }
      
        return ref;
      }
    },
    
    classCount : function()
    {
      return Mojo.Util.getKeys(_classes, true).length;
    },
    
    _buildPackage : function(packageName, alias)
    {
      if(packageName == null || packageName === '')
      {
        return alias;
      }
    
      var parts = packageName.split(".");

      var currentBuild = alias;
      for(var i=0; i<parts.length; i++)
      {
        var part = parts[i];

        if(!currentBuild[part])
        {
          currentBuild[part] = {};
        }

        currentBuild = currentBuild[part];
      }

      return currentBuild;      
    },
    
    dropClass : function(type)
    {
      // FIXME drop all subclasses from tree (maybe add destroy() method to MetaClass that drops its own children)
      // FIXME remove all aliases and shorthand
      if (!Mojo.Meta.classExists(type))
      {
        throw new Exception("Unable to dropClass " + type + ". The specified class does not exist.");
      }
      
      delete _classes[type];
      
      var parts = type.split(".");
      
      // Delete global reference
      var pack = Mojo.GLOBAL;
      for (var i = 0; i < parts.length-1; i++) {
        pack = pack[parts[i]];
      }
      delete pack[ parts[parts.length-1] ];
      
      // Delete Mojo.$ reference
      var pack = Mojo.$;
      for (var i = 0; i < parts.length-1; i++) {
        pack = pack[parts[i]];
      }
      delete pack[ parts[parts.length-1] ];
    },
    
    getClasses : function()
    {
      return Mojo.Util.getKeys(_classes, true);
    },
    
    classExists : function(type)
    {
      return _classes.hasOwnProperty(type);
    },
    
    _makeSingleton : function(klass)
    {
      // block normal instantiation
      var sInitialize = klass.prototype.initialize;
      klass.prototype.initialize = function(){
     
        var message = "Cannot instantiate the singleton class ["+this.getMetaClass().getQualifiedName()+"]. " +
          "Use the static [getInstance()] method instead.";
        throw new Exception(message);        
      };
      
      klass.getInstance = (function(sInit){
        
        var instance = null;
        
        return function(){
          
          if(instance == null)
          {
            // TODO use something other than function swapping?
            var temp = klass.prototype.initialize;
            klass.prototype.initialize = sInit;
            instance = new klass();
            klass.prototype.initialize = temp;
          }
 
          return instance;      
        };
      })(sInitialize);
        
      return {name : 'getInstance', isStatic : true, isConstructor : false,
        method : klass.getInstance, klass: klass};  
    },
    
    _createConstructor : function()
    {
      return function(){
      
        if(_isInitialized && this.getMetaClass().isAbstract())
        {
          var msg = "Cannot instantiate the abstract class ["+this.getMetaClass().getQualifiedName()+"].";
          throw new Exception(msg);
        }
        
        this.__context = {}; // super context
        
        if(arguments.length === 1 && arguments[0] === _pseudoConstructor)
        {
          _pseudoConstructor(); // for "reflective" newInstance()
                      // calls.
        }
        else
        {
          this.initialize.apply(this, arguments);
        }
      };      
    },
    
    _addOverride : function(m)
    {
      return function(){
        // find the next different method on the superclass prototype
        // and execute it because it is the overridden super method.
        var current = this.__context[m] || this.constructor;
        var next = current.getMetaClass().getSuperClass();
        
        while(current.prototype[m] === next.prototype[m])
        {
          next = next.getMetaClass().getSuperClass();
        }
        
        this.__context[m] = next;
        
        var retObj = next.prototype[m].apply(this, arguments);
        
        this.__context[m] = current;
        
        return retObj;
      };    
    },
    
    _addMethod : function(klass, superClass, methodName, definition)
    {
      var isFunc = isFunction(definition);
      var method = isFunc ? definition : definition.Method;
    
      // add instance method to the prototype
      klass.prototype[methodName] = method;
      
      // add override accessor if the parent class defines the same method
      if(superClass !== Object && isFunction(superClass.prototype[methodName]))
      {
        var superName = '$'+methodName;
        klass.prototype[superName] = this._addOverride(methodName);
      }
      
      return {name : methodName, isStatic : false, isAbstract : (!isFunc && definition.IsAbstract),
          isConstructor : (methodName === 'initialize'), method : method, klass: klass,
          enforceArity: (definition.EnforceArity || false)};
    },
    
    _newType : function(metaRef, qualifiedName, def, isIF)
    {
      try {
      
        if (!isString(qualifiedName) || qualifiedName.length === 0) {
          throw new Exception('The first parameter must be a valid qualified type name.');
        }
        else 
          if (def != null && !isObject(def)) {
            throw new Exception('The second parameter must be a configuration object literal.');
          }
        
        // Set type defaults
        def = def ||
        {};
        
        var extendsClass = null;
        var constants = {};
        var instances = {};
        
        var statics = {};
        var isSingleton = false;
        var isAbstract = false;
        var interfaces = [];
        
        // override defaults and validate properties
        for (var prop in def) {
          if (def.hasOwnProperty(prop)) {
            switch (prop) {
              case 'Extends':
                if (def[prop] == null) {
                  var classOrIF = "class";
                  if (isIF) {
                    classOrIF = "interface";
                  }
                  throw new Exception('The ' + classOrIF + ' [' + qualifiedName + '] cannot extend a null or undefined ' + classOrIF + '.');
                }
                extendsClass = def[prop];
                break;
              case 'Constants':
                constants = def[prop];
                break;
              case 'Instance':
                instances = def[prop];
                break;
              case 'Static':
                if (isIF) {
                  throw new Exception('The interface [' + qualifiedName + '] cannot define static properties or methods.');
                }
                statics = def[prop];
                break;
              case 'IsSingleton':
                if (isIF) {
                  throw new Exception('The interface [' + qualifiedName + '] cannot be a singleton.');
                }
                isSingleton = def[prop];
                break;
              case 'IsAbstract':
                if (isIF) {
                  throw new Exception('The interface [' + qualifiedName + '] cannot be abstract.');
                }
                isAbstract = def[prop];
                break;
              case 'Implements':
                if (isIF) {
                  throw new Exception('The interface [' + qualifiedName + '] cannot implement other Interfaces.');
                }
                
                var ifs = def[prop];
                if (!isArray(ifs)) {
                  ifs = [ifs];
                }
                
                for (var i = 0; i < ifs.length; i++) {
                  var IF = ifs[i];
                  var ifKlass = isString(IF) ? _classes[IF] : IF;
                  if (ifKlass != null && ifKlass.getMetaClass().isInterface()) {
                    interfaces.push(ifKlass);
                  }
                  else {
                    throw new Exception('The class [' + qualifiedName + '] cannot implement the class [' + (ifKlass != null ? ifKlass.getQualifiedName() : null) + '].');
                  }
                }
                
                break;
              default:
                throw new Exception('The property [' + prop + '] on type [' + qualifiedName + '] is not recognized.');
            }
          }
        }
        
        var superClass;
        if (isFunction(extendsClass)) {
          superClass = extendsClass;
        }
        else 
          if (isString(extendsClass)) {
            superClass = _classes[extendsClass];
          }
          else {
            superClass = Base;
          }
        
        if (!superClass) {
          throw new Exception('The class [' + qualifiedName + '] does not extend a valid class.');
        }
        
        // attach the package/class to the alias
        var packageName;
        var className;
        if (/\./.test(qualifiedName)) {
          packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
          className = qualifiedName.substring(packageName.length + 1);
        }
        else {
          packageName = '';
          className = qualifiedName;
        }
        
        // make sure a constructor exists
        if (!isIF && !instances.initialize) {
          instances.initialize = function(){
          };
        }
        else 
          if (isIF && instances.initialize) {
            throw new Exception('The interface [' + qualifiedName + '] cannot define an initialize() constructor.');
          }
          else 
            if (isIF) {
              instances.initialize = function(obj){
                this.getMetaClass()._enforceAnonymousInnerClass(obj);
                Mojo.Util.copy(obj, this);
              };
            }
        
        // wrap the constructor function
        var klass = metaRef._createConstructor();
        
        // add the namespace to the global object and Mojo.$
        var namespace = metaRef._buildPackage(packageName, Mojo.GLOBAL);
        namespace[className] = klass;
        
        namespace = metaRef._buildPackage(packageName, Mojo.$);
        namespace[className] = klass;
        
        _classes[qualifiedName] = klass;
        
        // temp function is used for inheritance instantiation, to
        // avoid calling actual class constructor
        var temp = function(){
        };
        temp.prototype = superClass.prototype;
        klass.prototype = new temp();
        
        // reset constructor to point to the class, such that
        // new A().constructor === A
        klass.prototype.constructor = klass;
        
        // config obj for MetaClass constructor
        var config = {
          packageName: packageName,
          className: className,
          klass: klass,
          superClass: superClass,
          instanceMethods: {},
          staticMethods: {},
          isAbstract: isAbstract,
          isInterface: isIF,
          qualifiedName: qualifiedName,
          isSingleton: isSingleton,
          interfaces: interfaces,
          constants: []
        };
        
        for (var m in instances) {
          var methodConfig = metaRef._addMethod(klass, superClass, m, instances[m]);
          config.instanceMethods[m] = methodConfig;
        }
        
        if (isSingleton) {
          var methodDef = this._makeSingleton(klass);
          config.staticMethods.getInstance = methodDef;
        }
        
        // add constants
        for (var c in constants) {
          config.constants.push({
            name: c,
            value: constants[c]
          });
        }
        
        // add static methods
        for (var m in statics) {
          if (isFunction(statics[m])) {
            if (statics[m].IsAbstract) {
              throw new Exception("The method " + m + " defined on the class " + className + " cannot be both static and abstract.");
            }
            
            config.staticMethods[m] = {
              name: m,
              isStatic: true,
              isAbstract: false,
              isConstructor: false,
              method: statics[m],
              klass: klass,
              enforceArity: false
            };
          }
          else {
            // FIXME wrap static props in a Property class and have them
            // optionally
            // inherited (or use visibility modifiers?)
            klass[m] = statics[m];
          }
        }
        
        // attach the metadata Class
        if (_native !== null) {
          // MetaClass will be constructed later to complete bootstrapping
          klass.__metaClass = config;
          klass.prototype.__metaClass = config;
          _native.push(klass);
        }
        else {
          klass.__metaClass = new MetaClass(config);
          klass.prototype.__metaClass = klass.__metaClass;
        }
        
      } 
      catch (e) {
        if (this.classExists(qualifiedName)) {
          this.dropClass(qualifiedName);
        }
        throw e;
      }
      
      return klass;      
    },
    
    newInterface : function(qualifiedName, definition)
    {
      var metaRef = Mojo.Meta || this;
      return metaRef._newType(metaRef, qualifiedName, definition, true);
    },
    
    newClass : function(qualifiedName, definition)
    {
      var metaRef = Mojo.Meta || this;
      return metaRef._newType(metaRef, qualifiedName, definition, false);
    }
  };


  var Base = meta.newClass(Mojo.ROOT_PACKAGE+'Base', {

  IsAbstract : true,
  
  Extends : Object,

  Instance : {

    initialize : function()
    {
      // Default Constructor
      this.__hashCode = null;
    },
  
    equals : function(obj)
    {
      return this === obj;
    },
    
    getHashCode : function()
    {
      if(this.__hashCode == null)
      {
        this.__hashCode = Mojo.Util.generateId(16);
      }
      
      return this.__hashCode;
    },
  
    clone : function()
    {
      var args = [this.getMetaClass().getQualifiedName()].concat(Array.prototype.splice.call(arguments, 0, arguments.length));
      return Mojo.Meta.newInstance.apply(this, args);
    },
    
    valueOf : function()
    {
      return this;
    },
    
    toString : function()
    {
      return '['+this.getMetaClass().getQualifiedName()+'] : ['+this.getHashCode()+']';
    },
    
    addEventListener : function(type, listener, obj, context, capture)
    {
      type = Mojo.Util.isFunction(type) ? type.getMetaClass().getQualifiedName() : type;
      Mojo.$.com.runwaysdk.event.Registry.getInstance().addEventListener(this, type, listener, obj, context, capture);
    },

    removeEventListener : function(type, listener, capture)
    {
      type = Mojo.Util.isFunction(type) ? type.getMetaClass().getQualifiedName() : type;
      Mojo.$.com.runwaysdk.event.Registry.getInstance().removeEventListener(this, type, listener, capture);
    },
    
    removeEventListeners : function(type)
    {
      type = Mojo.Util.isFunction(type) ? type.getMetaClass().getQualifiedName() : type;
      Mojo.$.com.runwaysdk.event.Registry.getInstance().removeEventListeners(this, type);
    },
    
    removeAllEventListeners : function()
    {
      Mojo.$.com.runwaysdk.event.Registry.getInstance().removeAllEventListeners(this);
    },
    
    hasEventListener : function(type, listener, useCapture)
    {
      type = Mojo.Util.isFunction(type) ? type.getMetaClass().getQualifiedName() : type;
      return Mojo.$.com.runwaysdk.event.Registry.getInstance().hasEventListener(this, type, listener, useCapture);
    },

    /**
     * Dispatches the given event. Note that custom events do not support
     * a capturing phase.
     */
    dispatchEvent : function(evt)
    {
      // FIXME handle dispatching DOM events?
    
      // set the target to this object if the event is just being dispatched
      if(evt.getEventPhase() === EventIF.AT_TARGET)
      {
        evt._setTarget(this);
      }
      
      evt._setCurrentTarget(this);
      
      // dispatch the event for all listeners of this object (the current target)
      Mojo.$.com.runwaysdk.event.Registry.getInstance().dispatchEvent(evt);
      
      if(evt.getTarget().equals(this) && !evt.getPreventDefault())
      {
        evt.defaultAction();
      }
      
      return !evt.getPreventDefault();
    },
    
    destroy : function() {
      this.dispatchEvent(new DestroyEvent(this));
      this.removeAllEventListeners();
    }
  }
});

var MetaClass = meta.newClass(Mojo.ROOT_PACKAGE+'MetaClass', {

  Instance : {
  
    initialize : function(config)
    {
      this._packageName = config.packageName;
      this._className = config.className;
      this._isAbstract = config.isAbstract;
      this._isInterface = config.isInterface;
      this._isSingleton = config.isSingleton;
      this._klass = config.klass;
      this._superClass = config.superClass;
      this._qualifiedName = config.qualifiedName;
      this._subclasses = {};
      this._interfaces = config.interfaces;
      
      var notBase = this._superClass !== Object;  
      
      this._addInstanceMethods(notBase, config.instanceMethods);
      this._addStaticMethods(notBase, config.staticMethods);      
      this._addConstants(notBase, config.constants);
      
      if(notBase)
      {
        this._superClass.getMetaClass()._addSubClass(this._qualifiedName, this._klass);
      }
      
      this._addMetaClassMethod();
      
      if(_isInitialized && !this._isInterface)
      {
        this._enforceInterfaceMethods();
      }
    },
    
    _addInstanceMethods : function(notBase, tInstances)
    {
      var mKlass = Method;
      
      this._instanceMethods = {};
      var abstractMethods = {};
      if(notBase)
      {
        // instance methods will be copied via prototype
        var pInstances = this._superClass.getMetaClass().getInstanceMethods(true);
        for(var i in pInstances)
        {
          var method = pInstances[i];
          this._instanceMethods[i] = method;
          
          if(method.isAbstract())
          {
            abstractMethods[i] = method;
          }
        }
      }
        
      for(var i in tInstances)
      {
        var definition = tInstances[i];
      
        // Check for a method override
        if(this._instanceMethods.hasOwnProperty(i))
        {
          var overridden = this._instanceMethods[i];
          definition.overrideKlass = definition.klass;
          definition.klass = overridden.getDefiningClass();
        }
        
        this._instanceMethods[i] = new mKlass(definition, this);
        
        if(i in abstractMethods)
        {
          delete abstractMethods[i]; // abstract method implemented!
        }
      }
      
      // Make sure all abstract methods are implemented
      if(!this._isAbstract)
      {
        var unimplemented = [];
        for(var i in abstractMethods)
        {
          if(abstractMethods.hasOwnProperty(i))
          {
            unimplemented.push(abstractMethods[i].getName());
          }
        }
        
        if(unimplemented.length > 0)
        {
          var msg = "The class ["+this._qualifiedName+"] must " + 
            "implement the abstract method(s) ["+unimplemented.join(', ')+"].";
          throw new Exception(msg);
        }
      }
    },
    
    _addStaticMethods : function(notBase, tStatics)
    {
      var mKlass = Method;
      
      this._staticMethods = {};
      
      if(notBase)
      {
        // static methods must be explicitly copied
        var pStatics = this._superClass.getMetaClass().getStaticMethods(true);
        for(var i in pStatics)
        {
          var mStatic = pStatics[i];
          this._staticMethods[mStatic.getName()] = mStatic;
          
          this._klass[mStatic.getName()] = mStatic.getMethod();
        }
      }
        
      for(var i in tStatics)
      {
        var definition = tStatics[i];
      
        if(this._staticMethods.hasOwnProperty(i))
        {
          var overridden = this._staticMethods[i];
          definition.overrideKlass = definition.klass;
          definition.klass = overridden.getDefiningClass();
        }
        
        // add the method metadata
        var method = new mKlass(definition, this);
        this._staticMethods[i] = method;
        
        // add the actual method to the class
        this._klass[i] = definition.method;
      }
    },
    
    _addConstants : function(notBase, constants)
    {
      this._constants = {};
      var cKlass = Constant;
      if(notBase)
      {
        var pConstants = this._superClass.getMetaClass().getConstants(true);
        for(var i in pConstants)
        {
          if(pConstants.hasOwnProperty(i))
          {
            this._constants[i] = pConstants[i];
          }
        }
      }
      
      for(var i=0; i<constants.length; i++)
      {
        var constObj = new cKlass(constants[i]);
        
        if(notBase && this._constants[constObj.getName()])
        {
        // FIXME remove _setOverride and use same methodology as instance/static
    // methods above
          constObj._setDefiningClass(this._constants[constObj.getName()].getDefiningClass());
          constObj._setOverrideClass(this._klass);
        }
        else
        {
          constObj._setDefiningClass(this._klass);
        }
        
        this._constants[constObj.getName()] = constObj;
        this._klass[constObj.getName()] = constObj.getValue();
      }
    },
    
    _addMetaClassMethod : function()
    {
      var mName = Mojo.META_CLASS_GETTER;
      
      // Each class constructor function and instance gets
      // a method to return this Class instance.
      this._klass[mName] = (function(metaClass){
        return function() {
          return metaClass;
        };
      })(this);
      
      this._klass.prototype[mName] = this._klass[mName];
      
      var baseClass = Base;
      this._instanceMethods[mName] = new Method({
        name : mName,
        isStatic : false,
        isAbstract : false, 
        isConstructor : false,
        method : this._klass[mName],
        klass: baseClass,
        overrideKlass : this._klass,
        enforceArity : false
      }, this);
      
     this._staticMethods[mName] = new Method({
        name : mName,
        isStatic : true,
        isAbstract : false, 
        isConstructor : false,
        method : this._klass[mName],
        klass: baseClass,
        overrideKlass : this._klass,
        enforceArity : false
      }, this);
    },
    
    _addSubClass : function(qualifiedName, klass)
    {
      this._subclasses[qualifiedName] = klass;
    },
    
    isSingleton : function()
    {
      return this._isSingleton;
    },
    
    isInterface : function()
    {
      return this._isInterface;
    },
    
    getInterfaces : function()
    {
      return this._interfaces;
    },
    
    getSubClasses : function(asMap)
    {
      if(asMap)
      {
        return this._subclasses;
      }
      else
      {
        var values = [];
        for(var i in this._subclasses)
        {
          values.push(this._subclasses[i]);
        }
        return values;
      }
    },
    
    getConstants : function(asMap)
    {
      if(asMap)
      {
        return this._constants;
      }
      else
      {
        var values = [];
        for(var i in this._constants)
        {
          values.push(this._constants[i]);
        }
        return values;
      }
    },
    
    hasConstant : function(name)
    {
      return this._constants[name] != null;
    },
    
    getConstant: function(name)
    {
      return this._constants[name];
    },

    getMethod : function(name)
    {
      // FIXME will not work with instance/static method of same name
      return this._instanceMethods[name] || this._staticMethods[name];
    },
    
    hasInstanceMethod : function(name)
    {
      return this._instanceMethods[name] != null;
    },
    
    getInstanceMethod : function(name)
    {
      return this._instanceMethods[name];
    },
    
    hasStaticMethod : function(name)
    {
      return this._staticMethods[name] != null;
    },
    
    getStaticMethod : function(name)
    {
      return this._staticMethods[name];
    },
    
    getInstanceMethods : function(asMap)
    {
      if(asMap)
      {
        return this._instanceMethods;
      }
      else
      {
        var arr = [];
        for(var i in this._instanceMethods)
        {
          if(true || this._instanceMethods.hasOwnProperty(i))
          {
            arr.push(this._instanceMethods[i]);
          } 
        }
        
        return arr;
      }
    },
    
    getStaticMethods : function(asMap)
    {
      if(asMap)
      {
        return this._staticMethods;
      }
      else
      {
        var arr = [];
        for(var i in this._staticMethods)
        {
          if(true || this._staticMethods.hasOwnProperty(i))
          {
            arr.push(this._staticMethods[i]);
          }
        }
        
        return arr;
      }
    },
    
    isAbstract : function()
    {
      return this._isAbstract;
    },
    
    getMethods : function()
    {
      return [].concat(this.getInstanceMethods(false), this.getStaticMethods(false));
    },
  
    getPackage : function()
    {
      return this._packageName;
    },
    
    getName : function()
    {
      return this._className;
    },
    
    getQualifiedName : function()
    {
      return this._qualifiedName;
    },
    
    _getClass : function(klass)
    {
      if(klass instanceof this.constructor)
      {
        return klass;
      }
      else if(Mojo.Util.isFunction(klass) || klass instanceof Base)
      {
        return klass.getMetaClass();
      }
      else if(Mojo.Util.isString(klass))
      {
        var foundClass = Mojo.Meta.findClass(klass);
        if(foundClass)
        {
          return foundClass.getMetaClass();
        }
        else
        {
          return null;
        }
      }
      else
      {
        return null;
      }
    },
    
    isSuperClassOf : function(klass)
    {
      var classObj = this._getClass(klass); 
      
      if(this === classObj)
      {
        return true;
      }
    
      var superClass = classObj.getSuperClass();
      while(superClass !== Object)
      {
        if(superClass.getMetaClass() === this)
        {
          return true;
        }
        
        superClass = superClass.getMetaClass().getSuperClass();
      }
      
      return false;
    },
    
    isSubClassOf : function(klass)
    {
      var classObj = this._getClass(klass); 
      return classObj.isSuperClassOf(this);
    },
    
    getFunction : function()
    {
      return this._klass;
    },
    
    _enforceAnonymousInnerClass : function(obj)
    {
      var IFs = [this._klass];
      var parentClass = this._superClass;
      while(parentClass.getMetaClass().isInterface())
      {
        var parentMeta = parentClass.getMetaClass();
        IFs.push(parentMeta.getFunction());
        parentClass = parentMeta.getSuperClass();
      }
      
      var toImplement = [];
      for(var i=0; i<IFs.length; i++)
      {
        toImplement = toImplement.concat(IFs[i].getMetaClass().getInstanceMethods());
      }
      
      var unimplemented = [];
      for(var i=0; i<toImplement.length; i++)
      {
        var method = toImplement[i];
        if(!method.getDefiningClass().getMetaClass().isInterface())
        {
          continue;
        }
        
        var name = method.getName();
        if(!(isFunction(obj[name])) ||
            !obj.hasOwnProperty(name))
        {
          unimplemented.push(name);
        }
      }
      
      if(unimplemented.length > 0)
      {
        var msg = "The anonymous inner class ["+obj+"] must " + 
        "implement the interface method(s) ["+unimplemented.join(', ')+"].";
        throw new Exception(msg);
      }
    },
    
    _enforceInterfaceMethods : function()
    {
      var IFs = this.getInterfaces();
      var parentClass = this._superClass;
      while(parentClass !== Object)
      {
        var parentMeta = parentClass.getMetaClass();
        IFs = IFs.concat(parentMeta.getInterfaces());
        parentClass = parentMeta.getSuperClass();
      }
      
      var toImplement = [];
      for(var i=0; i<IFs.length; i++)
      {
        toImplement = toImplement.concat(IFs[i].getMetaClass().getInstanceMethods());
      }
      
      var unimplemented = [];
      for(var i=0; i<toImplement.length; i++)
      {
        var method = toImplement[i];
        if(!method.getDefiningClass().getMetaClass().isInterface())
        {
          continue;
        }
        
        var name = method.getName();
        var isDefined = (name in this._instanceMethods);
        if(!this._isAbstract && !isDefined)
        {
          unimplemented.push(name);
        }
        else if(isDefined && method.enforcesArity())
        {
          var implemented = this._instanceMethods[name];
          if(method.getArity() !== implemented.getArity())
          {
            var ifMethod = method.getDefiningClass().getMetaClass().getQualifiedName()+'.'+name;
            var msg = "The method ["+this._qualifiedName+"."+name+"] must " + 
            "define ["+method.getArity()+"] arguments as required by the interface method ["+ifMethod+"].";
            throw new Exception(msg);
          }
        }
      }
      
      if(unimplemented.length > 0)
      {
        var msg = "The class ["+this._qualifiedName+"] must " + 
        "implement the interface method(s) ["+unimplemented.join(', ')+"].";
        throw new Exception(msg);
      }
    },    
    
    doesImplement : function(IFinput)
    {
      if(this.isInterface() && this.isSubClassOf(IFinput))
      {
        // check for anonymous inner classes
        return true;
      }
      else if(!this.isInterface())
      {
        var klass = this._klass;
        while(klass !== Object)
        {
          var meta = klass.getMetaClass();
          var IFs = meta.getInterfaces();
          for(var i=0; i<IFs.length; i++)
          {
            var IF = IFs[i];
            if(IF.getMetaClass().isSubClassOf(IFinput))
            {
              return true;
            }
          }
          klass = meta.getSuperClass();
        }
        
        return false;
      }
      else
      {
        return false;
      }
    },
    
    isInstance : function(obj)
    {
      if(!isObject(obj))
      {
        return false;
      }
      
      // cover regular classes and anonymous inner classes
      if(obj instanceof this._klass)
      {
        return true;
      }

      if(this.isInterface())
      {
        if(obj instanceof Base)
        {
          // compare the object's interfaces to *this* IF class.
          var klass = obj.constructor;
          while(klass !== Object)
          {
            var meta = klass.getMetaClass();
            var IFs = meta.getInterfaces();
            for(var i=0; i<IFs.length; i++)
            {
              var IF = IFs[i];
              if(IF.getMetaClass().isSubClassOf(this._klass))
              {
                return true;
              }
            }
            klass = meta.getSuperClass();
          }
          
          return false;
        }
        else
        {
          return false;
        }
      }
      else
      {
        return false; // we don't know what this object is
      }
    },
    
    getSuperClass : function()
    {
      return this._superClass;
    },
    
    newInstance : function()
    {
      var args = [this.getQualifiedName()].concat([].splice.call(arguments, 0, arguments.length));
      return Mojo.Meta.newInstance.apply(this, args);
    },
    
    toString : function()
    {
      return '[MetaClass] ' + this.getQualifiedName();
    },
    
    toJSON : function ()
    {
      return undefined;
    },
    
    equals : function(obj) {
      if (this.$equals(obj)) {
        return true;
      }
      
      if (obj instanceof MetaClass && this.getQualifiedName() === obj.getQualifiedName()) {
        return true;
      }
      
      return false;
    }
  }
});

var Constant = meta.newClass(Mojo.ROOT_PACKAGE+"Constant", {
  
  Instance : {
  
    initialize : function(config)
    {
      this._name = config.name;
      this._value = config.value;
      this._klass = null;
      this._overrideKlass = null;
    },
    
    _setDefiningClass : function(klass)
    {
      this._klass = klass;
    },
    
    _setOverrideClass : function(klass)
    {
      this._overrideKlass = klass;
    },
    
    getName : function()
    {
      return this._name;
    },
    
    getValue : function()
    {
      return this._value;
    },
    
    getDefiningClass : function()
    {
      return this._klass;
    },
    
    isOverride : function()
    {
      return this._overrideKlass !== null;
    },
    
    getOverrideClass : function()
    {
      return this._overrideKlass;
    }
  }
  
});

var Method = meta.newClass(Mojo.ROOT_PACKAGE+'Method', {

  Instance : {
  
    initialize : function(config, metaClass)
    {
      this._name = config.name;
      this._isStatic = config.isStatic;
      this._isConstructor = config.isConstructor;
      this._klass = config.klass || null;
      this._overrideKlass = config.overrideKlass || null;
      this._isAbstract = config.isAbstract;
      this._enforceArity = config.enforceArity || false;
      
      if(_isInitialized && !metaClass.isAbstract()
         && this._isAbstract)
      {
        var msg = "The non-abstract class ["+metaClass.getQualifiedName()+"] cannot " + 
          "cannot declare the abstract method ["+this._name+"].";
        throw new Exception(msg);
      }
      
      if(this._isAbstract)
      {
        this._createAbstractMethod();
        this._arity = 0;
      }
      else
      {
        this._arity = config.method.length;
      }
    },
    
    _createAbstractMethod : function()
    {
      // Add the abstract method to always throw an error. This
      // will replace any method already on the prototype.
      this._klass.prototype[this._name] = (function(name){
        return function(){

          var definingClass = this.getMetaClass().getMethod(name).getDefiningClass().getMetaClass().getQualifiedName();

          var msg = "Cannot invoke the abstract method ["+name+"] on ["+definingClass+"].";
          throw new Exception(msg);
        };
      })(this._name);
    },
    
    isAbstract : function()
    {
      return this._isAbstract;
    },
    
    enforcesArity : function()
    {
      return this._enforceArity;
    },
    
    isConstructor : function()
    {
      return this._isConstructor;
    },
    
    getArity : function()
    {
      return this._arity;
    },
    
    isOverride : function()
    {
      return !this._isStatic && this._overrideKlass !== null;
    },
    
    isHiding : function()
    {
      return this._isStatic && this._overrideKlass !== null;
    },
    
    getOverrideClass : function()
    {
      return this._overrideKlass;
    },
    
    getMethod : function()
    {
      var klass = this._overrideKlass || this._klass;
      return this._isStatic ? klass[this._name] : klass.prototype[this._name];
    },
  
    getName : function()
    {
      return this._name;
    },
    
    isStatic : function()
    {
      return this._isStatic;
    },
    
    getDefiningClass : function()
    {
      return this._klass;
    },
    
    toString : function()
    {
      return '[Method] ' + this.getName();
    }
  }
  
});

// Finish bootstrapping the class system
for(var i=0; i<_native.length; i++)
{
  var bootstrapped = _native[i];
  
  // Convert the JSON config __metaClass into a MetaClass instance
  // and re-attach the metadata to the class definition.
  var cClass = new MetaClass(bootstrapped.__metaClass);
  bootstrapped.__metaClass = cClass;
  bootstrapped.prototype.__metaClass = cClass;
}
_native = null;

// convert the Meta object to a class
var metaProps = {};
for(var i in meta)
{
  if(meta.hasOwnProperty(i))
  {
    metaProps[i] = meta[i];
  }
}

var Meta = Mojo.Meta = metaProps.newClass('Mojo.Meta', {
  Static : metaProps  
});
meta = null;

_isInitialized = true;

var FeatureSet = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'FeatureSet', {
  IsSingleton : true,
  Instance : {
    initialize : function()
    {
      var impl = Mojo.GLOBAL.document.implementation;
      this._dom2Events = impl.hasFeature('Events', '2.0');
      this._dom3Events = impl.hasFeature('Events', '3.0');
      
      if(!this._dom2Events && !this._dom3Events)
      {
        throw new Exception('Neither DOM Level 2 nor 3 Events are supported.');
      }
    },
    supportsDOM2Events : function() { return this._dom2Events; },
    supportsDOM3Events : function() { return this._dom3Events; }
  }
});

/**
 * Exception
 * 
 * This is the actual exception that can be thrown and caught. There is no Mojo
 * Java counterpart, so we throw it in the root namespace.
 */
// Create a logger for our exceptions
var exLogger = new Log4js.getLogger("Runway Exception Constructor Logger");
exLogger.setLevel(Log4js.Level.ALL); // this should take a parameter from the clerver
if ( (window.console && window.console.log) || window.opera )
{
  exLogger.addAppender(new Log4js.BrowserConsoleAppender());
}
else {
  exLogger.addAppender(new Log4js.ConsoleAppender());
}
var Exception = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'Exception', {

  Extends : Mojo.ROOT_PACKAGE+'Base',
  
  Instance : {

    initialize : function ()
    {
      this._internalE = null;
      
      if(arguments.length == 1)
      {
        var arg = arguments[0];
        if(arg == null)
        {
          this.localizedMessage = null;
          this.developerMessage = null;
        }
        else if(Mojo.Util.isString(arg))
        {
          this.localizedMessage = arg;
          this.developerMessage = null;
        }
        else if(arg instanceof Mojo.GLOBAL.Error)
        {
          this.localizedMessage = arg.message;
          this.developerMessage = null;
          this._internalE = arg;
        }
        else if(Mojo.Util.isObject(arg)
          && 'localizedMessage' in arg
          && 'developerMessage' in arg)
        {
          this.localizedMessage = arg.localizedMessage;
          this.developerMessage = arg.developerMessage;
        }
        else
        {
          this.localizedMessage = null;
          this.developerMessage = null;
        }
      }
      else if(arguments.length === 2)
      {
        this.localizedMessage = arguments[0];
        this.developerMessage = arguments[1];
      }
      else
      {
        this.localizedMessage = null;
        this.developerMessage = null;
      }
      
      // Make the message public to conform with the Error API
      this.message = this.developerMessage || this.localizedMessage;
      
      
      this._stackTrace = [];
      if(this._internalE === null)
      {
        this._internalE = new Error(); // used to get a stacktrace
      }
      
      //FIXME get cross-browser stacktrace
      if(Mojo.Util.isString(this._internalE.stack)) // Mozilla
      {
      }
      else if(Mojo.Util.isString(this._internalE.stackTrace)) // Opera 10+
      {
      }
      else
      {
      }
      
      // Log it
      var msg = "A new exception was instantiated: " + this.developerMessage;
      exLogger.log(Log4js.Level.INFO, msg, null);
      com.runwaysdk.inspector.Inspector.getLogger().logInfo(msg);
    },
  
    getLocalizedMessage : function() { return this.localizedMessage; },
  
    getMessage : function() { return this.localizedMessage; },
  
    getDeveloperMessage : function() { return this.developerMessage; },
  
    toString : function() { return this.localizedMessage; },
    
    getStackTrace : function()
    {
      return this._stackTrace;
    }
  
  }
});

/**
 * All classes that extend Base implement this Interface such that they can
 * register event listeners and dispatch events.
 */
var EventTarget = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'EventTarget', {
  Instance : {
    addEventListener : function(type, listener, obj, context, capture){},
    removeEventListener : function(type, listener, useCapture){},
    dispatchEvent : function(evt){},
    removeEventListeners : function(target, type){},
    removeAllEventListeners : function(target){},
    hasEventListener : function(target, type, listener, useCapture){}
  }
});

// Manually add the EventTarget interface to Base, which must take place
// after the initial bootstrapping (since EventTarget extends Base). This
// breaks encapsulation and should only be done internally.
Base.getMetaClass()._interfaces.push(EventTarget);
Base.getMetaClass()._enforceInterfaceMethods();

var Util = Mojo.Meta.newClass('Mojo.Util', {

  IsAbstract : true,
  
  Instance : {
  
    initialize : function(){}
  },

  Static : {
  ISO8601_REGEX : "^([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2}):([0-9]{2})([-+])([0-9]{2})([0-9]{2})$",
    
    isObject : isObject,

    isArray : isArray,

    isFunction : isFunction,

    isDate : isDate,

    isString : isString,

    isNumber : isNumber,
    
    isBoolean : isBoolean,
    
    isUndefined : isUndefined,
    
    isElement : isElement,
    
    isValid : isValid,
    
    bind : function(thisRef, func)
    {
      if (!Mojo.Util.isFunction(func))
      {
        throw new Exception("Mojo.Util.bind: Unable to bind,  the second parameter is not a function.");
      }
    
      var args = [].splice.call(arguments, 2, arguments.length);
      return function(){
        return func.apply(thisRef, args.concat([].splice.call(arguments, 0, arguments.length)));
      };
    },
    
    curry : function(func)
    {
      var args = [].splice.call(arguments, 1, arguments.length);
      return function(){
        return func.apply(this, args.concat([].splice.call(arguments, 0, arguments.length)));
      };
    },
    
    /**
   * Extracts all script tag contents and returns a string of executable code
   * that can be evaluated.
   */
    extractScripts : function(html)
    {
      var scripts = html.match(/<script\b[^>]*>[\s\S]*?<\/script>/img);
      var executables = [];
      if(scripts != null)
      {
        for(var i=0; i<scripts.length; i++)
        {
          var scriptM = scripts[i].match(/<script\b[^>]*>([\s\S]*?)<\/script>/im);
          executables.push(scriptM[1]);
        }
      }

      return executables.join('');
    },

    /**
   * Removes all scripts from the HTML and returns a string of the processed
   * HTML.
   */
    removeScripts : function(html)
    {
      return html.replace(/<script\b[^>]*>[\s\S]*?<\/script>/img, '');
    },
    
    // TODO give credit to
    // http://blog.stevenlevithan.com/archives/faster-trim-javascript
    trim : function(str)
    {
      var str = str.replace(/^\s\s*/, '');
      var ws = /\s/;
      var i = str.length;
      while (ws.test(str.charAt(--i)));
      return str.slice(0, i + 1);
    },
    
    memoize : function(func, context)
    {
      func.memoCache = {};
    
      return function() {
        
        var args = [].splice.call(arguments, 0, arguments.length);
        if(func.memoCache[args])
        {
          return func.memoCache[args];
        }
        else
        {
          func.memoCache[args] = func.apply(context || this, args); 
          return func.memoCache[args];
        }
      };
    },
    
    generateId : function(idSize)
    {
      var result = '';
      idSize = idSize || 32;
      for(var i=0; i<idSize; i++)
      {
        result += Math.floor(Math.random()*16).toString(16);
      } 
      return result;
    },
    
    debounce : function(func, threshold, context, enforceWait)
    {
      var timeout = null;
      var isExec = null;

      return function(){

        if(timeout !== null || enforceWait && isExec)
        {
          return;
        }
      
        timeout = setTimeout(function(){
          clearTimeout(timeout);
          timeout = null;          
        }, threshold || 500);

        isExec = true;
        func.apply(context || this, arguments);
        isExec = false;
      };
    },
    
    setISO8601 : function (date, string, ignoreTimezone)
    {
      var regexp = new RegExp(Mojo.Util.ISO8601_REGEX);

      if(!Mojo.Util.isString(string) || string === '' || !regexp.test(string))
      {
        return false;
      }
            
      var d = string.match(regexp);

      var offset = 0;
      var tempDate = new Date(d[1], 0, 1);

      if (d[2]) { tempDate.setMonth(d[2] - 1); }
      if (d[3]) { tempDate.setDate(d[3]); }
      if (d[4]) { tempDate.setHours(d[4]); }
      if (d[5]) { tempDate.setMinutes(d[5]); }
      if (d[6]) { tempDate.setSeconds(d[6]); }
      if (d[8]) {
          offset = (Number(d[8]) * 60) + Number(d[9]);
          offset *= ((d[7] == '-') ? 1 : -1);
      }
      
      var time = Number(tempDate);

      if(ignoreTimezone !== true)
      {
        offset -= tempDate.getTimezoneOffset();
        time += (offset * 60 * 1000);
      }
      
      date.setTime(Number(time));
      
      return true;
    },

    toISO8601 : function (date, ignoreTimezone)
    {
      /*
     * ISO8601 format: Complete date plus hours, minutes, seconds and a
     * decimal fraction of a second YYYY-MM-DDThh:mm:ssZ (eg
     * 1997-07-16T19:20:30.45-0100)
     */
      var format = 6;
      var offset = date.getTimezoneOffset()/60;
      
      var tempDate = date;
         
      var zeropad = function (num) {
      var value = (num < 0 ? num * -1 : num);
      
        return (value < 10 ? '0' + value : value);
      };

      var str = "";

      // Set YYYY
      str += tempDate.getFullYear();
      // Set MM
      str += "-" + zeropad(tempDate.getMonth() + 1);
      // Set DD
      str += "-" + zeropad(tempDate.getDate());
      // Set Thh:mm
      str += "T" + zeropad(tempDate.getHours()) + ":" + zeropad(tempDate.getMinutes());
      // Set ss
      str += ":" + zeropad(tempDate.getSeconds());        
      // Set TZD
      
      if(!ignoreTimezone)
      {
        str += (offset > 0 ? '-' : '+') + zeropad(offset) + '00';
      }
      
      return str;
    },
    
    extendsBase : function(obj)
    {
      return obj instanceof Base;
    },
    
    /**
     * Tests the input objects for equality, calling Base.equals() if
     * one of the objects extends the com.runwaysdk.Base class. Otherwise,
     * an identity check is performed using ===. This method also treats
     * undefined and null as equal.
     */
    equals : function(obj1, obj2)
    {
      if(obj1 instanceof Base)
      {
        return obj1.equals(obj2);
      }
      else if(obj1 == null && obj2 == null) // null/undefined check
      {
        return true;
      }
      else
      {
        return obj1 === obj2;
      }
    },
    
    /**
   * This JSON object is based on the reference code provided by Douglas
   * Crockford. The original, commented source is located at
   * http://json.org/json2.js.
   */    
    JSON : (function(){

        var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
            escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
            gap,
            indent,
            meta = {    // table of character substitutions
                '\b': '\\b',
                '\t': '\\t',
                '\n': '\\n',
                '\f': '\\f',
                '\r': '\\r',
                '"' : '\\"',
                '\\': '\\\\'
            },
            rep;


        function quote(string) {

            escapable.lastIndex = 0;
            return escapable.test(string) ?
                '"' + string.replace(escapable, function (a) {
                    var c = meta[a];
                    return Mojo.Util.isString(c) ? c :
                        '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                }) + '"' :
                '"' + string + '"';
        }

        function f(n) {
          // Format integers to have at least two digits.
          return n < 10 ? '0' + n : n;
        }

        // Normally we wouldn't modify the prototype of a native object
        // but the spec defines the following behavior for Date serialization.
        if (typeof Date.prototype.toJSON !== 'function') {

          Date.prototype.toJSON = function (key) {

              return isFinite(this.valueOf()) ?
                     this.getUTCFullYear()   + '-' +
                   f(this.getUTCMonth() + 1) + '-' +
                   f(this.getUTCDate())      + 'T' +
                   f(this.getUTCHours())     + ':' +
                   f(this.getUTCMinutes())   + ':' +
                   f(this.getUTCSeconds())   + 'Z' : null;
          };
        }


        function str(key, holder) {

            var i,          // The loop counter.
                k,          // The member key.
                v,          // The member value.
                length,
                mind = gap,
                partial,
                value = holder[key];

            if(typeof value === 'function')
            {
              return undefined;
            }
            
            //var isClass = value instanceof Base;
            
            if (value && typeof value === 'object' &&
                typeof value.toJSON === 'function') {
              value = value.toJSON(key);
            }

            if (typeof rep === 'function') {
                value = rep.call(holder, key, value);
            }
            
            // Special case: if this is a Runway classes then return
            // because it has already been serialized.
            /*
            if(isClass)
            {
              return value;
            }
            */
            
            switch (typeof value) {
            
            case 'string':
                return quote(value);

            case 'number':
                return isFinite(value) ? String(value) : 'null';

            case 'boolean':
            case 'null':

                return String(value);

            case 'object':

                if (!value) {
                    return 'null';
                }
                
                gap += indent;
                partial = [];

                if (Mojo.Util.isArray(value)) {

                    length = value.length;
                    for (i = 0; i < length; i += 1) {
                        partial[i] = str(i, value) || 'null';
                    }

                    v = partial.length === 0 ? '[]' :
                        gap ? '[\n' + gap +
                                partial.join(',\n' + gap) + '\n' +
                                    mind + ']' :
                              '[' + partial.join(',') + ']';
                    gap = mind;
                    return v;
                }

                if (rep && typeof rep === 'object') {
                    length = rep.length;
                    for (i = 0; i < length; i += 1) {
                        k = rep[i];
                        if (typeof k === 'string') {
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                } else {

                    for (k in value) {
                        if (Object.hasOwnProperty.call(value, k)) {
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                }

                v = partial.length === 0 ? '{}' :
                    gap ? '{\n' + gap + partial.join(',\n' + gap) + '\n' +
                            mind + '}' : '{' + partial.join(',') + '}';
                gap = mind;
                return v;
            }
        }

        return {
        
            stringify : function (value, replacer, space) {

                var i;
                gap = '';
                indent = '';

                if (typeof space === 'number') {
                    for (i = 0; i < space; i += 1) {
                        indent += ' ';
                    }

                } else if (typeof space === 'string') {
                    indent = space;
                }

                rep = replacer;
                if (replacer && typeof replacer !== 'function' &&
                        (typeof replacer !== 'object' ||
                         typeof replacer.length !== 'number')) {
                    throw new Exception('Mojo.Util.getJSON');
                }

                return str('', {'': value});
            },


            parse : function (text, reviver) {

                var j;

                function walk(holder, key) {

                    var k, v, value = holder[key];
                    if (value && typeof value === 'object') {
                        for (k in value) {
                            if (Object.hasOwnProperty.call(value, k)) {
                                v = walk(value, k);
                                if (v !== undefined) {
                                    value[k] = v;
                                } else {
                                    delete value[k];
                                }
                            }
                        }
                    }
                    return reviver.call(holder, key, value);
                }

                text = String(text);
                cx.lastIndex = 0;
                if (cx.test(text)) {
                    text = text.replace(cx, function (a) {
                        return '\\u' +
                            ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                    });
                }

                if (/^[\],:{}\s]*$/.
                  test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@').
                  replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']').
                  replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

                    j = eval('(' + text + ')');

                    return typeof reviver === 'function' ?
                        walk({'': j}, '') : j;
                }

                throw new Exception('Mojo.Util.getObject');
            }
            
        };

    })(),
    
    getKeys : function(obj, hasOwnProp)
    {
      var keys = [];
      for(var i in obj)
      {
        if(!hasOwnProp || obj.hasOwnProperty(i))
        {
          keys.push(i);
        }
      }

      return keys;
    },

    getValues : function(obj, hasOwnProp)
    {
      var values = [];
      for(var i in obj)
      {
        if(!hasOwnProp || obj.hasOwnProperty(i))
        {
          values.push(obj[i]);
        }
      }

      return values;
    },

    copy : function(source, dest, hasOwnProp)
    {
      if(Mojo.Util.isObject(source))
      {
        for(var i in source)
        {
          if(!hasOwnProp || source.hasOwnProperty(i))
          {
            dest[i] = source[i];
          }
        }
      }
      
      return dest;
    },
    
    merge : function(source, dest, hasOwnProp)
    {
      if(Mojo.Util.isObject(source))
      {
        for(var i in source)
        {
          if((!hasOwnProp || source.hasOwnProperty(i)) && !(i in dest))
          {
            dest[i] = source[i];
          }
        }
      }
      
      return dest;
    },

    toObject : function(json, reviver)
    {
      if (Mojo.Util.isString(json))
      {
        var useNativeParsing = Mojo.ClientSession.isNativeParsingEnabled();

        if (useNativeParsing && Mojo.SUPPORTS_NATIVE_PARSING)
        {
          return JSON.parse(json, reviver);
        }
        else
        {
          return Mojo.Util.JSON.parse(json, reviver);
        }
      }
      else
      {
        return json;
      }
    },
    
    // alias for toObject()
    getObject : function(json, reviver)
    {
      return Mojo.Util.toObject(json, reviver);
    },

    toJSON : function(obj, replacer)
    {
      var useNativeParsing = Mojo.ClientSession.isNativeParsingEnabled();
      
      // Use the browser's toJSON if it exists
      if (useNativeParsing && Mojo.SUPPORTS_NATIVE_PARSING)
      {
        return JSON.stringify(obj, replacer); 
      }
      else
      {
        // Otherwise use Runway's
        return Mojo.Util.JSON.stringify(obj, replacer);
      }      
    },
    
    // alias for toJSON()
    getJSON : function(obj, replacer)
    {
      return Mojo.Util.toJSON(obj, replacer);
    },

    collectFormValues : function(formId)
    {

      var keyValues = {};
      function collect(elements)
      {
        for(var i=0; i<elements.length; i++)
        {
          var el = elements[i];
          if(el.disabled)
          {
            continue;
          }

          var name = el.name;

          var nodeName = el.nodeName.toLowerCase();
          switch(nodeName)
          {
            case 'select':
              var values = [];
              var options = el.options;
              for(var j=0; j<options.length; j++)
              {
                var option = options[j];
                if(option.selected)
                  values.push(option.value);
              }
              keyValues[name] = values;
              break;
            case 'textarea':
              keyValues[name] = el.value;
              break;
            case 'input':
              var type = el.type.toLowerCase();
              switch(type)
              {
                case 'radio':
                  if(el.checked)
                    keyValues[name] = el.value;
                  break;
                case 'checkbox':
                  if(!keyValues[name])
                    keyValues[name] = [];

                  if(el.checked)
                    keyValues[name].push(el.value);
                  break;
                default:
                  keyValues[name] = el.value;
              }
              break;
          }
        }
      }

      var form = Mojo.Util.isString(formId) ? document.getElementById(formId) : formId;
      collect(form.getElementsByTagName('input'));
      collect(form.getElementsByTagName('select'));
      collect(form.getElementsByTagName('textarea'));
      
      // FIXME use form.elements[] instead and remove inner function

      return keyValues;
    },

    convertMapToQueryString : function(map)
    {
      if(map == null)
      {
        return '';
      }

      var params = [];
      for(var key in map)
      {
        var entry = map[key];
        if(Mojo.Util.isArray(entry))
        {
          for(var i=0; i<entry.length; i++)
          {
            params.push(encodeURIComponent(key) + "[]=" + encodeURIComponent(entry[i]));
          }
        }
        else
        {
          params.push(encodeURIComponent(key) + "=" + encodeURIComponent(entry));
        }
      }

      var queryString = params.join("&");
      return queryString;
    }
  }
  
});

// FIXME iterate over different object types
// Look at prototype 1.6 and
// http://closure-library.googlecode.com/svn/trunk/closure/goog/docs/closure_goog_iter_iter.js.source.html
// for more iter methods and integrated iteration
Mojo.Meta.newClass('Mojo.Iter', {

  Instance : {
  
    initialize : function()
    {
      // todo take in arr/obj/iterable and wrap it to allow iteration functions
    }
  },
  
  Static : {
  
    filter : function(obj, func, context)
    {
      var bound = Mojo.Util.bind((context || this), func);
      var filtered = [];
      Mojo.Iter.forEach(obj, function(item, ind){
        
        if(bound(item, ind))
        {
          filtered.push(item);
        }
      });
      
      return filtered;
    },
    
    forEach : function(obj, func, context)
    {
      var bound = Mojo.Util.bind((context || this), func);
      if(Mojo.Util.isNumber(obj))
      {
        for(var i=0; i<obj; i++)
        {
          bound(i); 
        }
      }
      else if(Mojo.Util.isArray(obj))
      {
        for(var i=0; i<obj.length; i++)
        {
          var item = obj[i];
          bound(item, i);
        }
      }
      else if(Mojo.Util.isObject(obj))
      {
        var keys = Mojo.Util.getKeys(obj);
        for(var i=0; i<keys.length; i++)
        {
          var key = keys[i];
          bound(obj[key], key);
        }
      }
      else if('length' in obj)
      {
        for(var i=0; i<obj.length; i++)
        {
          bound(obj[i], i);
        }
      }
      else
      {
        throw Error('The object cannot be iterated over.');
      }
    },
    
    map : function(obj, func, context)
    {
      var bound = Mojo.Util.bind((context || this), func);
      var mapped = [];
      Mojo.Iter.forEach(obj, function(item, ind){
        
        mapped.push(bound(item, ind));
        
      });
      
      return mapped;
    }
  }
});

Mojo.Meta.newClass('Mojo.ClientRequest', {

  Instance : {
  
    initialize : function(handler){

      Mojo.Util.copy(handler, this);

      this._warnings = [];
      this._information = [];
      this._transport = null;
    },
     
    getMessages : function() { return this._warnings.concat(this._information); },
    
    setWarnings : function(warnings) { this._warnings = warnings; },
    
    getWarnings : function() { return this._warnings; },
    
    setInformation : function(information) { this._information = information; },
    
    getInformation : function() { return this._information; },
    
    getTransport : function() { return this._transport; },
    
    setTransport : function(transport) { this._transport = transport; }
  }
});

var ClientSession = Mojo.Meta.newClass('Mojo.ClientSession', {

  IsSingleton : true,
  
  Instance : {
    initialize : function()
    {
      this._nativeParsingEnabled = true;
      
      // FIXME use constants for the keys
      this._ajaxOptions ={
          'method':'post',
          'contentType':'application/x-www-form-urlencoded',
          'encoding':'UTF-8',
          'asynchronous':true,
          'successRange':[200,299]
      };
      
      this._baseEndpoint = (Mojo.GLOBAL.location.protocol + "//" + Mojo.GLOBAL.location.host  +'/'+ Mojo.GLOBAL.location.pathname.split( '/' )[1] +'/');
    }
  },
  
  Static : {
    
    isNativeParsingEnabled : function() { return Mojo.ClientSession.getInstance()._nativeParsingEnabled; },
    
    setNativeParsingEnabled : function(enabled){ Mojo.ClientSession.getInstance()._nativeParsingEnabled = enabled; },

    getBaseEndpoint : function() { return Mojo.ClientSession.getInstance()._baseEndpoint; },
    
    setBaseEndpoint : function(baseEndpoint) { Mojo.ClientSession.getInstance()._baseEndpoint = baseEndpoint; },
    
    getAjaxOptions : function() { return Mojo.Util.copy(Mojo.ClientSession.getInstance()._ajaxOptions, {}); },
    
    setAjaxOptions : function(defaultOptions) { Mojo.Util.copy(defaultOptions, Mojo.ClientSession.getInstance()._ajaxOptions); }
  }
});

var AjaxRequest = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'AjaxRequest', {

  Instance : {
    
    initialize: function (url, parameters, options)
    {
      this._url = url;
      this._xhr = this._xhrFactory();
      
      // encode the parameters if given a map
      this.paramStr = '';
      if(Mojo.Util.isObject(parameters))
      {
        var paramArray = [];
        for(var i in parameters)
        {
          paramArray.push(encodeURIComponent(i)+'='+encodeURIComponent(parameters[i]));
        }
        this.paramStr = paramArray.join('&');
      }
      else if (parameters != null)
      {
        this.paramStr = parameters.toString();
      }
      
      this.options = {};
      Mojo.Util.copy(Mojo.ClientSession.getAjaxOptions(), this.options);
      Mojo.Util.copy(options, this.options);
    },
    
    _xhrFactory : function()
    {
      try
      {
        // Firefox, Opera 8.0+, Safari
        return new XMLHttpRequest();
      }
      catch (e)
      {
        // Internet Explorer
        try
        {
          return new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
          try
          {
            return new ActiveXObject("Microsoft.XMLHTTP");
          }
          catch (e)
          {
            var message = "The browser does not support Ajax";
            throw new Exception(message);
          }
        }
      }
    },
    
    apply: function ()
    {
      this._send();
      
      try
      {      
        var bound = Mojo.Util.bind(this, this._onReadyStateChange);
        if(this.options.method.toLowerCase() === 'post')
        {
          this._xhr.open(this.options.method, this._url, this.options.asynchronous);
          this._xhr.onreadystatechange = bound;
          this._xhr.setRequestHeader("Content-type", this.options.contentType + "; charset="+this.options.encoding);
          this._xhr.setRequestHeader("Content-length", this.paramStr.length);
          this._xhr.setRequestHeader("Connection", "close");
  
          this._xhr.send(this.paramStr);
        }
        else
        {
          this._xhr.open(this.options.method, this._url+"?"+this.paramStr, this.options.asynchronous);
          this._xhr.onreadystatechange = bound;
          
          this._xhr.send(null);
        }
      }
      catch(e)
      {
        // FIXME add error handling for non-server exceptions
        this._complete();
      }
    },
    
    _send : function()
    {
      if (Mojo.Util.isFunction(this.options.onSend))
      {
        this.options.onSend(this);
      }
    },
    
    _complete : function()
    {
      if (Mojo.Util.isFunction(this.options.onComplete))
      {
        this.options.onComplete(this);
      }
    },
    
    _success : function()
    {
      if (Mojo.Util.isFunction(this.options.onSuccess))
      {
        this.options.onSuccess(this);
      }
    },
    
    _failure : function()
    {
      if (Mojo.Util.isFunction(this.options.onFailure))
      {
        this.options.onFailure(this);
      }
    },
    
    _onReadyStateChange : function()
    {
      if(this._xhr.readyState == 4)
      {
        this._complete();
        
        if(this._xhr.status >= this.options.successRange[0]
          && this._xhr.status <= this.options.successRange[1])
        {
          this._success();
        }
        else
        {
          this._failure();
        }
      }
    }
  }
});

/**
 * Class that serializes basic objects with optional key/value overriding.
 * Functions and the __context variables are ignored.
 */
var StandardSerializer = Mojo.Meta.newClass(Mojo.ROOT_PACKAGE+'StandardSerializer',{
  
  Instance : {
    initialize : function(source, override)
    {
      source = source || {};
      this._destination = {};
      
      // Copy the non-function properties to the destination object.
      // This will also remove any infinite recursion via toJSON()
      // declarations on the original source object.
      for(var i in source)
      {
        if(!isFunction(source[i]) && i !== "__context")
        {
          this._destination[i] = source[i];
        }
      }
      this._override = override || null;
    },
    toJSON : function(key)
    {
      var ssRef = this;
      var replacer = function(key, value)
      {
        if(ssRef._override !== null && key in ssRef._override)
        {
          return ssRef._override[key];
        }
        else
        {
          return value;
        }
      };
      
      if (key == null) {
        return Mojo.Util.toJSON(this._destination, replacer);
      }
      else {
        return this._destination;
      }
    }
  }
});



var Iterable = Mojo.Meta.newInterface(Mojo.STRUCTURE_PACKAGE+'Iterable', {
  Instance : {
    iterator : function(){}
  }
});

var Iterator = Mojo.Meta.newInterface(Mojo.STRUCTURE_PACKAGE+"Iterator", {
  Instance : {
    next:function(){},
    hasNext:function(){}
  }
});

var AbstractCollection = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'AbstractCollection', {
  IsAbstract: true,
  Instance : {
  }
});

var AbstractMap = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'AbstractMap', {
  Extends : AbstractCollection,
  IsAbstract : true,
  Instance : {
    initialize : function()
    {
      this.$initialize();
    }    
  }
});

// FIXME use hasOwnProperty() versus key in obj? Cross-browser/speed concerns?
var HashMap = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'HashMap', {
  Extends: AbstractMap,
  Instance : {
    initialize : function(map)
    {
      this.$initialize();
      this._map = {};
      this._size = 0;
      
      if(map)
      {
        this.putAll(map);
      }
    },
    
    _getKey : function(key)
    {
      return Mojo.Util.extendsBase(key) ? 
        key.getHashCode() : key.toString();
    },
    
    put : function(key, value)
    {
      var mapKey = this._getKey(key);
      
      var oldValue = null;
      var contains = this._containsKey(mapKey);
      if(contains)
      {
        oldValue = this._get(mapKey);
      }
      
      this._map[mapKey] = value;
      
      // We can only increase the size if we are not overwriting
      // and this check must be explicit because this._get(key) can
      // return null in the case where a key actually maps to a null
      // value. So incrementing the size if this._get(mapKey) === null
      // is not sufficient.
      if(!contains)
      {
        this._size++;
      }
      
      return oldValue;
    },
    
    // FIXME return boolean to match Java API?
    remove : function(key)
    {
      var mapKey = this._getKey(key);
      
      var oldValue = null;
      var contains = this._containsKey(mapKey);
      if(contains)
      {
        oldValue = this._get(mapKey);
      }
      
      delete this._map[mapKey];
      
      // We can only decrease the size if the key does not exist
      // and this check must be explicit because this._get(key) can
      // return null in the case where a key actually maps to a null
      // value. So decrementing the size if this._get(mapKey) === null
      // is not sufficient.
      if(contains)
      {
        this._size--;
      }
      
      return oldValue;
    },
    
    _get : function(mapKey)
    {
      return this._map.hasOwnProperty(mapKey) ? this._map[mapKey] : null;
    },
    
    get : function(key)
    {
      var mapKey = this._getKey(key);
      return this._get(mapKey);
    },
    
    clear : function()
    {
      var keys = Mojo.Util.getKeys(this._map, true);
      for (var i=0, len=keys.length; i<len; i++)
      {
        this.remove(keys[i]);
      }
    },
    
    _containsKey : function(mapKey)
    {
      return this._map.hasOwnProperty(mapKey);
    },
    
    containsKey : function(key)
    {
      var mapKey = this._getKey(key);
      return this._containsKey(mapKey);
    },
    
    containsValue : function(value)
    {
      for (var k in this._map)
      {
        if(this._map.hasOwnProperty(k) && Mojo.Util.equals(this._map[k], value))
        {
          return true;
        }
      }
      
      return false;
    },
    
    isEmpty : function()
    {
      return this._size === 0;
    },
    
    keySet : function()
    {
      return Mojo.Util.getKeys(this._map, true);
    },
    
    putAll : function(obj)
    {
      if(obj instanceof AbstractMap)
      {
        var keys = obj.keySet();
        for(var i=0; len=keys.length; i++)
        {
          var key = keys[i];
          var value = obj.get(key);
          this.put(key, value);
        }
      }
      else if(isArray(obj))
      {
        for(var i=0; i<obj.length; i++)
        {
          var o = obj[i];
          this.put(o, o);
        }
      }
      else if(isObject(obj))
      {
        for (var k in obj)
        {
          if(obj.hasOwnProperty(k))
          {
            this.put(k, obj[k]);
          }
        }
      }
    },
    
    size : function()
    {
      return this._size;
    },
    
    values : function()
    {
      return Mojo.Util.getValues(this._map, true);
    },
    /**
     * Serializes this Map into a basic JSON object.
     */
    toJSON : function(key){
      // only serialize the underlying map to avoid infinite recursion or other
      // circular issues.
      return new com.runwaysdk.StandardSerializer(this._map).toJSON(key);
    }
  }
});

// FIXME use common looping method with function callback
var LinkedHashMap = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'LinkedHashMap', {
  Extends : HashMap,
  Instance : {
    initialize : function(map){
      this._head = null;
      this._tail = null;
      this.$initialize(map);
    },
    keySet : function(){
      var keys = [];
      var current = this._head;
      while(current !== null){
        keys.push(this.get(current.key));
        current = current._next;
      }
      return keys;
    },
    values : function(){
      var values = [];
      var current = this._head;
      while(current !== null){
        values.push(this.get(current.key));
        current = current._next;
      }
      return values;
    },
    replace : function(key, value, oldKey){
      var keyStr = this._getKey(key);
      var oldKeyStr = this._getKey(oldKey);
    
      if(!this.containsKey(oldKeyStr)){
        throw new com.runwaysdk.Exception('Cannot replace the non-existent key ['+bKey+'].');
      }
      else if(this.containsKey(keyStr)){
        throw new com.runwaysdk.Exception('Cannot replace with the key ['+key+'] because it already exists in the map.');
      }
      
      var current = this._head;
      while(current !== null){
        if(current.key === oldKeyStr){
          // found the old key so simply reset the key but retain all pointers
          this.$remove(current.key);
          this.$put(keyStr, value);
          current.key = keyStr;
          
          return;
        }
        
        current = current._next;
      }      
    },
    insert : function(key, value, bKey){
      var keyStr = this._getKey(key);
      var bKeyStr = this._getKey(bKey);
    
      if(!this.containsKey(bKey)){
        throw new com.runwaysdk.Exception('Cannot insert before the non-existent key ['+bKey+'].');
      }
      else if(this.containsKey(keyStr)){
        throw new com.runwaysdk.Exception('Cannot insert the key ['+key+'] because it already exists in the map.');
      }
      
      var current = this._head;
      while(current !== null){
        if(current.key === bKeyStr){
          // found the old key so insert the new one before it
          var node = {key: keyStr, prev: current.prev, _next: current};
          
          if(this._head === current){
            // reset the head reference as the new node
            this._head = node;
          }
          else {
            current.prev._next = node;
          }
          current.prev = node;

          this.$put(keyStr, value);
          
          return;
        }
        
        current = current._next;
      }
    },
    put : function(key, value){

      key = this._getKey(key);
      
      if(!this.containsKey(key)){
        if(this._head === null){
          this._head = {key:key, prev: null, _next: null};
          this._tail = this._head;
        }
        else {
          var node = {key:key, prev: this._tail, _next: null};
          this._tail._next = node;
          this._tail = node;
        }
      }
      
      return this.$put(key, value);
    },
    clear : function(){
      this.$clear();
      this._head = null;
      this._tail = null;
    },
    remove : function(key){
      key = this._getKey(key);
      
      if(this.containsKey(key)){
        var current = this._head;
        while(current !== null){
          if(key === current.key){
            
            if(current === this._head){
              // removing the first item
              this._head = current._next;
              if(this._head){
                this._head.prev = null;
              }
              else {
                this._tail = null; // no items left (head is already null)
              }
            }
            else if(current === this._tail){
              // removing the last item            
              this._tail = current.prev;
              this._tail._next = null;
            }
            else {
              // all other items
              current.prev._next = current._next;
              current._next.prev = current.prev;
            }
            
            break;
          }
          else {
            current = current._next;
          }
        }        
      }
      
      return this.$remove(key);
    }
  }
});

var AbstractSet = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'AbstractSet', {
  Extends: AbstractCollection,
  Implements : Iterable,
  IsAbstract : true,
  Instance : {
    initialize : function()
    {
      this.$initialize();
    }
  }
});

var ArrayIterator = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'ArrayIterator', {
  Implements : Iterator,
  Instance : {
    initialize : function(arr)
    {
      this._array = arr;
      this._ind = 0;
    },
    next:function(){
      return this._array[this._ind++];
    },
    hasNext:function(){
      return this._ind < this._array.length;
    }
  }
});

var HashSet = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'HashSet', {
  Extends: AbstractSet,
  Instance : {
    initialize : function(collection)
    {
      this.$initialize();
      this._map = new HashMap();

      if(collection)
      {
        this.addAll(collection);
      }
    },
    iterator : function()
    {
      return new ArrayIterator(this.toArray());
    },
    
    add : function(obj)
    {
      this._map.put(obj, obj);
    },
    
    addAll : function(obj)
    {
      if(obj instanceof AbstractCollection)
      {
        var iter = obj.iterator();
        while(iter.hasNext())
        {
          this.add(iter.next());
        }
      }
      else if(isArray(obj))
      {
        for(var i=0; i<obj.length; i++)
        {
          this.add(obj[i]);
        }
      }
      else if(isObject(obj))
      {
        for (var k in obj)
        {
          if(obj.hasOwnProperty(k))
          {
            this.add(obj[k]);
          }
        }
      }
      else
      {
        throw new Exception('Object type ['+typeof obj+'] is not a recognized ' +
            'parameter for ['+this.getMetaClass().getQualifiedName()+'.addAll].');
      }
    },
    
    clear : function()
    {
      this._map.clear();
    },
    
    contains : function(obj)
    {
      return this._map.containsKey(obj);
    },
    
    _toCollection : function(collection)
    {
      if(collection instanceof AbstractCollection)
      {
        return collection;
      }
      else
      {
        return new HashSet(collection);
      }
    },
    
    containsAll : function(collection)
    {
      var compareTo = this._toCollection(collection);
      
      var iter = compareTo.iterator();
      while(iter.next())
      {
        if(!this.contains(iter.next))
        {
          return false;
        }
      }
      
      return true;
    },
    
    containsExactly : function(obj)
    {
      var collection = this._toCollection(obj);
      return this.size() === collection.size() && this.containsAll(collection);
    },
    
    isEmpty : function()
    {
      return this._map.isEmpty();
    },
    
    remove : function(obj)
    {
      this._map.remove(obj);
    },
    
    removeAll : function()
    {
      this._map.clear();
    },
    
    retainAll : function(obj)
    {
      var modified = false;
      var collection = this._toCollection(obj);
      var values = this._map.values();
      for(var i=0, len=values.length; i<len; i++)
      {
        var value = values[i];
        if(!collection.contains(value))
        {
          this.remove(value);
          modified = true;
        }
      }
      
      return modified;
    },
    
    size : function()
    {
      return this._map.size();
    },
    
    toArray : function()
    {
      return this._map.values();
    }
  }
});

var EventListener = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'EventListener', {
  Instance : {
    handleEvent : function(evt){}
  }
});

var EventException = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'EventException', {
  Extends : Mojo.ROOT_PACKAGE+'Exception',
  Constants : {
    UNSPECIFIED_EVENT_TYPE_ERR : 0
  },
  Instance : {
    initialize : function(code, message)
    {
      this.$initialize(message);
      this._code = code;
    },
    getCode : function() { return this._code; }
  }
});

var EventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'EventIF', {
  Constants : {
    CAPTURING_PHASE : 1,
    AT_TARGET : 2,
    BUBBLING_PHASE: 3
  },
  Instance : {
    getType : function(){},
    getTarget : function(){},
    getCurrentTarget : function(){},
    getEventPhase : function(){},
    getBubbles : function(){},
    getCancelable : function(){},
    getTimeStamp : function(){},
    stopPropagation : function(){},
    stopImmediatePropagation : function(){},
    preventDefault : function(){},
    getPreventDefault : function(){},
    initEvent : function(eventType, canBubble, cancelable){}
  }
});

var CustomEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'CustomEventIF', {
  Instance : {
    getDetail : function(){},
    initCustomEvent : function(eventType, canBubble, cancelable, detail){}
  }
});

var HTMLEventsIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'HTMLEventsIF', {
  Extends : EventIF,
  Instance : {

  }
});

var UIEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'UIEventIF', {
  Extends : EventIF,
  Instance : {
    getView : function(){},
    getDetail : function(){},
    initUIEvent : function(type, canBubble, cancelable, view, detail){}
  }
});

var FocusEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'FocusEventIF', {
  Extends : UIEventIF,
  Instance : {
    getRelatedTarget : function(){},
    initFocusEvent : function(eventType, canBubble, cancelable, view, detail, relatedTarget){}
  }
});

var MouseEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'MouseEventIF', {
  Extends : UIEventIF,
  Instance : {
    getScreenX : function(){},
    getScreenY : function(){},
    getClientX : function(){},
    getClientY : function(){},
    getCtrlKey : function(){},
    getShiftKey : function(){},
    getAltKey : function(){},
    getMetaKey : function(){},
    getButton : function(){},
    getButtons : function(){},
    getRelatedTarget : function(){},
    getModifierState : function(keyArg){},
    initMouseEvent : function(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget){}
  }
});

var DragEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'DragEventIF', {
  Extends : MouseEventIF,
  Instance : {
    getDataTransfer : function(){},
    initDragEvent : function(typeArg, canBubbleArg, cancelableArg, dummyArg, detailArg, screenXArg, screenYArg, 
      clientXArg, clientYArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, buttonArg, relatedTargetArg, dataTransferArg){}
  }
});

var TextEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'TextEventIF', {
  Extends : UIEventIF,
  Constants : {
    DOM_INPUT_METHOD_UNKNOWN       : 0x00,
    DOM_INPUT_METHOD_KEYBOARD      : 0x01,
    DOM_INPUT_METHOD_PASTE         : 0x02,
    DOM_INPUT_METHOD_DROP          : 0x03,
    DOM_INPUT_METHOD_IME           : 0x04,
    DOM_INPUT_METHOD_OPTION        : 0x05,
    DOM_INPUT_METHOD_HANDWRITING   : 0x06,
    DOM_INPUT_METHOD_VOICE         : 0x07,
    DOM_INPUT_METHOD_MULTIMODAL    : 0x08,
    DOM_INPUT_METHOD_SCRIPT        : 0x09
  },
  Instance : {
    getData : function(){},
    getInputMethod : function(){},
    getLocale : function(){},
    initTextEvent : function(type, canBubble, cancelable, view, data, input, locale){}
  }
});

var WheelEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'WheelEventIF', {
  Extends : MouseEventIF,
  Constants : {
    DOM_DELTA_PIXEL : 0x01,
    DOM_DELTA_LINE : 0x02,
    DOM_DELTA_PAGE: 0x03
  },
  Instance : {
    getDeltaX : function(){},
    getDeltaY : function(){},
    getDeltaZ : function(){},
    getDeltaMode : function(){},
    initWheelEvent : function(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget, modifierList, deltaX, deltaY, deltaZ, deltaMode){}
  }
});

var KeyboardEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'KeyboardEventIF', {
  Extends : UIEventIF,
  Constants : {
    DOM_KEY_LOCATION_STANDARD : 0x00,
    DOM_KEY_LOCATION_LEFT : 0x01,
    DOM_KEY_LOCATION_RIGHT : 0x02,
    DOM_KEY_LOCATION_NUMPAD : 0x03,
    DOM_KEY_LOCATION_MOBILE : 0x04,
    DOM_KEY_LOCATION_JOYSTICK : 0x05
  },
  Instance : {
    getChar : function(){},
    getKey : function(){},
    getKeyCode : function(){},
    getLocation : function(){},
    getCtrlKey : function(){},
    getShiftKey : function(){},
    getAltKey : function(){},
    getMetaKey : function(){},
    getRepeat : function(){},
    getModifierState : function(key){},
    getLocale : function(){},
    initKeyboardEvent : function(type, canBubble, cancelable, view, key, locationArg, modifiersList, repeat, locale){}
  }
});

var CompositionEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'CompositionEventIF', {
  Extends : UIEventIF,
  Instance : {
    getData : function(){},
    getLocale : function(){},
    initUIEvent : function(type, canBubble, cancelable, view, data, locale){}
  }
});

var DocumentEventIF = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'DocumentEventIF', {
  Instance : {
    createEvent : function(eventType){},
    canDispatch : function(namespaceURI, eventType){}
  }
});

var DocumentEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'DocumentEvent', {
  Implements: DocumentEventIF,
  IsSingleton: true,
  Instance: {
    initialize : function()
    {
    },
    createEvent : function(eventType)
    {
      eventType = Mojo.Util.isFunction(eventType) ? eventType.getMetaClass().getQualifiedName() : eventType;

      var event = null;
      // look for a DOM type match and use an Event wrapper
      var eventDef = EventUtil.DOM_EVENTS[eventType];
      if(eventDef)
      {
        event = new eventDef.eventInterface(eventType);
      }
      // look for a custom event
      else if(Mojo.Meta.classExists(eventType))
      {
        event = Mojo.Meta.newInstance(eventType);
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception("The event [" + eventType + "] could not be created because it is not a recognized DOM event.");
      }

      return event;
    },
    canDispatch : function(namespaceURI, eventType)
    {
      // TODO implement
    }
  }
});

/**
 * Marker interface that an event must implement if it wants to allow
 * the global registration of listeners.
 */
var GlobalEvent = Mojo.Meta.newInterface(Mojo.EVENT_PACKAGE+'GlobalEvent', {});

var AbstractEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'AbstractEvent', {
  IsAbstract: true,
  Implements: EventIF,
  Instance : {
    initialize : function()
    {
      this.$initialize.apply(this);
      this._preventDefault = false;
    },
    preventDefault : function()
    {
      if(this.getCancelable())
      {
        this._preventDefault = true;
      } 
    },
    getPreventDefault : function()
    {
      return this._preventDefault;
    },
  }
});

// FIXME publish event variables to adhere to the spec
var Event = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'Event', {
  Extends: AbstractEvent,
  Instance : {
    initialize : function(evt)
    {
      this.$initialize();
      
      // Wrap a new event object or an existing one passed into this constructor
      if(Mojo.Util.isString(evt))
      {
        if (document.createEvent) // W3C compatible browsers
        {
          var eventInterface = this.getEventInterface();
          this._evt = document.createEvent(eventInterface);
        }
        else if (document.createEventObject) // IE family
        {
          this._evt = document.createEventObject();
          this._evt.type = evt;
        }
      }
      else
      {
        this._evt = evt;
      }
    },
    getEventInterface : function()
    {
      return 'Event';
    },
    getEvent : function()
    {
      return this._evt;
    },
    getType : function()
    {
      return this._evt.type;
    },
    getTarget : function()
    {
      return this._evt.target;
    },
    getCurrentTarget : function()
    {
      return this._evt.currentTarget;    
    },
    getEventPhase : function()
    {
      return this._evt.eventPhase;    
    },
    getBubbles : function()
    {
      return this._evt.bubbles;    
    },
    getCancelable : function()
    {
      return this._evt.cancelable;    
    },
    getTimeStamp : function()
    {
      return this._evt.timeStamp;    
    },
    stopPropagation : function()
    {
      this._evt.stopPropagation();    
    },
    stopImmediatePropagation : function()
    {
      // FIXME check for support before invoking
      this._evt.stopImmediatePropagation();
    },
    preventDefault : function()
    {
      this.$preventDefault();
      this._evt.preventDefault();    
    },
    
    initEvent : function(eventType, canBubble, cancelable)
    {
      this._evt.initEvent(eventType, canBubble, cancelable);
    }
  }
});

var HTMLEvents = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'HTMLEvents', {
  Extends : Event,
  Implements: HTMLEventsIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    }, 
    getEventInterface : function()
    {
      return 'HTMLEvents';
    },
    initEvent : function(type, canBubble, cancelable)
    {
      this.$initEvent.apply(this, arguments);
    },
  }
});

var UIEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'UIEvent', {
  Extends: Event,
  Implements: UIEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'UIEvent';
    },
    getView : function()
    {
      return this.getEvent().view;
    },
    getDetail : function()
    {
      return this.getEvent().detail;
    },
    initEvent : function(type, canBubble, cancelable)
    {
      this.initUIEvent.apply(this, arguments);
    },
    initUIEvent : function(type, canBubble, cancelable, view, detail)
    {
      this.getEvent().initUIEvent(type, canBubble, cancelable, view, detail);
    }
  }
});

var TextEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'TextEvent', {
  Extends : UIEvent,
  Implements : TextEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'TextEvent';
    },
    getData : function()
    {
      return this.getEvent().data;
    },
    getInputMethod : function()
    {
      return this.getEvent().inputMethod;
    },
    getLocale : function()
    {
      return this.getEvent().locale;
    },
    initTextEvent : function(type, canBubble, cancelable, view, data, input, locale)
    {
      this.getEvent().initTextEvent(type, canBubble, cancelable, view, data, input, locale);
    }
  }
});

var CompositionEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'CompositionEvent', {
  Extends : UIEvent,
  Implements : CompositionEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'CompositionEvent';
    },
    getData : function()
    {
      return this.getEvent().data;
    },
    getLocale : function()
    {
      return this.getEvent().locale;
    },
    initCompositionEvent : function(type, canBubble, cancelable, view, data, locale)
    {
      this.getEvent().initCompositionEvent(type, canBubble, cancelable, view, data, locale);
    }
  }
});

var FocusEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'FocusEvent', {
  Extends : UIEvent,
  Implements : FocusEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return FeatureSet.getInstance().supportsDOM3Events() ? 
        'FocusEvent' : this.$getEventInterface();
    },
    getRelatedTarget : function()
    {
      return this.getEvent().relatedTarget;
    },
    initFocusEvent : function(eventType, canBubble, cancelable, view, detail, relatedTarget)
    {
      this.getEvent().initFocusEvent(eventType, canBubble, cancelable, view, detail, relatedTarget);
    }
  }
});

var MouseEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'MouseEvent', {
  Extends: UIEvent,
  Implements: MouseEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'MouseEvent';
    },
    getScreenX : function()
    {
      return this.getEvent().screenX;
    },
    getScreenY : function()
    {
      return this.getEvent().screenY;
    },
    getClientX : function()
    {
      return this.getEvent().clientX;
    },
    getClientY : function()
    {
      return this.getEvent().clientY;
    },
    getCtrlKey : function()
    {
      return this.getEvent().ctrlKey;
    },
    getShiftKey : function()
    {
      return this.getEvent().shiftKey;
    },
    getAltKey : function()
    {
      return this.getEvent().altKey;
    },
    getMetaKey : function()
    {
      return this.getEvent().metaKey;
    },
    getButton : function()
    {
      return this.getEvent().button;
    },
    getButtons : function()
    {
      return this.getEvent().buttons;
    },
    getModifierState : function(keyArg)
    {
      // FIXME DOM3 only
      return this.getEvent().getModifierState(keyArg);
    },
    getRelatedTarget : function()
    {
      return this.getEvent().relatedTarget;
    },
    initEvent : function(type, canBubble, cancelable)
    {
      this.initMouseEvent.apply(this, arguments);
    },
    initMouseEvent : function(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget)
    {
      this.getEvent().initMouseEvent(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget);
    }
  }
});

var DragEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'DragEvent', {
  Extends : MouseEvent,
  Implements : DragEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getDataTransfer : function()
    {
      return this.getEvent().dataTransfer; // TODO normalize into wrapper class
    },
    initDragEvent : function(typeArg, canBubbleArg, cancelableArg, dummyArg, detailArg, screenXArg, screenYArg, 
      clientXArg, clientYArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, buttonArg, relatedTargetArg, dataTransferArg){
      this.getEvent().initDragEvent(typeArg, canBubbleArg, cancelableArg, dummyArg, detailArg, screenXArg, screenYArg, 
      clientXArg, clientYArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, buttonArg, relatedTargetArg, dataTransferArg);
    }  
  }
});

var WheelEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'WheelEvent', {
  Extends : MouseEvent,
  Implements : WheelEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return 'WheelEvent';
    },
    getDeltaX : function()
    {
      return this.deltaX;
    },
    getDeltaY : function()
    {
      return this.deltaY;
    },
    getDeltaZ : function()
    {
      return this.deltaZ;
    },
    getDeltaMode : function()
    {
      return this.deltaMode;
    },
    initWheelEvent : function(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget, modifierList, deltaX, deltaY, deltaZ, deltaMode)
    {
      this.initWheelEvent(type, canBubble, cancelable, view, detail, 
      screenX, screenY, clientX, clientY, ctrlKey, 
      altKey, shiftKey, metaKey, button, relatedTarget, modifierList, deltaX, deltaY, deltaZ, deltaMode);
    }
  }
});

var KeyboardEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'KeyboardEvent', {
  Extends: UIEvent,
  Implements: KeyboardEventIF,
  Instance : {
    initialize : function(event)
    {
      this.$initialize(event);
    },
    getEventInterface : function()
    {
      return FeatureSet.getInstance().supportsDOM3Events() ? 
        'KeyboardEvent' : this.$getEventInterface();
    },
    getChar : function()
    {
      return this.getEvent()['char'];
    },
    getKey : function()
    {
      return this.getEvent().key;
    },
    getKeyCode : function()
    {
      return this.getEvent().keyCode;
    },
    getLocation : function()
    {
      return this.getEvent().location;
    },
    getCtrlKey : function()
    {
      return this.getEvent().ctrlKey;
    },
    getShiftKey : function()
    {
      return this.getEvent().shiftKey;
    },
    getAltKey : function()
    {
      return this.getEvent().altKey;
    },
    getMetaKey : function()
    {
      return this.getEvent().metaKey;
    },
    getRepeat : function()
    {
      return this.getEvent().repeat;
    },
    getModifierState : function(key)
    {
      return this.getEvent().modifierState;
    },
    getLocale : function()
    {
      return this.getEvent().locale;
    },
    initEvent : function(type, canBubble, cancelable)
    {
      this.initKeyboardEvent.apply(this, arguments);
    },
    initKeyboardEvent : function(type, canBubble, cancelable, view, key, locationArg, modifiersList, repeat)
    {
      this.getEvent().initKeyboardEvent(type, canBubble, cancelable, view, key, locationArg, modifiersList, repeat);
    }
  }
});

var CustomEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'CustomEvent', {
  Extends: AbstractEvent,
  Implements: CustomEventIF,
  IsAbstract : true,
  Instance : {
    initialize : function(config)
    {
      this.$initialize();
      config = config || {};
      
      /*
       * These fields are defined as public in the event specification, but it
       * is recommended that the accessor methods are invoked instead.
       */
      this.type = this.getMetaClass().getQualifiedName();
      this.target = null;
      this.currentTarget = null;
      this.bubbles = config.bubbles || true;
      this.cancelable = config.cancelable || true;
      this.timeStamp = new Date().getTime();
      this.eventPhase = EventIF.AT_TARGET;
      this._preventDefault = false;
      this._detail = null;
      this._stopped = false;
      this._immediateStopped = false;
    },
    getEventInterface : function()
    {
      return 'CustomEvent';
    },
    defaultAction : function()
    {
      // Do nothing as the default implementation. Subclasses may override this.
    },
    getType : function()
    {
      return this.type;
    },
    getTarget : function()
    {
      return this.target;
    },
    _setTarget : function(target)
    {
      this.target = target;
    },
    _setCurrentTarget : function(currentTarget)
    {
      this.currentTarget = currentTarget;
    },
    getCurrentTarget : function()
    {
      return this.currentTarget;
    },
    _setEventPhase : function(phase)
    {
      this.eventPhase = phase;
    },
    getEventPhase : function()
    {
      return this.eventPhase;   
    },
    getBubbles : function()
    {
      return this.bubbles; 
    },
    getCancelable : function()
    {
      return this.cancelable; 
    },
    getTimeStamp : function()
    {
      return this.timeStamp;  
    },
    stopPropagation : function()
    {
      this._stopped = true;
    },
    getStopPropagation : function()
    {
      return this._stopped;
    },
    stopImmediatePropagation : function()
    {
      this._immediateStopped = true;
    },
    getStopImmediatePropagation : function()
    {
      return this._immediateStopped;
    },
    initEvent : function(eventType, canBubble, cancelable)
    {
      var type = Mojo.Util.isFunction(eventType) ? eventType.getMetaClass().getQualifiedName() : eventType;
      if(type !== this.type)
      {
        throw new Exception('Cannot change the type of a custom error from ['+this.type+'] to ['+type+']');
      }
      
      this.bubbles = canBubble;
      this.cancelable = cancelable;
    },
    initCustomEvent : function(eventType, canBubble, cancelable, detail)
    {
      this.initEvent(eventType, canBubble, cancelable);
      this._detail = detail;
    },
    getDetail : function()
    {
      return this._detail;
    }
  }
});

var EventUtil = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'EventUtil', {

  IsAbstract : true,

  Static : {
    
    addEventListener: function(oTarget, sEvent, oListener, bUseCapture)
    {
      oTarget = this._validateTarget(oTarget);
    
      if (oTarget.addEventListener)
      {
        oTarget.addEventListener(sEvent, oListener, bUseCapture);
      }
      else if (oTarget.attachEvent) // IE
      {
        // Make it so that the handler is passed an event object with a proper event.target
        var wrapper = function() { var event = window.event; event.target = window.event.srcElement; oListener(event); };
        oTarget.attachEvent("on" + sEvent, wrapper);
      }
      else
      {
        oTarget["on" + sEvent] = oListener;
      }
    },
    
    removeEventListener: function(oTarget, sEvent, oListener, bUseCapture)
    {
      oTarget = this._validateTarget(oTarget);
    
      if (oTarget.removeEventListener)
      {
        oTarget.removeEventListener(sEvent, oListener, bUseCapture);
      }
      else if (oTarget.detachEvent) // IE
      {
        oTarget.detachEvent(sEvent, oListener);
      }
      else
      {
        oTarget["on" + sEvent] = null;
      }
    },
    
    /**
     * Factory method to create any type of event.
     */
    createEvent: function(eventType)
    {
      props = props || {};
      
      var event = null;
      
      // look for a DOM type match and use an Event wrapper to initialize the properties
      var eventDef = this.EVENTS[eventType];
      if(eventDef)
      {
        if (document.createEvent) // W3C compatible browsers
        {
          event = document.createEvent(eventDef.eventGroup);
        }
        else if (document.createEventObject) // IE family
        {
          event = document.createEventObject();
        }
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception("The event [" + eventType + "] could not be created because it is not a recognized DOM event.");
      }

      return event;
    },
    
    dispatchEvent: function(target, event)
    {
      target = this._validateTarget(target);
    
      if (target === window && !Mojo.Util.isFunction(target.dispatchEvent) && Mojo.Util.isFunction(document.dispatchEvent))
      {
        // Safari3 doesn't have window.dispatchEvent()
        target = document;
      }
      else if (target === document && !Mojo.Util.isFunction(document.documentElement.fireEvent))
      {
        // IE6,IE7 thinks window==document and doesn't have window.fireEvent()
        // IE6,IE7 cannot properly call document.fireEvent()
        target = document.documentElement;
      }
    
      if (Mojo.Util.isFunction(target.dispatchEvent)) // W3C compatible browsers
      {
        return target.dispatchEvent(event);
      }
      else if (Mojo.Util.isFunction(target.fireEvent)) // IE family
      {
        return target.fireEvent("on" + event.type, event);
      }
      else
      {
        throw new Mojo.$.com.runwaysdk.Exception("Unable to dispatch event [" + event + "] on target [" + target +
          "], no usable methods were found on the target to dispatch the event. (Either you are using some unsupported browser, or the target does not support events)");
      }
    },
    
    _validateTarget: function(target)
    {
      target = Mojo.Util.isString(target) ? document.getElementById(target) : target;
      if(target == null)
      {
        throw new Mojo.$.com.runwaysdk.Exception("Mojo.Event.addListener: Unable to find a target by the id of '" + sOldTarget + "'.");
      }

      return target;
    },
    
    DEPRECATED_EVENTS : {
      // TODO should we white or black list deprecation?
    },
    
    // FIXME add default args ... maybe use LinkedHashMap for ordering and overrides
    DOM_EVENTS : {
    
      // FormEvent
      change : {eventInterface : HTMLEvents},
    
      // UIEvent
      abort : {eventInterface : UIEvent},
      load : {eventInterface : UIEvent},
      unload : {eventInterface : UIEvent},
      select : {eventInterface : UIEvent},
      error : {eventInterface : UIEvent},
      resize : {eventInterface : UIEvent},
      scroll : {eventInterface : UIEvent},
      
      // FocusEvent
      blur : {eventInterface : FocusEvent},
      focus : {eventInterface : FocusEvent},
      focusin : {eventInterface : FocusEvent},
      focusout : {eventInterface : FocusEvent},
      
      // MouseEvent
      click : {eventInterface : MouseEvent},
      dblclick : {eventInterface : MouseEvent},
      mousedown : {eventInterface : MouseEvent},
      mouseenter : {eventInterface : MouseEvent},
      mouseleave : {eventInterface : MouseEvent},
      mousemove : {eventInterface : MouseEvent},
      mouseout : {eventInterface : MouseEvent},
      mouseover : {eventInterface : MouseEvent},
      mouseup : {eventInterface : MouseEvent},
      
      // WheelEvent
      wheel : {eventInterface : WheelEvent},
      
      // DragEvent
      dragstart : {eventInterface : DragEvent},
      drag : {eventInterface : DragEvent},
      dragend : {eventInterface : DragEvent},
      dragenter : {eventInterface : DragEvent},
      dragover : {eventInterface : DragEvent},
      dragleave : {eventInterface : DragEvent},
      drop : {eventInterface : DragEvent},
      
      // KeyboardEvent
      keydown : {eventInterface : KeyboardEvent},
      keyup : {eventInterface : KeyboardEvent},
      keypress : {eventInterface : KeyboardEvent}, // FIXME deprecated in DOM3 (use textInput)
      
      // TextEvent
      textinput : {eventInterface : TextEvent},
      
      // CompositionEvent
      compositionstart : {eventInterface : CompositionEvent},
      compositionend : {eventInterface : CompositionEvent},
      compositionupdate : {eventInterface : CompositionEvent}
    }
  }
});

/**
 * Wrapper class to store the data for an instance of an EventListener
 */
var ListenerEntry = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'ListenerEntry', {
  Instance : {
    initialize : function(type, listener, wrapper, obj, context, capture)
    {
      this._type = type;
      this._listener = listener;
      this._wrapper = wrapper;
      this._object = obj;
      this._context = context;
      this._capture = capture;
    },
    getType : function() { return this._type; },
    getListener : function() { return this._listener; },
    getWrapper : function() { return this._wrapper; },
    getObject : function() { return this._object; },
    getContext : function() { return this._context; },
    captures : function() { return this._capture; },
    matches : function(type, listener, capture)
    {
      return this._type === type && this._listener === listener && this._capture === capture;
    }
  }
});

var Registry = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'Registry', {
  IsSingleton : true,
  Instance : {
    initialize : function()
    {
      this._listeners = new HashMap();
      this._globalListeners = new HashMap();
      this._domPrefix = /^dom.*/i;
    },
    
    isDispatching : function()
    {
      return this._dispatching;
    },
    
    dispatchEvent : function(evt)
    {
      try
      {
        var listeners = this._listeners.get(evt.getCurrentTarget());
        if(listeners === null)
        {
          return; // no listeners for the current event target
        }
        
        for(var i=0, len=listeners.length; i<len; i++)
        {
          if(evt.getStopImmediatePropagation())
          {
            return;
          }
          
          // FIXME match on event phase
          var listener = listeners[i];
          if(listener.getType() === evt.getType())
          {
            var wrapper = listener.getWrapper();
            var context = listener.getContext() || evt.getCurrentTarget();
            var obj = listener.getObject();
            var handler = Mojo.Util.isObject(wrapper) ? wrapper.handleEvent : wrapper;
            
            try
            {
              var args = [evt];
              if(!Mojo.Util.isUndefined(obj))
              {
                args.push(obj);
              }
              
              handler.apply(context, args);
            }
            catch(e)
            {
              // Invoke an error handler if one exists, but an error within any event listener
              // SHOULD NOT disrupt futher listener processing according to the spec.
              // FIXME wrap with EventException and store event as instance var
              if(Mojo.Util.isFunction(wrapper.handleError))
              {
                wrapper.handleError.call(context, e);
              }
            }
          }
        }
      }
      catch (e)
      {
        // FIXME allow for Event-level error handling
        //http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-EventException
        //http://www.findmeat.org/tutorials/javascript/x946556.htm
      }
    },
    
    _wrapDOMListener : function(type, listener, obj, context, capture)
    {
      return Mojo.Util.bind(context, function(evt){
          
          // FIXME normalize
          var eventInterface = EventUtil.DOM_EVENTS[evt.type].eventInterface;
          var event = new eventInterface(evt);
          if (Mojo.Util.isObject(listener)) 
          {
            listener.handleEvent.call(context, event, obj);
          }
          else 
          {
            listener.call(this, event, obj);
          }
        });
    },
    
    addEventListener : function(target, type, listener, obj, context, capture)
    {
      if (type == null) {
        throw new com.runwaysdk.Exception("Unable to listen to a null event.");
      }
      
      if(Mojo.Util.isObject(listener) && Mojo.Util.isFunction(listener.handleEvent))
      {
        if(context && context !== listener)
        {
          var msg = 'An event listener object cannot have a different context than the listener itself.';
          throw new Mojo.$.com.runwaysdk.Exception(msg);
        }
        else
        {
          context = listener;
        }
      }
      else if(!Mojo.Util.isFunction(listener))
      {
        var msg = 'An event listener must either be an object, instance of ['+EventListener.getMetaClass().getQualifiedName()+'], or object that defines a handleEvent(evt) function.';
        throw new Mojo.$.com.runwaysdk.Exception(msg);
      }
      
      // DOM and custom events are wrapped differently
      var wrapper;
      if(Mojo.Meta.classExists(type))
      {
        // The spec states that custom events cannot start with the DOM prefix
        if(this._domPrefix.test(type))
        {
          var msg = "Custom events, such as ["+type+"] cannot start with the prefix 'DOM' as required by the event specification.";
          throw new Mojo.$.com.runwaysdk.Exception(msg);
        }
      
        // custom event
        wrapper = listener;
        if(capture)
        {
          var msg = "Custom events, such as ["+type+"], cannot be listened for in capture mode. Listen globally instead.";
          throw new Mojo.$.com.runwaysdk.Exception(msg);
        }
        
      }
      else
      {
        wrapper = this._wrapDOMListener(type, listener, obj, context, capture);
        
        // FIXME Justin
        if (com.runwaysdk.ui.ElementProviderIF.getMetaClass().isInstance(target))
        {
          EventUtil.addEventListener(target.getEl().getRawEl(), type, wrapper, capture);
        }
        else if (com.runwaysdk.ui.ElementIF.getMetaClass().isInstance(target))
        {
          EventUtil.addEventListener(target.getRawEl(), type, wrapper, capture);
        }
        else if (target === document) {
          EventUtil.addEventListener(target, type, wrapper, capture);
        }
        else
        {
          var msg = "Cannot add event listener on type ["+target+"].";
          throw new Mojo.$.com.runwaysdk.Exception(msg);
        }
      }
      
      capture = capture || false;
    
      var listenerObj = new ListenerEntry(type, listener, wrapper, obj, context, capture);
      if(this._listeners.containsKey(target))
      {
        // discard duplicates
        var listeners = this._listeners.get(target);
        for(var i=0, len=listeners.length; i<len; i++)
        {
          var entry = listeners[i];
          if(type === entry.getType() && listener === entry.getListener())
          {
            return;
          }
        }
        listeners.push(listenerObj);
      }
      else
      {
        this._listeners.put(target, [listenerObj]);
      }
    },
    
    removeEventListener : function(target, type, listener, capture)
    {
      capture = capture || false;
      
      if(this._listeners.containsKey(target))
      {
        var listeners = this._listeners.get(target);
        if(listeners !== null)
        {
          for(var i=0; i<listeners.length; i++)
          {
            var l = listeners[i];
            if(l.matches(type, listener, capture))
            {
              listeners.splice(i,1);
              
              if(!Mojo.Meta.classExists(type))
              {
                EventUtil.removeEventListener(target, type, l.getWrapper(), l.captures());
              }
              
              return;
            }
          }
        }
      }
    },
    
    removeEventListeners : function(target, type)
    {
      var listeners = this._listeners.get(target);
      if(listeners !== null)
      {
        for(var i=0, len=listeners.length; i<len; i++)
        {
          var l = listeners[i];
          var filtered = [];
          if(l.matches(type, l.getListener(), l.captures()))
          {
             
            if(!Mojo.Meta.classExists(type))
            {
              EventUtil.removeEventListener(target, type, l.getWrapper(), l.captures());
            }
          }
          else
          {
            filtered.push(l);
          }
        }
        
        this._listeners.put(target, filtered);
      }
    },
    
    removeAllEventListeners : function(target)
    {
      var listeners = this._listeners.get(target);
      if(listeners !== null)
      {
        for(var i=0, len=listeners.length; i<len; i++)
        {
          var l = listeners[i];
             
          if(!Mojo.Meta.classExists(l.getType()))
          {
            EventUtil.removeEventListener(target, l.getType(), l.getWrapper(), l.captures());
          }
        }
        
        this._listeners.remove(target);
      }
    },
    
    hasEventListener : function(target, type, listener, capture)
    {
      capture = capture || false;
      
      var listeners = this._listeners.get(target);
      if(listeners !== null)
      {
        for(var i=0, len=listeners.length; i<len; i++)
        {
          var l = listeners[i];
          if(l.matches(type, listener, capture))
          {
            return true;
          }
        }
      }
      
      return false;      
    }
  }
});

var DestroyEvent = Mojo.Meta.newClass(Mojo.EVENT_PACKAGE+'DestroyEvent', {
  Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(object)
    {
      this.$initialize();
      this._object = object;
    },
    getObject : function() { return this._object; },
  }
});

var TaskIF = Mojo.Meta.newInterface(Mojo.STRUCTURE_PACKAGE+'TaskIF', {
  Instance : {
    /**
     * Starts this task.
     * 
     * @param taskQueue The TaskQueue instance that is managing this TaskIF.
     */
    start : function(taskQueue){}
  }
});

var TaskListenerIF = Mojo.Meta.newInterface(Mojo.STRUCTURE_PACKAGE+'TaskListenerIF', {
  Instance : {
    /**
     * Called when a TaskIF is started.
     * 
     * @param taskQueue The TaskQueue instance that is managing this TaskIF.
     */
    onStart : function(taskQueue){},
    /**
     * Called when a TaskIF is finished (success case)
     * 
     * @param taskQueue The TaskQueue instance that is managing this TaskIF.
     */
    onFinish : function(taskQueue){},
    /**
     * Called when a TaskIF is stopped (error case)
     * 
     * @param taskQueue The TaskQueue instance that is managing this TaskIF.
     * @param e An optional object, usually an Error instance, that can be used for error handling, logging, or debugging.
     */
    onStop : function(taskQueue, e){}  
  }
});

var TaskListener = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+"TaskListener", {
  Implements: TaskListenerIF,
  
  Instance : {
    onStart : function(){},
    onFinish : function(){},
    onStop : function(){}
  }
});

var TaskQueue = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TaskQueue', {
  Extends : AbstractCollection,
  Implements : TaskIF, 
  Instance : {
    
    initialize : function()
    {
      this._remaining = [];
      this._listeners = [];
      this._currentTask = null;
      this._currentListeners = null;
      this._completed = [];
      this._processing = false;
      this._stopped = false;
      this._locked = false;
      this._queueListeners = [];
      this._taskQueue = null;
    },
    
    /**
     * Locks this instance such that no modifications can take place
     * while it processes.
     */
    lock : function(){
      this._locked = true;
    },
    
    /**
     * Unlocks this instance such that modifications can take place
     * while it processes.
     */
    unlock : function(){
      this._locked = false;
    },
    
    /**
     * Checks if this instance is locked.
     * 
     * @return Returns true if locked or false otherwise.
     */
    isLocked : function(){
      return this._locked;
    },
    
    /**
     * Adds a TaskListenerIF object, which will have its handler methods
     * invoked when this TaskQueue starts, finishes, or aborts.
     * 
     * @taskListenerIFs An object that implements the methods of TaskListenerIF.
     * This parameter can be any number of TaskListenerIF objects
     * (i.e., TaskQueue.addTaskQueueListener(taskListenerIFs*))
     */
    addTaskQueueListener : function(taskListenerIF){
      // Cannot add new listeners if the queue is locked
      if(this.isLocked()){
        throw new Exception('Cannot invoke TaskQueue.addTaskQueueListener() while the TaskQueue is locked');
      }
      
      this.addEventListener(TQStartEvent, {handleEvent: taskListenerIF.onStart});
      this.addEventListener(TQStopEvent, {handleEvent: taskListenerIF.onStop});
      this.addEventListener(TQFinishEvent, {handleEvent: taskListenerIF.onFinish});
    },
    
    /**
     * Adds a TaskListenerIF object, which will have its handler methods
     * invoked for each TaskIF object that starts, finishes, or aborts.
     * 
     * @taskListenerIFs An object that implements the methods of TaskListenerIF.
     * This parameter can be any number of TaskListenerIF objects
     * (i.e., TaskQueue.addTaskListener(taskListenerIFs*)).
     */
    addTaskListener : function(taskListenerIF){
    
      // Cannot add new listeners if the queue is locked
      if(this.isLocked()){
        throw new Exception('Cannot invoke TaskQueue.addTaskListener() while the TaskQueue is locked');
      }
      
      this.addEventListener(TStartEvent, {handleEvent: taskListenerIF.onStart});
      this.addEventListener(TStopEvent, {handleEvent: taskListenerIF.onStop});
      this.addEventListener(TFinishEvent, {handleEvent: taskListenerIF.onFinish});
    },
    
    /**
     * Adds a TaskIF object to the end of this TaskQueue.
     * 
     * @param taskIFs An object that implements the methods of TaskIF.
     * This parameter can be any number of TaskIF objects 
     * (i.e., TaskQueue.addTask(task1*))
     */
    addTask : function(taskIFs){
    
      // Cannot add new tasks if the queue is locked
      if(this.isLocked()){
        throw new Exception('Cannot invoke TaskQueue.addTask() while the TaskQueue is locked');
      }
    
      this._remaining = this._remaining.concat(Array.prototype.splice.call(arguments, 0, arguments.length));
    },
    
    /**
     * Returns an array of all tasks that remain in the queue.
     * 
     * @return An array, in queue order, of the remaining TaskIF objects.
     */
    getRemainingTasks : function(){
      return this._remaining;
    },
    
    /**
     * Returns the current TaskIF object that is being processed. This method
     * returns null if TaskQueue.start() has not been called or if the queue
     * has been successfully processed.
     * 
     * @return The current TaskIF that is being processed.
     */
    getCurrentTask : function(){
      return this._currentTask;
    },
    
    /**
     * Returns an array of all tasks that have completed.
     * 
     * @return An array, in queue order, of the completed TaskIF objects.
     */
    getCompletedTasks : function(){
      return this._completed;
    },
    
    /**
     * Checks if this TaskQueue is currently processing tasks.
     * 
     * @return Returns true if this instance is processing or false otherwise.
     */
    isProcessing : function(){
      return this._processing;
    },
    
    /**
     * Checks if this TaskQueue has had its processing stopped.
     * 
     * @return Returns true if this instance has been stopped or false otherwise.
     */
    isStopped : function(){
      return this._stopped;
    },
    
    /**
     * Aborts the processing of the queue. This method has no effect if called more
     * than once.
     * 
     * @param e An optional object, usually an Error instance, that will be passed to TaskListenerIF.onAbort(e).
     */
    stop : function(e){
      
      if(this._stopped){
        return;
      }
      
      // error checking
      if(!this._processing){
        throw new Exception('Cannot invoke TaskQueue.stop() if processing has not started.');
      }
      
      this._stopped = true;
      this._processing = false;
  
      // notify the listeners
      this.dispatchEvent(new TQStopEvent(this));
      this.dispatchEvent(new TStopEvent(this));
      
      this.removeAllEventListeners();
    },
    
    /**
     * Starts the processing of the tasks in queue order.
     * 
     * @param taskQueue An optional "owner" TaskQueue that is running this instance as a TaskIF.
     */
    start : function(taskQueue){
      this._processing = true;
      this._taskQueue = taskQueue;
      this.dispatchEvent(new TQStartEvent(this));
      this.next();
    },
    
    /**
     * Returns the next TaskIF object in the queue.
     * 
     * @return Returns the next TaskIF object in the queue or null if it is empty.
     */
    peek : function(){
      return this.hasNext() ? this._remaining[0] : null;
    },
    
    /**
     * Checks if this TaskQueue has another TaskIF object to process.
     * 
     * @return Returns true if there is another task to process or false otherwise.
     */
    hasNext : function(){
      return this._remaining.length > 0;
    },
    
    /**
     * The method that MUST be invoked by TaskIF instances to signal this TaskQueue that
     * processing can continue.
     */
    next : function(){
    
      // error checking
      if(this._stopped){
        throw new Exception('Cannot invoke TaskQueue.next() because the processing was stopped.');
      }
      else if(!this._processing){
        throw new Exception('Cannot invoke TaskQueue.next() because the queue is no longer processing.');
      }
    
      if(this._currentTask !== null){
        this._completed.push(this._currentTask);
        this.dispatchEvent(new TFinishEvent(this));
      }
    
      if(this.hasNext()){
        this._currentTask = this._remaining.shift();
        this.dispatchEvent(new TStartEvent(this));
        var args = [this].concat(Array.prototype.splice.call(arguments, 0, arguments.length));
        this._currentTask.start.apply(this, args);
      }
      else {
        // finished!
        this._processing = false;
        this._currentTask = null;
        this.dispatchEvent(new TQFinishEvent(this));
        
        this.removeAllEventListeners();
        
        // notify the owning TaskQueue (if this is a composite) that this instance is done processing
        if(this._taskQueue && Mojo.Util.isFunction(this._taskQueue.next)){
          this._taskQueue.next();
        }
      }
    },
    
    /**
     * String representation of this TaskQueue object and its current state.
     */
    toString : function(){
      return '[TaskQueue] processing: '+this.isProcessing()+', remaining: '+this.getRemainingTasks().length+', completed: '+this.getCompletedTasks().length;
    }  
  }
});

// TaskQueue Custom Events:
var TQBaseEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TQBaseEvent', {
  Extends : Mojo.$.com.runwaysdk.event.CustomEvent,
  Instance : {
    initialize : function(tq) {
      this.$initialize();
      this._tq = tq;
    },
    getTaskQueue : function() {
      return this._tq;
    }
  }
});
var TQStartEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TQStartEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TQStopEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TQStopEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TQFinishEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TQFinishEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TStartEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TStartEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TStopEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TStopEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});
var TFinishEvent = Mojo.Meta.newClass(Mojo.STRUCTURE_PACKAGE+'TFinishEvent', {
  Extends : TQBaseEvent,
  Instance : {
    initialize : function(tq)
    {
      this.$initialize(tq);
    }
  }
});

})();