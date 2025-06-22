package br.edu.atitus.api_sample.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.edu.atitus.api_sample.components.ExtractEmail;
import br.edu.atitus.api_sample.dtos.PointDTO;
import br.edu.atitus.api_sample.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_sample.entities.PointEntity;
import br.edu.atitus.api_sample.entities.UserEntity;
import br.edu.atitus.api_sample.repositories.PointRepository;

@Service
public class PointService {

	private final PointRepository repository;
	private final UserRepository userRepository;

	public PointService(PointRepository repository, UserRepository userRepository) {
		super();
		this.repository = repository;
        this.userRepository = userRepository;
    }
	
	public PointEntity save(PointEntity point) throws Exception {
		if (point == null)
			throw new Exception("Objeto Nulo");
		
		if (point.getDescription() == null || point.getDescription().isEmpty())
			throw new Exception("Descrição Inválida");
		point.setDescription(point.getDescription().trim());
		
		if (point.getLatitude() < -90 || point.getLatitude() > 90)
			throw new Exception("Latitude Inválida");
		
		if (point.getLongitude() < -180 || point.getLongitude() > 180)
			throw new Exception("Longitude Inválida");
		
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		point.setUser(userAuth);
		
		return repository.save(point);
	}


	public void editPoint (UUID id , PointDTO pointDTO) throws Exception {
		PointEntity point = repository.findById(id).orElseThrow(() -> new Exception("Point não existe"));
		UserEntity user = point.getUser();
		String email = ExtractEmail.extrairEmail();
		System.out.println(email);
		System.out.println(user.getEmail());
		if (!user.getEmail().equals(email)) {
			throw new Exception("Esse usuario não pode realizar essa ação");
		}
		point.setDescription(pointDTO.description());
		point.setLatitude(pointDTO.latitude());
		point.setLongitude(pointDTO.longitude());
		repository.save(point);
	}



	public void deleteById(UUID id) throws Exception {
		var point = repository.findById(id)
				.orElseThrow(() -> new Exception("Não existe ponto cadastrado com este ID"));
		
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if (!point.getUser().getId().equals(userAuth.getId()))
			throw new Exception("Você não tem permissão para apagar este registro");
		
		repository.deleteById(id);
	}

	public List<PointEntity> findAll() throws Exception {
		System.out.println(ExtractEmail.extrairEmail());
		String email = ExtractEmail.extrairEmail();
		return repository.findByUserEmail(email);
	}
	
}
