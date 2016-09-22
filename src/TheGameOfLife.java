/**
 * Created by mmons on 9/22/2016.
 */

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class TheGameOfLife extends Application
{

  private Group mainRoot = new Group();
  private Cell word = new Cell();

  Cell[] x = new Cell[30];
  Cell[] y = new Cell[30];
  Cell[] z = new Cell[30];

  private void createGrid()
  {
  }

  public static void main(String[] args)
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage)
  {
    primaryStage.setFullScreen(true);
    primaryStage.setTitle("The Game of Life");

    mainRoot.getChildren().add(word);
    TheGameOfLife theGameOfLife = new TheGameOfLife();
  }
}
