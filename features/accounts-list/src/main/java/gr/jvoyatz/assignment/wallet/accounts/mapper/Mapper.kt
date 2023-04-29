package gr.jvoyatz.assignment.wallet.accounts.mapper

import gr.jvoyatz.assignment.wallet.accounts.model.AccountUi
import gr.jvoyatz.assignment.wallet.domain.models.Account

fun Account.toUiModel() = AccountUi(
    id = id,
    accountNickname = accountNickname,
    accountNumber = accountNumber,
    accountType = accountType,
    balance = balance,
    currencyCode = currencyCode,
    isFavorite = isFavorite
)