/**
 * Created by mmons on 9/22/2016.
 */

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Observable;
import java.util.Random;

public class TheGameOfLife extends Application
{
  //GUI
  private BorderPane borderPane;
  private HBox buttonLayout;
  private Button startButton;
  private Button rotateButton;
  private ComboBox dropDown;
  private TextField textField;
  private ObservableList<String> dropDownList;

  //groups
  private Scene scene;
  private SubScene subscene;
  private PerspectiveCamera camera;
  private Group root = new Group();
  private Group world = new Group();
  private Group cameraGroup = new Group();

  private LinearGradient bg;
  private Random random = new Random();
  private InputHandler inputHandler;
  private Timeline timeline;

  private static final Stop WHITE_END = new Stop(.6, Color.BLACK);
  private static final Stop AQUA = new Stop(0, Color.BLUEVIOLET);

  private Cell[][][] cells = new Cell[32][32][32];
  private Cell[][][] cells2 = new Cell[32][32][32];
  private int numDeadCell = 0;

  HBox getButtonLayout()
  {
    return buttonLayout;
  }

  PerspectiveCamera getCamera()
  {
    return camera;
  }

  ComboBox getDropDown()
  {
    return dropDown;
  }

  Button getRotateButton()
  {
    return rotateButton;
  }
  
  Button getStartButton()
  {
    return startButton;
  }

  SubScene getSubscene()
  {
    return subscene;
  }

  TextField getTextField()
  {
    return textField;
  }

  Timeline getTimeline()
  {
    return timeline;
  }

  void setNumDeadCell(int amount)
  {
    numDeadCell = amount;
  }

  void startAutoRotation(Timeline timeline)
  {
    Rotate rotate = new Rotate(0, 0, 0);

    cameraGroup.getTransforms().add(rotate);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(14), new KeyValue(rotate.angleProperty(), 360)));
    timeline.setCycleCount(Animation.INDEFINITE);
    rotate.setAxis(Rotate.Y_AXIS);
  }

  void startGame(Timeline timeline)
  {

  }

  private void buildCamera()
  {
    camera = new PerspectiveCamera(true);
    subscene.setCamera(camera);
    cameraGroup.getChildren().add(camera);
    root.getChildren().add(cameraGroup);
    camera.setFieldOfView(70);
    camera.setTranslateZ(-200);
    camera.setFarClip(500);
    camera.setDepthTest(DepthTest.ENABLE);

    System.out.println("FOV = " + camera.getFieldOfView() + "\nDepthTest = " + camera.getDepthTest() + "\ncameraGroup-Xpos = " + cameraGroup.getTranslateX() + "\ncameraGroup-Ypos = " + cameraGroup.getTranslateY() + "\ncameraGroup-Zpos = " + cameraGroup.getTranslateZ() + "\n\ncamera-Xpos = " + camera.getTranslateX() + "\ncamera-Ypos = " + camera.getTranslateY() + "\ncamera-Zpos = " + camera.getTranslateZ());
  }

  /**
   * Creates 30x30x30 structure of cells pseudo-randomly chosen to be dead/alive
   */
  private void createRandomCells()
  {
    int offset = 58; //used to center "life cube" on the axis
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          cells[x][y][z] = new Cell(random.nextBoolean());
          cells2[x][y][z] = new Cell();
          cells[x][y][z].setTranslateX(x * cells[x][y][z].getBoxSide() - offset);
          cells[x][y][z].setTranslateY(y * cells[x][y][z].getBoxSide() - offset);
          cells[x][y][z].setTranslateZ(z * cells[x][y][z].getBoxSide() - offset);
          cells[x][y][z].setX(x);
          cells[x][y][z].setY(y);
          cells[x][y][z].setZ(z);

          root.getChildren().add(cells[x][y][z]);
        }
      }
    }
  }

  /**
   * sets up stage borders (i.e. UI on top, and 3D model under it)
   */
  private void setupLayout()
  {
    borderPane = new BorderPane();
    buttonLayout = new HBox();
    startButton = new Button("Start");
    rotateButton = new Button("Rotate: Off");
    textField = new TextField();
    dropDownList = FXCollections.observableArrayList("Random Cells", "n Cells Alive", "Preset 1", "Preset 2", "Preset 3");
    dropDown = new ComboBox<>(dropDownList);
    bg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, AQUA, WHITE_END);

    //makes textfield only accept number
    //http://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
    textField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (!newValue.matches("\\d*")) {
          textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
      }
    });

    //buttons
    startButton.setPrefSize(100, 20);
    startButton.setDisable(true);
    rotateButton.setPrefSize(100, 20);
    dropDown.setPrefSize(140, 20);

    //scene setup
    scene = new Scene(borderPane, 1080, 800);
    subscene = new SubScene(root, 1080, 700, true, SceneAntialiasing.DISABLED);
    subscene.setFill(Color.DARKVIOLET);

    //hbox setup
    buttonLayout.setPadding(new Insets(15, 12, 15, 12));
    buttonLayout.setSpacing(10);
    buttonLayout.getChildren().addAll(startButton, rotateButton, dropDown);

    borderPane.setTop(buttonLayout);
    borderPane.setCenter(subscene);
    borderPane.prefHeightProperty().bind(subscene.heightProperty());
    borderPane.prefWidthProperty().bind(subscene.widthProperty());
    root.getChildren().add(world);
    root.setDepthTest(DepthTest.ENABLE);
  }

  private Cell[][][] updateCells(Cell[][][] current, Cell[][][] updated)
  {
    int neighbors = 0;
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          if (current[x][y][z] != null) ;
        }
      }
    }
    return updated;
  }

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    timeline = new Timeline();

    setupLayout();
    inputHandler = new InputHandler(this);
    buildCamera();
    createRandomCells();
    startGame(timeline);

    subscene.addEventHandler(MouseEvent.ANY, inputHandler);
    startButton.setOnAction(inputHandler);
    rotateButton.setOnAction(inputHandler);
    textField.setOnAction(inputHandler);
    dropDown.setOnAction(inputHandler);

    startAutoRotation(timeline);

    primaryStage.setScene(scene);
    primaryStage.setTitle("The Game of Life");
    primaryStage.show();
  }
}