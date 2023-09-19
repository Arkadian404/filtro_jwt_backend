package utils;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {

    public static final String CLIENT_SITE_URL = "http://localhost:4200";

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

}
