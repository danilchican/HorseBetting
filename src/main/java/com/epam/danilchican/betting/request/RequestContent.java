package com.epam.danilchican.betting.request;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestContent {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Request attributes and parameters.
     */
    private HashMap<String, Object> requestAttributes;
    private HashMap<String, String[]> requestParameters;

    /**
     * Session attributes.
     */
    private HashMap<String, Object> sessionAttributes;

    /**
     * Default constructor.
     */
    public RequestContent() {
        this.requestAttributes = new HashMap<>();
        this.requestParameters = new HashMap<>();

        this.sessionAttributes = new HashMap<>();
    }

    /**
     * Extract values to maps from request.
     *
     * @param request
     */
    public void extractValues(HttpServletRequest request) {
        Map<String, String[]> paramsMap = request.getParameterMap();
        Enumeration<String> requestAttrNames = request.getAttributeNames();

        HttpSession session = request.getSession();
        Enumeration<String> sessionAttrNames = session.getAttributeNames();

        this.requestParameters.putAll(paramsMap);
        LOGGER.log(Level.INFO, "Request params saved.");

        while (requestAttrNames.hasMoreElements()) {
            String attrName = requestAttrNames.nextElement();
            Object attrValue = request.getAttribute(attrName);

            this.requestAttributes.put(attrName, attrValue);
        }

        LOGGER.log(Level.INFO, "Request attributes saved.");

        while (sessionAttrNames.hasMoreElements()) {
            String attrName = sessionAttrNames.nextElement();
            Object attrValue = session.getAttribute(attrName);

            this.sessionAttributes.put(attrName, attrValue);
        }

        LOGGER.log(Level.INFO, "Session attributes saved.");
    }

    /**
     * Insert values to maps from request.
     *
     * @param request
     */
    public void insertAttributes(HttpServletRequest request) {

        /* CHECK IF ITS RIGHT! */

        for (Map.Entry<String, Object> entry : requestAttributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            request.setAttribute(key, value);
        }

        for (Map.Entry<String, Object> entry : sessionAttributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            request.getSession().setAttribute(key, value);
        }

        request.getParameterMap().putAll(requestParameters);
    }
}
