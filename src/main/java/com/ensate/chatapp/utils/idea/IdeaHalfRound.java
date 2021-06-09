package com.ensate.chatapp.utils.idea;

import com.ensate.chatapp.utils.bits.AdditionModuloOperator;
import com.ensate.chatapp.utils.bits.BitArray;

/**
 * Created by krzysztofkaczor on 3/11/15.
 */
public class IdeaHalfRound {
    public IdeaBlock encrypt(IdeaBlock block, BitArray k1, BitArray k2, BitArray k3, BitArray k4) {
        if (    k1.size() != 16 ||
                k2.size() != 16 ||
                k3.size() != 16 ||
                k4.size() != 16 ) {
            throw new IllegalArgumentException();
        }

        IdeaBlock resultBlock = block.cloneDeep();

        AdditionModuloOperator additionModuloOperator = new AdditionModuloOperator();
        IdeaMultiplicationModuloOperator multiplicationModuloOperator = new IdeaMultiplicationModuloOperator();

        BitArray[] block16bits = resultBlock.split16();
        BitArray a = block16bits[0];
        BitArray b = block16bits[2]; //swapped
        BitArray c = block16bits[1]; //swapped
        BitArray d = block16bits[3];

        a = multiplicationModuloOperator.combine(a, k1);
        b = additionModuloOperator.combine(b, k2);
        c = additionModuloOperator.combine(c, k3);
        d = multiplicationModuloOperator.combine(d, k4);

        return new IdeaBlock(a, b, c, d);
    }
}
