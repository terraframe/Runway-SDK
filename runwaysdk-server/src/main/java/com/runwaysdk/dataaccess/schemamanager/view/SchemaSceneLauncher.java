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
package com.runwaysdk.dataaccess.schemamanager.view;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.netbeans.api.visual.widget.Scene;
import org.openide.util.RequestProcessor;

public class SchemaSceneLauncher
{
     public static void show (final Scene scene) {
        if (SwingUtilities.isEventDispatchThread ())
            showEDT (scene);
        else
            SwingUtilities.invokeLater (new Runnable() {
                public void run () {
                    showEDT (scene);
                }
            });
    }

    private static void showEDT (Scene scene) {
        JComponent sceneView = scene.getView ();
        if (sceneView == null)
            sceneView = scene.createView ();
        show (sceneView);
    }

    public static void show (final JComponent sceneView) {
        if (SwingUtilities.isEventDispatchThread ())
            showEDT (sceneView);
        else
            SwingUtilities.invokeLater (new Runnable() {
                public void run () {
                    showEDT (sceneView);
                }
            });
    }

    private static void showEDT (JComponent sceneView) {
        JScrollPane panel = new JScrollPane (sceneView);
        panel.getHorizontalScrollBar ().setUnitIncrement (32);
        panel.getHorizontalScrollBar ().setBlockIncrement (256);
        panel.getVerticalScrollBar ().setUnitIncrement (32);
        panel.getVerticalScrollBar ().setBlockIncrement (256);
        showCoreEDT (panel);
    }

    public static void showCore (final JComponent view) {
        if (SwingUtilities.isEventDispatchThread ())
            showCoreEDT (view);
        else
            SwingUtilities.invokeLater (new Runnable() {
                public void run () {
                    showCoreEDT (view);
                }
            });
    }

    private static void showCoreEDT (JComponent view) {
        int width=800,height=600;
        JFrame frame = new JFrame ();//new JDialog (), true);
        frame.add (view, BorderLayout.CENTER);
        frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        frame.setBounds((screenSize.width-width)/2, (screenSize.height-height)/2, width, height);
        frame.setVisible (true);
    }

    public static void invokeLater (final Runnable runnable, int delay) {
        RequestProcessor.getDefault ().post (new Runnable() {
            public void run () {
                SwingUtilities.invokeLater (runnable);
            }
        }, delay);
    }

    public static void sleep (int delay) {
        try {
            Thread.sleep (delay);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }
    }
