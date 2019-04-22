package com.org.wm.drone.service;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public interface DroneService {

	public String processStart(File file) throws Exception;
}
