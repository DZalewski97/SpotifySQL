package Zalewa.SpotifySQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class SpotifyController {

    private final SpotifyAlbumServer spotifyAlbumService;
    private final SpotifyAlbumRepository spotifyAlbumRepository;

    @Autowired
    public SpotifyController(SpotifyAlbumServer spotifyAlbumService, SpotifyAlbumRepository spotifyAlbumRepository) {
        this.spotifyAlbumService = spotifyAlbumService;
        this.spotifyAlbumRepository = spotifyAlbumRepository;
    }

    @GetMapping("/spotify/search")
    public String getSearch() {
        return "SearchSpotify";
    }

    @GetMapping("/spotify/search/submit")
    public String getSearchSubmit(@RequestParam String trackSearch) {
        return String.format("redirect:/spotify/track/%s", trackSearch);
    }

    @GetMapping("/spotify/MySQL/submit")
    public String getAllSubmit() {
        return "redirect:/spotify/MySQL";
    }

    @GetMapping("/spotify/track/{trackSearch}")
    public String getTracks(@PathVariable String trackSearch, Model model) {
        List<Map<String, String>> albumsData = spotifyAlbumService.getTracks(trackSearch);
        model.addAttribute("albumsData", albumsData);
        return "SearchResultSpotify";
    }

    @PostMapping("/spotify/MySQL/add")
    public String addTrack(
            @RequestParam("trackName") String trackName,
            @RequestParam("albumName") String albumName,
            @RequestParam("albumImage") String albumImage,
            @RequestParam("rating") int rating) {

        spotifyAlbumRepository.saveTrack(trackName, albumName, albumImage, rating);

        return "redirect:/spotify/search";
    }

    @PostMapping("/spotify/MySQL/delete")
    public String deleteTrack(
            @RequestParam("trackName") String trackName,
            @RequestParam("albumName") String albumName,
            @RequestParam("albumImage") String albumImage,
            @RequestParam("rating") int rating) {

        spotifyAlbumRepository.deleteTrack(trackName, albumName, albumImage, rating);

        return "redirect:/spotify/MySQL";
    }

    @GetMapping("/spotify/MySQL")
    public String getAll(Model model) {
        List<Map<String, String>> allTracks = spotifyAlbumRepository.getAll();
        model.addAttribute("allTracks", allTracks);
        return "AllTracksView";
    }
}

