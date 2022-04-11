package domain.entities.fieldsmappers

import domain.entities.Item
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FieldsMapperFactoryTest {


    lateinit var factory: FieldsMapperFactory


    @BeforeEach
    fun setup() {
        factory = FieldsMapperFactory()
    }


    @Test
    fun get_items_mapper_test() {
        val itemsMapper = factory.getFieldsMapper(Item::class)
        println("got items mapper: $itemsMapper")
        println("items column fields:")
        itemsMapper.getEntityIDs().forEach {
            println("tag: ${it.tag}, name: ${it.name}")
        }
    }
}