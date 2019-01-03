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
package com.runwaysdk.manager.general;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;

import com.runwaysdk.dataaccess.MdEntityDAOIF;
import com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdown;
import com.runwaysdk.dataaccess.transaction.IPropertyListener;
import com.runwaysdk.dataaccess.transaction.ITaskListener;
import com.runwaysdk.manager.controller.AdminController;
import com.runwaysdk.manager.controller.ConfigurationAdapter;
import com.runwaysdk.manager.controller.IConfiguration;
import com.runwaysdk.manager.controller.IModule;
import com.runwaysdk.manager.controller.IPauseListener;
import com.runwaysdk.manager.controller.IViewStrategy;
import com.runwaysdk.manager.controller.ViewConfig;
import com.runwaysdk.manager.model.IComponentObject;
import com.runwaysdk.manager.model.object.IEntityObject;
import com.runwaysdk.manager.view.ClassListView;
import com.runwaysdk.manager.view.DetailView;
import com.runwaysdk.manager.view.EditView;
import com.runwaysdk.manager.view.ExportDialog;
import com.runwaysdk.manager.view.IAdminModule;
import com.runwaysdk.manager.view.IViewPart;
import com.runwaysdk.manager.view.ImportDialog;
import com.runwaysdk.manager.view.SearchView;
import com.runwaysdk.manager.view.TransactionResultView;
import com.runwaysdk.manager.widgets.PausableTaskDialog;
import com.runwaysdk.manager.widgets.TabManager;
import com.runwaysdk.session.Request;

public class MainWindow extends ApplicationWindow implements IWindow, IAdminModule
{
  private static final Log   log         = LogFactory.getLog(MainWindow.class);

  public static final String EDIT_PREFIX = "Edit ";

  class TransactionRecordAction extends Action
  {
    public TransactionRecordAction()
    {
      this.setText(Localizer.getMessage("VIEW_TRANSACTION_RECORDS"));
      this.setAccelerator(SWT.CONTROL | SWT.SHIFT | 'v');
    }

    @Override
    public void run()
    {
      try
      {
        for (IModule module : modules)
        {
          module.validateAction("VIEW_TRANSACTIONS");
        }

        manager.openTab(new IViewStrategy()
        {
          public String getTitle()
          {
            return Localizer.getMessage("TRANSACTION_RECORDS");
          }

          public String getKey()
          {
            return "TRANSACTION_RECORDS";
          }

          public IViewPart getContent()
          {
            return new TransactionResultView(manager, controller);
          }

          @Override
          public boolean isClosable()
          {
            return true;
          }
        });
      }
      catch (Throwable t)
      {
        MainWindow.this.getConfiguration().handleError(t);
      }
    }
  }

  class TransactionImportAction extends Action
  {
    public TransactionImportAction()
    {
      this.setText(Localizer.getMessage("IMPORT_TRANSACTION"));
      this.setAccelerator(SWT.CONTROL | SWT.SHIFT | 'm');
    }

    @Override
    public void run()
    {
      try
      {
        for (IModule module : modules)
        {
          module.validateAction("IMPORT");
        }

        new ImportDialog(getShell(), controller).open();
      }
      catch (Throwable t)
      {
        MainWindow.this.getConfiguration().handleError(t);
      }
    }
  }

  class TransactionExportAction extends Action
  {
    public TransactionExportAction()
    {
      this.setText(Localizer.getMessage("EXPORT_TRANSACTION"));
      this.setAccelerator(SWT.CONTROL | SWT.SHIFT | 'e');
    }

    @Override
    public void run()
    {
      try
      {
        for (IModule module : modules)
        {
          module.validateAction("EXPORT");
        }

        ExportDialog dialog = new ExportDialog(getShell(), controller);
        dialog.open();
      }
      catch (Throwable t)
      {
        MainWindow.this.getConfiguration().handleError(t);
      }
    }
  }

  class ExitAction extends Action
  {
    private ApplicationWindow window;

    public ExitAction(ApplicationWindow window)
    {
      this.window = window;

      this.setText(Localizer.getMessage("EXIT_MENU_ITEM"));
    }

