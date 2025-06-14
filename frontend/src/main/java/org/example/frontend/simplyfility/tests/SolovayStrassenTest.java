package org.example.frontend.simplyfility.tests;


import org.example.frontend.simplyfility.BasePrimeAbstr;

import java.math.BigInteger;

import static org.example.frontend.stateless.Math.jacobi;
import static org.example.frontend.stateless.Math.powMod;

public class SolovayStrassenTest extends BasePrimeAbstr {
    @Override
    protected boolean oneIteration(BigInteger checkingValue, BigInteger a) {
        BigInteger jacob = BigInteger.valueOf(jacobi(a, checkingValue));
        if (jacob.equals(BigInteger.ZERO)) {
            return false;
        }
        BigInteger modulo = powMod(a, checkingValue.subtract(BigInteger.ONE).divide(BigInteger.TWO) , checkingValue);
        jacob = jacob.mod(checkingValue).add(checkingValue).mod(checkingValue);
        return jacob.equals(modulo);
    }
}
