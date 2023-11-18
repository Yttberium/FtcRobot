package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepLeft {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(1000);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(800), Math.toRadians(800), 10)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 61, Math.toRadians(-90)))
//                                .strafeRight(-24)
//                                .lineToLinearHeading(new Pose2d(36, 37, Math.toRadians(180)))
//                                .waitSeconds(1)
//                                .lineToLinearHeading(new Pose2d(48, 37, Math.toRadians(180)))
//                                .waitSeconds(2)
//                                .lineToLinearHeading(new Pose2d(0, -12, Math.toRadians(180)))
//                                .lineToLinearHeading(new Pose2d(-47, 24, Math.toRadians(180)))
//                                .waitSeconds(2)
//                                .lineToLinearHeading(new Pose2d(0, -12, Math.toRadians(180)))
//                                .lineToLinearHeading(new Pose2d(48, 36, Math.toRadians(180)))
//                                .waitSeconds(2)
                                .forward(5)
                                .strafeLeft(16)
                                .forward(19)
                                .back(20)
                                .strafeLeft(20)
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}