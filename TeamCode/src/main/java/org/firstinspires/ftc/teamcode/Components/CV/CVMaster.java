package org.firstinspires.ftc.teamcode.Components.CV;

import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.dashboard;
import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.op;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

public class CVMaster {
    private OpenCvWebcam webcam;
    private StickObserverPipeline opencv = null;
    private SleeveObserverPipeline openSleevi = null;
    private boolean isStreaming = false;
    public CVMaster(){
        int cameraMonitorViewId = op.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", op.hardwareMap.appContext.getPackageName());
        webcam = /*OpenCvCameraFactory.getInstance().createWebcam(op.hardwareMap.get(WebcamName.class, "webcam"), cameraMonitorViewId);*/
        OpenCvCameraFactory.getInstance().createWebcam(op.hardwareMap.get(WebcamName.class, "webcam"));
    }
    public boolean isStreaming(){
        return isStreaming;
    }
    public void observeStick(){
        opencv = new StickObserverPipeline();

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                /*
                 * Tell the webcam to start streaming images to us! Note that you must make sure
                 * the resolution you specify is supported by the camera. If it is not, an exception
                 * will be thrown.
                 *
                 * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
                 * supports streaming from the webcam in the uncompressed YUV image format. This means
                 * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
                 * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
                 *
                 * Also, we specify the rotation that the webcam is used in. This is so that the image
                 * from the camera sensor can be rotated such that it is always displayed with the image upright.
                 * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                 * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                 * away from the user.
                 */
                webcam.setPipeline(opencv);
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

                dashboard.startCameraStream(webcam, 5);

            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
        isStreaming = true;
    }
    public void observeSleeve(){
        openSleevi = new SleeveObserverPipeline();

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                /*
                 * Tell the webcam to start streaming images to us! Note that you must make sure
                 * the resolution you specify is supported by the camera. If it is not, an exception
                 * will be thrown.
                 *
                 * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
                 * supports streaming from the webcam in the uncompressed YUV image format. This means
                 * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
                 * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
                 *
                 * Also, we specify the rotation that the webcam is used in. This is so that the image
                 * from the camera sensor can be rotated such that it is always displayed with the image upright.
                 * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                 * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                 * away from the user.
                 */
                webcam.setPipeline(openSleevi);
                webcam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

                dashboard.startCameraStream(webcam, 5);

            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
        isStreaming = true;

    }
    public int getPosition(){
        return openSleevi.getPosition();
    }
    public double centerOfPole(){
        return opencv.centerOfPole();
    }
    public double poleSize(){
        return opencv.poleSize();
    }
    public double[] rotatedPolarCoord(){
        return opencv.poleRotatedPolarCoord();
    }

    public void stopCamera(){
        webcam.stopStreaming();
    }
}