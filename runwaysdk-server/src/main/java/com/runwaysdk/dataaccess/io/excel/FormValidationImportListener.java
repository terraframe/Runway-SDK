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
package com.runwaysdk.dataaccess.io.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.runwaysdk.business.Entity;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.dataaccess.FieldConditionDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.MdFieldDAOIF;
import com.runwaysdk.dataaccess.MdFormDAOIF;
import com.runwaysdk.dataaccess.MdWebAttributeDAOIF;
import com.runwaysdk.dataaccess.metadata.FieldValidationProblem;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;

public class FormValidationImportListener extends ImportAdapter implements ImportApplyListener
{
  private MdFormDAOIF                                      mdForm;

  private Map<MdAttributeDAOIF, List<FieldConditionDAOIF>> map;

  public FormValidationImportListener(MdFormDAOIF mdForm)
  {
    this.mdForm = mdForm;
    this.map = new HashMap<MdAttributeDAOIF, List<FieldConditionDAOIF>>();

    List<? extends MdFieldDAOIF> fields = this.mdForm.getAllMdFields();

    for (MdFieldDAOIF field : fields)
    {
      if (field instanceof MdWebAttributeDAOIF)
      {
        MdWebAttributeDAOIF mdWebAttribute = (MdWebAttributeDAOIF) field;

        List<FieldConditionDAOIF> conditions = field.getConditions();

        map.put(mdWebAttribute.getDefiningMdAttribute(), conditions);
      }
    }
  }

  @Override
  public void validate(Mutable instance, HashMap<String, List<Entity>> entities)
  {
    Set<MdAttributeDAOIF> keys = map.keySet();

    for (MdAttributeDAOIF key : keys)
    {
      String value = instance.getValue(key.getMdAttributeConcrete().getValue(MdAttributeConcreteInfo.NAME));

      // IMPORTANT: Only valiate attributes that have values
      if (value != null && value.length() > 0)
      {
        List<FieldConditionDAOIF> conditions = map.get(key);

        for (FieldConditionDAOIF condition : conditions)
        {
          boolean valid = condition.evaluate(instance, entities);

          if (!valid)
          {
            MdClassDAOIF mdClass = MdClassDAO.getMdClassDAO(instance.getType());
            String formattedString = condition.getFormattedString();
            String msg = "Attribute is not applicable when [" + condition + "] does not evaluate to true";

            FieldValidationProblem problem = new FieldValidationProblem(instance.getId(), mdClass, key, msg);
            problem.setCondition(formattedString);
            problem.throwIt();
          }
        }
      }
    }

    super.beforeApply(instance);
  }
}
