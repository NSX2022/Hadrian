package utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;

/*We could use an encryption library like BouncyCastle for this? Would probably be funner
  to make on our own
*/
public class Primes {
    //TODO Use BigInteger for truly massive public keys
    //See Obsidian notes for my thoughts on how we should handle public key size
    
    /**
     * Returns two large prime numbers that are {@code digits} number of digits long
     *
     * @param digits Number representing the length of each large prime number
     * @return An array containing only two large prime numbers
     */
    public static long[] generatePrimes(long digits) {
        // Naive implementation
        SecureRandom secRand = new SecureRandom();
        
        long[] toRet = { 4, 4 };  // initialize both numbers as the first non-prime numbers, to fail isPrime() check
        
        long digitMin = (long) Math.pow(10, digits), digitMax = 9 * digitMin;
        
        while (!isPrime(toRet[0]))
            toRet[0] = secRand.nextLong(digitMin, digitMax);
        
        while (!isPrime(toRet[1]))
            toRet[1] = secRand.nextLong(digitMin, digitMax);
        
        return toRet;
    }
    
    /**
     * Return the two private prime factors of the large public number
     *
     * @param pubNum A very large non-prime number, that is the product of two prime numbers
     * @return Two prime numbers that produce {@code pubNum}
     */
    public static long[] calcRoots(long pubNum) {
        long[] toRet = { 4, 4 };  // initialize both numbers as the first non-prime numbers, to fail isPrime() check
        
        long digits = (long) Math.floor(Math.log10(Math.abs(pubNum))) + 1;
        SecureRandom secRand = new SecureRandom();
        
        while (toRet[0] * toRet[1] != pubNum) {
            long digitMin = (long) Math.pow(10, digits), digitMax = 9 * digitMin;
            
            while (!isPrime(toRet[0]))
                toRet[0] = secRand.nextLong(digitMin, digitMax);
            
            while (!isPrime(toRet[1]))
                toRet[1] = secRand.nextLong(digitMin, digitMax);
        }
        
        return toRet;
    }
    
    
    /**
     * Check if {@code number} parameter is a prime number
     *
     * @param number large number to prime check
     * @return true if {@code number} is a prime number, false otherwise
     */
    public static boolean isPrime(long number) {
        for (int i = 2; i <= number / i; i++)
            if (number % i == 0)
                return false;
        return true;
    }
    
    /**
     * Generate an extremely large integer between the given start and end values, as a BigInteger object.
     * <p>
     * Uses BigDecimal to represent largest and smallest possible values,
     * then maps it to the given range as a BigInteger object.
     *
     * @param rangeStart A large BigInteger value, the smallest possible return value
     * @param rangeEnd   A large BigInteger value, the largest possible return value
     * @return A random BigInteger value, between {@code rangeStart} and {@code rangeEnd} (both inclusive)
     * @see <a href="https://stackoverflow.com/a/70607245">StackOverflow post by Panibo (2026-02-03, License - CC BY-SA 4.0)</a>
     * @see BigInteger
     * @see BigDecimal
     */
    private static BigInteger randomBigInteger(BigInteger rangeStart, BigInteger rangeEnd) {
        SecureRandom secRand = new SecureRandom();
        
        int scale = rangeEnd.toString().length();
        
        StringBuilder generated = new StringBuilder();
        for (int i = 0; i < scale; i++)
            generated.append(secRand.nextInt(10));
        
        BigDecimal inputRangeStart = new BigDecimal("0").setScale(scale, RoundingMode.FLOOR),
                inputRangeEnd = new BigDecimal(String.format("%0" + (scale) + "d", 0)
                                                     .replace('0', '9')).setScale(scale, RoundingMode.FLOOR),
                outputRangeStart = new BigDecimal(rangeStart).setScale(scale, RoundingMode.FLOOR),
                // Adds 1 to the output range to correct rounding
                outputRangeEnd = new BigDecimal(rangeEnd).add(new BigDecimal("1"))
                                                         .setScale(scale, RoundingMode.FLOOR),
                
                // Calculates: (generated - inputRangeStart) / (inputRangeEnd - inputRangeStart) * (outputRangeEnd - outputRangeStart) + outputRangeStart
                bd1 = new BigDecimal(new BigInteger(generated.toString())).setScale(scale, RoundingMode.FLOOR)
                                                                          .subtract(inputRangeStart),
                bd2 = inputRangeEnd.subtract(inputRangeStart),
                bd3 = bd1.divide(bd2, RoundingMode.FLOOR),
                bd4 = outputRangeEnd.subtract(outputRangeStart),
                bd5 = bd3.multiply(bd4),
                bd6 = bd5.add(outputRangeStart);
        
        BigInteger returnInteger = bd6.setScale(0, RoundingMode.FLOOR).toBigInteger();
        // Converts number to the end of output range if it's over it. This is to correct rounding.
        return returnInteger.compareTo(rangeEnd) > 0 ? rangeEnd : returnInteger;
    }
}
