package it.unibo.checker

/*
 * This Java source file was generated by the Gradle 'init' task.
 */

import it.unibo.artificial_vision_tracking
import it.unibo.artificial_vision_tracking.aruco_markers.*
import org.bytedeco.javacpp.Loader
import org.bytedeco.javacv.FrameGrabber
import org.bytedeco.opencv.opencv_java
import org.opencv.core.Mat
import org.opencv.objdetect.Objdetect

import java.util
import java.util.{ArrayList, List}

/**
 * This class is used to test the CameraPose class.
 * It calculates the pose of the camera and then captures the positioning of the camera.
 */
object CameraAppCheck {
  @throws[FrameGrabber.Exception]
  @throws[InterruptedException]
  def main(args: Array[String]): Unit = {
    val markersX = 11 // Numero di marker sull'asse X
    val markersY = 8 // Numero di marker sull'asse Y
    val markerLength = 0.09f // Lunghezza del marker (in metri)
    val selectedCamera = 4 // change this to the camera you want to use
    val dictionaryType = Objdetect.DICT_4X4_100
    val cameraParam = new util.ArrayList[Mat]
    var cameraMatrix = new Mat
    var distCoeffs = new Mat
    cameraMatrix = new Mat(3, 3, org.opencv.core.CvType.CV_64F)
    val data: Array[Double] =
      Array[Double](1340.821804232236, 0, 945.5377637384079, 0, 1339.251046705548, 581.4177912549047, 0, 0, 1)
    cameraMatrix.put(0, 0, data: _*)
    distCoeffs = new Mat(1, 5, org.opencv.core.CvType.CV_64F)
    val data2: Array[Double] = Array[Double](-0.3898373600798533, 0.08115247413122996, -1.965974706520358e-05,
      -0.0006330161088470909, 0.1140937797457088)
    distCoeffs.put(0, 0, data2: _*)
    cameraParam.add(cameraMatrix)
    cameraParam.add(distCoeffs)
    // val cc: CameraCalibrator = new CameraCalibrator(markersX, markersY, "calibration")
    // val cameraParam: List[Mat] = cc.calibration()
    val cp = new CameraPose(cameraParam.get(0), cameraParam.get(1), markerLength, dictionaryType, selectedCamera)
    cp.calcPose();
    val camera = cp.getCamera
    var passedTime = 0
    var ticks = 0
    while (true) {
      val time = System.currentTimeMillis.toInt
      val result = cp.capturePositioning(camera)
      val time2 = System.currentTimeMillis.toInt
      passedTime += time2 - time
      ticks += 1
      if (passedTime > 1000) {
        println("FPS: " + ticks)
        ticks = 0
        passedTime = 0
      }
    }
    // Test to calculate the pose of a single frame
    /*VideoCapture capture = cp.getCamera();
            long startTime = System.currentTimeMillis();
            int i = 0;
            //A FRAME LIMITER MAY BE REQUIRED (not sure about this)
            while(i < 100){
                System.out.println("\n" + cp.calcSinglePose(capture)[0].dump() + "\n");
                i++;
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Avg frame time: " + (endTime - startTime) / i + "ms");
     */
    // Test of the RobotScreenSaver
    // RobotScreenSaver.screenSaver(cameraParam.get(0), cameraParam.get(1), markerLength, dictionary, selectedCamera);
  }

  try Loader.load(classOf[opencv_java])

}
