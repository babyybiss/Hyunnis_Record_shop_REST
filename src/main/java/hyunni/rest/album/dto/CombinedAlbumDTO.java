package hyunni.rest.album.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CombinedAlbumDTO {
    private Long albumCode;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String releaseDate;
    private Long albumPrice;
    private GenreDTO genre;
    private ArtistDTO artist;
    private AlbumFileDTO albumFile;

}
