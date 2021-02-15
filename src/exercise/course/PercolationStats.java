package exercise.course;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int trials;
    private final double[] count;
    private final double mean_value;
    private final double stddev_value;
    public PercolationStats(int n, int trials){
        if(n <=0||trials<=0)    throw new IllegalArgumentException();
        this.trials = trials;
        count = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while(!p.percolates()){
                int x = StdRandom.uniform(1,n+1);
                int y = StdRandom.uniform(1, n+1);
                p.open(x, y);
            }
            count[i] = p.numberOfOpenSites()/ Math.pow(n,2);
        }
        mean_value = StdStats.mean(count);          //这里必须调用StdStats类的mean方法，要不然有一项测试无法通过
        stddev_value = StdStats.stddev(count);      //这里也必须调用StdStats类的stddev方法
    }
    public double mean(){
        return mean_value;
    }
    public double stddev(){
        return stddev_value;
    }
    public double confidenceLo(){
        return mean_value - 1.96 * stddev_value / Math.sqrt(trials);
    }
    public double confidenceHi(){
        return mean_value + 1.96 * stddev_value / Math.sqrt(trials);
    }

    public static void main(String[] args){         //main()方法必须存在，且需要按照题目要求编写main方法测试
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean                    = "+ps.mean());
        StdOut.println("stddev                  = "+ps.stddev());
        StdOut.println("95% confidence interval = ["+ps.confidenceLo()+","+ps.confidenceHi()+"]");
    }
}
