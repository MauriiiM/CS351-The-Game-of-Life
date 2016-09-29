import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.util.Random;

/**
 * @author Mauricio Monsivais
 */
public class TheGameOfLife extends Application
{
  //GUI
  private HBox buttonLayout;
  private Button startButton;
  private Button rotateButton;
  private ComboBox dropDown;
  private TextField textField;
  private ComboBox r1dropDown;
  private ComboBox r2dropDown;
  private ComboBox r3dropDown;
  private ComboBox r4dropDown;

  //groups
  private Scene scene;
  private SubScene subscene;
  private PerspectiveCamera camera;
  private Group root = new Group();
  private Group world = new Group();
  private Group cameraGroup = new Group();

  private Random random = new Random();
  private Timeline timeline;

  private static final Stop WHITE_END = new Stop(.6, Color.BLACK);
  private static final Stop AQUA = new Stop(0, Color.BLUEVIOLET);

  private Cell[][][] cells = new Cell[32][32][32];
  private Cell[][][] cells2 = new Cell[32][32][32];
  private int numDeadCell = 0;
  private int r1, r2, r3, r4;
  private boolean isMegaCellClear = true;

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

  ComboBox getR1dropDown()
  {
    return r1dropDown;
  }

  ComboBox getR2dropDown()
  {
    return r2dropDown;
  }

  ComboBox getR3dropDown()
  {
    return r3dropDown;
  }

  ComboBox getR4dropDown()
  {
    return r4dropDown;
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

  boolean isMegaCellClear()
  {
    return isMegaCellClear;
  }

  void clearMegaCell()
  {
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          cells[x][y][z] = null;
        }
      }
    }
    isMegaCellClear = true;
  }

  /**
   * Creates 30x30x30 structure of cells pseudo-randomly chosen to be dead/alive
   */
  void createRandomCells()
  {
    isMegaCellClear = false;
    int offset = 58; //used to center "life cube" on the axis
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          cells[x][y][z] = new Cell(random.nextBoolean());
          cells2[x][y][z] = new Cell(false);
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

  void createDeadCells()
  {
    int offset = 58; //used to center "life cube" on the axis
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          cells[x][y][z] = new Cell(false);
          cells2[x][y][z] = new Cell(false);
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

  void randomCellsToLife()
  {
    isMegaCellClear = false;
    for (int i = 0; i < numDeadCell; i++)
    {
      cells[random.nextInt(30) + 1][random.nextInt(30) + 1][random.nextInt(30) + 1].setAlive();
    }
  }

  void setNumDeadCell(int amount)
  {
    numDeadCell = amount;
  }

  void setRs(int r1, int r2, int r3, int r4)
  {
    this.r1 = r1;
    this.r2 = r2;
    this.r3 = r3;
    this.r4 = r4;
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
  }

  private void setEventHandler()
  {
    InputHandler inputHandler = new InputHandler(this);
    subscene.addEventHandler(MouseEvent.ANY, inputHandler);
    startButton.setOnAction(inputHandler);
    rotateButton.setOnAction(inputHandler);
    textField.setOnAction(inputHandler);
    dropDown.setOnAction(inputHandler);
    r1dropDown.setOnAction(inputHandler);
    r2dropDown.setOnAction(inputHandler);
    r3dropDown.setOnAction(inputHandler);
    r4dropDown.setOnAction(inputHandler);
  }

  /**
   * sets up stage borders (i.e. UI on top, and 3D model under it)
   */
  private void setupLayout()
  {
    //ComboBoxes
    ObservableList<String> dropDownList = FXCollections.observableArrayList("n Cells Alive", "Random Cells", "Preset 1", "Preset 2", "Preset 3");
    ObservableList<Integer> rNeighbor = FXCollections.observableArrayList(0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27);
    dropDown = new ComboBox<>(dropDownList);
    dropDown.setPromptText("Cell Structure");
    r1dropDown = new ComboBox<>(rNeighbor);
    r1dropDown.setPromptText("r1");
    r2dropDown = new ComboBox<>(rNeighbor);
    r2dropDown.setPromptText("r2");
    r3dropDown = new ComboBox<>(rNeighbor);
    r3dropDown.setPromptText("r3");
    r4dropDown = new ComboBox<>(rNeighbor);
    r4dropDown.setPromptText("r4");

    //textField
    textField = new TextField();
    //makes textfield only accept number
    //http://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
    textField.textProperty().addListener(new ChangeListener<String>()
    {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
      {
        if (!newValue.matches("\\d*"))
        {
          textField.setText(newValue.replaceAll("[^\\d]", ""));
        }
      }
    });
    textField.setPromptText("alive cells [1-10k]");

    //buttons
    buttonLayout = new HBox();
    startButton = new Button("Start");
    rotateButton = new Button("Rotate: Off");
    startButton.setPrefSize(100, 20);
    startButton.setDisable(true);
    rotateButton.setPrefSize(100, 20);
    dropDown.setPrefSize(140, 20);

    //scene setup
    BorderPane borderPane = new BorderPane();
    LinearGradient bg = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, AQUA, WHITE_END);
    scene = new Scene(borderPane, 1080, 742);
    subscene = new SubScene(root, 1080, 700, true, SceneAntialiasing.DISABLED);
    subscene.setFill(Color.DARKVIOLET);

    //hbox setup
    buttonLayout.setPadding(new Insets(8, 12, 8, 12));
    buttonLayout.setSpacing(15);
    buttonLayout.getChildren().addAll(startButton, r1dropDown, r2dropDown, r3dropDown, r4dropDown, rotateButton, dropDown);
    borderPane.setTop(buttonLayout);
    borderPane.setCenter(subscene);
    borderPane.prefHeightProperty().bind(subscene.heightProperty());
    borderPane.prefWidthProperty().bind(subscene.widthProperty());
    root.getChildren().add(world);
    root.setDepthTest(DepthTest.ENABLE);
  }

  private void startAutoRotation(Timeline timeline)
  {
    Rotate rotate = new Rotate(0, 0, 0);

    cameraGroup.getTransforms().add(rotate);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(14), new KeyValue(rotate.angleProperty(), 360)));
    timeline.setCycleCount(Animation.INDEFINITE);
    rotate.setAxis(Rotate.Y_AXIS);
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
    buildCamera();
    startGame(timeline);
    startAutoRotation(timeline);

    setEventHandler();

    primaryStage.setScene(scene);
    primaryStage.setTitle("The Game of Life");
    primaryStage.show();
  }
}