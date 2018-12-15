package com.sda.rest.server.phone.controller;

import com.sda.rest.server.phone.PhoneDAO;
import com.sda.rest.server.phone.PhoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "api/phones")
public class PhonesController {
    @Autowired
    private PhoneDAO phoneDAO;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PhoneDTO>> getPhones() {
        List<PhoneDTO> phones = phoneDAO.getPhones();
        if (phones != null && phones.size() > 0) {
            return ResponseEntity.ok(phones);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PhoneDTO> addPhone(@RequestBody PhoneDTO phoneDTO) {
        phoneDAO.insertPhones(Arrays.asList(phoneDTO));
        PhoneDTO addedPhone = phoneDAO.getPhoneByName(phoneDTO.getName());
        if (addedPhone != null) {
            URI uriString = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(addedPhone.getId()).toUri();
            return ResponseEntity.created(uriString).body(addedPhone);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET) //z URLa
    public ResponseEntity<PhoneDTO> getPhone(@PathVariable("id") String id){
        PhoneDTO phoneById = phoneDAO.getPhoneById(id);
        if(phoneById != null){
            return ResponseEntity.ok(phoneById);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @RequestMapping(method = RequestMethod.GET, params = "name") //po ?
    public ResponseEntity<PhoneDTO> getPhoneByName(@RequestParam("name") String name){
        PhoneDTO phoneByName = phoneDAO.getPhoneByName(name);
        if(phoneByName != null){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("test", "test"); //mapa headerow
//            ResponseEntity<PhoneDTO> ok = ResponseEntity.ok().header("test-header", "test-header").body(phoneByName);
            ResponseEntity<PhoneDTO> ok = ResponseEntity.ok().headers(httpHeaders).body(phoneByName);
            return ok;
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
