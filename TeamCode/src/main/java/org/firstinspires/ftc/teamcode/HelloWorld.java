package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Autonomous
public class HelloWorld extends OpMode {
    private final String name = "Trung";

    @Override
    public void init(){
        telemetry.addData("Hello", name);
    }

    @Override
    public void loop(){

    }
}
