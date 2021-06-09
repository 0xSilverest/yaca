package com.ensate.chatapp.utils.bits;

/**
 * Created by krzysztofkaczor on 3/9/15.
 */
public class AdditionModuloOperator implements BinaryOperator {

    @Override
    public BitArray combine(BitArray operand1, BitArray operand2) {
        if(operand1.size() != operand2.size()) {
            throw new IllegalArgumentException("AdditionModuloOperator operands must have same size");
        }

        int size = operand1.size();
        BitArray result = new BitArray(size);

        boolean overflow = false;
        boolean set = false;
        for(int i = size-1;i >= 0;i--) {
            boolean a = operand1.get(i);
            boolean b = operand2.get(i);

            if (a && b || a && overflow || b && overflow) {
                set = a && b && overflow;
                overflow = true;
            } else {
                set = overflow || a || b;
                overflow = false;
            }
            result.set(i, set);
        }

        return result;
    }
}
