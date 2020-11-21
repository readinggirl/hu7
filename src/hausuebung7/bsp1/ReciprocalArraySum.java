/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hausuebung7.bsp1;

/**
 *
 * @author bmayr
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Class wrapping methods for implementing reciprocal array sum in parallel.
 */
public final class ReciprocalArraySum {

    public static void main(String[] args) {
        double[] input = new double[60000];

        for (int i = 0; i < input.length; i++) {
            input[i] = Math.random() * 100;
        }

        ReciprocalArraySumTask rast = new ReciprocalArraySumTask(0, input.length - 1, input);

        rast.compute();

        parManyTaskArraySum(input, 2);
    }

    /**
     * Default constructor.
     */
    private ReciprocalArraySum() {

    }

    /**
     * Sequentially compute the sum of the reciprocal values for a given array.
     *
     * @param input Input array
     * @return The sum of the reciprocals of the array input
     */
    protected static double seqArraySum(final double[] input) {
        // ToDo: Compute sum of reciprocals of array elements
        double sum = 0;
        for (int i = 0; i < input.length; i++) {
            sum += (1 / input[i]);
        }
        return sum;
    }

    /**
     * This class stub can be filled in to implement the body of each task
     * created to perform reciprocal array sum in parallel.
     */
    private static class ReciprocalArraySumTask extends RecursiveAction {

        /**
         * Starting index for traversal done by this task.
         */
        private final int startIndexInclusive;
        /**
         * Ending index for traversal done by this task.
         */
        private final int endIndexExclusive;
        /**
         * Input array to reciprocal sum.
         */
        private final double[] input;
        /**
         * Intermediate value produced by this task.
         */
        private double value;

        private static final int SEQUENTIAL_THRESHOLD = 50000;

        /**
         * Constructor.
         *
         * @param setStartIndexInclusive Set the starting index to begin
         * parallel traversal at.
         * @param setEndIndexExclusive Set ending index for parallel traversal.
         * @param setInput Input values
         */
        ReciprocalArraySumTask(final int setStartIndexInclusive, final int setEndIndexExclusive, final double[] setInput) {
            this.startIndexInclusive = setStartIndexInclusive;
            this.endIndexExclusive = setEndIndexExclusive;
            this.input = setInput;
        }

        /**
         * Getter for the value produced by this task.
         *
         * @return Value produced by this task
         */
        public double getValue() {
            return value;
        }

        @Override
        protected void compute() {
            // TODO: Implement Thread forking on Threshold value. (If size of
            // array smaller than threshold: compute sequentially else, fork
            // 2 new threads
            if (input.length <= SEQUENTIAL_THRESHOLD) { // base case
                double sum = seqArraySum(input);
                System.out.println(sum);
            } else { // recursive case
                // Calculate new range
                int mid = input.length / 2;

                double[] first = Arrays.copyOfRange(input, 0, mid);
                double[] second = Arrays.copyOfRange(input, mid, input.length - 1);

                ReciprocalArraySumTask firstHalf = new ReciprocalArraySumTask(startIndexInclusive, mid, first);
                ReciprocalArraySumTask secondHalf = new ReciprocalArraySumTask(mid, endIndexExclusive, second);

                invokeAll(firstHalf, secondHalf);
            }

        }
    }

    /**
     * TODO: Extend the work you did to implement parArraySum to use a set
     * number of tasks to compute the reciprocal array sum.
     *
     * @param input Input array
     * @param numTasks The number of tasks to create
     * @return The sum of the reciprocals of the array input
     */
    protected static double parManyTaskArraySum(final double[] input, final int numTasks) {
        double sum = 0;
        // ToDo: Start Calculation with help of ForkJoinPool
        ForkJoinPool fjp = new ForkJoinPool(numTasks);
        List<ReciprocalArraySumTask> reciprocalArraySumTasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            ReciprocalArraySumTask t = new ReciprocalArraySumTask(0, input.length - 1, input);
            ForkJoinPool.commonPool().invoke(t);
            sum += t.getValue();
        }
        return sum;
    }
}
