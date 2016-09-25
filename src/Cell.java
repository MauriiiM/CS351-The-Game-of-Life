import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.Random;

/**
 * @author Mauricio Monsivais
 */
class Cell extends Box
{
  final int BOX_WIDTH = 20;

  private Box cell;
  private boolean isAlive;
  PhongMaterial color = new PhongMaterial();

  Cell(boolean isAlive)
  {
    this.isAlive = isAlive;
    if (isAlive)
    {
      this.setWidth(BOX_WIDTH);
      this.setDepth(BOX_WIDTH);
      this.setHeight(BOX_WIDTH);
      this.setMaterial(color);
      color.setDiffuseColor(Color.DEEPSKYBLUE);
      color.setSpecularColor(Color.CADETBLUE);
//      this.setOpacity(1);
    }
    else
    {
      this.setWidth(0);
      this.setDepth(0);
      this.setHeight(0);
      this.setMaterial(color);
      color.setDiffuseColor(Color.FIREBRICK);
      color.setSpecularColor(Color.CHOCOLATE);
    }
//    cell.setTranslateX(xMultiplier * BOX_WIDTH/2);
//    cell.setTranslateY(yMultiplier * BOX_WIDTH/2);
//    cell.setTranslateZ(zMultiplier * BOX_WIDTH/2);
  }

  void birthOfCell()
  {

  }


  void deathOfCell()
  {

  }
}
