package domain.entities.fieldsmappers

import domain.entities.EntityCountable
import domain.entities.Item
import domain.entities.Project
import utils.replace

class ProjectFieldsMapper : IFieldsMapper<Project> {

    override fun getEntityIDs(): List<EntityFieldID> {
        return listOf(
            EntityFieldID(tag = EntityFieldID.tag_name, name = "name"),
            EntityFieldID(tag = EntityFieldID.tag_description, name = "description"),
            EntityFieldID(tag = EntityFieldID.tag_date_time, name = "date"),
            EntityFieldID(tag = tag_ext_file, name = "file"),
            EntityFieldID(tag = tag_items, name = "items")
        )
    }


    override fun getFieldByID(entity: Project, fieldID: EntityFieldID): EntityField {
        return when (fieldID.tag) {
            tag_items -> EntityField.EntityLinksList(
                fieldID = fieldID,
                descriptionID = "items",
                entities = entity.items.map { i ->
                    val entityID = EntityFieldID(
                        tag = i.entity.id,
                        name = i.entity.name
                    )

                    EntityField.EntityLink.EntityLinkCountable(
                        fieldID = entityID,
                        descriptionID = i.entity.name,        //?
                        entity = i.entity,
                        entityClass = Item::class,
                        count = i.count
                    )
                },
                entityClass = Item::class
            )

            EntityFieldID.tag_name -> EntityField.StringField(
                fieldID = fieldID,
                descriptionID = "project's name",
                value = entity.name
            )
            EntityFieldID.tag_description -> EntityField.StringField(
                fieldID = fieldID,
                descriptionID = "project's description",
                value = entity.description
            )
            tag_ext_file -> EntityField.StringField(
                fieldID = fieldID,
                descriptionID = "external file attached",
                value = entity.extFile ?: ""
            )
//                    else -> throw IllegalArgumentException("field with tag: ${fieldID.tag} was not found in entity: $entity")

            EntityFieldID.tag_date_time -> EntityField.DateTimeField(
                fieldID = fieldID,
                descriptionID = "project's created date",
                value = entity.dateTime
            )
            else -> {
                // tag = item.id
                val item = entity.items.find { it.entity.id == fieldID.tag }
                    ?: throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
                EntityField.EntityLink.EntityLinkCountable(
                    fieldID = fieldID,
                    entity = item.entity,
                    entityClass = Item::class,
                    count = item.count,
                    descriptionID = item.entity.name
                )
            }
//            else -> throw IllegalArgumentException("field with id: $fieldID was not found in entity: $entity")
        }
    }

    override fun mapIntoEntity(entity: Project, field: EntityField): Project {
        return when (field) {
            is EntityField.StringField ->
                when (field.fieldID.tag) {
                    EntityFieldID.tag_name -> entity.copy(name = field.value)
                    EntityFieldID.tag_description -> entity.copy(description = field.value)
                    tag_ext_file -> entity.copy(extFile = field.value)
                    else -> throw IllegalArgumentException("field with tag: ${field.fieldID.tag} was not found in entity: $entity")
                }
            is EntityField.EntityLink -> setItem(entity, field)
                ?: throw IllegalArgumentException("unknown fieldID: ${field.fieldID} for entity: $entity")
            is EntityField.EntityLinksList -> entity.copy(items = field.entities.mapNotNull { link ->
                (link.entity as? Item)?.let { i ->
                    EntityCountable(i, count = (link as? EntityField.EntityLink.EntityLinkCountable)?.count ?: 1L)
                }
            })
            is EntityField.DateTimeField -> entity.copy(dateTime = field.value)
            else -> throw IllegalArgumentException("field with fieldID: ${field.fieldID} was not found in entity: $entity")
        }
    }

    private fun setItem(project: Project, field: EntityField.EntityLink): Project? {
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
    }

    companion object {
        const val tag_ext_file = "external_file"
        const val tag_items = "tag_items"
    }

}

