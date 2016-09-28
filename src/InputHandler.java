import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
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
  private Timeline timeline;

  private Object source;

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
    timeline = game.getTimeline();
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
      if (rotateButton.getText().equals("Rotate: On")) rotateButton.setText("Rotate: Off");
      else rotateButton.setText("Rotate: On");
      game.startAutoRotation(timeline, rotateButton.getText());
    }
    else if (source == startButton)
    {
      if (startButton.getText().equals("Start")) startButton.setText("Pause");
      else startButton.setText("Start");
      game.startGame(timeline);
    }
  }

  private void handleKeyEvent(KeyEvent event)
  {

  }

  private void handleMouse(MouseEvent event)
  {
    System.out.println(event.getEventType());
    System.out.println(event.getTarget());

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
