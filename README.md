# DroneDeliveryChallenge
DroneDeliveryChallenge 

## How to build
1. download code
2. go to respective code folder; and run the below command

	```mvn clean install ```
	
	It should build and run 3 tests.
## How to test
3. cd target 
4. java -cp dronedeliverychallange-1.0-jar-with-dependencies.jar com.org.wm.drone.DroneDelivery <filePath&name>
5. go to the parent folder of the provided file, there would be 2 files generated

	* filename_result/milliseconds/.txt -> File a is result file as expected Order Number & start time for that order's delivery
	* filename_result_explanation milliseconds.csv -> File b contains all attributes values for order details which helps in calculation of schedule.
	
	
## How test cases works
6. 3 Test cases wrote for the application
	* for DroneDataLoader; to load the file data to list of order details & verify if its not empty.
	* for DroneScheduler; to schedule the loaded data and verify with expected list (hardcoded)
	* for DroneService; to execute full drone service and read the actual output file and compare with expected results file.
