package application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import application.service.StatusService;

@RestController
@RequestMapping("/api/service")
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @PostMapping("/clear")
    @ResponseBody
    public void clearStatus(){
        statusService.clearStatus();
    }

    @GetMapping("/status")
    @ResponseBody
    public ResponseEntity<?> getStatus(){
        return ResponseEntity.ok().body(statusService.getStatus());
    }
}
