package com.epam.horsebetting.router;

public class Router {

    /**
     * Route type.
     */
    public enum RouteType {
        FORWARD, REDIRECT
    }

    /**
     * Route to page.
     */
    private String route;

    /**
     * Type of route.
     */
    private RouteType type;

    /**
     * Router instance name.
     */
    public static final String ROUTER_INSTANCE_NAME = "routerInstance";

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

    @Override
    public String toString() {
        return "Router{" + "route='" + route + '\'' + ", type=" + type + '}';
    }
}
