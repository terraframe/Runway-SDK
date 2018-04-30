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
package com.runwaysdk.dataaccess.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.runwaysdk.CommonExceptionMessageLocalizer;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.RunwayException;
import com.runwaysdk.RunwayExceptionIF;
import com.runwaysdk.StopTransactionException;
import com.runwaysdk.SystemException;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Entity;
import com.runwaysdk.business.LocalizableIF;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.io.excel.AttributeColumn;
import com.runwaysdk.dataaccess.io.excel.ContextBuilder;
import com.runwaysdk.dataaccess.io.excel.ContextBuilderIF;
import com.runwaysdk.dataaccess.io.excel.ErrorSheet;
import com.runwaysdk.dataaccess.io.excel.ExcelColumn;
import com.runwaysdk.dataaccess.io.excel.ExcelUtil;
import com.runwaysdk.dataaccess.io.excel.ImportApplyListener;
import com.runwaysdk.dataaccess.io.excel.ImportListener;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.logging.LogLevel;
import com.runwaysdk.logging.RunwayLogUtil;
import com.runwaysdk.session.RequestState;
import com.runwaysdk.session.Session;
import com.runwaysdk.transport.conversion.ExcelErrors;
import com.runwaysdk.transport.conversion.ExcelMessage;

public class ExcelImporter
{
  private static final String ERROR_SHEET = "Error Messages";

  private ContextBuilderIF    builder;

  /**
   * List of sheets in the excel file we're importing
   */
  private List<ImportContext> contexts;

  /**
   * The in memory representation of the error xls file
   */
  private Workbook            errorWorkbook;

  /**
   * Optional logger
   */
  private ExcelImportProgressMonitorIF    monitor;

  /**
   * Constructor for this importer. Opens the stream and parses some header information. The source stream is accepted as is, so any necessary buffering should be handled by the caller.
   * 
   * @param stream
   */
  public ExcelImporter(InputStream stream)
  {
    this(stream, new ContextBuilder());
  }

  public ExcelImporter(InputStream stream, ContextBuilderIF builder)
  {
    this.builder = builder;
    this.contexts = new LinkedList<ImportContext>();

    this.openStream(stream);
  }

  /**
   * @param monitor
   *          the progress monitor to set
   */
  public void setProgressMonitor(ExcelImportProgressMonitorIF monitor)
  {
    this.monitor = monitor;
  }

