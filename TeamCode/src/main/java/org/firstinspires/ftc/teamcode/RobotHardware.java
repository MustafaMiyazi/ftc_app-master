package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightMultiplexor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by musta on 6/3/2018.
 */

public class RobotHardware {


    //Robot Hardware Elements Go

    public final static double COUNTS_PER_REV = 1120; //AndyMark 40:1 motor
    public final static double WHEEL_DIAMETER = 4; //Wheel diameter of 4 inches
    public final static double DRIVE_GEAR_REDUCTION = 1.0; //No gear reduction applied
    public final static double COUNTS_PER_DEGREE = (COUNTS_PER_REV * DRIVE_GEAR_REDUCTION)/360;
    public final static double COUNTS_PER_INCH = (COUNTS_PER_REV * DRIVE_GEAR_REDUCTION)/(WHEEL_DIAMETER * Math.PI);

    public enum ArcadeDriveSticks {RIGHT, LEFT, BOTH}
    public enum ButtonChange {TOGGLE, WHEN_PRESSED}
    public enum CountsUnit {RAW_COUNTS, INCHES, DEGREES}

    public DriveTrain driveTrain = null;
    private FtcDcMotor frontRightMotor = null, backRightMotor = null, frontLeftMotor = null, backLeftMotor = null;
    private FtcDcMotor[] rightMotors = {frontRightMotor, backRightMotor};
    private String[] rightMotorNames = {"frontRightMotor", "backRightMotor"};
    private FtcDcMotor[] leftMotors = {frontLeftMotor, backLeftMotor};
    private String[] leftMotorNames = {"frontLeftMotor", "backLeftMotor"};

    public FtcColorSensor colorSensor = null;
    public RevIMU revIMU = null;
    public FtcServo servo = null;

    private HardwareMap hardwareMap = null;
    private Telemetry telemetry = null;
    private ElapsedTime elapsedTime = null;
    private LinearOpMode linearOpMode = null;

    public RobotHardware(LinearOpMode linearOpMode, HardwareMap hardwareMap, Telemetry telemetry, ElapsedTime elapsedTime){
        this.linearOpMode = linearOpMode;
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.elapsedTime = elapsedTime;
    }

    public void init(){
        elapsedTime.reset();
        driveTrain = new DriveTrain(rightMotors, rightMotorNames, leftMotors, leftMotorNames, hardwareMap,
                telemetry, linearOpMode, elapsedTime);
        colorSensor = new FtcColorSensor("colorSensor", hardwareMap, telemetry);
        revIMU = new RevIMU("IMU", hardwareMap, telemetry);
        servo = new FtcServo("servo", 0, hardwareMap, telemetry);
    }
}
