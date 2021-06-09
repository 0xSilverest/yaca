package com.ensate.chatapp.utils.bits;

/**
 * Created by krzysztofkaczor on 3/10/15.
 */
public class ExclusiveOrOperator implements BinaryOperator
{
    @Override
    public BitArray combine(BitArray operand1, BitArray operand2) {
        if(operand1.size() != operand2.size()) {
            throw new IllegalArgumentException("ExclusiveOrOperator operands must have same size");
        }

        int size = operand1.size();
        BitArray result = new BitArray(size);

        for (int i = 0;i < size;i++) {
            boolean a = operand1.get(i);
            boolean b = operand2.get(i);

            result.set(i, a != b );
        }

        return result;
    }
}
