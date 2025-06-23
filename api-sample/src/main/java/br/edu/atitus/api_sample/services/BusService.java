package br.edu.atitus.api_sample.services;

import br.edu.atitus.api_sample.dtos.BusProximityDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BusService {

    // Dados de ônibus simulados em memória para demonstração
    // Cada array interno é [latitude, longitude, description]
    private static final List<Object[]> SIMULATED_BUS_LOCATIONS = Arrays.asList(
        new Object[]{-23.5510, -46.6340, "Ônibus 101 - Centro", "bus_101"},
        new Object[]{-23.5480, -46.6300, "Ônibus 202 - Santa Cruz", "bus_202"},
        new Object[]{-23.5600, -46.6450, "Ônibus 303 - Avenida Paulista", "bus_303"},
        new Object[]{-23.5550, -46.6380, "Ônibus 404 - Brigadeiro", "bus_404"},
        new Object[]{-23.6000, -46.7000, "Ônibus 505 - Longe", "bus_505"}, // Ônibus mais distante
        new Object[]{-23.5500, -46.6330, "Ônibus 606 - Perto do Ponto", "bus_606"} // Ônibus bem próximo
    );

    // Método para encontrar ônibus próximos a uma dada localização e raio
    // O usuário deve estar autenticado para chamar este serviço (garantido pelo controlador)
    public List<BusProximityDTO> findNearestBuses(double userLatitude, double userLongitude, double radiusKm) {
        List<BusProximityDTO> nearestBuses = new ArrayList<>();

        for (Object[] busData : SIMULATED_BUS_LOCATIONS) {
            double busLatitude = (double) busData[0];
            double busLongitude = (double) busData[1];
            String busDescription = (String) busData[2];
            String busId = (String) busData[3];

            // Calcula a distância entre o ponto do usuário e o ônibus
            double distance = calculateDistance(userLatitude, userLongitude, busLatitude, busLongitude);

            // Se o ônibus estiver dentro do raio especificado, adiciona à lista
            if (distance <= radiusKm) {
                nearestBuses.add(new BusProximityDTO(busId, busDescription, busLatitude, busLongitude, distance));
            }
        }

        // Ordena os ônibus pela distância (os mais próximos primeiro)
        return nearestBuses.stream()
                .sorted((b1, b2) -> Double.compare(b1.distanceKm(), b2.distanceKm()))
                .collect(Collectors.toList());
    }

    /**
     * Calcula a distância entre dois pontos de coordenadas (latitude e longitude) em quilômetros.
     * Utiliza uma aproximação de distância euclidiana simples para fins de demonstração,
     * mais precisa para distâncias curtas. Para precisão em grandes distâncias,
     * a fórmula de Haversine seria mais adequada, mas exigiria mais código.
     * @param lat1 Latitude do primeiro ponto
     * @param lon1 Longitude do primeiro ponto
     * @param lat2 Latitude do segundo ponto
     * @param lon2 Longitude do segundo ponto
     * @return Distância em quilômetros
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Raio da Terra em quilômetros (média aproximada)
        final double EARTH_RADIUS_KM = 6371.0;

        // Converte graus para radianos
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Diferenças de coordenadas
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Fórmula euclidiana simplificada (para pequenos deslocamentos, como em uma cidade)
        // Isso é uma aproximação. Para maior precisão, use a fórmula de Haversine.
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}
