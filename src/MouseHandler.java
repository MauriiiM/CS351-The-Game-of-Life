import javafx.event.EventHandler;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * @author Mauricio Monsivais
 */
public class MouseHandler implements EventHandler<MouseEvent>
{
  private Scene scene;
  private PerspectiveCamera camera;

  MouseHandler(Scene scene, PerspectiveCamera camera)
  {
    this.scene = scene;
    this.camera = camera;
  }

  @Override
  public void handle(MouseEvent event)
  {
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    System.out.println(event.getEventType());


    if (event.isPrimaryButtonDown())
    {
      mousePosX = event.getSceneX();
      mousePosY = event.getSceneY();
      mouseOldX = event.getSceneX();
      mouseOldY = event.getSceneY();
      System.out.println("mouseX = " + mouseOldX + "\tmouseY = " + mouseOldY);
    }
//    else if (scene.is)
//    {
//      mousePosX = event.getSceneX();
//      mousePosY = event.getSceneY();
//      mouseOldX = mousePosX;
//      mouseOldY = mousePosY;
//      mouseDeltaX = (mousePosX - mouseOldX);
//      mouseDeltaY = (mousePosY - mouseOldY);
//      System.out.println("mouseDeltaX = " + mouseDeltaX + "\tmouseDeltaY = " + mouseDeltaY);
//
//      camera.setRotate(mouseDeltaX);
//    }
  }

  public void handleClick(MouseEvent event)
  {

  }
}
