/**
 * Created by Michał Wiatron 2015-02-10.
 */
public class PercolationStats {
    private int size;
    private double results[];
    private int times;

    public PercolationStats(int N, int T)     // perform T independent experiments on an N-by-N grid
    {
        if(N<=0 || T<=0) {
            throw new java.lang.IllegalArgumentException();
        }

        size = N;
        results = new double[T];
        times = T;
        Percolation simulation;

        double maxSites = size * size;
        for(int i = 0; i < times; i++) {
            simulation = new Percolation(size);
            double numbersCount = 0;
            while(!simulation.percolates()) {
                int x = StdRandom.uniform(1, size + 1);
                int y = StdRandom.uniform(1, size + 1);
                if(!simulation.isOpen(x, y)) {
                    simulation.open(x, y);
                    numbersCount++;
                }
            }
            double result = numbersCount/maxSites;
            results[i] = result;
            simulation = null;
        }

    }

    public double mean()                      // sample mean of percolation threshold
    {
        return StdStats.mean(results);
    }
    public double stddev()                    // sample standard deviation of percolation threshold
    {
        return StdStats.stddev(results);
    }
    public double confidenceLo()              // low  endpoint of 95% confidence interval
    {
        return mean() - (1.96 *  stddev() / Math.sqrt(times));
    }
    public double confidenceHi()              // high endpoint of 95% confidence interval
    {
        return mean() + (1.96 *  stddev() / Math.sqrt(times));
    }
    public static void main(String[] args)    // test client (described below)
    {
        Out out = new Out();
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(N, T);
        out.printf("mean                    = %f\n", ps.mean());
        out.printf("stddev                  = %f\n", ps.stddev());
        out.printf("95% confidence interval = %f, %f\n", ps.confidenceLo(), ps.confidenceHi());
        out.close();
    }
}
