package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by musta on 5/22/2018.
 */

public class FtcColorSensor{

    public String instanceName = null;
    private ColorSensor colorSensor;
    private Telemetry telemetry;


    public FtcColorSensor(String instanceName, HardwareMap hardwareMap, Telemetry telemetry){
        this.instanceName = instanceName;
        this.telemetry = telemetry;
        colorSensor =  hardwareMap.get(ColorSensor.class, this.instanceName);
    }

    public boolean isBlue(){
        return(colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green());
    }

    public boolean isRed(){
        return (colorSensor.red() > colorSensor.blue() && colorSensor.red() > colorSensor.green());
    }

    public boolean isGreen(){
        return (colorSensor.green() > colorSensor.red() && colorSensor.green() > colorSensor.blue());

    }

    public boolean isWhite(){
        return (colorSensor.alpha() >= 27);
    }

    public int getAlphaValue(){
        return colorSensor.alpha();
    }

    public int getRedValue(){
        return colorSensor.red();
    }

    public int getBlueValue(){
        return colorSensor.blue();
    }

    public int getGreenValue(){
        return colorSensor.green();
    }

    public int getHue(){
        return colorSensor.argb();
    }

    public void enableLED(boolean state){
        colorSensor.enableLed(state);
    }

    public void setI2cAddress(I2cAddr address){
        colorSensor.setI2cAddress(address);
    }

    public String getI2cAddress(){

        return colorSensor.getI2cAddress().toString();
    }

    public void addTelemetry(){
        telemetry.addData(instanceName + " I2c Address: ", getI2cAddress());
        telemetry.addData(instanceName + " is blue: ", isBlue());
        telemetry.addData(instanceName + " is red: ", isRed());
        telemetry.addData(instanceName + " is green: ", isGreen());
        telemetry.addData(instanceName + " is white: ", isWhite());
        telemetry.addData(instanceName + " blue value: ", getBlueValue());
        telemetry.addData(instanceName + " red value: ", getRedValue());
        telemetry.addData(instanceName + " green value: ", getGreenValue());
        telemetry.addData(instanceName + " alpha value: ", getAlphaValue());
        telemetry.addData(instanceName + " hue value: ", getHue());
    }

    public String processText(String text){
        switch (text){
            case "is white":
                if(isWhite()){
                    return instanceName + " is white: true";
                } else{
                    return instanceName + " is white: false";
                }
            case "is blue":
                if(isBlue()){
                    return instanceName + " is blue: true";
                } else{
                    return instanceName + " is blue: false";
                }
            case "is red":
                if(isRed()){
                    return instanceName + " is red: true";
                } else{
                    return instanceName + " is red: false";
                }
            case "is green":
                if(isGreen()){
                    return instanceName + " is green: true";
                } else{
                    return instanceName + " is green: false";
                }
            case "get alpha":
                return instanceName + " alpha value: " + String.valueOf(getAlphaValue());
            case "get blue":
                return instanceName + " blue value: " + String.valueOf(getBlueValue());
            case "get red":
                return instanceName + " red value: " + String.valueOf(getRedValue());
            case "get green":
                return instanceName + " green value: " + String.valueOf(getGreenValue());
            case "get hue":
                return instanceName + " hue value: " + String.valueOf(getHue());
            case "enable led":
                return "request: enable led" + instanceName + " led has been enabled";
            case "get address":
                return instanceName + " I2C address: " + getI2cAddress();
            default:
                return "Could not register command";
        }
    }


}