  /**
   * Opens the stream, parses the types from the sheets and set up context objects for them
   * 
   * @param stream
   * @return
   * @throws IOException
   */
  private void openStream(InputStream stream)
  {
    try
    {
      Workbook workbook = ExcelUtil.getWorkbook(stream);

      this.errorWorkbook = ExcelUtil.createWorkbook(workbook);

      for (int i = 0; i < workbook.getNumberOfSheets(); i++)
      {
        Sheet sheet = workbook.getSheetAt(i);
        String sheetName = workbook.getSheetName(i);

        // Skip the error sheet
        if (this.isValidSheet(sheet, sheetName))
        {
          Row row = sheet.getRow(0);
          Cell cell = row.getCell(0);
          String type = ExcelUtil.getString(cell);

          contexts.add(builder.createContext(sheet, sheetName, errorWorkbook, type));
        }
      }

      errorWorkbook.createSheet(ERROR_SHEET);
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  private boolean isValidSheet(Sheet sheet, String sheetName)
  {
    if (!sheetName.equals(ERROR_SHEET))
    {
      Row row = sheet.getRow(0);

      if (row != null)
      {
        Cell cell = row.getCell(0);
        String type = ExcelUtil.getString(cell);

        try
        {
          return ( type != null && MdTypeDAO.getMdTypeDAO(type) != null );
        }
        catch (Exception e)
        {
          return false;
        }
      }
    }

    return false;
  }

  /**
   * The standard entry point for reading an excel file.
   * 
   * @param stream
   */
  public byte[] read()
  {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedOutputStream buffer = new BufferedOutputStream(bytes);

    try
    {
      this.read(buffer);
    }
    finally
    {
      try
      {
        buffer.flush();
        buffer.close();
      }
      catch (IOException e)
      {
        throw new FileWriteException(null, e);
      }
    }

    return bytes.toByteArray();
  }

  /**
   * The standard entry point for reading an excel file.
   * 
   * @param stream
   */
  public void read(OutputStream stream)
  {
    // Initialize some class variables
    boolean hadErrors = false;

    // Calculate how many total rows there are
    long totalRows = 0;
    for (ImportContext context : contexts)
    {
      Sheet sheet = context.getImportSheet();
      totalRows += sheet.getLastRowNum() - 2; // zero indexed so add one then we have 3 headers so subtract 3
    }
    if (this.monitor != null)
    {
      this.monitor.setTotalRows(totalRows);
    }
    
    // Actually do the import
    long rowNum = 0;
    for (ImportContext context : contexts)
    {
      readSheet(context, rowNum);
      
      Sheet sheet = context.getImportSheet();
      totalRows += sheet.getLastRowNum() - 2;
      rowNum = rowNum + totalRows;

      if (context.hasErrors())
      {
        hadErrors = true;
      }
    }
    
    for (ImportContext context : contexts)
    {
      for (ImportListener listener : context.getListeners())
      {
        listener.onFinishImport();
      }
    }

    // If we had no errors, just return an empty array
    if (!hadErrors)
    {
      return;
    }

    // Resize the columns for error sheets
    for (ImportContext c : contexts)
    {
      c.autoSizeErrorSheet();
    }

    // Write the error messages to sheet 2
    writeMessages(false);

    try
    {
      // Write out the bytes
      errorWorkbook.write(stream);
    }
    catch (IOException e)
    {
      throw new SystemException(e);
    }
  }

  private void readSheet(ImportContext context, long baseRow)
  {
    Sheet sheet = context.getImportSheet();
    Iterator<Row> rowIterator = sheet.rowIterator();

    // Parse the header rows
    readHeaders(context, rowIterator);

    // The main loop where we import each row as an instance
    while (rowIterator.hasNext())
    {
      try
      {
        readRow(context, rowIterator.next(), baseRow);
      }
      catch (ProgrammingErrorException e)
      {
        Throwable cause = e.getCause();

        if (! ( cause instanceof StopTransactionException ))
        {
          throw e;
        }

      }
      catch (StopTransactionException e)
      {
        // This is thrown only to keep the Transaction from committing. We can
        // ignore it.
      }
    }
  }

  /**
   * Reads a row and captures and Exceptions or Problems associated with it
   * 
   * @param context
   *          TODO
   * @param row
   */
  @Transaction
  private void readRow(ImportContext context, Row row, long baseRow)
  {
    if (!rowHasValues(row))
    {
      return;
    }
    
    int previousErrorCount = context.getErrorCount();
    
    if (this.monitor != null)
    {
      this.monitor.setCurrentRow(row.getRowNum() + 1 + baseRow);
    }

    try
    {
      context.readRow(row, this.monitor);
    }
    catch (Exception e)
    {
      context.addException(e);
    }

    // Loop over any problems we encountered and wrap them as ExcelProblems
    List<ProblemIF> problemsInTransaction = RequestState.getProblemsInCurrentRequest();

    for (ProblemIF problem : problemsInTransaction)
    {
      if (isEmptyValueProblem(context, problem, row.getRowNum() + 1))
        continue;

      context.addProblem(problem);
    }
    // We've rewrapped and stored these problems, so clear them out of the
    // Session buffer
    problemsInTransaction.clear();

    // If there are new problems, then this row has failed. Append it to the
    // error file
    if (previousErrorCount != context.getErrorCount())
    {
      context.addErrorRow(row);

      // We don't want the transaction to commit, so this is thrown to ensure
      // that it doesn't. It gets caught one layer up.
      throw new StopTransactionException(true);
    }
  }

  /**
   * Checks to see if the given row has specified at least one column with a value
   * 
   * @param row
   * @return
   */
  private boolean rowHasValues(Row row)
  {
    Iterator<Cell> cellIterator = row.cellIterator();

    while (cellIterator.hasNext())
    {
      Cell cell = cellIterator.next();
      int cellType = cell.getCellType();

      if (cellType == Cell.CELL_TYPE_FORMULA)
      {
        cellType = cell.getCachedFormulaResultType();
      }

      Object value = null;

      switch (cellType)
      {
        case Cell.CELL_TYPE_STRING:
          value = ExcelUtil.getString(cell);
          break;
        case Cell.CELL_TYPE_BOOLEAN:
          value = ExcelUtil.getBoolean(cell);
          break;
        case Cell.CELL_TYPE_NUMERIC:
          value = cell.getNumericCellValue();
          break;
      }

      if (value == null)
      {
        continue;
      }

      if (value.toString().trim().length() > 0)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * If there is an exception when we attempt to set a value, we'll get a misleading EmptyValueProblem when we try to apply(), because the exception prevented the value from ever being set. The
   * original message is more accurate, so we'd like to eliminate the noise of the EmptyValueProblem.
   * 
   * This method returns true if the given problem is an instance of EmptyValueProblem and we've already observed an error on this row on the empty attribute.
   * 
   * @param context
   *          TODO
   * @param problem
   * @param rowNumber
   * 
   * @return
   */
  private boolean isEmptyValueProblem(ImportContext context, ProblemIF problem, int rowNumber)
  {
    if (! ( problem instanceof EmptyValueProblem ))
    {
      return false;
    }

    EmptyValueProblem evp = (EmptyValueProblem) problem;
    List<ExcelMessage> messages = context.getErrorMessages();
    for (int i = messages.size() - 1; i >= 0; i--)
    {
      ExcelMessage excelMessage = messages.get(i);

      // If we hit a previous row, there was no match on this row. Return false.
      if (excelMessage.getRow() < rowNumber)
      {
        return false;
      }

      // If the attribute wasn't set, we can't match against it. Try the next
      // message.
      String attributeName = excelMessage.getMdAttribute();
      if (attributeName == null)
      {
        continue;
      }

      // Is there is an ExcelMessage with the same row and same attribute?
      if (excelMessage.getRow() == rowNumber && attributeName.equals(evp.getAttributeName()))
      {
        return true;
      }
    }

    // If we go through the whole list of messages, we haven't hit a match
    return false;
  }

  /**
   * Reads the first two rows, which represent the attribute names and attribute display labels respectively.
   * 
   * @param context
   *          TODO
   * @param iterator
   */
  private void readHeaders(ImportContext context, Iterator<Row> iterator)
  {
    Row typeRow = iterator.next();
    Row nameRow = iterator.next();
    Row labelRow = iterator.next();

    builder.configure(context, typeRow, nameRow, labelRow);
  }

  /**
   * Writes out errors to the correct sheet. Inclusion of the "Column" column is based on the passed parameter. "Row" and "Message" columns are always included.
   * 
   * @param includeColumn
   */
  private void writeMessages(boolean includeColumn)
  {
    int col = 0;

    CreationHelper helper = errorWorkbook.getCreationHelper();
    Sheet sheet = errorWorkbook.getSheet(ERROR_SHEET);
    Row row = sheet.createRow(0);

    row.createCell(col++).setCellValue(helper.createRichTextString("Row"));
    row.createCell(col++).setCellValue(helper.createRichTextString("Sheet"));

    if (includeColumn)
    {
      row.createCell(col++).setCellValue(helper.createRichTextString("Column"));
    }

    row.createCell(col++).setCellValue(helper.createRichTextString("Error Message"));

    int i = 1;

    for (ImportContext c : contexts)
    {
      for (ExcelMessage message : c.getErrorMessages())
      {
        col = 0;
        row = sheet.createRow(i++);
        row.createCell(col++).setCellValue(message.getRow());
        row.createCell(col++).setCellValue(helper.createRichTextString(c.getSheetName()));
        if (includeColumn)
        {
          row.createCell(col++).setCellValue(helper.createRichTextString(message.getColumn()));
        }
        row.createCell(col++).setCellValue(helper.createRichTextString(message.getMessage()));
      }
    }

    short c = 0;
    sheet.autoSizeColumn(c++);
    if (includeColumn)
    {
      sheet.autoSizeColumn(c++);
    }
    sheet.autoSizeColumn(c++);
  }

  public List<ImportContext> getContexts()
  {
    return contexts;
  }

  public static class ImportContext
  {
    /**
     * The sheet containing the user input
     */
    private Sheet                 importSheet;

    /**
     * The name of the importing sheet
     */
    private String                sheetName;

    /**
     * The MdClass that this context is importing
     */
    private MdClassDAOIF          mdClass;

    /**
     * A list of columns that are defined by metadata and thus are expected
     */
    private List<AttributeColumn> expectedColumns;

    /**
     * A list of columns that are in the file but not associated with metadata
     */
    private List<ExcelColumn>     extraColumns;

    /**
     * List of registered listeners for this context.
     */
    private List<ImportListener>  listeners;

    /**
     * A wrapper containing all of the {@link ExcelMessage}s that have been created as other {@link Exception}s or {@link ProblemIF}s have been caught, and annotated with row/column information
     */
    private ExcelErrors           errors;

    private ErrorSheet            errorSheet;

    public ImportContext(Sheet importSheet, String sheetName, Sheet errorSheet, MdClassDAOIF mdClass)
    {
      this.importSheet = importSheet;
      this.sheetName = sheetName;
      this.errorSheet = new ErrorSheet(errorSheet);
      this.mdClass = mdClass;

      this.extraColumns = new LinkedList<ExcelColumn>();
      this.expectedColumns = new LinkedList<AttributeColumn>();
      this.listeners = new LinkedList<ImportListener>();
      this.errors = new ExcelErrors();
    }

    /**
     * Adds the given listener to this Exporter
     * 
     * @param listener
     */
    public void addListener(ImportListener listener)
    {
      listeners.add(listener);
    }

    public MdClassDAOIF getMdClass()
    {
      return this.mdClass;
    }

    public List<AttributeColumn> getExpectedColumns()
    {
      return expectedColumns;
    }

    public List<ExcelColumn> getExtraColumns()
    {
      return extraColumns;
    }

    public List<ImportListener> getListeners()
    {
      return listeners;
    }

    public List<ImportApplyListener> getApplyListeners()
    {
      List<ImportApplyListener> list = new LinkedList<ImportApplyListener>();

      for (ImportListener listener : listeners)
      {
        if (listener instanceof ImportApplyListener)
        {
          list.add((ImportApplyListener) listener);
        }
      }

      return list;
    }

    public void addExtraColumn(ExcelColumn column)
    {
      this.extraColumns.add(column);
    }

    public void addExpectedColumn(AttributeColumn column)
    {
      this.expectedColumns.add(column);
    }

    public String getMdClassType()
    {
      return this.mdClass.definesType();
    }

    public void addError(ExcelMessage message)
    {
      this.errors.add(message);
    }

    public List<ExcelMessage> getErrorMessages()
    {
      return this.errors.getMessages();
    }

    public boolean hasErrors()
    {
      return ( this.errors.size() > 0 );
    }

    public int getErrorCount()
    {
      return this.errors.size();
    }

    public String getSheetName()
    {
      return this.sheetName;
    }

    public Sheet getImportSheet()
    {
      return this.importSheet;
    }

    public void addErrorRow(Row row)
    {
      this.errorSheet.addRow(row);
    }

    public void autoSizeErrorSheet()
    {
      this.errorSheet.autoSize();
    }

    public int getErrorRowCount()
    {
      return this.errorSheet.getCount();
    }

    public void addProblem(ProblemIF problem)
    {
      addProblem("", null, problem);
    }

    public void addProblem(String column, MdAttributeDAOIF mdAttribute, ProblemIF problem)
    {
      problem.setLocale(Session.getCurrentLocale());
      String message = problem.getLocalizedMessage();

      // If there is no message, at least say what type of exception this is
      if (message == null)
      {
        message = problem.getClass().getName();
      }

      String attributeName = null;
      if (mdAttribute != null)
      {
        attributeName = mdAttribute.definesAttribute();
      }

      int count = this.getErrorRowCount();

      this.addError(new ExcelMessage(count + 1, column, message, attributeName));
    }

    public void addException(Exception e)
    {
      addException("", null, e);
    }

    public void addException(String column, MdAttributeDAOIF mdAttribute, Exception cause)
    {
      String message = cause.getClass().getSimpleName();
      String local;
      if (cause instanceof RunwayException)
      {
        ( (RunwayException) cause ).setLocale(Session.getCurrentLocale());
      }
      if (cause instanceof LocalizableIF)
      {
        local = ( (LocalizableIF) cause ).localize(Session.getCurrentLocale());
      }
      else
      {
        local = cause.getLocalizedMessage();
      }

      if (local != null)
      {
        message += ": " + local;
      }
      
      String attributeName = null;
      if (mdAttribute != null)
      {
        attributeName = mdAttribute.definesAttribute();
      }

      int count = this.getErrorRowCount();
      
      if (CommonExceptionMessageLocalizer.runwayException(Session.getCurrentLocale()).equals(local) || !(cause instanceof RunwayExceptionIF))
      {
        RunwayLogUtil.logToLevel(LogLevel.ERROR, "An unusual error occurred while importing data in cell (row:column) [" + (count + 1) + ":" + column + "]. " + message, cause);
      }

      this.addError(new ExcelMessage(count + 1, column, message, attributeName));
    }

    /**
     * Reads a single row, instantiating an instance and calling typesafe setters for each attribute
     * 
     * @param row
     */
    public void readRow(Row row, ExcelImportProgressMonitorIF monitor) throws Exception
    {
      Mutable instance = this.getMutableForRow(row);

      for (AttributeColumn column : this.getExpectedColumns())
      {
        Cell cell = row.getCell(column.getIndex());

        // Don't try to do anything for blank cells
        if (cell == null)
        {
          continue;
        }

        try
        {
          Object attributeValue = column.getValue(cell);

          column.setInstanceValue(instance, attributeValue);
        }
        catch (InvocationTargetException e)
        {
          Throwable targetException = e.getTargetException();
          if (targetException instanceof Exception)
          {
            this.addException(column.getDisplayLabel(), column.getMdAttribute(), (Exception) targetException);
          }
          else
          {
            this.addException(column.getDisplayLabel(), column.getMdAttribute(), e);
          }
        }
        catch (Exception e)
        {
          this.addException(column.getDisplayLabel(), column.getMdAttribute(), e);
        }

        List<ProblemIF> problemsInTransaction = RequestState.getProblemsInCurrentRequest();

        for (ProblemIF problem : problemsInTransaction)
        {
          this.addProblem(column.getDisplayLabel(), column.getMdAttribute(), problem);
        }

        problemsInTransaction.clear();
      }

      // Now let the listeners do whatever they will with the extra columns
      for (ImportListener listener : this.getListeners())
      {
        listener.handleExtraColumns(instance, this.getExtraColumns(), row);
      }

      List<ImportApplyListener> listeners = this.getApplyListeners();

      for (ImportApplyListener listener : listeners)
      {
        listener.beforeApply(instance);
      }

      HashMap<String, List<Entity>> extraEntities = new HashMap<String, List<Entity>>();

      for (ImportApplyListener listener : listeners)
      {
        listener.addAdditionalEntities(extraEntities);
      }

      for (ImportApplyListener listener : listeners)
      {
        listener.validate(instance, extraEntities);
      }

      instance.apply();

      for (ImportApplyListener listener : listeners)
      {
        listener.afterApply(instance);
      }

      if (monitor != null)
      {
        monitor.entityImported(instance, extraEntities);
      }
    }

    protected Mutable getMutableForRow(Row row)
    {
      return BusinessFacade.newMutable(this.getMdClassType());
    }

  }
}
