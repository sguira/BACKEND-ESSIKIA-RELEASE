// package com.formation.demo.controllers;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.formation.demo.services.SuscriptionService;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("api/v1/request")
// @RequiredArgsConstructor
// public class RequestController {

// private final SuscriptionService suscriptionService;

// @PostMapping("/send")
// ResponseEntity<Object> sendRequestSuscription(@RequestParam(name = "email")
// String email,
// @RequestParam("module") String idModule) {

// try {
// return suscriptionService.register(email, idModule);
// } catch (Exception e) {
// return ResponseEntity.badRequest().body(e.getMessage());
// }

// }

// }
