package org.firstinspires.ftc.teamcode;

import android.widget.EditText;
import android.widget.TextView;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

/**
 * Created by musta on 6/3/2018.
 */

public class TextProcessor extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();
    private FtcRobotControllerActivity robotController = new FtcRobotControllerActivity();
    private RobotHardware robot = new RobotHardware(this, hardwareMap, telemetry, runtime);
    private TextView outputTextView;

    public TextProcessor(TextView outputTextView){
        this.outputTextView = outputTextView;
    }

    public void processText(String text){

    }


    @Override
    public void runOpMode() throws InterruptedException {
        robotController.textToSpeech("I have gained access to robot systems. You can now pose robot specific requests.");
        robot.init();

        waitForStart();
    }
}
