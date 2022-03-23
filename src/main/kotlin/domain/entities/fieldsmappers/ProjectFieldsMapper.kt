package domain.entities.fieldsmappers

import com.akhris.domain.core.utils.log
import domain.entities.EntityCountable
import domain.entities.Item
import domain.entities.Project

class ProjectFieldsMapper : BaseFieldsMapper<Project>() {

    private val tag_items = "tag_items"

    override fun getEntityIDs(entity: Project): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(EntityFieldID.tag_name, "name"),
            EntityFieldID.StringID(EntityFieldID.tag_description, "description"),
            EntityFieldID.EntitiesListID(
                tag = tag_items,
                name = "items",
                entitiesIDs = List(entity.items.size) { index ->
                    EntityFieldID.EntityID(
                        tag = "$tag_items$index",
                        name = "item ${index + 1}",
                        entityClass = Item::class
                    )
                },
                entityClass = Item::class
            )
        )
    }


    override fun getFieldParamsByFieldID(entity: Project, fieldID: EntityFieldID): DescriptiveFieldValue {
        log("get field params for id: $fieldID")
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                val index = fieldID.tag.substring(startIndex = tag_items.length).toIntOrNull() ?: -1
                val item = entity.items.getOrNull(index)
                log("item: $item")
                DescriptiveFieldValue.CountableField(
                    entity = item?.entity,
                    description = item?.entity?.name ?: "",
                    count = item?.count
                )
            }
            is EntityFieldID.EntitiesListID -> DescriptiveFieldValue.CommonField(
                entity = entity.items,
                description = "items"
            )
            is EntityFieldID.StringID ->
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> DescriptiveFieldValue.CommonField(
                        entity = entity.name,
                        description = "project's name"
                    )
                    EntityFieldID.tag_description -> DescriptiveFieldValue.CommonField(
                        entity = entity.description,
                        description = "project's description"
                    )
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }

            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: Project, field: EntityField): Project {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID ->
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> entity.copy(name = (field as EntityField.StringField).value)
                    EntityFieldID.tag_description -> entity.copy(description = (field as EntityField.StringField).value)
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            is EntityFieldID.EntityID -> setItem(entity, field)
                ?: throw IllegalArgumentException("unknown fieldID: $fieldID for entity: $entity")
            is EntityFieldID.EntitiesListID -> entity.copy(items = (field as EntityField.EntityLinksList).entities.mapNotNull { link ->
                (link as? EntityField.EntityLink.EntityLinkCountable)?.let {
                    (it.entity as? Item)?.let { i ->
                        EntityCountable(i, it.count ?: 1L)
                    }
                }
            })
            else -> throw IllegalArgumentException("field with fieldID: $fieldID was not found in entity: $entity")
        }
    }

    private fun setItem(project: Project, field: EntityField): Project? {
        val fieldID = field.fieldID
        val itemIndex = fieldID.tag.substring(startIndex = tag_items.length).toIntOrNull() ?: return null
        if (project.items.getOrNull(itemIndex) == null) {
            return null
        }
        return project.copy(
            items = project.items.mapIndexedNotNull { index, itemCountable ->
                if (index == itemIndex) {
                    val countableField = field as? EntityField.EntityLink.EntityLinkCountable
                    countableField?.let {
                        EntityCountable(it.entity as Item, it.count ?: 0L)
                    }
                } else itemCountable
            }
        )
    }

}
//    override fun mapFields(entity: Any): List<EntityField> {
//        return when (entity) {
//            is ObjectType -> mapObjectTypeFields(entity)
//            else -> throw IllegalArgumentException("$this cannot map $entity fields, use another mapper")
//        }
//    }

//    private fun mapObjectTypeFields(type: ObjectType): List<EntityField> {
//        return listOf(
//            EntityField.StringField(
//                tag = "objecttype_field_name",
//                name = "name",
//                description = "type name",
//                value = type.name
//            ),
//            EntityField.EntityLinksList(
//                tag = "objecttype_field_parameters",
//                name = "type parameters",
//                description = "default parameters set that is mandatory for this type",
//                entities = type.parameters.mapIndexed { i, p ->
//                    EntityField.EntityLink(
//                        tag = "objecttype_field_parameter_link$i",
//                        name = p.name,
//                        description = p.description,
//                        entity = p
//                    )
//                }
//
//
//            ),
//            EntityField.CaptionField(
//                tag = "objecttype_field_id",
//                name = "UUID",
//                description = "unique id of the object type",
//                caption = type.id
//            )
//        )
//    }
//

