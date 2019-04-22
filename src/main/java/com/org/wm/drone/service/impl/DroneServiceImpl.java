package com.org.wm.drone.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.org.wm.drone.pojo.DroneData;
import com.org.wm.drone.pojo.OrderDetails;
import com.org.wm.drone.service.DroneSchedulerService;
import com.org.wm.drone.service.DroneService;

public class DroneServiceImpl implements DroneService {

	@Autowired
	private DroneDataLoader droneDataLoader;

	@Autowired
	private DroneSchedulerService droneSchedulerService;

	private File file;
	private String space = " ";

	/**
	 * This method will load the data file and schedule based on their order time
	 * and to maximize the NPS for the same.
	 */
	@Override
	public String processStart(File file) throws Exception {
		String outputFileName = null;
		try {
			this.file = file;
			droneDataLoader.loadData(file);
			droneSchedulerService.scheduleDelivery();
			outputFileName = writeToFile(setStartTimeForDelivery());
			writeExplanation();
		} catch (Exception e) {
			throw e;
		}
		return outputFileName;
	}

	/**
	 * It will print all fields value for all orders to csv file and that data can
	 * be helpful to understand the schedule
	 * 
	 * @throws IOException
	 */
	private void writeExplanation() throws IOException {
		String header = "OrderNumber, Location, OrderTime, timeRemainsForNPS, timeToDeliver, isNPSPossible, travelTime, startTimeOfDelivery, isNPSAchieved\n";
		File outputFile = new File(
				file.getParent() + "/" + file.getName() + "_result_explanation" + (new Date()).getTime() + ".csv");
		StringBuffer sb = new StringBuffer();
		sb.append(header);
		// DroneData.getOrderList().stream().forEach(s -> sb.append(s.toString() +
		// "\n"));
		for (OrderDetails oDetails : DroneData.getOrderList()) {
			sb.append(oDetails.toString() + "\n");
		}
		FileUtils.writeByteArrayToFile(outputFile, sb.toString().getBytes());
		outputFile = null;
	}

	/**
	 * This method will print results to a file.
	 * 
	 * @param data
	 * @throws IOException
	 */
	private String writeToFile(String data) throws IOException {
		File outputFile = new File(
				file.getParent() + "/" + file.getName() + "_result" + (new Date()).getTime() + ".txt");
		FileUtils.writeByteArrayToFile(outputFile, data.getBytes());
		// System.out.println("Output::\n" + data);
		String fileName = outputFile.getAbsolutePath();
		outputFile = null;
		return fileName;
	}

	/**
	 * This method will set start time for each order based on time constraints.
	 * 
	 * @return
	 */
	private String setStartTimeForDelivery() {
		StringBuffer data = new StringBuffer();
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, 1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("kk:mm:ss");
		dateFormat.setCalendar(now);
		Calendar timeLimit = Calendar.getInstance();
		timeLimit.set(Calendar.HOUR_OF_DAY, 22);
		timeLimit.set(Calendar.MINUTE, 0);
		timeLimit.set(Calendar.SECOND, 0);
		double promoters = 0;
		double detractors = 0;
		int passive = 0;

		String time = null;
		for (OrderDetails oDetails : DroneData.getOrderList()) {
			if (now.get(Calendar.HOUR_OF_DAY) >= 22 || now.get(Calendar.HOUR_OF_DAY) < 6) {
				now.add(Calendar.DAY_OF_MONTH, 1);
				now.set(Calendar.HOUR_OF_DAY, 6);
				now.set(Calendar.MINUTE, 0);
				now.set(Calendar.SECOND, 0);
			} else {
				long timeIndicator = timeLimit.getTimeInMillis() - now.getTimeInMillis();
				if (timeIndicator > 0) {
					timeIndicator = TimeUnit.MILLISECONDS.toMinutes(timeIndicator);
					if (timeIndicator < oDetails.getTravelTime()) {
						now.add(Calendar.DAY_OF_MONTH, 1);
						now.set(Calendar.HOUR_OF_DAY, 6);
						now.set(Calendar.MINUTE, 0);
						now.set(Calendar.SECOND, 0);
					}
				}

			}
			long checkIfNPSAchieved = now.getTimeInMillis() - oDetails.getOrderTimeInMilliSeconds();
			checkIfNPSAchieved = TimeUnit.MILLISECONDS.toMinutes(checkIfNPSAchieved);
			if (checkIfNPSAchieved >= 0 && checkIfNPSAchieved <= 120) {
				oDetails.setIsNPSAchieved(1);
				promoters++;
			} else if (checkIfNPSAchieved >= 0 && checkIfNPSAchieved <= 240) {
				oDetails.setIsNPSAchieved(0);
				passive++;
			} else {
				oDetails.setIsNPSAchieved(-1);
				detractors++;
			}

			time = dateFormat.format(now.getTime());
			oDetails.setStartTimeOfDelivery(time);
			data.append(oDetails.getOrderNumber() + space + oDetails.getStartTimeOfDelivery() + "\n");
			now.add(Calendar.MINUTE, oDetails.getTravelTime());
		}
		double totalOrder = promoters + detractors + passive;
		double promotersPercentage = (promoters / totalOrder) * 100;
		double detractorsPercentage = (detractors / totalOrder) * 100;
		double nps = promotersPercentage - detractorsPercentage;
		data.append("NPS = " + nps);
		return data.toString();
	}

}
