import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

import java.util.Random;

/**
 * @author Mauricio Monsivais
 */
class Cell extends Box
{
  final int BOX_WIDTH = 10;

  private Box cell;
  private boolean isAlive;
  private int x, y, z;
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
      this.setOpacity(0.5);
    }
    else
    {
      this.setWidth(BOX_WIDTH * .1);
      this.setDepth(BOX_WIDTH * .1);
      this.setHeight(BOX_WIDTH * .1);
      this.setMaterial(color);
      color.setDiffuseColor(Color.FIREBRICK);
      color.setSpecularColor(Color.CHOCOLATE);
    }
//    cell.setTranslateX(xMultiplier * BOX_WIDTH/2);
//    cell.setTranslateY(yMultiplier * BOX_WIDTH/2);
//    cell.setTranslateZ(zMultiplier * BOX_WIDTH/2);
  }

  public int getX()
  {
    return x;
  }

  public void setX(int x)
  {
    this.x = x;
  }

  public int getY()
  {
    return y;
  }

  public void setY(int y)
  {
    this.y = y;
  }

  public int getZ()
  {
    return z;
  }

  public void setZ(int z)
  {
    this.z = z;
  }

  void birthOfCell()
  {

  }


  void deathOfCell()
  {

  }
}
