package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.mechanisms.TestBench;

@Disabled
@TeleOp
public class TouchSensorPractice extends OpMode {
    private final TestBench bench = new TestBench();

    @Override
    public void init() {
        bench.init(hardwareMap);
    }

    @Override
    public void loop(){
        boolean tsPressed = bench.isTouchSensorPressed();
        boolean tsReleased = bench.isTouchSensorReleased();
        telemetry.addData("Touch Sensor Pressed State", tsPressed);
        telemetry.addData("Touch Sensor Released State", tsReleased);
        if (tsPressed && !tsReleased) telemetry.addData("pressed?", "pressed");
        else telemetry.addData("pressed?", "not pressed");
        telemetry.update();
    }
}
