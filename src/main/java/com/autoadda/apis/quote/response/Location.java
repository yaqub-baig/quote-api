package com.autoadda.apis.quote.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Integer id;
    private String streetAddress1;
    private String streetAddress2;
    private String city;
    private String postalCode;
    private String area;
    private String state;
    private String country;
    private String gmap;
    private String type;

}
