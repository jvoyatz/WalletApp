package gr.jvoyatz.assignment.core.common.resultdata

/**
 * Sealed interface with two possible states
 *  1) Success
 *  2) Error
 *
 *  Acts as a wrapper for data.
 *
 *  Can be used as well to wrap the result of given a block
 *  into a instance of this class.
 */
sealed interface ResultData<out T>{
    data class Success<T>(val data: T): ResultData<T>

    data class Error(val exception: Throwable? = null): ResultData<Nothing>

    companion object{
        /**
         * Creates an instance of the Success instance
         */
        fun <T> success(data: T): Success<T> = Success(data)

        /**
         * Creates an instance of the Error instance
         */
        fun error(throwable: Throwable?): Error = Error(throwable)
    }
}
