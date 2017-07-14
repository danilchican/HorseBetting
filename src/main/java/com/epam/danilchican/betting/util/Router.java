package com.epam.danilchican.betting.util;

import com.epam.danilchican.betting.type.RouteType;

public class Router {

    /**
     * Route to page.
     */
    private String route;

    /**
     * Type of route.
     */
    private RouteType type;

    /**
     * Constructor.
     *
     * @param route
     * @param type
     */
    public Router(String route, RouteType type) {
        this.route = route;
        this.type = type;
    }

    /**
     * Get page route.
     *
     * @return route
     */
    public String getRoute() {
        return route;
    }

    /**
     * Get route type.
     *
     * @return
     */
    public RouteType getType() {
        return type;
    }
}
