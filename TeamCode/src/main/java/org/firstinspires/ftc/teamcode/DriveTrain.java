package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.lang.reflect.Field;

/**
 * Created by musta on 5/25/2018.
 */

public class DriveTrain {
    private FtcDcMotor[] rightMotors = null;
    private String[] rightMotorNames = null;
    private FtcDcMotor[] leftMotors = null;
    private String[] leftMotorNames = null;
    private Telemetry telemetry = null;
    private LinearOpMode opMode = null;
    private ElapsedTime elapsedTime = null;
    private FtcRobotControllerActivity audio = null;

    public DriveTrain(FtcDcMotor[] rightMotors, String[] rightMotorNames, FtcDcMotor[] leftMotors, String[] leftMotorNames, HardwareMap hardwareMap,
                      Telemetry telemetry, LinearOpMode opMode, ElapsedTime elapsedTime){
        this.rightMotors = rightMotors;
        this.rightMotorNames = rightMotorNames;
        for(int i=0; i<rightMotors.length; i++){
            this.rightMotors[i] = new FtcDcMotor(rightMotorNames[i], false, opMode, hardwareMap, telemetry, elapsedTime);
            this.rightMotors[i].setInverted(false);
        }

        this.leftMotors = leftMotors;
        this.leftMotorNames = leftMotorNames;
        for(int i=0; i<leftMotors.length; i++){
            this.leftMotors[i] = new FtcDcMotor(leftMotorNames[i], false, opMode, hardwareMap, telemetry, elapsedTime);
            leftMotors[i].setInverted(true);
        }

        this.telemetry = telemetry;
        this.opMode = opMode;
        this.elapsedTime = elapsedTime;
    }

    public void setDriveMode(DcMotor.RunMode runMode){
        for(FtcDcMotor rightMotor : rightMotors){
            rightMotor.setMode(runMode);
        }
        for(FtcDcMotor leftMotor : leftMotors){
            leftMotor.setMode(runMode);
        }
    }

    public void setPowerPID(double rightPower, double leftPower){
        for(FtcDcMotor rightMotor : rightMotors){
            rightMotor.setPowerPID(rightPower);
        }
        for(FtcDcMotor leftMotor : leftMotors){
            leftMotor.setPowerPID(leftPower);
        }
    }

    public void setPower(double rightPower, double leftPower){
        for(FtcDcMotor rightMotor : rightMotors){
            rightMotor.setPower(rightPower);
        }
        for(FtcDcMotor leftMotor : leftMotors){
            leftMotor.setPower(leftPower);
        }
    }

    public void powerBrake(){
        for(FtcDcMotor rightMotor : rightMotors){
            rightMotor.powerBrake();
        }
        for(FtcDcMotor leftMotor : leftMotors){
            leftMotor.powerBrake();
        }
    }

    public void coastBrake(){
        for(FtcDcMotor rightMotor : rightMotors){
            rightMotor.coastBrake();
        }
        for(FtcDcMotor leftMotor : leftMotors){
            leftMotor.coastBrake();
        }
    }




    public void driveUntilReachedDistance(double distance, FtcDistanceSensor distanceSensor, double motorSpeed, double buffer){
        if((distanceSensor.fromDistance(distance) >= buffer) && opMode.opModeIsActive()){
            setPowerPID(motorSpeed, motorSpeed);
            telemetry.addData("Drive w/  " + distanceSensor.instanceName + " :", "Distance of %d remaining",distance - distanceSensor.getDistance());
            telemetry.update();
            opMode.idle();
            driveUntilReachedDistance(distance, distanceSensor, motorSpeed, buffer);
        }
        else{
            powerBrake();
            telemetry.addData("Drive w/  " + distanceSensor.instanceName + " :", "Reached distance of %d", distance);
            telemetry.update();
        }
    }

    public void driveUntilExceededDistance(double distance, FtcDistanceSensor distanceSensor, double motorSpeed, double buffer){
        if((distanceSensor.fromDistance(distance) <= -buffer) && opMode.opModeIsActive()){
            setPowerPID(motorSpeed, motorSpeed);
            telemetry.addData("Drive w/  " + distanceSensor.instanceName + " :", "Distance of %d remaining", distanceSensor.getDistance() - distance);
            telemetry.update();
            opMode.idle();
            driveUntilExceededDistance(distance, distanceSensor, motorSpeed, buffer);
        }
        else{
            powerBrake();
            telemetry.addData("Drive w/  " + distanceSensor.instanceName + " :", "Reached distance of %d", distance);
            telemetry.update();
        }
    }

