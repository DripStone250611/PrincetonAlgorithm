package exercise.course.week1;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
public class Percolation {
    private final boolean[][] state;          //储存每个方格是否处于打开状态
    private final int n;
    private final WeightedQuickUnionUF uf;      //包含两个虚拟节点
    private final WeightedQuickUnionUF backwash;    //只包含前一个虚拟节点
    private int count;          //保存处于打开状态的节点数量

    public Percolation(int n){
        if(n <= 0) throw new IllegalArgumentException("n<=0");
        this.n = n;
        count = 0;
        state = new boolean[n][n];
        for (int i = 0; i < n; i++) {           //初始化状态数组
            for (int j = 0; j < n; j++) {
                state[i][j] = false;
            }
        }
        uf = new WeightedQuickUnionUF(n*n+2);//总共有n*n+2个节点，其中第n*n个和第n*n+1（从0开始计数）个节点为虚拟节点
        backwash = new WeightedQuickUnionUF(n*n+1);//总共有n*n+1个节点，其中第n*n个节点为虚拟节点
    }
    public void open(int row, int col){
        if(row < 1|| row >n)    throw new IllegalArgumentException("row is out of bounds");
        if(col < 1||col >n)     throw new IllegalArgumentException("col is out of bounds");
        int index_row = row - 1;
        int index_col = col - 1;
        if(state[index_row][index_col])      return;
        state[index_row][index_col] = true;
        count++;
        if(index_row == 0){
            uf.union(index_col, n*n);       //将第一行已经打开的节点与第一个虚拟节点（n*n）连接
            backwash.union(index_col,n*n);
        }
        if(index_row == n-1){
            uf.union(n*(n-1)+index_col, n*n+1); //将最后一行已经打开的节点与第二个虚拟节点连接
        }
        if(index_col!=0) {      //判断是否为最左列的节点
            if (state[index_row][index_col - 1])  //判断当前节点左边的节点是否打开
            {
                uf.union(index_row * n + index_col - 1, index_row * n + index_col);
                backwash.union(index_row * n + index_col - 1, index_row * n + index_col);
            }
        }
        if (index_row!=0){      //判断是否为最上面的一行节点
            if(state[index_row-1][index_col]){      //判断当前节点上面的节点是否打开
                uf.union((index_row-1)*n+index_col,index_row*n+index_col);
                backwash.union((index_row-1)*n+index_col,index_row*n+index_col);
            }
        }
        if(index_col!=n-1) {    //判断是否为最右列的节点
            if (state[index_row][index_col + 1]){   //判断当前节点右面的节点是否打开
                uf.union(index_row * n + index_col, index_row * n + index_col + 1);
                backwash.union(index_row * n + index_col, index_row * n + index_col + 1);
            }
        }
        if(index_row!=n-1){        //判断是否为最下面一行的节点
            if (state[index_row+1][index_col]){      //判断当前节点下面的节点是否打开
                uf.union((index_row+1)*n+index_col,index_row*n+index_col);
                backwash.union((index_row+1)*n+index_col,index_row*n+index_col);
            }
        }
    }
    public boolean isOpen(int row, int col){
        if(row < 1|| row >n)    throw new IllegalArgumentException("row is out of bounds");
        if(col < 1||col >n)     throw new IllegalArgumentException("col is out of bounds");
        int index_row = row - 1;
        int index_col = col - 1;
        return state[index_row][index_col];
    }
    public boolean isFull(int row, int col){

        if(row < 1|| row >n)    throw new IllegalArgumentException("row is out of bounds");
        if(col < 1||col >n)     throw new IllegalArgumentException("col is out of bounds");
        int index_row = row - 1;
        int index_col = col - 1;
        return backwash.find(index_row*n+index_col) == backwash.find(n*n);  //也可以使用connected方法进行检查，但是conneed方法已过时，官方不推荐使用
    }
    public int numberOfOpenSites(){
        return count;
    }
    public boolean percolates(){
        return uf.find(n*n) == uf.find(n*n+1);
    }

    public static void main(String[] args){         //main方法可有可无，主要是用于测试
        Percolation p = new Percolation(3);
        while (!p.percolates()){
            int x = StdRandom.uniform(1,4);
            int y = StdRandom.uniform(1, 4);
            p.open(x,y);
            System.out.println(x + " "+ y+ " "+p.isFull(x,y));
            System.out.println(x + " "+ y+ " "+p.isOpen(x,y));
        }
        System.out.println(p.numberOfOpenSites());
        System.out.println(p.numberOfOpenSites()/400.0);
    }
}
