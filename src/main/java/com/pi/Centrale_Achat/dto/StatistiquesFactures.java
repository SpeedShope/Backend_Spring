package com.pi.Centrale_Achat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class StatistiquesFactures {
    private int nombreTotalFactures;
    private float montantTotalVentes;
    private float montantMoyenVentes;
}