package lenicorp.security.audit;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.ext.web.RoutingContext;

@RequestScoped
public class AuditService {

    @Inject
    SecurityIdentity securityIdentity;

    @Inject
    RoutingContext routingContext; // ✅ Fonctionne avec Quarkus

    public String getCurrentUsername() {
        if (securityIdentity != null && !securityIdentity.isAnonymous()) {
            return securityIdentity.getPrincipal().getName();
        }
        return "ANONYMOUS";
    }

    public String getCurrentIpAddress() {
        try {
            if (routingContext != null && routingContext.request() != null) {
                return getClientIpAddress();
            }
        } catch (Exception e) {
            // Log si nécessaire
        }
        return "LOCALHOST";
    }

    public String getCurrentUserAgent() {
        try {
            if (routingContext != null && routingContext.request() != null) {
                String userAgent = routingContext.request().getHeader("User-Agent");
                if (userAgent != null && userAgent.length() > 255) {
                    userAgent = userAgent.substring(0, 252) + "...";
                }
                return userAgent;
            }
        } catch (Exception e) {
            // Log si nécessaire
        }
        return "NO_REQUEST_CONTEXT";
    }

    public String getSessionId() {
        try {
            if (routingContext != null && routingContext.session() != null) {
                return routingContext.session().id();
            }
        } catch (Exception e) {
            // Log si nécessaire
        }
        return "NO_SESSION";
    }

    private String getClientIpAddress() {
        var request = routingContext.request();

        // Vérifier les headers de proxy/load balancer
        String[] headerNames = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // Prendre la première IP si plusieurs (séparées par des virgules)
                return ip.split(",")[0].trim();
            }
        }

        // Fallback sur l'IP remote standard
        return request.remoteAddress().host();
    }
}