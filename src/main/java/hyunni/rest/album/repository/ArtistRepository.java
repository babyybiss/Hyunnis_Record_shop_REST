package hyunni.rest.album.repository;

import hyunni.rest.album.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist findByArtistName(String artistName);

    @Query(value = "SELECT a.artistCode FROM albumPKG_entityArtist a WHERE a.artistName = :artistName")
    Long findArtistCodeByArtistName(@Param("artistName") String artistName);
}
