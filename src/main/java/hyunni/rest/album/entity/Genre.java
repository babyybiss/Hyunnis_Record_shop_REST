package hyunni.rest.album.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "albumPKG_entityGenre")
@Table(name = "tbl_genre")
@NoArgsConstructor
@Getter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_code")
    private Long genreCode;

    @Column(name = "genre_name")
    private String genreName;
}
