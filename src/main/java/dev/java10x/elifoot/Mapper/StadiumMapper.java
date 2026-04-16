package dev.java10x.elifoot.Mapper;


import dev.java10x.elifoot.controller.request.CreateStadiumRequest;
import dev.java10x.elifoot.controller.response.StadiumResponse;
import dev.java10x.elifoot.entity.Stadium;

public class StadiumMapper {

    public static StadiumResponse toStadiumResponse (Stadium stadium) {
        return StadiumResponse.builder()
                .id(stadium.getId())
                .name(stadium.getName())
                .city(stadium.getCity())
                .capacity(stadium.getCapacity())
                .urlImg(stadium.getUrlImg())
                .build();
    }

    public static Stadium toStadium (CreateStadiumRequest createStadiumRequest) {
        return Stadium.builder()
                .name(createStadiumRequest.getName())
                .city(createStadiumRequest.getCity())
                .capacity(createStadiumRequest.getCapacity())
                .urlImg(createStadiumRequest.getUrlImg())
                .build();
    }
}
