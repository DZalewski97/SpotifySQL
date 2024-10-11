package Zalewa.SpotifySQL;


import com.nimbusds.jose.shaded.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SpotifyAlbum {

    //private final OAuth2AuthorizedClientService authorizedClientService;
    private final SpotifyToken spotifyToken;

    @Autowired
    public SpotifyAlbum(SpotifyToken spotifyToken) {
        this.spotifyToken = spotifyToken;
    }


    public String getAlbumsByAuthor(@PathVariable String authorName) {

        String accessToken = spotifyToken.getSpotifyAccessToken();
        System.out.println(accessToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> exchange = restTemplate.exchange("https://api.spotify.com/v1/search?q=" + authorName + "&type=track&limit=2&offset=0", HttpMethod.GET, httpEntity, String.class);
        StringBuilder result = new StringBuilder();
        Gson gson = new Gson();


        JsonObject jsonObject = gson.fromJson(exchange.getBody(), JsonObject.class);


        JsonObject tracks = jsonObject.getAsJsonObject("tracks");
        JsonArray items = tracks.getAsJsonArray("items"); // items is an array

        String[][] resultRepository = new String[items.size()][3];

        for (int i = 0; i < items.size(); i++) {
            JsonElement itemElement = items.get(i);
            JsonObject track = itemElement.getAsJsonObject();
            String trackName = track.get("name").getAsString();

            JsonObject album = track.getAsJsonObject("album");
            String albumName = album.get("name").getAsString();
            JsonArray images = album.getAsJsonArray("images");
            String imageURL = images.get(0).getAsJsonObject().get("url").getAsString();

            resultRepository[i][0] = trackName;
            resultRepository[i][1] = albumName;
            resultRepository[i][2] = imageURL;

            result.append("Track: ").append(trackName).append(", Album: ").append(albumName).append(", Image URL: ").append(imageURL).append("\n");
        }
        System.out.println(Arrays.deepToString(resultRepository));
        return result.toString();


    }
}
