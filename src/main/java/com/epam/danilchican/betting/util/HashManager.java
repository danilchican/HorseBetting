package com.epam.danilchican.betting.util;

import org.apache.commons.codec.digest.DigestUtils;

public class HashManager {

    /**
     * Make hash from String object.
     *
     * @param st
     * @return hash string
     */
    public static String make(String st) {
        return DigestUtils.md5Hex(st);
    }
}
