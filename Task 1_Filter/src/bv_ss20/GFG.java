package bv_ss20;

//https://www.geeksforgeeks.org/median-of-an-unsorted-array-in-liner-time-on/
//JAVA program to find median of 
//an array 
class GFG  
{ 
 static int a, b; 

 // Utility function to swapping of element 
 static int[] swap(int[] arr, int i, int j) 
 { 
     int temp = arr[i]; 
     arr[i] = arr[j]; 
     arr[j] = temp; 
     return arr; 
 } 

 // Returns the correct position of 
 // pivot element 
 static int Partition(int arr[], int l, int r) 
 { 
     int lst = arr[r], i = l, j = l; 
     while (j < r)  
     { 
         if (arr[j] < lst) 
         { 
             arr = swap(arr, i, j); 
             i++; 
         } 
         j++; 
     } 
     arr = swap(arr, i, r); 
     return i; 
 } 

 // Picks a random pivot element between 
 // l and r and partitions arr[l..r] 
 // around the randomly picked element 
 // using partition() 
 static int randomPartition(int arr[], int l, int r) 
 { 
     int n = r - l + 1; 
     int pivot = (int) (Math.random() % n); 
     arr = swap(arr, l + pivot, r); 
     return Partition(arr, l, r); 
 } 

 // Utility function to find median 
 static int MedianUtil(int arr[], int l, int r, int k)  
 { 

     // if l < r 
     if (l <= r)  
     { 

         // Find the partition index 
         int partitionIndex = randomPartition(arr, l, r); 

         // If partion index = k, then 
         // we found the median of odd 
         // number element in arr[] 
         if (partitionIndex == k) 
         { 
             b = arr[partitionIndex]; 
             if (a != -1) 
                 return Integer.MIN_VALUE; 
         } 

         // If index = k - 1, then we get 
         // a & b as middle element of 
         // arr[] 
         else if (partitionIndex == k - 1) 
         { 
             a = arr[partitionIndex]; 
             if (b != -1) 
                 return Integer.MIN_VALUE; 
         } 

         // If partitionIndex >= k then 
         // find the index in first half 
         // of the arr[] 
         if (partitionIndex >= k) 
             return MedianUtil(arr, l, partitionIndex - 1, k); 

         // If partitionIndex <= k then 
         // find the index in second half 
         // of the arr[] 
         else
             return MedianUtil(arr, partitionIndex + 1, r, k); 
     } 

     return Integer.MIN_VALUE; 
 } 

 // Function to find Median 
 static int findMedian(int arr[], int n) 
 { 
     int ans; 
     a = -1; 
     b = -1; 

     // If n is odd 
     if (n % 2 == 1)  
     { 
         MedianUtil(arr, 0, n - 1, n / 2); 
         ans = b; 
     } 

     // If n is even 
     else 
     { 
         MedianUtil(arr, 0, n - 1, n / 2); 
         ans = (a + b) / 2; 
     } 

     return ans;
 } 

} 

//This code is contributed by 29AjayKumar 
