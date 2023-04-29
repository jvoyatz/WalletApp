package gr.jvoyatz.assignment.wallet.core.api.config.response

import android.accounts.AccountsException
import gr.jvoyatz.assignment.core.common.resultdata.ResultData

object ApiResponseExt {
    fun <T, E> ApiResponse<T, E>.asResult(): ResultData<T> {
        return if (this.isSuccess()) ResultData.success(this.asSuccess()!!.body)
        else ResultData.error(
            if (this.isError()) AccountsException(
                this.asError()!!.extractError()
            ) else null
        )
    }
}
