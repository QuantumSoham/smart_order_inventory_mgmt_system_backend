package com.chubb.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.chubb.inventory.service.*;
import org.springframework.web.bind.annotation.*;
import com.chubb.inventory.dto.request.*;
import com.chubb.inventory.dto.response.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @PostMapping
    public ResponseEntity<WarehouseResponse> create(@RequestBody WarehouseRequest req) {
        WarehouseResponse response = service.create(req);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getAll() {
        
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getById(@PathVariable Long id) {
        
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> update(@PathVariable Long id,
                                                 @RequestBody WarehouseRequest req) {
        service.update(id, req);
        
        return ResponseEntity.ok(new MessageResponse("Warehouse updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> disable(@PathVariable Long id) {
        service.disable(id);
        
        return ResponseEntity.ok(new MessageResponse("Warehouse disabled"));
    }
}


//package com.chubb.inventory.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//import com.chubb.inventory.entity.*;
//import com.chubb.inventory.service.*;
//import com.chubb.inventory.dto.request.*;
//import com.chubb.inventory.dto.response.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/warehouses")
//@RequiredArgsConstructor
//public class WarehouseController {
//
//    private final WarehouseService service;
//
//    @PostMapping
//    public WarehouseResponse create(@RequestBody WarehouseRequest req) {
//        return service.create(req);
//    }
//
//    @GetMapping
//    public List<WarehouseResponse> getAll() {
//        return service.getAll();
//    }
//
//    @GetMapping("/{id}")
//    public WarehouseResponse getById(@PathVariable Long id) {
//        return service.getById(id);
//    }
//
//    @PutMapping("/{id}")
//    public MessageResponse update(@PathVariable Long id,
//                                  @RequestBody WarehouseRequest req) {
//        service.update(id, req);
//        return new MessageResponse("Warehouse updated");
//    }
//
//    @DeleteMapping("/{id}")
//    public MessageResponse disable(@PathVariable Long id) {
//        service.disable(id);
//        return new MessageResponse("Warehouse disabled");
//    }
//}
