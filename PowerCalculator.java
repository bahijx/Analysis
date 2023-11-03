import java.math.BigInteger;

public class PowerCalculator {
    public static BigInteger power(BigInteger base, int exponent) {
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < exponent; i++) {
            result = result.multiply(base);
        }
        return result;
    }

    public static BigInteger power2(BigInteger base, int exponent) {
        if (exponent == 0) {
            return BigInteger.ONE;
        } else if (exponent % 2 == 0) {
            BigInteger halfResult = power2(base, exponent / 2);
            return halfResult.multiply(halfResult);
        } else {
            BigInteger halfResult = power2(base, (exponent - 1) / 2);
            return base.multiply(halfResult.multiply(halfResult));
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        power(new BigInteger("3"), 180000);
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");
        startTime = System.currentTimeMillis();
        power2(new BigInteger("3"), 180000);
        endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");
    }
}
