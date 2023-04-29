package gr.jvoyatz.assignment.wallet.features.accounts.ui.mapper

import gr.jvoyatz.assignment.wallet.features.accounts.domain.models.Account
import gr.jvoyatz.assignment.wallet.features.accounts.ui.model.AccountUi


fun Account.toUiModel() = AccountUi(
    id = id,
    accountNickname = accountNickname,
    accountNumber = accountNumber,
    accountType = accountType,
    balance = balance,
    currencyCode = currencyCode,
    isFavorite = isFavorite
)