package com.berryinkstamp.berrybackendservice.dtos.request;

import com.berryinkstamp.berrybackendservice.models.Design;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CollectionAdditionRequest {
    Set<Long> designs = new HashSet<>();
}
