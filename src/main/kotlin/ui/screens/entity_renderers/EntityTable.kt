package ui.screens.entity_renderers

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.entities.IEntity
import domain.entities.Container
import domain.entities.fieldsmappers.EntityField
import domain.entities.fieldsmappers.FieldsMapperFactory
import domain.entities.fieldsmappers.IFieldsMapper
import domain.entities.fieldsmappers.flatten
import test.Containers

/**
 * Renders list of entities in a table-mode.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : IEntity<*>> EntityTableContent(entities: List<T>, fieldsMapper: IFieldsMapper<T>) {
    val fields = remember(fieldsMapper, entities) {
        entities.flatMap {
            fieldsMapper.getEntityIDs(it).flatten()
        }.toSet()
    }

    val columnWidth = remember { 180.dp }
    val headerRowHeight = remember { 32.dp }
    val rowHeight = remember { 60.dp }


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
                                .height(headerRowHeight)
                                .border(1.dp, Color.DarkGray)
                                .padding(4.dp)
                                .align(Alignment.CenterVertically),
                            text = field.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.h6
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
                            modifier = Modifier.width(columnWidth).height(rowHeight).border(1.dp, Color.LightGray),
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

/**
 * Renders entity's field cell
 */
@Composable
private fun RowScope.RenderEntityFieldCell(
    modifier: Modifier = Modifier,
    field: EntityField,
    onFieldChange: ((EntityField) -> Unit)? = null
) {
    when (field) {
        is EntityField.BooleanField -> {
            RenderBooleanFieldCell(modifier = modifier, field, onValueChange = {
                onFieldChange?.invoke(field.copy(value = it))
            })
        }
        is EntityField.EntityLink -> {
            RenderEntityLinkFieldCell(modifier = modifier, field, onValueChange = {
                onFieldChange?.invoke(field.copy(entity = it))
            })
        }
        is EntityField.EntityLinksList -> {

        }

        is EntityField.FloatField -> {

        }
        is EntityField.StringField -> {
            RenderTextFieldCell(modifier = modifier, field, onValueChange = {
                onFieldChange?.invoke(field.copy(value = it))
            })
        }
        is EntityField.DateTimeField -> {}
        is EntityField.LongField -> {}
    }
}


@Preview
@Composable
fun TestTable() {
    val entities = listOf(Containers.box1, Containers.box2, Containers.box3)
    val fieldsMapper = FieldsMapperFactory().getFieldsMapper(Container::class)
    EntityTableContent(entities, fieldsMapper)
}