import javafx.animation.*;
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
  private final int OFFSET = 62; //used to center "life cube" on the axis

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

  private Cell[][][] cell = new Cell[32][32][32];
  private boolean[][][] cellFuture = new boolean[32][32][32];
  private int r1, r2, r3, r4;
  private boolean isMegaCellClear = true;
  private int frame = 60;

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

  void clearMegaCell()
  {
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          root.getChildren().remove(cell[x][y][z]);
          cell[x][y][z] = null;
          cellFuture[x][y][z] = false;
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
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          if (random.nextBoolean()) addNewCell(x, y, z);
          else cell[x][y][z] = null;
        }
      }
    }
  }

  /**
   * Will create approximately given number of cells to be alive, is not exact as ranom could land on already created cell;
   *
   * @param aliveCells cells to be created
   */
  void randomCellsToLife(int aliveCells)
  {
    isMegaCellClear = false;
    int x, y, z;
    for (int i = 0; i < aliveCells; i++)
    {
      x = random.nextInt(30) + 1;
      y = random.nextInt(30) + 1;
      z = random.nextInt(30) + 1;
      cell[x][y][z] = new Cell();

      cell[x][y][z].setTranslateX(x * cell[x][y][z].getBoxSize() - OFFSET);
      cell[x][y][z].setTranslateY(y * cell[x][y][z].getBoxSize() - OFFSET);
      cell[x][y][z].setTranslateZ(z * cell[x][y][z].getBoxSize() - OFFSET);
      cell[x][y][z].setX(x);
      cell[x][y][z].setY(y);
      cell[x][y][z].setZ(z);

      root.getChildren().add(cell[x][y][z]);
    }
  }

  /**
   * will create a cell mega structure in a staircase shape
   */
  void createPreset1()
  {
    int zLim = 31;
    int xLim = 0;
    isMegaCellClear = false;
    for (int y = 30; y > 0; y--)
    {
      for (int x = 30; x > xLim; x--)
      {
        for (int z = 1; z < zLim; z++)
        {
          addNewCell(x, y, z);
        }
        zLim--;
      }
      xLim++;
      zLim = (y);
    }
  }

  /**
   *life structure outline
   */
  void createPreset2()
  {
    for(int i = 1; i < 30; i++)
    {
      addNewCell(i, 1, 1);
      addNewCell(i, 1, 30);
      addNewCell(i, 30, 1);
      addNewCell(i, 30, 30);

      addNewCell(1, i, 1);
      addNewCell(1, i, 30);
      addNewCell(30, i, 30);
      addNewCell(30, i, 1);

      addNewCell(1, 1, i);
      addNewCell(1, 30, i);
      addNewCell(30, 1, i);
      addNewCell(30, 30, i);
    }
  }

  /**
   * small cube with missing corner
   */
  void createPreset3()
  {
    addNewCell(14, 14, 14);
    addNewCell(15, 14, 14);
    addNewCell(14, 15, 14);
    addNewCell(14, 14, 15);
    addNewCell(15, 14, 15);
    addNewCell(14, 15, 15);
  }

  void setR1(int r1)
  {
    this.r1 = r1;
  }

  void setR2(int r2)
  {
    this.r2 = r2;
  }

  void setR3(int r3)
  {
    this.r3 = r3;
  }

  void setR4(int r4)
  {
    this.r4 = r4;
  }

  void setRs(int r1, int r2, int r3, int r4)
  {
    this.r1 = r1;
    this.r2 = r2;
    this.r3 = r3;
    this.r4 = r4;
  }

  /**
   * has the actual animation loop which will check neighbors of each cell and check whether it should live/die
   */
  void startGame()
  {
    Transition timer = new Transition()
    {
      {
        setCycleDuration(Duration.INDEFINITE);
      }

      @Override
      protected void interpolate(double frac)
      {
        if (frame == 60)
        {
          clearBooleanCells();
          decideLife();
          frame = 0;
        }
        frame++;
        animateLife(frame);
      }
    };
    timer.play();
  }

  /**
   * create a new cell in 3D coordinates from given position and will draw it
   *
   * @param x x-coordinate in 3D structure
   * @param y y-coordinate in 3D structure
   * @param z z-coordinate in 3D structure
   */
  private void addNewCell(int x, int y, int z)
  {
    cell[x][y][z] = new Cell();

    cell[x][y][z].setTranslateX(x * cell[x][y][z].getBoxSize() - OFFSET);
    cell[x][y][z].setTranslateY(y * cell[x][y][z].getBoxSize() - OFFSET);
    cell[x][y][z].setTranslateZ(z * cell[x][y][z].getBoxSize() - OFFSET);
    cell[x][y][z].setX(x);
    cell[x][y][z].setY(y);
    cell[x][y][z].setZ(z);

    root.getChildren().add(cell[x][y][z]);
  }

  /**
   * will create/kill cell when time and does animation
   *
   * @param frame current frame from fps
   */
  private void animateLife(int frame)
  {
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          //case to see if it should be a new cell
          if (cellFuture[x][y][z] && cell[x][y][z] == null)
          {
            if (frame == 1)
            {
              addNewCell(x, y, z);
            }
            cell[x][y][z].setBoxSize(frame / 60);
          }
          else if (!cellFuture[x][y][z] && cell[x][y][z] != null)
          {
            float size = cell[x][y][z].getBoxSize();
            cell[x][y][z].setColor(false);
            cell[x][y][z].setBoxSize(size / frame);
            if (frame == 59)
            {
              root.getChildren().remove(cell[x][y][z]);
              cell[x][y][z] = null;
            }
          }
        }
      }
    }
  }

  /**
   * builds camera 220 away and a FOV of 60 degrees
   */
  private void buildCamera()
  {
    camera = new PerspectiveCamera(true);
    subscene.setCamera(camera);
    cameraGroup.getChildren().add(camera);
    root.getChildren().add(cameraGroup);
    camera.setFieldOfView(60);
    camera.setTranslateZ(-220);
    camera.setFarClip(500);
    camera.setDepthTest(DepthTest.ENABLE);
  }

  /**
   * will set all of cellFuture 3D boolean array to false
   */
  private void clearBooleanCells()
  {
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          cellFuture[x][y][z] = false;
        }
      }
    }
  }

  /**
   * iterates through entire 3D array checking each cell calling decideCellLife
   */
  private void decideLife()
  {
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          decideCellLife(x, y, z);
        }
      }
    }
  }

  /**
   * helper method to check if cell stays alive by checking all 26 neighbors
   *
   * @param currX current cell x
   * @param currY current cell y
   * @param currZ current cell z
   */
  private void decideCellLife(int currX, int currY, int currZ)
  {
    int aliveNeighbors = 0;
    for (int y = -1; y < 2; y++)
    {
      for (int x = -1; x < 2; x++)
      {
        for (int z = -1; z < 2; z++)
        {
          if (x == 0 && y == 0 && z == 0) continue;

          if (cell[currX + x][currY + y][currZ + z] != null) aliveNeighbors++;
        }
      }
    }
    if (aliveNeighbors >= r1 && aliveNeighbors <= r2) cellFuture[currX][currY][currZ] = true;
  }

  /**
   * sets InputHandler to be called on any action in GUI
   */
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
   * sets up stage GUI (i.e. UI on top, and 3D model under it)
   */
  private void setupLayout()
  {
    //ComboBoxes
    ObservableList<String> dropDownList = FXCollections.observableArrayList("n Cells Alive", "Random Cells", "Preset 1", "Preset 2", "Preset 3");
    ObservableList<Integer> rNeighbor = FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);
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

  /**
   * Called to rotate cells about the Y axis
   *
   * @param timeline timeline object
   */
  private void startAutoRotation(Timeline timeline)
  {
    Rotate rotate = new Rotate(0, 0, 0);

    cameraGroup.getTransforms().add(rotate);
    timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(14), new KeyValue(rotate.angleProperty(), 360)));
    timeline.setCycleCount(Animation.INDEFINITE);
    rotate.setAxis(Rotate.Y_AXIS);
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
    startAutoRotation(timeline);

    setEventHandler();

    primaryStage.setScene(scene);
    primaryStage.setTitle("The Game of Life");
    primaryStage.show();
  }
}