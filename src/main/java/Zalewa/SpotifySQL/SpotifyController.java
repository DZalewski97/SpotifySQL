package Zalewa.SpotifySQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyController {

    private final SpotifyAlbum spotifyAlbumService;


    @Autowired
    public SpotifyController(SpotifyAlbum spotifyAlbumService) {
        this.spotifyAlbumService = spotifyAlbumService;
    }

    @GetMapping("/spotify/album/{authorName}")
    public String getAlbumsByAuthor(@PathVariable String authorName) {
        return spotifyAlbumService.getAlbumsByAuthor(authorName);
    }
}
