package org.firstinspires.ftc.teamcode;

import android.graphics.drawable.GradientDrawable;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

/**
 * Created by musta on 5/26/2018.
 */

public class RevIMU {

    public String instanceName = null;
    private Telemetry telemetry = null;
    private BNO055IMU imu = null;
    private Orientation angles = null;


    public RevIMU(String instanceName, HardwareMap hardwareMap, Telemetry telemetry){
        this.instanceName = instanceName;
        this.telemetry = telemetry;
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu = hardwareMap.get(BNO055IMU.class, this.instanceName);
        imu.initialize(parameters);
    }

    public double getZAngle(){
        imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        return angles.thirdAngle;
    }

    public double getYAngle(){
        imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        return angles.secondAngle;
    }

    public double getXAngle(){
        imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        return angles.firstAngle;
    }

    public double[] getAllAxis(){ //when needed to poll multiple axis, getting angles is expensive process
        imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        double[] values = {angles.firstAngle, angles.secondAngle, angles.thirdAngle};
        return values;
    }

    public void addTelemetry(){
        double[] values = getAllAxis();
        telemetry.addData(instanceName + " X-Axis: ", values[0]);
        telemetry.addData(instanceName + " Y-Axis: ", values[1]);
        telemetry.addData(instanceName + " Z-Axis: ", values[2]);
        telemetry.update();
    }


}
