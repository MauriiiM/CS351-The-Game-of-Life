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
 * @author Mauricio Monsivais
 */
public class InputHandler implements EventHandler
{
  private TheGameOfLife game;
  private SubScene subscene;
  private PerspectiveCamera camera;
  private Button startButton;
  private Button rotateButton;
  private ComboBox dropDown, r1DropDown,r2DropDown,r3DropDown,r4DropDown;
  private TextField textField;
  private Timeline timeline;

  private Object source;


  private String currentSet = "";
  private String selectedSet = "";
  private int r1 = 0;
  private int r2 = 0;
  private int r3 = 0;
  private int r4 = 0;
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
        timeline.pause();
      }
      else
      {
        rotateButton.setText("Rotate: Off");
        timeline.play();
      }
    }
    else if (source == startButton)
    {
      if (startButton.getText().equals("Start"))
      {
        startButton.setText("Pause");
        if (!currentSet.equals(selectedSet)) //want to switch to different cell set
        {
          if (!game.isMegaCellClear())
          {
            System.out.println("Clearing Cell Structure");
            game.clearMegaCell();
          }

          currentSet = selectedSet;
          switch (selectedSet)
          {
            case "Random Cells":
              System.out.println("creating Random Cells");
              game.createRandomCells();
              break;
            case "n Cells Alive":
              System.out.println("creating n Random Cells");
              game.createDeadCells();
              game.randomCellsToLife();
              break;
            case "Preset 1":
              System.out.println("creating preset 1");
//            game.createPreset1();
              break;
            case "Preset 2":
              System.out.println("creating preset 2");
//            game.createPreset2();
              break;
            case "Preset 3":
              System.out.println("creating preset 3");
//            game.createPreset3();
              break;
          }
        }
      }
      else
      {
        startButton.setText("Start");
      }
      game.startGame();
    }
    else if (source == textField)
    {
      System.out.println(textField.getText());
      game.setNumDeadCell(Integer.parseInt(textField.getText()));
      if (selectedSet.equals("n Cells Alive")) startButton.setDisable(false);
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
    }
    else if (source == r1DropDown) r1 = Integer.parseInt(r1DropDown.getValue().toString());
    else if (source == r2DropDown) r2 = Integer.parseInt(r2DropDown.getValue().toString());
    else if (source == r3DropDown) r3 = Integer.parseInt(r3DropDown.getValue().toString());
    else if (source == r4DropDown) r4 = Integer.parseInt(r4DropDown.getValue().toString());
    game.setRs(r1,r2,r3,r4);
  }

  private void handleKeyEvent(KeyEvent event)
  {

  }

  private void handleMouse(MouseEvent event)
  {
    if (event.isPrimaryButtonDown())
    {
      subscene.cursorProperty().setValue(Cursor.CLOSED_HAND);

      mouseOldX = event.getSceneX();
      mouseOldY = event.getSceneY();
      System.out.println("mouseX = " + mouseOldX + "\tmouseY = " + mouseOldY);

      if (event.getTarget() instanceof Cell)
      {
        System.out.println("IT'S A CELL");
      }
    }
    else if (!event.isPrimaryButtonDown())
    {
      subscene.cursorProperty().setValue(Cursor.DEFAULT);
    }
  }
}
