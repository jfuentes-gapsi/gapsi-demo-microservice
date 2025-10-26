package mx.gapsi.gapsi_demo_microservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import mx.gapsi.commons.model.Base;
import mx.gapsi.commons.dto.CustomDto;
import mx.gapsi.commons.utils.InitObject;
import mx.gapsi.gapsi_demo_microservice.model.Label;
import mx.gapsi.gapsi_demo_microservice.service.MsService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/ms-gapsi/")
public class MsController {

    @Autowired
    private MsService msService;

    @GetMapping
    public ResponseEntity<CustomDto> findAll(@RequestParam(name="sortPaginator", defaultValue = "", required = true) String sortPaginator) {
        Base base = new Base();
        try {
            InitObject.toBase(base, sortPaginator);
            msService.findAll(base);
            base.getCustomDto().setCode(HttpStatus.OK.value());
            base.getCustomDto().setDescription("Successfully");
            return new ResponseEntity<CustomDto>(base.getCustomDto(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            base.getCustomDto().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            base.getCustomDto().setDescription(e.getMessage());
            return new ResponseEntity<CustomDto>(base.getCustomDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
	public ResponseEntity<?> insert(@RequestBody(required = true) Label label) {
        Base base = new Base();
        try {
            InitObject.toBase(base);
            base.setData(label);
            msService.insert(base);
            base.getCustomDto().setCode(HttpStatus.OK.value());
            base.getCustomDto().setDescription("Inserted successfully");
            return new ResponseEntity<CustomDto>(base.getCustomDto(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            base.getCustomDto().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            base.getCustomDto().setDescription(e.getMessage());
            return new ResponseEntity<CustomDto>(base.getCustomDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

    @PutMapping
    public ResponseEntity<?> update(@RequestBody(required = true) Label label) {
        Base base = new Base();
        try {
            InitObject.toBase(base);
            base.setData(label);
            msService.update(base);
            base.getCustomDto().setCode(HttpStatus.OK.value());
            base.getCustomDto().setDescription("Update successfully");
            return new ResponseEntity<CustomDto>(base.getCustomDto(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            base.getCustomDto().setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            base.getCustomDto().setDescription(e.getMessage());
            return new ResponseEntity<CustomDto>(base.getCustomDto(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}
}
