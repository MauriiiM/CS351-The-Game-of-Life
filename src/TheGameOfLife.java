import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import com.sun.org.apache.xpath.internal.SourceTree;
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
  private double camDistance = -220;

  private Cell[][][] cell = new Cell[32][32][32];
  private boolean[][][] shoudBeAlive = new boolean[32][32][32];
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
    for (int y = 0; y < 31; y++)
    {
      for (int x = 0; x < 31; x++)
      {
        for (int z = 0; z < 31; z++)
        {
          cell[x][y][z].setDead();
          shoudBeAlive[x][y][z] = false;
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
          if (random.nextBoolean())
          {
            addNewCell(x, y, z);
          }
          else
          {
            cell[x][y][z].setDead();
          }
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
      cell[x][y][z].setAlive();

      cell[x][y][z].setTranslateX(x * cell[x][y][z].getBoxSize() - OFFSET);
      cell[x][y][z].setTranslateY(y * cell[x][y][z].getBoxSize() - OFFSET);
      cell[x][y][z].setTranslateZ(z * cell[x][y][z].getBoxSize() - OFFSET);
      cell[x][y][z].setX(x);
      cell[x][y][z].setY(y);
      cell[x][y][z].setZ(z);
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
    setRPreset();

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
   * life structure outline
   */
  void createPreset2()
  {
    for (int i = 1; i < 31; i++)
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

  void createPreset4()
  {
    r1 = 1;
    r2 = 3;
    r3 = 1;
    r4 = 4;
    setRPreset();
    addNewCell(15, 15, 14);
    addNewCell(15, 15, 15);
    addNewCell(15, 15, 16);
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
          decideLife();

        animateLife(frame);
          frame = 0;
        }
        frame++;
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
    cell[x][y][z].setAlive();
    shoudBeAlive[x][y][z] = true;

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
          if (shoudBeAlive[x][y][z] && !cell[x][y][z].isAlive())
          {
//            ScaleTransition st = new ScaleTransition(Duration.millis(1000), cell[x][y][z]);
//            st.setByX(1.5);
//            st.setByY(1.5f);
//            st.setByZ(1.5);
//            st.setAutoReverse(true);
//
//            st.play();
            cell[x][y][z].setColor(true);
            cell[x][y][z].setBoxSize(4 * frame / 59);
            if (cell[x][y][z].getBoxSize() == 4) cell[x][y][z].setAlive();

          }
          else if (!shoudBeAlive[x][y][z] && cell[x][y][z].isAlive())
          {
            cell[x][y][z].setColor(false);
            cell[x][y][z].setBoxSize(4 - (4 * frame / 59));

            if (cell[x][y][z].getBoxSize() == 0) cell[x][y][z].setDead();
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
    camera.setTranslateY(-140);
    camera.setRotationAxis(Rotate.X_AXIS);
    camera.setRotate(-35);
    camera.setFieldOfView(60);
    camera.setTranslateZ(camDistance);
    camera.setFarClip(500);
    camera.setDepthTest(DepthTest.ENABLE);
  }

  private void handleZoom()
  {
    scene.setOnScroll(event ->
    {
      if (event.getDeltaY() < 0) camDistance *= 1.01;
      else camDistance *= .99;
      camera.setTranslateZ(camDistance);
    });
  }

  private void createLife()
  {
    for (int y = 0; y < 32; y++)
    {
      for (int x = 0; x < 32; x++)
      {
        for (int z = 0; z < 32; z++)
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
      }
    }
  }

  /**
   * iterates through entire 3D array checking each cell calling decideCellLife
   */
  private void decideLife()
  {
    transferLife();
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

          if (cell[currX + x][currY + y][currZ + z].isAlive()) aliveNeighbors++;
        }
      }
    }
    if (aliveNeighbors >= r1 && aliveNeighbors <= r2)
    {
      shoudBeAlive[currX][currY][currZ] = true;
    }
    else if (aliveNeighbors > r3 || aliveNeighbors < r4) shoudBeAlive[currX][currY][currZ] = false;
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
    handleZoom();
  }

  private void setRPreset()
  {
    r1dropDown.setPromptText("r1 = " + r1);
    r2dropDown.setPromptText("r2 = " + r2);
    r3dropDown.setPromptText("r3 = " + r3);
    r4dropDown.setPromptText("r4 = " + r4);
  }

  /**
   * sets up stage GUI (i.e. UI on top, and 3D model under it)
   */
  private void setupLayout()
  {
    //ComboBoxes
    ObservableList<String> dropDownList = FXCollections.observableArrayList("n Cells Alive", "Random Cells", "Preset 1", "Preset 2", "Preset 3", "Preset 4");
    ObservableList<Integer> rNeighbor = FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26);
    dropDown = new ComboBox<>(dropDownList);
    dropDown.setPromptText("Cell Structure");
    r1dropDown = new ComboBox<>(rNeighbor);
    r1dropDown.setPromptText("r1=" + r1);
    r2dropDown = new ComboBox<>(rNeighbor);
    r2dropDown.setPromptText("r2=" + r2);
    r3dropDown = new ComboBox<>(rNeighbor);
    r3dropDown.setPromptText("r3=" + r3);
    r4dropDown = new ComboBox<>(rNeighbor);
    r4dropDown.setPromptText("r4=" + r4);

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

  private void transferLife()
  {
    for (int y = 1; y < 31; y++)
    {
      for (int x = 1; x < 31; x++)
      {
        for (int z = 1; z < 31; z++)
        {
          if (shoudBeAlive[x][y][z])
          {
            cell[x][y][z].setAlive();
          }
          else
          {
            cell[x][y][z].setDead();
          }
        }
      }
    }
  }

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    timeline = new Timeline();

    createLife();

    setupLayout();
    buildCamera();
    startAutoRotation(timeline);

    setEventHandler();

    primaryStage.setScene(scene);
    primaryStage.setTitle("The Game of Life");
    primaryStage.show();
  }
}