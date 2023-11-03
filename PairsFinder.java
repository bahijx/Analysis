
import java.util.Arrays;
import java.util.ArrayList;

public class PairsFinder {

    public static void mergeSort(int[] arr) {
        if (arr.length <= 1)
            return;
        int mid = arr.length / 2;
        int[] left = Arrays.copyOfRange(arr, 0, mid);
        int[] right = Arrays.copyOfRange(arr, mid, arr.length);
        mergeSort(left);
        mergeSort(right);
        merge(arr, left, right);
    }

    public static void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            if (left[i] < right[j])
                arr[k++] = left[i++];
            else
                arr[k++] = right[j++];
        }
        while (i < left.length)
            arr[k++] = left[i++];

        while (j < right.length)
            arr[k++] = right[j++];

    }

    public static boolean binarySearchRecursive(int[] arr, int target, int left, int right) {
        if (left > right)
            return false; // Element not found

        int mid = left + (right - left) / 2;

        if (arr[mid] == target)
            return true; // Element found
        else if (arr[mid] < target)
            return binarySearchRecursive(arr, target, mid + 1, right); // Search the right half
        else
            return binarySearchRecursive(arr, target, left, mid - 1); // Search the left half

    }

    public static void findPairsWithSum(int[] arr, int targetSum) {
        mergeSort(arr);
        ArrayList<Integer> pairs = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            int complement = targetSum - arr[i];
            if (binarySearchRecursive(arr, complement, i + 1, arr.length - 1)) {
                pairs.add(arr[i]);
                pairs.add(complement);
            }
        }

        // Print the unique pairs
        for (int i = 0; i < pairs.size(); i += 2)
            System.out.println(pairs.get(i) + " " + pairs.get(i + 1));

    }

    public static void main(String[] args) {
        int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
        int targetSum = 10;
        findPairsWithSum(arr, targetSum);
    }
}
