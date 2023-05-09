package com.berryinkstamp.berrybackendservice.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CollectionMoveRequest {
    Set<Long> designs = new HashSet<>();
    private Long newCollection;
}
