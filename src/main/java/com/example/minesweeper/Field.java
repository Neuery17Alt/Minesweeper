package com.example.minesweeper;


import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Field extends StackPane {
    public boolean bomb;
    private int x, y;
    private String text = "";
    private Text bombCount;
    private Rectangle fieldNode;
    private boolean openedField;
    private boolean flagSet = false;

    /**
     * Getter & Setter
     */

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

    /**
     * Class-Constructer
     */
    public Field (int x, int y, boolean bomb) {
        this.x = x;
        this.y = y;
        this.bomb = bomb;
        openedField = false;
        if (bomb) {
            text="X";
        }

        /**
         * This Part makes every grid a Rectangle with the FIELD_SIZE and colors
         * (colors edges, colors field) them right away
         */
        fieldNode = new Rectangle(MineSweeperApplication.FIELD_SIZE, MineSweeperApplication.FIELD_SIZE);
        fieldNode.setFill(Color.BLACK);
        fieldNode.setStroke(Color.PURPLE);
        fieldNode.setVisible(true);

        /**
         * This Section styles the bombfields and listens if a mousebutton is pressed
         */
        bombCount = new Text();
        bombCount.setVisible(false);
        bombCount.setText(this.bomb ? "X" : "");
        bombCount.setStroke(Color.YELLOW);
        setOnMouseClicked(e -> checkMouseButton(e.getButton()));

        getChildren().addAll(fieldNode, bombCount);
        setTranslateX(x*MineSweeperApplication.FIELD_SIZE);
        setTranslateY(y*MineSweeperApplication.FIELD_SIZE);
    }


    /**
     * This Method is called when a mousebutton is pressed, after that it checks what Button (Left/Right) is clicked!
     *      -If the left Mousbutton is clicked it will call the open method which opens a fieldnode
     *       (uncovers the bombCount).
     *      -If the right Mousbutton is clicked it sets a flag (colors the fieldnode yellow) on the fieldnode so that you cant
     *       open it anymore unless you remove the flag by right-clicking again!
     */
    public void checkMouseButton (MouseButton e) {
        if (e.equals(MouseButton.PRIMARY) && !MineSweeperApplication.getGameStatus().equals("YOU LOSE") && !MineSweeperApplication.getGameStatus().equals("YOU WIN")) {
            open();
        } else if (e.equals(MouseButton.SECONDARY) && !MineSweeperApplication.getGameStatus().equals("YOU LOSE") && !MineSweeperApplication.getGameStatus().equals("YOU WIN")) {
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

    /**
     * This method opens the fieldnode (lets you see the bombcount) and checks for the winner right away. If the fieldnode
     * is opened, the fieldnode gets the style according to the content of it
     */
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
