package PracticaPrueba;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class cdlPrimos {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> nums = IntStream.generate(() ->
                ThreadLocalRandom.current().nextInt(1, 100_000)).limit(24)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("resultado " +results(nums));

    }
    public static int results(List<Integer> numbers) throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        int[] coresList = IntStream.range(0, cores).toArray();
        List<Tarea> listaRecolectora= new ArrayList<>();
        int ini = 0, fin = 0;
        var result = 0;

        // Descomposición de Datos
        List<List<Integer>>  desconDatos = new ArrayList<List<Integer>>() ;

        for (var c : coresList) {
            ini = (c * ((numbers.size()) / cores));
            fin = (c == (cores - 1)) ? numbers.size() : ((c + 1) * ((numbers.size()) / cores));

            List<Integer> subCad = numbers.subList(ini, fin);
            desconDatos.add(c, subCad);
        }


        for (var x:desconDatos) {
            ThreadPoolExecutor tpe= (ThreadPoolExecutor) Executors.newFixedThreadPool(cores);//fixed ->grupo de  hilos reusables
            CountDownLatch endController = new CountDownLatch(x.size());// Se pasa como argumento # numero de elementos de la lista

            for (int n : x) {
                Tarea task = new Tarea(n, endController);
                listaRecolectora.add(task);
                tpe.execute(task);
            }
            endController.await();
            // lanza un excepcion  interrupted, y debe invoncar el método countDown dentro del método run en la clase Primo
            tpe.shutdown();
        }

        return (int) listaRecolectora.stream().filter(Tarea::getIsPrime).count();
    }
    public class Primos implements Runnable{
        private final int n;
        private CountDownLatch endController;
        private boolean isPrime;

        public Primos(int n, CountDownLatch endController) {
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

}
