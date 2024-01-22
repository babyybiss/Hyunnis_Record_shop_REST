package hyunni.rest.album.repository;

import hyunni.rest.album.entity.AlbumFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumFileRepository extends JpaRepository<AlbumFile, Long> {

    @Query(value = "SELECT * FROM tbl_album_file where album_code = :albumCode", nativeQuery = true)
    AlbumFile findByAlbumCode(Long albumCode);

    @Modifying
    @Query(value = "DELETE FROM tbl_album_file WHERE album_code = :albumCode", nativeQuery = true)
    void deleteByAlbumCode(@Param("albumCode") Long albumCode);

    @Query(value = "SELECT COUNT(*) AS albumFile_count FROM tbl_album_file WHERE album_code = :albumCode", nativeQuery = true)
    int countExisting(Long albumCode);
}
