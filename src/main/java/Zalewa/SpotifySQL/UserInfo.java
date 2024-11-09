package Zalewa.SpotifySQL;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfo {

    @GetMapping("/user")
    public OAuth2User getUserInfo(Authentication authentication) {
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        return (OAuth2User) oauth2Token.getPrincipal();
    }


    @GetMapping("/userinfo")
    public Principal userinfo(Principal principal) {
        return principal;
    }
}
