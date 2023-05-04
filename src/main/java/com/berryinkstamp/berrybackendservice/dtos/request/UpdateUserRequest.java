package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserRequest {

     private String phoneNumber;
     private Gender gender;
     private String profilePicture;

     @NotBlank(message = "name is required")
     private String name;
     private String address;
     private String city;
     private String state;
     private String country;
     private String postalCode;

}
