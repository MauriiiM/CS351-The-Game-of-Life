/**
 * Created by mmons on 9/22/2016.
 */

import javafx.application.Application;
import javafx.stage.Stage;

public class TheGameOfLife extends Application
{

  Cell[] x;
  Cell[] y;
  Cell[] z;

  TheGameOfLife()
  {
    x = new Cell[30];
    y = new Cell[30];
    z = new Cell[30];


  }

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {

  }
}
