package Zalewa.SpotifySQL;


import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyAlbum {

    //private final OAuth2AuthorizedClientService authorizedClientService;
    private final SpotifyToken spotifyToken;

@Autowired
    public SpotifyAlbum(SpotifyToken spotifyToken) {
        this.spotifyToken = spotifyToken;
    }



    public String getAlbumsByAuthor(@PathVariable String authorName){

        String accessToken = spotifyToken.getSpotifyAccessToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> exchange = restTemplate.exchange("https://api.spotify.com/v1/search?q=" + authorName +"&type=artist&offset=0&limit=1",HttpMethod.GET,httpEntity, String.class);
        JsonObject jsonResponse = JsonParser.parseString(exchange.getBody()).getAsJsonObject();
        JsonObject artists = jsonResponse.getAsJsonObject("artists");

        return artists.toString();

    }
}
