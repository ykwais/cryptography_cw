package org.example.frontend.dh;

import org.example.frontend.simplyfility.SimplifilityInterface;
import org.example.frontend.simplyfility.tests.MillerRabinTest;

import java.math.BigInteger;

import java.util.List;
import java.util.concurrent.*;

public class Protocol {

    private static final double PROBABILITY = 0.9999;

    private static final int DEFAULT_BITLEN = 5;


    public static Pair<BigInteger, BigInteger> generatePairParallel(int bitlen)
            throws InterruptedException, ExecutionException {


        ExecutorService executor = Executors.newFixedThreadPool(2);

        SimplifilityInterface primeTest = new MillerRabinTest();


        Callable<Pair<BigInteger, BigInteger>> task = () -> {
            KeyGenForDh gen = new KeyGenForDh(primeTest, PROBABILITY, bitlen);
            while (true) {

                BigInteger pCandidate = gen.generatePrimaryNumberDh();
                BigInteger phi = pCandidate.subtract(BigInteger.ONE);
                BigInteger q = phi.shiftRight(1);


                BigInteger gCandidate = BigInteger.valueOf(2);

                    if (!gCandidate.modPow(phi.divide(q), pCandidate).equals(BigInteger.ONE)
                            && !gCandidate.modPow(phi.shiftRight(1), pCandidate).equals(BigInteger.ONE)) {

                        return new Pair<>(pCandidate, gCandidate);
                    }


            }
        };

        List<Callable<Pair<BigInteger, BigInteger>>> tasks = List.of(task, task);

        Pair<BigInteger, BigInteger> result = executor.invokeAny(tasks);


        executor.shutdownNow();

        return result;
    }


    public static Pair<BigInteger, BigInteger> generatePairParallel() throws InterruptedException, ExecutionException {
        return generatePairParallel(DEFAULT_BITLEN);
    }


    public static Pair<BigInteger, BigInteger> generatePair(int bitlen) {
        SimplifilityInterface primeTest = new MillerRabinTest();
        KeyGenForDh gen = new KeyGenForDh(primeTest, PROBABILITY, bitlen);

        BigInteger pCandidate;
        BigInteger gCandidate = null;
        while (true) {

            pCandidate = gen.generatePrimaryNumberDh();
            BigInteger phi = pCandidate.subtract(BigInteger.ONE);
            BigInteger q = phi.shiftRight(1);


            boolean found = false;
            for (BigInteger probG = BigInteger.valueOf(2); probG.compareTo(phi) < 0; probG = probG.add(BigInteger.ONE)) {
                if (!probG.modPow(phi.divide(q), pCandidate).equals(BigInteger.ONE)
                        && !probG.modPow(phi.shiftRight(1), pCandidate).equals(BigInteger.ONE)) {
                    gCandidate = probG;
                    found = true;
                    break;
                }
            }
            if (found) {
                return new Pair<>(pCandidate, gCandidate);
            }

        }
    }


    public static Pair<BigInteger, BigInteger> generatePair() {
        return generatePair(DEFAULT_BITLEN);
    }


    public static BigInteger generateDhKeys(BigInteger g, BigInteger secret, BigInteger p) {

        return g.modPow(secret, p);
    }


    public static BigInteger calculateSharedSecret(BigInteger mySecret, BigInteger otherPublic, BigInteger p) {

        return otherPublic.modPow(mySecret, p);
    }


    public static BigInteger generateSecret(int bitlen) {
        KeyGenForDh gen = new KeyGenForDh(new MillerRabinTest(), PROBABILITY, bitlen);
        return gen.generateCandidate();
    }

    public static BigInteger generateSecret() {
        return generateSecret(DEFAULT_BITLEN);
    }

    public static BigInteger getBigIntegerFromArray(byte[] array) {
        return new BigInteger(1, array);
    }
}
