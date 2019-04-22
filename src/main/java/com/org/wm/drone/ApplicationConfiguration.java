package com.org.wm.drone;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.org.wm.drone.service.DroneSchedulerService;
import com.org.wm.drone.service.DroneService;
import com.org.wm.drone.service.impl.DroneDataLoader;
import com.org.wm.drone.service.impl.DroneSchedulerServiceImpl;
import com.org.wm.drone.service.impl.DroneServiceImpl;

@Configuration
public class ApplicationConfiguration {

	@Bean(name = "droneService")
	public DroneService droneService() {
		return new DroneServiceImpl();
	}

	@Bean(name = "droneDataLoader")
	public DroneDataLoader droneDataLoader() {
		return new DroneDataLoader();
	}

	@Bean(name = "droneSchedulerService")
	public DroneSchedulerService droneSchedulerService() {
		return new DroneSchedulerServiceImpl();
	}

}
