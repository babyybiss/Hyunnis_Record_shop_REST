package hyunni.rest.album.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
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
    private Date releaseDate;

    @Column(name = "album_price")
    private Long albumPrice;

    @ManyToOne
    @JoinColumn(name = "genre_code")
    private Genre genre;

    @ManyToOne
    @JoinColumn(name = "artist_code")
    private Artist artist;

    @OneToOne(mappedBy = "album", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AlbumFile albumFile;

    public Album(Long albumCode, String title, Date releaseDate, Long albumPrice, Genre genre) {
    }

    public Album albumCode(Long albumCode) {
        this.albumCode = albumCode;
        return this;
    }

    public Album title(String title) {
        this.title = title;
        return this;
    }

    public Album albumPrice(Long albumPrice) {
        this.albumPrice = albumPrice;
        return this;
    }

    public Album releaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Album genre(Genre genre) {
        this.genre = genre;
        return this;
    }
    
    public Album artist(Artist artist) {
        this.artist = artist;
        return this;
    }


    public Album build() {
        return new Album(albumCode,title, releaseDate,albumPrice, genre);
    }

}


