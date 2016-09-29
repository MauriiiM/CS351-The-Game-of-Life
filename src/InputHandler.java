import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.StrokeLineCap;

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
  private ComboBox dropDown;
  private TextField textField;
  private Timeline timeline;

  private Object source;


  private String currentSet = "";
  private String selectedSet = "";
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
              game.createEmptyCells();
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
      game.startGame(timeline);
    }
    else if (source == dropDown)
    {
      selectedSet = dropDown.getValue().toString();
      if (!currentSet.equals(dropDown)) startButton.setText("Start");
      if (selectedSet.equals("n Cells Alive"))
      {
        startButton.setDisable(true);
        game.getButtonLayout().getChildren().add(textField);
        game.createEmptyCells();
      }
      else
      {
        startButton.setDisable(false);
        game.getButtonLayout().getChildren().remove(textField);
      }
    }
    else if (source == textField)
    {
      System.out.println(textField.getText());
      game.setNumDeadCell(Integer.parseInt(textField.getText()));
      if (selectedSet.equals("n Cells Alive")) startButton.setDisable(false);
    }
  }

  private void handleKeyEvent(KeyEvent event)
  {

  }

  private void handleMouse(MouseEvent event)
  {
    if (((MouseEvent) event).isPrimaryButtonDown())
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
