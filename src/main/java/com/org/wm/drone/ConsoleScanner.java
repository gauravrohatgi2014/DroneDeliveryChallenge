package com.org.wm.drone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console Scanner Class 
 * @author GauravRohatgi
 *
 */
public class ConsoleScanner {
	
	List<String> scannerPrint = new ArrayList<String>();

	 public ConsoleScanner() {
		 scannerPrint.add("File Path:");
		 scannerPrint.add("Wrong File Path provided, Please provide correct File Path:");
	 }
	
	@SuppressWarnings("resource")
	public String getFilePath() {
		String filePath=null;
		Scanner scan = new Scanner(System.in);
		System.out.print(scannerPrint.get(0));
		while(true) {
			filePath = scan.nextLine();
			File file = new File(filePath);
			if(file.isDirectory()) {
				//Good Start
				break;
			} else {
				System.out.print(scannerPrint.get(1));
			}
		}
		return filePath;
	}
}
