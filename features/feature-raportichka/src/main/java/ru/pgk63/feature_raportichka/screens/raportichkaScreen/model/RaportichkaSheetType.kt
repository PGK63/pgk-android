package ru.pgk63.feature_raportichka.screens.raportichkaScreen.model

internal sealed class RaportichkaSheetType {
    data class RaportichkaRowMenu(val row: ru.pgk63.core_model.deputyHeadma.RaportichkaRow): RaportichkaSheetType()
    object AddRaportichkaMenu: RaportichkaSheetType()
    object AddRaportichka: RaportichkaSheetType()
}