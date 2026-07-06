package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class TestBench {
    private DigitalChannel touchSensor;
    private DcMotor motor;
    private DistanceSensor distance;
    private Servo posServo;
    private CRServo rotServo;
    private double ticksPerRev;

    public void init(HardwareMap hwMap){
        touchSensor = hwMap.get(DigitalChannel.class, "touch_sensor");
        touchSensor.setMode(DigitalChannel.Mode.INPUT);

        motor = hwMap.get(DcMotor.class, "motor");
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER); //RUN_TO_POSITION, w/ encoder to tick, RUN_USING_ENCODER, w/ encoder run to targeted speed, RUN_WITHOUT_ENCODER w/o encoder same thing but no verfication
        ticksPerRev = motor.getMotorType().getTicksPerRev();
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); //BRAKE actually tries, FLOAT just cuts power w/ floating
//        motor.setDirection(DcMotorSimple.Direction.REVERSE); //FORWARD or REVERSE

        distance = hwMap.get(DistanceSensor.class, "distance_sensor");

        posServo = hwMap.get(Servo.class, "pos_servo");
        rotServo = hwMap.get(CRServo.class, "rot_servo");
        posServo.scaleRange(0.5, 1.0);
//        posServo.setDirection(Servo.Direction.REVERSE);
//        rotServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    //-------------------------------------------

    public boolean isTouchSensorPressed(){
        return !touchSensor.getState();
    }

    public boolean isTouchSensorReleased(){
        return touchSensor.getState();
    }

    //-------------------------------------------

    public void setMotorSpeed(double speed){
        //speed -1 to 1
        motor.setPower(speed);
    }

    public double getMotorRevs(){
        return motor.getCurrentPosition() / ticksPerRev;
    }

    public void setMotorBrakeType(DcMotor.ZeroPowerBehavior zeroBehaviour){
        motor.setZeroPowerBehavior(zeroBehaviour);
    }

    //-------------------------------------------

    public double getDistance(){
        return distance.getDistance(DistanceUnit.CM);
    }

    //-------------------------------------------

    public void setServoPos(double angle){
        posServo.setPosition(angle);
    }

    public void setServoRot(double speed){
        rotServo.setPower(speed);
    }
}
