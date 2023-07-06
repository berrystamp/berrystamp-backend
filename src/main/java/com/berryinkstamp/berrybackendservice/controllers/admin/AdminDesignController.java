package com.berryinkstamp.berrybackendservice.controllers.admin;

import com.berryinkstamp.berrybackendservice.enums.DesignStatus;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin/designs")
public class AdminDesignController {
    private DesignService designService;

    @PatchMapping("/{id}/review")
    public ResponseEntity<Design> adminAcceptOrRejectDesign(@PathVariable("id") Long designId, @RequestParam boolean approved){
        return new ResponseEntity<>(designService.acceptDesign(designId, approved), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<Design>> adminFetchAllDesign(@RequestParam(required = false) DesignStatus status, Pageable pageable){
      return new ResponseEntity<>(designService.adminFetchAllDesign(status, pageable),HttpStatus.OK);
    }
}
