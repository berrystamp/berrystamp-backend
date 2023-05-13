package com.berryinkstamp.berrybackendservice.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowRequest {
    @NotNull(message = "following id is required")
    private Long followingProfileId;
}
