package hyunni.rest.album.repository;

import hyunni.rest.album.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query(value = "SELECT a.albumCode FROM albumPKG_entityAlbum a WHERE a.title = :title")
    Long findAlbumCodeByTitle(@Param("title") String title);

    @Query(value = "SELECT a FROM albumPKG_entityAlbum a " +
                    "WHERE a.title like %:searchValue%")
    List<Album> findFilteredAlbum(@Param("searchValue") String searchValue);

    @Modifying
    @Query(value = "DELETE FROM tbl_album WHERE album_code = :albumCode", nativeQuery = true)
    void deleteByAlbumCode(@Param("albumCode") Long albumCode);

    @Query(value = "SELECT COUNT(*) AS album_count FROM tbl_album WHERE album_code = :albumCode", nativeQuery = true)
    int countExisting(Long albumCode);
}
