package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.math.RoundingMode;

/**
 * Created by musta on 6/4/2018.
 */

public class FtcDcMotor {
    public String instanceName = null;
    private Telemetry telemetry = null;
    private DcMotor dcMotor = null;
    private ElapsedTime elapsedTime = null;
    private LinearOpMode opMode = null;
    private int currentTarget = 0;
    private boolean isInverted;


    public FtcDcMotor(String instanceName, boolean isInverted, LinearOpMode opMode, HardwareMap hardwareMap, Telemetry telemetry, ElapsedTime elapsedTime){
        this.instanceName = instanceName;
        this.telemetry = telemetry;
        this.elapsedTime = elapsedTime;
        this.isInverted = isInverted;
        this.opMode = opMode;

        dcMotor = hardwareMap.get(DcMotor.class, this.instanceName);
        setInverted(isInverted);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setMode(DcMotor.RunMode runMode){
        dcMotor.setMode(runMode);
    }

    public DcMotor.RunMode getMode(){
        return dcMotor.getMode();
    }

    public String getModeAsString(){
        if(getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER){
            return "run without encoder";
        } else if(getMode() == DcMotor.RunMode.RUN_USING_ENCODER){
            return "run using encoder";
        } else if(getMode() == DcMotor.RunMode.RUN_TO_POSITION){
            return "run to position";
        } else if(getMode() == DcMotor.RunMode.STOP_AND_RESET_ENCODER){
            return "stop and reset encoders";
        } else{
            return "mode is unknown";
        }
    }

    public void setPower(double power){
        dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        dcMotor.setPower(power);
    }

    public void setPowerPID(double power){
        dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        dcMotor.setPower(power);
    }

    public double getPower(){
        return dcMotor.getPower();
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior){
        dcMotor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    public DcMotor.ZeroPowerBehavior getZeroPowerBehavior(){
        return dcMotor.getZeroPowerBehavior();
    }

    public String getZeroPowerBehaviorAsString(){
        if(getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.BRAKE){
            return "powered brake";
        } else if(getZeroPowerBehavior() == DcMotor.ZeroPowerBehavior.FLOAT){
            return "coasting brake";
        } else{
            return "power behavior unknown";
        }
    }

    public boolean isBusy(){
        return dcMotor.isBusy();
    }

    public void setTargetPosition(int position){
        dcMotor.setTargetPosition(position);
        currentTarget = position;
    }

    public int getTargetPosition(){
        return dcMotor.getTargetPosition();
    }

    public int getCurrentPosition(){
        return dcMotor.getCurrentPosition();
    }

    public boolean isInverted(){
        return dcMotor.getDirection() == DcMotorSimple.Direction.REVERSE;
    }

    public void setInverted(boolean isInverted){
        if(isInverted){
            dcMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        } else{
            dcMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
    }

    public int getPortNumber(){
        return dcMotor.getPortNumber();
    }

    public void powerBrake(){
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setPower(0);
    }

    public void coastBrake(){
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        setPower(0);
    }

    //only to be used for one motor
    public void encoderRunLoop(int degrees, double power, double timeOutSeconds){
        if(opMode.opModeIsActive()){
            timeOutSeconds += elapsedTime.seconds();
            int target = getCurrentPosition() + (int)(degrees * RobotHardware.COUNTS_PER_DEGREE);
            setTargetPosition(target);
            telemetry.addData(instanceName + " Encoder Run Status: ", "Target Position Set");
            telemetry.update();

            setMode(DcMotor.RunMode.RUN_TO_POSITION);
            telemetry.addData(instanceName + " Encoder Drive Status: ", "Set to RUN_TO_POSITION");
            telemetry.update();

            setPower(power);
            telemetry.addData(instanceName + " Encoder Run Status: ", "Drive turned on");
            telemetry.update();

            while(opMode.opModeIsActive() && isBusy() && (timeOutSeconds > elapsedTime.seconds())){
                telemetry.addData(instanceName + " Motor Counts Remaining: ",
                        dcMotor.getTargetPosition() - dcMotor.getCurrentPosition());
                telemetry.addData(instanceName + " Motor Power: ", dcMotor.getPower());
                telemetry.update();
                opMode.idle();
            }

            powerBrake();
            telemetry.addData(instanceName + " Encoder Run Status: ", "Drive turned off");
            telemetry.update();

            setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            telemetry.addData(instanceName + " Encoder Drive Status: ", "Set to RUN_USING_ENCODER");
            telemetry.update();
        }
    }

    public void setEncoderTarget(double target, RobotHardware.CountsUnit countsUnit){
        if(countsUnit == RobotHardware.CountsUnit.DEGREES) {
            target *= RobotHardware.COUNTS_PER_DEGREE;
        }else if(countsUnit == RobotHardware.CountsUnit.INCHES){
            target *= RobotHardware.COUNTS_PER_INCH;
        }
        currentTarget = (int)target;
    }

    public void toTarget(double power, double buffer){
        if(currentTarget > getCurrentPosition() + buffer){
            setPowerPID(-power);
        } else if(currentTarget < getCurrentPosition() - buffer){
            setPowerPID(power);
        } else{
            powerBrake();
        }
    }

    //only to be used for one motor
    public void timeRun(double timeInSeconds, double power){
        timeInSeconds += elapsedTime.seconds();
        setPowerPID(power);
        while((timeInSeconds > elapsedTime.seconds()) && opMode.opModeIsActive()){
            telemetry.addData(instanceName + " Time Run Time Left: " , "%.2f", timeInSeconds - elapsedTime.seconds());
            telemetry.update();
            opMode.idle();
        }
        powerBrake();
    }

    public void setToButton(boolean button, double defaultPower, double secondaryPower, RobotHardware.ButtonChange buttonChange){
        if(buttonChange == RobotHardware.ButtonChange.TOGGLE){
            if(button) {
                if (getPower() == defaultPower) {
                    setPowerPID(secondaryPower);
                } else {
                    setPowerPID(defaultPower);
                }
            }
        } else if(buttonChange == RobotHardware.ButtonChange.WHEN_PRESSED){
            if(button){
                setPowerPID(secondaryPower);
            } else{
                setPowerPID(defaultPower);
            }
        }
    }

    public void setButtonsToPositions(boolean[] buttons, int[] encoderCounts, RobotHardware.CountsUnit countsUnit){
        //need to call to target in a loop when implementing this method
        for(int i = 0; i < encoderCounts.length; i++){
            if(buttons[i]){
                if(countsUnit == RobotHardware.CountsUnit.INCHES){
                    encoderCounts[i] *= RobotHardware.COUNTS_PER_INCH;
                } else if(countsUnit == RobotHardware.CountsUnit.DEGREES){
                    encoderCounts[i] *= RobotHardware.COUNTS_PER_DEGREE;
                }
                currentTarget = encoderCounts[i];
            }
        }
    }

    public void addTelemetry(){
        telemetry.addData(instanceName + " Port Number: ", getPortNumber());
        telemetry.addData(instanceName + " Power: ", getPower());
        telemetry.addData(instanceName + " Mode: ", getModeAsString());
        telemetry.addData(instanceName + " Zero Power Behavior: ", getZeroPowerBehaviorAsString());
        telemetry.addData(instanceName + " Is Inverted: ", String.valueOf(isInverted()));
        telemetry.addData(instanceName + " Target: ", currentTarget);
        telemetry.addData(instanceName + " Position: ", getCurrentPosition());
        telemetry.update();
    }


}
