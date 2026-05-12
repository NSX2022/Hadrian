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
     * Returns two <em>distinct</em> large prime numbers that are {@code digits} digits long.
     *
     * <p>Primality is verified with Miller–Rabin at {@link Constants#HIGH_CERTAINTY} rounds.
     * Both candidates are re-drawn together if they turn out to be equal, which is
     * astronomically unlikely to occur more than once in practice.</p>
     *
     * <p>The {@code digits} value also controls how long {@link #calcRoots} takes on the
     * receiving end — larger values mean a harder factoring challenge:</p>
     * <ul>
     *   <li>7 digits → ~10⁷ trial steps → tens of milliseconds</li>
     *   <li>8 digits → ~10⁸ trial steps → a few seconds</li>
     *   <li>9 digits → ~10⁹ trial steps → tens of seconds (likely too slow)</li>
     * </ul>
     *
     * @param digits number of digits in each prime
     * @return {@code [p, q]} where p ≠ q and both are prime
     */
    public static BigInteger[] generatePrimes(int digits) {
        SecureRandom secRand   = new SecureRandom();
        BigInteger   digitMin  = BigInteger.TEN.pow(digits);
        BigInteger   digitMax  = BigInteger.valueOf(9).multiply(digitMin);
        BigInteger   range     = digitMax.subtract(digitMin);
        int          bitLen    = digitMax.bitLength();
        int          certainty = Constants.HIGH_CERTAINTY.value();

        BigInteger[] toRet = { BigInteger.valueOf(4), BigInteger.valueOf(4) };

        do {
            // Certainty = how many rounds of the Miller-Rabin primality test.
            // Less than 2 x (1/2^certainty) chance that the number isn't actually prime.
            while (!toRet[0].isProbablePrime(certainty))
                toRet[0] = new BigInteger(bitLen, secRand).mod(range).add(digitMin);

            while (!toRet[1].isProbablePrime(certainty))
                toRet[1] = new BigInteger(bitLen, secRand).mod(range).add(digitMin);

        } while (toRet[0].equals(toRet[1])); // guarantee p != q

        return toRet;
    }

    /**
     * Returns the two distinct prime factors of {@code pubNum} using wheel-factorised
     * trial division (2, 3, then all numbers of the form 6k±1).
     *
     * <p>Skipping multiples of 2 and 3 reduces the number of trial divisions by ~67%
     * compared to a naive loop, making the computation take a meaningful but not
     * unreasonable amount of time for the prime sizes produced by
     * {@link #generatePrimes(int)}.</p>
     *
     * <p>Because {@code pubNum} is a semiprime (product of exactly two distinct primes),
     * the first divisor found is necessarily the smaller prime; dividing immediately
     * yields the larger one.</p>
     *
     * @param pubNum a semiprime: the product of exactly two distinct primes
     * @return {@code [smallerFactor, largerFactor]}
     * @throws ArithmeticException if no factor <= sqrt(pubNum) exists (pubNum is itself
     *                             prime, which must not happen in normal use)
     */
    public static BigInteger[] calcRoots(BigInteger pubNum) {
        BigInteger TWO   = BigInteger.TWO;
        BigInteger THREE = BigInteger.valueOf(3);
        BigInteger SIX   = BigInteger.valueOf(6);

        // Handle small factors first
        if (pubNum.mod(TWO).equals(BigInteger.ZERO))
            return new BigInteger[]{ TWO, pubNum.divide(TWO) };
        if (pubNum.mod(THREE).equals(BigInteger.ZERO))
            return new BigInteger[]{ THREE, pubNum.divide(THREE) };

        // Wheel: only test candidates of the form 6k-1 and 6k+1.
        // Every prime > 3 has this form, so no valid factor is ever skipped.
        BigInteger limit = sqrt(pubNum);
        for (BigInteger k = BigInteger.valueOf(6);
             k.subtract(BigInteger.ONE).compareTo(limit) <= 0;
             k = k.add(SIX)) {

            BigInteger candidate = k.subtract(BigInteger.ONE); // 6k - 1
            if (pubNum.mod(candidate).equals(BigInteger.ZERO))
                return new BigInteger[]{ candidate, pubNum.divide(candidate) };

            candidate = k.add(BigInteger.ONE);                 // 6k + 1
            if (pubNum.mod(candidate).equals(BigInteger.ZERO))
                return new BigInteger[]{ candidate, pubNum.divide(candidate) };
        }

        throw new ArithmeticException(
                "No factor found - pubNum does not appear to be a semiprime: " + pubNum);
    }

    /**
     * Returns the integer square root of {@code n} (i.e. floor(sqrt(n))),
     * using Newton's method on BigIntegers.
     */
    private static BigInteger sqrt(BigInteger n) {
        if (n.signum() < 0) throw new ArithmeticException("Square root of negative number");
        if (n.signum() == 0) return BigInteger.ZERO;

        BigInteger x = BigInteger.ONE.shiftLeft((n.bitLength() + 1) / 2); // initial estimate
        while (true) {
            BigInteger y = x.add(n.divide(x)).shiftRight(1);
            if (y.compareTo(x) >= 0) return x;
            x = y;
        }
    }

}
