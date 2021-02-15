package org.operatingsystem.javafxapp.thread;

import java.util.concurrent.Semaphore;

public class RowCheck implements Runnable {
    final int[][] array;
    boolean[] checkInputForNumber;
    int row;
    int col;
    int inputNumber;

    public RowCheck(int[][] array , boolean[] checkInputForNumber , int row , int col,int inputNumber) {
        this.inputNumber = inputNumber;
        this.col = col;
        this.row = row;
        this.checkInputForNumber = checkInputForNumber;
        this.array = array;
    }

    @Override
    public void run() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (array[row][i] == inputNumber) {
                count++;
            }
        }
        System.out.println(Thread.currentThread().getName() + " done");
        checkInputForNumber[1] = count <= 1;

    }
}
