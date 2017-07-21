package com.epam.horsebetting.request;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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
     * Request headers.
     */
    private HashMap<String, String> requestHeaders;

    /**
     * Session attribute names to remove.
     */
    private ArrayList<String> sessionAttrsToRemove;

    /**
     * Default constructor.
     */
    public RequestContent() {
        this.requestAttributes = new HashMap<>();
        this.requestParameters = new HashMap<>();
        this.requestHeaders = new HashMap<>();

        this.sessionAttributes = new HashMap<>();
        this.sessionAttrsToRemove = new ArrayList<>();
    }

    /**
     * Extract values to maps from request.
     *
     * @param request
     */
    public void extractValues(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Enumeration requestParameterNames = request.getParameterNames();

        while (requestParameterNames.hasMoreElements()) {
            String paramName = (String) requestParameterNames.nextElement();
            requestParameters.put(paramName, request.getParameter(paramName));
        }

        LOGGER.log(Level.INFO, "Request params saved.");
        Enumeration<String> requestAttrNames = request.getAttributeNames();

        while (requestAttrNames.hasMoreElements()) {
            String attrName = requestAttrNames.nextElement();
            Object attrValue = request.getAttribute(attrName);

            this.requestAttributes.put(attrName, attrValue);
        }

        LOGGER.log(Level.INFO, "Request attributes saved.");
        Enumeration<String> sessionAttrNames = session.getAttributeNames();

        while (sessionAttrNames.hasMoreElements()) {
            String attrName = sessionAttrNames.nextElement();
            Object attrValue = session.getAttribute(attrName);

            this.sessionAttributes.put(attrName, attrValue);
        }

        LOGGER.log(Level.INFO, "Session attributes saved.");
        Enumeration<String> requestHeaderNames = request.getHeaderNames();

        while (requestHeaderNames.hasMoreElements()) {
            String headerName = requestHeaderNames.nextElement();
            String headerValue = request.getHeader(headerName);

            this.requestHeaders.put(headerName, headerValue);
        }

        LOGGER.log(Level.INFO, "Request headers saved.");
    }

    /**
     * Remove attributes from session.
     *
     * @param session
     */
    private void removeSessionAttributes(HttpSession session) {
        this.sessionAttrsToRemove.forEach(session::removeAttribute);
    }

    /**
     * Insert values to maps from request.
     *
     * @param request
     */
    public void insertValues(HttpServletRequest request) {
        HttpSession session = request.getSession();

        for (Map.Entry<String, Object> entry : requestAttributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            request.setAttribute(key, value);
        }

        for (Map.Entry<String, Object> entry : sessionAttributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            session.setAttribute(key, value);
        }

        this.removeSessionAttributes(session);
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
     * Find session attribute value.
     *
     * @param key
     * @return
     */
    public Object findSessionAttribute(String key) {
        return this.sessionAttributes.get(key);
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
     * Remove attribute from session by key.
     *
     * @param key
     */
    public void removeSessionAttribute(String key) {
        this.sessionAttrsToRemove.add(key);
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
     * Insert new attribute to session.
     *
     * @param key
     * @param value
     */
    public void insertSessionAttribute(String key, Object value) {
        this.sessionAttributes.put(key, value);
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

    /**
     * Find header in request headers map.
     *
     * @param headerName
     * @return header value
     */
    public String findHeader(String headerName) {
        return this.requestHeaders.get(headerName);
    }
}
