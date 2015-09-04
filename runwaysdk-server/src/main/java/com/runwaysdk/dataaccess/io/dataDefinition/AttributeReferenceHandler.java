package com.runwaysdk.dataaccess.io.dataDefinition;

import org.xml.sax.Attributes;

import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdAttributeReferenceDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.attributes.InvalidAttributeTypeException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.ImportManager;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;

public class AttributeReferenceHandler extends TagHandler implements TagHandlerIF, HandlerFactoryIF
{
  public AttributeReferenceHandler(ImportManager manager)
  {
    super(manager);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.dataaccess.io.dataDefinition.TagHandlerIF#onStartElement(java.lang.String, org.xml.sax.Attributes, com.runwaysdk.dataaccess.io.dataDefinition.TagContext)
   */
  @Override
  public void onStartElement(String localName, Attributes attributes, TagContext context)
  {
    EntityDAO entity = (EntityDAO) context.getObject(EntityInfo.CLASS);

    String attributeRefName = attributes.getValue(XMLTags.ENTITY_ATTRIBUTE_NAME_ATTRIBUTE);
    String referenceKey = attributes.getValue(XMLTags.KEY_ATTRIBUTE);

    MdAttributeDAOIF mdAttributeDAOIF = entity.getAttributeIF(attributeRefName).getMdAttribute();

    if (! ( mdAttributeDAOIF instanceof MdAttributeReferenceDAOIF ))
    {
      String errMsg = "The attribute [" + mdAttributeDAOIF.definesAttribute() + "] on type [" + entity.getType() + "] is not a reference attribute.";

      MdBusinessDAOIF expectedAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(MdAttributeReferenceInfo.CLASS);
      MdBusinessDAOIF givenAttributeTypeDefinition = MdBusinessDAO.getMdBusinessDAO(mdAttributeDAOIF.getType());

      throw new InvalidAttributeTypeException(errMsg, mdAttributeDAOIF, expectedAttributeTypeDefinition, givenAttributeTypeDefinition);
    }

    MdAttributeReferenceDAOIF mdAttributeReferenceDAOIF = (MdAttributeReferenceDAOIF) mdAttributeDAOIF;
    String referenceType = mdAttributeReferenceDAOIF.getReferenceMdBusinessDAO().definesType();

    String id = "";

    try
    {
      id = EntityDAO.getIdFromKey(referenceType, referenceKey);
    }
    catch (DataNotFoundException e)
    {
      if (! ( referenceType.equals(entity.getType()) && referenceKey.equals(entity.getKey()) ))
      {
        SearchCriteriaIF criteria = new EntitySearchCriteria(referenceType, referenceKey, XMLTags.OBJECT_TAG);

        SearchHandler.searchEntity(this.getManager(), criteria, entity.getKey());
      }
    }

    if (id.equals(""))
    {
      id = EntityDAO.getIdFromKey(referenceType, referenceKey);
    }

    entity.setValue(attributeRefName, id);

  }
}
