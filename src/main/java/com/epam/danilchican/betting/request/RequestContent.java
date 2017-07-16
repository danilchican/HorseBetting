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
     * Request attributes.
     */
    private HashMap<String, Object> requestAttributes;

    /**
     * Session attributes.
     */
    private HashMap<String, Object> sessionAttributes;

    /**
     * Request parameters.
     */
    private HashMap<String, String> requestParameters;

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
        Enumeration<String> requestAttrNames = request.getAttributeNames();
        Enumeration paramNames = request.getParameterNames();

        HttpSession session = request.getSession();
        Enumeration<String> sessionAttrNames = session.getAttributeNames();

        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            requestParameters.put(paramName, request.getParameter(paramName));
        }

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
    public void insertValues(HttpServletRequest request) {
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
    }

    /**
     * Find request attribute value.
     *
     * @param key
     * @return
     */
    public Object findRequestAttribute(String key) {
        return this.requestAttributes.get(key);
    }

    /**
     * Remove attribute from request by key.
     *
     * @param key
     */
    public void removeRequestAttribute(String key) {
        this.requestAttributes.remove(key);
    }

    /**
     * Insert new attribute to request.
     *
     * @param key
     * @param value
     */
    public void insertRequestAttribute(String key, Object value) {
        this.requestAttributes.put(key, value);
    }

    /**
     * Find parameter in request parameters map.
     *
     * @param paramName
     * @return param value
     */
    public String findParameter(String paramName) {
        return this.requestParameters.get(paramName);
    }
}