    @Override
    public void run()
    {
      try
      {
        for (IModule module : modules)
        {
          module.validateAction("EXIT");
        }

        if (MainWindow.this.canHandleShellCloseEvent())
        {
          window.close();
        }
      }
      catch (Throwable t)
      {
        MainWindow.this.getConfiguration().handleError(t);
      }

    }
  }

  class ImportListener implements ITaskListener
  {
    @Override
    public void taskStart(String name, int amount)
    {
      // Do nothing
    }

    @Override
    public void taskProgress(int percent)
    {
      // Do nothing
    }

    @Override
    public void done(boolean success)
    {
      syncExec(new Runnable()
      {
        @Override
        public void run()
        {
          transactionMenu.removeAll();
          transactionMenu.add(new TransactionRecordAction());
          transactionMenu.add(new TransactionExportAction());
          transactionMenu.add(new TransactionImportAction());
        }
      });

      if (success)
      {
        message(Localizer.getMessage("IMPORT_FINISHED"));
      }

      listeners.clear();
    }

    @Override
    public void start()
    {
      syncExec(new Runnable()
      {
        @Override
        public void run()
        {
          transactionMenu.removeAll();
          transactionMenu.add(new TransactionRecordAction());
          transactionMenu.add(new TransactionExportAction());
        }
      });
    }
  }

  private TabManager           manager;

  private AdminController      controller;

  private List<IPauseListener> listeners;

  private IModule[]            modules;

  private MenuManager          transactionMenu;

  private String               shellText;

  private IConfiguration       configuration;

  public MainWindow(IModule... modules)
  {
    this(new ConfigurationAdapter(), modules);
  }

  public MainWindow(IConfiguration configuration, IModule... modules)
  {
    super(null);

    this.configuration = configuration;
    this.configuration.register(this);

    this.shellText = configuration.getShellText();
    this.modules = modules;
    this.controller = new AdminController(configuration, this);
    this.listeners = new LinkedList<IPauseListener>();

    ExportDialog.setExceptionHandler(new IExceptionHandler()
    {
      @Override
      public void handleException(Throwable t)
      {
        MainWindow.this.getConfiguration().handleError(t);
      }
    });
  }

  public IConfiguration getConfiguration()
  {
    return configuration;
  }

