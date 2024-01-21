package hyunni.rest.album.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ArtistDTO {
    private Long artistCode;
    private String artistName;
}
