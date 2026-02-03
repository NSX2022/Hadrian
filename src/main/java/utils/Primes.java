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

    /// Returns two large prime numbers that are 'digits' long
    public static long[] getPrimes(long digits) {
        //Naive implementation
        SecureRandom secrand = new SecureRandom();

        //initialize both numbers to return as nonprimes with 'digits' digits
        long[] toRet = {(long) (4 * Math.pow(10, digits)), (long) (4 * Math.pow(10, digits))};
        while(!isPrime(toRet[0])){
            toRet[0] = secrand.nextLong((long) (1 * Math.pow(10, digits)), (long) (9 * Math.pow(10, digits)));
        }
        while(!isPrime(toRet[1])){
            toRet[1] = secrand.nextLong((long) (1 * Math.pow(10, digits)), (long) (9 * Math.pow(10, digits)));
        }

        return toRet;
    }

    /// Return the prime roots of a large number
    public static long[] calcRoots(long pubNum) {
        long[] toRet = {4,4};

        long digits = (long) Math.floor(Math.log10(Math.abs(pubNum))) + 1;
        SecureRandom secrand = new SecureRandom();

        while(!((toRet[0] * toRet[1]) == pubNum)){
            while(!isPrime(toRet[0])){
                toRet[0] = secrand.nextLong((long) (1 * Math.pow(10, digits)), (long) (9 * Math.pow(10, digits)));
            }
            while(!isPrime(toRet[1])){
                toRet[1] = secrand.nextLong((long) (1 * Math.pow(10, digits)), (long) (9 * Math.pow(10, digits)));
            }
        }

        return toRet;
    }


    public static boolean isPrime(long number){
        int i = 2;
        while(i <= (number/i)){
            if(number % i == 0){
                break;
            }
            i += 1;
        }
        return i > (number/i);
    }

    // A surprise tool that will help us later
    // Source - https://stackoverflow.com/a/70607245
    // Posted by Panibo
    // Retrieved 2026-02-03, License - CC BY-SA 4.0
    private static BigInteger RandomBigInteger(BigInteger rangeStart, BigInteger rangeEnd){

        SecureRandom rand = new SecureRandom();
        int scale = rangeEnd.toString().length();
        String generated = "";
        for(int i = 0; i < rangeEnd.toString().length(); i++){
            generated += rand.nextInt(10);
        }
        BigDecimal inputRangeStart = new BigDecimal("0").setScale(scale, RoundingMode.FLOOR);
        BigDecimal inputRangeEnd = new BigDecimal(String.format("%0" + (rangeEnd.toString().length()) +  "d", 0).replace('0', '9')).setScale(scale, RoundingMode.FLOOR);
        BigDecimal outputRangeStart = new BigDecimal(rangeStart).setScale(scale, RoundingMode.FLOOR);
        BigDecimal outputRangeEnd = new BigDecimal(rangeEnd).add(new BigDecimal("1")).setScale(scale, RoundingMode.FLOOR); //Adds one to the output range to correct rounding

        //Calculates: (generated - inputRangeStart) / (inputRangeEnd - inputRangeStart) * (outputRangeEnd - outputRangeStart) + outputRangeStart
        BigDecimal bd1 = new BigDecimal(new BigInteger(generated)).setScale(scale, RoundingMode.FLOOR).subtract(inputRangeStart);
        BigDecimal bd2 = inputRangeEnd.subtract(inputRangeStart);
        BigDecimal bd3 = bd1.divide(bd2, RoundingMode.FLOOR);
        BigDecimal bd4 = outputRangeEnd.subtract(outputRangeStart);
        BigDecimal bd5 = bd3.multiply(bd4);
        BigDecimal bd6 = bd5.add(outputRangeStart);

        BigInteger returnInteger = bd6.setScale(0, RoundingMode.FLOOR).toBigInteger();
        returnInteger = (returnInteger.compareTo(rangeEnd) > 0 ? rangeEnd : returnInteger); //Converts number to the end of output range if it's over it. This is to correct rounding.
        return returnInteger;
    }

}
