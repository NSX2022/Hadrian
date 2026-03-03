import utils.*;

import java.math.BigInteger;
import java.util.Objects;

public class TestPrimes {

    public static void main(String[] args){
        BigInteger[] p = Primes.generatePrimes(1005);
        BigInteger sum = p[0].multiply(p[1]);

        System.out.println("Prime1: "+ p[0] +"\nPrime2: "+ p[1] +"\nPublic Num: "+ sum);

        BigInteger[] b = {p[0], p[1]};
        assert (Objects.equals(Primes.calcRoots(sum), b));

    }
}
