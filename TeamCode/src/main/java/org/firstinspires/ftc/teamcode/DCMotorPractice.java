package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.mechanisms.TestBench;

@Disabled
@TeleOp
public class DCMotorPractice extends OpMode {

    TestBench bench = new TestBench();

    private void breakMotor(){ bench.setMotorSpeed(0.0); }

    @Override
    public void init() {
        bench.init(hardwareMap);
    }

    @Override
    public void loop() {
        double motorSpeed = gamepad1.right_trigger;

        if (bench.isTouchSensorPressed() && motorSpeed != 0) bench.setMotorSpeed(motorSpeed);
        else breakMotor();
        telemetry.addData("Motor Revs", bench.getMotorRevs());

        if (gamepad1.a) bench.setMotorBrakeType(DcMotor.ZeroPowerBehavior.BRAKE);
        else if (gamepad1.b) bench.setMotorBrakeType(DcMotor.ZeroPowerBehavior.FLOAT);
    }
}
