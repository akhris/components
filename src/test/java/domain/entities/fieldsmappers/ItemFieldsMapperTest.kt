package domain.entities.fieldsmappers

import domain.entities.Item
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.Items

internal class ItemFieldsMapperTest {

    lateinit var mapper: ItemFieldsMapper
    lateinit var item: Item

    @BeforeEach
    fun setUp() {
        mapper = ItemFieldsMapper()
        item = Items.Resistors.resistor1
    }

    @Test
    fun test_item_fields() {
        val fieldsIDs = mapper.getEntityIDs()
        println("item: ${Items.Resistors.resistor1}")
        println("fields:")
        fieldsIDs.forEachIndexed { index, fieldID ->
            println("fieldID $index. $fieldID")
            val value = mapper.getFieldParamsByFieldID(item, fieldID)
            println("tag: ${fieldID.tag} name: ${fieldID.name} value: ${value.entity} description: ${value.description}")
            println()
        }


    }


}