package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class Variables extends OpMode {
    @Override
    public void init() {
        int teamNumber = 62957;
        double motorSpeed = 1.25;
        boolean moving = false;
        String teamName = "Robotots";
        int motorAngle = 90;

        telemetry.addData("Team #", teamNumber);
        telemetry.addData("Motor Speed", motorSpeed);
        telemetry.addData("Is Moving", moving);
        telemetry.addData("Team Name", teamName);
        telemetry.addData("Motor Angle", motorAngle);
    }

    @Override
    public void loop() {

    }
}
