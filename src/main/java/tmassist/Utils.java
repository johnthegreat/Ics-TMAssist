package tmassist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static String[] splitToLengthIfPossible(final String message, int maxLength, final String delimiter) {
        final List<String> strings = new ArrayList<>();

        String[] messagePieces = message.split(delimiter);
        while(messagePieces.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(maxLength);
            int i=0;
            if (messagePieces.length > 1) {
                for (; i < messagePieces.length; i++) {
                    if (stringBuilder.length() + messagePieces[i].length() + delimiter.length() <= maxLength) {
                        stringBuilder.append(messagePieces[i]).append(delimiter);
                    } else {
                        break;
                    }
                }
            } else {
                stringBuilder.append(messagePieces[i]);
                i++;
            }
            strings.add(stringBuilder.toString());
            if (messagePieces.length >= i) {
                messagePieces = Arrays.copyOfRange(messagePieces, i, messagePieces.length);
            }
        }

        return strings.toArray(new String[0]);
    }
}
