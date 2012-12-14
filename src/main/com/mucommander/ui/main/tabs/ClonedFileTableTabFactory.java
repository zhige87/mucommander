/*
 * This file is part of muCommander, http://www.mucommander.com
 * Copyright (C) 2002-2012 Maxence Bernard
 *
 * muCommander is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * muCommander is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mucommander.ui.main.tabs;

import com.mucommander.commons.file.AbstractFile;
import com.mucommander.core.LocalLocationHistory;
import com.mucommander.ui.main.FolderPanel;
import com.mucommander.ui.tabs.TabFactory;

/**
 * Factory for creating {@link com.mucommander.ui.main.tabs.FileTableTab} which is a clone of a given {@link com.mucommander.ui.main.tabs.FileTableTab}
 * 
 * @author Arik Hadas
 */
public class ClonedFileTableTabFactory implements TabFactory<FileTableTab, FileTableTab> {

	private FolderPanel folderPanel;
	
	public ClonedFileTableTabFactory(FolderPanel folderPanel) {
		this.folderPanel = folderPanel;
	}
	
	public FileTableTab createTab(FileTableTab tab) {
		if (tab.getLocation() == null)
			throw new RuntimeException("Invalid location");

		return new ClonedFileTableTab(tab, folderPanel);
	}

	class ClonedFileTableTab implements FileTableTab {
		
		/** The location presented in this tab */
		private AbstractFile location;

		/** Flag that indicates whether the tab is locked or not */
		private boolean locked;
		
		/** History of accessed location within the tab */
		private LocalLocationHistory locationHistory;

		/**
		 * Private constructor
		 * 
		 * @param location - the location that would be presented in the tab
		 */
		private ClonedFileTableTab(FileTableTab tab, FolderPanel folderPanel) {
			this.location = tab.getLocation();
			this.locked = tab.isLocked();
			locationHistory = new LocalLocationHistory(folderPanel);
		}
		
		public void setLocation(AbstractFile location) {
			this.location = location;
			
			// add location to the history (See LocalLocationHistory to see how it handles the first location it gets)
			locationHistory.tryToAddToHistory(location.getURL());
		}

		public AbstractFile getLocation() {
			return location;
		}
		
		public void setLocked(boolean locked) {
			this.locked = locked;
		}

		public boolean isLocked() {
			 return locked;
		}

		public LocalLocationHistory getLocationHistory() {
			return locationHistory;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof FileTableTab)
				return location.getAbsolutePath().equals(((FileTableTab) obj).getLocation().getAbsolutePath()) &&
					   locked == ((FileTableTab) obj).isLocked();
			return false;
		}

		@Override
		public int hashCode() {
			return location.hashCode();
		}
	}
}