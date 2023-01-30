package com.example.minesweeper;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**--------------------------------------------------------
 *      Hoehere Technische Bundeslehranstalt STEYR
 *------------------------------------------------------*/
/**
 * @author  : Jannick Angerer
 *
 * @date    : 30.01.2023
 * @file    : MineSweeperApplication.java
 *
 * @details
 * This is the game called Minesweeper. In this game you
 * have a grid with a certain number of grids. There a
 * 3 basic sizes:
 *      -8x8 (Beginner)
 *      -16x16 (Medium)
 *      -20x20 (Advanced)
 * This game is all about not clicking on a bomb, which isnt
 * even that easy, because at the start every field needs
 * to be uncovered. But if you uncover every field without
 * touching a bomb you win, but if you do touch a bomb you
 * lose! To remember where a bomb is, you have a certain amount
 * of flags, which can be used to mark bombs, but you only have
 * as many flags as there are bombs on the field
 *
 *
 *
 */


public class MineSweeperApplication extends Application {

    public static int X_FIELD = 16;
    public static int Y_FIELD = 16;
    public static int FIELD_SIZE = 25;
    private static Field[][] grid;
    private static int countBomb = 0;
    int count = 0;

    private static MineSweeperApplication instance;

    @FXML
    ChoiceBox choiceBox;

    @FXML
    Label flagField;
    @FXML
    Label timerLabel;
    @FXML
    Label gameStatus;
    @FXML
    Button resetButton;

    private boolean choiceSet = false;
    @FXML
    AnchorPane anchorField;
    @FXML
    AnchorPane anchorFieldMain;
    private static Pane root;

    private int bombCount = 0;

    /**
     * Sets the instance
     */
    public MineSweeperApplication() {
        instance = this;
    }

    /**
     * Creates the new anchorPane where the anchorPane for the gameField is placed on
     */
    @FXML
    public void initialize() {
        root = new Pane();
        createContent();
    }

    /**
     * If the "Reset"-Button is clicked this method is called and resets the field (grid)
     * then generates a new one!
     */
    @FXML
    public void resetButtonClicked (){
        root.getChildren().clear();
        initialize();
    }

    /**
     * If a field is opened it calls this method, which checks for the winner:
     *      -if every field, which isnt a bomb, is opened the player won
     *      -if a field, with a bomb in it, is opened the player lost
     */
    public static boolean checkifGamevictory() {
        boolean checkwinner = true;
        for (int y = 0; y < Y_FIELD; y++) {
            for (int x = 0; x < X_FIELD; x++) {
                Field field = grid[x][y];
                if (!field.isOpenedField() && !field.isBomb()) {
                    checkwinner = false;
                    break;
                }
            }
        }
        return checkwinner;
    }


    /**
     * Resets the Game and resizes the Gamegrid when the choiceBox-Choice is changed. Options are:
     *      8x8 Grid (Beginner)
     *      16x16 Grid (Medium)
     *      20x20 Grid (Advanced)
     * Then the field is gets reseted and the game is initialized (created) again!
     */
    @FXML
    public void choiceBoxClicked() {
        choiceBox.setOnAction((event) -> {
            SingleSelectionModel selectionModel = choiceBox.getSelectionModel();
            int index = selectionModel.getSelectedIndex();
            System.out.println(index);
            root.getChildren().clear();
            switch (index) {
                case 0 -> {
                    Y_FIELD = 8;
                    X_FIELD = 8;
                    FIELD_SIZE = 50;
                }
                case 1 -> {
                    Y_FIELD = 16;
                    X_FIELD = 16;
                    FIELD_SIZE = 25;
                }
                case 2 -> {
                    Y_FIELD = 20;
                    X_FIELD = 20;
                    FIELD_SIZE = 20;
                }
            }
            bombCount = 0;
            initialize();
        });
    }


    /**
     * In this Method the Gamefield is created and all the default values (e.g. gameStatus) are set
     */
    private void createContent() {
        gameStatus.setText("...");
        //Pane root = new Pane();
        root.setPrefSize(400, 400);
        grid = new Field[X_FIELD][Y_FIELD];

        anchorField.getChildren().add(root);
        if (!choiceSet) {
            choiceBox.setValue("Medium (16x16)");
            choiceBox.getItems().addAll("Beginner (8x8)", "Medium (16x16)", "Advanced (20x20)");
            choiceSet = true;
        }

        /**
         * Creates the field where every grid gets created seperatly and
         * with a 20% Bombfield chance!
         */
        // Spielfeld mit Zufallsbomben wird generiert
        for (int y = 0; y < Y_FIELD; y++) {
            for (int x = 0; x < X_FIELD; x++) {
                Field field = new Field(x, y, Math.random() < 0.01);
                grid[x][y] = field;
                root.getChildren().add(field);
            }
        }


        /**
         * Counts the Bombs around every fieldnode and sets the bombcount after that
         */

        bombCount=0;
        for (int y = 0; y < Y_FIELD; y++) {
            for (int x = 0; x < X_FIELD; x++) {
                Field field = grid[x][y];
                if (!field.bomb) {
                    int count = 0;
                    ArrayList<Field> fields = getNeighbours(grid[x][y]);

                    for (Field f : fields) {
                        if (f.bomb) count++;
                    }
                    if (count > 0) {
                        field.setBombCount(Integer.toString(count));
                    }
                } else {
                    bombCount++;
                }
            }
        }
        flagField.setText(""+bombCount);
    }

    /**
     * This Method is to calculate the available Flags after placing one and after it resets the field.
     */
    public static void calcFlag (boolean check) {
        int anzahl = Integer.parseInt(MineSweeperApplication.getInstance().flagField.getText());
        if (check) {
            anzahl--;
        } else {
            anzahl++;
        }
        MineSweeperApplication.getInstance().flagField.setText(""+anzahl);
    }

    /**
     * If all Fields who aren´t bombs are opened it sets the Label "gameStatus" to "YOU WIN" but if you open a field
     * with a bomb in it the label "gameStatus" will be set to "YOU LOSE"
     */

    public static void setWinner(boolean check) {
        if (check) {
            MineSweeperApplication.getInstance().gameStatus.setText("YOU WIN");
        } else {
            MineSweeperApplication.getInstance().gameStatus.setText("YOU LOSE");
        }
    }

    /**
     * Getter for the Label "gameStatus"
     */
    public static String getGameStatus() {
        return MineSweeperApplication.getInstance().gameStatus.getText();
    }

    /**
     * This method makes every field around a field a neighbour and saves position
     * of the neighbours to be able to count the bombs around a field later
     */
    static ArrayList<Field> getNeighbours(Field field) {
        ArrayList<Field> neighbours = new ArrayList<>();

        int[] points = new int[]{-1, -1, -1, 0, -1, 1, 0, 1, 1, 1, 1, 0, 1, -1, 0, -1};

        for (int i = 0; i < points.length; i++) {
            int dx = points[i];
            int dy = points[i + 1];

            int newX = field.getX() + dx;
            int newY = field.getY() + dy;

            if (newX >= 0 && newX < X_FIELD && newY >= 0 && newY < Y_FIELD) {
                neighbours.add(grid[newX][newY]);
            }
            i++;
        }

        return neighbours;
    }

    /**
     * If I get a NullpointerException I can use the Instance to fix it
     */

    public static MineSweeperApplication getInstance() {
        if (instance == null) {
            instance = new MineSweeperApplication();
        }
        return instance;
    }


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MineSweeperApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("MineSweeper");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }


}