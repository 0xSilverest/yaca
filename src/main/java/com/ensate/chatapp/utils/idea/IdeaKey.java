package com.ensate.chatapp.utils.idea;

import com.ensate.chatapp.utils.bits.BitArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krzysztofkaczor on 3/11/15.
 */
public class IdeaKey {
    private BitArray key;

    public IdeaKey(BitArray key) {
        this.key = key;
    }

    public IdeaKey() {
        this.key = new BitArray(128);
    }

    public void setK(int id, int n) {
        BitArray bitArray = new BitArray(n, 16);

        for (int i = 0;i < 16;i++) {
            key.set(id*16 + i, bitArray.get(i));
        }
    }

    public BitArray getSubkey(int id) {
        BitArray subKey = new BitArray(16);

        for(int i = 0;i < 16;i++) {
            subKey.set(i, key.get(id * 16 + i));
        }

        return subKey;
    }

    @Override
    public String toString() {
        return key.toHexString();
    }

    public String toBinString() {
        return key.toBinString();
    }

    public IdeaKey generateNextKey() {
        return new IdeaKey(key.rotateLeft(25));
    }

    public List<BitArray> getSubkeys() {
        ArrayList<BitArray> subkeys = new ArrayList<BitArray>();

        for (int i = 0;i < 8;i++) {
            subkeys.add(getSubkey(i));
        }
        return subkeys;
    }

    public List<BitArray> getHalfOfSubkeys() {
        ArrayList<BitArray> subkeys = new ArrayList<BitArray>();

        for (int i = 0;i < 4;i++) {
            subkeys.add(getSubkey(i));
        }
        return subkeys;
    }
}
