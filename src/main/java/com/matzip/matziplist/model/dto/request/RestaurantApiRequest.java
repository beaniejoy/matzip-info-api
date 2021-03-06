package com.matzip.matziplist.model.dto.request;

import com.matzip.matziplist.model.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantApiRequest {

    private String name;

    private String imgUrl;

    private String phoneNumber;

    private String address;

    private Double lat;

    private Double lng;

    private String description;

    private List<MenuItemApiRequest> menuItems;

    private List<TagApiRequest> tag;

    private List<OperTimeApiRequest> operTime;

    private GradeApiRequest grade;

    public Restaurant toEntity() {

        return Restaurant.builder()
                .name(name)
                .imgUrl(imgUrl)
                .phoneNumber(phoneNumber)
                .address(address)
                .lat(lat)
                .lng(lng)
                .description(description)
                .build();
    }
}
