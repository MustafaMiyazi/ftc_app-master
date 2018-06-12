package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by musta on 5/24/2018.
 */

public class FtcDistanceSensor{
    public String instanceName = null;
    public DistanceUnit distanceUnit;
    private DistanceSensor distanceSensor = null;
    private Telemetry telemetry = null;

    public FtcDistanceSensor(DistanceUnit distanceUnit, String instanceName, HardwareMap hardwareMap, Telemetry telemetry){
        this.distanceUnit = distanceUnit;
        this.instanceName = instanceName;
        this.telemetry = telemetry;
        distanceSensor = hardwareMap.get(DistanceSensor.class, this.instanceName);
    }

    public void setDistanceUnit(DistanceUnit distanceUnit){
        this.distanceUnit = distanceUnit;
    }

    public double getDistance(){
        if(distanceSensor.getDistance(distanceUnit) == DistanceSensor.distanceOutOfRange){
            telemetry.addData( instanceName + " Status: ", "Out of Range");
            telemetry.update();
        }
        return  distanceSensor.getDistance(distanceUnit);
    }

    public double fromDistance(double distance){
        return getDistance() - distance;
    }

    public void addTelemetry(){
        telemetry.addData(instanceName + " Distance: ", getDistance());
        telemetry.update();
    }
}
