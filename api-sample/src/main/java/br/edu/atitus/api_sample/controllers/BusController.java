package br.edu.atitus.api_sample.controllers;

import br.edu.atitus.api_sample.dtos.BusProximityDTO;
import br.edu.atitus.api_sample.services.BusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ws/bus") // Novo caminho base para endpoints de ônibus
public class BusController {

    private final BusService busService; // Injeção de dependência do novo serviço

    // Construtor para injeção de dependência
    public BusController(BusService busService) {
        this.busService = busService;
    }

    // Endpoint GET para buscar ônibus próximos
    // Requer autenticação (já configurado via ConfigSecurity para /ws/**)
    @GetMapping("/nearest")
    public ResponseEntity<List<BusProximityDTO>> getNearestBuses(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "1.0") double radiusKm) { // Raio padrão de 1km

        // Validações básicas para os parâmetros de entrada
        if (latitude < -90 || latitude > 90) {
            return ResponseEntity.badRequest().body(null); // Poderia ser uma exceção mais específica
        }
        if (longitude < -180 || longitude > 180) {
            return ResponseEntity.badRequest().body(null); // Poderia ser uma exceção mais específica
        }
        if (radiusKm <= 0) {
            return ResponseEntity.badRequest().body(null); // Raio deve ser positivo
        }

        List<BusProximityDTO> nearestBuses = busService.findNearestBuses(latitude, longitude, radiusKm);
        return ResponseEntity.ok(nearestBuses);
    }

    // Manipulador de exceções global para este controlador (opcional, já existe um em AuthController)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handlerException(Exception ex) {
        String message = ex.getMessage().replaceAll("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }
}
