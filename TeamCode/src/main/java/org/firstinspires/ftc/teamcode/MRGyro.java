package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by musta on 5/26/2018.
 */

public class MRGyro {

    public String instanceName = null;
    private I2cAddr i2cAddr = null;
    private Telemetry telemetry = null;
    private LinearOpMode opMode = null;
    private ModernRoboticsI2cGyro gyro = null;


    public MRGyro(String instanceName, I2cAddr i2cAddr, HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode opMode){
        this.instanceName = instanceName;
        this.telemetry = telemetry;
        this.opMode = opMode;
        gyro = (ModernRoboticsI2cGyro) hardwareMap.get(Gyroscope.class, instanceName);
        gyro.setI2cAddress(i2cAddr);
        calibrate();
    }

    public double getZAngle(){
        return gyro.getIntegratedZValue();
    }

    public double getRawZAngle(){
        return gyro.rawZ();
    }

    public double getRawYAngle(){
        return gyro.rawY();
    }

    public double getRawXAngle(){
        return gyro.rawX();
    }

    public void calibrate(){
        gyro.calibrate();
        telemetry.addData(instanceName + " status: ", "is calibrating.");
        telemetry.update();
        while(gyro.isCalibrating()){
            opMode.idle();
        }
        telemetry.addData(instanceName + " status: ", "calibrated!");
        telemetry.update();
    }

    public int getHeading(){
        return gyro.getHeading();
    }

    public void addTelemetry(){
        telemetry.addData(instanceName + " Raw X-Axis: ", getRawXAngle());
        telemetry.addData(instanceName + " Raw Y-Axis: ", getRawYAngle());
        telemetry.addData(instanceName + " Raw Z-Axis: ", getRawZAngle());
        telemetry.addData(instanceName + " Integrated Z Value", getZAngle());
        telemetry.addData(instanceName + " Heading", getHeading());
        telemetry.update();
    }


}
