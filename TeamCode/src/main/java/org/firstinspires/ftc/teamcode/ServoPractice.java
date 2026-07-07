package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.TestBench;

@Disabled
@TeleOp
public class ServoPractice extends OpMode {

    TestBench bench = new TestBench();

    private double leftTrigger, rightTrigger;

    @Override
    public void init() {
        bench.init(hardwareMap);
        leftTrigger = 0.0;
        rightTrigger = 0.0;
    }

    @Override
    public void loop() {
        leftTrigger = gamepad1.left_trigger;
        rightTrigger = gamepad1.right_trigger;
//        if (gamepad1.a) bench.setServoPos(1);
//        else  bench.setServoPos(0.0);
//
//        if (gamepad1.b) bench.setServoRot(1.0);
//        else bench.setMotorSpeed(0.0);
        bench.setServoPos(leftTrigger);
        bench.setServoRot(rightTrigger);
    }
}
