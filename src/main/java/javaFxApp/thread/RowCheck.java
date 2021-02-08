package javaFxApp.thread;

import java.util.concurrent.Semaphore;

public class RowCheck implements Runnable {
    final int[][] array;
    Semaphore semaphore;
    boolean[] checkInputForNumber;
    int row;
    int col;
    int inputNumber;

    public RowCheck(int[][] array , Semaphore semaphore , boolean[] checkInputForNumber , int row , int col,int inputNumber) {
        this.inputNumber = inputNumber;
        this.col = col;
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
            System.out.println(Thread.currentThread().getName() + ": after acquire  -> available permit = "+ semaphore.availablePermits());
            for (int i = 0 ; i < 9 ; i++){
                if (array[row][i] == inputNumber){
                    count++;
                }
            }
            checkInputForNumber[1] = count <= 1;
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + ": after release  -> available permit = "+ semaphore.availablePermits());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
