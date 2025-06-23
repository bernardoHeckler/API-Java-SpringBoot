package br.edu.atitus.api_sample.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.atitus.api_sample.dtos.PointDTO;
import br.edu.atitus.api_sample.entities.PointEntity;
import br.edu.atitus.api_sample.services.PointService;

@RestController
@RequestMapping("/ws/point")
public class PointController {
	
	public final PointService service;

	public PointController(PointService service) {
		super();
		this.service = service;
	}	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable UUID id) throws Exception{
		service.deleteById(id);
		
		return ResponseEntity.ok("Localização excluída");
		
	}
	
	@PostMapping
	public ResponseEntity<PointEntity> save(@RequestBody PointDTO dto) throws Exception {
		PointEntity point = new PointEntity();
		BeanUtils.copyProperties(dto, point);
		
		service.save(point);
		
		return ResponseEntity.status(201).body(point);
	}

	// Método que fará a ATUALIZAÇÃO (PUT)
		@PutMapping("/{id}")
		public ResponseEntity<PointEntity> update(@PathVariable UUID id, @RequestBody PointDTO dto) throws Exception {
			PointEntity updatedPoint = new PointEntity();
			BeanUtils.copyProperties(dto, updatedPoint); // Copia os dados do DTO para o objeto temporário
			
			// Chama o método de atualização no serviço, passando o ID do caminho e os dados
			PointEntity result = service.update(id, updatedPoint);
			
			return ResponseEntity.ok(result); // Retorna o objeto atualizado
		}



	@GetMapping
	public ResponseEntity<List<PointEntity>> findAll() throws Exception {
		var lista = service.findAll();
		return ResponseEntity.ok(lista);
	}
	
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handlerException(Exception ex) {
		String message = ex.getMessage().replaceAll("\r\n", "");
		return ResponseEntity.badRequest().body(message);
	}
	
}
