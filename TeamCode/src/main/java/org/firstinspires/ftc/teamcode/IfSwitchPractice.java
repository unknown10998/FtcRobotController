package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class IfSwitchPractice extends OpMode {
    @Override
    public void init() {

    }

    @Override
    public void loop() {
        float restingZone = 0.05f;
        //telemetry.addData("Left Joystick State: ", "x: %s y: %s", gamepad1.left_stick_x >= restingZone ? gamepad1.left_stick_x >= restingZone ? "RIGHT" : "RESTING": "LEFT", gamepad1.left_stick_y >= 0 ? gamepad1.left_stick_y >= restingZone ? "DOWN" : "RESTING": "UP");
        float ySpeed = gamepad1.left_stick_y;
        ySpeed += (-gamepad1.left_trigger + gamepad1.right_trigger);
        telemetry.addData("YSpeed", ySpeed);
        telemetry.update();
    }
}




















