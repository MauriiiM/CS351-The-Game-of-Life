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
  private float boxSize = 4;
  PhongMaterial color = new PhongMaterial();

  Cell()
  {
    setAlive();
  }

  boolean isAlive()
  {
    return isAlive;
  }

  int getX()
  {
    return x;
  }

  float getBoxSize()
  {
    return boxSize;
  }

  void setBoxSize(float boxSize)
  {
    this.boxSize = boxSize;
  }

  void setX(int x)
  {
    this.x = x;
  }

  int getY()
  {
    return y;
  }

  void setY(int y)
  {
    this.y = y;
  }

  int getZ()
  {
    return z;
  }

  void setZ(int z)
  {
    this.z = z;
  }

  void setAlive()
  {
    isAlive = true;
    this.setWidth(boxSize * .9);
    this.setDepth(boxSize * .9);
    this.setHeight(boxSize * .9);
    this.setMaterial(color);
    color.setDiffuseColor(Color.DEEPSKYBLUE);
    color.setSpecularColor(Color.CADETBLUE);
  }

  void setDead()
  {
    isAlive = false;
    // "*.1 because we want it to be a tenth of the size when dead"
//    this.setWidth(0);
//    this.setDepth(0);
//    this.setHeight(0);
//    this.setMaterial(color);
//    color.setDiffuseColor(Color.FIREBRICK);
//    color.setSpecularColor(Color.CHOCOLATE);
//    this.setOpacity(1);
  }
}
