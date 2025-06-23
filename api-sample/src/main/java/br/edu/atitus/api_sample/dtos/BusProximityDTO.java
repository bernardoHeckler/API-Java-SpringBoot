package br.edu.atitus.api_sample.dtos;

public record BusProximityDTO(
    String busId,           // Um ID simulado para o ônibus
    String description,     // Descrição do ônibus (ex: Linha 100, Placa ABC-1234)
    double latitude,        // Latitude atual do ônibus simulado
    double longitude,       // Longitude atual do ônibus simulado
    double distanceKm       // Distância em quilômetros até o ponto de busca
) {}
