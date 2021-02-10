package org.operatingsystem.javafxapp.thread;

import java.util.concurrent.Semaphore;


public class ColumnCheck implements Runnable{
    final int[][] array;
    Semaphore semaphore;
    boolean[] checkInputForNumber;
    int row;
    int col;
    int inputNumber;


    public ColumnCheck(int[][] array , Semaphore semaphore ,boolean[] checkInputForNumber , int row, int col, int inputNumber) {
        this.inputNumber = inputNumber;
        this.col= col;
        this.row = row;
        this.checkInputForNumber = checkInputForNumber;
        this.array = array;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        int count = 0;
        try {
            semaphore.acquire();
            System.out.println("[INFO] " + Thread.currentThread().getName() + ": after acquire  -> available permit = " + semaphore.availablePermits());
            for (int i = 0; i < 9; i++) {
                if (array[i][col] == inputNumber) {
                    count++;
                }
            }
            checkInputForNumber[0] = count <= 1;
            semaphore.release();
            System.out.println("[INFO] " + Thread.currentThread().getName() + ": after release  -> available permit = " + semaphore.availablePermits());
        } catch (InterruptedException e) {
            System.err.println("interrupted!");
            Thread.currentThread().interrupt();
        }


    }
}
