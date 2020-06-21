package PracticaPrueba;

import threads.Taller3Excutors;
import threads.contarAbundantes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class csAbundantes {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> nums = IntStream.generate(() ->
                ThreadLocalRandom.current().nextInt(1, 100_000)).limit(11)
                .boxed()
                .collect(Collectors.toList());
        /*
        int sumaTot=0;

        FutureTask task = new FutureTask(new Tarea(nums));
        ExecutorService es = Executors.newSingleThreadExecutor ();
        es.submit (task);
        try {
            Integer result = (Integer) task.get();
            sumaTot +=  result;

        } catch (Exception e) {
            System.err.println (e);
        }
        System.out.println(sumaTot+"\n");
        es.shutdown ();

         */

        System.out.println("resultado " +results(nums));


    }
    public static int results(List<Integer> numbers) throws InterruptedException {

        int cores = Runtime.getRuntime().availableProcessors();
        int[] coresList = IntStream.range(0, cores).toArray();
        int ini = 0, fin = 0;
        var result = 0;
        // Descomposición de Datos
        List<List<Integer>>  divDatos = new ArrayList<List<Integer>>() ;

        for (var c : coresList) {
            ini = (c * ((numbers.size()) / cores));
            fin = (c == (cores - 1)) ? numbers.size() : ((c + 1) * ((numbers.size()) / cores));

            List<Integer> subCad = numbers.subList(ini, fin);
            divDatos.add(c, subCad);
        }
        ExecutorService executor=  Executors.newFixedThreadPool(cores);
        CompletionService<Integer> cs= new ExecutorCompletionService<>(executor);
        List<Future<Integer>> futures=  new ArrayList<>();

        for (var x:divDatos){
            futures.add(cs.submit(new Tarea(x)));

        }
        var total=0;var aux=0;
        try {
            for (int i = 0; i <futures.size() ; i++) {
                var  subtotal=cs.take().get();
                aux+=subtotal;
                System.out.println("Parte: "+i+", es: "+aux);
            }
            total+=aux;
        } catch (Exception e) {
            System.err.println(e);
        }
        result=total;
        System.out.println("Total Parcial= "+total);
        executor.shutdown();

    return result;
    }
    public static class Tarea implements Callable<Integer> {
        private final List<Integer> nums;

        public Tarea(List<Integer> nums) {
            this.nums = nums;
        }

        public Integer call() {
            int sumafinal = 0;
            for (var num : nums) {
                var sum = 0;
                for (var i = 1; i < num; i++) {
                    if (num % i == 0) {
                        sum += i;
                    }
                }
                if (sum > num) {
                    System.out.printf("%d es abundante     <--\n", num);
                    sumafinal++;

                } else {
                    System.out.printf("%d no es abundante\n", num);
                }
            }
            return sumafinal;
        }
    }
}
