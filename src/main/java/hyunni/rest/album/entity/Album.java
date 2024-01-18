package hyunni.rest.album.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity(name = "albumPKG_entityAlbum")
@Table(name = "tbl_album")
@NoArgsConstructor
@Getter
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_code")
    private Long albumCode;

    @Column(name = "title")
    private String title;

    @Column(name = "release_date")
    //@DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm")
    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(name = "artist_code")
    private Artist artist;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "album_file_code", referencedColumnName = "album_file_code")
    private AlbumFile albumFile;


}


