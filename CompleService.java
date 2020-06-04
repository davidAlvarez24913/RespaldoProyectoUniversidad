package PracticaPrueba;

import threads.contarAbundantes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompleService {
    public static void main(String[] args) throws InterruptedException,Exception {
        List<Integer> nums = IntStream.generate(() ->
                ThreadLocalRandom.current().nextInt(1, 100_000)).limit(5)
                .boxed()
                .collect(Collectors.toList());
        nums.forEach(System.out::println);
        System.out.println("resultado " +results(nums));


    }
    public static int results(List<Integer> numbers) throws InterruptedException, Exception {

        int cores = Runtime.getRuntime().availableProcessors();
        int[] coresList = IntStream.range(0, cores).toArray();
        int ini = 0, fin = 0;
        var result = 0;

        // Descomposición de Datos

        List<List<Integer>> divDatos = new ArrayList<List<Integer>>();
        for (var c : coresList) {
            ini = (c * ((numbers.size()) / cores));
            fin = (c == (cores - 1)) ? numbers.size() : ((c + 1) * ((numbers.size()) / cores));

            List<Integer> subCad = numbers.subList(ini, fin);
            divDatos.add(c, subCad);

            //ExecutorService exe=  Executors.newFixedThreadPool(cores);
            ExecutorService exe=  Executors.newSingleThreadExecutor();
            //CompletionService<Integer> cs= new ExecutorCompletionService<>(exe);
            Future<Integer> resultado= exe.submit(new CI(subCad));
            /*List<Future<Integer>> futures= new ArrayList<>();
            for (var f:futures){
                futures.add(fu);
            }



            for (var i = 0; i <  futures.size() ; i++) {
                int subtotal = cs.take().get();
                System.out.printf("subtotal: %d", subtotal);
                result += subtotal;
            }

             */
            //System.out.println(resultado.get());
            result=resultado.get();
            exe.shutdown();
            System.out.println(" \n");

        }
        return result;
    }
    public static class CI implements Callable<Integer>  {

        private final List<Integer> nums;

        public CI(List<Integer> nums) {
            this.nums = nums;
        }

        @Override
        public Integer call(){
            return nums.stream().mapToInt(x->x).sum();
        }
    }
}
