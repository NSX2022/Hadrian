package utils;

import java.security.SecureRandom;

//We could use an encryption library like BouncyCastle for this
public class Primes {

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
    /*public static long[] calcRoots(long pubNum) {

    }
     */

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
}
