package hyunni.rest.album.dto;

import hyunni.rest.album.entity.Genre;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbumDTO {
    private Long albumCode;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String releaseDate;
    private Long albumPrice;
    private Long genreCode;
    private Long artistCode;
    private Long AlbumFileCode;
}
