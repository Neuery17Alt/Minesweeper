package com.example.minesweeper;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;




public class MineSweeperApplication extends Application {

    private static int X_FIELD = 16;
    private static int Y_FIELD = 16;
    public static int FIELD_SIZE = 25;
    private static Field[][] grid;
    public static int countBomb = 0;

    @FXML
    ChoiceBox choiceBox;

    boolean choiceSet = false;
    @FXML
    AnchorPane anchorField;
    @FXML
    AnchorPane anchorFieldMain;
    public static Pane root;

    @FXML
    public void initialize () {
        root = new Pane();
        createContent();
    }



    @FXML
    public void choiceBoxClicked () {
        choiceBox.setOnAction((event) -> {
            SingleSelectionModel selectionModel = choiceBox.getSelectionModel();
            int index = selectionModel.getSelectedIndex();
            System.out.println(index);
            switch (index) {
                case 0 -> {
                    root.getChildren().clear();
                    Y_FIELD = 8;
                    X_FIELD = 8;
                    FIELD_SIZE = 50;
                }
                case 1 -> {
                    root.getChildren().clear();
                    Y_FIELD = 16;
                    X_FIELD = 16;
                    FIELD_SIZE = 25;
                }
                case 2 -> {
                    root.getChildren().clear();
                    Y_FIELD = 20;
                    X_FIELD = 20;
                    FIELD_SIZE = 20;
                }
            }
           initialize();
        });
    }



    private void createContent () {
        //Pane root = new Pane();
        root.setPrefSize(400, 400);
        grid= new Field[X_FIELD][Y_FIELD];

        anchorField.getChildren().add(root);

        if (!choiceSet) {
            choiceBox.setValue("16x16");
            choiceBox.getItems().addAll("8x8", "16x16", "20x20");
            choiceSet=true;
        }


    // Spielfeld mit Zufallsbomben wird generiert
        for (int y = 0; y < Y_FIELD; y++) {
            for (int x = 0; x < X_FIELD; x++) {
                Field field = new Field(x, y, Math.random() < 0.2);
                grid[x][y] = field;
                root.getChildren().add(field);
            }
        }

        for (int y = 0; y < Y_FIELD; y++) {
            for (int x = 0; x < X_FIELD; x++) {
                Field field = grid[x][y];
                if (!field.bomb){
                    int count = 0;
                    ArrayList<Field> fields = getNeighbours(grid[x][y]);

                    for (Field f:fields) {
                        if (f.bomb) count++;
                    }
                    if (count > 0) {
                        field.setBombCount(Integer.toString(count));
                    }
                }
            }
        }
    }

    static ArrayList<Field> getNeighbours (Field field) {
        ArrayList<Field> neighbours = new ArrayList<>();

        int[] points = new int[] {-1,-1, -1,0, -1,1, 0,1, 1,1, 1,0, 1,-1, 0,-1};

        for (int i = 0; i<points.length; i++) {
            int dx = points[i];
            int dy = points[i+1];

            int newX = field.getX() + dx;
            int newY = field.getY() + dy;

            if (newX >= 0 && newX < X_FIELD && newY >= 0 && newY < Y_FIELD) {
                neighbours.add(grid[newX][newY]);
            }
            i++;
        }

        return neighbours;
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