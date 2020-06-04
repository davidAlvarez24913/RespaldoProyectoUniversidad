package PracticaPrueba;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class Tarea implements Runnable{
    private final int n;
    private CountDownLatch endController;
    private boolean isPrime;

    public Tarea(int n, CountDownLatch endController) {
        this.n = n;
        this.endController = endController;
    }
    public boolean getIsPrime() {
        return  isPrime;
    }

    @Override
    public void run(){
        String david;
        isPrime=!IntStream.range(2,n).anyMatch(i ->n %i ==0);
        david= (isPrime)? " es Primo <--":" no es primo";
        System.out.println("el numero "+n+david);
        endController.countDown();
    }

}
