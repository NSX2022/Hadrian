import utils.*;

public class TestPrimes {

    public static void main(String[] args){
        long[] p = Primes.getPrimes(8);
        assert Primes.isPrime(p[0]) && Primes.isPrime(p[1]);

        System.out.println("Prime1: "+ p[0] +" Prime2: "+ p[1] +" Public Num: "+ p[0] * p[1]);
    }
}
