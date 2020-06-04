package PracticaPrueba;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainPractica {
    public static void main(String[] args) throws InterruptedException {
        // crear Arreglovar ThreadLocalRandom.current().ints(5,1,11).toArray();

        List<Integer> nums = IntStream.generate(() ->
                ThreadLocalRandom.current().nextInt(1, 30)).limit(10)
                .boxed()
                .collect(Collectors.toList());
        List<Tarea> listaRecolectora= new ArrayList<>();
        int cores=  Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor tpe= (ThreadPoolExecutor) Executors.newFixedThreadPool(cores);//fixed ->grupo de  hilos reusables
        CountDownLatch endController= new CountDownLatch(nums.size());// Se pasa como argumento # numero de elementos de la lista

        for(int n:nums){
            Tarea task= new Tarea(n,endController);
            listaRecolectora.add(task);
            tpe.execute(task);
        }

        endController.await();// lanza un excepcion  interrupted, y debe inconcar el método countDown dentro del método run en la clase Tarea
        System.out.println("\nSalida números primos ="+listaRecolectora.stream().filter(Tarea::getIsPrime).count());
        System.out.println("\nSalida números no primos ="+listaRecolectora.stream().filter(Predicate.not(Tarea::getIsPrime)).count());
        tpe.shutdown();

        int numero=42524;
        int falta,numeroInvertido,resto;
        while(numero<=0);
        falta=numero;
        numeroInvertido=0;
        resto=0;
        while(falta!=0)
        {
            resto=falta%10;
            numeroInvertido=numeroInvertido*10+resto;
            falta=falta/10;
        }
        if(numeroInvertido==numero)
            System.out.println("\nEl numero es capicua\n");
        else
            System.out.println("\nEl numero no es capicua\n");



    }
}
