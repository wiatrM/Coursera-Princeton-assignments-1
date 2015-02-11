/**
 * Created by Michał Wiatr on 2015-02-10.
 */
public class Percolation {

    private WeightedQuickUnionUF grid;
    private int siteSize;
    private boolean openSites[];

    public Percolation(int N) { // create N-by-N grid, with all sites blocked
        if(N <= 0)
            throw new IllegalArgumentException(Integer.toString(N));
        siteSize = N;
        openSites = new boolean[siteSize * siteSize + 2];
        grid = new WeightedQuickUnionUF(siteSize * siteSize + 2);
        for (int i = 0; i < siteSize * siteSize + 2; i++) {
            openSites[i] = false;
        }
    }

    public void open(int i, int j){ // open site (row i, column j) if it is not open already
        validate(i, j);
        if(!getSite(i, j)) { // if not opened than
            setSite(i, j, true);

            if(i != 1 && getSite(i-1, j)) {
                grid.union(xyTo1D(i, j), xyTo1D(i-1, j));
            }
            if(i != siteSize && getSite(i+1, j)) {
                grid.union(xyTo1D(i, j), xyTo1D(i+1, j));
            }
            if(j != 1 && getSite(i, j-1)) {
                grid.union(xyTo1D(i, j), xyTo1D(i, j-1));
            }
            if(j != siteSize && getSite(i, j+1)) {
                grid.union(xyTo1D(i, j), xyTo1D(i, j+1));
            }
            if(i == 1) {
                grid.union(xyTo1D(i, j), 0); // connect to virtual TOP verticle
            }
            // check for bottom "backwash"
            if (isFull(i, j)) {
                for (int x = 1; x <= siteSize; x++) {
                    if (getSite(siteSize, x) && isFull(siteSize, x)) {
                        grid.union(xyTo1D(siteSize, x), siteSize * siteSize + 1);
                    }
                }
            }
        }
    }

    public boolean isOpen(int i, int j){ // is site (row i, column j) open?
        validate(i, j);
        if(getSite(i, j)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull(int i, int j){ // is site (row i, column j) full?
        validate(i, j);
        // chceck if site is opened from virtual TOP verticle
        return isOpen(i, j) && grid.connected(0, xyTo1D(i, j));
    }

    public boolean percolates(){ // does the system percolate?
        return grid.connected(0, siteSize * siteSize + 1);
    }

    private void validate(int i, int j) {
        if(i < 1 || j < 1 || i > siteSize || j > siteSize) {
            throw new IndexOutOfBoundsException();
        }
    }

    private int xyTo1D(int x, int y) {
        return x * siteSize - (siteSize - y);
    }

    private boolean getSite(int x, int y) {
        return openSites[xyTo1D(x,y)];
    }

    private void setSite(int x, int y, boolean val) {
        openSites[xyTo1D(x,y)] = val;
    }
}
