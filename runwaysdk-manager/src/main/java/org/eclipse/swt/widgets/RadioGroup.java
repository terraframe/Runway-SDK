/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;


/**
 * This object decorates a bunch of SWT.RADIO buttons and provides saner
 * selection semantics than you get by default with those radio buttons.
 * <p>
 * Its API is basically the same API as List, but with unnecessary methods
 * removed.
 */
public class RadioGroup extends Composite
{
  private List<RadioButton>       buttons;

  private RadioButton             selectedButton        = null;

  private RadioButton             potentialNewSelection = null;

  private List<SelectionListener> listeners;

  /**
   * Constructs an instance of this widget given an array of RadioButton objects
   * to wrap. The RadioButton objects must have been created with the SWT.RADIO
   * style bit set, and they must all be in the same Composite.
   * 
   * @param radioButtons
   *          Object[] an array of radio buttons to wrap.
   * @param values
   *          Object[] an array of objects corresponding to the value of each
   *          radio button.
   */
  public RadioGroup(Composite parent, int style)
  {
    super(parent, style);

    this.buttons = new Vector<RadioButton>();
    this.listeners = new LinkedList<SelectionListener>();

    new SelectionListener()
    {
      public void widgetDefaultSelected(SelectionEvent e)
      {
        widgetSelected(e);
      }

      public void widgetSelected(SelectionEvent e)
      {
        potentialNewSelection = getButton(e);

        if (!potentialNewSelection.getSelection())
        {
          return;
        }
        if (potentialNewSelection.equals(selectedButton))
        {
          return;
        }
      }

      private RadioButton getButton(SelectionEvent e)
      {
        if (e.data != null)
        {
          return (RadioButton) e.data;
        }

        return (RadioButton) e.widget;
      }
    };
  }
  
  public void addComponentSelectionListener(SelectionListener listener)
  {
    for(RadioButton button : buttons)
    {
      button.addSelectionListener(listener);
    }
  }

  
  public void removeComponentSelectionListener(SelectionListener listener)
  {
    for(RadioButton button : buttons)
    {
      button.removeSelectionListener(listener);
    }
  }
  
  /**
   * Returns the object corresponding to the currently-selected radio button or
   * null if no radio button is selected.
   * 
   * @return the object corresponding to the currently-selected radio button or
   *         null if no radio button is selected.
   */
  public Object getSelection()
  {
    int selectionIndex = getSelectionIndex();

    if (selectionIndex < 0)
    {
      return "";
    }

    return buttons.get(selectionIndex).getValue();
  }

  /**
   * Sets the selected radio button to the radio button whose model object
   * equals() the object specified by newSelection. If !newSelection.equals()
   * any model object managed by this radio group, deselects all radio buttons.
   * 
   * @param newSelection
   *          A model object corresponding to one of the model objects
   *          associated with one of the radio buttons.
   */
  public void setSelection(Object newSelection)
  {
    deselectAll();

    for (int i = 0; i < buttons.size(); i++)
    {
      String value = buttons.get(i).getValue();

      if (value.equals(newSelection))
      {
        setSelection(i);
        return;
      }
    }
  }

  protected void fireWidgetSelectedEvent(SelectionEvent e)
  {
    for (SelectionListener listener : listeners)
    {
      listener.widgetSelected(e);
    }
  }

  protected void fireWidgetDefaultSelectedEvent(SelectionEvent e)
  {
    fireWidgetSelectedEvent(e);
  }

  /**
   * Adds the listener to the collection of listeners who will be notified when
   * the receiver's selection changes, by sending it one of the messages defined
   * in the <code>SelectionListener</code> interface.
   * <p>
   * <code>widgetSelected</code> is called when the selection changes.
   * <code>widgetDefaultSelected</code> is typically called when an item is
   * double-clicked.
   * </p>
   * 
   * @param listener
   *          the listener which should be notified
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * 
   * @see SelectionListener
   * @see #removeSelectionListener
   * @see SelectionEvent
   */
  public void addSelectionListener(SelectionListener listener)
  {
    listeners.add(listener);
  }

  /**
   * Removes the listener from the collection of listeners who will be notified
   * when the receiver's selection changes.
   * 
   * @param listener
   *          the listener which should no longer be notified
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * 
   * @see SelectionListener
   * @see #addSelectionListener
   */
  public void removeSelectionListener(SelectionListener listener)
  {
    listeners.remove(listener);
  }

