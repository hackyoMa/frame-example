package com.github.hackyoma.frameexample.backend.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * GetIp
 *
 * @author hackyo
 * @version V1.0.0
 */
public final class GetIp {

    private final static String[] IP_HEADER_CANDIDATES = {"X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};

    /**
     * 获取用户真实IP
     *
     * @param request request
     * @return 用户真实IP
     */
    public static String getUserIp(HttpServletRequest request) {
        String ip = null;
        for (String candidate : IP_HEADER_CANDIDATES) {
            ip = request.getHeader(candidate);
            if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
                break;
            }
        }
        if (!StringUtils.isEmpty(ip) && ip.contains(",")) {
            String[] ips = ip.split(",");
            for (String i : ips) {
                if (!"unknown".equalsIgnoreCase(i)) {
                    ip = i;
                    break;
                }
            }
        }
        return ip;
    }

}
