package gr.jvoyatz.afse.core.common

import com.google.common.truth.Truth
import gr.jvoyatz.afse.core.common.resultdata.ResultData
import gr.jvoyatz.afse.core.common.resultdata.asError
import gr.jvoyatz.afse.core.common.resultdata.asResult
import gr.jvoyatz.afse.core.common.resultdata.asSuccess
import gr.jvoyatz.afse.core.common.resultdata.isError
import gr.jvoyatz.afse.core.common.resultdata.isSuccess
import gr.jvoyatz.afse.core.common.resultdata.onError
import gr.jvoyatz.afse.core.common.resultdata.onSuccess
import gr.jvoyatz.afse.core.common.resultdata.resultOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
class ResultDataTest {

    @Test
    fun `resultdata state success with data non null`(){
        //given
        val data = 1

        //when
        val result = ResultData.success(data)

        //then
        Truth.assertThat(result.data).isNotNull()
        Truth.assertThat(result.data).isEqualTo(1)
    }

    @Test
    fun `resultdata state error with non null exception`(){
        //given
        val msg = "test exc"

        //when
        val result = ResultData.error(IllegalStateException(msg))

        //then
        Truth.assertThat(result.exception).isNotNull()
    }

    @Test
    fun `resultdata state is success returns true`(){
        //given
        val data = 1

        //when
        val result = ResultData.success(data)

        //then
        Truth.assertThat(result.isSuccess()).isTrue()
    }

    @Test
    fun `resultdata state is error returns true`(){
        //when
        val result = ResultData.error(null)

        //then
        Truth.assertThat(result.isError()).isTrue()
    }

    @Test
    fun `resultdata state as success returns non null`(){
        //given
        val data = 1

        //when
        val result = ResultData.success(data)

        //then
        Truth.assertThat(result.isSuccess()).isTrue()
        Truth.assertThat(result.asSuccess()).isNotNull()
    }

    @Test
    fun `resultdata state as error returns non null`(){

        //when
        val result = ResultData.error(null)

        //then
        Truth.assertThat(result.isError()).isTrue()
        Truth.assertThat(result.asError()).isNotNull()
    }

    @Test
    fun `resultdata success state as error returns null`(){
        //when
        val result = ResultData.error(null)

        //then
        Truth.assertThat(result.asSuccess()).isNull()
    }

    @Test
    fun `resultdata error state asSuccess returns null`(){
        //given
        val data = 1

        //when
        val result = ResultData.success(data)

        //then
        Truth.assertThat(result.asError()).isNull()
    }

    @Test
    fun `resultdata state onSuccess executes block`(){
        //given
        var wasCalled = false

        //when
        val result = ResultData.success(1)
        result.onSuccess {
            wasCalled = true
        }
        //then
        Truth.assertThat(wasCalled).isTrue()
    }

    @Test
    fun `resultdata error state onSuccess does not execute block`(){
        //given
        var wasCalled = false

        //when
        val result = ResultData.error(null)
        result.onSuccess {
            wasCalled = true
        }
        //then
        Truth.assertThat(wasCalled).isFalse()
    }


    @Test
    fun `resultdata error state onError executes block`(){
        //given
        var wasCalled = false

        //when
        val result = ResultData.error(null)
        result.onError {
            wasCalled = true
        }
        //then
        Truth.assertThat(wasCalled).isFalse()
    }

    @Test
    fun `resultdata success state onSuccess does not execute block`(){
        //given
        var wasCalled = false

        //when
        val result = ResultData.success(1)
        result.onError {
            wasCalled = true
        }
        //then
        Truth.assertThat(wasCalled).isFalse()
    }


    @Test
    fun `resultof block returns success`(){
        //given
        val data = "test"

        //when
        val result = resultOf { data }

        //then
        Truth.assertThat(result.isSuccess()).isTrue()
    }

    @Test
    fun `resultof block throws exception when cancellation exception`(){
        Assert.assertThrows(CancellationException::class.java ){
            resultOf { throw CancellationException() }
        }
    }

    @Test
    fun `resultof block returns error when timeout cancellation exception`(){
        //given
        val excc = IllegalStateException("test xception")

        //when
        val result = resultOf { throw excc }

        //then
        Truth.assertThat(result.isError()).isTrue()
        Truth.assertThat(result.asError()!!.exception).isInstanceOf(IllegalStateException::class.java)
    }


    @Test
    fun `flow as result, emits result success`() = runTest{
        //given
        val flow = flowOf(1).asResult()


        //when
        val res = flow.first()

        //then
        Truth.assertThat(res.isSuccess()).isTrue()
        Truth.assertThat(res.asSuccess()!!.data).isEqualTo(1)
    }

    @Test
    fun `flow as result, emits result error`() = runTest{
        //given
        val flow = flow<Int> {
            throw IllegalStateException("test exception")
           emit(1)
        }.asResult()


        //when
        val res = flow.first()

        //then
        Truth.assertThat(res.isError())
    }
}