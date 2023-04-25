package gr.jvoyatz.afse.core.common

import com.google.common.truth.Truth
import gr.jvoyatz.afse.core.common.StringToIntDtoMapper.from
import gr.jvoyatz.afse.core.common.StringToIntDtoMapper.to
import org.junit.Test

class MapperTest {

    @Test
    fun `map string item to int type`(){
        //when
        val intVal = "1".to()

        //then
        Truth.assertThat(intVal.toString()).isEqualTo("1")

        //when
        val stringVal = 1.from()
        //then
        Truth.assertThat(stringVal).isEqualTo(1.toString())
    }
    @Test
    fun `map list items to another type`(){
        //given
        val list = listOf<String>("1", "2", "3")


        //when
        val mapList = list.mapList {
            it.to()
        }

        //then
        list.zip(mapList) { a, b ->
            Truth.assertThat(a).isEqualTo(b.toString())
        }
    }

}
