/**
 * @(#)SystemTrayAction.java Oct 22, 2010
 * Copyright 2010 BarryWei. All rights reserved.
 */
package barrywei.igosyncdocsv2.action;

import java.awt.AWTException;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import barrywei.igosyncdocsv2.gui.MainFrame;
import barrywei.igosyncdocsv2.gui.util.FaceUtils;
import barrywei.igosyncdocsv2.resource.ImageResource;
import barrywei.igosyncdocsv2.resource.LanguageResource;

/**
 * 
 *
 *
 * @author BarryWei
 * @version 1.0, Oct 22, 2010
 * @since JDK1.6
 */
public class SystemTrayAction extends WindowAdapter {

	private MainFrame frMain;
	
	public SystemTrayAction(MainFrame frMain) {
		this.frMain = frMain;
	}
	
	public void windowIconified(WindowEvent e) {
		if (SystemTray.isSupported()) {
			createTray();
		}
	}
	
	private void createTray() {
		
		final SystemTray tray = SystemTray.getSystemTray();
		// create popupmenu
		final PopupMenu pm = new PopupMenu();
		final MenuItem miExit = new MenuItem("Close iGoSyncDocs");
		final MenuItem miShow = new MenuItem("Restore iGoSyncDocs");
		miShow.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
		final MenuItem miRefresh = new MenuItem("Refresh All");
		final MenuItem miNewDoc = new MenuItem("Create New Document");
		final MenuItem miNewPre = new MenuItem("Create New Presentation");
		final MenuItem miNewSpr = new MenuItem("Create New Spreadsheet");	
		
		pm.add(miShow);
		pm.addSeparator();
		pm.add(miNewDoc);
		pm.add(miNewPre);
		pm.add(miNewSpr);
		pm.add(miRefresh);
		pm.addSeparator();
		pm.add(miExit);
		
		// create image
		final Image trayImage = ImageResource.getImage("stared.png");	
		

		// create tool tips
		String tooltip = new String(LanguageResource.getStringValue("app.tooltip"));

		// create tray icon
		final TrayIcon trayIcon = new TrayIcon(trayImage, tooltip, pm);

		try {		
			frMain.setVisible(false);
			tray.add(trayIcon);
		} catch (AWTException e) {
			String message = LanguageResource.getStringValue("frMain");
			FaceUtils.showErrorMessage(frMain, message.replace("{1}", e.getMessage()));
		}
		
		// listen for user to double-click on tray icon
		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frMain.setVisible(true);
				tray.remove(trayIcon);
			}
		});
		
		// exit action
		miExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result = FaceUtils.showConfirmMessage(frMain, LanguageResource.getStringValue("app.confirm_exit"));
				if (result == JOptionPane.YES_OPTION) {
					frMain.dispose();
					System.exit(1);
				}
			}
		});	
		
		miShow.addActionListener(trayIcon.getActionListeners()[0]);
	}// end of method	
}
