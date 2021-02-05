package javaFxApp;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javaFxApp.thread.ColumnCheck;
import javaFxApp.thread.RowCheck;
import javaFxApp.thread.TableCheck;


public class Controller implements Initializable{

    @FXML
    private Canvas canvas;
    int player_selected_row;
    int player_selected_col;
    int[][] array = new int[9][9];
    Semaphore semaphore = new Semaphore(3);
    //0 index for columnCheck 1 index for row check and 2 index for table check
    boolean[] checkInputForNumber = new boolean[3];


    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//Get graphics context from canvas
		GraphicsContext context = canvas.getGraphicsContext2D();
		//Call drawOnCanvas method, with the context we have gotten from the canvas
		drawOnCanvas(context);
	}

	public boolean resultOfThreadChecking(int inputNumber ,int row , int col){
	    Thread columnCheck = new Thread(new ColumnCheck(array , semaphore,checkInputForNumber , row ,col , inputNumber));
	    Thread rowCheck = new Thread(new RowCheck(array , semaphore,checkInputForNumber, row ,col , inputNumber));
	    Thread tableCheck = new Thread(new TableCheck(array , semaphore,checkInputForNumber,inputNumber,row,col));
	    columnCheck.setName("COLUMN THREAD");
	    tableCheck.setName("TABLE THREAD ");
	    rowCheck.setName("ROW THREAD   ");
	    columnCheck.start();
	    rowCheck.start();
	    tableCheck.start();
        try {
            columnCheck.join();
            rowCheck.join();
            tableCheck.join();
            System.out.println("_________________________________________________");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return checkInputForNumber[0] && checkInputForNumber[1] && checkInputForNumber[2];
    }
    public void drawOnCanvas(GraphicsContext context) {
        context.clearRect(0, 0, 450, 450);
        // draw white rounded rectangles for our board
        for(int row = 0; row<9; row++) {
            for(int col = 0; col<9; col++) {
                // finds the y position of the cell, by multiplying the row number by 50, which is the height of a row in pixels
                // then adds 2, to add some offset
                int position_y = row * 50 + 2;
                // finds the x position of the cell, by multiplying the column number by 50, which is the width of a column in pixels
                // then add 2, to add some offset
                int position_x = col * 50 + 2;
                // defines the width of the square as 46 instead of 50, to account for the 4px total of blank space caused by the offset
                // as we are drawing squares, the height is going to be the same
                int width = 46;
                // set the fill color to white (you could set it to whatever you want)
                context.setFill(Color.DARKORANGE);
                // draw a rounded rectangle with the calculated position and width. The last two arguments specify the rounded corner arcs width and height.
                // Play around with those if you want.
                context.fillRoundRect(position_x, position_y, width, width, 10, 10);
            }
        }

        // draw highlight around selected cell
        // set stroke color to res
        context.setStroke(Color.RED);
        // set stroke width to 5px
        context.setLineWidth(3);
        // draw a strokeRoundRect using the same technique we used for drawing our board.
        context.strokeRoundRect(player_selected_col * 50 + 2, player_selected_row * 50 + 2, 46, 46, 10, 10);
        int testEnd = 0;
        for(int row = 0; row<9; row++) {
            for(int col = 0; col<9; col++) {

                // finds the y position of the cell, by multiplying the row number by 50, which
                // is the height of a row in pixels then adds 2, to add some offset
                int position_y = row * 50 + 30;

                // finds the x position of the cell, by multiplying the column number by 50,
                // which is the width of a column in pixels then add 2, to add some offset
                int position_x = col * 50 + 20;

                // set the fill color to white (you could set it to whatever you want)
//                context.setFill(Color.BLACK);

                // set the font, from a new font, constructed from the system one, with size 20

                context.setFont(new Font(20));
                if (array[row][col] != 0) {
                    if (!resultOfThreadChecking(array[row][col], row, col)) {
                        context.setFill(Color.RED);
                        context.fillText(array[row][col] + "", position_x, position_y);
                        testEnd--;
                    } else {
                        context.setFill(Color.BLACK);
                        context.fillText(array[row][col] + "", position_x, position_y);
                        testEnd++;
                        if (testEnd > 80) {
                            Alert alert = new Alert(Alert.AlertType.NONE);
                            ButtonType buttonTypeOne = new ButtonType("exit");
                            ButtonType buttonTypeTwo = new ButtonType("again");
                            alert.setTitle("END");
                            alert.setHeaderText("you complete all element successfully");
                            alert.setContentText("continue or exit?");
                            alert.getButtonTypes().setAll(buttonTypeOne,buttonTypeTwo);
                            Window window = alert.getDialogPane().getScene().getWindow();
                            Stage stage = (Stage) window;
                            stage.getIcons().add(new Image(getClass().getResourceAsStream("/sample/image/1.png")));
                            window.setOnCloseRequest(e -> alert.hide());
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent())
                                if (result.get() == buttonTypeOne) {
                                    System.exit(0);
                                    alert.close();
                                }else
                                    if (result.get() == buttonTypeTwo){
                                    for (int k = 0 ; k < 9 ; k++)
                                        for (int m = 0 ; m < 9 ; m++)
                                            buttonResetClicked();
                                        drawOnCanvas(context);
                                }
                        }
                    }

                }
            }
        }
    }
    public void canvasMouseClicked() {
        canvas.setOnMouseClicked(event -> {
            int mouse_x = (int) event.getX();
            int mouse_y = (int) event.getY();

            // convert the mouseX and mouseY into rows and cols
            // we are going to take advantage of the way integers are treated and we are going to divide by a cell's width
            // this way any value between 0 and 449 for x and y is going to give us an integer from 0 to 8, which is exactly what we are after
            player_selected_row = mouse_y / 50;
            player_selected_col = mouse_x / 50;

            //get the canvas graphics context and redraw

            drawOnCanvas(canvas.getGraphicsContext2D());
        });
    }

    @FXML
    void buttonFillRandomClicked(){
        int[][] arr = new int[9][9];
        int[][] arr2 = {
            {5,3,4,6,7,8,9,1,2},
            {6,7,2,1,9,5,3,4,8},
            {1,9,8,3,4,2,5,6,7},
            {8,5,9,7,6,1,4,2,3},
            {4,2,6,8,5,3,7,9,1},
            {7,1,3,9,2,4,8,5,6},
            {9,6,1,5,3,7,2,8,4},
            {2,8,7,4,1,9,6,3,5},
            {3,4,5,2,8,6,1,7,9}
        };
        int[][] arr1 = {
             {8,2,7,1,5,4,3,9,6} ,
             {9,6,5,3,2,7,1,4,8} ,
             {3,4,1,6,8,9,7,5,2} ,
             {5,9,3,4,6,8,2,7,1} ,
             {4,7,2,5,1,3,6,8,9} ,
             {6,1,8,9,7,2,4,3,5} ,
             {7,8,6,2,3,5,9,1,4} ,
             {1,5,4,7,9,6,8,2,3} ,
             {2,3,9,8,4,1,5,6,7}
        };
        int[][] arr3 = {
                {7,3,6,4,5,2,9,8,1},
                {1,9,8,6,3,7,4,5,2},
                {4,2,5,9,8,1,3,7,6},
                {3,6,4,5,2,8,1,9,7},
                {9,5,2,7,1,4,6,3,8},
                {8,1,7,3,9,6,2,4,5},
                {2,8,9,1,7,3,5,6,4},
                {6,7,3,2,4,5,8,1,9},
                {5,4,1,8,6,9,7,2,3},
        };
        Random random = new Random();
        int a = (int)(Math.random() * (2 + 1) + 0);
        if (a == 0)
            arr = arr1;
        else if (a == 1)
            arr = arr2;
        else
            arr = arr3;

         for (int i = 0 ; i < 7 ; i++){
             for (int j = 0; j < 7; j++) {
                 arr[(int)(Math.random() * (8 + 1) + 0)][(int)(Math.random() * (8 + 1) + 0)] = 0 ;
             }
         }
        System.out.println("______________________Fill Random__________________________");
         array = arr;
         drawOnCanvas(canvas.getGraphicsContext2D());
    }
    @FXML
    void buttonExitClicked(){
	    System.exit(0);
    }
    @FXML
    void buttonClearPressed(){
        array[player_selected_row][player_selected_col] = 0;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }
    @FXML
    void buttonEightPressed() {
	    array[player_selected_row][player_selected_col] = 8;
	    drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    void buttonFivePressed() {
	    array[player_selected_row][player_selected_col] = 5;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    void buttonFourPressed() {
	    array[player_selected_row][player_selected_col] = 4;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    void buttonNinePressed() {
	    array[player_selected_row][player_selected_col] = 9;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    void buttonOnePressed() {
	    array[player_selected_row][player_selected_col] = 1;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    void buttonSevenPressed() {
	    array[player_selected_row][player_selected_col] = 7;
	    drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    void buttonSixPressed() {
	    array[player_selected_row][player_selected_col] = 6;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    void buttonThreePressed() {
	    array[player_selected_row][player_selected_col] = 3;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }

    @FXML
    void buttonTwoPressed() {
	    array[player_selected_row][player_selected_col] = 2;
        drawOnCanvas(canvas.getGraphicsContext2D());
    }
    @FXML
    void buttonResetClicked(){
	    for (int i = 0 ; i < 9 ; i++){
	        for (int j = 0 ; j < 9 ; j++){
	            array[i][j] = 0 ;
            }
        }
        drawOnCanvas(canvas.getGraphicsContext2D());
    }
}
