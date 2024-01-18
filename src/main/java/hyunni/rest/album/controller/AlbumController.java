package hyunni.rest.album.controller;

import hyunni.rest.album.dto.CombinedAlbumDTO;
import hyunni.rest.album.service.AlbumService;
import hyunni.rest.common.ResponseMessageDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value={"/","albums"})
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
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_REQUEST, "get All albums = failed...", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_REQUEST);
        }
    }

}
