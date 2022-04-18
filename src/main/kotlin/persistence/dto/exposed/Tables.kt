package persistence.dto.exposed

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object Tables {


    object Items : UUIDTable() {
        val name = text(name = "name")
        val type = reference(name = "type", foreign = ObjectTypes).nullable()
    }


    object Units : UUIDTable() {
        val unit = text(name = "unit")
        val isMultipliable = bool(name = "isMultipliable")
    }

    object ItemIncomes : UUIDTable() {
        val item = reference(name = "item", foreign = Items, onDelete = ReferenceOption.CASCADE)
        val count = long(name = "count").nullable()
        val container = reference(name = "container", foreign = Containers).nullable()
        val dateTime = datetime(name = "dateTime").nullable()
        val supplier = reference(name = "supplier", foreign = Suppliers).nullable()
    }

    object ItemOutcomes : UUIDTable() {
        val item = reference(name = "item", foreign = Items, onDelete = ReferenceOption.CASCADE)
        val count = long(name = "count").nullable()
        val container = reference(name = "container", foreign = Containers).nullable()
        val dateTime = datetime(name = "dateTime").nullable()
    }

    object Containers : UUIDTable() {
        val name = text(name = "name")
        val description = text(name = "description")
    }


    object Suppliers : UUIDTable() {
        val name = text(name = "name")
        val description = text(name = "description")
        val url = text(name = "url")
        val isFavorite = bool(name = "isFavorite")
    }

    object ObjectTypes : UUIDTable() {
        val name = text(name = "name")
    }

    object Parameters : UUIDTable() {
        val name = text(name = "name")
        val description = text(name = "description")
        val unit = reference(name = "unit", foreign = Units).nullable()
    }

    object Projects : UUIDTable() {
        val name = text(name = "name")
        val description = text(name = "description")
    }

    //Additional tables for relations:


    /**
     * Table for parent-child references
     * https://github.com/JetBrains/Exposed/wiki/DAO#parent-child-reference
     */
    object ContainerToContainers : Table() {
        val parent = reference(name = "parent", foreign = Containers, onDelete = ReferenceOption.CASCADE)
        val child = reference(name = "child", foreign = Containers, onDelete = ReferenceOption.CASCADE)
    }

    object ObjectTypeToObjectTypes : Table() {
        val parent = reference(name = "parent", foreign = ObjectTypes, onDelete = ReferenceOption.CASCADE)
        val child = reference(name = "child", foreign = ObjectTypes, onDelete = ReferenceOption.CASCADE)
    }

    object ParametersToObjectType : Table() {
        val parameter = reference(name = "parameter", foreign = Parameters, onDelete = ReferenceOption.CASCADE)
        val objectType = reference(name = "type", foreign = ObjectTypes, onDelete = ReferenceOption.CASCADE)
        override val primaryKey: PrimaryKey = PrimaryKey(parameter, objectType)
    }


    object ProjectItems : IntIdTable() {
        val project = reference(name = "project", foreign = Projects, onDelete = ReferenceOption.CASCADE)
        val item = reference(name = "item", foreign = Items, onDelete = ReferenceOption.CASCADE)
        val count = long("count")
    }

    object ItemValues : IntIdTable() {
        val item = reference(name = "item", foreign = Items, onDelete = ReferenceOption.CASCADE)
        val parameter = reference(name = "parameter", foreign = Parameters, onDelete = ReferenceOption.CASCADE)
        val value = text(name = "value").nullable()
        val factor = integer(name = "factor").nullable()
        val position = integer(name = "position")
    }


}
