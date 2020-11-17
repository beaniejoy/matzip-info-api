package com.matzip.matziplist.interfaces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matzip.matziplist.application.RestaurantService;
import com.matzip.matziplist.model.dto.request.RestaurantApiRequest;
import com.matzip.matziplist.model.dto.response.RestaurantApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public ResponseEntity<List<RestaurantApiResponse>> list() {

        List<RestaurantApiResponse> responseList = restaurantService.getRestaurantList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<RestaurantApiResponse> detail(@PathVariable Long id) {

        RestaurantApiResponse response = restaurantService.getRestaurant(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/restaurants/all")
    public ResponseEntity<String> bulkSave() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

//        String path = "./json"; // dev path
        String path = "/home/ec2-user/app/matziplist"; // AWS ec2 path
        String file = "/matzip.json";

        List<RestaurantApiRequest> requestList = objectMapper.readValue(new File(path + file),
                new TypeReference<List<RestaurantApiRequest>>() {});

        restaurantService.bulkSave(requestList);

        return ResponseEntity.ok("Success All Updates");
    }

}
