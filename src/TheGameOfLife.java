/**
 * Created by mmons on 9/22/2016.
 */

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class TheGameOfLife extends Application
{
  //groups
  private Scene scene;
  private PerspectiveCamera camera;
  private Group cameraGroup = new Group();
  private Group root = new Group();
  private Group world = new Group();

  private LinearGradient bg;
  private Random random = new Random();
  private MouseHandler mouseHandler;

  private Rotate rotate = new Rotate(0, 0, 0);

  private static final Stop WHITE_END = new Stop(.6, Color.BLACK);
  private static final Stop AQUA = new Stop(0, Color.BLUEVIOLET);

  private Cell[][][] cells = new Cell[32][32][32];

  private void buildCamera()
  {
    camera = new PerspectiveCamera(true);
    scene.setCamera(camera);
    cameraGroup.getChildren().add(camera);
    root.getChildren().add(cameraGroup);
    camera.setFieldOfView(90);
    camera.setTranslateZ(-200);
    camera.setDepthTest(DepthTest.ENABLE);
    cameraGroup.getTransforms().add(rotate);

    System.out.println("FOV = " + camera.getFieldOfView() +
            "\nDepthTest = " + camera.getDepthTest() +
            "\ncameraGroup-Xpos = " + cameraGroup.getTranslateX() +
            "\ncameraGroup-Ypos = " + cameraGroup.getTranslateY() +
            "\ncameraGroup-Zpos = " + cameraGroup.getTranslateZ()+
            "\n\ncamera-Xpos = " + camera.getTranslateX() +
            "\ncamera-Ypos = " + camera.getTranslateY() +
            "\ncamera-Zpos = " + camera.getTranslateZ());
  }

  private void createCells()
  {
    int offset = 93; //used to center "life cube" on the axis
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          cells[x][y][z] = new Cell(random.nextBoolean());
          cells[x][y][z].setTranslateX(x * cells[x][y][z].BOX_WIDTH - offset);
          cells[x][y][z].setTranslateY(y * cells[x][y][z].BOX_WIDTH - offset);
          cells[x][y][z].setTranslateZ(z * cells[x][y][z].BOX_WIDTH - offset);
          cells[x][y][z].setX(x);
          cells[x][y][z].setY(y);
          cells[x][y][z].setZ(z);

          root.getChildren().add(cells[x][y][z]);
        }
      }
    }
  }

  private void startAutoRotation()
  {
    final Timeline rotationAnimation = new Timeline();
    rotationAnimation.getKeyFrames().add(new KeyFrame(Duration.seconds(15), new KeyValue(rotate.angleProperty(), 360)));
    rotationAnimation.setCycleCount(Animation.INDEFINITE);
    rotate.setAxis(Rotate.Y_AXIS);
    rotationAnimation.play();
  }

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    root.getChildren().add(world);
    root.setDepthTest(DepthTest.ENABLE);

    bg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, AQUA, WHITE_END);
    scene = new Scene(root, 1024, 768, true);
    scene.setFill(bg);
    mouseHandler = new MouseHandler(scene, camera);
    scene.addEventHandler(MouseEvent.ANY, mouseHandler);

    buildCamera();
    createCells();
    startAutoRotation();

    primaryStage.setScene(scene);

//    primaryStage.setFullScreen(true);
    primaryStage.setTitle("The Game of Life");
    primaryStage.show();
  }
}
