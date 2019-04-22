package com.org.wm.drone.service.impl;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.org.wm.drone.pojo.DroneData;
import com.org.wm.drone.pojo.OrderDetails;

public class DroneDataLoader {

	public void loadData(File file) throws Exception {
		List<OrderDetails> orderList = new ArrayList<OrderDetails>();

		FileReader reader = new FileReader(file);
		List<String> lines = IOUtils.readLines(reader);
		reader.close();

		for (String record : lines) {
			OrderDetails orderDetails = new OrderDetails(record);
			orderList.add(orderDetails);
		}
		if (!orderList.isEmpty())
			DroneData.setOrderList(orderList);
		else
			throw new Exception("DroneDataLoader:loadData::Order List is empty.");

	}
}
