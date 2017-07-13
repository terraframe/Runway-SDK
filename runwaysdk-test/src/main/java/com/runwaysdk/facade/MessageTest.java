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
package com.runwaysdk.facade;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;

import com.runwaysdk.AttributeNotification;
import com.runwaysdk.AttributeNotificationDTO;
import com.runwaysdk.ClientSession;
import com.runwaysdk.DoNotWeave;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.TestSuiteTF;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.business.SmartExceptionDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdLocalizableInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MdWarningInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SupportedLocaleInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblemDTO;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.ExcelProblemDTO;
import com.runwaysdk.dataaccess.io.excel.ExcelProblem;
import com.runwaysdk.dataaccess.metadata.InheritanceException;
import com.runwaysdk.dataaccess.metadata.SupportedLocaleDAO;
import com.runwaysdk.dataaccess.transaction.AbortIfProblem;
import com.runwaysdk.dataaccess.transaction.AttributeNotificationMap;
import com.runwaysdk.dataaccess.transaction.SkipIfProblem;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class MessageTest extends TestCase implements DoNotWeave
{
  protected static String          label;

  protected static ClientSession   systemSession;

  protected static ClientRequestIF clientRequest;

  protected static BusinessDTO     newUser;

  private static BusinessDTO       exception;

  private static BusinessDTO       mdBusinessBook;

  private static String            bookType;

  private static String            bookQueryType;

  private static String            bookViewType;

  private static BusinessDTO       mdAttributeTitle;

  private static BusinessDTO       mdAttributeLockedBy = null;

  private static BusinessDTO       mdViewBook;

  private static BusinessDTO       mdAttributeViewTitle;

  protected static BusinessDTO     attributeProblem1;

  protected static BusinessDTO     problem1;

  protected static BusinessDTO     problem2;

  protected static BusinessDTO     warning1;

  protected static BusinessDTO     information1;

  protected static BusinessDTO     exceptionMdMethod;

  protected static BusinessDTO     problemMdMethod;

  protected static BusinessDTO     warningMdMethod1;

  protected static BusinessDTO     warningMdMethodReturnArrayObjects;

  protected static BusinessDTO     warningMdMethodReturnQueryObject;

  protected static BusinessDTO     warningMdMethodReturnInt;

  protected static BusinessDTO     warningMdMethodReturnIntArray;

  protected static BusinessDTO     informationMdMethod;

  protected static BusinessDTO     multipleMessagesMdMethod;

  protected static BusinessDTO     skipIfProblemsMethod;

  protected static BusinessDTO     noSkipIfProblemsMethod;

  protected static BusinessDTO     abortIfProblemsMethod;

  protected static BusinessDTO     noAbortIfProblemsMethod;

  protected static BusinessDTO     excelProblemMethod;

  protected static String          pack                = "test.bookstore";

  public static Test suite()
  {
    TestSuiteTF suite = new TestSuiteTF();
    suite.addTestSuite(MessageTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        label = "default";
        systemSession = ClientSession.createUserSession(label, ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

        try
        {
          clientRequest = systemSession.getRequest();
          clientRequest.setKeepMessages(false);
          classSetUp();
          changeStubSource();
        }
        catch (Exception e)
        {
          systemSession.logout();
        }
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  protected ClientSession createAnonymousSession()
  {
    return ClientSession.createAnonymousSession(label, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientSession createSession(String userName, String password)
  {
    return ClientSession.createUserSession(label, userName, password, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientSession createSession(String userName, String password, Locale locale)
  {
    return ClientSession.createUserSession(label, userName, password, new Locale[] { locale });
  }

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    ClientRequestIF clientRequestIF = clientSession.getRequest();
    clientRequestIF.setKeepMessages(false);

    return clientRequestIF;
  }

  public static void classSetUp()
  {
    addLocale(Locale.GERMAN);

    bookType = pack + ".Book";
    bookQueryType = bookType + EntityQueryAPIGenerator.QUERY_API_SUFFIX;

    bookViewType = pack + ".BookView";

    exception = clientRequest.newBusiness(MdExceptionInfo.CLASS);
    exception.setValue(MdTypeInfo.NAME, "AlreadyCheckedOutException");
    exception.setValue(MdTypeInfo.PACKAGE, pack);
    exception.setStructValue(MdTypeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Exception thrown when there are no copies available for checkout of the requested book");
    exception.setStructValue(MdTypeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Already Checked Out Exception");
    clientRequest.createBusiness(exception);

    BusinessDTO title = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    title.setValue(MdAttributeCharacterInfo.NAME, "bookTitle");
    title.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Title");
    title.setValue(MdAttributeCharacterInfo.SIZE, "64");
    title.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, exception.getId());
    clientRequest.createBusiness(title);

    problem1 = clientRequest.newBusiness(MdProblemInfo.CLASS);
    problem1.setValue(MdProblemInfo.NAME, "TooManyCheckedOutBooksProblem");
    problem1.setValue(MdProblemInfo.PACKAGE, pack);
    problem1.setStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Problem thrown when the library user already has too many books checked out.");
    problem1.setStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Too Many Checked Out Books Problem");
    clientRequest.createBusiness(problem1);

    // Attributes on localized message classex
    BusinessDTO checkedOutBooks = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    checkedOutBooks.setValue(MdAttributeIntegerInfo.NAME, "checkedOutBooks");
    checkedOutBooks.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Number of Checked Out Books");
    checkedOutBooks.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, problem1.getId());
    clientRequest.createBusiness(checkedOutBooks);

    problem2 = clientRequest.newBusiness(MdProblemInfo.CLASS);
    problem2.setValue(MdProblemInfo.NAME, "OverdueLibraryFeesProblem");
    problem2.setValue(MdProblemInfo.PACKAGE, pack);
    problem2.setStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Problem thrown when the library user has overdue library fees.");
    problem2.setStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Overdue Library Fees Problem");
    clientRequest.createBusiness(problem2);

    attributeProblem1 = clientRequest.newBusiness(MdProblemInfo.CLASS);
    attributeProblem1.setValue(MdProblemInfo.NAME, "InvalidTitleAttributeProblem");
    attributeProblem1.setValue(MdProblemInfo.PACKAGE, pack);
    attributeProblem1.setStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Thrown when the itle on a book is invalid.");
    attributeProblem1.setStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Invalid Title Attribute Problem");
    clientRequest.createBusiness(attributeProblem1);

    BusinessDTO viewTitle = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    viewTitle.setValue(MdAttributeCharacterInfo.NAME, "bookTitle");
    viewTitle.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Name");
    viewTitle.setValue(MdAttributeCharacterInfo.SIZE, "64");
    viewTitle.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeProblem1.getId());
    clientRequest.createBusiness(viewTitle);

    BusinessDTO attributeName = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    attributeName.setValue(MdAttributeCharacterInfo.NAME, "attributeName");
    attributeName.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Attribute Name");
    attributeName.setValue(MdAttributeCharacterInfo.SIZE, "32");
    attributeName.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeProblem1.getId());
    clientRequest.createBusiness(attributeName);

    BusinessDTO displayLabel = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    displayLabel.setValue(MdAttributeCharacterInfo.NAME, "attributeDisplayLabel");
    displayLabel.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Display Label");
    displayLabel.setValue(MdAttributeCharacterInfo.SIZE, "128");
    displayLabel.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeProblem1.getId());
    clientRequest.createBusiness(displayLabel);

    BusinessDTO definingType = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    definingType.setValue(MdAttributeCharacterInfo.NAME, "definingType");
    definingType.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Containing Type");
    definingType.setValue(MdAttributeCharacterInfo.SIZE, Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    definingType.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeProblem1.getId());
    clientRequest.createBusiness(definingType);

    BusinessDTO definingTypeDisplayLabel = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    definingTypeDisplayLabel.setValue(MdAttributeCharacterInfo.NAME, "definingTypeDisplayLabel");
    definingTypeDisplayLabel.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Containing Type Display Label");
    definingTypeDisplayLabel.setValue(MdAttributeCharacterInfo.SIZE, Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    definingTypeDisplayLabel.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeProblem1.getId());
    clientRequest.createBusiness(definingTypeDisplayLabel);

    BusinessDTO componentId = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    componentId.setValue(MdAttributeCharacterInfo.NAME, "componentId");
    componentId.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Component Id");
    componentId.setValue(MdAttributeCharacterInfo.SIZE, Database.DATABASE_ID_SIZE);
    componentId.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeProblem1.getId());
    clientRequest.createBusiness(componentId);

    BusinessDTO outstandingFees = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    outstandingFees.setValue(MdAttributeIntegerInfo.NAME, "totalOutstandingFees");
    outstandingFees.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Total Outstanding Fees");
    outstandingFees.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, problem2.getId());
    clientRequest.createBusiness(outstandingFees);

    warning1 = clientRequest.newBusiness(MdWarningInfo.CLASS);
    warning1.setValue(MdWarningInfo.NAME, "CheckoutLimitReached");
    warning1.setValue(MdWarningInfo.PACKAGE, pack);
    warning1.setStructValue(MdWarningInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning that lets the user know that they have checked out the maximum number of books.");
    warning1.setStructValue(MdWarningInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Checkout Limit Reached");
    clientRequest.createBusiness(warning1);

    BusinessDTO maxAllowedBooks = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    maxAllowedBooks.setValue(MdAttributeIntegerInfo.NAME, "maxAllowedBooks");
    maxAllowedBooks.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Maximum Allowed Books");
    maxAllowedBooks.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    maxAllowedBooks.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, warning1.getId());
    clientRequest.createBusiness(maxAllowedBooks);

    information1 = clientRequest.newBusiness(MdInformationInfo.CLASS);
    information1.setValue(MdInformationInfo.NAME, "RecommendedBook");
    information1.setValue(MdInformationInfo.PACKAGE, pack);
    information1.setStructValue(MdInformationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Message that recommends another book to the user.");
    information1.setStructValue(MdInformationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Recommendation");
    clientRequest.createBusiness(information1);

    BusinessDTO recommendationTitle = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    recommendationTitle.setValue(MdAttributeCharacterInfo.NAME, "title");
    recommendationTitle.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Title");
    recommendationTitle.setValue(MdAttributeCharacterInfo.SIZE, "64");
    recommendationTitle.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, information1.getId());
    clientRequest.createBusiness(recommendationTitle);

    mdBusinessBook = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    mdBusinessBook.setValue(MdBusinessInfo.NAME, "Book");
    mdBusinessBook.setValue(MdBusinessInfo.PACKAGE, pack);
    mdBusinessBook.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusinessBook.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Book");
    clientRequest.createBusiness(mdBusinessBook);

    mdAttributeTitle = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeTitle.setValue(MdAttributeCharacterInfo.NAME, "title");
    mdAttributeTitle.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Title");
    mdAttributeTitle.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeTitle.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeTitle.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdBusinessBook.getId());
    clientRequest.createBusiness(mdAttributeTitle);

    mdViewBook = clientRequest.newBusiness(MdViewInfo.CLASS);
    mdViewBook.setValue(MdBusinessInfo.NAME, "BookView");
    mdViewBook.setValue(MdBusinessInfo.PACKAGE, pack);
    mdViewBook.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdViewBook.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Book View");
    clientRequest.createBusiness(mdViewBook);

    mdAttributeViewTitle = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeViewTitle.setValue(MdAttributeCharacterInfo.NAME, "viewTitle");
    mdAttributeViewTitle.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book View Title");
    mdAttributeViewTitle.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeViewTitle.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeViewTitle.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdViewBook.getId());
    clientRequest.createBusiness(mdAttributeViewTitle);

    exceptionMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    exceptionMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    exceptionMdMethod.setValue(MdMethodInfo.NAME, "exceptionMethod");
    exceptionMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Exception Method");
    exceptionMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(exceptionMdMethod);

    problemMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    problemMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    problemMdMethod.setValue(MdMethodInfo.NAME, "problemMethod");
    problemMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Problem Method");
    problemMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(problemMdMethod);

    warningMdMethod1 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethod1.setValue(MdMethodInfo.RETURN_TYPE, bookType);
    warningMdMethod1.setValue(MdMethodInfo.NAME, "warningMethod");
    warningMdMethod1.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method");
    warningMdMethod1.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(warningMdMethod1);

    warningMdMethodReturnArrayObjects = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethodReturnArrayObjects.setValue(MdMethodInfo.RETURN_TYPE, bookType + "[]");
    warningMdMethodReturnArrayObjects.setValue(MdMethodInfo.NAME, "warningMethodReturnArrayObjects");
    warningMdMethodReturnArrayObjects.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method With Array of Return Objects");
    warningMdMethodReturnArrayObjects.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(warningMdMethodReturnArrayObjects);

    warningMdMethodReturnQueryObject = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethodReturnQueryObject.setValue(MdMethodInfo.RETURN_TYPE, bookQueryType);
    warningMdMethodReturnQueryObject.setValue(MdMethodInfo.NAME, "warningMdMethodReturnQueryObject");
    warningMdMethodReturnQueryObject.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method With Return Query Object");
    warningMdMethodReturnQueryObject.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(warningMdMethodReturnQueryObject);

    warningMdMethodReturnInt = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethodReturnInt.setValue(MdMethodInfo.RETURN_TYPE, Integer.class.getName());
    warningMdMethodReturnInt.setValue(MdMethodInfo.NAME, "warningMdMethodReturnInt");
    warningMdMethodReturnInt.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method With Return Integer");
    warningMdMethodReturnInt.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(warningMdMethodReturnInt);

    warningMdMethodReturnIntArray = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethodReturnIntArray.setValue(MdMethodInfo.RETURN_TYPE, Integer.class.getName() + "[]");
    warningMdMethodReturnIntArray.setValue(MdMethodInfo.NAME, "warningMdMethodReturnIntArray");
    warningMdMethodReturnIntArray.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method With Return Integer Array");
    warningMdMethodReturnIntArray.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(warningMdMethodReturnIntArray);

    multipleMessagesMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    multipleMessagesMdMethod.setValue(MdMethodInfo.RETURN_TYPE, pack + ".Book");
    multipleMessagesMdMethod.setValue(MdMethodInfo.NAME, "multipleMessagesMethod");
    multipleMessagesMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method");
    multipleMessagesMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(multipleMessagesMdMethod);

    informationMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    informationMdMethod.setValue(MdMethodInfo.RETURN_TYPE, pack + ".Book");
    informationMdMethod.setValue(MdMethodInfo.NAME, "informationMethod");
    informationMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Information Method");
    informationMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(informationMdMethod);

    skipIfProblemsMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    skipIfProblemsMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    skipIfProblemsMethod.setValue(MdMethodInfo.NAME, "skipIfProblemsMethod");
    skipIfProblemsMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Method should be skipped if problems exist in the transaction.");
    skipIfProblemsMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(skipIfProblemsMethod);

    noSkipIfProblemsMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    noSkipIfProblemsMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    noSkipIfProblemsMethod.setValue(MdMethodInfo.NAME, "noSkipIfProblemsMethod");
    noSkipIfProblemsMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Method should not be skipped if problems exist in the transaction.");
    noSkipIfProblemsMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(noSkipIfProblemsMethod);

    abortIfProblemsMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    abortIfProblemsMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    abortIfProblemsMethod.setValue(MdMethodInfo.NAME, "abortIfProblemsMethod");
    abortIfProblemsMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Transaction should be aborted if a problem occured and a method has the abort annotation.");
    abortIfProblemsMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(abortIfProblemsMethod);

    noAbortIfProblemsMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    noAbortIfProblemsMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    noAbortIfProblemsMethod.setValue(MdMethodInfo.NAME, "noAbortIfProblemsMethod");
    noAbortIfProblemsMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Transaction should not be aborted if a problem occured and a method does not have the abort annotation.");
    noAbortIfProblemsMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(noAbortIfProblemsMethod);

    excelProblemMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    excelProblemMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    excelProblemMethod.setValue(MdMethodInfo.NAME, "excelProblemMethod");
    excelProblemMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Throws an ExcelProblem to test the translation to DTO.");
    excelProblemMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusinessBook.getId());
    clientRequest.createBusiness(excelProblemMethod);

    List<? extends BusinessDTO> mdAttributeDTOList = clientRequest.getChildren(mdBusinessBook.getId(), RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());

    for (BusinessDTO mdAtrributeDTO : mdAttributeDTOList)
    {
      if (mdAtrributeDTO.getValue(MdAttributeConcreteInfo.NAME).equals(MetadataInfo.LOCKED_BY))
      {
        mdAttributeLockedBy = mdAtrributeDTO;
      }
    }

    newUser = clientRequest.newBusiness(UserInfo.CLASS);
    newUser.setValue(UserInfo.USERNAME, "Tommy");
    newUser.setValue(UserInfo.PASSWORD, "music");
    clientRequest.createBusiness(newUser);
  }

  protected static void changeStubSource()
  {
    clientRequest.lock(attributeProblem1);
    attributeProblem1.setValue(MdClassInfo.STUB_SOURCE, "package test.bookstore;\n" + "\n" + "\n" + "public class InvalidTitleAttributeProblem extends InvalidTitleAttributeProblemBase implements " + Reloadable.class.getName() + ",\n" + " " + AttributeNotification.class.getName() + " \n" + "{\n" + "  public InvalidTitleAttributeProblem()\n" + "  {\n" + "    super();\n" + "  }\n" + "\n" + "  public InvalidTitleAttributeProblem(" + String.class.getName() + " developerMessage)\n" + "  {\n"
        + "    super(developerMessage);\n" + "  }\n" + "}\n");
    attributeProblem1.setValue(MdClassInfo.DTO_STUB_SOURCE, "package test.bookstore;\n" + "\n" + "\n" + "public class InvalidTitleAttributeProblemDTO extends InvalidTitleAttributeProblemDTOBase implements " + Reloadable.class.getName() + ",\n" + " " + AttributeNotificationDTO.class.getName() + " \n" + "{\n" + "  public InvalidTitleAttributeProblemDTO(" + ClientRequestIF.class.getName() + " clientRequestIF)\n" + "  {\n" + "    super(clientRequestIF);\n" + "  }\n" + "\n"
        + "  public InvalidTitleAttributeProblemDTO(" + ClientRequestIF.class.getName() + " clientRequestIF, " + java.util.Locale.class.getName() + " locale)\n" + "  {\n" + "    super(clientRequestIF, locale);\n" + "  }\n" + "}\n");
    clientRequest.update(attributeProblem1);

    clientRequest.lock(mdViewBook);
    mdViewBook.setValue(MdClassInfo.STUB_SOURCE, "package test.bookstore;\n" + "\n" + "public class BookView extends BookViewBase implements " + Reloadable.class.getName() + "\n" + "{\n" + "  public BookView()\n" + "  {\n" + "    super();\n" + "  }\n" + "\n" + "  public void apply()\n" + "  {\n" + "    " + bookType + " book1 = new " + bookType + "();\n" + "\n" + "    " + AttributeNotificationMap.class.getName() + " attrNotification = \n" + "      new " + AttributeNotificationMap.class.getName()
        + "(book1, \"title\", this, \"viewTitle\");\n" + "\n" + "    InvalidTitleAttributeProblem attributeProblem1 = new InvalidTitleAttributeProblem(\"Attribute Problem Developer Message\");\n" + "    attributeProblem1.setBookTitle(\"Chicky Monkey\");\n" + "    attributeProblem1.setComponentId(book1.getId());\n" + "    attributeProblem1.setDefiningType(\"" + bookType + "\");\n" + "    attributeProblem1.setDefiningTypeDisplayLabel(\"A Book\");\n"
        + "    attributeProblem1.setAttributeName(\"title\");\n" + "    attributeProblem1.setAttributeDisplayLabel(\"Book Title\");\n" + "    attributeProblem1.apply();\n" + "    attributeProblem1.throwIt();\n" + "  }\n" + "}\n");
    clientRequest.update(mdViewBook);

    clientRequest.lock(mdBusinessBook);
    mdBusinessBook.setValue(MdClassInfo.STUB_SOURCE, "package test.bookstore;\n" + "\n" + "import " + InheritanceException.class.getName() + ";\n" + "import " + ExcelProblem.class.getName() + ";\n" + "import " + java.util.Locale.class.getName() + ";\n" + "\n" + "public class Book extends BookBase implements " + Reloadable.class.getName() + "\n" + "{\n" + "  public Book()\n" + "  {\n" + "    super();\n" + "  }\n" + "  \n" + "  public void exceptionMethod()\n" + "  {\n"
        + "    AlreadyCheckedOutException acoe = new AlreadyCheckedOutException(\"Sup, developer\");\n" + "    acoe.setBookTitle(\"Atlas Shrugged\");\n" + "    throw acoe;\n" + "  }\n" + "  \n" + "  @" + Transaction.class.getName() + "\n" + "  public void problemMethod()\n" + "  {\n" + "    this.lock();\n" + "    this.setTitle(\"Changed Value1\");\n" + "    this.apply();\n" + "    \n"
        + "    TooManyCheckedOutBooksProblem problem1 = new TooManyCheckedOutBooksProblem(\"Problem1 Developer Message\");\n" + "    problem1.setCheckedOutBooks(10);\n" + "    problem1.apply();\n" + "    problem1.throwIt();\n" + "    \n" + "    OverdueLibraryFeesProblem problem2 = new OverdueLibraryFeesProblem(\"Problem2 Developer Message\");\n" + "    problem2.setTotalOutstandingFees(1000);\n" + "    problem2.apply();\n" + "    problem2.throwIt();\n" + "    \n" + "    this.lock();\n"
        + "    this.setTitle(\"Changed Value2\");\n" + "    this.apply();\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public " + bookType + " warningMethod()\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "    return this;\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public " + bookType
        + "[] warningMethodReturnArrayObjects()\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "    \n" + "    " + bookType + "[] bookArray = new " + bookType + "[2];\n" + "    \n" + "    " + bookType + " book1 = new " + bookType + "();\n" + "    book1.setTitle(\"The Illiad\");\n" + "    book1.apply();\n" + "    bookArray[0] = book1;\n" + "    \n" + "    "
        + bookType + " book2 = new " + bookType + "();\n" + "    book2.setTitle(\"The Odyssey\");\n" + "    book2.apply();\n" + "    bookArray[1] = book2;\n" + "    \n" + "    return bookArray;\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public " + bookType + "Query warningMdMethodReturnQueryObject()\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n"
        + "    warning1.throwIt();\n" + "    \n" + "    " + QueryFactory.class.getName() + " q = new " + QueryFactory.class.getName() + "();\n" + "    " + bookQueryType + " bookQuery = new " + bookQueryType + "(q);\n" + "    bookQuery.ORDER_BY_ASC(bookQuery.getTitle());\n" + "    \n" + "    return bookQuery;\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public " + Integer.class.getName() + " warningMdMethodReturnInt()\n" + "  {\n"
        + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "    \n" + "    return 5;\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public " + Integer.class.getName() + "[] warningMdMethodReturnIntArray()\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n"
        + "    warning1.throwIt();\n" + "    \n" + "    Integer[] intArray = new Integer[2];\n" + "    intArray[0] = 6;\n" + "    intArray[1] = 7;\n" + "    return intArray;\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public " + bookType + " informationMethod()\n" + "  {\n" + "    RecommendedBook information1 = new RecommendedBook();\n" + "    information1.setTitle(\"Tom Sawyer\");\n" + "    information1.apply();\n" + "    information1.throwIt();\n" + "    return this;\n"
        + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public " + bookType + " multipleMessagesMethod()\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "\n" + "    RecommendedBook information1 = new RecommendedBook();\n" + "    information1.setTitle(\"Tom Sawyer\");\n" + "    information1.apply();\n" + "    information1.throwIt();\n"
        + "    return this;\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public void skipIfProblemsMethod()\n" + "  {\n" + "    OverdueLibraryFeesProblem problem2 = new OverdueLibraryFeesProblem(\"Problem2 Developer Message\");\n" + "    problem2.setTotalOutstandingFees(1000);\n" + "    problem2.apply();\n" + "    problem2.throwIt();\n" + "\n" + "    skipThisMethod();\n" + "    this.lock();\n" + "    this.setTitle(\"Changed Value\");\n" + "    this.apply();\n" + "  }\n"
        + "  @" + SkipIfProblem.class.getName() + "\n" + "  private void skipThisMethod()\n" + "  {\n" + "    AlreadyCheckedOutException acoe = new AlreadyCheckedOutException(\"Sup, developer\");\n" + "    acoe.setBookTitle(\"Atlas Shrugged\");\n" + "    throw acoe;\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public void noSkipIfProblemsMethod()\n" + "  {\n" + "    OverdueLibraryFeesProblem problem2 = new OverdueLibraryFeesProblem(\"Problem2 Developer Message\");\n"
        + "    problem2.setTotalOutstandingFees(1000);\n" + "    problem2.apply();\n" + "    problem2.throwIt();\n" + "\n" + "    doNotSkipThisMethod();\n" + "    this.lock();\n" + "    this.setTitle(\"Changed Value\");\n" + "    this.apply();\n" + "  }\n" + "\n" + "  private void doNotSkipThisMethod()\n" + "  {\n" + "    AlreadyCheckedOutException acoe = new AlreadyCheckedOutException(\"Sup, developer\");\n" + "    acoe.setBookTitle(\"Atlas Shrugged\");\n" + "    throw acoe;\n" + "  }\n"
        + "\n" + "  @" + Transaction.class.getName() + "\n" + "  public void abortIfProblemsMethod()\n" + "  {\n" + "    OverdueLibraryFeesProblem problem1 = new OverdueLibraryFeesProblem(\"Problem1 Developer Message\");\n" + "    problem1.setTotalOutstandingFees(1000);\n" + "    problem1.apply();\n" + "    problem1.throwIt();\n" + "\n" + "    abortOnExecuteMethod();\n" + "\n" + "    this.lock();\n" + "    this.setTitle(\"Changed Value\");\n" + "    this.apply();\n" + "\n"
        + "    OverdueLibraryFeesProblem problem2 = new OverdueLibraryFeesProblem(\"Problem2 Developer Message\");\n" + "    problem2.setTotalOutstandingFees(1000);\n" + "    problem2.apply();\n" + "    problem2.throwIt();\n" + "  }\n" + "  @" + AbortIfProblem.class.getName() + "\n" + "  private void abortOnExecuteMethod()\n" + "  {\n" + "    AlreadyCheckedOutException acoe = new AlreadyCheckedOutException(\"Sup, developer\");\n" + "    acoe.setBookTitle(\"Atlas Shrugged\");\n"
        + "    throw acoe;\n" + "  }\n" + "\n" + "  @" + Transaction.class.getName() + "\n" + "  public void noAbortIfProblemsMethod()\n" + "  {\n" + "    OverdueLibraryFeesProblem problem1 = new OverdueLibraryFeesProblem(\"Problem1 Developer Message\");\n" + "    problem1.setTotalOutstandingFees(1000);\n" + "    problem1.apply();\n" + "    problem1.throwIt();\n" + "\n" + "    noAbortOnExecuteMethod();\n" + "\n" + "    this.lock();\n" + "    this.setTitle(\"Changed Value\");\n"
        + "    this.apply();\n" + "\n" + "    OverdueLibraryFeesProblem problem2 = new OverdueLibraryFeesProblem(\"Problem2 Developer Message\");\n" + "    problem2.setTotalOutstandingFees(1000);\n" + "    problem2.apply();\n" + "    problem2.throwIt();\n" + "  }\n" + "\n" + "  private void noAbortOnExecuteMethod()\n" + "  {\n" + "    AlreadyCheckedOutException acoe = new AlreadyCheckedOutException(\"Sup, developer\");\n" + "    acoe.setBookTitle(\"Atlas Shrugged\");\n" + "    throw acoe;\n"
        + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public void excelProblemMethod()\n" + "  {\n" + "    InheritanceException inheritanceException = new InheritanceException(\"This is a developer message\");" + "    inheritanceException.setLocale(" + CommonProperties.class.getName() + ".getDefaultLocale());" + "    ExcelProblem excelProblem = new ExcelProblem(inheritanceException);" + "    excelProblem.setRow(2);" + "    excelProblem.setColumn(\"Cookie\");"
        + "    excelProblem.throwIt();" + "  }\n" + "}\n");
    clientRequest.update(mdBusinessBook);

    clientRequest.lock(exception);
    exception.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "There are no copies of {bookTitle} available for checkout.");
    exception.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "Il n'existe pas de copies disponibles pour {bookTitle} checkout.");
    clientRequest.update(exception);

    clientRequest.lock(problem1);
    problem1.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "You already have {checkedOutBooks} books checked out.");
    problem1.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "Sie haben schon {checkedOutBooks} ausgeliehen.");
    clientRequest.update(problem1);

    clientRequest.lock(problem2);
    problem2.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "You have ${totalOutstandingFees}.00 in outstanding fees.");
    problem2.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "Sie haben unbezahlte Gebuehr in Summe ${totalOutstandingFees}.00.");
    clientRequest.update(problem2);

    clientRequest.lock(warning1);
    warning1.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "{maxAllowedBooks} is the maximum number of books you can check out at a given time.");
    warning1.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "{maxAllowedBooks} ist die Hoechstzahl der Buecher, die Sie zur gegebener Zeit ausleihen koennen.");
    clientRequest.update(warning1);

    clientRequest.lock(information1);
    information1.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "You may also enjoy {title}.");
    information1.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "Das Buch {title} werden Sie gefallen.");
    clientRequest.update(information1);

    clientRequest.lock(attributeProblem1);
    attributeProblem1.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "The title [{bookTitle}] is invalid.");
    clientRequest.update(attributeProblem1);
  }

  public static void classTearDown()
  {
    try
    {
      clientRequest.delete(mdViewBook.getId());

      clientRequest.delete(mdBusinessBook.getId());

      clientRequest.delete(exception.getId());

      clientRequest.delete(problem1.getId());

      clientRequest.delete(problem2.getId());

      clientRequest.delete(attributeProblem1.getId());

      clientRequest.delete(warning1.getId());

      clientRequest.delete(information1.getId());

      clientRequest.delete(newUser.getId());

      deleteLocale(Locale.GERMAN);
    }
    finally
    {
      systemSession.logout();
    }
  }

  public void testAttributeNotification()
  {
    BusinessDTO instance = clientRequest.newBusiness(bookType);

    try
    {
      clientRequest.createBusiness(instance);
    }
    catch (Throwable e)
    {
      if (! ( e instanceof ProblemExceptionDTO ))
      {
        fail("Exception expected was [" + ProblemExceptionDTO.class.getName() + "] but instead was [" + e.getClass().getName() + "]");
      }

      List<AttributeNotificationDTO> notificationList = clientRequest.getAttributeNotifications(instance.getId(), "title");

      assertTrue("List of attribute notifications is null", notificationList != null);

      assertTrue("No attribute notificatoins were returned", notificationList.size() > 0);
      assertEquals("The wrong number of attribute notificatoins was returned", 1, notificationList.size());

      AttributeNotificationDTO notificationDTO = notificationList.get(0);

      assertTrue("Returned notification is of the wrong type.", notificationDTO instanceof EmptyValueProblemDTO);
    }
  }

  public void testViewDomainAttributeMapping()
  {
    Class<?> viewDTOclass = LoaderDecorator.load(bookViewType + "DTO");

    Class<?> invalidTitleProblemDTOclass = LoaderDecorator.load(pack + ".InvalidTitleAttributeProblem" + "DTO");

    ViewDTO instance = (ViewDTO) clientRequest.newMutable(bookViewType);

    try
    {
      instance = (ViewDTO) viewDTOclass.getMethod("apply").invoke(instance);

      fail("Problem Exception not thrown.");
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (! ( te instanceof ProblemExceptionDTO ))
        {
          fail("Exception expected was [" + ProblemExceptionDTO.class.getName() + "] but instead was [" + te.getClass().getName() + "]");
        }

        List<AttributeNotificationDTO> notificationList = clientRequest.getAttributeNotifications(instance.getId(), "viewTitle");

        assertTrue("List of attribute notifications is null", notificationList != null);

        assertTrue("No attribute notificatoins were returned", notificationList.size() > 0);
        assertEquals("The wrong number of attribute notificatoins was returned", 1, notificationList.size());

        AttributeNotificationDTO notificationDTO = notificationList.get(0);

        assertTrue("Returned notification is of the wrong type.", invalidTitleProblemDTOclass.isInstance(notificationDTO));

        try
        {
          String definingType = (String) invalidTitleProblemDTOclass.getMethod("getDefiningType").invoke(notificationDTO);
          String definingTypeDisplayLabel = (String) invalidTitleProblemDTOclass.getMethod("getDefiningTypeDisplayLabel").invoke(notificationDTO);

          assertEquals("Containging type for an attributenotification was not remapped to the view type.", bookViewType, definingType);
          assertEquals("Containging type display label for an attributenotification was not remapped to the view type display label.", "A Book View", definingTypeDisplayLabel);
        }
        catch (Exception innerException)
        {
          fail(innerException.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
  }

  public void testWarning()
  {
    Class<?> warningClass = LoaderDecorator.load("test.bookstore.CheckoutLimitReachedDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      instance = (BusinessDTO) bookClass.getMethod("warningMethod").invoke(instance);

      // Make sure the original object is returned.
      assertEquals("Moby Dick", instance.getValue("title"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("No information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

      systemSession.logout();
      try
      {
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, Locale.GERMAN);
        clientRequest = getRequest(systemSession);
        instance = (BusinessDTO) clientRequest.get(instance.getId());
        instance = (BusinessDTO) bookClass.getMethod("warningMethod").invoke(instance);
        messageList = clientRequest.getMessages();
        messageDTO = messageList.get(0);
        localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
        assertEquals("10 ist die Hoechstzahl der Buecher, die Sie zur gegebener Zeit ausleihen koennen.", localizedMessage);
      }
      finally
      {
        systemSession.logout();
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD);
        clientRequest = getRequest(systemSession);
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testWarningMethodReturnArrayObjects()
  {
    Class<?> warningClass = LoaderDecorator.load("test.bookstore.CheckoutLimitReachedDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    BusinessDTO[] bookArray = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      bookArray = (BusinessDTO[]) bookClass.getMethod("warningMethodReturnArrayObjects").invoke(instance);

      assertEquals("Returned the wrong number of elements in the array", 2, bookArray.length);

      assertEquals("The Illiad", bookArray[0].getValue("title"));
      assertEquals("The Odyssey", bookArray[1].getValue("title"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("No information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }

      if (bookArray != null)
      {
        for (BusinessDTO bookDTO : bookArray)
        {
          clientRequest.delete(bookDTO.getId());
        }
      }
    }
  }

  public void testWarningMdMethodReturnQueryObject()
  {
    Class<?> warningClass = LoaderDecorator.load("test.bookstore.CheckoutLimitReachedDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance1 = null;
    BusinessDTO instance2 = null;
    BusinessQueryDTO bookQueryDTO = null;
    try
    {
      instance1 = clientRequest.newBusiness(bookType);
      instance1.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance1);

      instance2 = clientRequest.newBusiness(bookType);
      instance2.setValue("title", "Hunt for Red October");
      clientRequest.createBusiness(instance2);

      bookQueryDTO = (BusinessQueryDTO) bookClass.getMethod("warningMdMethodReturnQueryObject").invoke(instance1);

      assertEquals("Returned the wrong number of elements in the array", 2, bookQueryDTO.getCount());

      List<? extends BusinessDTO> businessDTOList = bookQueryDTO.getResultSet();

      assertEquals("Hunt for Red October", businessDTOList.get(0).getValue("title"));
      assertEquals("Moby Dick", businessDTOList.get(1).getValue("title"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("No information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance1 != null)
      {
        clientRequest.delete(instance1.getId());
      }

      if (instance2 != null)
      {
        clientRequest.delete(instance2.getId());
      }
    }
  }

  public void testWarningMdMethodReturnInt()
  {
    Class<?> warningClass = LoaderDecorator.load("test.bookstore.CheckoutLimitReachedDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance1 = null;
    try
    {
      instance1 = clientRequest.newBusiness(bookType);
      instance1.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance1);

      Integer integer = (Integer) bookClass.getMethod("warningMdMethodReturnInt").invoke(instance1);

      assertEquals("Returned the wrong Integer Value.", 5, integer.intValue());

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("No information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance1 != null)
      {
        clientRequest.delete(instance1.getId());
      }
    }
  }

  public void testWarningMdMethodReturnIntArray()
  {
    Class<?> warningClass = LoaderDecorator.load("test.bookstore.CheckoutLimitReachedDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance1 = null;
    try
    {
      instance1 = clientRequest.newBusiness(bookType);
      instance1.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance1);

      Integer[] integerArray = (Integer[]) bookClass.getMethod("warningMdMethodReturnIntArray").invoke(instance1);

      assertEquals("Returned Integer Array is of the wrong size.", 2, integerArray.length);

      assertEquals("Returned the wrong Integer Value.", 6, integerArray[0].intValue());

      assertEquals("Returned the wrong Integer Value.", 7, integerArray[1].intValue());

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("No information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance1 != null)
      {
        clientRequest.delete(instance1.getId());
      }
    }
  }

  public void testInformation()
  {
    Class<?> informationClass = LoaderDecorator.load("test.bookstore.RecommendedBookDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      instance = (BusinessDTO) bookClass.getMethod("informationMethod").invoke(instance);

      // Make sure the original object is returned.
      assertEquals("Moby Dick", instance.getValue("title"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("No warning should have been returned.", 0, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 1, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", informationClass.isInstance(messageDTO));

      String recommendedTitle = (String) informationClass.getMethod("getTitle").invoke(messageDTO);
      assertEquals("Attribute on information class has the wrong value.", "Tom Sawyer", recommendedTitle);

      String localizedMessage = (String) informationClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("You may also enjoy Tom Sawyer.", localizedMessage);

      systemSession.logout();
      try
      {
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, Locale.GERMAN);
        clientRequest = getRequest(systemSession);
        instance = (BusinessDTO) clientRequest.get(instance.getId());
        instance = (BusinessDTO) bookClass.getMethod("informationMethod").invoke(instance);
        messageList = clientRequest.getMessages();
        messageDTO = messageList.get(0);
        localizedMessage = (String) informationClass.getMethod("getMessage").invoke(messageDTO);
        assertEquals("Das Buch Tom Sawyer werden Sie gefallen.", localizedMessage);
      }
      finally
      {
        systemSession.logout();
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD);
        clientRequest = getRequest(systemSession);
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testMultipleMessages()
  {
    Class<?> warningClass = LoaderDecorator.load("test.bookstore.CheckoutLimitReachedDTO");
    Class<?> informationClass = LoaderDecorator.load("test.bookstore.RecommendedBookDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      instance = (BusinessDTO) bookClass.getMethod("multipleMessagesMethod").invoke(instance);

      // Make sure the original object is returned.
      assertEquals("Moby Dick", instance.getValue("title"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only two message should have been returned.", 2, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 1, informationList.size());

      boolean warningClassFound = false;
      boolean informationClassFound = false;
      for (MessageDTO messageDTO : messageList)
      {
        if (warningClass.isInstance(messageDTO))
        {
          warningClassFound = true;

          Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
          assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

          String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
          assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);
        }
        else if (informationClass.isInstance(messageDTO))
        {
          informationClassFound = true;

          String recommendedTitle = (String) informationClass.getMethod("getTitle").invoke(messageDTO);
          assertEquals("Attribute on information class has the wrong value.", "Tom Sawyer", recommendedTitle);

          String localizedMessage = (String) informationClass.getMethod("getMessage").invoke(messageDTO);
          assertEquals("You may also enjoy Tom Sawyer.", localizedMessage);
        }
        else
        {
          fail("Returned message was of the wrong type.");
        }
      }

      assertTrue("Both message types were not returned from the method.", warningClassFound && informationClassFound);

      systemSession.logout();
      try
      {
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, Locale.GERMAN);
        clientRequest = getRequest(systemSession);
        instance = (BusinessDTO) clientRequest.get(instance.getId());
        instance = (BusinessDTO) bookClass.getMethod("multipleMessagesMethod").invoke(instance);

        messageList = clientRequest.getMessages();

        for (MessageDTO messageDTO : messageList)
        {
          if (warningClass.isInstance(messageDTO))
          {
            String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
            assertEquals("10 ist die Hoechstzahl der Buecher, die Sie zur gegebener Zeit ausleihen koennen.", localizedMessage);
          }
          else if (informationClass.isInstance(messageDTO))
          {
            String localizedMessage = (String) informationClass.getMethod("getMessage").invoke(messageDTO);
            assertEquals("Das Buch Tom Sawyer werden Sie gefallen.", localizedMessage);
          }
        }
      }
      finally
      {
        systemSession.logout();
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD);
        clientRequest = getRequest(systemSession);
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testSkipIfProblems()
  {
    Class<?> exceptionClass = LoaderDecorator.load("test.bookstore.AlreadyCheckedOutExceptionDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      instance = (BusinessDTO) bookClass.getMethod("skipIfProblemsMethod").invoke(instance);
    }
    catch (Throwable e)
    {
      if (!instance.getValue("title").equals("Moby Dick"))
      {
        fail("Problems did not abort the transaction.  A value on an attribute was set. " + instance.getValue("title"));
      }

      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null)
        {
          if (exceptionClass.isInstance(te))
          {
            fail("Method marked to be skipped by an annotation was executed.");
          }

          if (! ( te instanceof ProblemExceptionDTO ))
          {
            fail("Method failed to throw problems");
          }
        }
        else
        {
          fail("Method failed to throw problems");
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testNoSkipIfProblems()
  {
    Class<?> exceptionClass = LoaderDecorator.load("test.bookstore.AlreadyCheckedOutExceptionDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      instance = (BusinessDTO) bookClass.getMethod("noSkipIfProblemsMethod").invoke(instance);
    }
    catch (Throwable e)
    {
      if (!instance.getValue("title").equals("Moby Dick"))
      {
        fail("Exception did not abort the transaction.  A value on an attribute was set. " + instance.getValue("title"));
      }

      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null)
        {
          if (!exceptionClass.isInstance(te))
          {
            fail("Method that was not marked to be skipped by an annotation was not executed.");
          }

          if (te instanceof ProblemExceptionDTO)
          {
            fail("Method throwed problems when it should not have");
          }
        }
        else
        {
          fail("Method failed to throw exception");
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testAbortIfProblemsMethod()
  {
    Class<?> exceptionClass = LoaderDecorator.load("test.bookstore.AlreadyCheckedOutExceptionDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      instance = (BusinessDTO) bookClass.getMethod("abortIfProblemsMethod").invoke(instance);
    }
    catch (Throwable e)
    {
      if (!instance.getValue("title").equals("Moby Dick"))
      {
        fail("Problems did not abort the transaction.  A value on an attribute was set. " + instance.getValue("title"));
      }

      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null)
        {
          if (exceptionClass.isInstance(te))
          {
            fail("Method marked to abort transaction by an annotation was executed.");
          }

          if (! ( te instanceof ProblemExceptionDTO ))
          {
            fail("Method failed to throw problems");
          }
          else
          {
            ProblemExceptionDTO problemExceptionDTO = (ProblemExceptionDTO) te;
            assertEquals("Only one problem should be returned.  Transaction should have been aborted before the second problem.", problemExceptionDTO.getProblems().size(), 1);
          }
        }
        else
        {
          fail("Method failed to throw problems");
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testNoAbortIfProblemsMethod()
  {
    Class<?> exceptionClass = LoaderDecorator.load("test.bookstore.AlreadyCheckedOutExceptionDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      instance = (BusinessDTO) bookClass.getMethod("noAbortIfProblemsMethod").invoke(instance);
    }
    catch (Throwable e)
    {
      if (!instance.getValue("title").equals("Moby Dick"))
      {
        fail("Problems did not abort the transaction.  A value on an attribute was set. " + instance.getValue("title"));
      }

      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null)
        {
          if (! ( exceptionClass.isInstance(te) ))
          {
            fail("Method not marked to abort transaction by an annotation was not executed.");
          }

          if (te instanceof ProblemExceptionDTO)
          {
            fail("Method throwed problems when it should have thrown an exception.");
          }
        }
        else
        {
          fail("Method failed to throw problems");
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testExcelProblemMethod()
  {
    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      bookClass.getMethod("excelProblemMethod").invoke(instance);
      fail("No ExcelProblem was thrown");
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null)
        {
          if (te instanceof ProblemExceptionDTO)
          {
            ProblemExceptionDTO problemExceptionDTO = (ProblemExceptionDTO) te;
            List<? extends ProblemDTOIF> problems = problemExceptionDTO.getProblems();
            if (problems.size() != 1)
              fail("Expected 1 ProblemDTO, found " + problems.size());

            ProblemDTOIF problem = problems.get(0);
            if (! ( problem instanceof ExcelProblemDTO ))
              fail("Expected ExcelProblemDTO, found " + problem.getClass().getName());

            ExcelProblemDTO excelProblem = (ExcelProblemDTO) problem;
            if (excelProblem.getRowNumber() != 2)
              fail("Expected rowNumber=2, found " + excelProblem.getRowNumber());

            if (!excelProblem.getColumn().equals("Cookie"))
              fail("Expected Column.equals(\"Cookie\"), found " + excelProblem.getColumn());
          }
          else
          {
            fail("Expected ProblemExceptionDTO, caught " + te.getClass().getName());
          }
        }
        else
        {
          fail("Method failed to throw problems");
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
  }

  public void testThrow()
  {
    Class<?> exceptionClass = LoaderDecorator.load("test.bookstore.AlreadyCheckedOutExceptionDTO");

    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");

    BusinessDTO instance = null;
    try
    {
      instance = clientRequest.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      clientRequest.createBusiness(instance);

      bookClass.getMethod("exceptionMethod").invoke(instance);
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null && exceptionClass.isInstance(te))
        {
          String localizedMessage = "";

          try
          {
            localizedMessage = (String) exceptionClass.getMethod("getMessage").invoke(te);
          }
          catch (Exception exception)
          {
            fail(exception.getMessage());
          }

          assertEquals("There are no copies of Atlas Shrugged available for checkout.", localizedMessage);
        }
        else
        {
          fail(e.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testClientSideLocalizeException()
  {
    Class<?> alreadyCheckedOutDTOclass = LoaderDecorator.load("test.bookstore.AlreadyCheckedOutExceptionDTO");

    try
    {
      // String sessionId, ProxyIF clientRequest, Locale locale
      SmartExceptionDTO smartExceptionDTO = (SmartExceptionDTO) alreadyCheckedOutDTOclass.getConstructor(ClientRequestIF.class, Locale.class).newInstance(clientRequest, CommonProperties.getDefaultLocale());
      alreadyCheckedOutDTOclass.getMethod("setBookTitle", String.class).invoke(smartExceptionDTO, "Moby Dick");

      throw smartExceptionDTO;
    }
    catch (SmartExceptionDTO smartExceptionDTO)
    {
      assertEquals("There are no copies of Moby Dick available for checkout.", smartExceptionDTO.getMessage());
    }
    catch (Exception exception)
    {
      fail(exception.getMessage());
    }

  }

  public void testClientSideLocalizeProblem()
  {
    Class<?> tooManyCheckedOutDTOclass = LoaderDecorator.load("test.bookstore.TooManyCheckedOutBooksProblemDTO");

    try
    {
      // String sessionId, ProxyIF clientRequest, Locale locale
      ProblemDTO problemDTO = (ProblemDTO) tooManyCheckedOutDTOclass.getConstructor(ClientRequestIF.class, Locale.class).newInstance(clientRequest, CommonProperties.getDefaultLocale());
      tooManyCheckedOutDTOclass.getMethod("setCheckedOutBooks", Integer.class).invoke(problemDTO, 10);

      assertEquals("You already have 10 books checked out.", problemDTO.getMessage());
    }
    catch (IllegalAccessException e)
    {
      fail(e.getMessage());
    }
    catch (InvocationTargetException e)
    {
      fail(e.getMessage());
    }
    catch (NoSuchMethodException e)
    {
      fail(e.getMessage());
    }
    catch (InstantiationException e)
    {
      fail(e.getMessage());
    }

  }

  @SuppressWarnings("unchecked")
  public void testProblem_DeveloperMessage()
  {
    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");
    Class<?> tooManyCheckedOutDTOclass = LoaderDecorator.load("test.bookstore.TooManyCheckedOutBooksProblemDTO");
    Class<?> overdueFeesDTOclass = LoaderDecorator.load("test.bookstore.OverdueLibraryFeesProblemDTO");

    List<ProblemDTO> problemDTOList = new LinkedList<ProblemDTO>();

    boolean foundTooManyCheckedOut = false;
    boolean foundOverdueFee = false;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    BusinessDTO instance = null;
    try
    {
      clientRequest.assignMember(newUser.getId(), RoleDAOIF.DEVELOPER_ROLE);
      clientRequest.grantTypePermission(newUser.getId(), mdBusinessBook.getId(), Operation.CREATE.name());
      clientRequest.grantTypePermission(newUser.getId(), mdBusinessBook.getId(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(newUser.getId(), mdAttributeTitle.getId(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(newUser.getId(), mdAttributeTitle.getId(), Operation.READ.name());
      clientRequest.grantAttributePermission(newUser.getId(), mdAttributeLockedBy.getId(), Operation.READ.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      instance = tommyProxy.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");

      tommyProxy.createBusiness(instance);

      bookClass.getMethod("problemMethod").invoke(instance);

    }
    catch (Throwable e)
    {
      if (instance != null)
      {
        if (!instance.getValue("title").equals("Moby Dick"))
        {
          fail("Problems did not abort the transaction.  A value on an attribute was set. " + instance.getValue("title"));
        }
        if (!instance.getValue("lockedBy").equals(""))
        {
          fail("Problems did not abort the transaction.  User lock was not rolled back.");
        }
      }

      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null && te instanceof ProblemExceptionDTO)
        {
          Class<?> problemExceptionDTOclass = LoaderDecorator.load(ProblemExceptionDTO.class.getName());

          try
          {
            problemDTOList = (List<ProblemDTO>) problemExceptionDTOclass.getMethod("getProblems").invoke(te);
          }
          catch (Exception exception)
          {
            fail(exception.getMessage());
          }

          for (ProblemDTO problemDTO : problemDTOList)
          {
            if (tooManyCheckedOutDTOclass.isInstance(problemDTO))
            {
              foundTooManyCheckedOut = true;

              int numberOfBooks = 10;
              try
              {
                numberOfBooks = (Integer) tooManyCheckedOutDTOclass.getMethod("getCheckedOutBooks").invoke(problemDTO);
              }
              catch (Exception exception)
              {
                fail(exception.getMessage());
              }

              assertEquals("Problem1 Developer Message", problemDTO.getDeveloperMessage());
              assertEquals("You already have 10 books checked out.", problemDTO.getMessage());
              assertEquals(10, numberOfBooks);
            }
            else if (overdueFeesDTOclass.isInstance(problemDTO))
            {
              foundOverdueFee = true;

              int overdueFees = 1000;
              try
              {
                overdueFees = (Integer) overdueFeesDTOclass.getMethod("getTotalOutstandingFees").invoke(problemDTO);
              }
              catch (Exception exception)
              {
                fail(exception.getMessage());
              }

              assertEquals("Problem2 Developer Message", problemDTO.getDeveloperMessage());
              assertEquals("You have $1000.00 in outstanding fees.", problemDTO.getMessage());
              assertEquals(overdueFees, 1000);
            }
          }
        }
        else
        {
          fail(e.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }

      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }

      clientRequest.revokeTypePermission(newUser.getId(), mdBusinessBook.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(newUser.getId(), mdBusinessBook.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(newUser.getId(), mdAttributeTitle.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(newUser.getId(), mdAttributeTitle.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(newUser.getId(), mdAttributeLockedBy.getId(), Operation.READ.name());
      clientRequest.removeMember(newUser.getId(), RoleDAOIF.DEVELOPER_ROLE);
    }

    assertEquals("Wrong number of problems was generated.", 2, problemDTOList.size());
    assertTrue("The [" + tooManyCheckedOutDTOclass + "] problem was not returned.", foundTooManyCheckedOut);
    assertTrue("The [" + overdueFeesDTOclass + "] problem was not returned.", foundOverdueFee);
  }

  @SuppressWarnings("unchecked")
  public void testProblem_NoDeveloperMessage()
  {
    Class<?> bookClass = LoaderDecorator.load("test.bookstore.BookDTO");
    Class<?> tooManyCheckedOutDTOclass = LoaderDecorator.load("test.bookstore.TooManyCheckedOutBooksProblemDTO");
    Class<?> overdueFeesDTOclass = LoaderDecorator.load("test.bookstore.OverdueLibraryFeesProblemDTO");

    List<ProblemDTO> problemDTOList = new LinkedList<ProblemDTO>();

    boolean foundTooManyCheckedOut = false;
    boolean foundOverdueFee = false;

    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    BusinessDTO instance = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getId(), mdBusinessBook.getId(), Operation.CREATE.name());
      clientRequest.grantTypePermission(newUser.getId(), mdBusinessBook.getId(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(newUser.getId(), mdAttributeTitle.getId(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(newUser.getId(), mdAttributeTitle.getId(), Operation.READ.name());
      clientRequest.grantAttributePermission(newUser.getId(), mdAttributeLockedBy.getId(), Operation.READ.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      instance = tommyProxy.newBusiness(bookType);
      instance.setValue("title", "Moby Dick");
      tommyProxy.createBusiness(instance);

      bookClass.getMethod("problemMethod").invoke(instance);

    }
    catch (Throwable e)
    {
      if (instance != null)
      {
        if (!instance.getValue("title").equals("Moby Dick"))
        {
          fail("Problems did not abort the transaction.  A value on an attribute was set. " + instance.getValue("title"));
        }
        if (!instance.getValue("lockedBy").equals(""))
        {
          fail("Problems did not abort the transaction.  User lock was not rolled back.");
        }
      }

      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null && te instanceof ProblemExceptionDTO)
        {
          Class<?> problemExceptionDTOclass = LoaderDecorator.load(ProblemExceptionDTO.class.getName());

          try
          {
            problemDTOList = (List<ProblemDTO>) problemExceptionDTOclass.getMethod("getProblems").invoke(te);
          }
          catch (Exception exception)
          {
            fail(exception.getMessage());
          }

          for (ProblemDTO problemDTO : problemDTOList)
          {
            if (tooManyCheckedOutDTOclass.isInstance(problemDTO))
            {
              foundTooManyCheckedOut = true;

              int numberOfBooks = 10;
              try
              {
                numberOfBooks = (Integer) tooManyCheckedOutDTOclass.getMethod("getCheckedOutBooks").invoke(problemDTO);
              }
              catch (Exception exception)
              {
                fail(exception.getMessage());
              }

              assertEquals("", problemDTO.getDeveloperMessage());
              assertEquals("You already have 10 books checked out.", problemDTO.getMessage());
              assertEquals(10, numberOfBooks);
            }
            else if (overdueFeesDTOclass.isInstance(problemDTO))
            {
              foundOverdueFee = true;

              int overdueFees = 1000;
              try
              {
                overdueFees = (Integer) overdueFeesDTOclass.getMethod("getTotalOutstandingFees").invoke(problemDTO);
              }
              catch (Exception exception)
              {
                fail(exception.getMessage());
              }

              assertEquals("", problemDTO.getDeveloperMessage());
              assertEquals("You have $1000.00 in outstanding fees.", problemDTO.getMessage());
              assertEquals(overdueFees, 1000);
            }
          }
        }
        else
        {
          fail(e.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }

      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }

      clientRequest.revokeTypePermission(newUser.getId(), mdBusinessBook.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(newUser.getId(), mdBusinessBook.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(newUser.getId(), mdAttributeTitle.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(newUser.getId(), mdAttributeTitle.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(newUser.getId(), mdAttributeLockedBy.getId(), Operation.READ.name());
    }

    assertEquals("Wrong number of problems was generated.", 2, problemDTOList.size());
    assertTrue("The [" + tooManyCheckedOutDTOclass + "] problem was not returned.", foundTooManyCheckedOut);
    assertTrue("The [" + overdueFeesDTOclass + "] problem was not returned.", foundOverdueFee);
  }

  @Request
  private static void addLocale(Locale locale)
  {
    SupportedLocaleDAO supportedLocaleDAO = SupportedLocaleDAO.newInstance();
    supportedLocaleDAO.setValue(SupportedLocaleInfo.NAME, locale.toString());
    supportedLocaleDAO.setValue(SupportedLocaleInfo.DISPLAY_LABEL, locale.toString());
    supportedLocaleDAO.setValue(SupportedLocaleInfo.LOCALE_LABEL, locale.toString());
    supportedLocaleDAO.apply();
  }

  @Request
  private static void deleteLocale(Locale locale)
  {
    SupportedLocaleDAO supportedLocaleDAO = (SupportedLocaleDAO) SupportedLocaleDAO.getEnumeration(SupportedLocaleInfo.CLASS, locale.toString());
    supportedLocaleDAO.delete();
  }
}
