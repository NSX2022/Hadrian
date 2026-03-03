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

        // Certainty = how many rounds of the approximate prime test.
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

        // MULTIPLE OCCURRENCES ARE INTENTIONAL HERE
        while (!toRet[0].multiply(toRet[1]).equals(pubNum)) {
            while (!toRet[0].isProbablePrime(256))
                toRet[0] = new BigInteger(digitMax.bitLength(), secRand)
                        .mod(digitMax.subtract(digitMin))
                        .add(digitMin);

            while (!toRet[1].isProbablePrime(256))
                toRet[1] = new BigInteger(digitMax.bitLength(), secRand)
                        .mod(digitMax.subtract(digitMin))
                        .add(digitMin);
        }

        return toRet;
    }
}
