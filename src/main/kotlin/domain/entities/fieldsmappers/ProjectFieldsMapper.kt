package domain.entities.fieldsmappers

import domain.entities.EntityCountable
import domain.entities.Item
import domain.entities.Project
import utils.replace

class ProjectFieldsMapper : IFieldsMapper<Project> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID.StringID(EntityFieldID.tag_name, "name"),
            EntityFieldID.StringID(EntityFieldID.tag_description, "description"),
            EntityFieldID.DateTimeID(name = "date"),
            EntityFieldID.StringID(extFile, name = "file"),
            EntityFieldID.EntitiesListID(
                tag = tag_items,
                name = "items"
            )
        )
    }


    override fun getFieldByID(entity: Project, fieldID: EntityFieldID): EntityField {
        return when (fieldID) {
            is EntityFieldID.EntityID -> {
                // tag = item.id
                val item = entity.items.find { it.entity.id == fieldID.tag }
                EntityField.EntityLink.EntityLinkCountable(
                    fieldID = fieldID,
                    entity = item?.entity,
                    entityClass = Item::class,
                    count = item?.count,
                    description = item?.entity?.name ?: ""
                )
            }
            is EntityFieldID.EntitiesListID -> EntityField.EntityLinksList(
                fieldID = fieldID,
                description = "items",
                entities = entity.items.map { i ->
                    val entityID = EntityFieldID.EntityID(
                        tag = i.entity.id,
                        name = i.entity.name
                    )

                    EntityField.EntityLink.EntityLinkCountable(
                        fieldID = entityID,
                        description = i.entity.name,        //?
                        entity = i.entity,
                        entityClass = Item::class,
                        count = i.count
                    )
                },
                entityClass = Item::class
            )
            is EntityFieldID.StringID ->
                // make EntityField.File
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> EntityField.StringField(
                        fieldID = fieldID,
                        description = "project's name",
                        value = entity.name
                    )
                    EntityFieldID.tag_description -> EntityField.StringField(
                        fieldID = fieldID,
                        description = "project's description",
                        value = entity.description
                    )
                    extFile -> EntityField.StringField(
                        fieldID = fieldID,
                        description = "external file attached",
                        value = entity.extFile ?: ""
                    )
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            is EntityFieldID.DateTimeID -> EntityField.DateTimeField(
                fieldID = fieldID,
                description = "project's created date",
                value = entity.dateTime
            )
            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: Project, field: EntityField): Project {
        return when (val fieldID = field.fieldID) {
            is EntityFieldID.StringID ->
                when (fieldID.tag) {
                    EntityFieldID.tag_name -> entity.copy(name = (field as? EntityField.StringField)?.value ?: "")
                    EntityFieldID.tag_description -> entity.copy(
                        description = (field as? EntityField.StringField)?.value ?: ""
                    )
                    extFile -> entity.copy(extFile = (field as? EntityField.StringField)?.value)
                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")
                }
            is EntityFieldID.EntityID -> setItem(entity, field)
                ?: throw IllegalArgumentException("unknown fieldID: $fieldID for entity: $entity")
            is EntityFieldID.EntitiesListID -> entity.copy(items = (field as EntityField.EntityLinksList).entities.mapNotNull { link ->
                (link.entity as? Item)?.let { i ->
                    EntityCountable(i, count = (link as? EntityField.EntityLink.EntityLinkCountable)?.count ?: 1L)
                }
            })
            is EntityFieldID.DateTimeID -> entity.copy(dateTime = (field as? EntityField.DateTimeField)?.value)
            else -> throw IllegalArgumentException("field with fieldID: $fieldID was not found in entity: $entity")
        }
    }

    private fun setItem(project: Project, field: EntityField): Project? {
        val fieldID = field.fieldID
        val itemID = fieldID.tag ?: return null
        val newEntity = (field as? EntityField.EntityLink.EntityLinkCountable)?.let {
            EntityCountable(it.entity as Item, it.count ?: 0L)
        } ?: return null
        val newItems = project.items.replace(
            newEntity
        ) {
            it.entity.id == itemID
        }

        return project.copy(items = newItems)

//        return project.copy(
//            items = project.items.mapIndexedNotNull { index, itemCountable ->
//                if (index == itemIndex) {
//                    val countableField = field as? EntityField.EntityLink.EntityLinkCountable
//                    countableField?.let {
//                        EntityCountable(it.entity as Item, it.count ?: 0L)
//                    }
//                } else itemCountable
//            }
//        )
    }

    companion object {
        const val extFile = "external_file"
        private const val tag_items = "tag_items"
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

