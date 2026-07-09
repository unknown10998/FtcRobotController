package org.firstinspires.ftc;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.reference.mechanisms.TestBench;

@TeleOp
public class ColorSensorPractice extends OpMode {

    TestBench bench = new TestBench();
    TestBench.DetectedColor detectedColor;

    @Override
    public void init() {
        bench.init(hardwareMap);
    }

    @Override
    public void loop() {
        detectedColor = bench.getDetectedColor(telemetry);
        telemetry.addData("Color", ": %s", detectedColor);
        telemetry.update();
    }
}
