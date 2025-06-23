package br.edu.atitus.api_sample.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_sample.entities.PointEntity;
import br.edu.atitus.api_sample.entities.UserEntity;
import br.edu.atitus.api_sample.repositories.PointRepository;

@Service
public class PointService {

	private final PointRepository repository;

	public PointService(PointRepository repository) {
		super();
		this.repository = repository;
	}
	
	// MÉTODO SAVE (AGORA MAIS FOCADO EM CRIAÇÃO E VALIDAÇÕES INICIAIS)
	public PointEntity save(PointEntity point) throws Exception {
		if (point == null)
			throw new Exception("Objeto Nulo");
		
		// Validações de campos obrigatórios
		if (point.getDescription() == null || point.getDescription().isEmpty())
			throw new Exception("Descrição Inválida");
		point.setDescription(point.getDescription().trim());
		
		if (point.getLatitude() < -90 || point.getLatitude() > 90)
			throw new Exception("Latitude Inválida");
		
		if (point.getLongitude() < -180 || point.getLongitude() > 180)
			throw new Exception("Longitude Inválida");
		
		// Associa o ponto ao usuário autenticado (isso é para criação)
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		point.setUser(userAuth);
		
		return repository.save(point);
	}
	
	// MÉTODO PARA ATUALIZAÇÃO (PUT)
	public PointEntity update(UUID id, PointEntity updatedPointData) throws Exception {
		// 1. Verificar se o ponto existe
		PointEntity existingPoint = repository.findById(id)
				.orElseThrow(() -> new Exception("Não existe ponto cadastrado com este ID"));
		
		// 2. Verificar se o ponto pertence ao usuário logado
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!existingPoint.getUser().getId().equals(userAuth.getId()))
			throw new Exception("Você não tem permissão para atualizar este registro");
			
		// 3. Aplicar as atualizações do DTO para o ponto existente
		// As validações de latitude/longitude/description são aplicadas aqui
		if (updatedPointData.getDescription() == null || updatedPointData.getDescription().isEmpty())
			throw new Exception("Descrição Inválida");
		existingPoint.setDescription(updatedPointData.getDescription().trim());
		
		if (updatedPointData.getLatitude() < -90 || updatedPointData.getLatitude() > 90)
			throw new Exception("Latitude Inválida");
		existingPoint.setLatitude(updatedPointData.getLatitude());
		
		if (updatedPointData.getLongitude() < -180 || updatedPointData.getLongitude() > 180)
			throw new Exception("Longitude Inválida");
		existingPoint.setLongitude(updatedPointData.getLongitude());
				
		// 4. Salva (atualiza) o ponto no banco de dados
		return repository.save(existingPoint);
	}
	
	public void deleteById(UUID id) throws Exception {
		var point = repository.findById(id)
				.orElseThrow(() -> new Exception("Não existe ponto cadastrado com este ID"));
		
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (!point.getUser().getId().equals(userAuth.getId()))
			throw new Exception("Você não tem permissão para apagar este registro");
		
		repository.deleteById(id);
	}
	
	public List<PointEntity> findAll() {
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return repository.findByUser(userAuth);
	}
	
}