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
    public static BigInteger[] generatePrimes(int digits) {
        SecureRandom secRand = new SecureRandom();


        BigInteger digitMin = BigInteger.TEN.pow(digits);
        BigInteger digitMax = BigInteger.valueOf(9).multiply(digitMin);

        BigInteger[] toRet = { BigInteger.valueOf(4), BigInteger.valueOf(4) };

        // Certainty = how many rounds of the approximate prime test. Change to 100 if 256 takes too long
        // Less that 2 * (1/2^certainty) chance that the number isn't prime
        while (!toRet[0].isProbablePrime(256))
            toRet[0] = new BigInteger(digitMax.bitLength(), secRand)
                    .mod(digitMax.subtract(digitMin))
                    .add(digitMin);

        while (!toRet[1].isProbablePrime(256))
            toRet[1] = new BigInteger(digitMax.bitLength(), secRand)
                    .mod(digitMax.subtract(digitMin))
                    .add(digitMin);

        return toRet;
    }
    
    /**
     * Return the two private prime factors of the large public number
     *
     * @param pubNum A very large non-prime number, that is the product of two prime numbers
     * @return Two prime numbers that produce {@code pubNum}
     */
    public static BigInteger[] calcRoots(BigInteger pubNum) {
        BigInteger digits = BigInteger.valueOf(
                (long) Math.floor(Math.log10(pubNum.abs().doubleValue())) + 1
        );

        BigInteger digitMin = BigInteger.TEN.pow(digits.intValue());
        BigInteger digitMax = BigInteger.valueOf(9).multiply(digitMin);

        SecureRandom secRand = new SecureRandom();
        BigInteger[] toRet = { BigInteger.valueOf(4), BigInteger.valueOf(4) };

        while (!toRet[0].multiply(toRet[1]).equals(pubNum)) {
            while (!toRet[0].isProbablePrime(100))
                toRet[0] = new BigInteger(digitMax.bitLength(), secRand)
                        .mod(digitMax.subtract(digitMin))
                        .add(digitMin);

            while (!toRet[1].isProbablePrime(100))
                toRet[1] = new BigInteger(digitMax.bitLength(), secRand)
                        .mod(digitMax.subtract(digitMin))
                        .add(digitMin);
        }

        return toRet;
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
