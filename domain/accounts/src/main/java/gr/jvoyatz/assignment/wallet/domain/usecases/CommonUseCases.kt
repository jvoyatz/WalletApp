package gr.jvoyatz.assignment.wallet.domain.usecases

import javax.inject.Inject


data class CommonUseCases @Inject constructor (
    val addFavoriteAccountUseCase: UseCases.AddFavoriteAccountUseCase,
    val removeFavoriteAccountUseCase: UseCases.RemoveFavoriteAccountUseCase
)