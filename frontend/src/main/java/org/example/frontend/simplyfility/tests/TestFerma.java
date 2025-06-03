package org.example.frontend.simplyfility.tests;


import org.example.frontend.simplyfility.BasePrimeAbstr;

import java.math.BigInteger;

import static org.example.frontend.stateless.Math.powMod;

public class TestFerma extends BasePrimeAbstr {

    @Override
    protected boolean oneIteration(BigInteger checkingValue, BigInteger a) {
        return BigInteger.ONE.equals(powMod(a, checkingValue.subtract(BigInteger.ONE), checkingValue));
    }
}
