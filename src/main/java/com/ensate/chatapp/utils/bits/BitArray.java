package com.ensate.chatapp.utils.bits;

import java.math.BigInteger;
import java.io.Serializable;

public class BitArray implements Serializable{
    private final int size;
    private boolean bitArray[];

    public BitArray(int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }

        bitArray = new boolean[size];
        this.size = size;
    }

    public BitArray(boolean[] bitArray) {
        this.bitArray = bitArray.clone();
        this.size = bitArray.length;
    }

    public BitArray(int value, int size) {
        this(size);

        String binString = Integer.toBinaryString(value); //i am lazy
        int offset = size - binString.length();

        if (offset < 0) {
            throw new IllegalArgumentException();
        }

        for(int i = 0; i < binString.length(); i++) {
            this.bitArray[i + offset] = binString.charAt(i) == '1';
        }
    }

    public boolean get(int i) {
        return bitArray[i];
    }

    public void set(int i, boolean value) {
        bitArray[i] = value;
    }

    public void swap(int i) {
        bitArray[i] = !bitArray[i];
    }

    @Override
    public String toString() {
//        return toDecString();
        if (this.size % 8 == 0)
            return this.toHexString(); //use hex notation if it is possible
        else
            return toBinString();
    }

    public String toBinString() {
        StringBuilder sb = new StringBuilder();
        for (boolean bit : bitArray) {
            sb.append(bit ? 1 : 0);
        }
        return "[" + sb.toString() + "]";
    }

    public String toHexString() {
        if (size % 8 != 0)
            throw new UnsupportedOperationException();

        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < size;i += 8) {
            int n = 0;
            for (int j = 0;j < 8;j++) {
                n <<= 1;
                n += bitArray[i + j]? 1 : 0;
            }

            //it also adds leading 0
            sb.append(Integer.toHexString(0x100 | n).substring(1) + " ");
        }
        return "[" + sb.deleteCharAt(sb.length() - 1).toString() + "]";
    }


    public String toDecString() {
        if (size % 16 != 0)
            throw new UnsupportedOperationException();

        int n = 0;
        for(int i = 0;i < size;i++) {
            n <<= 1;
            n += bitArray[i]? 1 : 0;
        }
        return "[" + n + "]";
    }

    public int toInt() {
        int n = 0;
        for(int i = 0;i < size;i++) {
            n <<= 1;
            n += bitArray[i]? 1 : 0;
        }
        return n;
    }

    public int size() {
        return size;
    }

    public BitArray shiftLeft(int n) {
        boolean[] newBitArray = new boolean[this.size + n];

        for(int i = 0;i < this.size;i++) {
            newBitArray[i] = this.bitArray[i];
        }

        return new BitArray(newBitArray);
    }

    public BitArray shiftRight(int n) {
        boolean[] newBitArray = new boolean[this.size + n];

        for(int i = 0;i < this.size;i++) {
            newBitArray[n+i] = this.bitArray[i];
        }

        return new BitArray(newBitArray);
    }

    public BitArray moduloShiftLeft(int n) {
        boolean[] newBitArray = new boolean[this.size];

        for(int i = 0;i < this.size;i++) {
            if (n+i < size) {
                newBitArray[i] = this.bitArray[n + i];
            }
        }

        return new BitArray(newBitArray);
    }

    public BitArray rotateLeft(int n) {
        boolean[] newBitArray = new boolean[this.size];

        for(int i = 0;i < this.size;i++) {
            newBitArray[i] = this.bitArray[(n + i) % size];
        }

        return new BitArray(newBitArray);
    }

    public BitArray cloneDeep() {
        return new BitArray(bitArray.clone());
    }

    public BitArray negate() {
        BitArray bitArray = new BitArray(this.size);
        for(int i = 0;i < size;i++){
            bitArray.set(i, !this.bitArray[i]);
        }

        return bitArray;
    }

    public BitArray invert() {
        int n = this.toInt();
        int i = BigInteger.valueOf(n).modInverse(BigInteger.valueOf(65537)).intValue();

        return new BitArray(i, 16);
    }

    public BitArray twosComplement() {
        AdditionModuloOperator additionModuloOperator = new AdditionModuloOperator();
        return additionModuloOperator.combine(this.negate(), new BitArray(1, this.size));
    }

    public String toASCII() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < size;i += 8) {
            int n = 0;
            for (int j = 0;j < 8;j++) {
                n <<= 1;
                n += bitArray[i + j]? 1 : 0;
            }

            //it also adds leading 0
            sb.append((char)n);
        }
        return sb.toString();
    }
}
