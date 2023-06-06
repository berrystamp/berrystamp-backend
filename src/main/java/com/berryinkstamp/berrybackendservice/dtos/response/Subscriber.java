package com.berryinkstamp.berrybackendservice.dtos.response;

import com.berryinkstamp.berrybackendservice.enums.ProfileType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscriber {

    private Long userId;

    private String name;

    private String username;

    private String email;

    private String profileType;
}
