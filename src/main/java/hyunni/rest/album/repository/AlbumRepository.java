package hyunni.rest.album.repository;

import hyunni.rest.album.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {

    @Query(value = "SELECT a.albumCode FROM albumPKG_entityAlbum a WHERE a.title = :title")
    Long findAlbumCodeByTitle(@Param("title") String title);
}
