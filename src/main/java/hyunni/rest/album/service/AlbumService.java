package hyunni.rest.album.service;

import hyunni.rest.album.dto.*;
import hyunni.rest.album.entity.Album;
import hyunni.rest.album.entity.AlbumFile;
import hyunni.rest.album.entity.Artist;
import hyunni.rest.album.entity.Genre;
import hyunni.rest.album.repository.AlbumFileRepository;
import hyunni.rest.album.repository.AlbumRepository;
import hyunni.rest.album.repository.ArtistRepository;
import hyunni.rest.album.repository.GenreRepository;
import hyunni.rest.util.FileUploadUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumFileRepository albumFileRepository;
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;


    Path rootPath;
    String IMAGE_DIR = null;
    String replaceFileName = null;
    int result = 0;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository, AlbumFileRepository albumFileRepository, GenreRepository genreRepository, ModelMapper modelMapper) {
        this.albumRepository = albumRepository;
        this.albumFileRepository = albumFileRepository;
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
        this.artistRepository = artistRepository;
    }

    public List<CombinedAlbumDTO> findAlbumList() {
        List<Album> albumEntityList = albumRepository.findAll();

        return albumEntityList.stream()
                .map(albumDtoList -> modelMapper.map(albumDtoList, CombinedAlbumDTO.class))
                .collect(Collectors.toList());
    }

    public CombinedAlbumDTO findAlbumDetails(Long albumCode) throws IllegalAccessException {
        Album albumEntity = albumRepository.findById(albumCode).orElseThrow(IllegalAccessException::new);

        return modelMapper.map(albumEntity, CombinedAlbumDTO.class);


    }

    @Transactional
    public void postAlbum(AlbumDTO albumDTO, MultipartFile imageFile, ArtistDTO artistDTO) throws IOException {

        log.info("[AlbumService] registNewAlbum Start ===========================");
        log.info("[AlbumService] registNewAlbum : " + albumDTO);

        /////////////////////////////////////////////

        Long findAlbumCode = null;

        //search if Artist exists in dataBase
        Artist artistExists = artistRepository.findByArtistName(artistDTO.getArtistName());
        log.info("is it there? : " + artistExists);

        if(artistExists == null) {
            log.info("Artist registration needed : " + artistExists);

            //insert new artist
            Artist artist = modelMapper.map(artistDTO, Artist.class);
            artistRepository.save(artist);

            // find new inserted artist's artistCode and set to albumDTO artistCode column
            Long newArtist = artistRepository.findArtistCodeByArtistName(artistDTO.getArtistName());
            albumDTO.setArtistCode(newArtist);

            log.info("whats in the albumDTO : " + albumDTO);

            log.info("album code is? : " + newArtist);
            log.info("album code is? : " + albumDTO.getAlbumCode()); // will return null because it has not been inserted yet

            // insert new Album first because we will need the auto-incremented albumCode for inserting the following albumFile
            Album insertAlbum = modelMapper.map(albumDTO, Album.class);

            albumRepository.save(insertAlbum);
            albumRepository.flush();

            findAlbumCode = albumRepository.findAlbumCodeByTitle(albumDTO.getTitle());
            log.info("what is the albumCode of the new album? :" + findAlbumCode);


        } else {
            log.info("Artist already exists : " + artistExists);
        }

        //Save image
        AlbumFileDTO albumFileDTO = new AlbumFileDTO();

        if (FileSystems.getDefault().getSeparator().equals("/")) {
            Path MACPath = Paths.get("/Hyunnis_Record_shop_REACT/public/images").toAbsolutePath();
            // Unix-like system (MacOS, Linux)
            rootPath = Paths.get("/User").toAbsolutePath();
            Path relativePath = rootPath.relativize(MACPath);
            IMAGE_DIR = String.valueOf(relativePath);

        } else {
            // Windows
            Path WinPath = Paths.get("/Hyunnis_Record_shop_REACT/public/images").toAbsolutePath();
            rootPath = Paths.get("C:\\").toAbsolutePath();
            Path relativePath = rootPath.resolve(WinPath);
            IMAGE_DIR = String.valueOf(relativePath);
            rootPath = Paths.get("C:\\dev\\").toAbsolutePath();
            Path resolvePath = rootPath.resolve(WinPath);
            IMAGE_DIR = String.valueOf(resolvePath);
        }
        log.info("what is the path? : " + IMAGE_DIR);

        String imageName = UUID.randomUUID().toString().replace("-", "");
        replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, imageFile);

        albumFileDTO.setAlbumCode(findAlbumCode);
        albumFileDTO.setFileSaveName(replaceFileName);
        albumFileDTO.setFileSavePath("/images/" + replaceFileName);
        albumFileDTO.setFileExtension("PNG");

        albumFileDTO.setFileOriginName(imageFile.getOriginalFilename());
        albumFileDTO.setFileOriginPath(IMAGE_DIR);

        AlbumFile insertAlbumFile = modelMapper.map(albumFileDTO, AlbumFile.class);

        albumFileRepository.save(insertAlbumFile);

        result = 1;

        System.out.println("check");
    }


    @Transactional
    public void putAlbum(AlbumDTO albumDTO, GenreDTO genreDTO, MultipartFile imageFile) {
        log.info("[ProductService] updateReview Start ===================================");
        log.info("[ProductService] productDTO : " + albumDTO);

        String replaceFileName = null;
        int result = 0;

        try {
            // get the entities of the data we will be modifying
            Album replaceableAlbum = albumRepository.findById(albumDTO.getAlbumCode()).get();
            AlbumFile replaceabeAlbumFile = albumFileRepository.findByAlbumCode(albumDTO.getAlbumCode());

            String replaceableImgPath = replaceabeAlbumFile.getFileSaveName();

            AlbumFile albumFile = replaceableAlbum.getAlbumFile();

            if(albumFile != null) {
                String fileOriginPath = albumFile.getFileOriginPath();

                replaceableAlbum = replaceableAlbum.albumCode(albumDTO.getAlbumCode())
                        .title(albumDTO.getTitle())
                        .releaseDate(Date.valueOf(albumDTO.getReleaseDate()))
                        .albumPrice(albumDTO.getAlbumPrice());

                Genre existingGenre = replaceableAlbum.getGenre();

                if (existingGenre != null) {
                    // Use the updateGenreCode method to create a new instance with the updated genreCode
                    Genre updatedGenre = existingGenre.updateGenreCode(genreDTO.getGenreCode());
                    replaceableAlbum.genre(updatedGenre);
                } else {
                    // Create a new Genre entity and associate it with the Album
                    Genre newGenre = new Genre().genreCode(genreDTO.getGenreCode());
                    replaceableAlbum.genre(newGenre);
                }

                replaceableAlbum = replaceableAlbum.build();


                if(imageFile != null) {
                    if (FileSystems.getDefault().getSeparator().equals("/")) {
                        Path MACPath = Paths.get("/Hyunnis_Record_shop_REACT/public/images").toAbsolutePath();
                        // Unix-like system (MacOS, Linux)
                        rootPath = Paths.get("/User").toAbsolutePath();
                        Path relativePath = rootPath.relativize(MACPath);
                        IMAGE_DIR = String.valueOf(relativePath);

                    } else {
                        // Windows
                        Path WinPath = Paths.get("/Hyunnis_Record_shop_REACT/public/images").toAbsolutePath();
                        rootPath = Paths.get("C:\\").toAbsolutePath();
                        Path relativePath = rootPath.resolve(WinPath);
                        IMAGE_DIR = String.valueOf(relativePath);
                        rootPath = Paths.get("C:\\dev\\").toAbsolutePath();
                        Path resolvePath = rootPath.resolve(WinPath);
                        IMAGE_DIR = String.valueOf(resolvePath);
                    }
                    log.info("what is the path? : " + IMAGE_DIR);
                    String imageName = UUID.randomUUID().toString().replace("-", "");

                    replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, imageFile);

                    AlbumFile albumFile1 = albumFileRepository.findByAlbumCode(albumDTO.getAlbumCode());
                    albumFile1 = albumFile1.fileSaveName(replaceFileName).build();

                    boolean isDelete = FileUploadUtils.deleteFile(IMAGE_DIR, replaceableImgPath);
                    log.info("[Update Album] isDelete ? : " + isDelete);
                    log.info("[Update Album] InsertFileName ? : " + replaceFileName);
                }
            } else {
                AlbumFile albumFile2 = albumFileRepository.findByAlbumCode(albumDTO.getAlbumCode());
                albumFile2 = albumFile2.fileSaveName(replaceableImgPath).build();

                log.info("[Update Album] No files came through");
            }
        } catch (IOException e) {
            log.error("Error occurred : " + e);
            throw new RuntimeException(e);
        }


    }

    public List<CombinedAlbumDTO> findReviewsBySearchFilter(String searchValue) {
        List<Album> filteredAlbumList = albumRepository.findFilteredAlbum(searchValue);

        return filteredAlbumList.stream()
                .map(filteredAlbums -> modelMapper.map(filteredAlbums, CombinedAlbumDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteAlbum(Long albumCode) {

        boolean isFileDelete = false;

        // find the albumFile saved name
        AlbumFile deletableFile = albumFileRepository.findByAlbumCode(albumCode);
        String deleteImgPath = deletableFile.getFileSaveName();

        if (FileSystems.getDefault().getSeparator().equals("/")) {
            Path MACPath = Paths.get("/Hyunnis_Record_shop_REACT/public/images").toAbsolutePath();
            // Unix-like system (MacOS, Linux)
            rootPath = Paths.get("/User").toAbsolutePath();
            Path relativePath = rootPath.relativize(MACPath);
            IMAGE_DIR = String.valueOf(relativePath);
        } else {
            // Windows
            Path WinPath = Paths.get("/Hyunnis_Record_shop_REACT/public/images").toAbsolutePath();
            rootPath = Paths.get("C:\\").toAbsolutePath();
            Path relativePath = rootPath.resolve(WinPath);
            IMAGE_DIR = String.valueOf(relativePath);
            rootPath = Paths.get("C:\\dev\\").toAbsolutePath();
            Path resolvePath = rootPath.resolve(WinPath);
            IMAGE_DIR = String.valueOf(resolvePath);
        }

        log.info("what is the path: " + IMAGE_DIR);

        try{
            boolean fileIsDelete = FileUploadUtils.deleteFile(IMAGE_DIR, deleteImgPath);
            log.info("[Delete Review] isDelete : " + fileIsDelete);

            albumRepository.deleteByAlbumCode(albumCode);
            albumFileRepository.deleteByAlbumCode(albumCode);
        } catch (Exception e) {
            log.error("Error occurred during album deletion!");
            e.printStackTrace();
        }

        log.info("Will now delete the album");
        int deleted1 = albumRepository.countExisting(albumCode);
        log.info("Will now delete the album file");
        int deleted2 = albumFileRepository.countExisting(albumCode);

        if(deleted1 == 0 && deleted2 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
