package com.org.wm.drone.service.impl;

import java.util.Collections;
import java.util.Comparator;

import com.org.wm.drone.pojo.DroneData;
import com.org.wm.drone.pojo.OrderDetails;
import com.org.wm.drone.service.DroneSchedulerService;

public class DroneSchedulerServiceImpl implements DroneSchedulerService {

	@Override
	public void scheduleDelivery() throws Exception {
		// TODO Auto-generated method stub
		if (DroneData.getOrderList() != null && !DroneData.getOrderList().isEmpty())
			Collections.sort(DroneData.getOrderList(), new OrderComparator());
		else
			throw new Exception("DroneSchedularServiceImpl:scheduleDelivery::Order List is null or empty.");
	}

}

class OrderComparator implements Comparator<OrderDetails> {

	/**
	 * Compare two objects for OrderDetails.
	 */
	public int compare(OrderDetails o1, OrderDetails o2) {
		int me = -1;
		int other = 1;
		if (o1.isNPSPossible() && !o2.isNPSPossible())
			return me;
		else if (!o1.isNPSPossible() && o2.isNPSPossible())
			return other;
		else if (!o1.isNPSPossible() && !o2.isNPSPossible()) {
			if (o1.getTravelTime() > o2.getTravelTime())
				return other;
			else
				return me;
		}

		if (o1.getTimeRemainsForNPS() > o2.getTimeRemainsForNPS() && o2.isNPSPossible()
				&& o1.getTravelTime() > (o2.getTimeRemainsForNPS() - o2.getTimeToDeliver()))
			return other;
		else if (o1.getTimeRemainsForNPS() <= o2.getTimeRemainsForNPS() && o1.isNPSPossible()
				&& o2.getTravelTime() > (o1.getTimeRemainsForNPS() - o1.getTimeToDeliver()))
			return me;
		else if (o1.getTimeRemainsForNPS() > o2.getTimeRemainsForNPS() && o2.isNPSPossible()
				&& o1.getTravelTime() < o2.getTravelTime())
			return other;
		else if (o1.getTimeRemainsForNPS() > o2.getTimeRemainsForNPS() && o1.isNPSPossible()
				&& o1.getTravelTime() > o2.getTravelTime())
			return me;
		else if (o1.getTravelTime() > o2.getTravelTime())
			return me;
		else
			return other;
	}
}
