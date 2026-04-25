package com.recurrence.pagbank.domain.commons;

import org.jspecify.annotations.NonNull;

public record PagBankBillingInfo(
    String type,
    PagBankCard card,
    PagBankBoleto boleto
){}


// Todo aceitar o tipo BOLETO mapear o link do pdf e o da imagem

//"boleto": {
//                    "id": "678A809E-1CE6-4202-848C-566F79910F69",
//                    "barcode": "03399853012970000024227020901016278150000015630",
//                    "formatted_barcode": "03399.85301 29700.000242 27020.901016 2 78150000015630",
//                    "due_at": "2026-04-06"
//                }