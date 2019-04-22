package com.org.wm.drone;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.org.wm.drone.service.DroneService;

/**
 * Drone Delivery Class
 * 
 * @author GauravRohatgi
 *
 */
public class DroneDelivery {

	private static DroneService droneService;

	private static String filePathNotProvided = "Please provide file name with complete path. for example /var/xyz/file.txt";
	private static String itsNotFile = "It seems provided file is not actual file";

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println(filePathNotProvided);
			System.exit(0);
		}
		File file = new File(args[0]);
		if (!file.isFile()) {
			System.out.println(itsNotFile);
			System.exit(0);
		}
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		droneService = (DroneService) context.getBean("droneService");
		try {
			droneService.processStart(file);
		} catch (Exception e) {
			System.out.println("---------------------------------------------------------------");
			System.out.println("Error::" + e.getMessage());
			System.out.println("---------------------------------------------------------------");
		}
	}

	public DroneDelivery() {
		// Empty
	}

}
