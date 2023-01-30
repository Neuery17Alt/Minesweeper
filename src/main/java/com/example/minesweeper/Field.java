package com.example.minesweeper;


import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Field extends StackPane {
    private int x, y;
    public boolean bomb;
    private String text = "";
    public Text bombCount = new Text();
    private Rectangle fieldNode = null;
    private boolean openedField;
    private boolean flagSet = false;

    public static int getBombshidden() {
        return bombshidden;
    }

    private static int bombshidden = 0;

    private ArrayList<Integer> arrXBomb = new ArrayList<>();
    private ArrayList<Integer> arrYBomb = new ArrayList<>();

    public boolean isBomb() {
        return bomb;
    }

    public boolean isOpenedField () {
        return openedField;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setBombCount(String bombCount) {
        this.bombCount.setText(bombCount);
    }

    public Field (int x, int y, boolean bomb) {
        this.x = x;
        this.y = y;
        this.bomb = bomb;
        openedField = false;
        if (bomb) {
            bombshidden++;
            text="X";
            arrXBomb.add(x);
            arrYBomb.add(y);
        }

        fieldNode = new Rectangle(MineSweeperApplication.FIELD_SIZE, MineSweeperApplication.FIELD_SIZE);
        fieldNode.setFill(Color.BLACK);
        fieldNode.setStroke(Color.PURPLE);
        fieldNode.setVisible(true);

        bombCount = new Text();
        bombCount.setVisible(false);
        bombCount.setText(this.bomb ? "X" : "");
        bombCount.setStroke(Color.YELLOW);
        setOnMouseClicked(e -> checkMouseButton(e.getButton()));

        getChildren().addAll(fieldNode, bombCount);
        setTranslateX(x*MineSweeperApplication.FIELD_SIZE);
        setTranslateY(y*MineSweeperApplication.FIELD_SIZE);
    }


    public void checkMouseButton (MouseButton e) {
        if (e.equals(MouseButton.PRIMARY) && !MineSweeperApplication.getGameStatus().equals("YOU LOSE")) {
            open();
        } else if (e.equals(MouseButton.SECONDARY) && !MineSweeperApplication.getGameStatus().equals("YOU LOSE")) {
            if (!flagSet && !this.openedField) {
                fieldNode.setFill(Color.YELLOW);
                flagSet=true;
                MineSweeperApplication.calcFlag(flagSet);
            } else if (flagSet && !this.openedField){
                fieldNode.setFill(Color.BLACK);
                flagSet=false;
                MineSweeperApplication.calcFlag(flagSet);
            }
        }

    }

    public void open () {
        if (!fieldNode.getFill().equals(Color.YELLOW)) {
            if (this.openedField) return;
            this.openedField = true;
            bombCount.setVisible(true);
            fieldNode.setFill(Color.rgb(70, 74, 70));
            fieldNode.setStroke(Color.BLACK);
            if (bombCount.getText().isEmpty()) {
                MineSweeperApplication.getNeighbours(this).forEach(Field::open);
            } else if (bombCount.getText().equals("X")) {
                fieldNode.setFill(Color.RED);
                MineSweeperApplication.setWinner(false);
            }
            if (MineSweeperApplication.checkifGamevictory()) {
                MineSweeperApplication.setWinner(true);
            }
        }
    }

}
