package hyunni.rest.album.repository;

import hyunni.rest.album.entity.AlbumFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumFileRepository extends JpaRepository<AlbumFile, Integer> {
}
