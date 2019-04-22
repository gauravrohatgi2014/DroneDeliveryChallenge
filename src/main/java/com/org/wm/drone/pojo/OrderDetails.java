package com.org.wm.drone.pojo;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class OrderDetails {

	private String orderNumber;
	private String location;
	private String orderTime;
	private long orderTimeInMilliSeconds;
	private int timeToDeliver;
	private boolean isNPSPossible = false;
	private long timeRemainsForNPS = 0;
	private int travelTime = 0;
	private String startTimeOfDelivery;
	private int isNPSAchieved;

	public int getIsNPSAchieved() {
		return isNPSAchieved;
	}

	public void setIsNPSAchieved(int isNPSAchieved) {
		this.isNPSAchieved = isNPSAchieved;
	}

	public long getOrderTimeInMilliSeconds() {
		return orderTimeInMilliSeconds;
	}

	public int getTimeToDeliver() {
		return timeToDeliver;
	}

	public void setNPSPossible(boolean isNPSPossible) {
		this.isNPSPossible = isNPSPossible;
	}

	public boolean isNPSPossible() {
		return isNPSPossible;
	}

	public long getTimeRemainsForNPS() {
		return timeRemainsForNPS;
	}

	public int getTravelTime() {
		return travelTime;
	}

	public String getStartTimeOfDelivery() {
		return startTimeOfDelivery;
	}

	public void setStartTimeOfDelivery(String startTimeOfDelivery) {
		this.startTimeOfDelivery = startTimeOfDelivery;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getLocation() {

		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public OrderDetails(String record) {
		String[] strings = record.split(" ");
		if (strings.length != 3) {
			System.out.println("Record:" + record + " - corrupted data");
			return;
		}
		this.orderNumber = strings[0];
		this.location = strings[1];
		this.orderTime = strings[2];
		init();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((orderNumber == null) ? 0 : orderNumber.hashCode());
		result = prime * result + ((orderTime == null) ? 0 : orderTime.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderDetails other = (OrderDetails) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (orderNumber == null) {
			if (other.orderNumber != null)
				return false;
		} else if (!orderNumber.equals(other.orderNumber))
			return false;
		if (orderTime == null) {
			if (other.orderTime != null)
				return false;
		} else if (!orderTime.equals(other.orderTime))
			return false;
		return true;
	}

	private void init() {
		char[] charLocation = location.toCharArray();
		if (charLocation[0] == 'N' || charLocation[0] == 'S') {
			if (charLocation[2] == 'W' || charLocation[2] == 'E') {
				timeToDeliver += Integer.parseInt(String.valueOf(charLocation[1]));
				if (charLocation.length == 4) {
					timeToDeliver += Integer.parseInt(String.valueOf(charLocation[3]));
				} else {
					timeToDeliver += Integer
							.parseInt(String.valueOf(charLocation[3]) + String.valueOf(charLocation[4]));
				}
			} else if (charLocation[3] == 'W' || charLocation[3] == 'E') {
				timeToDeliver += Integer.parseInt(String.valueOf(charLocation[1]) + String.valueOf(charLocation[2]));
				if (charLocation.length == 5) {
					timeToDeliver += Integer.parseInt(String.valueOf(charLocation[4]));
				} else {
					timeToDeliver += Integer
							.parseInt(String.valueOf(charLocation[4]) + String.valueOf(charLocation[5]));
				}
			}
		}
		travelTime = timeToDeliver * 2;
		Calendar now = Calendar.getInstance();

		String[] hms = orderTime.split(":");
		// Date date1 = new Date();
		Calendar calendar = Calendar.getInstance();
		int iDate = calendar.get(Calendar.DAY_OF_MONTH);// date1.getDate();
		calendar.set(Calendar.DAY_OF_MONTH, iDate);
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hms[0]));
		calendar.set(Calendar.MINUTE, Integer.parseInt(hms[1]));
		calendar.set(Calendar.SECOND, Integer.parseInt(hms[2]));
		// orderTimeInMilliSeconds = calendar.getTimeInMillis();
		Calendar timeStart = Calendar.getInstance();

		timeStart.set(Calendar.HOUR_OF_DAY, 6);
		timeStart.set(Calendar.MINUTE, 0);
		timeStart.set(Calendar.SECOND, 0);

		if (now.get(Calendar.HOUR_OF_DAY) >= 22 || now.get(Calendar.HOUR_OF_DAY) < 6) {
			if (now.get(Calendar.HOUR_OF_DAY) >= 22)
				timeStart.set(Calendar.DAY_OF_MONTH, iDate + 1);
			else
				timeStart.set(Calendar.DAY_OF_MONTH, iDate);

			long delayIndicator = timeStart.getTimeInMillis() - now.getTimeInMillis();
			delayIndicator = TimeUnit.MILLISECONDS.toMinutes(delayIndicator);

			long difference = now.getTimeInMillis() - calendar.getTimeInMillis();
			if (difference < 0) {
				iDate--;
				calendar.set(Calendar.DATE, iDate);
				difference = now.getTimeInMillis() - calendar.getTimeInMillis();
			}
			orderTimeInMilliSeconds = calendar.getTimeInMillis();
			long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
			timeRemainsForNPS = 120 - minutes;

			if (timeRemainsForNPS >= delayIndicator + timeToDeliver)
				isNPSPossible = true;
		} else {
			Calendar timeLimit = Calendar.getInstance();
			timeLimit.set(Calendar.HOUR_OF_DAY, 22);
			timeLimit.set(Calendar.MINUTE, 0);
			timeLimit.set(Calendar.SECOND, 0);
			long delayIndicator = timeLimit.getTimeInMillis() - now.getTimeInMillis();
			if (delayIndicator > 0) {
				delayIndicator = TimeUnit.MILLISECONDS.toMinutes(delayIndicator);
				if (delayIndicator < travelTime)
					isNPSPossible = false;
				else
					isNPSPossible = true;
			}

			long difference = now.getTimeInMillis() - calendar.getTimeInMillis();
			if (difference < 0) {
				iDate--;
				calendar.set(Calendar.DATE, iDate);
				difference = now.getTimeInMillis() - calendar.getTimeInMillis();
			}
			orderTimeInMilliSeconds = calendar.getTimeInMillis();
			long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);
			timeRemainsForNPS = 120 - minutes;
			if (timeRemainsForNPS > 0 && timeRemainsForNPS > timeToDeliver)
				isNPSPossible = true;
			else
				isNPSPossible = false;
		}

	}

	/*
	 * public static void main(String[] args) { // OrderDetails od = new
	 * OrderDetails("WM0001 N11E5 15:03:00");
	 * System.out.println(Calendar.getInstance().getTime()); }
	 */

	public String toString() {
		return this.orderNumber + "," + this.location + "," + this.orderTime + "," + this.timeRemainsForNPS + ","
				+ this.timeToDeliver + "," + this.isNPSPossible + "," + this.travelTime + "," + this.startTimeOfDelivery
				+ "," + isNPSAchieved;
	}

}