    public void driveToDistance(double distance,  FtcDistanceSensor distanceSensor, double motorSpeed, double buffer){
        if((distanceSensor.fromDistance(distance) >= buffer) && opMode.opModeIsActive()){
            driveUntilReachedDistance(distance, distanceSensor, motorSpeed, buffer);
            driveToDistance(distance, distanceSensor, motorSpeed, buffer);
        }
        else if((distanceSensor.fromDistance(distance) <= -buffer) && opMode.opModeIsActive()){
            driveUntilExceededDistance(distance, distanceSensor, -motorSpeed, buffer);
            driveToDistance(distance, distanceSensor, motorSpeed, buffer);
        }
        else{
            powerBrake();
        }
    }

    public void turnAbsoluteWithGyro(double degrees, double motorPower, double buffer, double timeOutSeconds, RevIMU imu){
        double startPosition = imu.getZAngle();
        timeOutSeconds += elapsedTime.seconds();
        while((Math.abs(startPosition - degrees) > buffer) && timeOutSeconds > elapsedTime.seconds()){
            if(startPosition > degrees){
                 setPowerPID(-motorPower, motorPower);
            }
            else if(startPosition < degrees){
                setPowerPID(motorPower, -motorPower);
            }
            opMode.idle();
            startPosition = imu.getZAngle();
            telemetry.addData("Turn with Gyro: ", "%d degrees remaining", Math.abs(startPosition-degrees));
            telemetry.update();
        }
        powerBrake();
        telemetry.addData("Turn with Gyro: ", "Finished Turn");
        telemetry.update();
    }

    public void turnWithGyro(double degrees, double motorPower, double buffer, double timeOutSeconds, RevIMU imu){
        turnAbsoluteWithGyro(imu.getZAngle() + degrees, motorPower, buffer, timeOutSeconds, imu);
    }

    public void encoderDrive(double rightDistance, double rightPower, double leftDistance,
                             double leftPower, double timeOutSeconds) throws InterruptedException{
        if(opMode.opModeIsActive()){
            timeOutSeconds += elapsedTime.seconds();
            int rightTarget = rightMotors[0].getCurrentPosition() + (int)(rightDistance * RobotHardware.COUNTS_PER_INCH);
            int leftTarget = leftMotors[0].getCurrentPosition() + (int)(leftDistance * RobotHardware.COUNTS_PER_INCH);
            rightMotors[0].setTargetPosition(rightTarget);
            leftMotors[0].setTargetPosition(leftTarget);
            telemetry.addData("Encoder Drive Status: ", "Target Position Set");
            telemetry.update();

            setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
            telemetry.addData("Encoder Drive Status: ", "Set to RUN_TO_POSITION");
            telemetry.update();

            for(FtcDcMotor rightMotor : rightMotors){
                rightMotor.setPower(rightPower);
            }
            for(FtcDcMotor leftMotor : leftMotors){
                leftMotor.setPower(leftPower);
            }
            telemetry.addData("Encoder Drive Status: ", "Drive turned on");
            telemetry.update();


            while(opMode.opModeIsActive() && rightMotors[0].isBusy() && leftMotors[0].isBusy() && (timeOutSeconds > elapsedTime.seconds())){
                telemetry.addData("Right Motor Counts Remaining: ",
                        rightMotors[0].getTargetPosition() - rightMotors[0].getCurrentPosition());
                telemetry.addData("Right Motor Power: ", rightMotors[0].getPower());
                telemetry.addData("Left Motor Counts Remaining: ",
                        leftMotors[0].getTargetPosition() - leftMotors[0].getCurrentPosition());
                telemetry.addData("Left Motor Power: ", leftMotors[0].getPower());
                telemetry.update();
                opMode.idle();
            }

            powerBrake();
            telemetry.addData("Encoder Drive Status: ", "Drive turned off");
            telemetry.update();

            setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
            telemetry.addData("Encoder Drive Status: ", "Set to RUN_USING_ENCODER");
        }
    }

    public void driveWithEncodersAndGyro(double distance, double motorPower, double timeOutSeconds, RevIMU imu){
        timeOutSeconds += elapsedTime.seconds();
        double rightPower, leftPower, currentOrientation;
        double startOrientation = imu.getZAngle();
        int target = rightMotors[0].getCurrentPosition() + (int)(distance * RobotHardware.COUNTS_PER_INCH);

        while((rightMotors[0].getCurrentPosition() < target) && timeOutSeconds > elapsedTime.seconds()){
            currentOrientation = imu.getZAngle();
            rightPower = motorPower - (currentOrientation - startOrientation)/100;
            leftPower = motorPower + (currentOrientation - startOrientation)/100;

            Range.clip(rightPower, -1, 1);
            Range.clip(leftPower, -1, 1);

            setPowerPID(rightPower, leftPower);

            telemetry.addData("Drive Straight Distance Remaining: ", target - rightMotors[0].getCurrentPosition());
            telemetry.addData("Gyro Deviation: ", currentOrientation - startOrientation);
            telemetry.update();

            opMode.idle();
        }
        powerBrake();
    }

