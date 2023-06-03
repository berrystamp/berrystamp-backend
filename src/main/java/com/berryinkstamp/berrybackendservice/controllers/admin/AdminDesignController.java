package com.berryinkstamp.berrybackendservice.controllers.admin;

import com.berryinkstamp.berrybackendservice.enums.DesignStatus;
import com.berryinkstamp.berrybackendservice.models.Design;
import com.berryinkstamp.berrybackendservice.services.DesignService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminDesignController {
    private DesignService designService;

    @PutMapping("/design/{id}/accept")
    public ResponseEntity<?> acceptDesign(@PathVariable("id") Long designId){
        designService.acceptDesign(designId);
        return new ResponseEntity<>("Design accepted successfully", HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/design/{id}/decline")
    public ResponseEntity<?> declineDesign(@PathVariable("id") Long designId){
        designService.declineDesign(designId);
        return new ResponseEntity<>("Design decline successfully",HttpStatus.OK);
    }
    @GetMapping("/designs")
    public ResponseEntity<List<Design>> fetchDesign(@RequestParam(value = "design_status",required = false)DesignStatus designStatus){
      List<Design> designs;
      if (designStatus != null){
          designs = designService.fetchDesignByDesignStatus(designStatus);
      }else{
          designs = designService.fetchAllDesign();
      }
      return new ResponseEntity<>(designs,HttpStatus.OK);
    }
}
