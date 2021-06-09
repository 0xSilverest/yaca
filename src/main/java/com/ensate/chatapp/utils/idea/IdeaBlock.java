package com.ensate.chatapp.utils.idea;

import com.ensate.chatapp.utils.bits.BitArray;

import java.util.IllegalFormatCodePointException;
import java.io.Serializable;

/**
 * Created by krzysztofkaczor on 3/10/15.
 */

//64bits block
public class IdeaBlock implements Serializable {
    private final BitArray bitArray;

    public IdeaBlock(BitArray bitArray) {
        if (bitArray.size() != 64)
            throw new IllegalArgumentException();
        this.bitArray = bitArray;
    }

    public IdeaBlock() {
        this(new BitArray(64));
    }

    public IdeaBlock(int leftHalf, int rightHalf) {
        bitArray = new BitArray(64);

        BitArray leftBitArray = new BitArray(leftHalf, 32);
        BitArray rightBitArray = new BitArray(rightHalf, 32);

        for(int i = 0;i < 32;i++) {
            bitArray.set(i, leftBitArray.get(i));
            bitArray.set(32 + i, rightBitArray.get(i));
        }
    }

    public IdeaBlock(BitArray a, BitArray b, BitArray c, BitArray d) {
        this();

        if (    a.size() != 16 ||
                b.size() != 16 ||
                c.size() != 16 ||
                d.size() != 16
                )
        {
            throw new IllegalArgumentException();
        }

        for(int i =0;i < 64;i++) {
            boolean bit = false;

            if (i < 16) {
                bit = a.get(i % 16);
            } else if (i < 32) {
                bit = b.get(i % 16);
            } else if (i < 48) {
                bit = c.get(i % 16);
            } else {
                bit = d.get(i % 16);
            }

            bitArray.set(i, bit);
        }
    }

    public BitArray getBitArray() {
        return bitArray;
    }

    public IdeaBlock(int... values) {
        this();
        if (values.length != 8) {
            throw new IllegalArgumentException();
        }

        BitArray[] bitArrays = new BitArray[8];
        for(int i = 0;i < 8;i++) {
            bitArrays[i] = new BitArray(values[i], 8);
        }

        for(int i =0;i < 64;i++) {
            boolean bit = false;

            int index = i / 8;
            int indexInBitArray = i % 8;

            bitArray.set(i, bitArrays[index].get(indexInBitArray));
        }
    }

    public String toHexString() {
        return bitArray.toHexString();
    }

    public BitArray[] split16() {
        BitArray a = new BitArray(16);
        BitArray b = new BitArray(16);
        BitArray c = new BitArray(16);
        BitArray d = new BitArray(16);


        for(int i =0;i < 64;i++) {
            boolean bit = bitArray.get(i);
            if (i < 16) {
                a.set(i % 16, bit);
            } else if (i < 32) {
                b.set(i % 16, bit);
            } else if (i < 48) {
                c.set(i % 16, bit);
            } else {
                d.set(i % 16, bit);
            }
        }
        return new BitArray[] {a, b, c, d};
    }

    public IdeaBlock cloneDeep() {
        return new IdeaBlock(bitArray.cloneDeep());
    }
}
