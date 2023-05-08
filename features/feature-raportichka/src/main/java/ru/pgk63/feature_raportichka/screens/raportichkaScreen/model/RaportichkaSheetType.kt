package ru.pgk63.feature_raportichka.screens.raportichkaScreen.model

import ru.pgk63.core_model.raportichka.RaportichkaRow

internal sealed class RaportichkaSheetType {
    data class RaportichkaRowMenu(val row: RaportichkaRow): RaportichkaSheetType()
    object AddRaportichkaMenu: RaportichkaSheetType()
    object AddRaportichka: RaportichkaSheetType()
}