    public void timeDrive(double timeInSeconds, double rightPower, double leftPower){
        timeInSeconds += elapsedTime.seconds();
        setPowerPID(rightPower, leftPower);
        while((timeInSeconds > elapsedTime.seconds()) && opMode.opModeIsActive()){
            telemetry.addData("Time Drive Time Left: " , "%.2f", timeInSeconds - elapsedTime.seconds());
            telemetry.update();
            opMode.idle();
        }
        powerBrake();
    }

    public void tankDrive(Gamepad gamepad){
        double rightPower = -gamepad.right_stick_y;
        double leftPower = -gamepad.left_stick_y;

        Range.clip(rightPower, -1, 1);
        Range.clip(leftPower, -1, 1);

        setPowerPID(rightPower, leftPower);

        telemetry.addData("Right Tank Drive Power: ", rightPower);
        telemetry.addData("Left Tank Drive Power ", leftPower);
        telemetry.update();
    }



    public void arcadeDrive(Gamepad gamepad, RobotHardware.ArcadeDriveSticks arcadeDriveSticks){
        double rightPower, leftPower;
        if(arcadeDriveSticks == RobotHardware.ArcadeDriveSticks.RIGHT){
            rightPower = -gamepad.right_stick_y - gamepad.right_stick_x;
            leftPower = -gamepad.right_stick_y + gamepad.right_stick_x;
        }
        else if(arcadeDriveSticks == RobotHardware.ArcadeDriveSticks.LEFT){
            rightPower = -gamepad.left_stick_y - gamepad.left_stick_x;
            leftPower = -gamepad.left_stick_y + gamepad.left_stick_x;
        }
        else{
            rightPower = -gamepad.left_stick_y - gamepad.right_stick_x;
            leftPower = -gamepad.left_stick_y + gamepad.right_stick_x;
        }

        Range.clip(rightPower, -1, 1);
        Range.clip(leftPower, -1, 1);
        setPowerPID(rightPower, leftPower);
    }

    public void addTelemetry(){
        for(int i = 0; i < rightMotors.length; i++){
            telemetry.addData(rightMotorNames[i] + " Power: " , rightMotors[i].getPower());
            telemetry.addData(rightMotorNames[i] + " Encoder Value: ", rightMotors[i].getCurrentPosition());
            telemetry.addData(rightMotorNames[i] + " Is Inverted:", String.valueOf(rightMotors[i].isInverted()));
            telemetry.addData(rightMotorNames[i] + " Port Number: ", rightMotors[i].getPortNumber());
            telemetry.addData("//////////////", "//////////////");
        }

        for(int i = 0; i < leftMotors.length; i++){
            telemetry.addData(leftMotorNames[i] + " Power: " , leftMotors[i].getPower());
            telemetry.addData(leftMotorNames[i] + " Encoder Value: ", leftMotors[i].getCurrentPosition());
            telemetry.addData(leftMotorNames[i] + " Is Inverted:", String.valueOf(leftMotors[i].isInverted()));
            telemetry.addData(leftMotorNames[i] + " Port Number: ", leftMotors[i].getPortNumber());
            telemetry.addData("//////////////", "//////////////");
        }

        telemetry.update();
    }

    public String proccessText(String text){
        switch (text) {
            case "get inverted":
                String invertedString = null;
                for (int i = 0; i < rightMotors.length; i++) {
                    if (rightMotors[i].isInverted()) {
                        invertedString += rightMotorNames[i] + " is inverted\n";
                    }
                }
                for (int i = 0; i < leftMotors.length; i++) {
                    if (leftMotors[i].isInverted()) {
                        invertedString += leftMotorNames[i] + " is inverted\n";
                    }
                }
                if (invertedString == null) {
                    return "No motors on the drive train are inverted";
                } else {
                    return invertedString;
                }

            case "get power":
                String powerString = null;
                for (int i = 0; i < rightMotors.length; i++) {
                    powerString += rightMotorNames[i] + " motor power: " + rightMotors[i].getPower();
                }
                for (int i = 0; i < leftMotors.length; i++) {
                    powerString += leftMotorNames[i] + " motor power: " + rightMotors[i].getPower();
                }
                return powerString;

            case "get zero behavior":
                String zeroString = null;
                for (int i = 0; i < rightMotors.length; i++) {
                    zeroString += rightMotorNames[i] + " " + rightMotors[i].getZeroPowerBehaviorAsString() + "\n";
                }
                for (int i = 0; i < leftMotors.length; i++) {
                    zeroString += leftMotorNames[i] + " " + leftMotors[i].getZeroPowerBehaviorAsString() + "\n";
                }
                return zeroString;
            default:
                return "Could not register command.";
        }
    }

}
