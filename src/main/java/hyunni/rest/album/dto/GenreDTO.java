package hyunni.rest.album.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class GenreDTO {
    private Long genreCode;
    private String genreName;
}
