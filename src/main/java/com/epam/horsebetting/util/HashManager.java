package com.epam.horsebetting.util;

import com.epam.horsebetting.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

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
