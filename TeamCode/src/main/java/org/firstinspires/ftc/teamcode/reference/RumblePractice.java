package org.firstinspires.ftc.teamcode.reference;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled
@TeleOp
public class RumblePractice extends OpMode {

//    boolean wasA, isA, wasB, isB;

    private double endGameStart = 0.0;
    boolean isEndGame = false;


    @Override
    public void init() {

    }

    @Override
    public void start(){
        endGameStart = getRuntime() + 90;
    }

    @Override
    public void loop() {
//        isA = gamepad1.a; isB = gamepad1.b;
//
//        if (isA != wasA) gamepad1.rumbleBlips(3);
//        else if (isA != wasA) gamepad1.rumble(1, 2, 100);
//
//        wasA = isA; wasB = isB;
        if (endGameStart >= getRuntime() && !isEndGame){
            gamepad1.rumbleBlips(3);
            isEndGame = true;
        }
    }
}
