package com.epam.horsebetting.tag;

class Paginator {

    /**
     * Limit of entities to display on page.
     */
    private int limit = 10;

    /**
     * Total count entities in current table.
     */
    private int total = 0;

    /**
     * Current page number.
     */
    private int page = 1;

    /**
     * Describes count of links before and after
     * current page link.
     */
    private int countLinks = 3;

    /**
     * Postfix of the request URI.
     */
    private String postfix;

    /**
     * Class name of pagination list.
     */
    private String className = "pagination";

    /**
     * Disabled class name of link.
     */
    private String disabledClass = "disabled";

    /**
     * Active class name of link.
     */
    private String activeClass = "active";

    /**
     * Request URI.
     */
    private String requestURI;

    /**
     * Constructor.
     *
     * @param limit
     * @param total
     */
    Paginator(int limit, int total) {
        this.limit = limit;
        this.total = total;
    }

    /**
     * Set postfix of the request uri.
     *
     * @param postfix
     */
    void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    /**
     * Set current page.
     *
     * @param page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Set count of links to display.
     *
     * @param countLinks
     */
    public void setCountLinks(int countLinks) {
        this.countLinks = countLinks;
    }

    /**
     * Set class name of pagination.
     *
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Set name of disabled link.
     *
     * @param disabledClass
     */
    public void setDisabledClass(String disabledClass) {
        this.disabledClass = disabledClass;
    }

    /**
     * Set active class name.
     *
     * @param activeClass
     */
    public void setActiveClass(String activeClass) {
        this.activeClass = activeClass;
    }

    /**
     * Set request uri.
     *
     * @param uri
     */
    void setRequestURI(String uri) {
        this.requestURI = uri;
    }

    /**
     * Get request URI.
     *
     * @return request uri with postfix
     */
    private String getRequestURI() {
        return this.requestURI +
                ((postfix != null) ? postfix + "&" : "?") + "page=";
    }

    /**
     * Generate html code of navigation.
     *
     * @return
     */
    public String generate() {
        StringBuilder builder = new StringBuilder();

        final int totalPages = (int) Math.ceil((double) total / limit);
        final int startLink = ((page - countLinks) > 0) ? page - countLinks : 1;
        final int endLink = ((page + countLinks) < totalPages) ? page + countLinks : totalPages;

        builder.append("<ul class=\"").append(className).append("\">");

        generatePreviousLink(builder);

        if (startLink > 1) {
            builder.append("<li><a href=\"").append(this.getRequestURI()).append("1\">1</a></li>");
            builder.append("<li class=\"").append(disabledClass).append("\">");
            builder.append("<span>...</span></li>");
        }

        for (int i = startLink; i <= endLink; i++) {
            boolean isCurrentPage = this.page == i;
            builder.append("<li");

            if (isCurrentPage) {
                builder.append(" class=\"").append(activeClass).append("\"><a>");
            } else {
                builder.append("><a href=\"").append(this.getRequestURI());
                builder.append(i).append("\">");
            }

            builder.append(i).append("</a></li>");
        }

        if (endLink < totalPages) {
            builder.append("<li class=\"").append(disabledClass).append("\"><span>...</span></li>");
            builder.append("<li><a href=\"").append(this.getRequestURI()).append(totalPages);
            builder.append("\">").append(totalPages).append("</a></li>");
        }

        generateNextLink(builder, totalPages);
        builder.append("</ul>");

        return builder.toString();
    }

    /**
     * Generate link (previous or next).
     *
     * @param builder
     * @param defaultPage
     * @param wishedPage
     * @param toPage
     */
    private void generateLink(StringBuilder builder, int defaultPage, int wishedPage, int toPage) {
        final boolean isThisCurrentPage = this.page == wishedPage;
        final int previousPage = isThisCurrentPage ? defaultPage : toPage;

        builder.append("<li");

        if (isThisCurrentPage) {
            builder.append(" class=\"").append(disabledClass);
            builder.append("\"><a>");
        } else {
            builder.append("><a href=\"").append(this.getRequestURI());
            builder.append(previousPage).append("\">");
        }

        builder.append("&").append(wishedPage == 1 ? "l" : "r").append("aquo;</a></li>");
    }

    /**
     * Generate previous link.
     *
     * @param builder
     */
    private void generatePreviousLink(StringBuilder builder) {
        generateLink(builder, 1, 1, page - 1);
    }

    /**
     * Generate next link.
     *
     * @param builder
     * @param lastPage
     */
    private void generateNextLink(StringBuilder builder, int lastPage) {
        generateLink(builder, page, lastPage, page + 1);
    }
}
