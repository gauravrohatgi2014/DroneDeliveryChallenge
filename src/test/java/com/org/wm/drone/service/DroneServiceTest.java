/**
 * 
 */
package com.org.wm.drone.service;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.org.wm.drone.ApplicationConfiguration;

/**
 * @author GaRohatg
 *
 */
public class DroneServiceTest {

	private File file = null;

	@SuppressWarnings("resource")
	@Test
	public void test() {
		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
		DroneService droneService = (DroneService) context.getBean("droneService");

		file = new File("testdata/wm_data_test.txt");
		try {
			CleanUp.cleanUpTestFolder();
			String outputFile = droneService.processStart(new File(file.getAbsolutePath()));
			FileReader reader = new FileReader(new File(outputFile));
			List<String> actualResults = IOUtils.readLines(reader);
			FileReader readerExpected = new FileReader(new File("testdata/expected_results.txt"));
			List<String> expectedResults = IOUtils.readLines(readerExpected);
			assertTrue(actualResults.size() == expectedResults.size());
			// assertTrue(actualResults.get(5).equals(expectedResults.get(5)));
			file = null;
			readerExpected.close();
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
