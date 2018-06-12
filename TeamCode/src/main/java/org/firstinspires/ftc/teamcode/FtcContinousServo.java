package org.firstinspires.ftc.teamcode;

import android.content.Context;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

/**
 * Created by musta on 5/29/2018.
 */

public class FtcContinousServo {
    private CRServo crServo;
    public String instanceName = null;
    private Telemetry telemetry = null;
    private ElapsedTime elapsedTime = null;
    private LinearOpMode opMode = null;

    public FtcContinousServo(String instanceName, HardwareMap hardwareMap, Telemetry telemetry, ElapsedTime elapsedTime, LinearOpMode opMode){
        this.instanceName = instanceName;
        this.telemetry = telemetry;
        this.elapsedTime = elapsedTime;
        this.opMode = opMode;
        crServo = hardwareMap.get(CRServo.class, instanceName);
        crServo.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void setPower(double powerDelta){
        Range.clip(powerDelta, -0.5, 0.5);
        powerDelta += 0.5;
        telemetry.addData(instanceName + " Power: ", powerDelta);
        telemetry.update();
        crServo.setPower(powerDelta);
    }

    public void stop(){
        setPower(0);
    }

    public void setInverted(){
        crServo.setDirection(DcMotorSimple.Direction.REVERSE);
        telemetry.addData(instanceName + " Direction: ", "Reverse");
        telemetry.update();
    }

    public void setForward(){
        crServo.setDirection(DcMotorSimple.Direction.FORWARD);
        telemetry.addData(instanceName + " Direction: ", "Forward");
        telemetry.update();
    }

    public boolean isInverted(){
        return crServo.getDirection() == DcMotorSimple.Direction.REVERSE;
    }

    public double getPower(){
        telemetry.addData(instanceName + " Power: ", crServo.getPower());
        telemetry.update();
        return crServo.getPower();
    }

    public void setToButton(boolean button, double powerDelta){
        if(button){
            setPower(powerDelta);
        }
        else{
            stop();
        }
    }

    public void runByTime(double timeInSeconds, double powerDelta){
        timeInSeconds += elapsedTime.seconds();
        setPower(powerDelta);
        while((timeInSeconds > elapsedTime.seconds()) && opMode.opModeIsActive()){
            telemetry.addData(instanceName + " Time Remaining: ", "%.2f", timeInSeconds - elapsedTime.seconds());
            telemetry.update();
            opMode.idle();
        }
        stop();
    }

    public void setButtonTimeBounce(boolean button, double timeInSeconds, double powerDelta){

        if(button){

        }
    }




}
