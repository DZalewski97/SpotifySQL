package Zalewa.SpotifySQL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SpotifyAlbumRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void saveTrack(String trackName, String albumName, String albumImage, int rating) {
        jdbcTemplate.update("INSERT INTO Spotify(`Track`, `Album`, `Album images`, `Rating`) VALUES (?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE `Rating` = ?",
                trackName, albumName, albumImage, rating, rating);
    }

    public List<Map<String, String>> getAll() {
        String query = "SELECT `Track`, `Album`, `Album images`, `Rating` FROM Spotify";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        return rows.stream()
                .map(row -> {
                    Map<String, String> stringRow = new HashMap<>();
                    row.forEach((key, value) -> stringRow.put(key, value != null ? value.toString() : null));
                    return stringRow;
                })
                .toList();
    }

    public void deleteTrack(String trackName, String albumName, String albumImage, int rating) {
        jdbcTemplate.update("DELETE FROM Spotify WHERE Track = ? AND Album = ? AND `Album images` = ? AND Rating = ?",
                trackName, albumName, albumImage, rating);
    }
}
