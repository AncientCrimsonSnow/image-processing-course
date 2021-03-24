package bv_ss20;
//https://javabeginners.de/Algorithmen/Sortieralgorithmen/Quicksort.php
public class Quicksort {

    public static int[] intArr;
    

    public Quicksort(int[] intArr) {
		this.intArr = intArr;
	}

	public int[] sort(int l, int r) {
        int q;
        if (l < r) {
            q = partition(l, r);
            sort(l, q);
            sort(q + 1, r);
        }
        return intArr;
    }

    int partition(int l, int r) {

        int i, j, x = intArr[(l + r) / 2];
        i = l - 1;
        j = r + 1;
        while (true) {
            do {
                i++;
            } while (intArr[i] < x);

            do {
                j--;
            } while (intArr[j] > x);

            if (i < j) {
                int k = intArr[i];
                intArr[i] = intArr[j];
                intArr[j] = k;
            } else {
                return j;
            }
        }
    }
}
