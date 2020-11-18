package com.matzip.matziplist.model.entity;

import com.matzip.matziplist.model.dto.response.RestaurantApiResponse;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"menuItemList", "tagList", "operTimeList", "grade"})
@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imgUrl;

    private String phoneNumber;

    private String address;

    private Double lat;

    private Double lng;

    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<MenuItem> menuItemList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<Tag> tagList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private List<OperTime> operTimeList;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "restaurant")
    private Grade grade;

    // Restaurant List로 보내줄 때 (보류)
    public RestaurantApiResponse toResponseForList() {
        return RestaurantApiResponse.builder()
                .id(id)
                .name(name)
                .imgUrl(imgUrl)
                .phoneNumber(phoneNumber)
                .address(address)
                .lat(lat)
                .lng(lng)
                .description(description)
                .menu(menuItemList.stream()
                        .map(MenuItem::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // 단일 Restaurant을 보내줄 때 (상세 페이지)
    public RestaurantApiResponse toResponse() {
        return RestaurantApiResponse.builder()
                .id(id)
                .name(name)
                .imgUrl(imgUrl)
                .phoneNumber(phoneNumber)
                .address(address)
                .lat(lat)
                .lng(lng)
                .description(description)
                .menu(menuItemList.stream()
                        .map(MenuItem::toResponse)
                        .collect(Collectors.toList()))
                .tag(tagList.stream()
                        .map(Tag::toResponse)
                        .collect(Collectors.toList()))
                .operTime(operTimeList.stream()
                        .map(OperTime::toResponse)
                        .collect(Collectors.toList()))
                .grade(grade.toResponse())
                .build();
    }
}
