import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * @author Mauricio Monsivais
 */
class Cell extends Box
{
  final int BOX_WIDTH = 6;

  private Box cell;
  private boolean isAlive;
  private int x, y, z;
  PhongMaterial color = new PhongMaterial();

  Cell(boolean isAlive)
  {
    this.isAlive = isAlive;
    this.setOpacity(0.6);
    this.setMouseTransparent(true);

    if (isAlive)
    {
      this.setWidth(BOX_WIDTH * .9);
      this.setDepth(BOX_WIDTH * .9);
      this.setHeight(BOX_WIDTH * .9);
      this.setMaterial(color);
      color.setDiffuseColor(Color.DEEPSKYBLUE);
      color.setSpecularColor(Color.CADETBLUE);
    }
    else
    {
      // "*.1 because we want it to be a tenth of the size when dead"
      this.setWidth(BOX_WIDTH * .1);
      this.setDepth(BOX_WIDTH * .1);
      this.setHeight(BOX_WIDTH * .1);
      this.setMaterial(color);
      color.setDiffuseColor(Color.FIREBRICK);
      color.setSpecularColor(Color.CHOCOLATE);
    }
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
