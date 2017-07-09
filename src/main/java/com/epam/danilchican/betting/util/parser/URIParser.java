package com.epam.danilchican.betting.util.parser;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URIParser {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Page chunk regular expression.
     * Take only chunk without any GET params.
     */
    private static final String PAGE_CHUNK_REGEX = "(\\/[a-z\\-\\d]+)";

    /**
     * Parse uri to chunk array list.
     *
     * @param uri
     * @return list of uri chunks
     */
    public ArrayList<String> parseURI(String uri) {
        Pattern pattern = Pattern.compile(PAGE_CHUNK_REGEX);
        Matcher matcher = pattern.matcher(uri);

        ArrayList<String> chunks = new ArrayList<>();

        while (matcher.find()) {
            String chunk = matcher.group();
            chunks.add(chunk.substring(1, chunk.length()));
        }

        LOGGER.log(Level.DEBUG, "URI chunks: " + chunks);
        return chunks;
    }
}
