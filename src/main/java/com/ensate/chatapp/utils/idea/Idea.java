package com.ensate.chatapp.utils.idea;

import com.ensate.chatapp.utils.bits.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krzysztofkaczor on 3/11/15.
 */
public class Idea {
    private IdeaKey originalKey;
    private List<BitArray> encryptionKeys = new ArrayList<>();
    private List<BitArray> decryptionKeys = new ArrayList<>();

    public Idea(IdeaKey originalKey) {
        this.originalKey = originalKey;

        generateAllSubkeys();
    }

    private void generateAllSubkeys() {
        IdeaKey nextKey = originalKey;

        for(int i = 0;i < 6;i++) {
            encryptionKeys.addAll(nextKey.getSubkeys());
            nextKey = nextKey.generateNextKey();
        }
        encryptionKeys.addAll(nextKey.getHalfOfSubkeys());

        //generate decrypt keys
        //this part is a little bit tricky
        decryptionKeys.add(encryptionKeys.get(48).invert());
        decryptionKeys.add(encryptionKeys.get(49).twosComplement());
        decryptionKeys.add(encryptionKeys.get(50).twosComplement());
        decryptionKeys.add(encryptionKeys.get(51).invert());

        for(int i = 7;i >= 0; i--) {
            decryptionKeys.add(encryptionKeys.get(i * 6 + 4));
            decryptionKeys.add(encryptionKeys.get(i * 6 + 5));

            decryptionKeys.add(encryptionKeys.get(i * 6 + 0).invert());
            if (i == 0) {
                decryptionKeys.add(encryptionKeys.get(i * 6 + 1).twosComplement());
                decryptionKeys.add(encryptionKeys.get(i * 6 + 2).twosComplement());
            } else {
                decryptionKeys.add(encryptionKeys.get(i * 6 + 2).twosComplement());
                decryptionKeys.add(encryptionKeys.get(i * 6 + 1).twosComplement());
            }

            decryptionKeys.add(encryptionKeys.get(i * 6 + 3).invert());
        }
    }

    public IdeaBlock compute(IdeaBlock inputBlock, List<BitArray> keys) {
        IdeaRound round = new IdeaRound();
        IdeaHalfRound halfRound = new IdeaHalfRound();

        IdeaBlock resultBlock = inputBlock;
        for (int i = 0; i < 8;i++) {
            resultBlock = round.encrypt(resultBlock, keys.get(i * 6), keys.get(i * 6 + 1),
                    keys.get(i * 6 + 2), keys.get(i * 6 + 3), keys.get(i * 6 + 4),
                    keys.get(i * 6 + 5));
        }
        resultBlock = halfRound.encrypt(resultBlock, keys.get(48), keys.get(49), keys.get(50), keys.get(51));

        return resultBlock;
    }

    public IdeaBlock encrypt(IdeaBlock inputBlock) {
        return compute(inputBlock, encryptionKeys);
    }

    public IdeaBlock decrypt(IdeaBlock inputBlock) {
        return compute(inputBlock, decryptionKeys);
    }
}
