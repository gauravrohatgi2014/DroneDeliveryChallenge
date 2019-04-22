package com.org.wm.drone.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CleanUp {
	
	public static void cleanUpTestFolder() {
		File file = new File("testdata/wm_data_test.txt");
		//File f = new File(file.getAbsolutePath());
		File parent = new File(file.getParent());
		if(parent.isDirectory()) {
			String list[] = parent.list();
			for(int i=0;i<list.length;i++) {
				if(list[i].contains(".txt_")) {
					try {
						FileUtils.forceDelete(new File("testdata/"+list[i]));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/*public static void main(String args[]) {
		cleanUpTestFolder();
	}*/
}
