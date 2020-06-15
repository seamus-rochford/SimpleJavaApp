
To run the SimpleJavaApp locally

	1. Export jar file to export folder

	2. cd d:/Projects/BriteBin/SimpleJavaApp/export

	3. java -jar SimpleJavaApp.jar

	
	To deploy to dev-server for testing
	
		1.	Open Filezilla
		2.	Connect to server (161.35.31.177) using "Site Manager"
		3.  Navigate to "D:\Projects\BriteBin\SimpleJavaApp\export\" locally
		4.	Navigate to "/apps/britebin/simplejavaapp" remotely 
		5.	Drag SimpleJavaApp.jar from left pane to right pane
		6.	To test
				-	ssh to server
				-	navigate to /apps/britebin/simplejavaapp
				-	java -jar SimpleJavaApp.jar
				