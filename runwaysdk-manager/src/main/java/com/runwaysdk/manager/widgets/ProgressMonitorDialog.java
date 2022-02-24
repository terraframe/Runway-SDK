/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.manager.widgets;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ProgressMonitorDialog extends Dialog implements IProgressMonitor
{
  /**
   * Name to use for task when normal task name is empty string.
   */
  private static String       DEFAULT_TASKNAME = JFaceResources.getString("ProgressMonitorDialog.message"); //$NON-NLS-1$

  /**
   * Constants for label and monitor size
   */
  private static int          LABEL_DLUS       = 21;

  private static int          BAR_DLUS         = 9;

  /**
   * The progress indicator control.
   */
  protected ProgressIndicator progressIndicator;

  /**
   * The label control for the task. Kept for backwards compatibility.
   */
  protected Label             taskLabel;

  /**
   * The label control for the subtask.
   */
  protected Label             subTaskLabel;

  /**
   * The cursor used in the cancel button;
   */
  protected Cursor            arrowCursor;

  /**
   * The cursor used in the shell;
   */
  private Cursor              waitCursor;

  private boolean             canceled;

  private Display             display;

  private List<Runnable>      runnables;

  public ProgressMonitorDialog(Shell parentShell)
  {
    super(parentShell);

    this.canceled = false;
    this.display = parentShell.getDisplay();
    this.runnables = new LinkedList<Runnable>();

    if (isResizable())
    {
      setShellStyle(getDefaultOrientation() | SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL | SWT.RESIZE | SWT.MAX);
    }
    else
    {
      setShellStyle(getDefaultOrientation() | SWT.BORDER | SWT.TITLE | SWT.APPLICATION_MODAL);
    }
  }

  @Override
  protected void configureShell(final Shell shell)
  {
    super.configureShell(shell);

    shell.setText(JFaceResources.getString("ProgressMonitorDialog.title")); //$NON-NLS-1$

    if (waitCursor == null)
    {
      waitCursor = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
    }

    shell.setCursor(waitCursor);

    // Add a listener to set the message properly when the dialog becomes
    // visible
    shell.addListener(SWT.Show, new Listener()
    {
      public void handleEvent(Event event)
      {
        // We need to async the message update since the Show precedes
        // visibility
        shell.getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            setTask(DEFAULT_TASKNAME, true);

            for (Runnable runnable : runnables)
            {
              runnable.run();
            }
          }
        });
      }
    });
  }

  /**
   * Set the message in the message label.
   * 
   * @param taskString
   *          The string for the new message.
   * @param force
   *          If force is true then always set the message text.
   */
  private void setTask(String taskString, boolean force)
  {
    String task = taskString == null ? "" : taskString;

    if (taskLabel == null || taskLabel.isDisposed())
    {
      return;
    }
    if (force || taskLabel.isVisible())
    {
      taskLabel.setToolTipText(task);
      taskLabel.setText(shortenText(task, taskLabel));
    }
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite composite = (Composite) super.createDialogArea(parent);
    composite.setLayout(new GridLayout());

    taskLabel = new Label(composite, SWT.WRAP);
    taskLabel.setText(DEFAULT_TASKNAME);
    GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false).hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH), SWT.DEFAULT).applyTo(taskLabel);

    // progress indicator
    GridData gd = new GridData();
    gd.heightHint = convertVerticalDLUsToPixels(BAR_DLUS);
    gd.horizontalAlignment = GridData.FILL;
    gd.grabExcessHorizontalSpace = true;
    gd.horizontalSpan = 2;

    progressIndicator = new ProgressIndicator(composite);
    progressIndicator.setLayoutData(gd);

    // label showing current task
    gd = new GridData(GridData.FILL_HORIZONTAL);
    gd.heightHint = convertVerticalDLUsToPixels(LABEL_DLUS);
    gd.horizontalSpan = 2;

    subTaskLabel = new Label(composite, SWT.LEFT | SWT.WRAP);
    subTaskLabel.setLayoutData(gd);
    subTaskLabel.setFont(parent.getFont());

    composite.layout(true);

    return composite;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
  }

  private synchronized void execute(Runnable runnable)
  {
    if (this.getShell() != null)
    {
      display.asyncExec(runnable);
    }
    else
    {
      runnables.add(runnable);
    }
  }

  @Override
  public void beginTask(final String taskName, final int totalWork)
  {
    this.execute(new Runnable()
    {
      @Override
      public void run()
      {
        if (progressIndicator.isDisposed())
        {
          return;
        }

        setTask(taskName, false);

        if (totalWork == UNKNOWN)
        {
          progressIndicator.beginAnimatedTask();
        }
        else
        {
          progressIndicator.beginTask(totalWork);
        }
      }
    });
  }

  @Override
  public void done()
  {
    this.execute(new Runnable()
    {
      @Override
      public void run()
      {

        if (!progressIndicator.isDisposed())
        {
          progressIndicator.sendRemainingWork();
          progressIndicator.done();
        }

        close();
      }
    });
  }

  @Override
  public void internalWorked(final double work)
  {
    this.execute(new Runnable()
    {
      @Override
      public void run()
      {

        if (!progressIndicator.isDisposed())
        {
          progressIndicator.worked(work);
        }
      }
    });
  }

  @Override
  public boolean isCanceled()
  {
    return canceled;
  }

  @Override
  public void setCanceled(boolean canceled)
  {
    this.canceled = canceled;
  }

  @Override
  public void setTaskName(final String name)
  {
    this.execute(new Runnable()
    {
      @Override
      public void run()
      {

        setTask( ( name == null ) ? DEFAULT_TASKNAME : name, false);
      }
    });
  }

  @Override
  public void subTask(String subTask)
  {
    final String text = subTask == null ? "" : subTask;

    this.execute(new Runnable()
    {
      @Override
      public void run()
      {
        if (subTaskLabel.isDisposed())
        {
          return;
        }

        subTaskLabel.setText(shortenText(text, subTaskLabel));
      }
    });
  }

  @Override
  public void worked(int work)
  {
    internalWorked(work);
  }
}
