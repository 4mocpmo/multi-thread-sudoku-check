package javaFxApp.thread;

import java.util.concurrent.Semaphore;

public class TableCheck  implements Runnable{
    final int[][] array;
    Semaphore semaphore;
    boolean[] checkInputForNumber;
    int inputNumber;
    int row1,row2;
    int col1,col2;

    public TableCheck(int[][] array , Semaphore semaphore , boolean[] checkInputForNumber , int inputNumber , int row , int col) {
        this.inputNumber = inputNumber;
        this.checkInputForNumber = checkInputForNumber;
        this.array = array;
        this.semaphore = semaphore;

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
        try {
            semaphore.acquire();
            System.out.println(Thread.currentThread().getName() + ": after acquire  -> available permit = "+ semaphore.availablePermits());
            synchronized (array) {
                    for (int i = row1 ; i <= row2 ;i++){
                        for (int j = col1 ; j <= col2; j++){
                            if (array[i][j] == inputNumber){
                                count++;
                            }
                        }
                    }
                checkInputForNumber[2] = count <= 1;
            }
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + ": after release  -> available permit = "+ semaphore.availablePermits());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
