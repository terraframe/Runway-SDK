package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.dataaccess.MdProblemDAOIF;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdLocalizableDAO;
import com.runwaysdk.dataaccess.metadata.MdProblemDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;

public class MdProblemHandler extends MdLocalizableHandler implements TagHandlerIF, HandlerFactoryIF
{
  public MdProblemHandler(ImportManager manager)
  {
    super(manager, MdProblemInfo.CLASS);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.MdNotificationHandler#populate(com.runwaysdk.dataaccess.metadata.MdNotificationDAO, org.xml.sax.Attributes)
   */
  @Override
  protected void populate(MdLocalizableDAO mdLocalizable, Attributes attributes)
  {
    super.populate(mdLocalizable, attributes);

    // Import optional reference attributes
    String extend = attributes.getValue(XMLTags.EXTENDS_ATTRIBUTE);

    if (extend != null)
    {
      // Ensure the parent class has already been defined in the database
      if (!MdTypeDAO.isDefined(extend))
      {
        // The type is not defined in the database, check if it is defined
        // in the further down in the xml document.
        String[] search_tags = { XMLTags.MD_PROBLEM_TAG };
        SearchHandler.searchEntity(this.getManager(), search_tags, XMLTags.NAME_ATTRIBUTE, extend, mdLocalizable.definesType());
      }

      MdProblemDAOIF superException = MdProblemDAO.getMdProblem(extend);
      mdLocalizable.setValue(MdProblemInfo.SUPER_MD_PROBLEM, superException.getId());
    }
  }

}
