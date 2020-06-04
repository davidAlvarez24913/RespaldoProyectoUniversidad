package PracticaPrueba;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
/*
*Autor:
*@david Alvarez C
*segundo intento hacer push luego commit
*/

public class repaso {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Integer> nums= List.of(1,2,3,4,5,6);
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService cs= Executors.newFixedThreadPool(cores);
        //cs.take().get();
        System.out.println(" "+cs.getClass());
        Future<Integer> f= cs.submit(new Task(nums));
        f.get();
        cs.shutdown();
        System.out.println("resultado de la suma = " +f.get());

    }

    public static class Task implements Callable<Integer>{
        private final List<Integer> nums;

        public Task(List<Integer> nums) {
            this.nums = nums;
        }

        @Override
        public Integer call() throws Exception {
            return nums.stream().mapToInt(x->x).sum();
        }
    }
}
