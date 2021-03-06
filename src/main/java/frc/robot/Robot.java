/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
/*
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.InternalButton;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
*/

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  Command autonomousCommand;
  SendableChooser<String> chooser = new SendableChooser<>();

  public static OI oi;
  public static Chassis chassis;
  public static ChassisSensors chassisSensors;
  public static Dashboard dashboard;
  public static Debugger debugger;
  public static Elevator elevator;
  public static LEDControl ledControl;
  public static Lift lift;
  public static Limelight limelight;
  public static Manipulator manipulator;
  public static DriverStation DS;
  public static UsbCamera rearCameraServer;
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    RobotMap.init();

    chassis = new Chassis();
    chassisSensors = new ChassisSensors();
    dashboard = new Dashboard();
    debugger = new Debugger();
    elevator = new Elevator();
    ledControl = new LEDControl();
    lift = new Lift();
    limelight = new Limelight();
    manipulator = new Manipulator();
    
    // OI must be constructed after subsystems. If the OI creates Commands
    //(which it very likely will), subsystems are not guaranteed to be
    // constructed yet. Thus, their requires() statements may grab null
    // pointers. Bad news. Don't move it.
    oi = new OI();
    
    DS = DriverStation.getInstance();

    //Setup Camera
    rearCameraServer = CameraServer.getInstance().startAutomaticCapture();
    rearCameraServer.setResolution(320, 180);
    rearCameraServer.setFPS(15);

    // Add commands to Autonomous Sendable Chooser
    chooser.setDefaultOption("Do Nothing", "Do Nothing");
    SmartDashboard.putData("Auto mode", chooser);

    Scheduler.getInstance().add(new InitializeDashboard());
    Scheduler.getInstance().add(new InitializeSensors());
    Scheduler.getInstance().add(new LimelightCamera());
    Scheduler.getInstance().run();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    //Scheduler.getInstance().add(new GetRoboPrefs());
    Scheduler.getInstance().add(new ReadLimelight());
    Scheduler.getInstance().add(new DashboardOutput());
    Scheduler.getInstance().run();
  }
  
  @Override
  public void disabledInit(){
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard.
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    switch (chooser.getSelected()) {
      case "Do Nothing":  autonomousCommand = new DoNothing();
      break;
      
      default:            autonomousCommand = new DoNothing();
      break;
    }
    
    if (autonomousCommand != null) autonomousCommand.start();
  }

  @Override
  public void testInit() {
  }

  
  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
