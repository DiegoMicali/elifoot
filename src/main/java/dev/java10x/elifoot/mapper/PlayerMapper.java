package dev.java10x.elifoot.mapper;

import dev.java10x.elifoot.controller.request.CreatePlayerRequest;
import dev.java10x.elifoot.controller.response.PlayerDetailResponse;
import dev.java10x.elifoot.controller.response.PlayerResponse;
import dev.java10x.elifoot.entity.Player;
import dev.java10x.elifoot.entity.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlayerMapper {

    @Mapping(target = "position", source = "position", qualifiedByName = "enumToString")
    PlayerResponse toPlayerResponse(Player player);

    @Mapping(target = "position", source = "position", qualifiedByName = "enumToString")
    PlayerDetailResponse toPlayerDetailResponse(Player player);

    @Mapping(target = "club.id", source = "clubId")
    Player toPlayer(CreatePlayerRequest request);

    @Named("enumToString")
    default String mapPositionToString(Position position) {
        return position != null ? position.getLabel() : null;
    }

}
