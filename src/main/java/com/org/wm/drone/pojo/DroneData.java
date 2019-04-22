package com.org.wm.drone.pojo;

import java.util.ArrayList;
import java.util.List;

public class DroneData {

	private static List<OrderDetails> orderList = new ArrayList<OrderDetails>();

	public static List<OrderDetails> getOrderList() {
		return orderList;
	}

	public static void setOrderList(List<OrderDetails> list) {
		orderList = list;
	}

}
