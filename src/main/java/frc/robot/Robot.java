// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import edu.wpi.first.wpilibj.Filesystem;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.nio.file.*;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);



    getConfigFromProperties();
    getConfigFromXML();
    getConfigFromJSON();

  }


    /**
   * Read configuration using java properties
   * 
   * File is config.properties in the "deploy" folder
   * 
   * include the following libraries:
   * 
   * import java.io.FileReader;
   * import java.io.File;
   * import java.util.Properties;
   * import edu.wpi.first.wpilibj.Filesystem;
   * 
   */
  private void getConfigFromProperties(){

    File configFile = new File(Filesystem.getDeployDirectory(), "config.properties");
    try(FileReader reader =  new FileReader(configFile)) {

      Properties properties = new Properties();
      properties.load(reader);

      Double frameWidth = Double.parseDouble(properties.getProperty("frameWidth"));
      Double frameLen = Double.parseDouble(properties.getProperty("frameLen"));
      Double bumperThickness = Double.parseDouble(properties.getProperty("bumperThickness"));

      Integer swerveManagerCanSteerID0 = Integer.parseInt(properties.getProperty("swerveManagerCanSteerID0"));
      Integer swerveManagerCanSteerID1 = Integer.parseInt(properties.getProperty("swerveManagerCanSteerID1"));
      Integer swerveManagerCanSteerID2 = Integer.parseInt(properties.getProperty("swerveManagerCanSteerID2"));
      Integer swerveManagerCanSteerID3 = Integer.parseInt(properties.getProperty("swerveManagerCanSteerID3"));

      Integer swerveManagerCanDriveID0 = Integer.parseInt(properties.getProperty("swerveManagerCanDriveID0"));
      Integer swerveManagerCanDriveID1 = Integer.parseInt(properties.getProperty("swerveManagerCanDriveID1"));
      Integer swerveManagerCanDriveID2 = Integer.parseInt(properties.getProperty("swerveManagerCanDriveID2"));
      Integer swerveManagerCanDriveID3 = Integer.parseInt(properties.getProperty("swerveManagerCanDriveID3"));
      
      System.out.println("\n\n---------------Configuration from Properties---------------------------");
      System.out.println("frameWidth: " + frameWidth + " frameLen: " + frameLen + " bumperThickness: " + bumperThickness);
      System.out.println("swerveManager CAN Steer ID0: " + swerveManagerCanSteerID0
                                              + " ID1: " + swerveManagerCanSteerID1
                                              + " ID2: " + swerveManagerCanSteerID2
                                              + " ID3: " + swerveManagerCanSteerID3);
      System.out.println("swerveManager CAN Drive ID0: " + swerveManagerCanDriveID0
                                              + " ID1: " + swerveManagerCanDriveID1
                                              + " ID2: " + swerveManagerCanDriveID2
                                              + " ID3: " + swerveManagerCanDriveID3);
      System.out.println("-----------------------------------------------------------------------\n\n");

      
    }catch (Exception e) {
      System.err.println("Reading configuration properties file failed: " + e.getClass().toString() + ": " + e.getMessage());
      e.printStackTrace();
      //System.exit(-1);
    }
  }




  /**
   * Read configuration using XML
   * 
   * File is config.xml in the "deploy" folder
   * 
   * include the following libraries:
   * 
   * import java.io.File;
   * import edu.wpi.first.wpilibj.Filesystem;
   * import org.w3c.dom.Document;
   * import org.w3c.dom.Element;
   * import org.w3c.dom.NodeList;
   * import javax.xml.parsers.DocumentBuilder;
   * import javax.xml.parsers.DocumentBuilderFactory;
   */
  private void getConfigFromXML(){

    try{
      File configFile = new File(Filesystem.getDeployDirectory(), "config.xml");
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(configFile);

      doc.getDocumentElement().normalize();

      System.out.println("\n\n------");
      System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());
      System.out.println("------");

      // get frame size data
      Element element = (Element)doc.getElementsByTagName("frameSize").item(0);

      Double frameWidth = Double.parseDouble(element.getElementsByTagName("width").item(0).getTextContent());
      Double frameLen = Double.parseDouble(element.getElementsByTagName("length").item(0).getTextContent());
      Double bumperThickness = Double.parseDouble(element.getElementsByTagName("bumperThickness").item(0).getTextContent());

      // get swerve data
      Element swerveManagerCan = (Element)doc.getElementsByTagName("swerveManagerCan").item(0);

      NodeList steerIDs = swerveManagerCan.getElementsByTagName("steerID");
      
      int[] swerveManagerCanSteerIDs = new int[4];

      for (int i = 0; i < steerIDs.getLength(); i++) {

        Element e = (Element)steerIDs.item(i);
        int index = Integer.parseInt(e.getAttribute("id"));
        swerveManagerCanSteerIDs[index] = Integer.parseInt(e.getTextContent());
      }

      NodeList driveIDs = swerveManagerCan.getElementsByTagName("driveID");
      
      int[] swerveManagerCanDriveIDs = new int[4];

      for (int i = 0; i < driveIDs.getLength(); i++) {

        Element e = (Element)driveIDs.item(i);
        int index = Integer.parseInt(e.getAttribute("id"));
        swerveManagerCanDriveIDs[index] = Integer.parseInt(e.getTextContent());
      }
      


      System.out.println("\n\n---------------Configuration from XML----------------------------------");
      System.out.println("frameWidth: " + frameWidth + " frameLen: " + frameLen + " bumperThickness: " + bumperThickness);
      System.out.println("swerveManager CAN Steer ID0: " + swerveManagerCanSteerIDs[0]
                                              + " ID1: " + swerveManagerCanSteerIDs[1]
                                              + " ID2: " + swerveManagerCanSteerIDs[2]
                                              + " ID3: " + swerveManagerCanSteerIDs[3]);
      System.out.println("swerveManager CAN Drive ID0: " + swerveManagerCanDriveIDs[0]
                                              + " ID1: " + swerveManagerCanDriveIDs[1]
                                              + " ID2: " + swerveManagerCanDriveIDs[2]
                                              + " ID3: " + swerveManagerCanDriveIDs[3]); 
      System.out.println("-----------------------------------------------------------------------\n\n");

      
    }catch (Exception e) {
      
      System.err.println("Reading configuration XML file failed: " + e.getClass().toString() + ": " + e.getMessage());
      e.printStackTrace();
      //System.exit(-1);
    }
  }



  
  /**
   * Read configuration using JSON
   * 
   * File is config.json in the "deploy" folder
   * 
   * include the following libraries:
   * 
   * import java.nio.file.*;
   * import edu.wpi.first.wpilibj.Filesystem;
   * import com.fasterxml.jackson.databind.ObjectMapper;
   */
  private void getConfigFromJSON(){

    try{
      Path configFilePath = Paths.get(Filesystem.getDeployDirectory().toString(), "config.json");
      byte[] jsonData = Files.readAllBytes(configFilePath);
		
      //create ObjectMapper instance
      ObjectMapper objectMapper = new ObjectMapper();

      //convert json string to object
      RobotConfig robotConfig = objectMapper.readValue(jsonData, RobotConfig.class);

      FrameSize frameSize = robotConfig.getFrameSize();
      System.out.println("\n\n---------------Configuration from JSON----------------------------------");
      System.out.println("frameWidth: " + frameSize.getWidth() + " frameLen: " + frameSize.getLength() + " bumperThickness: " + frameSize.getBumperThickness());
      
      SwerveManagerCan swerveManagerCan = robotConfig.getSwerveManagerCan();
      int[] steerIDs = swerveManagerCan.getSteerIDs();
      System.out.println("swerveManager CAN Steer ID0: " + steerIDs[0]
                                              + " ID1: " + steerIDs[1]
                                              + " ID2: " + steerIDs[2]
                                              + " ID3: " + steerIDs[3]);
      int[] driveIDs = swerveManagerCan.getDriveIDs();
      System.out.println("swerveManager CAN Drive ID0: " + driveIDs[0]
                                              + " ID1: " + driveIDs[1]
                                              + " ID2: " + driveIDs[2]
                                              + " ID3: " + driveIDs[3]);
      
      Map<String, String> robotInfo = robotConfig.getRobotInfo();
      System.out.println("RobotInfo:");
      for (String key : robotInfo.keySet()) {
        System.out.println("\t" + key + ": " + robotInfo.get(key));
    }
      System.out.println("-----------------------------------------------------------------------\n\n");

      
    }catch (Exception e) {
      
      System.err.println("Reading configuration JSON file failed: " + e.getClass().toString() + ": " + e.getMessage());
      e.printStackTrace();
      //System.exit(-1);
    }

  }


  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
