package hyunni.rest.album.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity(name = "albumPKG_entityArtist")
@Table(name = "tbl_artist")
@NoArgsConstructor
@Getter
@ToString
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "artist_code")
    private Long artistCode;

    @Column(name = "artist_name")
    private String artistName;

}
