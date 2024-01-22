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

    public Genre updateGenreCode(Long newGenreCode) {
        return new Genre(newGenreCode, this.genreName);
    }
    public Genre(Long genreCode, String genreName) {
        this.genreCode = genreCode;
        this.genreName = genreName;
    }

    public Genre genreCode(Long genreCode) {
        this.genreCode = genreCode;
        return this;
    }

    public Genre genreName(String genreName) {
        this.genreName = genreName;
        return this;
    }

    public void setGenreCode(Long genreCode) {
        this.genreCode = genreCode;
    }
}
