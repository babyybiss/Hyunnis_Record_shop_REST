package hyunni.rest.album.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "albumPKG_entityAlbumFile")
@Table(name = "tbl_albumFile")
@NoArgsConstructor
@Getter
public class AlbumFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_file_code")
    private Long albumFileCode;

    @Column(name = "file_origin_name")
    private String fileOriginName;

    @Column(name = "file_origin_path")
    private String fileOriginPath;

    @Column(name = "file_save_name")
    private String fileSaveName;

    @Column(name = "file_save_path")
    private String fileSavePath;

    @Column(name = "file_extension")
    private String fileExtension;

    @OneToOne
    @JoinColumn(name = "album_code", referencedColumnName = "album_code")
    private Album album;

}
