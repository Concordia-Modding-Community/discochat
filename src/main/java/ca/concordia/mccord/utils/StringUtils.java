package ca.concordia.mccord.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {
    /**
     * Splits string by regex into List.
     * 
     * @param string
     * @return
     */
    public static List<String> tokenize(String string, String regex) {
        return Arrays.asList(string.strip().split(regex));
    }

    /**
     * Splits string by spaces.
     * 
     * @param string
     * @return
     */
    public static List<String> tokenize(String string) {
        return tokenize(string, " ");
    }
}
