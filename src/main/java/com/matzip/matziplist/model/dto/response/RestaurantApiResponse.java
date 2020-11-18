package com.matzip.matziplist.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantApiResponse {

    private Long id;

    private String name;

    private String imgUrl;

    private String phoneNumber;

    private String address;

    private Double lat;

    private Double lng;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuItemApiResponse> menu;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TagApiResponse> tag;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OperTimeApiResponse> operTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GradeApiResponse grade;
}
