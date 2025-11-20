package com.labzang.api.soccer.stadium;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.labzang.api.common.domain.Messenger;
import com.labzang.api.soccer.stadium.domain.StadiumDTO;
import com.labzang.api.soccer.stadium.domain.Stadium;
import com.labzang.api.soccer.stadium.repository.StadiumRepository;

@Service
@RequiredArgsConstructor
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    private StadiumModel entityToDTO(Stadium entity) {
        return StadiumModel.builder()
                .id(entity.getId())
                .stadium_uk(entity.getStadium_uk())
                .stadium_name(entity.getStadium_name())
                .hometeam_uk(entity.getHometeam_uk())
                .seat_count(entity.getSeat_count())
                .address(entity.getAddress())
                .ddd(entity.getDdd())
                .tel(entity.getTel())
                .build();
    }

    private Stadium dtoToEntity(StadiumModel dto) {
        return Stadium.builder()
                .id(dto.id)
                .stadium_uk(dto.stadium_uk)
                .stadium_name(dto.stadium_name)
                .hometeam_uk(dto.hometeam_uk)
                .seat_count(dto.seat_count)
                .address(dto.address)
                .ddd(dto.ddd)
                .tel(dto.tel)
                .build();
    }

    @Override
    public Messenger findById(StadiumModel stadiumDTO) {
        Optional<Stadium> entity = stadiumRepository.findById(stadiumDTO.id);
        if (entity.isPresent()) {
            StadiumModel dto = entityToDTO(entity.get());
            return Messenger.builder()
                    .Code(200)
                    .message("Ï°∞Ìöå ?±Í≥µ")
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("Í≤ΩÍ∏∞?•ÏùÑ Ï∞æÏùÑ ???ÜÏäµ?àÎã§.")
                    .build();
        }
    }

    @Override
    public Messenger findAll() {
        List<Stadium> entities = stadiumRepository.findAll();
        List<StadiumModel> dtoList = entities.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
        return Messenger.builder()
                .Code(200)
                .message("?ÑÏ≤¥ Ï°∞Ìöå ?±Í≥µ: " + dtoList.size() + "Í∞?)
                .data(dtoList)
                .build();
    }

    @Override
    @Transactional
    public Messenger save(StadiumModel stadiumDTO) {
        Stadium entity = dtoToEntity(stadiumDTO);
        Stadium saved = stadiumRepository.save(entity);
        StadiumModel dto = entityToDTO(saved);
        return Messenger.builder()
                .Code(200)
                .message("?Ä???±Í≥µ: " + saved.getId())
                .data(dto)
                .build();
    }

    @Override
    @Transactional
    public Messenger saveAll(List<StadiumModel> stadiumDTOList) {
        List<Stadium> entities = stadiumDTOList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
        
        List<Stadium> saved = stadiumRepository.saveAll(entities);
        return Messenger.builder()
                .Code(200)
                .message("?ºÍ¥Ñ ?Ä???±Í≥µ: " + saved.size() + "Í∞?)
                .build();
    }

    @Override
    @Transactional
    public Messenger update(StadiumModel stadiumDTO) {
        Optional<Stadium> optionalEntity = stadiumRepository.findById(stadiumDTO.id);
        if (optionalEntity.isPresent()) {
            Stadium existing = optionalEntity.get();
            Stadium updated = Stadium.builder()
                    .id(existing.getId())
                    .stadium_uk(stadiumDTO.stadium_uk != null ? stadiumDTO.stadium_uk : existing.getStadium_uk())
                    .stadium_name(stadiumDTO.stadium_name != null ? stadiumDTO.stadium_name : existing.getStadium_name())
                    .hometeam_uk(stadiumDTO.hometeam_uk != null ? stadiumDTO.hometeam_uk : existing.getHometeam_uk())
                    .seat_count(stadiumDTO.seat_count != null ? stadiumDTO.seat_count : existing.getSeat_count())
                    .address(stadiumDTO.address != null ? stadiumDTO.address : existing.getAddress())
                    .ddd(stadiumDTO.ddd != null ? stadiumDTO.ddd : existing.getDdd())
                    .tel(stadiumDTO.tel != null ? stadiumDTO.tel : existing.getTel())
                    .schedules(existing.getSchedules())
                    .teams(existing.getTeams())
                    .build();
            
            Stadium saved = stadiumRepository.save(updated);
            StadiumModel dto = entityToDTO(saved);
            return Messenger.builder()
                    .Code(200)
                    .message("?òÏ†ï ?±Í≥µ: " + stadiumDTO.id)
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("?òÏ†ï??Í≤ΩÍ∏∞?•ÏùÑ Ï∞æÏùÑ ???ÜÏäµ?àÎã§.")
                    .build();
        }
    }

    @Override
    @Transactional
    public Messenger delete(StadiumModel stadiumDTO) {
        Optional<Stadium> optionalEntity = stadiumRepository.findById(stadiumDTO.id);
        if (optionalEntity.isPresent()) {
            stadiumRepository.deleteById(stadiumDTO.id);
            return Messenger.builder()
                    .Code(200)
                    .message("??†ú ?±Í≥µ: " + stadiumDTO.id)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("??†ú??Í≤ΩÍ∏∞?•ÏùÑ Ï∞æÏùÑ ???ÜÏäµ?àÎã§.")
                    .build();
        }
    }

}


