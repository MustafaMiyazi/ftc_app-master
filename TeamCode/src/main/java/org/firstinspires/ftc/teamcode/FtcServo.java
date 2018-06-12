package org.firstinspires.ftc.teamcode;



import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by musta on 5/27/2018.
 */

public class FtcServo {
    private Servo servo = null;
    public String instanceName = null;
    private Telemetry telemetry = null;
    public int initPosition;

    public FtcServo(String instanceName, int initPosition, HardwareMap hardwareMap, Telemetry telemetry) {
        this.instanceName = instanceName;
        this.telemetry = telemetry;
        this.initPosition = initPosition;
        servo = hardwareMap.get(Servo.class, instanceName);
        servo.setDirection(Servo.Direction.FORWARD);
    }

    public void setPosition(double position){
        servo.setPosition(position);
        telemetry.addData(instanceName + " At Position: ", position);
        telemetry.update();
    }

    public void setInverted(){
        servo.setDirection(Servo.Direction.REVERSE);
        telemetry.addData(instanceName + " Direction: ", "Reverse");
        telemetry.update();
    }

    public void setForward(){
        servo.setDirection(Servo.Direction.FORWARD);
        telemetry.addData(instanceName + " Direction: ", "Forward");
        telemetry.update();
    }

    public boolean isInverted(){
        return servo.getDirection() == Servo.Direction.REVERSE;
    }

    public void setInitPosition(int newInitPosition) {
        initPosition = newInitPosition;
    }

    public void initialize() {
        setPosition(initPosition);
        telemetry.addData(instanceName + " Status: ", instanceName + " initialized");
        telemetry.update();
    }

    public boolean isInitialized(){
        return getPosition() == initPosition;
    }


    public double getPosition(){
        telemetry.addData(instanceName + " At Position: ", servo.getPosition());
        telemetry.update();
        return servo.getPosition();
    }

    public void setToggleButton(boolean button, double...positions){
        double servoPosition = getPosition();
        if(button) {
            for (int i = 0; i < positions.length; i++){
                if(positions[i] == servoPosition){
                    if(i == positions.length - 1){
                        setPosition(positions[0]);
                    }
                    else{
                        setPosition(positions[i+1]);
                    }
                }
            }
        }
    }

    public void setButtonsToPositions(boolean[] buttons , double[] positions){
        for(int i = 0; i < buttons.length; i++){
            if(buttons[i]){
                setPosition(positions[i]);
            }
        }
    }

    public void pair(FtcServo otherServo){
        setPosition(otherServo.getPosition());
        telemetry.addData(instanceName + " Status: ", "Paired with ", otherServo.instanceName);
        telemetry.update();
    }


}
