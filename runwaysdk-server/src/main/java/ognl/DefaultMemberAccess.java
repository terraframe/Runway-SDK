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
package ognl;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Map;

public class DefaultMemberAccess implements MemberAccess
{

  /*
   * Assign an accessibility modification mechanism, based on Major Java
   * Version. Note: Can be override using a Java option flag {@link
   * OgnlRuntime#USE_PREJDK9_ACESS_HANDLER}.
   */
  private static final AccessibleObjectHandler _accessibleObjectHandler;
  static
  {
    _accessibleObjectHandler = OgnlRuntime.usingJDK9PlusAccessHandler() ? AccessibleObjectHandlerJDK9Plus.createHandler() : AccessibleObjectHandlerPreJDK9.createHandler();
  }

  public boolean allowPrivateAccess          = false;

  public boolean allowProtectedAccess        = false;

  public boolean allowPackageProtectedAccess = false;

  /*
   * ===================================================================
   * Constructors
   * ===================================================================
   */
  public DefaultMemberAccess(boolean allowAllAccess)
  {
    this(allowAllAccess, allowAllAccess, allowAllAccess);
  }

  public DefaultMemberAccess(boolean allowPrivateAccess, boolean allowProtectedAccess, boolean allowPackageProtectedAccess)
  {
    super();
    this.allowPrivateAccess = allowPrivateAccess;
    this.allowProtectedAccess = allowProtectedAccess;
    this.allowPackageProtectedAccess = allowPackageProtectedAccess;
  }

  /*
   * =================================================================== Public
   * methods ===================================================================
   */
  public boolean getAllowPrivateAccess()
  {
    return allowPrivateAccess;
  }

  public void setAllowPrivateAccess(boolean value)
  {
    allowPrivateAccess = value;
  }

  public boolean getAllowProtectedAccess()
  {
    return allowProtectedAccess;
  }

  public void setAllowProtectedAccess(boolean value)
  {
    allowProtectedAccess = value;
  }

  public boolean getAllowPackageProtectedAccess()
  {
    return allowPackageProtectedAccess;
  }

  public void setAllowPackageProtectedAccess(boolean value)
  {
    allowPackageProtectedAccess = value;
  }

  /*
   * ===================================================================
   * MemberAccess interface
   * ===================================================================
   */
  public Object setup(Map context, Object target, Member member, String propertyName)
  {
    Object result = null;

    if (isAccessible(context, target, member, propertyName))
    {
      AccessibleObject accessible = (AccessibleObject) member;

      if (!accessible.isAccessible())
      {
        result = Boolean.FALSE;
        _accessibleObjectHandler.setAccessible(accessible, true);
      }
    }
    return result;
  }

  public void restore(Map context, Object target, Member member, String propertyName, Object state)
  {
    if (state != null)
    {
      final AccessibleObject accessible = (AccessibleObject) member;
      final boolean stateboolean = ( (Boolean) state ).booleanValue(); // Using
                                                                       // twice
                                                                       // (avoid
                                                                       // unboxing)
      if (!stateboolean)
      {
        _accessibleObjectHandler.setAccessible(accessible, stateboolean);
      }
      else
      {
        throw new IllegalArgumentException("Improper restore state [" + stateboolean + "] for target [" + target + "], member [" + member + "], propertyName [" + propertyName + "]");
      }
    }
  }

  /**
   * Returns true if the given member is accessible or can be made accessible by
   * this object.
   *
   * @param context
   *          the current execution context (not used).
   * @param target
   *          the Object to test accessibility for (not used).
   * @param member
   *          the Member to test accessibility for.
   * @param propertyName
   *          the property to test accessibility for (not used).
   * @return true if the member is accessible in the context, false otherwise.
   */
  public boolean isAccessible(Map context, Object target, Member member, String propertyName)
  {
    int modifiers = member.getModifiers();
    boolean result = Modifier.isPublic(modifiers);

    if (!result)
    {
      if (Modifier.isPrivate(modifiers))
      {
        result = getAllowPrivateAccess();
      }
      else
      {
        if (Modifier.isProtected(modifiers))
        {
          result = getAllowProtectedAccess();
        }
        else
        {
          result = getAllowPackageProtectedAccess();
        }
      }
    }
    return result;
  }

}
