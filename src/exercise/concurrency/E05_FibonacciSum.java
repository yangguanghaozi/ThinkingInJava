package exercise.concurrency;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import net.mindview.util.Generator;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class FibonacciSum implements Generator<Integer>, Callable<Integer>{
    private int count;
    private final int n;
    public FibonacciSum(int n){
        this.n = n;
    }

    private int fib(int n){
        if (n<2){
            return 1;
        }
        return fib(n-2) + fib(n-1);
    }

    @Override
    public Integer next() {
        return  fib(count++);
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i=0; i<n; i++){
            sum += next();
        }
        return sum;
    }


}

public class E05_FibonacciSum {
    public static void main(String[] args){
        ExecutorService exec = Executors.newCachedThreadPool();
        ArrayList<Future<Integer>> resualt = new ArrayList<Future<Integer>>();
        for (int i=0; i<10; i++){
            resualt.add(exec.submit(new FibonacciSum(i)));
        }
        Thread.yield();
        exec.shutdown();
        for (Future<Integer> f : resualt){
            try {
                System.out.println(f.get());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
