import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Used to handle all action in the Game of Life
 *
 * @author Mauricio Monsivais
 */
public class InputHandler implements EventHandler
{
  private TheGameOfLife game;
  private SubScene subscene;
  private PerspectiveCamera camera;
  private Button startButton;
  private Button rotateButton;
  private ComboBox dropDown, r1DropDown, r2DropDown, r3DropDown, r4DropDown;
  private TextField textField;
  private Timeline timeline;

  private Object source;

  private String currentSet = "";
  private String selectedSet = "";
  private int inputAliveCells;
  private int r1 = 0;
  private int r2 = 15;
  private int r3 = 16;
  private int r4 = 27;
  double mousePosX;
  double mousePosY;
  double mouseOldX;
  double mouseOldY;
  double mouseDeltaX;
  double mouseDeltaY;

  InputHandler(TheGameOfLife game)
  {
    this.game = game;
    subscene = game.getSubscene();
    camera = game.getCamera();
    startButton = game.getStartButton();
    rotateButton = game.getRotateButton();
    dropDown = game.getDropDown();
    r1DropDown = game.getR1dropDown();
    r2DropDown = game.getR2dropDown();
    r3DropDown = game.getR3dropDown();
    r4DropDown = game.getR4dropDown();
    timeline = game.getTimeline();
    textField = game.getTextField();
  }

  @Override
  public void handle(Event event)
  {
    if (event instanceof MouseEvent)
    {
      handleMouse((MouseEvent) event);
    }
    else if (event instanceof ActionEvent)
    {
      handleAction((ActionEvent) event);
    }
    else if (event instanceof KeyEvent)
    {
      handleKeyEvent((KeyEvent) event);
    }
  }

  private void handleAction(ActionEvent event)
  {
    source = event.getSource();

    if (source == rotateButton)
    {
      if (rotateButton.getText().equals("Rotate: Off"))
      {
        rotateButton.setText("Rotate: On");
        timeline.play();
      }
      else
      {
        rotateButton.setText("Rotate: Off");
        timeline.pause();
      }
    }
    else if (source == startButton)
    {
      if (startButton.getText().equals("Start"))
      {
        startButton.setText("Pause");
        if (!currentSet.equals(selectedSet)) //want to switch to different cell set
        {
          game.setRs(r1, r2, r3, r4); //will set Rs to default if not chosen
          currentSet = selectedSet;

          game.startGame();
        }
      }
      else
      {
        startButton.setText("Start");
      }
    }
    else if (source == textField)
    {
      System.out.println(textField.getText());
      inputAliveCells = (Integer.parseInt(textField.getText()));
      if (selectedSet.equals("n Cells Alive"))
      {
        System.out.println("Clearing Cell Structure");
        game.clearMegaCell();
        System.out.println("creating " + inputAliveCells + " alive cells");
        game.randomCellsToLife(inputAliveCells);
        startButton.setDisable(false);
      }
    }
    else if (source == dropDown)
    {
      selectedSet = dropDown.getValue().toString();
      if (!currentSet.equals(dropDown)) startButton.setText("Start");
      if (selectedSet.equals("n Cells Alive"))
      {
        startButton.setDisable(true);
        game.getButtonLayout().getChildren().add(textField);
      }
      else
      {
        startButton.setDisable(false);
        game.getButtonLayout().getChildren().remove(textField);
      }
      switch (selectedSet)
      {
        case "Random Cells":
          System.out.println("Clearing Cell Structure");
          game.clearMegaCell();
          System.out.println("creating Random Cells");
          game.createRandomCells();
          break;
        case "Preset 1":
          System.out.println("Clearing Cell Structure");
          game.clearMegaCell();
          System.out.println("creating preset 1");
          game.createPreset1();
          break;
        case "Preset 2":
          System.out.println("Clearing Cell Structure");
          game.clearMegaCell();
          System.out.println("creating preset 2");
          game.createPreset2();
          break;
        case "Preset 3":
          System.out.println("Clearing Cell Structure");
          game.clearMegaCell();
          System.out.println("creating preset 3");
          game.createPreset3();
          break;
      }
    }
    else if (source == r1DropDown) game.setR1(r1 = Integer.parseInt(r1DropDown.getValue().toString()));
    else if (source == r2DropDown) game.setR2(r2 = Integer.parseInt(r2DropDown.getValue().toString()));
    else if (source == r3DropDown) game.setR3(r3 = Integer.parseInt(r3DropDown.getValue().toString()));
    else if (source == r4DropDown) game.setR4(r4 = Integer.parseInt(r4DropDown.getValue().toString()));
  }

  private void handleKeyEvent(KeyEvent event)
  {

  }

  private void handleMouse(MouseEvent event)
  {
    if (event.isPrimaryButtonDown())
    {
      event.setDragDetect(true);
      mouseOldX = event.getSceneX();
      mouseOldY = event.getSceneY();
      System.out.println("mouseX = " + mouseOldX + "\tmouseY = " + mouseOldY);

      if (event.getTarget() instanceof Cell)
      {
        subscene.cursorProperty().setValue(Cursor.CLOSED_HAND);
      }
    }
    else if (!event.isPrimaryButtonDown())
    {

      subscene.cursorProperty().setValue(Cursor.DEFAULT);
    }
  }
}
