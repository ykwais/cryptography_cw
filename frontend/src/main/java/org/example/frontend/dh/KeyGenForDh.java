package org.example.frontend.dh;

import org.example.frontend.simplyfility.SimplifilityInterface;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyGenForDh {
    private final SecureRandom rng = new SecureRandom();
    private final SimplifilityInterface primaryTest;
    private final double probability;
    private final int bitLength;


    public KeyGenForDh(SimplifilityInterface primaryTest, double probability, int bitLength) {
        if (probability < 0.5 || probability >= 1.0) {
            throw new IllegalArgumentException("Probability must be in [0.5, 1)");
        }
        if (bitLength < 3) {
            throw new IllegalArgumentException("Bit length is too small");
        }
        this.primaryTest = primaryTest;
        this.probability = probability;
        this.bitLength = bitLength;
    }


    public BigInteger generatePrimaryNumberDh() {
        BigInteger candidate;
        boolean probablyPrime;

        int remBits = bitLength & 7;
        int size = (bitLength + 7) >> 3;

        byte[] buffer = new byte[size];
        while (true) {

            rng.nextBytes(buffer);


            if (remBits == 0) {
                remBits = 8;
            }

            buffer[size - 1] |= (byte) (1 << (remBits - 1));

            buffer[0] |= (byte) 0x01;


            candidate = new BigInteger(1, buffer);


            probablyPrime = primaryTest.isSimple(candidate, probability);

            if (probablyPrime) {

                BigInteger pCandidate = candidate.shiftLeft(1).add(BigInteger.ONE);


                if (primaryTest.isSimple(pCandidate, probability)) {
                    return pCandidate;
                }

            }

        }
    }


    public BigInteger generateCandidate() {

        BigInteger candidate = new BigInteger(bitLength, rng);

        candidate = candidate.setBit(bitLength - 1);

        candidate = candidate.setBit(0);
        return candidate;
    }


    public BigInteger generatePrimeAsync() {
        return generatePrimaryNumberDh();
    }
}
