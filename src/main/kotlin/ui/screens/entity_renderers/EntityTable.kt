package ui.screens.entity_renderers

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.entities.IEntity
import domain.entities.Container
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.fieldsmappers.IFieldsMapper
import test.Containers

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : IEntity<*>> EntityTableContent(entities: List<T>, fieldsMapper: IFieldsMapper<T>) {
    val fields = remember(fieldsMapper, entities) {
        entities.flatMap {
            val columns = fieldsMapper.getEntityColumns(it)
            println()
            columns
        }.toSet()
    }

    val columnWidth = remember { 180.dp }
    val rowHeight = remember { 32.dp }

    val rowState = rememberLazyListState()
    val scrollState = rememberScrollState()

    LazyColumn {

        stickyHeader(key = "table_title") {
            Surface {
                Row(modifier = Modifier.horizontalScroll(scrollState)) {
                    fields.forEach { field ->
                        Text(
                            modifier =
                            Modifier
                                .width(columnWidth)
                                .height(rowHeight)
                                .border(1.dp, Color.DarkGray)
                                .padding(4.dp),
                            text = field.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }




        items(entities) { entity ->

            Row(modifier = Modifier.horizontalScroll(scrollState)) {
                fields.forEach { fieldID ->
                    val field = fieldsMapper.getFieldByID(entity, fieldID)
                    field?.let {

                        RenderEntityFieldCell(
                            modifier = Modifier.width(columnWidth).border(1.dp, Color.LightGray),
                            it,
                            onFieldChange = { changedField ->
                                fieldsMapper.mapIntoEntity(entity, changedField)
                            })

                    }
                }
            }
        }


    }

}


@Composable
private fun RenderEntityFieldCell(
    modifier: Modifier = Modifier,
    field: EntityField,
    onFieldChange: ((EntityField) -> Unit)? = null
) {
    when (field) {
        is EntityField.BooleanField -> {

        }
        is EntityField.CaptionField -> {

        }
        is EntityField.EntityLink -> {

        }
        is EntityField.EntityLinksList -> {

        }
        is EntityField.FavoriteField -> {

        }
        is EntityField.FloatField -> {

        }
        is EntityField.StringField -> {
            RenderTextFieldCell(modifier = modifier, field, onValueChange = {
                onFieldChange?.invoke(field.copy(value = it))
            })
        }
        is EntityField.URLField -> {

        }
    }
}


@Preview
@Composable
fun TestTable() {
    val entities = listOf(Containers.box1, Containers.box2, Containers.box3)
    val fieldsMapper = FieldsMapperFactory().getFieldsMapper(Container::class)
    EntityTableContent(entities, fieldsMapper)
}