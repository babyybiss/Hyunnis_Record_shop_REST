package hyunni.rest.album.service;

import hyunni.rest.album.dto.AlbumDTO;
import hyunni.rest.album.dto.AlbumFileDTO;
import hyunni.rest.album.dto.ArtistDTO;
import hyunni.rest.album.dto.CombinedAlbumDTO;
import hyunni.rest.album.entity.Album;
import hyunni.rest.album.entity.AlbumFile;
import hyunni.rest.album.entity.Artist;
import hyunni.rest.album.repository.AlbumFileRepository;
import hyunni.rest.album.repository.AlbumRepository;
import hyunni.rest.album.repository.ArtistRepository;
import hyunni.rest.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumFileRepository albumFileRepository;
    private final ModelMapper modelMapper;

    public AlbumService(AlbumRepository albumRepository, ArtistRepository artistRepository, AlbumFileRepository albumFileRepository, ModelMapper modelMapper) {
        this.albumRepository = albumRepository;
        this.albumFileRepository = albumFileRepository;
        this.modelMapper = modelMapper;
        this.artistRepository = artistRepository;
    }

    public List<CombinedAlbumDTO> findAlbumList() {
        List<Album> albumEntityList = albumRepository.findAll();

        return albumEntityList.stream()
                .map(albumDtoList -> modelMapper.map(albumDtoList, CombinedAlbumDTO.class))
                .collect(Collectors.toList());
    }

    public CombinedAlbumDTO findAlbumDetails(int albumCode) throws IllegalAccessException {
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

        String imageName = UUID.randomUUID().toString().replace("-", "");
        Path rootPath;
        String IMAGE_DIR = null;
        String replaceFileName = null;
        int result = 0;

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


}
