package org.operatingsystem.javafxapp.thread;


public class TableCheck  implements Runnable{
    final int[][] array;
    boolean[] checkInputForNumber;
    int inputNumber;
    int row1;
    int row2;
    int col1;
    int col2;

    public TableCheck(int[][] array ,  boolean[] checkInputForNumber , int inputNumber , int row , int col) {
        this.inputNumber = inputNumber;
        this.checkInputForNumber = checkInputForNumber;
        this.array = array;

        if (row >= 0 && row < 3) {
            row1 = 0;
            row2 = 2;
        }
        if (row >= 3 && row < 6) {
            row1 = 3;
            row2 = 5;
        }
        if (row >= 6 && row < 9) {
            row1 = 6;
            row2 = 8;
        }
        if (col >= 0 && col < 3) {
            col1 = 0;
            col2 = 2;
        }
        if (col >= 3 && col < 6) {
            col1 = 3;
            col2 = 5;
        }
        if (col >= 6 && col < 9) {
            col1 = 6;
            col2 = 8;
        }
    }

    @Override
    public void run() {
        int count = 0;
        for (int i = row1 ; i <= row2 ;i++) {
            for (int j = col1; j <= col2; j++) {
                if (array[i][j] == inputNumber) {
                    count++;
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + " done");
        checkInputForNumber[2] = count <= 1;

    }
}
