package Zalewa.SpotifySQL;


import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpotifyAlbumServer {

    //private final OAuth2AuthorizedClientService authorizedClientService;
    private final SpotifyToken spotifyToken;

    @Autowired
    public SpotifyAlbumServer(SpotifyToken spotifyToken) {
        this.spotifyToken = spotifyToken;
    }


    public List<Map<String, String>> getTracks(String trackSearch) {

        String accessToken = spotifyToken.getSpotifyAccessToken();
        System.out.println(accessToken);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> exchange = restTemplate.exchange("https://api.spotify.com/v1/search?q=" + trackSearch + "&type=track&limit=10&offset=0", HttpMethod.GET, httpEntity, String.class);
        StringBuilder result = new StringBuilder();
        Gson gson = new Gson();


        JsonObject jsonObject = gson.fromJson(exchange.getBody(), JsonObject.class);
        JsonObject tracks = jsonObject.getAsJsonObject("tracks");
        JsonArray items = tracks.getAsJsonArray("items"); // items is an array

        List<Map<String, String>> resultRepository = new ArrayList<>();

        for (JsonElement itemElement : items) {
            JsonObject track = itemElement.getAsJsonObject();
            String trackName = track.get("name").getAsString();

            JsonObject album = track.getAsJsonObject("album");
            String albumName = album.get("name").getAsString();
            JsonArray images = album.getAsJsonArray("images");
            String imageURL = images.get(0).getAsJsonObject().get("url").getAsString();

            Map<String, String> trackData = new HashMap<>();
            trackData.put("trackName", trackName);
            trackData.put("albumName", albumName);
            trackData.put("imageURL", imageURL);

            resultRepository.add(trackData);
        }

        resultRepository.forEach(track -> System.out.printf("Track: %s, Album: %s, Image URL: %s%n", track.get("trackName"), track.get("albumName"), track.get("imageURL")));


        return resultRepository;
    }
}
