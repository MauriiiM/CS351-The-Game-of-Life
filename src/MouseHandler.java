import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * @author Mauricio Monsivais
 */
public class MouseHandler implements EventHandler<MouseEvent>
{
  double mousePosX;
  double mousePosY;
  double mouseOldX;
  double mouseOldY;
  double mouseDeltaX;
  double mouseDeltaY;

  Scene scene;

  public MouseHandler(Scene scene)
  {
    this.scene = scene;
  }

  @Override
  public void handle(MouseEvent event)
  {
    if (event.isPrimaryButtonDown())
    {
      System.out.println("CLICKED");
      mousePosX = event.getSceneX();
      mousePosY = event.getSceneY();
      mouseOldX = event.getSceneX();
      mouseOldY = event.getSceneY();
    }
    else if (event.isPrimaryButtonDown() && event.isDragDetect())
    {
      System.out.println("DRAGGED");
      mousePosX = event.getSceneX();
      mousePosY = event.getSceneY();
      mouseOldX = mousePosX;
      mouseOldY = mousePosY;
      mouseDeltaX = (mousePosX - mouseOldX);
      mouseDeltaY = (mousePosY - mouseOldY);
    }
  }

  public void handleClick(MouseEvent event)
  {

  }
}
