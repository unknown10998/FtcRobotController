package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class OOPMemberPractice extends OpMode {

    boolean initDone = false;


    private double squareWithSign(double input){
        return input * Math.abs(input);
    }

    @Override
    public void init() {
        telemetry.addData("init", initDone);
        initDone = true;
    }

    @Override
    public void loop() {
        telemetry.addData("init", initDone);

        double yAxis = gamepad1.left_stick_y;

        telemetry.addData("yAxis", yAxis);

        yAxis = squareWithSign(yAxis);

        telemetry.addData("yAxis", yAxis);
        telemetry.update();
    }
}
