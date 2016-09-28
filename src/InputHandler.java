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


  private String selectedSet;
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
      }
      else
      {
        startButton.setText("Start");
      }
      game.startGame(timeline);
    }
    else if(source == dropDown)
    {
      selectedSet = dropDown.getValue().toString();
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
    else if (source == textField)
    {
      System.out.println(textField.getText());
      game.setNumDeadCell(Integer.parseInt(textField.getText()));
      textField.clear();
      if(selectedSet.equals("n Cells Alive")) startButton.setDisable(false);
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
