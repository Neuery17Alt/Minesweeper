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


public class MineSweeperApplication extends Application {

    private static int X_FIELD = 16;
    private static int Y_FIELD = 16;
    public static int FIELD_SIZE = 25;
    private static Field[][] grid;
    public static int countBomb = 0;
    int count = 0;

    private static MineSweeperApplication instance;
    public MineSweeperApplication() {
        instance = this;
    }

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

    public boolean choiceSet = false;
    @FXML
    AnchorPane anchorField;
    @FXML
    AnchorPane anchorFieldMain;
    public static Pane root;

    int bombCount = 0;

    @FXML
    public void initialize() {
        root = new Pane();
        createContent();
    }

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

    @FXML
    public void resetButtonClicked (){
        resetButton.setDisable(true);
        root.getChildren().clear();
        resetButton.setDisable(false);
        initialize();
    }

    /**
     * Resets the Game and resizes the Gamegrid when the choiceBox-Choice is changed. Options are:
     *      8x8 Grid
     *      16x16 Grid
     *      20x20 Grid
     * Then the game is intialized (created) again!
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
     * In this Method the Gamefield is created. Besides that the neighbours of a field are created to check
     * for bombs (for the bombcount)
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
         * with a 20% Bombfield chance
         */
        // Spielfeld mit Zufallsbomben wird generiert
        for (int y = 0; y < Y_FIELD; y++) {
            for (int x = 0; x < X_FIELD; x++) {
                Field field = new Field(x, y, Math.random() < 0.2);
                grid[x][y] = field;
                root.getChildren().add(field);
            }
        }


        /**
         * Counts the Bombs around every fieldnode field and sets the bombcount after that
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

    public static void calcFlag (boolean check) {
        int anzahl = Integer.parseInt(MineSweeperApplication.getInstance().flagField.getText());
        if (check) {
            anzahl--;
        } else {
            anzahl++;
        }
        MineSweeperApplication.getInstance().flagField.setText(""+anzahl);
    }

    public static void setWinner(boolean check) {
        if (check) {
            MineSweeperApplication.getInstance().gameStatus.setText("YOU WIN");
        } else {
            MineSweeperApplication.getInstance().gameStatus.setText("YOU LOSE");
        }
    }

    public static String getGameStatus() {

        return MineSweeperApplication.getInstance().gameStatus.getText();
    }

    /**
     * makes every field around a field a neighbour and saves position
     * of the neighbours to count the bombs around a field
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