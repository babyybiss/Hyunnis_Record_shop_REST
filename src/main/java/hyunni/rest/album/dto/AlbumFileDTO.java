package hyunni.rest.album.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbumFileDTO {
    private Long albumFileCode;
    private String fileOriginName;
    private String fileOriginPath;
    private String fileSaveName;
    private String fileSavePath;
    private String fileExtension;
    private Long albumCode;
}
