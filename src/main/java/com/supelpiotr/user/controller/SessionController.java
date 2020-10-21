package com.supelpiotr.user.controller;

import com.supelpiotr.utils.controller.BaseController;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@RestController
public class SessionController extends BaseController {

    @GetMapping(value = "/api/session", produces = MEDIA_TYPE)
    public String getSession() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();
        return objectResult(getJSON(authentication));
    }

    public static JSONObject getJSON(Authentication authentication) {
        JSONObject jsonObject = new JSONObject();

        if (authentication != null) {
            String pesel = authentication.getName();
            boolean isLogged = authentication.isAuthenticated() && !"anonymousUser".equals(pesel);
            if (isLogged) {
                jsonObject.put("userName", pesel);
                jsonObject.put("isLogged", true);
                jsonObject.put("userRoles", getUserRoles(authentication));
                return jsonObject;
            }
        }

        jsonObject.put("userName", "Guest");
        jsonObject.put("isLogged", false);
        jsonObject.put("userRoles", new JSONArray());
        return jsonObject;
    }

    private static JSONArray getUserRoles(Authentication authentication) {
        JSONArray userRoles = new JSONArray();
        authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .forEach(userRoles::put);
        return userRoles;
    }

}
