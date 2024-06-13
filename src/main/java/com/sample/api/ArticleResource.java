package com.sample.api;

import com.sample.model.Article;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.*;
import java.util.stream.Collectors;

@Path("/api/articles")
public class ArticleResource {

    private static final String ROLE_PREMIUM = "\"premium_access\"";
    private static final Map<Long, Article> articles = createArticles();

    @Inject
    JsonWebToken jwt;

    private static Map<Long, Article> createArticles() {
        Map<Long, Article> articleMap = new HashMap<>();
        articleMap.put(1L, new Article(1L, "Free Article", "This is a free article.", false));
        articleMap.put(2L,
                new Article(2L, "Premium Article", "This is a premium article, for premium members only.", true));
        return Collections.unmodifiableMap(articleMap);
    }

    @GET
    @Path("/basic")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "basic_access", "premium_access" })
    public String getBasicArticle() {
        return "Free Article";
    }

    @GET
    @Path("/premium")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("premium_access")
    public String getPremiumArticle() {
        return "Premium Article";
    }

    @GET
    @Path("/all/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticleById(@PathParam("id") Long id) {
        if (!articles.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND).entity("This content may have already deleted.").build();
        }
        Article article = articles.get(id);
        if (article.isPremium()) {
            if (isUserInRole(ROLE_PREMIUM)) {
                return Response.ok(article).build();
            } else {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("This content is only available for premium members.").build();
            }
        } else {
            return Response.ok(article).build();
        }
    }

    private boolean isUserInRole(String role) {
        List<String> roles = new ArrayList<>();
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess != null) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("news_app_client");
            if (clientAccess != null) {
                List<?> rawRoles = (List<?>) clientAccess.get("roles");
                if (rawRoles != null) {
                    roles = rawRoles.stream()
                            .map(Object::toString)
                            .collect(Collectors.toList());
                }
            }
        }
        return roles.contains(role);
    }
}