  @Override
  protected Control createContents(final Composite parent)
  {
    Display display = parent.getDisplay();
    Monitor monitor = display.getPrimaryMonitor();

    parent.getShell().setSize(900, 900);
    parent.getShell().setText(shellText);
    parent.getShell().setImage(ImageDescriptor.createFromURL(Object.class.getResource("/icons/Transfer.png")).createImage());

    Rectangle windowRect = parent.getShell().getBounds();
    Rectangle monitorRect = monitor.getBounds();

    int x = ( monitorRect.width - windowRect.width ) / 2;
    int y = ( monitorRect.height - windowRect.height ) / 2;

    parent.getShell().setLocation(x, y);

    Splash splash = this.createSplash(monitor);
    splash.open();

    try
    {
      display.syncExec(new Runnable()
      {
        @Override
        public void run()
        {
          SashForm form = new SashForm(parent, SWT.HORIZONTAL | SWT.FILL);

          ClassListView classList = new ClassListView(MainWindow.this);
          classList.createPartControl(form);
          classList.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));

          CTabFolder tabFolder = new CTabFolder(form, SWT.NONE);
          tabFolder.setUnselectedCloseVisible(false);
          tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1));
          tabFolder.setLayout(new FillLayout());
          tabFolder.setSimple(false);

          manager = new TabManager(tabFolder);

          form.setWeights(new int[] { 1, 3 });

          for (IModule module : modules)
          {
            module.init(MainWindow.this);
          }
        }
      });
    }
    catch (Exception e)
    {
      splash.close();

      MainWindow.this.getConfiguration().handleError(e);

      try
      {
        CacheShutdown.shutdown();
      }
      catch (Exception exception)
      {
        // Do nothing
      }

      /*
       * An error has occured while trying to build the contents of the window.
       * As a result the window will never appear on screen, however the process
       * will continue to run silently. Therefore, the only solution is to have
       * the system exit when an error occurs.
       */
      System.exit(-1);
    }
    finally
    {
      if (!splash.isDisposed())
      {
        splash.close();
      }
    }

    return parent;
  }

  private Splash createSplash(Monitor monitor)
  {
    return new Splash(monitor);
  }

  @Override
  protected MenuManager createMenuManager()
  {
    IMenuManager manager = new MainMenuManager(new MenuManager());

    MenuManager fileMenu = manager.getMenu(Localizer.getMessage("FILE_MENU"));

    manager.addMenu(fileMenu);

    transactionMenu = manager.getMenu(Localizer.getMessage("TRANSACTION_MENU"));
    transactionMenu.add(new TransactionRecordAction());
    transactionMenu.add(new TransactionExportAction());
    transactionMenu.add(new TransactionImportAction());
    // transactionMenu.add(new ResumeImportAction());

    manager.addMenu(transactionMenu);

    for (IModule module : modules)
    {
      module.generateMenu(manager);
    }

    MenuManager helpMenu = new MenuManager(Localizer.getMessage("HELP_MENU"));

    manager.addMenu(helpMenu);

    // The exit action must be the last thing in the file menu
    fileMenu.add(new ExitAction(this));

    return manager.getMenuBar();
  }

  @Override
  protected StatusLineManager createStatusLineManager()
  {
    for (IModule module : modules)
    {
      StatusLineManager manager = module.createStatusLineManager();

      if (manager != null)
      {
        return manager;
      }
    }

    return super.createStatusLineManager();
  }

  public void run()
  {
    // Don't return from open() until window closes
    this.setBlockOnOpen(true);

    this.addMenuBar();

    for (IModule module : modules)
    {
      if (module.hasStatusLineManager())
      {
        this.addStatusLine();
      }
    }

    // Open the main window
    this.open();

    // Dispose the display
    Display.getCurrent().dispose();
  }

  @Override
  public void closeTab(String key)
  {
    manager.closeTab(key);
  }

  @Override
  public Display getDisplay()
  {
    return manager.getDisplay();
  }

  @Override
  public void show(IViewStrategy strategy)
  {
    manager.openTab(strategy);
  }

  public IAdminModule getModule()
  {
    return this;
  }

  private void error(String message)
  {
    MessageDialog.openError(this.getShell(), Localizer.getMessage("ERROR_TITLE"), message);
  }

  @Override
  public void error(final Throwable throwable)
  {
    this.syncExec(new Runnable()
    {
      @Override
      public void run()
      {
        log.error(this, throwable);

        error(throwable.getLocalizedMessage());
      }
    });
  }

  @Override
  public void message(final String message)
  {
    this.syncExec(new Runnable()
    {
      @Override
      public void run()
      {
        MessageDialog.openInformation(getShell(), Localizer.getMessage("MESSAGE_TITLE"), message);
      }
    });
  }

  @Override
  public void syncExec(Runnable runnable)
  {
    this.getShell().getDisplay().syncExec(runnable);
  }

  @Override
  public void run(IRunnableWithProgress worker)
  {
    try
    {
      new ProgressMonitorDialog(this.getShell()).run(true, false, worker);
    }
    catch (InvocationTargetException e)
    {
      MainWindow.this.getConfiguration().handleError(e);
    }
    catch (Exception e)
    {
      MainWindow.this.getConfiguration().handleError(e);
    }
  }

  @Override
  public void pause(String taskName)
  {
    for (IPauseListener listener : listeners)
    {
      listener.pause();
    }
  }

  @Override
  public void resume(String taskName)
  {
    for (IPauseListener listener : listeners)
    {
      listener.resume();
    }
  }

  @Override
  public void edit(final IEntityObject entity, final ViewConfig config)
  {
    final MdEntityDAOIF mdEntity = entity.getMdClassDAO();

    this.show(new IViewStrategy()
    {

      public IViewPart getContent()
      {
        return new EditView(entity, getModule(), controller, config);
      }

      public String getTitle()
      {
        return Localizer.getMessage("EDIT_COMPONENT") + " " + mdEntity.getDisplayLabel(Localizer.getLocale());
      }

      public String getKey()
      {
        return TabManager.getEditKey(entity);
      }

      @Override
      public boolean isClosable()
      {
        return config.isClosable();
      }
    });
  }

  @Override
  public void search(final IComponentObject entity, final boolean closable)
  {
    final MdEntityDAOIF mdEntity = entity.getMdClassDAO();

    this.show(new IViewStrategy()
    {
      public IViewPart getContent()
      {
        return new SearchView(entity, getModule());
      }

      public String getTitle()
      {
        return Localizer.getMessage("SEARCH") + " " + mdEntity.getDisplayLabel(Localizer.getLocale());
      }

      public String getKey()
      {
        return "Search " + entity.getOid();
      }

      @Override
      public boolean isClosable()
      {
        return closable;
      }
    });
  }

  @Override
  public void view(final IComponentObject entity, final boolean closable)
  {
    final MdEntityDAOIF mdEntity = entity.getMdClassDAO();

    this.show(new IViewStrategy()
    {

      public IViewPart getContent()
      {
        return new DetailView(entity);
      }

      public String getTitle()
      {
        return Localizer.getMessage("VIEW_COMPONENT") + " " + mdEntity.getDisplayLabel(Localizer.getLocale());
      }

      public String getKey()
      {
        return "View " + entity.getOid();
      }

      @Override
      public boolean isClosable()
      {
        return closable;
      }
    });
  }

  @Override
  public List<ITaskListener> getImportListeners()
  {
    List<ITaskListener> taskListeners = new LinkedList<ITaskListener>();

    PausableTaskDialog dialog = new PausableTaskDialog(getShell());

    listeners.add(dialog);
    taskListeners.add(dialog);

    for (IModule module : modules)
    {
      ITaskListener listener = module.getImportListener();

      if (listener != null)
      {
        taskListeners.add(listener);
      }
    }

    taskListeners.add(new ImportListener());

    return taskListeners;
  }

  @Override
  public List<ITaskListener> getExportListeners()
  {
    List<ITaskListener> taskListeners = new LinkedList<ITaskListener>();

    for (IModule module : modules)
    {
      ITaskListener listener = module.getExportListener();

      if (listener != null)
      {
        taskListeners.add(listener);
      }
    }

    return taskListeners;
  }

  @Override
  public HashMap<String, String> getExportProperties()
  {
    HashMap<String, String> properties = new HashMap<String, String>();

    for (IModule module : modules)
    {
      properties.putAll(module.getExportProperties());
    }

    return properties;
  }

  @Override
  public List<IPropertyListener> getImportPropertyListeners()
  {
    List<IPropertyListener> listeners = new LinkedList<IPropertyListener>();

    for (IModule module : modules)
    {
      listeners.addAll(module.getImportPropertyListeners());
    }

    return listeners;
  }

  @Override
  public boolean close()
  {

    for (IModule module : modules)
    {
      module.shellCloseEvent();
    }

    try
    {
      new ProgressMonitorDialog(getShell()).run(true, false, new IRunnableWithProgress()
      {
        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          monitor.beginTask(Localizer.getMessage("MANAGER_SHUTDOWN"), IProgressMonitor.UNKNOWN);

          shutdown();

          monitor.done();
        }
      });
    }
    catch (Exception e)
    {
      // An exception occured while showing a dialog about the progress of
      // shutting down the cache. Ensure that the cache gets shutdown without
      // the dialog.
      this.error(e);

      return false;
    }

    return super.close();
  }

  @Request
  private void shutdown()
  {
    CacheShutdown.shutdown();
  }

  @Override
  protected boolean canHandleShellCloseEvent()
  {
    if (this.listeners.size() > 0)
    {
      String title = Localizer.getMessage("CONFIRM");
      String msg = Localizer.getMessage("IMPORT_IN_PROGRESS");

      boolean response = MessageDialog.openQuestion(getShell(), title, msg);

      if (!response)
      {
        return false;
      }
    }

    return super.canHandleShellCloseEvent();
  }

  public static void main(String[] args)
  {
    final Display display = Display.getDefault();

    Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable()
    {
      public void run()
      {
        MainWindow window = new MainWindow();
        window.run();
      }
    });
  }
}
