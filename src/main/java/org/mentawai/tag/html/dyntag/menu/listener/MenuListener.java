package org.mentawai.tag.html.dyntag.menu.listener;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.mentawai.core.Controller;
import org.mentawai.tag.html.dyntag.BaseListener;
import org.mentawai.tag.html.dyntag.BaseTag;
import org.mentawai.tag.html.dyntag.FileTransfer;

/**
 * 
 * @author Robert Willain
 */

public class MenuListener extends BaseListener {

	public static List<String> DIRS_FOLDER_NAME = new ArrayList<String>();

	private ArrayList<FileTransfer> LIST_PATH_FILES = new ArrayList<FileTransfer>();

	private ArrayList<FileTransfer> filesSwap = null;

	public static String DEFAULT_SKIN = "simple1";

	public static Map<String, ArrayList<FileTransfer>> SKINS = new HashMap<String, ArrayList<FileTransfer>>();

	public static String loadedSkin = null;

	private String skinName;

	private final String JS = "js";

	private final String GIF = "gif";

	private final String CSS = "css";

	@Override
	public void init() {
		DIRS_FOLDER_NAME = Arrays.asList(new String[] { BaseTag.BASE_DIR + "menuTag/" });

		// Skin slim;
		setSkinName("slim");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "h_arrow.gif");
		sendFile(GIF, "h_arrow_over.gif");
		sendFile(GIF, "v_arrow.gif");
		sendFile(GIF, "v_arrow_over.gif");
		SKINS.put(skinName, filesSwap);
		// Skin slim

		// Mac
		setSkinName("mac");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "osx_item_bg.gif");
		sendFile(GIF, "osx_item_over_bg.gif");
		sendFile(GIF, "v_osx_arrow_over.gif");
		sendFile(GIF, "v_osx_arrow.gif");
		SKINS.put(skinName, filesSwap);
		// Mac

		// Skin WinXp
		setSkinName("winxp");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "h_arrow_black.gif");
		sendFile(GIF, "h_arrow_white.gif");
		sendFile(GIF, "v_arrow_black.gif");
		sendFile(GIF, "v_arrow_white.gif");
		sendFile(GIF, "winxp_menu_bg.gif");
		SKINS.put(skinName, filesSwap);
		// Skin WinXp

		// Gnome
		setSkinName("gnome");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "bluecurve_item_over_bg.gif");
		sendFile(GIF, "h_bluecurve_arrow.gif");
		sendFile(GIF, "h_bluecurve_arrow_over.gif");
		sendFile(GIF, "v_bluecurve_arrow.gif");
		sendFile(GIF, "v_bluecurve_arrow_over.gif");
		SKINS.put(skinName, filesSwap);
		// Gnome

		// Kde
		setSkinName("kde");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "h_keramik_arrow.gif");
		sendFile(GIF, "keramik_item_over_bg.gif");
		sendFile(GIF, "v_keramik_arrow.gif");
		SKINS.put(skinName, filesSwap);
		// Kde

		// Simple1
		setSkinName("simple1");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "h_arrow_black.gif");
		sendFile(GIF, "h_arrow_white.gif");
		sendFile(GIF, "v_arrow_black.gif");
		sendFile(GIF, "v_arrow_white.gif");
		SKINS.put(skinName, filesSwap);
		// Simple1

		// Simple2
		setSkinName("simple2");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "h_arrow_black.gif");
		sendFile(GIF, "v_arrow_black.gif");
		SKINS.put(skinName, filesSwap);
		// Simple2

		// Blue
		setSkinName("blue");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "h_arrow_dark_blue.gif");
		sendFile(GIF, "v_arrow_dark_blue.gif");
		SKINS.put(skinName, filesSwap);
		// Blue

		// Web
		setSkinName("web");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "h_web2.0_arrow.gif");
		sendFile(GIF, "v_web2.0_arrow.gif");
		SKINS.put(skinName, filesSwap);
		// Web

		// Windows Classic
		setSkinName("winclassic");
		sendFile(CSS, "mtwMenu.css");
		sendFile(JS, "c_config.js");
		sendFile(GIF, "h_arrow_black.gif");
		sendFile(GIF, "v_arrow_black.gif");
		sendFile(GIF, "v_arrow_white.gif");
		SKINS.put(skinName, filesSwap);
		// Windows Classic

	}

	private void sendFile(String type, String nameFile) {
		if (!PREVENT_COPY) {
			filesSwap.add(new FileTransfer(type, nameFile, "/org/mentawai/tag/html/dyntag/menu/lib/" + skinName + "/", BaseTag.BASE_DIR + "menuTag/" + skinName + "/"));
		}
	}

	private void setSkinName(String skinName) {
		this.skinName = skinName;
		filesSwap = new ArrayList<FileTransfer>();
	}

	public void loadSkin(String skinName) {
		init();
		if (!SKINS.containsKey(skinName)) {
			skinName = DEFAULT_SKIN;
		}
		loadedSkin = skinName;
		LIST_PATH_FILES = SKINS.get(skinName);
		
		if (!PREVENT_COPY) {
			LIST_PATH_FILES.add(new FileTransfer(JS, "c_smartmenus.js", "/org/mentawai/tag/html/dyntag/menu/lib/", BaseTag.BASE_DIR + "menuTag/"));
			LIST_PATH_FILES.add(new FileTransfer(JS, "c_addon_fx_slide.js", "/org/mentawai/tag/html/dyntag/menu/lib/", BaseTag.BASE_DIR + "menuTag/"));
		}
		contextInitialized(Controller.getApplication());
	}

	@Override
	public void contextInitialized(ServletContextEvent evt) {
		contextInitialized(evt.getServletContext());
	}

	public void contextInitialized(ServletContext servletContext) {

		createComponentDir(servletContext, LIST_PATH_FILES);

		for (int j = 0; j < LIST_PATH_FILES.size(); j++) {
			FileTransfer fileTransfer = LIST_PATH_FILES.get(j);
			URL fileOrigin = getClass().getResource(fileTransfer.getFileOrigin() + fileTransfer.getFileName());
			String fileDestination = servletContext.getRealPath(fileTransfer.getFileDestination() + fileTransfer.getFileName());
			boolean exists = (new File(fileDestination)).exists();
			if (!exists) {
				writeFile(fileOrigin, fileDestination, servletContext);
			}
		}

	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}
}
