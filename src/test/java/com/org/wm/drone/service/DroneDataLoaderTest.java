package com.org.wm.drone.service;

import static org.junit.Assert.assertFalse;

import java.io.File;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.org.wm.drone.ApplicationConfiguration;
import com.org.wm.drone.pojo.DroneData;
import com.org.wm.drone.service.impl.DroneDataLoader;

public class DroneDataLoaderTest {

	private File file = null;

	@SuppressWarnings("resource")
	@Test
	public void test() {

		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		DroneDataLoader droneDataLoader = (DroneDataLoader) context.getBean("droneDataLoader");
		file = new File("testdata/wm_data_test.txt");
		try {
			CleanUp.cleanUpTestFolder();
			droneDataLoader.loadData(new File(file.getAbsolutePath()));
			assertFalse(DroneData.getOrderList().isEmpty());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
