package com.org.wm.drone.service;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.org.wm.drone.ApplicationConfiguration;
import com.org.wm.drone.pojo.DroneData;
import com.org.wm.drone.pojo.OrderDetails;
import com.org.wm.drone.service.impl.DroneDataLoader;

public class DroneSchedulerTest {

	private File file = null;

	@SuppressWarnings("resource")
	@Test
	public void test() {
		List<String> localList = new ArrayList<String>();
		localList.add("WM001");
		localList.add("WM003");
		localList.add("WM002");
		localList.add("WM004");
		localList.add("WM005");

		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		DroneSchedulerService droneSchedulerService = (DroneSchedulerService) context.getBean("droneSchedulerService");
		DroneDataLoader droneDataLoader = (DroneDataLoader) context.getBean("droneDataLoader");
		file = new File("testdata/wm_data_test.txt");
		try {
			CleanUp.cleanUpTestFolder();
			droneDataLoader.loadData(new File(file.getAbsolutePath()));
			droneSchedulerService.scheduleDelivery();
			int i = 0;
			for (OrderDetails oDetails : DroneData.getOrderList()) {
				assertTrue(oDetails.getOrderNumber().equals(localList.get(i++)));
			}
			file = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
