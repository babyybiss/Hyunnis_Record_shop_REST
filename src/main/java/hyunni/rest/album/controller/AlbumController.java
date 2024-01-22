package hyunni.rest.album.controller;

import hyunni.rest.album.dto.AlbumDTO;
import hyunni.rest.album.dto.ArtistDTO;
import hyunni.rest.album.dto.CombinedAlbumDTO;
import hyunni.rest.album.dto.GenreDTO;
import hyunni.rest.album.service.AlbumService;
import hyunni.rest.common.ResponseMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value={"/","albums"})
@Slf4j
public class AlbumController {

    private AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @ApiOperation(value = "viewAllAlbums", notes = "View by Artist Name or by recently added albums", tags = {"viewAllAlbums"})
    @GetMapping()
    public ResponseEntity<ResponseMessageDTO> main(@RequestParam(required = false) String status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        if ("viewAll".equals(status)) {
            List<CombinedAlbumDTO> albumList = albumService.findAlbumList();
            responseMap.put("albumList", albumList);
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "get All albums = success!", responseMap);
            System.out.println("albums brought : " + responseMap);

            return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        } else {
            log.info("Did Search value (status) come through? : " + status);
            List<CombinedAlbumDTO> searchResult = albumService.findReviewsBySearchFilter(status);

            responseMap.put("albumList", searchResult);
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "get search result = success!", responseMap);
            System.out.println("search Result : " + responseMap);

            return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        }
    }


    @ApiOperation(value = "viewSpecificAlbum", notes = "View by album code", tags = {"viewSpecificAlbum"})
    @GetMapping("{albumCode}")
    public ResponseEntity<ResponseMessageDTO> getSpecificAlbum(@PathVariable Long albumCode) throws IllegalAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();

        CombinedAlbumDTO album = albumService.findAlbumDetails(albumCode);
        responseMap.put("album", album);
        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "get album details = success!", responseMap);
        System.out.println("album Details brought : " + responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "postAlbum", notes = "Regist new album", tags = {"postAlbum"})
    @PostMapping()
    public ResponseEntity<?> postAlbum(@ModelAttribute AlbumDTO albumDTO,
                                       @ModelAttribute ArtistDTO artistDTO,
                                       //@RequestParam (name = "artistName") String artistName,
                                       MultipartFile imageFile) throws IOException {
        log.info("(Album Controller) RegistAlbum : " + albumDTO);
        log.info("(Album Controller) RegistAlbum : " + artistDTO);
        log.info("(Album Controller) RegistAlbum Image : " + imageFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        albumService.postAlbum(albumDTO, imageFile, artistDTO);

        return new ResponseEntity<>("new Album register success!", headers, HttpStatus.OK);
    }

    @ApiOperation(value = "putAlbum", notes = "modify album", tags = {"putAlbum"})
    @PutMapping()
    public ResponseEntity<?> putAlbum(@ModelAttribute AlbumDTO albumDTO,
                                      @ModelAttribute GenreDTO genreDTO,
                                      MultipartFile imageFile) throws IOException {
        log.info("(Album Controller) ModifyAlbum : " + albumDTO);
        log.info("(Album Controller) ModifyAlbum : " + imageFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        albumService.putAlbum(albumDTO, genreDTO, imageFile);

        return new ResponseEntity<>("album modify success!", headers, HttpStatus.OK);

    }

    @ApiOperation(value = "deleteAlbum", notes = "delete album by albumCode", tags = {"deleteALbum"})
    @DeleteMapping("{albumCode}")
    public ResponseEntity<ResponseMessageDTO> deleteAlbum(@PathVariable Long albumCode) {
        log.info("(Album Controller) DeleteAlbum : " + albumCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        boolean deleteAlbum = albumService.deleteAlbum(albumCode);
        log.info("done?: " + deleteAlbum);

        if (deleteAlbum) {
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "album deletion success!", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        } else {
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "album deletion failed!", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
        }
    }

}

