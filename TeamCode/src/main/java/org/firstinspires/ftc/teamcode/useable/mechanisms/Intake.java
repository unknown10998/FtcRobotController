package org.firstinspires.ftc.teamcode.useable.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    private DcMotor intakeMotor;
    private Servo intakeServo;

    private final double INTAKE_UP = 0.15;
    private final double INTAKE_DOWN = 0.75;

    public void init(HardwareMap hwMap) {
        intakeMotor = hwMap.get(DcMotor.class, "intake_motor");
        intakeServo = hwMap.get(Servo.class, "intake_servo");

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void intakeMotor(double power) {
        intakeMotor.setPower(power);
    }

    public void stop() {
        intakeMotor.setPower(0.0);
    }

    public void deployServo() {
        intakeServo.setPosition(INTAKE_DOWN);
    }

    public void stowServo() {
        intakeServo.setPosition(INTAKE_UP);
    }

    public double getMotorPower() {
        return intakeMotor.getPower();
    }

    public double getServoPosition() {
        return intakeServo.getPosition();
    }
}
