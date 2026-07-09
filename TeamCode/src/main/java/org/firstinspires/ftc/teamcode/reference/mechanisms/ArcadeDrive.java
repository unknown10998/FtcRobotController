package org.firstinspires.ftc.teamcode.reference.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ArcadeDrive {

    private DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;

    public void init(HardwareMap hwMap){
        frontLeftMotor = hwMap.get(DcMotor.class, "front_left_motor");
        backLeftMotor = hwMap.get(DcMotor.class, "back_left_motor");
        frontRightMotor = hwMap.get(DcMotor.class, "front_right_motor");
        backRightMotor = hwMap.get(DcMotor.class, "back_right_motor");

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void drive(double throttle, double spin){
        double leftPower = throttle + spin;
        double rightPower = throttle - spin;
        double largest = Math.max(Math.abs(leftPower), Math.abs(rightPower));

        //Normalizing to the range -1 to 1.
        if (largest > 1.0){
            leftPower /= largest;
            rightPower /= largest;
        }

        frontLeftMotor.setPower(leftPower);
        frontRightMotor.setPower(rightPower);
        backLeftMotor.setPower(leftPower);
        backRightMotor.setPower(rightPower);

        /*
         * Arcade drive math:
         *
         * throttle = forward/backward movement
         * spin     = left/right turning
         *
         * leftPower  = throttle + spin
         * rightPower = throttle - spin
         *
         * If throttle is positive and spin is 0:
         * leftPower and rightPower are both positive, so the robot drives forward.
         *
         * If throttle is 0 and spin is positive:
         * leftPower becomes positive and rightPower becomes negative, so the robot turns in place.
         *
         * If both throttle and spin are used:
         * one side gets more power than the other, so the robot curves while driving.
         *
         * The largest/math scaling part keeps motor power between -1.0 and 1.0.
         * If the math creates a value bigger than 1.0, both sides are divided by the largest value.
         * This keeps the same left/right power ratio while making the values safe for the motors.
         */
    }

}
