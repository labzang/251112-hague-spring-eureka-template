package com.labzang.api.soccer.team;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.labzang.api.common.domain.Messenger;
import com.labzang.api.soccer.team.domain.TeamDTO;
import com.labzang.api.soccer.team.domain.Team;
import com.labzang.api.soccer.team.repository.TeamRepository;
import com.labzang.api.soccer.stadium.repository.StadiumRepository;
import com.labzang.api.soccer.stadium.domain.Stadium;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final StadiumRepository stadiumRepository;

    private TeamModel entityToDTO(Team entity) {
        return TeamModel.builder()
                .id(entity.getId())
                .team_uk(entity.getTeam_uk())
                .region_name(entity.getRegion_name())
                .team_name(entity.getTeam_name())
                .e_team_name(entity.getE_team_name())
                .orig_yyyy(entity.getOrig_yyyy())
                .zip_code1(entity.getZip_code1())
                .zip_code2(entity.getZip_code2())
                .address(entity.getAddress())
                .ddd(entity.getDdd())
                .tel(entity.getTel())
                .fax(entity.getFax())
                .homepage(entity.getHomepage())
                .owner(entity.getOwner())
                .stadium_uk(entity.getStadium_uk())
                .build();
    }

    private Team dtoToEntity(TeamModel dto) {
        Stadium stadium = null;
        if (dto.stadium_uk != null) {
            stadium = stadiumRepository.findByStadium_uk(dto.stadium_uk).orElse(null);
        }
        return Team.builder()
                .id(dto.id)
                .team_uk(dto.team_uk)
                .region_name(dto.region_name)
                .team_name(dto.team_name)
                .e_team_name(dto.e_team_name)
                .orig_yyyy(dto.orig_yyyy)
                .zip_code1(dto.zip_code1)
                .zip_code2(dto.zip_code2)
                .address(dto.address)
                .ddd(dto.ddd)
                .tel(dto.tel)
                .fax(dto.fax)
                .homepage(dto.homepage)
                .owner(dto.owner)
                .stadium_uk(dto.stadium_uk)
                .stadium(stadium)
                .build();
    }

    @Override
    public Messenger findById(TeamModel teamDTO) {
        Optional<Team> entity = teamRepository.findById(teamDTO.id);
        if (entity.isPresent()) {
            TeamModel dto = entityToDTO(entity.get());
            return Messenger.builder()
                    .Code(200)
                    .message("ì¡°íšŒ ?±ê³µ")
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("?€??ì°¾ì„ ???†ìŠµ?ˆë‹¤.")
                    .build();
        }
    }


    @Override
    public Messenger findAll() {
        List<Team> entities = teamRepository.findAll();
        List<TeamModel> dtoList = entities.stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
        return Messenger.builder()
                .Code(200)
                .message("?„ì²´ ì¡°íšŒ ?±ê³µ: " + dtoList.size() + "ê°?)
                .data(dtoList)
                .build();
    }

    @Override
    @Transactional
    public Messenger save(TeamModel teamDTO) {
        Team entity = dtoToEntity(teamDTO);
        Team saved = teamRepository.save(entity);
        TeamModel dto = entityToDTO(saved);
        return Messenger.builder()
                .Code(200)
                .message("?€???±ê³µ: " + saved.getId())
                .data(dto)
                .build();
    }

    @Override
    @Transactional
    public Messenger saveAll(List<TeamModel> teamDTOList) {
        List<Team> entities = teamDTOList.stream()
                .map(this::dtoToEntity)
                .collect(Collectors.toList());
        
        List<Team> saved = teamRepository.saveAll(entities);
        return Messenger.builder()
                .Code(200)
                .message("?¼ê´„ ?€???±ê³µ: " + saved.size() + "ê°?)
                .build();
    }

    @Override
    @Transactional
    public Messenger update(TeamModel teamDTO) {
        Optional<Team> optionalEntity = teamRepository.findById(teamDTO.id);
        if (optionalEntity.isPresent()) {
            Team existing = optionalEntity.get();
            Stadium stadium = teamDTO.stadium_uk != null 
                    ? stadiumRepository.findByStadium_uk(teamDTO.stadium_uk).orElse(existing.getStadium()) 
                    : existing.getStadium();
            
            Team updated = Team.builder()
                    .id(existing.getId())
                    .team_uk(teamDTO.team_uk != null ? teamDTO.team_uk : existing.getTeam_uk())
                    .region_name(teamDTO.region_name != null ? teamDTO.region_name : existing.getRegion_name())
                    .team_name(teamDTO.team_name != null ? teamDTO.team_name : existing.getTeam_name())
                    .e_team_name(teamDTO.e_team_name != null ? teamDTO.e_team_name : existing.getE_team_name())
                    .orig_yyyy(teamDTO.orig_yyyy != null ? teamDTO.orig_yyyy : existing.getOrig_yyyy())
                    .zip_code1(teamDTO.zip_code1 != null ? teamDTO.zip_code1 : existing.getZip_code1())
                    .zip_code2(teamDTO.zip_code2 != null ? teamDTO.zip_code2 : existing.getZip_code2())
                    .address(teamDTO.address != null ? teamDTO.address : existing.getAddress())
                    .ddd(teamDTO.ddd != null ? teamDTO.ddd : existing.getDdd())
                    .tel(teamDTO.tel != null ? teamDTO.tel : existing.getTel())
                    .fax(teamDTO.fax != null ? teamDTO.fax : existing.getFax())
                    .homepage(teamDTO.homepage != null ? teamDTO.homepage : existing.getHomepage())
                    .owner(teamDTO.owner != null ? teamDTO.owner : existing.getOwner())
                    .stadium_uk(teamDTO.stadium_uk != null ? teamDTO.stadium_uk : existing.getStadium_uk())
                    .stadium(stadium)
                    .players(existing.getPlayers())
                    .build();
            
            Team saved = teamRepository.save(updated);
            TeamModel dto = entityToDTO(saved);
            return Messenger.builder()
                    .Code(200)
                    .message("?˜ì • ?±ê³µ: " + teamDTO.id)
                    .data(dto)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("?˜ì •???€??ì°¾ì„ ???†ìŠµ?ˆë‹¤.")
                    .build();
        }
    }

    @Override
    @Transactional
    public Messenger delete(TeamModel teamDTO) {
        Optional<Team> optionalEntity = teamRepository.findById(teamDTO.id);
        if (optionalEntity.isPresent()) {
            teamRepository.deleteById(teamDTO.id);
            return Messenger.builder()
                    .Code(200)
                    .message("?? œ ?±ê³µ: " + teamDTO.id)
                    .build();
        } else {
            return Messenger.builder()
                    .Code(404)
                    .message("?? œ???€??ì°¾ì„ ???†ìŠµ?ˆë‹¤.")
                    .build();
        }
    }

}


