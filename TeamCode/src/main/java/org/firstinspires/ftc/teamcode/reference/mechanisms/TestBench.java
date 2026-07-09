package org.firstinspires.ftc.teamcode.reference.mechanisms;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.concurrent.RecursiveAction;

public class TestBench {
    private DigitalChannel touchSensor;
    private DcMotor motor;
    private DistanceSensor distance;
    private Servo posServo;
    private CRServo rotServo;
    private NormalizedColorSensor colorSensor;
    private LED redLED;
    private LED greenLED;
    private IMU imu;
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

        colorSensor = hwMap.get(NormalizedColorSensor.class, "color_sensor");
        colorSensor.setGain(1); // Color multiplier
//        posServo.setDirection(Servo.Direction.REVERSE);
//        rotServo.setDirection(DcMotorSimple.Direction.REVERSE);

        imu = hwMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD)));

        redLED = hwMap.get(LED.class, "red_led");
        greenLED = hwMap.get(LED.class, "green_led");
    }

    //-------------------------------------------


    public enum DetectedColor{
        RED,
        BLUE,
        GREEN,
        UNKNOWN
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

    //-------------------------------------------

    public DetectedColor getDetectedColor(Telemetry telemetry){
        NormalizedRGBA colors = colorSensor.getNormalizedColors(); // return 4 values, red. bul, gree, transparency

        float nRed = colors.red / colors.alpha, nBlue = colors.blue / colors.alpha, nGreen = colors.green / colors.alpha;

        telemetry.addData("Red", nRed);
        telemetry.addData("Blue", nBlue);
        telemetry.addData("Green", nGreen);
        telemetry.update();

        if (nRed > 0.7 && nGreen < 0.3 && nBlue < 0.3) return DetectedColor.RED;
        else if (nRed < 0.3 && nGreen > 0.7 && nBlue < 0.3) return DetectedColor.GREEN;
        else if (nRed < 0.3 && nGreen < 0.3 && nBlue > 0.7) return DetectedColor.BLUE;
        return DetectedColor.UNKNOWN;
    }

    //-------------------------------------------

    public double getHeading(AngleUnit unit){
        return imu.getRobotYawPitchRollAngles().getYaw(unit);
    }

    //-------------------------------------------

    public void setRedLED(boolean isOn){
        if (isOn) redLED.on();
        else redLED.off();
    }

    public void setGreenLED(boolean isOn){
        if (isOn) greenLED.on();
        else greenLED.off();
    }

}
