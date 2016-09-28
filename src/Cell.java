import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * @author Mauricio Monsivais
 */
class Cell extends Box
{
  private Box cell;
  private boolean isAlive;
  private int x, y, z;
  private float boxSide = 4;
  PhongMaterial color = new PhongMaterial();

  Cell()
  {

  }

  Cell(boolean isAlive)
  {
    this.isAlive = isAlive;

    if (isAlive)
    {
      this.setWidth(boxSide * .9);
      this.setDepth(boxSide * .9);
      this.setHeight(boxSide * .9);
      this.setMaterial(color);
      color.setDiffuseColor(Color.DEEPSKYBLUE);
      color.setSpecularColor(Color.CADETBLUE);
    }
    else
    {
      // "*.1 because we want it to be a tenth of the size when dead"
      this.setWidth(boxSide * .1);
      this.setDepth(boxSide * .1);
      this.setHeight(boxSide * .1);
      this.setMaterial(color);
      color.setDiffuseColor(Color.FIREBRICK);
      color.setSpecularColor(Color.CHOCOLATE);
      this.setOpacity(0);
    }
  }

  public int getX()
  {
    return x;
  }

  public float getBoxSide()
  {
    return boxSide;
  }

  public void setBoxSide(float boxSide)
  {
    this.boxSide = boxSide;
  }

  void setX(int x)
  {
    this.x = x;
  }

  public int getY()
  {
    return y;
  }

  void setY(int y)
  {
    this.y = y;
  }

  public int getZ()
  {
    return z;
  }

  void setZ(int z)
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
