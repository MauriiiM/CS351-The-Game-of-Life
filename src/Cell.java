import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * A cell which extends JavaFX box
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
    setDead();
  }

  float getBoxSize()
  {
    return boxSize;
  }

  void setBoxSize(float boxSize)
  {
    this.boxSize = boxSize;
    this.setHeight(boxSize);
    this.setWidth(boxSize);
    this.setWidth(boxSize);
  }

  void setX(int x)
  {
    this.x = x;
  }

  void setY(int y)
  {
    this.y = y;
  }

  void setZ(int z)
  {
    this.z = z;
  }

  boolean  isAlive()
  {
    return isAlive;
  }

  /**
   * will set color to blue and size to 95% of 4
   */
  void setAlive()
  {
    isAlive = true;
    this.setWidth(boxSize * .95);
    this.setDepth(boxSize * .95);
    this.setHeight(boxSize * .95);
    this.setMaterial(color);
    color.setDiffuseColor(Color.DEEPSKYBLUE);
    color.setSpecularColor(Color.CADETBLUE);
  }

  void setDead()
  {
    isAlive = false;
    this.setWidth(0);
    this.setDepth(0);
    this.setHeight(0);
    this.setMaterial(color);
    color.setDiffuseColor(Color.FIREBRICK);
    color.setSpecularColor(Color.CHOCOLATE);
  }

  /**
   * Used to set color while dying or being born
   * @param isAlive true if being born (blue), false if dying (red)
   */
  void setColor(boolean isAlive)
  {
    if (isAlive)
    {
      color.setDiffuseColor(Color.DEEPSKYBLUE);
      color.setSpecularColor(Color.CADETBLUE);
    }
    else
    {
      color.setDiffuseColor(Color.FIREBRICK);
      color.setSpecularColor(Color.CHOCOLATE);
    }
    this.setMaterial(color);
  }
}
