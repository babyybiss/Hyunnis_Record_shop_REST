package hyunni.rest.album.service;

import hyunni.rest.album.dto.CombinedAlbumDTO;
import hyunni.rest.album.entity.Album;
import hyunni.rest.album.repository.AlbumRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private AlbumRepository albumRepository;
    private final ModelMapper modelMapper;

    public AlbumService(AlbumRepository albumRepository, ModelMapper modelMapper) {
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
    }

    public List<CombinedAlbumDTO> findAlbumList() {
        List<Album> albumEntityList = albumRepository.findAll();

        return albumEntityList.stream()
                .map(albumDtoList -> modelMapper.map(albumDtoList, CombinedAlbumDTO.class))
                .collect(Collectors.toList());
    }
}