  /**
   * Deselects the item at the given zero-relative index in the receiver. If the
   * item at the index was already deselected, it remains deselected. Indices
   * that are out of range are ignored.
   * 
   * @param index
   *          the index of the item to deselect
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void deselect(int index)
  {
    if (index < 0 || index >= buttons.size())
      return;
    buttons.get(index).setSelection(false);
  }

  /**
   * Deselects all selected items in the receiver.
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void deselectAll()
  {
    for (int i = 0; i < buttons.size(); i++)
    {
      buttons.get(i).setSelection(false);
    }
  }

  /**
   * Returns the zero-relative index of the item which currently has the focus
   * in the receiver, or -1 if no item has focus.
   * 
   * @return the index of the selected item
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public int getFocusIndex()
  {
    for (int i = 0; i < buttons.size(); i++)
    {
      if (buttons.get(i).isFocusControl())
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns the item at the given, zero-relative index in the receiver. Throws
   * an exception if the index is out of range.
   * 
   * @param index
   *          the index of the item to return
   * @return the item at the given index
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_INVALID_RANGE - if the index is not between 0 and
   *              the number of elements in the list minus 1 (inclusive)</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * 
   *              FIXME: tck - this should be renamed to getItemText()
   */
  public String getItem(int index)
  {
    if (index < 0 || index >= buttons.size())
    {
      SWT.error(SWT.ERROR_INVALID_RANGE, null, "getItem for a nonexistant item");
    }

    return buttons.get(index).getText();
  }

  /**
   * Returns the number of items contained in the receiver.
   * 
   * @return the number of items
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public int getItemCount()
  {
    return buttons.size();
  }

  /**
   * Returns a (possibly empty) array of <code>String</code>s which are the
   * items in the receiver.
   * <p>
   * Note: This is not the actual structure used by the receiver to maintain its
   * list of items, so modifying the array will not affect the receiver.
   * </p>
   * 
   * @return the items in the receiver's list
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public String[] getItems()
  {
    List<String> itemStrings = new ArrayList<String>();

    for (int i = 0; i < buttons.size(); i++)
    {
      itemStrings.add(buttons.get(i).getText());
    }

    return itemStrings.toArray(new String[itemStrings.size()]);
  }

  public RadioButton[] getButtons()
  {
    return buttons.toArray(new RadioButton[buttons.size()]);
  }

  /**
   * Returns the zero-relative index of the item which is currently selected in
   * the receiver, or -1 if no item is selected.
   * 
   * @return the index of the selected item or -1
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public int getSelectionIndex()
  {
    for (int i = 0; i < buttons.size(); i++)
    {
      if (buttons.get(i).getSelection() == true)
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * Gets the index of an item.
   * <p>
   * The list is searched starting at 0 until an item is found that is equal to
   * the search item. If no item is found, -1 is returned. Indexing is zero
   * based.
   * 
   * @param string
   *          the search item
   * @return the index of the item
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed
   *              </li> <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public int indexOf(String string)
  {
    for (int i = 0; i < buttons.size(); i++)
    {
      if (buttons.get(i).getText().equals(string))
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * Searches the receiver's list starting at the given, zero-relative index
   * until an item is found that is equal to the argument, and returns the index
   * of that item. If no item is found or the starting index is out of range,
   * returns -1.
   * 
   * @param string
   *          the search item
   * @param start
   *          the zero-relative index at which to start the search
   * @return the index of the item
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public int indexOf(String string, int start)
  {
    for (int i = start; i < buttons.size(); i++)
    {
      if (buttons.get(i).getText().equals(string))
      {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns <code>true</code> if the item is selected, and <code>false</code>
   * otherwise. Indices out of range are ignored.
   * 
   * @param index
   *          the index of the item
   * @return the visibility state of the item at the index
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public boolean isSelected(int index)
  {
    return buttons.get(index).getSelection();
  }

  /**
   * Selects the item at the given zero-relative index in the receiver's list.
   * If the item at the index was already selected, it remains selected. Indices
   * that are out of range are ignored.
   * 
   * @param index
   *          the index of the item to select
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void select(int index)
  {
    if (index < 0 || index >= buttons.size())
    {
      return;
    }

    buttons.get(index).setSelection(true);
  }

  /**
   * Sets the text of the item in the receiver's list at the given zero-relative
   * index to the string argument. This is equivalent to <code>remove</code>'ing
   * the old item at the index, and then <code>add</code>'ing the new item at
   * that index.
   * 
   * @param index
   *          the index for the item
   * @param string
   *          the new text for the item
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_INVALID_RANGE - if the index is not between 0 and
   *              the number of elements in the list minus 1 (inclusive)</li>
   *              <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   */
  public void setItem(int index, String string)
  {
    if (index < 0 || index >= buttons.size())
    {
      SWT.error(SWT.ERROR_INVALID_RANGE, null, "setItem for a nonexistant item");
    }

    buttons.get(index).setText(string);
  }

  /**
   * Selects the item at the given zero-relative index in the receiver. If the
   * item at the index was already selected, it remains selected. The current
   * selection is first cleared, then the new item is selected. Indices that are
   * out of range are ignored.
   * 
   * @param index
   *          the index of the item to select
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
   *              thread that created the receiver</li>
   *              </ul>
   * @see List#deselectAll()
   * @see List#select(int)
   */
  public void setSelection(int index)
  {
    if (index < 0 || index > buttons.size() - 1)
    {
      return;
    }

    buttons.get(index).setSelection(true);
  }


  public void addButton(RadioButton button)
  {
    buttons.add(button);
  }

}
