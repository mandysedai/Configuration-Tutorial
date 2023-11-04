# Configuration-Tutorial
Config Files

Storing data in config files has the following advantages:
  - One place to store numbers, avoiding hard coding numbers in multiple places in code.  
  - If this number needs to be changed, just change in one place instead of searching for all places where it is hard coded.
  - Cuts down on issues if you miss changing a number in a place where it is hard coded.
  - Can change numbers directly in config file without need to recompile and re-push code.  Speeding up configuration changes / testing and tweaking numbers.

This example shows how to use 3 formats of config file: Properties, XML and JSON

Things to know:
  - Opening and reading a file is relatively slow.  This should be done once during startup and never in a periodic method.
  - Put the config files into the “deploy” folder in VS Code -> src/main/deploy.  They will be deployed appropriately onto the roboRIO
  - Use Filesystem.getDeployDirectory()to get the path to the file.  
    - (located at src/main/deploy, which is deployed by default. On the roboRIO, this is /home/lvuser/deploy. In simulation, it is where the simulation was launched from, in the subdirectory "src/main/deploy")
  - You can modify the config files directly on the roboRIO when testing and tweaking numbers. 
    - Use Notepad++ with plugin NPPFtp

Watch out for:
  - Syntax errors - build in good error logging to find any configuration file syntax errors quickly.
  - It can be easy to typo the name either in the file, or when retrieving the data.
  - If changes are made directly to config file on robot.  Once final testing/tweaking is complete, make those changes in the config file in the VS code project so they do not get overwritten next time code is pushed to robot.
